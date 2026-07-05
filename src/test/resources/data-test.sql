-- ============================================================================
-- 테스트 전용 시드 데이터 (운영 src/main/resources/data.sql 미러)
--
-- 규칙:
-- 1. 운영 data.sql이 바뀌면 이 파일도 함께 갱신한다 (미러 유지).
-- 2. 여기 들어가는 건 정적 마스터 데이터만: mountain, facility, base, base_image, course.
--    사용자/북마크/산행기록 같은 시나리오 데이터는 각 테스트가 코드로 생성한다.
-- 3. 테스트 전용 데이터를 추가할 때는 아래 "테스트 전용 추가" 섹션에만 추가하고 주석으로 의도를 남긴다.
-- ============================================================================

-- mountain, facility, base, base_image, course 엔티티에 대한 데이터 초기화
-- 데이터 초기화 시 테이블 이름은 소문자로 해주세요
-- ex)mountain(O) Mountain(X)
-- 칼럼명은 camelCase가 아닌 snake_case로 해주세요

-- 무등산을 제외한 나머지 위도, 경도 값들은 목 데이터.
INSERT INTO mountain(name, initials, location, latitude, longitude)
values ('무등산', 'ㅁㄷㅅ', '광주', 35.13349412111848, 126.99068462647199),
       ('북한산', 'ㅂㅎㅅ', '서울', 38, 130),
       ('지리산', 'ㅈㄹㅅ', '전남', 38, 130),
       ('설악산', 'ㅅㅇㅅ', '강원도', 38, 130),
       ('한라산', 'ㅎㄹㅅ', '제주도', 38, 130),
       ('덕유산', 'ㄷㅇㅅ', '전북', 38, 130),
       ('태백산', 'ㅌㅂㅅ', '강원도', 38, 130),
       ('오대산', 'ㅇㄷㅅ', '강원도', 38, 130),
       ('소백산', 'ㅅㅂㅅ', '충북', 38, 130),
       ('가야산', 'ㄱㅇㅅ', '경남', 38, 130),
       ('월출산', 'ㅇㅊㅅ', '전남', 38, 130) AS new
ON DUPLICATE KEY
UPDATE
    name = new.name,
    latitude = new.latitude,
    longitude = new.longitude,
    initials = new.initials;


-- 화장실 이름은 EXCEL 파일에 있는 지점명
-- 응급키트 이름은 EXCEL 파일에 있는 설치장소, 모름 데이터 제외하고 진행
-- ENUM(TOILET, MARKET, RENTAL, EMERGENCY_KIT)
INSERT INTO facility (mountain_id, name, type, latitude, longitude)
VALUES (1, '약사사', 'TOILET', 35.122349, 126.971828),
       (1, '문빈정사', 'TOILET', 35.13255928, 126.96163878),
       (1, '버스회차지공중화장실', 'TOILET', 35.133972, 126.955497),
       (1, '증심사지구공원 주차장 화장실', 'TOILET', 35.1335241500643, 126.95101658998),
       (1, '원효광장', 'TOILET', 35.144569, 126.988857),
       (1, '원효사주차장', 'TOILET', 35.146095, 126.987135),
       (1, '원효계곡', 'TOILET', 35.142143, 126.987409),
       (1, '청풍쉼터', 'TOILET', 35.1624, 126.962502),
       (1, '당산나무', 'TOILET', 35.125826, 126.972376),
       (1, '용연', 'TOILET', 35.1004, 126.963778),
       (1, '증심사제2주차장', 'TOILET', 35.131918, 126.94789),
       (1, '장불재', 'TOILET', 35.11645, 126.998179),
       (1, '아이더', 'MARKET', 35.13323974, 126.9570313),
       (1, '무등산사무소', 'RENTAL', 35.1314823638451, 126.948157767535),
       (1, '원효분소', 'RENTAL', 35.1623003853538, 126.980276058801),
       (1, '무등산 군왕봉 정상', 'EMERGENCY_KIT', 35.17214791, 126.9497182),
       (1, '무등산 장불재', 'EMERGENCY_KIT', 35.11612516, 126.9985684),
       (1, '무등산 중머리재', 'EMERGENCY_KIT', 35.11886432, 126.9843647),
       (1, '무등산 새인봉 삼거리', 'EMERGENCY_KIT', 35.1195428211767, 126.972851249219),
       (1, '무등산 토끼등', 'EMERGENCY_KIT', 35.13117145, 126.9779191),
       (1, '무등산 중봉', 'EMERGENCY_KIT', 35.12265985, 126.9927127),
       (1, '무등산 증심사 당산나무', 'EMERGENCY_KIT', 35.1256944736997, 126.972125113781),
       (1, '무등산 늦재삼거리', 'EMERGENCY_KIT', 35.143218, 126.9820064),
       (1, '인왕봉', 'EMERGENCY_KIT', 35.1239592, 127.0068104) ON DUPLICATE KEY
