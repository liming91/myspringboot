package com.ming.controller;

import com.ming.dao.DepartmentDao;
import com.ming.dao.EmployeeDao;
import com.ming.entities.Department;
import com.ming.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
public class EmployeeController {

    @Autowired
    EmployeeDao employeeDao;

    @Autowired
    DepartmentDao departmentDao;

    //查询所有员工
    @GetMapping("/emps")
    public String  list(Model model){
        Collection<Employee> employees = employeeDao.getAll();
        model.addAttribute("emps",employees);
        // thymeleaf默认就会拼串
        // classpath:/templates/xxxx.html
        return "emp/list";
    }

    //来到员工添加页面
    @GetMapping("/emp")
    public String toAddPage(Model model){
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("depts",departments);
        return "emp/add";
    }

    //springmvc请求的参数和入参对象进行一一绑定 要求请求参数的名字和Javabean入参对象里面的属性名是一样的
    @PostMapping("/emp")
    public String addEmp(Employee employee){
        employeeDao.save(employee);

        //redirect:表示重定向到一个地址 /代表当前 项目路径
        //forward:转发到一个地址
        return "redirect:/emps";
    }

    //来到员工修改页面,先查出当前页面员工
    @GetMapping("/emp/{id}")
    public String toEdit(@PathVariable("id") Integer id,Model model){
        Employee employee = employeeDao.get(id);
        model.addAttribute("emp",employee);
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("depts",departments);
        //修改和添加一个页面
        return "emp/add";
    }

    @PutMapping("/emp")
    public String updateEmp(Employee employee){
        employeeDao.save(employee);
        return "redirect:/emps";
    }

    @DeleteMapping("/emp/{id}")
    public String deleteEmp(@PathVariable("id") Integer id){
        employeeDao.delete(id);
        return "redirect:/emps";
    }
}
