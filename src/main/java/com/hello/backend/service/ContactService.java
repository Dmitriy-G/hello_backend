package com.hello.backend.service;

import com.hello.backend.model.Contact;
import com.hello.backend.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final EntityManager entityManager;

    @Autowired
    public ContactService(ContactRepository contactRepository, EntityManager entityManager) {
        this.contactRepository = contactRepository;
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public List<Contact> getContactsByRegex(String regex) {
        Pattern pattern;
        try {
            pattern = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            // если регулярное выражение не валидно, нужно бросить bad request
            // если не ловить ошибку, сервер вернет код 500
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getDescription(), e);
        }
        try (Stream<Contact> pages = contactRepository.streamAllPaged()){
            return pages.peek(entityManager::detach).filter( e -> !pattern.matcher(e.getName()).matches()).collect(Collectors.toList());
        }
    }
}
