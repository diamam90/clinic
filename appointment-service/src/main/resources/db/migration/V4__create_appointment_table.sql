CREATE TABLE IF NOT EXISTS clc.t_appointments(
    id bigint primary key,
    client_id varchar(24),
    doctor_id varchar(24),
    start_time time not null,
    end_time time not null,
    date date not null,
    is_available boolean default true
);

CREATE SEQUENCE IF NOT EXISTS clc.t_appointments_id_seq START 1;

ALTER TABLE clc.t_appointments ALTER COLUMN id SET DEFAULT nextval('clc.t_appointments_id_seq');
