######### ��עӰ�񡢹������� ###########

 DROP TABLE IF EXISTS INSBUsercommentUploadFile;  
CREATE TABLE INSBUsercommentUploadFile (
	id VARCHAR(32) NOT NULL PRIMARY KEY COMMENT "����",
	operator VARCHAR(60) COMMENT "������",
	createtime DATETIME COMMENT "����ʱ��",
	modifytime DATETIME COMMENT "�޸�ʱ��",
	noti VARCHAR(300) COMMENT "��ע������Ϣ",
	usercommentid VARCHAR(32) COMMENT "��עid",
	codetype VARCHAR(50) COMMENT "�ϴ��ļ�����"
)COMMENT="��ע�û��ϴ�Ӱ����Ϣ��"; 

SELECT * FROM INSBUsercommentUploadFile;

ALTER TABLE INSBFilebusiness ADD INDEX ix_code(CODE(7));

ALTER TABLE insbperson ADD INDEX idx_insbperson_name(NAME(50));


######### ����Ӱ������ #######################
INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`) 
VALUES(  'a48',    '0',    NOW(),    NULL,    'Ӱ������',    'temporaryresidentialpermit',    'insuranceimage',    '48',    '��ס֤',    '48',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`) 
VALUES(  'a49',    '0',    NOW(),    NULL,    'Ӱ������',    'floatingtoldbookseal',    'insuranceimage',    '49',    '������֪����£���λ����',    '49',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`) 
VALUES(  'a50',    '0',    NOW(),    NULL,    'Ӱ������',    'insurancepublicimage',    'insuranceimage',    '50',    'Ͷ�����ǹ�����',    '50',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`) 
VALUES(  'a51',    '0',    NOW(),    NULL,    'Ӱ������',    'usedcartradeinvoices',    'insuranceimage',    '51',    '���ֳ����׷�Ʊ',    '51',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`) 
VALUES(  'a52',    '0',    NOW(),    NULL,    'Ӱ������',    'insurancenotsurepledge',    'insuranceimage',    '52',    '��ǿ�ղ��˱���ŵ��',    '52',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`) 
VALUES(  'a53',    '0',    NOW(),    NULL,    'Ӱ������',    'claimsrecordslisting',    'insuranceimage',    '53',    '�����¼�嵥',    '53',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`) 
VALUES(  'a54',    '0',    NOW(),    NULL,    'Ӱ������',    'localusecertificate',    'insuranceimage',    '54',    '����ʹ��֤��',    '54',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`) 
VALUES(  'a55',    '0',    NOW(),    NULL,    'Ӱ������',    'finishedtallagecertificate',    'insuranceimage',    '55',    '��˰֤��',    '55',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`)
VALUES(  'a56',    '0',    NOW(),    NULL,    'Ӱ������',    'insurecardcertificateimage',    'insuranceimage',    '56',    '������������ô���֤��',    '56',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`)
VALUES(  'a57',    '0',    NOW(),    NULL,    'Ӱ������',    'policycardcertificateimage',    'insuranceimage',    '57',    'Ͷ����������ô���֤��',    '57',    NULL,    NULL,    NULL,    NULL  ) ;

INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`)
VALUES(  'a58',    '0',    NOW(),    NULL,    'Ӱ������',    'carownercardcertificateimage',    'insuranceimage',    '58',    '����������ô���֤��',    '58',    NULL,    NULL,    NULL,    NULL  ) ;
###### �޸�name:
UPDATE insccode SET codename='��ס֤' WHERE parentcode='insuranceimage' AND codename='��ס֤';
UPDATE insccode SET codename='�³���Ʊ��' WHERE parentcode='insuranceimage' AND codename='��Ʊ������';
UPDATE insccode SET codename='�ϸ�֤' WHERE parentcode='insuranceimage' AND codename='�ϸ�֤������';
UPDATE insccode SET codename='���������¼' WHERE parentcode='insuranceimage' AND codename='�����¼�嵥';
UPDATE insccode SET codename='�ϸ�֤' WHERE parentcode='insuranceimage' AND codename like '%�ϸ�֤����%';

COMMIT;