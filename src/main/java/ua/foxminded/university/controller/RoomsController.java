package ua.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.foxminded.university.service.implementation.RoomServiceImpl;

@Controller
@RequestMapping("/audiences")
public class RoomsController {

    private final RoomServiceImpl roomServiceImpl;
    
    @Autowired
    public RoomsController(RoomServiceImpl roomServiceImpl) {
        this.roomServiceImpl = roomServiceImpl;
    }
    
    @GetMapping
    public String list(Model model) {
        model.addAttribute("rooms", roomServiceImpl.findAll());
        return "rooms/list";
    }
    
}
