var flagId;
$(function(){
	
	flagId = $("#id").val();
	//获取标签
	$.ajax({
		url:"/product/showProductTypeFlagListForPc.action?time="+jsStrToTime(),
		type:"GET",
		ansyc:false,
		dataType:"json",
		success:function(res)
		{
			var list_data = res.data;
			var htmlStrTitle = "";
			for(var i = 0;i<list_data.length;i++){
				if(list_data[i].id==flagId){
					htmlStrTitle+= "<div class='col-md-2 col-xs-2 col-sm-2 col-lg-2 padding_col0 text product_nav_col product_nav_col_actived'><a href='/nb/pc/product/product_list.html?id="+list_data[i].id+"'>"+list_data[i].flagName+"</a></div>";
				}else{
					htmlStrTitle+="<div class='col-md-2 col-xs-2 col-sm-2 col-lg-2 padding_col0 text product_nav_col'><a href='/nb/pc/product/product_list.html?id="+list_data[i].id+"'>"+list_data[i].flagName+"</a></div>";
				}
			}
			
			$("#flagList").html(htmlStrTitle);	
		}
	})
	
	//获取列表数据
	getListData('');	
//	Countdown('recommendTime');
	
})

//计算倒计时
function Countdown(str){
	$.ajax({
		url:'/nb/pc/product/showProductListByFlagForPc.action?id='+$("#id").val()+"&orderField="+str+"&time="+jsStrToTime(),
		type:"GET",
		dataType:"json",
		success:function(res)
		{
			var list_data = res.data;
			var systemDate = res.systemDate;
			$.each(res.data,function(i,item){
					var fixedTimes=0;
					var expirationTime=0;
					if(item.borrowModel){
						if(item.borrowModel.fixedTime){
							fixedTimes = item.borrowModel.fixedTime;	
						}
						if(item.borrowModel.expirationTime){
							expirationTime = item.borrowModel.expirationTime;
						}
						nowTime = parseInt(systemDate);
						var appointTime = parseInt(fixedTimes);
						var endTime = parseInt(expirationTime);
						var id = item.borrowModel.id;
						var scale = parseFloat(item.borrowModel.scales);
						if(scale>=100){
							$("#timeInp_"+id).html("");
							$("#investBtn_"+id).html("抢光了");
							$("#investBtn_"+id).removeClass("ppfundBtn");
							$("#investBtn_"+id).addClass("soldOut");
						}else{
							if(nowTime>appointTime){
								
								if(nowTime>endTime){
									$("#timeInp_"+id).html("");
									$("#investBtn_"+id).html("结束了");
									$("#investBtn_"+id).removeClass("ppfundBtn");
									$("#investBtn_"+id).addClass("soldOut");
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
								}
								
							}else{							
								$("#investBtn_"+id).html("即将上线");
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
							}
						}
						
					}
				
			})			
		}
	})

}


/**
 * 根据投资期限判断显示的内容
 * @param data
 */
function timeLimit(data){
	if(data.experienceModel){
		if("0"!=data.experienceModel.timeLimit){
			return data.experienceModel.timeLimit+"<span class='ppfund_yuan'>天</span>";
		}else{
			return "活期";
		}
	}
	if(data.ppfundModel){
		if("0"!=data.ppfundModel.timeLimit){
			return data.ppfundModel.timeLimit+"<span class='ppfund_yuan'>天</span>";
		}else{
			return "活期";
		}
	}
	if(data.borrowModel){
		if("0"!=data.borrowModel.timeLimit){
			if("0"==data.borrowModel.borrowTimeType){
				return data.borrowModel.timeLimit+"<span class='ppfund_yuan'>月</span>";
			}
			if("1"==data.borrowModel.borrowTimeType){				
				return data.borrowModel.timeLimit+"<span class='ppfund_yuan'>天</span>";
			}
		}else{
			return "活期";
		}
	}
}
/**
 * 根据投资进度判断显示的内容
 * @param data
 */
