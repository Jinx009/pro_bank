var menue_data,list_data;
$(function()
{
	var productType = $("#productType").val();
	
	var storage = window.localStorage;
    
	if("1"==productType)
	{
		showBtn('btn_one','menue_data');
		$("#menue_data").show();
		$("#list_data").hide();
	}
	
	if("2"==productType)
	{
		showBtn('btn_two','list_data');
		$("#list_data").show();
		$("#menue_data").hide();
	}
	
	if("btn_one"===localStorage.type)
	{
		showBtn('btn_one','menue_data');
	}
	if("btn_two"===localStorage.type)
	{
		showBtn('btn_two','list_data');
	}
	
	/**
	 * 热门产品
	 */
	$.ajax({
		url:"/product/showPopularProductList.html",
		type:"GET",
		ansyc:false,
		dataType:"json",
		success:function(res)
		{
			
			menue_data = res.data;
			getMenueData();
		}
	})
	
	/**
	 * 获取全部产品
	 */
	$.ajax({
		url:"/product/showProductTypeFlagList.html",
		type:"GET",
		ansyc:false,
		dataType:"json",
		success:function(res)
		{
			list_data = res.data;
			getListData();
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
 * 获取热门产品数据
 */
function getMenueData(){
	
	var htmlStr = "";
	for(var i =0;i<menue_data.length;i++){
		
		htmlStr+="<div class=productAllLists>" +
				 "<div class=product_top><span class=product_type>["+menue_data[i].typeDesc+"]</span><span class=product_title>"+menue_data[i].productName+"</span>" +
				 "<dl class=hot_right>"+getLowestAccount(menue_data[i])+"</dl></div>" +
				 "<div class=product_bottom>" +
				 "<dl><dt>预期年化收益率</dt>"+getScales(menue_data[i])+"</dl></div><p class=appoint id=timeInp_"+menue_data[i].id+"></p></div>";
		
	}
	$("#menue_data").html(htmlStr);

}

/**
 * 控制显示起投金额
 * @param data
 */
function getLowestAccount(data){
	if(data.experienceModel){
		return "";
	}
	if(data.ppfundModel){
		return "<dt class=hot_userMoney_title>起投金额</dt><dd class=hot_userMoney>￥"+format_(data.ppfundModel.lowestAccount)+"元</dd>";
	}
	if(data.borrowModel){
		return "<dt class=hot_userMoney_title>起投金额</dt><dd class=hot_userMoney>￥"+format_(data.borrowModel.lowestAccount)+"元</dd>";
	}
}
/**
 * 投资进度和倒计时
 * @param data
 * @returns {String}
 */
function getScales(data){
	
	if(data.experienceModel){
		if(parseFloat(data.experienceModel.scales)>=100){
			return "<dd style='color:#97a7b5;font-weight:bold;'>"+getRate(data.lowestRefundRate,data.highestRefundRate)+getRateCoupon(data.experienceModel.interestRateValue)+"</dd></dl><dl>" +
					"<dt>投资期限</dt><dd class='second_dd' style='color:#97a7b5;font-weight:bold;'>"+timeLimit(data)+"</dd></dl><dl>" +
					"<div class=SoldBtn onclick=productDetail('"+data.id+"') >抢光了</div>";		
		}else{
			return "<dd >"+getRate(data.lowestRefundRate,data.highestRefundRate)+"<span style='color:#ff5858;'>"+getRateCoupon(data.experienceModel.interestRateValue)+"</span></dd></dl><dl>" +
					"<dt>投资期限</dt><dd class='second_dd' id=timeLimit_"+data.id+">"+timeLimit(data)+"</dd></dl><dl>" +
					"<div class=ListBtn onclick=productDetail('"+data.id+"') >立刻投资</div>";
		}
	}
	
	if(data.ppfundModel){
		if(parseFloat(data.ppfundModel.scales)>=100){
			return "<dd style='color:#97a7b5;font-weight:bold;'>"+getRate(data.lowestRefundRate,data.highestRefundRate)+getRateCoupon(data.ppfundModel.interestRateValue)+"</dd></dl><dl>" +
					"<dt>投资期限</dt><dd class='second_dd' style='color:#97a7b5;font-weight:bold;'>"+timeLimit(data)+"</dd></dl><dl>" +
					"<div class=SoldBtn onclick=productDetail('"+data.id+"') >抢光了</div>";		
		}else{
			return "<dd>"+getRate(data.lowestRefundRate,data.highestRefundRate)+"<span style='color:#ff5858;'>"+getRateCoupon(data.ppfundModel.interestRateValue)+"</span></dd></dl><dl>" +
					"<dt>投资期限</dt><dd class='second_dd' >"+timeLimit(data)+"</dd></dl><dl>" +
					"<div class=ListBtn onclick=productDetail('"+data.id+"') >立刻投资</div>";
		}
	}
	
	if(data.borrowModel){
		var fixedTimes=0;
		var expirationTime=0;
		
		if(data.borrowModel.fixedTime){
			fixedTimes = data.borrowModel.fixedTime;	
		}
		if(data.borrowModel.expirationTime){
			expirationTime = data.borrowModel.expirationTime;
		}
		var nowTime = parseInt((new Date()).valueOf());
		var appointTime = parseInt(fixedTimes);
		var endTime = parseInt(expirationTime);
		var id = data.id;

		if(parseFloat(data.borrowModel.scales)>=100){
			$("#timeInp_"+id).html("");
			return "<dd style='color:#97a7b5;font-weight:bold;'>"+getRate(data.lowestRefundRate,data.highestRefundRate)+getRateCoupon(data.borrowModel.interestRateValue)+"</dd></dl><dl>" +
					"<dt>投资期限</dt><dd class='second_dd' style='color:#97a7b5;font-weight:bold;'>"+timeLimit(data)+"</dd></dl><dl>" +
					"<div class=SoldBtn onclick=productDetail('"+data.id+"') >抢光了</div>";
		}else{
			if(nowTime>appointTime){
				if(nowTime>endTime){
					$("#timeInp_"+id).html("");
					return "<dd style='color:#97a7b5;font-weight:bold;'>"+getRate(data.lowestRefundRate,data.highestRefundRate)+getRateCoupon(data.borrowModel.interestRateValue)+"</dd></dl><dl>" +
							"<dt>投资期限</dt><dd class='second_dd' style='color:#97a7b5;font-weight:bold;'>"+timeLimit(data)+"</dd></dl><dl>" +
							"<div class=SoldBtn onclick=productDetail('"+data.id+"') >结束了</div>";
				}else{
					
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
							$("#timeInp_"+id).css("padding-top","4%");
							$("#timeInp_"+id).html("剩余时间："+showDay+"天"+showHour+"时"+showMinute+"分"+showSecond+"秒");
						} else {
							location.reload();
						}
					}, 1000);
					return "<dd>"+getRate(data.lowestRefundRate,data.highestRefundRate)+"<span style='color:#ff5858;'>"+getRateCoupon(data.borrowModel.interestRateValue)+"</span></dd></dl><dl>" +
						   "<dt>投资期限</dt><dd class='second_dd' >"+timeLimit(data)+"</dd></dl><dl>" +
						   "<div class=ListBtn onclick=productDetail('"+data.id+"') >立刻投资</div>";
					
				}
				
			}else{							
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
						$("#timeInp_"+id).css("padding-top","4%");
						$("#timeInp_"+id).html("倒计时间 ："+showDay+"天"+showHour+"时"+showMinute+"分"+showSecond+"秒");
					} else {
						location.reload();
					}
				}, 1000);
				return "<dd>"+getRate(data.lowestRefundRate,data.highestRefundRate)+"<span style='color:#ff5858;'>"+getRateCoupon(data.borrowModel.interestRateValue)+"</span></dd></dl><dl>" +
					   "<dt>投资期限</dt><dd class='second_dd' >"+timeLimit(data)+"</dd></dl><dl>" +
				       "<div class=ListBtn onclick=productDetail('"+data.id+"') >即将上线</div>";
			}
		}
	}
}


