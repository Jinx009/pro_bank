function goLoginPage()
{
	var user_id = $("#user_id").val();
	
	if(""==user_id||null==user_id)
	{
		location.href = "/wx/login.html?timeout=1&redirectURL=/wx/account/crowdfunding.html";
	}
	else
	{
		location.href = "/wx/account/crowdfunding.html";
	}
}
function goMorePage()
{
	var user_id = $("#user_id").val();
	
	if(""==user_id||null==user_id)
	{
		location.href = "/wx/login.html?timeout=1&redirectURL=/wx/account/setting.html";
	}
	else
	{
		location.href = "/wx/account/setting.html";
	}
}
function goThisMorePage()
{
	location.href = "/wx/account/setting.html";
}
$(document).ready(function() 
{
		$("#ichi").hover(function() 
		{
			$("#ichi").css("color", "#81D8D0");
			$("#ichiichi").attr("src","/themes/theme_default/wx/img/home_pressed.png");
		},
		function() 
		{
			$("#ichiichi").attr("src","/themes/theme_default/wx/img/home_normal.png");$("#ichi").css("color", "#FFFFFF");});
			$("#ni").hover(function() 
			{
				$("#ni").css("color", "#81D8D0");
				$("#nini").attr("src","/themes/theme_default/wx/img/lise_pressed.png");
				$(".dd").show();
			},
			function() 
			{
				$("#nini").attr("src","/themes/theme_default/wx/img/lise_normal.png");
				$("#ni").css("color", "#FFFFFF");
		});
		$("#san").hover(function() 
		{
			$("#san").css("color", "#81D8D0");
			$("#sansan").attr("src","/themes/theme_default/wx/img/account_pressed.png");
		},
		function() 
		{
			$("#sansan").attr("src","/themes/theme_default/wx/img/account_normal.png");
			$("#san").css("color", "#FFFFFF");
		});
		$("#yon").hover(function() 
		{
			$("#yon").css("color", "#81D8D0");
			$("#yonyon").attr("src","/themes/theme_default/wx/img/more_pressed.png");
		},
		function() 
		{
			$("#yonyon").attr("src","/themes/theme_default/wx/img/more_normal.png");
			$("#yon").css("color", "#FFFFFF");
		});
		$('.dd').hover(function() 
		{
			$('.header,.swiper-container').addClass('veil');
		}, 
		function() 
		{
			$('.header,.swiper-container').removeClass('veil');
			$('.dd').hide();
		});
								
		$("#ichi").click(function()
		{
			window.location.href="/wechat/crowdfunding/index.html";
		});
});