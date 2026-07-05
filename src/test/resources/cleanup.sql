-- E2E 테스트 메서드 간 격리용. 각 테스트 시작 전(BEFORE_TEST_METHOD)에 실행된다.
--
-- 동적(시나리오) 데이터만 비운다: 테스트가 코드로 생성하는 것들.
-- 마스터 데이터(mountain, facility, base, base_image, course, pathway, course_pathway_sequence)는
-- data-test.sql 시드이고 컨텍스트 시작 시 1회만 로드되므로 절대 삭제하지 않는다.
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE bookmark;
TRUNCATE TABLE travel_record;
TRUNCATE TABLE `user`;
SET FOREIGN_KEY_CHECKS = 1;
