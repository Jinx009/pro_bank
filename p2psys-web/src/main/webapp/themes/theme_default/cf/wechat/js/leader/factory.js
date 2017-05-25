$(function(){
	$('.product-detail-btn').bind('click',function(){
		location.href = '/cf/wechat/user/leaderFactory.html';
	})
	var size = $('#size').val();
	if(0==size){
		$('.product-detail-btn').html('申请领投人智库');
	}else{
		$('.product-detail-btn').html('我的智库资料');
	}
	$.ajax({
		url:'/cf/wechat/leaderFactoryData.action',
		type:'GET',
		dataType:'json',
		success:function(res){
			var data = res.data;
			if(null!=data){
				for(var i in data){
					if(null==data[i].picPath||''==data[i].picPath){
						data[i].picPath = '/themes/theme_default/cf/wechat/img/ltr_img.png';
					}else{
						if(1==data[i].picUrl){
							data[i].picPath = adminUrl+data[i].picPath;
						}
					}
				}
			}  
    		var htmlStr = '';
    		for(var i = 0;i<data.length;i++){
    			htmlStr += '<div class="grid" >';
    			htmlStr += '<div class="imgholder">';
    			htmlStr += '<img src="'+data[i].picPath+'" />';
    			htmlStr += '</div>';
    			htmlStr += '<strong>'+data[i].name+'</strong>';
    			htmlStr += '<p>擅长行业：<span>'+data[i].flagNames+'</span></p>';
    			htmlStr += '<div class="meta" >领投人简介：'+data[i].info+'</div>';
    			htmlStr += '</div>';
    		}
    		$('.attention-content').css('display','none');
    		$('.attention-content').html(htmlStr);
    		window.setTimeout(function(){
    		    $('.attention-content').BlocksIt({  
    		           numOfCol :2,//列数  
    		           offsetX : 5,//x轴间距  
    		           offsetY : 5//y轴间距  
    		     });  
    		},2000); 
    		$('.attention-content').css('display','block');
		}
	})
})