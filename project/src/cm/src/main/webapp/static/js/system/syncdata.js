require([ "jquery","bootstrap", "public" ], function($) {

	$(function() {
		$('#syncdept').on('click',function (){
			confirmmsg("开始同步机构数据", function(){
				$.ajax({
				    url: 'sync',
				    type: 'POST',
				    dataType: "json",
				    data: {"synctype":"dept"},
				    async: true,
				    error: function(e) {
				      alertmsg("保存失败："+e);
				    },
				    success: function(data) {
				      if (data != null) {
				    	  alertmsg(data.returnMsg);
				      }
				    }
				  });
			})
		});
		$('#sychdeptbycode').on('click',function (){
			if($("#comcode").val()){
				confirmmsg("开始同步机构数据", function(){
					$.ajax({
					    url: 'sync',
					    type: 'POST',
					    dataType: "json",
					    data: {"synctype":"dept","comcode":$("#comcode").val()},
					    async: true,
					    error: function(e) {
					      alertmsg("保存失败："+e);
					    },
					    success: function(data) {
					      if (data != null) {
					    	  alertmsg(data.returnMsg);
					      }
					    }
					  });
				})
			}else{
				alertmsg("请先填写需要同步机构代码。");
			}
		});
		$('#syncprovider').on('click',function (){
			confirmmsg("开始同步供应商数据", function(){
				$.ajax({
				    url: 'sync',
				    type: 'POST',
				    dataType: "json",
				    data: {"synctype":"insbprovider"},
				    async: true,
				    error: function(e) {
				      alertmsg("保存失败："+e);
				    },
				    success: function(data) {
				      if (data != null) {
				    	  alertmsg(data.returnMsg);
				      }
				    }
				  });
			})
		});
		$('#sychAgtData').on('click',function (){
			confirmmsg("开始同步代理人数据", function(){
				$.ajax({
				    url: 'sync',
				    type: 'POST',
				    dataType: "json",
				    data: {"synctype":"agt"},
				    async: true,
				    error: function(e) {
				      alertmsg("保存失败："+e);
				    },
				    success: function(data) {
			    	 if (data != null) {
				    	  alertmsg(data.returnMsg);
				      }
				    }
				  });
			});
		});
		
		$('#sychStatus').on('click',function (){
			$.ajax({
			    url: 'status',
			    type: 'POST',
			    dataType: "json",
			    async: true,
			    error: function(e) {
			      alertmsg("查询失败："+e);
			    },
			    success: function(data) {
		    	 if (data != null) {
		    		 $('#sychStatusMsg').html(data.returnMsg);
			      }
			    }
			  });
		});
		
		$('#sychAgtDatabyAgcode').on('click',function (){
			if($("#agcodevalue").val()){
				confirmmsg("根据工号同步代理人数据", function(){
					$.ajax({
					    url: 'sync',
					    type: 'POST',
					    dataType: "json",
					    data: {"synctype":"agt","agentcode":$("#agcodevalue").val()},
					    async: true,
					    error: function(e) {
					      alertmsg("保存失败："+e);
					    },
					    success: function(data) {
				    	 if (data != null) {
					    	  alertmsg(data.returnMsg);
					      }
					    }
					  });
				});
			}else{
				alertmsg("请先填写需要同步的代理人工号。");
			}
		});
	});
});
