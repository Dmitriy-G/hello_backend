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
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getDescription(), e);
        }
        Page<Contact> pages = contactRepository.findAll(pageable);
        return pages.get().filter( e -> !e.getName().matches(regex)).collect(Collectors.toList());
    }
}
