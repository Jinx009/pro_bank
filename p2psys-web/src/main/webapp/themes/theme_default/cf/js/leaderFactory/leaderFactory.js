$(function(){
	getData();
})

/**
 * 拉取列表
 */
function getData(){
	$.ajax({
		url:"/cf/leaderFactory/model.action",
		type:"POST",
		dataType:"json",
		success:function(res){
			if("success"==res.result){
				console.log(res)
			}
		}
	})
}