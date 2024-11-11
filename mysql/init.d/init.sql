DROP DATABASE IF EXISTS `concert`;

CREATE DATABASE `concert`;

USE `concert`;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `created_at` datetime(6) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `amount`;

CREATE TABLE `amount` (
  `amount` int(11) DEFAULT NULL COMMENT '사용자 잔액',
  `amount_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`amount_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `concert`;

CREATE TABLE `concert` (
   `total_seats` int(11) DEFAULT NULL COMMENT '콘서트에 배정된 최대 좌석 수',
   `concert_id` bigint(20) NOT NULL AUTO_INCREMENT,
   `created_at` datetime(6) DEFAULT NULL,
   `end_at` datetime(6) DEFAULT NULL,
   `start_at` datetime(6) DEFAULT NULL,
   `updated_at` datetime(6) DEFAULT NULL,
   `concert_name` varchar(255) DEFAULT NULL,
   `theater` varchar(255) DEFAULT NULL,
   PRIMARY KEY (`concert_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `concert_date`;

CREATE TABLE `concert_date` (
    `available_seats` int(11) DEFAULT NULL COMMENT '해당 날짜에 현재 이용 가능한 좌석 수',
    `concert_date` datetime(6) DEFAULT NULL,
    `concert_date_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `concert_id` bigint(20) DEFAULT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `status` varchar(255) DEFAULT NULL COMMENT '콘서트 날짜 상태(AVAILABLE(예약가능), FILLED(만석)',
    PRIMARY KEY (`concert_date_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `seat`;

CREATE TABLE `seat` (
    `ticket_price` int(11) DEFAULT NULL COMMENT '좌석별 티켓 가격',
    `version` int(11) DEFAULT NULL COMMENT '낙관적 락 버전',
    `concert_date_id` bigint(20) DEFAULT NULL,
    `concert_id` bigint(20) DEFAULT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `seat_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `updated_at` datetime(6) DEFAULT NULL,
    `seat_number` varchar(255) DEFAULT NULL,
    `status` varchar(255) DEFAULT NULL COMMENT '좌석 예약 상태 (TEMP_RESERVED(임시예약), EMPTY(빈 좌석), CONFIRMED(예약확정)',
    PRIMARY KEY (`seat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `reservation`;

CREATE TABLE `reservation` (
   `concert_date_id` bigint(20) DEFAULT NULL,
   `concert_id` bigint(20) DEFAULT NULL,
   `created_at` datetime(6) DEFAULT NULL,
   `reservation_id` bigint(20) NOT NULL AUTO_INCREMENT,
   `updated_at` datetime(6) DEFAULT NULL,
   `user_id` bigint(20) DEFAULT NULL,
   PRIMARY KEY (`reservation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `reservation_seatvvvvvv`;

CREATE TABLE `reservation_seat` (
    `created_at` datetime(6) DEFAULT NULL,
    `reservation_id` bigint(20) DEFAULT NULL,
    `reservation_seat_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `seat_id` bigint(20) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `user_id` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`reservation_seat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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

