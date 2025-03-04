package com.diamam.doctorservice.mapper;

import com.diamam.doctorservice.entity.DoctorEntity;
import com.diamam.doctorservice.model.CreateDoctorRequest;
import com.diamam.doctorservice.model.DoctorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface DoctorMapper {

    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    DoctorEntity fromCreateRequest(CreateDoctorRequest request);

    DoctorResponse toDto(DoctorEntity entity);
}
