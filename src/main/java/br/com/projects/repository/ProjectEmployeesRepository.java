package br.com.projects.repository;

import br.com.projects.model.entities.ProjectEmployees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectEmployeesRepository extends JpaRepository<ProjectEmployees, Long> {

    List<ProjectEmployees> findByProjectId(Long projectId);
    void deleteByProjectId(Long projectId);
}
