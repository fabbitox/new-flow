-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: flow
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Table structure for table `aws_value`
--

DROP TABLE IF EXISTS `aws_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aws_value` (
  `id` int NOT NULL AUTO_INCREMENT,
  `temperature` double DEFAULT NULL,
  `wind_direction` double DEFAULT NULL,
  `wind_speed` double DEFAULT NULL,
  `rainfall` double DEFAULT NULL,
  `humidity` double DEFAULT NULL,
  `aws_id` int NOT NULL,
  `input_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_aws_value_aws1_idx` (`aws_id`),
  KEY `fk_aws_value_input1_idx` (`input_id`),
  CONSTRAINT `fk_aws_value_aws1` FOREIGN KEY (`aws_id`) REFERENCES `aws` (`id`),
  CONSTRAINT `fk_aws_value_input1` FOREIGN KEY (`input_id`) REFERENCES `input` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aws_value`
--

LOCK TABLES `aws_value` WRITE;
/*!40000 ALTER TABLE `aws_value` DISABLE KEYS */;
INSERT INTO `aws_value` VALUES (1,18,10,1,1,90,1,1),(2,17,311,1,1,95,2,1),(3,17,255,2,1,90,3,1),(4,17,28,1,2,90,1,2),(5,18,317,1,2,90,2,2),(6,17,253,1,1,85,3,2),(7,18,10,1,0,80,1,3),(8,17,355,1,0,90,2,3),(9,17,356,1,0,85,3,3),(10,17,120,0,0,90,1,4),(11,17,267,1,0,95,2,4),(12,17,273,1,1,85,3,4),(13,17,29,0,0,85,1,5),(14,17,303,1,0,90,2,5),(15,17,292,1,0,90,3,5),(16,17,16,1,0,85,1,6),(17,17,354,1,0,85,2,6),(18,17,359,2,0,85,3,6),(19,17,120,0,0,90,1,7),(20,17,267,1,0,95,2,7),(21,17,273,1,1,85,3,7),(22,17,29,0,0,85,1,8),(23,17,303,1,1,90,2,8),(24,17,292,1,1,90,3,8),(25,17,16,1,0,85,1,9),(26,17,354,1,0,85,2,9),(27,17,359,2,0,85,3,9),(28,17,159,0,0,90,1,10),(29,17,290,1,0,95,2,10),(30,18,310,2,1,85,3,10),(31,17,311,0,0,85,1,11),(32,17,311,1,1,90,2,11),(33,17,319,2,1,90,3,11),(34,17,7,1,0,85,1,12),(35,17,347,1,0,85,2,12),(36,18,352,2,0,85,3,12);
/*!40000 ALTER TABLE `aws_value` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-03 19:54:00
