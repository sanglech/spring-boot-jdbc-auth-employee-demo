package com.christian.springbootthyme.jdbcauth.controller;

import com.christian.springbootthyme.jdbcauth.user.CrmUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserDetailsManager userDetailsManager;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private Logger logger = Logger.getLogger(getClass().getName());

    private Map<String, String> roles;

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
    }


    @GetMapping("showRegisterForm")
    public String showRegisterForm(Model theModel){
        //Create crm user dao, and entity
        // create roles and prepopulate

        //theModel.addAttribute("crmUser",null);
        //theModel.addAttribute("roles",null);

        theModel.addAttribute("crmUser", new CrmUser());

        // add roles to the model for form display
        theModel.addAttribute("roles", roles);

        return "registration-form";


        //return "helloworld";
    }
}
