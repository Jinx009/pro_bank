$(function(){
	
	changeLeftMenue("我的账户");
	
	$.ajax({
		type : "post",
		url : "/user/capitalStatis.html?random=" + Math.random(),
		dataType : "json",
		success : function(json) {
			if (checkUser(json.result)) {
				$(".total_money").html(format_(json.total));
				$(".account_income_money").html(format_(json.netProfit));
				$("#user_money").html(format_(json.useMoney) + "元");
				$("#no_user_money").html(format_(json.noUseMoney) + "元");
				$("#collection_money").html(format_(json.collection) + "元");
				bank(json.bankAccount);
				chart(json);
			} else {
			}
		}
	});

	// 800bank 精品推荐
	var htmlStr = "";
	$.ajax({
				type : "post",
				url : "/nb/pc/product/showPopularProductListForPc.html?random="
						+ Math.random(),
				dataType : "json",
				success : function(json) {
					if (checkUser(json.result)) {
						var systemDate = json.systemDate;
						if(json.data !=""){
							$.each(json.data,function(i, item) {
									if (i < 4) {
										htmlStr += "<div class='row list_col'>";
										htmlStr += "<div class='col-md-3 col_1'><div class='product_name col-md-12'>";
										htmlStr += item.productName;
										htmlStr += "</div></div>";
										htmlStr += "<div class='col-md-3 text col_2'><p class='rate'>";
										htmlStr += getRates(item.lowestRefundRate,item.highestRefundRate,item);
										htmlStr += "</p><p>预计年化收益率</p></div>";
										htmlStr += "<div class='col-md-2 text col_3'><p class='startMoney'>";
										htmlStr += item.lowestAccount;
										htmlStr += "<span>元</span></p><p>起投金额</p></div>";
										htmlStr += "<div class='col-md-2 text col_4'><p class='investState'>";
										htmlStr += getInvestDate(item);
										htmlStr += "</p><p>投资期限</p></div>";
										htmlStr += "<div class='col-md-2 text col_5'>";
										htmlStr +=  getInvestBtn(item,systemDate);
										htmlStr += "</div></div>";
									}
							$("#showData").html(htmlStr);
						  });
						}else{
							$(".more").hide();
						}
						
					} else {
						showDiv("util_login");
					}
				}
			});
});
//中心首页银行信息
function bank(account) 
{
	var htmlStr = "";
	if (account <= 0)
	{
		htmlStr += "<p>未绑定银行卡</p>"+ "<p class='addCard'><a href='/nb/pc/cash/bank.html'>添加新的银行卡信息</a></p>";
	} 
	else 
	{
		htmlStr += "<p class='hasband'>已经绑定银行卡:<span class='card-num'><a href='/nb/pc/cash/bank.html'>"+ account + "</a></span>张</p>";
	}
	$(".bank_").html(" ");
	$(".bank_").html(htmlStr);
}
//中心首页饼状图
function chart(json) 
{
	if(parseFloat(json.useMoney) <=0 && parseFloat(json.noUseMoney)<=0 && parseFloat(json.collection) <=0){
		$("#account-left-con2").css("display","none");
	}else{
		var doughnutData = [ 
		{
			value : json.useMoney,
			color : "#ffc659",
			label : "可用余额"
		}, 
		{
			value : json.noUseMoney,
			color : "#79cad6",
			label : "冻结金额"
		}, 
		{
			value : json.collection,
			color : "#d6f3f1",
			label : "待收金额"
		}];
		var ctx = document.getElementById("chart-area").getContext("2d");
		window.myDoughnut = new Chart(ctx).Doughnut(doughnutData, 
		{
			responsive : false
		});
	}
}

/**
 * 收益率计算
 * @param low 最低
 * @param high 最高
 * @returns {String}
 */
function getRates(low,high,data)
{
	var rate = "";
	if(data.experienceModel){
		var interestRateValue = data.experienceModel.interestRateValue;
		if(interestRateValue==0||interestRateValue==undefined||interestRateValue==null){
			rate=""
		}else{
			rate+="+"+interestRateValue+"<span'>%</span>"
		}
	}
	if(data.ppfundModel){
		var interestRateValue = data.ppfundModel.interestRateValue;
		if(interestRateValue==0||interestRateValue==undefined||interestRateValue==null){
			rate=""
		}else{
			rate+="+"+interestRateValue+"<span>%</span>"
		}
	}
	if(data.borrowModel){
		var interestRateValue = data.borrowModel.interestRateValue;
		if(interestRateValue==0||interestRateValue==undefined||interestRateValue==null){
			rate=""
		}else{
			rate+="+"+interestRateValue+"<span>%</span>"
		}
	}
	if(low===high&& "-1"==high){
		return "0.0<span>%</span>"+rate;
	}

	if(low===high&&"0"==high)
	{
		return "浮动"+rate;
	}
	if(low===high&&"0"!==high)
	{
		return low+"<span>%</span>"+rate;
	}
	if(low!==high&&"0"==high)
	{
		return low+"<span>%+浮动</span>"+rate;
		
	}
	if(low!==high&&"0"!==high)
	{
		return low+"<span>%+</span>"+high+"<span>%</span>"+rate;
	}
}

