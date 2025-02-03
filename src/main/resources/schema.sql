CREATE TABLE category (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description VARCHAR(255)
);

CREATE TABLE products (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT NOT NULL,
                          price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
                          quantity INT NOT NULL CHECK (quantity >= 0),
                          version INT DEFAULT 0 NOT NULL,
                          category_id BIGINT,
                          FOREIGN KEY (category_id) REFERENCES category(id)
);