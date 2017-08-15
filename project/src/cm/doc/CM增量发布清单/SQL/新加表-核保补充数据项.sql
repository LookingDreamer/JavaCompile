CREATE TABLE `insbinsuresupplyparam` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `operator` varchar(20) NOT NULL COMMENT '操作员',
  `createtime` datetime NOT NULL COMMENT '创建时间',
  `modifytime` datetime DEFAULT NULL COMMENT '修改时间',
  `noti` text COMMENT '备注',
  `taskid` varchar(32) NOT NULL COMMENT '任务id',
  `inscomcode` varchar(50) NOT NULL COMMENT '保险公司代码',
  `itemcode` varchar(50) NOT NULL COMMENT '数据项编码',
  `itemname` varchar(50) DEFAULT NULL COMMENT '数据项名称',
  `itemvalue` varchar(100) DEFAULT NULL COMMENT '数据项值',
  `iteminputtype` varchar(20) DEFAULT NULL COMMENT '数据项输入类型（文件、下拉列表、日期控件等）',
  `itemoptionallist` varchar(1000) DEFAULT NULL COMMENT '数据项为下拉列表时可选项列表',
  `itemorder` int(2) DEFAULT NULL COMMENT '数据项排序',
  PRIMARY KEY (`id`),
  KEY `idx_insbinsuresupplyparam_taskid` (`taskid`) USING BTREE,
  KEY `idx_insbinsuresupplyparam_inscomcode` (`inscomcode`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='核保补充数据项';