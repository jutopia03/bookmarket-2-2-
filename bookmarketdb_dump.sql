CREATE DATABASE  IF NOT EXISTS `bookmarketdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bookmarketdb`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: bookmarketdb
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `book_id` varchar(30) NOT NULL,
  `name` varchar(200) NOT NULL,
  `unit_price` int NOT NULL,
  `author` varchar(100) DEFAULT NULL,
  `description` text,
  `category` varchar(50) DEFAULT NULL,
  `release_date` varchar(20) DEFAULT NULL,
  `stock` int NOT NULL DEFAULT '0',
  `reg_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES ('ISBN-001','자바 프로그래밍 입문',25000,'홍길동','자바 기초부터 객체지향까지 설명한 입문서','프로그래밍','2020-03',10,'2025-11-25 11:35:48'),
                    ('ISBN-002','파이썬으로 배우는 알고리즘',28000,'이알고','알고리즘 구현과 문제 풀이 중심','알고리즘','2021-07',8,'2025-11-25 11:35:48'),
                    ('ISBN-003','데이터베이스 개론',30000,'김DB','관계형 데이터베이스 기본 개념과 SQL','데이터베이스','2019-02',5,'2025-11-25 11:35:48'),
                    ('ISBN-004','운영체제 이해',27000,'박OS','운영체제 이론과 실습 예제','운영체제','2018-11',4,'2025-11-25 11:35:48'),
                    ('ISBN-005','네트워크 프로그래밍',32000,'최NET','TCP/IP와 소켓 프로그래밍','네트워크','2022-01',7,'2025-11-25 11:35:48'),
                    ('ISBN-006','테스트용 책',10000,'테스터','테스트용 설명',NULL,NULL,0,'2025-11-25 14:37:21'),
                    ('ISBN-007','머신러닝 기초',35000,'김ML','머신러닝 개념과 실습 예제','인공지능','2014-09',6,'2025-11-25 11:35:48'),
                    ('ISBN-008','딥러닝 완전정복',42000,'박DL','심층 신경망 모델 핵심 설명','인공지능','2022-04',9,'2025-11-25 11:35:48'),
                    ('ISBN-009','자료구조와 알고리즘',29000,'이구조','자료구조 개념과 알고리즘 분석','컴퓨터과학','2017-10',12,'2025-11-25 11:35:48'),
                    ('ISBN-010','C언어 기본서',23000,'최C','C언어 문법과 실습 중심 입문서','프로그래밍','2011-05',14,'2025-11-25 11:35:48'),
                    ('ISBN-011','파이썬 데이터 분석',33000,'정DATA','Pandas 중심 데이터 분석 입문','데이터분석','2019-12',7,'2025-11-25 11:35:48'),
                    ('ISBN-012','컴퓨터 구조 이해',31000,'한ARCH','컴퓨터 구조와 CPU 동작 원리','컴퓨터구조','2013-07',5,'2025-11-25 11:35:48'),
                    ('ISBN-013','HTML/CSS 웹 입문',20000,'웹초보','HTML과 CSS를 통한 웹 제작 입문','웹개발','2010-03',11,'2025-11-25 11:35:48'),
                    ('ISBN-014','JavaScript 실전 가이드',27000,'JS고수','JS 핵심 문법과 실습 프로젝트','웹개발','2018-08',10,'2025-11-25 11:35:48'),
                    ('ISBN-015','리눅스 시스템 관리',36000,'유닉스맨','리눅스 환경 설정과 관리 실습','시스템','2016-02',8,'2025-11-25 11:35:48'),
                    ('ISBN-016','컴퓨터 네트워크 원리',34000,'네트워크박','OSI 7계층과 프로토콜 설명','네트워크','2023-01',6,'2025-11-25 11:35:48'),
                    ('ISBN-017','정보보안 입문',30000,'보안초보','기본 보안 개념과 해킹 기초','보안','2015-11',4,'2025-11-25 11:35:48'),
                    ('ISBN-018','자바스크립트 알고리즘',26000,'코딩러','JS로 배우는 알고리즘 문제 풀이','알고리즘','2020-06',9,'2025-11-25 11:35:48'),
                    ('ISBN-019','파이썬 웹 크롤링',25000,'크롤링짱','BeautifulSoup 기반 크롤링 기초','데이터수집','2024-03',13,'2025-11-25 11:35:48'),
                    ('ISBN-020','AI 윤리와 미래',22000,'AI철학','AI 기술 발전과 윤리 문제 논의','인공지능','2021-10',3,'2025-11-25 11:35:48'),
                    ('ISBN-021','UX/UI 디자인 개론',28000,'디자인러','사용자 경험 설계 기본 원칙','디자인','2012-04',7,'2025-11-25 11:35:48'),
                    ('ISBN-022','소프트웨어 공학',31000,'SW엔지니어','소프트웨어 개발 생명주기 설명','소프트웨어공학','2018-01',12,'2025-11-25 11:35:48'),
                    ('ISBN-023','클라우드 컴퓨팅 개론',33000,'클라우드맨','AWS·Azure·GCP 개요 설명','클라우드','2020-09',5,'2025-11-25 11:35:48'),
                    ('ISBN-024','자연어 처리의 이해',37000,'NLP박','텍스트 처리와 모델링 개요','인공지능','2017-12',9,'2025-11-25 11:35:48'),
                    ('ISBN-025','게임 프로그래밍 기초',27000,'게임초보','게임 엔진과 기초 로직 설명','게임개발','2014-02',6,'2025-11-25 11:35:48'),
                    ('ISBN-026','안드로이드 앱 개발',30000,'APP개발자','안드로이드 스튜디오 활용 앱 개발','모바일','2016-09',11,'2025-11-25 11:35:48'),
                    ('ISBN-027','iOS 앱 프로그래밍',32000,'APPLEDEV','Swift 기반 iOS 앱 개발 입문','모바일','2022-05',7,'2025-11-25 11:35:48'),
                    ('ISBN-028','데이터 분석 프로젝트',35000,'분석마스터','실전 데이터 분석 프로젝트 진행','데이터분석','2013-11',4,'2025-11-25 11:35:48'),
                    ('ISBN-029','백엔드 개발 실무',34000,'서버개발자','API 개발과 서버 구조 설명','백엔드','2025-02',8,'2025-11-25 11:35:48'),
                    ('ISBN-030','프론트엔드 마스터북',36000,'FE장인','React 중심 프론트엔드 실전서','프론트엔드','2011-06',10,'2025-11-25 11:35:48');
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `cart_id` int NOT NULL AUTO_INCREMENT,
  `member_id` int NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`cart_id`),
  KEY `fk_cart_member` (`member_id`),
  CONSTRAINT `fk_cart_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_item`
--

DROP TABLE IF EXISTS `cart_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_item` (
  `cart_item_id` int NOT NULL AUTO_INCREMENT,
  `cart_id` int NOT NULL,
  `book_id` varchar(30) NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  `total_price` int NOT NULL,
  PRIMARY KEY (`cart_item_id`),
  KEY `fk_cart_item_cart` (`cart_id`),
  KEY `fk_cart_item_book` (`book_id`),
  CONSTRAINT `fk_cart_item_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`book_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_cart_item_cart` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`cart_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_item`
--

LOCK TABLES `cart_item` WRITE;
/*!40000 ALTER TABLE `cart_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `member_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(50) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `address` varchar(200) DEFAULT NULL,
  `role` enum('USER','ADMIN') NOT NULL DEFAULT 'USER',
  `reg_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `uq_member_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (1,'admin','admin1234','관리자','010-0000-0000','온라인 서점 본사','ADMIN','2025-11-25 11:35:54'),(2,'user1','user1234','김학생','010-1111-1111','대전시 유성구 어딘가','USER','2025-11-25 11:35:54'),(3,'user2','user1234','이학생','010-2222-2222','대전시 동구 어딘가','USER','2025-11-25 11:35:54');
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `order_item_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `book_id` varchar(30) NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` int NOT NULL,
  PRIMARY KEY (`order_item_id`),
  KEY `fk_order_item_orders` (`order_id`),
  KEY `fk_order_item_book` (`book_id`),
  CONSTRAINT `fk_order_item_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`book_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_order_item_orders` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `member_id` int NOT NULL,
  `order_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('ORDERED','CANCELED') NOT NULL DEFAULT 'ORDERED',
  `total_price` int NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `fk_orders_member` (`member_id`),
  CONSTRAINT `fk_orders_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-25 15:01:23


