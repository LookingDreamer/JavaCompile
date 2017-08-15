(function($) {

/**
 *
 * ͼƬ�ϴ�ǰԤ�����
 * @author ��³�
 * @date 2014-12-8
 * 
 */
 $.fn.uploadPreview = function(opts){
	 if (this.length < 1){
		 return this;
	 }
	
	var $this = $(this);
	var msie = $.fn.uploadPreview.msie;// IE�汾

	opts = $.extend({
			container: '#preView',
			css:{},		 //���ð�ť����ʽmap����
			text:'���ͼƬ', //��ť�ϵ�����
			width: '100%', //ͼƬ���,���width��heightֻ��������һ��,
						 //��û�����õ����԰��ȱ�����,��Ϊnull����ʾԭʼͼ���С
						 //�������ðٷֱ�(���������Ĵ�С��������������)
			height: '100%',//ͼƬ�߶�, ���ֻ�string����
			imgType: ["gif", "jpeg", "jpg", "bmp", "png"]//֧�ֵ�ͼƬ��ʽ
		}, opts || {});

	

	if(msie) { //IE �����
		if(msie == 6) { //IE6
			//<change>
			$.fn.uploadPreview.loopInput($this, opts, function (input) {
				$(input).change({opts:opts}, function() {
					if(!$.fn.uploadPreview.checkFileType(input.value, opts)) {
						input.value = '';
						return false;
					}

					$.fn.uploadPreview.getImage(opts).attr('src', 'file:///' + input.value);
				});
				
			});
			
			//</change>
			return $this;
		}
		
		// ����IE6
		//<change>
		$.fn.uploadPreview.loopInput($this, opts, function (input) {
			$(input).change({opts:opts}, function() {
				if(!$.fn.uploadPreview.checkFileType(input.value, opts)) {
					input.value = '';
					return false;
				}
				
				var innerDiv = $('<div/>');
				var container = $(opts.container);
				container.empty().prepend(innerDiv);

				input.select();
				input.blur();
				var imgSrc = document.selection.createRange().text;

				try {
						innerDiv.css('filter', 'progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)');
						innerDiv[0].filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
						
						innerDiv.css($.fn.uploadPreview.getImageWH($(opts.container), opts.width, opts.height, imgSrc));
					} catch (e) {
						alert("���ϴ���ͼƬ��ʽ����ȷ��������ѡ��!");
						return false;
					}
					
					$('.upload-pre-view-image', container).hide();
					document.selection.empty();
			});

		});
		
		//</change>

		return $this;
	}

	//��IE�����
	//<change>
	$.fn.uploadPreview.loopInput($this, opts, function (input) {
		$(input).change({opts:opts}, function (){
			if(!$.fn.uploadPreview.checkFileType(input.value, opts)) {
				input.value = '';
				return false;
			}

			$.fn.uploadPreview.getImage(opts).attr('src', URL.createObjectURL(input.files[0]));
		});
	});
	
	//</change>
	return $this;
 };

//���IE�汾,����ÿ�λ�ȡ,�����ڳ�ʼ��ʱִֻ��һ��
 $.fn.uploadPreview.msie = (function() {
		var undef,
		v = 3,
		div = document.createElement('div'),
		all = div.getElementsByTagName('i');
		while ( div.innerHTML = '<!--[if gt IE ' + (++v) + ']><i></i><![endif]-->', all[0]);
		return v > 4 ? v : undef;
})();

//����ļ�����
$.fn.uploadPreview.checkFileType = function(value, opts){
	if(value) {
		 if (!RegExp('\.(' + opts.imgType.join('|') + ')$', 'i').test(value)) {
			alert('ͼƬ���ͱ�����' + opts.imgType.join(', ') + '�е�һ��');
			return false;
		}
		return true;
	}

	return false;
};

//��ȡͼƬ����
$.fn.uploadPreview.getImage = function(opts){
	var img = $('.upload-pre-view-image', opts.container); //��ȡͼƬ
	if(img.length > 0) {
		return img;
	}

	var wh = {};
	var container = $(opts.container);

	if(opts.width) {
		if(isNaN(opts.width)) {
			opts.width = $.trim(opts.width);
			if(opts.width.lastIndexOf('%') != -1) {
				wh.width = ((container.width() * (opts.width.split('%')[0]-0))/100) + 'px';
			} else {
				wh.width = opts.width;
			}
		} else {
			wh.width = opts.width + 'px';
		}
	}

	if(opts.height) {
		if(isNaN(opts.height - 0)) {
			opts.height = $.trim(opts.height);
			if(opts.height.lastIndexOf('%') != -1) {
				wh.height = ((container.height() * (opts.height.split('%')[0]-0))/100) + 'px';
			} else {
				wh.height = opts.height;
			}
		} else {
			wh.height = opts.height + 'px';
		}
	}

	img = $('<img />');
	img.addClass('upload-pre-view-image');
	img.css(wh);//���ø߿�

	$(opts.container).empty().prepend(img);
	img = $('.upload-pre-view-image', opts.container); //��ȡͼƬ
	return img;
};

//��ȡͼƬ��Ҫ���ŵĸߺͿ�
$.fn.uploadPreview.getImageWH = function (container, width, height, imgSrc){
	var wh = {}; //�洢ͼƬ��Ҫ���ŵĿ�͸�

	if(width) { //���width��ֵ

		if(isNaN(width - 0)) {
			width = $.trim(width);
			if(width.lastIndexOf('%') != -1) {
				wh.width = ((container.width() * (width.split('%')[0]-0))/100) + 'px';
			} else {
				wh.width = width;
			}
		} else {
			wh.width = width + 'px';
		}

		if(height) { //�߶���ֵ,��ֱ�ӷ���ָ��ֵ,����Ҫ���Ŵ���
			if(isNaN(height - 0)) {
				height = $.trim(height);
				if(height.lastIndexOf('%') != -1) {
					wh.height = ((container.height() * (height.split('%')[0]-0))/100) + 'px';
				} else {
					wh.height = height;
				}
			} else {
				wh.height = height + 'px';
			}
			return wh;
		} 

		//����ͼƬ��Ҫ���ŵĴ�С
		var img = new Image(); //����һ������
		img.src = imgSrc; //ͼƬ��С

		var w = wh.width.toLowerCase().split('px')[0] - 0;
		if(w != img.width) {
			wh.height = ((w/img.width) * img.height) + 'px';
		}
		return wh;
	}

	//height
	if(height) {

		if(isNaN(height - 0)) {
			wh.height = height;
		} else {
			wh.height = height + 'px';
		}

		//����ͼƬ��Ҫ���ŵĴ�С
		var img = new Image(); //����һ������
		img.src = imgSrc; //ͼƬ��С

		var h = wh.height.toLowerCase().split('px')[0] - 0;
		if(img.height != h) {
			wh.width = ((h/img.height) * img.width) + 'px';
		}

		return wh;
	}

	//�����Ⱥ͸߶ȶ�����Ϊnull,������ΪͼƬ��ԭʼ��С
	//����ͼƬ��Ҫ���ŵĴ�С
	var img = new Image(); //����һ������
	img.src = imgSrc; //ͼƬ��С
	if(img.width) wh.width = img.width;
	if(img.height) wh.height = img.height;

	return wh;
};

//ѭ��inputԪ��
$.fn.uploadPreview.loopInput = function (obj, opts, callback){
	
	var file;
	for (var i=0; i< obj.length; i++ ) {
		file = obj[i];
		
		if (file.tagName.toLowerCase() == 'input' && $(file).attr('type').toLowerCase() == 'file' ){
			$(file).wrap('<span class="upload-pre-view"></span>').before('<span>' + opts.text + '</span>');
			$(file).parent().css(opts.css);
			callback(file);
		} 
	
	}
};


$('head').append(function(){
	return '<style>.upload-pre-view{-moz-user-select:none;border:1px solid transparent;border-radius:3px;cursor:pointer;display:inline-block;font-size:13px;line-height:1.42857;margin-bottom:0;padding:5px 12px;text-align:center;vertical-align:middle;white-space:nowrap;overflow:hidden;position:relative;background-color:#5cb85c;border-color:#4cae4c;color:#fff;}.upload-pre-view:hover{color:#FFF;background-color:#449D44;border-color:#398439;}.upload-pre-view input{width:9999px;display:block;cursor:pointer;direction:ltr;font-size:200px;margin:0;opacity:0;filter:alpha(opacity=0);height:100%;*+font-size:130%;position:absolute;right:0;top:0;font-family:inherit;}</style>'
});

})(jQuery);