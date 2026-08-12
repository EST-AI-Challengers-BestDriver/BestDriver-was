# BestDriver-was

BestDriver의 Spring Boot API 서버입니다.

## Team

|                              개발 / Leader                               |                                   개발                                    |                                   개발                                   |                                     개발                                     |                                    기획                                    |
| :--------------------------------------------------------------------: | :---------------------------------------------------------------------: | :--------------------------------------------------------------------: | :------------------------------------------------------------------------: | :----------------------------------------------------------------------: |
| <img src="https://github.com/slowgrid.png" width="100" height="100" /> | <img src="https://github.com/SOSO-0304.png" width="100" height="100" /> | <img src="https://github.com/yujin164.png" width="100" height="100" /> | <img src="https://github.com/sungmin-Yoon.png" width="100" height="100" /> | <img src="https://github.com/raccoon297.png" width="100" height="100" /> |
|                   [김형준](https://github.com/slowgrid)                   |                   [지소현](https://github.com/SOSO-0304)                   |                   [이유진](https://github.com/yujin164)                   |                   [윤성민](https://github.com/sungmin-Yoon)                   |                   [윤동환](https://github.com/raccoon297)                   |

## 커밋 컨벤션

[Conventional Commits](https://www.conventionalcommits.org/) 형식을 사용합니다.

```text
<type>: <subject>
```

| Type | 설명 |
| --- | --- |
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `refactor` | 기능 변화가 없는 코드 구조 개선 |
| `style` | 코드 동작에 영향을 주지 않는 포맷 변경 |
| `docs` | 문서 추가 또는 수정 |
| `test` | 테스트 추가 또는 수정 |
| `chore` | 패키지, 설정 등 기타 작업 |
| `revert` | 이전 커밋 되돌리기 |
| `merge`| 브랜치 병합 |

### 작성 규칙

- `type`은 영문 소문자로 작성합니다.
- 제목은 변경 내용을 명확하고 간결하게 작성하며 마침표를 붙이지 않습니다.
- 하나의 커밋에는 하나의 논리적인 변경만 포함합니다.
- 상세 설명이 필요하면 제목 다음에 빈 줄을 두고 본문을 작성합니다.
## 로컬 실행

Java 17과 Maven 3.9 이상을 사용합니다.

PostgreSQL을 먼저 실행합니다.

```bash
docker compose -f compose.local.yml up -d
```

애플리케이션을 실행합니다.

```bash
mvn spring-boot:run
```

- API: `http://localhost:8080`
- 상태 확인: `http://localhost:8080/actuator/health`
- 지원하는 데모 경로: `부산(역) → 서울(역)`

경로 분석 요청 예시:

```bash
curl -X POST http://localhost:8080/api/v1/route-analyses \
  -H "Content-Type: application/json" \
  -d '{"start":"부산역","destination":"서울역","vehicleType":"트럭"}'
```

현재 결과는 `MockEnergyPredictor`가 생성한 시연용 예상치입니다. 분석 이력과
선택 결과는 저장하지 않습니다. DB 구조는 ERDCloud의 `route_request`,
`route_candidate`, `energy_prediction`, `route_recommendation` 구조를 따릅니다.
현재는 부산→서울 템플릿 요청과 후보 경로만 저장하며, API 호출별 요청·예측·추천
이력은 새로 저장하지 않습니다. ONNX 모델이 준비되면 `EnergyPredictor` 구현체를
교체합니다.

## 검증

```bash
mvn --batch-mode verify
```

## CI/CD

`.github/workflows/pipeline.yml`은 pull request와 `main` push에서 테스트와 패키징을
수행합니다. `main` push가 성공하면 `ghcr.io/<owner>/<repository>:sha-<commit>`과
`latest` 이미지를 발행합니다. 저장소 변수 `EC2_CD_ENABLED`를 `true`로 설정한
경우에만 EC2의 `~/bestdriver`에 배포합니다.

운영 환경변수는 `deploy/was.runtime.env.example`을 참고해 EC2의
`~/bestdriver/was.runtime.env`에 작성합니다.
