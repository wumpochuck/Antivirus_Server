```sql
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       login VARCHAR(255) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       email VARCHAR(255),
                       role VARCHAR(255),
                       CONSTRAINT unique_login UNIQUE (login)
);

CREATE TABLE products (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255),
                          is_blocked BOOLEAN
);

CREATE TABLE license_types (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               name VARCHAR(255),
                               default_duration INT,
                               description TEXT
);

CREATE TABLE licenses (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          code VARCHAR(255),
                          user_id BIGINT,
                          product_id BIGINT,
                          type_id BIGINT,
                          first_activation_date DATE,
                          ending_date DATE,
                          is_blocked BOOLEAN,
                          devices_count INT,
                          owner_id BIGINT,
                          duration INT,
                          description TEXT,
                          FOREIGN KEY (user_id) REFERENCES users(id),
                          FOREIGN KEY (product_id) REFERENCES products(id),
                          FOREIGN KEY (type_id) REFERENCES license_types(id),
                          FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE devices (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255),
                         mac_address VARCHAR(255),
                         user_id BIGINT,
                         FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE device_licenses (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 license_id BIGINT,
                                 device_id BIGINT,
                                 activation_date DATE,
                                 FOREIGN KEY (license_id) REFERENCES licenses(id),
                                 FOREIGN KEY (device_id) REFERENCES devices(id)
);

CREATE TABLE licence_history (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 license_id BIGINT,
                                 user_id BIGINT,
                                 status VARCHAR(255),
                                 change_date DATE,
                                 description TEXT,
                                 FOREIGN KEY (license_id) REFERENCES licenses(id),
                                 FOREIGN KEY (user_id) REFERENCES users(id)
);
```