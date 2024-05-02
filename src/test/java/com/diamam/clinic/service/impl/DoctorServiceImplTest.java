package com.diamam.clinic.service.impl;

import com.diamam.clinic.entity.Doctor;
import com.diamam.clinic.model.doctor.CreateDoctorDto;
import com.diamam.clinic.model.doctor.UpdateDoctorDto;
import com.diamam.clinic.repository.DoctorRepository;
import com.diamam.clinic.service.DoctorService;
import com.diamam.clinic.stub.DoctorDtoStubs;
import com.diamam.clinic.stub.DoctorStubs;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Import(DoctorService.class)
class DoctorServiceImplTest {

    @Mock
    DoctorRepository doctorRepository;
    @InjectMocks
    DoctorServiceImpl doctorService;
    @Captor
    ArgumentCaptor<Doctor> doctorArgumentCaptor;

    @Test
    void save() {
        CreateDoctorDto dto = DoctorDtoStubs.createDoctorDto();
        doctorService.save(dto);

        verify(doctorRepository).save(doctorArgumentCaptor.capture());
        Assertions.assertThat(doctorArgumentCaptor.getValue())
                .hasFieldOrPropertyWithValue("firstName",dto.firstName())
                .hasFieldOrPropertyWithValue("middleName",dto.middleName())
                .hasFieldOrPropertyWithValue("lastName",dto.lastName())
                .hasFieldOrPropertyWithValue("speciality",dto.speciality())
                .hasFieldOrPropertyWithValue("description",dto.description())
                .hasFieldOrPropertyWithValue("status",dto.status())
                .hasFieldOrPropertyWithValue("rating",dto.rating());
    }

    @Test
    void update() {
        UpdateDoctorDto dto = DoctorDtoStubs.updateDoctorDto();
        when(doctorRepository.findById(dto.id())).thenReturn(Optional.of(new Doctor()));
        doctorService.update(dto);
        verify(doctorRepository).save(doctorArgumentCaptor.capture());

        Assertions.assertThat(doctorArgumentCaptor.getValue())
                .hasFieldOrPropertyWithValue("id",dto.id())
                .hasFieldOrPropertyWithValue("firstName",dto.firstName())
                .hasFieldOrPropertyWithValue("middleName",dto.middleName())
                .hasFieldOrPropertyWithValue("lastName",dto.lastName())
                .hasFieldOrPropertyWithValue("speciality",dto.speciality())
                .hasFieldOrPropertyWithValue("description",dto.description())
                .hasFieldOrPropertyWithValue("status",dto.status())
                .hasFieldOrPropertyWithValue("rating",dto.rating());
    }

    @Test
    void delete() {
        Doctor doctor = DoctorStubs.savedPeterDmitriev();
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        doctorService.delete(doctor.getId());
        verify(doctorRepository).delete(doctor);
    }

    @Test
    void findById() {
        Doctor doctor = DoctorStubs.savedPeterDmitriev();
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        doctorService.findById(doctor.getId());
        verify(doctorRepository).findById(doctor.getId());
    }

    @Test
    void findAll() {
        List<Doctor> doctors = List.of(DoctorStubs.savedIvanIvanov(),DoctorStubs.savedPeterDmitriev());
        when(doctorRepository.findAll()).thenReturn(doctors);
        List<Doctor> all = doctorService.findAll();
        assertEquals(doctors,all);
    }
}