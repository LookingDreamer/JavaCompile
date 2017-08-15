CREATE TABLE `insecar` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `operator` varchar(20) DEFAULT NULL COMMENT '操作员',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `modifytime` datetime DEFAULT NULL COMMENT '修改时间',
  `selectconfig` varchar(255) DEFAULT NULL COMMENT '已选择保险配置',
  `selectins` varchar(255) DEFAULT NULL COMMENT '已选择保险公司',
  `ownerid` varchar(32) DEFAULT NULL COMMENT '车主id',
  `ownername` varchar(30) DEFAULT NULL COMMENT '车主姓名',
  `phonenumber` varchar(20) DEFAULT NULL COMMENT '联系方式',
  `address` varchar(100) DEFAULT NULL COMMENT '联系地址',
  `carlicenseno` varchar(20) DEFAULT NULL COMMENT '车牌号',
  `vincode` varchar(32) DEFAULT NULL COMMENT '车辆识别代号',
  `standardfullname` varchar(200) DEFAULT NULL COMMENT '车型信息描述',
  `engineno` varchar(50) DEFAULT NULL,
  `registdate` datetime DEFAULT NULL COMMENT '车辆初始登记日期',
  `registlicense` varchar(30) DEFAULT NULL COMMENT '注册许可证',
  `transferdate` datetime DEFAULT NULL COMMENT '过户日期',
  `isTransfercar` varchar(1) DEFAULT NULL COMMENT '过户车/0-不是过户车/1-是过户车',
  `Specifydriver` varchar(32) DEFAULT NULL COMMENT '是否指定驾驶人，保存的是驾驶人中间表的id',
  `drivingarea` varchar(30) DEFAULT NULL COMMENT '行驶区域',
  `mileage` varchar(50) DEFAULT NULL COMMENT '平均行驶里程',
  `accidentnum` int(11) DEFAULT NULL COMMENT '事故次数',
  `carid` int(11) DEFAULT NULL COMMENT '车辆编号',
  `useyears` int(11) DEFAULT NULL COMMENT '使用年限',
  `signdate` datetime DEFAULT NULL COMMENT '签署时间',
  `parksite` varchar(200) DEFAULT NULL COMMENT '固定停放地点',
  `preinscode` varchar(60) DEFAULT NULL COMMENT '上年商业承保公司',
  `taskstatus` varchar(30) DEFAULT NULL COMMENT '任务状态',
  `property` varchar(10) DEFAULT NULL COMMENT '所属性质',
  `carproperty` varchar(10) DEFAULT NULL COMMENT '车辆使用性质',
  `businessstartdate` datetime DEFAULT NULL,
  `businessenddate` datetime DEFAULT NULL,
  `strongstartdate` datetime DEFAULT NULL,
  `strongenddate` datetime DEFAULT NULL,
  `cloudinscompany` varchar(60) DEFAULT NULL,
  `islicense` varchar(2) DEFAULT NULL COMMENT '车辆是否上牌，0未上牌/1已上牌',
  `platecolor` int(11) DEFAULT NULL COMMENT '号牌颜色',
  `plateType` int(11) DEFAULT NULL COMMENT '号牌种类',
  `ineffectualDate` datetime DEFAULT NULL COMMENT '检验有效日期',
  `rejectDate` datetime DEFAULT NULL COMMENT '强制有效期',
  `lastCheckDate` datetime DEFAULT NULL COMMENT '最近定检日期',
  `isLoansCar` varchar(1) DEFAULT NULL COMMENT '上否贷款车',
  `carVehicularApplications` int(11) DEFAULT NULL COMMENT '车辆用途',
  `isFieldCar` varchar(1) DEFAULT NULL COMMENT '是否军牌外地车',
  `carBodyColor` int(11) DEFAULT NULL COMMENT '车身颜色',
  `loanManyYearsFlag` varchar(1) DEFAULT NULL COMMENT '上否车贷投保多年标志',
  `isNew` varchar(1) DEFAULT NULL COMMENT '新车标致，0 旧车/1 新车',
  `depprice` double DEFAULT NULL COMMENT '车辆折旧价',
  `floatprice` double DEFAULT NULL COMMENT '车辆浮动价',
  `isdanger` varchar(1) DEFAULT NULL COMMENT '是高危限制车型 0不是  1是',
  `isrob` varchar(1) DEFAULT NULL COMMENT '是易盗车型 0不是 1 是',
  `isspecial` varchar(1) DEFAULT NULL COMMENT '是管控车型 0不是 1 是',
  `maxprice` double DEFAULT NULL COMMENT '车辆浮动价上限',
  `maxpricerate` double DEFAULT NULL COMMENT '车辆浮动价上限比率',
  `minprice` double DEFAULT NULL COMMENT '车辆浮动价下限',
  `minpricerate` double DEFAULT NULL COMMENT '车辆浮动价下限比率',
  `taxprice` double DEFAULT NULL COMMENT '新车购置价',
  `drivinglicenseurl` varchar(150) DEFAULT NULL COMMENT '行驶证图片路径',
  `brandimg` varchar(200) DEFAULT NULL COMMENT '车辆照片（平台提供）',
  `cifstandardname` varchar(100) DEFAULT NULL COMMENT '平台查询传过来的车辆品牌',
  `insureconfigsameaslastyear` varchar(2) DEFAULT NULL COMMENT '保险配置是否与上年一致(1:是)',
  `platenumber` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;


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
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_orderno` (`orderno`) USING BTREE,
  KEY `idx_orderno_jobnum` (`orderno`,`agentcode`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='订单表';

CREATE TABLE `insepayment` (
  `id` varchar(32) NOT NULL,
  `orderid` varchar(45) DEFAULT NULL,
  `orderno` varchar(45) DEFAULT NULL,
  `transactionid` varchar(45) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `channelid` varchar(45) DEFAULT NULL,
  `paytype` varchar(10) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '0',
  `operator` varchar(45) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `modifytime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `inseperson` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `operator` varchar(20) DEFAULT NULL COMMENT '操作员',
  `createtime` datetime DEFAULT NULL COMMENT '创建日期',
  `modifytime` datetime DEFAULT NULL COMMENT '修改日期',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `ename` varchar(100) DEFAULT NULL COMMENT '英文名',
  `gender` int(11) DEFAULT NULL COMMENT '性别',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `bloodtype` int(11) DEFAULT NULL COMMENT '血型',
  `age` int(11) DEFAULT NULL,
  `idcardtype` int(11) DEFAULT NULL COMMENT '证件类型',
  `idcardno` varchar(20) DEFAULT NULL COMMENT '证件号码',
  `cellphone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `province` int(11) DEFAULT NULL COMMENT '省',
  `city` int(11) DEFAULT NULL COMMENT '城',
  `area` int(11) DEFAULT NULL COMMENT '区县',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  `zip` varchar(10) DEFAULT NULL COMMENT '邮编',
  `licensetype` varchar(20) DEFAULT NULL COMMENT '驾驶证类型',
  `licenseno` varchar(50) DEFAULT NULL COMMENT '驾驶证号码',
  `licensedate` datetime DEFAULT NULL COMMENT '驾驶证发照日期',
  `maritalstatus` varchar(10) DEFAULT NULL,
  `job` varchar(30) DEFAULT NULL,
  `educateinfo` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_index` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='保单表';

