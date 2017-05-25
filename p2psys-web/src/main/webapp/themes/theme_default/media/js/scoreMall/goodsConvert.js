define(function(require,exports,module){
	require('jquery');
	ajaxUrl('?categoryId=-1&startScore=-1&endScore=-1');
	$("#categoryId a").live("click",function(){
		if($(".range_span").is(":visible"))
		{

			ajaxUrl('?categoryId='+$(this).data('val')+'&startScore='+$('input[name="startScore"]').val()+'&endScore='+$('input[name="endScore"]').val());
		}
		else
		{
			ajaxUrl('?categoryId='+$(this).data('val')+'&startScore=-1&endScore=-1');
		}
		$(this).addClass('current').siblings().removeClass('current');
	});

	$("#range a").live("click",function(){
		$(this).addClass('current').siblings().removeClass('current');
		if($(this).hasClass('range'))
		{
			$(".range_span").css("display","inline-block");
		}
		else
		{
			ajaxUrl('?categoryId='+$("#categoryId .current").data('val')+'&startScore=-1&endScore=-1');
			$(".range_span").css("display","none");
		}
	});

	$(".searchBtn").live("click",function(){
		var $startScore = $('input[name="startScore"]');
		var $endScore = $('input[name="endScore"]')
		if(parseInt($startScore.val()) > parseInt($endScore.val()))
		{
			$startScore.css({"background":"#ea5154","color":"#fff"});
			$endScore.css({"background":"#ea5154","color":"#fff"});
		}
		else
		{
			$startScore.css({"background":"#fff","color":"#555"});
			$endScore.css({"background":"#fff","color":"#555"});	
			var search = '?categoryId='+$("#categoryId .current").data('val');
			if($('input[name="startScore"]').val() == '')
			{
				search += '&startScore=-1';
			}
			else
			{
				search += '&startScore='+$('input[name="startScore"]').val();
			}
			if($('input[name="endScore"]').val() == '')
			{
				search += '&endScore=-1';
			}
			else
			{
				search += '&endScore='+$('input[name="endScore"]').val();
			}
			ajaxUrl(search);
		}
	});

	function ajaxUrl(param){
		$.ajax({
			type:'get',
			url:'/scoreMall/goodsList.html'+param,
			dataType:'json',
			success:function(json){
				require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js',function(){
					require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
						var tpl = require('../../tpl/scoreMall/goodsConvert.tpl');
					var template = Handlebars.compile(tpl);
					var html = template(json);
					$("#goods_list").html(html);
					$(".goods_list li:nth-child(4n+4)").css("margin-right","0");
					$(".goods_list li .goods_bg").hover(function(){
						$(this).addClass("hover");
					},function(){
						$(this).removeClass("hover");
					});
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
						        		type:'get',
						        		url:'/themes/theme_default/media/json/goodsConvert.json',
						        		dataType:'json',
						        		success:function(json){
						        			require.async(['/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js','/plugins/handlebars-v1.3.0/transFormatJson'],function(){
												var tpl = require('../../tpl/scoreMall/goodsConvert.tpl');
												var template = Handlebars.compile(tpl);
												var html    = template(json);
												$('#investlist').html(html);
											});
						        		}
						        	});
									this.selectPage(n); //处理完后可以手动条用selectPage进行页码选中切换
								}
							});
						});
					}
					else
					{
						$("#kkpager").html("暂无数据");
					}
					});
					
				});
			}
		});
	};
	

});