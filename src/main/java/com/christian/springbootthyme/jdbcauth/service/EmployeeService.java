package com.christian.springbootthyme.jdbcauth.service;

import com.christian.springbootthyme.jdbcauth.entity.Employee;

import java.util.List;

public interface EmployeeService {
    public List<Employee> findAll();
    public Employee getEmployeeById( int id);
    public void saveEmployee(Employee theEmployee);
    public void deleteEmployeeById(int theId);
    public List<Employee> findByFirstNameContaining(String name);
    public List<Employee> findByLastNameLike(String name);
    public List<Employee> findByFirstNameAndLastName(String firstName,String lastName);

}
