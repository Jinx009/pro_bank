var data;
var matList;
$(function()
{
	var product_id = $("#product_id").val();
	
	$.ajax({
		url:"/product/showProductDetail.html?prodId="+product_id,
		type:"GET",
		ansyc:false,
		dataType:"json",
		success:function(res)
		{
			data = res.data;
			matList = res.matList;
			
			changeHtml();
			Countdown(data)
		}
	})
})

/**
 * 金额格式化（保留小数点后两位）
 * @param money
 * @returns
 */
function format_(money){
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
		for (i = 0; i < l.length; i++) { 
		t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : ""); 
		} 
		return  t.split("").reverse().join("")+ "." + r;
	}
}

/**
 * 投资
 * @param product_id
 */
function invest(product_id)
{
	location.href = "/nb/wechat/account/pay.action?productBasicId="+product_id;
}
/**
 * 拼接html
 */
function changeHtml()
{
	$("#productType").html("["+data.typeDesc+"]");
	$("#productType2").html("["+data.typeDesc+"]");
	$("#productType3").html("["+data.typeDesc+"]");
	
	changeShow("WC206");
	changeShow("WC204");
	changeShow("WC208");
	changeShow("WCP206");
	changeShow("WCP208");
	changeShow("WCE206");
	changeShow("WCE208");
	
	$("#lightSpot").html("暂无介绍");
	$("#tipsTitle").html("");
	$("#tips").html("");
	$("#tipsTitle1").html("");
	$("#tips1").html("");
	
	var flag1 = false;
	var flag2 = false;
	var flag3 = false;
	
	if(matList.length>0)
	{
		for(var i = 0;i<matList.length;i++)
		{

			if("WC206"===matList[i].materialType)
			{
				
				Show("WC206","WC206");
				Show("WCP206","WC206");
				Show("WCE206","WC206");
				$("#WC206").html(matList[i].material);
				$("#WCP206").html(matList[i].material);
				$("#WCE206").html(matList[i].material);
				flag1 = true;
			}
			if("WC204"===matList[i].materialType)
			{
				Show("WC204","WC204");
				$("#WC204").html(matList[i].material);
				flag2 = true;
			}
			if("WC208"===matList[i].materialType)
			{
				Show("WC208","WC208");
				Show("WCP208","WC208");
				Show("WCE208","WC208");
				$("#WC208").html(matList[i].material);
				$("#WCP208").html(matList[i].material);
				$("#WCE208").html(matList[i].material);
				flag3 = true;
			}
			if("WC205"===matList[i].materialType)
			{
				if(flag2){
					
					if(null!==matList[i].material&&""!==matList[i].material)
					{
						
						$("#WC205").html(matList[i].material);
					}
					else
					{
						changeShow("WC204");
						
					}
					
				}else{
					
					Show("WC204","WC205");
					if(null!==matList[i].material&&""!==matList[i].material)
					{
						
						$("#WC205").html(matList[i].material);
					}
					else
					{
						changeShow("WC204");
						
					}
				}
			}
			if("WC209"===matList[i].materialType)
			{				
				if(flag3){
					
					if(null!==matList[i].material&&""!==matList[i].material)
					{
						$("#WC209").html(matList[i].material);
					}
					else
					{
						changeShow("WC208");
						changeShow("WCP208");
						changeShow("WCE208");
					}
					
				}else{
					Show("WC208","WC209");
					Show("WCP208","WC209");
					Show("WCE208","WC209");
					if(null!==matList[i].material&&""!==matList[i].material)
					{
						$("#WC209").html(matList[i].material);
					}
					else
					{
						changeShow("WC208");
						changeShow("WCP208");
						changeShow("WCE208");
					}
					
				}
				
			}
			if("WC207"===matList[i].materialType)
			{
				if(flag1){
					
					if(null!==matList[i].material&&""!==matList[i].material)
					{
						$("#WC207").html(matList[i].material);
					}
					else
					{
						changeShow("WC206");
						changeShow("WCP206");
						changeShow("WCE206")
					}
					
				}else{
					
					Show("WC206","WC207");
					Show("WCP206","WC207");
					Show("WCE206","WC207");
					if(null!==matList[i].material&&""!==matList[i].material)
					{
						$("#WC207").html(matList[i].material);
					}
					else
					{
						changeShow("WC206");
						changeShow("WCP206");
						changeShow("WCE206")
					}
					
				}
			}
			if("WC202"===matList[i].materialType)
			{
				if(null!==matList[i].material&&""!==matList[i].material)
				{
					$("#lightSpot").html(matList[i].material);
				}
				else
				{
					$("#lightSpot").html("暂无介绍");
				}
			}
			if("WC203"===matList[i].materialType)
			{
				if(null!==matList[i].material&&""!==matList[i].material)
				{
					$("#tipsTitle").html("投资小贴士");
					 $("#tips").html(matList[i].material);
					 $("#tipsTitle1").html("投资小贴士");
					 $("#tips1").html(matList[i].material);
				}
				else
				{
					$("#tipsTitle1").html("");
					$("#tips1").html("");
				}
			}
		}
	}
	else
	{
		changeShow("WC206");
		changeShow("WC204");
		changeShow("WC208");
		changeShow("WCP206");
		changeShow("WCP208");
		changeShow("WCE206");
		changeShow("WCE208");
		
		$("#lightSpot").html("暂无介绍");
		$("#tipsTitle").html("");
		$("#tips").html("");
		$("#tipsTitle1").html("");
		$("#tips1").html("");
	}
		
	
	if(null!=data.ppfundModel)
	{
		$("#productTitle2").html(data.productName);
		splitTitle("productTitle2");
		$("#totalMoney2").html(format_(data.ppfundModel.accountYes));
		$("#rate2").html(getRate1(data.lowestRefundRate,data.highestRefundRate)+"<span style='color:#ff5858' id='rateSpan2'>"+getRateCoupon(data.ppfundModel.interestRateValue)+"</span>");
		$("#minMoney2").html(format_(data.ppfundModel.lowestAccount));
		
		$("#timeLimit2").html(getTimeLimit(data.ppfundModel.timeLimit));
		
		if((parseFloat(data.ppfundModel.scales))>=100)
		{
			$("#rate2").css({"color":"#97a7b5"});
			$("#startMoney2").css({"color":"#97a7b5"});
			$("#rateSpan2").css({"color":"#97a7b5"});
			$("#investBtn").css({"background":"#97a7b5"});
			$("#investBtn").css({"border":"1px solid #97a7b5"});
			$("#investBtn").html("抢光了");
			$("#investBtn").attr("onclick","");
		}
			
		$("#main2").css("display","block");
	}
	
	if(null!=data.experienceModel)
	{
		$("#productTitle3").html(data.productName);
		splitTitle("productTitle3");
		$("#totalMoney3").html(format_(data.experienceModel.accountYes));
		$("#rate3").html(getRate1(data.lowestRefundRate,data.highestRefundRate)+"<span style='color:#ff5858' id='rateSpan3'>"+getRateCoupon(data.experienceModel.interestRateValue)+"</span>");
		$("#minMoney3").html(format_(data.experienceModel.lowestAccount));
		
		$("#timeLimit3").html(getTimeLimit(data.experienceModel.timeLimit));
		
		if((parseFloat(data.experienceModel.scales))>=100)
		{
			$("#rate3").css({"color":"#97a7b5"});
			$("#rateSpan3").css({"color":"#97a7b5"});
			$("#startMoney3").css({"color":"#97a7b5"});
			
			$("#investBtn").css({"background":"#97a7b5"});
			$("#investBtn").css({"border":"1px solid #97a7b5"});
			$("#investBtn").html("抢光了");
			$("#investBtn").attr("onclick","");
		}
		/*$("#experienceBtn").show();	*/
		$("#main3").css("display","block");
	}
	
	if(null!=data.borrowModel)
	{
		var userMoney = parseFloat(parseFloat(data.borrowModel.account).toFixed(2) - parseFloat(data.borrowModel.accountYes).toFixed(2)).toFixed(2);
	
		$("#productTitle").html(data.productName);
		splitTitle("productTitle");
		$("#totalMoney").html(format_(userMoney));
		$("#rate").html(getRate1(data.lowestRefundRate,data.highestRefundRate)+"<span style='color:#ff5858' id='rateSpan'>"+getRateCoupon(data.borrowModel.interestRateValue)+"</span>");
		$("#timeLimit").html(data.borrowModel.timeLimitStr);
		$("#minAccount").html(format_(data.borrowModel.lowestAccount));
		
		if((parseFloat(data.borrowModel.scales))>=100)
		{
			$("#rate").css({"color":"#97a7b5"});
			$("#rateSpan").css({"color":"#97a7b5"});
			$("#timeLimit").css({"color":"#97a7b5"});
			$("#startMoney").css({"color":"#97a7b5"});
			
			$("#investBtn").css({"background":"#97a7b5"});
			$("#investBtn").css({"border":"1px solid #97a7b5"});
			$("#investBtn").html("抢光了");
			$("#investBtn").attr("onclick","");
		}
		
		$("#main").css("display","block");
		
	}
	
	if(data.ppfundModel){
		getSplit("minAccount2",data.ppfundModel.lowestAccount);
	}
	if(data.experienceModel){
		getSplit("minAccount3",data.experienceModel.lowestAccount);
	}
	if(data.borrowModel){
		getSplit("minAccount",data.borrowModel.lowestAccount);
	}
}

