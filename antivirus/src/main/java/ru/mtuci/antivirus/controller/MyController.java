package ru.mtuci.antivirus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
// @RestController
@RequestMapping("/")
public class MyController {
    
    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/redirect")
    public RedirectView redirectToRickAstley(){
        return new RedirectView("https://rutube.ru/video/c6cc4d620b1d4338901770a44b3e82f4/");
    }

}
