package me.gimun.demospringmvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {

    @GetMapping("/hellogimun")
    public String hello(Model model){
        model.addAttribute("name","gimun");
        return "hellogimun";
    }
}
