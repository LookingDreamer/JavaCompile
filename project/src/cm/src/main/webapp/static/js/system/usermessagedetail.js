//刷新列表
function reloaddata(){
	/*var fileTypes =[$("#fileTypes").val(),$("#fileTypes1").val()];
	var fileDescribes =[$("#fileDescribes").val(),$("#fileDescribes1").val()];
	$.ajaxFileUpload({
		url : "mobile/registered/filesUpLoad?fileTypes="+fileTypes+"&fileDescribes="+fileDescribes+"&jobNum="+$("#jobNum").val()+"",
		//url : "mobile/registered/fileUpLoad",
		//type : 'post',
		secureuri:false,                       //是否启用安全提交,默认为false
        fileElementId:["files","files1"],
        //fileElementId:'file',
		dataType : "json",
		error : function() {
			alert("Connection error");
		},
		success : function(data) {
			alert("success");
			jQuery($("#img")).attr("src",data.filepathlist[0]);
			jQuery($("#img1")).attr("src",data.filepathlist[1]);
		}
		
	});*/
	var data="{\"file\":\""+$("#file").val()+"\",\"fileName\":\"截图.png\",\"fileType\":\"insuredidcardopposite\",\"fileDescribes\":\"投保人身份证正面照\",\"jobNum\":\"8899\"}";
	$.ajax({
		headers: {
            "Content-type":"application/json",
            	"Accept":"application/json",
            		"token":"4cc242f9e0d53d4cb2a296fc57df64dd"
        },
		url : "mobile/registered/fileUpLoadBase64",
		type : 'post',
		data : data,
		dataType : "json",
		error : function() {
			alert("Connection error");
		},
		success : function(data) {
			alert(data);
		}
	});
}
function messageNotReadCount(){
	$.ajax({
		  type: "get",
		  url: "message/getNotReadCount",
		  data: {receiver:$("#receiver").val()},
		  dataType:"text",
		  success: function(data){
			  $("#messageNotReadCount").text(data);
		  }
		});
}