package com.diamam.appointmentservice.mapper;

import com.diamam.appointmentservice.entity.AppointmentEntity;
import com.diamam.appointmentservice.model.AppointmentResponse;
import com.diamam.appointmentservice.model.CreateAppointmentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "available",expression = "java(true)")
    AppointmentEntity create(CreateAppointmentRequest request);
    AppointmentResponse toDto(AppointmentEntity entity);
}
