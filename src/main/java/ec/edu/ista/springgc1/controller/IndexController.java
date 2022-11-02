package ec.edu.ista.springgc1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    //
    @GetMapping({"/index.html","/", "/index","/home",""})
    public String index(Model model) {
        model.addAttribute("titulo", "Bienvenido");
        return "index";
    }
}
