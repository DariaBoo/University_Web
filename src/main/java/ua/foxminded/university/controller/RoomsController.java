package ua.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.foxminded.university.service.RoomService;

@Controller
@RequestMapping("/audiences")
public class RoomsController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public String listAllRooms(Model model) {
        model.addAttribute("rooms", roomService.findAll());
        return "rooms/list";
    }
}
