var leaderId = 0;
$(function(){
	var id = $('#id').val();
	var params = 'id='+id;
	$.ajax({
		url:'/cf/wechat/beLeaderData.action',
		type:'POST',
		dataType:'json',
		data:params,
		success:function(res){
			if(200==res.code){
				if(null!=res.data){
					$('#name').val(res.data.name);
					$('#info').val(res.data.info);
					$('#reason').val(res.data.reason);
					leaderId = res.data.id;
				}
				var data = {};
				data = res;
				//创建模型
	            var model = new Vue({
	                el: 'body',
	                data: {
	                    model:data
	                }
	            });
			}
		}
	})
})

/**
 * 申请数据提交
 * @param type
 */
function sendData(){
	var projectId = $('#id').val();
	var name = $('#name').val();
	var info = $('#info').val();
	var reason = $('#reason').val();
	var leaderId = leaderId;
	var params = 'leaderId='+leaderId+'&name='+name+'&info='+info+'&reason='+reason+'&projectId='+projectId;
	
	$.ajax({
		url:'/cf/wechat/user/saveLeaderData.action',
		type:'POST',
		data:params,
		dataType:'json',
		success:function(res){
			if(200==res.code){
				layer.alert('保存成功！',{title:false,closeBtn: 0},function(){
					location.reload();
				});
			}
		}
	})
}