<#assign report=JspTaglibs["/WEB-INF/runqianReport4.tld"]>
<html>
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<body topmargin=0 leftmargin=0 rightmargin=0 bottomMargin=0>
<#include "report/reportFiles/toolbar.ftl">
<#if exists == "true">
<table id=rpt align=center>
<tr>
<td>
    <table id=param_tbl>
        <tr>
            <td>
                <@report.param name="form1" paramFileName="${param}"
                              needSubmit="no" params="${param}"
                        />
            </td>
            <td><a href="javascript:_submit( form1 )">
                <img src="../images/query.jpg" border=no style="vertical-align:middle"></a>
            </td>
        </tr>
    </table>
</#if>

    <table align=left>
        <tr>
            <td>
                <@report.html name="report1" reportFileName="${reportstr}"
                             action="/report/showReport"
                             funcBarLocation=""
                             needPageMark="yes"
                             useCache="no"
                             generateParamForm="no"
                             params="${param}"
                             width="-1"
                             exceptionPage="../reportFiles/reportJsp/myError2.jsp"
                        />
            </td>
        </tr>
    </table>

    <script language="javascript">
        //设置分页显示值
        document.getElementById("t_page_span").innerHTML = report1_getTotalPage();
        document.getElementById("c_page_span").innerHTML = report1_getCurrPage();
        require(["jquery","jqblockui","core"],function($){
			$(function() {
				$.insLoaded();
			});
});
    </script>
</body>
</html>
