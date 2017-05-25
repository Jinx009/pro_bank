define(function(require,exports,modlue){
	require('jquery');
	
	//通用显示函数
		$.ajax({
		type:'get',
		url:'/invest/investJson.html?randomTime='+(new Date()).getTime()+"&&type=103&&status=1",
		dataType:'json',
		success:function(json){
			//总记录数
			$(".quickBar span em").html(json.data.page.total);
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js',function(){
					require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
						var tpl = require('../../tpl/investList.tpl');
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
						        		url:"/invest/investJson.html?"+"&randomTime="+ (new Date()).getTime()+"&&type=103&&status=1"+"&page="+n,
						        		dataType:"json",//这个必不可少，如果缺少，导致传回来的不是json格式
						        		success:function(json){
						        			require.async(['/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js','/plugins/handlebars-v1.3.0/transFormatJson'],function(){
												var tpl = require('../../tpl/investList.tpl');
												var template = Handlebars.compile(tpl);
												var html    = template(json);
												$('.investItem').html(html);
												//偶数行增加背景颜色
												$(".investItem:odd").addClass("investItemBg");
												//判断是否是IE浏览器
												if(!+[1,]) require('/plugins/jquery.knob/excanvas');
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
													$('.knob').knob({
														'width':50,
							   							'height':50,
							   							'thickness':.2
													});
												});
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
			},
			error:function(){
			}
		})	


	
	//翻页置顶
	require.async("commonJS/rollTo",function(){
		$("#kkpager").rollTo({
			oFinish: ".investList ", //要滚动到的元素
			sSpeed: "300",  //滚动速度
			bMonitor: false, //是否楼层监听
			sClass: "", //楼层监听时需要添加的样式
			iBias:-18,
			fnAdditional: "" //追加方法
		});
	})	


	
});