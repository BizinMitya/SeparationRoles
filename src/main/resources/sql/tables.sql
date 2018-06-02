CREATE DATABASE IF NOT EXISTS `roles`;
USE `roles`;

DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `idProducts`   bigint(20)  NOT NULL AUTO_INCREMENT,
  `name`         varchar(45) NOT NULL,
  `manufacturer` varchar(45) NOT NULL,
  `cost`         int(11)     NOT NULL,
  `country`      varchar(45) NOT NULL,
  `description`  varchar(45) NOT NULL,
  `maskAccess`   bigint(19)  NOT NULL,
  PRIMARY KEY (`idProducts`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `idUser`   bigint(20)  NOT NULL AUTO_INCREMENT,
  `login`    varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `role`     varchar(45) NOT NULL,
  PRIMARY KEY (`idUser`, `login`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
