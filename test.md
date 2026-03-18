# 테스트 작성 계획

## 1. 이 문서 읽는 법

이 문서는 두 축으로 읽는다.

- 분류 체계
  - 테스트를 어떤 종류로 나눌지 정리한 기준
  - 예: 순수 단위 테스트, 서비스 단위 테스트, WebMvc 테스트
- 실행 전략
  - 실제로 어떤 도메인을 먼저 끝까지 밀고 갈지 정리한 작업 순서
  - 이 프로젝트는 도메인별 세로 슬라이스 방식으로 진행한다
- 작업 배치
  - 특정 `Slice`와 `Phase` 안에서 이번 턴에 실제로 처리한 실행 단위
  - 문서 구조가 아니라 진행 기록을 위한 보조 단위다

즉, 이 문서의 의미는 아래와 같다.

- 테스트는 `Phase 1 -> Phase 2 -> ... -> Phase 6` 순서로 확장한다.
- 하지만 실제 작성은 도메인 하나를 잡고 그 도메인 안에서 단위 -> 서비스 -> 계약 -> 저장소 -> 통합으로 내려간다.
- 실제 진행 기록은 `Slice > Phase > 작업 배치` 순서로 남긴다.

## 2. 체크박스 규칙

- `[ ]` 아직 시작 안 함
- `[x]` 완료

이 문서는 작업용 체크리스트로 쓴다.

## 3. 현재 상태

- [x] `travel`, `auth`, `user`, `common`, `course`, `pathway`, `mountain`, `base`, `facility` 테스트 작성됨
- [x] WebMvc 테스트 작성됨
- [x] WebSocket 테스트 작성됨
- [x] Repository/Querydsl 테스트 존재 (`travel`, `course`)
- [x] Testcontainers 기반 MySQL 저장소 테스트 존재
- [x] `NewProjectApplicationTests` 존재
- [ ] Redis 전용 테스트 없음
- [ ] 외부 API 연동 테스트 없음
- [ ] 기능 단위 통합 테스트(Phase 6) 없음

## 3-1. 리뷰 결과 요약

- Testcontainers 환경에서 `@DataJpaTest` 컨텍스트 캐시 재사용 시 DB 연결이 끊기는 문제가 있어 `@DirtiesContext(AFTER_CLASS)`로 안정화함.
- `Base.geo_point`의 unique 제약으로 MySQL DDL 경고가 발생하므로, 추후 저장소 테스트 확장 시 스키마 생성 로그를 확인할 것.
- Repository/Querydsl 테스트는 현재 `travel`, `course`만 작성되어 있어 다른 도메인은 아직 공백.
- Redis 전용 테스트, 외부 API stub 기반 테스트, Phase 6 기능 통합 테스트는 미진행.

## 4. 분류 체계

### Phase 1. 순수 단위 테스트

Spring context 없이 검증 가능한 엔티티, VO, DTO 변환, 유틸, 정책 객체를 테스트한다.

### Phase 2. 목 기반 서비스 단위 테스트

Repository, Redis, Feign, S3, WebSocket 세션 등을 mock 처리하고 서비스 로직을 테스트한다.

### Phase 3. WebMvc / WebSocket 계약 테스트

HTTP API 계약과 WebSocket 메시지 계약, 예외 응답 구조를 고정한다.

### Phase 4. JPA / Querydsl 테스트

JPA 매핑, Querydsl 정렬, 조회 쿼리, 저장소 동작을 검증한다.

### Phase 5. Redis / MySQL / Testcontainers 테스트

Redis와 MySQL spatial 의존 구간을 실제 인프라와 유사하게 검증한다.

### Phase 6. 기능 단위 통합 테스트

HTTP API 흐름과 WebSocket 이벤트 흐름을 레이어 관통으로 검증한다.

## 5. 실행 전략

이 프로젝트는 “모든 도메인의 단위 테스트를 먼저 다 끝낸 뒤, 그 다음 서비스 테스트를 전부 한다” 방식보다,
도메인 하나를 먼저 잡고 그 도메인 안에서 테스트 단위를 확장하는 방식이 더 적합하다.

권장 작업 순서는 아래다.

1. `travel + websocket + spatial query`
2. `auth + user + common`
3. `course + pathway`
4. `mountain + base + facility`

이 순서인 이유:

- 작성 시작 당시 `travel`은 신규 코드가 많고 테스트가 거의 없는 상태였다.
- 상태 전이, WebSocket, in-memory store, geometry 계산, MySQL spatial query가 한 흐름에 묶여 있다.
- 수동 검증 비용이 높다.
- 이미 코드상 리스크가 보인다.

따라서 실제 작업 방식은 이렇게 가져간다.

- `travel`을 먼저 잡는다.
- `travel`의 순수 단위 테스트를 쓴다.
- 바로 이어서 `travel` 서비스 단위 테스트를 쓴다.
- 그 다음 `travel`의 controller/WebSocket 계약 테스트를 쓴다.
- 그 다음 `travel` 저장소/MySQL spatial 테스트를 쓴다.
- 마지막으로 `travel` 기능 통합 테스트를 붙인다.
- 그 다음 도메인으로 `auth + user`로 넘어간다.

