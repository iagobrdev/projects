package br.com.projects.service;

import br.com.projects.exception.BusinessException;
import br.com.projects.model.dto.ProjectRequestDto;
import br.com.projects.model.dto.ProjectResponseDto;
import br.com.projects.model.entities.Project;
import br.com.projects.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public ProjectService(ProjectRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<ProjectResponseDto> findAll(Pageable pagination) {
        return repository
                .findAll(pagination)
                .map(p -> mapper.map(p, ProjectResponseDto.class));
    }

    public ProjectResponseDto createProject(ProjectRequestDto dto) {
        try {
            var project = mapper.map(dto, Project.class);

            var savedProject = repository.save(project);

            return mapper.map(savedProject, ProjectResponseDto.class);
        }
        catch (Exception e) {
            throw new BusinessException(
                "Falha ao cadastrar o projeto!",
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Transactional
    public void updateStatus(Long id, String newStatus) {
        var project = repository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "Falha ao atualizar o status!",
                        "Projeto não localizado.",
                        HttpStatus.INTERNAL_SERVER_ERROR
                ));

        project.setStatus(newStatus);

        repository.save(project);
    }
}
