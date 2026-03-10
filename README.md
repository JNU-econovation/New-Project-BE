# 🌲 산결 - 등산 코스 및 지리 정보 시스템

> **"복잡한 등산 데이터를 체계적으로 관리하고 사용자에게 최적의 코스를 제안합니다."**  
> **SOOP**은 객체지향적 설계와 데이터 최적화에 집중하여 구축된 등산 정보 플랫폼의 백엔드 서비스입니다.

---

## 🎯 Project Focus

본 프로젝트는 단순한 기능 구현을 넘어, **확장 가능하고 유지보수가 쉬운 서버 아키텍처**를 구축하는 데 초점을 맞추었습니다.

* **Data Driven**: 산, 코스, 경유지, 등산로 좌표(Pathway) 간의 복잡한 RDBMS 관계 설계.
* **Performance**: QueryDSL을 활용한 동적 쿼리 최적화 및 N+1 문제 해결.
* **Security**: Redis와 JWT를 결합한 상태가 없는 보안 인증 체계.

## 🛠 Tech Stack

### Core

- **Language**: Java 21
- **Framework**: Spring Boot 3.3.1
- **Database**: MySQL (Production), Redis (Cache/Auth)
- **ORM**: Spring Data JPA, QueryDSL

### Integration & Security

- **Authentication**: JWT, OAuth 2.0 (Kakao, Apple), SMS Verification
- **External API**: Spring Cloud OpenFeign (Weather API 연동)
- **Object Storage**: AWS S3 SDK

## 🏗 Backend Architecture & Design

### 1. Layered Architecture

엄격한 계층 분리를 통해 비즈니스 로직의 응집도를 높이고 결합도를 낮추었습니다.

* **Domain**: 핵심 비즈니스 엔티티 및 JPA 매핑.
* **Repository**: QueryDSL을 활용한 복잡한 데이터 조회 로직 추상화.
* **Service**: 트랜잭션 단위의 비즈니스 유즈케이스 처리.
* **Controller & DTO**: API 명세에 최적화된 Request/Response 객체 분리 및 검증.

### 2. Domain Modeling (Entity Relationship)

* `Mountain` - `Course` - `Base` - `Pathway`로 이어지는 계층적 구조 설계.
* 위경도 좌표(Pathway) 데이터의 정밀도 보장을 위한 `BigDecimal` 활용 및 인덱스 고려.

## 💡 Technical Deep Dive (핵심 문제 해결 경험)

### 📈 QueryDSL을 통한 복잡한 데이터 조인 및 성능 최적화

* **문제**: 등산 코스 목록 조회 시, 개별 사용자의 '북마크 상태'를 실시간으로 병합하고 난이도별(EASY -> NORMAL -> HARD) 커스텀 정렬을 수행해야 함.
* **해결**: `Left Join`과 `CaseBuilder`를 활용한 단일 쿼리 작성. 애플리케이션 메모리에서의 필터링을 지양하고 DB 엔진에서 정렬과 조인을 처리하여 응답 속도를 대폭 개선했습니다.

### 🔐 Redis 기반의 Stateless 인증 및 보안 강화

* **문제**: 서버 확장성을 위해 세션을 제거하고 JWT를 도입했으나, 로그아웃 처리 및 토큰 탈취 대응이 필요함.
* **해결**: **Redis**를 블랙리스트 저장소로 활용하여 로그아웃된 토큰의 접근을 즉시 차단했습니다. 또한, TTL(Time-To-Live) 기능을 활용하여 SMS 인증 번호의 유효 시간을 안전하고
  효율적으로 관리했습니다.

### 🧱 객체지향적 공통 예외 처리 시스템

* **문제**: 서비스 규모가 커짐에 따라 산발적인 에러 응답으로 인한 프론트엔드 협업 효율 저하.
* **해결**: `@RestControllerAdvice`와 `ErrorType` 인터페이스를 사용하여 표준화된 에러 응답 구조를 설계했습니다. 모든 비즈니스 예외는 추적 가능한 고유 코드와 메시지를 반환합니다.

### 🔄 유연한 외부 서비스 통합 (OpenFeign)

* **문제**: 외부 날씨 API 및 OAuth 제공자 서버의 장애가 서비스 전체로 확산될 위험.
* **해결**: **Spring Cloud OpenFeign**을 도입하여 선언적인 방식으로 외부 인터페이스를 관리하고, 설정 중심의 유연한 타임아웃 및 재시도 전략을 적용했습니다.

## 📂 Project Structure

```text
src/main/java/com/econo_4factorial/newproject/
├── auth/         # OAuth, JWT, SMS 인증 로직 및 Redis 통합
├── mountain/     # 산 정보 도메인 및 검색 로직
├── course/       # 등산 코스 및 북마크 (QueryDSL 중심의 복잡한 조회 로직)
├── pathway/      # 등산로 좌표 데이터 처리 및 경로 정보
├── base/         # 경유지 및 시설물 정보 관리
├── user/         # 사용자 프로필 및 계정 관리
└── common/       # 전역 설정 (Redis, Exception Handler, Annotation)
```

## 📄 License

This project is licensed under the MIT License.
