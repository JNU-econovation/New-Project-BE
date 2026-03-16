# 테스트 작성 계획

## 1. 현재 상태 요약
- 현재 테스트 클래스는 4개뿐이다.
- 존재하는 테스트: `JwtTokenProviderTest`, `AuthTokenServiceTest`, `S3ServiceTest`, `NewProjectApplicationTests`
- 현재 커버된 범위는 JWT 일부, 인증 토큰 일부, S3 서비스 일부, 스프링 컨텍스트 로드뿐이다.
- Redis 연동 테스트는 없다.
- 외부 API 연동 테스트는 없다.
- Controller slice 테스트(`@WebMvcTest`)가 없다.
- Repository/JPA/Querydsl 테스트가 없다.
- 기능 단위 통합 테스트(`@SpringBootTest`)가 없다.

정리하면, 지금은 “핵심 인증 유틸 일부만 단위 테스트가 있는 상태”이고, 실제 사용자 기능 흐름이나 인프라 연동은 거의 검증되지 않은 상태다.

## 2. 기본 원칙
- 1차는 순수 단위 테스트부터 작성한다.
- 2차는 목 기반 서비스 단위 테스트를 확장한다.
- 3차는 `@WebMvcTest`로 컨트롤러 계약을 고정한다.
- 4차는 `@DataJpaTest`로 Repository/Querydsl 쿼리를 검증한다.
- 5차는 `@SpringBootTest`로 “기능 단위 통합 테스트”를 작성한다.
- 외부 I/O는 실제 호출하지 않고, Redis는 Testcontainers, HTTP 연동은 WireMock 또는 MockWebServer, S3는 우선 mock 기반으로 간다.

## 3. 권장 테스트 인프라
- 단위 테스트: JUnit 5, Mockito, AssertJ
- 컨트롤러 테스트: `@WebMvcTest`, `MockMvc`
- JPA 테스트: `@DataJpaTest`
- 기능 단위 통합 테스트: `@SpringBootTest`
- Redis 통합 테스트: Redis Testcontainers
- MySQL 쿼리 검증: MySQL Testcontainers
- 외부 HTTP 연동 검증: WireMock 또는 MockWebServer

현재 `build.gradle`에는 아래 테스트 의존성이 없다. 계획 진행 전에 추가를 권장한다.
- `com.h2database:h2`
- `org.testcontainers:junit-jupiter`
- `org.testcontainers:mysql`
- `org.testcontainers:redis`
- `com.github.tomakehurst:wiremock-jre8` 또는 `com.squareup.okhttp3:mockwebserver`

## 4. 작성 우선순위

### Phase 1. 순수 단위 테스트
Spring context 없이 바로 돌릴 수 있는 테스트부터 작성한다.

#### auth
- `JwtTokenProvider`
  - 기존 테스트 보강
  - 잘못된 `Authorization` 헤더
  - null 헤더
  - refresh token 파싱 실패 케이스
  - `getExpirationTime` 토큰 타입별 분기
- `AppleJwtHandler`
  - 헤더 파싱 성공
  - 잘못된 토큰 형식
  - 만료 토큰
  - 서명 오류 토큰
- `ApplePublicKeyGenerator`
  - kid/alg 매칭 성공
  - 매칭 실패 시 예외
  - 잘못된 key spec 시 예외
- `SmsUtilService`
  - 인증번호 6자리 생성
  - 숫자만 생성되는지 검증
  - 전화번호 normalize
- `AuthToken`
  - `of()` 팩토리 결과 검증

#### user
- `BloodType`
  - 대소문자 입력 허용
  - 잘못된 혈액형 예외
  - null 입력 예외
- `User`
  - 기본정보 등록
  - 개인정보 등록
  - 프로필 수정
  - 프로필 이미지 파일명 저장/삭제
  - 기본정보 설정 여부
  - 개인정보 설정 여부
- `UserInfo`
  - 이메일/이름/전화번호 포맷 검증
  - null/blank 입력 예외
  - `isBasicInfoSet()`
