#新增加
INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`)
VALUES(  'a59',    '0',    NOW(),    NULL,    '影像类型',    'carfrontleftimage',    'insuranceimage',    '59',    '车辆正左面照',    '59',    NULL,    NULL,    NULL,    NULL  ) ;
INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`)
VALUES(  'a60',    '0',    NOW(),    NULL,    '影像类型',    'carfrontrightimage',    'insuranceimage',    '60',    '车辆正右面照',    '60',    NULL,    NULL,    NULL,    NULL  ) ;
INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`)
VALUES(  'a61',    '0',    NOW(),    NULL,    '影像类型',    'checkcarticket',    'insuranceimage',    '61',    '验车单',    '61',    NULL,    NULL,    NULL,    NULL  ) ;
#修改排序
UPDATE insccode SET codeorder=1 WHERE parentcode='insuranceimage' AND codename='行驶证正页照';
UPDATE insccode SET codeorder=2 WHERE parentcode='insuranceimage' AND codename='行驶证副页照';
UPDATE insccode SET codeorder=3 WHERE parentcode='insuranceimage' AND codename='行驶证年审页照';
UPDATE insccode SET codeorder=4 WHERE parentcode='insuranceimage' AND codename='被保人身份证正面照';
UPDATE insccode SET codeorder=5 WHERE parentcode='insuranceimage' AND codename='被保人身份证反面照';
UPDATE insccode SET codeorder=6 WHERE parentcode='insuranceimage' AND codename='被保人组织机构代码证照';
UPDATE insccode SET codeorder=7 WHERE parentcode='insuranceimage' AND codename='被保人社会信用代码证照';
UPDATE insccode SET codeorder=8 WHERE parentcode='insuranceimage' AND codename='车辆正面照片';
UPDATE insccode SET codeorder=9 WHERE parentcode='insuranceimage' AND codename='车辆正后照片';
UPDATE insccode SET codeorder=10 WHERE parentcode='insuranceimage' AND codename='车辆前左45度照片';
UPDATE insccode SET codeorder=11 WHERE parentcode='insuranceimage' AND codename='车辆前右45度照片';
UPDATE insccode SET codeorder=12 WHERE parentcode='insuranceimage' AND codename='车辆后左45度照片';
UPDATE insccode SET codeorder=13 WHERE parentcode='insuranceimage' AND codename='车辆后右45度照片';
UPDATE insccode SET codeorder=14 WHERE parentcode='insuranceimage' AND codename='带车架号照片';
UPDATE insccode SET codeorder=15 WHERE parentcode='insuranceimage' AND codename='人车合影';
UPDATE insccode SET codeorder=16 WHERE parentcode='insuranceimage' AND codename='车辆正左面照';             #（新增）
UPDATE insccode SET codeorder=17 WHERE parentcode='insuranceimage' AND codename='车辆正右面照';             #（新增）
UPDATE insccode SET codeorder=18 WHERE parentcode='insuranceimage' AND codename='合格证';
UPDATE insccode SET codeorder=19 WHERE parentcode='insuranceimage' AND codename='新车发票照';
UPDATE insccode SET codeorder=20 WHERE parentcode='insuranceimage' AND codename='居住证';
UPDATE insccode SET codeorder=21 WHERE parentcode='insuranceimage' AND codename='本地使用证明';
UPDATE insccode SET codeorder=22 WHERE parentcode='insuranceimage' AND codename='完税证明';
UPDATE insccode SET codeorder=23 WHERE parentcode='insuranceimage' AND codename='上年商业险保单照';
UPDATE insccode SET codeorder=24 WHERE parentcode='insuranceimage' AND codename='上年交强险保单照';
UPDATE insccode SET codeorder=25 WHERE parentcode='insuranceimage' AND codename='投保人身份证正面照';
UPDATE insccode SET codeorder=26 WHERE parentcode='insuranceimage' AND codename='投保人身份证反面照';
UPDATE insccode SET codeorder=27 WHERE parentcode='insuranceimage' AND codename='投保人组织机构代码证';        #开发、测试、生产库都没有"投保人组织机构代码证照",只有'投保人组织机构代码证'
UPDATE insccode SET codeorder=28 WHERE parentcode='insuranceimage' AND codename='投保人社会信用代码证照';
UPDATE insccode SET codeorder=29 WHERE parentcode='insuranceimage' AND codename='车主身份证正面照';
UPDATE insccode SET codeorder=30 WHERE parentcode='insuranceimage' AND codename='车主身份证反面照';
UPDATE insccode SET codeorder=31 WHERE parentcode='insuranceimage' AND codename='车主组织机构代码证照';
UPDATE insccode SET codeorder=32 WHERE parentcode='insuranceimage' AND codename='车主社会信用代码证照';
UPDATE insccode SET codeorder=33 WHERE parentcode='insuranceimage' AND codename='浮动告知书盖章（单位车）';
UPDATE insccode SET codeorder=34 WHERE parentcode='insuranceimage' AND codename='投保单盖公章照';
UPDATE insccode SET codeorder=35 WHERE parentcode='insuranceimage' AND codename='二手车交易发票';
UPDATE insccode SET codeorder=36 WHERE parentcode='insuranceimage' AND codename='交强险不退保承诺书';
UPDATE insccode SET codeorder=37 WHERE parentcode='insuranceimage' AND codename='上年理赔记录';
#38 没有
UPDATE insccode SET codeorder=39 WHERE parentcode='insuranceimage' AND codename='数字一致性证书第二页';
UPDATE insccode SET codeorder=40 WHERE parentcode='insuranceimage' AND codename='验车单';             #（新增）
UPDATE insccode SET codeorder=41 WHERE parentcode='insuranceimage' AND codename='前挡风玻璃';
UPDATE insccode SET codeorder=42 WHERE parentcode='insuranceimage' AND codename='倒车镜前';
UPDATE insccode SET codeorder=43 WHERE parentcode='insuranceimage' AND codename='倒车镜后';
UPDATE insccode SET codeorder=44 WHERE parentcode='insuranceimage' AND codename='左车仪表盘内饰';
UPDATE insccode SET codeorder=45 WHERE parentcode='insuranceimage' AND codename='右车仪表盘内饰';
UPDATE insccode SET codeorder=46 WHERE parentcode='insuranceimage' AND codename='玻璃标识';
UPDATE insccode SET codeorder=47 WHERE parentcode='insuranceimage' AND codename='打着火拍仪表盘';
UPDATE insccode SET codeorder=48 WHERE parentcode='insuranceimage' AND codename='铭牌';
UPDATE insccode SET codeorder=49 WHERE parentcode='insuranceimage' AND codename='发动机舱';
UPDATE insccode SET codeorder=50 WHERE parentcode='insuranceimage' AND codename='所有大灯特写一';
UPDATE insccode SET codeorder=51 WHERE parentcode='insuranceimage' AND codename='所有大灯特写二';
UPDATE insccode SET codeorder=52 WHERE parentcode='insuranceimage' AND codename='所有大灯特写三';
UPDATE insccode SET codeorder=53 WHERE parentcode='insuranceimage' AND codename='所有大灯特写四';
UPDATE insccode SET codeorder=54 WHERE parentcode='insuranceimage' AND codename='资格证正面照';
UPDATE insccode SET codeorder=55 WHERE parentcode='insuranceimage' AND codename='代理人资格证信息页';
UPDATE insccode SET codeorder=56 WHERE parentcode='insuranceimage' AND codename='代理人身份证正面照';
UPDATE insccode SET codeorder=57 WHERE parentcode='insuranceimage' AND codename='代理人身份证背面照';
UPDATE insccode SET codeorder=58 WHERE parentcode='insuranceimage' AND codename='供应商logo';
UPDATE insccode SET codeorder=59 WHERE parentcode='insuranceimage' AND codename='本地身份证照';
UPDATE insccode SET codeorder=60 WHERE parentcode='insuranceimage' AND codename='资格证正面照';
UPDATE insccode SET codeorder=61 WHERE parentcode='insuranceimage' AND codename='银行卡正面照';
UPDATE insccode SET codeorder=62 WHERE parentcode='insuranceimage' AND codename='驾驶证副页照';
UPDATE insccode SET codeorder=63 WHERE parentcode='insuranceimage' AND codename='驾驶证正页照';