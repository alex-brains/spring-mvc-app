package by.alex.spring.controllers;

import by.alex.spring.dao.PersonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/batch_update")
public class BatchController {
    private final PersonDAO personDAO;

    @Autowired
    public BatchController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index() {
        return "batch/index";
    }

    @GetMapping("/without_batch")
    public String withoutBatch() {
        personDAO.multipleUpdate();
        return "redirect:/people";
    }

    @GetMapping("/with_batch")
    public String withBatch() {
        personDAO.batchUpdate();
        return "redirect:/people";
    }
}
