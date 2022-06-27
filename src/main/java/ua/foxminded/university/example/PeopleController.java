package ua.foxminded.university.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/people")
public class PeopleController {
    
    private final PersonDAO personDAO;
    
    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }
    
    @GetMapping()//input link: localhost:8080/university/people
    public String index(Model model) {
        model.addAttribute("people", personDAO.indexd());
        return "people/index";
    }
    
    @GetMapping("/{id}")//input link: localhost:8080/university/people/numberOfId
    public String show(@PathVariable("id") int id, Model model) {//model sends data to .html page to work with
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }
    
    @GetMapping("/new")//input link: localhost:8080/university/people/new
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";//returns created html file with directory and name: people/new.html
    }

    @PostMapping()
    public String create(@ModelAttribute("person") Person person) {
        personDAO.save(person);
        return "redirect:/people";
    }
}
