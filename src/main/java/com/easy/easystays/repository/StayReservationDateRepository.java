package com.easy.easystays.repository;

import com.easy.easystays.model.StayReservedDate;
import com.easy.easystays.model.StayReservedDateKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface StayReservationDateRepository extends JpaRepository<StayReservedDate, StayReservedDateKey> {

    // key{ int id, LocalDate date }
    @Query(value = "SELECT srd.id.stayId FROM StayReservedDate srd WHERE srd.id.stayId IN ?1 AND srd.id.date BETWEEN ?2 AND ?3 GROUP BY srd.id.stayId")
    Set<Integer> findByIdInAndDateBetween(List<Integer> stayIds, LocalDate startDate, LocalDate endDate);
}
