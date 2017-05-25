$(function(){
	$.ajax({
		url:'/manage/code/noticeData.action',
		type:'GET',
		dataType:'json',
		success:function(res){
			if(200==res.code){
				var model = new Vue({
	                el: 'body',
	                data:{
	                    item:res.data,
	                }
	            });
			}
		}
	})
})