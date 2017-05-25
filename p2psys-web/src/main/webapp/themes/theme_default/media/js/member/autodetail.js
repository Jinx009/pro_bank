define(function(require,exports,module){
	require('jquery');
	//开关切换
	$('.autoSwitch').live('click',function(){
		var $autoSwitch = $(this);
		var $input = $autoSwitch.find('input');
		if($autoSwitch.hasClass("autoSwitch_hover"))
		{
			$input.prop("checked",false).val('false');
			$autoSwitch.removeClass('autoSwitch_hover');
			$('.select_condition').addClass('hide');
			$autoSwitch.next().addClass('hide');
			//关闭异步请求
			$.ajax({
				type:"post",
				url:"/member/auto/modifyStatus.html",
				dataType:"json",
				success:function(data){
					require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
						$.layer({
							    type: 1,
							    closeBtn: [0,true],
				                		    title: "&nbsp;",
							    area: ['384px', '186px'],
							    border: [1, 1, '#cecfd0'],
							    time:3,
							    page: {
							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>自动投标已关闭</span></div><div class="tipsMsg"><i class="tipsTime">3</i>秒后窗口自动关闭</div></div>'
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
					});
				}
			});
		}
		else
		{
			//开启异步校验最小余额请求
			$.ajax({
				type:"post",
				url:"/member/auto/modifyStatus.html?enable=true",
				dataType:"json",
				success:function(data){
					if(data.result)
					{
						$input.prop("checked",true).val('true');
						$autoSwitch.addClass('autoSwitch_hover');
						$('.select_condition').removeClass('hide');
					}
					else
					{
						require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
							$.layer({
								type: 1,
								closeBtn: [0,true],
								title: "&nbsp;",
								area: ['450px', '190px'],
								border: [1, 1, '#cecfd0'],
								page: {
								html: '<div class="tipsWrap w450"><div class="tipsTxt"><i class="iconfont tipIco">&#xe63c;</i><span>账户可用余额不足，先充值才能体验自动投资</span></div><div class="tipsBtnBar"><a href="/member/recharge/newRecharge.html" class="okBtn">去充值</a></div></div>'
								}
							});
						});
					}
				}
			});
		}
	});

	//修改设置
	$(".changeStatus").click(function(){
		$('.select_condition').toggleClass("hide");
	})

	//投标金额
	$('dl.money dd label').live('click',function(){
		var $this = $(this);
		$('dl.money dd input[type="text"]').each(function(){
			$(this).val('').prop('disabled',true);
			$(this).parents("dd").find(".errorTip").html("");
		});
		$this.parent().find('input[type="text"]').each(function(){
			$(this).val('').prop('disabled',false);
		})
	})

	//投标时间
	$('dl.invest_time dd label').live('click',function(){
		var $this = $(this);
		$('dl.invest_time dd select').each(function(){
			$(this).val('').prop('disabled',true);
			$(this).parents("dd").find(".errorTip").html("");
		});
		$this.parent().find('select').each(function(){
			$(this).val('').prop('disabled',false);
		})
	})

	//年华收益
	$('.select_condition li:eq(2) label:eq(1)').live('click',function(){
		$(this).parent().find('input[type="text"]').val('').prop('disabled',true);
		$(this).parents("li").find(".errorTip").html("");
	})
	$('.select_condition li:eq(2) label:eq(2)').live('click',function(){
		$(this).parent().find('input[type="text"]').val('').prop('disabled',false);
	})

	//返回表单
	$("#reset_button").click(function(){window.location.reload();})

	//表单校验+确认单
	var flag;//表单校验标识
	function autoValidate(){
		flag = true;
		var tenderStyle = "";//投资金额
		var timelimit = "";//投资时间
		var apr = "";//年华收益
		var style =""//还款方式
		var tipLayer;
		//投资金额
		if($("#money1").prop("checked"))
		{
			tenderStyle = '可用余额全投';
		}
		else if($("#money2").prop("checked"))
		{
			var moneyVal = $('input[name="money"]').val();
			var $errorTip = $('input[name="money"]').parents("dd").find(".errorTip");
			if(moneyVal=="" || parseInt(moneyVal) < 100)
			{
				$errorTip.html('<i class="iconfont">&#xe60f;</i>请输入不少于100元的整数')
				flag = false;
			}
			else
			{
				tenderStyle = '固定每次投资<br><em>'+moneyVal+'</em>元';
				$errorTip.html("")
			}
		}
		else if($("#money3").prop("checked"))
		{
			var minVal = $('input[name="min"]').val();
			var maxVal = $('input[name="max"]').val();
			var $errorTip = $('input[name="max"]').parents("dd").find(".errorTip");
			if(minVal == "" || maxVal == "" || parseInt(minVal) > parseInt(maxVal) || parseInt(minVal) < 100)
			{
				$errorTip.html('<i class="iconfont">&#xe60f;</i>请输入正确的区间金额');
				flag = false;
			}
			else
			{
				tenderStyle = '每次投资<br><em>'+minVal+'</em>元 - <em>'+maxVal+'</em>元';
				$errorTip.html("")
			}
		}
		//投资时间
		if($("#timelimitEnable1").prop("checked"))
		{
			timelimit = '不限制';
		}
		else if($("#timelimitEnable2").prop("checked"))
		{
			var timelimitDownVal= $('select[name="timelimitDown"]').val();
			var timelimitUpVal= $('select[name="timelimitUp"]').val();
			var $errorTip = $('select[name="timelimitUp"]').parents("dd").find(".errorTip");
			if(parseInt(timelimitDownVal) > parseInt(timelimitUpVal))
			{
				$errorTip.html('<i class="iconfont">&#xe60f;</i>请选择正确的月份范围');
				flag = false;
			}
			else
			{
				$errorTip.html("");
				timelimit = '<em>'+timelimitDownVal+'</em>个月 - <em>'+timelimitUpVal+'</em>个月';
			}
		}
		//年华收益
		if($("#aprEnable1").prop("checked"))
		{
			apr = '不限制';
		}
		else if($("#aprEnable2").prop("checked"))
		{
			var aprDownVal = $('input[name="aprDown"]').val();
			var $errorTip = $('input[name="aprDown"]').parents("li").find(".errorTip");
			if(aprDownVal =="" || parseInt(aprDownVal) < 1 ||  parseInt(aprDownVal) >24)
			{
				$errorTip.html('<i class="iconfont">&#xe60f;</i>请输入正确的收益范围');
				flag = false;
			}
			else
			{
				$errorTip.html("");
				apr = '不低于<em>'+aprDownVal+'%';
			}

			
		}
		//还款方式
		var $style = $('.select_condition li:eq(3) input[type="checkbox"]');
		var $errorTip = $style.parents("li").find(".errorTip");
		var i= 0;
		$style.each(function(){
			if($(this).prop("checked"))
			{
				i++;
			}
		});
		if(i < 1)
		{
			flag = false;
			$errorTip.html('<i class="iconfont">&#xe60f;</i>请至少选择一种还款方式');
		}
		else
		{
			$errorTip.html("");
		}
		if($("#style1").prop("checked"))
		{
			style += '等额本息<br>';
		}
		if($("#style2").prop("checked"))
		{
			style += '一次性到期还本<br>';
		}
		if($("#style3").prop("checked"))
		{
			style += '每月付息到期还本';
		}
		$('#confirmationSlip td:eq(0)').html(tenderStyle);
		$('#confirmationSlip td:eq(1)').html(timelimit);
		$('#confirmationSlip td:eq(2)').html(apr);
		$('#confirmationSlip td:eq(3)').html(style);
	}
	
	//逐个input校验表单
	$('input[name="money"]').blur(function(){
		autoValidate();
	});
	$('input[name="min"]').blur(function(){
		autoValidate();
	});
	$('input[name="max"]').blur(function(){
		autoValidate();
	});
	$('select[name="timelimitDown"]').blur(function(){
		autoValidate();
	});
	$('select[name="timelimitUp"]').blur(function(){
		autoValidate();
	});
	$('input[name="aprDown"]').blur(function(){
		autoValidate();
	});
	$('.select_condition li:eq(3)').click(function(){
		autoValidate();
	})



	//表单提交
	$('#save_button').click(function(){
		autoValidate();
		if(flag)
		{
			require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
				$.layer({
					type:1,
					title:"自动投资确认单",
					area:['871px','360'],
					border: [1, 1, '#cecfd0'],
					move:false,
					page: {dom: '#confirmationSlip'}
				})
			})
			//表单提交
			$(".confirmationButton").click(function(){
					$("#autoForm").submit();
			})
		}
	})
});