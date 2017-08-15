<style type="text/css">
	table.hovertable {
		font-size:11px;
		color:#333333;
		border-width: 1px;
		border-color: #999999;
		border-collapse: collapse;
		margin-bottom:5px;
		margin-top:5px;
		background-color:#F5F5F5;
	}
	table.hovertable td {
		border-width: 1px;
		padding: 8px;
		border-style: solid;
		border-color: #383838;
	}
	td.bgg{
		background-color:#E5E5E5;
	}
	table.whiteborder td {
		border-color: white;
	}
	table.orange td {
		border-width:0px;
	}
	table.orange{
		background-color:#FFE4B5;
	}
	table.table-platcarmessage{
		background-color:#F5F5F5;
	}
</style>
	<table border="1px" class="hovertable col-md-12">
		<tr>
			<td class="col-md-12" colspan="6" style="background-color:#8A8A8A;color:white;">
				<label>支付用补充信息</label>
				
				<label style="float:right;"><a href="javascript:window.parent.openDialogForCM('business/ordermanage/preEditIdCardInfo?taskid=${carInsTaskInfo.relationPersonInfo.taskid}&inscomcode=${inscomcode}');"><font style="color:white">修改</font></a></label>
			</td>
		</tr>
		<tr>
					<td class="col-md-1 bgg">姓名:</td>
					<td class="col-md-2" id="insd-name${carInsTaskInfo.relationPersonInfo.taskid}">${carInsTaskInfo.relationPersonInfo.insuredname}</td>
					<td class="col-md-1 bgg">身份证号:</td>
					<td class="col-md-2" id="insd-idcardno${carInsTaskInfo.relationPersonInfo.taskid}">
						<#if carInsTaskInfo.relationPersonInfo.insuredidcardtype==0>
							${carInsTaskInfo.relationPersonInfo.insuredidcardno}
						</#if>
						<input id="idcardnohidden" type="hidden" value="${carInsTaskInfo.relationPersonInfo.insuredidcardno}"/>
					</td>
					<td class="col-md-1 bgg">性别:</td>
					<td class="col-md-2" id="insd-sex${carInsTaskInfo.relationPersonInfo.taskid}">
						<#if carInsTaskInfo.relationPersonInfo.insuredsex == 1>
							女
						<#else>
							男
						</#if>
					</td>
				</tr>
				<tr>
					<td class="bgg">民族:</td>
					<td id="insd-nation${carInsTaskInfo.relationPersonInfo.taskid}">${carInsTaskInfo.relationPersonInfo.insurednation}</td>
					<td class="bgg">出生日期:</td>
					<td id="insd-birthday${carInsTaskInfo.relationPersonInfo.taskid}">${carInsTaskInfo.relationPersonInfo.insuredbirthday}</td>
					<td class="bgg">证件有效期:</td>
					<td id="insd-expdate${carInsTaskInfo.relationPersonInfo.taskid}">${carInsTaskInfo.relationPersonInfo.insuredexpdate}</td>
				</tr>
				<tr>
					<td class="bgg">手机号:</td>
					<td id="insd-cellphone${carInsTaskInfo.relationPersonInfo.taskid}">
						<input id="cellphonehidden" type="hidden" value="${carInsTaskInfo.relationPersonInfo.insuredcellphone}"/>
						${carInsTaskInfo.relationPersonInfo.insuredcellphone}
					</td>
					<td class="bgg">邮箱:</td>
					<td id="insd-email${carInsTaskInfo.relationPersonInfo.taskid}">${carInsTaskInfo.relationPersonInfo.insuredemail}</td>
					<td class="bgg">前端输入的验证码:</td>
					<td id="pincode">${carInsTaskInfo.relationPersonInfo.insuredpincode}</td>
				</tr>
				<tr>
					<td class="bgg">签发机关:</td>
					<td id="insd-authority${carInsTaskInfo.relationPersonInfo.taskid}">${carInsTaskInfo.relationPersonInfo.insuredauthority}</td>
					<td class="bgg">住址:</td>
					<td id="insd-address${carInsTaskInfo.relationPersonInfo.taskid}">${carInsTaskInfo.relationPersonInfo.insuredaddress}</td>
                    <td class="bgg">有效期限开始:</td>
                    <td id="insd-expstartdate${carInsTaskInfo.relationPersonInfo.taskid}">${carInsTaskInfo.relationPersonInfo.insuredexpstartdate}</td>
				</tr>
				<tr>
					<td class="bgg">有效期限结束:</td>
					<td id="insd-expenddate${carInsTaskInfo.relationPersonInfo.taskid}">${carInsTaskInfo.relationPersonInfo.insuredexpenddate}</td>
				</tr>

	</table>
