package br.com.projects.service;

import br.com.projects.exception.BusinessException;
import br.com.projects.model.dto.EmployeesToProject;
import br.com.projects.model.dto.ProjectRequestDto;
import br.com.projects.model.dto.ProjectResponseDto;
import br.com.projects.model.entities.Project;
import br.com.projects.model.entities.ProjectEmployees;
import br.com.projects.repository.EmployeeRepository;
import br.com.projects.repository.ProjectEmployeesRepository;
import br.com.projects.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectEmployeesRepository projectEmployeesRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper mapper;

    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                          ProjectEmployeesRepository projectEmployeesRepository,
                          EmployeeRepository employeeRepository,
                          ModelMapper mapper) {
        this.projectRepository = projectRepository;
        this.projectEmployeesRepository = projectEmployeesRepository;
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
    }

    @Transactional
    public Page<ProjectResponseDto> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(this::mapProjectToResponseDto);
    }

    @Transactional
    public ProjectResponseDto findById(Long id) {
        Project project = findProjectById(id);
        return mapProjectToResponseDto(project);
    }

    @Transactional
    public ProjectResponseDto createProject(ProjectRequestDto dto) {
        Project project = mapper.map(dto, Project.class);
        projectRepository.save(project);

        saveProjectEmployees(dto.getEmployees(), project);

        return mapProjectToResponseDto(project);
    }

    @Transactional
    public ProjectResponseDto updateProject(Long id, ProjectRequestDto dto) {
        Project project = findProjectById(id);
        mapper.map(dto, project);
        projectRepository.save(project);

        updateProjectEmployees(dto.getEmployees(), project);

        return mapProjectToResponseDto(project);
    }

    @Transactional
    public void deleteById(Long id) {
        Project project = findProjectById(id);
        validateProjectForDeletion(project);
        projectEmployeesRepository.deleteByProjectId(id);
        projectRepository.deleteById(id);
    }

    @Transactional
    public void updateStatus(Long id, String newStatus) {
        Project project = findProjectById(id);
        project.setStatus(newStatus);
        projectRepository.save(project);
    }

    private Project findProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(this::createProjectNotFoundException);
    }

    private BusinessException createProjectNotFoundException() {
        return new BusinessException(
                "Falha ao buscar o projeto!",
                "Projeto não localizado.",
                HttpStatus.NOT_FOUND
        );
    }

    private ProjectResponseDto mapProjectToResponseDto(Project project) {
        ProjectResponseDto responseDto = mapper.map(project, ProjectResponseDto.class);
        List<EmployeesToProject> employees = projectEmployeesRepository.findByProjectId(project.getId())
                .stream()
                .map(pe -> new EmployeesToProject(pe.getEmployee().getId(), pe.getEmployee().getName(), pe.getEmployee().getAssignment()))
                .collect(Collectors.toList());
        responseDto.setEmployees(employees);
        return responseDto;
    }

    private void saveProjectEmployees(List<EmployeesToProject> employeesToProjectList, Project project) {
        employeesToProjectList.forEach(employeesToProject -> {
            var employee = employeeRepository.findById(employeesToProject.getId())
                    .orElseThrow(() -> new BusinessException(
                            "Falha ao buscar o funcionário!",
                            "Funcionário não localizado.",
                            HttpStatus.NOT_FOUND
                    ));

            ProjectEmployees projectEmployee = new ProjectEmployees(0L, project, employee);
            projectEmployeesRepository.save(projectEmployee);
        });
    }

    private void updateProjectEmployees(List<EmployeesToProject> employeesToProjectList, Project project) {
        List<ProjectEmployees> existingEmployees = projectEmployeesRepository.findByProjectId(project.getId());
        projectEmployeesRepository.deleteAll(existingEmployees);
        saveProjectEmployees(employeesToProjectList, project);
    }

    private void validateProjectForDeletion(Project project) {
        String status = project.getStatus();
        if ("Iniciado".equals(status) || "Em Andamento".equals(status) || "Encerrado".equals(status)) {
            throw new BusinessException(
                    "Falha ao remover o projeto!",
                    "Não é possível remover projetos com status Iniciado, Em Andamento ou Encerrado",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}