/**
 * 投资期限
 */
function timeLimit(data){
	if(data.experienceModel){		
		if("0"!=data.experienceModel.timeLimit){
			
			return "<span style='font-size:1.1em;'>"+data.experienceModel.timeLimit+"</span><span class='day'>天</span>";
			
		}else{
			
			return "<span style='font-size:1.1em;'>活期</span>";
		}
	}
	if(data.ppfundModel){
		if("0"!=data.ppfundModel.timeLimit){
			
			return "<span style='font-size:1.1em;'>"+data.ppfundModel.timeLimit+"</span><span class='day'>天</span>";
			
		}else{
			
			return "<span style='font-size:1.1em;'>活期</span>";
		}
	}
	if(data.borrowModel){
		if("0"!==data.borrowModel.timeLimit){
			
			if("0"==data.borrowModel.borrowTimeType){
				
				return "<span style='font-size:1.1em;'>"+data.borrowModel.timeLimit+"</span><span class='day'>月</span>";
			}
			if("1"==data.borrowModel.borrowTimeType){
				
				return "<span style='font-size:1.1em;'>"+data.borrowModel.timeLimit+"</span><span class='day'>天</span>";
			}
			
		}else{
			
			return "<span style='font-size:1.1em;'>活期</span>";
			
		}
	}
}

