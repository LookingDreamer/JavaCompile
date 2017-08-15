

DELETE FROM insbpermission;
DELETE FROM insbpermissionset;
DELETE FROM insbpermissionallot; 
DELETE FROM insbagentpermission;
DELETE FROM insbagentprovider;
DELETE FROM INSBItemprovidestatus;

insert into `insbpermission` (`id`, `percode`, `permissionname`, `permissionpath`, `createtime`, `modifytime`, `istry`, `operator`, `noti`) values('1','001','车险投保','insured','1899-12-30 01:00:00','1899-12-30 01:00:00','1','1','insured');
insert into `insbpermission` (`id`, `percode`, `permissionname`, `permissionpath`, `createtime`, `modifytime`, `istry`, `operator`, `noti`) values('2','002','人工报价','quote','1899-12-30 01:00:00',NULL,'1','1','quote');
insert into `insbpermission` (`id`, `percode`, `permissionname`, `permissionpath`, `createtime`, `modifytime`, `istry`, `operator`, `noti`) values('3','003','快速续保','renewal','1899-12-30 02:00:00','1899-12-30 02:00:00','1','1','renewal');
insert into `insbpermission` (`id`, `percode`, `permissionname`, `permissionpath`, `createtime`, `modifytime`, `istry`, `operator`, `noti`) values('4','004','大数据查询','cif','2015-05-06 10:00:00','2015-05-06 10:00:00','1','1','cif');
insert into `insbpermission` (`id`, `percode`, `permissionname`, `permissionpath`, `createtime`, `modifytime`, `istry`, `operator`, `noti`) values('5','005','平台查询','query','2015-05-06 12:00:00','2015-05-06 12:00:00','1','1','query');
insert into `insbpermission` (`id`, `percode`, `permissionname`, `permissionpath`, `createtime`, `modifytime`, `istry`, `operator`, `noti`) values('6','006','提交核保','underwriting','2015-12-09 17:19:43',NULL,'1','1','underwriting');
insert into `insbpermission` (`id`, `percode`, `permissionname`, `permissionpath`, `createtime`, `modifytime`, `istry`, `operator`, `noti`) values('7','007','支付','pay','2015-12-18 17:51:55',NULL,'1','1','pay');


UPDATE insbagent  SET setid=NULL ;

DROP TABLE IF EXISTS `inscdeptpermissionset`;

CREATE TABLE `inscdeptpermissionset` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `comcode` varchar(32) NOT NULL COMMENT '机构编码',
  `trysetid` varchar(32) DEFAULT NULL COMMENT '试用用户默认权限包id',
  `formalsetid` varchar(32) DEFAULT NULL COMMENT '正式用户默认权限包id',
  `channelsetid` varchar(32) DEFAULT NULL COMMENT '渠道用户默认权限包id',
  `operator` varchar(20) NOT NULL COMMENT '操作员',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `modifytime` datetime DEFAULT NULL COMMENT '修改时间',
  `noti` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;