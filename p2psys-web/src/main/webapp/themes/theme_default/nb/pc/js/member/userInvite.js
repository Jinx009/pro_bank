define(function(require,exports,modlue)
{
	require('jquery');
	require.async('/themes/theme_default/nb/pc/js/jquery.qrcode.min.js',function()
	{
		$.ajax({
			url:"/nb/QRcode.action?time="+jsStrToTime(),
			type:"GET",
			dataType:"json",
			ansyc:false,
			success:function(res)
			{
				var urlData = JSON.parse(res.data);
				var counponCode = res.counponCode;
				
				$('#qrcodeCanvas').html("");
					
				$('#qrcodeCanvas').qrcode(
				{
					width:120,
					height:120,
					text:urlData.tinyurl
				});
			}
		})
	})
	
	require.async("/plugins/ZeroClipboard/ZeroClipboard",function()
	{
		var clip = new ZeroClipboard( document.getElementById("inviteUrlCopy"), 
		{
			moviePath: "/plugins/ZeroClipboard/ZeroClipboard.swf"
		});
		clip.setText($("#inviteUrl").val());
		clip.glue($("#inviteUrlCopy"));
		clip.addEventListener( "complete", function()
		{   
			showAlertDiv(true,"复制成功!","","");
		});
	})
	
	//通用显示函数
		$.ajax({
		type:'get',
		url:'/nb/userInviteList.html?time='+jsStrToTime(),
		dataType:'json',
		success:function(json)
		{
			var sessionStorage = window.sessionStorage;
			var webUrl = sessionStorage.getItem("webUrl");
			
			$("#inviteCode").html("3.直接告诉朋友您的邀请码:<span style='color:#ff5454;font-weight:bold;font-size:16px;margin-left:5px;'>"+json.errorMsg+"</span>");
			$("#inviteUrl").val(webUrl+"?regUi="+json.errorMsg);
			
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js',function()
			{
					require.async('/plugins/handlebars-v1.3.0/transFormatJson.js',function()
					{
						var tpl = require('/themes/theme_default/nb/pc/tpl/member/userInviteList.tpl');
						var template = Handlebars.compile(tpl);
						var html = template(json);	
						$('#table_content').html(html);
					});
			});
			
			//分页插件
			if(json.data.page.pages > 0)
			{
				require.async(['/plugins/pager/pager.css','/plugins/pager/pager'],function()
				{
					kkpager.generPageHtml(
					{
						pno : json.data.page.currentPage,
						total : json.data.page.pages,
						totalRecords : json.data.page.total,
						isShowFirstPageBtn:false, 
						isShowLastPageBtn:false, 
						isShowTotalPage:false, 
						isShowTotalRecords:false, 
						isGoPage:false,
						lang:{prePageText:'<',nextPageText:'>'},
						mode:'click',
						click:function(n,total,totalRecords)
						{
					        $.ajax({
					        	type:"get",
					        	url:'/nb/userInviteList.html'+"?page="+n+"&time="+jsStrToTime(),
					        	dataType:"json",
					        	success:function(json)
					        	{
					        		$("#inviteCode").html("3.我的邀请码:"+json.errorMsg);
					        		
					        		require.async(['/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js','/plugins/handlebars-v1.3.0/transFormatJson'],function()
					        		{
						        		var tpl = require('/themes/theme_default/nb/pc/tpl/member/userInviteList.tpl');
										var template = Handlebars.compile(tpl);
										var html  = template(json);
										$('#table_content').html(html);
									});
					        	}
					       });
							this.selectPage(n); 
						}
					});
				});
			}
			else
			{
				$("#kkpager").html("<p class='gray-error' >暂无数据!</p>");
			}
		},error:function(){}
	});	
});		