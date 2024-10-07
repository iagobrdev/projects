package br.com.projects.repository;

import br.com.projects.model.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE LOWER(e.assignment) = 'funcionario' OR LOWER(e.assignment) = 'funcionário'")
    Page<Employee> findAllByAssignment(Pageable pageable);
}