function getListData(){
	var htmlStr = "";
	for(var i = 0;i<list_data.length;i++){
		var picUrl = list_data[i].picUrl;
		if(picUrl==""||picUrl==null){
			picUrl = "/themes/theme_default/nb/wechat/img/productMenue_bg.png";
		}
		if(list_data[i].id==5){
			htmlStr+="<a href='/nb/wechat/product/appointment_product_list.html?id="+list_data[i].id+"'><div class=productMenue>" +
			"<img src="+picUrl+">" +
			"<div class=productMenueContent>" +
			"<p class=productMenue_title>"+list_data[i].flagName+"</p>" +
			"<p class=productMenue_desc>"+list_data[i].flagDescription+"</p>" +
			"<img src=/themes/theme_default/nb/wechat/img/productMenue_right.png>" +
			"</div>" +
			"</div></a>";
		}else{			
			htmlStr+="<a href='/nb/wechat/product/product_list.html?id="+list_data[i].id+"'><div class=productMenue>" +
			"<img src="+picUrl+">" +
			"<div class=productMenueContent>" +
			"<p class=productMenue_title>"+list_data[i].flagName+"</p>" +
			"<p class=productMenue_desc>"+list_data[i].flagDescription+"</p>" +
			"<img src=/themes/theme_default/nb/wechat/img/productMenue_right.png>" +
			"</div>" +
			"</div></a>";
		}
	}
	
	$("#list_data").html(htmlStr);
}


/**
 * 组合列表获取收益
 */
function getMenueRate(low,high)
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
		return "<span class=smallYield>0</span>.00%";
	}
	if(low===high&&"0"==high)
	{
		return "<span class=smallYield>浮动</span>";
	}
	if(low===high&&"0"!==high)
	{
		return "<span class=bigYield>"+lowArr[0]+"</span>.<span class=smallYield>"+lowArr[1]+"%</span>";
	}
	if(low!==high&&"0"==high)
	{
		return "<span class=bigYield>"+lowArr[0]+"</span>.<span class=smallYield>"+lowArr[1]+"%+浮动</span>";
		
	}
	if(low!==high&&"0"!==high)
	{
		return "<span class=bigYield>"+lowArr[0]+"</span>."+lowArr[1]+"<span class=smallYield>%</span>-<span class=bigYield>"+highArr[0]+"</span>."+highArr[1]+"<span class=smallYield>%</span>";
	}
}




/**
 * 收益率计算
 * @param low 最低
 * @param high 最高
 * @returns {String}
 */
function getRate(low,high)
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
		return "<span class=bigYield>0</span>.00%";
	}
	if(low===high&&"0"==high)
	{
		return "<span class=bigYield>浮动</span>";
	}
	if(low===high&&"0"!==high)
	{
		return "<span class=bigYield>"+lowArr[0]+".</span>"+lowArr[1]+"%";
	}
	if(low!==high&&"0"==high)
	{
		return "<span class=bigYield>"+lowArr[0]+".</span>"+lowArr[1]+"%+<span class=bigYield>浮动</span>";
		
	}
	if(low!==high&&"0"!==high)
	{
		return "<span class=bigYield>"+lowArr[0]+".</span>"+lowArr[1]+"%-<span class=bigYield>"+highArr[0]+".</span>"+highArr[1]+"%";
	}
}

/**
 * 判断是否要加息
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
		return "+<span class=bigYield>"+rateArr[0]+".</span>"+rateArr[1]+"%";
	}
}


/**
 * 普通产品详情页
 * 
 * @param product_id
 */
function productDetail(product_id)
{
	location.href = "/nb/wechat/product/productDetail.action?product_id="+product_id;
}
/**
 * 组合产品详情页
 * 
 * @param product_id
 */
function mapProductDetail(product_id)
{
	location.href = "/nb/wechat/product/mapProductDetail.action?product_id="+product_id;
}



function showBtn(btn_id,data_id)
{
	$("#btn_one").removeClass("productMenueBtnList_actived");
	$("#btn_two").removeClass("productMenueBtnList_actived");
	
	$("#"+btn_id).addClass("productMenueBtnList_actived");
	
	localStorage.type = btn_id;
	
	$("#menue_data").hide();
	$("#list_data").hide();
	
	$("#"+data_id).show();
}
