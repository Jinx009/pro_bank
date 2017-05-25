
/*
define(function(require,exports,module){
	require('jquery');
	$("#status a:eq(0)").addClass("current");
	var tpl = require('../../tpl/account/investList_mine.tpl');//载入tpl模板
	//初始化显示表格、搜索
	require.async(['./showTable','./search'],function(showTable,search){
		showTable.ajaxUrl('/nb/wechat/account/assetsList.html?time=-1&status=0',tpl);
		search.search('/nb/wechat/account/assetsList.html?',tpl);
	});
});*/
var flag = false;
define(function(require,exports,modlue){
	require('jquery');
//	var layer =require("/plugins/layer.mobile-v1.6/layer.m.js");
	//转让弹窗
	var tpl = require('../../wx/tpl/account/investList_mine.tpl');
    var id ="investItem";
    var status;
	ajaxUrl('/nb/wechat/account/assetsList.html');
	
	//通用显示函数
	function ajaxUrl(param){
		$.ajax({
			type:'get',
			cache:false,
			url:param+"?page=1&status="+status,
			dataType:'json',
			success:function(json){
				//总记录数
				require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js',function(){
						require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
							/*var tpl = require(tp);*/
							var template = Handlebars.compile(tpl);
							var html = template(json);
							if(json.data.page.pages <= 0){
								$("#f_d").css("display","none");
							}else{
								$("#f_d").css("display","block");
							}
							$('.'+id).html(html);
							$(".invest-list:nth-child(4n)").css("margin-right","0");
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
								        		url:param+"?page="+n+"&status="+status,
								        		dataType:"json",//这个必不可少，如果缺少，导致传回来的不是json格式
								        		success:function(json){
								        			require.async(['/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js','/plugins/handlebars-v1.3.0/transFormatJson'],function(){
												/*var tpl = require(tp);*/
												var template = Handlebars.compile(tpl);
												var html    = template(json);
												$('.'+id).html(html);
												$(".invest-list:nth-child(4n)").css("margin-right","0");
								        			});
								        		}
								        	});
									this.selectPage(n); //处理完后可以手动条用selectPage进行页码选中切换
								}
							});
						});
						
					}else{
						$("#kkpager").html("<p style='text-align:center;width:100%;margin:0 auto;'>暂无数据</p>");
					}
				}
		})	
	}	
	
	
	
	/*$(".redeem").live("click",function(){
		var zcid = $(this).parent().find(".redeemId").val();
		$.ajax({
					url:zcid,
					type:"get",
					dataType:"json",
					success:function(json){
						if(json.result ==false){
							wechatAlert(json.msg,"0");
						}else if(json.result==true){
							wechatAlert("赎回成功","1","/nb/wechat/account/assets.html");
						}
						}
					});
	});*/
	
	$(".fix_header ul li").click(function(){
	 		if("浮动收益类"==$(this).html()){
	 			 tpl = require('../../wx/tpl/account/invest_entrust.tpl');
	 			 id = "investItems";
	 			 status=1;
	 			ajaxUrl("/nb/wechat/account/entrustList.html");
	 			$("#fix_income").css("display","none");
    		    $("#float_income").css("display","block");
	 		}else{
	 			 tpl = require('../../wx/tpl/account/investList_mine.tpl');
	 		     id ="investItem";
	 			 ajaxUrl('/nb/wechat/account/assetsList.html');
	 			$("#fix_income").css("display","block");
    		    $("#float_income").css("display","none");
	 		}
	 	});
		
	
	$(".investItem").delegate('.redeem', 'click', function(event) {
		event.preventDefault();
        var self = $(this);
        var _money =self.attr("vtext");
        layer.open({
            content: '<div style="font-size: 16px; font-weight: 300; color: #4A4747; margin-top: 28px; margin-bottom: 3px;position: relative;text-align: center;"><div style="font-size: 16px; color: #343434; position: absolute; top: -32px; left: 50%; width: 140px; text-align: center; margin-left: -70px;">请输入赎回金额</div><input type="text" value='+_money+'  style="padding:1px 0 1px 4px; height: 27px; margin-top:4px; margin-bottom: 20px; border-radius: 3px; border: none; width:200px;" placeholder="请输入赎回金额" id="redeemMoney" autofocus=""><div style="width: 206px; margin: 0 auto;color:red;margin-top: -10px;margin-bottom: 20px;font-size: 12px;" class="wrong-msg"></div></div>',
            btn: ['确认', '取消'],
            style: 'min-width: 280px;min-height:125px;text-align: center;border-radius: 6px;padding-bottom: 20px;background: #ededed;',
            shadeClose: true,
            yes: function(index) {
                var redeemMoney = $("#redeemMoney").val();
                var _id = self.attr("id");
                	if(!flag){
                		flag = true;
                		$.ajax({
                    		url:"/ppfund/ppfundOut.html",
                    		type:"POST",
                    		dataType:"json",
                    		data:{
                    			ppfundId:self.attr("id"),
                    			redeemMoney: $("#redeemMoney").val()
                    		},
                    		success:function(json)
                    		{
                    			
                    			if(json.result ==false)
                    			{
                    				 $(".wrong-msg").html(json.msg).show();
                    			}else{
                    				var cash =parseFloat(json.msg).toFixed(2);
                    			 $.ajax({
                                     url:"/ppfund/doPpfundOut.html",
                                     type: 'POST',
                                     data: {
                                         id:self.attr("id"),
                                         redeemMoney: $("#redeemMoney").val()
                                     },
                                     success: function(res) {
                                         if (res.result == false) {
                                        	 layer.close(index);
                                             layer.open({
                                                 content: "赎回失败! "+res.msg+"",
                                                 style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:10px; letter-spacing: 2px; line-height: 100%;',
                                                 time: 3
                                             });
                                         } else if (res.result == true) {
                                             layer.close(index);
                                             layer.open({
                                                 content: "赎回成功! 您当日可赎回额度［"+cash+"］元",
                                                 style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:10px; letter-spacing: 2px; line-height: 100%;',
                                                 time: 3
                                             });
                                             setTimeout(function(){
                                            	 location.href="/nb/wechat/account/assets.html";
                                             },3500);
                                            
                                         }
                                         flag = false;
                                     }
                                 });
                    			}
                    			flag = false;
                    		}
                    		
                     	});
                	}
                	

            },
            no: function(index) {
                layer.close(index);
            }
        });
        $("#redeemMoney").keyup(function(){
    		if($("#redeemMoney").val().search(/^\d*(?:\.\d{0,2})?$/)==-1)
    			{
    				$("#redeemMoney").val("");
    			}
    	});
    }); //-.btn-donation.click
	
});




function redeem(zcid){

	// 操作按钮中转赠点击监听
    
//	$("#wrong_btn").show();
//	$("#success_div").show();
//	$("#mask_div").show();
//	$("#right_btn").attr("onclick","doRedeem('"+zcid+"')");
}

function doRedeem(zcid)
{
	$("#wrong_btn").hide();
	
	$.ajax({
		url:zcid,
		type:"get",
		dataType:"json",
		success:function(json)
		{
			if(json.result ==false)
			{
				$("#pic").attr("src","/themes/theme_default/nb/wechat/images/close.png");
				$("#errorMsg").html(json.msg);
				$("#right_btn").attr("onclick","hideError()");
				showError();
			}
			else if(json.result==true)
			{
				$("#pic").attr("src","/themes/theme_default/nb/wechat/images/open.png");
				$("#errorMsg").html("赎回成功");
				$("#right_btn").attr("onclick","goUrl('/nb/wechat/account/assets.html')");
				
				showError();
			}
		}
	});
}
function hideError()
{
	$("#success_div").hide();
	$("#mask_div").hide();
}
function showError()
{
	$("#success_div").show();
	$("#mask_div").show();
}
function goUrl(url)
{
	location.href = url;
}




