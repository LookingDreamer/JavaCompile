/**角色表**/
create table `insbminirole`(
	`id` varchar(32) not null comment '主键',
	`rolecode` varchar(21) default null comment'角色编号',
	`name` varchar(100) default null  comment '角色名称',
	`description` varchar(1000) default null comment '角色描述',
	`createtime` datetime not null comment '创建时间',
	`modifytime` datetime default null comment '更新时间',
	`operator` varchar(100) default null comment '操作者',
	`noti` varchar(1000) default null comment '备注',
	primary key (`id`)
)ENGINE=innoDB default charset=utf8;
/**权限表**/
create table `insbminipermission`(
	`id` varchar(32) not null comment'主键',
	`perindex` int(20) default null,
	`percode` varchar(255) default null comment '权限编号',
	`permissionname` varchar(100) default null comment '权限名称',
	`perdesc` varchar(1000) default null comment '权限描述',
	`url` varchar(1000) default null comment '资源路径',
	`createtime` datetime not null comment '创建时间',
	`modifytime` datetime default null comment '更新时间',
	`operator` varchar(100) default null comment '操作者',
	`noti` varchar(1000) default null comment '备注',
	primary key (`id`)
)ENGINE=innoDB default charset=utf8;
/**用户角色表**/
create table `insbminiuserrole`(
	`id` varchar(32) not null comment '主键',
	`miniuserid` varchar(32) not null comment 'mini用户id',
	`roleid` varchar(32) not null comment '角色id',
	`createtime` datetime not null comment '创建时间',
	`modifytime` datetime default null comment '更新时间',
	`operator` varchar(100) default null  comment '操作者',
	`noti` varchar(1000) default null comment '备注',
	primary key (`id`)
)ENGINE=innoDB default charset=utf8;
/**角色权限表**/
create table `insbrolepermission`(
	`id` varchar(32) not null comment '主键',
	`roleid` varchar(32) not null comment '角色id',
	`permissionid` varchar(32) default null comment '权限id',
	`createtime` datetime not null comment '创建时间',
	`modifytime` datetime default null comment '更新时间',
	`operator` varchar(100) default null comment '操作者',
	`noti` varchar(1000) default null comment '备注',
	primary key (`id`)
)ENGINE=innoDB default charset=utf8;
ALTER TABLE `insbminipermission` ADD INDEX `percodeindex` (`percode`);
/**将已有用户分角色**/
insert into `insbminirole`(`id`,`rolecode`,`name`,`createtime`)values ('8f0b994200c987aebfe38fdfc313d660','01','团队长',NOW());
insert into `insbminirole`(`id`,`rolecode`,`name`,`createtime`)values ('9c979c2acf80ccb6ebde0ae0907f77e6','03','普通成员',NOW());
insert into `insbminirole`(`id`,`rolecode`,`name`,`createtime`)values ('fd5befe0a94fd4cfaea89edee88024da','02','团队成员',NOW());