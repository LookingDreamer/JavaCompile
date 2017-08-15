ALTER TABLE `insborder`
ADD INDEX `idx_paymentstatus_paymentmethod` (`paymentstatus` ASC, `paymentmethod` ASC);

ALTER TABLE `insbfairyinsureerrorlog`
ADD INDEX `idx_taskid` (`taskId` ASC);
