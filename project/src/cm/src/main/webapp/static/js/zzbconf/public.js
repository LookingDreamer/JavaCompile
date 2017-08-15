/**
 * 效果等同于，alert和confirm
 */
//提示框初始化
function init(content,title,yes,no){
	var html = '<div id="alert_div" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" '+
			'data-backdrop="false" data-keyboard="false" aria-hidden="true" style="position: absolute;z-index: 99999;">'+
			'<div class="modal-dialog modal-sm"><div class="modal-content"><div class="modal-header">'+
			'<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true" onclick="closediv();">&times;</span>'+
			'</button><h4 class="modal-title">'+title+'</h4></div>'+
			'<div class="modal-body row" style="text-align: center;"><div style="text-align: center;"><p>'+content+'</p></div><div style="float: right;margin:0px 15px 0px 10px;">';
	var yesbutton = '<button id="buttonyes" type="button" class="btn btn-primary btn-sm">确定</button>';
	var nobutton = '<button id="buttonno" type="button" class="btn btn-default btn-sm m-left-5" data-dismiss="modal">取消</button>';
	var endhtml = '</div></div></div></div></div>';
	if(yes.length > 0){
		html += yesbutton;
	}
	if(no.length > 0){
		html += nobutton;
	}
	html += endhtml;
	$(document.body).append(html);
}
//只有确定按钮
function alertmsg(content,callback){
	init(content,"消息","yes","");
	$("#alert_div").modal();
	buttonyes(callback);
}

//什么都没有的提示
function alertmsgno(content,callback){
	init(content,"消息","","");
	$("#alert_div").modal();
	//buttonyes(callback);
}
//有确定和取消两个按钮
function confirmmsg(content,callbackyes,callbackno){
	init(content,"提示","yes","no");
	$("#alert_div").modal();
	buttonyes(callbackyes);
	buttonno(callbackno);
}
//关闭提示框
function closediv(){
	$(document.body).removeClass("modal-open");
	$(document.body).removeAttr("style")
	$('#alert_div').modal("hide");
	$("#alert_div").remove();
	if($("#xDialog").length>0){
		$(document.body).addClass("modal-open");
	}
}
//点击确定按钮  可加回掉函数
function buttonyes(callback){
	$("#buttonyes").click(function(){
		closediv();
		if (typeof (callback) == 'function') {
	        callback();
	      }
	});
}
//点击取消按钮  可加回掉函数
function buttonno(callback){
	$("#buttonno").click(function(){
		closediv();
		if (typeof (callback) == 'function') {
	        callback();
	      }
	});
}
function dateFormatter(datetime){
	if(datetime!=null){
		return new Date(datetime.time).Format("yyyy-MM-dd hh:mm:ss");
	}
	
}
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}     
//未通过验证的加样式
function validateHighlight(element) {
	$(element).closest(".form-group").addClass("has-error");
}
// 验证成功调用
function validateSuccess(element) {
	$(".form-group").removeClass("has-error");
	element.remove();
}
     

