require(["jquery", "bootstrap-table", "ajaxfileupload", "bootstrap", "bootstrapTableZhCn", "zTree", "zTreecheck", "fuelux", "public","multiselect","jqvalidatei18n",
	"additionalmethods","bootstrapdatetimepicker",
	"bootstrapdatetimepickeri18n","jqtreeview"], function ($) {

        	var insbChannel = new InsbChannel();
        	insbChannel.init();
        	
        	
        });

/**渠道对象*/
var InsbChannel = function () {
};

/**渠道属性方法*/
InsbChannel.prototype = {
	//参数设置(与获取)
	param: function() {
		var that = this;
		var channel = {
			upchannelcode: $("#upchannelcode").val(),
			channelcode: $("#channelcode").val(),
			channelsecret: $("#channelsecret").val(),
			channelinnercode: $("#channelinnercode").val(),
			channelname: $("#channelname").val(),
			channeltype: $("#channeltype").val(),
			webaddress: $("#webaddress").val(),
			address: $("#address").val(),
			deptid: $("#deptid").val(),
			jobnum: $("#jobnum").val(),
			province: $("#province").val(),
			citys: that.getSelectedCityIds(),
			channelid: $("#channelid").val(),
			agreementid: $("#agreementid").val(),
			agreementstatus: $("#agreementstatus").val(),
			illustration: $("#illustration").val(),
			noti: $("#noti").val(),
			senderphone: $("#senderphone").val(),
			senderchannel: $("#senderchannel").val(),
			senderaddress: $("#senderaddress").val(),
			sendername: $("#sendername").val(),
			isdefined: $("#isdefined").val()
		};
		console.log("channel-illustration:" + channel.illustration);
		return channel ;
	},

	//初始化
	init: function () {
		var that = this;

		that.initTree();
		that.initArea();
		$("#province").click(that.changeProv()) ;
		$("#deptname").click(that.showDeptDialog) ; //所属机构点击
		$("#addButton").click(that.showAddChannelTab) ; //添加渠道点击
		//$("#providertabDetailNav").click(that.initProvider);
		$("#deleteButton").click(function() { that.deleted(that) ; }) ; //删除渠道点击
		$("#saveButton").click(function() { that.saveAndUpdate(that) ; }) ; //保存渠道点击
		$('#isdefined').change(function(){ that.changeSen(that);});
	},

	//初始化渠道列表
	initTree: function () {
		var that = this;
		$("#channelTree").empty() ;
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
					view: {
						fontCss: function (treeId, treeNode) {
							return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
						},
						expandSpeed: ""
					},
					callback : {
						onClick : function (event, treeId, treeNode) {
							that.treeNodeClick(event, treeId, treeNode, that);}
					}
				}, data);
			}
		});
		
	},

	//初始化应用地区
	initArea: function () {
		var that = this;
		//初始化省份
		that.initProv();
		//初始化市区
		that.changeProv();
		//绑定省份选择事件changeProv
		$("#areaDiv").delegate("select", "change", that.changeProv);
		
	},

	//渠道列表点击事件
	treeNodeClick: function (event, treeId, treeNode, that) {
		var channelId = treeNode.id;

		$("#deleteButton").show();
		$.ajax({
			type: "POST",
			url: "/cm/channel/channelInfo",
			data: "id=" + channelId,
			dataType: "json",
			success: function (data) {
				$("#channelid").val(data.channelInfo.id);
				$("#upchannelcode").val(data.channelInfo.upchannelcode);
				$("#channelcode").val(data.channelInfo.channelcode);
				$("#channelinnercode").val(data.channelInfo.channelinnercode);
				$("#channelname").val(data.channelInfo.channelname);
				$("#province").val(data.channelInfo.province);
				$("#address").val(data.channelInfo.address);
				$("#webaddress").val(data.channelInfo.webaddress);
				$("#jobnum").val(data.channelInfo.jobnum);
				$("#channeltype").val(data.channelInfo.channeltype);
				$("#channelsecret").val(data.channelInfo.channelsecret);
				$("#illustration").val(data.channelInfo.illustration);
				$("#noti").val(data.channelInfo.noti);
				/**
				 * 新增的寄件人信息
				 */
				$("#senderphone").val(data.channelInfo.senderphone);
				$("#senderchannel").val(data.channelInfo.senderchannel);
				$("#senderaddress").val(data.channelInfo.senderaddress);
				$("#sendername").val(data.channelInfo.sendername);
				$("#isdefined").val(data.channelInfo.isdefined);
				//修改机构信息
				if(data.channelInfo.dept != null) {
					$("#deptid").val(data.channelInfo.dept.comcode);
					$("#deptname").val(data.channelInfo.dept.comname);
				} else {
					$("#deptid").val("");
					$("#deptname").val("");
				}

				//修改协议信息
				if(data.channelInfo.agreement != null){
					$("#agreementstatus").val(data.channelInfo.agreement.agreementstatus);
					$("#agreementid").val(data.channelInfo.agreement.id);
					console.log("agreementid:"+data.channelInfo.agreement.id);
				} else {
					$("#agreementstatus").val(0);
					$("#agreementid").val("");
				}
				if($("#isdefined").val()== '0') {
					$("#senderphone").attr("readonly", "readonly");
					$("#senderchannel").attr("readonly", "readonly");
					$("#senderaddress").attr("readonly", "readonly");
					$("#sendername").attr("readonly", "readonly");
				}else {
					$("#senderphone").removeAttr("readonly", "readonly");
					$("#senderchannel").removeAttr("readonly", "readonly");
					$("#senderaddress").removeAttr("readonly", "readonly");
					$("#sendername").removeAttr("readonly", "readonly");
				}
				//是否父节点
				if ($("#upchannelcode").val() == '') {
					//是父节点 可以填写秘钥
					$("#channelsecret").removeAttr("readonly");
					
				} else {
					//不是父节点 不能填写秘钥
					$("#channelsecret").attr("readonly", "readonly");
					$("#channeltype").parent().parent("#channeltype");
//					$(".webaddress").hide;
//					$(".payaddress").hide;
				}

				$("#province").trigger("change");     //触发改变事件

				//如果是父节点，隐藏childItem项
				if($("#upchannelcode").val() == null
					|| $("#upchannelcode").val() == "") {
					$(".childItem").hide() ;
					$(".webaddress").show();
					$(".payaddress").show();
					$(".channeltype").show();
					
				} else {
					$(".childItem").show() ;
					$(".webaddress").hide();
					$(".payaddress").hide();
					$(".channeltype").hide();
				}
			}

		});
	},

	//初始化省份
	initProv: function () {
		var $province = $("#province");
		$.ajax({
			url: "/cm/region/getregionsbyparentid",
			type: 'GET',
			dataType: "json",
			data: {
				parentid: "0"
			},
			//async : true,
			async: false,
			error: function () {
				alertmsg("Connection error");
			},
			success: function (data) {
				$province.empty();
				if (data != null && data.length > 0) {
					for (var i = 0; i < data.length; i++) {
						$province.append("<option value='" + data[i].id + "'>" + data[i].comcodename + "</option>");
					}
					$("#province").val($province.val());
				}
			}
		});
	},

	//省份选择事件
	changeProv: function () {
		//选中修改值域
		var provId = $("#province").val();
		var $cityDiv = $("#cityDiv");
		//设置已选地区
		var agreementId = $("#agreementid").val() ;
		$.ajax({
			url : "/cm/channel/getSelectArea",
			type : 'post',
			dataType : "JSON",
			data: {
				"province": provId,
				"agreementid": agreementId
			},
			async : false,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				console.log("city:"+data);
				var citys = "";
				for(var i=0;i<data.length;i++){
					if(data[i].parentid == "checked"){
						//citys += "<input type=\"checkbox\" checked name=\"citys["+i+"]\" value=\""+data[i].comcode+"\"/>"+data[i].comcodename;
						citys += "<input type=\"checkbox\" checked name=\"citys\" value=\""+data[i].comcode+"\"/>"+data[i].comcodename;
					}else{
						//citys += "<input type=\"checkbox\" name=\"citys["+i+"]\" value=\""+data[i].comcode+"\"/>"+data[i].comcodename;
						citys += "<input type=\"checkbox\" name=\"citys\" value=\""+data[i].comcode+"\"/>"+data[i].comcodename;
					}
					//9个城市换一行
					if((i+1)%9==0){
						citys += "<br>";
					}
				}
				$cityDiv.html(citys);
			}
		});
	},
	
	//所属机构点击事件
	showDeptDialog: function() {
		$('#deptDialog').modal();
		$.fn.zTree.init($("#deptTree"), {
			async: {
				enable: true,
				url:"/cm/channel/queryDeptTreeList",
				autoParam:["id"],
				dataType: "json",
				type: "post"
			},
//			view: {
//				fontCss: function (treeId, treeNode) {
//					return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
//				},
//				expandSpeed: ""
//			},
			check: {
				enable: true,
				chkStyle: "radio",
				radioType: "all"
			},
			callback: {
				onCheck: function(event, treeId, treeNode) {
					$("#deptid").val(treeNode.id);
					$("#deptname").val(treeNode.name);
					$('#deptDialog').modal("hide");
				}
			}
		});
	},

	//展示添加渠道Tab
	showAddChannelTab: function() {
		/*if(!$("#comcode").val()){
		 alertmsg("请选择渠道!")
		 }*/
		$("#upchannelcode").val($("#channelcode").val());
		$("#channelcode").val("");
		$("#channelname").val("");
		$("#province").val("110000");
		//触发改变事件
		$("#province").trigger("change");
//		$("#isdefined").trigger("change");
		$("#address").val("");
		$("#webaddress").val("");
		$("#channelid").val("");
		$("#agreementid").val("");
		$("#deptid").val("");
		$("#deptname").val("");
		$("#jobnum").val("");
		$("#illustration").val("");
		$("#channeltype").val();
		$("#noti").val("");
		$("#senderphone").val("");
		$("#senderchannel").val("");
		$("#senderaddress").val("");
		$("#sendername").val("");
		$("#isdefined").val("0");
		if($("#upchannelcode").val() == null
			|| $("#upchannelcode").val() == ''){
			$("#channelsecret").removeAttr("readonly").val("");
			$("#channelinnercode").val("");
			$(".childItem").hide() ;
			$(".webaddress").show();
			$(".payaddress").show();
			$(".channeltype").show();
		}else{
			$("#channelsecret").attr("readonly","readonly") ;
			$(".childItem").show() ;
			$(".webaddress").hide();
			$(".payaddress").hide();
			$(".channeltype").hide();
		}
		if($("#isdefined").val()== '0') {
			$("#senderphone").attr("readonly", "readonly");
			$("#senderchannel").attr("readonly", "readonly");
			$("#senderaddress").attr("readonly", "readonly");
			$("#sendername").attr("readonly", "readonly");
		}else {
			$("#senderphone").removeAttr("readonly", "readonly");
			$("#senderchannel").removeAttr("readonly", "readonly");
			$("#senderaddress").removeAttr("readonly", "readonly");
			$("#sendername").removeAttr("readonly", "readonly");
		}
	},

	//保存或更新渠道
	saveAndUpdate: function(that) {
		var param = that.param();

		//参数检测
		if(param.channelinnercode == null || param.channelinnercode == "") {
			alertmsg("渠道id不能为空 ");
			return false;
		}
		if(param.channelname == null || param.channelname == "") {
			alertmsg("渠道名称不能为空");
			return false;
		}
		if((param.upchannelcode != null && param.upchannelcode != "" )
			&& (param.deptid == null || param.deptid == "")) {
			alertmsg("所属机构不能为空");
			return false;
		}
		//子节点必须填写地址
		if(param.upchannelcode != null &&
			param.upchannelcode != "" &&
			$("input[name^='citys']:checked").length==0 && param.channeltype != "02"){
			alertmsg("请选择地市");
			return false;
		}
	
		//提交表单 (firefox不支持submit的)
		//$("#orgsaveform").submit();
//		
//		if(param.upchannelcode == null && param.upchannelcode == "" ) {
//			
//			param.channeltype = $("#channeltype").parent().parent("#channeltype");
//		}
		$.post("/cm/channel/saveAndUpdate", that.param(), function(data) {
			if(data.errCode == "0") {
				alertmsg("保存成功") ;
				//刷新树
				that.initTree() ;
				$(".ztree-search").val('');
				//清空表单
				//that.clear() ;
			}else {
				alertmsg("操作失败，无法更新或添加");
				//清空表单
				that.clear() ;
			}
		}) ;
	},
	changeSen : function (that) {
		var isdefined = $('#isdefined').val();
		if(isdefined== '0') {
			$("#senderphone").attr("readonly", "readonly");
			$("#senderchannel").attr("readonly", "readonly");
			$("#senderaddress").attr("readonly", "readonly");
			$("#sendername").attr("readonly", "readonly");
		}else {
			$("#senderphone").removeAttr("readonly", "readonly");
			$("#senderchannel").removeAttr("readonly", "readonly");
			$("#senderaddress").removeAttr("readonly", "readonly");
			$("#sendername").removeAttr("readonly", "readonly");
		}
	},
	//删除渠道
	deleted: function(that) {
		var param = that.param() ;
		var channelId = param.channelid ;
		if(channelId == null || channelId == ""){
			alertmsg("请选择渠道!");
			return false ;
		}
		confirmmsg("是否确定删除"+$("#channelname").val()+"渠道？", function() {
			$.post("/cm/channel/delete", {id: channelId}, function(data) {
				if(data.errCode == "0") {
					alertmsg("删除成功") ;
					//刷新树
					that.initTree() ;

					//清空表单
					that.clear() ;
				} else {
					alertmsg("当前渠道存在下级渠道，不允许删除！");
				}
			}) ;
		}, null) ;
	},

	//获取选中地区ID
	getSelectedCityIds: function() {
		//获取选中地区
		var cityIds = {};
		var $citys = $("input[name^='citys']:checked") ;
		$citys.each(function(index) {
			cityIds[""+index+""] = $(this).val() ;
		});

		return cityIds ;
	},

	//清空项
	clear: function() {
		$("#upchannelcode").val("");
		$("#channelcode").val("");
		$("#channelname").val("");
		$("#province").val("110000");
		$("#province").trigger("change");
		$("#address").val("");
		$("#webaddress").val("");
		$("#channelid").val("");
		$("#agreementid").val("");
		$("#deptid").val("");
		$("#deptname").val("");
		$("#jobnum").val("");
		$("#illustration").val("");
		$("#noti").val("");
		$("#channelsecret").removeAttr("readonly").val("");
		$("#channelinnercode").val("");
		$("#senderphone").val("");
		$("#senderchannel").val("");
		$("#senderaddress").val("");
		$("#sendername").val("");
		$("#isdefined").val("0");
		$(".childItem").hide();
	}
}