/********************************************************
 * 2015-06-26
 * version : 2.0
 * author : 郝利军 
 * cell phone : 15510124410
 *******************************************************/
一、开发说明如下：
1.代码结构
	1）java代码,
		所有的java代码都要放在包路径src/main/java/com/*/**下
		,src/main/java/com/cninsure/**为系统管理模块禁止添加内容
		,在此路径下新建模块,在模块下新建controller、entity、service、service/impl、dao、dao/impl
		,参考结构见：src/main/java/com/cninsure/system
	2）mapper.xml文件,
		所有的mapper文件都要放在目录src/main/resources/mapping/mysql下
		,在此路径下新建子项目名称
		,在子项目名称下新建模块,在模块下新建*Mapper.xml文件
		,参考结构见：src/main/resources/mapping/mysql/system
	3）css,img,js静态文件,
		所有的静态文件都要放在目录src/main/webapp/static下,其中
		,css文件放在src/main/webapp/static/css下
		,img文件放在src/main/webapp/static/images下
		,js文件放在src/main/webapp/static/js下,
		,在此路径下新建模块,在模块下新建相应的静态文件
		,参考结构见：src/main/webapp/static/css/system
				 src/main/webapp/static/images/system
				 src/main/webapp/static/js/system
	4）ftl视图文件,
		所有的视图文件都要放在目录src/main/webapp/WEB-INF/freemaker下
		,在此路径下新建模块,在模块下新建ftl视图文件
		,参考结构见：src/main/webapp/WEB-INF/freemaker/system
	5）单元测试文件,
		所有的java测试文件都要放在目录src/test/java下
		,所有的测试配制文件都要放在目录src/test/resources下
		,在此路径下新建模块,在模块下新建相应的文件
	
注意：1)、2)、3)、4)、5)中的模块名要保持一致。
	
2.命名规则
	1）java代码(INSC为系统管理模块相关表,业务表以INSB为前缀),
		controller：      INSB + 单词首字母大写 + Controller.java  ,例如:INSBUserRoleController.java
		entity：                  INSB + 单词首字母大写 .java              ,例如:INSBUserRole.java
		service：               INSB + 单词首字母大写 + Service.java     ,例如:INSBUserRoleService.java
		service/impl：INSB + 单词首字母大写 + ServiceImpl.java ,例如:INSBUserRoleServiceImpl.java
		dao：                           INSB + 单词首字母大写 + Dao.java         ,例如:INSBUserRoleDao.java
		dao/impl：            INSB + 单词首字母大写 + DaoImpl.java     ,例如:INSBUserRoleDaoImpl.java
	2） mapper.xml文件： INSB + 单词首字母大写 + Mapper.xml		   ,例如:INSBUserRoleMapper.xml
	3） 静态文件
		css：以小写字母开头,第二和第二个之后的每个单词首字母大写  + .css      ,例如: userList.css
		img：以小写字母开头,第二和第二个之后的每个单词首字母大写  + .gif	    ,例如: imgIconBtn.gif
		js：   以小写字母开头,第二和第二个之后的每个单词首字母大写  + .js       ,例如: userList.js
	4）ftl视图文件:  以小写字母开头,第二和第二个之后的每个单词首字母大写  + .js  ,例如: userList.ftl
		
3.数据库表命名规则
	1） 表名命名规则：insb + 单词全部小写					,例如: insbuserrole
	2） 表字段名命名规则: 单词全部小写（不允许出现下划线）		,例如: createtime
	
二 、代码生成器说明如下：
1.在配制文件 src/main/resources/config/config.properties中配制如下参数：
   1）generator.tableNames（数据库表名,多个表名时需要用逗号分开）
   2）generator.package.prefix （模块名称前的包前缀）
   3）generator.sysModule （模块名称,目前只支持配制单个模块） 
   4）generator.outputFilePath （生成的文件输出的路径）
   5）generator.generate.entity （是否需要生成实体文件，true：生成；false：不生成）
   6）generator.generate.mapper （是否需要生成mapper文件，true：生成；false：不生成）
   7）generator.generate.service（是否需要生成service及serviceImpl文件，true：生成；false：不生成）
   8）generator.generate.dao    （是否需要生成dao及daoImpl文件，true：生成；false：不生成）

2、运行程序src/main/java/com/cninsure/system/tool/Generator.java

/********************************************************
 * 2015-6-25
 * version : 1.1(add)
 * author : 张帝 
 * cell phone : 18310766458
 *******************************************************/
一、数据库开发规范
	1）表名命名,
		insc开头代表系统基础表,
		,insb开头代表业务逻辑表
		,所有的表名和字段名均为小写,实体类参数也为小写,且与数据库保持一致
	2）表结构必选字段,
		id、operator、createtime、modifytime、noti
		,分别代表主键（非空）、操作员（非空）、创建时间（非空）、修改时间（可空）、备注（可空）
		,Id通过UUID获得
		,字段名为小写,写上注释
	3）字段类型,
		字符类型用varchar（JAVA_String）
		,数字整形用int（JAVA_int）
		,数字非整形用decimal(16,4)（JAVA_Double）
		,日期用datetime（JAVA_Date）
