<!DOCTYPE html>
<html lang ="en" class="fuelux" >  
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机构管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/system/flat-ui-icon.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<style type="text/css">
		.htmleaf-header{margin-bottom: 15px;font-family: "Segoe UI", "Lucida Grande", Helvetica, Arial, "Microsoft YaHei", FreeSans, Arimo, "Droid Sans", "wenquanyi micro hei", "Hiragino Sans GB", "Hiragino Sans GB W3", "FontAwesome", sans-serif;}
		.htmleaf-icon{color: #fff;}
	</style>
<script type="text/javascript">
	requirejs([ "system/regionMng" ]);
</script>
</head>
<body>

<div class="col-md-4">
 <div class="panel panel-default m-bottom-5">
 <div class="panel-heading padding-5-5">地区管理</div>
  <div class="panel-body">

	<div class="ztree" id="deptTree" style="width:100%; height:400px; overflow-y:auto;overflow-x:auto;"></div>
	
 </div>


 </div>
</div>
<div class="col-md-8">
	<div class="panel panel-default m-bottom-5">
   	   <div class="panel-heading padding-5-5">默认注册网点</div>
   	     <div class="panel-body">
	 	   <form  id = "orgsaveform" action="update" method="post">
			   <input type="hidden" id="id" name="id">
			 <div class="alert alert-danger" role="alert" id="showerrormessage" style="display: none;"></div>
               <label id="setDistrict">设置地区：</label>
	         <table class="table table-bordered " >
                 <tr >
                     <td width="130px">
                         <label for="exampleInputName">是否开放注册</label>
                     </td>
                     <td >
                         <select name="register" class="form-control" id ="register">
                             <option value=1>是</option>
                             <option value=0>否</option>
                         </select>
                     </td>
                 </tr>
		      <tr>
				<td ><label for="exampleInputName">默认出单网点</label></td>
			  	<td>
                    <div class="input-group">
                    <input type="hidden" id="deptid" name="deptid" value=""/>
                    <input type="text"	class="form-control"  id="shortname"  value="" placeholder="请选择"/>
                    <span class="input-group-btn">
                         <button class="btn btn-default" id="choose" type="button">选择</button>
                     </span>
                    </div>
				</td>
			  </tr>
			  <tr>
			  	<td><input class="btn btn-primary"  id="savebutton" type="button" value = "保存"></td>
			  </tr>
	         </table>
	         </form>
	         
        </div>
      </div>
     </div>

<div id="showDeptTree" class="modal fade"  role="dialog" aria-labelledby="myModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close closeShowDeptTree" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="gridSystemModalLabel">选择默认注册网点</h4>
            </div>
            <div class="modal-body" style="overflow:auto; height:400px;">
                <div class="container-fluid">
                    <div class="row">
                        <ul id="deptTreeDemo" class="ztree"></ul>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default closeShowDeptTree">关闭</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
