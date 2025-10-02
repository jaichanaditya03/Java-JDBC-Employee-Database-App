package com.employee;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Employee Model Class (POJO - Plain Old Java Object)
 * Represents an employee entity with all required fields
 */
public class Employee {
    
    // Private fields
    private int id;
    private String name;
    private String email;
    private String department;
    private BigDecimal salary;
    private Timestamp createdAt;
    
    // Default constructor
    public Employee() {
    }
    
    // Constructor without ID (for new employees)
    public Employee(String name, String email, String department, BigDecimal salary) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.salary = salary;
    }
    
    // Constructor with ID (for existing employees)
    public Employee(int id, String name, String email, String department, BigDecimal salary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.salary = salary;
    }
    
    // Complete constructor with all fields
    public Employee(int id, String name, String email, String department, BigDecimal salary, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.salary = salary;
        this.createdAt = createdAt;
    }
    
    // Getter and Setter methods
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public BigDecimal getSalary() {
        return salary;
    }
    
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    // toString method for easy display
    @Override
    public String toString() {
        return String.format(
            "Employee{id=%d, name='%s', email='%s', department='%s', salary=%.2f, createdAt=%s}",
            id, name, email, department, salary, createdAt
        );
    }
    
    // Formatted display method for user-friendly output
    public String toDisplayString() {
        return String.format(
            "ID: %-3d | Name: %-20s | Email: %-25s | Department: %-10s | Salary: $%,.2f",
            id, name, email, department, salary
        );
    }
    
    // equals method for comparing employees
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return id == employee.id;
    }
    
    // hashCode method
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}