二、前台页面开发规范
	1）css引用路径,
		${staticRoot}代表/cm/static
		,例：${staticRoot}/css/example.css即为/cm/static /css/example.css
	2）js引用,
		如果页面中用到jquery或jquery的组件等js文件
		,需写上<script data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.min.js"></script>
		,并用requirejs([ " example " ])
		,表示引用/cm/static/js/example.js
		,注：若引用多个,则requirejs([ " example1 "," example2 " ])
		, 且不需要写 .js
	3）load.js文件说明,
		"paths" : {}中用来定义js的声明和别名
		,例："jquery" : "lib/jquery.min",表示用别名”jquery”代表/cm/static/js/lib下的jquery.min.js
		,"shim" : {}用来定义js文件的依赖关系,即js的加载顺序
		,例："jqcookie": ["jquery"],表示别名” jqcookie”对应的jquery.cookie.js文件依赖于别名” jquery”对应的jquery.min.js文件
	4）example.js文件说明,
		require(["jquery",” jqtreeview”], function ($) {})
		,表示该js文件引用哪些js文件,其中使用js文件的别名定义
		,$(function() { });
		,表示使用jquery,在大括号里面写jquery代码,参考dept.js


/********************************************************
 * 2015-06-24
 * version : 1.0
 * author : 郝利军 
 * cell phone : 15510124410
 *******************************************************/
一、开发说明如下：
1.代码结构
	1）java代码,
		所有的java代码都要放在包路径src/main/java/com/*/**下
		,src/main/java/com/cninsure/**为系统管理模块禁止添加内容
		,在此路径下新建模块,在模块下新建controller、entity、service、service/impl、dao、dao/impl
		,参考结构见：src/main/java/com/cninsure/system
	2）mapper.xml文件,
		所有的mapper文件都要放在目录src/main/resources/mapping/mysql下
		,在此路径下新建模块,在模块下新建*Mapper.xml文件
		,参考结构见：src/main/resources/mapping/mysql/system
	3）css,img,js静态文件,
		所有的静态文件都要放在目录src/main/webapp/static下,其中
		,css文件放在src/main/webapp/static/css下
		,img文件放在src/main/webapp/static/images下
		,js文件放在src/main/webapp/static/js下,
		,在此路径下新建模块,在模块下新建相应的静态文件
		,参考结构见：src/main/webapp/static/css/system
				 src/main/webapp/static/images/system
				 src/main/webapp/static/js/system
	4）ftl视图文件,
		所有的视图文件都要放在目录src/main/webapp/WEB-INF/freemaker下
		,在此路径下新建模块,在模块下新建ftl视图文件
		,参考结构见：src/main/webapp/WEB-INF/freemaker/system
	5）单元测试文件,
		所有的java测试文件都要放在目录src/test/java下
		,所有的测试配制文件都要放在目录src/test/resources下
		,在此路径下新建模块,在模块下新建相应的文件
	
注意：1)、2)、3)、4)、5)中的模块名要保持一致。
	
2.命名规则
	1）java代码(INSC为系统管理模块相关表,业务表以INSB为前缀),
		controller：      INSB + 单词首字母大写 + Controller.java  ,例如:INSBUserRoleController.java
		entity：                  INSB + 单词首字母大写 .java              ,例如:INSBUserRole.java
		service：               INSB + 单词首字母大写 + Service.java     ,例如:INSBUserRoleService.java
		service/impl：INSB + 单词首字母大写 + ServiceImpl.java ,例如:INSBUserRoleServiceImpl.java
		dao：                           INSB + 单词首字母大写 + Dao.java         ,例如:INSBUserRoleDao.java
		dao/impl：            INSB + 单词首字母大写 + DaoImpl.java     ,例如:INSBUserRoleDaoImpl.java
	2） mapper.xml文件： INSB + 单词首字母大写 + Mapper.xml		   ,例如:INSBUserRoleMapper.xml
	3） 静态文件
		css：以小写字母开头,第二和第二个之后的每个单词首字母大写  + .css      ,例如: userList.css
		img：以小写字母开头,第二和第二个之后的每个单词首字母大写  + .gif	    ,例如: imgIconBtn.gif
		js：   以小写字母开头,第二和第二个之后的每个单词首字母大写  + .js       ,例如: userList.js
	4）ftl视图文件:  以小写字母开头,第二和第二个之后的每个单词首字母大写  + .js  ,例如: userList.ftl
		
3.数据库表命名规则
	1） 表名命名规则：insb + 单词全部小写					,例如: insbuserrole
	2） 表字段名命名规则: 单词全部小写（不允许出现下划线）		,例如: createtime
	
二 、代码生成器说明如下：
1.在配制文件 src/main/resources/config/config.properties中配制如下参数：
   1）generator.tableNames（数据库表名,多个表名时需要用逗号分开）
   2）generator.sysModule （模块名称,目前只支持配制单个模块） 
   3）generator.outputFilePath （生成的文件输出的路径）
2、运行程序src/main/java/com/cninsure/system/tool/Generator.java
