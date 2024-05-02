package com.diamam.clinic.stub;

import com.diamam.clinic.model.doctor.CreateDoctorDto;
import com.diamam.clinic.model.doctor.UpdateDoctorDto;
import org.springframework.data.mongodb.core.query.Update;

public class DoctorDtoStubs {

    public static CreateDoctorDto createDoctorDto(){
        return new CreateDoctorDto(
                "Андрей",
                "Дмитриевич",
                "Артемович",
                "Терапевт",
                "Успешный врач с 10-летним стажем",
                "в отпуске",
                2.7);
    }

    public static UpdateDoctorDto updateDoctorDto(){
        return new UpdateDoctorDto(
                "1694",
                "Андрей",
                "Дмитриевич",
                "Артемович",
                "Терапевт",
                "Успешный врач с 10-летним стажем",
                "в отпуске",
                2.7);
    }


}
