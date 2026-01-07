package io.zipcoder.persistenceapp.entities;

import jakarta.persistence.*;

@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentNumber;

    private String departmentName;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Employee departmentManager;

    public Department() {}

    public Long getDepartmentNumber() { return departmentNumber; }
    public void setDepartmentNumber(Long departmentNumber) { this.departmentNumber = departmentNumber; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public Employee getDepartmentManager() { return departmentManager; }
    public void setDepartmentManager(Employee departmentManager) { this.departmentManager = departmentManager; }
}