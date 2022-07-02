package com.easy.easystays.controller;

import com.easy.easystays.model.Stay;
import com.easy.easystays.service.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StayController {
    private StayService stayService;

    @Autowired
    public StayController(StayService stayService) {
        this.stayService = stayService;
    }

    @PostMapping(value = "/stays")
    public void addStay(@RequestBody Stay stay) {
        this.stayService.add(stay);
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
