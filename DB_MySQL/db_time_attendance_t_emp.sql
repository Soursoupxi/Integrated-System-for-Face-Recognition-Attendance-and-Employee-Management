-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: db_time_attendance
-- ------------------------------------------------------
-- Server version	8.3.0

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
-- Table structure for table `t_emp`
--

DROP TABLE IF EXISTS `t_emp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_emp` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `code` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_emp`
--

LOCK TABLES `t_emp` WRITE;
/*!40000 ALTER TABLE `t_emp` DISABLE KEYS */;
INSERT INTO `t_emp` VALUES (1,'test男青年2号','0ee4c9fd608d4322aa55054086e34768'),(2,'test男青年3号','34ea4066437d4ebaa4e408a979eff961'),(3,'test男青年4号','1cd94b10afc04354b1e20efe105b4b13'),(4,'test女青年1号','d40f95e3cec9481b9372429fe4d02a80'),(5,'test老头1号','6a1e79a2b3a144029b711bf29310cc8b'),(6,'test老太1号','a3cc6ede42b24b0dae223ff5ca3ad4de'),(7,'test男孩1号','0de8905ef4f44acdaf22652b937894be'),(8,'test女孩1号','37103dbad2f143d98742294189a2e249'),(9,'test老头2号','c1d49718420b4771b778ba144b2c667e'),(10,'郑德运','8234914798d8451faf23390e957d2a5b'),(11,'毛展鹏','393a294bd73d4c47a67b9ae4bd1b6eeb'),(12,'史宾鸿','a79c02dfa65c4742ab2ccc2849a702e9'),(13,'高宏逸','9bc579945ce34da68eb372058b767480'),(14,'吴修伟','7265e30cfcac4ef488d5038b7707e4ab'),(15,'Antonio Mcgoon','22acf006dddc4c14acfeebff9c01fe9a'),(16,'Shitala Singh','d1c3545e08e9441e92cb14e0f2af8f01'),(17,'Romeo Miles','077492c1b03d4619b7b7fe548be78947'),(18,'Dana Bean','aeda1c3a841d4156b511f687dc6267dd'),(19,'Susie Alexander','591b8df399294b0780a8005bf29fcd00'),(20,'Rahul Ojha','205b8fd7a11e4efe85a80236e5753bf4'),(21,'Perry Sanford','33e253148fe440adab4cb7e3caf2c582'),(22,'كليم العزر','c308e344dff140fcbaa1e813c5a95f54'),(23,'حارث العبود','c90d73faf19846f3b417752e8faacfb2'),(24,'نصيرة القاضي','a27508a4a57941c3a3c49756ca3b04e5'),(25,'高井一代','10d60a1f78e04154b353d462dca12813'),(26,'川田八雲','b3354b6926e34dcc8ab7a98fc2eb7677'),(27,'山部義貞','9243dded65864ccab52eb6c0a6baee0e'),(28,'河野長徳','5d85f9b525004172bd349f6bf75fa707'),(29,'松本日蓮','f747ab46e0a44b98b8d2bc919027244f'),(30,'하해숙','e55c5ed49ef64910a0f89b5552dd277a'),(31,'지윤호','82e5ce7962d74477b030d6bf0676251c'),(32,'정인표','237dbb25064b4d1b8ffe907682da1b6b'),(33,'曹冠宇','cc83d4597fb24d49945e00af40962d35'),(34,'丁雨星','420f29d7df27410d8dc1ca89d2b566fb'),(35,'黄文轩','9fef6f93fe6c4d2e89a11653106930e2'),(36,'卢俊智','f7c95abcb25548929926195ad978d1a3'),(37,'马巍奕','03da6800f4184ef7aa7a339d4d131ac1'),(39,'张舒方','afdb29672c874205b973d17048b4ede7'),(40,'步春雁','eec8f720c24f42919cedb62be6997be5'),(41,'林晨曦','8d9ce9eb4ee245d58030f1181bc7dc63'),(42,'邹振文','77112f2f2b9f47b68734e0d869beb4d7'),(43,'张晓旋','2ed0b15e5489415f8a594d8e9b0ad917'),(44,'李仓','0113f4075f9e4896b387396ed04ca7d8'),(45,'白倡','29b715db682742f2ab9ae9ca4422d35a'),(46,'蔡前','f33d20ead8ec43c2b966cffc00d8074e'),(47,'陈棋','38bd410af63e42b683003035ad3e4b14'),(48,'辛玚','766bdd8bf4484973a21d2f20eb32339f'),(49,'夏侯荭','0fe3a8ef0e084413908d2ab2491e1951'),(50,'孙之槐','4bd3259568554a19ad351251c4077277'),(51,'习靖易','48093326f4ba4b2ea6e3d0f412bb6541'),(52,'Edward Chellis','daf796c57c244e818172b6f327ff80aa'),(53,'Edmund Stinnett','b161999640334fc2bfaa3d1be49b6cb4'),(54,'Pat Verigan','52e650d65810482981a98eb57de6bc06'),(55,'Elena Evans','97810f32af9f4e4f9a023f2f26b12b6c'),(56,'Aaliyah James','978acc8c1dd444d5a587ffc7c154857c'),(57,'Матвей Волков','e2fa8338f81444eea5df9ec3b6ef1b86'),(58,'Тимофей Давидов','85b38f0b4ce54d72aac515b389548882'),(59,'Александра Бабински','a76b79da1d4243ab82b8cfbe3f9e3531'),(60,'Мариша Липовская','8eacb0c290ba44ba907c747b8312b061'),(61,'Вася Генрих','6a6f27895e6c428288d5cf583a48c15a'),(62,'Ian Anderson','3be7251495c847d5b976f7b0e134b3c4'),(63,'Wesley Gray','f0653834d9454ef788be70a66dc3643d'),(64,'Emily Taylor','8932eb0e436e4d18888db73c799d8d8d'),(65,'Sarah Wilson','06177175f423498bb7aefec962f40383'),(66,'Sarah Wilson','6c3f2d795c8b48b09b8871fd1b4d0066'),(67,'Clemente Pomar','a0a9e9debd3c43b8b5242b866fc07a23'),(68,'Beatrisa Cabezas','496989e15cad46d6bc5447ef4d823a17'),(69,'Leira Del Pozo','21f0a3dd497d4ae68a295c564694bbd5'),(70,'Gervaso Ureña','e7ac1c11d1dd47c18cd391f5b3e8ef29'),(71,'Noé Nepomuceno','52b7da027b3b4a14a8fa5fce47e63c1a'),(72,'Estefanía Bandera','45681e2b77224baeb9d166555622a90f'),(73,'Esperanza Gutiérrez','9bfe6d512f1c4502a7bb8c5833d933cc'),(74,'天願賢治','3008ca93f42d4a378f5c5d246c569a44'),(75,'川田寅次郎','89d417cb6d2e481db0ee00b413a5515a'),(76,'石川菊五郎','fc3cbfe923d844fd88928bfcaff2483b'),(77,'岩井雪季','9ebfbad2ba0f45c186cfcc92185845fd'),(78,'黒沢親房','9e49959e6f7e4137bfc9a8c8101b48fd');
/*!40000 ALTER TABLE `t_emp` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-22 10:33:40
