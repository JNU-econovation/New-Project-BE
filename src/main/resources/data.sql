INSERT INTO mountain(name, location)
values ('무등산', '광주'),
       ('북한산', '서울'),
       ('지리산', '전남'),
       ('설악산', '강원도'),
       ('한라산', '제주도'),
       ('덕유산', '전북'),
       ('태백산', '강원도'),
       ('오대산', '강원도'),
       ('소백산', '충북'),
       ('가야산', '경남'),
       ('월출산', '전남')
ON DUPLICATE KEY update name = VALUES(name);

INSERT INTO course(mountain_id, name, length, duration, difficulty)
VALUES (1, '코스1', 6.7, 180, 'NORMAL'),
       (2, '코스2', 4.3, 120, 'EASY'),
       (3, '코스3', 9.5, 300, 'HARD'),
       (1, '코스4', 3.8, 90, 'EASY'),
       (2, '코스5', 11.2, 330, 'HARD'),
       (3, '코스6', 5.0, 160, 'NORMAL'),
       (1, '코스7', 7.6, 220, 'NORMAL'),
       (2, '코스8', 6.1, 150, 'EASY'),
       (3, '코스9', 10.8, 340, 'HARD'),
       (1, '코스10', 4.4, 100, 'HARD'),
       (2, '코스11', 8.9, 280, 'HARD'),
       (3, '코스12', 3.3, 80, 'NORMAL'),
       (1, '코스13', 7.0, 200, 'NORMAL'),
       (2, '코스14', 9.7, 310, 'HARD'),
       (3, '코스15', 5.9, 170, 'EASY'),
       (1, '코스16', 2.5, 60, 'EASY'),
       (2, '코스17', 6.8, 190, 'NORMAL'),
       (3, '코스18', 11.5, 350, 'HARD'),
       (1, '코스19', 4.9, 130, 'NORMAL'),
       (2, '코스20', 8.2, 260, 'HARD')
ON DUPLICATE KEY update name = VALUES(name);
