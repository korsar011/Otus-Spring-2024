package ru.otus.hw.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/403")
    public String unathorizedPage() {
        return "403";
    }

}
