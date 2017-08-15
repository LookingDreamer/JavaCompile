//默认一页显示十条记录
var pagesize = 2;
//当前调用的url
var pageurl = "message/listAllMessagesPaging";
//获得选中行的id列表
function getSelectedRows() {
    var data = $('#table-javascript').bootstrapTable('getSelections');
    if(data.length == 0){
    	alertmsg("没有行被选中！");
    }else{
    	var arrayuserid = new Array();
    	for(var i=0;i<data.length;i++){
    		arrayuserid.push(data[i].id)
    	}
    	return arrayuserid;
    }
} 

//删除信息
function deletemsg(id){
	if(!confirm("是否确认删除？"))return;
	$.ajax({
		url:"message/deleteMessage",
		method:"get",
		dataType : 'text',
		data:"messageid="+id,
		success:function(data){
			if(data=="success")
				alertmsg("删除成功");
			else{
				alertmsg("删除失败");
			}
		},
		error:function(){
			alertmsg("删除操作失败");
		}
	});
}
//查看信息
function viewmsg(id){
	openDialog('message/viewMessage?msgId='+id);
}
//刷新列表
function reloaddata(data){
	$.ajax({
		url : pageurl,
		type : 'GET',
		dataType : "json",
		data: data+"&limit="+pagesize+"&receiver="+$("#receiver").val(),
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#table-javascript').bootstrapTable('load', data);
		}
	});
}

//添加事件
function operateFormatter(value, row, index) {
  return [
      /*'<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
      '<i class="glyphicon glyphicon-edit"></i>',
      '</a>',
      '<a class="remove m-left-5" href="javascript:void(0)" title="删除">',
      '<i class="glyphicon glyphicon-remove"></i>',
      '</a>'
      */
  ].join('');
}
//事件相应
window.operateEvents = {
  /*'click .edit': function (e, value, row, index) {
  	viewmsg(row.id);
  },*/
  'click .remove': function (e, value, row, index) {
  	deletemsg(row.id);
  }
};
function messageNotReadCount(){
	$.ajax({
		  type: "get",
		  url: "message/getNotReadCount",
		  data: {receiver:$("#receiver").val()},
		  dataType:"text",
		  success: function(data){
			  $("#messageNotReadCount").text(data);
		  }
		});
}