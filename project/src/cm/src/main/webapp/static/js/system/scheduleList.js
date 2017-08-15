require(["jquery", "bootstrapTableZhCn", "bootstrap-table", "bootstrap", "bootstrapdatetimepicker", "bootstrapdatetimepickeri18n","public"],
function($) {
  $(function() {
    $('#scheduleListTableId').bootstrapTable({
      method: 'get',
      url: pageurl,
      cache: false,
      //      height: 460,
      striped: true,
      pagination: true,
      sidePagination: 'server',
      pageSize: pagesize,
      pageList: [5, 10, 25, 50, 100, 200],
      //      search: true,
      //      showColumns: true,
      //      showRefresh: true,
      //      showToggle: true,
      minimumCountColumns: 5,
      clickToSelect: true,
      //      classes: 'table table-hover',
      columns: [{
        field: 'stateFlag',
        align: 'center',
        valign: 'middle',
        checkbox: true
      },
      {
        field: 'id',
        title: '计划id',
        visible: false,
        switchable: false
      },
      {
        field: 'tasktypename',
        title: '任务类别',
        align: 'center',
        valign: 'middle',
        sortable: true
        // formatter: nameFormatter
      },
      {
        field: 'taskname',
        title: '任务名称',
        align: 'center',
        valign: 'middle',
        sortable: true
        // formatter: priceFormatter,
        // sorter: priceSorter
      },
      {
        field: 'state',
        title: '是否启用',
        width: 20,
        align: 'center',
        valign: 'middle',
        sortable: true,
        // clickToSelect: false,
        formatter: isUsingFormatter,
        // events: operateEvents
      },
      {
        field: 'nexttime',
        title: '下次运行时间',
        width: 160,
        align: 'center',
        valign: 'top',
        clickToSelect: true,
        sortable: true,
        //        formatter: timeFormatter,
        // events: operateEvents
      },
      {
        field: 'ip',
        title: 'IP地址',
        width: 160,
        align: 'center',
        valign: 'top',
        clickToSelect: true,
        sortable: true,
        // formatter : formatter,
        // events: operateEvents
      },
      {
        field: 'comment',
        title: '描述',
        // width : 100,
        align: 'center',
        valign: 'middle',
        // formatter: nameFormatter
      }]
    });

    //删除
    $('#deletebtn').click(function() {
      var scheduleIds = getSelectedRows();
      if (scheduleIds == null || scheduleIds.length < 1) {
        return;
      }
      if (window.confirm("您确定要删除" +scheduleIds.length+ "计划吗？")) {
        location.href = 'deletebyids?ids=' + scheduleIds;
      }
    });

    //新建
    $('#addNewbtn').click(function() {
      window.parent.openDialog("schedule/scheeditpage?scheid=");
    });

    //修改
    $('#modifybtn').click(function() {
      var id = getSelectedRow();
      if (id == 'null' || id == '') {
        return;
      }
      window.parent.openDialog("schedule/scheeditpage?scheid=" + id);
    });

    //手动执行
    $('#handExebtn').click(function() {
      var scheIds = getSelectedRows();
      if (typeof(scheIds) == undefined || scheIds == 'null' || scheIds.length < 1) {
        return;
      }
      if (window.confirm("您确定要手动执行这些计划吗？")) {
        getExeResult(scheIds);
      }
    });

    //
    $("#toggle").on("click",
    function(e) {
      $('#scheduleListTableId').bootstrapTable('toggleView');
    });

    //刷新
    $("#refresh").on("click",
    function(e) {
      refreshTable();
    });

  });

});

//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "listData";

function isUsingFormatter(value, row) {
  var result = "";
  if (value != null && value == '1') {
    result = "启用";
  } else if (value != null && value == '2') {
    result = "停用";
  } else {
    result = "显示不正确";
  }
  return result;
}

// 获得选中行的id
function getSelectedRow() {
  var result = "";
  var data = $('#scheduleListTableId').bootstrapTable('getSelections');
  if (data.length < 1) {
    alertmsg("请选择要修改的1行！");
  } else if (data.length > 1) {
    alertmsg("您选择了" + data.length + "行，只能选择1行，请重新选择！");
  } else {
    result = data[0].id;
  }

  return result;
}

//获得选中行的id
function getSelectedRows() {
  var ids = new Array();
  var data = $('#scheduleListTableId').bootstrapTable('getSelections');
  if (data.length < 1) {
    alertmsg("请选择要操作的行！");
  } else {
    for (var i = 0; i < data.length; i++) {
      ids.push(data[i].id)
    }
  }
  return ids;
}

function getExeResult(scheIds) {
  var message = '';
  $.ajax({
    url: 'executebyids?ids=' + scheIds,
    type: 'GET',
    dataType: "json",
    async: false,
    error: function() {
      alertmsg("Connection error");
    },
    success: function(data) {
      if (data != null) {
        var failMsg = data.fail;
        if (typeof(failMsg) != 'undefined' && failMsg != 'null' && failMsg != '') {
          alertmsg(failMsg);
        } else {
          var successMsg = data.success;
          if (typeof(successMsg) != 'undefined' && successMsg != 'null' && successMsg != '') {
            alertmsg(successMsg);
          }
        }
      }
    }
  });
}

function refreshTable() {
  $('#scheduleListTableId').bootstrapTable('refresh');
}