function getScale(data,systemDate){
	if(data.experienceModel){
		if(parseFloat(data.experienceModel.account)==0){
			return "<div class='ppfundBtn'  onclick=ppfundDetail("+data.id+","+flagId+","+data.experienceModel.id+")>立刻投资</div>";
		}else{			
			if(parseFloat(data.experienceModel.scales)>=100){
				return "<div class='soldOut'  onclick=ppfundDetail("+data.id+","+flagId+","+data.experienceModel.id+")>抢光了</div>";
			}else{
				return "<div class='ppfundBtn'  onclick=ppfundDetail("+data.id+","+flagId+","+data.experienceModel.id+")>立刻投资</div>";
			}
		}
	}
	if(data.ppfundModel){
		if(parseFloat(data.ppfundModel.account)==0){
			return "<div class='ppfundBtn'  onclick=ppfundDetail("+data.id+","+flagId+","+data.ppfundModel.id+")>立刻投资</div>";	
		}else{			
			if(parseFloat(data.ppfundModel.scales)>=100){
				return "<div class='soldOut'  onclick=ppfundDetail("+data.id+","+flagId+","+data.ppfundModel.id+")>抢光了</div>";
			}else{
				return "<div class='ppfundBtn'  onclick=ppfundDetail("+data.id+","+flagId+","+data.ppfundModel.id+")>立刻投资</div>";	
			}
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
		var nowTime = parseInt(systemDate);
		var appointTime = parseInt(fixedTimes);
		var endTime = parseInt(expirationTime);
		var id = data.borrowModel.id;
		if(parseFloat(data.borrowModel.scales)>=100){
			$("#timeInp_"+id).html("");
			return "<div class='soldOut' id='investBtn_"+data.borrowModel.id+"'  onclick=borrowDetail("+data.id+","+flagId+","+data.borrowModel.id+")>抢光了</div>";			
		}else{
			if(nowTime>appointTime){
				
				if(nowTime>endTime){
					$("#timeInp_"+id).html("");
					return "<div class='soldOut' id='investBtn_"+data.borrowModel.id+"'  onclick=borrowDetail("+data.id+","+flagId+","+data.borrowModel.id+")>结束了</div>";
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
					return "<div class='ppfundBtn' id='investBtn_"+data.borrowModel.id+"'  onclick=borrowDetail("+data.id+","+flagId+","+data.borrowModel.id+")>立刻投资</div>";
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
				return "<div class='ppfundBtn' id='investBtn_"+data.borrowModel.id+"'  onclick=borrowDetail("+data.id+","+flagId+","+data.borrowModel.id+")>即将上线</div>";
			}
		}
		
	}
	
}
/**
 * 拼接收益
 * @param low
 * @param high
 * @returns {String}
 */
function getRate(low,high,data)
{	
	var rate = "";
	if(data.experienceModel){
		var interestRateValue = data.experienceModel.interestRateValue;
		if(interestRateValue==0||interestRateValue==undefined||interestRateValue==null){
			rate=""
		}else{
			rate+="<span class='ppfund_precent'>+</span><span class='ppfund_rate'>"+interestRateValue+"<span class='ppfund_precent'>%</span></span>"
		}
	}
	if(data.ppfundModel){
		var interestRateValue = data.ppfundModel.interestRateValue;
		if(interestRateValue==0||interestRateValue==undefined||interestRateValue==null){
			rate=""
		}else{
			rate+="<span class='ppfund_precent'>+</span><span class='ppfund_rate'>"+interestRateValue+"<span class='ppfund_precent'>%</span></span>"
		}
	}
	if(data.borrowModel){
		var interestRateValue = data.borrowModel.interestRateValue;
		if(interestRateValue==0||interestRateValue==undefined||interestRateValue==null){
			rate=""
		}else{
			rate+="<span class='ppfund_precent'>+</span><span class='ppfund_rate'>"+interestRateValue+"<span class='ppfund_precent'>%</span></span>"
		}
	}
	if(low==high&&"-1"==high)
	{
		return "<p class='ppfund_rate'>0<span class='ppfund_precent'>%</span>"+rate+"</p>";
	}	
	if(low==high&&"0"==high)
	{
		return "<p class='ppfund_rate'>浮动</p>"+rate;
	}
	if(low==high&&"0"!=high)
	{
		return "<p class='ppfund_rate'>"+low+"<span class='ppfund_precent'>%</span>"+rate+"</p>";
	}
	if(low!=high&&"0"==high)
	{
		return "<p class='ppfund_rate'>"+low+"<span class='ppfund_precent'>%+浮动</span>"+rate+"</p>";
		
	}
	if(low!=high&&"0"!=high)
	{
		return "<p class='ppfund_rate'>"+low+"<span class='ppfund_precent'>%-</span>"+high+"<span class='ppfund_precent'>%</span></p>"+rate;
	}
}

/**
 * 现金
 * @param productId
 * @param flagId
 * @param ppfundId
 */
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

/**
 * 排序
 * @param str
 * @param id
 */
function orderField(str,id){
	$(".sort_col").removeClass("sort_col_actived");
	$("#"+id).addClass("sort_col_actived");
	getListData(str);
//	Countdown(str);

}

/**
 * 获取产品列表的数据
 * @param str
 */
function getListData(str){
	$.ajax({
		type:'get',
		url:'/nb/pc/product/showProductListByFlagForPc.action?id='+$("#id").val()+"&orderField="+str+"&time="+jsStrToTime(),
		ansyc:false,
		dataType:'json',
		success:function(res){
			var list_data = res.data;
			var htmlStr="";
			var systemDate = res.systemDate;
			if(list_data.length==0){
				$("#productListPPfund").html("<p class='text-center'>暂无数据</p>");
			}else{
				var iconUrl = res.listUrl;
				if(iconUrl===""||iconUrl===undefined||iconUrl===null){
					iconUrl="/themes/theme_default/nb/pc/img/list_icon.png";
				}
				
				htmlStr+="<div class='min-row productListAll'>" +
				"<div class='col-md-2 col-xs-2 col-sm-2 col-lg-2 padding_col0'><img src="+iconUrl+"  class='productImg'/></div><div class='col-md-10 col-xs-10 col-sm-10 col-lg-10 padding_col0 ppfund_list ppfund_border'>";
				for(var i=0;i<list_data.length;i++){
					
						htmlStr+="<div class='row padding_col0 row_col ppfund_col_bottom'>" +
								 "<p class='ppfund_name col-md-7 col-xs-7 col-sm-7 col-lg-7 padding_col0'>";
								  if(list_data[i].hotProduct==1){
										htmlStr+="<span><img src='/themes/theme_default/nb/pc/img/hot.png' class='productHot'/></span>";
								  }else{
										htmlStr+="<span><img src='/themes/theme_default/nb/pc/img/hot.png' class='productHot' style='display:none;'/></span>";
								  }
								  htmlStr+=list_data[i].productName+"</p>";
								  if(list_data[i].experienceModel){
									  htmlStr+="<p class='total col-md-6 text-right padding_col0' style='display:none;'>已累计投资<span class='product_total_money'>"+list_data[i].totalInvestMoney+"</span>金额，产生<span class='product_total_money'>"+list_data[i].totalProfitMoney+"</span>利益</p>";
									
								  }
								  if(list_data[i].ppfundModel){
									  htmlStr+="<p class='total col-md-6 text-right padding_col0' style='display:none;'>已累计投资<span class='product_total_money'>"+list_data[i].totalInvestMoney+"</span>金额，产生<span class='product_total_money'>"+list_data[i].totalProfitMoney+"</span>利益</p>";
									
								  }
								  if(list_data[i].borrowModel){
									  htmlStr+="<p class='total col-md-5 col-xs-5 col-sm-5 col-lg-5 text-right padding_col0' id='timeInp_"+list_data[i].borrowModel.id+"'></p>";
								  }

										   
								   htmlStr+="<div class='clearfix'></div>" +
								   "<div class='col-md-3 col-xs-3 col-sm-3 col-lg-3 padding_col0 text-center'>" +
								   getRate(list_data[i].lowestRefundRate,list_data[i].highestRefundRate,list_data[i])+
								   "<p>预期年化收益率</p></div>" +
								   	"<div class='col-md-3 col-xs-3 col-sm-3 col-lg-3 text-center padding_col0 ppfund_col'><p class='ppfund_start'><span class='ppfund_yuan'>&yen;</span>"+format_(list_data[i].lowestAccount)+"</p>" +
										   "<p>起投金额</p>" +
										   "</div>" +
										   "<div class='col-md-3 col-xs-3 col-sm-3 col-lg-3 text-center padding_col0 ppfund_col'>" +
										   "<p class='ppfund_start'>"+timeLimit(list_data[i])+"</p>" +
										   "<p>投资期限</p></div>" +
										   "<div class='col-md-3 col-xs-3 col-sm-3 col-lg-3 text-center padding_col0'>"+getScale(list_data[i],systemDate)+
										   "</div></div>";												 								  		   
					
				}				
				htmlStr+="</div></div>";
				$("#productListPPfund").html(htmlStr);				
			}
		}
	});
}
	
