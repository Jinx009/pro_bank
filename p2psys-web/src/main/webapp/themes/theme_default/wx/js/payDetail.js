define(function(require,exports,module){
    require('jquery');

  	var submitUrl = $("#submitUrl").val();
  	
  	$("#doPay").click(function(){
      	var rdVal = $("input[name='payMent'][checked]").val(); 
      	if($("#protocol").attr("checked")){
        		if(rdVal=="1"){
          			$("#pay").attr('action',$("#submitUrl").val());
          			$("#pay").submit();
        		}
      	}else{
      		  $("#pro_msg").html("请选择服务协议");
      	}

	
/*$.ajax({
	type : "post",
	url : submitUrl + "?random=" + Math.random(),
	dataType : "json",
	success : function(json) {
		
	}
});*/
	});
	//协议弹窗
  $("#service_contract").click(function(){
    require.async("/plugins/layer-v1.8.4/layer.min",function(){
      var i = $.layer({
        type :1,
        title : "协议内容",
        closeBtn :[0,true],
        border : [10 , 0.3 , '#000', true],
        area :['310px','250px'],
        page : {dom:'#modal_dialog'}
      });
    })
  });
    
/*   //判断用户验证信息
  $.ajax({
      type:"post",
      url:"/member/investIdentify.html?random="+Math.random(),
      dataType:"json",
      success:function(json){
          require.async("/plugins/handlebars-v1.3.0/handlebars-v1.3.0",function(){
        	  require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
        		  $(".account_home_msg").html(Handlebars.compile(require("../../tpl/member/main_info.tpl"))(json));
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
    
    */
});

