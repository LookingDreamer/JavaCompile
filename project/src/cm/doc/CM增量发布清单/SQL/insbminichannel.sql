/*
Navicat MySQL Data Transfer

Source Server         : Test
Source Server Version : 50511
Source Host           : 10.68.3.63:3306
Source Database       : zzb_pro

Target Server Type    : MYSQL
Target Server Version : 50511
File Encoding         : 65001

Date: 2016-10-18 16:01:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for insbminichannel
-- ----------------------------
CREATE TABLE `insbminichannel` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `channelcode` varchar(10) NOT NULL COMMENT '渠道编码',
  `channelname` varchar(50) NOT NULL COMMENT '渠道名称',
  `waynum` int(11) DEFAULT NULL COMMENT '渠道推广途径数量',
  `tempcode` int(11) NOT NULL COMMENT '临时编码',
  `terminaldate` datetime NOT NULL COMMENT '渠道合作截止时间',
  `operator` varchar(20) DEFAULT NULL COMMENT '操作员',
  `createtime` datetime NOT NULL COMMENT '创建时间',
  `modifytime` datetime DEFAULT NULL COMMENT '修改时间',
  `noti` varchar(100) DEFAULT NULL COMMENT '渠道描述',
  PRIMARY KEY (`id`),
  KEY `idx_insbminichannel_channelcode` (`channelcode`),
  KEY `idx_insbminichannel_channelname` (`channelname`),
  KEY `idx_insbminichannel_terminaldate` (`terminaldate`),
  KEY `idx_insbminichannel_createtime` (`createtime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='minizzb渠道';

-- ----------------------------
-- Table structure for insbminichannelway
-- ----------------------------
CREATE TABLE `insbminichannelway` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `channelcode` varchar(10) NOT NULL COMMENT '渠道编码',
  `waycode` int(11) NOT NULL COMMENT '渠道推广途径编码',
  `wayname` varchar(50) NOT NULL COMMENT '渠道推广途径名称',
  `operator` varchar(20) DEFAULT NULL COMMENT '操作员',
  `createtime` datetime NOT NULL COMMENT '创建时间',
  `modifytime` datetime DEFAULT NULL COMMENT '修改时间',
  `noti` varchar(100) DEFAULT NULL COMMENT '渠道描述',
  PRIMARY KEY (`id`),
  KEY `idx_insbminichannelway_channelcode` (`channelcode`),
  KEY `idx_insbminichannelway_waycode` (`waycode`),
  KEY `idx_insbminichannelway_createtime` (`createtime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='minizzb渠道推广途径';
