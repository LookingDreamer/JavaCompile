######### 备注影像、规则任务： ###########

 DROP TABLE IF EXISTS INSBUsercommentUploadFile;  
CREATE TABLE INSBUsercommentUploadFile (
	id VARCHAR(32) NOT NULL PRIMARY KEY COMMENT "主键",
	operator VARCHAR(60) COMMENT "操作者",
	createtime DATETIME COMMENT "创建时间",
	modifytime DATETIME COMMENT "修改时间",
	noti VARCHAR(300) COMMENT "备注内容信息",
	usercommentid VARCHAR(32) COMMENT "备注id",
	codetype VARCHAR(50) COMMENT "上传文件类型"
)COMMENT="备注用户上传影像信息表"; 

SELECT * FROM INSBUsercommentUploadFile;

ALTER TABLE INSBFilebusiness ADD INDEX ix_code(CODE(7));

ALTER TABLE insbperson ADD INDEX idx_insbperson_name(NAME(50));


######### 增加影像类型 #######################
INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`) 
VALUES(  'a48',    '0',    NOW(),    NULL,    '影像类型',    'temporaryresidentialpermit',    'insuranceimage',    '48',    '暂住证',    '48',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`) 
VALUES(  'a49',    '0',    NOW(),    NULL,    '影像类型',    'floatingtoldbookseal',    'insuranceimage',    '49',    '浮动告知书盖章（单位车）',    '49',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`) 
VALUES(  'a50',    '0',    NOW(),    NULL,    '影像类型',    'insurancepublicimage',    'insuranceimage',    '50',    '投保单盖公章照',    '50',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`) 
VALUES(  'a51',    '0',    NOW(),    NULL,    '影像类型',    'usedcartradeinvoices',    'insuranceimage',    '51',    '二手车交易发票',    '51',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`) 
VALUES(  'a52',    '0',    NOW(),    NULL,    '影像类型',    'insurancenotsurepledge',    'insuranceimage',    '52',    '交强险不退保承诺书',    '52',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`) 
VALUES(  'a53',    '0',    NOW(),    NULL,    '影像类型',    'claimsrecordslisting',    'insuranceimage',    '53',    '理赔记录清单',    '53',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`) 
VALUES(  'a54',    '0',    NOW(),    NULL,    '影像类型',    'localusecertificate',    'insuranceimage',    '54',    '本地使用证明',    '54',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`) 
VALUES(  'a55',    '0',    NOW(),    NULL,    '影像类型',    'finishedtallagecertificate',    'insuranceimage',    '55',    '完税证明',    '55',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`)
VALUES(  'a56',    '0',    NOW(),    NULL,    '影像类型',    'insurecardcertificateimage',    'insuranceimage',    '56',    '被保人社会信用代码证照',    '56',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`)
VALUES(  'a57',    '0',    NOW(),    NULL,    '影像类型',    'policycardcertificateimage',    'insuranceimage',    '57',    '投保人社会信用代码证照',    '57',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`)
VALUES(  'a58',    '0',    NOW(),    NULL,    '影像类型',    'carownercardcertificateimage',    'insuranceimage',    '58',    '车主社会信用代码证照',    '58',    NULL,    NULL,    NULL,    NULL  ) ;
###### 修改name:
UPDATE insccode SET codename='居住证' WHERE parentcode='insuranceimage' AND codename='暂住证';
UPDATE insccode SET codename='新车发票照' WHERE parentcode='insuranceimage' AND codename='发票正面照';
UPDATE insccode SET codename='合格证' WHERE parentcode='insuranceimage' AND codename='合格证正面照';
UPDATE insccode SET codename='上年理赔记录' WHERE parentcode='insuranceimage' AND codename='理赔记录清单';
UPDATE insccode SET codename='合格证' WHERE parentcode='insuranceimage' AND codename like '%合格证正面%';

COMMIT;