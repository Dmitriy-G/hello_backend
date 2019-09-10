package com.hello.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hello.backend.model.Contact;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = ApplicationRunner.class)
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase
@FlywayTest(locationsForMigrate = "test/db/migration")
public class ContactControllerTest extends AbstractTestNGSpringContextTests {

    // несколько тестов для контроллера
    // в качестве data source используется EmbeddedPostgres
    // данные для тестов заполняются в миграции
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private final String URL = "/hello/contacts";
    private final String PARAM = "nameFilter";


    @Test
    public void incorrectRegexTest() throws Exception {
        // при некорректном регулярном выражении должна быть ошибка с нужным кодом
        mockMvc.perform(get(URL))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get(URL).param(PARAM,"[text"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getContactsByRegexTest() throws Exception {
        // по регулярному выражению должно вернутся возвращается нужное количесво контактов
        final List<Contact> firstContacts = getContactsByRegex("^a.*$");

        assertThat(firstContacts).isNotNull();
        assertThat(firstContacts.size()).isEqualTo(5);

        final List<Contact> secondContacts = getContactsByRegex("^.*[aei].*$");

        assertThat(secondContacts).isNotNull();
        assertThat(secondContacts.size()).isEqualTo(2);

        final List<Contact> thirdContacts = getContactsByRegex("^.*[a-z].*$");

        assertThat(thirdContacts).isNotNull();
        assertThat(thirdContacts.size()).isEqualTo(1);
    }

    @Test
    public void getWithSpecialCharactersTest() throws Exception {
        // специальные символы должны обрабатыватся корректно
        final List<Contact> firstContacts = getContactsByRegex("\\^\\^\\^");

        assertThat(firstContacts).isNotNull();
        assertThat(firstContacts.size()).isEqualTo(6);

        final List<Contact> secondContacts = getContactsByRegex("^.*[\\^\\/\"].*$");
        assertThat(secondContacts).isNotNull();
        assertThat(secondContacts.size()).isEqualTo(4);
    }




    private List<Contact>getContactsByRegex(String regex) throws Exception {
        // код, который получает данные от контроллера вынес в отдельный метод
        final MvcResult getContactAction = mockMvc.perform(get(URL).param(PARAM,regex))
                .andExpect(status().isOk())
                .andReturn();
        final String getContactResponse = getContactAction.getResponse().getContentAsString();
        return mapper.readValue(getContactResponse, new TypeReference<List<Contact>>(){});
    }

}
