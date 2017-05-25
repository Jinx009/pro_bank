$(function(){
	getData();
})

/**
 * 获取数据
 */
function getData(){
	$.ajax({
		url:'/manage/code/waitList.action',
		type:'GET',
		dataType:'json',
		success:function(res){
			if(200==res.code){
				var model = new Vue({
	                el: 'body',
	                data:{
	                    item:res,
	                }
	            });
			}
		}
	})
}
/**
 * 提交审核操作
 * @param id
 */
function addSubmit(id){
	layer.confirm('确定要提交审核？', {
	    btn: ['确定','取消'],title:false,closeBtn: 0 
	}, function(){
		$.ajax({
			url:'/manage/code/subProject.action',
			data:'id='+id,
			dataType:'json',
			type:'POST',
			success:function(res){
				if(200==res.code){
					layer.closeAll();
					layer.alert('提交审核成功！',{title:false,closeBtn:0},function(){
						location.reload();
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