<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<script type="text/javascript">
		requirejs(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","jqvalidatei18n","additionalmethods","public","jqtreeview","system/usermessage"], function ($) {
			//数据初始化 
			$(function() {
				$('#table-javascript').bootstrapTable({
		            method: 'get',
		            url: pageurl,
		            cache: false,
		            striped: true,
		            pagination: true,
		            sidePagination: 'server', 
		            pageSize: pagesize,
		            minimumCountColumns: 2,
		            clickToSelect: true,
		            columns: [ {
		                field: 'id',
		                title: 'msgid',
		                visible: false,
		                switchable:false
		            }, {
		                field: 'sender',
		                title: '发送人',
		                align: 'center',
		                valign: 'middle',
		                sortable: true
		            },{
		                field: 'msgtitle',
		                title: '标题',
		                align: 'center',
		                valign: 'middle',
		                sortable: true
		            }, {
		            	field: 'msgcontent',
		                title: '内容',
		                align: 'center',
		                valign: 'middle',
		                sortable: true
		            }, {
		            	field: 'sendtime',
		                title: '时间',
		                align: 'center',
		                valign: 'middle',
		                sortable: true
		            },{
		            	field: 'status',
		                title: '读取状态',
		                align: 'center',
		                valign: 'middle',
		                sortable: true
		            } /*,{
		                field: 'operating',
		                title: '操作',
		                align: 'center',
		                valign: 'middle',
		                switchable:false,
		                formatter: operateFormatter,
		                events: operateEvents
		            }*/]
		        }).on('click-row.bs.table', function (e, row, $element) {viewmsg(row.id);});

			});
			
			
		});
</script>
<style> 
	body{font-size:14px;}
	#table-javascript{  
    width:690px;  
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
			<form action="/user/doedit" method="post">
			   	<table id="table-javascript">
					
				</table>
			</form>
		</div>
		<div class="modal-footer">
			<div class="col-md-12" align="right">
				<lable id="weidu"></lable>
		  		<button type="button" class="btn btn-default" align="right" data-dismiss="modal" onclick="messageNotReadCount()">关闭</button>
		  	</div>
		</div>
	</div>
</body>
</html>
