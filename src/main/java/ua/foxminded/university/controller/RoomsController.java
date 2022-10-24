package ua.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ua.foxminded.university.controller.urls.URL;
import ua.foxminded.university.service.RoomService;

@Controller
public class RoomsController {

    @Autowired
    private RoomService roomService;

    @GetMapping(URL.APP_ROOMS)
    public String listAllRooms(Model model) {
        model.addAttribute("rooms", roomService.findAll());
        return "rooms/list";
    }
}
