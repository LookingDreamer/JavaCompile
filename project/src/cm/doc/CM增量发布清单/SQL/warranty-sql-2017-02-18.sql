CREATE TABLE `coupon` (
  `id` varchar(32) NOT NULL,
  `amount` double DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `modifyTime` datetime DEFAULT NULL,
  `operatorId` varchar(32) DEFAULT NULL,
  `operatorName` varchar(45) DEFAULT NULL,
  `couponCount` int(11) DEFAULT NULL,
  `effectiveTime` datetime DEFAULT NULL,
  `expireTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `amount_UNIQUE` (`amount`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='代金券';

CREATE TABLE `couponconfig` (
  `id` varchar(32) NOT NULL,
  `key` varchar(45) DEFAULT NULL,
  `value` varchar(45) DEFAULT NULL,
  `deadline` datetime DEFAULT NULL,
  `defaultCouponTimes` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_UNIQUE` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `couponpool` (
  `couponId` varchar(32) NOT NULL,
  `couponCount` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`couponId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `usercoupon` (
  `couponCode` varchar(32) NOT NULL,
  `amount` double DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `effectiveTime` datetime DEFAULT NULL,
  `expireTime` datetime DEFAULT NULL,
  `isUsed` tinyint(1) DEFAULT '0',
  `usedTime` datetime DEFAULT NULL,
  `userCode` varchar(32) NOT NULL,
  `userName` varchar(45) DEFAULT NULL,
  `couponId` varchar(45) NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `productCode` varchar(45) DEFAULT NULL COMMENT '奖券可用于的产品',
  PRIMARY KEY (`couponCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `usercoupontimes` (
  `id` varchar(32) NOT NULL,
  `userCode` varchar(45) DEFAULT NULL COMMENT '用户code',
  `couponTimes` int(11) DEFAULT NULL COMMENT '用户已抽奖次数',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `operatorId` varchar(45) DEFAULT NULL,
  `operatorName` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userCode_UNIQUE` (`userCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `inseorder`;
CREATE TABLE `inseorder` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `operator` varchar(20) NOT NULL COMMENT '操作员',
  `createtime` datetime NOT NULL COMMENT '创建时间',
  `modifytime` datetime DEFAULT NULL COMMENT '修改时间',
  `noti` varchar(100) DEFAULT NULL COMMENT '备注',
  `taskid` varchar(32) DEFAULT NULL COMMENT '任务id',
  `orderno` varchar(36) DEFAULT NULL COMMENT '订单号',
  `orderstatus` int(1) NOT NULL DEFAULT '0' COMMENT '订单状态与insccode相关联',
  `paymentstatus` varchar(2) DEFAULT NULL COMMENT '支付状态与insccode相关联',
  `deliverystatus` varchar(2) DEFAULT NULL COMMENT '配送状态与insccode相关联',
  `buyway` varchar(2) DEFAULT NULL COMMENT '销售渠道与insccode相关联',
  `agentcode` varchar(20) DEFAULT NULL COMMENT '该订单的代理人编码',
  `agentname` varchar(30) DEFAULT NULL COMMENT '该订单的代理人姓名',
  `inputusercode` varchar(20) DEFAULT NULL COMMENT '录单人',
  `currency` varchar(10) DEFAULT NULL COMMENT '币种编码',
  `totaldeliveryamount` decimal(16,4) DEFAULT NULL COMMENT '配送费（总）',
  `totalpaymentamount` decimal(16,4) DEFAULT NULL COMMENT '实付金额：配送费 + 订单项实付总额',
  `totalproductamount` decimal(16,4) DEFAULT NULL COMMENT '产品标价总金额',
  `totalpromotionamount` decimal(16,4) DEFAULT NULL COMMENT '优惠金额：产品标价总金额 - (实付金额 - 配送费)',
  `deptcode` varchar(100) NOT NULL COMMENT '出单网点',
  `paymentmethod` varchar(2) DEFAULT NULL COMMENT '字典表',
  `prvid` varchar(32) DEFAULT NULL COMMENT '供应商',
  `customerinsurecode` varchar(2) DEFAULT NULL COMMENT '01-续保,02-转保,03-新保,10-异地(客户忠诚度)',
  `insorderno` varchar(100) DEFAULT NULL COMMENT '保险公司订单号',
  `needinvoice` tinyint(1) DEFAULT NULL,
  `invoiceemail` varchar(45) DEFAULT NULL,
  `transactionId` varchar(45) DEFAULT NULL,
  `policyid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_orderno` (`orderno`) USING BTREE,
  KEY `idx_orderno_jobnum` (`orderno`,`agentcode`) USING BTREE,
  KEY `idx_createtime` (`createtime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='订单表';

DROP TABLE IF EXISTS insepolicy;
CREATE TABLE `insepolicy` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `operator` varchar(20) DEFAULT NULL COMMENT '操作员',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `modifytime` datetime DEFAULT NULL COMMENT '修改时间',
  `orderid` varchar(32) DEFAULT NULL COMMENT '订单表id',
  `applicantid` varchar(32) DEFAULT NULL COMMENT '投保人id',
  `applicantname` varchar(50) DEFAULT NULL COMMENT '投保人姓名',
  `insuredid` varchar(32) DEFAULT NULL COMMENT '被保人id',
  `insuredname` varchar(50) DEFAULT NULL COMMENT '被保人姓名',
  `carownerid` varchar(32) DEFAULT NULL COMMENT '车主id',
  `carownername` varchar(50) DEFAULT NULL COMMENT '车主姓名',
  `carinfoid` varchar(32) DEFAULT NULL COMMENT '车辆信息id',
  `standardfullname` varchar(200) DEFAULT NULL COMMENT '车型信息描述',
  `proposalformno` varchar(50) DEFAULT NULL COMMENT '投保单号',
  `policyno` varchar(50) DEFAULT NULL COMMENT '保单号',
  `premium` decimal(16,4) DEFAULT NULL COMMENT '保费',
  `insureddate` datetime DEFAULT NULL COMMENT '承保日期',
  `startdate` datetime DEFAULT NULL COMMENT '起保日期',
  `enddate` datetime DEFAULT NULL COMMENT '终止日期',
  `totalepremium` decimal(16,4) DEFAULT NULL COMMENT '总保费',
  `agentnum` varchar(50) DEFAULT NULL COMMENT '代理人工号',
  `agentname` varchar(30) DEFAULT NULL COMMENT '代理人名称',
  `team` varchar(60) DEFAULT NULL COMMENT '所属团队',
  `closedstatus` varchar(1) DEFAULT '0' COMMENT '关闭状态；0 未关闭 1 已关闭',
  `policystatus` varchar(2) DEFAULT NULL COMMENT '保单状态',
  `risktype` varchar(1) DEFAULT NULL COMMENT '险种类型',
  `paynum` varchar(50) DEFAULT NULL,
  `checkcode` varchar(50) DEFAULT NULL,
  `discountCharge` decimal(16,6) DEFAULT NULL COMMENT '折后保费',
  `discountRate` decimal(16,6) DEFAULT NULL COMMENT '折扣率',
  `amount` decimal(16,6) DEFAULT NULL COMMENT '保额',
  `inscomcode` varchar(50) DEFAULT NULL COMMENT '保险公司代码',
  `biztype` varchar(20) DEFAULT NULL,
  `legalrightclaimid` varchar(45) DEFAULT NULL,
  `legalrightclaimname` varchar(45) DEFAULT NULL,
  `extendwarrantytype` int(1) DEFAULT NULL COMMENT '延保方案',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='保单表';

INSERT INTO `coupon` (`id`, `amount`, `description`, `createTime`, `modifyTime`, `operatorId`, `operatorName`, `couponCount`, `effectiveTime`, `expireTime`) VALUES ('03aa5e20ee7511e66098b859b8e47076', '20', '代金券20元', '2017-02-09 11:08:22', '2017-02-09 11:08:22', NULL, NULL, '10', '2017-02-10 17:25:02', '2017-03-10 00:00:00');
INSERT INTO `coupon` (`id`, `amount`, `description`, `createTime`, `modifyTime`, `operatorId`, `operatorName`, `couponCount`, `effectiveTime`, `expireTime`) VALUES ('03aef200ee7511e66098b859b8e47076', '50', '代金券50元', '2017-02-09 11:08:22', '2017-02-09 11:08:22', NULL, NULL, '10', '2017-02-10 17:25:02', '2017-03-10 00:00:00');
INSERT INTO `coupon` (`id`, `amount`, `description`, `createTime`, `modifyTime`, `operatorId`, `operatorName`, `couponCount`, `effectiveTime`, `expireTime`) VALUES ('03b05190ee7511e66098b859b8e47076', '100', '代金券100元', '2017-02-09 11:08:22', '2017-02-09 11:08:22', NULL, NULL, '10', '2017-02-10 17:25:02', '2017-03-10 00:00:00');
INSERT INTO `coupon` (`id`, `amount`, `description`, `createTime`, `modifyTime`, `operatorId`, `operatorName`, `couponCount`, `effectiveTime`, `expireTime`) VALUES ('03b2c290ee7511e66098b859b8e47076', '-1', '免费延保', '2017-02-09 11:08:22', '2017-02-09 11:08:22', NULL, NULL, '1', '2017-02-10 17:25:02', '2017-03-10 00:00:00');
INSERT INTO `coupon` (`id`, `amount`, `description`, `createTime`, `modifyTime`, `operatorId`, `operatorName`, `couponCount`, `effectiveTime`, `expireTime`) VALUES ('03b49750ee7511e66098b859b8e47076', '-2', '谢谢参与', '2017-02-09 11:08:22', '2017-02-09 11:08:22', NULL, NULL, '100', '2017-02-10 17:25:02', '2017-03-10 00:00:00');

INSERT INTO `couponconfig` (`id`, `key`, `value`, `deadline`, `defaultCouponTimes`) VALUES ('b495a9a0ee8f11e64c26115df37d8689', 'test', 'udpate', NULL, '2000');