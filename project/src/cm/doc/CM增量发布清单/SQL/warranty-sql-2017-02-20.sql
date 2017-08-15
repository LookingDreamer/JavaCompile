ALTER TABLE `couponconfig`
ADD COLUMN `startTime` DATETIME NULL AFTER `defaultCouponTimes`,
ADD COLUMN `endTime` DATETIME NULL AFTER `startTime`;