/**
 * 投资连接
 * @param data
 */
function getInvestUrl(data)
{
	if(data.ppfundModel)
	{
		return  "ppfundDetail('"+data.id+"','"+data.flagId+"','"+data.relatedId+"')"; 
	}
	else
	{
		return "borrowDetail('"+data.id+"','"+data.flagId+"','"+data.relatedId+"')";
	}
}

/**
 * 投资期限
 * @param data
*/
function getInvestDate(data)
{
	if(data.ppfundModel)
	{
		return "活期";
	}else if(data.experienceModel){
		
	}
	else
	{
		if("0"==data.borrowModel.borrowTimeType)
		{
			return  data.borrowModel.timeLimit+"月";
		}
		if("1"==data.borrowModel.borrowTimeType)
		{
			return  data.borrowModel.timeLimit+"天";
		}
	}
}
function ppfundDetail(productId,flagId,ppfundId)
{
	var url = "/nb/pc/product/ppfund_detail.html?productId="+productId+"&flagId="+flagId+"&ppfundId="+ppfundId;
	
	$.ajax({
		url:"/nb/sessionUser.action?time="+jsStrToTime(),
		type:"GET",
		ansyc:false,
		dataType:"json",
		success:function(res)
		{
			if(null!=res.result&&""!=res.result)
			{
				location.href = url;
			}
			else
			{
				changeLoginBtn(url);
			}
		}
	})
}
/**
 * 更改登陆确定按钮链接
 * @param url
 */
function changeLoginBtn(url)
{
	$("#commonLoginBtn").attr("onclick","doProductLogin('"+url+"')");
	showDiv("util_login");
}
/**
 * 非现金
 * @param productId
 * @param flagId
 * @param investId
 */
function borrowDetail(productId,flagId,investId)
{
	var url = "/nb/pc/product/borrow_detail.html?productId="+productId+"&flagId="+flagId+"&investId="+investId;
	
	$.ajax({
		url:"/nb/sessionUser.action?time="+jsStrToTime(),
		type:"GET",
		ansyc:false,
		dataType:"json",
		success:function(res)
		{
			if(null!=res.result&&""!=res.result)
			{
				location.href = url;
			}
			else
			{
				changeLoginBtn(url);
			}
		}
	})
}

function getInvestBtn(data,systemDate)
{
	var nowTime = parseInt(systemDate);
	var appointTime = 0,endTime = 0;
	
	if(data.borrowModel)
	{
		appointTime = parseInt(data.borrowModel.fixedTime);
		endTime = parseInt(data.borrowModel.expirationTime);
		
		if(nowTime<appointTime)
		{
			return "<div class='investBtn' onclick="+getInvestUrl(data)+" >预约</div>";
		}
		else if(nowTime>endTime)
		{
			return "<div class='investBtn-1' onclick="+getInvestUrl(data)+">结束了</div>";
		}
		else
		{
			return getScale(data);
		}
	}
	else
	{
		return getScale(data);
	}
}
/**
 * 根据投资进度判断显示的内容
 * @param data
 */
function getScale(data)
{
	if(data.experienceModel)
	{
		if(parseFloat(data.experienceModel.account)==0)
		{
			return "<div class='investBtn'  onclick="+getInvestUrl(data)+">立刻投资</div>";
		}
		else
		{			
			if(parseFloat(data.experienceModel.scales)>=100)
			{
				return "<div class='investBtn-1' onclick="+getInvestUrl(data)+">抢光了</div>";
			}
			else
			{
				return "<div class='investBtn' onclick="+getInvestUrl(data)+"  >立刻投资</div>";
			}
		}
	}
	if(data.ppfundModel)
	{
		if(parseFloat(data.ppfundModel.account)==0)
		{
			return "<div class='investBtn' onclick="+getInvestUrl(data)+" >立刻投资</div>";	
		}
		else
		{			
			if(parseFloat(data.ppfundModel.scales)>=100)
			{
				return "<div class='investBtn-1' onclick="+getInvestUrl(data)+"  >抢光了</div>";
			}
			else
			{
				return "<div class='investBtn' onclick="+getInvestUrl(data)+" >立刻投资</div>";	
			}
		}
	}
	if(data.borrowModel)
	{
		
		if(parseFloat(data.borrowModel.scales)>=100)
		{
			return "<div class='investBtn-1' onclick="+getInvestUrl(data)+" >抢光了</div>";
		}
		else
		{
			return "<div class='investBtn' onclick="+getInvestUrl(data)+" >立刻投资</div>";
		}
	}
	
}