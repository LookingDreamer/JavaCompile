/**��ɫ��**/
create table `insbminirole`(
	`id` varchar(32) not null comment '����',
	`rolecode` varchar(21) default null comment'��ɫ���',
	`name` varchar(100) default null  comment '��ɫ����',
	`description` varchar(1000) default null comment '��ɫ����',
	`createtime` datetime not null comment '����ʱ��',
	`modifytime` datetime default null comment '����ʱ��',
	`operator` varchar(100) default null comment '������',
	`noti` varchar(1000) default null comment '��ע',
	primary key (`id`)
)ENGINE=innoDB default charset=utf8;
/**Ȩ�ޱ�**/
create table `insbminipermission`(
	`id` varchar(32) not null comment'����',
	`perindex` int(20) default null,
	`percode` varchar(255) default null comment 'Ȩ�ޱ��',
	`permissionname` varchar(100) default null comment 'Ȩ������',
	`perdesc` varchar(1000) default null comment 'Ȩ������',
	`url` varchar(1000) default null comment '��Դ·��',
	`createtime` datetime not null comment '����ʱ��',
	`modifytime` datetime default null comment '����ʱ��',
	`operator` varchar(100) default null comment '������',
	`noti` varchar(1000) default null comment '��ע',
	primary key (`id`)
)ENGINE=innoDB default charset=utf8;
/**�û���ɫ��**/
create table `insbminiuserrole`(
	`id` varchar(32) not null comment '����',
	`miniuserid` varchar(32) not null comment 'mini�û�id',
	`roleid` varchar(32) not null comment '��ɫid',
	`createtime` datetime not null comment '����ʱ��',
	`modifytime` datetime default null comment '����ʱ��',
	`operator` varchar(100) default null  comment '������',
	`noti` varchar(1000) default null comment '��ע',
	primary key (`id`)
)ENGINE=innoDB default charset=utf8;
/**��ɫȨ�ޱ�**/
create table `insbrolepermission`(
	`id` varchar(32) not null comment '����',
	`roleid` varchar(32) not null comment '��ɫid',
	`permissionid` varchar(32) default null comment 'Ȩ��id',
	`createtime` datetime not null comment '����ʱ��',
	`modifytime` datetime default null comment '����ʱ��',
	`operator` varchar(100) default null comment '������',
	`noti` varchar(1000) default null comment '��ע',
	primary key (`id`)
)ENGINE=innoDB default charset=utf8;
ALTER TABLE `insbminipermission` ADD INDEX `percodeindex` (`percode`);
/**�������û��ֽ�ɫ**/
insert into `insbminirole`(`id`,`rolecode`,`name`,`createtime`)values ('8f0b994200c987aebfe38fdfc313d660','01','�Ŷӳ�',NOW());
insert into `insbminirole`(`id`,`rolecode`,`name`,`createtime`)values ('9c979c2acf80ccb6ebde0ae0907f77e6','03','��ͨ��Ա',NOW());
insert into `insbminirole`(`id`,`rolecode`,`name`,`createtime`)values ('fd5befe0a94fd4cfaea89edee88024da','02','�Ŷӳ�Ա',NOW());