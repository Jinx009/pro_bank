var data;
var matList;
var prodId;
var systemDate;
$(function(){
	prodId = $("#productId").val();
	$.ajax({
		url:"/product/showProductDetail.action?prodId="+prodId+"&time="+jsStrToTime(),
		type:"GET",
		dataType:"json",
		ansyc:false,
		success:function(res)
		{
			data = res.data;
			matList = res.matList;	
			systemDate = res.systemDate;
			changeHtml();
			Countdown(data,systemDate);
		}
	})	
	
	
	$.ajax({
		url:"/user/capitalStatis.action?time="+jsStrToTime(),
		type:"GET",
		dataType:"json",
		ansyc:false,
		success:function(res)
		{
			if(checkUser(res.result))
			{
				$("#useMoney").html("&yen;"+format_(res.useMoney));
			}
			else
			{
				showDiv("util_login");
			}
		}
	})
})

function addInvest()
{
	var recommendId = redPacketRecommend,notRecommendId = redPacketNotRecommend;
	var ids = "";
	
	if(""!=recommendId)
	{
		ids += recommendId+"str";
	}
	if(""!=notRecommendId)
	{
		ids += notRecommendId+"str";
	}
	var flag = false;
	
	var money = $("#money").val(),
		key = $("#payPwd").val(),
		pwd = $("#inputpwd").val(),
		subMoney = $("#realMoney").html(),
		productBasicId = $("#productId").val();
	var isBindC = $("#isBindC").val();
	
	if(0==parseFloat(subMoney))
	{
		subMoney = money;
	}
	if(isBindC==null||isBindC==undefined){
		if(money==0||money==null||money=="")
		{
			flag = true;
		}
		if(flag)
		{
			money = $("#investMoneyInp").val();
		}
	}
	var goldMoney = 0;
	
	if($("#goldMoney").length>0)
	{
		goldMoney = $("#goldMoney").val();
	}
	
	var params  = "productBasicId="+productBasicId+"&money="+money+"&goldMoney="+goldMoney+"&pwd="+pwd+"&subMoney="+subMoney+"&ids="+ids + "&cids=" + $("#coupons-id").val();
	var params2 = "key="+key;
	
	$.ajax({
		url:"/nb/checkPayPwd.action",
		data:params2,
		dataType:"json",
		type:"POST",
		success:function(res)
		{
			if(checkUser(res.result))
			{

				if("success"==res.result)
				{
					$.ajax({
						url:"/nb/pc/account/addInvest.action",
						type:"POST",
						data:params,
						dataType:"json",
						success:function(res)
						{
							if(checkUser(res.result))
							{
								if("success"==res.result)
								{
									showAlertDiv(true,"恭喜您投资成功!","","/user/center.html");
								}
								else
								{
									if("noMoney"==res.errorMsg)
									{
										showAlertDiv(false,"账户余额不足!","点击确定充值","/nb/pc/recharge/newRecharge.html");
									}
									else
									{
										showAlertDiv(false,null,res.errorMsg,null);
									}
								}
							}
						}
					})
				}
				else
				{
					showAlertDiv(false,null,res.errorMsg,null);
				}
			}
		  }
	})
}
/**
 * 拼接json
 */
