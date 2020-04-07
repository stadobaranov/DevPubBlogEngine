/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Дамп структуры базы данных DevPubBlogEngine
DROP DATABASE IF EXISTS `DevPubBlogEngine`;
CREATE DATABASE IF NOT EXISTS `DevPubBlogEngine` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `DevPubBlogEngine`;

-- Дамп структуры для таблица DevPubBlogEngine.captcha_codes
DROP TABLE IF EXISTS `captcha_codes`;
CREATE TABLE IF NOT EXISTS `captcha_codes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` tinytext NOT NULL,
  `secret_code` tinytext NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx__captcha_codes__secret_code` (`secret_code`(46))
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8;

-- Экспортируемые данные не выделены.
-- Дамп структуры для таблица DevPubBlogEngine.global_settings
DROP TABLE IF EXISTS `global_settings`;
CREATE TABLE IF NOT EXISTS `global_settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx__global_settings__code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Экспортируемые данные не выделены.
-- Дамп структуры для таблица DevPubBlogEngine.posts
DROP TABLE IF EXISTS `posts`;
CREATE TABLE IF NOT EXISTS `posts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `text` text NOT NULL,
  `user_id` int(11) NOT NULL,
  `time` datetime NOT NULL,
  `is_active` tinyint(4) NOT NULL,
  `moderation_status` enum('NEW','ACCEPTED','DECLINED') NOT NULL DEFAULT 'NEW',
  `moderator_id` int(11) DEFAULT NULL,
  `view_count` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk__posts__moderator` (`moderator_id`),
  KEY `fk__posts__user` (`user_id`),
  KEY `idx__posts__time` (`time`),
  KEY `idx__posts__is_active` (`is_active`),
  KEY `idx__posts__moderation_status` (`moderation_status`),
  CONSTRAINT `fk__posts__moderator` FOREIGN KEY (`moderator_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk__posts__user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8;

-- Экспортируемые данные не выделены.
-- Дамп структуры для таблица DevPubBlogEngine.post_comments
DROP TABLE IF EXISTS `post_comments`;
CREATE TABLE IF NOT EXISTS `post_comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` text NOT NULL,
  `post_id` int(11) NOT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk__post_comments__post` (`post_id`),
  KEY `fk__post_comments__user` (`user_id`),
  KEY `fk__post_comments__parent` (`parent_id`),
  CONSTRAINT `fk__post_comments__parent` FOREIGN KEY (`parent_id`) REFERENCES `post_comments` (`id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `fk__post_comments__post` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `fk__post_comments__user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- Экспортируемые данные не выделены.
-- Дамп структуры для таблица DevPubBlogEngine.post_votes
DROP TABLE IF EXISTS `post_votes`;
CREATE TABLE IF NOT EXISTS `post_votes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  `value` tinyint(4) NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx__post_votes` (`user_id`,`post_id`),
  KEY `fk__post_votes__post` (`post_id`),
  CONSTRAINT `fk__post_votes__post` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `fk__post_votes__user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8;

-- Экспортируемые данные не выделены.
-- Дамп структуры для таблица DevPubBlogEngine.tag2post
DROP TABLE IF EXISTS `tag2post`;
CREATE TABLE IF NOT EXISTS `tag2post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `post_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx__tag2post` (`post_id`,`tag_id`),
  KEY `fk__tag2post__tag` (`tag_id`),
  CONSTRAINT `fk__tag2post__post` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `fk__tag2post__tag` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;

-- Экспортируемые данные не выделены.
-- Дамп структуры для таблица DevPubBlogEngine.tags
DROP TABLE IF EXISTS `tags`;
CREATE TABLE IF NOT EXISTS `tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx__tags__name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8;

-- Экспортируемые данные не выделены.
-- Дамп структуры для таблица DevPubBlogEngine.users
DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `photo` text,
  `reg_time` datetime NOT NULL,
  `is_moderator` tinyint(4) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx__users__email` (`email`),
  UNIQUE KEY `uidx__users_name` (`name`),
  UNIQUE KEY `uidx__users__code` (`code`(46))
) ENGINE=InnoDB AUTO_INCREMENT=1000035 DEFAULT CHARSET=utf8;

-- Экспортируемые данные не выделены.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
