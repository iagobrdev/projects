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

   public ProjectResponseDto findById(Long id) {
        var response = repository.findById(id).orElseThrow(() -> new BusinessException(
               "Falha ao busocar o projeto!",
               "Projeto não localizado.",
               HttpStatus.NOT_FOUND
        ));

        return mapper.map(response, ProjectResponseDto.class);
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

    public ProjectResponseDto updateProject(Long id, ProjectRequestDto dto) {
        try {
            var project = repository.findById(id).orElseThrow(() -> new BusinessException(
                    "Falha ao buscar o projeto!",
                    "Projeto não localizado.",
                    HttpStatus.NOT_FOUND
            ));

            mapper.map(dto, project);

            project = repository.save(project);

            return mapper.map(project, ProjectResponseDto.class);
        } catch (Exception e) {
            throw new BusinessException(
                    "Falha ao atualizar o projeto!",
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public void deleteById(Long id) {
        var project = repository.findById(id).orElseThrow(() -> new BusinessException(
                "Falha ao busocar o projeto!",
                "Projeto não localizado.",
                HttpStatus.NOT_FOUND
        ));

        var status = project.getStatus();

        if(status.equals("Iniciado") || status.equals("Em Andamento") || status.equals("Encerrado")) {
            throw new BusinessException(
                    "Falha ao remover o projeto!",
                    "Não é possível remover projetos com status iniciado, em andamento ou encerrado",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        repository.deleteById(id);
    }

    @Transactional
    public void updateStatus(Long id, String newStatus) {
        var project = repository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "Falha ao atualizar o status!",
                        "Projeto não localizado.",
                        HttpStatus.NOT_FOUND
                ));

        project.setStatus(newStatus);

        repository.save(project);
    }
}