## 6. 공통 인프라 준비 체크리스트

- [x] `build.gradle`에 `testcontainers-junit-jupiter` 추가
- [x] `build.gradle`에 `testcontainers-mysql` 추가
- [x] Redis Testcontainers 전략 확정
- [x] `build.gradle`에 `WireMock` 또는 `MockWebServer` 추가
- [x] `src/test/resources/application-test.yml` 작성
- [x] MySQL Testcontainers 공통 베이스 클래스 준비
- [x] Redis Testcontainers 공통 베이스 클래스 준비
- [ ] WebSocket 테스트용 공통 helper 준비

### 6-1. 외부 의존성 테스트 원칙

- `Phase 1 ~ Phase 3`에서는 Redis, S3, SMS, OAuth, OpenWeather 같은 외부 의존성을 mock으로 끊고 로직/계약만 검증한다.
- `Phase 5`에서는 Redis는 Testcontainers, 외부 HTTP 연동은 `WireMock` 또는 `MockWebServer` 기반 stub 서버로 검증한다.
- 프로필 분리와 `application-test.yml` 정리는 완료됐고, `@SpringBootTest`는 Testcontainers 기반 DB/Redis 주입으로 동작한다.
- Testcontainers 기반 테스트 실행 전에는 Docker Desktop 또는 호환 Docker runtime이 반드시 실행 중이어야 한다.
- 실제 네트워크 호출을 테스트에서 직접 사용하지 않는다.

## 7. Slice 1: travel + websocket + spatial query

### 7-1. Phase 1 체크리스트

- [x] `TravelEvent`
- [x] `Status`
- [x] `EventPolicy`
- [x] `GeoUtil`
- [x] `DateUtil`
- [x] `TimeMapper`
- [x] `TravelTrackingInfo`
- [x] `RemainingTime`
- [x] `TravelMapper`
- [x] `PayloadMapper`
- [x] `TravelResponseMapper`
- [x] `TravelDistanceCalculator`
- [x] `CourseLocationMatcher`
- [x] `ClosestCoordinateInfo`
- [x] `TravelRecordDTO`
- [x] `TravelRecordDetailDTO`
- [x] `GetTravelRecordByMonthRes`
- [x] `GetTravelRecordDetailRes`
- [x] `TravelEventResponseData`

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 1 > Phase 1 > 작업 배치 1` 범위를 처리했다.
- 추가한 테스트 파일:
  - `TravelEventTest`
  - `EventPolicyTest`
  - `GeoUtilTest`
  - `TravelDistanceCalculatorTest`
  - `CourseLocationMatcherTest`
  - `DateUtilTest`
  - `TimeMapperTest`
- 검증한 내용:
  - 이벤트 이름 매핑과 잘못된 이벤트 예외
  - 상태별 허용 이벤트 정책
  - 좌표 -> `Point` / `LineString` 변환과 SRID
  - 이전 위치 null 처리, km/m 거리 계산, 남은 거리 계산, 목표 지점 통과 시 0 처리
  - 가장 가까운 코스 좌표 인덱스 계산과 빈 배열 예외
  - 월 시작/종료 시각 계산
  - epoch millis -> `Asia/Seoul` 기준 `LocalDateTime` 변환
- 실행 결과:
  - `./gradlew test --tests '*TravelEventTest' --tests '*EventPolicyTest' --tests '*GeoUtilTest' --tests '*TravelDistanceCalculatorTest' --tests '*CourseLocationMatcherTest' --tests '*DateUtilTest' --tests '*TimeMapperTest'` 통과

</details>

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 1 > Phase 1 > 작업 배치 2` 범위를 처리했다.
- 추가한 테스트 파일:
  - `TravelTrackingInfoTest`
  - `TravelMapperTest`
  - `TravelResponseMapperTest`
  - `PayloadMapperTest`
- 검증한 내용:
  - 산행 상태 객체의 상태 전이와 경로/거리/시간 갱신
  - 기본 산행정보 생성과 산행기록 매핑
  - start/current/pause/restart/end/keep-alive 응답 매핑
  - WebSocket JSON 메시지 -> `Payload` 파싱
  - `Payload.data` -> 요청 DTO 변환
- 실행 결과:
  - `./gradlew test --tests '*TravelTrackingInfoTest' --tests '*TravelMapperTest' --tests '*TravelResponseMapperTest' --tests '*PayloadMapperTest' --tests '*WebSocketAuthServiceTest'` 통과

