define(function(require,exports,module){
    require('jquery');

   $.ajax({
	  type:"post",
	  url:"/member/investHandleMoney.html?random="+Math.random(),
	  dataType:"json", 
	  success:function(json){
		  require.async("/plugins/handlebars-v1.3.0/handlebars-v1.3.0",function(){
	            require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
	              require.async('/plugins/echarts/echarts-plain-original',function(){

	               $("#totalMoney").html(formatMoney(json.account.total));
				   $("#iceMoney").html(formatMoney(json.account.noUseMoney));
				   $("#userMoney").html(formatMoney(json.account.useMoney));
				   $("#waitMoney").html(formatMoney(json.account.collection));
	               $(".tbqk").html(Handlebars.compile(require("../../tpl/member/main_top.tpl"))(json))
	              var myChart = echarts.init(document.getElementById('moneychart')); 
	                
	          var option = {
                title : {
                show:false
            },
            legend: {
                data:['可用金额','冻结金额','待收总额']
            },
  
                            series : [
                                
                                {
                                    name:'资金统计图',
                                    type:'pie',
                                    radius : [65, 50],
                                    x: '60%',
                                    width: '35%',
                                    funnelAlign: 'left',
                                    data:[
                                        {value:json.useMoney, name:'可用金额'},
                                        {value:json.account.noUseMoney, name:'冻结金额'},
                                        {value:json.account.collection, name:'待收总额'},
                                    ]
                                }
                            ]
                        };
	              
	                      // 为echarts对象加载数据 
	                      myChart.setOption(option); 
	                   });
	                  });
                  });
	  }
   });
    
   //判断用户验证信息
  $.ajax({
      type:"post",
      url:"/member/investIdentify.html?random="+Math.random(),
      dataType:"json",
      success:function(json){
          require.async("/plugins/handlebars-v1.3.0/handlebars-v1.3.0",function(){
        	  require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
        		  $(".account_home_msg11").html(Handlebars.compile(require("../../tpl/member/main_info.tpl"))(json));
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
    
    //收益、余额等信息
    $.ajax({
      type:"post",
      url:"/member/investHandleMoney.html?random="+Math.random(),
      dataType:"json",
      success:function(json){
          require.async("/plugins/handlebars-v1.3.0/handlebars-v1.3.0",function(){
        	  require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
              $(".account_home").html(Handlebars.compile(require("../../tpl/member/main_account.tpl"))(json));
              $("#date-receipt").html(Handlebars.compile(require('../../tpl/member/main_next_collection.tpl'))(json));
              //提示效果
              require.async(['/plugins/poshytip/tip-yellowsimple/tip-yellowsimple.css','/plugins/poshytip/jquery.poshytip.min'],function(){
      	    	$('.textTips').poshytip({
      	    		className: 'tip-yellowsimple',
      	    		showTimeout: 1,
      	    		alignTo: 'target',
      					alignX: 'center',
      					alignY: 'top',
      					offsetY: 10,
      	    		allowTipHover:false
      	    	});
          	});
          });
          });
      }
   });
    
    //关闭开能第三方账户条
    $(".close").click(function(){
    	$(".noAuthentication").hide();
    })
    
    //首页Tab标签
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
    
    var flagId;
    $.ajax({
		url:"/product/showProductTypeFlagList.html",
		type:"GET",
		dataType:"json",
		success:function(res)
		{
			list_data = res.data;
			flagId=list_data[0].id;
			
			
		}
	})
    
    //可投借款
    $.ajax({
        url:'/member/investList.html?random='+Math.random(),
        type:'get',
        dataType:'json',
        success:function(json){
        	if(json.list.length){
        		require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
                    require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
                        $("#main_invest").html(Handlebars.compile(require('../../tpl/member/main_invest.tpl'))(json));
                        $("#flag").attr("href","/nb/pc/product/productList.html?id="+flagId);
                    });
                });
        		
        		
        	}else{
        		$(".main_invest_list").html("<p class='noData'>暂无数据</p>");
        	}            
        }
    })
    
    //收款信息
     $.ajax({
        url:'/member/investCollectionList.html?random='+Math.random(),
        type:'get',
        dataType:'json',
        success:function(json){
        	if(json.data.length){
        		require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
                    require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
                        $("#main_collection").html(Handlebars.compile(require('../../tpl/member/main_collection.tpl'))(json));
                    });
                });
        	}else{
        		$(".main_collection_list").html("<p class='noData'>暂无数据</p>");
        	}
        }
    });
    
    //交易记录
    $.ajax({
        url:'/member/investTransactionLog.html?random='+Math.random(),
        type:'get',
        dataType:'json',
        success:function(json){
        	if(json.accountLogList.length){
        		require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
                    require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
                        $("#tradeLogTable").html(Handlebars.compile(require('../../tpl/member/main_trade.tpl'))(json));
                    });
                });
        	}else{
        		$(".tradeLogTable_list").html("<p class='noData'>暂无数据</p>");
        	}
        }
    });
    
    
});

function formatMoney(money){
	if(money == 0)
	{
		return money;
	}
	else if(money == 'undefined')
	{
		return 0;
	}
	else
	{
		n = 2; 
		money = parseFloat((money + "").replace(/[^\d\.-]/g, "")).toFixed(n) + ""; 
		var l = money.split(".")[0].split("").reverse(), r = money.split(".")[1]; 
		t = ""; 
		for (i = 0; i < l.length; i++) 
		{ 
		t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : ""); 
		} 
		var str = t.split("").reverse().join("")+ "." + r;
		
		if(str.length>10)
		{
			return "<font style='font-size:16px;' ><b>"+str+"</b></font>";
		}
	
		return  t.split("").reverse().join("")+ "." + r;
	}
}