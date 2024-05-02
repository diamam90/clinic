package com.diamam.clinic.repository;

import com.diamam.clinic.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataMongoTest
@Testcontainers(disabledWithoutDocker = true)
abstract class AbstractRepositoryTest {
    static final MongoDBContainer container = new MongoDBContainer("mongo:4.0.10");
    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    ClientRepository clientRepository;
    @MockBean
    AppointmentService appointmentService;
    @MockBean
    CommandLineRunner runner;


    @DynamicPropertySource
    static void registry(DynamicPropertyRegistry registry) {
        container.start();
        registry.add("spring.data.mongodb.uri", container::getConnectionString);
    }
}
