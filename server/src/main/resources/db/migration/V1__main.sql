create table organisation
(
    id                text        not null PRIMARY KEY,
    display_name      TEXT        not null,
    created_date      TIMESTAMPTZ not null,
    last_updated_date TIMESTAMPTZ
);

CREATE TABLE user_detail
(
--     id                text        not null PRIMARY KEY default gen_random_uuid(),
    id                INT         not null PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    organisation_id   text        not null,
    display_name      TEXT        not null,
    created_date      TIMESTAMPTZ not null,
    last_updated_date TIMESTAMPTZ,

    CONSTRAINT organisationId_fk FOREIGN KEY (organisation_id) REFERENCES organisation (id) on delete cascade
);

CREATE TABLE user_authentication
(
    id                INT         not null PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id           int         not null,
    login_type        TEXT        not null,
    authentication_id TEXT        not null,
    created_date      TIMESTAMPTZ not null,
    last_updated_date TIMESTAMPTZ,

    CONSTRAINT userId_fk FOREIGN KEY (user_id) REFERENCES user_detail (ID) on delete cascade
);

create table parameters
(
    id                INT         not null PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    organisation_id   text        not null,
    user_id           int,
    parameter_name    TEXT        not null,
    value             TEXT        not null,
    created_date      TIMESTAMPTZ not null,
    last_updated_date TIMESTAMPTZ,

    CONSTRAINT organisationId_fk FOREIGN KEY (organisation_id) REFERENCES organisation (id) on delete cascade,
    CONSTRAINT userId_fk FOREIGN KEY (user_id) REFERENCES user_detail (ID) on delete cascade
);