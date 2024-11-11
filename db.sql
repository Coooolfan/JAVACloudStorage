-- javacloudstorage.`user` definition
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  `password` text NOT NULL,
  `avatar` varchar(255) NOT NULL DEFAULT 'https://zhifengmuxue.top/img/1.jpg',
  `quota` int NOT NULL DEFAULT '1000000',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  `used_quota` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- javacloudstorage.filemap definition
CREATE TABLE `filemap` (
  `id` int NOT NULL AUTO_INCREMENT,
  `size` int unsigned NOT NULL DEFAULT '0',
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `format` varchar(100) DEFAULT NULL,
  `is_directory` tinyint(1) NOT NULL DEFAULT '0',
  `parent` int NOT NULL,
  `owner` int NOT NULL,
  `is_root` tinyint(1) DEFAULT NULL,
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  `sha256` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `filemap_unique` (`owner`,`parent`,`filename`,`format`),
  KEY `filemap_isRoot_IDX` (`is_root`) USING BTREE,
  KEY `filemap_parent_IDX` (`parent`) USING BTREE,
  FULLTEXT KEY `filemap_filename_IDX` (`filename`),
  CONSTRAINT `filemap_user_FK` FOREIGN KEY (`owner`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
