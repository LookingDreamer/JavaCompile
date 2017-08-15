var BOSH_SERVICE = 'http://203.195.141.57:7070/http-bind/';
var connection = null;
var tFlag = false;

function onConnect(status)
{
    if (status == Strophe.Status.CONNECTED) {
    	tFlag = true;
    	connection.addHandler(onMessage, null, 'message', null, null,  null); 
    	connection.send($pres().tree());
    }else if (status == Strophe.Status.CONNFAIL) {
    	tFlag = false;
    }else if (status == Strophe.Status.DISCONNECTING) {
    	tFlag = false;
    }else if (status == Strophe.Status.DISCONNECTED) {
        tFlag = false;
    }
}

function onMessage(msg) {
    var to = msg.getAttribute('to');
    var from = msg.getAttribute('from');
    var type = msg.getAttribute('type');
    var elems = msg.getElementsByTagName('body');
    if (type == "chat" && elems.length > 0) {
	var body = elems[0];
	var text1 = Strophe.getText(body);
	text1 = text1.replace(/&quot;/g,"\"");
	//替换
	var jsont1 = $.parseJSON(text1);
	//我的任务消息
	if(jsont1.msgtype=="taskmsg"){
		$("#im_mytask").addClass("blink");
	}
	else if(jsont1.msgtype=="msg"){
		$("#im_message").addClass("blink");
		//alert(jsont1.dateTime);
		$.ajax({
			url : '/cm/message/saveNewNotReadMessage',
			type : 'POST',
			dataType : 'text',
			contentType: "application/json",
			data : "{\"fromPeople\":\""+from+"\",\"title\":\""+jsont1.title+"\",\"content\":\""+jsont1.content+"\",\"reciever\":\""+to+"\",\"dateTime\":\""+jsont1.dateTime+"\"}",
			cache : false,
			async : true,
			error : function() {
				alert("Connection error");
			},
			success : function(data) {
				if (data == "success") {
					//alert("保存新未读数据成功！");
				}else {
					alert("保存新未读数据失败！");
				}
			}
		});
	}
	
    }
    // we must return true to keep the handler alive.  
    // returning false would remove it after it finishes.
    return true;
}

$(document).ready(function () {
	connection = new Strophe.Connection(BOSH_SERVICE);
	connection.connect($("#ulogfute").val()+'@localhost/cm',$("#qazwsx").val(),onConnect,'','','');
	setInterval( "myReConnect()" , 25000);
	//25秒后重新连接
	$("#im_logut").on("click",function(){
		 connection.disconnect();
		 //断开连接
	});
	$("#im_message").on("click",function(){
		$(this).removeClass("blink");
	});
	$("#im_mytask").on("click",function(){
		$(this).removeClass("blink");
	});
});

function myReConnect(){
	if(tFlag==false){
		connection = new Strophe.Connection(BOSH_SERVICE);
		connection.connect($("#ulogfute").val()+'@localhost/cm',$("#qazwsx").val(),onConnect,'','','');
	}
}
