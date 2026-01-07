package io.zipcoder.persistenceapp.services;

import io.zipcoder.persistenceapp.entities.Department;
import io.zipcoder.persistenceapp.entities.Employee;
import io.zipcoder.persistenceapp.repositories.DepartmentRepository;
import io.zipcoder.persistenceapp.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    // Basic CRUD
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    // Set manager
    public Employee updateEmployeeManager(Long employeeId, Long managerId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        employee.setManager(manager);
        // Also set employee's department to match manager's department
        employee.setDepartment(manager.getDepartment());
        return employeeRepository.save(employee);
    }

    // Update employee fields
    public Employee updateEmployee(Long employeeId, Employee updatedFields) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (updatedFields.getFirstName() != null) employee.setFirstName(updatedFields.getFirstName());
        if (updatedFields.getLastName() != null) employee.setLastName(updatedFields.getLastName());
        if (updatedFields.getTitle() != null) employee.setTitle(updatedFields.getTitle());
        if (updatedFields.getPhoneNumber() != null) employee.setPhoneNumber(updatedFields.getPhoneNumber());
        if (updatedFields.getEmail() != null) employee.setEmail(updatedFields.getEmail());
        if (updatedFields.getHireDate() != null) employee.setHireDate(updatedFields.getHireDate());

        return employeeRepository.save(employee);
    }

    // Set employee's department
    public Employee setEmployeeDepartment(Long employeeId, Long departmentId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        employee.setDepartment(department);
        return employeeRepository.save(employee);
    }

    // Get employees with no manager
    public List<Employee> getEmployeesWithNoManager() {
        return employeeRepository.findByManagerIsNull();
    }

    // Get direct reports of a manager
    public List<Employee> getDirectReports(Long managerId) {
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        return employeeRepository.findByManager(manager);
    }

    // Get entire reporting hierarchy (manager's manager's manager, etc.)
    public List<Employee> getReportingHierarchy(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<Employee> hierarchy = new ArrayList<>();
        Employee current = employee.getManager();

        while (current != null) {
            hierarchy.add(current);
            current = current.getManager();
        }

        return hierarchy;
    }

    // Get all employees in a department
    public List<Employee> getEmployeesByDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        return employeeRepository.findByDepartment(department);
    }

    // Get all direct AND indirect reports (recursive)
    public List<Employee> getAllReports(Long managerId) {
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        List<Employee> allReports = new ArrayList<>();
        collectAllReports(manager, allReports);
        return allReports;
    }

    private void collectAllReports(Employee manager, List<Employee> allReports) {
        List<Employee> directReports = employeeRepository.findByManager(manager);
        for (Employee report : directReports) {
            allReports.add(report);
            collectAllReports(report, allReports); // Recursively get their reports
        }
    }

    // Remove all employees from a department
    public void removeEmployeesFromDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        List<Employee> employees = employeeRepository.findByDepartment(department);
        employeeRepository.deleteAll(employees);
    }

    // Remove all employees under a manager (including indirect reports)
    public void removeAllReports(Long managerId) {
        List<Employee> allReports = getAllReports(managerId);
        employeeRepository.deleteAll(allReports);
    }

    // Remove direct reports, reassign their reports to the manager above
    public void removeDirectReportsAndReassign(Long managerId) {
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        List<Employee> directReports = employeeRepository.findByManager(manager);

        for (Employee directReport : directReports) {
            // Reassign this person's reports to the manager above
            List<Employee> theirReports = employeeRepository.findByManager(directReport);
            for (Employee report : theirReports) {
                report.setManager(manager);
                employeeRepository.save(report);
            }
            // Now delete the direct report
            employeeRepository.delete(directReport);
        }
    }

    // Delete multiple employees by ID
    public void deleteEmployees(List<Long> employeeIds) {
        employeeRepository.deleteAllById(employeeIds);
    }
}