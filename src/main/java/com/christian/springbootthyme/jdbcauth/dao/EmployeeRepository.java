package com.christian.springbootthyme.jdbcauth.dao;

import com.christian.springbootthyme.jdbcauth.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path="members")
public interface EmployeeRepository extends JpaRepository <Employee, Integer> {
    //finished.... all crud methods come from SpringDataJPA
    //add a method to sort by last name (needs to follow format)
    //check spring data jpa
    public List<Employee> findAllByOrderByLastNameAsc();
    public List<Employee> findByFirstNameContaining(String name);
    public List<Employee> findByLastNameLike(String name);
    public List<Employee> findByFirstNameAndLastName(String firstName,String lastName);
}
