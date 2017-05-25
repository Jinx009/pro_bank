define(function(require,exports,module){
	require('jquery');
	var showTable = require("../member/showTableUpdate");
	showTable.ajaxUrl("/bond/saleableBondList.html?status=1",require("../../tpl/bond/transferable.tpl"));
	//状态搜索
	$("#status a").live("click",function(){
		$(this).addClass("current").siblings().removeClass("current");
		search();
	});
	//搜索通用功能
	function search()
	{
		var tpl;
		var param;
		var statusVal = $("#status a.current").data("val");
		switch(statusVal)
		{
			case 1://可转让
			param = "/bond/saleableBondList.html?status=" + statusVal;
			tpl = require("../../tpl/bond/transferable.tpl");
			break;
			case 2://转让中
			param = "/bond/sellingBondList.html?status=" + statusVal;
			tpl = require("../../tpl/bond/transfering.tpl");
			break;
			case 3://已转出
			param = "/bond/soldBondList.html?status=" + statusVal;
			tpl = require("../../tpl/bond/transferredOut.tpl");
			break;
			case 4://已转入
			param = "/bond/boughtBondList.html?status=" + statusVal;
			tpl = require("../../tpl/bond/transferredIn.tpl");
			break;
		}
		showTable.ajaxUrl(param,tpl);
	}

	//转让操作
	var bondVal;//债权价值
	var $transferPart = $(".transferPart");//转让债权
	var transferPartVal = $transferPart.val();//转让价格
	var $discount = $(".discountInput");//转让折让率
	var $transferVal = $(".transferVal");//转让价格
	var discountValL;//最低转让折让率
	var discountValH;//最高转让率
	var discountVal;//转让折让率
	var $reduce = $(".reduce");
	var $add = $(".add");
	$(".transferAble").live("click",function(){
		//折让率赋值
		discountValL = parseFloat($(this).attr("data-discountMin"));
		discountValH = parseFloat($(this).attr("data-discountMax"));
		$discount.val(discountValL);
		//清空错误提示语
		$(".errorTip").eq(0).html("");
		$(".errorTip").eq(1).html("");
		//转让协议id
		$("#bondSellProtocolPreview").attr("data-val",$(this).data("value")[1]);
		//债权价值
		bondVal = $(this).data("value")[0];
		$(".bondVal").text(bondVal);
		//债权小于1000全部转让
		if(bondVal<=1000)
		{
			$transferPart.val(bondVal).attr("readonly","readonly");
			bondCount()
		}
		else
		{
			$transferPart.val("").attr("readonly",false);
			$transferVal.text("0.00");
		}
		//隐藏传值
		$('input[name="borrowId"]').val($(this).data("value")[1]);
		$('input[name="borrowTenderId"]').val($(this).data("value")[2]);
		$('input[name="kfId"]').val($(this).data("value")[3]);
		$('input[name="type"]').val($(this).data("value")[4]);

		//转让弹窗
		require.async(["/plugins/layer-v1.8.4/skin/layer.css","/plugins/layer-v1.8.4/layer.min.js"],function(){
			$.layer({
				type:1,
				closeBtn:[0,true],
				title:"转让设置",
				area:['728px','445px'],
				border:[1,1,'#cecfd0'],
				page:{dom:"#transfer"},
				close:function(){
					$reduce.addClass("disabled");
					$add.removeClass("disabled");
				}
			})
		});
	});
	
	//解决IE下不支持placeholder
	require.async('common1',function(){
		if($.browser.msie) { 
			$(":input[placeholder]").each(function(){
				$(this).placeholder({
					pColor: "#ccc",
		            pActive: "#999",
		            pFont: "14px",
		            activeBorder: "#080",
		            posL: 90,
		            zIndex: "99"
				});
			});
		}
	})

	//转让价格计算
	function bondCount()
	{
		if($transferPart.val() == "")
		{
			transferPartVal = 0;
		}
		else
		{
			transferPartVal = parseFloat($transferPart.val());
		}
		$transferVal.text((transferPartVal - transferPartVal*parseFloat($discount.val())/100).toFixed(2));//计算转让价格
	}

	//转让债权计算
	$transferPart.keyup(function(){
			bondCount();
			validateValue();
	})

	//折让率增加
	$add.click(function(){
		if(!$add.hasClass("disabled"))
		{
			$discount.val((parseFloat($discount.val()) + 0.1).toFixed(1));
			bondCount();
			if(parseFloat($discount.val()) == discountValH)
			{
				$add.addClass("disabled");
			}
			else if(parseFloat($discount.val()) > discountValL)
			{
				$reduce.removeClass("disabled");
			}
		}
	});

	//折让率减少
	$reduce.click(function(){
		if(!$reduce.hasClass("disabled"))
		{
			$discount.val((parseFloat($discount.val()) - 0.1).toFixed(1));
			bondCount();
			if(parseFloat($discount.val()) == discountValL)
			{
				$reduce.addClass("disabled");
			}
			else if(parseFloat($discount.val()) < discountValH)
			{
				$add.removeClass("disabled");
			}
		}
	})

	//转让校验
	var valueFlag,protocolFlag;
	var $errorTip = $(".errorTip");
	//转让债权校验
	function validateValue()
	{
		valueFlag = true;
		var transferPartVal = parseFloat($transferPart.val());
		if(bondVal > 1000)
		{
			if(!transferPartVal || transferPartVal < 1000 || transferPartVal > bondVal)
			{
				$errorTip.eq(0).html('<i class="iconfont">&#xe60f;</i>请输入[1000,'+bondVal+']的转让债权');
				valueFlag = false;
			}
			else
			{
				$errorTip.eq(0).html("");
			}
		}
		else if(bondVal <= 1000)
		{
			if(transferPartVal != bondVal)
			{
				$errorTip.eq(0).html('<i class="iconfont">&#xe60f;</i>请输入全部的剩余债权价值');
				valueFlag = false;
			}
			else
			{
				$errorTip.eq(0).html("");
			}
		}
	}

	function validateProtocol(){
		protocolFlag = true;	
		//协议校验
		if(!$('input[name="protocol"]').prop("checked")){
			$errorTip.eq(1).html('<i class="iconfont">&#xe60f;</i>请认真阅读，并同意《转让协议》');
			protocolFlag = false;
		}
		else
		{
			$errorTip.eq(1).html("");
		}
	}

	$('input[name="protocol"]').click(function(){
		validateProtocol();
	});
	//保存操作
	$(".transferBtn a").click(function(){
		validateValue();
		validateProtocol();
		if(protocolFlag&&valueFlag)
		{
			require.async("jquery.form",function(){
				$("#transfer").ajaxSubmit({
					type:"get",
					dataType:"json",
					success:function(data){
						if(data.result)
						{
							require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
								layer.closeAll();
								$discount.val(discountVal);
								$reduce.addClass("disabled");
								$.layer({
									    type: 1,
									    closeBtn: [0,true],
						                		    title: "&nbsp;",
									    area: ['400px', '186px'],
									    border: [1, 1, '#cecfd0'],
									    time:3,
									    page: {
									        html: '<div class="tipsWrap w420"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>操作成功，您可在转让中的记录列表中查看。</span></div><div class="tipsMsg"><i class="tipsTime">3</i>秒后窗口自动关闭</div></div>'
									    },
									    success:function(){
									    	var time = 3;
									    	function tipsTime(){
									    		time--;
									    		$(".tipsTime").html(time)
									    		if(time == 0)
									    		{
									    			clearInterval(timeCount)
									    		}
									    	}
									    	var timeCount = setInterval(function(){tipsTime()},1000);
									    },
									    close:function(){
									    	window.location.reload();
									    },
									    end:function(){
									    	window.location.reload();
									    }
								});
							});
						}
					}
				});
			})
		}
	});
	
	//撤回操作
	$(".revocation").live("click",function(){
		var $this = $(this);
		require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
			$.layer({
				    type: 1,
				    closeBtn: [0,true],
	                		    title: "&nbsp;",
				    area: ['450px', '200px'],
				    border: [1, 1, '#cecfd0'],
				    page: {
				        html: '<div class="tipsWrap w450"><div class="tipsTxt"><i class="iconfont tipIco">&#xe63c;</i><span>确定要撤回转让剩余的债权吗？</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn failBtn">确定</a></div></div>'
				    },
				    close: function(index){
				    	layer.closeAll();
				    }
				});
				$(".failBtn").click(function(){
					layer.closeAll();
					$.ajax({
						url:"/bond/stopBond.html?id="+$this.attr("data-val"),
						type:"get",
						dataType:"json",
						cache:false,
						success:function(data){
							if(data.result)//撤销成功
							{
								//重新载入输入
								showTable.ajaxUrl("/bond/sellingBondList.html?status=2",require("../../tpl/bond/transfering.tpl"));
								$.layer({
									    type: 1,
									    closeBtn: [0,true],
							            	    title: "&nbsp;",
									    area: ['384px', '186px'],
									    border: [1, 1, '#cecfd0'],
									    time:3,
									    page: {
									        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>已成功撤回转让剩余的债权！</span></div><div class="tipsMsg"><i class="tipsTime">3</i>秒后窗口自动关闭</div></div>'
									    },
									    success:function(){
									    	var time = 3;
									    	function tipsTime(){
									    		time--;
									    		$(".tipsTime").html(time)
									    		if(time == 0)
									    		{
									    			clearInterval(timeCount)
									    		}
									    	}
									    	var timeCount = setInterval(function(){tipsTime()},1000);
									    }
								});
							}
							else//撤销失败
							{
								$.layer({
									    type: 1,
									    closeBtn: [0,true],
							            	    title: "&nbsp;",
									    area: ['384px', '186px'],
									    border: [1, 1, '#cecfd0'],
									    time:3,
									    page: {
									        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont errIco">&#xe63e;</i><span>撤销操作失败！</span></div><div class="tipsMsg"><i class="tipsTime">3</i>秒后窗口自动关闭</div></div>'
									    },
									    success:function(){
									    	var time = 3;
									    	function tipsTime(){
									    		time--;
									    		$(".tipsTime").html(time)
									    		if(time == 0)
									    		{
									    			clearInterval(timeCount)
									    		}
									    	}
									    	var timeCount = setInterval(function(){tipsTime()},1000);
									    }
								});
							}
						}
					})
					
					
				});
		})
	})

	//转让中详情和已转出详情
	$(".transferingDetail").live("click",function(){
		var $this = $(this);
		require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
			$.layer({
				type: 1,
				closeBtn: [0,true],
				title: "&nbsp;&nbsp;",
				area: ['874px', '220px'],
				border: [1, 1, '#cecfd0'],
				page: {
				    html: '<div class="tipsWrap w794"><table class="table table-bordered"><thead><tr><th>项目名称</th><th>收益率</th><th>投资金额</th><th>已持有期限</th><th>计息起始日</th><th>到期日</th><th>还款方式</th></tr></thead><tbody id="transferingDetailTable"></tbody></table></div>'
				},
				success:function(){
					$.ajax({
						url:"/bond/sellDetail.html?id="+$this.attr("data-val"),
						type:"get",
						dataType:"json",
						success:function(json){
							$("#transferingDetailTable").html(Handlebars.compile(require("../../tpl/bond/transferingDetail.tpl"))(json));
						}
					})
				}
			});
		});
	});

	//已转入详情
	$(".transfeInDetail").live("click",function(){
		var $this = $(this);
		require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
			$.layer({
				type:1,
				closeBtn:[0,true],
				title:"&nbsp;&nbsp;",
				area:['874px', '220px'],
				border:[1, 1, '#cecfd0'],
				page:{
					html: '<div class="tipsWrap w794"><table class="table table-bordered"><thead><tr><th>项目名称</th><th>收益率</th><th>投资金额</th><th>债权剩余期限</th><th>计息起始日</th><th>到期日</th><th>还款方式</th></tr></thead><tbody id="transferInDetailTable"></tbody></table></div>'
				},
				success:function(){
					$.ajax({
						url:"/bond/buyDetail.html?id="+$this.attr("data-val"),
						type:"get",
						dataType:"json",
						success:function(json){
							$("#transferInDetailTable").html(Handlebars.compile(require("../../tpl/bond/transferInDetail.tpl"))(json));
						}
					})
				}
			});
		});
	});

	//债权转让协议预览
	$("#bondSellProtocolPreview").click(function(){
		$.layer({
			type:2,
			tilte:"协议预览",
			border:[1,1,'#cecfd0'],
			area: ['1000px' , '500px'],
			iframe: {src: '/bond/bondSellProtocolPreview.html?id='+$("#bondSellProtocolPreview").attr("data-val")}
		});
	})

})