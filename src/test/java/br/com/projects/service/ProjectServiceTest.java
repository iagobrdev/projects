package br.com.projects.service;

import br.com.projects.exception.BusinessException;
import br.com.projects.model.dto.EmployeesToProject;
import br.com.projects.model.dto.ProjectRequestDto;
import br.com.projects.model.dto.ProjectResponseDto;
import br.com.projects.model.entities.Employee;
import br.com.projects.model.entities.Project;
import br.com.projects.model.entities.ProjectEmployees;
import br.com.projects.repository.EmployeeRepository;
import br.com.projects.repository.ProjectEmployeesRepository;
import br.com.projects.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectEmployeesRepository projectEmployeesRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProjectService projectService;

    private Project project;
    private ProjectRequestDto projectRequestDto;
    private ProjectResponseDto projectResponseDto;
    private Employee employee;
    private ProjectEmployees projectEmployees;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setAssignment("Developer");

        project = new Project();
        project.setId(1L);
        project.setName("New Project");
        project.setStatus("Planejado");

        projectEmployees = new ProjectEmployees(1L, project, employee);

        projectRequestDto = new ProjectRequestDto();
        projectRequestDto.setName("New Project");
        projectRequestDto.setStatus("Planejado");
        projectRequestDto.setEmployees(Collections.singletonList(new EmployeesToProject(1L, "John Doe", "Developer")));

        projectResponseDto = new ProjectResponseDto();
        projectResponseDto.setId(1L);
        projectResponseDto.setName("New Project");
        projectResponseDto.setStatus("Planejado");
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> projectPage = new PageImpl<>(Collections.singletonList(project), pageable, 1);

        when(projectRepository.findAll(pageable)).thenReturn(projectPage);
        when(modelMapper.map(any(Project.class), eq(ProjectResponseDto.class))).thenReturn(projectResponseDto);

        Page<ProjectResponseDto> result = projectService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(projectResponseDto.getName(), result.getContent().get(0).getName());

        verify(projectRepository, times(1)).findAll(pageable);
        verify(modelMapper, times(1)).map(any(Project.class), eq(ProjectResponseDto.class));
    }

    @Test
    void testFindById_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(modelMapper.map(any(Project.class), eq(ProjectResponseDto.class))).thenReturn(projectResponseDto);

        ProjectResponseDto result = projectService.findById(1L);

        assertNotNull(result);
        assertEquals(projectResponseDto.getName(), result.getName());

        verify(projectRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(any(Project.class), eq(ProjectResponseDto.class));
    }

    @Test
    void testFindById_NotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> projectService.findById(1L));

        assertEquals("Falha ao buscar o projeto!", exception.getMessage());

        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateProject_Success() {
        when(modelMapper.map(any(ProjectRequestDto.class), eq(Project.class))).thenReturn(project);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectEmployeesRepository.save(any(ProjectEmployees.class))).thenReturn(projectEmployees);
        when(modelMapper.map(any(Project.class), eq(ProjectResponseDto.class))).thenReturn(projectResponseDto);

        ProjectResponseDto result = projectService.createProject(projectRequestDto);

        assertNotNull(result);
        assertEquals(projectResponseDto.getName(), result.getName());

        verify(projectRepository, times(1)).save(any(Project.class));
        verify(employeeRepository, times(1)).findById(1L);
        verify(projectEmployeesRepository, times(1)).save(any(ProjectEmployees.class));
        verify(modelMapper, times(1)).map(any(Project.class), eq(ProjectResponseDto.class));
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void testUpdateProject_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(modelMapper.map(any(ProjectRequestDto.class), eq(Project.class))).thenReturn(project);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectEmployeesRepository.save(any(ProjectEmployees.class))).thenReturn(projectEmployees);
        when(modelMapper.map(any(Project.class), eq(ProjectResponseDto.class))).thenReturn(projectResponseDto);

        ProjectResponseDto result = projectService.updateProject(1L, projectRequestDto);

        assertNotNull(result);
        assertEquals(projectResponseDto.getName(), result.getName());

        verify(projectRepository, times(1)).save(any(Project.class));
        verify(employeeRepository, times(1)).findById(1L);
        verify(projectEmployeesRepository, times(1)).save(any(ProjectEmployees.class));
        verify(modelMapper, times(1)).map(any(Project.class), eq(ProjectResponseDto.class));
    }

    @Test
    void testDeleteProject_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        projectService.deleteById(1L);

        verify(projectEmployeesRepository, times(1)).deleteByProjectId(1L);
        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProject_WithInvalidStatus() {
        project.setStatus("Iniciado");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        BusinessException exception = assertThrows(BusinessException.class, () -> projectService.deleteById(1L));

        assertEquals("Falha ao remover o projeto!", exception.getMessage());

        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateStatus_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        projectService.updateStatus(1L, "Encerrado");

        verify(projectRepository, times(1)).save(any(Project.class));
    }
}
