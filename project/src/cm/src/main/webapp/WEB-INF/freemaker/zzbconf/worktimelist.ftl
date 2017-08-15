<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作时间管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/bootstrap.min.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/bootstrap-datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/worktime" ]);
</script>
<style type="text/css">
		.edit{background-color:#f5f5f5;}
		#editworktimetable{background-color:#f3f6f8;border: solid thin white;}
		#editholidaytable{background-color:#f3f6f8;border: solid thin white;}
		#editworkremindtable{background-color:#f3f6f8;border: solid thin white;}
</style>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5" id="panelOne">
		  <div class="panel-heading padding-5-5">
		      <h4 class="panel-title">
			  <button class="btn" data-toggle="collapse" data-parent="#panelOne" href="#collapseOne">
		              搜索具体机构</button></h4>
		  </div>
		  <div class="panel-body collapse in" id="collapseOne">
		    <div class="row">
			 <div class="col-md-12">
			   <form id="querydeptform" action="queryOneFuture" method="post">
			    <table id="tb1">
			      <tr>
			       <td>机构选择：财险集团：</td>
			       <td>
						 <div class="input-group">
						   <input type="hidden" id="deptid" name="deptid" >
                           <input type="text" class="form-control" id="deptname" name="deptname" placeholder="请选择">
                             <span class="input-group-btn">
                               <button class="btn btn-default" id="checkdept" type="button">选择</button>
                             </span>
                         </div>
			       </td>
			       <td>
			           &nbsp;&nbsp;<button class="btn btn-primary" type="button" name="turn2edite" id="querybutton" title="查询" >查询</button>
			       </td>
			      </tr>
			      <tr height="40">
			        <td colspan="3">
			          <label>
                       <input type="checkbox" id="onlyfuture" name="onlyfuture" value="1">只显示将来的节假日值班日及节假日
                      </label>
                    </td>
			      </tr>
			    </table>
			   </form>
			 </div>
		    </div>
		  </div>
		</div>

		<div class="panel panel-default" id="accordion">
		  <div class="panel-heading padding-5-5">
		  <div class="row">
				<div class="col-md-3">
				    <h4 class="panel-title">
				    <button class="btn" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">
					编辑具体机构运营时间表</button></h4>
				</div>
					<div class="col-md-9" align="right">
					    <button class="btn btn-primary" id="addworktime" onclick="openmodal()" title="添加上班时间">添加上班时间</button>
					    <#--<a href="javascript:openDialog('worktimeedit');">弹出</a>-->
					</div>
			</div>
			</div>
		  <div class="panel-body">	
		    <div class="row">
				<div id="collapseTwo" class="col-md-12 collapse in">
					<!-- <table id="table-javascript"></table> -->
					<table id="worktimelist" class="table table-bordered" style="border: 2px solid gray;">
						<tr align="center" bgcolor="#6699cc"  style="color: white">
						   <td width="150">机构</td>
						   <td colspan="2" width="400">时间</td>
						   <td width="100">操作</td>
						</tr>
						<#list allData.rowList as row>
						 <tr <#if row_index%2 = 0>class="edit"</#if>>
						   <td rowspan="7" id="deptname_val_${row_index }">
						        ${row.deptname!"" }
						        <input type="hidden" id="inscdeptid" name="inscdeptid" value="${row.deptid}" />
						        <input type="hidden" id="insbworktimeid" name="insbworktimeid" value="${row.insbworktimeid}" />
						   </td>
						   <td width="160"><b>上班时间：</b>
						   </td>
						   <td width="600"> 
						   <#list row.workdaylist as wdlist>
						      <span id="workdaystart_val_${wdlist_index }" name="workdaystart_val">
						            <#if wdlist.workdaystart = 1>星期一</#if>
						            <#if wdlist.workdaystart = 2>星期二</#if>
						            <#if wdlist.workdaystart = 3>星期三</#if>
						            <#if wdlist.workdaystart = 4>星期四</#if>
						            <#if wdlist.workdaystart = 5>星期五</#if>
						            <#if wdlist.workdaystart = 6>星期六</#if>
						            <#if wdlist.workdaystart = 7>星期日</#if>
						      </span><b>至</b>
						      <span id="workdayend_val_${wdlist_index }" name="workdayend_val">
						            <#if wdlist.workdayend = 1>星期一</#if>
						            <#if wdlist.workdayend = 2>星期二</#if>
						            <#if wdlist.workdayend = 3>星期三</#if>
						            <#if wdlist.workdayend = 4>星期四</#if>
						            <#if wdlist.workdayend = 5>星期五</#if>
						            <#if wdlist.workdayend = 6>星期六</#if>
						            <#if wdlist.workdayend = 7>星期日</#if>
						      </span>&nbsp;&nbsp;
						      <span id="daytimestart_val_${wdlist_index }" name="daytimestart_val"> ${wdlist.daytimestart!"" } </span> - 
						      <span id="daytimeend_val_${wdlist_index }" name="daytimeend_val"> ${wdlist.daytimeend!"" }; </span><br>
						   </#list>
						   </td>
						   <td rowspan="4" align="center">
						      &nbsp;<br><br><br>
						      <a href="javascript:void(0);" onclick="openmodal('${row.deptid}')">编辑</a>
						   </td>
						 </tr>
						 <tr <#if row_index%2 = 0>class="edit"</#if>>
						   <td><b>网点禁用状态：</b></td>
						   <td id="networkstate_val_${row_index}">
						   
						     <input type="checkbox" value="0" disabled="disabled" <#if row.networkstate = 0>checked="checked"</#if>/>禁用&nbsp;&nbsp;&nbsp;&nbsp;
						     <input type="checkbox" value="1" disabled="disabled" <#if row.networkstate = 1>checked="checked"</#if>/>启用
						   
						   </td>
						 </tr>
						 <tr <#if row_index%2 = 0>class="edit"</#if>>
						   <td><b>支付时间类型：</b></td>
						   <td id="paytime_val_${row_index}">
						     <#if row.paytimetype = 0>每天工作时间</#if>
						     <#if row.paytimetype = 1>支付有效期前一个工作日的工作时间</#if>
						   </td>
						 </tr>
						 <tr <#if row_index%2 = 0>class="edit"</#if>>
						   <td><b>非工作时间文字提示：</b></td>
						   <td id="noworkwords_val_${row_index}">${row.noworkwords }</td>
						 </tr>
						 <tr <#if row_index%2 = 0>class="edit"</#if>>
						   <td><b>节假日值班：</b></td>
						   <td>
						   <#list row.holidayworklist as hwlist>
						       <span name="holidayworklist_val">${hwlist.datestart!"" } <b>至</b> ${hwlist.dateend!"" }&nbsp;&nbsp;${hwlist.daytimestart!"" } - ${hwlist.daytimeend!"" }; </span><br>
						   </#list>
						   </td>
						   <td align="center"><a href="javascript:void(0);" onclick="openmodal_holidaywork('${row.deptid}')">编辑</a></td>
						 </tr>
						 <tr <#if row_index%2 = 0>class="edit"</#if>>
						   <td><b>节假日：</b></td>
						   <td>
						   <#list row.holidaylist as hlist>
						       <span name="holidaylist_val">${hlist.datestart!"" } <b>至</b> ${hlist.dateend!"" }&nbsp;&nbsp;${hlist.daytimestart!"" } - ${hlist.daytimeend!"" };</span><br>
						   </#list>
						   </td>
						   <td align="center"><a href="javascript:void(0);"  onclick="openmodal_holiday('${row.deptid}')">编辑</a></td>
						 </tr>
						 <tr <#if row_index%2 = 0>class="edit"</#if>>
						   <td><b>工作时间内提醒：</b></td>
						   <td>
						   <#list row.workdayremindList as wdrdlist>
						     <span name="holidaylist_val">${wdrdlist.workdaystart!"" } <b>至</b> ${wdrdlist.workdayend!"" }&nbsp;&nbsp;${wdrdlist.daytimestart!"" } - ${wdrdlist.daytimeend!"" };</span><br>
						   </#list>
						     <span id="workwords_val">${row.workwords!"" }</span>
						   </td>
						   <td align="center"><a href="javascript:void(0);" onclick="openmodal_workremind('${row.deptid}')">编辑</a></td>
						 </tr>
					    </#list>
					</table>
				</div>
		  </div>
		  </div>
		  <div class="panel-footer">
		    <div class="row">
		      <div class="col-md-12">
                    <div class="col-md-8">
						共<label id="lblToatl"> ${allData.total!"0" } </label>条数据  第【<label id="lblCurent">${allData.currentPage!"0" }</label>】页/
						共【<label id="lblPageCount">${allData.totalPage!"0" }</label>】页
					</div>
					<div class="col-md-4">
						跳转到：第
						<select name="goto" id="goto"
							onchange="go(this.options[this.options.selectedIndex].value)">
							<#if allData.totalPage??>
							<#list 1..allData.totalPage as i> 
								<option value="${i}" <#if "${allData.currentPage}" == "${i}">selected </#if>>${i}</option>
							</#list>
							<#else>
								<option value="1">1</option>
							</#if>
						</select>页
						<a id="first" href="javascript:void(0);">首页</a> 
						<a id="previous" href="javascript:void(0);">上一页</a> 
						<a id="next" href="javascript:void(0);"> 下一页</a> 
						<a id="last" href="javascript:void(0);">末页</a>
							
						<form role="form" id="currentPageform" action="queryworktimelistByPage" method="post">
						<input type="hidden" id="currentPage" name="currentPage" value="1">
						<input type="hidden" id="onlyfuture2" name="onlyfuture2" value="">
						</form>
					</div>		        
		      </div>
		    </div>
		  </div>
		  
		</div>
	</div>

<div id="editworktime" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <input type="button" class="close" data-dismiss="modal" aria-label="Close"  onclick="closeModal()" value="&times;"/>
        <h4 class="modal-title" id="gridSystemModalLabel">编辑</h4>
      </div>
      <div class="modal-body">
        <div class="container-fluid">
          <div class="row">
			<div class="col-md-12">
			  <form role="form" id="editform" class="form-inline" action="editworktimesave" method="post">
			    <table id="editworktimetable" width="500" border="0" align="center" cellspacing="0" rules="rows">
			       <thead>
                      <tr bgcolor="#e8e8e8">
                       <th colspan="3">工作时间</th>
                      </tr>
                    </thead>
			       <tr>
			          <td>设置机构：<br>财险集团：</td>
			          <td>
			            <div class="input-group">
						   <input type="hidden" id="insbworktimeid2" name="insbworktimeid" >
						   <input type="hidden" id="deptid2" name="inscdeptid" >
                           <input type="text" class="form-control" id="deptname2" name="inscdeptName" placeholder="请选择">
                             <span class="input-group-btn">
                               <button class="btn btn-default" id="checkdept2" type="button">选择</button>
                             </span>
                         </div>
			          </td>
			       </tr>
			       <tr>
			          <td>网点禁用状态:</td>
			          <td>
			           <div class="input-group">
			           <input id="networkstate0" name="networkstate" type="radio" value="0" />禁用&nbsp;&nbsp;&nbsp;&nbsp;
					   <input id="networkstate1" name="networkstate" type="radio" value="1" />启用</div>
			          </td>
			       </tr>
			       <tr class="sel" style="display: none;">
			          <td>星期：</td>
			          <td>
			           <div class="form-group">
			            <select class="form-control">
			              <option value="1">星期一</option>
			              <option value="2">星期二</option>
			              <option value="3">星期三</option>
			              <option value="4">星期四</option>
			              <option value="5">星期五</option>
			              <option value="6">星期六</option>
			              <option value="7">星期日</option>
			            </select>
			            <b>至</b>
			            <select class="form-control">
			              <option value="1">星期一</option>
			              <option value="2">星期二</option>
			              <option value="3">星期三</option>
			              <option value="4">星期四</option>
			              <option value="5">星期五</option>
			              <option value="6">星期六</option>
			              <option value="7">星期日</option>
			            </select>
			           </div> 
			          </td>
			          <td>
			             <input class="deleteClass" id="deletetr" type="button" value="删除"/>
            			 <input type="hidden" id="areaid" value="" />
            			 <input type="hidden" id="areaworktimeid" value="" />
			          </td>
			       </tr>
			       <tr class="inp" style="display: none;">
			          <td>时间：</td>
			          <td colspan="2">
						<div class="form-group">
						  <div class="row">&nbsp;&nbsp;&nbsp;
             			   <div class="input-group date form_time col-md-5" data-date="" data-date-format="hh:ii" data-link-field="dtp_input3" data-link-format="hh:ii">
            			       <input class="form-control" size="5" type="text" value="" />
							   <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
          			       </div>
          			       <b>至</b>
             			   <div class="input-group date form_time col-md-5" data-date="" data-date-format="hh:ii" data-link-field="dtp_input3" data-link-format="hh:ii">
            			       <input class="form-control" size="5" type="text" value="" />
							   <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
          			       </div>
          			       </div>
          			    </div>
			          </td>
			       </tr>
			       
			       <tr id ="addtr">
			         <td colspan="3">
			            <input id="addtrbtn" type="button" value="+添加时间"/>
			         </td>
			       </tr>
			       <tr>
			           <td colspan="3">
			                           非工作时间文字提示：<br>
			             <textarea id="noworkwords" name="noworkwords" rows="3" cols="60"></textarea>
			          </td>
			       </tr>
			       <tr>
			          <td colspan="3">
			                         支付时间：<br>
			            <input id="payMethod" type="radio" name="paytimetype" value="0" />每天工作时间 <br>
                        <input id="payMethod" type="radio" name="paytimetype" value="1" />支付有效期前一个工作日的工作时间
			          </td>
			       </tr>
			       <tr>
			           <td colspan="3">
			              <input type="button" class="btn btn-primary" onclick="saveedit()" value="保存"/>&nbsp;&nbsp;
			              <input type="button" class="btn btn-primary" data-dismiss="modal" onclick="closeModal()" value="取消"/>
			           </td>
			       </tr>
			    </table>
			   </form>
			</div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <input type="button" class="btn btn-primary" data-dismiss="modal" onclick="closeModal()" value="关闭"/>
      </div>
    </div>
  </div>
</div>


<div id="edittime_holiday" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <input type="button" class="close" data-dismiss="modal" aria-label="Close"  onclick="closeModal()" value="&times;"/>
        <h4 class="modal-title" id="gridSystemModalLabel">编辑</h4>
      </div>
      <div class="modal-body">
        <div class="container-fluid">
          <div class="row">
			<div class="col-md-12">
			  <form role="form" id="editform_holiday" class="form-inline" action="editworktimesave" method="post">
			    <table id="editholidaytable" width="500" border="0" align="center" cellspacing="0" rules=rows>
			       <thead>
                      <tr bgcolor="#e8e8e8">
                       <th colspan="3">节假日值班</th>
                      </tr>
                    </thead>
			       <tr>
			          <td>设置机构：<br>财险集团：</td>
			          <td>
			            <div class="input-group">
						   <input type="hidden" id="insbworktimeid3" name="insbworktimeid" >
						   <input type="hidden" id="deptid3" name="inscdeptid" >
						   <input type="hidden" id="noworkwords3" name="noworkwords" >
						   <input type="hidden" id="paytimetype3" name="paytimetype" >
                           <input type="text" class="form-control" id="deptname3" name="inscdeptName" placeholder="请选择">
                             <span class="input-group-btn">
                               <button class="btn btn-default" id="checkdept2" type="button">选择</button>
                             </span>
                         </div>
			          </td>
			       </tr>
			       
			       <tr class="dateinput" style="display: none;">
			          <td>日期：</td>
			          <td>
						<div class="form-group">
						  <div class="row">&nbsp;&nbsp;&nbsp;
           			       <div class="input-group date form_date col-md-5" data-date="" data-date-format="dd MM yyyy" data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
          			          <input class="form-control" style="width:100px;" size="5" type="text" value="" >
							   <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
          			       </div>
          			       <b>至</b>
          			       <div class="input-group date form_date col-md-5" data-date="" data-date-format="dd MM yyyy" data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
          			          <input class="form-control" style="width:100px;" size="5" type="text" value="" >
							   <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
          			       </div>
          			      </div>
        			    </div>
			          </td>
			          <td>
			             <input class="deleteClass" id="deletetr" type="button" value="删除"/>
            			 <input type="hidden" id="holidayAreaid" value="" />
            			 <input type="hidden" id="insbworktimeid" value="" />
			          </td>
			       </tr>
			       
			       <tr class="timeinput" style="display: none;">
			          <td>时间：</td>
			          <td colspan="2">
						<div class="form-group">
						  <div class="row">&nbsp;&nbsp;&nbsp;
             			   <div class="input-group date form_time col-md-5" data-date="" data-date-format="hh:ii" data-link-field="dtp_input3" data-link-format="hh:ii">
            			       <input class="form-control" size="5" type="text" value="" />
							   <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
          			       </div>
          			       <b>至</b>
             			   <div class="input-group date form_time col-md-5" data-date="" data-date-format="hh:ii" data-link-field="dtp_input3" data-link-format="hh:ii">
            			       <input class="form-control" size="5" type="text" value="" />
							   <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
          			       </div>
          			       </div>
          			    </div>
			          </td>
			       </tr>
			       
			       <tr id ="addtr_holiday">
			         <td colspan="3">
			            <input id="addtrbtn_holiday" type="button" value="+添加日期时间"/>
			            <input id="holidayOrWork" type="hidden" value=""/>
			         </td>
			       </tr>
			       <tr>
			           <td colspan="3">
			              <input type="button" class="btn btn-primary" onclick="saveedit_holiday()" value="保存"/>&nbsp;&nbsp;
			              <input type="button" class="btn btn-primary" data-dismiss="modal" onclick="closeModal()" value="取消"/>
			           </td>
			       </tr>
			    </table>
			   </form>
			</div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <input type="button" class="btn btn-primary" data-dismiss="modal" onclick="closeModal()" value="关闭"/>
      </div>
    </div>
  </div>
</div>

<div id="edittime_workremind" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <input type="button" class="close" data-dismiss="modal" aria-label="Close"  onclick="closeModal()" value="&times;"/>
        <h4 class="modal-title" id="gridSystemModalLabel">编辑</h4>
      </div>
      <div class="modal-body">
        <div class="container-fluid">
          <div class="row">
			<div class="col-md-12">
			  <form role="form" id="editform_workremind" class="form-inline" action="editworktimesave" method="post">
			    <table id="editworkremindtable" width="500" border="0" align="center" cellspacing="0" rules=rows>
			       <thead>
                      <tr bgcolor="#e8e8e8">
                       <th colspan="3">工作时间提醒</th>
                      </tr>
                    </thead>
			       <tr>
			          <td>设置机构：<br>财险集团：</td>
			          <td>
			            <div class="input-group">
						   <input type="hidden" id="insbworktimeid4" name="insbworktimeid" >
						   <input type="hidden" id="deptid4" name="inscdeptid" >
						   <input type="hidden" id="noworkwords4" name="noworkwords" >
						   <input type="hidden" id="paytimetype4" name="paytimetype" >
                           <input type="text" class="form-control" id="deptname4" name="inscdeptName" placeholder="请选择">
                             <span class="input-group-btn">
                               <button class="btn btn-default" id="checkdept2" type="button">选择</button>
                             </span>
                         </div>
			          </td>
			       </tr>
			       <tr class="sel" style="display: none;">
			          <td>星期：</td>
			          <td>
			           <div class="form-group">
			            <select class="form-control">
			              <option value="1">星期一</option>
			              <option value="2">星期二</option>
			              <option value="3">星期三</option>
			              <option value="4">星期四</option>
			              <option value="5">星期五</option>
			              <option value="6">星期六</option>
			              <option value="7">星期日</option>
			            </select>
			            <b>至</b>
			            <select class="form-control">
			              <option value="1">星期一</option>
			              <option value="2">星期二</option>
			              <option value="3">星期三</option>
			              <option value="4">星期四</option>
			              <option value="5">星期五</option>
			              <option value="6">星期六</option>
			              <option value="7">星期日</option>
			            </select>
			           </div> 
			          </td>
			          <td>
			             <input class="deleteClass" id="deletetr" type="button" value="删除"/>
            			 <input type="hidden" id="areaid" value="" />
            			 <input type="hidden" id="areaworktimeid" value="" />
			          </td>
			       </tr>
			       <tr class="inp" style="display: none;">
			          <td>时间：</td>
			          <td colspan="2">
						<div class="form-group">
						  <div class="row">&nbsp;&nbsp;&nbsp;
             			   <div class="input-group date form_time col-md-5" data-date="" data-date-format="hh:ii" data-link-field="dtp_input3" data-link-format="hh:ii">
            			       <input class="form-control" size="5" type="text" value="" />
							   <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
          			       </div>
          			       <b>至</b>
             			   <div class="input-group date form_time col-md-5" data-date="" data-date-format="hh:ii" data-link-field="dtp_input3" data-link-format="hh:ii">
            			       <input class="form-control" size="5" type="text" value="" />
							   <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
          			       </div>
          			       </div>
          			    </div>
			          </td>
			       </tr>
			       
			       <tr id ="addtr_workremind">
			         <td colspan="3">
			            <input id="addtrbtn_workremind" type="button" value="+添加时间"/>
			         </td>
			       </tr>
			       <tr>
			           <td colspan="3">
			                           工作时间内提醒：<br>
			             <textarea id="workwords" name="workwords" rows="3" cols="60"></textarea>
			          </td>
			       </tr>
			       <tr>
			           <td colspan="3">
			              <input type="button" class="btn btn-primary" onclick="saveedit_workremind()" value="保存"/>&nbsp;&nbsp;
			              <input type="button" class="btn btn-primary" data-dismiss="modal" onclick="closeModal()" value="取消"/>
			           </td>
			       </tr>
			    </table>
			   </form>
			</div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <input type="button" class="btn btn-primary" data-dismiss="modal" onclick="closeModal()" value="关闭"/>
      </div>
    </div>
  </div>
</div>


<div id="showdept" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="gridSystemModalLabel">选择机构</h4>
      </div>
      <div class="modal-body">
        <div class="container-fluid">
          <div class="row">
			<ul id="depttree" class="ztree"></ul>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
    </div>
  </div>
</div>

</body>
</html>
