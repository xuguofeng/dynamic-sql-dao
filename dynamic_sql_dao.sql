/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50559
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50559
File Encoding         : 65001

Date: 2018-07-03 11:44:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for base_dao_department
-- ----------------------------
DROP TABLE IF EXISTS `base_dao_department`;
CREATE TABLE `base_dao_department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `parent_department_id` int(11) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `parent_dept_fk` (`parent_department_id`),
  CONSTRAINT `parent_dept_fk` FOREIGN KEY (`parent_department_id`) REFERENCES `base_dao_department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for base_dao_employee
-- ----------------------------
DROP TABLE IF EXISTS `base_dao_employee`;
CREATE TABLE `base_dao_employee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL,
  `gender` int(11) NOT NULL DEFAULT '1',
  `salary` float DEFAULT NULL,
  `phone` char(11) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `birthday` date NOT NULL,
  `join_date` date NOT NULL,
  `create_time` datetime NOT NULL,
  `department_id` int(11) DEFAULT NULL,
  `version` int(255) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `emp_name_unique` (`name`),
  KEY `emp_dept+fk` (`department_id`),
  CONSTRAINT `emp_dept+fk` FOREIGN KEY (`department_id`) REFERENCES `base_dao_department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for base_dao_student
-- ----------------------------
DROP TABLE IF EXISTS `base_dao_student`;
CREATE TABLE `base_dao_student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for base_dao_student_teacher
-- ----------------------------
DROP TABLE IF EXISTS `base_dao_student_teacher`;
CREATE TABLE `base_dao_student_teacher` (
  `student_id` int(11) NOT NULL,
  `teacher_id` int(11) NOT NULL,
  KEY `student_fk` (`student_id`),
  KEY `teacher_fk` (`teacher_id`),
  CONSTRAINT `student_fk` FOREIGN KEY (`student_id`) REFERENCES `base_dao_student` (`id`),
  CONSTRAINT `teacher_fk` FOREIGN KEY (`teacher_id`) REFERENCES `base_dao_teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for base_dao_teacher
-- ----------------------------
DROP TABLE IF EXISTS `base_dao_teacher`;
CREATE TABLE `base_dao_teacher` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
