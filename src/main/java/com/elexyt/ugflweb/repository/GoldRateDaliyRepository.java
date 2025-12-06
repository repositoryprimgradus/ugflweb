package com.elexyt.ugflweb.repository;

import com.elexyt.ugflweb.entity.GoldRateDaliy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Spring Data JPA repository for the GoldRateDaliy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoldRateDaliyRepository extends JpaRepository<GoldRateDaliy, String> {

    Optional<GoldRateDaliy> findByDate(LocalDate date);

    Optional<GoldRateDaliy> findTopByOrderByDateDesc();

    default Optional<GoldRateDaliy> findTodayOrLast() {
        LocalDate today = LocalDate.now();
        Optional<GoldRateDaliy> todayOpt = findByDate(today);
        return todayOpt.isPresent() ? todayOpt : findTopByOrderByDateDesc();
    }
}
