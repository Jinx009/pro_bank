var realId = 0;
$(function(){
	var data = {};
	//产品标签
	$.ajax({
		url:'/cf/wechat/flagData.action',
		type:'GET',
		dataType:'json',
		success:function(res){
			if(200==res.code){
				data.items = res.data;
			}
		}
	})
	//我的数据
	$.ajax({
		url:'/cf/wechat/user/leaderFactoryData.action',
		type:'GET',
		dataType:'json',
		success:function(res){
			data.obj = res.data;
			//是否申请过状态判断
			if(null!=res.data.factory){
				realId = res.data.factory.id;
				$('#name').val(res.data.factory.name);
				$('#info').val(res.data.factory.info);
				$('#reason').val(res.data.factory.reason);
				$('#history').val(res.data.factory.history);
			}else{
				$('#name').attr('readOnly',false);
			}
			console.log(data)
			//构建模型
			 var model = new Vue({
              el: 'body',
              data:{
                  model:data
              }
			});
			//产品标签判断
			if(null!=res.data.flag){
				for(var j in res.data.flag){
					$('#flag'+res.data.flag[j].flag.id).prop('checked','checked');
					$('#flag'+res.data.flag[j].flag.id).attr('checked','checked');
				}
			}
		}
	})
	
})


/**
 * 保存数据
 */
function sendData(){
	
	var name = $('#name').val();
	var info = $('#info').val();
	var reason = $('#reason').val();
	var history = $('#history').val();
	var id = realId;
	var flag = '';
	$('input[name="flag"]').each(function(){
		if("checked"==$(this).is(':checked')||true==$(this).is(':checked')){
			flag += $(this).val()+'str';
		}
	})
	var params = 'id='+id+'&name='+name+'&info='+info+'&reason='+reason+'&history='+history+'&flag='+flag;
	if(null==name||''==name){
		layer.alert('领投人姓名未填写！',{title:false,closeBtn: 0});
	}else if(null==info||''==info){
		layer.alert('个人简介未填写！',{title:false,closeBtn: 0});
	}else if(null==info||''==info){
		layer.alert('领投理由未填写！',{title:false,closeBtn: 0});
	}else{
		$.ajax({
			url:'/cf/wechat/user/saveFactory.action',
			type:'POST',
			data:params,
			dataType:'json',
			success:function(res){
				if(200==res.code){
					layer.alert('操作成功！',{title:false,closeBtn: 0},function(){
						location.reload();
					});
				}
			}
		})
	}
}