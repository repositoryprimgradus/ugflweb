package com.elexyt.ugflweb.mapper;

import com.elexyt.ugflweb.dto.AnnualReportDTO;
import com.elexyt.ugflweb.entity.AnnualReport;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnnualReportMapper extends EntityMapper<AnnualReportDTO, AnnualReport> {
}
