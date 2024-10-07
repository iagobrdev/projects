package br.com.projects.controller;

import br.com.projects.model.dto.EmployeeRequestDto;
import br.com.projects.model.dto.EmployeeResponseDto;
import br.com.projects.model.dto.ProjectRequestDto;
import br.com.projects.model.dto.ProjectResponseDto;
import br.com.projects.service.EmployeeService;
import br.com.projects.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private final ProjectService projectService;
    private final EmployeeService employeeService;

    @Autowired
    public ApiController(ProjectService projectService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

    @PostMapping("/projects")
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectRequestDto projectRequest) {
        return ResponseEntity.ok(projectService.createProject(projectRequest));
    }

    @PostMapping("/employees")
    public ResponseEntity<EmployeeResponseDto> createEmployee(@RequestBody EmployeeRequestDto employeeRequest) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeRequest));
    }

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects(@PageableDefault(size = 100) Pageable pageable) {
        return ResponseEntity.ok(projectService.findAll(pageable).getContent());
    }

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees(@PageableDefault(size = 100) Pageable pageable) {
        return ResponseEntity.ok(employeeService.findAll(pageable).getContent());
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    @PatchMapping("/projects/{id}/status")
    public ResponseEntity<String> updateProjectStatus(@PathVariable Long id, @RequestBody Map<String, String> statusUpdate) {
        projectService.updateStatus(id, statusUpdate.get("status"));
        return ResponseEntity.ok("Status atualizado com sucesso");
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable Long id, @RequestBody ProjectRequestDto projectRequest) {
        return ResponseEntity.ok(projectService.updateProject(id, projectRequest));
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        projectService.deleteById(id);
        return ResponseEntity.ok("Projeto removido com sucesso!");
    }
}