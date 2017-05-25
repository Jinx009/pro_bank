define(function(require,exports,module){
    require('jquery');
    //用户基本信息
    $.ajax({
        type:"post",
        url:"/member_guarantee/getGuaranteerInfo.html",
        dataType:"json",
        success:function(json){
            require.async("/plugins/handlebars-v1.3.0/handlebars-v1.3.0",function(){
                require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
                	$(".account_home_msg dl").html(Handlebars.compile(require("../../tpl/member_guarantee/main_info.tpl"))(json));
                });
            });
        }
    });

    //担保信息
    $.ajax({
        type:"post",
        url:"/member_guarantee/getGuaranteeProject.html",
        dataType:"json",
        success:function(json){
            require.async("/plugins/handlebars-v1.3.0/handlebars-v1.3.0",function(){
                require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
                	$(".account_home").html(Handlebars.compile(require("../../tpl/member_guarantee/main_guarantee_account.tpl"))(json));
                });
            });
        }
    });
    
    //选项卡
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
    
    //待登记项目
    $.ajax({
        url:'/member_guarantee/getNeedGuaranteeRegisterList.html',
        type:'get',
        dataType:'json',
        success:function(json){
        	if(json.data.length){
        		require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
                    require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
                        $("#main_registration").html(Handlebars.compile(require('../../tpl/member_guarantee/main_registration.tpl'))(json));
                    });
                });
        	}else{
        		$(".main_registration_list").html("<p class='noData'>暂无数据</p>");
        	}
        }
    });
    
    //催收项目
    $(".account_home_con ul li:eq(1)").one("click",function(){
    	$.ajax({
            url:'/member_guarantee/collection/collectionListJSON.html?size=4',
            type:'get',
            dataType:'json',
            success:function(json){
            	if(json.data.list.length){
            		require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
                        require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
                            $("#main_collection").html(Handlebars.compile(require('../../tpl/member_guarantee/main_collection.tpl'))(json));
                        });
                    });
            	}else{
            		$(".main_collection_list").html("<p class='noData'>暂无数据</p>");
            	}
            }
        });
    });
    
    $(".guaranteeReg").live("click",function(){
    	var idVal = $(this).attr("data-id");
    	window.open("/member_guarantee/registerGuarantee.html?borrowId="+idVal);
		require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
			//构造确认框DOM
			$.layer({
			    type: 1,
			    closeBtn: false,
			    title: false,
			    area: ['460px', '240px'],
			    shade: [0.5, '#000'],
			    border: [10, 0.3, '#000'],
			    page: {
			        html: '<div class="fileconfirm"><div class="fileconfirm_title"><h1 class="float_left">登记提示</h1><span class="float_right cancleBtn">X</span></div><div class="fileconfirm_msg"><p>您即将跳往登记操作，登记完成前，请不要关闭本窗口。登记完成后，请根据您的登记结果点击下面按钮。</p></div><div class="fileconfirmbtn"><a href="javascript:;" class="okBtn">登记成功</a><a href="javascript:;" class="cancleBtn okBtn">登记失败</a></div></div>'
			    }
			});	
			//确认操作
			$(".okBtn").click(function(){
				window.location.reload();	
			});
			//删除操作
			$(".cancleBtn").click(function(){
				layer.closeAll();
			});
		});
		
	})

});