</details>

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 1 > Phase 1 > 작업 배치 3` 범위를 처리했다.
- 추가한 테스트 파일:
  - `StatusTest`
  - `RemainingTimeTest`
  - `ClosestCoordinateInfoTest`
  - `TravelRecordDTOTest`
  - `TravelRecordDetailDTOTest`
  - `GetTravelRecordByMonthResTest`
  - `GetTravelRecordDetailResTest`
  - `TravelEventResponseDataTest`
- 같이 수정한 코드:
  - `TravelRecordDetailDTO.from()`의 `endAt` 매핑 수정
- 검증한 내용:
  - 상태 enum 정의와 `valueOf`
  - 남은시간 VO 생성/갱신
  - 가장 가까운 좌표 정보 record 저장
  - 월별/상세 산행기록 DTO 변환
  - 좌표 배열과 상세 응답 변환
  - 산행 이벤트 응답 데이터 builder
- 실행 결과:
  - `./gradlew test --tests '*StatusTest' --tests '*RemainingTimeTest' --tests '*ClosestCoordinateInfoTest' --tests '*TravelRecordDTOTest' --tests '*TravelRecordDetailDTOTest' --tests '*GetTravelRecordByMonthResTest' --tests '*GetTravelRecordDetailResTest' --tests '*TravelEventResponseDataTest' --tests '*TravelRecordControllerTest'` 통과

</details>

### 7-2. Phase 2 체크리스트

- [x] `TravelTrackingInfoStore`
- [x] `RemainingTimeCalculator`
- [x] `TravelDomainService`
- [x] `TravelRecordService`
- [x] `WebSocketAuthService`
- [x] `TravelService`

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 1 > Phase 2 > 작업 배치 1` 범위를 처리했다.
- 추가한 테스트 파일:
  - `WebSocketAuthServiceTest`
- 검증한 내용:
  - 인증 이벤트 처리 후 세션 인증 상태 저장
  - 인증 실패 시 세션 미저장
  - 세션 제거 시 인증 정보 삭제
- 실행 결과:
  - `./gradlew test --tests '*TravelTrackingInfoTest' --tests '*TravelMapperTest' --tests '*TravelResponseMapperTest' --tests '*PayloadMapperTest' --tests '*WebSocketAuthServiceTest'` 통과

</details>

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 1 > Phase 2 > 작업 배치 2` 범위를 처리했다.
- 추가한 테스트 파일:
  - `TravelDomainServiceTest`
  - `TravelRecordServiceTest`
  - `TravelServiceTest`
- 검증한 내용:
  - 가장 가까운 좌표, 남은시간, 도착여부, 이탈여부, 누적거리 계산
  - 월별 기록 조회, 기록 저장, 상세 조회, 미존재 예외, 삭제
  - `start`, `current-position`, `pause`, `keep-alive`, `restart`, `end` 이벤트 라우팅
  - 허용되지 않은 이벤트 예외
  - 종료 후 기록 저장 및 tracking info 삭제
  - 남아있는 tracking info 정리
- 실행 결과:
  - `./gradlew test --tests '*TravelDomainServiceTest' --tests '*TravelRecordServiceTest' --tests '*TravelServiceTest'` 통과

</details>

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 1 > Phase 2 > 작업 배치 3` 범위를 처리했다.
- 추가한 테스트 파일:
  - `TravelTrackingInfoStoreTest`
  - `RemainingTimeCalculatorTest`
- 검증한 내용:
  - 산행 정보 생성, 상태 조회, 현재위치/일시정지/재시작/종료 업데이트, 삭제
  - 미존재 tracking info 조회 예외
  - 남은거리 기반 경유지/도착지 남은시간 계산
  - 남은거리 0초 처리와 코스 조회 실패 예외
- 실행 결과:
  - `./gradlew test --tests '*TravelTrackingInfoStoreTest' --tests '*RemainingTimeCalculatorTest'` 통과

</details>

### 7-3. Phase 3 체크리스트

- [x] `TravelRecordController`
- [x] `WebSocketHandler`
- [x] `WebSocketResponser`
- [x] `WebSocketConfig`

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Phase 3` 계약 테스트 범위를 처리했다.
- 추가한 테스트 파일:
  - `TravelRecordControllerTest`
  - `WebSocketHandlerTest`
  - `WebSocketResponserTest`
  - `WebSocketConfigTest`
- 검증한 내용:
  - 월별 기록 조회, 상세 조회, 삭제 API 응답 구조와 예외 응답 구조
  - WebSocket 인증 이벤트 처리, 미인증 세션 종료, 실시간 이벤트 위임, 예외별 실패 응답
  - WebSocket 성공/실패 메시지 JSON 직렬화
  - `/travel-navigate` 핸들러 등록과 허용 origin 설정
- 실행 결과:
  - `./gradlew test --tests '*TravelRecordControllerTest' --tests '*WebSocketHandlerTest' --tests '*WebSocketResponserTest' --tests '*WebSocketConfigTest'` 통과

</details>

### 7-4. Phase 4 체크리스트

- [x] `TravelRecordRepository`
- [x] `CourseRepository.isUserArrivedDestination()` (`travel` 흐름에서 사용하는 spatial query 선검증)

### 7-5. Phase 5 체크리스트

- [x] MySQL Testcontainers로 `TravelRecord` geometry 저장/조회 검증
- [x] MySQL Testcontainers로 `ST_Distance_Sphere` 검증
- [ ] WebSocket 인증 시나리오 테스트 환경 구성

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 1 > Phase 4/5 > 작업 배치 1` 범위를 처리했다.
- 추가한 테스트 파일:
  - `TravelRecordRepositoryTest`
  - `CourseRepositoryTest`
