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

-- 화장실 이름은 EXCEL 파일에 있는 지점명
-- 응급키트 이름은 EXCEL 파일에 있는 설치장소, 모름 데이터 제외하고 진행
-- ENUM(TOILET, MARKET, RENTAL, EMERGENCY_KIT)
INSERT INTO facility (mountain_id, name, type, latitude, longitude)
VALUES(1, '약사사', 'TOILET', 35.122349, 126.971828),
      (1, '문빈정사', 'TOILET', 35.13255928, 126.96163878),
      (1, '버스회차지공중화장실', 'TOILET', 35.133972, 126.955497),
      (1, '증심사지구공원 주차장 화장실', 'TOILET', 35.1335241500643, 126.95101658998),
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
      (1, '인왕봉', 'EMERGENCY_KIT', 35.1239592, 127.0068104)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- weather과 temperature은 추후 날씨 API를 받아와 구현합니다. 현재는 default값으로 hard coding 하였습니다.
-- 현재 24개 컬럼
-- 옛길갈림길, 용추삼거리, 도원마을 탐방 지원센터, 도원마을(영신마을)누락
INSERT INTO base (mountain_id, name, weather, temperature, latitude, longitude)
VALUES(1, '교리터널', '맑음', 25.0, 35.070906194893, 126.979223621632),
      (1, '규봉', '맑음', 25.0, 35.1185479, 127.0160852),
      (1, '규봉암', '맑음', 25.0, 35.1183113, 127.0161729),
      (1, '너릿재', '맑음', 25.0, 35.07795368, 126.9572814),
      (1, '늦재', '맑음', 25.0, 35.143218, 126.9820064),
      (1, '담양경상리', '맑음', 25.0, 35.1507622842581, 127.033206059354),
      (1, '당산나무', '맑음', 25.0, 35.1256944736997, 126.972125113781),
      (1, '동화사터', '맑음', 25.0, 35.13310602, 126.9854894),
      (1, '만연사', '맑음', 25.0, 35.0845540009366, 126.986567122924),
      (1, '무등산편백자연휴양림', '맑음', 25.0, 35.0921551744249, 127.02525066854),
      (1, '북산', '맑음', 25.0, 35.1411077, 127.0242756),
      (1, '새인봉', '맑음', 25.0, 35.12133025, 126.9655478),
      (1, '서석대', '맑음', 25.0, 35.12092492, 127.0026686),
      (1, '서인봉', '맑음', 25.0, 35.11732844, 126.9810868),
      (1, '시무지기폭포', '맑음', 25.0, 35.1281959, 127.0231746),
      (1, '안양산', '맑음', 25.0, 35.1008716, 127.0192404),
      (1, '원효사', '맑음', 25.0, 35.14848918, 126.9854474),
      (1, '원효사입구', '맑음', 25.0, 35.1477305979936, 126.985212424637),
      (1, '인계리', '맑음', 25.0, 35.1248164327792, 127.033642594001),
      (1, '장불재', '맑음', 25.0, 35.11612516, 126.9985684),
      (1, '중머리재', '맑음', 25.0, 35.11886432, 126.9843647),
      (1, '증심교', '맑음', 25.0, 35.1314293384178, 126.963405989889),
      (1, '증심사주차장', '맑음', 25.0, 35.1337932344148, 126.956774973978),
      (1, '토끼등', '맑음', 25.0, 35.13117145, 126.9779191)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- base_image는 mock data로서 각 baae마다 2개씩 존재합니다.
INSERT INTO base_image (base_id, image_url)
VALUES(1,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
      (1,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
      (2,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
      (2,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
      (3,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
      (3,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
      (4,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
      (4,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
      (5,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
      (5,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
      (6,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
      (6,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
      (7,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
      (7,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
      (8,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
      (8,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
      (9,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock1.png'),
      (9,  'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png'),
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
      (24, 'https://oasis-joa.s3.ap-northeast-2.amazonaws.com/soop/mock2.png')
    ON DUPLICATE KEY UPDATE image_url = VALUES(image_url);