require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","jqvalidatei18n","public"], function ($) {
	//数据初始化 
	$(function() {
		 $("#agreementid1").change(function(){
			 var agreementid= $("#agreementid1").find("option:selected").val();
			 $.ajax({
				 url:'getwangdianbyagreement',
				 data:({agreementid:agreementid}),
				 type:"post",
				 success:function(data){
//					 $("#wdid2").children().remove();
//					 if(data.length>0){
//						 var temp='<option value="">请选择-网点</option>';
//						 for(var i=0;i<data.length;i++ ){
//							 temp +='<option value="'+data[i].comcode+'"  >'+data[i].comname+'</option>'
//						 } 
//						 $("#wdid2").append(temp);
//					 }
//					
				 }
			 })
		 })
//		返回
		$('#backbutton').on("click",function(){
			history.go(-1)
		});
		 
		 $("#closeMyModel").on("click",function(){
			 var channelid = $("#paychannelid").val();
				window.location.href="paychanneledit?id="+channelid;
		 })

	});
	$("#deptTree").on("click", function(e) {
		$('#showdept').modal();
		$.fn.zTree.init($("#depttree"), deptsetting);
	});


	if ($('input:radio[id="collectiontype4"]:checked').val()) {
		$("#collectiontype1").show();
	} else {
		$("#collectiontype1").hide();
	}

	$(":radio").click(function(){
		if ($('input:radio[id="collectiontype4"]:checked').val()) {
			$("#collectiontype1").show();
		} else {
			$("#collectiontype1").hide();
		}
	});

});

var deptsetting = {
		async : {
			enable : true,
			url : "../agreement/inscdeptlist2",
			autoParam : [ "id=root" ]
		},
		check : {
			enable : true,
			chkStyle : "radio",
			radioType : "all"
		},
		callback: {
			onCheck: deptTreeOnCheck
		}
	};
function deptTreeOnCheck(event, treeId, treeNode) {
	$("#pladeptid").val(treeNode.id);
	$("#deptTree").val(treeNode.name);
	$('#showdept').modal("hide");
	var pladeptid = treeNode.id;
	$.ajax({
		 url:'getpladept',
		 data :({pladeptid:pladeptid}),
		 type:"post",
		 success:function(data){
//			 $("#agreementid1").children().remove();
			 if(data) {
				 $("#addagreement").remove();
			 }
			 if(data.length>0){
//				 var temp='<option value="">请选择-协议</option>';
//				 $("#agreementid1").append(temp);
//				 for(var i=0;i<data.length;i++ ){
//					 temp +='<option value="'+data[i].id+'"  >'+data[i].agreementname+'</option>'
//				 } 
				 if ($("#addagreement").length>0) {

					 var html = "<div id='addagreement'>"; 
						$.each(data, function(j, item) {
							 html +="<label><input name='agreementids' type='checkbox' value='"+item.id+"'/>"+item.agreementname+"</label>";
						});
						html +="</div>";
					 $("#guanlianagreement").append(html);
				}else{
					var html = "<div id='addagreement'>"; 
					$.each(data, function(j, item) {
						 html +="<label><input name='agreementids' type='checkbox' value='"+item.id+"'/>"+item.agreementname+"</label>";
					});
					html +="</div>";
				 $("#guanlianagreement").append(html);
				}
			 }
			
		 }
	 })
}

function onsubmitClick() {
	if ($("input[name^='agreementids']:checked").length==0) {
		alert("请至少选择一个协议！");
		return false;
	}

	if ($("#collectiontype1").is(":visible")==true) {
		if (!$("#collectiontype1").val()) {
			alert("请选择具体的第三方结算商户");
			return false;
		}
		$('input:radio[id="collectiontype4"]:checked').val($("#collectiontype1").val());
	}

	return true;
}