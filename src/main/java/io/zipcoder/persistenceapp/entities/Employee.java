package io.zipcoder.persistenceapp.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeNumber;

    private String firstName;
    private String lastName;
    private String title;
    private String phoneNumber;
    private String email;
    private LocalDate hireDate;

    // This employee's manager (also an Employee)
    // Many employees can report 
    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonBackReference
    private Employee manager;
    

    // Employees who report to THIS employee
    // mappedBy = "manager" tells JPA "look at the manager field on the other side"
    @OneToMany(mappedBy = "manager")
    @JsonManagedReference
    private List<Employee> directReports = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties("departmentManager")
    private Department department;

    // Default constructor required by JPA
    public Employee() {}

    // Getters and setters
    public Long getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(Long employeeNumber) { this.employeeNumber = employeeNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public Employee getManager() { return manager; }
    public void setManager(Employee manager) { this.manager = manager; }

    public List<Employee> getDirectReports() { return directReports; }
    public void setDirectReports(List<Employee> directReports) { this.directReports = directReports; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}
