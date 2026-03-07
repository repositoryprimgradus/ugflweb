package com.elexyt.ugflweb.mapper;

import com.elexyt.ugflweb.dto.BseIntimationDTO;
import com.elexyt.ugflweb.entity.BseIntimation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BseIntimationMapper extends EntityMapper<BseIntimationDTO, BseIntimation> {
}
