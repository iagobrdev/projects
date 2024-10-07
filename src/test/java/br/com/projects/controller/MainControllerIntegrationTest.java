package br.com.projects.controller;

import br.com.projects.repository.EmployeeRepository;
import br.com.projects.repository.ProjectEmployeesRepository;
import br.com.projects.repository.ProjectRepository;
import br.com.projects.service.EmployeeService;
import br.com.projects.service.ProjectService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class MainControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private EntityManager entityManager;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private ProjectEmployeesRepository projectEmployeesRepository;

    @MockBean
    private org.flywaydb.core.Flyway flyway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetHome_thenReturnProjectsView() throws Exception {
        when(projectService.findAll(Mockito.any())).thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects"));
    }
}
