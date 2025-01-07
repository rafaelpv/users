ALTER TABLE users
    ADD COLUMN user_name VARCHAR(255),
    ADD COLUMN full_name VARCHAR(255),
    ADD COLUMN account_non_expired BOOLEAN DEFAULT TRUE,
    ADD COLUMN account_non_locked BOOLEAN DEFAULT TRUE,
    ADD COLUMN credentials_non_expired BOOLEAN DEFAULT TRUE,
    ADD COLUMN enabled BOOLEAN DEFAULT TRUE;
