package com.hello.backend.repository;

import com.hello.backend.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.stream.Stream;

import static org.hibernate.annotations.QueryHints.READ_ONLY;
import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;
import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-streaming
    @QueryHints(value = {
            @QueryHint(name = HINT_FETCH_SIZE, value = "1000"),
            @QueryHint(name = HINT_CACHEABLE, value = "false"),
            @QueryHint(name = READ_ONLY, value = "true")
    })
    @Query("select c from Contact c")
    Stream<Contact> streamAllPaged();
}
