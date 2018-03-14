# JavaCompile
Java compile是一个自动增量/全量编译java的api接口应用。
包含以下特性:
- 全量编译
- 增量编译
- svnkit接口(svn操作web接口)
- 全流程使用案例
***
[toc]

## 说在最前面的话
> 本人只是一枚从事运维6年的老兵，从今年年初一直在构建自动化运维平台，从持续交互着手，流程最开始的就是代码的抽包编译(这里说的java)，然后研究了一下关于java的编译的相关知识。
> 传统的开发人员直接利用java编程工具直接对java进行编译，然后就是maven,ant等等工具对java编译。当然也少不了javac，我想大部分学习java的同学都是从javac来编译开始。
> 废话不多说，开始说正题。
> 自动化运维体系就是将一切可以自动化的东西自动化，减少人工干预。
> **早期我们会经历这样的一个阶段**
>

```flow
st=>start: 1.开发人员通过工具打好包给运维发布
e=>end
op=>operation: 2.运维人员自己通过编程工具打包发布
op1=>operation: 3.运维人员通过mvn打整包发布
op2=>operation: 4.运维人员利用jenkins将mvn命令整合打包发布
op3=>operation: 5.开发人员开发java打包工具通过抽取注释的方法增量打包发布
op4=>operation: 6.开发人员开发java web打包工具通过mvn命令打整包然后在抽增量包
st->op->op1->op2->op3->op4

```

 _md不支持流程图,补一张截图_ 
![输入图片说明](https://gitee.com/uploads/images/2018/0315/001242_2b28c028_119746.png "屏幕截图.png")

目前就我所经历的java抽包编译的这么一个发展，其中我们也使用过teamcity来进行编译，后来由于适应性的问题放弃。目前我们使用开发人员自己开发的java web打包工具，原理就是利用mvn命令打整包，然后通过svn命令获取相关文件增量抽包，这样的一个好处就是纯web实现，无需人工干涉。唯一不好的地方就是即使maven库是私有库，但是mvn去编clean到生成包的过程还是很慢。根据java工程的大小大概需要几分钟甚至更久的时间。
## 开发状态
本人只是一个java初学者,只是非常粗糙的完成了相应的功能，并进行了简单的测试。
## 原理
`Java全量编译:` 通过mvn命令直接对应用打包。
`Java增量编译:` 通过java8自带的ToolProvider来对java进行增量编译。
## 快速开始
> 1. 准备tomcat环境，建议tomcat8以上
> 2. 在[附件](https://gitee.com/lookingdreamer/pack/attach_files)处下载最新已经编译完成好的pack.war部署到tomcat根目录
> 3. 修改对应配置文件，启动即可

配置文件修改如下（配置文件路径:WEB-INF/classes/config.properties）
```
#svn地址
svn.url = http://code.taobao.org/svn/mjfinal_cms
#svn账号
svn.username = huanggaoming
#svn密码
svn.password = 123456789
#svn项目路径
svn.project_suffix = /trunk
#默认获取svn提交的天数
svn.interval_days = 7
#源码路径(相对于项目路径)
src.java = /src/main/java/
#配置资料文件路径(相对于项目路径)
src.resources = /src/main/resources/
#WebRoot路径(相对于项目路径)
src.webapp = /src/main/webapp/
```
## 使用示例
具体的接口使用文档请撮: [http://lookingdreamer.gitee.io/pack/doc/doc.html](http://lookingdreamer.gitee.io/pack/doc/doc.html)

## 功能说明
- 执行本地命令
- 获取svn提交历史
- checkout代码
- mvn全量编译
- 根据本地java文件编译
- 根据svn版本号增量编译打包
- 更新svn文件并返回文件md5值

## Java compile实现逻辑
### 目标
> 根据svn的提交历史,自动编译相关代码。覆盖: 增量编译和全量编译。（后续添加git支持）

### 增量编译实现
1. 通过java svnkit登陆svn服务
2. 获取svn的提交历史
3. 选中一条或者多条提交commit去编译相关代码

`由于实时从svnkit获取svn提交记录非常慢(根数据量以及网络相关),建议将添加定时任务定期将svn提交记录保存到数据库,从数据库获取提交记录`


4. 根据revision去获取提交文件,如果涉及到.java文件去通过ToolProvider去编译打包
5. 非.java文件根据标准的目录结构直接打包到一个增量包
6. 增量编译完成

### 全量编译实现
1. java cmd直接调用mvn进行全量打包
2. 涉及命令:
```bash
mvn clean
mvn compile
mvn clean package -Dmaven.test.skip=true
```

## 开发计划(TODO)
* 添加git支持
