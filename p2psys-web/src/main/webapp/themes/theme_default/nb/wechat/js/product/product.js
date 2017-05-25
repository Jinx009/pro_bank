var list_data,type;
$(function()
{
	type = $("#id").val();
	
	
	/*
	 * 根据分类标签显示产品列表
	 */
	$.ajax({
		url:"/product/showProductListByFlag.html?id="+type,
		type:"GET",
		dataType:"json",
		success:function(res)
		{
			list_data = res.data;
			getListData();

		}
	})	
	
	$.ajax({
		url:"/product/showProductTypeFlagList.html",
		type:"GET",
		dataType:"json",
		success:function(res)
		{
			list_data = res.data;
			for(var i=0;i<list_data.length;i++){
				var id=list_data[i].id;
				if(id==type){
					$("#productTitle").html(list_data[i].flagName);
				}
			}
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
 * 普通标列表呈现
 */
function getListData()
{
	var htmlStr = "";
	
	for(var i = 0;i<list_data.length;i++)
	{

		if(list_data[i].experienceModel)
		{
			htmlStr += "<div class='productList'><p>";
			htmlStr += "<span class='indexType' >["+list_data[i].typeDesc+"]</span>";
			htmlStr += "<span class='indexTitle' >"+list_data[i].productName+"</span></p>";
			
			if((parseFloat(list_data[i].experienceModel.scales))>=100)
			{
				htmlStr += "<dl class='privateList_dl'><dt>预期年化收益率</dt><dd  style='color:#97a7b5;font-weight: bold;'  >"+getRate1(list_data[i].lowestRefundRate,list_data[i].highestRefundRate)+getRateCoupon(list_data[i].experienceModel.interestRateValue)+"</dd></dl>";
				
				if("0"!=list_data[i].experienceModel.timeLimit)
				{
					htmlStr += "<dl class='privateList_dl1'><dt>投资期限</dt><dd><p  style='color:#97a7b5;'  >"+list_data[i].experienceModel.timeLimit+"</p><span style='color:#97a7b5;' >天</span></dd></dl>";
				}
				else
				{
					htmlStr += "<dl class='privateList_dl1'><dt>投资期限</dt><dd><p  style='color:#97a7b5;'  >活期</p></dd></dl>";
				}
				htmlStr += "<dl class='privateList_dl1'><dt>起投金额</dt><dd><p style='color:#97a7b5;' id="+list_data[i].id+">￥"+format_(list_data[i].experienceModel.lowestAccount)+"<p><span style='color:#97a7b5;' >元</span></dd></dl>";
				/*htmlStr += "<span class='productEndTime'  >投资时间:全天</span>";	*/
				
				htmlStr += "<div class='productListBtn' style='background:#97a7b5;border:1px solid #97a7b5;' onclick=productDetail('"+list_data[i].id+"') >抢光了</div>";
				
			}
			else
			{
				htmlStr += "<dl class='privateList_dl'><dt>预期年化收益率</dt><dd style='font-weight: bold;'>"+getRate1(list_data[i].lowestRefundRate,list_data[i].highestRefundRate)+"<span style=color:#ff5858>"+getRateCoupon(list_data[i].experienceModel.interestRateValue)+"</span></dd></dl>";
				
				if("0"!=list_data[i].experienceModel.timeLimit)
				{
					htmlStr += "<dl class='privateList_dl1'><dt>投资期限</dt><dd><p>"+list_data[i].experienceModel.timeLimit+"</p><span>天</span></dd></dl>";
				}
				else
				{
					htmlStr += "<dl class='privateList_dl1'><dt>投资期限</dt><dd><p>活期</p></dd></dl>";
				}
				htmlStr += "<dl class='privateList_dl1'><dt>起投金额</dt><dd><p id="+list_data[i].id+">￥"+format_(list_data[i].experienceModel.lowestAccount)+"<p><span>元</span></dd></dl>";
					
				/*htmlStr += "<span class='productEndTime'  >投资时间:全天</span>";	*/
				
				htmlStr += "<div class='productListBtn' onclick=productDetail('"+list_data[i].id+"') >立刻投资</div>";
			}
			
			
		}
		
		if(list_data[i].ppfundModel)
		{
			htmlStr += "<div class='productList'><p>";
			htmlStr += "<span class='indexType' >["+list_data[i].typeDesc+"]</span>";
			htmlStr += "<span class='indexTitle' >"+list_data[i].productName+"</span></p>";
			
			if((parseFloat(list_data[i].ppfundModel.scales))>=100)
			{
				htmlStr += "<dl class='privateList_dl'><dt>预期年化收益率</dt><dd  style='color:#97a7b5;font-weight: bold;'  >"+getRate1(list_data[i].lowestRefundRate,list_data[i].highestRefundRate)+getRateCoupon(list_data[i].ppfundModel.interestRateValue)+"</dd></dl>";
				
				if("0"!=list_data[i].ppfundModel.timeLimit)
				{
					htmlStr += "<dl class='privateList_dl1'><dt>投资期限</dt><dd><p  style='color:#97a7b5;'  >"+list_data[i].ppfundModel.timeLimit+"</p><span style='color:#97a7b5;' >天</span></dd></dl>";
				}
				else
				{
					htmlStr += "<dl class='privateList_dl1'><dt>投资期限</dt><dd><p  style='color:#97a7b5;'  >活期</p></dd></dl>";
				}
				htmlStr += "<dl class='privateList_dl1'><dt>起投金额</dt><dd><p style='color:#97a7b5;' id="+list_data[i].id+">￥"+format_(list_data[i].ppfundModel.lowestAccount)+"<p><span style='color:#97a7b5;' >元</span></dd></dl>";
					
				/*htmlStr += "<span class='productEndTime'  >投资时间:全天</span>";	*/
				
				htmlStr += "<div class='productListBtn' style='background:#97a7b5;border:1px solid #97a7b5;' onclick=productDetail('"+list_data[i].id+"') >抢光了</div>";
				
			}
			else
			{
				htmlStr += "<dl class='privateList_dl'><dt>预期年化收益率</dt><dd style='font-weight: bold;'>"+getRate1(list_data[i].lowestRefundRate,list_data[i].highestRefundRate)+"<span style=color:#ff5858>"+getRateCoupon(list_data[i].ppfundModel.interestRateValue)+"</span></dd></dl>";
				
				if("0"!=list_data[i].ppfundModel.timeLimit)
				{
					htmlStr += "<dl class='privateList_dl1'><dt>投资期限</dt><dd><p>"+list_data[i].ppfundModel.timeLimit+"</p><span>天</span></dd></dl>";
				}
				else
				{
					htmlStr += "<dl class='privateList_dl1'><dt>投资期限</dt><dd><p>活期</p></dd></dl>";
				}
				
				htmlStr += "<dl class='privateList_dl1'><dt>起投金额</dt><dd><p id="+list_data[i].id+">￥"+format_(list_data[i].ppfundModel.lowestAccount)+"<p><span>元</span></dd></dl>";

				htmlStr += "<div class='productListBtn' onclick=productDetail('"+list_data[i].id+"') >立刻投资</div>";
			}
			
			
		}
		if(list_data[i].borrowModel)
		{			
			var userMoney = parseFloat(parseFloat(list_data[i].borrowModel.account).toFixed(2) - parseFloat(list_data[i].borrowModel.accountYes).toFixed(2)).toFixed(2);
			htmlStr += "<div class='productList'><div class='borrow'>";
			htmlStr += "<span class='indexType' >["+list_data[i].typeDesc+"]</span>";
			htmlStr += "<span class='indexTitle' >"+list_data[i].productName+"</span>";
			htmlStr += "<dl class='borrow_right'><dt class='userMoney_title'>可投余额</dt><dd class='userMoney'>￥"+format_(userMoney)+"元</dd></dl></div>"+getScales(list_data[i]);
		}
		if(list_data[i].subProductList)
		{
			var lightspot = list_data[i].lightspot;
			htmlStr += "<div class='productList detailBottom' >";
			htmlStr += "<p><span class='indexType' >["+list_data[i].typeDesc+"]</span>";
			htmlStr += "<span class='indexTitle' >"+list_data[i].productName+"</span>";
			htmlStr += "<span class='indexHasCast' ></span></p>";
			htmlStr += "<span class='indexYieldDesc' >组合预期年化收益率</span><div class='productListBtn' onclick=mapProductDetail("+list_data[i].id+") >查看</div><br />";
			htmlStr += "<span class='indexYield' >"+getRate(list_data[i].lowestRefundRate,list_data[i].highestRefundRate)+"</span>";
			
			if(""==lightspot||null==lightspot){
				htmlStr += "<div class='popTip' style='margin-left:2px;color:rgb(114,113,113)' >暂无介绍。。。</div>";
			}else{
				htmlStr += "<div class='popTip' style='margin-left:2px;color:rgb(114,113,113)' >"+lightspot+"</div>";
			}
			
			for(var j = 0;j<list_data[i].subProductList.length;j++)
			{
				if(j!=list_data[i].subProductList.length-1)
				{
					htmlStr += "<div class='popDetail'  >";
					htmlStr += "<span class='indexType'>["+list_data[i].subProductList[j].typeDesc+"]";
					htmlStr += "</span><span class='indexTitle'>"+list_data[i].subProductList[j].productName+"</span>";
					htmlStr += "<span class='allocation'>配置比例<span class='allocation_yield'>"+list_data[i].subProductList[j].rate+"%</span></span></div>";
				}
				else
				{
					htmlStr += "<div class='popDetail popDetail_other' >";
					htmlStr += "<span class='indexType'>["+list_data[i].subProductList[j].typeDesc+"]";
					htmlStr += "</span><span class='indexTitle'>"+list_data[i].subProductList[j].productName+"</span>";
					htmlStr += "<span class='allocation'>配置比例<span class='allocation_yield'>"+list_data[i].subProductList[j].rate+"%</span></span></div>";
				}
				
			}
			
		}
		
		
		htmlStr += "</div>";
	}
	
	$("#"+type).html(htmlStr);
	
	$(".productList .indexTitle").each(function(){		
 		var str = $(this).html();
 		var strArray = new Array();
 		strArray = str;
 		if(strArray.length>=16){
 			$(this).html(str.substring(8,str)+"...");
 		}

	}); 
	
	for(var i = 0;i<list_data.length;i++){
		if(list_data[i].experienceModel){
			
			getSplit(list_data[i].id,list_data[i].experienceModel.lowestAccount);
		}
		if(list_data[i].ppfundModel){
			
			getSplit(list_data[i].id,list_data[i].ppfundModel.lowestAccount);
		}
		if(list_data[i].borrowModel){
			
			getSplit(list_data[i].id,list_data[i].borrowModel.lowestAccount);
		}
	}
	
}

function getTimeLimit(data){
	if(data.borrowModel){		
		if("0"!==data.borrowModel.timeLimit)
		{
			if("0"==data.borrowModel.borrowTimeType)
			{
				return "<p>"+data.borrowModel.timeLimit+"</p><span>月</span>";
			}
			if("1"==data.borrowModel.borrowTimeType)
			{
				return "<p>"+data.borrowModel.timeLimit+"</p><span>天</span>";
			}
		}
		else
		{
			return "<span>活期</span>";
		}
	}
}

function getScales(data){
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
			return "<dl class='privateList_dl'><dt>预期年化收益率</dt><dd style='color:#97a7b5;font-weight: bold;' >"+getRate1(data.lowestRefundRate,data.highestRefundRate)+"<span>"+getRateCoupon(data.borrowModel.interestRateValue)+"</span></dd></dl>" +
				   "<dl class='privateList_dl1'><dt>投资期限</dt><dd style='color:#97a7b5;'>"+getTimeLimit(data)+"</dd></dl>"+
				   "<dl class='privateList_dl1'><dt>起投金额</dt><dd><p  style='color:#97a7b5;' id="+data.id+">￥"+format_(data.borrowModel.lowestAccount)+"</p><span style='color:#97a7b5;' >元</span></dd></dl>" +
				   "<span class='productEndTime' id=timeInp_"+data.id+"></span>" +
				   "<div class=productListSoldBtn onclick=productDetail('"+data.id+"') >抢光了</div>";		
		}else{
			if(nowTime>appointTime){
				
				if(nowTime>endTime){
					$("#timeInp_"+id).html("");
					return "<dl class='privateList_dl'><dt>预期年化收益率</dt><dd style='color:#97a7b5;font-weight: bold;' >"+getRate1(data.lowestRefundRate,data.highestRefundRate)+"<span>"+getRateCoupon(data.borrowModel.interestRateValue)+"</span></dd></dl>" +
						   "<dl class='privateList_dl1'><dt>投资期限</dt><dd style='color:#97a7b5;'>"+getTimeLimit(data)+"</dd></dl>"+		
						   "<dl class='privateList_dl1'><dt>起投金额</dt><dd><p  style='color:#97a7b5;' id="+data.id+">￥"+format_(data.borrowModel.lowestAccount)+"</p><span style='color:#97a7b5;' >元</span></dd></dl>" +
							"<span class='productEndTime' id=timeInp_"+data.id+"></span>" +
							"<div class=productListSoldBtn onclick=productDetail('"+data.id+"') >结束了</div>";
					
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
							$("#timeInp_"+id).html("剩余时间："+showDay+"天"+showHour+"时"+showMinute+"分"+showSecond+"秒");
						} else {
							location.reload();
						}
					}, 1000);
					return "<dl class='privateList_dl'><dt>预期年化收益率</dt><dd style='font-weight: bold;' >"+getRate1(data.lowestRefundRate,data.highestRefundRate)+"<span style='color:#ff5858'>"+getRateCoupon(data.borrowModel.interestRateValue)+"</span></dd></dl>" +
						   "<dl class='privateList_dl1'><dt>投资期限</dt><dd >"+getTimeLimit(data)+"</dd></dl>"+
						   "<dl class='privateList_dl1'><dt>起投金额</dt><dd><p id="+data.id+">￥"+format_(data.borrowModel.lowestAccount)+"</p><span >元</span></dd></dl>" +
						   "<span class='productEndTime' id=timeInp_"+data.id+"></span>" +
						   "<div class=productListBtn onclick=productDetail('"+data.id+"') >立刻投资</div>";
					
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
						$("#timeInp_"+id).html("倒计时间 ："+showDay+"天"+showHour+"时"+showMinute+"分"+showSecond+"秒");
					} else {
						location.reload();
					}
				}, 1000);
				return "<dl class='privateList_dl'><dt>预期年化收益率</dt><dd style='font-weight: bold;' >"+getRate1(data.lowestRefundRate,data.highestRefundRate)+"<span style='color:#ff5858'>"+getRateCoupon(data.borrowModel.interestRateValue)+"</span></dd></dl>" +
					   "<dl class='privateList_dl1'><dt>投资期限</dt><dd>"+getTimeLimit(data)+"</dd></dl>"+
					   "<dl class='privateList_dl1'><dt>起投金额</dt><dd><p id="+data.id+">￥"+format_(data.borrowModel.lowestAccount)+"</p><span >元</span></dd></dl>" +
					   "<span class='productEndTime' id=timeInp_"+data.id+"></span>" +
					   "<div class=productListBtn onclick=productDetail('"+data.id+"') >即将上线</div>";
			}
		}
	
	}
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
/**
 * 切换显示div
 */
