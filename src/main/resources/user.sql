CREATE TABLE IF NOT EXISTS users
(
    uuid       BINARY(16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID())),
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,

    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL
);