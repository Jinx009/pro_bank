$(function(){
	$('#cacleBtn').bind('click',function(){
		location.href = '/cf/dream.html';
	})
	$.ajax({
		url:'/manage/approvalStatus.action',
		type:'POST',
		dataType:'json',
		success:function(res){
			var data = res.data;
			var model =new Vue({
	            el: 'body',
	            data: {
	                data:data
	            }
	        });
		}
	})
	loadForms();
	showLi('dream_li');
})

/**
 * 资料提交
 */
function sendData(){
	var userIdentity = $('#name').val();
	var idCardPicPositive = $('#headerImg').attr('src');
	var idCardPicOther = $('#headerImg1').attr('src');
	var businessLicense = $('#headerImg2').attr('src');
	if('/themes/theme_default/cf/img/upload.png'==idCardPicPositive){
		layer.alert('身份证正面照未上传！',{title:false,closeBtn: 0});
	}else if('/themes/theme_default/cf/img/upload.png'==idCardPicOther){
		layer.alert('身份证反面照未上传！',{title:false,closeBtn: 0});
	}else{
		 var params = 'userIdentity='+userIdentity+'&idCardPicPositive='+idCardPicPositive+
		 			  '&idCardPicOther='+idCardPicOther+'&businessLicense='+businessLicense;
		 $.ajax({
			 url:'/manage/saveCache.action',
			 typr:'POST',
			 data:params,
			 dataType:'json',
			 success:function(res){
				 if(200==res.code){
					 layer.alert('资料提交成功，正在等待审核！',{title:false,closeBtn: 0},function(){
							reloadPage();
					 });
				 }else{
					 layer.alert(res.data,{title:false,closeBtn:0});
				 }
			 }
		 })
	}
}

/**
 * 刷新页面
 */
function reloadPage(){
	location.reload();
}
/**
 * 触发选择图片
 */
function getSub(cla){
	$('.'+cla).click();
}

/**
 * 触发表单提交
 */
function subForm(id){
	$('#'+id).submit();
}

/**
 * 上传图片
 */
function loadForms(){
	$('#form').ajaxForm({
		success : function(data) {
			if(null!=data&&''!=data){
				$('#headerImg').attr('src',data);
			}
		},
		complete : function(t) {
		}
	});
	
	$('#form1').ajaxForm({
		success : function(data) {
			if(null!=data&&''!=data){
				$('#headerImg1').attr('src',data);
			}
		},
		complete : function(t) {
		}
	});
	
	$('#form2').ajaxForm({
		success : function(data) {
			if(null!=data&&""!=data){
				$('#headerImg2').attr('src',data);
			}
		},
		complete : function(t) {
		}
	});
}