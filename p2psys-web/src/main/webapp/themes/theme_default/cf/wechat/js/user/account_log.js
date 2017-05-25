$(function(){
	$.ajax({
		url:'/cf/wechat/accountLog.action',
		type:'GET',
		dataType:'json',
		success:function(res){
			if(200==res.code){
				//创建模型
	            var model = new Vue({
	                el: 'body',
	                data:{
	                    items: res.data,
	                }
	            });
	            if(null!=res.data){
	            	$('#'+res.data[0].id).css('display','block');
	            }
			}else{
				alert(res.data);
			}
		}
	})
})

/**
 * 显示更多
 * @param id
 */
function showDetail(id){
	$('.bottom-div').css('display','none');
	$('#'+id).css('display','block');
}