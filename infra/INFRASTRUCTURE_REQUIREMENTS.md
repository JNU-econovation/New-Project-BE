# 인프라 레포지토리 요구사항

이 문서는 백엔드 레포지토리가 호출하는 인프라 레포지토리의 워크플로우 구조와 요구사항을 설명합니다.

## 필요한 워크플로우

### 1. AWS ECS 배포 워크플로우 (`aws-ecs-deploy.yml`)

**경로**: `JNU-econovation/soop-infra/.github/workflows/aws-ecs-deploy.yml`

#### 입력 파라미터 (inputs)
```yaml
repository-name:
  required: true
  type: string
git-ref:
  required: true
  type: string
aws-region:
  required: true
  type: string
ecr-repository:
  required: true
  type: string
ecs-cluster:
  required: true
  type: string
ecs-service:
  required: true
  type: string
container-name:
  required: true
  type: string
dockerfile-path:
  required: true
  type: string
build-context:
  required: true
  type: string
environment:
  required: true
  type: string
```

#### 필요한 시크릿 (secrets)
```yaml
AWS_ACCESS_KEY_ID:
  required: true
AWS_SECRET_ACCESS_KEY:
  required: true
SLACK_WEBHOOK_URL:
  required: true
ECS_SECRETS:
  required: true
```

#### 수행 작업
1. 백엔드 레포지토리 체크아웃
2. AWS 인증 설정
3. Docker Buildx 설정 및 캐싱
4. ECR 로그인
5. Docker 이미지 빌드 및 ECR 푸시
6. ECS task definition 업데이트 (백엔드 레포지토리의 `infra/ecs/task-definition.json` 사용)
7. ECS 서비스 배포
8. Slack 알림

### 2. 온프레미스 배포 워크플로우 (`onprem-deploy.yml`)

**경로**: `JNU-econovation/soop-infra/.github/workflows/onprem-deploy.yml`

#### 입력 파라미터 (inputs)
```yaml
repository-name:
  required: true
  type: string
git-ref:
  required: true
  type: string
java-version:
  required: true
  type: string
gradle-build-command:
  required: true
  type: string
jar-source-pattern:
  required: true
  type: string
jar-name:
  required: true
  type: string
server-port:
  required: true
  type: number
deploy-path:
  required: true
  type: string
```

#### 필요한 시크릿 (secrets)
```yaml
ONPREM_HOST:
  required: true
ONPREM_USER:
  required: true
ONPREM_SSH_PASSWORD:
  required: true
ONPREM_SSH_PORT:
  required: true
```

#### 수행 작업
1. 백엔드 레포지토리 체크아웃
2. JDK 설정
3. Gradle 빌드
4. SCP를 통한 JAR 파일 배포
5. SSH를 통한 애플리케이션 재시작

## 호출 방법

백엔드 레포지토리에서는 다음과 같이 워크플로우를 호출합니다:

### AWS ECS 배포
```yaml
jobs:
  call-aws-deploy:
    uses: JNU-econovation/soop-infra/.github/workflows/aws-ecs-deploy.yml@main
    with:
      # 파라미터 전달
    secrets:
      # 시크릿 전달
```

### 온프레미스 배포
```yaml
jobs:
  call-onprem-deploy:
    uses: JNU-econovation/soop-infra/.github/workflows/onprem-deploy.yml@main
    with:
      # 파라미터 전달
    secrets:
      # 시크릿 전달
```

## 장점

1. **중앙 집중식 관리**: 모든 배포 로직이 인프라 레포지토리에서 관리됨
2. **재사용성**: 여러 백엔드 프로젝트에서 동일한 워크플로우 사용 가능
3. **관심사 분리**: 애플리케이션 코드와 인프라 코드 분리
4. **유지보수성**: 배포 프로세스 변경 시 인프라 레포지토리만 수정하면 됨
5. **보안**: 민감한 배포 로직을 중앙에서 관리