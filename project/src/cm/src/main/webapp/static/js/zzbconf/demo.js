require(["jquery","public"], function ($) {
	document.onkeydown = function(e) {
		if (!e)
			e = window.event;
		if ((e.keyCode || e.which) == 13) {
			$("#loginBtn").click();
		}
	}
	
});

function validateForm() {
		if ($("#username").val() == "" || $("#password").val() == "") {
			alertmsg("用户名或密码不能为空。");
			return false;
		}
		return true;
	}
	$(function() {

		$("#username").focus();
	});
	
