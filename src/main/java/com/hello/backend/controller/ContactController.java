package com.hello.backend.controller;

import com.hello.backend.model.Contact;
import com.hello.backend.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(value = "/hello", produces = APPLICATION_JSON_UTF8_VALUE)
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // контроллер дополнительно может принимать параметры page(номер страницы) и size(размер страницы), если они не будут заданы, подставится дефолтное значение
    // я сделал возможность для пользователя самому настраивать пагинацию
    // другой вариант, это создать в коде объект Pageable с фиксированными параметрами
    @GetMapping(value = "/contacts")
    public List<Contact> getContactsByRegex(@RequestParam(value = "nameFilter") String regex, Pageable pageable) {
        return contactService.getContactsByRegex(regex, pageable);
    }
}
