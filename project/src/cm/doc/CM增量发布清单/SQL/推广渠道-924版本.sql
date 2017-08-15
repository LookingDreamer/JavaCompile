
# 推广渠道 李武才 20160914
ALTER TABLE `zzb_uat`.`insbagent`
  ADD COLUMN `pushChannel` VARCHAR(10) NULL  COMMENT '推广渠道' AFTER `weixinheadphotourl`,
  ADD COLUMN `pushWay` VARCHAR(10) NULL  COMMENT '推广方式' AFTER `pushChannel`;
