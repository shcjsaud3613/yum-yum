-- MariaDB dump 10.19  Distrib 10.5.9-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: ssafydb
-- ------------------------------------------------------
-- Server version	10.5.9-MariaDB-1:10.5.9+maria~bionic

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `feeds`
--

DROP TABLE IF EXISTS `feeds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feeds` (
  `feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(200) DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `is_completed` bit(1) DEFAULT NULL,
  `modified_date` datetime NOT NULL,
  `score` bigint(20) DEFAULT NULL,
  `thumbnail_path` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `video_path` varchar(255) DEFAULT NULL,
  `place_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`feed_id`),
  KEY `FK2xvbxwlhi1teiib07b1p03wig` (`place_id`),
  KEY `FKa4nmt7wyx9clm9okj61dgd1tw` (`user_id`),
  CONSTRAINT `FK2xvbxwlhi1teiib07b1p03wig` FOREIGN KEY (`place_id`) REFERENCES `places` (`place_id`),
  CONSTRAINT `FKa4nmt7wyx9clm9okj61dgd1tw` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feeds`
--

LOCK TABLES `feeds` WRITE;
/*!40000 ALTER TABLE `feeds` DISABLE KEYS */;
INSERT INTO `feeds` VALUES (1,'너무 느끼하고 맛있어요!!','2021-05-20 17:05:35','','2021-05-20 17:06:04',4,'http://k4b206.p.ssafy.io/resources/media/1621497611791resotto_thumbnail.png','리조또','http://k4b206.p.ssafy.io/resources/media/1621497611791resotto.mp4',1,43),(2,'','2021-05-20 17:10:14','\0','2021-05-20 18:15:11',5,'http://k4b206.p.ssafy.io/resources/media/1621498145796chicken_thumbnail.png','치킨','http://k4b206.p.ssafy.io/resources/media/1621498145796chicken.mp4',4,43),(3,'기대 이하였어요 ㅠ.ㅠ','2021-05-20 17:11:29','','2021-05-20 18:10:35',2,'http://k4b206.p.ssafy.io/resources/media/1621498227100curry_thumbnail.png','커리','http://k4b206.p.ssafy.io/resources/media/1621498227100curry.mp4',3,43),(4,'맜있어요!','2021-05-20 17:47:05','','2021-05-20 17:47:05',3,'http://k4b206.p.ssafy.io/resources/media/1621499610032KakaoTalk_Video_2021-05-20-17-32-37_thumbnail.png','라면','http://k4b206.p.ssafy.io/resources/media/1621499610032KakaoTalk_Video_2021-05-20-17-32-37.mp4',1,59),(5,'바삭하니 맛있어요!','2021-05-20 17:51:18','','2021-05-20 17:51:18',5,'http://k4b206.p.ssafy.io/resources/media/162150067785008FBA0F5-7600-459D-A284-9AFA77947DED_thumbnail.png','피자','http://k4b206.p.ssafy.io/resources/media/162150067785008FBA0F5-7600-459D-A284-9AFA77947DED.mp4',2,59),(6,'이맛은?!','2021-05-20 18:22:15','\0','2021-05-20 18:22:15',5,'http://k4b206.p.ssafy.io/resources/media/1621502477033friedrice_thumbnail.png','치즈볶음밥','http://k4b206.p.ssafy.io/resources/media/1621502477033friedrice.mp4',NULL,43),(7,'미쳐따 미쳐써','2021-05-20 18:23:20','\0','2021-05-20 18:23:20',5,'http://k4b206.p.ssafy.io/resources/media/1621502568056waffle_thumbnail.png','크로플','http://k4b206.p.ssafy.io/resources/media/1621502568056waffle.mp4',NULL,43),(8,'돈까스는 역시 김밥천국 ㅠ.ㅠ','2021-05-20 18:24:10','','2021-05-20 18:24:33',1,'http://k4b206.p.ssafy.io/resources/media/1621502611066forkcutlet_thumbnail.png','돈까스','http://k4b206.p.ssafy.io/resources/media/1621502611066forkcutlet.mp4',5,43),(9,'육질이 여기보다 좋은 닭강정은 없다','2021-05-20 23:50:08','','2021-05-20 23:50:08',5,'http://k4b206.p.ssafy.io/resources/media/16214483521842021180425926_thumbnail.png','닭강정','http://k4b206.p.ssafy.io/resources/media/16214483521842021180425926.mp4',6,61),(10,'곱창에 곱이...부족..해..','2021-05-20 23:51:19','','2021-05-20 23:51:19',2,'http://k4b206.p.ssafy.io/resources/media/16214484101452021180939340_thumbnail.png','곱창전골','http://k4b206.p.ssafy.io/resources/media/16214484101452021180939340.mp4',7,61),(11,'고추바사삭은 언제나 옳다','2021-05-20 23:51:26','','2021-05-21 05:44:53',4,'http://k4b206.p.ssafy.io/resources/media/162144826009620211825644236_thumbnail.png','고추바사삭','http://k4b206.p.ssafy.io/resources/media/162144826009620211825644236.mp4',8,61),(12,'5살 적 아버지가 퇴근할 때 사오셨던 그 맛','2021-05-21 00:10:59','','2021-05-21 00:10:59',2,'http://k4b206.p.ssafy.io/resources/media/1621523135610202115297101_thumbnail.png','옛날 통닭','http://k4b206.p.ssafy.io/resources/media/1621523135610202115297101.mp4',9,29),(19,'정말 맛있었어요!','2021-05-21 02:52:35','','2021-05-21 02:52:35',5,'http://k4b206.p.ssafy.io/resources/media/1621533155125721953A7-F2FB-4BAF-9239-ACB9E763AA40_thumbnail.png','라면','http://k4b206.p.ssafy.io/resources/media/1621533155125721953A7-F2FB-4BAF-9239-ACB9E763AA40.mp4',14,59),(23,'붱궐레 하나!','2021-05-21 10:20:36','\0','2021-05-21 10:20:36',4,'http://k4b206.p.ssafy.io/resources/media/1621559967490pasta_thumbnail.png','봉골레파스타','http://k4b206.p.ssafy.io/resources/media/1621559967490pasta.mp4',NULL,43),(24,'봉명동 최고맛집!!','2021-05-21 10:21:39','\0','2021-05-21 10:21:39',5,'http://k4b206.p.ssafy.io/resources/media/1621560048562curry2_thumbnail.png','푸팟퐁커리','http://k4b206.p.ssafy.io/resources/media/1621560048562curry2.mp4',NULL,43),(25,'쌀국수는 제스타일이 아니네유..','2021-05-21 10:23:41','\0','2021-05-21 10:23:41',1,'http://k4b206.p.ssafy.io/resources/media/1621560140737ricenoodle_thumbnail.png','쌀국수','http://k4b206.p.ssafy.io/resources/media/1621560140737ricenoodle.mp4',NULL,43),(26,'마늘향이 강하고 마쉿!!','2021-05-21 10:24:46','\0','2021-05-21 10:24:46',3,'http://k4b206.p.ssafy.io/resources/media/1621560232924spicybackrips_thumbnail.png','매운 갈비찜','http://k4b206.p.ssafy.io/resources/media/1621560232924spicybackrips.mp4',NULL,43);
/*!40000 ALTER TABLE `feeds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `follows`
--

