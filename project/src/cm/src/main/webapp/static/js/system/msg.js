var msg_connection = window.msg_connection || null;
var msg_services = window.msg_services || "";
var msg_resource = window.msg_resource || new Date().getTime();
//var  postnum =0;
var setIn=null;
//新任务数
var tasknum=0;
//弹框动画状态
var taskmsgStatus=0;
var socket= window.socket || null;//add by hewc for websocket 20170720
var messagePushChannel=null;//add by hewc for websocket 20170720
var websocketServer=null;//add by hewc for websocket 20170720
var defaultoptions = {
		target : (window.top.location != window.self.location ? window.top
				: window)
}
require(["jquery","system/strophe"], function ($) {
	$(function () {
		//$(window).resize();
		//add by hewc for websocket 20170720
		messagePushChannel = $("#messagePushChannel").val();
		websocketServer = $("#websocketServer").val();
		if(messagePushChannel == "websocket" && websocketServer != "" && websocketServer!=null){
			connectionSocket();
		}else{ //add by hewc for websocket 20170720
			window.msg_resource = msg_resource;
			if (msg_services == "") {
				msg_services = $("#ytyzea").val();
				window.msg_services = msg_services;
			}
			setIn=setInterval( "myReConnect()" , 3000);
		}

		
		$("#im_logut").on("click",function(){
			if(messagePushChannel == "websocket"){//add by hewc for websocket 20170720
				if(socket!=null){
					socket.disconnect();
					$(".top_user").removeClass("online").addClass("offline");
				}
				window.socket=null;
			}else{//add by hewc for websocket 20170720
				if(msg_connection!=null){
					msg_connection.disconnect();
					$(".top_user").removeClass("online").addClass("offline");
				}
				window.msg_connection=null;
			}

		});
		$("#im_message").on("click",function(){
			$(this).removeClass("blink");
		});
		$("#head_show").on("click",function(){
			$(this).removeClass("blink");
		});
		$("#im_mytask").on("click",function(){
			$(this).removeClass("blink");
		});
		var oBtn=document.getElementById('incondisplaydown'); 
		var dspan=document.getElementById('incondisplay');
		/*oBtn.onclick=function(){
	       	$('.userLogin-drop .dropdown-menu').show();
	    }
		dspan.onclick=function(){
			$('.userLogin-drop .dropdown-menu').show();	
	    }*/
		document.onclick=function(event){
	        ////兼容ie和非ie的event
	        var e=event || window.event;
	        var aim=e.srcElement || e.target; //兼容ie和非ie的事件源 && aim!=obj
	        //alert(aim); alert(oBtn);
	        if(e.srcElement){
	         var aim=e.srcElement;
	          if(aim!=oBtn&&aim!=dspan){
	        	  $('.userLogin-drop .dropdown-menu').hide()
	          }else{
	        	  $('.userLogin-drop .dropdown-menu').show();
	          }
	        }else{
	          var aim=e.target;
	          if(aim!=oBtn&&aim!=dspan){
	        	  $('.userLogin-drop .dropdown-menu').hide()
	          }else{
	        	  $('.userLogin-drop .dropdown-menu').show();
	          }
	        }
	    }    
		$('#dropdown-toggle-btn').on('click',function(){
            $('.userLogin-drop .dropdown-menu').show();
        })
        $('.dropdown-menu li').on('click',function(){
            $('#incondisplay').attr('class','fui-user cus-icon '+$(this).attr('status'));
            $('#usernamedisplay').attr('class','top_user '+$(this).attr('status'));
            //$('#downbtn').attr('class','glyphicon glyphicon-menu-down '+$(this).attr('status'));
            $('.userLogin-drop .dropdown-menu').hide()
            if ($(this).attr('status')=='busy') {
				$.ajax({
				    url: 'openfire/onlineusersetStatus',
				    type: 'GET',
				    dataType: "json",
				    data: {"status":"2","usercode":$("#ulogfute").val()},
				    async: true,
				    error: function(e) {
				      alertmsg("您的网络有点故障哦，请刷新后再试！");
				    },
				    success: function() {
				    }
				  });
			} else if ($(this).attr('status')=='online') {
				$.ajax({
				    url: 'openfire/onlineusersetStatus',
				    type: 'GET',
				    dataType: "json",
				    data: {"status":"1","usercode":$("#ulogfute").val()},
				    async: true,
				    error: function(e) {
				      alertmsg("您的网络有点故障哦，请刷新后再试！");
				    },
				    success: function() {
				    }
				  });
			}else{
				$.ajax({
				    url: 'openfire/onlineusersetStatus',
				    type: 'GET',
				    dataType: "json",
				    data: {"status":"0","usercode":$("#ulogfute").val()},
				    async: true,
				    error: function(e) {
				      //alertmsg("修改状态失败："+e);
				      alertmsg("您的网络有点故障哦，请刷新后再试！");
				    },
				    success: function() {
				    }
				  });
			}
        }) 
		$(".top_user").on("click",function(){
			if(messagePushChannel == "websocket"){
				console.log("websocket connect heart");
			}else{
				if ($(".top_user").hasClass("online")) {
					if(msg_connection!=null){
						clearInterval(setIn);
						//msg_connection.disconnect();
						//msg_connection = null
						//$(".top_user").removeClass("online").addClass("busy");
					}
				} else if ($(".top_user").hasClass("offline")) {
					//postnum = 0;
					//$(".top_user").removeClass("offline");
					setIn=setInterval( "myReConnect()" , 3000);
				}else{
					//$(".top_user").removeClass("busy");
					setIn=setInterval( "myReConnect()" , 3000);
				}
			}
		});
	});
	showTaskMsg();
});

