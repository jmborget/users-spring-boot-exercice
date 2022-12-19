-- Tabla users
CREATE SEQUENCE sq_users;

CREATE TABLE users (	
    id bigint NOT NULL,
    name varchar(50) NOT NULL,
    surname1 varchar(50),
    surname2 varchar(50),
    email varchar(255),
    phone_number varchar(15),
    nif varchar(11) NOT NULL,
    nickname varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    status varchar(255),
    entry_date timestamp without time zone NOT NULL,
    cancel_date timestamp without time zone,
    modified_date timestamp without time zone
);

ALTER TABLE users
    ADD CONSTRAINT users_id_pk PRIMARY KEY (id);