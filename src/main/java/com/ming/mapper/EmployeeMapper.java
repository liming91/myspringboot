package com.ming.mapper;

import com.ming.bean.Employee;
import org.springframework.stereotype.Repository;

//@Mapper或者@MapperScan将接口转配到容器中
public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

    public void insertEmp(Employee employee);
}
