CREATE
OR REPLACE VIEW v_mini_uncompbusiness AS SELECT
	agent.jobnum,
	task.taskid,
        task.`status`,
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
	odpayment.paytime AS insuredate,
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
				concat('保单号：', policyno) AS policyno
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
			concat('保单号：', policyno) AS policyno
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
	referrer.jobnum AS refjobnum,
	IFNULL(
		referrer. NAME,
		referrer.nickname
	) AS refname,
	'3%' AS reward_rate,
	IF(IFNULL(referrer.jobnum,'NULL')='NULL',0,IFNULL(reward.counts, 0)) AS reward_counts
FROM
	insbagenttask AS task
LEFT JOIN insbagent AS agent ON agent.id = task.agentid
LEFT JOIN insbagent AS referrer ON agent.referrerid = referrer.id
LEFT JOIN insborder AS od ON task.taskid = od.taskid
LEFT JOIN insborderpayment AS odpayment ON  odpayment.taskid=od.taskid and odpayment.payresult='02'
LEFT JOIN insbcarinfo AS carinfo ON od.taskid = carinfo.taskid
LEFT JOIN insbquotetotalinfo t ON t.taskid = task.taskid  
LEFT JOIN insbquoteinfo q ON q.quotetotalinfoid=t.id and q.inscomcode=od.prvid
LEFT JOIN insbagreement AS agreement ON agreement.id = q.agreementid
LEFT JOIN inscdept AS dept ON agreement.deptid = dept.id
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
  agent.usersource='minizzb'
AND odpayment.paytime > '20160720'
ORDER BY
	odpayment.paytime asc