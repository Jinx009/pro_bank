var buyType = 0,//是否可以预约 1代表不能
	virtual = 0,//虚拟货币原始金额
	profitArray,//权益规则数组
	moneyType = 1,//3代表指定金额投资，2代表规则增加投资，1代表自由金额投资
	investType = 0,//实际支付规则，0代表全额投资，1代表预约投资
	money = 0,//投资金额
	minMoney = 0,//最小投资金额
	addAmount = 0,//金额增加幅度
	proportion = 0,//保障金比例
	projectType = 1,//产品类型
	realMoney = 0;//真实付款金额
$(function(){
	var timestamp = Date.parse(new Date());
	$('#orderId').val(timestamp);
	var status = $('#status').val();
	var projectId = $('#projectId').val();
	var ruleId = $('#ruleId').val();
	if(0==status){
		layer.msg('点击设置交易密码！',{
	        time: 2500
	    });
		 setTimeout(function(){
        	 location.href= '/cf/wechat/user/set-pay.html?redirectUrl=/wechat/pro/buy.html?projectId='+projectId+'%26ruleId='+ruleId;
         },2500);
	}
	
	$('.orderBack').click(function(event){
		 event.preventDefault();
		 location.href='/cf/wechat/user/rightsDetail.html?projectId='+$('#projectId').val();
	});
	
	var params = 'id='+projectId;
	
	//获取相应产品数据
	$.ajax({
		url:'/cf/wechat/buyProjectDetail.action',
		type:'POST',
		data:params,
		dataType:'json',
		success:function(res){
			var p = res.data.pro;
			var data = res.data;
			data.type = p.type;
			projectType = p.type;
			minMoney = p.minMoney;
			addAmount = p.addAmount;
			proportion = p.breach;
			data.moneyType = getMoneyType(p);
			moneyType = data.moneyType;
			profitArray = res.data.pro.profitRuleList;
			if(2==res.data.pro.type){
				layer.alert('温馨提示：股权项目只能支持一次！',{title:false,closeBtn:0});
			}
			//构建页面数据模型
			var model =new Vue({
	            el: 'body',
	            data: {
	                data:res.data
	            }
	        });
			//不是股权众筹无预约功能
			if(2!=p.type){
				$('.add-big').hide();
				buyType = 1;
			}
			//指定金额投资
			if(3===data.moneyType){
				var value = $('#ruleId').val();
				var element = document.getElementById('profit');
				for (i = 0; i < element.length; i++){
					if (value == element.options[i].value){
						element.options[i].selected = true;
					}
				}
				changeMoney();
			}else{
				changeMoney();
			}
			//是否可用800币
			if(1!=p.type||0==data.virual){
				data.virualType = 0;
			}else{
				data.virualType = 1;
			}
		}
	})
	
	// add By False
	if(projectType == 2){
		$(".order-p").html("由于认购股权众筹项...>>")
		$(".order-p-auto").html("由于认购股权众筹项目作为一种直接投资方式，800众服平台（“平台”）及项目发起方或领投人等，均无法保证您的本金及收益，在投资过程中可能会面临多种风险因素。因此，根据有关法律法规规定的要求，在您选择参与股权众筹项目前，请仔细阅读以下《风险提示书》。")
	}else{
		$(".order-p").html("法律声明：以上商品（或产品）标识...>>")
		$(".order-p-auto").html("法律声明：以上商品（或产品）标识、描述、图片及宣传内容等均由项目方提供，其真实性、准确性和合法性由信息提供者（项目方）负责，本站不提供任何保证。项目方应严格按照中国境内相关法律法规的规定提供商品（或产品）的内容及服务。投资者如遇任何商品（或产品）内容、质量及配送等问题可向项目方进行咨询。")
	}
	$(".order-p").click(function(){
		$(".order-p").hide();
		$(".order-p-auto").slideDown();
	})
	$(".order-p-auto").click(function(){
		$(".order-p").slideDown();
		$(".order-p-auto").hide();
	})
})

/**
 * 更换购买方式
 */
function changeBuyType(type){
	if(0!=buyType){
		/*alert(type);*/
		//layer.alert(type, {icon: 2});
	}else{
		investType = type;
		$('#subBig').removeClass('sub-big');
		$('#addBig').removeClass('add-big');
		$('#addBig').removeClass('sub-big');
		$('#subBig').removeClass('add-big');
		if(0==type){
			$('#subBig').addClass('sub-big');
			$('#addBig').addClass('add-big');
		}else{
			$('#subBig').addClass('add-big');
			$('#addBig').addClass('sub-big');
		}
		changeMoney();
	}
}

/**
 * 选取投资方式
 * @param element
 */
function getMoneyType(element){
	if(1<element.profitRuleList.length){
		return 3;
	}else{
		if(0!=element.addAmount){
			return 2;
		}else{
			return 1;
		}
	}
}

/**
 * 购买
 * @param type
 */
