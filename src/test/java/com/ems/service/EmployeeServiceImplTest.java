package com.ems.service;

import com.ems.dto.EmployeeRequest;
import com.ems.dto.EmployeeResponse;
import com.ems.entity.Department;
import com.ems.entity.Employee;
import com.ems.entity.EmployeeStatus;
import com.ems.exception.DuplicateRecordException;
import com.ems.exception.ResourceNotFoundException;
import com.ems.repository.DepartmentRepository;
import com.ems.repository.EmployeeRepository;
import com.ems.serviceImpl.EmployeeServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Department department;
    
    private Employee employee;
    
    private EmployeeRequest request;

    @BeforeEach
    void setUp() {
        department = Department.builder()
                .departmentId(1L)
                .departmentName("Engineering")
                .build();

        employee = Employee.builder()
                .employeeId(1L)
                .employeeName("John Doe")
                .email("john@example.com")
                .mobileNumber("9876543210")
                .designation("Developer")
                .salary(50000.0)
                .dateOfJoining(LocalDate.of(2024, 1, 1))
                .status(EmployeeStatus.ACTIVE)
                .department(department)
                .build();

        request = new EmployeeRequest();
        request.setEmployeeName("John Doe");
        request.setEmail("john@example.com");
        request.setMobileNumber("9876543210");
        request.setDesignation("Developer");
        request.setSalary(50000.0);
        request.setDateOfJoining(LocalDate.of(2024, 1, 1));
        request.setDepartmentId(1L);
    }

    @Test
    void createEmployee_shouldReturnSavedEmployee_whenValidRequest() {
        when(employeeRepository.findAll()).thenReturn(List.of());
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeResponse response = employeeService.createEmployee(request);

        assertEquals("John Doe", response.getEmployeeName());
        assertEquals("Engineering", response.getDepartmentName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void createEmployee_shouldThrowDuplicateRecordException_whenEmailAlreadyExists() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        assertThrows(DuplicateRecordException.class,
                () -> employeeService.createEmployee(request));

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void createEmployee_shouldThrowResourceNotFoundException_whenDepartmentDoesNotExist() {
        when(employeeRepository.findAll()).thenReturn(List.of());
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.createEmployee(request));
    }

    @Test
    void getEmployeeById_shouldReturnEmployee_whenExists() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeResponse response = employeeService.getEmployeeById(1L);

        assertEquals(1L, response.getEmployeeId());
        assertEquals("john@example.com", response.getEmail());
    }

    @Test
    void getEmployeeById_shouldThrowResourceNotFoundException_whenNotExists() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.getEmployeeById(99L));
    }

    @Test
    void deleteEmployee_shouldSetStatusToInactive_notActuallyDeleteRow() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeService.deleteEmployee(1L);

        assertEquals(EmployeeStatus.INACTIVE, employee.getStatus());
        verify(employeeRepository, times(1)).save(employee);
        verify(employeeRepository, never()).deleteById(anyLong());
    }
}