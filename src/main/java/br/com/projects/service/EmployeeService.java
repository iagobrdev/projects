package br.com.projects.service;

import br.com.projects.exception.BusinessException;
import br.com.projects.model.dto.EmployeeRequestDto;
import br.com.projects.model.dto.EmployeeResponseDto;
import br.com.projects.model.entities.Employee;
import br.com.projects.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public EmployeeService(EmployeeRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<EmployeeResponseDto> findAll(Pageable pageable) {
        return repository
                .findAllByAssignment(pageable)
                .map(employee -> mapper.map(employee, EmployeeResponseDto.class));
    }

    public EmployeeResponseDto createEmployee(EmployeeRequestDto employeeRequest) {
        try {
            var employee = mapper.map(employeeRequest, Employee.class);
            employee = repository.save(employee);
            return mapper.map(employee, EmployeeResponseDto.class);
        } catch (Exception e) {
            throw new BusinessException(
                    "Falha ao cadastrar o membro!",
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}