function doBuy(type){
	var status = false;
	var payPwd = $('#payPwd').val(),
	id = $('#projectId').val();
	var profit = $('#ruleId').val();
	if($('#profit').length){
		profit = $('#profit').val();
	}
	if(!status){
		if(1==type){
			var params = 'payPwd='+payPwd+'&investMoney='+money+'&virtual='+virtual+'&id='+id+'&profitRule='+profit; 
			$.ajax({
				url:'/cf/user/buyData.action',
				type:'POST',
				data:params,
				dataType:'json',
				success:function(res){
					if('success'==res.result){
						//实物众筹
						if(1==projectType){
							var orderId = res.errorMsg.id;
							var address = $('#address').val();
							var mobielPhone = $('#mobilePhone').val();
							var postNum = $('#postNum').val();
							var realName = $('#realName').val();
							params = 'id='+orderId+'&address='+address+'&mobilePhone='+mobilePhone+'&postNum='+postNum+'&realName='+realName;
							$.ajax({
								url:'/cf/order/addressInfo.action',
								type:'POST',
								data:params,
								dataType:'json',
								success:function(res){
									layer.alert('恭喜您支持成功！',{title:false,closeBtn: 0},function(){
										location.href = '/cf/wechat/user/buyList.html';
									});
								} 
							})
						}else{
							layer.alert('恭喜您支持成功！',{title:false,closeBtn: 0},function(){
								location.href = '/cf/wechat/user/buyList.html';
							});
						}
					}else{
						if('账户可用余额不足!'==res.errorMsg){
							layer.confirm('账户可用余额不足！', {
							    btn: ['去充值','确定'],title:false,closeBtn: 0 
							}, function(){
								location.href = "/cf/wechat/user/recharge.html";
							});
						}else{
							layer.alert(res.errorMsg,{title:false,closeBtn: 0});
						}
					}
				}
			})
		}else{
			//股权众筹
			var params = 'payPwd='+payPwd+'&investMoney='+money+'&id='+id+'&type='+investType+'&profitRule='+profit;
			$.ajax({
				url:'/cf/user/order.action',
				type:'POST',
				data:params,
				dataType:'json',
				success:function(res){
					if('success'==res.result){
						layer.alert('恭喜您，支持成功！',{title:false,closeBtn: 0},function(){
							location.href = '/cf/wechat/user/buyList.html';
						});
					}else{
						if('账户可用余额不足!'==res.errorMsg){
							layer.confirm('账户可用余额不足！', {
							    btn: ['去充值','确定'],title:false,closeBtn: 0 
							}, function(){
								location.href = "/cf/wechat/user/recharge.html";
							});
						}else{
							layer.alert(res.errorMsg,{title:false,closeBtn: 0});
						}
					}
				}
			})
		}
	}
}

/**
 * 更换数据
 */
function changeMoney(type){
	var buyType = moneyType;
	if(0==investType){//全额投资
		if(1==moneyType){
			money = $('#money').val();
			money = parseFloat(money);
			virtual = getVirtual();
			realMoney = money-virtual;
			$('#realMoney').html('￥'+realMoney);
			$('#profitContent').html(profitArray[0].content);
		}else if(2==moneyType){
			money = $('#money').val();
			money = parseFloat(money);
			if(1==type&&money>minMoney){
				money = money-addAmount;
				virtual = getVirtual();
				$('#money').val(money);
				realMoney = money-virtual;
				$('#realMoney').html('￥'+realMoney);
				$('#profitContent').html(profitArray[0].content);
			}else if(0==type){
				money = money+addAmount;
				virtual = getVirtual();
				$('#money').val(money);
				realMoney = money-virtual;
				$('#realMoney').html('￥'+realMoney);
				$('#profitContent').html(profitArray[0].content);
			}else{
				virtual = getVirtual();
				$('#money').val(money);
				realMoney = money-virtual;
				$('#realMoney').html('￥'+realMoney);
				$('#profitContent').html(profitArray[0].content);
			}
		}else if(3==moneyType){
			var profitId = $('#profit').val();
			for(var i in profitArray){
				if(profitId == profitArray[i].id){
					money = parseFloat(profitArray[i].money);
					virtual = getVirtual();
					realMoney = money-virtual;
					$('#realMoney').html('￥'+realMoney);
					$('#profitContent').html(profitArray[i].content);
				}
			}
		}
	}else{//预约投资
		if(1==moneyType){
			money = $('#money').val();
			realMoney = parseFloat(money*proportion).toFixed(2);
			$('#realMoney').html('￥'+realMoney);
			$('#profitContent').html(profitArray[0].content);
		}else if(2==moneyType){
			money = $('#money').val();
			money = parseFloat(money);
			if(1==type&&money>minMoney){
				money = money-addAmount;
				$('#money').val(money);
				realMoney = parseFloat(money*proportion).toFixed(2);
				$('#realMoney').html('￥'+realMoney);
				$('#profitContent').html(profitArray[0].content);
			}else if(0===type){
				money = money+addAmount;
				$('#money').val(money);
				realMoney = parseFloat(money*proportion).toFixed(2);
				$('#realMoney').html('￥'+realMoney);
				$('#profitContent').html(profitArray[0].content);
			}else{
				realMoney = parseFloat(money*proportion).toFixed(2);
				$('#realMoney').html('￥'+realMoney);
				$('#profitContent').html(profitArray[0].content);
			}
		}else if(3==moneyType){
			var profitId = $('#profit').val();
			for(var i in profitArray){
				if(profitId == profitArray[i].id){
					money = parseFloat(profitArray[i].money);
					realMoney = parseFloat(money*proportion).toFixed(2);
					$('#realMoney').html('￥'+realMoney);
					$('#profitContent').html(profitArray[i].content);
				}
			}
		}
	}
}

/**
 * 是否存在虚拟货币
 */
function getVirtual(){
	if($('#virtual').length){
		virtual = $('#virtual').val();
		virtual = parseFloat(virtual);
		return virtual;
	}else{
		return virtual;
	}
}