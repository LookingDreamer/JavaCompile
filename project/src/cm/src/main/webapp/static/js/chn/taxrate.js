require([ "jquery", "zTree", "zTreecheck", "flat-ui", "bootstrap-table", "bootstrapdatetimepicker", "bootstrapdatetimepickeri18n",
    "bootstrap", "bootstrapTableZhCn","public" ], function ($) {
    $(function () {
    	inittree();
    	initModeltree()

        $('#taxrate_list').bootstrapTable({
            method: 'post',
//            url: pageurl,
            queryParams: queryParams,
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server',
            pageSize: pagesize,
            pageList: [15, 30, 50, 100],
            clickToSelect: true,
            minimumCountColumns: 2,
            columns: [
                {
                    field: 'state',
                    align: 'center',
                    valign: 'middle',
                    checkbox: true
                },
                {
                    field: 'id',
                    title: 'id',
                    visible: false,
                    switchable: false
                },
                {
                	field: 'channelid',
                	title: 'channelid',
                	visible: false,
                	switchable: false
                },
                {
                	field: 'status',
                	title: 'status',
                	visible: false,
                	switchable: false
                },
                {
                	field: 'channeleffectivetime',
                	title: 'channeleffectivetime',
                	visible: false,
                	switchable: false
                },
                {
                	field: 'channelterminaltime',
                	title: 'channelterminaltime',
                	visible: false,
                	switchable: false
                },
                {
                    field: 'createtime',
                    title: '创建时间',
                    align: 'center',
                    valign: 'middle',
                    sortable: false
                },
                {
                	field: 'channelname',
                    title: '渠道',
                    align: 'center',
                    valign: 'middle',
                    sortable: false
                },
                {
                    field: 'taxrate',
                    title: '税率',
                    align: 'center',
                    valign: 'middle',
                    sortable: false
                },
               
                {
                    field: 'effectivetime',
                    title: '生效时间',
                    align: 'center',
                    valign: 'middle',
                    sortable: false
                },
                {
                	field: 'terminaltime',
                	title: '失效时间',
                	align: 'center',
                	valign: 'middle',
                	sortable: false
                },
                {
                    field: 'statusname',
                    title: '状态',
                    align: 'center',
                    valign: 'middle',
                    sortable: false
                },
                {
                    field: 'operating',
                    title: '操作',
                    align: 'center',
                    valign: 'middle',
                    switchable: false,
                    formatter: operateFormatter1,
                    events: operateEvents1
                }
            ]
        });


        $("#effectivedate").datetimepicker({
            language: 'zh-CN',
            format: "yyyy-mm-dd",
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            forceParse: 0,
            minView: 2,
            pickerPosition: "bottom-left",
            pickTime: true,
            todayBtn: true,
            pickDate: true,
            autoclose: true,
            startDate: new Date()
        });
        $("#xeffectivedate").datetimepicker({
        	language: 'zh-CN',
        	format: "yyyy-mm-dd",
        	weekStart: 1,
        	todayBtn: 1,
        	autoclose: 1,
        	todayHighlight: 1,
        	startView: 2,
        	forceParse: 0,
        	minView: 2,
        	pickerPosition: "bottom-left",
        	pickTime: true,
        	todayBtn: true,
        	pickDate: true,
        	autoclose: true,
        	startDate: new Date()
        });

        $("#terminaldate").datetimepicker({
            language: 'zh-CN',
            format: "yyyy-mm-dd",
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            forceParse: 0,
            minView: 2,
            pickerPosition: "bottom-left",
            pickTime: true,
            todayBtn: true,
            pickDate: true,
            autoclose: true,
            startDate: new Date()
        });
        $("#xterminaldate").datetimepicker({
        	language: 'zh-CN',
        	format: "yyyy-mm-dd",
        	weekStart: 1,
        	todayBtn: 1,
        	autoclose: 1,
        	todayHighlight: 1,
        	startView: 2,
        	forceParse: 0,
        	minView: 2,
        	pickerPosition: "bottom-left",
        	pickTime: true,
        	todayBtn: true,
        	pickDate: true,
        	autoclose: true,
        	startDate: new Date()
        });
        $("#cterminaldate").datetimepicker({
        	language: 'zh-CN',
        	format: "yyyy-mm-dd",
        	weekStart: 1,
        	todayBtn: 1,
        	autoclose: 1,
        	todayHighlight: 1,
        	startView: 2,
        	forceParse: 0,
        	minView: 2,
        	pickerPosition: "bottom-left",
        	pickTime: true,
        	todayBtn: true,
        	pickDate: true,
        	autoclose: true,
        	startDate: new Date()
        });
        // 查询
		$("#querybt").on("click",function(){
			query();
			
		});
		//新建功能
		$('#addbt').on('click', function(){
			$('#tableAddTaxModel').show();
			$('#execAddButton').show();
			$('#execCopyButton').hide();
			$('#taxrate_list').bootstrapTable('refresh',{url:'getTarrateList?channelcode=' + $('#channelcode').val()});
		});
//		关闭按钮
		$('#closebttax').on('click', function(){
			$('#tablemode').hide();
		});
		$('#defultclose').on('click', function(){
			$('#tablemode').hide();
		});
		$('#xdefultclose').on('click', function(){
			$('#tableAddTaxModel').hide();
		});
		$("#confirmDateClose").on("click", function (e) {
            $("#set-terminaldate-tr").show();
        });
//		保存按钮更新
		$('#savebutton').on('click', function(){
			var id = $("#id").val();
			var channelid = $("#channelid").val();
		    if (id != "") {
		        var effectivedate = $("#effectivedate").val();
		        var terminaldate = $("#terminaldate").val();
		        var taxrate = $("#taxrate").val();
		        var status = $('#xstatus').val();
		        if(effectivedate==''){
		            alertmsg("生效时间不能为空！")
		            return;
		        }
		        if(taxrate==''){
		            alertmsg("税率不能为空！")
		            return;
		        }
		        if(!/^[0-9]+\.{0,1}[0-9]{0,4}$/.test(taxrate)){
		            alertmsg("税率必须为数字类型！");
		            return false;
		        }
		        
		        var arr = effectivedate.split("-");
		        var effectivetime = new Date(arr[0], parseInt(arr[1]) - 1, arr[2]);
		        if(terminaldate != ''){
		            var arr2 = terminaldate.split("-");
		            var terminaltime = new Date(arr2[0], parseInt(arr2[1]) - 1, arr2[2],'23','59','59');
		            var terminalTime2 = terminaltime.format('yyyy-MM-dd hh:mm:ss');
		            var currtime = new Date().format('yyyy-MM-dd hh:mm:ss');
		            if(terminalTime2 < currtime){
		                return alertmsg('失效时间设置有误，请重新核对！');
		            }
		            if(effectivedate > terminaldate){
		                return alertmsg('失效时间需大于生效时间！');
		            }

		            $.ajax({
		           	 url: 'saveOrUpdateTaxrate',
		           	 type: 'get',
		           	 dataType: "json",
		           	 data:  {
		           		id : id,
		           		effectivetime : effectivetime,
		           		terminaltime : terminaltime,
		           		taxrate : taxrate,
		           		channelid : channelid,
		           		status : status
		           	 },
		           	 async: true,
		           	 error: function () {
		           		 alertmsg("Connection error");
		           	 },
		           	 success: function (data) {
		           		 if (data.success) {
		           			query();
		           			 alertmsg(data.result);
		           		 }
		           		 else
		           			 alertmsg(data.result)
		           	 }
		            });
		        }else {

		        	 $.ajax({
		            	 url: 'saveOrUpdateTaxrate',
		            	 type: 'get',
		            	 dataType: "json",
		            	 data:  {id : id,
			           		effectivetime : effectivetime,
			           		terminaltime : terminaltime,
			           		taxrate : taxrate,
			           		status : status,
			           		channelid : channelid},
		            	 async: true,
		            	 error: function () {
		            		 alertmsg("Connection error");
		            	 },
		            	 success: function (data) {
		            		 if (data.success) {
		            			 query();
		            			 alertmsg(data.result);
		            		 }
		            		 else
		            			 alertmsg(data.result)
		            	 }
		             });
		        }
		   
		    	
		    	$('#tablemode').hide();
		    	$('#addOrUpdateForm')[0].reset();
		    	$('#channelcode').val(channelid);
		    }
		});
//		单个启动按钮
		$('#usingbutton').on('click', function(){
			var id = $("#id").val();
			var channelid = $("#channelid").val();
			if (id != "") {
				var effectivedate = $("#effectivedate").val();
				var terminaldate = $("#terminaldate").val();
				var taxrate = $("#taxrate").val();
				var status = $('#xstatus').val();
				if(effectivedate==''){
					alertmsg("生效时间不能为空！")
					return;
				}
				if(taxrate==''){
					alertmsg("税率不能为空！")
					return;
				}
				if(!/^[0-9]+\.{0,1}[0-9]{0,4}$/.test(taxrate)){
					alertmsg("税率必须为数字类型！");
					return false;
				}
				if(status != '3') {
					alertmsg("待审核的税率才能被开启，请重新审核");
					return false;
				}
				var arr = effectivedate.split("-");
				var effectivetime = new Date(arr[0], parseInt(arr[1]) - 1, arr[2]);
				if(terminaldate != ''){
					var arr2 = terminaldate.split("-");
					var terminaltime = new Date(arr2[0], parseInt(arr2[1]) - 1, arr2[2],'23','59','59');
					var terminalTime2 = terminaltime.format('yyyy-MM-dd hh:mm:ss');
					var currtime = new Date().format('yyyy-MM-dd hh:mm:ss');
					if(terminalTime2 < currtime){
						return alertmsg('失效时间设置有误，请重新核对！');
					}
					if(effectivedate > terminaldate){
						return alertmsg('失效时间需大于生效时间！');
					}
					
					$.ajax({
						url: 'saveOrUpdateTaxrate',
						type: 'get',
						dataType: "json",
						data:  {
							id : id,
							effectivetime : effectivetime,
							terminaltime : terminaltime,
							taxrate : taxrate,
							channelid : channelid,
							status : '1'
						},
						async: true,
						error: function () {
							alertmsg("Connection error");
						},
						success: function (data) {
							if (data.success) {
								query();
								alertmsg(data.result);
							}
							else
								alertmsg(data.result)
						}
					});
				}else {
					
					$.ajax({
						url: 'saveOrUpdateTaxrate',
						type: 'get',
						dataType: "json",
						data:  {id : id,
							effectivetime : effectivetime,
							terminaltime : terminaltime,
							taxrate : taxrate,
							status : '1',
							channelid : channelid},
							async: true,
							error: function () {
								alertmsg("Connection error");
							},
							success: function (data) {
								if (data.success) {
									query();
									alertmsg(data.result);
								}
								else
									alertmsg(data.result)
							}
					});
				}
				
				
				$('#tablemode').hide();
				$('#addOrUpdateForm')[0].reset();
				$('#channelcode').val(channelid);
			}
		});
		$("#clear-terminaldate").on("click", function (e) {
            $("#terminaldate").val('');
        });
		$("#clear-terminaldate2").on("click", function (e) {
            $("#xterminaldate").val('');
        });
//		单个关闭按钮
		$('#closebutton').on('click', function(){
			var id = $("#id").val();
			var channelid = $("#channelid").val();
			if (id != "") {
				var effectivedate = $("#effectivedate").val();
				var terminaldate = $("#terminaldate").val();
				var taxrate = $("#taxrate").val();
				var status = $('#xstatus').val();
				if(effectivedate==''){
					alertmsg("生效时间不能为空！")
					return;
				}
				if(taxrate==''){
					alertmsg("税率不能为空！")
					return;
				}
				if(!/^[0-9]+\.{0,1}[0-9]{0,4}$/.test(taxrate)){
					alertmsg("税率必须为数字类型！");
					return false;
				}
				if(status != '1') {
					alertmsg("只有开启的税率才能被关闭，请重新审核");
					return false;
				}
				var arr = effectivedate.split("-");
				var effectivetime = new Date(arr[0], parseInt(arr[1]) - 1, arr[2]);
				if(terminaldate != ''){
					var arr2 = terminaldate.split("-");
					var terminaltime = new Date(arr2[0], parseInt(arr2[1]) - 1, arr2[2],'23','59','59');
					var terminalTime2 = terminaltime.format('yyyy-MM-dd hh:mm:ss');
					var currtime = new Date().format('yyyy-MM-dd hh:mm:ss');
					if(terminalTime2 < currtime){
						return alertmsg('失效时间设置有误，请重新核对！');
					}
					if(effectivedate > terminaldate){
						return alertmsg('失效时间需大于生效时间！');
					}
					
					$.ajax({
						url: 'saveOrUpdateTaxrate',
						type: 'get',
						dataType: "json",
						data:  {
							id : id,
							effectivetime : effectivetime,
							terminaltime : terminaltime,
							taxrate : taxrate,
							channelid : channelid,
							status : '2'
						},
						async: true,
						error: function () {
							alertmsg("Connection error");
						},
						success: function (data) {
							if (data.success) {
								query();
								alertmsg(data.result);
							}
							else
								alertmsg(data.result)
						}
					});
				}else {
					
					$.ajax({
						url: 'saveOrUpdateTaxrate',
						type: 'get',
						dataType: "json",
						data:  {id : id,
							effectivetime : effectivetime,
							terminaltime : terminaltime,
							taxrate : taxrate,
							status : '2',
							channelid : channelid},
							async: true,
							error: function () {
								alertmsg("Connection error");
							},
							success: function (data) {
								if (data.success) {
									query();
									alertmsg(data.result);
								}
								else
									alertmsg(data.result)
							}
					});
				}
				
				
				$('#tablemode').hide();
				$('#addOrUpdateForm')[0].reset();
				$('#channelcode').val(channelid);
			}
		});
//		新增保存按钮
		$('#xsavebutton').on('click', function(){
			var channelids = $('#channelids').val();
			var effectivedate = $("#xeffectivedate").val();
			var terminaldate = $("#xterminaldate").val();
			var taxrate = $("#xtaxrate").val();
			if(effectivedate==''){
				alertmsg("生效时间不能为空！")
				return;
			}
			if(taxrate==''){
				alertmsg("税率不能为空！")
				return;
			}
			if(!/^[0-9]+\.{0,1}[0-9]{0,4}$/.test(taxrate)){
				alertmsg("税率必须为数字类型！");
				return false;
			}
			var currtime = new Date().format('yyyy-MM-dd hh:mm:ss');
			var arr = effectivedate.split("-");
			var effectivetime = new Date(arr[0], parseInt(arr[1]) - 1, arr[2]);
			if(terminaldate != ''){
				var arr2 = terminaldate.split("-");
				var terminaltime = new Date(arr2[0], parseInt(arr2[1]) - 1, arr2[2],'23','59','59');
				var terminalTime2 = terminaltime.format('yyyy-MM-dd hh:mm:ss');
				
				if(terminalTime2 < currtime){
					return alertmsg('失效时间设置有误，请重新核对！');
				}
				if(effectivedate > terminaldate){
					return alertmsg('失效时间需大于生效时间！');
				}
				
				$.ajax({
					url: 'saveOrUpdateTaxrate',
					type: 'get',
					dataType: "json",
					data:  {
						effectivetime : effectivetime,
						terminaltime : terminaltime,
						taxrate : taxrate,
						createtime : new Date(),
						channelids : channelids
					},
					async: true,
					error: function () {
						alertmsg("Connection error");
					},
					success: function (data) {
						if (data != null) {
							
							queryTaxrateBychannelids(currtime);
							alertmsg("操作成功！");
						}
						else
							alertmsg("操作失败！")
					}
				});
			}else {
				
				$.ajax({
					url: 'saveOrUpdateTaxrate',
					type: 'get',
					dataType: "json",
					data:  {
						effectivetime : effectivetime,
						terminaltime : terminaltime,
						taxrate : taxrate,
						createtime : new Date(),
						channelids : channelids
						},
						async: true, 
						error: function () {
							alertmsg("Connection error");
						},
						success: function (data) {
							if (data != null) {
								queryTaxrateBychannelids(currtime);
								alertmsg("操作成功！");
							}
							else
								alertmsg("操作失败！")
						}
				});
			}
			
			
			$('#tableAddTaxModel').hide();
			$('#execAddButton').hide();
			$('#xaddOrUpdateForm')[0].reset();
			
			
		});
//		批量启用
		$('#usingbt').on('click', function() {
			var selectData = $('#taxrate_list').bootstrapTable('getSelections');
            if(selectData.length != 1){
                alertmsg("请选择数据，并且一个渠道只能开启一条数据！");
                return false;
            }
            var arrterminal;
            var tterminalTime;
            var currtime = new Date().format('yyyy-MM-dd hh:mm:ss');
            var id = "";
            for(var i=0;i<selectData.length;i++){
            	id = selectData[i].id;
                if(selectData[i].statusname != '待审核'){
                    alertmsg("只有待审核的佣金系数才允许启用，请重新核对！");
                    return false;
                }
                if(selectData[i].terminaltime !='' && selectData[i].terminaltime != undefined) {
                    arrterminal = selectData[i].terminaltime.split("-");
                    tterminalTime = new Date(arrterminal[0], parseInt(arrterminal[1]) - 1, arrterminal[2], '23', '59', '59').format('yyyy-MM-dd hh:mm:ss');
                    if (tterminalTime < currtime) {
                        return alertmsg('失效时间设置有误，请重新核对！');
                    }
                    if(selectData[i].effectivetime > selectData[i].terminaltime){
                        return alertmsg('失效时间需大于生效时间！');
                    }
                }
            }
            var arrayrateid = getSelectedRowsId();
            $.ajax({
                url : 'batchUpdateTaxrateStatus?status=1&id=' + id,
                type : 'GET',
                dataType : "json",
                async : true,
                error : function() {
                    alertmsg("Connection error");
                },
                success : function(data) {
                	if (data.success) {
                		query();
	           			 alertmsg(data.result);
	           		 }
	           		 else
	           			 alertmsg(data.result)
                }
            });
		});
//		批量关闭
		$('#closebt').on('click', function() {
			var selectData = $('#taxrate_list').bootstrapTable('getSelections');
			var id = "";
			var channelterminaltime = "";
			if(selectData.length==0){
				alertmsg("请选中操作数据！");
				return false;
			}
			for(var i=0;i<selectData.length;i++){
				if(selectData[i].statusname != '已启用'){
					alertmsg("只有已启用的佣金系数才允许关闭，请重新核对！");
					return false;
				}
				id = selectData[i].id;
				channelterminaltime = selectData[i].channelterminaltime;
			}
			$("#taxrateId").val(id);
			$("#cterminaldate").val(channelterminaltime);
			var arrayrateid = getSelectedRowsId();
			$("#colseCommissionRateModel").modal('show');
			
		});
		 $("#cexecButton").on("click", function (e) {
			var operate = $('input[name="operatetype"]:checked').val();
           
            var terminaldate = $("#cterminaldate").val();
            var id = $("#taxrateId").val();
            var terminalTime;
            if(terminaldate!=''){
                var arrterminal = terminaldate.split("-");
                terminalTime = new Date(arrterminal[0], parseInt(arrterminal[1]) - 1, arrterminal[2],'23','59','59');
            }
            if(operate == '1' ){
            	 
            	closeTaxrate(id);
            }else if(operate == '2'  ){
                if(terminaldate == ''){
                    return alertmsg('失效时间不能为空！');
                }
                closeTaxrate(id,terminalTime);
            }
            
            
            $("#colseCommissionRateModel").modal('hide');
		 });
//		批量删除
		$('#deletebt').on('click', function() {
			var selectData = $('#taxrate_list').bootstrapTable('getSelections');
			if(selectData.length==0){
				alertmsg("请选中操作数据！");
				return false;
			}
			for(var i=0;i<selectData.length;i++){
				if(selectData[i].statusname == '已启用'){
					alertmsg("已启用的税率不能删除，请重新核对！");
					return false;
				}
			}
			var arrayrateid = getSelectedRowsId();
			$.ajax({
				url : 'deleteTaxrate?ids=' + JSON.stringify(arrayrateid),
				type : 'GET',
				dataType : "json",
				async : true,
				error : function() {
					alertmsg("Connection error");
				},
				success : function(data) {
					if (data.success) {
						query();
						alertmsg(data.result);
					}
					else
						alertmsg(data.result)
				}
			});
		});
		 
		$('#copybt').on('click', function() {
			 var arraytaxid = getSelectedRowsId();
			 $('#execCopyButton').show();
			 $('#execAddButton').hide();
			 
			 if(arraytaxid.length == 0) {
				 alertmsg("请选择至少一条数据！")
				 return false;
			 }
//			 initModeltree();
			$('#copyTaxrateModel').modal('show');
		});
		$('#addChannelBt').on('click', function() {
			$('#copyTaxrateModel').modal('show');
		});
		/**
		 * 状态变更查询
		 */
		$('#status').on('change', function(){
			query();
		});
//		点击确定按钮
		$("#execAddButton").on("click",function() {
            // 拿到所有选中菜单ids
            var treeObj = $.fn.zTree.getZTreeObj('deptModalTree');
            var nodes = treeObj.getCheckedNodes(true);
            var checkedIds = '';
            var checkedNames = '';
            var channelnames = "";
            var channelArray = new Array();
            var index = 1;
            for (var i = 0; i < nodes.length; i++) {
                if(nodes[i].channelid != ''){
                    checkedIds += nodes[i].innercode + ',';
                    checkedNames += (index) +':'+ nodes[i].name +' ('+ nodes[i].innercode+') <br> ';
                    index +=1;
                    channelnames +=nodes[i].name + ',';
                }
            }
            if (checkedIds.length > 0) {
                checkedIds = checkedIds.substring(0, checkedIds.length - 1);
                checkedNames = checkedNames.substring(0, checkedNames.length - 1);
                channelnames = channelnames.substring(0, channelnames.length - 1);
                
            }else{
                alertmsg('请至少选择一家渠道新增！');
                return ;
            }
//            var arrayratioid = getSelectedRowsId();
            confirmmsg('是否配置到如下渠道： '+'<br>'+checkedNames,function(e){
            },function(e){

            });
            $("#addChannelBt").val(channelnames);
            $("#channelids").val(checkedIds);
            $('#copyTaxrateModel').modal('hide');
            treeObj.checkAllNodes(false);

        });
		/**
		 * 批量复制税率按钮
		 */
		$("#execCopyButton").on("click",function() {
			// 拿到所有选中菜单ids
			var treeObj = $.fn.zTree.getZTreeObj('deptModalTree');
			var nodes = treeObj.getCheckedNodes(true);
			var checkedIds = '';
			var checkedNames = '';
			var channelnames = "";
			var index = 1;
			for (var i = 0; i < nodes.length; i++) {
				if(nodes[i].channelid != ''){
					checkedIds += nodes[i].innercode + ',';
					checkedNames += (index) +':'+ nodes[i].name +' ('+ nodes[i].innercode+') <br> ';
					index +=1;
				}
			}
			if (checkedIds.length > 0) {
				checkedIds = checkedIds.substring(0, checkedIds.length - 1);
				checkedNames = checkedNames.substring(0, checkedNames.length - 1);
			}else{
				alertmsg('请至少选择一家渠道复制！');
				return ;
			}
			var arrayratioid = getSelectedRowsId();
			confirmmsg('是否复制到如下渠道： '+'<br>'+checkedNames,function(e){
				
				batchCopyTaxrate(arrayratioid.toString(),checkedIds);
			},function(e){
				
			});
			$('#execCopyButton').hide();
			treeObj.checkAllNodes(false);
		});
    });
});
function closeTaxrate(id,terminalTime){
	$.ajax({
        url : 'batchUpdateTaxrateStatus?status=2',
        type : 'GET',
        data:{
        	id : id,
        	terminaltime : terminalTime
        },
        dataType : "json",
        async : true,
        error : function() {
            alertmsg("Connection error");
        },
        success : function(data) {
        	if (data.success) {
        		query();
       			 alertmsg(data.result);
       		 }
       		 else
       			 alertmsg(data.result)
        }
    });
}

