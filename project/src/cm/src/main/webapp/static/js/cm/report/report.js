require([ "jquery",  "flat-ui", "bootstrap-table", "bootstrapdatetimepicker","bootstrapdatetimepickeri18n",
		"bootstrap", "bootstrapTableZhCn","date","iscroll" ], function($) {
	$(function() {
		$("#report_frame").height($(window.top.document.body).find("#appcontent").height() - 35 - $(".container-fluid").height());
	});
	$('.form_datetime').datetimepicker({
	      language: 'zh-CN',
	      format: "yyyy-mm-dd",
	      weekStart: 1,
	      todayBtn: 1,
	      autoclose: 1,
	      todayHighlight: 1,
	      startView: 2,
	      forceParse: 0,
	      minView: 2,
	      pickerPosition: "bottom-left"
	      // showMeridian: 1
	    });
})

function operateFormatter(value, row, index) {
    return [
		'<button id="selectorderlist" class="btn btn-default" name="selectorderlist">选择</button>'
    ].join('');
}

window.operateEvents = {
	    'click #selectcarmodel': function (e, value, row, index) {
	    }
	}

function getQueryPara0(params){
	orderListVo = new Object();
	return {
		offset:params.offset,
	    limit:params.limit,
	    orderstatus:"0"
	}
}

function getQueryPara1(params){
	return {
		offset:params.offset,
		limit:params.limit,
		orderstatus:"1"
	}
}

function getQueryPara2(params){
	return {
		offset:params.offset,
		limit:params.limit,
		orderstatus:"2"
	}
}