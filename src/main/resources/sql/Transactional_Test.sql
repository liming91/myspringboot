CREATE TABLE `song` (
                        `id` varchar(40) NOT NULL COMMENT '歌曲编号',
                        `name` varchar(40) NOT NULL COMMENT '歌曲名',
                        `singer` varchar(40) NOT NULL COMMENT '歌手',
                        `url` varchar(40) NOT NULL DEFAULT '' COMMENT '歌曲文件URL',
                        `is_exist` tinyint(4) NOT NULL DEFAULT 0 COMMENT '歌曲文件是否存在',
                        `note` varchar(40) NOT NULL DEFAULT '' COMMENT '描述信息',
                        `last_update_time` timestamp NULL DEFAULT current_timestamp() COMMENT 'last_update',
                        PRIMARY KEY (`id`) USING BTREE,
                        KEY `index_1` (`name`,`singer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;