package by.alex.spring.controllers;

import by.alex.spring.dao.PersonDAO;
import by.alex.spring.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final PersonDAO personDAO;

    @Autowired
    public AdminController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping
    public String adminPage(Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("people", personDAO.allPeople());
        return "adminPage";
    }

    @PatchMapping("/add")
    public String saveAdmin(@ModelAttribute("person") Person person){
        personDAO.makeAdmin(person.getId());
        return "redirect:/people";
    }
}
