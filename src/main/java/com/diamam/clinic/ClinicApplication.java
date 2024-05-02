package com.diamam.clinic;

import com.diamam.clinic.entity.Client;
import com.diamam.clinic.entity.Doctor;
import com.diamam.clinic.model.appointment.AppointmentInfo;
import com.diamam.clinic.model.appointment.UpdateAppointmentDto;
import com.diamam.clinic.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class ClinicApplication {
    private final MongoOperations mongoOperations;
    private final AppointmentService appointmentService;
    public static void main(String[] args) {
        SpringApplication.run(ClinicApplication.class, args);
    }

    @Bean
    public CommandLineRunner exec() {
        return (args) -> {
            mongoOperations.dropCollection("doctor");
            mongoOperations.dropCollection("client");
            mongoOperations.dropCollection("appointment");

            Doctor alex = new Doctor();
            alex.setFirstName("Алексей");
            alex.setLastName("Алексеев");
            alex.setMiddleName("Алексеевич");
            alex.setSpeciality("Терапевт");
            alex.setDescription("Алексей Алексеевич Алексеев закончил Волгоградский" +
                    " Медицинский Университет по специальности общая медицина");
            alex.setStatus("active");

            Doctor ivan = new Doctor();
            ivan.setFirstName("Иван");
            ivan.setLastName("Иванов");
            ivan.setRating(1.1);
            ivan.setSpeciality("Хирург");
            ivan.setStatus("active");

            Doctor dmitry = new Doctor();
            dmitry.setFirstName("Дмитрий");
            dmitry.setLastName("Терафлю");
            dmitry.setMiddleName("Александрович");
            dmitry.setStatus("active");
            dmitry.setDescription("Терафлю Дмитрий Александрович уже 15 лет помогает пациентам чувствовать себя лучше");
            dmitry.setSpeciality("Мануальный терапевт");

            Client client1 = new Client();
            client1.setFirstName("ClientI");
            client1.setLastName("lastnameI");
            client1.setEmail("client1@email.com");
            client1.setPhone("8923239595");
            client1.setAge(25);
            client1.setPassport("1808239800");

            Client client2 = new Client();
            client2.setFirstName("client2");
            client2.setLastName("lastNameovich");
            client2.setMiddleName("Middle");
            client2.setAge(77);
            client2.setPassport("777");
            client2.setEmail("email@gmail.com");
            client2.setPassport("2020900999");

            Doctor alexSaved = mongoOperations.save(alex, "doctor");
            Doctor dmitrySaved = mongoOperations.save(dmitry, "doctor");
            Doctor ivanSaved = mongoOperations.save(ivan, "doctor");

            Client client1Saved = mongoOperations.save(client1, "client");
            Client client2Saved = mongoOperations.save(client2, "client");

            List<AppointmentInfo> alexAppointments =
                    appointmentService.generateSchedule(alexSaved.getId(), LocalDate.now(), 7);
            List<AppointmentInfo> dmitryAppointments =
                    appointmentService.generateSchedule(dmitrySaved.getId(), LocalDate.now(), 5);
            List<AppointmentInfo> ivanAppointments =
                    appointmentService.generateSchedule(ivanSaved.getId(), LocalDate.now(), 7);

            AppointmentInfo alexAppointment = alexAppointments.get(3);
            UpdateAppointmentDto appointment1 = new UpdateAppointmentDto(
                    alexAppointment.id(),
                    client1Saved.getId(),
                    alexAppointment.doctor().id());

            appointmentService.make(appointment1);

            AppointmentInfo dmitryAppointment = dmitryAppointments.get(0);
            UpdateAppointmentDto appointment2 = new UpdateAppointmentDto(
                    dmitryAppointment.id(),
                    client1Saved.getId(),
                    dmitryAppointment.doctor().id());

            appointmentService.make(appointment2);

            AppointmentInfo ivanAppointment = ivanAppointments.get(40);
            UpdateAppointmentDto appointment3 = new UpdateAppointmentDto(
                    ivanAppointment.id(),
                    client2Saved.getId(),
                    ivanAppointment.doctor().id());

            appointmentService.make(appointment3);
        };
    }
}
