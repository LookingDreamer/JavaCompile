require(["jquery", "bootstrapdatetimepicker", "bootstrap-table", "bootstrap","bootstrapTableZhCn","zTree","zTreecheck","flat-ui","bootstrapdatetimepickeri18n","public"], function ($) {
    $(function() {
        var chnsetting = {
            async : {
                enable : true,
                url : "/cm/channel/queryTreeTopList",
                autoParam: ["id=parentCode"],
                dataType : "json",
                type : "post"
            },
            check : {
                enable : true,
                chkStyle : "radio",
                radioType : "all"
            },
            callback : {
                onClick : zTreeOnCheckChn,//回调函数
                onCheck : zTreeOnCheckChn//回调函数
            }
        };

        $(".closeshowchannel").on("click", function(e) {
            $('#showchannel').modal("hide");
        });

        //选择后回调函数
        function zTreeOnCheckChn(event, treeId, treeNode) {
            $("#channelinnercode").val(treeNode.innercode);
            $("#channelName").val(treeNode.name);
            $('#showchannel').modal("hide");
        }
        //点击弹出供应商选择页面
        $("#channelName").on("click", function(e) {
            $('#showchannel').modal();
            $.fn.zTree.init($("#channelTree"), chnsetting);
        });

    });
});