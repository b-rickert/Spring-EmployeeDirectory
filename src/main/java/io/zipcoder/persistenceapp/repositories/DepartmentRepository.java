package io.zipcoder.persistenceapp.repositories;

import io.zipcoder.persistenceapp.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}