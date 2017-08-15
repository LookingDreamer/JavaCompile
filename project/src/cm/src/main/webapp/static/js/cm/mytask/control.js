// 任务操作控制
require(["jquery", "core" ], function ($) {
	
	$(function() {
		$(".manualtask").css("margin-top",$(".task_fixtop").outerHeight());
		$(window).scroll(function() {
			if ($(window).scrollTop() - $(".task_fixtop").outerHeight() >= $(".myTabdiv").offset().top) {
				$(".myTabdivfix").css({
					"position":"fixed",
					"top" : $(".task_fixtop").outerHeight() + "px"
				});
				$(".taskimgs").css({
					"position":"fixed",
					"top" : ($(".task_fixtop").outerHeight() + 5) + "px",
					"right":"35px"
				});
			} else {
				$(".myTabdivfix").css("position","static");
				$(".taskimgs").css({
					"position":"absolute",
					"top" : "-60px",
					"right":"20px"
				});
			}
		});
		$(".imgbtn").click(function(){
			if ($(".imglist").css("display") == "none") {
				$(".imglist").show();
			} else {
				$(".imglist").hide();
			}
		});
		
		$("div.flowCompleted:not(.fArrow)").last().removeClass("flowCompleted").addClass("flowProcessing");
		$("div.flowProcessing").next(".flowCompleted").removeClass("flowCompleted").addClass("flowProcessing");
	});
});
