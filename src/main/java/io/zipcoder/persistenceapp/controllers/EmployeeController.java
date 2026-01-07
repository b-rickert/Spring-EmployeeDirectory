package io.zipcoder.persistenceapp.controllers;

import io.zipcoder.persistenceapp.entities.Employee;
import io.zipcoder.persistenceapp.services.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/API/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Create employee
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    // Get all employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // Get employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    // Delete multiple employees
    @DeleteMapping
    public ResponseEntity<Void> deleteEmployees(@RequestBody List<Long> employeeIds) {
        employeeService.deleteEmployees(employeeIds);
        return ResponseEntity.noContent().build();
    }

    // Update employee's manager
    @PutMapping("/{employeeId}/manager/{managerId}")
    public Employee updateEmployeeManager(@PathVariable Long employeeId, @PathVariable Long managerId) {
        return employeeService.updateEmployeeManager(employeeId, managerId);
    }

    // Update employee fields
    @PutMapping("/{employeeId}")
    public Employee updateEmployee(@PathVariable Long employeeId, @RequestBody Employee updatedFields) {
        return employeeService.updateEmployee(employeeId, updatedFields);
    }

    // Set employee's department
    @PutMapping("/{employeeId}/department/{departmentId}")
    public Employee setEmployeeDepartment(@PathVariable Long employeeId, @PathVariable Long departmentId) {
        return employeeService.setEmployeeDepartment(employeeId, departmentId);
    }

    // Get employees with no manager
    @GetMapping("/no-manager")
    public List<Employee> getEmployeesWithNoManager() {
        return employeeService.getEmployeesWithNoManager();
    }

    // Get direct reports of a manager
    @GetMapping("/{managerId}/direct-reports")
    public List<Employee> getDirectReports(@PathVariable Long managerId) {
        return employeeService.getDirectReports(managerId);
    }

    // Get reporting hierarchy (manager chain)
    @GetMapping("/{employeeId}/hierarchy")
    public List<Employee> getReportingHierarchy(@PathVariable Long employeeId) {
        return employeeService.getReportingHierarchy(employeeId);
    }

    // Get all employees in a department
    @GetMapping("/department/{departmentId}")
    public List<Employee> getEmployeesByDepartment(@PathVariable Long departmentId) {
        return employeeService.getEmployeesByDepartment(departmentId);
    }

    // Get all direct and indirect reports
    @GetMapping("/{managerId}/all-reports")
    public List<Employee> getAllReports(@PathVariable Long managerId) {
        return employeeService.getAllReports(managerId);
    }

    // Remove all employees from a department
    @DeleteMapping("/department/{departmentId}")
    public ResponseEntity<Void> removeEmployeesFromDepartment(@PathVariable Long departmentId) {
        employeeService.removeEmployeesFromDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }

    // Remove all reports under a manager
    @DeleteMapping("/{managerId}/all-reports")
    public ResponseEntity<Void> removeAllReports(@PathVariable Long managerId) {
        employeeService.removeAllReports(managerId);
        return ResponseEntity.noContent().build();
    }

    // Remove direct reports and reassign their reports up the chain
    @DeleteMapping("/{managerId}/direct-reports")
    public ResponseEntity<Void> removeDirectReportsAndReassign(@PathVariable Long managerId) {
        employeeService.removeDirectReportsAndReassign(managerId);
        return ResponseEntity.noContent().build();
    }
}