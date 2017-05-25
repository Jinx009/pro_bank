define(function(require,exports,module){
	//分页显示函数ajaxUrl(param,tpl)
	//param:请求参数，其中一定要含有"?"，否则分页的时候就会报错
	//tpl:语义模板，其中一定要含有require("../../tpl/.tpl")，不然会报错
	var ajaxUrl = function (param,tpl){
		$.ajax({
			type:'get',
			url:param,
			cache:false,
			dataType:'json',
			success:function(json){
				require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js',function(){
					require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
						var template = Handlebars.compile(tpl);
						var html = template(json);
						$('.table_content').html(html);
						//鼠标经过加颜色
						$("#member_table tr").hover(
						  function () {
						    $(this).addClass("showTabBg");
						  },
						  function () {
						    $(this).removeClass("showTabBg");
						  }
						);
					});
				});
				//分页插件，总页码大于0的时候才能显示
				if(json.data.page.pages > 1)
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
								prePageText				: '<b>&lt;</b>',
								nextPageText			: '<b>&gt;</b>'
							},
							mode:'click',
							click:function(n){
							        	$.ajax({
								        		type:"get",
								        		url:param+"&page="+n,
								        		cache:false,
								        		dataType:"json",
								        		success:function(json){
											var template = Handlebars.compile(tpl);
											var html    = template(json);
											$('.table_content').html(html);
											//鼠标经过加颜色
											$("#member_table tr").hover(
												function () {
													$(this).addClass("showTabBg");
												},function () {
													$(this).removeClass("showTabBg");
												}
											);
								        		}
							        	});
								this.selectPage(n); //处理完后可以手动条用selectPage进行页码选中切换
							}
						});
					});
				}
				else if(json.data.page.pages == 0)
				{
					$("#kkpager").html('暂无数据');
				}
				else{
					$("#kkpager").html('');
				}
			}
		})	
	}
	exports.ajaxUrl = ajaxUrl;
});