function showBtn(btn_id,data_id)
{
	$("#btn_one").removeClass("actived");
	$("#btn_two").removeClass("actived");
	
	$("#"+btn_id).addClass("actived");
	
	$("#map_data").hide();
	$("#list_data").hide();
	
	$("#"+data_id).show();
}
/**
 * 获取收益
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
/**
 * 时间戳转换
 * 
 * @param unixtime
 * @returns
 */
function jsDateTime(unixtime)  
{  
	 var date = new Date(unixtime);
	 
	 return  date.format("yyyy-MM-dd hh:mm:ss"); 
}  
Date.prototype.format = function(format)
{
	var o = 
	{
	"M+" : this.getMonth()+1, 
	"d+" : this.getDate(), 
	"h+" : this.getHours(),
	"m+" : this.getMinutes(), 
	"s+" : this.getSeconds(), 
	"q+" : Math.floor((this.getMonth()+3)/3), 
	"S" : this.getMilliseconds() 
	}

	if(/(y+)/.test(format)) 
	{
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	}

	for(var k in o) 
	{
		if(new RegExp("("+ k +")").test(format))
		{
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
		}
	}
	return format;
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

/**
 * 返回
 */
function goBack()
{
	location.href = "/nb/wechat/product/product_menue.html?productType=2";
}