function batchCopyTaxrate(arrayratioid,checkedIds) {
	var currtime = new Date();
	var currtimes = new Date().format('yyyy-MM-dd hh:mm:ss');
	$.ajax({
        url: 'batchCopyTaxrate',
        type: 'POST',
        dataType: "json",
        data: {
            ratioIds: arrayratioid,
            channelIds:checkedIds,
            createtime : currtime
        },
        async: true,
        error: function () {
            alertmsg("Connection error");
        },
        success: function (data) {
            $('#copyTaxrateModel').modal('hide');
            queryTaxrateTableBychannelids(currtimes); 
            alertmsg('复制成功！')
        }
    });
}
function inittree() {
    $.ajax({
        url: "initChanneTree",
        type: 'POST',
        dataType: "json",
        error: function () {
            $("#deptTree").html('加载失败，<a href="javascrpt:void(0);" onclick="inittree();">点击这里</a>重试。');
        },
        success: function (data) {
            $.fn.zTree.init($("#deptTree"), {
                data: {
                    simpleData: {
                        enable: true,
                        idKey: "id",
                        pIdKey: "pId"
                    }
                },
                view: {
                    fontCss: function (treeId, treeNode) {
                        return (!!treeNode.highlight) ? {color: "#A60000", "font-weight": "bold"} : {color: "#333", "font-weight": "normal"};
                    },
                    expandSpeed: ""
                },
                callback: {
                    onClick: getdatafromid
                }
            }, data);
        }
    });
}
function initModeltree() {
	$.ajax({
		url: "initChanneTree",
		type: 'POST',
		dataType: "json",
		error: function () {
			$("#deptModalTree").html('加载失败，<a href="javascrpt:void(0);" onclick="initModeltree();">点击这里</a>重试。');
		},
		success: function (data) {
			$.fn.zTree.init($("#deptModalTree"), {
				data: {
					simpleData: {
						enable: true,
						idKey: "id",
						pIdKey: "pId"
					}
				},
				 check : {
	                    enable : true
	             },
				view: {
					fontCss: function (treeId, treeNode) {
						return (!!treeNode.highlight) ? {color: "#A60000", "font-weight": "bold"} : {color: "#333", "font-weight": "normal"};
					},
					expandSpeed: ""
				},
				callback: {
				}
			}, data);
		}
	});
}
function queryParams(params) {
    var channelid = $("#channelid").val();
    return {
        channelid: channelid,
        offset: params.offset,
        limit: params.limit
    };
}
function query(){
    var channelid = $("#channelid").val();
    if(channelid == ""){
        return alertmsg("请选择渠道！");
    }
    var status = $("#status").val();//add by hwc 20161109
    var keyword = $("#keyword").val();//add by hwc 20161109
    queryTaxrateList(channelid,status,keyword);
}
function queryTaxrateList(channelid,status,keyword) {
    $.ajax({
        url: "getTarrateList",
        type: 'POST',
        dataType: "json",
        data: {
            channelid: channelid,
            status:status,
            keyword:keyword,
            limit: pagesize
        },
        async: true,
        error: function () {
            alertmsg("Connection error");
        },
        success: function (data) {
            $('#taxrate_list').bootstrapTable('load', data);
        }
    });
}
function getdatafromid(event, treeId, treeNode) {
    var channelid = treeNode.innercode;

    if (channelid != "") {
        $("#channelid").val(channelid);
        $("#channelname").val(treeNode.name);
        queryTaxrateList(channelid);
    }
}
/**
 * 操作形式
 * @param value
 * @param row
 * @param index
 * @returns
 */