/**
 * 计算倒计时
 * @param data1
 */
function Countdown(data){
	if(data.borrowModel){
		var expirationTime = 0;
		var fixedTimes = 0;
		if(data.borrowModel.expirationTime){
			expirationTime = data.borrowModel.expirationTime;
		}
		if(data.borrowModel.fixedTime){
			fixedTimes = data.borrowModel.fixedTime;
		}
		var nowTime = parseInt((new Date()).valueOf());
		var appointTime = parseInt(fixedTimes); 
		var endTime = parseInt(expirationTime);
		var scale = parseFloat(data.borrowModel.scales);
		if(scale>=100){
			$("#investBtn").removeClass("sureBtn").addClass("sureSoldBtn");
			$("#investBtn").html("抢光了");
			$("#investBtn").attr("onclick","");
			$("#expirationTime").html("倒计时已经结束");
		}else{
			if(nowTime>appointTime){
				if(nowTime>endTime){
					$("#totalMoney").html("0");
					$("#rate").css({"color":"#97a7b5"});
					$("#timeLimit").css({"color":"#97a7b5"});
					$("#startMoney").css({"color":"#97a7b5"});
					$("#rateSpan").css({"color":"#97a7b5"});
					$("#investBtn").removeClass("sureBtn").addClass("sureSoldBtn");
					$("#expirationTime").html("倒计时已经结束");
					$("#investBtn").attr("onclick","");
					$("#investBtn").html("结束了");
					
				}else{
					
					$("#investBtn").attr("onclick","invest("+data.id+")");
					$("#investBtn").html("立刻投资");						
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
							$("#expirationTime").html("剩余时间："+showDay+"天"+showHour+"时"+showMinute+"分"+showSecond+"秒");
						} else {
							location.reload();
						}
					}, 1000);
					
					
				}
			}else{
				$("#investBtn").attr("onclick","");
				$("#investBtn").html("即将上线");
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
						$("#expirationTime").html("倒计时间："+showDay+"天"+showHour+"时"+showMinute+"分"+showSecond+"秒");
						
					} else {
						location.reload();
					}
				}, 1000);
			}
		}
	}
}

