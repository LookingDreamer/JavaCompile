require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public"], function ($) {
    $(function() {
	    var timeNum=null;
		$(document).on("click",".cx_queryordresdig",function(){
			clearTimeout(timeNum);
			$('#showqueryordresult').modal("hide");
			timeNum=setTimeout(function(){
				$("body").addClass("modal-open");
			},500)
		});
		$(document).on("click",".cx_fqqueryordresdig",function(){
			clearTimeout(timeNum);
			$('#fqshowqueryordresult').modal("hide");
			timeNum=setTimeout(function(){
				$("body").addClass("modal-open");
			},500)
		});
    });
});

function strToDate (dateStr){
	return dateStr.substr(0,4) + "-" + dateStr.substr(4,2) + "-" + dateStr.substr(6,2) + " " + dateStr.substr(8,2) + ":" + dateStr.substr(10,2) + ":" + dateStr.substr(12,2);
}

function showqueryorderresult(commonrequrl,bizTransId, orderno, channelId, certNo){
	//银联快捷支付url，channelId = 30
	var reqUrl = '/queryResultFromChannelByBizTransId?bizTransId=' + bizTransId;
	if(channelId == 28){
		// 分期保接口url, orderno 订单号
		reqUrl = '/secondPay/zhongan/queryResult?bizId=' + orderno + '&certNo=' + certNo; 
	}

	$.ajax({
          url: commonrequrl + '/restful/paymentService' + reqUrl,
          type: 'GET',
          contentType: "application/json",
          cache: false,
          async: true,
          error: function () {
            alertmsg("Connection error!!");
          },
          success: function (data) {
	          if(data != null){
				  //code="0000"查询成功
				  if(data.code == "0000"){
					  var  obj = data;
		          	if(channelId == 30){
						if(data.channelId == "unionpay"){
							$("#tx_yl_channelId").text("银联支付");
						}
						if(data.orderState == "02"){
							$("#tx_yl_orderState").text("支付成功");
						} else {
							$("#tx_yl_orderState").text("未支付成功(状态:" + data.orderState  + ")");
						}
						
						$("#tx_yl_payType").text(data.payType);
						$("#tx_yl_bizTransactionId").text(data.bizTransactionId);
						
						if(data.amount!= null && data.amount != ""){
							$("#tx_yl_amount").text(parseInt(data.amount) * 0.01);
						}

						$("#tx_yl_code").text(data.code);
						$("#tx_yl_paySerialNo").text(data.paySerialNo);
						
						if(data.transDate != null && data.transDate != ""){
							$("#tx_yl_transDate").text(strToDate(data.transDate));
						}
						
						$("#tx_yl_bizId").text(data.bizId);
						if(data.channelParam.referNo != null && data.channelParam.referNo != ""){
							$("#tx_yl_referNo").text(data.channelParam.referNo);
						}
						if(data.channelParam.cardNo != null && data.channelParam.cardNo != ""){
							$("#tx_yl_cardNo").text(data.channelParam.cardNo);
						}
						$("#tx_yl_description").text(data.description);
						
						$("#showqueryordresult").modal();
		
					} else if(channelId == 28){
						$("#tx_fq_transDate").text(data.transDate);
						if(data.amount!= null && data.amount != ""){
							$("#tx_fq_amount").text(parseInt(data.amount) * 0.01);
						}
						
						if(data.orderState == "02"){
							$("#tx_fq_orderState").text("支付成功");
						} else {
							$("#tx_fq_orderState").text("未支付成功(状态码:" + data.orderState  + ")");
						}
						
						//orderStatus 1 支付中, 5 支付成功, 8 支付过期或失败, 9 撤单, 11 借款中, 12 借款成功, 13 借款失败, 14 放款成功, 15 放款失败
						var statusTx = "无状态信息";
						switch(data.channelParam.orderStatus){
							case "1": statusTx="支付中"; break;
							case "5": statusTx="支付成功"; break;
							case "8": statusTx="支付过期或失败"; break;
							case "9": statusTx="撤单"; break;
							case "11": statusTx="借款中"; break;
							case "12": statusTx="借款成功"; break;
							case "13": statusTx="借款失败"; break;
							case "14": statusTx="放款成功"; break;
							case "15": statusTx="放款失败"; break;
						}
						$("#tx_fq_orderStatus").text(statusTx + " (状态码:" + data.channelParam.orderStatus + ")");
						
						//fundStatus
						statusTx = "无状态信息";
						var fdstatus = data.channelParam.fundStatus;
						if(fdstatus != null && fdstatus != ""){
							switch(fdstatus){
								case "1": statusTx="支付中"; break;
								case "5": statusTx="支付成功"; break;
								case "8": statusTx="支付过期或失败"; break;
								case "9": statusTx="撤单"; break;
								case "11": statusTx="借款中"; break;
								case "12": statusTx="借款成功"; break;
								case "13": statusTx="借款失败"; break;
								case "14": statusTx="放款成功"; break;
								case "15": statusTx="放款失败"; break;
							}
						}
						$("#tx_fq_fundStatus").text(statusTx + " (状态码:" + data.channelParam.fundStatus + ")");
						
						$("#tx_fq_fundAmount").text(data.channelParam.fundAmount);
						$("#tx_fq_fundSuccDate").text(data.channelParam.fundSuccDate);
						$("#tx_fq_innerPayNo").text(data.channelParam.innerPayNo);
						$("#tx_fq_partnerFee").text(data.channelParam.partnerFee);
						$("#tx_fq_partnerOrderNo").text(data.channelParam.partnerOrderNo);
						$("#tx_fq_partnerPayNo").text(data.channelParam.partnerPayNo);
						$("#tx_fq_payAmount").text(data.channelParam.payAmount);
						$("#tx_fq_payNo").text(data.channelParam.payNo);
						$("#tx_fq_paySuccDate").text(data.channelParam.paySuccDate);
				
						$("#fqshowqueryordresult").modal();
					}
				} else {
					  alertmsg("返回码: " + data.code + "; 返回信息: " + data.msg + ", 请核对！");
				}
			} else {
				alertmsg("查看订单结果失败!data=" + data);
			}
          }
     });
}