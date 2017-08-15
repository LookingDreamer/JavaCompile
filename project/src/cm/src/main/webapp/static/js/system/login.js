require(["jquery", "jqform", "jqgrid", "jqgridi18n", "jqvalidate", "jqmetadata", "jqvalidatei18n", "additionalmethods","public"], function ($) {

	$(function() {
		if($("#msg").val()!=""){
			$("#errorMsg").text("用户名或密码错误,请重新输入！");
		};
		
		$("#username").focus();
		$("#username").click(function(){
			$("#errorMsg").text("");
		});
		
		//返回登录
		$("body").on("click", "#returnlogin", function(e) {
			window.location.replace("/cm/auth/sign");
		});

		$("#pwderrorMsg").html("<i>密码已超过<b> 90 </b>天未修改, 请修改密码！</i>");
		
		$("#newpwd").focus();
		$("#newpwd").click(function(){
			$("#pwderrorMsg").html("<font color='#666666'>(* 新密码格式必须是6-16位包含数字、大写字母、小写字母、特殊符号四种任意三种组合!)</font>");
		});
		$("#checknewpwd").focus();
		$("#checknewpwd").click(function(){
			$("#pwderrorMsg").html("<font color='#666666'>(* 确认密码格式必须是6-16位包含数字、大写字母、小写字母、特殊符号四种任意三种组合!)</font>");
		});
		$("#oldpwd").focus();
		$("#oldpwd").click(function(){
			$("#pwderrorMsg").html("<i>密码已超过<b> 90 </b>天未修改, 请修改密码！</i>");
		});
		
		//修改密码【确定】按钮
		$("body").on("click", "#updatepasswordbutton", function(e) {
			if(!$("#oldpwd").val()){
				$("#pwderrorMsg").html("* 请输入旧密码!");
				return;
			} else if(!$("#newpwd").val()){
				$("#pwderrorMsg").html("* 请输入新密码!");
				return;
			}else if(!$("#checknewpwd").val()){
				$("#pwderrorMsg").html("* 请输入确认密码!");
				return;
			}else if($("#newpwd").val()!=$("#checknewpwd").val()){
				$("#pwderrorMsg").html("* 新密码与确认密码不一致!");
				return;
			} else if ($("#newpwd").val() == $("#oldpwd").val()){
				$("#pwderrorMsg").html("* 新密码不能与旧密码一致, 请重新输入!");
				return;
			}
			
			/*
			var checkpwdreg = /^(?![0-9a-z]+$)(?![0-9A-Z]+$)(?![0-9\W]+$)(?![a-z\W]+$)(?![a-zA-Z]+$)(?![A-Z\W]+$)[a-zA-Z0-9\W_]{6,16}$/;
			if(!checkpwdreg.test($("#newpwd").val()) || !checkpwdreg.test($("#checknewpwd").val())){
				$("#pwderrorMsg").html("* 新密码格式不规范,6-16位包含数字、大写字母、小写字母、特殊符号四种任意三种组合!");
				return;
			} else {
				$("#pwderrorMsg").html("");
			}
			*/
			
			
			//url中替换特殊字符
			var oldpwdstr = $("#oldpwd").val();
			var newpwdstr = $("#newpwd").val();
			var dispatchParams = {
				"usercode":$("#usercode").val(),
				"oldpwd": oldpwdstr,
				"newpwd": newpwdstr
			};
			
			$.ajax({
				url : "user/changepassword",
				type : 'POST',
				data : dispatchParams,
				dataType : 'json',
				cache : false,
                async : true,
				error : function() {
					alert("Connection error");
				},
				success : function(data) {
					var result = data.msg;
					if (result == "true") {
						$("#pwderrorMsg").html("* 修改成功, 请重新登录！");
						alert("修改成功, 请重新登录！");
						window.location.replace("/cm/auth/sign");
					} else if (result == "invalidpwd"){
						$("#pwderrorMsg").html("* 新密码格式不规范,6-16位包含数字、大写字母、小写字母、特殊符号四种任意三种组合!");
					} else if (result == "false"){
						$("#pwderrorMsg").html("* 旧密码错误, 请核对！");
					} else {
						alert("未知错误！" + result);
						$("#pwderrorMsg").html("* 未知错误！" + result);
					}
				}
			});
			
		});
	});
	
	function validateForm() {
		if ($("#username").val() == "" || $("#password").val() == "") {
			alert("用户名或密码不能为空。");
			return false;
		}
		return true;
	}
	
	document.onkeydown = function(e) {
		if (!e)
			e = window.event;
		if ((e.keyCode || e.which) == 13) {
			$("#loginBtn").click();
			//调用openfire登陆
		}
	}

});

