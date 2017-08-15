1. task438-增加证件类型
   戴华侨
INSERT INTO `insccode` (`id`, `operator`, `createtime`, `modifytime`, `noti`, `codetype`, `parentcode`, `codevalue`, `codename`, `codeorder`, `prop1`, `prop2`, `prop3`, `prop4`)
 VALUES ('990e8dai666b11e55d44746e9a37c00c', '1', '2016-09-01 01:01:01', NULL, '证件种类', 'CertKinds', 'CertKinds', '9', '税务登记证', 9, NULL, NULL, NULL, NULL);
INSERT INTO `insccode` (`id`, `operator`, `createtime`, `modifytime`, `noti`, `codetype`, `parentcode`, `codevalue`, `codename`, `codeorder`, `prop1`, `prop2`, `prop3`, `prop4`)
 VALUES ('990e8dai888b11e55d44746e9a37c00c', '1', '2016-09-01 01:01:01', NULL, '证件种类', 'CertKinds', 'CertKinds', '10', '营业执照', 10, NULL, NULL, NULL, NULL);
 
 
2. req-440-CM增加两个证件类型
   马帅

-- INSERT INTO insccode VALUES ('990e83508d0b11e55d44746e9a37c00d', '1', '2016-09-02 17:14:25', NULL, '证件种类', 'CertKinds', 'CertKinds', '9', '税务登记证', 9, 0, NULL, NULL, NULL);
-- INSERT INTO insccode VALUES ('990e83508d0b11e55d44746e9a37c00e', '1', '2016-09-02 17:14:25', NULL, '证件种类', 'CertKinds', 'CertKinds', '10', '营业执照', 10, 0, NULL, NULL, NULL);
UPDATE insbcorecodemap SET cmcode='9',cmname='税务登记证' where id='102';
UPDATE insbcorecodemap SET cmcode='10',cmname='营业执照' where id='103';
INSERT INTO insbcorecodemap VALUES ('2404', '', '', NULL, NULL, '72', '税务登记证', '9', '税务登记证', 'idcardtype');
INSERT INTO insbcorecodemap VALUES ('2405', '', '', NULL, NULL, '73', '营业执照', '10', '营业执照', 'idcardtype');


3.task421-增加保险配置与上年一致字段
   戴华侨
ALTER TABLE `insbcarinfo`
ADD COLUMN `insureconfigsameaslastyear`  varchar(2) NULL COMMENT '保险配置是否与上年一致(1:是)' AFTER `cifstandardname`;
ALTER TABLE `insbcarinfohis`
ADD COLUMN `insureconfigsameaslastyear`  varchar(2) NULL COMMENT '保险配置是否与上年一致(1:是)' AFTER `cifstandardname`;


4.bug4545-车辆使用性质去掉旅游营业客车
   戴华侨
UPDATE insccode SET codetype='UseProps_bak', parentcode='UseProps_bak' WHERE id='db1cec00772111e573ef10b28965b75d';

5-退回修改备注更新20160913
ALTER TABLE `insbagent`
MODIFY COLUMN `approvesstate`  int(1) NULL DEFAULT 1 COMMENT '认证状态  1-未认证  2-认证中  3-认证通过  4-认证失败 5-退回修改' AFTER `firstlogintime`;
ALTER TABLE `insbcertification`
MODIFY COLUMN `status`  tinyint(1) NOT NULL DEFAULT 0 COMMENT '0-初始 ；1-审核通过 ；2-退回修改；3-认证失败' AFTER `agree`;

6-task365-渠道配置后台，去掉 平台查询 的权限配置
  伍尚森
DELETE FROM zzb_uat.insbagreementinterface WHERE interfaceid=13
DELETE FROM zzb_uat.insbagreementinterface_20160913 WHERE interfaceid=13
DELETE FROM zzb_uat.insbagreementinterface_copy1 WHERE interfaceid=13
DELETE FROM zzb_uat.insbinterface WHERE id=13


7-task380-渠道配置后台，接口权限模块优化
  伍尚森
ALTER TABLE `insbagreementinterface`
ADD COLUMN `extendspattern`  varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '免费次数用完后能否继续使用 0-收费模式 1-禁止使用' AFTER `noti`;


8.task-386-报表涉及的工作流及cm节点处理(1029版本)
   戴华侨
