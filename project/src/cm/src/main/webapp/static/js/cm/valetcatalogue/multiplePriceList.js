require([ "jquery", "zTree", "zTreecheck", "flat-ui", "bootstrap-table", "bootstrapdatetimepicker","bootstrapdatetimepickeri18n",
		"bootstrap", "bootstrapTableZhCn" ], function($) {
	getconfig($("#taskid").val(),$("#li0").val());
	$("#confirmpid").on("click",function(){
		$("#mymodallipei").modal('hide');
	});
})
function getconfig(taskid,inscomcode){
	$.ajax({
		url : "insuredconflist",
		type : 'GET',
		dataType : "json",
		data: {taskid:taskid,
				inscomcode:inscomcode
				},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			deleteconfigtabletr(inscomcode);
			createconfigtabletr(inscomcode,data.body);
		}
	});
}

function deleteconfigtabletr(inscomcode){
	var listtr = $("#configtable"+inscomcode).find("tr");
	if(listtr.length > 0){
		for(var i=0;i<listtr.length;i++){
			$(listtr[i]).remove();
		}
	}
	var listtr1 = $("#configtable1"+inscomcode).find("tr");
	if(listtr1.length > 0){
		for(var i=0;i<listtr1.length;i++){
			$(listtr1[i]).remove();
		}
	}
}

function policySubmit(button1,taskid,agentnum){
	
	$.ajax({
		url:'checkSubStep',
		type:'post',
		data:{mainTaskid : taskid , agentnum : agentnum},
		error : function() {
			alert("Connection error");
		},
		success:function(data){
			if(data == "1"){
				alert("仅支持一家公司提交核保！");
				return ;
			}else{
				$.ajax({
					url:'getProcessStatus',
					type:'post',
					data:{taskid:taskid,
						  provid:button1.id},
					error : function() {
						alert("Connection error");
					},
					success:function(data){
						if(data == "0"){
							$.ajax({
								url:'policySubmit',
								type:'post',
								data:getPolicyParams(taskid,button1.id,agentnum),
								error : function() {
									alert("Connection error");
								},
								success:function(data){
									if(data == "success"){
										alert("提交投保成功！");
									}else{
										alert("提交投保失败！");
									}
								}
							});
						}else{
							alert("该流程不在选择投保节点，不能投保 ");
						}
					}
					
				});
			}
		}
	});
}

function getPolicyParams(taskid,inscomcode,agentnum){
	return {
		processInstanceId : taskid,
		inscomcode : inscomcode,
		agentnum : agentnum,
		totalpaymentamount : $("#total"+inscomcode).val()
	}
}

function getplant(taskid){
	$("#mymodal1").modal('show');
	$("#table-getplant").bootstrapTable({
		method: 'get',
		url: "getplant",
		cache: false,
		striped: true,
		pagination: true,
		queryParams : {taskid : taskid},
		sidePagination: 'server', 
		clickToSelect: true,
		columns: [{
			field: 'kindname',
			title: '品牌型号',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: true
		},{
			field: 'amount',
			title: '保额',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: true
		},{
			field: 'prem',
			title: '保费',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: true
		}]
	});
}

