package br.com.projects.service;

import br.com.projects.exception.BusinessException;
import br.com.projects.model.dto.EmployeeRequestDto;
import br.com.projects.model.dto.EmployeeResponseDto;
import br.com.projects.model.entities.Employee;
import br.com.projects.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private EmployeeRequestDto employeeRequestDto;
    private EmployeeResponseDto employeeResponseDto;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setAssignment("Developer");

        employeeRequestDto = new EmployeeRequestDto();
        employeeRequestDto.setName("John Doe");
        employeeRequestDto.setAssignment("Developer");

        employeeResponseDto = new EmployeeResponseDto();
        employeeResponseDto.setId(1L);
        employeeResponseDto.setName("John Doe");
        employeeResponseDto.setAssignment("Developer");
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> employeePage = new PageImpl<>(Collections.singletonList(employee), pageable, 1);

        when(employeeRepository.findAllByAssignment(pageable)).thenReturn(employeePage);
        when(modelMapper.map(any(Employee.class), eq(EmployeeResponseDto.class))).thenReturn(employeeResponseDto);

        Page<EmployeeResponseDto> result = employeeService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(employeeResponseDto.getName(), result.getContent().get(0).getName());

        verify(employeeRepository, times(1)).findAllByAssignment(pageable);
        verify(modelMapper, times(1)).map(any(Employee.class), eq(EmployeeResponseDto.class));
    }

    @Test
    void testCreateEmployee_Success() {
        when(modelMapper.map(any(EmployeeRequestDto.class), eq(Employee.class))).thenReturn(employee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(modelMapper.map(any(Employee.class), eq(EmployeeResponseDto.class))).thenReturn(employeeResponseDto);

        EmployeeResponseDto result = employeeService.createEmployee(employeeRequestDto);

        assertNotNull(result);
        assertEquals(employeeResponseDto.getName(), result.getName());

        verify(modelMapper, times(1)).map(any(EmployeeRequestDto.class), eq(Employee.class));
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(modelMapper, times(1)).map(any(Employee.class), eq(EmployeeResponseDto.class));
    }

    @Test
    void testCreateEmployee_Exception() {
        when(modelMapper.map(any(EmployeeRequestDto.class), eq(Employee.class))).thenReturn(employee);
        when(employeeRepository.save(any(Employee.class))).thenThrow(new RuntimeException("Database error"));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            employeeService.createEmployee(employeeRequestDto);
        });

        assertEquals("Falha ao cadastrar o membro!", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());

        verify(modelMapper, times(1)).map(any(EmployeeRequestDto.class), eq(Employee.class));
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }
}
