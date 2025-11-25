# BookMarketSwing

JAVA 프로그래밍 수업 팀프로젝트 - 온라인 서점 프로그램 (Swing + JDBC)

## 실행 환경
- JDK 17 (또는 사용 버전)
- MySQL 8.x
- MySQL JDBC Driver (mysql-connector-j … .jar)

## 데이터베이스 설정
1. MySQL Workbench에서 `bookmarket` 스키마 생성
2. `db/bookmarket.sql` 실행해서 테이블 + 예제 데이터 생성
3. `src/com/market/common/DBUtil.java` 에서
   - URL, USER, PASSWORD 를 본인 환경에 맞게 수정

## 실행 방법
1. Eclipse에서 이 프로젝트 import
2. `Welcome.java` (또는 메인 클래스) 실행

## 주요 기능
- 도서 목록 조회 / 장바구니 / 주문 기능
- (앞으로 추가할 기능들 TODO 목록)

## TODO
- 회원 로그인 기능
- 주문 내역 조회
- UI 개선 등
