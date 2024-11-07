INSERT INTO user (created_at, updated_at, user_name) VALUES (now(), now(), '예이츠');
INSERT INTO user (created_at, updated_at, user_name) VALUES (now(), now(), '셰익스피어');
INSERT INTO user (created_at, updated_at, user_name) VALUES (now(), now(), '워드워스');
INSERT INTO user (created_at, updated_at, user_name) VALUES (now(), now(), '루드비히');
INSERT INTO user (created_at, updated_at, user_name) VALUES (now(), now(), '쉐퍼');

INSERT INTO amount (amount, created_at, updated_at, user_id) VALUES (10000, now(), now(), 2);
INSERT INTO amount (amount, created_at, updated_at, user_id) VALUES (20000, now(), now(), 3);
INSERT INTO amount (amount, created_at, updated_at, user_id) VALUES (30000, now(), now(), 4);
INSERT INTO amount (amount, created_at, updated_at, user_id) VALUES (40000, now(), now(), 5);
INSERT INTO amount (amount, created_at, updated_at, user_id) VALUES (50000, now(), now(), 6);

INSERT INTO concert (total_seats, created_at, end_at, start_at, updated_at, concert_name, theater) VALUES (300, now(), '2024-12-31T00:00:00', '2024-10-31T00:00:00', now(), '조수미 콘서트', '국립극장');
INSERT INTO concert (total_seats, created_at, end_at, start_at, updated_at, concert_name, theater) VALUES (300, now(), '2024-12-31T00:00:00', '2024-10-31T00:00:00', now(), '조성진', '세종문화회관');
INSERT INTO concert (total_seats, created_at, end_at, start_at, updated_at, concert_name, theater) VALUES (300, now(), '2024-12-31T00:00:00', '2024-10-31T00:00:00', now(), '아이유 콘서트', '충무아트홀');
INSERT INTO concert (total_seats, created_at, end_at, start_at, updated_at, concert_name, theater) VALUES (300, now(), '2024-12-31T00:00:00', '2024-10-31T00:00:00', now(), '임영웅', '버스킹');
INSERT INTO concert (total_seats, created_at, end_at, start_at, updated_at, concert_name, theater) VALUES (300, now(), '2024-12-31T00:00:00', '2024-10-31T00:00:00', now(), '소향', '국립극장');

INSERT INTO concert_date (available_seats, concert_date, concert_id, created_at, updated_at, status) VALUES (100, '2024-11-10T00:00:00', 1, now(), now(), 'AVAILABLE');
INSERT INTO concert_date (available_seats, concert_date, concert_id, created_at, updated_at, status) VALUES (100, '2024-11-10T00:00:00', 2, now(), now(), 'AVAILABLE');
INSERT INTO concert_date (available_seats, concert_date, concert_id, created_at, updated_at, status) VALUES (100, '2024-11-11T00:00:00',  1, now(), now(), 'AVAILABLE');
INSERT INTO concert_date (available_seats, concert_date, concert_id, created_at, updated_at, status) VALUES (100, '2024-11-12T00:00:00', 4, now(), now(), 'AVAILABLE');

INSERT INTO seat (ticket_price, concert_date_id, concert_id, created_at, updated_at, seat_number, status) VALUES  (10000, 2, 1, now(), now(), '05', 'AVAILABLE');
INSERT INTO seat (ticket_price, concert_date_id, concert_id, created_at, updated_at, seat_number, status) VALUES  (10000, 2, 1, now(), now(), '06', 'AVAILABLE');
INSERT INTO seat (ticket_price, concert_date_id, concert_id, created_at, updated_at, seat_number, status) VALUES  (10000, 3, 1, now(), now(), '07', 'AVAILABLE');
INSERT INTO seat (ticket_price, concert_date_id, concert_id, created_at, updated_at, seat_number, status) VALUES  (10000, 4, 2, now(), now(), '08', 'AVAILABLE');
INSERT INTO seat (ticket_price, concert_date_id, concert_id, created_at, updated_at, seat_number, status) VALUES  (10000, 6, 4, now(), now(), '09', 'AVAILABLE');