UPDATE name =
VALUES (name);

-- weather과 temperature은 추후 날씨 API를 받아와 구현합니다. 현재는 default값으로 hard coding 하였습니다.
-- 총 28개 컬럼
INSERT INTO base (mountain_id, name, weather, temperature, geo_point, altitude)
VALUES (1, '교리터널', '맑음', 25.0, ST_GeomFromText('POINT(35.070906194893 126.979223621632)', 4326), 120),
       (1, '규봉암', '맑음', 25.0, ST_GeomFromText('POINT(35.1183113 127.0161729)', 4326), 828),
       (1, '너릿재', '맑음', 25.0, ST_GeomFromText('POINT(35.077089549970225 126.95648210924638)', 4326), 240),
       (1, '늦재', '맑음', 25.0, ST_GeomFromText('POINT(35.143169369692444 126.98197203529612)', 4326), 631),
       (1, '담양경상리', '맑음', 25.0, ST_GeomFromText('POINT(35.1507622842581 127.033206059354)', 4326), 311),
       (1, '당산나무', '맑음', 25.0, ST_GeomFromText('POINT(35.1256944736997 126.972125113781)', 4326), 294),
       (1, '동화사터', '맑음', 25.0, ST_GeomFromText('POINT(35.13311278480902 126.98549348553924)', 4326), 799),
       (1, '만연사', '맑음', 25.0, ST_GeomFromText('POINT(35.0845540009366 126.986567122924)', 4326), 220),
       (1, '무등산편백자연휴양림', '맑음', 25.0, ST_GeomFromText('POINT(35.0921551744249 127.02525066854)', 4326), 445),
       (1, '북산', '맑음', 25.0, ST_GeomFromText('POINT(35.1411077 127.0242756)', 4326), 762),
       (1, '새인봉', '맑음', 25.0, ST_GeomFromText('POINT(35.12133025 126.9655478)', 4326), 400),
       (1, '서석대', '맑음', 25.0, ST_GeomFromText('POINT(35.1209371 127.0027309)', 4326), 1098),
       (1, '서인봉', '맑음', 25.0, ST_GeomFromText('POINT(35.11732844 126.9810868)', 4326), 610),
       (1, '시무지기폭포', '맑음', 25.0, ST_GeomFromText('POINT(35.1272399 127.0258408)', 4326), 522),
       (1, '안양산', '맑음', 25.0, ST_GeomFromText('POINT(35.1008716 127.0192404)', 4326), 852),
       (1, '원효사입구', '맑음', 25.0, ST_GeomFromText('POINT(35.1477306332732 126.98512166936014)', 4326), 404),
       (1, '인계리', '맑음', 25.0, ST_GeomFromText('POINT(35.1248164327792 127.033642594001)', 4326), 352),
       (1, '장불재', '맑음', 25.0, ST_GeomFromText('POINT(35.116271104289275 126.99861163127287)', 4326), 894),
       (1, '중머리재', '맑음', 25.0, ST_GeomFromText('POINT(35.1190767 126.9844812)', 4326), 590),
       (1, '증심교', '맑음', 25.0, ST_GeomFromText('POINT(35.1314720337961 126.96334833116366)', 4326), 145),
       (1, '증심사주차장', '맑음', 25.0, ST_GeomFromText('POINT(35.133446586363135 126.95784494317081)', 4326), 115),
       (1, '토끼등', '맑음', 25.0, ST_GeomFromText('POINT(35.13108600606006 126.97777179326329)', 4326), 454),
       (1, '도원마을(도원탐방지원센터)', '맑음', 25.0, ST_GeomFromText('POINT(35.1145825326321 127.029387653321)', 4326), 350),
       (1, '도원마을(영신마을)', '맑음', 25.0, ST_GeomFromText('POINT(35.1195930318044 127.033604792924)', 4326), 350),
       (1, '용추삼거리', '맑음', 25.0, ST_GeomFromText('POINT(35.11941906644682 126.99410503013233)', 4326), 760),
       (1, '옛길갈림길', '맑음', 25.0, ST_GeomFromText('POINT(35.1207594 126.9979485)', 4326), 885),
       (1, '수레바위산', '맑음', 25.0, ST_GeomFromText('POINT(35.0948959 126.9884253)', 4326), 504),
       (1, '입석대', '맑음', 25.0, ST_GeomFromText('POINT(35.117550944 127.002572752)', 4326), 965) ON DUPLICATE KEY
UPDATE name =
VALUES (name);


