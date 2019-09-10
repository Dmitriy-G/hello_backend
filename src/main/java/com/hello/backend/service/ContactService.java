package com.hello.backend.service;

import com.hello.backend.model.Contact;
import com.hello.backend.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Transactional(readOnly = true)
    public List<Contact> getContactsByRegex(String regex, Pageable pageable) {
        try {
            Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            // если регулярное выражение не валидно, нужно бросить bad request
            // если не ловить ошибку, сервер вернет код 500
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getDescription(), e);
        }
        // получаем записи,из базы выберутся только те, которые нужны
        // т.е. hibernate будет использовать offset limit
        Page<Contact> pages = contactRepository.findAll(pageable);
        // полученные данные фильтруются на совпадение имени с регулярным выражением
        // pageable на мой взляд оптимален для этой задачи, с миллионами записей
        // в задаче есть условие не использовать явно sql для фильтрации, при текущем подходе без sql здесь будет баг
        // фильтруются уже полученные данные, т.е. страница
        // на запрос может вернутся страница с произвольным количеством записей или вообще пустая
        // при этом подходящие записи могут быть в базе
        // и возвращать не до конца заполненную страницу при наличии подходящих данных это плохое решение, которое может запутать пользователя сервиса
        // нужно фильтровать записи во время запроса страницы, а в задаче как я понял требуется сначала получить их, а потом фильтровать
        // при таком подходе нужно придумывать некий метод, который будет дозаполнять страницу новыми записями
        return pages.get().filter( e -> !e.getName().matches(regex)).collect(Collectors.toList());
    }
}
