define(function(require,exports,module){
    require('jquery');
    //判断用户验证信息
    
      $.ajax({
            type:"post",
            url:"/member_borrow/businessUserIdentify.html?random="+Math.random(),
            dataType:"json",
            success:function(json){
                require.async("/plugins/handlebars-v1.3.0/handlebars-v1.3.0",function(){
                    require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
                    	$(".account_home_msg11").html(Handlebars.compile(require("../../tpl/member_borrow/main_info.tpl"))(json));
                    });
                });
                //判断是否开通第三方账户
                if(json.openApi){//托管版
                  if(json.apiStatus == 0){
                      $(".noAuthentication").slideDown();
                  }
                }else{//标准版
              	  if(json.realNameStatus==0){
              		  $(".noAuthentication").slideDown();
              	  }
                }
            }
       });
     
      
    //可提现金额、正在借款项目、下一个待还日期
    $.ajax({
        url:"/member_borrow/businessHandleMoney.html?random="+Math.random(),
        type:"post",
        dataType:"json",
        success:function(json){
                require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
                    require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
                        $(".member_borrow_home").html(Handlebars.compile(require("../../tpl/member_borrow/main_total.tpl"))(json));
                    });
                });
        }
    });
	require.async('common1',function(){
		$(".account_home_con").tabChange({
			isClick:true,
            isHover:false,
			childLi:"ul li",//tab选项卡
			childContent:".con",//tab内容
			hoverClassName:"hover",//选中当前选项卡的样式
			callBack:false	
		});
	});
    
    //借款中的项目
    $.ajax({
        url:'/member_borrow/businessBid.html?random='+Math.random(),
        type:'get',
        dataType:'json',
        success:function(json){
        	if(json.businessBidList.length){
        		require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
                    require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
                        $("#main_invest").html(Handlebars.compile(require('../../tpl/member_borrow/main_borrow.tpl'))(json));
                    });
                });
        	}else{
        		$("#main_invest_tab").html("<p class='noData'>暂无数据</p>");
        	}
        }
    });
    //还款中的项目
    $(".account_home_con ul li:eq(1)").one("click",function(){
         $.ajax({
            url:'/member_borrow/businessRepayment.html?random='+Math.random(),
            type:'get',
            dataType:'json',
            success:function(json){
            	if(json.businessRepaymentList.length){
            		require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
                        require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
                            $("#main_collection").html(Handlebars.compile(require('../../tpl/member_borrow/main_repayment.tpl'))(json));
                        });
                    });
            	}else{
            		$("#main_collection_tab").html("<p class='noData'>暂无数据</p>");
            	}
            }
        });
    });

    //交易记录
    $(".account_home_con ul li:eq(2)").one("click",function(){
            $.ajax({
                url:'/member_borrow/accountTransactionLog.html?random='+Math.random(),
                type:'get',
                dataType:'json',
                success:function(json){
                	if(json.accountLogList.length){
                		require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
                            require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
                                $("#tradeLogTable").html(Handlebars.compile(require('../../tpl/member_borrow/main_trade.tpl'))(json));
                            });
                        });
                	}else{
                		$("#trade_log_tab").html("<p class='noData'>暂无数据</p>");
                	}
                }
            })
    });
});

