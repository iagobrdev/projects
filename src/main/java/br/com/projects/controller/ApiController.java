package br.com.projects.controller;

import br.com.projects.model.dto.ProjectRequestDto;
import br.com.projects.model.dto.ProjectResponseDto;
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
@RequestMapping("/api/v1/projects")
public class ApiController {

    private final ProjectService service;

    @Autowired
    public ApiController(ProjectService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDto> createNewProject(@RequestBody ProjectRequestDto dto) {
        return ResponseEntity.ok(service.createProject(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> findAll(@PageableDefault(size = 100) Pageable pagination) {
        return ResponseEntity.ok(service.findAll(pagination).getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateProjectStatus(@PathVariable Long id, @RequestBody Map<String, String> update) {
        service.updateStatus(id, update.get("status"));
        return ResponseEntity.ok("Status atualizado com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable Long id, @RequestBody ProjectRequestDto dto) {
        return ResponseEntity.ok(service.updateProject(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok("Projeto removido comsucesso!");
    }
}
