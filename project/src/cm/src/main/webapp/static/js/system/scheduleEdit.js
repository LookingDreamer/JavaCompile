function initScript() {
  $('#exePeriodRadiocron').on('click',
  function() {
    $('#period').attr('disabled', 'disabled');
    $('#periodunit').attr('disabled', 'disabled');
    $('#cronexp').removeAttr('disabled');
  });

  $('#exePeriodRadioNotcron').on('click',
  function() {
    $('#cronexp').attr('disabled', 'disabled');
    $('#period').removeAttr('disabled');
    $('#periodunit').removeAttr('disabled');
  });

  $('#tasktypeid').change(function() {
    var selVal = $(this).find('option:selected').val();
    var taskString = '<option value="" selected="selected">--请选择任务--</option>';
    if (selVal != null && selVal != '') {
      getTaskList(selVal, taskString);
    } else {
      $('#taskid').empty();
      $('#taskid').append(taskString);
    }

  });

  $('.form_datetime').datetimepicker({
    language: 'zh-CN',
    format: "yyyy-mm-dd hh:ii:ss",
    weekStart: 1,
    todayBtn: 1,
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    forceParse: 0,
    minView: 2,
    // showMeridian: 1
  });

  // 保存计划任务
  $('#addScheduleBtn').on("click",
  function(e) {
    var validateMsg = '';
    var tasktypeid = $('#tasktypeid').val();
    if (tasktypeid == null || tasktypeid == '') {
      validateMsg += "请选择【任务类别】,\n";
      $('#tasktypeid').focus();
    }

    var taskid = $('#taskid').val();
    if (taskid == null || taskid == '') {
      validateMsg += "请选择【任务】,\n";
      $('#taskid').focus();
    }

    var starttime = $('#starttime').val();
    if (starttime == null || starttime == '') {
      validateMsg += "请选择【起始时间】,\n";
      $('#starttimeTxt').focus();
    }

    var iscron = $('input[name="iscron"]:checked').val();
    if (iscron != null && iscron == '1') {
      var period = $('#period').val();
      if (period == null || period == '') {
        validateMsg += "请输入【周期】,\n";
        $('#period').focus();
      } else {
        if (isPositiveInteger(period) == false) {
          validateMsg += "【周期】的值为正整数，请重新输入,\n";
          $('#period').focus();
        }
      }

      var periodunit = $('#periodunit').val();
      if (periodunit == null || periodunit == '') {
        validateMsg += "请选择【周期单位】,\n";
        $('#periodunit').focus();
      }

    } else if (iscron != null && iscron == '2') {
      var cronexp = $('#cronexp').val();
      if (cronexp == null || cronexp == '') {
        validateMsg += "请输入【Cron表达式】,\n";
        $('#cronexp').focus();
      }

    }

    var ip = $('#ip').val();
    if (ip == null || ip == '') {
      validateMsg += "请输入【IP地址】,\n";
      $('#ip').focus();
    } else {
      if (isIP(ip) == false) {
        validateMsg += "【ip地址】格式不正确，请修改,\n";
        $('#ip').focus();
      }
    }

    if (validateMsg != '') {
      validateMsg = validateMsg.substring(0, validateMsg.length - 2);
      alertmsg(validateMsg);
      return;
    }

    //$('#scheduleForm').submit();
    $('#scheduleForm').ajaxSubmit({
      url: 'schedule/saveSchedule',
      type: 'POST',
      dataType: "json",
      clearForm: true,
      error: function() {
        alertmsg("Connection error");
      },
      success: function(data) {
        var result = data.flag;
        if (result == "1") {
          alertmsg("保存计划成功！");
        } else {
          alertmsg("保存计划失败");;
        }

        $('#xDialog').modal('hide');
        window.fra_schedulelist.refreshTable();
      }
    });

  });
}

function getTaskList(taskTypeid, showString) {
  $.ajax({
    url: 'schedule/gettaskbyTasktypeid?id=' + taskTypeid,
    type: 'GET',
    dataType: "json",
    async: false,
    error: function() {
      alertmsg("Connection error");
    },
    success: function(data) {
      if (data != null) {
        var id, taskname;
        for (i = 0; i < data.length; i++) {
          id = data[i].id;
          taskname = data[i].taskname;
          showString = showString + '<option value="' + id + '">' + taskname + '</option>';
        }
      }
      $('#taskid').empty();
      $('#taskid').append(showString);

    }
  });
}

//正整数
function isPositiveInteger(str) {
  var reg = new RegExp("^[0-9]*[1-9][0-9]*$");
  var flag = str.match(reg);
  if (flag != undefined && flag != "") {
    return true;
  }
  return false;
}

//ip地址格式校验
function isIP(ip) {
  var exp = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
  var flag = ip.match(exp);
  if (flag != undefined && flag != "") {
    return true;
  }
  return false;
}