function changeHtml(){
	$("#productTitle").html(data.productName);
	
	if(null!=data.ppfundModel){
		$("#startMoney").html("&yen;"+format_(data.ppfundModel.lowestAccount));
		$("#timeLimit").html(getTimeLimit(data.ppfundModel.timeLimit));
		$("#interestWay").html("T+"+data.ppfundModel.interestWay);
		$("#rate").html(getRate(data.lowestRefundRate,data.highestRefundRate,data.ppfundModel.interestRateValue));
		getVatePPfund(data,0);
	}	
	if(null!=data.borrowModel){
		$("#startMoney").html("&yen;"+format_(data.borrowModel.lowestAccount));
		$("#timeLimit").html(data.borrowModel.timeLimitStr);
		$("#mostAccount").html("&yen;"+format_(data.borrowModel.account));
		$("#progress").css("width",data.borrowModel.scales+"%");
		$("#progressComplete").html(data.borrowModel.scales+"% Complete");
		
		$("#scale").html(data.borrowModel.scales+"%");
		$("#rate").html(getRate(data.lowestRefundRate,data.highestRefundRate,data.borrowModel.interestRateValue));
		if(data.borrowModel.unitAmount>0){
			$("#unitAmount").show();
			$("#unUnitAmount").hide();
			$("#investNumInp").val(data.borrowModel.unitAmount);
			$("#investNum").html("&yen;"+format_(data.borrowModel.unitAmount)+"/份");
			$("#investMoney").html("&yen;"+format_(data.borrowModel.unitAmount));
			$("#investMoneyInp").val(data.borrowModel.unitAmount);
		}else{
			$("#unitAmount").hide();
			$("#unUnitAmount").show();
		}
		if(data.borrowModel.pwd){
			$("#pwd").show();
		}else{
			$("#pwd").hide();
		}
		var userMoney = parseFloat(parseFloat(data.borrowModel.account).toFixed(2) - parseFloat(data.borrowModel.accountYes).toFixed(2)).toFixed(2);
		$("#amount_yes").html("&yen;"+format_(userMoney));
		getVate(data.borrowModel.apr,data);
		
		
	}
	if(null!=data.experienceModel){
		$("#startMoney").html("&yen;"+format_(data.experienceModel.lowestAccount));
		$("#timeLimit").html(getTimeLimit(data.experienceModel.timeLimit));
		$("#interestWay").html("T+"+data.experienceModel.interestWay);
		$("#rate").html(getRate(data.lowestRefundRate,data.highestRefundRate,data.experienceModel.interestRateValue));
		getVatePPfund(data,0);
	}
	isEndinvest(data);
	var flag1 = false;
	var flag2 = false;
	if(matList.length>0){		
		for(var i = 0;i<matList.length;i++){
			//产品详情
			if("PC102"==matList[i].materialType){
				if(matList[i].material){					
					$("#detail-menus1").html("<p>"+matList[i].material+"</p>");
					$("#detail-menus1 p span").css("white-space","normal");
					$("#detail-menus1 p img").css("width","100%");
					flag1 = true;
				}
			}
			//产品资料
			if("PC103"==matList[i].materialType){
				if(matList[i].material){					
					$("#detail-menus2").html("<p>"+matList[i].material+"</p>");
					$("#detail-menus2 p span").css("white-space","normal");
					$("#detail-menus2 p img").css("width","100%");
					flag2 = true;
				}
			}
			
			if(!flag1){
				$("#detail-menus1").html("<p>暂无数据</p>");
			}
			
			if(!flag2){
				$("#detail-menus2").html("<p>暂无数据</p>");
			}
		}
	}else{
		$("#detail-menus1").html("<p>暂无数据</p>");
		$("#detail-menus2").html("<p>暂无数据</p>");
	}	
}

/**
 * 投资是否结束
 * @param data
 */
function isEndinvest(data){

	if(data.experienceModel){
		if((parseFloat(data.experienceModel.account)!=0)&&(parseFloat(data.experienceModel.scales)>=100)){
			$("#money").attr("disabled",true);
			$("#payPwd").attr("disabled",true);
			$("#inputpwd").attr("disabled",true);
			$("#num").attr("disabled",true);
			$("#add").attr("onclick","");
			$("#sub").attr("onclick","");
			$("#invest_btn").html("抢光了");
			$("#invest_btn").css("background-color","#d3d3d3");
			$("#invest_btn").attr("onclick","");
		}
		
	}
	if(data.ppfundModel){
		if((parseFloat(data.ppfundModel.account)!=0)&&(parseFloat(data.ppfundModel.scales)>=100)){
			$("#money").attr("disabled",true);
			$("#payPwd").attr("disabled",true);
			$("#inputpwd").attr("disabled",true);
			$("#num").attr("disabled",true);
			$("#add").attr("onclick","");
			$("#sub").attr("onclick","");
			$("#invest_btn").html("抢光了");
			$("#invest_btn").css("background-color","#d3d3d3");
			$("#invest_btn").attr("onclick","");
		}
	}
	if(data.borrowModel){
		if(parseFloat(data.borrowModel.scales)>=100){
			$("#money").attr("disabled",true);
			$("#payPwd").attr("disabled",true);
			$("#inputpwd").attr("disabled",true);
			$("#num").attr("disabled",true);
			$("#add").attr("onclick","");
			$("#sub").attr("onclick","");
			$("#invest_btn").html("抢光了");
			$("#invest_btn").css("background-color","#d3d3d3");
			$("#invest_btn").attr("onclick","");
		}
	}
	
}
	
/**
 * 计算倒计时
 * @param data1
 */
