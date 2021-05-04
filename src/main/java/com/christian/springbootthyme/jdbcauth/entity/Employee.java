package com.christian.springbootthyme.jdbcauth.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="employee")
public class Employee {

    //define fields
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @NotNull(message="is required")
    @Size(min=1, message="is required")
    @Column(name="first_name")
    private String firstName;

    @NotNull(message="is required")
    @Size(min=1, message="is required")
    @Column(name="last_name")
    private String lastName;

    @Column(name="email")
    private String email;


    //define constructors
    public Employee(){

    }

    public Employee(String firstName, String lastName, String email,String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Employee(int id, String firstName, String lastName, String email,String username) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    //define getter and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    //define tostring


    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