- 검증한 내용:
  - `TravelRecordRepository`의 기간/사용자 조회와 소유권 조회
  - `CourseRepository.isUserArrivedDestination()`의 `ST_Distance_Sphere` 결과
- 실행 결과:
  - `./gradlew test --tests '*TravelRecordRepositoryTest' --tests '*CourseRepositoryTest'`
  - 현재는 `@DirtiesContext(AFTER_CLASS)` 적용 후 정상 통과
  - 초기 시도에서는 Docker 미실행 및 컨텍스트 재사용 이슈로 MySQL 연결 실패가 있었음

</details>

### 7-6. Phase 6 체크리스트

- [ ] 월별 산행 기록 조회 API
- [ ] 산행 기록 상세 조회 API
- [ ] 산행 기록 삭제 API
- [ ] WebSocket `auth-user`
- [ ] WebSocket `start`
- [ ] WebSocket `current-position`
- [ ] WebSocket `pause`
- [ ] WebSocket `keep-alive`
- [ ] WebSocket `restart`
- [ ] WebSocket `end`
- [ ] 종료 후 `TravelRecord` 저장 검증
- [ ] 종료 후 tracking info 제거 검증

## 8. Slice 2: auth + user + common

### 8-1. Phase 1 체크리스트

- [x] `JwtTokenProvider` 보강
- [x] `AuthToken`
- [x] `AppleJwtHandler`
- [x] `ApplePublicKeyGenerator`
- [x] `SmsUtilService`
- [x] `AppleLoginReq`
- [x] `AppleLoginRes`
- [x] `SendSmsRes`
- [x] `VerifySmsRes`
- [x] `KaKaoUserInfoRes`
- [x] `BloodType`
- [x] `User`
- [x] `UserInfo`
- [x] `PhysicalInfo`
- [x] `UserAlert`
- [x] `UserMapper`
- [x] `UserProfileDTO`
- [x] `UserAlertSettingDTO`
- [x] `ProfileStatusInfoDTO`
- [x] `GetProfileRes`
- [x] `GetProfileStatusRes`
- [x] `GetAlertSettingRes`
- [x] `GetRandomNicknameRes`
- [x] `GetNicknameAvailabilityRes`
- [x] `RedirectUriBuilder`
- [x] `HttpHeadersGenerator`
- [x] `ApiResponse`
- [x] `ValidEmailPatternValidator`
- [x] `JwtInterceptor`
- [x] `UserIdResolver`
- [x] `NicknamePoolInitializer`
- [x] `GeometryConfig`
- [x] `WebSocketSuccessRes`
- [x] `WebSocketFailRes`

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 2 > Phase 1 > 작업 배치 1` 범위를 처리했다.
- 추가한 테스트 파일:
  - `AuthDtoTest`
  - `AuthTokenTest`
  - `BloodTypeTest`
  - `UserInfoTest`
  - `PhysicalInfoTest`
  - `UserAlertTest`
  - `UserTest`
  - `UserMapperTest`
  - `UserProfileDTOTest`
  - `UserResponseDtoTest`
  - `RedirectUriBuilderTest`
  - `HttpHeadersGeneratorTest`
  - `ApiResponseTest`
  - `ValidEmailPatternValidatorTest`
  - `GeometryConfigTest`
  - `WebSocketResponseDtoTest`
- 검증한 내용:
  - Apple/Kakao 요청·응답 DTO 변환
  - 토큰 record 생성
  - 혈액형 파싱과 잘못된 값 예외
  - `User`, `UserInfo`, `PhysicalInfo`, `UserAlert` 상태 변경
  - `UserMapper`, `UserProfileDTO`, 프로필/알림 응답 DTO 변환
  - redirect URI, Location 헤더, API 응답 body 생성
  - 이메일 정규식 검증, GeometryFactory SRID, WebSocket 성공/실패 응답

</details>

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 2 > Phase 1 > 작업 배치 2` 범위를 처리했다.
- 추가한 테스트 파일:
  - `AppleJwtHandlerTest`
  - `ApplePublicKeyGeneratorTest`
  - `SmsUtilServiceTest`
  - `JwtInterceptorTest`
  - `UserIdResolverTest`
  - `NicknamePoolInitializerTest`
- 같이 보강한 테스트 파일:
  - `JwtTokenProviderTest`
- 검증한 내용:
  - Apple 토큰 헤더 파싱, claim 검증, 공개키 선택/생성
  - SMS 메시지 생성, 발송 실패 예외, 인증번호/전화번호 정규화
  - JWT interceptor의 preflight, reissue, 로그인 요청 분기
  - `@UserId` argument resolver 동작과 토큰 누락 예외
  - Redis 준비 확인, 닉네임 풀 시작 위치 초기화, recover 예외 변환
  - `JwtTokenProvider` 헤더 파싱 예외와 refresh token 서명 검증 보강

