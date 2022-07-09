package com.easy.easystays.service;

import com.easy.easystays.model.Stay;
import com.easy.easystays.repository.StayRepository;
import com.easy.easystays.repository.StayReservationDateRepository;
import com.easy.easystays.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SearchService {
    private StayRepository stayRepository;
    private StayReservationDateRepository stayReservationDateRepository;
    private LocationRepository locationRepository;

    @Autowired
    public SearchService(StayRepository stayRepository,
                         StayReservationDateRepository stayReservationDateRepository,
                         LocationRepository locationRepository) {
        this.stayRepository = stayRepository;
        this.stayReservationDateRepository = stayReservationDateRepository;
        this.locationRepository = locationRepository;
    }

    public List<Stay> search(int guestNumber,
                             LocalDate checkinDate,
                             LocalDate checkoutDate,
                             double lat,
                             double lon,
                             String distance) {

        // Step 1: First, search on EC by distance to get all Stays
        List<Integer> stayIds = locationRepository.searchByDistance(lat, lon, distance);
        if (stayIds == null || stayIds.isEmpty()) {
            return new ArrayList<>();
        }

        // Step 2: Then, search by date and find the stays that can be reserved.
        Set<Integer> reservedStayIds = stayReservationDateRepository.findByIdInAndDateBetween(stayIds, checkinDate, checkoutDate.minusDays(1));

        List<Integer> availableStayIds = new ArrayList<>();
        for (Integer stayId : stayIds) {
            if (!reservedStayIds.contains(stayId)) {
                availableStayIds.add(stayId);
            }
        }

        // Step 3: Finally, delete all the stays that is too small to hold all the guests
        return stayRepository.findByIdInAndGuestNumberGreaterThanEqual(availableStayIds, guestNumber);
    }

}
