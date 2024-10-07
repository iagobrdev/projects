package br.com.projects.controller;

import br.com.projects.model.dto.EmployeeRequestDto;
import br.com.projects.model.dto.EmployeeResponseDto;
import br.com.projects.model.dto.ProjectRequestDto;
import br.com.projects.model.dto.ProjectResponseDto;
import br.com.projects.service.EmployeeService;
import br.com.projects.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeRequestDto employeeRequest;
    private EmployeeResponseDto employeeResponse;
    private ProjectRequestDto projectRequest;
    private ProjectResponseDto projectResponse;

    @BeforeEach
    void setUp() {
        employeeRequest = new EmployeeRequestDto();
        employeeRequest.setName("John Doe");
        employeeRequest.setAssignment("Developer");

        employeeResponse = new EmployeeResponseDto();
        employeeResponse.setId(1L);
        employeeResponse.setName("John Doe");
        employeeResponse.setAssignment("Developer");

        projectRequest = new ProjectRequestDto();
        projectRequest.setName("New Project");
        projectRequest.setStatus("Planejado");

        projectResponse = new ProjectResponseDto();
        projectResponse.setId(1L);
        projectResponse.setName("New Project");
        projectResponse.setStatus("Planejado");
    }

    @Test
    void createEmployee_ReturnsCreatedEmployee() throws Exception {
        Mockito.when(employeeService.createEmployee(Mockito.any(EmployeeRequestDto.class)))
                .thenReturn(employeeResponse);

        String jsonRequest = objectMapper.writeValueAsString(employeeRequest);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.assignment", is("Developer")));
    }

    @Test
    void getAllEmployees_ReturnsEmployeeList() throws Exception {
        Mockito.when(employeeService.findAll(Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(employeeResponse)));

        mockMvc.perform(get("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[0].assignment", is("Developer")));
    }

    @Test
    void createProject_ReturnsCreatedProject() throws Exception {
        Mockito.when(projectService.createProject(Mockito.any(ProjectRequestDto.class)))
                .thenReturn(projectResponse);

        String jsonRequest = objectMapper.writeValueAsString(projectRequest);

        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Project")))
                .andExpect(jsonPath("$.status", is("Planejado")));
    }

    @Test
    void getAllProjects_ReturnsProjectList() throws Exception {
        Mockito.when(projectService.findAll(Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(projectResponse)));

        mockMvc.perform(get("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("New Project")))
                .andExpect(jsonPath("$[0].status", is("Planejado")));
    }

    @Test
    void getProjectById_ReturnsProject() throws Exception {
        Mockito.when(projectService.findById(1L))
                .thenReturn(projectResponse);

        mockMvc.perform(get("/api/v1/projects/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Project")))
                .andExpect(jsonPath("$.status", is("Planejado")));
    }

    @Test
    void updateProjectStatus_ReturnsSuccessMessage() throws Exception {
        Mockito.doNothing().when(projectService).updateStatus(1L, "Em Andamento");

        mockMvc.perform(patch("/api/v1/projects/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"Em Andamento\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Status atualizado com sucesso"));
    }

    @Test
    void updateProject_ReturnsUpdatedProject() throws Exception {
        Mockito.when(projectService.updateProject(Mockito.eq(1L), Mockito.any(ProjectRequestDto.class)))
                .thenReturn(projectResponse);

        String jsonRequest = objectMapper.writeValueAsString(projectRequest);

        mockMvc.perform(put("/api/v1/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Project")))
                .andExpect(jsonPath("$.status", is("Planejado")));
    }

    @Test
    void deleteProject_ReturnsSuccessMessage() throws Exception {
        Mockito.doNothing().when(projectService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/projects/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Projeto removido com sucesso!"));
    }
}