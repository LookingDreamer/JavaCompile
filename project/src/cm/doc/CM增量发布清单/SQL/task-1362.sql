CREATE TABLE `insbfairyinsureerrorlog` (
  `id` varchar(32) NOT NULL,
  `taskId` varchar(32) NOT NULL,
  `insuranceCompanyId` varchar(20) DEFAULT NULL,
  `errorCode` varchar(10) DEFAULT NULL,
  `errorDesc` varchar(100) DEFAULT NULL,
  `operator` varchar(45) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `modifyTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_createtime` (`createTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='精灵承保失败错误信息';
