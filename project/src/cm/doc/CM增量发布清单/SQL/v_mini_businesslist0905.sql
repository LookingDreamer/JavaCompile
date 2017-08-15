CREATE
OR REPLACE VIEW v_mini_businesslist AS SELECT
	agent.jobnum,
	task.taskid,
	IFNULL(agent.`name`, agent.nickname) AS `name`,
	dept.shortname,
	carinfo.carlicenseno,
	IFNULL(
		(
			SELECT
				policy.insuredname
			FROM
				insbpolicyitem AS policy
			WHERE
				policy.taskid = task.taskid
			AND policy.orderid = od.id
			LIMIT 1
		),
		carinfo.ownername
	) AS insuredname,
	od.modifytime AS insuredate,
	IFNULL((select prvshotname from insbprovider where id = od.prvid limit 1 ),agreement.agreementname) as agreementname,
	IFNULL(
		(
			SELECT
				SUM(IFNULL(policy.premium, 0))
			FROM
				insbpolicyitem AS policy
			WHERE
				policy.taskid = task.taskid
			AND policy.orderid = od.id
		),
		0
	) AS totalinsuredamount,
		(
			SELECT
				concat('±£µ¥ºÅ£º', policyno) AS policyno
			FROM
				insbpolicyitem AS policy
			WHERE
				policy.taskid = task.taskid
			AND policy.orderid = od.id
			AND policy.risktype = '0'
		)  AS commercial_policyno,
	IFNULL(
		(
			SELECT
				policy.premium
			FROM
				insbpolicyitem AS policy
			WHERE
				policy.taskid = task.taskid
			AND policy.orderid = od.id
			AND policy.risktype = '0'
		),
		0
	) AS commercial_num,
	concat(
		cast(
			IFNULL(commercial.rate * 100, 0) AS UNSIGNED INT
		),
		'%'
	) AS commercial_rate,
	IFNULL(commercial.counts, 0) AS commercial_counts,
	(
		SELECT
			concat('±£µ¥ºÅ£º', policyno) AS policyno
		FROM
			insbpolicyitem AS policy
		WHERE
			policy.taskid = task.taskid
		AND policy.orderid = od.id
		AND policy.risktype = '1'
	) AS compulsory_policyno,
	IFNULL(
		(
			SELECT
				policy.premium
			FROM
				insbpolicyitem AS policy
			WHERE
				policy.taskid = task.taskid
			AND policy.orderid = od.id
			AND policy.risktype = '1'
		),
		0
	) AS compulsory_num,
	concat(
		cast(
			IFNULL(compulsory.rate * 100, 0) AS UNSIGNED INT
		),
		'%'
	) AS compulsory_rate,
	IFNULL(compulsory.counts, 0) AS compulsory_counts,
	(
		IFNULL(commercial.counts, 0) + IFNULL(compulsory.counts, 0)
	) AS commission_total,
        IFNULL(
		(
			SELECT
				detail2.amount
			FROM
				insbaccountdetails AS detail2
			WHERE
				detail2.accountid = account.id
			AND detail2.fundsource = '102'
			AND locate(task.taskid, detail2.noti) > 0
		),
		0
	) AS redpacket,
        IFNULL(
		(
			SELECT
				detail2.amount
			FROM
				insbaccountdetails AS detail2
			WHERE
				detail2.accountid = account.id
			AND detail2.fundsource = '103'
			AND locate(task.taskid, detail2.noti) > 0
		),
		0
	) AS cashprize,
	referrer.jobnum AS refjobnum,
	IFNULL(
		referrer. NAME,
		referrer.nickname
	) AS refname,
	'3%' AS reward_rate,
	IF(IFNULL(referrer.jobnum,'NULL')='NULL',0,IFNULL(reward.counts, 0)) AS reward_counts,
	(
		IFNULL(commercial.counts, 0) + IF(IFNULL(referrer.jobnum,'NULL')='NULL',0,IFNULL(reward.counts, 0)) + IFNULL(compulsory.counts, 0) + IFNULL(
			(
				SELECT
					detail2.amount
				FROM
					insbaccountdetails AS detail2
				WHERE
					detail2.accountid = account.id
				AND detail2.fundsource = '102'
				AND locate(task.taskid, detail2.noti) > 0
			),
			0
		)+IFNULL(
		(
			SELECT
				detail2.amount
			FROM
				insbaccountdetails AS detail2
			WHERE
				detail2.accountid = account.id
			AND detail2.fundsource = '103'
			AND locate(task.taskid, detail2.noti) > 0
		),
		0
	        )
	) AS total_amount
FROM
	insbagenttask AS task
LEFT JOIN insbagent AS agent ON agent.id = task.agentid
LEFT JOIN insbagent AS referrer ON agent.referrerid = referrer.id
LEFT JOIN insborder AS od ON task.taskid = od.taskid
LEFT JOIN insbcarinfo AS carinfo ON od.taskid = carinfo.taskid
LEFT JOIN insbquotetotalinfo t ON t.taskid = task.taskid  
LEFT JOIN insbquoteinfo q ON q.quotetotalinfoid=t.id and q.inscomcode=od.prvid
LEFT JOIN insbagreement AS agreement ON agreement.id = q.agreementid
LEFT JOIN inscdept AS dept ON agreement.deptid = dept.id
LEFT JOIN insbaccount AS account ON account.agentid = agent.id
LEFT JOIN insbaccountdetails AS detail ON detail.accountid = account.id
AND locate(task.taskid, detail.noti) > 0
AND detail.fundsource = '101'
LEFT JOIN v_mini_commission AS commercial ON commercial.taskid = task.taskid
AND commercial.commissiontype = '01'
AND commercial.agreementid = agreement.id
LEFT JOIN v_mini_commission AS compulsory ON compulsory.taskid = task.taskid
AND compulsory.commissiontype = '03'
AND compulsory.agreementid = agreement.id
LEFT JOIN v_mini_commission AS reward ON reward.taskid = task.taskid
AND reward.commissiontype = '02'
AND reward.agreementid = agreement.id
WHERE
 carinfo.ownername NOT LIKE '%²âÊÔ%'
AND IFNULL(agent.name,'NULL') not like '%²âÊÔ%'
AND IFNULL(agent.nickname,'NULL') not like '%²âÊÔ%'
AND task.`status` = '1'
AND agent.usersource='minizzb'
AND od.modifytime > '20160720'
ORDER BY
	od.modifytime DESC