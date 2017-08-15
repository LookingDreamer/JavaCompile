require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "fileinput", "fileinputi18n"], function ($) {

	$(function () {
		//添加保存分类
		$("#saveclassify").on("click", function(e) {
			window.location.href="file/index";
		});
	});

});