ALTER TABLE `insbworkflowsub` ADD COLUMN `wfsubtrackid`  varchar(32) NULL AFTER `operatorstate`;
<<<<<<< .working
ALTER TABLE `insbworkflowsubtrackdetail` ADD COLUMN `wfsubtrackid`  varchar(32) NULL AFTER `taskfeedback`, ADD INDEX `wfsubtrackid_index` (`wfsubtrackid`) USING BTREE ;

9-task541 【1029】CM，提供接口，能够判断订单是否为“已失效订单”，并按不同情况进行不同的删除操作
ALTER TABLE `zzb_uat`.`insbquotetotalinfo`   
  ADD COLUMN `deleteflag` VARCHAR(1) NULL  COMMENT '删掉订单标志 1 删除' AFTER `datasourcesfrom`;||||||| .merge-left.r61
ALTER TABLE `insbworkflowsubtrackdetail` ADD COLUMN `wfsubtrackid`  varchar(32) NULL AFTER `taskfeedback`, ADD INDEX `wfsubtrackid_index` (`wfsubtrackid`) USING BTREE ;=======
ALTER TABLE `insbworkflowsubtrackdetail` ADD COLUMN `wfsubtrackid`  varchar(32) NULL AFTER `taskfeedback`, ADD INDEX `wfsubtrackid_index` (`wfsubtrackid`) USING BTREE ;


9.task-565-核保状态自动查询(1029版本)
   戴华侨
CREATE TABLE `insbloopunderwriting` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `operator` varchar(20) NOT NULL COMMENT '操作员',
  `createtime` datetime NOT NULL COMMENT '创建时间',
  `modifytime` datetime DEFAULT NULL COMMENT '修改时间',
  `noti` text COMMENT '备注',
  `taskid` varchar(32) NOT NULL COMMENT '主任务号',
  `inscomcode` varchar(50) NOT NULL COMMENT '保险公司代码',
  `taskcreatetime` datetime DEFAULT NULL COMMENT '轮询任务创建时间',
  `loopstatus` varchar(20) DEFAULT NULL COMMENT '轮询状态',
  `tasktype` varchar(20) DEFAULT NULL COMMENT '任务类型',
  PRIMARY KEY (`id`),
  KEY `idx_insbloopunderwriting_taskid` (`taskid`) USING BTREE,
  KEY `idx_insbloopunderwriting_inscomcode` (`inscomcode`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT '任务轮询信息表';

CREATE TABLE `insbloopunderwritingdetail` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `operator` varchar(20) NOT NULL COMMENT '操作员',
  `createtime` datetime NOT NULL COMMENT '创建时间',
  `modifytime` datetime DEFAULT NULL COMMENT '修改时间',
  `noti` text COMMENT '备注',
  `loopid` varchar(32) NOT NULL COMMENT '任务轮询信息表主键',
  `starttime` datetime DEFAULT NULL COMMENT '轮询开始时间',
  `loopresult` varchar(20) DEFAULT NULL COMMENT '轮询结果',
  `msg` varchar(1000) DEFAULT NULL COMMENT '结果详情',
  PRIMARY KEY (`id`),
  KEY `idx_insbloopunderwritingdetail_loopid` (`loopid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT '任务轮询信息详情表';

INSERT INTO `inscmenu` (`id`, `operator`, `createtime`, `modifytime`, `noti`, `nodecode`, `parentnodecode`, `nodelevel`, `nodename`, `childflag`, `activeurl`, `iconurl`, `orderflag`, `status`)
 VALUES ('28fa5da091ba11e650e9c646453ae28d', '1', '2016-10-14 10:59:01', '2016-10-14 10:59:40', '核保轮询状态查询', 'm001606', 'm0016', '2', '核保轮询状态查询', '0', 'business/loop/loopunderwritinglist', 'fui-time', 10, '1');
 
 
 10-task541 【1029】CM，提供接口，能够判断订单是否为“已失效订单”，并按不同情况进行不同的删除操作
ALTER TABLE `zzb_uat`.`insbquotetotalinfo`   
  ADD COLUMN `deleteflag` VARCHAR(1) NULL  COMMENT '删掉订单标志 1 删除' AFTER `datasourcesfrom`;
>>>>>>> .merge-right.r1146
