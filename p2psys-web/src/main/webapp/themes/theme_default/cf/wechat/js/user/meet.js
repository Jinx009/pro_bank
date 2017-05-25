$(function(){
	$.ajax({
		url:'/cf/wechat/user/meetData.action',
		dataType:'json',
		type:'POST',
		success:function(res){
			var data = {};
			data.status = res.data;
			if(1==data.status){
				data.content = '您已经是合格投资人了';
			}else{
				$('#meetBtn').attr('onclick','beInvest()');
				data.content = '我同意以上条件点击成为合格投资人';
			}
			//构建模型
			 var model = new Vue({
                el: 'body',
                data:{
                    item:data
                }
            });
		}
	})
})
/**
 * 返回前置页面
 */
function goBack(){
	var url = $("#redirectUrl").val();
	if(null==url||""==url){
		url = "/cf/wechat/user/setting.action";
	}
	location.href = url;
}


/**
 * 成为合格投资人
 */
function beInvest(){
	$.ajax({
		url:"/cf/user/beInvester.action",
		type:"POST",
		dataType:"json",
		success:function(res){
			layer.alert('恭喜您成为合格投资人！',{title:false,closeBtn: 0},function(){
				location.reload();
			});
		}
	})
}