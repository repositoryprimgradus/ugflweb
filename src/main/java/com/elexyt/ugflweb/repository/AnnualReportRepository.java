package com.elexyt.ugflweb.repository;

import com.elexyt.ugflweb.entity.AnnualReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnualReportRepository extends JpaRepository<AnnualReport, String> {
}
