package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@SpringBootApplication
@Controller
public class StartApplication {


    @GetMapping("/")
    public String index(final Model model) {
        model.addAttribute("title", "Hello everyone from DevOps with Mickael");
        model.addAttribute("msg", "We are deploying a Java Springboot application into AWS EKS cluster using Helm + Jenkins Pipeline!");
        return "index";
    }

    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

}