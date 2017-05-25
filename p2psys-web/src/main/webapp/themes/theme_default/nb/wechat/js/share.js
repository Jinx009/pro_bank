$(function()
{
	var size = $("#size").val();
	var wechatUrl = $("#wechatUrl").val();
	
	if(parseInt(size)>=2)
	{
		$("#main").attr("onclick","openUrl('"+wechatUrl+"')");
	}
	else
	{
		$("#main").attr("onclick","openUrl('/nb/wechat/account/main.action')");
	}
})
function openUrl(url)
{
	location.href = url;
}