</details>

### 8-2. Phase 2 체크리스트

- [x] `AuthTokenService` 보강
- [x] `OAuthService`
- [x] `SmsService`
- [x] `KaKaoOAuthService`
- [x] `AppleOAuthService`
- [x] `UserService`
- [x] `S3Service` 보강
- [x] `RandomNicknameService`
- [x] `RandomNicknamePoolService`
- [x] `RandomNicknamePoolManager`

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 2 > Phase 2 > 작업 배치 1` 범위를 처리했다.
- 추가한 테스트 파일:
  - `SmsServiceTest`
  - `KaKaoOAuthServiceTest`
  - `AppleOAuthServiceTest`
  - `OAuthServiceTest`
- 보강한 테스트 파일:
  - `AuthTokenServiceTest`
- 같이 수정한 코드:
  - `AppleOAuthService.isOurServiceAudience()`
- 검증한 내용:
  - refresh token 저장/재발급/로그아웃/로그인상태 검증
  - SMS 인증번호 저장/검증/삭제 흐름
  - Kakao 로그인 URI 생성과 access token -> user info 조회 흐름
  - Apple 공개키/claim 기반 user info 조회와 issuer/audience 검증
  - 상위 `OAuthService`의 Kakao/Apple 로그인, logout, reissue 위임
  - `Claims.getAudience()`를 `Set<String>`로 해석해 `client_id` 포함 여부로 검증하도록 수정

</details>

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 2 > Phase 2 > 작업 배치 2` 범위를 처리했다.
- 추가한 테스트 파일:
  - `UserServiceTest`
  - `RandomNicknameServiceTest`
  - `RandomNicknamePoolServiceTest`
  - `RandomNicknamePoolManagerTest`
- 보강한 테스트 파일:
  - `S3ServiceTest`
- 검증한 내용:
  - 유저 조회/생성, 기본정보·개인정보 등록, 프로필/알림/파일명 수정
  - 랜덤 닉네임 접두사 결합, Redis set pop/size/add 위임
  - 닉네임 풀 부족 시 2000개 suffix 생성과 start 위치 갱신
  - presigned URL, 기본/사용자 프로필 이미지 조회, 이미지 저장/삭제 예외 분기

</details>

### 8-3. Phase 3 체크리스트

- [x] `OAuthController`
- [x] `SmsController`
- [x] `UserController`
- [x] `ImageController`
- [x] `GlobalExceptionHandler`
- [x] `WebConfig`

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 2 > Phase 3 > 작업 배치 1` 범위를 처리했다.
- 추가한 테스트 파일:
  - `OAuthControllerTest`
  - `SmsControllerTest`
  - `UserControllerTest`
  - `ImageControllerTest`
- 검증한 내용:
  - Kakao/Apple 로그인, logout, reissue API 응답 계약
  - SMS 발송/검증 요청과 validation 오류 응답
  - 프로필/알림/닉네임/이미지 관련 API 응답 구조
  - `Location` 헤더, `@UserId` custom resolver, query/body validation contract

</details>

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 2 > Phase 3 > 작업 배치 2` 범위를 처리했다.
- 추가한 테스트 파일:
  - `GlobalExceptionHandlerTest`
  - `WebConfigTest`
- 검증한 내용:
  - validation/auth/internal/unexpected/request-param 예외의 공통 응답 구조
  - 인증 예외 시 redirect header 부여
  - `UserIdResolver` 등록, JWT interceptor 등록/제외 경로, CORS 매핑 등록

</details>

### 8-4. Phase 4 체크리스트

- [ ] `UserRepository`

### 8-5. Phase 5 체크리스트

- [ ] `SmsRepository` Redis 통합 테스트
- [ ] `RefreshTokenRepository` Redis 통합 테스트
- [ ] `BlacklistTokenRepository` Redis 통합 테스트
- [ ] `RandomNicknamePoolService` Redis 통합 테스트
- [ ] Kakao OAuth stub 통합 테스트
- [ ] Apple OAuth stub 통합 테스트
- [ ] CoolSMS stub 통합 테스트

### 8-6. Phase 6 체크리스트

- [ ] Kakao 로그인 흐름
- [ ] Apple 로그인 흐름
- [ ] refresh token 재발급 흐름
- [ ] logout 흐름
- [ ] SMS 인증번호 발송 흐름
- [ ] SMS 인증번호 검증 흐름
- [ ] 프로필 조회/수정 흐름
- [ ] 기본정보 등록 흐름
- [ ] 개인정보 등록 흐름
- [ ] 알림 조회/수정 흐름
- [ ] 랜덤 닉네임 조회 흐름
- [ ] 프로필 이미지 흐름

## 9. Slice 3: course + pathway

### 9-1. Phase 1 체크리스트