function operateFormatter1(value, row, index) {
	return [ 
        '<a class="edit m-left-5" href="#ratio-set" name="edit" title="编辑">',
        '<i class="glyphicon glyphicon-edit"></i>', '</a>',
        '<a class="remove m-left-5" href="javascript:void(0)" title="删除">',
        '<i class="glyphicon glyphicon-remove"></i>', '</a>'
         ].join('');
}
/**
 * 表格绑定事件
 */
window.operateEvents1 = {
	'click .edit': function (e, value, row, index) {
		$("#id").val(row.id);
		$("#xxchannelid").val(row.channelid);
		$("#xstatus").val(row.status);
		$("#effectivedate").val(row.channeleffectivetime);
		$("#terminaldate").val(row.channelterminaltime);
		$("#taxrate").val(row.taxrate);
		$("#xchannelcode").val(row.channelid);
		$('#xchannelname').val(row.channelname);
        if(row.statusname == '已启用') {
        	$('#terminaldate').removeAttr("disabled");
        	$('#effectivedate').attr('disabled','disabled');
        	$('#taxrate').attr('readonly','readonly');
        }else if(row.statusname == '已关闭') {
        	$('#effectivedate').attr('disabled','disabled');
        	$('#terminaldate').attr('disabled','disabled');
        	$('#taxrate').attr('readonly','readonly');
        }else {
        	$('#effectivedate').removeAttr("disabled");
        	$('#terminaldate').removeAttr("disabled");
        	$('#taxrate').removeAttr("readonly");
        }
        $('#tablemode').show();
    },
    'click .remove': function (e, value, row, index) {
    	if(row.statusname=='已启用') {
    		alertmsg("已启用的税率不能被删除");
    		return false;
    	}
        if (confirm("确认删除吗?")) {
            deleteTaxrate(row.id)
        }
    }
}
/**
 * 编辑税率，
 * @param row
 * @returns
 */
