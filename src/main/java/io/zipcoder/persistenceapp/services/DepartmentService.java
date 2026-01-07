package io.zipcoder.persistenceapp.services;

import io.zipcoder.persistenceapp.entities.Department;
import io.zipcoder.persistenceapp.entities.Employee;
import io.zipcoder.persistenceapp.repositories.DepartmentRepository;
import io.zipcoder.persistenceapp.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    // Set a new department manager
    public Department setDepartmentManager(Long departmentId, Long employeeId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        department.setDepartmentManager(employee);
        return departmentRepository.save(department);
    }

    // Change department name
    public Department updateDepartmentName(Long departmentId, String newName) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        
        department.setDepartmentName(newName);
        return departmentRepository.save(department);
    }

    // Merge departments: Move manager of B to report to manager of A, update all employees to department A
    public Department mergeDepartments(Long departmentAId, Long departmentBId) {
        Department deptA = departmentRepository.findById(departmentAId)
                .orElseThrow(() -> new RuntimeException("Department A not found"));
        Department deptB = departmentRepository.findById(departmentBId)
                .orElseThrow(() -> new RuntimeException("Department B not found"));

        // Move B's manager to report to A's manager
        if (deptB.getDepartmentManager() != null && deptA.getDepartmentManager() != null) {
            deptB.getDepartmentManager().setManager(deptA.getDepartmentManager());
            employeeRepository.save(deptB.getDepartmentManager());
        }

        // Move all employees from B to A
        List<Employee> deptBEmployees = employeeRepository.findByDepartment(deptB);
        for (Employee emp : deptBEmployees) {
            emp.setDepartment(deptA);
            employeeRepository.save(emp);
        }

        // Delete department B
        departmentRepository.delete(deptB);

        return deptA;
    }
}