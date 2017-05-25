$(function(){
	var status = $("#status").val();
	console.log(status)
	if("1"==status){
		$("#beInvester").attr("onclick","");
		$("#beInvester").html("您已经是合格投资人")
		/*$("#beInvester").removeClass("btn-info");
		$("#beInvester").addClass("btn-default");*/
		$(".checkbox").hide();
	}else{
		$(".checkbox").show();
		$("#beInvester").attr("onclick","beInvest()");
		$("#beInvester").html("若满足以上条件点击成为合格投资人");
	}
})

/**
 * 成为合格投资人
 */
function beInvest(){
	$.ajax({
		url:"/cf/user/beInvester.action",
		type:"POST",
		dataType:"json",
		success:function(res){
			if("success"==res.result){
				layer.alert('恭喜您成为合格投资人！',{title:false,closeBtn: 0},function(){
					goBuy();
				});
			}
		}
	})
}
/**
 * 成功后跳转
 */
function goBuy(){
	var redirectUrl = $("#redirectUrl").val();
	if(null!=redirectUrl&&""!=redirectUrl){
		location.href = redirectUrl;
	}else{
		location.reload();
	}
}