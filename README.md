## 프로젝트
- Blog API

## 인프라(예정)

## CI/CD(예정)

## 사용한 기술
- multi-module
- framework(spring) 
  - jpa, jpql, specification
    - jpa(단순 조회, 카운트 등)와 jpql(수정, 삭제 등)을 사용해 객체 영속성을 관리하고 db의 맵핑을 간단하게 구현
    - specification을 활용해 다중 키워드 조회시 jpa의 함수 및 함수명의 복잡함을 해결
  - security, jwt
    - security + jwt를 이용해 인증 및 인가에 대한 보안을 구현
    - refresh, access 두가지 토큰을 사용해 jwt의 stateless 특성을 강화하여 서버의 부하를 줄이고 보안성 향상 
  - feign client
    - 외부 API와의 연동에서 인터페이스를 자동으로 구현해주는 feign client 사용
  - oauth2.0
    - 자체적인 회원 관리 및 소셜 로그인을 구현하여 접근성 향상
  - event
  - circuit breaker
- database 
  - h2 db
    - 개발 및 테스트 단계에서 사용할 목적으로 가벼운 h2 db를 채택
  - mysql
    - 실제 배포 단계에서 사용할 목적으로 mysql 채택
  - redis
    - 스케쥴링이 필요한 항목 임시 저장 및 자동 삭제
    - 영속적 저장이 필요한 항목보다는 pub, sub가 중요한 항목에 대한 세션 관리
- web socket
  - 실시간 통신 기능 구현 목적
- docker(예정)
- circuit breaker(추가중)
- swagger(rest)


## 프로젝트 기능 구현 진행 상황
- auth
  - 소셜 로그인(oauth2.0)
  - 일반 로그인
    - 회원 가입, 로그인, 로그아웃
    - 관리자 전용 계정
- chat
  - 채팅방 입장, 퇴장
  - 실시간 메세지 송수신
  - 채팅 로그 redis2rdb 마이그레이션
  - 채팅 오면 이벤트 발행
- politics news
  - 뉴스 조회
  - 키워드 기반 랭킹 조회
- post
  - feed 
    - 작성, 조회, 수정, 삭제
    - 키워드 기반 조회
  - recommend 
    - 추천, 추천 취소
    - 비추천, 비추천 취소
  - reply
    - 작성, 수정, 삭제
  - stat
    - 추천수 기반 top n [누적, 일간]
    - 비추천수 기반 top n [누적, 일간]
    - 조회수 기반 top n [누적]

## 추가 작업 필요 목록
- 서킷브레이커 및 이벤트 발행
- 쪽지 기능 , 웹소켓
  - redis
    - 채팅방 메세지(1분)
    - 채팅방 사용자 목록
    - 사용자 세션 상태
    - 실시간 알림
  - rdb
    - 사용자 정보 
    - 채팅방 정보
    - 채팅 기록(하루에 한번씩 몰아서 저장)
- 스웨거 연결

- post
  - feed
    - 관리자 전용 기능 생성
    - 이미지 사용 (ex. 섬네일, 이미지 업로드)
- info
  - 사용자 정보
  - 회원 탈퇴
  - 활동 기록 조회


