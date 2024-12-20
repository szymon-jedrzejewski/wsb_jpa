insert into address (id, address_line1, address_line2, city, postal_code)
values (1, 'xx', 'yy', 'city', '62-030');
insert into address (id, address_line1, address_line2, city, postal_code)
values (2, 'street1', 'apt101', 'city2', '11-111');
insert into address (id, address_line1, address_line2, city, postal_code)
values (3, 'street2', 'apt202', 'city3', '22-222');
insert into doctor (id, first_name, last_name, telephone_number, email, doctor_number, specialization)
values (1, 'John', 'Doe', '123456789', 'john.doe@example.com', 'D001', 'CARDIOLOGY');
insert into doctor (id, first_name, last_name, telephone_number, email, doctor_number, specialization)
values (2, 'Jane', 'Smith', '987654321', 'jane.smith@example.com', 'D002', 'NEUROLOGY');
insert into doctor (id, first_name, last_name, telephone_number, email, doctor_number, specialization)
values (3, 'Mike', 'Brown', '456123789', 'mike.brown@example.com', 'D003', 'DERMATOLOGY');
insert into patient (id, first_name, last_name, telephone_number, email, patient_number, date_of_birth, blood_type)
values (1, 'Alice', 'Green', '321654987', 'alice.green@example.com', 'P001', '1990-05-01', 4);
insert into patient (id, first_name, last_name, telephone_number, email, patient_number, date_of_birth, blood_type)
values (2, 'Bob', 'White', '789123456', 'bob.white@example.com', 'P002', '1985-08-15', 4);
insert into patient (id, first_name, last_name, telephone_number, email, patient_number, date_of_birth, blood_type)
values (3, 'Charlie', 'Black', '159753486', 'charlie.black@example.com', 'P003', '2000-12-20', 4);
insert into visit (id, description, time, doctor_id, patient_id)
values (1, 'Routine checkup', '2024-11-20 10:00:00', 1, 1);
insert into visit (id, description, time, doctor_id, patient_id)
values (2, 'Follow-up appointment', '2024-11-21 14:30:00', 2, 2);
insert into visit (id, description, time, doctor_id, patient_id)
values (3, 'Dermatology consultation', '2024-11-22 09:15:00', 3, 3);
insert into medical_treatment (id, description, type, visit_id)
values (1, 'Blood Test', 'LAB_TEST', 1);
insert into medical_treatment (id, description, type, visit_id)
values (2, 'MRI Scan', 'IMAGING', 2);
insert into medical_treatment (id, description, type, visit_id)
values (3, 'Skin Biopsy', 'SURGICAL', 3);
insert into visit (id, description, time, doctor_id, patient_id)
values (4, 'Cardiology consultation', '2024-11-23 11:00:00', 1, 3);
insert into visit (id, description, time, doctor_id, patient_id)
values (5, 'Neurology evaluation', '2024-11-24 12:00:00', 2, 1);
insert into visit (id, description, time, doctor_id, patient_id)
values (6, 'Routine skin check', '2024-11-25 13:45:00', 3, 2);
insert into address (id, address_line1, address_line2, city, postal_code)
values (4, 'Main St', 'Building 3', 'City4', '33-333');
insert into doctor (id, first_name, last_name, telephone_number, email, doctor_number, specialization)
values (4, 'Emma', 'Taylor', '112233445', 'emma.taylor@example.com', 'D004', 'ORTHOPEDICS');
insert into patient (id, first_name, last_name, telephone_number, email, patient_number, date_of_birth)
values (4, 'Eve', 'Adams', '998877665', 'eve.adams@example.com', 'P004', '1992-06-18');
insert into visit (id, description, time, doctor_id, patient_id)
values (7, 'Orthopedic checkup', '2024-11-26 15:00:00', 4, 4);
insert into medical_treatment (id, description, type, visit_id)
values (4, 'X-Ray', 'IMAGING', 7);
insert into visit (id, description, time, doctor_id, patient_id)
values (8, 'Physical therapy session', '2024-11-27 16:30:00', 4, 3);
insert into doctor (id, first_name, last_name, telephone_number, email, doctor_number, specialization)
values (5, 'Liam', 'Clark', '223344556', 'liam.clark@example.com', 'D005', 'PEDIATRICS');