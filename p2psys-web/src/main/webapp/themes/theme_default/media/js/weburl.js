var back_web_url = "http://zchomestaging.teamserver.cn";
var front_web_url = "http://zcstaging.teamserver.cn";

$(function()
{
	$.ajax({
		url:"/crowdfunding/getConfig.html",
		type:"GET",
		dataType:"json",
		ansyc:false,
		success:function(res)
		{
			back_web_url = res.admin;
			front_web_url = res.web;
		}
	})
})