function splitTitle(id){
	var str = $("#"+id).html();
	var strArray = new Array();
	strArray = str;
	if(strArray.length>=16){
		$("#"+id).html(str.substring(8,str)+"...");
	}

}
function changeShow(id)
{

	$("#"+id).html("暂无更多信息");
	
	$("#"+id).attr("onclick","");
	
	$("#"+id).addClass("productHighlights_desc_zeroWallet");
}

function Show(id,type)
{

	if("WC206"==id){
		$("#WC206").html("募集成立当天开始计算收益");
		type="WC207";
	}
	
	if("WC204"==id){
		$("#WC204").html("你想了解的都在这里");
		type="WC205";
	}
	
	if("WC208"==id){
		$("#WC208").html("全程保障本金收益安全");
		type="WC209";
	}
	
	if("WCP206"==id){
		$("#WCP206").html("投资成功即可开始以计算收益");
		type="WC207";
	}
	
	if("WCP208"==id){
		$("#WCP208").html("全程保障本金收益安全");
		type="WC209";
	}
	
	if("WCE206"==id){
		$("#WCP206").html("投资成功即可开始以计算收益");
		type="WC207";
	}
	
	if("WCE208"==id){
		$("#WCP208").html("全程保障本金收益安全");
		type="WC209";
	}

	var product_id = $("#product_id").val();
	
	$("#"+id).attr("onclick","gotoChildDetail('"+type+"',"+product_id+")");
	
	$("#"+id).removeClass("productHighlights_desc_zeroWallet");
}



