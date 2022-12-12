package com.employee.mappers;

import com.employee.dto.EmployeeDto;
import com.employee.entities.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee fromDtoToEntity(EmployeeDto dto);

    EmployeeDto fromEntityToDto(Employee employee);
}