function myReConnect(){
//	postnum++;
//	if(postnum>100){
//		clearInterval(setIn);
//		return ;
//	}
	if (msg_connection == null) {
		msg_connection = new Strophe.Connection(msg_services);
		msg_connection.connect($("#ulogfute").val()+$("#usxiazx").val()+msg_resource,$("#qazwsx").val(),
		function (status) {
			if (status == Strophe.Status.CONNECTED) {
				$(".top_user").removeClass("offline").addClass("online");
				$('#incondisplay').attr('class','fui-user cus-icon online');
		    	msg_connection.addHandler(function (msg) {
		    		console.log("msg");
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
			    		//alert(jsont1.msgtype);
			    		//我的任务消息
			    		if(jsont1.msgtype=="taskmsg"){
			    			tasknum+=1;
			    			if($("#desktop").css("display")=="none"){
			    				$("#head_show").addClass("blink");
			    			} else {
			    				//$.cmTaskList('my', 'nothing', 'noMask');
			    				$("#im_mytask").addClass("blink");
			    			}
			    			$("#im_mytask").addClass("blink");
			    			showTaskMsg();
			    			
			    		}else if(jsont1.msgtype=="taskDel"){
			    			 
			    			alert(jsont1.content);
			    			$("#desktop").css('display', 'block');
			    			$("#menu").css('display', 'none');
			    			$(".head-task").css('display', 'block');
			    			$("#apptask ul li.desk").addClass("active");
			    			if ($("#desktop_tasks").attr("src") == "about:blank") {
			    				$.insLoading();
			    				$("#desktop_tasks").attr("src", "business/mytask/queryTask");
			    			}
			    			$("#head-title").text("工作桌面");
			    			$("#head-title-s").text("切换至运营管理");
			    			$.cmTaskList('my', '', true);
			    			
			    		} else if(jsont1.msgtype=="1"){
			    			 
			    			$(".top_user").removeClass("offline").addClass("online");
			    			$('#incondisplay').attr('class','fui-user cus-icon online');
			    			
			    		} else if(jsont1.msgtype=="2"){
			    			 
			    			$(".top_user").removeClass("online").addClass("busy");
			    			$('#incondisplay').attr('class','fui-user cus-icon busy');
			    			
			    		} else if(jsont1.msgtype=="0"){
			    			 
			    			$(".top_user").removeClass("busy").addClass("offline");
			    			$('#incondisplay').attr('class','fui-user cus-icon offline');
			    			
			    		} else if(jsont1.msgtype=="msg") {
			    			$("#im_message").addClass("blink");
//			    			$.ajax({
//			    				url : '/cm/message/saveNewNotReadMessage',
//			    				type : 'POST',
//			    				dataType : 'text',
//			    				contentType: "application/json",
//			    				data : "{\"fromPeople\":\""+from+"\",\"title\":\""+jsont1.title+"\",\"content\":\""+jsont1.content+"\",\"reciever\":\""+to+"\",\"dateTime\":\""+jsont1.dateTime+"\"}",
//			    				cache : false,
//			    				async : true,
//			    				error : function() {
//			    					alert("Connection error");
//			    				},
//			    				success : function(data) {
//			    					if (data == "success") {
//			    					}else {
//			    						alert("保存新未读数据失败！");
//			    					}
//			    				}
//			    			});
			    		}
		    	    }
		    	    return true;
		    	}, null, "message", null, null, null);
		    	msg_connection.addHandler(function (presence) {
		    		console.log("presence");
		    	},null,"presence","unavailable", null,null);//监听好友下线通知
		    	msg_connection.send($pres().tree());
		    	window.msg_connection = msg_connection;
		    	console.log("connected");
		    }else if (status == Strophe.Status.AUTHFAIL || status == Strophe.Status.CONNFAIL
		    	|| status == Strophe.Status.DISCONNECTED || status == Strophe.Status.ERROR){
		    	msg_connection = null;
		    	if (!$(".top_user").hasClass("offline")&&!$(".top_user").hasClass("busy")) {
		    		$(".top_user").removeClass("online").addClass("offline");
			    	$('#incondisplay').attr('class','fui-user cus-icon offline');
		    	}
//		    	if (offline_ == 0) {
//		    		console.log("connect re try");
//		    		postnum = 0;
//					$(".top_user").removeClass("offline");
//					setIn=setInterval( "myReConnect()" , 3000);
//		    	} else {
//		    		console.log("connect dis");
//		    	}
		    }
		}, 60, 1, null);
		console.log("re connect ");
		return;
	}
	console.log("connect heart");
}

