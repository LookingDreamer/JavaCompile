ALTER TABLE `insbworkflowsub`
ADD COLUMN `firstunderwritingtime`  datetime NULL AFTER `wfsubtrackid`,
ADD COLUMN `lastunderwritingovertime`  datetime NULL AFTER `firstunderwritingtime`;