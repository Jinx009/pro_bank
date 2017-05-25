$(function(){
	$.ajax({
		url:'/cf/wechat/user/walletData.action',
		type:'POST',
		dataType:'json',
		success:function(res){
			var data = res.data;
			//创建模型
            var model = new Vue({
                el: 'body',
                data:{
                    data:data
                }
            });
		}
	})
})