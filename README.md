# 일정 관리 앱
- 레벨별로 브랜치를 분리했습니다.
## API 명세
| 기능       | Method | URL             | request                          | response  | 상태 코드 |
|----------|--------|-----------------|----------------------------------|-----------|-------|
| 전체 일정 조회 | GET    | /schedules      | 요청 param (updatedDate, author)   | 일정 정보 리스트 | 200   |
| 단일 일정 조회 | GET    | /schedules/{id} | -                                | 일정 정보     | 200   |
| 일정 생성    | POST   | /schedules      | 요청 body (task, author, password) | -         | 201   |

## ERD
![erd.png](erd.png)