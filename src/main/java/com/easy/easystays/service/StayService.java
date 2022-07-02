package com.easy.easystays.service;

import com.easy.easystays.exception.StaysNotExistException;
import com.easy.easystays.model.Stay;
import com.easy.easystays.model.User;
import com.easy.easystays.repository.StayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import java.util.List;

@Service
public class StayService {
    private StayRepository stayRepository;

    @Autowired
    public StayService(StayRepository stayRepository) {
        this.stayRepository = stayRepository;
    }

    // 1. upload
    public void add(Stay stay) {
        this.stayRepository.save(stay);
    }

    // 2. delete by id
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Long stayId, String username) throws StaysNotExistException{
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
    public Stay findByIdAndHost(Long stayId, String username) throws StaysNotExistException {
        Stay stay = this.stayRepository.findByIdAndHost(stayId, new User.Builder().setUsername(username).build());
        if(stay == null) {
            throw new StaysNotExistException("Stay not exists.");
        }
        return stay;
    }
}
