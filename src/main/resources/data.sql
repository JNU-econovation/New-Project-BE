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
VALUES (1, '세인봉-입석대 코스', 6.8, 210, 'NORMAL'),
       (1, '늦재-옛길 코스', 10.4, 350, 'NORMAL'),
       (1, '당산나무 코스', 4, 95, 'NORMAL'),
       (1, '시무지기폭포 코스', 12, 355, 'NORMAL'),
       (1, '너릿재-옛길코스', 14.5, 440, 'NORMAL'),
       (1, '안양산-북산 코스', 14.6, 420, 'NORMAL'),
       (1, '도원마을-규봉 코스', 7.5, 270, 'NORMAL'),
       (1, '교리-만연산 코스', 7.5, 240, 'NORMAL')
ON DUPLICATE KEY update name = VALUES(name);
