define(function(require,exports,module){
	require('jquery');
	var tpl = require('../../tpl/member_borrow/borrow_repayment.tpl');//载入tpl模板
	//初始化显示表格、搜索
	
	if(window.location.search.substr(1)){//还款明细
		require.async(['./showTable','./search'],function(showTable,search){
			showTable.ajaxUrl('/member/borrow/repaymentList.html?'+window.location.search.substr(1)+'&status=99&time=-1',tpl);
			search.search('/member/borrow/repaymentList.html?'+window.location.search.substr(1)+'&',tpl);
		});
	}else{
		require.async(['./showTable','./search'],function(showTable,search){
			showTable.ajaxUrl('/member/borrow/repaymentList.html?status=99&time=-1',tpl);
			search.search('/member/borrow/repaymentList.html?',tpl);
		});
	
	}
	
	//【托管】环迅还款
	$("#toPayBtn").live("click",function(){
		//获取还款ID
		var idVal = $(this).attr("data-val");
		//获取还款金额
		var totalVal = parseInt($(this).attr("data-total"));
		//获取可用余额
		var moneyVal = parseInt($(this).attr("date-money"));
		
		if(totalVal > moneyVal){
			require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
				//构造确认框DOM
				$.layer({
					type: 1,
					closeBtn: false,
	                title: "&nbsp;",
				    area: ['450px', '190px'],
				    border: [1, 1, '#cecfd0'],
				    page: {
				        html: '<div class="tipsWrap w450"><div class="tipsTxt"><i class="iconfont tipIco">&#xe63c;</i><span>您的账户余额不足，请先充值。</span></div><div class="tipsBtnBar"><a href="/member/recharge/newRecharge.html" class="okBtn">马上去充值</a></div></div>'
				    }
				});
			});
		}else{
			window.open("/invest/repaySkip.html?repaymentId="+idVal);
			require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
				$.layer({
                    type: 1,
                    closeBtn: [0,true],
                    title: "&nbsp;",
                    area: ['460px', '194px'],
                    border: [1, 1, '#cecfd0'],
                    page: {
                        html: '<div class="tipsWrap"><dl><dt>还款完成前，请不要关闭本窗口;</dt><dd>还款完成后，请根据您的还款结果点击下面按钮。</dd></dl><div class="tipsBtnBar"><a href="javascript:;" class="okBtn">还款成功</a><a href="javascript:;" class="cancleBtn">还款失败</a></div></div>'
                    }
                });
				$(".okBtn").click(function(){
					window.location.reload();	
				});
				$(".cancleBtn").click(function(){
					layer.closeAll();
				});
			}); 
		}
	})
	
	//【托管】汇付还款
	require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
		$(".chinapnr").live("click",function(){
			var _this = $(this);
			var reMoney = parseFloat(_this.attr("data-tatal")).toFixed(2);
			$.layer({
				type :1,
				title : "<span class='tipsTitle'><i class='iconfont'>&#xe642;</i><b>每笔资金受特别保护，安全可靠，请放心操作</b></span>",
				closeBtn :[0,true],
				area: ['384px', '236px'],
				border: [1, 1, '#cecfd0'],
				page: {
				      html: '<div class="tipsWrap w384"><form><ul><li><strong>应还金额：</strong><span>'+reMoney+'</span>元</li><li class="repayment-mm"><strong>支付密码：</strong><input value="" type="password" name="payPwd"></li></ul></form><div class="tipsBtnBar"><a href="javascript:;" class="okBtn">确认还款</a></div></div>'
				}
			});
			$(".okBtn").click(function(){
				 $.ajax({
					type:"post",
					url:'/invest/repaySkip.html?repaymentId='+_this.attr("data-val"),
					dataType:"json",
					success:function(data){
    						if(data.result){
    							layer.closeAll();
    							var borrowId=window.location.search;
    							$.layer({
    								type: 1,
    							    closeBtn: [0,true],
    				                title: "&nbsp;",
    							    area: ['384px', '186px'],
    							    border: [1, 1, '#cecfd0'],
    							    time:3,
    							    page: {
    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>还款成功</span></div><div class="tipsMsg">3秒后窗口自动关闭</div></div>'
    							    },
    							    close: function(index){
    							    	window.location.href="/member_borrow/borrow/repayment.html"+borrowId;
    							    },
    							    end: function(){
    							    	window.location.href="/member_borrow/borrow/repayment.html"+borrowId;
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
	    						var closeLayer = $.layer({
									type: 1,
								    closeBtn: [0,true],
					                title: "&nbsp;",
								    area: ['384px', '186px'],
								    border: [1, 1, '#cecfd0'],
								    page: {
								        html: '<div class="tipsWrap w384"><div class="tipsTxt1"><i class="iconfont errIco">&#xe63e;</i><span>'+data.msg+'</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn failBtn">确定</a></div></div>'
								    },
								    close: function(index){
								    	layer.close(closeLayer);
								    }
								});
								$(".failBtn").click(function(){
									layer.close(closeLayer);
								});
	    					}
					}
				});
			});

		})
	})

	// 标准版还款
	require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
		$(".repayment").live("click",function(){
			var _this = $(this);
			var reMoney = parseFloat(_this.attr("data-tatal")).toFixed(2);

			$.layer({
				type :1,
				title : "<span class='tipsTitle'><i class='iconfont'>&#xe642;</i><b>每笔资金受特别保护，安全可靠，请放心操作</b></span>",
				closeBtn :[0,true],
				area: ['384px', '236px'],
				border: [1, 1, '#cecfd0'],
				page: {
				      html: '<div class="tipsWrap w384"><form><ul><li><strong>应还金额：</strong><span>'+reMoney+'</span>元</li></ul></form><div class="tipsBtnBar"><a href="javascript:;" class="okBtn">确认还款</a></div></div>'
				}
			});
			$(".okBtn").click(function(){
				 $.ajax({
					type:"post",
					url:'/member/borrow/overduePayment.html?repaymentId='+_this.attr("data-val"),
					dataType:"json",
					success:function(data){
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
    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>还款成功</span></div><div class="tipsMsg">3秒后窗口自动关闭</div></div>'
    							    },
    							    close: function(index){
    							    	window.location.reload();
    							    },
    							    end: function(){
    							    	window.location.reload();
    							    }
	    						});
	    					}
					}
				});
			});
			

		})
	})
});