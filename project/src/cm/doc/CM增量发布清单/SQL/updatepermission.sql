INSERT INTO `insbpermission` (`id`, `percode`, `permissionname`, `permissionpath`, `createtime`, `istry`, `operator`, `noti`) VALUES ('8', '008', '自有数据', 'cif_own', '2015-12-18 17:51:55', '1', '1', 'cif_own');
INSERT INTO `insbpermission` (`id`, `percode`, `permissionname`, `permissionpath`, `createtime`, `istry`, `operator`, `noti`) VALUES ('9', '009', '续保查询数据', 'cif_renewal', '2015-12-18 17:51:55', '1', '1', 'cif_renewal');
INSERT INTO `insbpermission` (`id`, `percode`, `permissionname`, `permissionpath`, `createtime`, `istry`, `operator`, `noti`) VALUES ('910', '010', '社会化数据', 'cif_social', '2015-12-18 17:51:55', '1', '1', 'cif_social');
DELETE FROM `insbpermission` WHERE `id`='4';


insert into insbpermissionallot
	SELECT CONCAT('cif_own',@rownum:=@rownum+1) AS id,
    `insbpermissionallot`.`setid`,
    '8' as `permissionid`,
    '自有数据' as `permissionname`,
    `insbpermissionallot`.`frontstate`,
    `insbpermissionallot`.`functionstate`,
    `insbpermissionallot`.`validtimestart`,
    `insbpermissionallot`.`num`,
    `insbpermissionallot`.`warningtimes`,
    `insbpermissionallot`.`validtimeend`,
    `insbpermissionallot`.`abort`,
    `insbpermissionallot`.`createtime`,
    `insbpermissionallot`.`modifytime`,
    `insbpermissionallot`.`operator`,
    `insbpermissionallot`.`noti`
FROM `insbpermissionallot`,(SELECT @rownum:=0) c where permissionid = '4';
insert into insbpermissionallot
	SELECT CONCAT('cif_renewal',@rownum:=@rownum+1) AS id,
    `insbpermissionallot`.`setid`,
    '9' as `permissionid`,
    '续保查询数据' as `permissionname`,
    `insbpermissionallot`.`frontstate`,
    `insbpermissionallot`.`functionstate`,
    `insbpermissionallot`.`validtimestart`,
    `insbpermissionallot`.`num`,
    `insbpermissionallot`.`warningtimes`,
    `insbpermissionallot`.`validtimeend`,
    `insbpermissionallot`.`abort`,
    `insbpermissionallot`.`createtime`,
    `insbpermissionallot`.`modifytime`,
    `insbpermissionallot`.`operator`,
    `insbpermissionallot`.`noti`
FROM `insbpermissionallot`,(SELECT @rownum:=0) c where permissionid = '4';
insert into insbpermissionallot
	SELECT CONCAT('cif_social',@rownum:=@rownum+1) AS id,
    `insbpermissionallot`.`setid`,
    '910' as `permissionid`,
    '社会化数据' as `permissionname`,
    `insbpermissionallot`.`frontstate`,
    `insbpermissionallot`.`functionstate`,
    `insbpermissionallot`.`validtimestart`,
    `insbpermissionallot`.`num`,
    `insbpermissionallot`.`warningtimes`,
    `insbpermissionallot`.`validtimeend`,
    `insbpermissionallot`.`abort`,
    `insbpermissionallot`.`createtime`,
    `insbpermissionallot`.`modifytime`,
    `insbpermissionallot`.`operator`,
    `insbpermissionallot`.`noti`
FROM `insbpermissionallot`,(SELECT @rownum:=0) c where permissionid = '4';

delete from `insbpermissionallot` where permissionid = '4';

delete FROM insbagentpermission where permissionid = '4' ;