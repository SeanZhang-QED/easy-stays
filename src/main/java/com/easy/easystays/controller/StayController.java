package com.easy.easystays.controller;

import com.easy.easystays.model.Stay;
import com.easy.easystays.model.User;
import com.easy.easystays.service.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class StayController {
    private StayService stayService;

    @Autowired
    public StayController(StayService stayService) {
        this.stayService = stayService;
    }

    @PostMapping("/stays")
    public void addStay(
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("description") String description,
            @RequestParam("host") String host,
            @RequestParam("guest_number") int guestNumber,
            @RequestParam("images") MultipartFile[] images) {

        Stay stay = new Stay.Builder().setName(name)
                .setAddress(address)
                .setDescription(description)
                .setGuestNumber(guestNumber)
                .setHost(new User.Builder().setUsername(host).build())
                .build();
        stayService.add(stay, images);
    }


    @DeleteMapping(value = "/stays")
    public void deleteStay(@RequestParam(name = "stay_id") Long stayId,
                           @RequestParam(name = "host") String hostName) {
        this.stayService.delete(stayId, hostName);
    }

    @GetMapping(value = "/stays")
    public List<Stay> listStays(@RequestParam(name = "host") String hostName) {
        return this.stayService.listByHost(hostName);
    }

    @GetMapping(value = "/stays/id")
    public Stay getStay(@RequestParam(name = "stay_id") Long stayId,
                        @RequestParam(name = "host") String hostName) {
        return this.stayService.findByIdAndHost(stayId, hostName);
    }
}
