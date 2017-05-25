define(function(require,exports,module){
	//分页显示函数ajaxUrl(param,tpl)
	//param:请求参数
	//tpl:语义模板
	var ajaxUrl = function (param,tpl){
		if(param.indexOf("?") == -1)
		{
			param = param + '?randomTime=' + (new Date()).getTime();
		}
		else
		{
			param = param + '&randomTime=' + (new Date()).getTime();
		}
		$.ajax({
		type:'get',
		url:param,
		dataType:'json',
		success:function(json){
			//异步加载数据
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					var template = Handlebars.compile(tpl);
					var html = template(json);
					$('#member_table').html(html);
					//偶数行增加背景颜色
					$("#member_table tr:even").addClass("showTabBg");
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
							mode:'click',
							click:function(n){
					        	$.ajax({
					        		type:"get",
					        		url:param+"&randomTime="+ (new Date()).getTime()+"&page="+n,
					        		dataType:"json",
					        		success:function(json){
											var template = Handlebars.compile(tpl);
											var html    = template(json);
											$('#member_table').html(html);
											//偶数行增加背景颜色
											$("#member_table tr:even").addClass("showTabBg");
					        		}
					        	});
								this.selectPage(n); //处理完后可以手动条用selectPage进行页码选中切换
							}
					});
				});
			}
			else
			{
				$("#kkpager").html('暂无数据');
			}
			
		}
		})	
	}
	exports.ajaxUrl = ajaxUrl;
});