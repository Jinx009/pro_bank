$(function(){
	var status = $("#status").val();
	var projectId = $("#projectId").val();
	if("0"==status){
		$("#leaderStatus").html("尚未申请");
		$.ajax({
			url:"/cf/leader/singleData.action",
			type:"POST",
			dataType:"json",
			success:function(res){
				if(null!=res.errorMsg){
					$("#info").val(res.errorMsg[0].info);
					$("#reason").val(res.errorMsg[0].reason);
					$("#name").val(res.errorMsg[0].name)
				}
			}
		})
	}else{
		$("#leaderStatus").html("已申请");
		$("#leaderStatus").css("cursor","default");
		$("#beBtn").css("display","none");
		var params = "projectId="+projectId;
		$.ajax({
			url:"/cf/leader/singleProjectData.action",
			data:params,
			type:"POST",
			dataType:"json",
			success:function(res){
				if(null!=res.errorMsg){
					$("#info").attr("readonly",true);
					$("#reason").attr("readonly",true);
					$("#name").attr("readonly",true);
					$("#info").val(res.errorMsg[0].info);
					$("#reason").val(res.errorMsg[0].reason);
					$("#name").val(res.errorMsg[0].name);
				}
			}
		})
	}
})

/**
 * 返回
 */
function goBack(){
	var projectId = $("#projectId").val();
	location.href = "/pro/detail.html?id="+projectId;
}

/**
 * 执行申请
 */
function doBe(){
	var projectId = $("#projectId").val();
	var reason = $("#reason").val();
	var info = $("#info").val();
	var name = $("#name").val();
	var params = "id="+projectId+"&reason="+reason+"&info="+info+"&name="+name;
	
	$.ajax({
		url:"/cf/user/saveLeader.action",
		type:"POST",
		data:params,
		dataType:"json",
		success:function(res){
			if("success"===res.result){
				layer.alert('恭喜您，申请成功！',{title:false,closeBtn: 0},function(){
					goReload();
				});
			}else{
				layer.alert(res.errorMsg,{title:false,closeBtn: 0});
			}
		}
	})
}

/**
 * 重载页面
 */
function goReload(){
	location.reload();
}

/**
 * 隐藏弹窗
 */
function hideAlert(){
	$("#errorDiv").hide();
}