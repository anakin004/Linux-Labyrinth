package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import com.example.demo.service.CreateUserService;
import com.example.demo.model.PlayerEntity;

@Controller
public class LoginController{

    private final CreateUserService CUS;

    // Autowire CreateUserService via the constructor
    @Autowired
    public LoginController(CreateUserService createUserService) {
        CUS = createUserService;
    }
 
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    /* 
       
       this method is actually going to be taken care of by security config
       however i dont want to remove this code yet, might be useful for some manual login verification stuff

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username, @RequestParam String password) {
        // handle login logic 
        return "redirect:/home"; // Redirect to home page after successful login
    }
    */

    @GetMapping("/create-user")
    public String createUserPage() {
        return "create-user";
    }

    @GetMapping("/home")
    public String homePage() {
        return "index";  // Renders index.html
    }

    @PostMapping("/create-user")
    public String createUserSubmit(@RequestParam String username, 
                                    @RequestParam String password,
                                    @RequestParam String confirmPassword) {

        if (!password.equals(confirmPassword)) {
            return "create-user"; 
        }

        CUS.create(username,password);

        return "redirect:/login"; 
    }

}