<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<link href="../static/css/lib/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="../static/css/lib/bootstrap-datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
<script data-ver="${jsver}" data-main="../static/js/load"
	src="../static/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "zzbconf/demo" ]);
</script>
</head>
<body>
demo 测试

<div class="container">
    <form action="" class="form-horizontal"  role="form">
   <#list data as demo>
    ${demo.id}====${demo.name}
    </#list>
    </form>
</div>
</body>
</html>
