-- Create database
CREATE DATABASE IF NOT EXISTS todo_db;
USE todo_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(100),
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create tasks table
CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_description VARCHAR(500) NOT NULL,
    priority ENUM('URGENT', 'NORMAL', 'LOW') NOT NULL,
    status ENUM('NOT_STARTED', 'IN_PROGRESS', 'DONE', 'MISSED_DEADLINE') NOT NULL,
    deadline DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_deadline (deadline),
    INDEX idx_priority (priority),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Sample data for testing
INSERT INTO users (email, name, password) VALUES
('john@example.com', 'John Doe', '$2a$10$slYQmyNdGzin7olVN3p5be7DlH.PKZbv5H8KnzzVgXXbVxzy2QIDM'),
('jane@example.com', 'Jane Smith', '$2a$10$slYQmyNdGzin7olVN3p5be7DlH.PKZbv5H8KnzzVgXXbVxzy2QIDM');

-- Sample tasks (password for both users: password123)
INSERT INTO tasks (task_description, priority, status, deadline, user_id) VALUES
('Complete project proposal', 'URGENT', 'IN_PROGRESS', '2025-01-15 17:00:00', 1),
('Review team feedback', 'NORMAL', 'NOT_STARTED', '2025-01-20 10:00:00', 1),
('Update documentation', 'LOW', 'DONE', '2025-01-10 18:00:00', 1),
('Schedule meeting with client', 'URGENT', 'NOT_STARTED', '2025-01-12 14:00:00', 2),
('Fix bug in login', 'URGENT', 'IN_PROGRESS', '2025-01-14 16:00:00', 2);
