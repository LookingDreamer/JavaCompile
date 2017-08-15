<table id=titleTable width=100% cellspacing=0 cellpadding=0 border=0 ><tr>
    <td height="22" width=100% valign="middle"  style="font-size:13px" background="${staticRoot}/images/report/ta51top.jpg">
        <table width="100%">
            <tr >
                <td width=53% align="left"  style="font-size:13px" >&nbsp;&nbsp;&nbsp;&nbsp;</td>
                <td width="47%" align="center" valign="middle"   style="font-size:12px" >共<span id="t_page_span"></span>页/第<span id="c_page_span"></span>页&nbsp;&nbsp;
                    <a href="#" title="打  印" onClick="report1_print();return false;"><img src='${staticRoot}/images/report/print.gif' border=no ></a>
                    <a href="#" title="导出Excel" onClick="report1_saveAsExcel();return false;"><img src='${staticRoot}/images/report/excel.gif' border=no ></a>
                    <a href="#" title="导出Pdf" onClick="report1_saveAsPdf();return false;"><img src='${staticRoot}/images/report/pdf.gif' border=no ></a>
                    <a href="#" title="导出Word" onClick="report1_saveAsWord();return false;"><img src='${staticRoot}/images/report/doc.gif' border=no ></a>
                    <a href="#" title="首  页" onClick="try{report1_toPage( 1 );}catch(e){}return false;"><img src='${staticRoot}/images/report/firstpage.gif' border=no ></a>
                    <a href="#" title="上一页" onClick="try{report1_toPage(report1_getCurrPage()-1);}catch(e){}return false;"><img src='${staticRoot}/images/report/prevpage.gif' border=no ></a>
                    <a href="#" title="下一页" onClick="try{report1_toPage(report1_getCurrPage()+1);}catch(e){}return false;"><img src='${staticRoot}/images/report/nextpage.gif' border=no ></a>
                    <a href="#" title="尾  页" onClick="try{report1_toPage(report1_getTotalPage());}catch(e){}return false;"><img src='${staticRoot}/images/report/lastpage.gif' border=no ></a>
                </td>
            </tr>
        </table>
    </td>
</table>