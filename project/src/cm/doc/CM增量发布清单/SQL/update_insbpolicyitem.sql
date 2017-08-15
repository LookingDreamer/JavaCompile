#操作sql前，备份insbpolicyitem表数据
#更新所有投保人关联，由于数据较多，可以根据时间进行更新，如：2016-10-01做条件更新
UPDATE insbpolicyitem li,(SELECT MIN(id) AS id,taskid,inscomcode FROM insbApplicantHis WHERE createtime>='2016-10-01' GROUP BY taskid,inscomcode ) iHis
   SET li.applicantid=iHis.id
 WHERE li.taskid=iHis.taskid AND li.`inscomcode`=iHis.`inscomcode`;
 
UPDATE insbpolicyitem li,(SELECT MIN(id) AS id,taskid,inscomcode FROM insbApplicantHis WHERE createtime<'2016-10-01' GROUP BY taskid,inscomcode ) iHis
   SET li.applicantid=iHis.id
 WHERE li.taskid=iHis.taskid AND li.`inscomcode`=iHis.`inscomcode`;
#更新所有被投保人关联，由于数据较多，可以根据时间进行更新，如：2016-10-01做条件更新
UPDATE insbpolicyitem li,(SELECT MIN(id) AS id,taskid,inscomcode FROM insbInsuredHis WHERE createtime>='2016-10-01' GROUP BY taskid,inscomcode ) iHis
   SET li.`insuredid`=iHis.id
 WHERE li.taskid=iHis.taskid AND li.`inscomcode`=iHis.`inscomcode`;
 
UPDATE insbpolicyitem li,(SELECT MIN(id) AS id,taskid,inscomcode FROM insbInsuredHis WHERE createtime<'2016-10-01' GROUP BY taskid,inscomcode ) iHis
   SET li.`insuredid`=iHis.id
 WHERE li.taskid=iHis.taskid AND li.`inscomcode`=iHis.`inscomcode`;