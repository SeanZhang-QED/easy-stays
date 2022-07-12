package com.easy.easystays.controller;

import com.easy.easystays.model.Reservation;
import com.easy.easystays.model.Stay;
import com.easy.easystays.model.User;
import com.easy.easystays.service.ReservationService;
import com.easy.easystays.service.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
public class StayController {
    private StayService stayService;
    private ReservationService reservationService;

    @Autowired
    public StayController(StayService stayService, ReservationService reservationService) {
        this.stayService = stayService;
        this.reservationService = reservationService;
    }


    @PostMapping("/stays")
    public void addStay(
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("description") String description,
            @RequestParam("guest_number") int guestNumber,
            @RequestParam("images") MultipartFile[] images,
            Principal principal) {

        Stay stay = new Stay.Builder().setName(name)
                .setAddress(address)
                .setDescription(description)
                .setGuestNumber(guestNumber)
                .build();
        stayService.add(stay, images, principal.getName());
    }


    @DeleteMapping(value = "/stays/{stayId}")
    public void deleteStay(@PathVariable int stayId,
                           Principal principal) {
        this.stayService.delete(stayId, principal.getName());
    }

    @GetMapping(value = "/stays")
    public List<Stay> listStays(Principal principal) {
        return this.stayService.listByHost(principal.getName());
    }

    @GetMapping(value = "/stays/{stayId}")
    public Stay getStay(@PathVariable int stayId,
                        Principal principal) {
        return this.stayService.findByIdAndHost(stayId, principal.getName());
    }

    @GetMapping(value = "/stays/reservations/{stayId}")
    public List<Reservation> listReservations(@PathVariable int stayId, Principal principal) {
        return reservationService.listByStay(stayId);
    }
}
