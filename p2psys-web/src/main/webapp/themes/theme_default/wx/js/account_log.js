define(function(require,exports,modlue){
	require('jquery');
	//转让弹窗
	

	ajaxUrl('/nb/wechat/account/logList.html');
	
	//通用显示函数
	function ajaxUrl(param){
		$.ajax({
			type:'get',
			cache:false,
			url:param+"?page=1",
			dataType:'json',
			success:function(json){
				//总记录数
				require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js',function(){
						require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
							var tpl = require('../../wx/tpl/account/account_log.tpl');
							var template = Handlebars.compile(tpl);
							var html = template(json);
							$('.investItem').html(html);
							$(".investItem .money_details_list .test").bind("click",function(){
								$(".mark_details").hide();
							  	 $(this).next("div").toggle();
							});
							$(".invest-list:nth-child(4n)").css("margin-right","0")
						});
					});
					//分页插件
					if(json.data.page.pages > 0)	
					{
						require.async(['/plugins/pager/pager.css','/plugins/pager/pager'],function(){
							kkpager.generPageHtml({
								pno : json.data.page.currentPage,//当前页码
								total : json.data.page.pages,//总页码
								totalRecords : json.data.page.total,//总数据条数
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
								        		cache:false,
								        		url:param+"?page="+n,
								        		dataType:"json",//这个必不可少，如果缺少，导致传回来的不是json格式
								        		success:function(json){
								        			require.async(['/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js','/plugins/handlebars-v1.3.0/transFormatJson'],function(){
												var tpl = require('../../wx/tpl/account/account_log.tpl');
												var template = Handlebars.compile(tpl);
												var html    = template(json);
												$('.investItem').html(html);
												$(".investItem .money_details_list .test").bind("click",function(){
													$(".mark_details").hide();
												  	 $(this).next("div").toggle();
												});
								        			});
								        		}
								        	});
									this.selectPage(n); //处理完后可以手动条用selectPage进行页码选中切换
								}
							});
						});
					}else{
						$("#kkpager").html("'<p style='text-align:center;width:100%;margin:0 auto;'>暂无数据</p>'");
					}
				}
			})	
	}
	
	

});