- `PhysicalInfo`
  - 키/몸무게 범위 검증
  - etc 길이 검증
  - `isPersonalInfoSet()`
- `UserAlert`
  - 알림 값 변경
- `UserMapper`
  - Kakao DTO -> User
  - Apple DTO -> User

#### common
- `RedirectUriBuilder`
  - 성공 URI 파라미터 포함 여부
  - 실패 URI 생성
- `HttpHeadersGenerator`
  - Location 헤더 설정
- `ApiResponse`
  - success/fail 응답 body/status
- `ValidEmailPatternValidator`
  - 정상 이메일
  - 비정상 이메일
  - null 입력 정책 명시

#### base
- `WeatherRes`
  - `toDTO()` 매핑
- `BaseDTO`
  - 엔티티 -> DTO
- `BaseDetailDTO`
  - 엔티티 + 이미지 리스트 -> DTO
- `CourseDetailDTO`
  - 엔티티 -> DTO

#### course
- `CourseSearchCondition`
  - `of()` 생성 검증
- `BookmarkMapper`
  - User/Course -> Bookmark
- `CourseWithBookmarkDTO`
  - `from()` 매핑

#### facility
- `FacilityDTO`
  - 엔티티 -> DTO

#### mountain
- `MountainDTO`
  - 엔티티 -> DTO
- `SuggestedMountainDTO`
  - 엔티티 -> DTO
- `SuggestMountainService`
  - null/blank 입력
  - 초성 검색 분기
  - 단어 검색 분기
  - trim/normalize 동작

#### pathway
- `PathwayMapper`
  - coordinates JSON 파싱 성공
  - 잘못된 JSON 예외

### Phase 2. 목 기반 서비스 단위 테스트
Repository, Feign client, Redis template, 외부 어댑터를 mock 처리한다.

#### auth.service
- `OAuthService`
  - 카카오 로그인 성공
  - 애플 로그인 성공
  - 로그인 실패 시 `AuthException` 전환
  - logout 위임
  - reissue 위임
- `SmsService`
  - 번호 중복 검증 후 저장/전송
  - normalize 후 Redis 저장
  - 인증 성공 시 Redis 삭제
  - 저장 코드 없음 예외
  - 인증 코드 불일치 예외
- `KaKaoOAuthService`
  - 로그인 URI 생성
  - 인가 코드 -> access token -> user info 조회 체인
- `AppleOAuthService`
  - identity token 헤더 파싱
  - 공개키 조회
  - 공개키 생성
  - claims 검증
  - `AppleUserInfoDTO` 반환

#### auth.jwt
- `AuthTokenService`
  - 현재 테스트 보강
  - `saveRefreshToken()`
  - `isLoggedIn()`의 refresh token 미존재 케이스

#### user.service
- `UserService`
  - 사용자 조회 실패
  - 카카오/애플 사용자 생성 분기
  - 기존 사용자 재사용 분기
  - 닉네임 중복 확인
  - 이메일/전화번호 중복 검증
  - 기본정보 등록
  - 개인정보 등록
  - 알림 조회/수정
  - 프로필 조회/수정
  - 프로필 이미지 파일명 조회/수정/삭제
- `S3Service`
  - 기존 테스트 보강
  - 기본 이미지 삭제 방지
  - 삭제 대상 파일이 S3에 없을 때 동작
  - Presigned URL key prefix/확장자 검증
- `RandomNicknameService`
  - pool suffix 조합
  - pool 비어 있을 때 정책 확인
- `RandomNicknamePoolService`
  - pop
  - size
  - suffix add
- `RandomNicknamePoolManager`
  - threshold 이하일 때 보충
  - threshold 초과일 때 skip
  - start position 조회/갱신
  - suffix 범위 생성 포맷

#### base.service
- `BaseService`
  - mountain 존재 검증
  - base 목록 조회
  - 상세 조회 시 이미지 조합