- [x] `CourseSearchCondition`
- [x] `CourseWithBookmarkDTO`
- [x] `BookmarkMapper`
- [x] `PathwayMapper`
- [x] `PathwayCoordinatesArrayDTO`

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 3 > Phase 1 > 작업 배치 1` 범위를 처리했다.
- 추가한 테스트 파일:
  - `CourseSearchConditionTest`
  - `CourseWithBookmarkDTOTest`
  - `BookmarkMapperTest`
  - `PathwayMapperTest`
  - `PathwayCoordinatesArrayDTOTest`
- 검증한 내용:
  - 코스 검색조건 생성과 null 입력 유지
  - 코스 + 북마크 여부 기반 DTO 매핑
  - 사용자 / 코스 기반 북마크 엔티티 생성
  - 등산로 엔티티 -> DTO 변환
  - `LineString` -> 좌표 배열 응답 변환
- 실행 결과:
  - `./gradlew test --tests '*CourseSearchConditionTest' --tests '*CourseWithBookmarkDTOTest' --tests '*BookmarkMapperTest' --tests '*PathwayMapperTest' --tests '*PathwayCoordinatesArrayDTOTest'` 통과

</details>

### 9-2. Phase 2 체크리스트

- [x] `CourseService`
- [x] `BookmarkService`
- [x] `CoursePathwaySequenceService`
- [x] `PathwayService`

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 3 > Phase 2 > 작업 배치 1` 범위를 처리했다.
- 추가한 테스트 파일:
  - `CourseServiceTest`
  - `BookmarkServiceTest`
  - `CoursePathwaySequenceServiceTest`
  - `PathwayServiceTest`
- 검증한 내용:
  - 코스 조회 성공 / 실패
  - 코스 목록 조회 시 검색조건 생성과 repository 위임
  - 코스 상세 조회와 예외
  - 경로 flatten 후 가장 가까운 좌표 계산 위임
  - 도착여부 `1/0` -> `true/false` 변환
  - 북마크 추가 / 삭제 / 미존재 예외 / 목록 조회
  - 코스별 sequence 조회 위임
  - sequence -> 등산로 DTO 목록 변환과 빈 목록 처리
- 실행 결과:
  - `./gradlew test --tests '*CourseServiceTest' --tests '*BookmarkServiceTest' --tests '*CoursePathwaySequenceServiceTest' --tests '*PathwayServiceTest'` 통과

</details>

### 9-3. Phase 3 체크리스트

- [x] `CourseController`
- [x] `BookmarkController`
- [x] `PathwayController`

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 3 > Phase 3 > 작업 배치 1` 범위를 처리했다.
- 추가한 테스트 파일:
  - `CourseControllerTest`
  - `BookmarkControllerTest`
  - `PathwayControllerTest`
- 검증한 내용:
  - 코스 목록 조회 응답 구조와 정렬 파라미터 전달
  - 코스 상세 조회 성공 / 미존재 예외 응답
  - 북마크 추가 / 삭제 / 목록 조회 응답 구조
  - 북마크 추가 요청 검증 실패와 북마크 미존재 예외 응답
  - 경로 조회 응답 구조와 필수 쿼리스트링 누락 예외 응답
- 실행 결과:
  - `./gradlew test --tests '*CourseControllerTest' --tests '*BookmarkControllerTest' --tests '*PathwayControllerTest'` 통과

</details>

### 9-4. Phase 4 체크리스트

- [ ] `BookmarkRepository`
- [ ] `CourseCustomRepositoryImpl`
- [ ] `CoursePathwaySequenceCustomRepositoryImpl`
- [ ] `PathwayRepository`

### 9-5. Phase 5 체크리스트

- [ ] `CourseRepository` native query 보강 테스트
- [ ] Querydsl 정렬 smoke 테스트

### 9-6. Phase 6 체크리스트

- [ ] 코스 목록 조회 흐름
- [ ] 코스 상세 조회 흐름
- [ ] 즐겨찾기 등록/삭제/목록 흐름
- [ ] 경로 조회 흐름

## 10. Slice 4: mountain + base + facility

### 10-1. Phase 1 체크리스트

- [x] `MountainDTO`
- [x] `SuggestedMountainDTO`
- [x] `FacilityDTO`
- [x] `BaseDTO`
- [x] `BaseDetailDTO`
- [x] `CourseDetailDTO`
- [x] `WeatherRes`

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 4 > Phase 1 > 작업 배치 1` 범위를 처리했다.
- 추가한 테스트 파일:
  - `MountainDTOTest`
  - `SuggestedMountainDTOTest`
  - `FacilityDTOTest`
  - `BaseDTOTest`
  - `BaseDetailDTOTest`
  - `CourseDetailDTOTest`
  - `WeatherResTest`
- 검증한 내용:
  - 산/시설 좌표 응답의 경도-위도 순서
  - 베이스 `Point` -> 좌표 배열 변환
  - 베이스 상세 응답의 이미지 목록과 `recommendedOutfit` 기본값
  - 코스 상세 DTO 변환
  - OpenWeather 응답의 첫 번째 날씨와 온도 추출
- 실행 결과:
  - `./gradlew test --tests '*MountainDTOTest' --tests '*SuggestedMountainDTOTest' --tests '*FacilityDTOTest' --tests '*BaseDTOTest' --tests '*BaseDetailDTOTest' --tests '*CourseDetailDTOTest' --tests '*WeatherResTest'` 통과

