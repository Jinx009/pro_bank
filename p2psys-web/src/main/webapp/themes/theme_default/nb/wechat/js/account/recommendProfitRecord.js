/*$(function()
{
	$.ajax({
		url:"/nb/wechat/account/recommendProfitRecordData.action",
		type:"GET",
		dataType:"json",
		success:function(res)
		{
			if(res.data)
			{
				if(res.data.length>0)
				{
					for(var i = 0;i<res.data.length;i++)
					{
						var htmlStr = "";
						htmlStr += "<tr>";
						htmlStr += "<td>"+res.data[i].userName+"</td>";
						htmlStr += "<td>"+res.data[i].money+"</td>";
						htmlStr += "<td>"+jsDateTime(res.data[i].addTime)+"</td>";
						htmlStr += "</tr>";
						
						$("#data_info").append(htmlStr);
					}
				}
				else
				{
					$("#data_info").append("<tr><td  colspan=3 >暂无推荐收益.</td></tr>");
					$("#btn").html("快去邀请好友吧");
				}
			}
		}
	})
})*/

define(function(require,exports,modlue)
{
	require('jquery');
	//通用显示函数
		$.ajax({
		type:'get',
		url:'/nb/wechat/account/recommendProfitRecordData.html',
		dataType:'json',
		success:function(json)
		{
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js',function()
			{
					require.async('/plugins/handlebars-v1.3.0/transFormatJson',function()
					{
						var tpl = require('/themes/theme_default/nb/pc/tpl/member/wechatRecommendProfit.tpl');
						var template = Handlebars.compile(tpl);
						var html = template(json);	
						$('#table_content').html(html);
					});
			});
			
			//分页插件
			if(json.errorMsg.page.pages > 0)
			{
				require.async(['/plugins/pager/pager.css','/plugins/pager/pager'],function()
				{
					kkpager.generPageHtml(
					{
						pno : json.errorMsg.page.currentPage,
						total : json.errorMsg.page.pages,
						totalRecords : json.errorMsg.page.total,
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
					        	url:'/nb/wechat/account/recommendProfitRecordData.html'+"?page="+n,
					        	dataType:"json",
					        	success:function(json)
					        	{
					        		require.async(['/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js','/plugins/handlebars-v1.3.0/transFormatJson'],function()
					        		{
						        		var tpl = require('/themes/theme_default/nb/pc/tpl/member/wechatRecommendProfit.tpl');
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
				$("#kkpager").html("<p class='gray-error' ></p>");
			}
		},error:function(){}
	});	
});		
/**
 * 时间戳转换
 * 
 * @param unixtime
 * @returns
 */
function jsDateTime(unixtime)  
{  
	 var date = new Date(unixtime);
	 
	 return  date.format("yyyy-MM-dd"); 
}  
Date.prototype.format = function(format)
{
	var o = 
	{
	"M+" : this.getMonth()+1, 
	"d+" : this.getDate(), 
	"h+" : this.getHours(),
	"m+" : this.getMinutes(), 
	"s+" : this.getSeconds(), 
	"q+" : Math.floor((this.getMonth()+3)/3), 
	"S" : this.getMilliseconds() 
	}

	if(/(y+)/.test(format)) 
	{
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	}

	for(var k in o) 
	{
		if(new RegExp("("+ k +")").test(format))
		{
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
		}
	}
	return format;
} 