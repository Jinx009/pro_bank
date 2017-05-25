var data;
$(function()
{
	if(!isWechat())
	{
		$("#login_out").show();
	}
	
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
})


function changeHtml()
{
	var htmlStr = "";
	
	for(var i = 0;i<data.user_list.length;i++)
	{
		if(null!=data.user_list[i].userName&&""!=data.user_list[i].userName)
		{
			htmlStr += "<div class='row'  >";
			htmlStr += "<img src='/themes/theme_default/nb/wechat/img/setLoginPwd.png' class='row_icon'/>";
			htmlStr += "<span>"+data.user_list[i].userName+"</span>";
		    htmlStr += "<span class='row_value' id='bank_type' ) ></span></div>";
		    //onclick=changePwd('"+data.user_list[i].userName+"'
		}
	}
	$("#main").append(htmlStr);
}
/**
 * 忘记密码
 * 
 * @param tel
 */
function changePwd(tel)
{
	location.href = "/nb/wechat/forgetPwd.html?tel="+tel+"&redirectURL=/nb/wechat/account/user.html";
}