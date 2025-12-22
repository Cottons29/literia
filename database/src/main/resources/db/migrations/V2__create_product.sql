CREATE TABLE role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE paper (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    abstract TEXT,
    file_path VARCHAR(255) NOT NULL,
    status ENUM('SUBMITTED','UNDER_REVIEW','ACCEPTED','REJECTED') DEFAULT 'SUBMITTED',
    author_id BIGINT NOT NULL,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_paper_author FOREIGN KEY (author_id) REFERENCES user(id)
);

CREATE TABLE review (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    paper_id BIGINT NOT NULL,
    reviewer_id BIGINT NOT NULL,
    comments TEXT,
    score INT CHECK (score BETWEEN 1 AND 10),
    status ENUM('PENDING','COMPLETED') DEFAULT 'PENDING',
    reviewed_at TIMESTAMP NULL,
    CONSTRAINT fk_review_paper FOREIGN KEY (paper_id) REFERENCES paper(id),
    CONSTRAINT fk_review_reviewer FOREIGN KEY (reviewer_id) REFERENCES user(id)
);

CREATE TABLE assignment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    paper_id BIGINT NOT NULL,
    reviewer_id BIGINT NOT NULL,
    assigned_by_admin_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_assignment_paper FOREIGN KEY (paper_id) REFERENCES paper(id),
    CONSTRAINT fk_assignment_reviewer FOREIGN KEY (reviewer_id) REFERENCES user(id),
    CONSTRAINT fk_assignment_admin FOREIGN KEY (assigned_by_admin_id) REFERENCES user(id)
);

CREATE TABLE audit_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    action VARCHAR(100) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_user FOREIGN KEY (user_id) REFERENCES user(id)
);
