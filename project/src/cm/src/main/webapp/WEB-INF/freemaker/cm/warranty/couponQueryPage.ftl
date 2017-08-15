<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>延保奖券查询</title>
    <link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>
    <script data-ver="${jsver}" data-main="${staticRoot}/js/load"
            src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
    <script type="text/javascript">
        requirejs(["cm/warranty/couponQuery", "chn/channelTree"]);
    </script>
    <style type="text/css">
        div#selectStype a {
            position: relative;
            left: 15px;
            top: 10px;
        }
    </style>
</head>
<body>
<div class="container-fluid" style="margin-bottom:30px">

    <div class="panel panel-default m-bottom-5" id="superquerypanel">
        <div class="panel-heading padding-5-5">
            <label>查询奖券</label>
            <button class="close" type="button" id="superquerypanelclose">&times;</button>
        </div>
        <div class="panel-body">
            <form role="form" id="cartasksuperqueryform">
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group col-md-4  form-inline">
                            <label for="exampleInputJobNum">代理工作号</label>
                            <input type="text" class="form-control" id="jobNum" name="jobNum" placeholder=""/>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <div class="panel-footer padding-5-5">
            <div class="row">
                <div class="col-md-12">
                    <div class="col-md-12" align="right">
                        <button id="superquerybutton" type="button" name="querybutton"
                                class="btn btn-primary">查询
                        </button>
                        <button id="singlequery" type="button"
                                class="btn btn-primary">优化查询
                        </button>
                        <button id="resetbutton" type="button" name="resetbutton"
                                class="btn btn-primary">重置
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="panel panel-default">
        <div class="panel-heading padding-5-5">
            <div class="row">
                <div class="col-md-2">
                    <label>奖券列表</label>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <table id="table-javascript"></table>
            </div>
        </div>
    </div>

</div>
</body>
</html>
