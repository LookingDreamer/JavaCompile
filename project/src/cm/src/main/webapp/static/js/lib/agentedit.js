var MyValidator = function() {
    var handleSubmit = function() {   	
        $('.form-inline').validate({
            
            rules : {
            	name : {
                    required : true,
                    minlength:2,
                    maxlength:10
                    
                },
                idno : {
                    required : true,
                   
                }
            },
            messages : {
            	name : {
                    required : "姓名不能为空.",
                    	 name: "姓名格式不正确"
                },
                idno : {
                    required : "身份证号不能为空.",
                        
                }
            },
			/* 重写错误显示消息方法,以alert方式弹出错误消息 */

            showErrors: function(errorMap, errorList) {
                if(errorList.length > 0){
                    alertmsg(errorList[0].message);
                    return false;
                }
            },
            ignore:"", // 验证所有元素，包括隐藏input
            onfocusout: false,
            onkeyup: false,


            errorPlacement : function(error, element) {
                element.parent('div').append(error);
            },

            submitHandler : function(form) {
                form.submit();
            }
        });

        $('.form-inline input').keypress(function(e) {
            if (e.which == 13) {
                if ($('.form-inline').validate().form()) {
                    $('.form-inline').button();
                }
                return false;
            }
        });
    }
    return {
        init : function() {
            handleSubmit();
        }
    };

}();
