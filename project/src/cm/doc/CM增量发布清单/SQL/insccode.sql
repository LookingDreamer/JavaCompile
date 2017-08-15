update insccode set codename = '指定第一受益人(转人工报价)', prop1 = '1' where codetype = 'agentnoti' and codevalue = '1';
update insccode set codename = '修改车辆信息备注(转人工报价)', prop1 = '1' where codetype = 'agentnoti' and codevalue = '2';
update insccode set codename = '客户信息备注(转人工报价)', prop1 = '1' where codetype = 'agentnoti' and codevalue = '3';
update insccode set codename = '指定起保日期(转人工报价)', prop1 = '1' where codetype = 'agentnoti' and codevalue = '4';
update insccode set codename = '其它事宜备注(转人工报价)', prop1 = '1' where codetype = 'agentnoti' and codevalue = '5';
update insccode set codename = '进口车厂牌型号备注(转人工报价)', prop1 = '1' where codetype = 'agentnoti' and codevalue = '6';

insert into `insccode` (`id`, `operator`, `createtime`, `modifytime`, `noti`, `codetype`, `parentcode`, `codevalue`, `codename`, `codeorder`, `prop1`, `prop2`, `prop3`, `prop4`) values('14325cbaf5cfe49238a53d853e55dcb0','qxl',now(),now(),'代理人提核备注','agentnoti1','agentnoti','1','指定第一受益人(转人工核保)','4','0',NULL,NULL,NULL);
insert into `insccode` (`id`, `operator`, `createtime`, `modifytime`, `noti`, `codetype`, `parentcode`, `codevalue`, `codename`, `codeorder`, `prop1`, `prop2`, `prop3`, `prop4`) values('12f11e4d0f685c81d90441b10d317ed0','qxl',now(),now(),'代理人提核备注','agentnoti1','agentnoti','2','修改车辆信息备注(转人工核保)','2','1',NULL,NULL,NULL);
insert into `insccode` (`id`, `operator`, `createtime`, `modifytime`, `noti`, `codetype`, `parentcode`, `codevalue`, `codename`, `codeorder`, `prop1`, `prop2`, `prop3`, `prop4`) values('1e36889f5005c7e91e9cfecaa02de7aa','qxl',now(),now(),'代理人提核备注','agentnoti1','agentnoti','3','客户信息备注(转人工核保)','3','1',NULL,NULL,NULL);
insert into `insccode` (`id`, `operator`, `createtime`, `modifytime`, `noti`, `codetype`, `parentcode`, `codevalue`, `codename`, `codeorder`, `prop1`, `prop2`, `prop3`, `prop4`) values('13d7eb742435c145cac2dc1a5e1efa80','qxl',now(),now(),'代理人提核备注','agentnoti1','agentnoti','4','指定起保日期(转人工核保)','1','1',NULL,NULL,NULL);
insert into `insccode` (`id`, `operator`, `createtime`, `modifytime`, `noti`, `codetype`, `parentcode`, `codevalue`, `codename`, `codeorder`, `prop1`, `prop2`, `prop3`, `prop4`) values('1275a109b87c5be890d3eb59d60c8200','qxl',now(),now(),'代理人提核备注','agentnoti1','agentnoti','5','其它事宜备注(转人工核保)','6','0',NULL,NULL,NULL);
insert into `insccode` (`id`, `operator`, `createtime`, `modifytime`, `noti`, `codetype`, `parentcode`, `codevalue`, `codename`, `codeorder`, `prop1`, `prop2`, `prop3`, `prop4`) values('1275h109q87c5be890d3eb59d60c8200','qxl',now(),now(),'代理人提核备注','agentnoti1','agentnoti','6','进口车厂牌型号备注(转人工核保)','5','0',NULL,NULL,NULL);

insert into `insccode` (`id`, `operator`, `createtime`, `modifytime`, `noti`, `codetype`, `parentcode`, `codevalue`, `codename`, `codeorder`, `prop1`, `prop2`, `prop3`, `prop4`) values('ztjdabc0000021','1',now(),now(),'工作流节点状态码对应关系','workflowNodelName','workflowNodelName','40','EDI自动核保','60','0','EDI自动核保','','1');
insert into `insccode` (`id`, `operator`, `createtime`, `modifytime`, `noti`, `codetype`, `parentcode`, `codevalue`, `codename`, `codeorder`, `prop1`, `prop2`, `prop3`, `prop4`) values('ztjdabc0000022','1',now(),now(),'工作流节点状态码对应关系','workflowNodelName','workflowNodelName','41','精灵自动核保','70','0','精灵自动核保','','1');


ALTER TABLE  insbpolicyitem add column biztype varchar(20);