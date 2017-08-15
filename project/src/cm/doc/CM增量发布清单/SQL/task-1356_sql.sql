ALTER TABLE `insbagent`
ADD COLUMN `clienttype`  tinyint UNSIGNED NULL COMMENT '1.ios, 2.android, 3.web, 4.wechat' AFTER `recommendtype`;

ALTER TABLE `insbpaychannel`
ADD COLUMN `clienttypes` VARCHAR(60) NULL DEFAULT 'web,weChat,ios,android' COMMENT '支持的终端类型,作为一个集合，以逗号分隔' AFTER `noti`;

ALTER TABLE `insbpaychannel`
ADD COLUMN `onlyedionlineunderwriting` SMALLINT(1) NULL DEFAULT 0 COMMENT '是否仅支持线上EDI核保' AFTER `clienttypes`;

