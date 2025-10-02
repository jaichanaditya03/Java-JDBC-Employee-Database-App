package com.employee;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Employee Data Access Object (DAO)
 * Handles all database operations for Employee entity using JDBC
 */
public class EmployeeDAO {
    
    // SQL Queries as constants
    private static final String INSERT_EMPLOYEE = 
        "INSERT INTO employees (name, email, department, salary) VALUES (?, ?, ?, ?)";
    
    private static final String SELECT_ALL_EMPLOYEES = 
        "SELECT id, name, email, department, salary, created_at FROM employees ORDER BY id";
    
    private static final String SELECT_EMPLOYEE_BY_ID = 
        "SELECT id, name, email, department, salary, created_at FROM employees WHERE id = ?";
    
    private static final String UPDATE_EMPLOYEE = 
        "UPDATE employees SET name = ?, email = ?, department = ?, salary = ? WHERE id = ?";
    
    private static final String DELETE_EMPLOYEE = 
        "DELETE FROM employees WHERE id = ?";
    
    private static final String SELECT_EMPLOYEES_BY_DEPARTMENT = 
        "SELECT id, name, email, department, salary, created_at FROM employees WHERE department = ? ORDER BY id";
    
    private static final String COUNT_EMPLOYEES = 
        "SELECT COUNT(*) FROM employees";
    
    /**
     * Create a new employee in the database
     * @param employee Employee object to insert
     * @return true if successful, false otherwise
     */
    public boolean createEmployee(Employee employee) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_EMPLOYEE, Statement.RETURN_GENERATED_KEYS)) {
            
            // Set parameters
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getEmail());
            statement.setString(3, employee.getDepartment());
            statement.setBigDecimal(4, employee.getSalary());
            
            // Execute the insert
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get the generated ID
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        employee.setId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("✅ Employee created successfully with ID: " + employee.getId());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error creating employee: " + e.getMessage());
            if (e.getErrorCode() == 1062) { // Duplicate entry error code for MySQL
                System.err.println("   Reason: Email already exists!");
            }
        }
        return false;
    }
    
    /**
     * Retrieve all employees from the database
     * @return List of all employees
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_EMPLOYEES);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                Employee employee = mapResultSetToEmployee(resultSet);
                employees.add(employee);
            }
            
            System.out.println("📋 Retrieved " + employees.size() + " employees from database");
            
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving employees: " + e.getMessage());
        }
        
        return employees;
    }
    
    /**
     * Retrieve a specific employee by ID
     * @param id Employee ID
     * @return Employee object or null if not found
     */
    public Employee getEmployeeById(int id) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_EMPLOYEE_BY_ID)) {
            
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Employee employee = mapResultSetToEmployee(resultSet);
                    System.out.println("🔍 Found employee: " + employee.getName());
                    return employee;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving employee by ID: " + e.getMessage());
        }
        
        System.out.println("❌ Employee with ID " + id + " not found");
        return null;
    }
    
    /**
     * Update an existing employee
     * @param employee Employee object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateEmployee(Employee employee) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_EMPLOYEE)) {
            
            // Set parameters
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getEmail());
            statement.setString(3, employee.getDepartment());
            statement.setBigDecimal(4, employee.getSalary());
            statement.setInt(5, employee.getId());
            
            // Execute the update
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✅ Employee updated successfully: " + employee.getName());
                return true;
            } else {
                System.out.println("❌ No employee found with ID: " + employee.getId());
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error updating employee: " + e.getMessage());
            if (e.getErrorCode() == 1062) {
                System.err.println("   Reason: Email already exists!");
            }
        }
        return false;
    }
    
    /**
     * Delete an employee by ID
     * @param id Employee ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteEmployee(int id) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_EMPLOYEE)) {
            
            statement.setInt(1, id);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✅ Employee deleted successfully (ID: " + id + ")");
                return true;
            } else {
                System.out.println("❌ No employee found with ID: " + id);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error deleting employee: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get employees by department
     * @param department Department name
     * @return List of employees in the department
     */
    public List<Employee> getEmployeesByDepartment(String department) {
        List<Employee> employees = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_EMPLOYEES_BY_DEPARTMENT)) {
            
            statement.setString(1, department);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Employee employee = mapResultSetToEmployee(resultSet);
                    employees.add(employee);
                }
            }
            
            System.out.println("🏢 Found " + employees.size() + " employees in " + department + " department");
            
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving employees by department: " + e.getMessage());
        }
        
        return employees;
    }
    
    /**
     * Get total count of employees
     * @return Total number of employees
     */
    public int getEmployeeCount() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_EMPLOYEES);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                System.out.println("📊 Total employees in database: " + count);
                return count;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error counting employees: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Helper method to map ResultSet to Employee object
     * @param resultSet ResultSet from database query
     * @return Employee object
     * @throws SQLException if any SQL error occurs
     */
    private Employee mapResultSetToEmployee(ResultSet resultSet) throws SQLException {
        return new Employee(
            resultSet.getInt("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("department"),
            resultSet.getBigDecimal("salary"),
            resultSet.getTimestamp("created_at")
        );
    }
    
    /**
     * Check if an employee exists by email
     * @param email Email to check
     * @return true if employee exists, false otherwise
     */
    public boolean employeeExistsByEmail(String email) {
        String query = "SELECT COUNT(*) FROM employees WHERE email = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, email);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error checking employee existence: " + e.getMessage());
        }
        
        return false;
    }
}