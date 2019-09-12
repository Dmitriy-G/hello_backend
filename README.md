[![Build Status](https://travis-ci.com/Dmitriy-G/hello_backend.svg?branch=master)](https://travis-ci.com/Dmitriy-G/hello_backend)

Должна быть создана база postgres (с таблицей contacts и записями в ней)
1. Название базы - contactsdb
2. Пользователь - testuser
3. Пароль - testuser

Для тестов используется EmbeddedPostgres

Запуск без Docker:

1. mvn clean install
2. mvn spring-boot:run (или java -jar hello_backend-0.0.1-SNAPSHOT.jar)

Доступные url:
/hello/contacts - принимает get запрос с параметром nameFilter - регулярное выражение. Так же есть возможность настраивать пагинацию через дополнительные необязательные параметры:
                  size - размер страницы
                  page - номер страницы