- `WeatherService`
  - 정상 응답이면 base 날씨 갱신
  - 응답 null이면 skip
  - temperature/weather null이면 skip
  - 외부 API 예외를 base 단위로 삼키고 다음 base 진행

#### course/facility/mountain/pathway.service
- `CourseService`
  - course 조회 성공/실패
  - 코스 목록 정렬 조건 전달
  - 상세 조회
- `BookmarkService`
  - 즐겨찾기 추가
  - 즐겨찾기 삭제 성공/실패
  - 즐겨찾기 목록 조회
- `FacilityService`
  - mountain 존재 검증
  - 시설 목록 조회
- `MountainService`
  - 전체 산 목록
  - 산 존재 검증
  - 초성 검색
  - 이름 포함 검색
- `CoursePathwaySequenceService`
  - courseId 기반 조회 위임
- `PathwayService`
  - sequence -> pathway DTO 매핑

#### common
- `JwtInterceptor`
  - preflight 허용
  - reissue URI 허용
  - 일반 요청은 토큰 추출 + 로그인 검사
- `UserIdResolver`
  - `@UserId` 파라미터 지원 여부
  - 헤더 없음 예외
  - 토큰에서 userId 추출
- `NicknamePoolInitializer`
  - Redis ready 성공
  - Redis ready 실패 후 recover
  - start key 없으면 초기화
  - 초기화 후 pool manager 호출
- `WeatherScheduler`
  - startup/run 시 weatherService 호출

### Phase 3. 컨트롤러 슬라이스 테스트
`@WebMvcTest`로 request/response 계약, validation, 예외 응답을 고정한다.

#### auth.controller
- `OAuthController`
  - 카카오 로그인 URI 조회
  - 카카오 콜백 시 redirect Location 헤더
  - 애플 로그인 성공
  - 로그아웃
  - reissue
  - 잘못된 body/파라미터 validation
- `SmsController`
  - 인증번호 전송
  - 인증번호 검증
  - validation 실패

#### user.controller
- `UserController`
  - 프로필 조회/수정
  - 프로필 상태 조회
  - 기본정보 등록
  - 개인정보 등록
  - 알림 조회/수정
  - 랜덤 닉네임 조회
  - 닉네임 중복 확인
- `ImageController`
  - presigned URL 요청
  - 이미지 조회
  - 이미지 삭제
  - 파일명 저장

#### read-only controller
- `MountainController`
- `BaseController`
- `FacilityController`
- `CourseController`
- `BookmarkController`
- `PathwayController`

각 컨트롤러는 아래를 공통 검증한다.
- 정상 응답의 status/body 구조
- `@UserId` resolver 적용
- validation 실패 응답
- 서비스 예외 -> `GlobalExceptionHandler` 변환 결과

### Phase 4. Repository / Querydsl 통합 테스트
`@DataJpaTest` 중심으로 작성한다.

#### JPA repository
- `UserRepository`
  - kakaoId/appleSub 조회
  - nickname/email/phone exists
- `BookmarkRepository`
  - userId + courseId 조회
  - userId 기준 목록 조회
- `BaseRepository`
  - mountainId 기준 목록
- `BaseImageRepository`
  - baseId 기준 이미지 조회
- `FacilityRepository`
  - mountainId 기준 목록
- `MountainRepository`
  - initials 시작 검색
  - 이름 포함 검색

#### Querydsl custom repository
- `CourseCustomRepositoryImpl`
  - mountainId 필터
  - bookmark 여부 계산
  - difficulty 정렬
  - length 정렬
  - default 정렬
- `CoursePathwaySequenceCustomRepositoryImpl`
  - courseId 기준 sequence 정렬
  - pathway/departure/destination fetch join 결과

주의:
- Querydsl 정렬/조인 로직은 H2로 1차 검증하되, 실제 운영 DB가 MySQL이므로 최종적으로는 MySQL Testcontainers smoke 테스트를 추가하는 것이 안전하다.

