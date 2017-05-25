define(function(require,exports,modlue){
	require('jquery');
	
	$.ajax({
		url:"/product/showProductTypeFlagList.html",
		type:"GET",
		dataType:"json",
		success:function(res)
		{
			list_data = res.data;
			var htmlStrTitle = "";
			var id = $("#id").val();
			for(var i = 0;i<list_data.length;i++){
				if(list_data[i].id==id){
					htmlStrTitle+= "<a href='/nb/pc/product/productList.html?id="+list_data[i].id+"'  class='active' >"+list_data[i].flagName+"</a>";
				}else{
					htmlStrTitle+= "<a href='/nb/pc/product/productList.html?id="+list_data[i].id+"' >"+list_data[i].flagName+"</a>";
				}
			}
			
			$(".invest-tab-con").html(htmlStrTitle);
			
		}
	})
	

	
	//通用显示函数
		$.ajax({
		type:'get',
		url:'/product/showProductListByFlagForPc.html?id='+$("#id").val(),
		dataType:'json',
		success:function(json){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js',function(){
					require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
						var tpl = require('../../tpl/product/index.tpl');
						var template = Handlebars.compile(tpl);
						var html = template(json);	
						$('.investItem').html(html);
						
						//偶数行增加背景颜色
						$(".investItem:odd").addClass("investItemBg");
						//不同的进度，更换成不同的颜色
						require.async('/plugins/jquery.knob/jquery.knob.min',function(){
							$('.knob').each(function(){
								var val = parseInt($(this).val());
								if(val <= 25)
								{
									$(this).attr("data-fgColor","#5bc0de");
								}
								else if(val > 25 && val <= 50)
								{
									$(this).attr("data-fgColor","#12adff");
								}
								else if(val > 50 && val < 100)
								{
									$(this).attr("data-fgColor","#28b726");
								}
								else
								{
									var isIE = function(ver){
									    var b = document.createElement('b')
									    b.innerHTML = '<!--[if IE ' + ver + ']><i></i><![endif]-->'
									    return b.getElementsByTagName('i').length === 1
									}
									if(isIE(8)){
									    $(this).next().addClass("investComplete100");
									}
									$(this).attr("data-fgColor","#c83e41");
								}
							});

							$(".invest-list:nth-child(4n)").css("margin-right","0")

							$('.knob').knob({
								'width':50,
	   							'height':50,
	   							'thickness':.2
							});
						});
						
						
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
					        		url:'/product/showProductListByFlagForPc.html?id='+$("#id").val()+"&page="+n,
					        		dataType:"json",//这个必不可少，如果缺少，导致传回来的不是json格式
					        		success:function(json){
					        			require.async(['/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js','/plugins/handlebars-v1.3.0/transFormatJson'],function(){
					        				
					        				var tpl = require('../../tpl/product/index.tpl');
											var template = Handlebars.compile(tpl);
											var html    = template(json);
											$('.investItem').html(html);
											
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
			
			
			
		},error:function(){
		}
		
		});
		
		
		
		
});		