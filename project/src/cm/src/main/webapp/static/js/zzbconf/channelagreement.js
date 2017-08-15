require([ "jquery", "zTree", "zTreecheck", "flat-ui", "bootstrap", "bootstrap-table", "fuelux", "bootstrapTableZhCn",
	"public","jqtreeview","multiselect","zTreeexhide" ], function($) {
	
	$("#protoSwitch").click(function () {
		//alert("111");
		freshChannelSwitch();
    });
	
	/*ztree数据*/
	$.ajax({
		url : "/cm/channel/queryTreeListDim",
		type : 'POST',
		dataType : "json",
		error : function() {
			$("#channelTree").html('加载失败，<a href="javascrpt:void(0);" onclick=";">点击这里</a>重试。');
		},
		success : function(data) {
			$.fn.zTree.init($("#channelTree"), {
				data : {
					simpleData: {
						enable: true,
						idKey: "id",
						pIdKey: "pId",
						rootPId: null
					}
				},
				check : {
					enable : true,
					chkStyle : "radio",
					radioType : "all"
				},
				view: {
					fontCss: function (treeId, treeNode) {
						return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
					},
					expandSpeed: ""
				},
				callback : {
					onClick : freshchannelinfo,
					onCheck : freshchannelinfo
				}
			}, data);
		}
	});
	
//	$.fn.zTree.init($("#channelTree"), channelSetting);
	function freshchannelinfo(event, treeId, treeNode){
		//alert("选中的渠道id="+treeNode.id);
		$.ajax({
			url : '/cm/channel/getNewChannelProtocolInfo',
			type : 'GET',
			dataType : 'html',
			contentType: "application/json" ,
			data :{
				"id":treeNode.id
			},
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data) {
					$("#channelagreementsubpage").empty();
					$("#channelagreementsubpage").html(data);
				}else{
					alertmsg("读取渠道信息失败！");
				}
			}
		});
	}
	
	function freshChannelSwitch() {
		
		$.ajax({
			url : '/cm/channelagreement/agreementChn',
			type : 'GET',
			dataType : 'html',
			//contentType: "application/json" ,
			//data :{
			//	"id":treeNode.id
			//},
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data) {
					$("#channelagreementsubpage").empty();
					$("#channelagreementsubpage").html(data);
				}else{
					alertmsg("读取协议信息失败！");
				}
			}
		});
	}
	
});
