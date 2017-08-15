insert into insbPermissionallot(id,setid,permissionid,permissionname,functionstate,createtime,operator)
select CONCAT('xyqaddallot',@rownum:=@rownum+1) AS id,s.id as setid,p.id,p.permissionname,'0',now(),'xyq'
from insbpermissionset s,insbpermission p,(SELECT @rownum:=0) c
where not exists(
select 1 from insbPermissionallot a where a.setid=s.id and a.permissionid=p.id
)
order by s.id,p.id;