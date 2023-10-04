-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: newflow
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `water_level`
--

DROP TABLE IF EXISTS `water_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `water_level` (
  `target_id` int NOT NULL,
  `observe_datetime` varchar(45) NOT NULL,
  `water_level` double DEFAULT NULL,
  PRIMARY KEY (`target_id`,`observe_datetime`),
  KEY `fk_water_level_target1_idx` (`target_id`),
  CONSTRAINT `fk_water_level_target1` FOREIGN KEY (`target_id`) REFERENCES `target` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `water_level`
--

LOCK TABLES `water_level` WRITE;
/*!40000 ALTER TABLE `water_level` DISABLE KEYS */;
INSERT INTO `water_level` VALUES (1,'2023100312',1.16),(1,'2023100313',0.97),(1,'2023100314',0.57),(1,'2023100315',0.2),(1,'2023100316',0.04),(1,'2023100317',-0.04),(1,'2023100318',0.09),(1,'2023100319',0.39),(1,'2023100405',-0.04),(1,'2023100406',0.05),(1,'2023100407',0.22),(1,'2023100408',0.51),(1,'2023100409',0.76),(1,'2023100410',0.97),(1,'2023100411',1.15),(1,'2023100412',1.08),(1,'2023100413',1.01),(1,'2023100414',0.78);
/*!40000 ALTER TABLE `water_level` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-04 15:09:39
