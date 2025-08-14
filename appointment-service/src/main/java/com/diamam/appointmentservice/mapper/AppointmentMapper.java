package com.diamam.appointmentservice.mapper;

import com.diamam.appointmentservice.entity.AppointmentEntity;
import com.diamam.appointmentservice.dto.appointment.AppointmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "isAvailable", source = "available")
    AppointmentResponse toDto(AppointmentEntity entity);
}
