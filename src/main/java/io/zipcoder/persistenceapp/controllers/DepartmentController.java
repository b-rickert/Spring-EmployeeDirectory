package io.zipcoder.persistenceapp.controllers;

import io.zipcoder.persistenceapp.entities.Department;
import io.zipcoder.persistenceapp.services.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/API/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        return departmentService.createDepartment(department);
    }

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    // Set department manager
    @PutMapping("/{departmentId}/manager/{employeeId}")
    public Department setDepartmentManager(@PathVariable Long departmentId, @PathVariable Long employeeId) {
        return departmentService.setDepartmentManager(departmentId, employeeId);
    }

    // Update department name
    @PutMapping("/{departmentId}/name")
    public Department updateDepartmentName(@PathVariable Long departmentId, @RequestBody String newName) {
        return departmentService.updateDepartmentName(departmentId, newName);
    }

    // Merge departments
    @PostMapping("/merge/{departmentAId}/{departmentBId}")
    public Department mergeDepartments(@PathVariable Long departmentAId, @PathVariable Long departmentBId) {
        return departmentService.mergeDepartments(departmentAId, departmentBId);
    }
}