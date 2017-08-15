ALTER TABLE `insequote`
ADD COLUMN `standardfullname` VARCHAR(45) NULL AFTER `standardname`;

ALTER TABLE `insequote`
ADD COLUMN `cifstandardname` VARCHAR(45) NULL AFTER `standardfullname`;