DROP TABLE IF EXISTS `follows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `follows` (
  `follow_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `follower_id` bigint(20) DEFAULT NULL,
  `host_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`follow_id`),
  KEY `FKqnkw0cwwh6572nyhvdjqlr163` (`follower_id`),
  KEY `FK8wg7gwp5wsifmy7pv08g1gs9k` (`host_id`),
  CONSTRAINT `FK8wg7gwp5wsifmy7pv08g1gs9k` FOREIGN KEY (`host_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKqnkw0cwwh6572nyhvdjqlr163` FOREIGN KEY (`follower_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follows`
--

LOCK TABLES `follows` WRITE;
/*!40000 ALTER TABLE `follows` DISABLE KEYS */;
/*!40000 ALTER TABLE `follows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `likes`
--

DROP TABLE IF EXISTS `likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `likes` (
  `like_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`like_id`),
  KEY `FKlcg11aub6xguvro342ug7uxvv` (`feed_id`),
  KEY `FKnvx9seeqqyy71bij291pwiwrg` (`user_id`),
  CONSTRAINT `FKlcg11aub6xguvro342ug7uxvv` FOREIGN KEY (`feed_id`) REFERENCES `feeds` (`feed_id`),
  CONSTRAINT `FKnvx9seeqqyy71bij291pwiwrg` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likes`
--

LOCK TABLES `likes` WRITE;
/*!40000 ALTER TABLE `likes` DISABLE KEYS */;
INSERT INTO `likes` VALUES (1,1,43),(3,1,61),(4,1,24),(7,4,59),(8,3,59),(10,4,24),(11,3,24),(12,5,24),(13,8,24),(20,5,61),(21,12,59),(24,8,59),(27,12,61),(28,11,61),(29,9,61),(30,10,61);
/*!40000 ALTER TABLE `likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phones`
--

DROP TABLE IF EXISTS `phones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phones` (
  `phone_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `phone_header` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`phone_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phones`
--

