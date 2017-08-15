<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>用户管理</title>
<script type="text/javascript">
		requirejs([ "system/usermessagedetail" ]);
</script>
<style> 
	body{font-size:14px;}
	.div-height{height:300px;} 
	#table-javascript{  
    width:610px;  
    table-layout:fixed;
	}  
	#table-javascript td:nth-child(3){  
	    width:100%;  
	    word-break:keep-all; 
	    white-space:nowrap;  
	    overflow:hidden; 
	    text-overflow:ellipsis;
	}  
</style>
<!--
--> 
</head>
<body>
	<div class="modal-header">
       <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
       <span class="modal-title" id="myModalLabel">我的消息</span>
	</div>
	<div class="modal-body">
		<div class="div-height">        
			 <div class="panel panel-default">
    <div class="panel-heading" role="tab" id="headingTask">
      <h4 class="panel-title">
        	消息详情
       
      </h4>
    </div>
    <!--<form id="form">
    	<input type="file" id="file"/>
    	<input type="text" id="jobNum" value="8899"/>
    	<input type="text" id="fileName" value="截图.png"/>
    	<input type="text" id="fileType" value="insuredidcardopposite"/>
    	<input type="text" id="fileDescribes" value="投保人身份证正面照"/>
    	<input type="button" onclick="javascript:reloaddata()" value="提交"/>
    </form>-->
    <div id="collapseTask" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingTask">
      <div class="panel-body">
        <div class="row">
        <input type="hidden" value="${messageDetail.receiver}" id="receiver" >
			<div class="col-md-12">
				<table class="table table-bordered">
						<tr>
							<td class="col-md-2 active">标题</td>
							<td class="col-md-2" colspan="3">${messageDetail.msgtitle}</td>
						</tr>
						<tr>
							<td class="col-md-2 active">发送人</td>
							<td class="col-md-2">${messageDetail.sender}</td>
							<td class="col-md-2 active">发送时间</td>
							<td class="col-md-2">${messageDetail.sendtime}</td>
						</tr>
						<tr>
							<td class="col-md-2 active" colspan="4">内容</td>
						</tr>
						<tr>
							<td class="col-md-2" colspan="4">${messageDetail.msgcontent}</td>
						</tr>
	 			</table>
			</div>
		</div>
		</div>
    </div>
  </div>
		</div>
		<div class="modal-footer">
		  <button type="button" class="btn btn-primary" onclick="openDialog('user/message/${messageDetail.receiver}');">返回</button>
		  <button type="button" class="btn btn-default" data-dismiss="modal" onclick="messageNotReadCount()" >关闭</button>
		</div>
	</div>
</body>
</html>
