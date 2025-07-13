-- 데이터 초기화 시 테이블 이름은 소문자로 해주세요
-- ex)mountain(O) Mountain(X)

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