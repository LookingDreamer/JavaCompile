#������
INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`)
VALUES(  'a59',    '0',    NOW(),    NULL,    'Ӱ������',    'carfrontleftimage',    'insuranceimage',    '59',    '������������',    '59',    NULL,    NULL,    NULL,    NULL  ) ;
INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`)
VALUES(  'a60',    '0',    NOW(),    NULL,    'Ӱ������',    'carfrontrightimage',    'insuranceimage',    '60',    '������������',    '60',    NULL,    NULL,    NULL,    NULL  ) ;
INSERT INTO `insccode` (  `id`,  `operator`,  `createtime`,  `modifytime`,  `noti`,  `codetype`,  `parentcode`,  `codevalue`,  `codename`,  `codeorder`,  `prop1`,  `prop2`,  `prop3`,  `prop4`)
VALUES(  'a61',    '0',    NOW(),    NULL,    'Ӱ������',    'checkcarticket',    'insuranceimage',    '61',    '�鳵��',    '61',    NULL,    NULL,    NULL,    NULL  ) ;
#�޸�����
UPDATE insccode SET codeorder=1 WHERE parentcode='insuranceimage' AND codename='��ʻ֤��ҳ��';
UPDATE insccode SET codeorder=2 WHERE parentcode='insuranceimage' AND codename='��ʻ֤��ҳ��';
UPDATE insccode SET codeorder=3 WHERE parentcode='insuranceimage' AND codename='��ʻ֤����ҳ��';
UPDATE insccode SET codeorder=4 WHERE parentcode='insuranceimage' AND codename='���������֤������';
UPDATE insccode SET codeorder=5 WHERE parentcode='insuranceimage' AND codename='���������֤������';
UPDATE insccode SET codeorder=6 WHERE parentcode='insuranceimage' AND codename='��������֯��������֤��';
UPDATE insccode SET codeorder=7 WHERE parentcode='insuranceimage' AND codename='������������ô���֤��';
UPDATE insccode SET codeorder=8 WHERE parentcode='insuranceimage' AND codename='����������Ƭ';
UPDATE insccode SET codeorder=9 WHERE parentcode='insuranceimage' AND codename='����������Ƭ';
UPDATE insccode SET codeorder=10 WHERE parentcode='insuranceimage' AND codename='����ǰ��45����Ƭ';
UPDATE insccode SET codeorder=11 WHERE parentcode='insuranceimage' AND codename='����ǰ��45����Ƭ';
UPDATE insccode SET codeorder=12 WHERE parentcode='insuranceimage' AND codename='��������45����Ƭ';
UPDATE insccode SET codeorder=13 WHERE parentcode='insuranceimage' AND codename='��������45����Ƭ';
UPDATE insccode SET codeorder=14 WHERE parentcode='insuranceimage' AND codename='�����ܺ���Ƭ';
UPDATE insccode SET codeorder=15 WHERE parentcode='insuranceimage' AND codename='�˳���Ӱ';
UPDATE insccode SET codeorder=16 WHERE parentcode='insuranceimage' AND codename='������������';             #��������
UPDATE insccode SET codeorder=17 WHERE parentcode='insuranceimage' AND codename='������������';             #��������
UPDATE insccode SET codeorder=18 WHERE parentcode='insuranceimage' AND codename='�ϸ�֤';
UPDATE insccode SET codeorder=19 WHERE parentcode='insuranceimage' AND codename='�³���Ʊ��';
UPDATE insccode SET codeorder=20 WHERE parentcode='insuranceimage' AND codename='��ס֤';
UPDATE insccode SET codeorder=21 WHERE parentcode='insuranceimage' AND codename='����ʹ��֤��';
UPDATE insccode SET codeorder=22 WHERE parentcode='insuranceimage' AND codename='��˰֤��';
UPDATE insccode SET codeorder=23 WHERE parentcode='insuranceimage' AND codename='������ҵ�ձ�����';
UPDATE insccode SET codeorder=24 WHERE parentcode='insuranceimage' AND codename='���꽻ǿ�ձ�����';
UPDATE insccode SET codeorder=25 WHERE parentcode='insuranceimage' AND codename='Ͷ�������֤������';
UPDATE insccode SET codeorder=26 WHERE parentcode='insuranceimage' AND codename='Ͷ�������֤������';
UPDATE insccode SET codeorder=27 WHERE parentcode='insuranceimage' AND codename='Ͷ������֯��������֤';        #���������ԡ������ⶼû��"Ͷ������֯��������֤��",ֻ��'Ͷ������֯��������֤'
UPDATE insccode SET codeorder=28 WHERE parentcode='insuranceimage' AND codename='Ͷ����������ô���֤��';
UPDATE insccode SET codeorder=29 WHERE parentcode='insuranceimage' AND codename='�������֤������';
UPDATE insccode SET codeorder=30 WHERE parentcode='insuranceimage' AND codename='�������֤������';
UPDATE insccode SET codeorder=31 WHERE parentcode='insuranceimage' AND codename='������֯��������֤��';
UPDATE insccode SET codeorder=32 WHERE parentcode='insuranceimage' AND codename='����������ô���֤��';
UPDATE insccode SET codeorder=33 WHERE parentcode='insuranceimage' AND codename='������֪����£���λ����';
UPDATE insccode SET codeorder=34 WHERE parentcode='insuranceimage' AND codename='Ͷ�����ǹ�����';
UPDATE insccode SET codeorder=35 WHERE parentcode='insuranceimage' AND codename='���ֳ����׷�Ʊ';
UPDATE insccode SET codeorder=36 WHERE parentcode='insuranceimage' AND codename='��ǿ�ղ��˱���ŵ��';
UPDATE insccode SET codeorder=37 WHERE parentcode='insuranceimage' AND codename='���������¼';
#38 û��
UPDATE insccode SET codeorder=39 WHERE parentcode='insuranceimage' AND codename='����һ����֤��ڶ�ҳ';
UPDATE insccode SET codeorder=40 WHERE parentcode='insuranceimage' AND codename='�鳵��';             #��������
UPDATE insccode SET codeorder=41 WHERE parentcode='insuranceimage' AND codename='ǰ���粣��';
UPDATE insccode SET codeorder=42 WHERE parentcode='insuranceimage' AND codename='������ǰ';
UPDATE insccode SET codeorder=43 WHERE parentcode='insuranceimage' AND codename='��������';
UPDATE insccode SET codeorder=44 WHERE parentcode='insuranceimage' AND codename='���Ǳ�������';
UPDATE insccode SET codeorder=45 WHERE parentcode='insuranceimage' AND codename='�ҳ��Ǳ�������';
UPDATE insccode SET codeorder=46 WHERE parentcode='insuranceimage' AND codename='������ʶ';
UPDATE insccode SET codeorder=47 WHERE parentcode='insuranceimage' AND codename='���Ż����Ǳ���';
UPDATE insccode SET codeorder=48 WHERE parentcode='insuranceimage' AND codename='����';
UPDATE insccode SET codeorder=49 WHERE parentcode='insuranceimage' AND codename='��������';
UPDATE insccode SET codeorder=50 WHERE parentcode='insuranceimage' AND codename='���д����дһ';
UPDATE insccode SET codeorder=51 WHERE parentcode='insuranceimage' AND codename='���д����д��';
UPDATE insccode SET codeorder=52 WHERE parentcode='insuranceimage' AND codename='���д����д��';
UPDATE insccode SET codeorder=53 WHERE parentcode='insuranceimage' AND codename='���д����д��';
UPDATE insccode SET codeorder=54 WHERE parentcode='insuranceimage' AND codename='�ʸ�֤������';
UPDATE insccode SET codeorder=55 WHERE parentcode='insuranceimage' AND codename='�������ʸ�֤��Ϣҳ';
UPDATE insccode SET codeorder=56 WHERE parentcode='insuranceimage' AND codename='���������֤������';
UPDATE insccode SET codeorder=57 WHERE parentcode='insuranceimage' AND codename='���������֤������';
UPDATE insccode SET codeorder=58 WHERE parentcode='insuranceimage' AND codename='��Ӧ��logo';
UPDATE insccode SET codeorder=59 WHERE parentcode='insuranceimage' AND codename='�������֤��';
UPDATE insccode SET codeorder=60 WHERE parentcode='insuranceimage' AND codename='�ʸ�֤������';
UPDATE insccode SET codeorder=61 WHERE parentcode='insuranceimage' AND codename='���п�������';
UPDATE insccode SET codeorder=62 WHERE parentcode='insuranceimage' AND codename='��ʻ֤��ҳ��';
UPDATE insccode SET codeorder=63 WHERE parentcode='insuranceimage' AND codename='��ʻ֤��ҳ��';