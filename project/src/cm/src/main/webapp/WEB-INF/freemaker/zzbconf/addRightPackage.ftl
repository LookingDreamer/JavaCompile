<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<style type="text/css"> 
        tr{height:40px;}
		td{vertical-align: left;}
</style>
<script type="text/javascript">
	requirejs([ "zzbconf/agent" ]);
</script>
<body>
              <div class="modal-body" style="overflow: auto; height: 400px;">
                    <div class="container-fluid">
	                <div class="row">
		               <h2>批量添加权限包</h2>
		           	</div> 
                    <div class="row">
						<label  for="funcs" >第一步:选择需要关联的权限包</label>
                        <select   id="funcs" name="funcs" class="form-control" style="width:220px;">
                        	<#list result as re>
								<option value="${re.id}">${re.setname}</option>
							</#list>
                        </select>
					</div>
					<div class="row">
						<label  for="funcs2" >第二步:输入需要被关联的工号或手机号(支持同时添加多个,每行一个工号或手机号)</label>
						<textarea id="funcsDeptid" name="funcsDeptid" style="resize:none;width:560px;height:190px;"> </textarea>
                    </div>
                  </div>
              </div>
              <div class="modal-footer">
                    <button class="btn btn-primary" type="button" id="funcsOK" name="funcsOK">确定</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
              </div>
              
              <div id="showdeptpic1" class="modal fade" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-body" style="overflow: auto; height: 200px;">
	                <div class="container-fluid">
	                   <h1>全部账号关联成功</h1>
	                </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
    
    <div id="showdeptpic2" class="modal fade" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-body" style="overflow: auto; height: 200px;">
                    <div class="container-fluid">
                        <h1>权限包关联失败</h1>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-primary" type="button" id="funcsOK1">导出关联结果</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
              
</body>
</html>