CREATE TABLE IF NOT EXISTS status
(
    status_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name      VARCHAR NOT NULL
);


CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name    VARCHAR(255)                            NOT NULL,
    email   VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests
(
    request_id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description  VARCHAR                                 NOT NULL,
    requestor_id BIGINT                                  NOT NULL,
    FOREIGN KEY (requestor_id) REFERENCES users (user_id) ON DELETE CASCADE,
    created      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_request PRIMARY KEY (request_id)
);


CREATE TABLE IF NOT EXISTS items
(
    item_id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name         VARCHAR                                 NOT NULL,
    description  VARCHAR                                 NOT NULL,
    is_available BOOLEAN                                 NOT NULL,
    owner_id     BIGINT                                  NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users (user_id) ON DELETE CASCADE,
    request_id   BIGINT,
    FOREIGN KEY (request_id) REFERENCES requests (request_id) ON DELETE CASCADE,
    CONSTRAINT pk_item PRIMARY KEY (item_id)
);


CREATE TABLE IF NOT EXISTS bookings
(
    booking_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    item_id    BIGINT                                  NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items (item_id) ON DELETE CASCADE,
    booker_id  BIGINT                                  NOT NULL,
    FOREIGN KEY (booker_id) REFERENCES users (user_id) ON DELETE CASCADE,
    status_id  INTEGER,
    FOREIGN KEY (status_id) REFERENCES status (status_id) ON DELETE CASCADE,
    FOREIGN KEY (booker_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT pk_booking PRIMARY KEY (booking_id)
);


CREATE TABLE IF NOT EXISTS comments
(
    comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text       VARCHAR                                 NOT NULL,
    item_id    BIGINT                                  NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items (item_id) ON DELETE CASCADE,
    author_id  BIGINT                                  NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT pk_comment PRIMARY KEY (comment_id),
    created    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS responses
(
    response_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_id     BIGINT                                  NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items (item_id) ON DELETE CASCADE,
    owner_id    BIGINT                                  NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users (user_id) ON DELETE CASCADE,
    request_id  BIGINT                                  NOT NULL,
    FOREIGN KEY (request_id) REFERENCES requests (request_id) ON DELETE CASCADE,

    CONSTRAINT pk_response PRIMARY KEY (response_id)
);