CREATE TABLE `insequote` (
  `id` varchar(32) NOT NULL,
  `operator` varchar(45) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `modifytime` datetime DEFAULT NULL,
  `vincode` varchar(45) DEFAULT NULL,
  `standardname` varchar(45) DEFAULT NULL,
  `engineno` varchar(45) DEFAULT NULL,
  `carprice` varchar(45) DEFAULT NULL,
  `registerdate` datetime DEFAULT NULL,
  `startdate` datetime DEFAULT NULL,
  `enddate` datetime DEFAULT NULL,
  `extendwarrantytype` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `origialwarrantyperiod` int(11) DEFAULT NULL,
  `platenumber` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `insewarrantyprice` (
  `id` varchar(32) NOT NULL,
  `downprice` double DEFAULT NULL,
  `upprice` double DEFAULT NULL,
  `warrantyplan` int(11) DEFAULT NULL,
  `warrantyprice` double DEFAULT NULL,
  `isimported` tinyint(1) DEFAULT NULL,
  `warrantyperiod` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='计算报价数据';


INSERT INTO `insewarrantyprice` VALUES ('038c3f00dbc211e668c005ef26792c34', 0, 100000, 1, 730, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('038d0250dbc211e668c005ef26792c34', 100001, 150000, 1, 862, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('038d7780dbc211e668c005ef26792c34', 150001, 200000, 1, 1022, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('038decb0dbc211e668c005ef26792c34', 200001, 250000, 1, 1199, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('038e61e0dbc211e668c005ef26792c34', 250001, 300000, 1, 1473, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('038f2530dbc211e668c005ef26792c34', 300001, 400000, 1, 1738, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('038fc170dbc211e668c005ef26792c34', 400001, 500000, 1, 2000, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('039036a0dbc211e668c005ef26792c34', 500001, 600000, 1, 2279, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('0390d2e0dbc211e668c005ef26792c34', 600001, 700000, 1, 2553, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03912100dbc211e668c005ef26792c34', 700001, 900000, 1, 2833, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03919630dbc211e668c005ef26792c34', 900001, 1100000, 1, 3118, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03920b60dbc211e668c005ef26792c34', 1100001, 1300000, 1, 3428, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03925980dbc211e668c005ef26792c34', 1300001, 1500000, 1, 3839, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('0392ceb0dbc211e668c005ef26792c34', 1500001, 1700000, 1, 4223, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('039343e0dbc211e668c005ef26792c34', 1700001, 1900000, 1, 4645, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03940730dbc211e668c005ef26792c34', 1900001, 2100000, 1, 5110, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('0394ca80dbc211e668c005ef26792c34', 2100001, 2500000, 1, 5621, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03953fb0dbc211e668c005ef26792c34', 2500001, 3000000, 1, 6183, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('0395b4e0dbc211e668c005ef26792c34', 3000001, 3500000, 1, 6802, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03962a10dbc211e668c005ef26792c34', 3500001, 4000000, 1, 7481, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03969f40dbc211e668c005ef26792c34', 0, 100000, 1, 949, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03971470dbc211e668c005ef26792c34', 100001, 150000, 1, 1121, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03976290dbc211e668c005ef26792c34', 150001, 200000, 1, 1329, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('0397fed0dbc211e668c005ef26792c34', 200001, 250000, 1, 1559, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03987400dbc211e668c005ef26792c34', 250001, 300000, 1, 1915, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('0398e930dbc211e668c005ef26792c34', 300001, 400000, 1, 2260, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03995e60dbc211e668c005ef26792c34', 400001, 500000, 1, 2600, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('0399ac80dbc211e668c005ef26792c34', 500001, 600000, 1, 2963, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('0399faa0dbc211e668c005ef26792c34', 600001, 700000, 1, 3319, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('039a6fd0dbc211e668c005ef26792c34', 700001, 900000, 1, 3683, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('039abdf0dbc211e668c005ef26792c34', 900001, 1100000, 1, 4053, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('039b5a30dbc211e668c005ef26792c34', 1100001, 1300000, 1, 4456, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('039bcf60dbc211e668c005ef26792c34', 1300001, 1500000, 1, 4991, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('039c4490dbc211e668c005ef26792c34', 1500001, 1700000, 1, 5490, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('039c92b0dbc211e668c005ef26792c34', 1700001, 1900000, 1, 6039, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('039d2ef0dbc211e668c005ef26792c34', 1900001, 2100000, 1, 6643, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('039dcb30dbc211e668c005ef26792c34', 2100001, 2500000, 1, 7307, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('039e1950dbc211e668c005ef26792c34', 2500001, 3000000, 1, 8038, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('039e6770dbc211e668c005ef26792c34', 3000001, 3500000, 1, 8842, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('039edca0dbc211e668c005ef26792c34', 3500001, 4000000, 1, 9726, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('039f51d0dbc211e668c005ef26792c34', 0, 100000, 1, 1388, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('039f9ff0dbc211e668c005ef26792c34', 100001, 150000, 1, 1638, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('039fee10dbc211e668c005ef26792c34', 150001, 200000, 1, 1943, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a06340dbc211e668c005ef26792c34', 200001, 250000, 1, 2278, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a0b160dbc211e668c005ef26792c34', 250001, 300000, 1, 2799, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a12690dbc211e668c005ef26792c34', 300001, 400000, 1, 3303, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a174b0dbc211e668c005ef26792c34', 400001, 500000, 1, 3800, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a1c2d0dbc211e668c005ef26792c34', 500001, 600000, 1, 4331, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a210f0dbc211e668c005ef26792c34', 600001, 700000, 1, 4851, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a25f10dbc211e668c005ef26792c34', 700001, 900000, 1, 5382, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a2d440dbc211e668c005ef26792c34', 900001, 1100000, 1, 5924, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a34970dbc211e668c005ef26792c34', 1100001, 1300000, 1, 6513, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a3bea0dbc211e668c005ef26792c34', 1300001, 1500000, 1, 7294, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a40cc0dbc211e668c005ef26792c34', 1500001, 1700000, 1, 8024, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a481f0dbc211e668c005ef26792c34', 1700001, 1900000, 1, 8826, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a4f720dbc211e668c005ef26792c34', 1900001, 2100000, 1, 9710, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a56c50dbc211e668c005ef26792c34', 2100001, 2500000, 1, 10679, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a5e180dbc211e668c005ef26792c34', 2500001, 3000000, 1, 11748, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a656b0dbc211e668c005ef26792c34', 3000001, 3500000, 1, 12923, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a6cbe0dbc211e668c005ef26792c34', 3500001, 4000000, 1, 14215, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a74110dbc211e668c005ef26792c34', 0, 100000, 1, 1804, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a7b640dbc211e668c005ef26792c34', 100001, 150000, 1, 2129, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a82b70dbc211e668c005ef26792c34', 150001, 200000, 1, 2525, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a8a0a0dbc211e668c005ef26792c34', 200001, 250000, 1, 2962, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a915d0dbc211e668c005ef26792c34', 250001, 300000, 1, 3639, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03a9b210dbc211e668c005ef26792c34', 300001, 400000, 1, 4294, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03aa2740dbc211e668c005ef26792c34', 400001, 500000, 1, 4939, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03aa7560dbc211e668c005ef26792c34', 500001, 600000, 1, 5630, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03aac380dbc211e668c005ef26792c34', 600001, 700000, 1, 6307, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03ab11a0dbc211e668c005ef26792c34', 700001, 900000, 1, 6997, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03abade0dbc211e668c005ef26792c34', 900001, 1100000, 1, 7701, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03ac2310dbc211e668c005ef26792c34', 1100001, 1300000, 1, 8467, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03ac7130dbc211e668c005ef26792c34', 1300001, 1500000, 1, 9482, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03ace660dbc211e668c005ef26792c34', 1500001, 1700000, 1, 10431, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03ad5b90dbc211e668c005ef26792c34', 1700001, 1900000, 1, 11473, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03ada9b0dbc211e668c005ef26792c34', 1900001, 2100000, 1, 12622, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03adf7d0dbc211e668c005ef26792c34', 2100001, 2500000, 1, 13883, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03ae6d00dbc211e668c005ef26792c34', 2500001, 3000000, 1, 15272, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03aebb20dbc211e668c005ef26792c34', 3000001, 3500000, 1, 16800, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03af7e70dbc211e668c005ef26792c34', 3500001, 4000000, 1, 18479, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03aff3a0dbc211e668c005ef26792c34', 0, 100000, 0, 1446, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b041c0dbc211e668c005ef26792c34', 100001, 150000, 0, 1707, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b0b6f0dbc211e668c005ef26792c34', 150001, 200000, 0, 2027, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b17a40dbc211e668c005ef26792c34', 200001, 250000, 0, 2374, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b1ef70dbc211e668c005ef26792c34', 250001, 300000, 0, 2919, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b264a0dbc211e668c005ef26792c34', 300001, 400000, 0, 3444, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b2d9d0dbc211e668c005ef26792c34', 400001, 500000, 0, 3961, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b34f00dbc211e668c005ef26792c34', 500001, 600000, 0, 4516, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b3c430dbc211e668c005ef26792c34', 600001, 700000, 0, 5058, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b41250dbc211e668c005ef26792c34', 700001, 900000, 0, 5614, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b48780dbc211e668c005ef26792c34', 900001, 1100000, 0, 6176, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b4d5a0dbc211e668c005ef26792c34', 1100001, 1300000, 0, 6793, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b523c0dbc211e668c005ef26792c34', 1300001, 1500000, 0, 7608, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b571e0dbc211e668c005ef26792c34', 1500001, 1700000, 0, 8369, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b5e710dbc211e668c005ef26792c34', 1700001, 1900000, 0, 9205, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b65c40dbc211e668c005ef26792c34', 1900001, 2100000, 0, 10125, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b6aa60dbc211e668c005ef26792c34', 2100001, 2500000, 0, 11138, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b794c0dbc211e668c005ef26792c34', 2500001, 3000000, 0, 12253, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b809f0dbc211e668c005ef26792c34', 3000001, 3500000, 0, 13477, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b87f20dbc211e668c005ef26792c34', 3500001, 4000000, 0, 14826, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b8cd40dbc211e668c005ef26792c34', 0, 100000, 0, 1879, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b91b60dbc211e668c005ef26792c34', 100001, 150000, 0, 2220, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b96980dbc211e668c005ef26792c34', 150001, 200000, 0, 2635, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03b9deb0dbc211e668c005ef26792c34', 200001, 250000, 0, 3086, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03ba53e0dbc211e668c005ef26792c34', 250001, 300000, 0, 3795, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03bac910dbc211e668c005ef26792c34', 300001, 400000, 0, 4478, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03bb3e40dbc211e668c005ef26792c34', 400001, 500000, 0, 5150, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03bbb370dbc211e668c005ef26792c34', 500001, 600000, 0, 5871, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03bc28a0dbc211e668c005ef26792c34', 600001, 700000, 0, 6575, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03bc9dd0dbc211e668c005ef26792c34', 700001, 900000, 0, 7298, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03bd3a10dbc211e668c005ef26792c34', 900001, 1100000, 0, 8028, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03bdaf40dbc211e668c005ef26792c34', 1100001, 1300000, 0, 8831, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03be4b80dbc211e668c005ef26792c34', 1300001, 1500000, 0, 9890, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03bec0b0dbc211e668c005ef26792c34', 1500001, 1700000, 0, 10879, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03bf35e0dbc211e668c005ef26792c34', 1700001, 1900000, 0, 11967, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03bf8400dbc211e668c005ef26792c34', 1900001, 2100000, 0, 13163, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03bff930dbc211e668c005ef26792c34', 2100001, 2500000, 0, 14479, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03c06e60dbc211e668c005ef26792c34', 2500001, 3000000, 0, 15928, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03c0e390dbc211e668c005ef26792c34', 3000001, 3500000, 0, 17521, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03c158c0dbc211e668c005ef26792c34', 3500001, 4000000, 0, 19274, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03c1cdf0dbc211e668c005ef26792c34', 0, 100000, 0, 2747, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c26a30dbc211e668c005ef26792c34', 100001, 150000, 0, 3244, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c30670dbc211e668c005ef26792c34', 150001, 200000, 0, 3851, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c37ba0dbc211e668c005ef26792c34', 200001, 250000, 0, 4511, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c3f0d0dbc211e668c005ef26792c34', 250001, 300000, 0, 5546, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c46600dbc211e668c005ef26792c34', 300001, 400000, 0, 6544, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c4db30dbc211e668c005ef26792c34', 400001, 500000, 0, 7526, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c57770dbc211e668c005ef26792c34', 500001, 600000, 0, 8581, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c5eca0dbc211e668c005ef26792c34', 600001, 700000, 0, 9610, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c661d0dbc211e668c005ef26792c34', 700001, 900000, 0, 10667, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c6d700dbc211e668c005ef26792c34', 900001, 1100000, 0, 11734, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c72520dbc211e668c005ef26792c34', 1100001, 1300000, 0, 12907, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c79a50dbc211e668c005ef26792c34', 1300001, 1500000, 0, 14455, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c7e870dbc211e668c005ef26792c34', 1500001, 1700000, 0, 15900, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c83690dbc211e668c005ef26792c34', 1700001, 1900000, 0, 17490, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c8abc0dbc211e668c005ef26792c34', 1900001, 2100000, 0, 19238, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c920f0dbc211e668c005ef26792c34', 2100001, 2500000, 0, 21162, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03c99620dbc211e668c005ef26792c34', 2500001, 3000000, 0, 23280, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03ca0b50dbc211e668c005ef26792c34', 3000001, 3500000, 0, 25607, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03ca5970dbc211e668c005ef26792c34', 3500001, 4000000, 0, 28169, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03cacea0dbc211e668c005ef26792c34', 0, 100000, 0, 3570, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03cb43d0dbc211e668c005ef26792c34', 100001, 150000, 0, 4217, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03cb91f0dbc211e668c005ef26792c34', 150001, 200000, 0, 5006, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03cbe010dbc211e668c005ef26792c34', 200001, 250000, 0, 5864, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03cc5540dbc211e668c005ef26792c34', 250001, 300000, 0, 7210, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03ccca70dbc211e668c005ef26792c34', 300001, 400000, 0, 8507, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03cd1890dbc211e668c005ef26792c34', 400001, 500000, 0, 9784, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03cd8dc0dbc211e668c005ef26792c34', 500001, 600000, 0, 11155, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03cddbe0dbc211e668c005ef26792c34', 600001, 700000, 0, 12493, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03ce5110dbc211e668c005ef26792c34', 700001, 900000, 0, 13867, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03cec640dbc211e668c005ef26792c34', 900001, 1100000, 0, 15254, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03cf3b70dbc211e668c005ef26792c34', 1100001, 1300000, 0, 16779, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03cf8990dbc211e668c005ef26792c34', 1300001, 1500000, 0, 18792, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03cfd7b0dbc211e668c005ef26792c34', 1500001, 1700000, 0, 20671, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03d025d0dbc211e668c005ef26792c34', 1700001, 1900000, 0, 22736, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03d073f0dbc211e668c005ef26792c34', 1900001, 2100000, 0, 25010, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03d0e920dbc211e668c005ef26792c34', 2100001, 2500000, 0, 27511, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03d13740dbc211e668c005ef26792c34', 2500001, 3000000, 0, 30264, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03d1ac70dbc211e668c005ef26792c34', 3000001, 3500000, 0, 33289, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03d1fa90dbc211e668c005ef26792c34', 3500001, 4000000, 0, 36620, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03d248b0dbc211e668c005ef26792c34', 0, 100000, 2, 620, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d296d0dbc211e668c005ef26792c34', 100001, 150000, 2, 732, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d30c00dbc211e668c005ef26792c34', 150001, 200000, 2, 869, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d35a20dbc211e668c005ef26792c34', 200001, 250000, 2, 1019, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d3a840dbc211e668c005ef26792c34', 250001, 300000, 2, 1251, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d3f660dbc211e668c005ef26792c34', 300001, 400000, 2, 1477, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d46b90dbc211e668c005ef26792c34', 400001, 500000, 2, 1700, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d4b9b0dbc211e668c005ef26792c34', 500001, 600000, 2, 1937, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d507d0dbc211e668c005ef26792c34', 600001, 700000, 2, 2171, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d555f0dbc211e668c005ef26792c34', 700001, 900000, 2, 2407, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d5cb20dbc211e668c005ef26792c34', 900001, 1100000, 2, 2651, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d64050dbc211e668c005ef26792c34', 1100001, 1300000, 2, 2914, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d6b580dbc211e668c005ef26792c34', 1300001, 1500000, 2, 3264, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d703a0dbc211e668c005ef26792c34', 1500001, 1700000, 2, 3590, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d751c0dbc211e668c005ef26792c34', 1700001, 1900000, 2, 3949, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d79fe0dbc211e668c005ef26792c34', 1900001, 2100000, 2, 4344, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d81510dbc211e668c005ef26792c34', 2100001, 2500000, 2, 4778, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d86330dbc211e668c005ef26792c34', 2500001, 3000000, 2, 5256, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d88a40dbc211e668c005ef26792c34', 3000001, 3500000, 2, 5783, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d8ff70dbc211e668c005ef26792c34', 3500001, 4000000, 2, 6362, 0, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d94d90dbc211e668c005ef26792c34', 0, 100000, 2, 806, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d974a0dbc211e668c005ef26792c34', 100001, 150000, 2, 952, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03d9e9d0dbc211e668c005ef26792c34', 150001, 200000, 2, 1130, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03da37f0dbc211e668c005ef26792c34', 200001, 250000, 2, 1324, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03da8610dbc211e668c005ef26792c34', 250001, 300000, 2, 1627, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03dad430dbc211e668c005ef26792c34', 300001, 400000, 2, 1920, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03db4960dbc211e668c005ef26792c34', 400001, 500000, 2, 2211, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03db7070dbc211e668c005ef26792c34', 500001, 600000, 2, 2518, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03dbe5a0dbc211e668c005ef26792c34', 600001, 700000, 2, 2822, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03dc33c0dbc211e668c005ef26792c34', 700001, 900000, 2, 3129, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03dc81e0dbc211e668c005ef26792c34', 900001, 1100000, 2, 3446, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03dcd000dbc211e668c005ef26792c34', 1100001, 1300000, 2, 3788, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03dd4530dbc211e668c005ef26792c34', 1300001, 1500000, 2, 4243, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03dd9350dbc211e668c005ef26792c34', 1500001, 1700000, 2, 4667, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03dde170dbc211e668c005ef26792c34', 1700001, 1900000, 2, 5134, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03de2f90dbc211e668c005ef26792c34', 1900001, 2100000, 2, 5647, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03de7db0dbc211e668c005ef26792c34', 2100001, 2500000, 2, 6212, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03decbd0dbc211e668c005ef26792c34', 2500001, 3000000, 2, 6833, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03df4100dbc211e668c005ef26792c34', 3000001, 3500000, 2, 7518, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03df8f20dbc211e668c005ef26792c34', 3500001, 4000000, 2, 8270, 1, 1);
INSERT INTO `insewarrantyprice` VALUES ('03dfdd40dbc211e668c005ef26792c34', 0, 100000, 2, 1179, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e02b60dbc211e668c005ef26792c34', 100001, 150000, 2, 1391, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e07980dbc211e668c005ef26792c34', 150001, 200000, 2, 1651, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e0eeb0dbc211e668c005ef26792c34', 200001, 250000, 2, 1936, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e13cd0dbc211e668c005ef26792c34', 250001, 300000, 2, 2378, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e1b200dbc211e668c005ef26792c34', 300001, 400000, 2, 2806, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e20020dbc211e668c005ef26792c34', 400001, 500000, 2, 3231, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e24e40dbc211e668c005ef26792c34', 500001, 600000, 2, 3680, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e2c370dbc211e668c005ef26792c34', 600001, 700000, 2, 4125, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e31190dbc211e668c005ef26792c34', 700001, 900000, 2, 4574, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e35fb0dbc211e668c005ef26792c34', 900001, 1100000, 2, 5036, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e3d4e0dbc211e668c005ef26792c34', 1100001, 1300000, 2, 5537, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e42300dbc211e668c005ef26792c34', 1300001, 1500000, 2, 6201, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e47120dbc211e668c005ef26792c34', 1500001, 1700000, 2, 6821, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e4bf40dbc211e668c005ef26792c34', 1700001, 1900000, 2, 7503, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e50d60dbc211e668c005ef26792c34', 1900001, 2100000, 2, 8253, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e55b80dbc211e668c005ef26792c34', 2100001, 2500000, 2, 9079, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e5d0b0dbc211e668c005ef26792c34', 2500001, 3000000, 2, 9987, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e61ed0dbc211e668c005ef26792c34', 3000001, 3500000, 2, 10987, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e66cf0dbc211e668c005ef26792c34', 3500001, 4000000, 2, 12087, 0, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e6bb10dbc211e668c005ef26792c34', 0, 100000, 2, 1532, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03e73040dbc211e668c005ef26792c34', 100001, 150000, 2, 1808, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03ef1f80dbc211e668c005ef26792c34', 150001, 200000, 2, 2147, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03ef6da0dbc211e668c005ef26792c34', 200001, 250000, 2, 2516, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03efe2d0dbc211e668c005ef26792c34', 250001, 300000, 2, 3091, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03f05800dbc211e668c005ef26792c34', 300001, 400000, 2, 3648, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03f0a620dbc211e668c005ef26792c34', 400001, 500000, 2, 4200, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03f0f440dbc211e668c005ef26792c34', 500001, 600000, 2, 4783, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03f16970dbc211e668c005ef26792c34', 600001, 700000, 2, 5363, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03f1dea0dbc211e668c005ef26792c34', 700001, 900000, 2, 5946, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03f22cc0dbc211e668c005ef26792c34', 900001, 1100000, 2, 6547, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03f27ae0dbc211e668c005ef26792c34', 1100001, 1300000, 2, 7198, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03f2f010dbc211e668c005ef26792c34', 1300001, 1500000, 2, 8062, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03f36540dbc211e668c005ef26792c34', 1500001, 1700000, 2, 8868, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03f3b360dbc211e668c005ef26792c34', 1700001, 1900000, 2, 9754, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03f42890dbc211e668c005ef26792c34', 1900001, 2100000, 2, 10729, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03f476b0dbc211e668c005ef26792c34', 2100001, 2500000, 2, 11803, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03f4ebe0dbc211e668c005ef26792c34', 2500001, 3000000, 2, 12983, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03f56110dbc211e668c005ef26792c34', 3000001, 3500000, 2, 14284, 1, 2);
INSERT INTO `insewarrantyprice` VALUES ('03f67280dbc211e668c005ef26792c34', 3500001, 4000000, 2, 15713, 1, 2);
