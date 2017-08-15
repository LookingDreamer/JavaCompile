DELETE  FROM insbrealtimetask;
SELECT * FROM insbrealtimetask;
INSERT INTO insbrealtimetask
SELECT CONCAT('xxx201612-',@rownum:=@rownum+1) AS id,'admin',NOW(),NOW(),
CASE WHEN ws.taskcode = '33' THEN zds2.codevalue
			ELSE zds.codevalue
			END AS tasktype,
			CASE WHEN ws.taskcode = '33' THEN zds2.codename
			ELSE zds.codename
			END AS tasktypename,
			carh.carlicenseno AS carlicenseno,inhp.id,inhp.name AS insuredname,inhp.idcardno,'','','',
			ag.jobnum,ag.name,
			
		 sdept.comcode AS deptcode,
		 sdept.comname AS deptname,sdept.deptinnercode,
		 pro.prvcode,pro.prvshotname AS inscomName,
		 ws.maininstanceId AS maininstanceId,
		CASE WHEN ws.taskcode = '33' THEN ws.maininstanceid  ELSE ws.instanceId END AS subInstanceId,
		CASE WHEN ws.taskcode = '33' THEN wfm.operator
			ELSE ws.operator END  AS operatorcode,
		CASE WHEN ws.taskcode = '33' THEN  inu2.NAME
			ELSE inu.NAME END AS operatorname,
			DATE_FORMAT(wst.createtime, '%Y-%m-%d %H:%i:%S')AS taskcreatetime,
			qt.datasourcesfrom,l.`channelinnercode`, l.`channelname`
		 
		FROM insbworkflowsub ws
		LEFT JOIN insbquoteinfo q ON ws.instanceid = q.workflowinstanceid
		
		LEFT JOIN insbcarinfohis carh ON carh.taskid = ws.maininstanceid
		AND carh.inscomcode = q.inscomcode
		
		LEFT JOIN insbinsuredhis insdh ON insdh.taskid = ws.maininstanceid
		AND q.inscomcode = insdh.inscomcode
		LEFT JOIN insbperson inhp ON inhp.id = insdh.personid
		LEFT JOIN insbworkflowmain wfm ON wfm.instanceid = ws.maininstanceid
		LEFT JOIN insccode zds ON zds.codevalue = ws.taskcode
		AND zds.codetype = 'workflowNodelName'
		AND zds.parentcode = 'workflowNodelName'
		LEFT JOIN insccode zds2 ON zds2.codevalue = wfm.taskcode
		AND zds2.codetype = 'workflowNodelName'
		AND zds2.parentcode = 'workflowNodelName'
		LEFT JOIN insccode dcode ON dcode.codename = zds.codename
		AND dcode.codetype = 'tasktype'
		AND dcode.parentcode = 'tasktype'
		LEFT JOIN insccode dcode2 ON dcode2.codename = zds2.codename
		AND dcode2.codetype = 'tasktype'
		AND dcode2.parentcode = 'tasktype'
		LEFT JOIN inscdept sdept ON sdept.comcode = q.deptcode
		INNER JOIN insbquotetotalinfo qt ON qt.taskid = ws.maininstanceid
		LEFT JOIN insbagent ag ON qt.agentnum = ag.jobnum
		LEFT JOIN insbprovider pro ON pro.prvcode = q.inscomcode
		LEFT JOIN inscuser inu ON inu.usercode = ws.operator
		LEFT JOIN inscuser inu2 ON inu2.usercode = wfm.operator
		LEFT JOIN insbworkflowsubtrack wst ON wst.maininstanceid=ws.maininstanceid AND wst.instanceid=ws.instanceid AND wst.taskcode=ws.taskcode
		AND wst.createtime=(SELECT MAX(a.createtime) FROM insbworkflowsubtrack a WHERE a.maininstanceid=ws.maininstanceid AND a.instanceid=ws.instanceid AND a.taskcode=ws.taskcode)
		LEFT JOIN insbchannel l ON qt.`purchaserchannel` = l.channelinnercode AND l.childflag='1' AND (l.deleteflag IS NULL OR l.deleteflag !='0')
		,(SELECT @rownum:=0) c
		WHERE 1=1
		AND ((ws.taskcode !='33' AND ws.taskcode != '37' AND ws.taskcode != '30') 
				  or (ws.taskcode ='33' AND wfm.taskcode != '33' AND wfm.taskcode != '37')
				)
		AND  wst.createtime >= '2016-12-01 00:00:00'
			
			 	AND wst.createtime <= '2017-01-15 23:59:59'
			 	