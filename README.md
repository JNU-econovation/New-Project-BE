# 🌲 산결 Backend

> 등산 정보 조회, 인증/사용자 관리, 북마크, 프로필 이미지, 산행 기록, WebSocket 기반 산행 이벤트 처리를 담당하는 백엔드 애플리케이션입니다.  
> 이 문서는 현재 저장소 기준으로 정리한 README입니다.

## 👀 한눈에 보기

산결 백엔드는 크게 아래 기능을 포함합니다.

- 🔐 Kakao / Apple OAuth, JWT 발급 및 재발급, Redis 기반 토큰 저장
- 📱 SMS 인증번호 발송 및 검증
- 🧑 사용자 프로필, 알림 설정, 랜덤 닉네임, 프로필 이미지 관리
- 🏔 산 / 코스 / 경유지 / 시설 / 등산로 정보 조회
- 🧭 산행 기록 조회 / 삭제
- ⚡ WebSocket 기반 산행 이벤트 처리

## ✨ 현재 구현 범위

### 🔐 인증 / 사용자

**`auth`**

- OAuth 로그인 컨트롤러
- Kakao / Apple 연동 서비스
- JWT access / refresh token 발급
- Redis 기반 refresh token / blacklist token 저장
- SMS 인증번호 발송 / 검증

**`user`**

- 기본정보 / 개인정보 / 알림 설정 / 프로필 조회 및 수정
- Redis 기반 랜덤 닉네임 풀 관리
- AWS S3 Presigned URL 발급
- 프로필 이미지 파일명 저장 / 조회 / 삭제

### 🏔 산 / 코스 / 경로 정보

**`mountain`**

- 산 목록 조회
- 자동완성 검색

**`course`**

- 코스 목록 조회
- 북마크 추가 / 삭제 / 목록
- QueryDSL 기반 정렬 / 조인 조회

**`pathway`**

- 코스별 경로 조회

**`base`**

- 경유지 목록 / 상세 조회
- OpenWeather 연동
- 스케줄링 기반 날씨 갱신

**`facility`**

- 산별 시설 조회

### 🧭 산행 기록 / 실시간 이벤트

**`travel`**

- 산행 기록 저장 엔티티와 기록 조회 API
- 좌표 기반 거리 / 남은 시간 계산 유틸
- WebSocket 메시지 파싱, 인증, 이벤트 라우팅
- 산행 상태 관리
  - `UNSTARTED`
  - `STARTED`
  - `TRAVEL`
  - `PAUSED`
  - `RESTARTED`
  - `END`

처리하는 WebSocket 이벤트는 아래와 같습니다.

- `auth-user`
- `start`
- `current-position`
- `pause`
- `keep-alive`
- `restart`
- `end`

### 🧩 공통 인프라

**`common`**

- 전역 예외 처리
- API 응답 래퍼
- JWT 인터셉터와 `@UserId` resolver
- Redis / QueryDSL / Swagger / WebSocket / Geometry / S3 설정

## 🛠 기술 스택

| 구분 | 사용 기술 |
| --- | --- |
| Language / Framework | Java 21, Spring Boot 3.3.1 |
| Data / Persistence | MySQL, Redis, Spring Data JPA, QueryDSL |
| Spatial | Hibernate Spatial, JTS |
| Integration | Spring Cloud OpenFeign, Spring WebSocket, AWS S3, CoolSMS SDK, OpenWeather API |
| Auth / Docs / Ops | JWT, Kakao OAuth, Apple OAuth, springdoc-openapi, Spring Boot Actuator, Prometheus metrics export |

## 🧱 주요 구성 요소

### 인증 / 인가 흐름

- [AuthTokenService](src/main/java/com/econo_4factorial/newproject/auth/jwt/service/AuthTokenService.java)
- [JwtTokenProvider](src/main/java/com/econo_4factorial/newproject/auth/jwt/service/JwtTokenProvider.java)
- [JwtInterceptor](src/main/java/com/econo_4factorial/newproject/common/config/JwtInterceptor.java)
- [UserIdResolver](src/main/java/com/econo_4factorial/newproject/common/annotation/resolver/UserIdResolver.java)

이 프로젝트는 Spring Security 필터 체인 중심 구조가 아니라, 인터셉터와 argument resolver를 조합해 인증 컨텍스트를 처리합니다.

### 실시간 산행 이벤트 처리

- [WebSocketHandler](src/main/java/com/econo_4factorial/newproject/travel/handler/WebSocketHandler.java)
- [WebSocketAuthService](src/main/java/com/econo_4factorial/newproject/common/util/webSocket/WebSocketAuthService.java)
- [TravelService](src/main/java/com/econo_4factorial/newproject/travel/service/TravelService.java)
- [TravelDomainService](src/main/java/com/econo_4factorial/newproject/travel/service/TravelDomainService.java)
- [TravelTrackingInfoStore](src/main/java/com/econo_4factorial/newproject/travel/service/TravelTrackingInfoStore.java)

