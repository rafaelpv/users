CREATE TABLE user_permission (
    id_user INT NOT NULL REFERENCES users(id),
    id_permission INT NOT NULL REFERENCES permission(id)
);
