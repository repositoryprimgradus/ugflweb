package com.elexyt.ugflweb.repository;

import com.elexyt.ugflweb.entity.BseIntimation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BseIntimationRepository extends JpaRepository<BseIntimation, String> {
}
