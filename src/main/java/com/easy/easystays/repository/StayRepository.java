package com.easy.easystays.repository;

import com.easy.easystays.model.Stay;
import com.easy.easystays.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StayRepository extends JpaRepository<Stay, Integer> {
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    // keywords: findBy (field name) And

    List<Stay> findByHost(User host); // select * where stay.host = host

    Stay findByIdAndHost(int id, User host); // select * where stay.id = id and stay.host = host

    List<Stay> findByIdInAndGuestNumberGreaterThanEqual(List<Integer> ids, int guestNumber);
}
