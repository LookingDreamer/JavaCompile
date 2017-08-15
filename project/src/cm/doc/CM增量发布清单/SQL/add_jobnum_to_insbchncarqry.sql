DROP PROCEDURE IF EXISTS add_jobnum_to_insbchncarqry;
CREATE PROCEDURE add_jobnum_to_insbchncarqry()
  BEGIN
    DECLARE oneAddr VARCHAR(100) DEFAULT '';
    DECLARE allAddr VARCHAR(255) DEFAULT '';
    DECLARE done INT DEFAULT 0;
    DECLARE curl CURSOR FOR SELECT table_name FROM information_schema.tables WHERE table_schema='zzb_uat' AND table_name LIKE  'insbchncarqry%' AND table_name <> 'insbchncarqrycount';
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN curl;
    REPEAT
      FETCH curl INTO oneAddr;
      IF NOT done THEN
         SET @sql=CONCAT('alter table ',oneAddr,' add jobnum varchar(50) COMMENT \'出单工号\' ');
         PREPARE stmt FROM @sql;
         EXECUTE stmt;
      END IF;
    UNTIL done END REPEAT;
    SELECT allAddr;
    CLOSE curl;
  END;
  CALL add_jobnum_to_insbchncarqry();