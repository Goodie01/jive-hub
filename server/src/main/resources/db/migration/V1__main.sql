create table organisation
(
    id                INT         not null PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    display_name      TEXT        not null,
    created_date      TIMESTAMPTZ not null,
    last_updated_date TIMESTAMPTZ
);

ALTER SEQUENCE organisation_id_seq RESTART 1000;

CREATE TABLE user_detail
(
    id                INT         not null PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    organisation_id   int         not null references organisation (id) on update cascade on delete cascade,
    email             text        not null,
    name              TEXT        not null,
    preferred_name    TEXT        not null,
    created_date      TIMESTAMPTZ not null,
    last_updated_date TIMESTAMPTZ,

    unique (organisation_id, email)
);

create table user_session
(
    id              INT  not null PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    organisation_id int references organisation (id) on update cascade on delete cascade,
    user_id         int references user_detail (id) on update cascade on delete cascade,
    session_Key     text not null,

    unique (organisation_id, session_Key)
);

create table role
(
    organisation_id   int references organisation (id) on update cascade on delete cascade,
    name              TEXT        not null,
    policy            text        not null,
    created_date      TIMESTAMPTZ not null,
    last_updated_date TIMESTAMPTZ,

    primary key (organisation_id, name)
);

create table user_has_role
(
    organisation_id   int,
    role_name         text,
    user_id           int references user_detail (id) on update cascade on delete cascade,
    created_date      TIMESTAMPTZ not null,
    last_updated_date TIMESTAMPTZ,

    primary key (organisation_id, role_name, user_id),
    FOREIGN KEY (organisation_id, role_name) REFERENCES role (organisation_id, name) on update cascade on delete cascade
);

create table parameters
(
    id                INT         not null PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    organisation_id   int         not null references organisation (id) on update cascade on delete cascade,
    user_id           int references user_detail (id) on update cascade on delete cascade,
    parameter_name    TEXT        not null,
    value             TEXT        not null,
    created_date      TIMESTAMPTZ not null,
    last_updated_date TIMESTAMPTZ,

    UNIQUE (organisation_id, user_id, parameter_name)
);

create table page
(
    path            text not null,
    title           text not null,
    content         text not null,
    organisation_id int references organisation (id) on update cascade on delete cascade,
    menuName        text,
    menuOrder       smallint,

    primary key (organisation_id, path),
    unique (organisation_id, menuName)
);

create table host_names
(
    host            text not null unique,
    organisation_id int references organisation (id) on update cascade on delete cascade
);

create table event
(
    id                INT         not null PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    organisation_id   int references organisation (id) on update cascade on delete cascade,
    title             text        not null,
    by_line           text        not null,
    start_date        TIMESTAMPTZ not null,
    end_date          TIMESTAMPTZ not null,
    created_date      TIMESTAMPTZ not null,
    last_updated_date TIMESTAMPTZ
);