LOCK TABLES `phones` WRITE;
/*!40000 ALTER TABLE `phones` DISABLE KEYS */;
/*!40000 ALTER TABLE `phones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `places`
--

DROP TABLE IF EXISTS `places`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `places` (
  `place_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `location_x` double DEFAULT NULL,
  `location_y` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`place_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `places`
--

LOCK TABLES `places` WRITE;
/*!40000 ALTER TABLE `places` DISABLE KEYS */;
INSERT INTO `places` VALUES (1,'대전 유성구 도룡동 4-18',127.39409703618195,36.37530447691212,'더빛나','042-863-6326'),(2,'대전 동구 가오동 235-26',127.45225341257996,36.30609828392011,'피자헛 대전가오점','042-335-5589'),(3,'대전 유성구 궁동 395-2',127.34762844463,36.3631215860454,'커리컬쳐 궁동점','042-825-2321'),(4,'대전 유성구 구암동 669-4',127.306996427394,36.3589180679582,'BHC치킨 한밭대점','042-823-9975'),(5,'대전 유성구 지족동 905-5',127.31888589621559,36.37236579481404,'쑝쑝돈까스 대전노은점','042-822-5383'),(6,'대전광역시 유성구 궁동 48-39',35.6,128.4,'오늘닭강정 궁동점','010-8392-4344'),(7,'부산광역시 금정구 장전 2동 482번지',35.6,128.4,'만석곱창 장전점','010-8392-4344'),(8,'굽네치킨 사상점',35.6,128.4,'고추바사삭','010-8392-4344'),(9,'대전광역시 유성구 궁동 48-39',35.6,128.4,'옛날통닭 사상점','010-8392-4344'),(10,'대전 유성구 봉명동 617-1',127.343392539443,36.3576222517127,'상무초밥 유성점','042-825-8983'),(11,'대전 유성구 봉명동 621-1',127.345886572125,36.3587622536858,'경성삼겹살',''),(12,'서울 강남구 신사동 583-3',127.02777914657199,37.5239082745753,'김밥천국 압구정점','02-3448-5009'),(13,'대전 서구 갈마동 266-1',127.37115291088,36.3539669915273,'오한순손수제비','042-301-3484'),(14,'대전 동구 용전동 172-4',127.43206278163355,36.35245917970843,'김밥천국 용전점','042-632-3233');
/*!40000 ALTER TABLE `places` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime NOT NULL,
  `email` varchar(255) NOT NULL,
  `introduction` varchar(255) DEFAULT NULL,
  `modified_date` datetime NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `profile_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2021-04-22 09:54:43','ssafy@naver.com','한줄로 자신을 소개해주세요.','2021-04-22 09:54:43','김싸피',''),(3,'2021-04-22 23:13:31','weekyear@gmail.com','string','2021-04-22 23:13:31','string',''),(4,'2021-04-26 13:07:56','ssafy@a.com','yumyumhi','2021-04-26 13:07:56','quokka',''),(5,'2021-04-26 14:13:31','a@a.com','하이하이','2021-04-26 14:13:31','염염','http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg'),(6,'2021-04-26 14:28:52','ssafy1@a.com','제발되라제발되라','2021-04-26 14:28:52','별명1',''),(7,'2021-04-26 14:32:05','ssafy2@a.com','하이하이','2021-04-26 14:32:05','염염쓰',''),(8,'2021-04-26 14:48:03','ssafy3@a.com','그만해 ㅠㅠ','2021-04-26 14:48:03','멈춰!',''),(9,'2021-04-26 16:38:13','ssafy4@a.com','안녕하셨나요?','2021-04-26 16:38:13','염염2',''),(10,'2021-04-26 18:22:31','qq@qq.qq','안녕하세요','2021-04-26 18:22:31','popopo','http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg'),(11,'2021-04-26 23:49:21','ssafy5@a.com','안녕하하십니까','2021-04-26 23:49:21','염염쓰',''),(12,'2021-04-26 23:55:07','ssafy6@a.com','안녕하세요?','2021-04-26 23:55:07','염쓰','http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg'),(13,'2021-04-26 23:57:30','ssafy7@a.com','안녕하세요?','2021-04-26 23:57:30','염쓰','http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg'),(14,'2021-04-27 12:07:10','ssafy10@a.com','sdfsdfs','2021-04-27 12:07:10','dad dad',''),(24,'2021-04-27 21:52:57','ahyeonlog@gmail.com','ㅎㅎ','2021-05-20 11:55:20','아롱','http://k4b206.p.ssafy.io/resources/profile/1621479318951file.jpeg'),(29,'2021-04-28 18:29:12','jwnsgus@gmail.com','I am Android Developer','2021-05-10 15:29:18','weekyear','http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg'),(43,'2021-04-29 15:28:19','deckloc603@gmail.com','맛잇국','2021-05-05 19:02:52','뼈국','http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg'),(53,'2021-05-01 05:18:47','hi@hi.com','d','2021-05-01 05:18:47','d','http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg'),(54,'2021-05-01 05:29:24','ahyeonway@gmail.com','클레오파트라','2021-05-01 05:29:24','안녕',''),(59,'2021-05-05 18:27:12','hysung01@gmail.com','할수있어어!','2021-05-17 16:56:50','도비는 자유예요','http://k4b206.p.ssafy.io/resources/profile/1621238195708file.jpeg'),(60,'2021-05-09 18:45:42','deckloc103@gmail.com','구뜨구뜨','2021-05-09 18:45:42','Potato','http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg'),(61,'2021-05-17 02:29:46','jweekyear@gmail.com','맛을 추적하는 자','2021-05-17 02:29:46','week.year','http://k4b206.p.ssafy.io/resources/profile/1621186185287IMG_20210430_154634.jpg'),(62,'2021-05-17 20:17:16','ashandoil51@gmail.com','탕','2021-05-17 20:17:16','감자','http://k4b206.p.ssafy.io/resources/profile/1621250236780Screenshot_20210505-134040_YouTube.jpg');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-21 10:24:52