function Countdown(data1){
	if(data1.borrowModel){
		var expirationTime = 0;
		var fixedTimes = 0;
		if(data1.borrowModel.expirationTime){
			expirationTime = data1.borrowModel.expirationTime;
		}
		if(data1.borrowModel.fixedTime){
			fixedTimes = data1.borrowModel.fixedTime;
		}
		var nowTime = parseInt(systemDate);
		var appointTime = parseInt(fixedTimes); 
		var endTime = parseInt(expirationTime);
		var scale = parseFloat(data1.borrowModel.scales);
		if(scale>=100){
			$("#invest_btn").html("抢光了");
			$("#invest_btn").css("background-color","#d3d3d3");
			$("#invest_btn").attr("onclick","");
			$("#expirationTime").html("倒计时已经结束");
			$("#money").attr("disabled",true);
			$("#payPwd").attr("disabled",true);
			$("#inputpwd").attr("disabled",true);
			$("#num").attr("disabled",true);
			$("#add").attr("onclick","");
			$("#sub").attr("onclick","");
		}else{
			if(nowTime>appointTime){
				if(nowTime>endTime){
					$("#expirationTime").html("倒计时已经结束");
					$("#invest_btn").attr("onclick","");
					$("#amount_yes").html("&yen;"+0);
					$("#invest_btn").html("结束了");
					$("#invest_btn").css("background-color","#d3d3d3");
					$("#progress").css("width",100+"%");
					$("#progressComplete").html(100+"% Complete");
					$("#scale").html(100+"%");
					
					$("#money").attr("disabled",true);
					$("#payPwd").attr("disabled",true);
					$("#inputpwd").attr("disabled",true);
					$("#num").attr("disabled",true);
					$("#add").attr("onclick","");
					$("#sub").attr("onclick","");
				}else{
					
					$("#money").attr("disabled",false);
					$("#payPwd").attr("disabled",false);
					$("#inputpwd").attr("disabled",false);
					$("#num").attr("disabled",false);
					$("#add").attr("onclick","add()");
					$("#sub").attr("onclick","sub()");
					
					$("#invest_btn").attr("onclick","addInvest()");
					$("#invest_btn").html("立刻投资");						
					var sys_second =(endTime-nowTime)/1000;
					var showDay,showHour,showMinute,showSecond;
					var timer = setInterval(function(){
						if (sys_second > 1) {
							sys_second -= 1;
							var day = Math.floor((sys_second / 3600) / 24);
							var hour = Math.floor((sys_second / 3600) % 24);
							var minute = Math.floor((sys_second / 60) % 60);
							var second = Math.floor(sys_second % 60);
							showDay = day;
							showHour = hour<10?"0"+hour:hour;//计算小时
							showMinute = minute<10?"0"+minute:minute;//计算分钟
							showSecond = second<10?"0"+second:second;//计算秒杀
							$("#expirationTime").html(showDay+"天"+showHour+"时"+showMinute+"分"+showSecond+"秒");
						} else {
							location.reload();
						}
					}, 1000);
					
					
				}
			}else{
				$("#invest_btn").attr("onclick","");
				$("#invest_btn").html("即将上线");
				$("#money").attr("disabled",true);
				$("#payPwd").attr("disabled",true);
				$("#inputpwd").attr("disabled",true);
				$("#num").attr("disabled",true);
				$("#add").attr("onclick","");
				$("#sub").attr("onclick","");
				var sys_second =(appointTime-nowTime)/1000;
				var showDay,showHour,showMinute,showSecond;
				var timer = setInterval(function(){
					if (sys_second > 1) {
						sys_second -= 1;
						var day = Math.floor((sys_second / 3600) / 24);
						var hour = Math.floor((sys_second / 3600) % 24);
						var minute = Math.floor((sys_second / 60) % 60);
						var second = Math.floor(sys_second % 60);
						showDay = day;
						showHour = hour<10?"0"+hour:hour;//计算小时
						showMinute = minute<10?"0"+minute:minute;//计算分钟
						showSecond = second<10?"0"+second:second;//计算秒杀
						$("#remainingTime").html("倒计时间：");
						$("#expirationTime").html(showDay+"天"+showHour+"时"+showMinute+"分"+showSecond+"秒");
						
					} else {
						location.reload();
					}
				}, 1000);
			}
		}
	}
}

/**
 * 加份数
 */
function add()
{
	var investNum = $("#investNumInp").val();
	investNum = parseFloat(investNum);
	var real_money = parseFloat($("#num").val());
	var add = real_money+1;
	var add_money = parseFloat(add*parseFloat(investNum));
	$("#num").val(add);
	$("#investMoney").html("&yen;"+format_(add_money));
	$("#investMoneyInp").val(add_money);
	getVate(data.borrowModel.apr,data);
}

/**
 * 减份数
 */
function sub()
{
	var investNum = $("#investNumInp").val();
	investNum = parseFloat(investNum);
	var real_money = parseFloat($("#num").val());
	var sub = real_money-1;
	var sub_money = parseFloat(sub*investNum);
	if(sub_money<=1)
	{
		$("#num").val(1);
		$("#investMoney").html("&yen;"+format_(parseFloat(investNum)));
		$("#investMoneyInp").val(parseFloat(investNum));
		getVate(data.borrowModel.apr,data);
	}
	else
	{
		$("#num").val(sub);
		$("#investMoney").html("&yen;"+format_(sub_money));
		$("#investMoneyInp").val(sub_money);
		getVate(data.borrowModel.apr,data);
	}
}