function editTaxrate(row) {
	if(row.id != null && row.id == "") {
		$("#id").val(row.id);
		$("#channelid").val(row.channelid);
		$("#xstatus").val(row.status);
		$("#effectivedate").val(row.channeleffectivetime);
		$("#terminaldate").val(row.terminaltime);
		$("#taxrate").val(row.taxrate);
		$("#xchannelcode").val(row.channelcode);
		
	}
}
/**
 * 根据id，删除税率
 * @param id
 * @returns
 */
function deleteTaxrate(id) {
	var arrayrateid = new Array();
	arrayrateid.push(id);
	if (id != "" ) {
        $.ajax({
            url: "deleteTaxrate?ids="+JSON.stringify(arrayrateid),
            type: 'get',
            dataType: "json",
            async: true,
            error: function () {
                alertmsg("Connection error");
            },
            success: function (data) {
                if (data.success) {
                	query();
                    alertmsg("删除成功！");
                }
                else
                    alertmsg(data.result);
            }
        });
    }
}
/**
 * 查询税率
 * @param channelid
 * @returns
 */
function queryTaxrate(channelid){
//    var channelid = $("#xchannelcode").val();
    if(channelid == ""){
        return alertmsg("请选择渠道！");
    }
    var status = $("#status").val();//add by hwc 20161109
    var keyword = $("#keyword").val();//add by hwc 20161109
   
    queryTaxrateTable(channelid,status,keyword);
}
function queryTaxrateBychannelids(currtime){

	var status = $("#status").val();//add by hwc 20161109
	var keyword = $("#keyword").val();//add by hwc 20161109
	
	queryTaxrateTableBychannelids(currtime);
}
function queryTaxrateTable(channelid,status,keyword) {
    $.ajax({
        url: "getTarrateList",
        type: 'post',
        dataType: "json",
        data: {
            channelid: channelid,
            status:status,
            keyword:keyword,
            limit: pagesize
        },
        async: true,
        error: function () {
            alertmsg("Connection error");
        },
        success: function (data) {
            $('#taxrate_list').bootstrapTable('load', data);
        }
    });
}
/**
 * 直接set当前时间，执行这个回调函数
 * @param currtime
 * @returns
 */
function queryTaxrateTableBychannelids(currtime) {
	$.ajax({
		url: "getTarrateList",
		type: 'post',
		dataType: "json",
		data: {
			createtime: currtime,
			limit: pagesize
		},
		async: true,
		error: function () {
			alertmsg("Connection error");
		},
		success: function (datay) {
			$('#taxrate_list').bootstrapTable('load', datay);
		}
	});
}
/**
 * 获取被选中的id,并且放在数组中
 * @returns
 */
function getSelectedRowsId() {
    var data = $('#taxrate_list').bootstrapTable('getSelections');
    if(data.length == 0){
        alertmsg("至少选择一行数据！");
    }else{
        var arrayrateid = new Array();
        for(var i=0;i<data.length;i++){
            arrayrateid.push(data[i].id)
        }
        return arrayrateid;
    }
}
Date.prototype.format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt)) {
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
}
// 默认一页显示15条记录
var pagesize = 15;
// 当前调用的url
var pageurl = "getTarrateList";