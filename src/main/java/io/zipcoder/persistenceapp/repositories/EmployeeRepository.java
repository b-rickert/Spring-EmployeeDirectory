package io.zipcoder.persistenceapp.repositories;

import io.zipcoder.persistenceapp.entities.Department;
import io.zipcoder.persistenceapp.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // JpaRepository gives us save(), findById(), findAll(), delete(), etc. for free

    List<Employee> findByDepartment(Department department);
    
    List<Employee> findByManager(Employee manager);
    
    List<Employee> findByManagerIsNull();
}