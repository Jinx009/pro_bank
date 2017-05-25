$(function(){
	$.ajax({
		url:'/cf/wechat/user/main.action',
		type:'GET',
		dataType:'json',
		success:function(res){
			var data = {};
			data.account = res.account.useMoney;
			data.money = res.money;
			data.attention = res.attention;
			data.order = res.order;
			drawProcess('￥'+data.money,'投资资产',data.money/(data.money+data.account)*100);
			//创建模型
            var model = new Vue({
                el: 'body',
                data: {
                    model: data
                }
            });
		}
	})
})

/**
 * 页面跳转
 * @param url
 */
function openUrl(url){
	location.href = url;
}

function loginOut(){
	$.ajax({
		url:'/cf/loginOut.action',
		type:'POST',
		dataType:'json',
		success:function(res){
			if('success'==res.result){
				var localStorage = window.localStorage;
				localStorage.setItem("mobile_",'');
				localStorage.setItem("md5_",'');
				location.href = '/cf/wechat/pro/index.action?id=2';
			}
		}
	})
}