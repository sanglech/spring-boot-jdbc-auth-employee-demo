package com.christian.springbootthymeleafdemo.controller;

import com.christian.springbootthymeleafdemo.entity.Employee;
import com.christian.springbootthymeleafdemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    //load employee data
    private EmployeeService employeeService;

    @Autowired
     public EmployeeController (EmployeeService theEmployeeService){
         employeeService= theEmployeeService;
     }

    //Need so returning string is null instead of empty
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }


    //addmapping for /list
    @GetMapping("/list")
    public String listEmployees(Model theModel,Model searchModel){

        //Get employees from db
        List<Employee> employees= employeeService.findAll();

        theModel.addAttribute("theEmployees",employees);
        searchModel.addAttribute("employee",new Employee());

        return "employees/list-employees";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel){
        //create model attribute to bind form data
        Employee theEmplyee = new Employee();

        theModel.addAttribute("employee",theEmplyee);

        return "employees/employee-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("employeeId") int theId, Model theModel){
        //get employee from the service
        Employee theEmployee = employeeService.getEmployeeById(theId);

        //prepopulate model

        theModel.addAttribute("employee",theEmployee);

        //send to our form page
        return "employees/employee-form";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee theEmployee){
        //save the employee
        employeeService.saveEmployee(theEmployee);
        //use redirect to rpevent multple saves
        return "redirect:/employees/list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("employeeId") int theId){
        //get employee from the service
        Employee theEmployee = employeeService.getEmployeeById(theId);

        //prepopulate model
        if(theEmployee==null){
            throw new RuntimeException("Employee with the id: "+ theId+ " does not exist");
        }
        employeeService.deleteEmployeeById(theId);

        //redirect back to list
        return "redirect:/employees/list";
    }


    @RequestMapping("/search")
    public String searchEmployee(@ModelAttribute("employee") Employee theEmployee, Model theModel){
        //save the employee
        //employeeService.saveEmployee(theEmployee);
        List<Employee> employees=null;
        if(theEmployee.getFirstName() ==null&&theEmployee.getLastName()==null){
            employees= employeeService.findAll();
        }
        else if(theEmployee.getFirstName()==null&&theEmployee.getLastName()!=null){
           // System.out.println("SEARCHING BY LAST NAME: "+ theEmployee.getLastName() );
            employees= employeeService.findByLastNameLike(theEmployee.getLastName());
        }
        else if(theEmployee.getFirstName()!=null&&theEmployee.getLastName()==null){
            employees=employeeService.findByFirstNameContaining(theEmployee.getFirstName());
        }
        else{
           // System.out.println("LOOKING FOR FIRST NAME: "+
           //         theEmployee.getFirstName() +" "+theEmployee.getLastName());
            employees=employeeService.findByFirstNameAndLastName(theEmployee.getFirstName(),
                    theEmployee.getLastName());
        }

        theModel.addAttribute("theEmployees",employees);
       /// System.out.println(">>>>>>>>"+ employees);
        //System.out.println(">>>>In search: "+ theEmployee.getFirstName() + theEmployee.getLastName());

        //theModel.addAttribute("employee",new Employee());
        return "employees/search-employee";
    }
}
