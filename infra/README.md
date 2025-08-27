# Infrastructure Configuration

이 디렉토리는 인프라 레포지토리에서 사용하는 설정 파일들을 포함합니다.

## ECS 배포 설정

### task-definition.json
AWS ECS 태스크 정의 파일입니다. 인프라 레포지토리의 워크플로우에서 이 파일을 사용하여 ECS 서비스를 배포합니다.

#### 주요 구성요소:
- **Container 이름**: application
- **포트 매핑**: 8080
- **환경 변수**: AWS Secrets Manager를 통해 관리
- **로깅**: CloudWatch Logs 사용

## 워크플로우 통합

백엔드 레포지토리의 CI/CD 워크플로우는 다음과 같이 인프라 레포지토리를 호출합니다:

### AWS ECS 배포 (back-dev.yml)
```yaml
uses: JNU-econovation/soop-infra/.github/workflows/aws-ecs-deploy.yml@main
```

### 온프레미스 배포 (onprem-dev.yml)
```yaml
uses: JNU-econovation/soop-infra/.github/workflows/onprem-deploy.yml@main
```

이 구조를 통해 배포 로직은 인프라 레포지토리에서 중앙 관리되며, 백엔드 레포지토리는 필요한 매개변수와 시크릿만 전달합니다.