define(function(require,exports,module){
	require('jquery');
	
	$(".zfsmTab tbody tr:odd").addClass("tdbg");
	var tpl = require('/themes/theme_default/media/tpl/help_invest.tpl');//载入tpl模板
	$.ajax({
		url:"/noviceJson.html?&random="+Math.random(),
		type:"get",
		dataType:"json",
		success:function(json){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					var template = Handlebars.compile(tpl);
					var html = template(json);
					$(".help-list").html(html);
				})
			})
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
						        		url:"/noviceJson.html?random="+Math.random()+ (new Date()).getTime()+"&page="+n,
						        		dataType:"json",//这个必不可少，如果缺少，导致传回来的不是json格式
						        		success:function(json){
						        			require.async(['/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js','/plugins/handlebars-v1.3.0/transFormatJson'],function(){
												var template = Handlebars.compile(tpl);
												var html    = template(json);
												$('.help-list').html(html);
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
	})

	$(function(){
		 var href = window.location.href;
		 var hrefval = href.split('=')
		 if(hrefval[1]=="rhjk"){
    	  $(".help-tab span").eq(1).addClass('selected').siblings().removeClass('selected');
    	  $(".helpContent .con").eq(1).css('display','block').siblings().css('display','none');
    	 }
     })

	require.async('common1',function(){
		$(".help-box").tabChange({
			isClick:true,
            isHover:false,
			childLi:".help-tab span",//tab选项卡
			childContent:".con",//tab内容
			hoverClassName:"selected",//选中当前选项卡的样式
			callBack:false	
		});
	});

	 $(function(){
		require.async('./jquery.flexslider-min',function(){
			$('.index_intro_box').flexslider({
				directionNav: true,
				pauseOnAction: false,
				controlNav: true,
				manualControls: ".flex-control-nav1 li",
			});
		})
	});

	  $(function(){
		require.async('./jquery.flexslider-min',function(){
			$('.index_intro_boxa').flexslider({
				directionNav: true,
				pauseOnAction: false,
				controlNav: true,
				manualControls: ".flex-control-nav1a li",
			});
		})
	});


	   $(function(){
		require.async('./jquery.flexslider-min',function(){
			$('.index_intro_boxb').flexslider({
				directionNav: true,
				pauseOnAction: false,
				controlNav: true,
				manualControls: ".flex-control-nav1b li",
			});
		})
	});
	 
	 //新手专区平均年化收益显示
	 $.ajax({
			url:"/index/indexStatistics.html?random="+Math.random(),
			type:"get",
			dataType:"json",
			success:function(data){
				//保留两位小数
				var str = data.averageApr.toFixed(2);
				$(".help-rate").html(str.replace(/(.)/g, '<span >$1</span>')+'%');
				$(".help-rate span").eq(2).addClass("point");
			}
		})
		
});

