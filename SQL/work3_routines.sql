CREATE DATABASE  IF NOT EXISTS `work3` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `work3`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: work3
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
-- Temporary view structure for view `order_report`
--

DROP TABLE IF EXISTS `order_report`;
/*!50001 DROP VIEW IF EXISTS `order_report`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `order_report` AS SELECT 
 1 AS `orderid`,
 1 AS `member_name`,
 1 AS `gmail`,
 1 AS `products_detail`,
 1 AS `total`,
 1 AS `employee_name`,
 1 AS `date`,
 1 AS `payment_method`,
 1 AS `wallet_balance`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `order_report`
--

/*!50001 DROP VIEW IF EXISTS `order_report`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `order_report` AS select `o`.`orderid` AS `orderid`,`m`.`name` AS `member_name`,`m`.`gmail` AS `gmail`,group_concat(concat(`p`.`name`,' x',`oi`.`amount`,' (',(`p`.`price` * `oi`.`amount`),'元)') separator '\n            ') AS `products_detail`,sum((`p`.`price` * `oi`.`amount`)) AS `total`,`e`.`name` AS `employee_name`,`o`.`date` AS `date`,`o`.`payment_method` AS `payment_method`,`o`.`wallet_after` AS `wallet_balance` from ((((`orders` `o` join `order_items` `oi` on((`o`.`orderid` = `oi`.`orderid`))) join `member` `m` on((`o`.`memberid` = `m`.`memberid`))) join `product` `p` on((`oi`.`productid` = `p`.`productid`))) left join `employee` `e` on((`o`.`employeeid` = `e`.`employeeid`))) group by `o`.`orderid` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-20 17:05:04
