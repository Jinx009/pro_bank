define(function(require,exports,module){
	require('jquery');
	
	//回款计划
	var statusVal = $("#status a.selected").attr("data-val");
	var loading = "status="+statusVal;
	ajaxUrl(loading);//初始化调用
	
	//状态搜索
	$("#status a").click(function(){
		$(this).addClass("selected").siblings().removeClass("selected");
		var param = "status=" + $(this).data("val");
		if (!$("#fullTime").is(":hidden")){
			if($("#startTime").val()!=""){
				param += "&startTime=" + $("#startTime").val();
			}
			if($("#endTime").val() != ""){
				param +=  "&endTime="+ $("#endTime").val();
			}
		}
		else{			
			param += "&time=" + $("#timeStatus a.current").data("val");
		}
		ajaxUrl(param);
	});
	
	//具体日期搜索
	$("#timeStatus a").click(function(){
		$(this).addClass("current").siblings().removeClass("current");
		var param = "status=" + $("#status a.selected").data("val")+"&time="+$(this).data("val") ;
		ajaxUrl(param);
	});
	
	//具体日期和日期范围切换
	$("#timeStatus em").click(function(){
		$("#shortTime").hide();
		$("#fullTime").show();
	});
	
	$("#fullTime em").click(function(){
		$("#fullTime").hide();
		$("#shortTime").show();
	});
	
	function ajaxUrl(param){
		$.ajax({
			type:'get',
			cache:false,
			url:'/member/invest/allRepayPlan.html?'+param,
			dataType:'json',
			success:function(json){
				//总记录数
				require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js',function(){
						require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
							var tpl = require('../../tpl/member/invest_repayPlan.tpl');
							var template = Handlebars.compile(tpl);
							var html = template(json);
							$('#repayPlan_table').html(html);
							//鼠标经过加颜色
							$("#repayPlan_table tr").hover(
							  function () {
							    $(this).addClass("showTabBg");
							  },
							  function () {
							    $(this).removeClass("showTabBg");
							  }
							);
						});
					});
				
					//分页插件
					if(json.data.page.pages > 0)
					{
						require.async(['/plugins/pager/pager.css','/plugins/pager/pager'],function(){
							kkpager.generPageHtml({
									pagerid:"kkpager",
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
									mode:'click',//click模式匹配getHref 和 click
									click:function(n,total,totalRecords){
							        	$.ajax({
							        		type:"get",
							        		url:"/member/invest/allRepayPlan.html?"+param+"&page="+n,
							        		cache:false,
							        		dataType:"json",//这个必不可少，如果缺少，导致传回来的不是json格式
							        		success:function(json){
							        			require.async(['/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js','/plugins/handlebars-v1.3.0/transFormatJson'],function(){
													var tpl = require('../../tpl/member/invest_repayPlan.tpl');
													var template = Handlebars.compile(tpl);
													var html    = template(json);
													$('#repayPlan_table').html(html);
													//鼠标经过加颜色
													$("#repayPlan_table tr").hover(
													  function () {
													    $(this).addClass("showTabBg");
													  },
													  function () {
													    $(this).removeClass("showTabBg");
													  }
													);
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
	}
	
});
