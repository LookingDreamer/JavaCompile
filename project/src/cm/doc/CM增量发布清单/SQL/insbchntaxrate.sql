/**税率表**/
create table `insbchntaxrate`(
	`id` varchar(32) not null comment '主键',
	`createtime` datetime not null comment '创建时间',
	`channelid` varchar(100) not null comment '渠道id',
	`effectivetime` datetime not null comment '生效时间',
	`terminaltime` datetime default null comment '失效时间',
	`taxrate` decimal(10,2) DEFAULT NULL comment '税率',
	`status` varchar(1) default null comment '税率状态 1-已启用，2-已关闭，3-待审核 ',
	`modifytime` datetime default null comment '更新时间',
	`operator` varchar(100) default null comment '操作者',
	`noti` varchar(1000) default null comment '备注',
	primary key(`id`),
	key `idx_insbtaxrate_channelid`(`channelid`),
	key `idx_insbtaxrate_taxrate`(`taxrate`)
	
)engine=innodb default charset=utf8 ROW_FORMAT=DYNAMIC comment '税率表';

/**字典存储**/
insert into `insccode`(`id`,`operator`,`createtime`,`noti`,`codetype`,`parentcode`,`codevalue`,`codename`)
values(uuid(),'shiguiwu',now(),'渠道税率状态','taxratestatus','status','1','已启用'),
(uuid(),'shiguiwu',now(),'渠道税率状态','taxratestatus','status','2','已关闭'),
(uuid(),'shiguiwu',now(),'渠道税率状态','taxratestatus','status','3','待审核');