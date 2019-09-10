[![Build Status](https://travis-ci.com/Dmitriy-G/hello_backend.svg?branch=master)](https://travis-ci.com/Dmitriy-G/hello_backend)

Должна быть создана база postgres (с таблицей contacts и записями в ней, я не делал миграции, не создавал и не заполнял contacts, как я понял сервис должен работать с уже сущесвующими данными)
1. Название базы - contactsdb
2. Пользователь - testuser
3. Пароль - testuser

Запуск без Docker:

1. mvn clean install
2. mvn spring-boot:run (или java -jar hello_backend-0.0.1-SNAPSHOT.jar)