function createconfigtabletr(inscomcode,item){
	var html = "";
		html += '<tr style="background-color:#383838;color:white;"><td colspan="3"><font size="3">保险配置</font></td></tr>';
		html += '<tr style="background-color:#8A8A8A;color:white;"><td class="col-md-4">商业险</td><td class="col-md-4">保额（及其他信息）</td><td class="col-md-4">保费</td></tr>';
		//商业险
		$.each(item.buessList, function(j, sitem) {
			html +='<tr><td>'+sitem.riskname+'</td><td>';
			if (sitem.riskname=="修理期间费用补偿险") {
				if (sitem.amount==1) {
					html +='投保</td><td>';
				}else{
					if (sitem.amount==0) {
						html +='不投保</td><td>';
					} else {
						html +=sitem.amount+'元/天</td><td>';
					}
				}
			}else{
				if (sitem.amount==1) {
					html +='投保</td><td>';
				}else{
					if (sitem.amount==0) {
						html +='不投保</td><td>';
					} else {
						html +=sitem.amount+'</td><td>';
					}
				}
			}
			if(sitem.discountCharge==null){
				html +='报价中';
			}else{
				html +=sitem.discountCharge;
			};
			html +='</td></tr>';
		});
		html += '<tr style="background-color:#8A8A8A;color:white;"><td>不计免赔</td><td>保额（及其他信息）</td><td>保费</td></tr>';
		//不计免赔
		$.each(item.bjmpList, function(j, sitem) {
			html +='<tr><td>'+sitem.riskname+'</td><td>'+sitem.amount+'</td><td>';
			if(sitem.discountCharge==null){
				html +='报价中';
			}else{
				html +=sitem.discountCharge;
			};
			html +='</td></tr>';
		});
		html += '<tr style="background-color:#8A8A8A;color:white;"><td>交强险和车船税</td><td>保额（及其他信息）</td><td>保费</td></tr>';
		//交强险和车船税
		$.each(item.vehicleList, function(j, sitem) {
			html +='<tr><td>'+sitem.riskname+'</td><td>'+sitem.amount+'</td><td>';
			if(sitem.discountCharge==null){
				html +='报价中';
			}else{
				html +=sitem.discountCharge;
			};
			html +='</td></tr>';
		});
		html += '<tr><td class="bgg" style="text-align:left;">商业险折扣率</td><td>--</td><td>'+item.discountRate+'</td></tr>';
		html += '<tr><td class="bgg" style="text-align:left;">交强险折扣率</td><td>--</td><td>'+item.vehicleDiscountRate+'</td></tr>';
	$("#configtable"+inscomcode).append(html);
	var html1 = "";
	var sum=0;
	   html1 +='<tr><td >商业险保费:'+item.totalamountprice.toFixed(2)+'元&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>';
	   $.each(item.vehicleList, function(j, sitem) {
			html1 +='<td>'+sitem.riskname+'保费:'
			if(sitem.discountCharge==null){
				html1 +='0&nbsp;';
			}else{
				html1 +=sitem.discountCharge;
				sum=sum+sitem.discountCharge;
			};
			html1 +='元&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>';
		});
	   sum=sum+item.totalamountprice;
	   html1 +='</tr>';
	   html1 +='<tr><td  colspan="3"><font style="color:red;">保费总额:'+sum.toFixed(2)+'元</font><input type="hidden" id="total' + inscomcode + '" value="' + sum.toFixed(2) + '"/></td></tr>';
	   var status;
	   var yesorno = "不";
	   if(item.processstatus == "0"){
		   status = "报价成功";
		   yesorno = "";
	   }else if(item.processstatus == "1"){
		   status = "报价中";
	   }else if(item.processstatus == "2"){
		   status = "核保中";
	   }else if(item.processstatus == "3"){
		   status = "退回修改";
	   }else if(item.processstatus == "4"){
		   status = "核保退回";
	   }else if(item.processstatus == "5"){
		   status = "核保成功";
	   }else if(item.processstatus == "6"){
		   status = "支付确认中";
	   }else if(item.processstatus == "7"){
		   status = "支付成功";
	   }else if(item.processstatus == "8"){
		   status = "承保完成";
	   }else if(item.processstatus == "9"){
		   status = "拒绝承保";
	   }else if(item.processstatus == "10"){
		   status = "放弃承保";
	   }else if(item.processstatus == "11"){
		   status = "暂停支付";
	   }else if(item.processstatus == "12"){
		   status = "结束状态";
	   }
	   html1 += '<tr><td colspan="3">流程状态:目前流程正处于' + status + ',' + yesorno + '可以投保</td></tr>'
	$("#configtable1"+inscomcode).append(html1);
}

function getlipei(taskid){
 	$("#mymodallipei").modal(); 
 	$.ajax({
		url:'claimNumber',
		type:'post',
		data:{taskid:taskid},
		error : function() {
			alert("Connection error");
		},
		success:function(data){
			var json=JSON.parse(data);
			$("#claimrate").val(json.claimrate);
			$("#claimtimes").val(json.claimtimes);
			$("#firstinsuretype").val(json.firstinsuretype);
			$("#jqclaimrate").val(json.jqclaimrate);
			$("#jqclaimtimes").val(json.jqclaimtimes);
			$("#jqlastclaimsum").val(json.jqlastclaimsum);
			$("#lastclaimsum").val(json.lastclaimsum);
			$("#trafficoffence").val(json.trafficoffence);
			$("#trafficoffencediscount").val(json.trafficoffencediscount);
		}
		
	})
}