`/travel-navigate` WebSocket 엔드포인트에서 인증 이벤트와 산행 상태 이벤트를 처리합니다.

### 코스 / 공간 정보 처리

- [CourseCustomRepositoryImpl](src/main/java/com/econo_4factorial/newproject/course/repository/CourseCustomRepositoryImpl.java)
- [CourseRepository](src/main/java/com/econo_4factorial/newproject/course/repository/CourseRepository.java)
- [CourseLocationMatcher](src/main/java/com/econo_4factorial/newproject/course/service/CourseLocationMatcher.java)
- [GeoUtil](src/main/java/com/econo_4factorial/newproject/travel/util/GeoUtil.java)

코스 목록 조회에는 QueryDSL 기반 조인 / 정렬 로직이 포함되어 있고, 산행 분석에는 geometry 타입과 좌표 계산 유틸을 사용합니다.

### 스케줄링

- [WeatherScheduler](src/main/java/com/econo_4factorial/newproject/base/service/weather/WeatherScheduler.java)
- [RandomNicknamePoolScheduler](src/main/java/com/econo_4factorial/newproject/user/service/RandomNicknamePoolScheduler.java)

## 🚀 실행 방법

### 사전 준비

- Java 21
- MySQL
- Redis
- `.env` 또는 환경 변수 설정

애플리케이션은 [application.yml](src/main/resources/application.yml)에서 `optional:file:.env[.properties]`를 import 하며, 기본 활성 프로필은 `local`입니다.

`local` 프로필 그룹에는 아래 설정이 포함됩니다.

- `jpa`
- `token`
- `kakao`
- `apple`
- `metrics`

### 로컬 실행

```bash
./gradlew bootRun
```

### 테스트 실행

```bash
./gradlew test
```

### 데이터 초기화

- [data.sql](src/main/resources/data.sql)에 샘플 데이터가 포함되어 있습니다.
- [application-jpa.yml](src/main/resources/application-jpa.yml)에서 SQL init 설정을 사용합니다.

## 🔧 환경 변수

필수 환경 변수는 아래 그룹으로 나뉩니다.

### DB / Redis

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `REDIS_HOST`
- `REDIS_PORT`

### JWT

- `CUSTOM_JWT_ACCESS_TOKEN_EXPIRED_TIME`
- `CUSTOM_JWT_ACCESS_TOKEN_SECRET_KEY`
- `CUSTOM_JWT_REFRESH_TOKEN_EXPIRED_TIME`
- `CUSTOM_JWT_REFRESH_TOKEN_SECRET_KEY`

### OAuth / Redirect

- `KAKAO_CLIENT_ID`
- `KAKAO_REDIRECT_URI`
- `APPLE_CLIENT_ID`
- `APPLE_ISS`
- `LOGIN_SUCCESS_BASE_URI`

### External API

- `OPENWEATHER_API_KEY`
- `COOLSMS_API_KEY`
- `COOLSMS_API_SECRET`
- `COOLSMS_SENDER_NUMBER`

### AWS S3

- `REGION`
- `BUCKET`
- `DEFAULT_PROFILE_KEY`
- `ACCESS_KEY`
- `SECRET_KEY`

## 📂 패키지 구조

```text
src/main/java/com/econo_4factorial/newproject/
├── auth/       # OAuth, JWT, SMS, Redis 토큰 저장소
├── base/       # 경유지, 상세 조회, 날씨 연동
├── common/     # 설정, 예외 처리, 인터셉터, 응답 래퍼, WebSocket 유틸
├── course/     # 코스 조회, 북마크, QueryDSL/공간 조회
├── facility/   # 시설 조회
├── mountain/   # 산 목록 / 검색
├── pathway/    # 경로 조회
├── travel/     # 산행 기록, 산행 상태, WebSocket 이벤트 처리
└── user/       # 사용자 프로필, 알림, 닉네임 풀, 이미지
```

## 🧪 테스트와 문서

- 테스트는 JUnit 5, Mockito, AssertJ 기반으로 작성합니다.
- 현재 `src/test/java`에는 JWT, S3, `travel/websocket` 중심의 단위 테스트 / 서비스 테스트 / 계약 테스트가 포함되어 있습니다.
- 전체 테스트 계획과 진행 현황은 [test.md](test.md)에 정리되어 있습니다.

## 📌 참고

- 루트에 [Dockerfile](Dockerfile)이 포함되어 있습니다.
- GitHub Actions 워크플로우와 ECS 관련 설정은 `.github/` 아래에 있습니다.

## 📄 License

This project is licensed under the MIT License.