//显示任务提示
function showTaskMsg(){
	setTimeout(function(){},100);
	//任务数大于0且没有动画的时候就进入
	if(tasknum>0&&taskmsgStatus==0){
		//显示3秒
		taskmsgStatus=1;
		$("#taskmsg").slideDown("fast");
		setTimeout(function(){
			//提示框完全收起来后递归
			$("#taskmsg").fadeOut(1000);
			setTimeout(function(){
				tasknum-=1;
				taskmsgStatus=0;
				showTaskMsg();
			},1000);
		},3000);
	};
}

function connectionSocket(){
	socket = io.connect(websocketServer);
	socket.on('connect',function() {
		socket.emit("chat",{
			from:$("#ulogfute").val(),
			type:"login",
			msg:{}
		});
		$(".top_user").removeClass("offline").addClass("online");
		$('#incondisplay').attr('class','fui-user cus-icon online');
	});
	socket.on('chat', function(data) {
		socketMsg(data);
	});
}

function socketMsg(data){
	console.log("msg");
	var to = data.to;
	var from = data.from;
	var type = data.type;
	var body = data.msg;
	console.log(body);
	if (type == "chat") {
		//替换
		//alert(jsont1.msgtype);
		//我的任务消息
		if(data.msg.msgtype=="taskmsg"){
			tasknum+=1;
			if($("#desktop").css("display")=="none"){
				$("#head_show").addClass("blink");
			} else {
				//$.cmTaskList('my', 'nothing', 'noMask');
				$("#im_mytask").addClass("blink");
			}
			$("#im_mytask").addClass("blink");
			showTaskMsg();

		}else if(data.msg.msgtype=="taskDel"){

			alert(data.msg.content);
			$("#desktop").css('display', 'block');
			$("#menu").css('display', 'none');
			$(".head-task").css('display', 'block');
			$("#apptask ul li.desk").addClass("active");
			if ($("#desktop_tasks").attr("src") == "about:blank") {
				$.insLoading();
				$("#desktop_tasks").attr("src", "business/mytask/queryTask");
			}
			$("#head-title").text("工作桌面");
			$("#head-title-s").text("切换至运营管理");
			$.cmTaskList('my', '', true);

		} else if(data.msg.msgtype=="1"){

			$(".top_user").removeClass("offline").addClass("online");
			$('#incondisplay').attr('class','fui-user cus-icon online');

		} else if(data.msg.msgtype=="2"){

			$(".top_user").removeClass("online").addClass("busy");
			$('#incondisplay').attr('class','fui-user cus-icon busy');

		} else if(data.msg.msgtype=="0"){

			$(".top_user").removeClass("busy").addClass("offline");
			$('#incondisplay').attr('class','fui-user cus-icon offline');

		} else if(data.msg.msgtype=="msg") {
			$("#im_message").addClass("blink");
//			    			$.ajax({
//			    				url : '/cm/message/saveNewNotReadMessage',
//			    				type : 'POST',
//			    				dataType : 'text',
//			    				contentType: "application/json",
//			    				data : "{\"fromPeople\":\""+from+"\",\"title\":\""+jsont1.title+"\",\"content\":\""+jsont1.content+"\",\"reciever\":\""+to+"\",\"dateTime\":\""+jsont1.dateTime+"\"}",
//			    				cache : false,
//			    				async : true,
//			    				error : function() {
//			    					alert("Connection error");
//			    				},
//			    				success : function(data) {
//			    					if (data == "success") {
//			    					}else {
//			    						alert("保存新未读数据失败！");
//			    					}
//			    				}
//			    			});
		}
	}
}



