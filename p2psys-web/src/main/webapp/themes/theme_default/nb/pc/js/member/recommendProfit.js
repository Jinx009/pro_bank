define(function(require,exports,modlue)
{
	require('jquery');
	//通用显示函数
		$.ajax({
		type:'get',
		url:'/nb/recommend/recommendProfitRecordList.html?time='+jsStrToTime(),
		dataType:'json',
		success:function(json)
		{
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js',function()
			{
					require.async('/plugins/handlebars-v1.3.0/transFormatJson',function()
					{
						var tpl = require('/themes/theme_default/nb/pc/tpl/member/recommendProfit.tpl');
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
					        	url:'/nb/recommend/recommendProfitRecordList.html'+"?page="+n+"&time="+jsStrToTime(),
					        	dataType:"json",
					        	success:function(json)
					        	{
					        		require.async(['/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js','/plugins/handlebars-v1.3.0/transFormatJson'],function()
					        		{
						        		var tpl = require('/themes/theme_default/nb/pc/tpl/member/recommendProfit.tpl');
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