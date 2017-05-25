define(function(require,exports,modlue){
	require('jquery');
	//转让弹窗
	var typeVal = $("#type a.current").attr("data-val");
	var aprSearchVal = $("#aprSearch a.current").attr("data-val");
	var moneySearchVal = $("#moneySearch a.current").attr("data-val");
	var timeSearchVal = $("#timeSearch a.current").attr("data-val");
	var statusVal = $("#status a.current").attr("data-val");
	var orderVal = $("#order li.current a").attr("data-val");
	var nameVal = encodeURIComponent($("#name").val());
	//排序
	$(".bondList li").click(function(){
		var $this = $(this),
		      $a = $this.find("a"),
		      $i = $this.find("i"),
		      val = $a .attr("data-val"),
		      param = "";
		if($this.hasClass("current"))
		{
			if(val == "desc")
			{
				$a.attr("data-val","asc");
				$i .removeClass("desc").addClass("asc");
			}
			else if(val == "asc")
			{
				$a.attr("data-val","desc");
				$i .removeClass("asc").addClass("desc");
			}
		}
		else
		{
			$this.addClass("current").siblings().removeClass("current");
		}
		if(val == "normal")
		{
			param = "/bond/bondList.html?sign=1";
		}
		else
		{
			param = "/bond/bondList.html?sort="+$a.attr("data-type")+"&order="+$a.attr("data-val");
		}
		console.log(param);
		ajaxUrl(param);
	});

	ajaxUrl('/bond/bondList.html?sign=1');


	//标种搜索
	$("#type a").click(function(){
		$(this).addClass("current").siblings().removeClass("current");
		var param = "/bond/bondList.html?borrowType="+$(this).data("val")+"&aprSearch="+$("#aprSearch a.current").data("val")+"&moneySearch="+$("#moneySearch a.current").data("val")+"&timeSearch="+$("#timeSearch a.current").data("val")+"&status="+$("#status a.current").attr("data-val")+"&order="+$("#order li.current a").attr("data-val")+"&borrowName="+encodeURIComponent($("#name").val());
		ajaxUrl(param);
	});
	
	//利率搜索
	$("#aprSearch a").click(function(){
		$(this).addClass("current").siblings().removeClass("current");
		var param = "/bond/bondList.html?borrowType="+$("#type a.current").data("val")+"&aprSearch="+$(this).data("val")+"&moneySearch="+$("#moneySearch a.current").data("val")+"&timeSearch="+$("#timeSearch a.current").data("val")+"&status="+$("#status a.current").attr("data-val")+"&order="+$("#order li.current a").attr("data-val")+"&borrowName="+encodeURIComponent($("#name").val());
		ajaxUrl(param);
	});
	
	//金额搜索
	$("#moneySearch a").click(function(){
		$(this).addClass("current").siblings().removeClass("current");
		var search = "/bond/bondList.html?borrowType="+$("#type a.current").data("val")+"&aprSearch="+$("#aprSearch a.current").data("val")+"&moneySearch="+$(this).data("val")+"&timeSearch="+$("#timeSearch a.current").data("val")+"&status="+$("#status a.current").attr("data-val")+"&order="+$("#order li.current a").attr("data-val")+"&borrowName="+encodeURIComponent($("#name").val());
		ajaxUrl(search);
	});
	
	//期限搜索
	$("#timeSearch a").click(function(){
		$(this).addClass("current").siblings().removeClass("current");
		var param = "/bond/bondList.html?borrowType="+$("#type a.current").data("val")+"&aprSearch="+$("#aprSearch a.current").data("val")+"&moneySearch="+$("#moneySearch a.current").data("val")+"&timeSearch="+$(this).data("val")+"&status="+$("#status a.current").attr("data-val")+"&order="+$("#order li.current a").attr("data-val")+"&borrowName="+encodeURIComponent($("#name").val());
		ajaxUrl(param);
	});
	
	//状态搜索
	$("#status a").click(function(){
		$(this).addClass("current").siblings().removeClass("current");
		var param = "/bond/bondList.html?borrowType="+$("#type a.current").data("val")+"&aprSearch="+$("#aprSearch a.current").data("val")+"&moneySearch="+$("#moneySearch a.current").data("val")+"&timeSearch="+$("#timeSearch a.current").data("val")+"&status="+$(this).attr("data-val")+"&order="+$("#order li.current a").attr("data-val")+"&borrowName="+encodeURIComponent($("#name").val());
		ajaxUrl(param);
	});
	
	//点击搜索调用
	$(".searchBtn").click(function(){
		var search = "/bond/bondList.html?borrowType="+$("#type a.current").data("val")+"&aprSearch="+$("#aprSearch a.current").data("val")+"&moneySearch="+$("#moneySearch a.current").data("val")+"&timeSearch="+$("#timeSearch a.current").data("val")+"&status="+$("#status a.current").attr("data-val")+"&order="+$("#order li.current a").attr("data-val")+"&borrowName="+encodeURIComponent($("#name").val());
		ajaxUrl(search);
	});
	//通用显示函数
	function ajaxUrl(param){
		$.ajax({
			type:'get',
			cache:false,
			url:param,
			dataType:'json',
			success:function(json){
				//总记录数
				require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js',function(){
						require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
							var tpl = require('../../tpl/bond/index.tpl');
							var template = Handlebars.compile(tpl);
							var html = template(json);
							$('.bondtListBox').html(html);
							$(".bondSellProtocolPreview").click(function(){
								require.async(["/plugins/layer-v1.8.4/skin/layer.css","/plugins/layer-v1.8.4/layer.min.js"],function(){
										$.layer({
											type:1,
											closeBtn:[0,true],
											title:"转让协议",
											area:['auto','auto'],
											border:[1,1,'#cecfd0'],
											page:{dom:"#transfer"},
											close:function(){
												
											}
										})
									});
							})
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
								        		url:param+"&page="+n,
								        		dataType:"json",//这个必不可少，如果缺少，导致传回来的不是json格式
								        		success:function(json){
								        			require.async(['/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js','/plugins/handlebars-v1.3.0/transFormatJson'],function(){
												var tpl = require('../../tpl/bond/index.tpl');
												var template = Handlebars.compile(tpl);
												var html    = template(json);
												$('.bondtListBox').html(html);
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
	//最新转让成交记录
	$.ajax({
		url:"/bond/latestTenerList.html",
		type:"get",
		dataType:"json",
		cache:false,
		success:function(json){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					$(".bondRecord ul").html(Handlebars.compile(require('../../tpl/bond/latestTenerList.tpl'))(json));
				})
			})
		}
	})

	
		
		
});