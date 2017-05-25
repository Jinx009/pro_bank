$(function(){
	loadForms();
	showLi('leader_li');
	var status = $("#status").val();
	if("0"!=status){
		$("#beBtn").html("保存");
		getSelfData();
	}
	getTrade();
})

/**
 * 触发选择图片
 */
function getSub(){
	$("#file").click();
}

/**
 * 触发表单提交
 */
function subForm(){
	$("#form").submit();
}

/**
 * 表单初始化
 */
function loadForms(){
	$("#form").ajaxForm({
		success : function(data) {
			console.log(data)
			if(null!=data&&""!=data){
				$("#headerImg").attr("src",data);
			}
		},
		complete : function(t) {
		}
	});
}
/**
 * 我的优秀领投人详情
 */
function getSelfData(){
	$.ajax({
		url:"/cf/user/factoryDetail.action",
		type:"POST",
		dataType:"json",
		success:function(res){
			if("success"==res.result){
				if(null!=res.error_time&&""!=res.error_time){
					for(var i = 0;i<res.error_time.length;i++){
						$("#flag"+res.error_time[i].flag.id).prop("checked","checked");
					}
				}
				if("0"==res.errorMsg.status){
					$("#goodStatus").html("申请中");
				}else{
					$("#goodStatus").html("审核通过");
				}
				if("1"==res.errorMsg.picUrl){
					$("#headerImg").attr("src",adminUrl+res.errorMsg.picPath);
				}else{
					$("#headerImg").attr("src",res.errorMsg.picPath);
				}
				$("#name").val(res.errorMsg.name);
				$("#name").attr("readonly",true);
				$("#info").val(res.errorMsg.info);
				$("#reason").val(res.errorMsg.reason);
				$("#history").val(res.errorMsg.history);
			}
		}
	})
}

/**
 * 获取行业信息
 */
function getTrade(){
	$.ajax({
		url:"/cf/flagData.action",  
		type:"POST",
		dataType:"json",
		success:function(res){
			if(res.errorMsg){
				var htmlStr = "";
				htmlStr += "<div class='row' >";
				for(var i = 0;i<res.errorMsg.length;i++){
					  htmlStr += "<div class='col-md-3' ><div class='checkbox' ><lable>";
					  htmlStr += "<input type='checkbox' id=flag"+res.errorMsg[i].id+" name='flag' value='"+res.errorMsg[i].id+"' >"+res.errorMsg[i].name;
					  htmlStr += "</lable></div></div>";
				}
				htmlStr += "</div>";
				$("#flagData").html(htmlStr);
			}
		}
	})
}

/**
 * 保存
 */
function doBe(){
	var name = $("#name").val();
	var info = $("#info").val();
	var reason = $("#reason").val();
	var history = $("#history").val();
	var id = $("#status").val();
	var picPath  = $("#headerImg").attr("src");
	var flag = "";
	$('input[name="flag"]').each(function(){
		if("checked"==$(this).is(":checked")||true==$(this).is(":checked")){
			flag += $(this).val()+"str";
		}
	})
	var params = "name="+name+"&info="+info+"&reason="+reason+"&flag="+flag+"&history="+history+"&id="+id+"&picPath="+picPath;
	$.ajax({
		url:"/cf/leaderFactory/saveLeaderFactory.action",
		type:"POST",
		data:params,
		dataType:"json",
		success:function(res){
			if("success"==res.result){
				layer.alert('操作成功！！',{title:false,closeBtn: 0},function(){
					goReload();
				});
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
 * 返回
 */
function goBack(){
	var redirectUrl = $("#redirectUrl").val();
	if(null!=redirectUrl&&""!=redirectUrl){
		location.href = redirectUrl;
	}else{
		location.href = "/leaderFactory.html";
	}
}