### Phase 5. Redis 통합 테스트
Redis는 단위 테스트만으로는 충분하지 않다. 최소한 아래는 실제 Redis와 붙여야 한다.

#### Redis adapter
- `SmsRepository`
  - `sms:{phone}` key 저장
  - TTL 적용 여부
  - 조회/삭제
- `RefreshTokenRepository`
  - 저장/조회/삭제
  - `existsByRefreshToken`
- `BlacklistTokenRepository`
  - 저장/존재 확인
- `RandomNicknamePoolService`
  - set pop/add/size
- `NicknamePoolInitializer`
  - key 초기화 + pool append

권장 방식:
- `@SpringBootTest` 또는 `@DataRedisTest` + Redis Testcontainers
- 테스트 전용 profile에서 SSL 비활성화

### Phase 6. 기능 단위 통합 테스트
여기서 말하는 기능 단위 통합 테스트는 “한 API 또는 한 유즈케이스가 여러 레이어를 통과하는 흐름”을 검증하는 테스트다.

#### 인증/인가 플로우
- 카카오 로그인 성공
  - controller -> service -> user 생성/조회 -> auth token 발급 -> redirect
  - 외부 카카오 API는 stub
- 애플 로그인 성공
  - controller -> apple 검증 -> user 생성/조회 -> auth token 발급
  - 외부 apple keys/JWT는 stub 또는 fake key 사용
- 토큰 재발급
  - refresh token 저장 상태에서 재발급
- 로그아웃
  - refresh token 삭제 + blacklist 저장

#### SMS 인증 플로우
- 인증번호 발송
  - normalize -> Redis 저장 -> SMS sender 호출
- 인증번호 검증 성공
  - Redis 조회 -> 검증 -> 삭제
- 인증번호 검증 실패
  - 미존재/불일치

#### 사용자 프로필 플로우
- 기본정보 등록
- 개인정보 등록
- 프로필 상태 조회
- 프로필 수정
- 알림 설정 조회/수정
- 닉네임 중복 확인

#### 이미지 플로우
- presigned URL 발급
- 파일명 저장
- 이미지 조회
- 이미지 삭제

#### 읽기 전용 조회 플로우
- 산 목록 조회
- 산 자동완성 조회
- 베이스 목록/상세 조회
- 시설 조회
- 코스 목록 조회
- 코스 상세 조회
- 경로 조회
- 즐겨찾기 등록/삭제/목록 조회

#### 스케줄러/백그라운드 플로우
- `WeatherScheduler` 실행 시 각 base 날씨 갱신
- `RandomNicknamePoolScheduler` 실행 시 pool 보충

## 5. 패키지별 전체 테스트 대상 목록

### auth 패키지
- 단위 테스트
  - `JwtTokenProvider`
  - `AuthTokenService`
  - `OAuthService`
  - `SmsService`
  - `SmsUtilService`
  - `AppleJwtHandler`
  - `ApplePublicKeyGenerator`
  - `AppleOAuthService`
  - `KaKaoOAuthService`
  - DTO/record 변환 메서드
- 통합 테스트
  - `OAuthController`
  - `SmsController`
  - `RefreshTokenRepository`
  - `BlacklistTokenRepository`

### user 패키지
- 단위 테스트
  - `User`
  - `UserInfo`
  - `PhysicalInfo`
  - `UserAlert`
  - `BloodType`
  - `UserMapper`
  - `UserService`
  - `S3Service`
  - `RandomNicknameService`
  - `RandomNicknamePoolService`
  - `RandomNicknamePoolManager`
- 통합 테스트
  - `UserController`
  - `ImageController`
  - `UserRepository`

### common 패키지
- 단위 테스트
  - `JwtInterceptor`
  - `UserIdResolver`
  - `RedirectUriBuilder`
  - `HttpHeadersGenerator`
  - `ApiResponse`
  - `ValidEmailPatternValidator`
  - `NicknamePoolInitializer`
  - `WeatherScheduler`
