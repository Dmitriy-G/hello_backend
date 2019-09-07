CREATE TABLE contacts
(
    id bigint PRIMARY KEY NOT NULL,
    name varchar(255) NOT NULL
);
CREATE UNIQUE INDEX contacts_id_uindex ON contacts (id);

INSERT INTO contacts ("id", "name") VALUES (1, 'aaa');
INSERT INTO contacts ("id", "name") VALUES (2, 'baa');
INSERT INTO contacts ("id", "name") VALUES (3, 'aab');
INSERT INTO contacts ("id", "name") VALUES (4, 'bbb');
INSERT INTO contacts ("id", "name") VALUES (5, '^^^');
INSERT INTO contacts ("id", "name") VALUES (6, '/?23/a');
INSERT INTO contacts ("id", "name") VALUES (7, '+="a"');