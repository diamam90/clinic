package com.diamam.appointmentservice.repository;

import com.diamam.appointmentservice.entity.AppointmentEntity;
import lombok.SneakyThrows;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class AppointmentRepositoryTest {

    @Autowired
    AppointmentRepository repository;

    @Autowired
    DataSource dataSource;

    static final String INSERT_QUERY = """
            INSERT INTO clc.t_appointments (client_id,doctor_id,start_time,end_time,date, is_available)
                VALUES (?,?,?,?,?,?)
            """;

    static final String CLEAN_QUERY = "TRUNCATE clc.t_appointments";

    static PostgreSQLContainer container =
            new PostgreSQLContainer(DockerImageName.parse("postgres:16.8"));


    {
        container.start();
    }


    @Test
    void findByClientId_ShouldReturnEmptyList() {

        var actual = repository.findByClientId("client2");

        assertThat(actual).isEmpty();


    }

    @Test
    void findByClientId_WhenClientIdIsNull_ShouldReturn2Appointments() {

        var actual = repository.findByClientId(null);

        assertThat(actual).hasSize(2)
                .extracting("doctorId", "clientId", "date", "start", "end", "isAvailable")
                .contains(
                        Tuple.tuple("doctor123",
                                null,
                                LocalDate.of(2025, 1, 1),
                                LocalTime.of(8, 0, 0),
                                LocalTime.of(8, 30, 0),
                                true),
                        Tuple.tuple("doctor6",
                                null,
                                LocalDate.of(2025, 2, 3),
                                LocalTime.of(9, 0, 0),
                                LocalTime.of(10, 0, 0),
                                false));
    }

    @Test
    void findByClientId_ShouldReturnAppointment() {

        var actual = repository.findByClientId("client228");
        assertThat(actual).hasSize(1)
                .element(0)
                .hasFieldOrPropertyWithValue("doctorId", "doctor322")
                .hasFieldOrPropertyWithValue("clientId", "client228")
                .hasFieldOrPropertyWithValue("date", LocalDate.of(2025, 1, 1))
                .hasFieldOrPropertyWithValue("start", LocalTime.of(12, 0, 0))
                .hasFieldOrPropertyWithValue("end", LocalTime.of(12, 30, 0))
                .hasFieldOrPropertyWithValue("isAvailable", false);
    }

    @Test
    void findByClientIdAndDate_ShouldReturn2Appointments() {
        var appointments = repository.findByClientIdAndDate("client1",
                LocalDate.of(2025, 1, 1));
        assertThat(appointments)
                .hasSize(2)
                .extracting("clientId", "doctorId", "date")
                .contains(Tuple.tuple("client1", "doctor345", LocalDate.of(2025, 1, 1)),
                        Tuple.tuple("client1", "doctor322", LocalDate.of(2025, 1, 1)));
    }

    @Test
    void findByDoctorId_ShouldReturnAppointment() {
        var actual = repository.findByDoctorId("doctor123");
        assertThat(actual)
                .hasSize(1)
                .element(0)
                .hasFieldOrPropertyWithValue("doctorId", "doctor123")
                .hasFieldOrPropertyWithValue("clientId", null)
                .hasFieldOrPropertyWithValue("date", LocalDate.of(2025, 1, 1))
                .hasFieldOrPropertyWithValue("start", LocalTime.of(8, 0, 0))
                .hasFieldOrPropertyWithValue("end", LocalTime.of(8, 30, 0))
                .hasFieldOrPropertyWithValue("clientId", null)
                .hasFieldOrPropertyWithValue("isAvailable", true);
    }

    @Test
    void findByDoctorIdAndDate_ShouldReturn2Appointments() {
        var actual = repository.findByDoctorIdAndDate("doctor322", LocalDate.of(2025, 1, 1));
        assertThat(actual)
                .hasSize(2)
                .extracting("doctorId", "clientId", "date")
                .contains(Tuple.tuple("doctor322", "client1", LocalDate.of(2025, 1, 1)))
                .contains(Tuple.tuple("doctor322", "client228", LocalDate.of(2025, 1, 1)));
    }

    @Test
    void findByDoctorIdAndDate_WhenAppointmentsNotExistAtCurrentDate_ShouldReturn2Appointments() {
        var actual = repository.findByDoctorIdAndDate("doctor322", LocalDate.of(2025, 2, 1));
        assertThat(actual).isEmpty();
    }

    @Test
    void findByDoctorIdAndDateBetween_ShouldReturn3Appointments() {
        var actual = repository.findByDoctorIdAndDateBetween("doctor6",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 2, 3));

        assertThat(actual)
                .hasSize(3)
                .extracting("doctorId", "clientId", "date")
                .contains(
                        Tuple.tuple("doctor6", "client3", LocalDate.of(2025, 1, 1)),
                        Tuple.tuple("doctor6", null, LocalDate.of(2025, 2, 3)),
                        Tuple.tuple("doctor6", "client34", LocalDate.of(2025, 1, 15))
                );
    }

    @Test
    void findByDoctorIdAndDateBetween_ShouldReturn2Appointments() {
        var actual = repository.findByDoctorIdAndDateBetween("doctor6",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 2, 1));

        assertThat(actual)
                .hasSize(2)
                .extracting("doctorId", "clientId", "date")
                .contains(
                        Tuple.tuple("doctor6", "client3", LocalDate.of(2025, 1, 1)),
                        Tuple.tuple("doctor6", "client34", LocalDate.of(2025, 1, 15))
                );
    }

    @Test
    void findByDoctorIdAndDateBetween_ShouldReturn1Appointments() {
        var actual = repository.findByDoctorIdAndDateBetween("doctor6",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 14));

        assertThat(actual)
                .hasSize(1)
                .extracting("doctorId", "clientId", "date")
                .contains(Tuple.tuple("doctor6", "client3", LocalDate.of(2025, 1, 1)));
    }

    @Test
    void findByDoctorIdAndDateBetween_ShouldReturnEmptyList() {
        var actual = repository.findByDoctorIdAndDateBetween("doctor6",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31));

        assertThat(actual).isEmpty();
    }

    @Test
    void existByDoctorAndDate_ShouldReturnTrue() {
        var actual = repository.existByDoctorAndDate("doctor6", LocalDate.of(2025, 1, 1));
        assertTrue(actual);
    }

    @Test
    void existByDoctorAndDate_ShouldReturnFalse() {
        var actual = repository.existByDoctorAndDate("doctor6", LocalDate.of(2025, 1, 2));
        assertFalse(actual);
    }

    @Test
    void existByDoctorAndDateBetween_ShouldReturnTrue() {
        var actual = repository.existByDoctorAndDateBetween("doctor6",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 2, 1));
        assertTrue(actual);
    }

    @Test
    void existByDoctorAndDateBetween_ShouldReturnFalse() {
        var actual = repository.existByDoctorAndDateBetween("doctor6",
                LocalDate.of(2025, 2, 4),
                LocalDate.of(2025, 2, 7));
        assertFalse(actual);
    }

    @SneakyThrows
    @Test
    void shouldSave() {
        var appointment = new AppointmentEntity();
        appointment.setDoctorId("acb2ac2");
        appointment.setClientId("client1");
        appointment.setDate(LocalDate.of(2025, 5, 5));
        appointment.setStart(LocalTime.of(9, 0, 5));
        appointment.setEnd(LocalTime.of(9, 6, 5));

        repository.save(appointment);

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM clc.t_appointments WHERE client_id = 'client1' AND doctor_id = 'acb2ac2'");
            assertTrue(resultSet.next());

            String doctorId = resultSet.getString("doctor_id");
            assertEquals(appointment.getDoctorId(), doctorId);

            String clientId = resultSet.getString("client_id");
            assertEquals(appointment.getClientId(), clientId);

            LocalDate  date = resultSet.getDate("date").toLocalDate();
            assertEquals(appointment.getDate(), date);

            LocalTime  start = resultSet.getTime("start_time").toLocalTime();
            assertEquals(appointment.getStart(), start);

            LocalTime  end = resultSet.getTime("end_time").toLocalTime();
            assertEquals(appointment.getEnd(), end);
        }
    }

    @SneakyThrows
    @Test
    void shouldSaveAll() {
        var appointment = new AppointmentEntity();
        appointment.setDoctorId("acb2ac2");
        appointment.setClientId("client1");
        appointment.setDate(LocalDate.of(2025, 5, 5));
        appointment.setStart(LocalTime.of(9, 0, 5));
        appointment.setEnd(LocalTime.of(9, 6, 5));

        repository.saveAll(List.of(appointment));

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM clc.t_appointments WHERE client_id = 'client1' AND doctor_id = 'acb2ac2'");
            assertTrue(resultSet.next());

            String doctorId = resultSet.getString("doctor_id");
            assertEquals(appointment.getDoctorId(), doctorId);

            String clientId = resultSet.getString("client_id");
            assertEquals(appointment.getClientId(), clientId);

            LocalDate  date = resultSet.getDate("date").toLocalDate();
            assertEquals(appointment.getDate(), date);

            LocalTime  start = resultSet.getTime("start_time").toLocalTime();
            assertEquals(appointment.getStart(), start);

            LocalTime  end = resultSet.getTime("end_time").toLocalTime();
            assertEquals(appointment.getEnd(), end);
        }
    }


    @BeforeEach
    void dummy() throws SQLException {
        var appointments = new ArrayList<AppointmentEntity>();

        var appointment1 = new AppointmentEntity();
        appointment1.setDoctorId("doctor123");
        appointment1.setDate(LocalDate.of(2025, 1, 1));
        appointment1.setStart(LocalTime.of(8, 0, 0));
        appointment1.setEnd(LocalTime.of(8, 30, 0));
        appointment1.setAvailable(true);

        appointments.add(appointment1);

        var appointment2 = new AppointmentEntity();
        appointment2.setDoctorId("doctor322");
        appointment2.setClientId("client1");
        appointment2.setDate(LocalDate.of(2025, 1, 1));
        appointment2.setStart(LocalTime.of(8, 30, 0));
        appointment2.setEnd(LocalTime.of(9, 0, 0));
        appointment2.setAvailable(false);

        appointments.add(appointment2);

        var appointment3 = new AppointmentEntity();
        appointment3.setDoctorId("doctor345");
        appointment3.setClientId("client1");
        appointment3.setDate(LocalDate.of(2025, 1, 1));
        appointment3.setStart(LocalTime.of(8, 0, 0));
        appointment3.setEnd(LocalTime.of(8, 30, 0));
        appointment3.setAvailable(false);

        appointments.add(appointment3);

        var appointment4 = new AppointmentEntity();
        appointment4.setDoctorId("doctor322");
        appointment4.setClientId("client228");
        appointment4.setDate(LocalDate.of(2025, 1, 1));
        appointment4.setStart(LocalTime.of(12, 0, 0));
        appointment4.setEnd(LocalTime.of(12, 30, 0));
        appointment4.setAvailable(false);

        appointments.add(appointment4);

        var appointment5 = new AppointmentEntity();
        appointment5.setDoctorId("doctor6");
        appointment5.setClientId("client3");
        appointment5.setDate(LocalDate.of(2025, 1, 1));
        appointment5.setStart(LocalTime.of(8, 0, 0));
        appointment5.setEnd(LocalTime.of(9, 0, 0));
        appointment5.setAvailable(false);

        appointments.add(appointment5);

        var appointment6 = new AppointmentEntity();
        appointment6.setDoctorId("doctor6");
        appointment6.setDate(LocalDate.of(2025, 2, 3));
        appointment6.setStart(LocalTime.of(9, 0, 0));
        appointment6.setEnd(LocalTime.of(10, 0, 0));
        appointment6.setAvailable(false);

        appointments.add(appointment6);

        var appointment7 = new AppointmentEntity();
        appointment7.setDoctorId("doctor6");
        appointment7.setClientId("client34");
        appointment7.setDate(LocalDate.of(2025, 1, 15));
        appointment7.setStart(LocalTime.of(9, 0, 0));
        appointment7.setEnd(LocalTime.of(10, 0, 0));
        appointment7.setAvailable(false);

        appointments.add(appointment7);


        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_QUERY)) {
            connection.setAutoCommit(false);
            for (AppointmentEntity appointment : appointments) {
                ps.setString(1, appointment.getClientId());
                ps.setString(2, appointment.getDoctorId());
                ps.setTime(3, Time.valueOf(appointment.getStart()));
                ps.setTime(4, Time.valueOf(appointment.getEnd()));
                ps.setDate(5, Date.valueOf(appointment.getDate()));
                ps.setBoolean(6, appointment.isAvailable());
                ps.addBatch();
            }

            ps.executeBatch();
            connection.commit();
        }
    }

    @AfterEach
    void clean() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(CLEAN_QUERY)) {
            st.execute();
        }
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> container.getJdbcUrl());
        registry.add("spring.datasource.username", () -> container.getUsername());
        registry.add("spring.datasource.password", () -> container.getPassword());

        registry.add("flyway.datasource.url", () -> container.getJdbcUrl());
        registry.add("flyway.datasource.user", () -> container.getUsername());
        registry.add("flyway.datasource.password", () -> container.getPassword());
    }
}