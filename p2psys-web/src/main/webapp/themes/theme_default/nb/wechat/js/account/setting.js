var data;
$(function()
{
	if(!isWechat())
	{
		$("#login_out").show();
	}
	
	var payPwd = $("#payPwdStatus").html();
	var wechat = $("#wechat").val();
	var code = $("#code").html();
	
	$.ajax({
		url:"/nb/wechat/settingJson.html",
		type:"GET",
		dataType:"json",
		ansyc:false,
		success:function(res)
		{
			data = res;
			
			changeHtml();
		}
	})
	
	if(""==code||null==code)
	{
		$("#code").html("暂未拥有");
	}
	if("nothas"===payPwd)
	{
		$("#payPwdText").html("设置交易密码");
		$("#payPwdLink").attr("href","/nb/wechat/account/payPwdSetting.html?redirectURL=/nb/wechat/account/setting.action");
	}
	
	 if("success"===wechat)
	 {
	 	$("#wechat_div").show();
	 	$("#wechat_div_phone").show();
	 }
	 localStorage.type = "btn_one";
})

function changeHtml()
{
	if("0"==data.bank_num)
	{
		$("#bank_type").html("未绑定");
	}
	if("0"!=data.bank_num)
	{
		$("#bank_type").html("已绑定");
	}
}
/**
 * 邀请好友
 */
function getUser()
{
	location.href = $("#wechatUrl").val();
}
/**
 * 我的手机账号
 */
function goToUserList()
{
	location.href = "/nb/wechat/account/user.html";
}
/**
 * 登出
 */
function loginOut()
{
	location.href = "/nb/wechat/landOut.html";
}
/**
 * 取消绑定
 * */
function cacleBind()
{
	$.ajax({
		url:"/nb/wechat/account/cacleBind.action",
		type:"POST",
		dataType:"json",
		ansyc:false,
		success:function(res)
		{
			if("success"===res.result)
			{
				$("#mask_div").show();
				$("#success_div").show();
			}
		}
	})
}
function goBind()
{
	location.href = "/nb/wechat/account/800bank.action";
}