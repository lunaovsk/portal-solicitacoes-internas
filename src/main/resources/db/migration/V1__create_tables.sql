CREATE TABLE tb_user (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    expire_at DATETIME NULL,
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',

    CONSTRAINT pk_tb_user PRIMARY KEY (id)
) ENGINE=InnoDB;


CREATE TABLE tb_request (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(14) NOT NULL,
    status ENUM('OPEN', 'IN_PROGRESS', 'COMPLETED') NOT NULL DEFAULT 'OPEN',
    created_at DATE NOT NULL,
    user_id INT UNSIGNED NOT NULL,

    CONSTRAINT pk_tb_request PRIMARY KEY (id),
    CONSTRAINT fk_request_user
        FOREIGN KEY (user_id) REFERENCES tb_user (id),
    INDEX idx_request_status (status),
    INDEX idx_request_category (category),
    INDEX idx_request_created_at (created_at)
) ENGINE=InnoDB;


CREATE TABLE tb_auth (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    token_jwt TEXT NOT NULL,
    expires_at DATETIME NOT NULL,

    CONSTRAINT pk_tb_auth PRIMARY KEY (id),
    INDEX idx_auth_expires_at (expires_at),
    INDEX idx_auth_token_prefix (token_jwt(191))
) ENGINE=InnoDB;