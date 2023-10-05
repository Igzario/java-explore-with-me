CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name    VARCHAR(255)                                        NOT NULL,
    email   VARCHAR(255)                                        NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name        VARCHAR(255)                                        NOT NULL,
    CONSTRAINT UQ_CAT UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS compilations
(
    compilation_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    pinned         BOOLEAN,
    title          VARCHAR(255),
    CONSTRAINT uq_compilation_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS events
(
    event_id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    annotation         VARCHAR(7500),
    category_id        BIGINT,
    confirmed_requests BIGINT,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    description        VARCHAR(7500),
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    initiator_id       BIGINT,
    paid               BOOLEAN,
    participant_limit  BIGINT,
    published_On       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state              VARCHAR(255),
    title              VARCHAR(255),
    views              BIGINT,
    lat                BIGINT,
    lon                BIGINT,
    CONSTRAINT fk_user FOREIGN KEY (initiator_id) REFERENCES users (user_id),
    CONSTRAINT fk_categories FOREIGN KEY (category_id) REFERENCES categories (category_id)
);

CREATE TABLE IF NOT EXISTS comp_events
(
    comp_id  BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    CONSTRAINT fk_comp FOREIGN KEY (comp_id) REFERENCES compilations (compilation_id),
    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events (event_id)
);

CREATE TABLE IF NOT EXISTS requests
(
    request_id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE,
    event_id     BIGINT,
    requester_id BIGINT,
    status       VARCHAR(20),
    CONSTRAINT fk_requester FOREIGN KEY (requester_id) REFERENCES users (user_id),
    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events (event_id)
);

CREATE TABLE IF NOT EXISTS comments
(
    comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text       VARCHAR(7000) NOT NULL,
    author_id  BIGINT,
    event_id   BIGINT,
    created_date TIMESTAMP NOT NULL,
    edited_date  TIMESTAMP,
    CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES users (user_id),
    CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES events (event_id)
);