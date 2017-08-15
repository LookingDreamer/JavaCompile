/*
SQLyog v10.2 
MySQL - 5.5.11-log : Database - zzb_pro
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`zzb_pro` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `zzb_pro`;

/*Table structure for table `insbrealtimetask` */

DROP TABLE IF EXISTS `insbrealtimetask`;

CREATE TABLE `insbrealtimetask` (
  `id` varchar(32) NOT NULL,
  `operator` varchar(20) NOT NULL COMMENT '操作员',
  `createtime` datetime NOT NULL COMMENT '创建时间',
  `modifytime` datetime DEFAULT NULL COMMENT '修改时间',
  `tasktype` varchar(5) NOT NULL COMMENT '任务类型编码',
  `tasktypename` varchar(40) DEFAULT NULL COMMENT '任务类型名称',
  `carlicenseno` varchar(20) NOT NULL COMMENT '车牌号',
  `insuredid` varchar(32) DEFAULT NULL COMMENT '被保人id',
  `insuredname` varchar(100) DEFAULT NULL COMMENT '被保人姓名',
  `insuredcardno` varchar(50) DEFAULT NULL COMMENT '被保人证件号码',
  `applicantid` varchar(32) DEFAULT NULL COMMENT '投保人id',
  `applicantname` varchar(100) DEFAULT NULL COMMENT '投保人姓名',
  `applicantcardno` varchar(50) DEFAULT NULL COMMENT '投保人证件号码',
  `agentNum` varchar(20) DEFAULT NULL COMMENT '代理人工号',
  `agentName` varchar(100) DEFAULT NULL COMMENT '代理人姓名',
  `deptcode` varchar(32) DEFAULT NULL COMMENT '出单网点comcode',
  `deptname` varchar(100) DEFAULT NULL COMMENT '出单网点名称',
  `deptinnercode` varchar(100) DEFAULT NULL COMMENT '出单网点innercode',
  `inscomcode` varchar(100) DEFAULT NULL COMMENT '保险公司编码',
  `inscomname` varchar(200) DEFAULT NULL COMMENT '保险公司名称',
  `maininstanceid` varchar(32) DEFAULT NULL COMMENT '主任务号',
  `subinstanceid` varchar(32) DEFAULT NULL COMMENT '子任务号',
  `operatorcode` varchar(50) DEFAULT NULL COMMENT '处理人code',
  `operatorname` varchar(50) DEFAULT NULL COMMENT '处理人name',
  `taskcreatetime` datetime DEFAULT NULL COMMENT '任务激活时间',
  `datasourcesfrom` varchar(2) DEFAULT NULL COMMENT '业务来源',
  `channelcode` varchar(32) DEFAULT NULL COMMENT '渠道编码',
  `channelname` varchar(200) DEFAULT NULL COMMENT '渠道名称',
  PRIMARY KEY (`id`),
  KEY `idx-taskcreatetime` (`taskcreatetime`),
  KEY `idx-carlicenseno` (`carlicenseno`),
  KEY `idx-agentNum` (`agentNum`),
  KEY `idx-agentName` (`agentName`),
  KEY `idx-tasktype` (`tasktype`),
  KEY `idx-deptinnercode` (`deptinnercode`),
  KEY `idx-inscomcode` (`inscomcode`),
  KEY `idx-channelcode` (`channelcode`),
  KEY `idx-maininstanceid` (`maininstanceid`),
  KEY `idx-subinstanceid` (`subinstanceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
