package com.christian.springbootthyme.jdbcauth.service;


import com.christian.springbootthyme.jdbcauth.dao.EmployeeRepository;
import com.christian.springbootthyme.jdbcauth.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository theEmployeeRepository){
        employeeRepository=theEmployeeRepository;
    }

    //No need for transactional for JPASpring data as it does transactional already
    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAllByOrderByLastNameAsc();
    }

    @Override
    public Employee getEmployeeById(int id) {
        Optional<Employee> result = employeeRepository.findById(id);
        Employee theEmployee=null;
        if(result.isPresent()){
            theEmployee= result.get();
            return theEmployee;
        }
        else{
            throw new RuntimeException("Could not find employee with id: " + id);
        }

    }

    @Override
    public void saveEmployee(Employee theEmployee) {
        employeeRepository.save(theEmployee);
    }

    @Override
    public void deleteEmployeeById(int theId) {
        employeeRepository.deleteById(theId);
    }

    @Override
    public List<Employee> findByFirstNameContaining(String name) {
        List<Employee> result = employeeRepository.findByFirstNameContaining(name);
        return result;
    }

    @Override
    public List<Employee> findByLastNameLike(String name) {
        List<Employee> result = employeeRepository.findByLastNameLike(name);
        return result;
    }

    @Override
    public List<Employee> findByFirstNameAndLastName(String firstName, String lastName) {
        List<Employee> result = employeeRepository.findByFirstNameAndLastName(firstName,lastName);
        return result;
    }

    //beanid is class name starting with lowercase
    //tells spring which bean to use
    /*

       private EmployeeDAO employeeDAO;

    @Autowired
    public EmployeeServiceImpl(@Qualifier("employeeDAOJPAImpl") EmployeeDAO theEmployeeDAO){
        employeeDAO=theEmployeeDAO;
    }
    @Override
    @Transactional
    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }

    @Override
    @Transactional
    public Employee getEmployeeById(int id) {
        return employeeDAO.getEmployeeById(id);
    }

    @Override
    @Transactional
    public void saveEmployee(Employee theEmployee) {
        employeeDAO.saveEmployee(theEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployeeById(int theId) {
        employeeDAO.deleteEmployeeById(theId);
    }*/
}