/**
 * 真正剩余时间
 * 
 * @param day_time
 * @returns {String}
 */
function getTimeLimit(day_time)
{
	if("0"==day_time)
	{
		return "可随时赎回";
	}
	else
	{
		
		return day_time+"天";
	}
}

/**
 * 预期收益率
 * @param low
 * @param high
 * @returns {String}
 */
function getRate(low,high,interestRateValue)
{	
	var rate = "";

	if(interestRateValue==0||interestRateValue==undefined||interestRateValue==null){
		rate=""
	}else{
		rate+="+<span class='product_rate'>"+interestRateValue+"</span>%"
	}
	
	
	if(low===high&&"-1"==high)
	{
		return "<span class='product_rate'>0</span>.00%"+rate;
	}
	if(low===high&&"0"==high)
	{
		return "<span class='product_rate'>浮动</span>"+rate;
	}
	if(low===high&&"0"!==high)
	{
		
		return "<span class='product_rate'>"+low+"</span>%"+rate;
		
	}
	if(low!==high&&"0"==high)
	{
		return "<span class='product_rate'>"+low+"</span>%+浮动"+rate;
		
	}
	if(low!==high&&"0"!==high)
	{
		return "<span class='product_rate'>"+low+"</span>%-<span class='product_rate'>"+high+"</span>%"+rate;
	}
}


function changePPfundMoney(){
	
	var money = $("#money").val();
	getVatePPfund(data,money);
	
}

/**
 * 现金计算收益
 * @param data
 * @param money
 */
function getVatePPfund(data,money){
	$("#expected_return1").hide();
	if(data.experienceModel){
		if("0"==data.experienceModel.timeLimit){
			$("#expected_return1").hide();
		}else{
			$("#expected_return1").show();
			
			var interestRateValue = data.experienceModel.interestRateValue;
			if(interestRateValue==0||interestRateValue==undefined||interestRateValue==null){
				interestRateValue=0;
			}else{
				interestRateValue = data.experienceModel.interestRateValue;
			}
			var rate_experienceModel = parseFloat(money*(data.experienceModel.apr+interestRateValue)/100/365*data.experienceModel.timeLimit).toFixed(2);
			$("#expected_return_label1").html("&yen;"+rate_experienceModel);
		}
	}
	if(data.ppfundModel){
		if("0"==data.ppfundModel.timeLimit){
			$("#expected_return1").hide();
		}else{
			$("#expected_return1").show();
			var interestRateValue = data.ppfundModel.interestRateValue;
			if(interestRateValue==0||interestRateValue==undefined||interestRateValue==null){
				interestRateValue=0;
			}else{
				interestRateValue = data.ppfundModel.interestRateValue;
			}
			var rate_ppfundModel = parseFloat(money*(data.ppfundModel.apr+interestRateValue)/100/365*data.ppfundModel.timeLimit).toFixed(2);
			$("#expected_return_label1").html("&yen;"+rate_ppfundModel);
		}
	}
	
}

/**
 * 非现金计算收益
 * @param apr
 * @param data
 */
function getVate(apr,data)
{	
	var money = 0;
	
	if(data.borrowModel){		
		if(data.borrowModel.unitAmount>0){
			var investMoneyInp = $("#investMoneyInp").val();
			if(null!=investMoneyInp&&""!=investMoneyInp)
			{
				money = parseFloat(investMoneyInp);
			}
		}else{
			var inputMoney = $("#money").val();
			if(null!=inputMoney&&""!=inputMoney)
			{
				money = parseFloat(inputMoney);
			}
		}
	}else{
		var inputMoney = $("#money").val();
		if(null!=inputMoney&&""!=inputMoney)
		{
			money = parseFloat(inputMoney);
		}
	}

	var rate  = 0;	
	var interestRateValue = data.borrowModel.interestRateValue;
	if(interestRateValue==0||interestRateValue==undefined||interestRateValue==null){
		interestRateValue=0;
	}else{
		interestRateValue = data.borrowModel.interestRateValue;
	}
	
	$("#expected_return").show();
	if("0"!==data.borrowModel.timeLimit){
		if("0"==data.borrowModel.borrowTimeType){
			//月
			
			rate = parseFloat(money*(apr+interestRateValue)/100/365*data.borrowModel.timeLimit*30).toFixed(2);
			
			$("#expected_return_label").html("&yen;"+rate);
		}
		
		if("1"==data.borrowModel.borrowTimeType){
			//天
			rate = parseFloat(money*(apr+interestRateValue)/100/365*data.borrowModel.timeLimit).toFixed(2);
			$("#expected_return_label").html("&yen;"+rate);
		}
		
	}else{
		
		$("#expected_return").hide();
	}
	
}

