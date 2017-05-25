define(function(require,exports,module){
	require('jquery');
	$(function(){
		$(".view-con li:nth-child(3n)").css("background","none")
  	});
	
	var financeSiteId = window.location.search.substr(1);
	$("#expertIntro").attr("href","/lcschool/expert.html?"+financeSiteId)
	var ss = "/lcschool/enterFinanceSite.html?"+financeSiteId


	$.ajax({
		url:"/lcschool/enterFinanceSite.html?" + window.location.search.substr(1),
		type:"get",
		dataType:"json",
		success:function(data){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					var tpl = require('/themes/theme_default/media/tpl/viewList.tpl');//载入tpl模板
					var template = Handlebars.compile(tpl);
					var html = template(data);
					$(".view-con").html(html);
				})
			})
	

	
	if(data.data.page.pages > 0)
	{
		require.async(['/plugins/pager/pager.css','/plugins/pager/pager'],function(){
			kkpager.generPageHtml({
					pno : data.data.page.currentPage,//当前页码
					total : data.data.page.pages,//总页码
					totalRecords : data.data.page.total,//总数据条数
					isShowFirstPageBtn	: false, 
					isShowLastPageBtn	: false, 
					isShowTotalPage 	: false, 
					isShowTotalRecords 	: false, 
					isGoPage 			: false,
					lang:{
						prePageText				: '<',
						nextPageText			: '>'
					},
					mode:'click',//click模式匹配getHref 和 click
					click:function(n,total,totalRecords){
			        	$.ajax({
			        		type:"get",
			        		url:ss+"&page="+n,
			        		dataType:"json",//这个必不可少，如果缺少，导致传回来的不是json格式
			        		success:function(json){
			        			require.async(['/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js','/plugins/handlebars-v1.3.0/transFormatJson'],function(){
									var tpl = require('/themes/theme_default/media/tpl/viewList.tpl');
									var template = Handlebars.compile(tpl);
									var html    = template(json);
									$(".view-con").html(html);
								});
			        		}
			        	});
						this.selectPage(n); //处理完后可以手动条用selectPage进行页码选中切换
					}
			});
		});
	}else{
		$("#kkpager").html('暂无数据');
	}
}
	
	
	
	
	});
	
})



