package com.employee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;

/**
 * Employee Database Application
 * Main class with interactive console menu for CRUD operations
 */
public class EmployeeApp {
    
    private static final EmployeeDAO employeeDAO = new EmployeeDAO();
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("    🏢 EMPLOYEE DATABASE MANAGEMENT SYSTEM 🏢");
        System.out.println("=================================================");
        
        // Test database connection first
        if (!DatabaseConnection.testConnection()) {
            System.out.println("❌ Failed to connect to database. Please check your configuration.");
            System.out.println("💡 Make sure MySQL is running and check database.properties file");
            return;
        }
        
        boolean running = true;
        
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice (1-8): ");
            
            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    viewAllEmployees();
                    break;
                case 3:
                    searchEmployeeById();
                    break;
                case 4:
                    updateEmployee();
                    break;
                case 5:
                    deleteEmployee();
                    break;
                case 6:
                    searchByDepartment();
                    break;
                case 7:
                    showStatistics();
                    break;
                case 8:
                    running = false;
                    System.out.println("👋 Thank you for using Employee Database System!");
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please enter 1-8.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    /**
     * Display the main menu options
     */
    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("               MAIN MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. ➕ Add New Employee");
        System.out.println("2. 📋 View All Employees");
        System.out.println("3. 🔍 Search Employee by ID");
        System.out.println("4. ✏️  Update Employee");
        System.out.println("5. 🗑️  Delete Employee");
        System.out.println("6. 🏢 Search by Department");
        System.out.println("7. 📊 View Statistics");
        System.out.println("8. 🚪 Exit");
        System.out.println("=".repeat(50));
    }
    
    /**
     * Add a new employee to the database
     */
    private static void addEmployee() {
        System.out.println("\n📝 ADD NEW EMPLOYEE");
        System.out.println("-".repeat(30));
        
        try {
            System.out.print("Enter employee name: ");
            String name = scanner.nextLine().trim();
            
            if (name.isEmpty()) {
                System.out.println("❌ Name cannot be empty!");
                return;
            }
            
            System.out.print("Enter employee email: ");
            String email = scanner.nextLine().trim();
            
            if (email.isEmpty() || !isValidEmail(email)) {
                System.out.println("❌ Please enter a valid email!");
                return;
            }
            
            // Check if email already exists
            if (employeeDAO.employeeExistsByEmail(email)) {
                System.out.println("❌ Employee with this email already exists!");
                return;
            }
            
            System.out.print("Enter department: ");
            String department = scanner.nextLine().trim();
            
            if (department.isEmpty()) {
                System.out.println("❌ Department cannot be empty!");
                return;
            }
            
            BigDecimal salary = getBigDecimalInput("Enter salary: $");
            
            if (salary.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("❌ Salary must be greater than 0!");
                return;
            }
            
            // Create new employee
            Employee employee = new Employee(name, email, department, salary);
            
            if (employeeDAO.createEmployee(employee)) {
                System.out.println("🎉 Employee added successfully!");
                System.out.println("👤 " + employee.toDisplayString());
            } else {
                System.out.println("❌ Failed to add employee!");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error adding employee: " + e.getMessage());
        }
    }
    
    /**
     * View all employees in the database
     */
    private static void viewAllEmployees() {
        System.out.println("\n📋 ALL EMPLOYEES");
        System.out.println("-".repeat(50));
        
        List<Employee> employees = employeeDAO.getAllEmployees();
        
        if (employees.isEmpty()) {
            System.out.println("📭 No employees found in the database.");
        } else {
            System.out.println("Found " + employees.size() + " employee(s):\n");
            System.out.println("-".repeat(100));
            
            for (Employee employee : employees) {
                System.out.println(employee.toDisplayString());
            }
            System.out.println("-".repeat(100));
        }
    }
    
    /**
     * Search for an employee by ID
     */
    private static void searchEmployeeById() {
        System.out.println("\n🔍 SEARCH EMPLOYEE BY ID");
        System.out.println("-".repeat(30));
        
        int id = getIntInput("Enter employee ID: ");
        
        Employee employee = employeeDAO.getEmployeeById(id);
        
        if (employee != null) {
            System.out.println("\n✅ Employee Found:");
            System.out.println("-".repeat(50));
            System.out.println("ID: " + employee.getId());
            System.out.println("Name: " + employee.getName());
            System.out.println("Email: " + employee.getEmail());
            System.out.println("Department: " + employee.getDepartment());
            System.out.println("Salary: $" + String.format("%,.2f", employee.getSalary()));
            System.out.println("Created: " + employee.getCreatedAt());
            System.out.println("-".repeat(50));
        }
    }
    
    /**
     * Update an existing employee
     */
    private static void updateEmployee() {
        System.out.println("\n✏️ UPDATE EMPLOYEE");
        System.out.println("-".repeat(30));
        
        int id = getIntInput("Enter employee ID to update: ");
        
        Employee employee = employeeDAO.getEmployeeById(id);
        
        if (employee == null) {
            return; // Employee not found message already shown
        }
        
        System.out.println("\nCurrent employee details:");
        System.out.println(employee.toDisplayString());
        System.out.println("\nEnter new details (press Enter to keep current value):");
        
        // Update name
        System.out.print("New name [" + employee.getName() + "]: ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            employee.setName(newName);
        }
        
        // Update email
        System.out.print("New email [" + employee.getEmail() + "]: ");
        String newEmail = scanner.nextLine().trim();
        if (!newEmail.isEmpty()) {
            if (!isValidEmail(newEmail)) {
                System.out.println("❌ Invalid email format!");
                return;
            }
            employee.setEmail(newEmail);
        }
        
        // Update department
        System.out.print("New department [" + employee.getDepartment() + "]: ");
        String newDepartment = scanner.nextLine().trim();
        if (!newDepartment.isEmpty()) {
            employee.setDepartment(newDepartment);
        }
        
        // Update salary
        System.out.print("New salary [" + employee.getSalary() + "]: $");
        String salaryInput = scanner.nextLine().trim();
        if (!salaryInput.isEmpty()) {
            try {
                BigDecimal newSalary = new BigDecimal(salaryInput);
                if (newSalary.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("❌ Salary must be greater than 0!");
                    return;
                }
                employee.setSalary(newSalary);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid salary format!");
                return;
            }
        }
        
        if (employeeDAO.updateEmployee(employee)) {
            System.out.println("🎉 Employee updated successfully!");
            System.out.println("👤 " + employee.toDisplayString());
        } else {
            System.out.println("❌ Failed to update employee!");
        }
    }
    
    /**
     * Delete an employee from the database
     */
    private static void deleteEmployee() {
        System.out.println("\n🗑️ DELETE EMPLOYEE");
        System.out.println("-".repeat(30));
        
        int id = getIntInput("Enter employee ID to delete: ");
        
        Employee employee = employeeDAO.getEmployeeById(id);
        
        if (employee == null) {
            return; // Employee not found message already shown
        }
        
        System.out.println("\nEmployee to delete:");
        System.out.println(employee.toDisplayString());
        System.out.print("\nAre you sure you want to delete this employee? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("yes") || confirmation.equals("y")) {
            if (employeeDAO.deleteEmployee(id)) {
                System.out.println("🗑️ Employee deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete employee!");
            }
        } else {
            System.out.println("❌ Deletion cancelled.");
        }
    }
    
    /**
     * Search employees by department
     */
    private static void searchByDepartment() {
        System.out.println("\n🏢 SEARCH BY DEPARTMENT");
        System.out.println("-".repeat(30));
        
        System.out.print("Enter department name: ");
        String department = scanner.nextLine().trim();
        
        if (department.isEmpty()) {
            System.out.println("❌ Department cannot be empty!");
            return;
        }
        
        List<Employee> employees = employeeDAO.getEmployeesByDepartment(department);
        
        if (employees.isEmpty()) {
            System.out.println("📭 No employees found in " + department + " department.");
        } else {
            System.out.println("\n✅ Employees in " + department + " department:");
            System.out.println("-".repeat(100));
            
            for (Employee employee : employees) {
                System.out.println(employee.toDisplayString());
            }
            System.out.println("-".repeat(100));
        }
    }
    
    /**
     * Show database statistics
     */
    private static void showStatistics() {
        System.out.println("\n📊 DATABASE STATISTICS");
        System.out.println("-".repeat(30));
        
        int totalEmployees = employeeDAO.getEmployeeCount();
        System.out.println("👥 Total Employees: " + totalEmployees);
        
        if (totalEmployees > 0) {
            List<Employee> allEmployees = employeeDAO.getAllEmployees();
            
            // Calculate average salary
            BigDecimal totalSalary = BigDecimal.ZERO;
            for (Employee emp : allEmployees) {
                totalSalary = totalSalary.add(emp.getSalary());
            }
            BigDecimal avgSalary = totalSalary.divide(BigDecimal.valueOf(totalEmployees), 2, RoundingMode.HALF_UP);
            
            System.out.println("💰 Average Salary: $" + String.format("%,.2f", avgSalary));
            
            // Find highest and lowest salary
            BigDecimal maxSalary = allEmployees.get(0).getSalary();
            BigDecimal minSalary = allEmployees.get(0).getSalary();
            
            for (Employee emp : allEmployees) {
                if (emp.getSalary().compareTo(maxSalary) > 0) {
                    maxSalary = emp.getSalary();
                }
                if (emp.getSalary().compareTo(minSalary) < 0) {
                    minSalary = emp.getSalary();
                }
            }
            
            System.out.println("📈 Highest Salary: $" + String.format("%,.2f", maxSalary));
            System.out.println("📉 Lowest Salary: $" + String.format("%,.2f", minSalary));
        }
    }
    
    /**
     * Get integer input with validation
     */
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
        }
    }
    
    /**
     * Get BigDecimal input with validation
     */
    private static BigDecimal getBigDecimalInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
        }
    }
    
    /**
     * Simple email validation
     */
    private static boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }
}