$(function(){
	$.ajax({
		url:'/manage/code/myDreamData.action',
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

/**
 * 募集完成
 * @param id
 */
function pass(id){
	layer.confirm('确定设定募集成功，成功后将无法继续募集？', {
	    btn: ['确定','取消'],title:false,closeBtn: 0 
	}, function(){
		$.ajax({
			url:'/manage/code/passProject.action',
			data:'id='+id,
			dataType:'json',
			type:'POST',
			success:function(res){
				if(200==res.code){
					layer.closeAll();
					layer.alert('设置成功！',{title:false,closeBtn:0},function(){
						$('#tr'+id).remove();
						layer.closeAll();
					})
				}else{
					layer.closeAll();
					layer.alert(res.data,{title:false,closeBtn:0});
				}
			}
		})
	});
}

/**
 * 撤销项目
 * @param id
 */
function cacle(id){
	layer.confirm('确定要撤销该项目吗？', {
	    btn: ['确定','取消'],title:false,closeBtn: 0 
	}, function(){
		$.ajax({
			url:'/manage/code/cacleProject.action',
			data:'id='+id,
			dataType:'json',
			type:'POST',
			success:function(res){
				if(200==res.code){
					layer.closeAll();
					layer.alert('撤销成功！',{title:false,closeBtn:0},function(){
						$('#tr'+id).remove();
						layer.closeAll();
					})
				}else{
					layer.closeAll();
					layer.alert(res.data,{title:false,closeBtn:0});
				}
			}
		})
	});
}
/**
 * 查看项目
 * @param id
 */
function look(id){
	location.href = "/manage/code/edit.html?id="+id;
}

/**
 * 
 * 查看订单
 * @param id
 */
function order(id){
	window.open('/manage/code/order.html?id='+id);
}
/**
 * 设置领投人
 * @param id
 */
function leader(id){
	window.open('/manage/code/leader.html?id='+id);
}