-- Employee Database Setup Script
-- Run this script to set up the database and initial data

-- Create database
CREATE DATABASE IF NOT EXISTS employee_db;
USE employee_db;

-- Create employees table
CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    department VARCHAR(50) NOT NULL,
    salary DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample data
INSERT INTO employees (name, email, department, salary) VALUES
('John Doe', 'john.doe@company.com', 'IT', 75000.00),
('Jane Smith', 'jane.smith@company.com', 'HR', 65000.00),
('Mike Johnson', 'mike.johnson@company.com', 'Finance', 80000.00);

-- Verify setup
SELECT 'Database setup completed successfully!' as Status;
SELECT COUNT(*) as 'Total Employees' FROM employees;