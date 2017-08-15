ALTER TABLE `usercoupon`
ADD COLUMN `orderno` VARCHAR(36) NULL AFTER `productCode`;

ALTER TABLE `usercoupon`
ADD INDEX `idx_orderno` (`orderno` ASC);