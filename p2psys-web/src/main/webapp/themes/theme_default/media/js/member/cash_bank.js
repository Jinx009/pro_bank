define(function(require,exports,module){
	require('jquery');
	//判断版本 0 为标准版
	var depositVal =$("#deposit").val();
	
	//判断是否已认证或开通第三方接口
	/*$.ajax({
		url:"/member/useridentify/realNameStatus.html",
		type:"get",
		dataType:"json",
		success:function(data){
			if(data.result==false){
				require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
					if(depositVal==0)//标准版
					{
						//构造确认框DOM
						$.layer({
							type: 1,
							closeBtn: false,
			                			title: "&nbsp;",
							area: ['450px', '190px'],
							border: [1, 1, '#cecfd0'],
							page: {
							    html: '<div class="tipsWrap w450"><div class="tipsTxt"><i class="iconfont tipIco">&#xe63c;</i><span>您尚未通过实名认证，请先认证。</span></div><div class="tipsBtnBar"><a href="/member/security/setting.html" class="okBtn">马上去认证</a></div></div>'
							}
						});
					}
					else//托管版
					{
						//构造确认框DOM
						$.layer({
							type: 1,
							closeBtn: false,
			                			title: "&nbsp;",
							area: ['450px', '190px'],
							border: [1, 1, '#cecfd0'],
							page: {
							    html: '<div class="tipsWrap w450"><div class="tipsTxt"><i class="iconfont tipIco">&#xe63c;</i><span>您尚未通过实名认证，请先认证。</span></div><div class="tipsBtnBar"><a href="/member/security/realNameIdentify.html" class="okBtn">马上去认证</a></div></div>'
							}
						});
					}
				});
			}
		}
	})*/
	//银行卡号解绑	
	$(".cardList").delegate("#resetBank","click",function(){
		var re_url = $(this).attr("data-value");
		require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
			//构造确认框DOM
			$.layer({
			    type: 1,
			    closeBtn: [0,true],
                		    title: "&nbsp;",
			    area: ['450px', '190px'],
			    border: [1, 1, '#cecfd0'],
			    page: {
			        html: '<div class="tipsWrap w450"><div class="tipsTxt"><i class="iconfont tipIco">&#xe63c;</i><span>确定解除绑定这张银行卡？</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn">确认</a><a href="javascript:;" class="cancleBtn">取消</a></div></div>'
			    }
			});	
			//确认操作
			$(".okBtn").click(function(){
				$.ajax({
					url:re_url,
					type:"post",
					success: function(data){
						if(data.result){
							layer.closeAll();
							$.layer({
							    type: 1,
							    closeBtn: [0,true],
				                		    title: "&nbsp;",
							    area: ['384px', '186px'],
							    border: [1, 1, '#cecfd0'],
							    time:3,
							    page: {
							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>解除绑定银行卡成功</span></div><div class="tipsMsg">3秒后窗口自动关闭</div></div>'
							    },
							    close: function(index){
							    	window.location.reload();
							    },
							    end: function(){
							    	window.location.reload();
							    },
							    success: function(layero){
							    	var time =3; 
							    	function closeTime () {
										time--;
										$(".tipsMsg").html(time+"秒后窗口自动关闭");
    								}
							    	setInterval(function(){
										closeTime();
									}, 1000);
							    }
							});
						}else{
							layer.closeAll();
							$.layer({
							    type: 1,
							    closeBtn: [0,true],
				                		    title: "&nbsp;",
							    area: ['384px', '186px'],
							    border: [1, 1, '#cecfd0'],
							    time:3,
							    page: {
							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont errIco">&#xe63e;</i><span>'+data.msg+'</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn failBtn">确定</a></div></div>'
							    },
							    close: function(index){
							    	layer.closeAll();
							    }
							});
							$(".failBtn").click(function(){
								layer.closeAll();
							});
						}						
					}			
				});				
			});
			//删除操作
			$(".cancleBtn").click(function(){
				layer.closeAll();
			});
		}); 
	});
	
	//汇付添加银行卡
	$(".chinapnrCard").click(function(){
		require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
			var okLayer = $.layer({
			type: 1,
			closeBtn: [0,true],
			title: "&nbsp;",
			area: ['450px', '190px'],
			border: [1, 1, '#cecfd0'],
			page: {
			    html: '<div class="tipsWrap w450"><div class="tipsTxt"><i class="iconfont tipIco">&#xe63c;</i><span>银行卡是否添加成功？</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn confirmOk">确认</a><a href="javascript:;" class="cancleBtn confirmCancle">取消</a></div></div>'
			}
		});
		//确认
		$(".confirmOk").click(function(){
			window.location.reload();
		});
		//取消
		$(".confirmCancle").click(function(){
			layer.closeAll();
		});
	})
			});	
		
});