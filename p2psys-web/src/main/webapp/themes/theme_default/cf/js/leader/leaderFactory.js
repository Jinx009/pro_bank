$(function(){
	showLi('leader_li');
	$('.ltr-list-content').css('background','rgb(243,243,243)');
	getData();
})

/**
 * 获取数据
 */
function getData(){
	$.ajax({
		url:"/cf/leaderFactory/model.action",
		type:"POST",
		dataType:"json",
		success:function(res){
			var htmlStr = "";
			if(null!=res.errorMsg){
				for(var i = 0;i<res.errorMsg.length;i++){
					htmlStr += '<div class="grid">';
					htmlStr += '<div class="imgholder">';
					if(null==res.errorMsg[i].picPath||""==res.errorMsg[i].picPath){
						htmlStr += '<img src="/themes/theme_default/cf/img/login_icon.png" /> ';
					}else{
						if("0"==res.errorMsg[i].picUrl){
							htmlStr += '<img src="'+res.errorMsg[i].picPath+'" /> ';
						}else{
							htmlStr += '<img src="'+adminUrl+res.errorMsg[i].picPath+'" /> ';
						}
					}
					htmlStr += '</div>';
					htmlStr += '<strong>'+res.errorMsg[i].name+'</strong>';
					htmlStr += '<p>擅长行业：<span>'+res.errorMsg[i].flagNames+'</span></p>';
					htmlStr += '<div class="meta">领投人简介：'+res.errorMsg[i].info+'</div>';
					htmlStr += '</div>';
				}
			}
			$('#leaderData').html(htmlStr);
		   $('#leaderData').BlocksIt(  
	       {  
	           numOfCol :4,//列数  
	           offsetX : 5,//x轴间距  
	           offsetY : 25//y轴间距  
	       }); 
		}
	})
}