CREATE TABLE IF NOT EXISTS schedules
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,

    task       VARCHAR(255) NOT NULL,
    author     BINARY(16)   NOT NULL,
    password   VARCHAR(255) NOT NULL,

    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL,

    FOREIGN KEY (author) REFERENCES users (uuid)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);