-- base_image는 mock data로서 각 baae마다 2개씩 존재합니다.
INSERT INTO base_image (base_id, image_url)
VALUES (1, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (1, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (2, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (2, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (3, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (3, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (4, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (4, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (5, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (5, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (6, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (6, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (7, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (7, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (8, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (8, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (9, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (9, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (10, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (10, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (11, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (11, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (12, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (12, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (13, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (13, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (14, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (14, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (15, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (15, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (16, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (16, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (17, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (17, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (18, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (18, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (19, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (19, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (20, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (20, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (21, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (21, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (22, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (22, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (23, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (23, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (24, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (24, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (25, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (25, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (26, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (26, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (27, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (27, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
       (28, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
       (28, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png') ON DUPLICATE KEY
UPDATE image_url =
VALUES (image_url);

INSERT INTO course(mountain_id, name, length, duration, difficulty, image_url, display_name, peak_base_id,
                   destination_base_id)
VALUES (1, '새인봉-입석대 코스', 6.0, 215, 'NORMAL', 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/새인봉-입석대+코스.PNG',
        '새인봉 - 입석대', 28, 28),
       (1, '늦재-옛길 코스', 9.3, 256, 'NORMAL', 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/늦재-옛길+코스.png',
        '원효사 입구 - 장불재 - 원효사 입구', 18, 16),
       (1, '당산나무 코스', 3.2, 104, 'EASY', 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/당산나무+코스.png',
        '증심사 주차장 - 중머리재', 19, 19),
       (1, '시무지기폭포 코스', 10.1, 281, 'NORMAL', 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/시무지기폭포+코스.png',
        '증심교 - 장불재 - 인계리', 18, 17),
       (1, '너릿재-옛길코스', 14.4, 407, 'NORMAL', 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/너릿재-옛길+코스.png',
        '너릿재 - 서석대 - 원효사 입구', 12, 16),
       (1, '안양산-북산코스', 10.7, 324, 'NORMAL', 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/안양산+-+북산코스.PNG',
        '무등산편백자연휴양림 - 장불재 - 담양 경상리', 18, 5),
       (1, '도원마을-규봉코스', 6.9, 201, 'NORMAL', 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/도원마을+-+규봉코스.PNG',
        '도원탐방지원센터 - 장불재 - 영신마을', 18, 24),
       (1, '교리-만연산코스', 4.8, 158, 'NORMAL', 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/교리+-+만연산+코스.PNG',
        '교리 - 수레바위산 - 만연사', 27, 8) ON DUPLICATE KEY
UPDATE
    name =
VALUES (name), difficulty =
VALUES (difficulty), length =
VALUES (length), duration =
VALUES (duration), image_url =
VALUES (image_url), display_name =
VALUES (display_name), peak_base_id =
VALUES (peak_base_id);

-- ============================================================================
-- 경로(pathway) + 코스-경로 순서(course_pathway_sequence) 마스터
-- 운영은 크기 때문에 pathway_insert.sql로 별도 import. 여기는 테스트용 대표 서브셋.
-- 시드된 base(1~28)를 참조한 합성 데이터. WKT는 base 시드와 동일하게 '위도 경도' 순(SRID 4326).
-- course 1(새인봉-입석대)에 경로 3개를 sequence 1~3으로 연결: 증심사주차장(21)→새인봉(11)→서인봉(13)→입석대(28)
-- ============================================================================
INSERT INTO pathway (id, departure_id, destination_id, coordinates, length, duration, difficulty)
VALUES
    (1, 21, 11, ST_GeomFromText('LINESTRING(35.1334 126.9578, 35.1300 126.9600, 35.1213 126.9655)', 4326), 2.0, 60, 'NORMAL'),
    (2, 11, 13, ST_GeomFromText('LINESTRING(35.1213 126.9655, 35.1190 126.9750, 35.1173 126.9810)', 4326), 1.5, 45, 'NORMAL'),
    (3, 13, 28, ST_GeomFromText('LINESTRING(35.1173 126.9810, 35.1170 126.9950, 35.1175 127.0025)', 4326), 1.8, 50, 'HARD')
ON DUPLICATE KEY UPDATE
    departure_id = VALUES(departure_id),
    destination_id = VALUES(destination_id),
    coordinates = VALUES(coordinates),
    length = VALUES(length),
    duration = VALUES(duration),
    difficulty = VALUES(difficulty);

INSERT INTO course_pathway_sequence (id, course_id, pathway_id, `sequence`)
VALUES
    (1, 1, 1, 1),
    (2, 1, 2, 2),
    (3, 1, 3, 3)
ON DUPLICATE KEY UPDATE
    course_id = VALUES(course_id),
    pathway_id = VALUES(pathway_id),
    `sequence` = VALUES(`sequence`);

-- ============================================================================
-- 테스트 전용 추가 (운영 미러 아님 — 추가 시 의도 주석 필수)
-- ============================================================================