</details>

### 10-2. Phase 2 체크리스트

- [x] `SuggestMountainService`
- [x] `MountainService`
- [x] `FacilityService`
- [x] `BaseService`
- [x] `WeatherService`
- [x] `WeatherScheduler`

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 4 > Phase 2 > 작업 배치 1` 범위를 처리했다.
- 추가한 테스트 파일:
  - `SuggestMountainServiceTest`
  - `MountainServiceTest`
  - `FacilityServiceTest`
  - `BaseServiceTest`
  - `WeatherServiceTest`
  - `WeatherSchedulerTest`
- 검증한 내용:
  - 초성/일반 키워드 분기와 정규화 위임
  - 산 목록 조회, 존재 검증, 자동완성 조회
  - 시설/베이스 조회 시 산 존재 검증 선행
  - 베이스 상세 조회 시 이미지 repository 결합
  - 날씨 응답 보정 온도 계산과 예외 격리
  - 시작 시 날씨 갱신과 스케줄 실행 위임
- 실행 결과:
  - `./gradlew test --tests '*SuggestMountainServiceTest' --tests '*MountainServiceTest' --tests '*FacilityServiceTest' --tests '*BaseServiceTest' --tests '*WeatherServiceTest' --tests '*WeatherSchedulerTest'` 통과

</details>

### 10-3. Phase 3 체크리스트

- [x] `MountainController`
- [x] `FacilityController`
- [x] `BaseController`

<details>
<summary>작업 결과</summary>

- 이번 배치에서는 `Slice 4 > Phase 3 > 작업 배치 1` 범위를 처리했다.
- 추가한 테스트 파일:
  - `MountainControllerTest`
  - `FacilityControllerTest`
  - `BaseControllerTest`
- 검증한 내용:
  - 산 목록 / 자동완성 응답 구조
  - 시설 목록 / 베이스 목록 / 베이스 상세 응답 구조
  - 자동완성 키워드 누락 시 공통 400 응답
  - 없는 산 조회 시 `MOUNTAIN404_001` 예외 응답
- 실행 결과:
  - `./gradlew test --tests '*MountainControllerTest' --tests '*FacilityControllerTest' --tests '*BaseControllerTest'` 통과

</details>

### 10-4. Phase 4 체크리스트

- [ ] `MountainRepository`
- [ ] `FacilityRepository`
- [ ] `BaseRepository`
- [ ] `BaseImageRepository`

### 10-5. Phase 5 체크리스트

- [ ] OpenWeather stub 통합 테스트

### 10-6. Phase 6 체크리스트

- [ ] 산 목록 조회 흐름
- [ ] 산 자동완성 흐름
- [ ] 시설 조회 흐름
- [ ] 베이스 목록 조회 흐름
- [ ] 베이스 상세 조회 흐름
- [ ] 날씨 갱신 흐름

## 11. 바로 잡아야 할 코드 리스크 체크리스트

- [x] `TravelRecordDetailDTO.from()`의 `endAt` 매핑 수정 완료
- [ ] `GetTravelRecordDetailRes.from()`의 `mountainId` 정책 확인 필요
- [x] `TravelRecordService.findRecordById()` 사용자 소유권 검증 추가 완료
- [x] `TravelRecordService.deleteRecordById()` 사용자 소유권 검증 추가 완료
- [x] `TravelRecordRepository`에 소유권 검증용 query 추가 완료
- [x] `AppleOAuthService.isOurServiceAudience()` 조건 검증 완료
- [ ] `AppleJwtHandler` base64url 디코딩 방식 검증 필요
- [ ] `ValidEmailPatternValidator` null 정책 명확화 필요
- [ ] `RandomNicknameService` empty pool 정책 필요
- [ ] `UserService.registerBasicInformation()` 전화번호 중복 검증 확인 필요
- [ ] `UserService.updateUserProfile()` email/phone unique 정책 확인 필요
- [x] `CourseRepository`의 `@Param` import 동작 검증 완료
- [ ] `WebSocketHandler.afterConnectionClosed()`의 미인증 세션 경로 검증 필요

## 12. 구조 개선 체크리스트

- [ ] `TravelService` 이벤트별 핸들러 분리 검토
- [ ] `WebSocketHandler` 인증 처리와 travel 처리 분리 검토
- [ ] `TravelRecordService` 소유권 검증 책임 명확화
- [ ] `CourseService` 위치 분석 책임 분리 검토
- [ ] `RemainingTimeCalculator`의 course 중복 조회 제거 검토
- [ ] `TravelTrackingInfoStore`의 외부 저장소 전환 필요성 검토
- [ ] `WebSocketAuthService` payload 계약 명시
- [ ] `UserService` 책임 분리 검토
- [ ] `AppleOAuthService` 검증 전용 컴포넌트 분리 검토
- [ ] `WeatherService` fetch/convert/update 분리 검토

## 13. 이력 부록: Slice 1 작업 배치 기록

이 섹션부터는 현재 상태가 아니라 작업 당시의 실행 단위를 보관한 이력이다.
현재 진행 여부 판단은 앞쪽 Slice/Phase 체크리스트를 기준으로 본다.

### 13-1. Phase 1 작업 배치

- [x] 작업 배치 1: `TravelEvent`, `EventPolicy`, `GeoUtil`, `TravelDistanceCalculator`, `CourseLocationMatcher`, `DateUtil`, `TimeMapper`
- [x] 작업 배치 2: `TravelTrackingInfo`, `TravelMapper`, `TravelResponseMapper`, `PayloadMapper`
- [x] 작업 배치 3: `Status`, `RemainingTime`, `ClosestCoordinateInfo`, `TravelRecordDTO`, `TravelRecordDetailDTO`, `GetTravelRecordByMonthRes`, `GetTravelRecordDetailRes`, `TravelEventResponseData`

### 13-2. Phase 2 작업 배치

- [x] 작업 배치 1: `WebSocketAuthService`
- [x] 작업 배치 2: `TravelDomainService`, `TravelService`, `TravelRecordService`
- [x] 작업 배치 3: `TravelTrackingInfoStore`, `RemainingTimeCalculator`

### 13-3. Phase 3 작업 배치

- [x] 작업 배치 1: `TravelRecordController`, `WebSocketHandler`, `WebSocketResponser`, `WebSocketConfig`

### 13-4. Phase 4 작업 배치

- [x] 작업 배치 1: `TravelRecordRepository`, `CourseRepository` spatial 테스트

## 14. 이력 부록: Slice 3 작업 배치 기록

### 14-1. Phase 1 작업 배치

- [x] 작업 배치 1: `CourseSearchCondition`, `CourseWithBookmarkDTO`, `BookmarkMapper`, `PathwayMapper`, `PathwayCoordinatesArrayDTO`

### 14-2. Phase 2 작업 배치

- [x] 작업 배치 1: `CourseService`, `BookmarkService`, `CoursePathwaySequenceService`, `PathwayService`

### 14-3. Phase 3 작업 배치

- [x] 작업 배치 1: `CourseController`, `BookmarkController`, `PathwayController`

## 15. 이력 부록: Slice 4 작업 배치 기록

### 15-1. Phase 1 작업 배치

- [x] 작업 배치 1: `MountainDTO`, `SuggestedMountainDTO`, `FacilityDTO`, `BaseDTO`, `BaseDetailDTO`, `CourseDetailDTO`, `WeatherRes`

### 15-2. Phase 2 작업 배치

- [x] 작업 배치 1: `SuggestMountainService`, `MountainService`, `FacilityService`, `BaseService`, `WeatherService`, `WeatherScheduler`

### 15-3. Phase 3 작업 배치

- [x] 작업 배치 1: `MountainController`, `FacilityController`, `BaseController`

## 16. 이력 부록: Slice 2 작업 배치 기록

### 16-1. Phase 1 작업 배치

- [x] 작업 배치 1: `AuthToken`, `AppleLoginReq`, `AppleLoginRes`, `SendSmsRes`, `VerifySmsRes`, `KaKaoUserInfoRes`, `BloodType`, `User`, `UserInfo`, `PhysicalInfo`, `UserAlert`, `UserMapper`, `UserProfileDTO`, `UserAlertSettingDTO`, `ProfileStatusInfoDTO`, `GetProfileRes`, `GetProfileStatusRes`, `GetAlertSettingRes`, `GetRandomNicknameRes`, `GetNicknameAvailabilityRes`, `RedirectUriBuilder`, `HttpHeadersGenerator`, `ApiResponse`, `ValidEmailPatternValidator`, `GeometryConfig`, `WebSocketSuccessRes`, `WebSocketFailRes`
- [x] 작업 배치 2: `JwtTokenProvider` 보강, `AppleJwtHandler`, `ApplePublicKeyGenerator`, `SmsUtilService`, `JwtInterceptor`, `UserIdResolver`, `NicknamePoolInitializer`

### 16-2. Phase 2 작업 배치

- [x] 작업 배치 1: `AuthTokenService` 보강, `SmsService`, `KaKaoOAuthService`, `AppleOAuthService`, `OAuthService`
- [x] 작업 배치 2: `UserService`, `RandomNicknameService`, `RandomNicknamePoolService`, `RandomNicknamePoolManager`, `S3Service` 보강

### 16-3. Phase 3 작업 배치

- [x] 작업 배치 1: `OAuthController`, `SmsController`, `UserController`, `ImageController`
- [x] 작업 배치 2: `GlobalExceptionHandler`, `WebConfig`

## 17. 결론

- 분류 체계는 `Phase 1 -> Phase 6` 확장 순서로 본다.
- 실제 작성은 도메인별 세로 슬라이스 방식으로 진행한다.
- 첫 번째 슬라이스는 `travel + websocket + spatial query`다.
- 즉, “단위 테스트부터 시작하되 그 첫 대상은 `travel`”로 이해하면 된다.