- 통합 테스트
  - `GlobalExceptionHandler`
  - `WebConfig`가 resolver/interceptor를 실제로 등록하는지 확인하는 MVC 통합 테스트

### base 패키지
- 단위 테스트
  - `BaseService`
  - `WeatherService`
  - `WeatherRes`
  - `BaseDTO`
  - `BaseDetailDTO`
  - `CourseDetailDTO`
- 통합 테스트
  - `BaseController`
  - `BaseRepository`
  - `BaseImageRepository`

### mountain 패키지
- 단위 테스트
  - `MountainService`
  - `SuggestMountainService`
  - `MountainDTO`
  - `SuggestedMountainDTO`
- 통합 테스트
  - `MountainController`
  - `MountainRepository`

### facility 패키지
- 단위 테스트
  - `FacilityService`
  - `FacilityDTO`
- 통합 테스트
  - `FacilityController`
  - `FacilityRepository`

### course 패키지
- 단위 테스트
  - `CourseService`
  - `BookmarkService`
  - `CourseSearchCondition`
  - `CourseWithBookmarkDTO`
  - `BookmarkMapper`
- 통합 테스트
  - `CourseController`
  - `BookmarkController`
  - `BookmarkRepository`
  - `CourseCustomRepositoryImpl`

### pathway 패키지
- 단위 테스트
  - `PathwayMapper`
  - `PathwayService`
  - `CoursePathwaySequenceService`
- 통합 테스트
  - `PathwayController`
  - `CoursePathwaySequenceCustomRepositoryImpl`

### travel 패키지
- 단위 테스트
  - `WebSocketHandler`
    - 연결/종료 시 session 관리
- 통합 테스트
  - 현재 메시지 처리 로직이 비어 있으므로 우선순위 낮음

## 6. 추천 실행 순서
1. 순수 도메인/유틸 단위 테스트부터 작성한다.
2. `UserService`, `OAuthService`, `SmsService`, `JwtInterceptor`처럼 핵심 흐름을 잡는 서비스 테스트를 작성한다.
3. `UserController`, `OAuthController`, `ImageController`, `SmsController` WebMvc 테스트를 먼저 만든다.
4. `CourseCustomRepositoryImpl`, `CoursePathwaySequenceCustomRepositoryImpl`, `UserRepository` 등 저장소 테스트를 추가한다.
5. Redis 통합 테스트를 추가한다.
6. 마지막에 로그인, 프로필, 즐겨찾기, 이미지, SMS 중심으로 기능 단위 통합 테스트를 완성한다.

## 7. 테스트 작성 시 바로 드러날 구조 개선 포인트

### 7-1. 책임 분리 우선 개선 대상
- `UserService`
  - 조회, 중복 검증, 프로필 변경, OAuth 사용자 생성이 한 클래스에 몰려 있다.
  - `UserQueryService`, `UserProfileService`, `UserRegistrationService` 정도로 분리하면 테스트가 쉬워진다.
- `OAuthService`
  - provider 선택, 예외 전환, 토큰 발급이 한 메서드에 묶여 있다.
  - `KakaoLoginUseCase`, `AppleLoginUseCase`로 분리하는 편이 낫다.
- `AppleOAuthService`
  - 헤더 파싱, 공개키 조회, 공개키 생성, claims 검증, DTO 생성이 한 메서드에 다 들어 있다.
  - `AppleIdentityTokenVerifier` 같은 검증 전용 컴포넌트로 빼는 것이 좋다.
- `WeatherService`
  - 외부 호출, 응답 파싱, 온도 보정, 엔티티 갱신, 예외 처리까지 한 메서드에서 수행한다.
  - fetch/convert/update 단위로 분리해야 테스트가 간단해진다.
- `RandomNicknamePoolManager` / `NicknamePoolInitializer`
  - Redis 키 관리, pool 정책, 초기화 라이프사이클이 섞여 있다.
  - key access와 business rule을 분리하는 것이 좋다.
