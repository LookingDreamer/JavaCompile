CREATE TABLE `INSBFilelibraryUploadCosFail` (
  `id` VARCHAR(32) NOT NULL COMMENT '主键',
  `operator` VARCHAR(32) NOT NULL COMMENT '操作员',
  `createtime` DATETIME NOT NULL COMMENT '创建时间',
  `modifytime` DATETIME DEFAULT NULL COMMENT '修改时间',
  `noti` VARCHAR(100) DEFAULT NULL COMMENT '备注',
  `imagetype` VARCHAR(32) NOT NULL COMMENT '影像类型-原图proto小图small',
  `filelibraryid` VARCHAR(32) NOT NULL COMMENT '附件库id',
  `filephysicalpath` VARCHAR(400) DEFAULT NULL COMMENT '文件物理路径',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='附件库上传cos失败日志表';

#      增加影像类型
INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`)
VALUES(  'a100',    '0',    NOW(),    NULL,    '影像类型',    'otherimage',    'insuranceimage',    '100',    '其它',    '100',    NULL,    NULL,    NULL,    NULL  ) ;

