package com.christian.springbootthyme.jdbcauth.controller;

import com.christian.springbootthyme.jdbcauth.entity.Employee;
import com.christian.springbootthyme.jdbcauth.service.EmployeeService;
import com.christian.springbootthyme.jdbcauth.user.CrmUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserDetailsManager userDetailsManager;

    //load employee data
    private EmployeeService employeeService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private Logger logger = Logger.getLogger(getClass().getName());

    private Map<String, String> roles;
    List<String> roleNames= new ArrayList<>();

    @Autowired
    public RegistrationController(EmployeeService theEmployeeService){
        employeeService=theEmployeeService;
    }


    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }


    @PostConstruct
    protected void loadRoles() {

        // using hashmap, could also read this info from a database

        roles = new LinkedHashMap<String, String>();

        // key=the role, value=display to user
        roles.put("ROLE_EMPLOYEE", "Employee");
        roles.put("ROLE_MANAGER", "Manager");
        roles.put("ROLE_ADMIN", "Admin");

        for(String name: roles.values()){
            roleNames.add(name);
        }

    }

    @GetMapping("/showRegisterForm")
    public String showRegisterForm(Model theModel, Model empModel){
        //Create crm user dao, and entity
        // create roles and prepopulate

        //theModel.addAttribute("crmUser",null);
        //theModel.addAttribute("roles",null);

        theModel.addAttribute("crmUser", new CrmUser());

        // add roles to the model for form display


        theModel.addAttribute("roles", roleNames);

        empModel.addAttribute("employee",new Employee());

        return "registration-form";

    }

    @PostMapping("/processRegistrationForm")
    public String processRegistrationForm(
            @Valid @ModelAttribute("crmUser") CrmUser theCrmUser,
            BindingResult theBindingResult,
            @Valid @ModelAttribute("employee") Employee theEmployee,
            BindingResult empBindingResult,
            Model theModel,
            Model empModel) {

        String userName = theCrmUser.getUserName();

        // form validation
        if (theBindingResult.hasErrors()) {

            theModel.addAttribute("crmUser", new CrmUser());
            empModel.addAttribute("employee",new Employee());

            // add roles to the model for form display
            theModel.addAttribute("roles", roleNames);


            theModel.addAttribute("registrationError", "User name/password can not be empty.");

            logger.warning("User name/password can not be empty.");

            return "registration-form";
        }

        if (empBindingResult.hasErrors()) {

            theModel.addAttribute("crmUser", new CrmUser());
            empModel.addAttribute("employee",new Employee());

            // add roles to the model for form display
            theModel.addAttribute("roles", roleNames);


            theModel.addAttribute("registrationError", "First Name/Last Name can not be empty.");

            logger.warning("First Name/Last Name can not be empty.");

            return "registration-form";
        }

        // check the database if user already exists
        boolean userExists = doesUserExist(userName);

        if (userExists) {
            theModel.addAttribute("crmUser", new CrmUser());
            empModel.addAttribute("employee",new Employee());

            // add roles to the model for form display
            theModel.addAttribute("roles", roleNames);

            theModel.addAttribute("registrationError", "User name already exists.");

            logger.warning("User name already exists.");

            return "registration-form";
        }




        String encodedPassword = passwordEncoder.encode(theCrmUser.getPassword());

        // prepend the encoding algorithm id
        encodedPassword = "{bcrypt}" + encodedPassword;

        // give user default role of "employee"
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList();
        authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));

        // if the user selected role other than employee,
        // then add that one too (multiple roles)
        String formRole = theCrmUser.getFormRole();

        for(Map.Entry<String, String> entry: roles.entrySet()) {
            if(entry.getValue().equals(formRole)) {
                System.out.println("The key for value " + formRole + " is " + entry.getKey());
                formRole=entry.getKey();
                break;
            }
        }

        if (!formRole.equals("ROLE_EMPLOYEE")) {
            authorities.add(new SimpleGrantedAuthority(formRole));
        }

        // create user object (from Spring Security framework)
        User tempUser = new User(userName, encodedPassword, authorities);

        // save user in the database
        userDetailsManager.createUser(tempUser);
        logger.info("Successfully created user: " + userName);


        // add user to employee table

        theEmployee.setEmail(theEmployee.getFirstName()+"@"+theEmployee.getLastName() +".com");
        employeeService.saveEmployee(theEmployee);
        
        return "registration-complete";

    }

    private boolean doesUserExist(String userName) {

        logger.info("Checking if user exists: " + userName);

        // check the database if the user already exists
        boolean exists = userDetailsManager.userExists(userName);

        logger.info("User: " + userName + ", exists: " + exists);

        return exists;
    }

}