- `SmsService` / `SmsUtilService`
  - normalize, code generation, 저장, 발송이 분산되어 있고 트랜잭션 관점도 불명확하다.
  - `PhoneNumberNormalizer`, `VerificationCodeGenerator`, `SmsSender`로 나누면 테스트가 쉬워진다.
- `PathwayMapper`
  - mapper 안에서 JSON 파싱까지 수행한다.
  - 좌표 파싱 로직을 별도 parser로 빼면 실패 케이스 테스트가 단순해진다.
- `JwtInterceptor` 와 `UserIdResolver`
  - 토큰 추출/파싱 로직이 중복된다.
  - 공통 `AccessTokenExtractor` 같은 컴포넌트로 묶는 편이 낫다.

### 7-2. 테스트로 바로 잡아야 할 리스크
- `AppleOAuthService.isOurServiceAudience()`
  - 현재 조건이 뒤집혀 보인다.
  - 지금 코드는 `aud == client_id`일 때 예외를 던지고 있다.
  - 이 부분은 테스트를 먼저 쓰고 수정하는 것이 안전하다.
- `AppleJwtHandler.decodeHeader()`
  - JWT header는 base64url인데 일반 `Base64.getDecoder()`를 사용 중이다.
  - URL-safe decoder 테스트가 필요하다.
- `ValidEmailPatternValidator`
  - `value == null`일 때 NPE가 날 수 있다.
  - null 허용 여부를 정책으로 정하고 validator를 맞춰야 한다.
- `RandomNicknameService`
  - pool이 비면 `"무등산null"`이 반환될 수 있다.
  - empty pool 정책이 필요하다.
- `SmsService.sendSms()`
  - Redis 저장 후 실제 발송을 시도한다.
  - 발송 실패 시 Redis에 만료 코드가 남을 수 있다.
- `UserService.registerBasicInformation()`
  - 이메일 중복만 검사하고 전화번호 중복은 검사하지 않는다.
- `UserService.updateUserProfile()`
  - 이메일/전화번호 unique 정책을 서비스 레벨에서 보장하지 않는다.
- `WeatherRes.toDTO()`
  - `weatherArray[0]`와 `main`이 무조건 존재한다고 가정한다.
- `WeatherService.updateAllBaseWeather()`
  - 예외를 모두 삼켜서 실패 원인 추적이 어렵다.
- `WebSocketHandler`
  - 메시지 처리 로직이 비어 있다.
  - 현재는 연결/종료 관리만 테스트하면 된다.

## 8. 현실적인 첫 주 작업안
- Day 1
  - 도메인/유틸 단위 테스트 작성
  - `BloodType`, `UserInfo`, `PhysicalInfo`, `User`, `UserAlert`, `RedirectUriBuilder`, `HttpHeadersGenerator`
- Day 2
  - 인증/유저 서비스 단위 테스트 작성
  - `UserService`, `OAuthService`, `SmsService`, `AppleJwtHandler`, `ApplePublicKeyGenerator`
- Day 3
  - `UserController`, `ImageController`, `OAuthController`, `SmsController` WebMvc 테스트 작성
- Day 4
  - `UserRepository`, `BookmarkRepository`, `CourseCustomRepositoryImpl`, `CoursePathwaySequenceCustomRepositoryImpl` 테스트 작성
- Day 5
  - Redis 통합 테스트 + 로그인/프로필/즐겨찾기 기능 단위 통합 테스트 시작

## 9. 결론
- 지금은 테스트 기반이 거의 없는 상태라, 바로 `@SpringBootTest`부터 늘리면 유지보수가 어렵다.
- 순수 단위 테스트 -> 목 기반 서비스 테스트 -> WebMvc 테스트 -> Repository 테스트 -> 기능 단위 통합 테스트 순서로 가는 것이 가장 비용 대비 효과가 좋다.
- 우선순위는 `auth`, `user`, `common`을 먼저 잡고, 그 다음 조회성 패키지(`mountain`, `base`, `facility`, `course`, `pathway`)를 채우는 방향이 적절하다.
