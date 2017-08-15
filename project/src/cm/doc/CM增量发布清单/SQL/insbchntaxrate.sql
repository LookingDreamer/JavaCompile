/**˰�ʱ�**/
create table `insbchntaxrate`(
	`id` varchar(32) not null comment '����',
	`createtime` datetime not null comment '����ʱ��',
	`channelid` varchar(100) not null comment '����id',
	`effectivetime` datetime not null comment '��Чʱ��',
	`terminaltime` datetime default null comment 'ʧЧʱ��',
	`taxrate` decimal(10,2) DEFAULT NULL comment '˰��',
	`status` varchar(1) default null comment '˰��״̬ 1-�����ã�2-�ѹرգ�3-����� ',
	`modifytime` datetime default null comment '����ʱ��',
	`operator` varchar(100) default null comment '������',
	`noti` varchar(1000) default null comment '��ע',
	primary key(`id`),
	key `idx_insbtaxrate_channelid`(`channelid`),
	key `idx_insbtaxrate_taxrate`(`taxrate`)
	
)engine=innodb default charset=utf8 ROW_FORMAT=DYNAMIC comment '˰�ʱ�';

/**�ֵ�洢**/
insert into `insccode`(`id`,`operator`,`createtime`,`noti`,`codetype`,`parentcode`,`codevalue`,`codename`)
values(uuid(),'shiguiwu',now(),'����˰��״̬','taxratestatus','status','1','������'),
(uuid(),'shiguiwu',now(),'����˰��״̬','taxratestatus','status','2','�ѹر�'),
(uuid(),'shiguiwu',now(),'����˰��״̬','taxratestatus','status','3','�����');