<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>掌中保-订单列表-业管版</title>
    <link rel="stylesheet"
          href="${staticRoot}/css/modelinsurance/bootstrap.min.css">
    <link rel="stylesheet"
          href="${staticRoot}/css/modelinsurance/zzb_yg.css">
    <link rel="stylesheet" href="${staticRoot}/css/modelinsurance/date.css">
    <script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
    <script type="text/javascript">
        requirejs([ "cm/report/report"]);
    </script>
    <script src="${staticRoot}/js/modelinsurance/date.js"></script>
    <script src="${staticRoot}/js/modelinsurance/iscroll.js"></script>
</head>
<body>
<form action="/cm/report/showReport" name="fm1" method="post" id="fm1" target="report_frame">

    <table width="100%" border="0" cellspacing="6" cellpadding="0"
           style="border-collapse: separate; border-spacing: 6px;">
        <tr valign="top">
            <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="6"
                       class="blockTable">
                    <tr>
                        <td valign="middle" class="blockTd"><img src="${staticRoot}/images/report/doc.gif" />代理人信息表</td>
                    </tr>
                    <tr>
                        <td>
                            <table>

                                <tr>
                                    <input id="raq" name="raq" value="inscompany" type="hidden">
                                    <td>
                                        机构名：
                                        <input name="company" type="text" id="company" value="" style="width: 90px" class="inputText">
                                    </td>
                                    <td><input type="button" name="Submit" id="Submit" value="查询" onClick="doSearch()"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                </table>
            </td>
        </tr>
    </table>
</form>
<script>
    function doSearch(){
        document.getElementById("Submit").value='查询中请稍后。。。。。';
        document.getElementById("fm1").submit();
    }
</script>
<iframe id="report_frame" name="report_frame" style="width:100%; height:100%;"></iframe>
</body>

</html>
