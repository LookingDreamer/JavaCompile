#����sqlǰ������insbpolicyitem������
#��������Ͷ���˹������������ݽ϶࣬���Ը���ʱ����и��£��磺2016-10-01����������
UPDATE insbpolicyitem li,(SELECT MIN(id) AS id,taskid,inscomcode FROM insbApplicantHis WHERE createtime>='2016-10-01' GROUP BY taskid,inscomcode ) iHis
   SET li.applicantid=iHis.id
 WHERE li.taskid=iHis.taskid AND li.`inscomcode`=iHis.`inscomcode`;
 
UPDATE insbpolicyitem li,(SELECT MIN(id) AS id,taskid,inscomcode FROM insbApplicantHis WHERE createtime<'2016-10-01' GROUP BY taskid,inscomcode ) iHis
   SET li.applicantid=iHis.id
 WHERE li.taskid=iHis.taskid AND li.`inscomcode`=iHis.`inscomcode`;
#�������б�Ͷ���˹������������ݽ϶࣬���Ը���ʱ����и��£��磺2016-10-01����������
UPDATE insbpolicyitem li,(SELECT MIN(id) AS id,taskid,inscomcode FROM insbInsuredHis WHERE createtime>='2016-10-01' GROUP BY taskid,inscomcode ) iHis
   SET li.`insuredid`=iHis.id
 WHERE li.taskid=iHis.taskid AND li.`inscomcode`=iHis.`inscomcode`;
 
UPDATE insbpolicyitem li,(SELECT MIN(id) AS id,taskid,inscomcode FROM insbInsuredHis WHERE createtime<'2016-10-01' GROUP BY taskid,inscomcode ) iHis
   SET li.`insuredid`=iHis.id
 WHERE li.taskid=iHis.taskid AND li.`inscomcode`=iHis.`inscomcode`;