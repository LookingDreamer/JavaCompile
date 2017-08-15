CREATE TABLE `insbminidate` (
  `id` varchar(32) NOT NULL,
  `year` varchar(4) DEFAULT NULL COMMENT '年份',
  `month` varchar(2) DEFAULT NULL COMMENT '月份',
  `day` varchar(2) DEFAULT NULL COMMENT '日期',
  `date` datetime DEFAULT NULL COMMENT '详细日期',
  `weekday` varchar(10) DEFAULT NULL COMMENT '星期',
  `datestr` varchar(20) DEFAULT NULL COMMENT '日期字符串格式',
  `datetype` varchar(2) DEFAULT NULL COMMENT '01工作日，02节假日',
  `createtime` datetime NOT NULL,
  `operator` varchar(100) DEFAULT NULL,
  `modifytime` datetime DEFAULT NULL,
  `noti` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `datestr-index` (`datestr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `insccode` (`id`, `operator`, `createtime`, `modifytime`, `noti`, `codetype`, `parentcode`, `codevalue`, `codename`, `codeorder`, `prop1`, `prop2`, `prop3`, `prop4`) 
VALUES ('minidatetype01', 'shiguiwu', '2016-12-12 12:11:48', NULL, 'mini日期类型', 'minidate', 'minidate', '01', '工作日', '0', '0', NULL, NULL, NULL);
INSERT INTO `insccode` (`id`, `operator`, `createtime`, `modifytime`, `noti`, `codetype`, `parentcode`, `codevalue`, `codename`, `codeorder`, `prop1`, `prop2`, `prop3`, `prop4`) 
VALUES ('minidatetype02', 'shiguiwu', '2016-12-12 12:11:48', NULL, 'mini日期类型', 'minidate', 'minidate', '02', '节假日', '0', '0', NULL, NULL, NULL);

