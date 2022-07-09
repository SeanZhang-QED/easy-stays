package com.easy.easystays.service;

import com.easy.easystays.exception.StaysNotExistException;
import com.easy.easystays.exception.UserNotExistException;
import com.easy.easystays.model.Location;
import com.easy.easystays.model.Stay;
import com.easy.easystays.model.StayImage;
import com.easy.easystays.model.User;
import com.easy.easystays.repository.LocationRepository;
import com.easy.easystays.repository.StayRepository;
import com.easy.easystays.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StayService {
    private StayRepository stayRepository;
    private ImageStorageService imageStorageService;
    private UserRepository userRepository;
    private LocationRepository locationRepository;
    private GeoCodingService geoCodingService;

    @Autowired
    public StayService(StayRepository stayRepository,
                       ImageStorageService imageStorageService,
                       UserRepository userRepository,
                       LocationRepository locationRepository,
                       GeoCodingService geoCodingService) {
        this.stayRepository = stayRepository;
        this.imageStorageService = imageStorageService;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.geoCodingService = geoCodingService;
    }

    // 1. upload
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(Stay stay, MultipartFile[] images, String host) throws UserNotExistException {
        // Step 1: save to GCS, and get url
        List<String> mediaLinks = Arrays
                                        .stream(images)
                                        .parallel()
                                        .map(image -> imageStorageService.save(image))
                                        .collect(Collectors.toList());
        List<StayImage> stayImages = new ArrayList<>();
        for (String mediaLink : mediaLinks) {
            stayImages.add(new StayImage(mediaLink, stay));
        }
        stay.setImages(stayImages);
        User u = this.userRepository.findByUsername(host);
        if (u != null) {
            stay.setHost(u);
        } else {
            throw new UserNotExistException("The host trying to upload not exits");
        }
        // Step 2: save to MySQL
        this.stayRepository.save(stay);
        // Step 3: get coordinate from GEO encoding, save to ES
        Location location = geoCodingService.getLocation(stay.getId(), stay.getAddress());
        locationRepository.save(location);
    }

    // 2. delete by id
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(int stayId, String username) throws StaysNotExistException{
        Stay stay = findByIdAndHost(stayId, username);
        if(stay == null) {
            throw new StaysNotExistException("Stay not exists");
        }
        this.stayRepository.delete(stay);
    }

    // 3. list the set of stays by host
    public List<Stay> listByHost(String username) {
        return this.stayRepository.findByHost(new User.Builder().setUsername(username).build());
    }

    // 4. find teh stay by stay id and host name
    public Stay findByIdAndHost(int stayId, String username) throws StaysNotExistException {
        Stay stay = this.stayRepository.findByIdAndHost(stayId, new User.Builder().setUsername(username).build());
        if(stay == null) {
            throw new StaysNotExistException("Stay not exists.");
        }
        return stay;
    }
}