/**
 * 获取总金额
 */
function getAccount(account)
{
	var real_account = parseFloat(parseFloat(account)/10000).toFixed(2);
	
	var array = real_account = real_account.split(".");
	
	if(null!==array[1]&&""!==array[1])
	{
		if(0===parseInt(array[1]))
		{
			
			return array[0];
		}
		else
		{
			return  parseFloat(parseFloat(account)/10000).toFixed(2);
		}
	}
	
	return  parseFloat(parseFloat(account)/10000).toFixed(2);
}

function getRate1(low,high)
{
	var lowArr = new Array();
	var highArr = new Array();
	var lowStr = parseFloat(low).toFixed(2).toString();
	var highStr = parseFloat(high).toFixed(2).toString();
	
	
	lowArr[0] = lowStr.split(".")[0]; 
	highArr[0] = highStr.split(".")[0];
	
	lowArr[1] = lowStr.split(".")[1]; 
	highArr[1] = highStr.split(".")[1];
	if(low===high&&"-1"==high)
	{
		return "<p>0</p>.00%";
	}
	if(low===high&&"0"==high)
	{
		return "<p>浮动</p>";
	}
	if(low===high&&"0"!==high)
	{
		return "<p>"+lowArr[0]+"</p>."+lowArr[1]+"<span>%</span>";
	}
	if(low!==high&&"0"==high)
	{
		return "<p>"+lowArr[0]+"</p>."+lowArr[1]+"<span>%+浮动</span>";
		
	}
	if(low!==high&&"0"!==high)
	{
		return "<p>"+lowArr[0]+"</p>."+lowArr[1]+"<span>%-</span><p>"+highArr[0]+"</p>."+highArr[1]+"<span>%</span>";
	}
}

/**
 * 判断是否加息
 * @param rate
 */
function getRateCoupon(rate){
	if(rate==0||rate==undefined||rate==null){
		return "";
	}else{
		var rateArr = new Array();
		var rateStr = parseFloat(rate).toFixed(2).toString();
		rateArr[0] = rateStr.split(".")[0];
		rateArr[1] = rateStr.split(".")[1];
		return "+<p>"+rateArr[0]+".</p>"+rateArr[1]+"%";
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
 * 返回
 */
function goBack()
{	
	location.href = "/nb/wechat/product/product_menue.html?productType=2";	
}

function hideDetail()
{
	$("#WC207").hide();
	$("#WC205").hide();
	$("#WC209").hide();
	
	$("#detail").hide();
	$("#main_div").show();
	$(".foot ").show();
}
function showDetail(id)
{
	$("#"+id).show();
	
	$("#detail").show();
	$("#main_div").hide();
	$(".foot ").hide();
}
	   
function gotoChildDetail(childId,prodId)
{
	location.href = "/nb/wechat/productChildDetail.html?product_child_id="+childId+"&product_id="+prodId;
}

function gotoProtocolDetail(prodId)
{
	location.href = "/nb/wechat/account/protocol.html?type=product&id="+prodId;
}


function getSplit(id,num)
{
	var pageMoney = parseFloat(num).toString();
	var array = new Array();
	array=pageMoney; 
	var j = array.length;

	if(j>=5)
	{
		$("#"+id).css("font-size","1em");

	}
	
}