Handlebars.registerHelper("transFormatStyle",function(value){
	switch (value)
	{
		case 1:
			return "按月分期还款";
			break;
		case 2:
			return "一次性还款";
			break;
		case 3:
			return "每月付息，到期还本";
			break;
		case 4:
			return "提前付息到期还本";
			break;
		case 5:
			return "每月提前还息到期还本";
			break;
		default:
			return ""
	}
});

Handlebars.registerHelper("transFormatScales",function(scales){
	if(scales<100){
		return '<img src="/themes/theme_default/images/index-list-j.jpg" />';
	}
	else{
		return '<img src="/themes/theme_default/images/index-list-h.jpg" />';
	}

})

Handlebars.registerHelper("transFormatfull",function(scales){
	if(scales==100){
		return '<img src="/themes/theme_default/images/full-biao.png" />';
	}
	else{
	}

})
Handlebars.registerHelper("transrecommend",function(value){
	if(value==true){
		return '<img src="/themes/theme_default/images/tj-biao.png" />';
	}
	else{
	}

})


Handlebars.registerHelper("transFormatType",function(value){
	switch(value)
	{
		case 101:
			return new Handlebars.SafeString('<img src="../../img/miao.png" />');//秒还标
			break;
		case 102:
			return new Handlebars.SafeString('<img src="../../img/xin.png" />');//信用标
			break;	
		case 103:
		return new Handlebars.SafeString('<img src="../../img/pledge.png" />');//固定收益类产品
			break;
		case 104:
		return new Handlebars.SafeString('<img src="../../img/jing.png" />');//净值标
			break;	
		case 105:
		return new Handlebars.SafeString('<img src="../../img/bao.png" />');//担保标
			break;
		case 108:
		return new Handlebars.SafeString('<img src="../../img/yuyue.png" />');//预约标
			break;
		case 110:
		return new Handlebars.SafeString('<img src="../../img/flow.png" />');//流转标
			break;
		case 112:
		return new Handlebars.SafeString('<img src="../../img/offvouch.png" />');//线下担保标
			break;
		case 113:
		return new Handlebars.SafeString('<img src="../../img/bao.png" />');//质押标
			break;
		case 115:
		return new Handlebars.SafeString('<img src="../../img/shi.png" />');//事业标
			break;
		case 116:
		return new Handlebars.SafeString('<img src="../../img/lian.png" />');//联名担保标
			break;
		case 119:
			return new Handlebars.SafeString('<img src="../../img/estate.png" />');//海外投资产品
			break;
		case 122:
			return new Handlebars.SafeString('<img src="../../img/entrust.png" />');//浮动收益类产品
			break;
		default:
			return ""
	}
});
//时间转换  格式:2014-08-15 00:00:00
Handlebars.registerHelper("transFormatDate",function(value){
	function formatDate(now) { 
		var year=now.getFullYear();
		var month=now.getMonth()+1; 
		var date=now.getDate(); 
		var hour=now.getHours(); 
		var minute=now.getMinutes(); 
		var second=now.getSeconds(); 
		if(second < 10)
		{
			second = '0' + second;
		}
		if(minute < 10)
		{
			minute = '0' + minute;	
		}
		return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second; 
	} 
	if(value==null ||value==''){
		return '';
	}
	var d = new Date(value);
	return formatDate(d);
});

//时间转换  格式:2014-08-15 00:00:00
Handlebars.registerHelper("countdownDate",function(tenderTime,nowTime){
	var t= parseInt(nowTime)-parseInt(tenderTime);
				   var d=parseInt(t/60/60/24);
				   var h=parseInt(t/60/60);
				   var m=parseInt(t/60);
				   var s=parseInt(t);
				    if(d>=1){
					   return d+"天前";
					}
					else if(h>=1){
						return h+"小时前";
					}
					else if(m>=1){
						return m+"分钟前";
					}
					else{
						return s+"秒前";
					}
});
//格式:2014-08-15
Handlebars.registerHelper("dateFormat",function(value){
	function formatDate(now) { 
		var year=now.getFullYear();
		var month=now.getMonth()+1; 
		var date=now.getDate(); 
		return year+"-"+month+"-"+date; 
	} 
	if(value==null ||value==''){
		return '';
	}
	var d = new Date(value);
	return formatDate(d);
});

// 格式:2014/08/15
Handlebars.registerHelper("noticeDateFormat",function(value){
	function formatDate(now) { 
		var year=now.getFullYear();
		var month=now.getMonth()+1; 
		var date=now.getDate(); 
		var hour=now.getHours(); 
		var minute=now.getMinutes(); 
		var second=now.getSeconds(); 
		if(second < 10)
		{
			second = '0' + second;
		}
		return year+"/"+month+"/"+date; 
	} 
	if(value==null ||value==''){
		return '';
	}
	var d = new Date(value);
	return formatDate(d);
});

// 格式:2014/08/15 12:00
Handlebars.registerHelper("noticeDateFormatNew",function(value){
	function formatDate(now) { 
		var year=now.getFullYear();
		var month=now.getMonth()+1; 
		var date=now.getDate(); 
		var hour=now.getHours(); 
		var minute=now.getMinutes(); 
		var second=now.getSeconds(); 
		if(month < 10)
		{
			month = '0' + month;
		}
		if(date < 10)
		{
			date = '0' + date;
		}
		if(hour < 10)
		{
			hour = '0' + hour;
		}
		if(minute < 10)
		{
			minute = '0' + minute;
		}
		return year+"/"+month+"/"+date+" "+hour+":"+minute; 
	} 
	if(value==null ||value==''){
		return '';
	}
	var d = new Date(value);
	return formatDate(d);
});

Handlebars.registerHelper("transFormatStatus",function(status,scales){
	if(status == 0){
		return "未满标复审";
	}else if(status == 1){
		return "满标复审通过";
	}else{
		return "满标复审未通过";
	}
});

Handlebars.registerHelper("transFormatTenderType",function(value){
	if(value == 0){
		return "手动投标";
	}else if(value == 1){
		return "自动投标";
	}else{
		return "手机投标";
	}
});

//添加判断是否满标复审通过
Handlebars.registerHelper("AddProtocol",function(status,scales,id){
	if(status == 1 && scales == 100){ 
		return '<a href="/member/invest/tenderProtocol.html?tenderId='+id+'" >下载协议</a>';
	}else{
		return "-";
	}
});
Handlebars.registerHelper("transFormatTenderStatus",function(status){
	if(status == 0){
		return "待审核";
	}else if(status == 1){
		return "成功";
	}else{
		return "失败";
	}
});

//添加判断是否投标成功
Handlebars.registerHelper("AddTenderProtocol",function(status,scales,id){
	if(status == 1 && scales == 100){ 
		return '<a href="/member/invest/tenderProtocol.html?tenderId='+id+'" >下载协议</a>';
	}else{
		return "-";
	}
});

//我的投资-收款明细-期数
Handlebars.registerHelper("investPeriodFun",function(borrowStyle,period,timeLimit){
	if(borrowStyle==2){
		return "1/1";
	}
	if(borrowStyle&&timeLimit){
		return (period)+"/"+timeLimit;
	}else{
		return (period)+"/"+1;
	}
});

//我的投资-收款明细-交易状态
Handlebars.registerHelper("transStatusFun",function(status){
	if(status==0){
		return "待收";
	}else{
		return "已收";
	}
	
});


//--------------------我的借款-我的借款----------------------------
//标题
Handlebars.registerHelper("logBorrowNameFun",function(name,id,addTime){
	if(name.length > 10){
		return new Handlebars.SafeString("<a href='/invest/detail.html?id="+id +"&startTime="+addTime+"' title="+name+" target='_blank'>"+name.substring(0,10)+"…</a>");
	}else{
		return new Handlebars.SafeString("<a href='/invest/detail.html?id=" + id + "&startTime="+addTime+"' title=" + name + " target='_blank'>" + name + "</a>");
	}
})


//标类型
Handlebars.registerHelper("borrowTypeName",function(type){
	switch (type) {
	case 101:
		 return "秒还标";
		break;
	case 102:
		 return "信用标";
		break;
	case 103:
		 return "固定收益类产品";
		break;
	case 104:
		 return "净值标";
		break;
	case 110:
		 return "流转标";
		break;
	case 112:
		 return "担保标";
		break;
	case 119:
		 return "海外投资产品";
		break;
	case 122:
		 return "浮动收益类产品";
		break;
	default:
		break;
	}
})

//PPfund还款方式
Handlebars.registerHelper("ppfundRepaymentWay",function(isFixedTerm){
	if(isFixedTerm == 1){
		return "到期还款";
	}
	return "随存随取";
})

//PPfund融资金额
Handlebars.registerHelper("ppfundAccount",function(account){
	if(account == 0){
		return new Handlebars.SafeString('<font>无限额</font>');
	}
	return new Handlebars.SafeString('<font>￥'+account+'</font>');
})

//PPfund融资期限
Handlebars.registerHelper("ppfundTimeLimit",function(timeLimit){
	if(timeLimit == 0){
		return "活期";
	}
	return timeLimit + "天";
})

//PPfund预约赎出
Handlebars.registerHelper("ppfundBookingOut",function(outTime,isFixedTerm){
	 var NowTime = new Date();
	 var tt =NowTime.getTime();

	if(isFixedTerm == 1){
		
	}else{
		if(outTime == "" || outTime == null || outTime == undefined){
			return new Handlebars.SafeString('<a class="yz">预约转出</a>');
		}else{
			var t= parseInt(outTime)-parseInt(tt);
			var d=parseInt(t/1000/60/60/24);
			var c=Math.ceil(t/1000/60/60/24);
		    if(c=0){
			   return d=d;
			}else if(c>0){
				return d=d+1;
			}
			if(d<=2) {
				
			}else{
				return new Handlebars.SafeString('<a class="yz">预约转出</a>');
			}
		}
	}
})

//PPfund进度
Handlebars.registerHelper("ppfundScales",function(scales){
	var showVal = scales.toString();
	showVal = showVal.substr(0,4);
	return showVal;
})

//PPfund可投金额
Handlebars.registerHelper("ppfundAccountWait",function(account, accountYes){
	return account-accountYes;
})

//借款期限
Handlebars.registerHelper("logBorrowTimeLimitFun",function(type,isDay,timeLimit){
	if(type == 101){
		return "满标即还";
	}else if(isDay != undefined && isDay == 1){
		return timeLimit + "天";
	}else{
		return timeLimit + "个月";
	}
})

//进度条的显示
Handlebars.registerHelper("slideBar",function(accountYes,account){
	showVal = ((accountYes/account)*100).toString();
	showVal = showVal.substr(0,4);
	return showVal;
})

//状态
Handlebars.registerHelper("logBorrowStatusFun",function(status,scales,type,flow){
	if(status == 0){
		return "等待初审";
	}else if(status == 1 && flow == true){
		return "已流标";
	}else if(status == 1 && scales != 100){
		return "初审通过";
	}else if(status == 1 && scales == 100){
		return "满标待审";
	}else if(status == 2){
		return "初审未通过";
	}else if(status == 3){
		return "复审通过";
	}else if(status == 4 || status == 49){
		return "复审未通过";
	}else if(status == -2){
		return "撤回处理中";
	}else if(status == 5 || status == 59){
		return "管理员撤回";
	}else if(status == 6){
		return "还款中";
	}else if(status == 7){
		return "部分还款";
	}else if(status == 8){
		if(type == 110){
			return "停止流转";
		}else{
			return "还款成功";
		}
	}else{
		return "已撤回";
	}
})
//借款详情-状态（区分后台管理员撤回）
Handlebars.registerHelper("borrowMineStatusFun",function(status,scales,type,flow){
	if(status == 0){
		return "等待初审";
	}else if(status == 1 && flow == true){
		return "初审通过(已流标)";
	}else if(status == 1 && scales != 100){
		return "初审通过";
	}else if(status == 1 && scales == 100){
		return "满标待审";
	}else if(status == 2){
		return "初审未通过";
	}else if(status == 3){
		return "复审通过";
	}else if(status == 4 || status == 49){
		return "复审未通过";
	}else if(status == -2){
		return "复审处理中";
	}else if(status == 5 || status == 59){
		return "管理员撤回";
	}else if(status == 6){
		return "还款中";
	}else if(status == 7){
		return "部分还款";
	}else if(status == 8){
		if(type == 110){
			return "停止流转";
		}else{
			return "还款成功";
		}
	}else if(status == 11){
		return "待初审";
	}else {
		return "已撤回";
	}
})

//操作
Handlebars.registerHelper("logBorrowOperateFun",function(status,type,id){
	if(status == 0){
			return  '-';
	}else if(status == 6 || status == 7 || status == 8){
		return "<a href='/member_borrow/borrow/repayment.html?borrowId=" + id + "' >还款明细</a>";
	}else{
		return '-';
	}
})

//协议下载
Handlebars.registerHelper("protocolBorrowerOperateFun",function(status,id){
	if(status == 3 || status == 6 || status == 7 || status == 8){ //添加判断是否满标复审通过
		return "<a href='/member/borrow/borrowerProtocol.html?borrowId=" + id + "' >下载协议</a>";
	}else{
		return "-";
	}
})


//--------------------我的借款-还款明细----------------------------
//第几期
Handlebars.registerHelper("repayPeriodFun",function(borrowStyle,borrowTimeType,timeLimit,period){
	if(borrowStyle!=2){
		if(borrowTimeType==1){//天标
			return "1/1";
		}else if(borrowTimeType==0){//月标
			return (period+1)+"/"+timeLimit;
		}else{
			return ""
		}
	}else{
		return "1/1";
	}
})

Handlebars.registerHelper("mainRepayPeriodFun",function(borrowStyle,borrowTimeType,timeLimit,currPeriod){
	if(borrowStyle!=2){
		if(borrowTimeType==1){//天标
			return "第1期/共1期";
		}else if(borrowTimeType==0){//月标
			return "第"+(currPeriod+1)+"期/共"+timeLimit+"期";
		}else{
			return ""
		}
	}
	else
	{
		return "第1期/共1期";
	}
})


//状态
Handlebars.registerHelper("repayStatusFun",function(status){
	if(status==0){
		return '待还款';
	}else if(status==2){
		return '网站垫付';
	}else if(status==1){
		return '已还款';
	}else if(status==3){
		return '还款处理中';
	}
})

//操作
Handlebars.registerHelper("repayOpearteFun",function(status,id,api_code,is_open_deposit,repaymentAccount,lateInterest,useMoney){
	var totalMoney = repaymentAccount + lateInterest;
	if(status==1){
		return '已还款';
	}else if(status==2)	{
		return '<a href="javascript:;" data-val="'+id+'" data-tatal="'+totalMoney+'" class="repayment" >逾期垫付还款</a>';
	}else if(status==3)	{
		return '处理中';
	}else{
		if(is_open_deposit == 1){
			if(api_code == 2 && ( status==0 || status==5 ) ){//环迅
				return '<a href="javascript:;" id="toPayBtn" data-val="'+id+'" data-total="'+totalMoney+'" date-money="'+useMoney+'">还款</a>';
			}else if(api_code == 3 &&  status==0){//汇付
				return '<a href="javascript:;" data-val="'+id+'" data-tatal="'+totalMoney+'" class="chinapnr" >还款</a>';
			}
		}else{
			return '<a href="javascript:;" data-val="'+id+'" data-tatal="'+totalMoney+'" class="repayment" >还款</a>';
		}
	}
})




//--------------------我的借款-信用额度----------------------------

//新额度
Handlebars.registerHelper("accountNewFun",function(accountNew){
	if(accountNew>0){
		return accountNew;
	}else{
		return "-";
	}
})

//状态
Handlebars.registerHelper("amountStatusFun",function(status){
	switch(status){
	case -1:
		return "审核未通过";
	case 1:
		return "审核通过";
	default:
		return "正在审核";
	}
})

//审核时间
Handlebars.registerHelper("verifyTimeFun",function(verifyTime,status){
	if(status!=0 && status!=2){
		function formatDate(now) { 
			var year=now.getFullYear();
			var month=now.getMonth()+1; 
			var date=now.getDate(); 
			var hour=now.getHours(); 
			var minute=now.getMinutes(); 
			var second=now.getSeconds(); 
			if(second < 10)
			{
				second = '0' + second;
			}
			return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second; 
		} 
		if(verifyTime==null ||verifyTime==''){
			return '';
		}
		var d = new Date(verifyTime);
		return formatDate(d);
	}			
	return "-";
})

//标名称隐藏
Handlebars.registerHelper("hideBorrowName",function(borrowName){
	if(borrowName.length > 7)
	{
		return new Handlebars.SafeString(borrowName.substr(0,7)+'…<u><div class="mark_details_l"><i></i>'+borrowName+'</div></u>')
	}
	else
	{
		return borrowName;
	}
	
});

//时间格式转换 2014-8-18 16:09:27
Handlebars.registerHelper("timeFormat",function(time){
		function formatDate(now) { 
			var year=now.getFullYear();
			var month=now.getMonth()+1; 
			var date=now.getDate(); 
			var hour=now.getHours(); 
			var minute=now.getMinutes(); 
			var second=now.getSeconds(); 
			if(second < 10)
			{
				second = '0' + second;
			}
			return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second; 
		} 
		if(time==null ||time==''){
			return '';
		}
		var d = new Date(time);
		return formatDate(d);
})

//时间格式转换 2014-9-4
Handlebars.registerHelper("timeMonthFormat",function(time){
		function formatDate(now) { 
			var year=now.getFullYear();
			var month=now.getMonth()+1; 
			var date=now.getDate(); 
			return year+"-"+month+"-"+date; 
		} 
		if(time==null ||time==''){
			return '';
		}
		var d = new Date(time);
		return formatDate(d);
})

//时间格式转换 2014年7月10日 12:32:00
Handlebars.registerHelper("timeMsgFormat",function(time){
	function formatDate(now){
		var year = now.getFullYear();
		var month = now.getMonth()+1;
		var date = now.getDate();
		var hour = now.getHours();
		var minute = now.getMinutes();
		var second = now.getSeconds();
		if(second < 10)
		{
			second = '0' + second;
		}
		return year+'年'+month+'月'+date+'日'+''+hour+':'+minute+':'+second;
	}
	if(time == null || time == '')
	{
		return '';
	}
	return formatDate(new Date(time));
})

//积分商城图片链接
Handlebars.registerHelper("imgSrcAdd",function(imgSrc){
	return $("#goods_list").data("href")+imgSrc;
	
});

//积分商城剩余数量
Handlebars.registerHelper("remainCount",function(store,sellAcount,freezeStore){
	return store - sellAcount - freezeStore;
	
});

//积分商城动态链接图片
Handlebars.registerHelper("imgDemicSrc",function(imgSrc){
	return $("#convert_dynamic_ul").data("href")+imgSrc;
	
});

//发货状态转换 状态:0待审核,1审核通过,-1审核未通过,2已发货,3已收货
Handlebars.registerHelper("deliverStatus",function(status){
	switch(status)
	{
		case 0:
		return '待审核';
		break;
		case 1:
		return '审核通过';
		break;
		case -1:
		return '审核未通过';
		break;
		case 2:
		return '已发货';
		break;
		case 3:
		return '已收货';
		break;
		default:
		return '';
	}

})

/*-------------------------首页-------------------------------------*/
//标类型
Handlebars.registerHelper("investTypeName",function(type){
	switch (type) {
	case 101:
		 return "<em class='type5'></em>";
		break;
	case 102:
		 return "<em class='type4'></em>";
		break;
	case 103:
		 return "<em class='type1'></em>";
		break;
	case 104:
		 return "<em class='type5'></em>";
		break;
	case 110:
		 return "<em class='type3'></em>";
		break;
	case 112:
		 return "<em class='type2'></em>";
		break;
	default:
		break;
	}
})

//内容截取
Handlebars.registerHelper("hidecontent",function(content){
	if(content.length > 36)
	{
		return new Handlebars.SafeString(content.substr(0,36)+'…');
	}
	else
	{
		return new Handlebars.SafeString(content);
	}
	
});

Handlebars.registerHelper("hidecompany",function(content){
	if(content.length > 15)
	{
		return new Handlebars.SafeString(content.substr(0,12)+'…');
	}
	else
	{
		return new Handlebars.SafeString(content);
	}
	
});

//投资详情 项目描述
Handlebars.registerHelper("indexContent",function(content){
	var content = content.replace(/<[^>].*?>/g,"");
	content = content.replace(/&nbsp;/g,"");
	if(content.length >35)
	{
		return new Handlebars.SafeString(content.substr(0,35)+'…');
	}
	else
	{
		return content;
	}

});


//标标题截取
Handlebars.registerHelper("hideIndexBorrowName",function(name){
	if(name.length > 18)
	{
		return new Handlebars.SafeString(name.substr(0,18)+'…');
	}
	else
	{
		return name;
	}
	
});

Handlebars.registerHelper("timeLimitFormat",function(type,timeLimit,borrowTimeType){
	if(type == 101){
		return "满标即还";
	}else{
		if(borrowTimeType == 0){//月标
			return timeLimit+"个月";
		}else if(borrowTimeType == 1){//天标
			return timeLimit+"天";
		}
	}
	
});

//奖励显示
Handlebars.registerHelper("awardShowFun",function(award,partAccount,funds){
	if(award == 1){
		return new Handlebars.SafeString("投资比例奖励：<em>"+partAccount+"</em>%");
	}else if(award == 2){
		return new Handlebars.SafeString("分摊奖励：<em>"+funds+"</em>元");
	}else{
		return "";
	}
});

//借款标状态显示
Handlebars.registerHelper("investBorrowStatus",function(status,account,accountYes){
	switch (status) {
	case -1:
		 return "用户取消";
		break;
	case 0:
		 return "等待审批";
		break;
	case 1:
		if(account == accountYes){
			return "等待复审";
		}else{
			return "招标中";
		}
		break;
	case 2:
		 return "管理员撤回";
		break;
	case 3:
		 return "复审通过";
		break;
	case 4:
		 return "复审失败";
		break;
	case 5:
		 return "用户取消";
		break;
	case 6:
		 return "还款中";
		break;
	case 7:
		 return "还款中";
		break;
	case 8:
		 return "已还款";
		break;
	case 19:
		 return "自动投标";
		break;
	case 59:
		 return "用户取消";
		break;
	default:
		break;
	}
})

//操作状态
Handlebars.registerHelper("inverstListOptFun",function(scales,id,addTime,fixedTime){
	if(scales == 100){
		return new Handlebars.SafeString('<a href="/invest/detail.html?id='+id+'&startTime='+addTime+'" class="accomplish" title="查看详情" id="index_invest_btn">查看详情</a>');
	}else{
		var html = "";
		if(fixedTime){
			var d = new Date(fixedTime);
			var fmtDate = (d.getYear()+1900)+'-'+(d.getMonth()+1)+'-'+d.getDate()+' '+d.getHours()+':'+d.getMinutes()+':'+d.getSeconds();
			html = '<span class="yomibox" data="'+fmtDate+'"></span><a  href="/invest/detail.html?id='+id+'&startTime='+addTime+'" title="我要投标" id="index_invest_btn">我要投标</a>';
		}else
			html = '<a href="/invest/detail.html?id='+id+'&startTime='+addTime+'" title="我要投标" id="index_invest_btn">我要投标</a>'
		return new Handlebars.SafeString(html);
	}
	
});

//公告标题截取
Handlebars.registerHelper("hideNoticeTitle",function(title){
	if(title.length > 20)
	{
		return new Handlebars.SafeString(title.substr(0,20)+'…');
	}
	else
	{
		return title;
	}
	
});
//公告简介截取
Handlebars.registerHelper("hideNoticIntroduction",function(introduction){
	if(introduction.length > 200)
	{
		return new Handlebars.SafeString(introduction.substr(0,200)+'…');
	}
	else
	{
		return new Handlebars.SafeString(introduction);
	}
	
});

//文章网页标签去除转换
Handlebars.registerHelper("noticIntroduction",function(introduction){
	return new Handlebars.SafeString(introduction);
});

//投资动态截取
Handlebars.registerHelper("newTender",function(borrowName){
	if(borrowName.length > 6)
	{
		return new Handlebars.SafeString(borrowName.substr(0,4)+'…');
	}
	else
	{
		return borrowName;
	}
	
});

//媒体报道简介截取
Handlebars.registerHelper("hideMediaIntroduction",function(introduction){
	if(introduction.length > 112 )
	{
		return new Handlebars.SafeString(introduction.substr(0,112));
	}
	else
	{
		return introduction;
	}
	
});

//消息中心已读未读
Handlebars.registerHelper("msgRead",function(status){
	switch(status)
	{
		case 0:
		return new Handlebars.SafeString('<i class="iconfont unread">&#xe616;</i>未读');
		break;
		case 1:
		return new Handlebars.SafeString('<i class="iconfont readed">&#xe60e;</i>已读');
		break;
		default:
		return '';
		break;
	}
});


//隐藏消息内容
Handlebars.registerHelper("hideMsgContent",function(content){
	var content = content.replace(/<[^>].*?>/g,""); 
	if(content.length > 55)
	{
		return new Handlebars.SafeString(content.substr(0,55)+'…');
	}
	else
	{
		return content;
	}
});

//取整
Handlebars.registerHelper("parseInt",function(value){
	return parseInt(value);
})

//借款者认证
Handlebars.registerHelper("transApi",function(apiStatus,apiUsercustName){
	if(apiStatus == 0)
	{
		return "请立即开通";
	}
	else
	{
		return apiUsercustName;
	}
});


//待还月份
Handlebars.registerHelper("repaymentMonth",function(time){
	var time = new Date(time);
	var month = parseInt(time.getMonth()+1);
	if(month < 10)
	{
		month = "0"+month;
	}
	return time.getFullYear()+"."+month;
});
//待还日
Handlebars.registerHelper("repaymentDay",function(time){
	var time = new Date(time);
	var day = time.getDate();
	if(parseInt(day) < 10)
	{
		day = "0"+day;
	}
	return day;
})
//正在借款项目
Handlebars.registerHelper("mineLink",function(value){
	if(value == 0)
	{
		return "";
	}
	else
	{
		return "?status=1";
	}

})

//实名认证-借款账户
Handlebars.registerHelper("transBorrowRealName",function(openApi,apiStatus,realNameStatus){
	if(openApi){
		if(apiStatus==0){
			return new Handlebars.SafeString('<a  href="/member_borrow/security/realNameIdentify.html" title="实名未认证"><i class="iconfont" title="实名未认证" style="color: #f1f1f1;">&#xe61d;</i></a>');
		}else{
			return new Handlebars.SafeString('<i class="iconfont" style="color:#3db4ff;" title="实名已认证">&#xe61d;</i>');
		}
	}else{
		if(realNameStatus == 1){
			return new Handlebars.SafeString('<i class="iconfont" style="color:#3db4ff;" title="实名已认证">&#xe61d;</i>');
		}else if(realNameStatus == -1){
			return new Handlebars.SafeString('<a  href="/member/security/setting.html?borrow=1" title="实名认证未通过"><i class="iconfont" title="实名认证未通过" style="color: #f1f1f1;">&#xe61d;</i></a>');
		}else if(realNameStatus == 2){
			return new Handlebars.SafeString('<a  href="/member/security/setting.html?borrow=1" title="实名认证审核中"><i class="iconfont" title="实名认证审核中" style="color: #f1f1f1;">&#xe61d;</i></a>');
		}else{
			return new Handlebars.SafeString('<a  href="/member/security/setting.html?borrow=1" title="实名未认证"><i class="iconfont" title="实名未认证" style="color: #f1f1f1;">&#xe61d;</i></a>');
		}
	}
	
});
//实名认证-投资账户
Handlebars.registerHelper("transRealName",function(openApi,apiStatus,realNameStatus){
	if(openApi){
		if(apiStatus==0){
			return new Handlebars.SafeString('<a  href="/member/cash/bank.html" title="实名未认证"><i class="iconfont" title="实名未认证" style="color: #f1f1f1;">&#xe61d;</i></a>');
		}else{
			return new Handlebars.SafeString('<i class="iconfont" style="color:#3db4ff;" title="实名已认证">&#xe61d;</i>');
		}	
	}else{
		if(realNameStatus == 1){
			return new Handlebars.SafeString('<i class="iconfont" style="color:#3db4ff;" title="实名已认证">&#xe61d;</i>');
		}else if(realNameStatus == -1){
			return new Handlebars.SafeString('<a  href="/member/security/setting.html" title="实名认证未通过"><i class="iconfont" title="实名认证未通过" style="color: #f1f1f1;">&#xe61d;</i></a>');
		}else if(realNameStatus == 2){
			return new Handlebars.SafeString('<a  href="/member/security/setting.html" title="实名认证审核中"><i class="iconfont" title="实名认证审核中" style="color: #f1f1f1;">&#xe61d;</i></a>');
		}else{
			return new Handlebars.SafeString('<a  href="/member/security/setting.html" title="实名未认证"><i class="iconfont" title="实名未认证" style="color: #f1f1f1;">&#xe61d;</i></a>');
		}
	}
});

//手机认证-借款账户
Handlebars.registerHelper("transBorrowMobile",function(openApi,apiStatus,mobilePhoneStatus){
	if(openApi){
		if(apiStatus==0){
			return new Handlebars.SafeString('<a  href="/member_borrow/security/realNameIdentify.html" title="手机未认证"><i class="iconfont" style="color: #f1f1f1;" title="手机未认证">&#xe620;</i></a>');
		}else{
			return new Handlebars.SafeString('<i class="iconfont" style="color:#febb38;" title="手机已认证">&#xe620;</i>');
		}
	}else{
		if(mobilePhoneStatus == 1){
			return new Handlebars.SafeString('<i class="iconfont" style="color:#febb38;" title="手机已认证">&#xe620;</i>');
		}else{
			return new Handlebars.SafeString('<a  href="/member/security/setting.html?borrow=1" title="手机未认证"><i class="iconfont" style="color: #f1f1f1;" title="手机未认证">&#xe620;</i></a>');
		}
	}
});
//手机认证-投资账户
Handlebars.registerHelper("transMobile",function(openApi,apiStatus,mobilePhoneStatus){
	if(openApi){
		if(apiStatus==0){
			return new Handlebars.SafeString('<a  href="/member/security/setting.html" title="手机未认证"><i class="iconfont" style="color: #f1f1f1;" title="手机未认证">&#xe620;</i></a>');
		}else{
			return new Handlebars.SafeString('<i class="iconfont" style="color:#febb38;" title="手机已认证">&#xe620;</i>');
		}
	}else{
		if(mobilePhoneStatus == 1){
			return new Handlebars.SafeString('<i class="iconfont" style="color:#febb38;" title="手机已认证">&#xe620;</i>');
		}else{
			return new Handlebars.SafeString('<a  href="/member/security/setting.html" title="手机未认证"><i class="iconfont" style="color: #f1f1f1;" title="手机未认证">&#xe620;</i></a>');
		}
	}
	
});

//邮箱认证-借款账户
Handlebars.registerHelper("transBorrowEmail",function(openApi,apiStatus,emailStatus){
	if(openApi){
		if(emailStatus == 1){
			return new Handlebars.SafeString('<i class="iconfont" style="color:#93cd38;" title="邮箱已认证">&#xe61f;</i>');
		}else{
			return new Handlebars.SafeString('<a  href="/member_borrow/security/realNameIdentify.html" title="邮箱未认证"><i class="iconfont" style="color: #f1f1f1;">&#xe61f;</i></a>');
		}
	}else{
		if(emailStatus == 1){
			return new Handlebars.SafeString('<i class="iconfont" style="color:#93cd38;" title="邮箱已认证">&#xe61f;</i>');
		}else{
			return new Handlebars.SafeString('<a  href="/member/security/setting.html?borrow=1" title="邮箱未认证"><i class="iconfont" style="color: #f1f1f1;">&#xe61f;</i></a>');
		}
	}
	
});
//邮箱认证-投资账户
Handlebars.registerHelper("transEmail",function(openApi,apiStatus,emailStatus){
	if(openApi){
		if(apiStatus == 0){
			return new Handlebars.SafeString('<a  href="/member/security/setting.html" title="邮箱未认证"><i class="iconfont" style="color: #f1f1f1;">&#xe61f;</i></a>');
		}else{
			return new Handlebars.SafeString('<i class="iconfont" style="color:#93cd38;" title="邮箱已认证">&#xe61f;</i>');
		}
	}else{
		if(emailStatus == 1){
			return new Handlebars.SafeString('<i class="iconfont" style="color:#93cd38;" title="邮箱已认证">&#xe61f;</i>');
		}else{
			return new Handlebars.SafeString('<a  href="/member/security/setting.html" title="邮箱未认证"><i class="iconfont" style="color: #f1f1f1;">&#xe61f;</i></a>');
		}
	}
});

//VIP认证
Handlebars.registerHelper("transBorrowVip",function(vipStatus){
	if(vipStatus == 1){
		return new Handlebars.SafeString('<i class="iconfont" style="color: #ff6955;">&#xe61e;</i>');
	}else if(vipStatus == 2){
		return new Handlebars.SafeString('<a  href="/member_borrow/security/realNameIdentify.html" title="VIP审核中"><i class="iconfont" style="color: #f1f1f1;">&#xe61e;</i></a>');
	}else if(vipStatus == 3){
		return new Handlebars.SafeString('<a  href="/member_borrow/security/realNameIdentify.html" title="已经过期"><i class="iconfont" style="color: #f1f1f1;">&#xe61e;</i></a>');
	}
});
Handlebars.registerHelper("transVip",function(vipStatus){
	if(vipStatus == 1){
		return new Handlebars.SafeString('<i class="iconfont" style="color: #ff6955;">&#xe61e;</i>');
	}else if(vipStatus == 2){
		return new Handlebars.SafeString('<a  href="/member/security/setting.html" title="VIP审核中"><i class="iconfont" style="color: #f1f1f1;">&#xe61e;</i></a>');
	}else if(vipStatus == 3){
		return new Handlebars.SafeString('<a  href="/member/security/setting.html" title="已经过期"><i class="iconfont" style="color: #f1f1f1;">&#xe61e;</i></a>');
	}
});

//新额度
Handlebars.registerHelper("checkStr",function(str){
	if(str != '' && str != null && str != 'undefined' && typeof(str) != 'undefined' ){
		return str;
	}else{
		return "--";
	}
})

Handlebars.registerHelper("moneyindexFormat",function(money){
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
		return  t.split("").reverse().join("");
	}
});

//金额格式化  12,345.00
Handlebars.registerHelper("moneyFormat",function(money){
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
		return  t.split("").reverse().join("");
	}
});


Handlebars.registerHelper("numberFormat",function(num){
	  var str=num.toString();
	  var length=str.length,a=(length - 1)/3,m=length%3,i,s,result;
	  m=m==0?3:m
	  s=str.substring(0,m)+",";
	  for(i=1;i<a;i++){
	    s=s+str.substring(m,m+3)+",";
	    m=m+3;
	  }
	  s=s+str.substring(m,m+3);
	  var e=s.substring(s.length-1);
	  if(e==","){
		  s=s.substring(0,s.length-1);
	  }
	  return s;
});

//截取2位小数点金额
Handlebars.registerHelper("moneyFormatNews",function(money){
	money = money+"";
	var result   =   money.substr(0,money.indexOf(".")+3);
	return result;
});

//个人账户首页账户余额计算
Handlebars.registerHelper("accountBalanceFun",function(useMoney,noUseMoney){
	var accountBalance = parseFloat(useMoney + noUseMoney);
	n = 2; 
	accountBalance = parseFloat((accountBalance + "").replace(/[^\d\.-]/g, "")).toFixed(n) + ""; 
	var l = accountBalance.split(".")[0].split("").reverse(), r = accountBalance.split(".")[1]; 
	t = ""; 
	for (i = 0; i < l.length; i++) { 
		t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : ""); 
	} 
	return t.split("").reverse().join("") + "." + r; 
	return accountBalance;
});


//公司类型判断
Handlebars.registerHelper("transCompanyType",function(companyType){
	switch(companyType)
	{
		case 1:
		return "个人独资企业";
		break;
		case 2:
		return "合伙企业";
		break;
		case 3:
		return "公司企业";
		break;
		case 4:
		return "个体工商户";
		break;
		default:
		return "";
	}
})

//--------------------------------S 我的投资---------------------------------------
//标题
Handlebars.registerHelper("investBorrowStyleFun",function(borrowStyle){
	if(borrowStyle==1){
		return "等额本息";
	}else if(borrowStyle==2){
		return "一次性还款付息";
	}else if(borrowStyle==3){
		return "每月付息，到期还本";
	}else{
		return "-";
	}
})

//还款方式target=1 告诉程序打开新窗口
Handlebars.registerHelper("investBorrowNameFun",function(borrowName,borrowId){
	if(borrowName.length > 12){
		return new Handlebars.SafeString("<a href='/invest/detail.html?target=1&id="+borrowId+"'title="+borrowName+" target='_blank'>"+borrowName.substring(0,12)+"…</a>");
	}else{
		return new Handlebars.SafeString("<a href='/invest/detail.html?target=1&id=" + borrowId + "' title=" + borrowName + " target='_blank'>" + borrowName + "</a>");
	}
})

//时间
Handlebars.registerHelper("investTimeFun",function(time){
	function formatDate(now) { 
		var year=now.getFullYear();
		var month=now.getMonth()+1; 
		var date=now.getDate(); 
		return year+"-"+month+"-"+date; 
	} 
	if(time==null ||time==''){
		return '未复审';
	}
	var d = new Date(time);
	return formatDate(d);

})
//协议下载
Handlebars.registerHelper("investProtocolDownFun",function(time){
	if(status == 1 && scales == 100){ 
		return '<a href="/member/invest/tenderProtocol.html?tenderId='+id+'" class="download">下载协议</a>|';
		
	}
})
//--------------------------------E 我的投资---------------------------------------

//投资详情借款期限
Handlebars.registerHelper("borrowLimitTime",function(borrowTimeType,timeLimit){
	switch(borrowTimeType)
	{
		case 0:
		return new Handlebars.SafeString('<b class="c315A8B f24">'+timeLimit+'<em>个月</em></b>');
		break;
		case 1:
		return new Handlebars.SafeString('<b class="c315A8B f24">'+timeLimit+'<em>天</em></b>');
		break;
		default:
		return "";
	}
	
})

//投资详情标类型
Handlebars.registerHelper("borrowDetailsTypeName",function(type){
	switch (type) {
	case 101:
		return new Handlebars.SafeString('<i class="icon bid_type_icon bid_type_second float_left mt5"></i>秒还标');
		break;
	case 102:
		return new Handlebars.SafeString('<i class="icon bid_type_icon bid_type_credit float_left mt5"></i>信用标');
		break;
	case 103:
		return new Handlebars.SafeString('<i class="icon bid_type_icon bid_type_pledge float_left mt5"></i>固定收益类产品');
		break;
	case 104:
		return new Handlebars.SafeString('<i class="icon bid_type_icon bid_type_net float_left mt5"></i>净值标');
		break;
	case 110:
		return new Handlebars.SafeString('<i class="icon bid_type_icon bid_type_flow float_left mt5"></i>流转标');
		break;
	case 112:
		return new Handlebars.SafeString('<i class="icon bid_type_icon bid_type_guarantee float_left mt5"></i>担保标');
		break;
	case 119:
		return new Handlebars.SafeString('<i class="icon bid_type_icon bid_type_estate float_left mt5"></i>海外投资产品');
		break;
	case 122:
		return new Handlebars.SafeString('<i class="icon bid_type_icon bid_type_entrust float_left mt5"></i>浮动收益类产品');
		break;
	default:
		break;
	}
})

//投资详情 项目描述
Handlebars.registerHelper("borrowDetailContent",function(content){
	if(content == "")
	{
		return "暂无项目描述"
	}
	else
	{
		return new Handlebars.SafeString(content);
	}
});
//投资详情 还款来源

//投资详情 资金用途
Handlebars.registerHelper("borrowDetailBorrowUse",function(borrowUse){
	switch(borrowUse)
	{
		case "1":
		return "短期周转";
		break;
		case "2":
		return "生意周转";
		break;
		case "3":
		return "生活周转";
		break;
		case "4":
		return "购物消费";
		break;
		case "5":
		return "不提现借款";
		break;
		case "6":
		return "其它借款";
		break;
		case "7":
		return "创业借款";
		break;
		default:
		return "暂无资金用途";

	}
});

//序号增加
Handlebars.registerHelper("addOne",function(index){
         this._index = index+1;
         return this._index;
});

//抵押价格比
Handlebars.registerHelper("overPercentWidth",function(account,totalAssessPrice){
	return parseInt(parseInt(account)*100/parseInt(totalAssessPrice));
})

//投资详情-实物照判断-档案照判断-担保函判断
Handlebars.registerHelper("borrowUploadsType",function(v1,v2,options){
   if(v1 == v2){
     //满足添加继续执行
     return options.fn(this);
   }else{
     //不满足条件执行{{else}}部分
     return options.inverse(this);
  }
});

//判断相等
Handlebars.registerHelper("equal",function(v1,v2,options){
   if(v1 == v2){
     //满足添加继续执行
     return options.fn(this);
   }else{
     //不满足条件执行{{else}}部分
     return options.inverse(this);
  }
});

Handlebars.registerHelper("morethan",function(account1,account2,options){
   if(v1 > v2){
     //满足添加继续执行
     return options.fn(this);
   }else{
     //不满足条件执行{{else}}部分
     return options.inverse(this);
  }
});


//投资详情-入库出库判断
Handlebars.registerHelper("mortgage_update",function(v1,v2,options){
   if(v1 != v2){
     //满足添加继续执行
     return options.fn(this);
   }else{
     //不满足条件执行{{else}}部分
     return options.inverse(this);
  }
});

//判断不相等
Handlebars.registerHelper("inequality",function(v1,v2,options){
   if(v1 != v2){
     //满足添加继续执行
     return options.fn(this);
   }else{
     //不满足条件执行{{else}}部分
     return options.inverse(this);
  }
});

Handlebars.registerHelper('borrowTypeval', function(conditional, options) {
  if(conditional == 119) {
    return options.fn(this);
  }
  else{
	     //不满足条件执行{{else}}部分
	     return options.inverse(this);
	  }
});

Handlebars.registerHelper('usertype1', function(conditional, options) {
	  if(conditional == 119) {
	          return options.fn(this);
	  }
	  else{
		     //不满足条件执行{{else}}部分
		     return options.inverse(this);
		  }
	});


Handlebars.registerHelper('userTypeval', function(conditional, options) {
  if((conditional == 2)||(conditional == 4)) {
    return options.fn(this);
  }
  else{
	     //不满足条件执行{{else}}部分
	     return options.inverse(this);
	  }
});

//判断逾期借款时间
Handlebars.registerHelper("less",function(v1,options){
	var time = parseInt((new Date()).valueOf());
	    v1   = parseInt(v1);
   if(v1 < time){
     //满足添加继续执行
     return options.fn(this);
   }else{
     //不满足条件执行{{else}}部分 
     return options.inverse(this);
  }
});


//判断逾期借款时间
Handlebars.registerHelper("less2",function(v1,options){
	var time = parseInt((new Date()).valueOf());
	    v1   = parseInt(v1);
   if(v1 < time){
     //满足添加继续执行
     return options.fn(this);
   }else{
     //不满足条件执行{{else}}部分 
     return options.inverse(this);
  }
});

//判断逾期借款时间
Handlebars.registerHelper("less3",function(v1,options){
	var time = parseInt((new Date()).valueOf());
	    v1   = parseInt(v1);
   if(v1 < time){
     //满足添加继续执行
     return options.fn(this);
    
   }else{
     //不满足条件执行{{else}}部分
        var sys_second =(v1-time)/1000;
		var timer = setInterval(function(){
	        if (sys_second > 1) {
	          sys_second -= 1;
	          var day = Math.floor((sys_second / 3600) / 24);
	          var hour = Math.floor((sys_second / 3600) % 24);
	          var minute = Math.floor((sys_second / 60) % 60);
	          var second = Math.floor(sys_second % 60);
	          var showDay = day;
	          var showHour = hour<10?"0"+hour:hour;//计算小时
	          var showMinute = minute<10?"0"+minute:minute;//计算分钟
	          var showSecond = second<10?"0"+second:second;//计算秒杀
	          $("#djs").html('剩余时间：'+showDay+'天'+showHour+'时'+showMinute+'分'+showSecond+'秒')
	        } else {
	        	 location.reload();
	        }
	      }, 1000);
     return options.inverse(this);
  }
});


//判断逾期借款时间
Handlebars.registerHelper("less1",function(v1,options){
	var time = parseInt((new Date()).valueOf());
	    v1   = parseInt(v1);
   if(v1 < time){
     //满足添加继续执行
     return options.fn(this);
    
   }else{
     //不满足条件执行{{else}}部分
        var sys_second =(v1-time)/1000;
		var timer = setInterval(function(){
	        if (sys_second > 1) {
	          sys_second -= 1;
	          var day = Math.floor((sys_second / 3600) / 24);
	          var hour = Math.floor((sys_second / 3600) % 24);
	          var minute = Math.floor((sys_second / 60) % 60);
	          var second = Math.floor(sys_second % 60);
	          var showDay = day;
	          var showHour = hour<10?"0"+hour:hour;//计算小时
	          var showMinute = minute<10?"0"+minute:minute;//计算分钟
	          var showSecond = second<10?"0"+second:second;//计算秒杀
	          $("#djs").html('剩余时间：'+showDay+'天'+showHour+'时'+showMinute+'分'+showSecond+'秒')
	        } else {
	        	 location.reload();
	        }
	      }, 1000);
     return options.inverse(this);
  }
});

//投资详情-最小投资额度
Handlebars.registerHelper("bidLimit",function(value){
	if(value == 0)
	{
		return "无限制"
	}
	else
	{
		n = 2; 
		value = parseFloat((value + "").replace(/[^\d\.-]/g, "")).toFixed(n) + ""; 
		var l = value.split(".")[0].split("").reverse(), r = value.split(".")[1]; 
		t = ""; 
		for (i = 0; i < l.length; i++) { 
		t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : ""); 
		} 
		return  t.split("").reverse().join("");
	}
})

//投资详情-最大投资额度
Handlebars.registerHelper("bidMostLimit",function(value,borrowfee){
	if(value == 0)
	{
		return new Handlebars.SafeString("最多投标总额　<span>￥"+borrowfee+"</span>");
		
	}
	else
	{
		if(value>borrowfee)
		{
			return new Handlebars.SafeString("最多投标总额　<span>￥"+borrowfee+"</span>");
		}else 
		{
			return new Handlebars.SafeString("最多投标总额　<span>￥"+value+"</span>");
		}
	}
	
})

//投资详情-图片添加前缀
Handlebars.registerHelper("urlAdd",function(url,picPath){
		return url+picPath;
})

//更新资产包 时间格式转换
Handlebars.registerHelper("updateTime",function(time){
	time = new Date(time);
	var year = time.getFullYear();
	var month = time.getMonth()+1;
	var day = time.getDate();
	return new Handlebars.SafeString(year+'<br>'+month+'/'+day);
})

//更新资产包 超出比
Handlebars.registerHelper("beyondProportion",function(value1,value2){
	return parseInt((value2-value1)*100/value1);
})

//标题
Handlebars.registerHelper("mainRepaymentNameFun",function(name,id){
	if(name.length > 5){
		return "<a href='/member_borrow/borrow/repayment.html?borrowId="+id + "' title="+name+" target='_blank'>"+name.substring(0,5)+"…</a><u><div class='mark_details_l'><i></i>"+name+"</div></u>";
	}else{
		return "<a href='/member_borrow/borrow/repayment.html?borrowId=" + id +"' title=" + name + " target='_blank'>" + name + "</a>";
	}
})
Handlebars.registerHelper("mainremarkFun",function(remark){
	if(remark.length > 30){
		return remark.substring(0,300)+"…";
	}else{
		return remark ;
	}
})
Handlebars.registerHelper("mainremarkFunction",function(remark){
	if(remark.length > 15){
		return remark.substring(0,90)+"…";
	}else{
		return remark ;
	}
})

//账户中心投资详情
Handlebars.registerHelper("memberInvestBtn",function(value){
	if(value != 100)
	{
		return "立即投资";
	}
	else
	{
		return "查看详情";
	}
})


//账户中心还款按钮
Handlebars.registerHelper("borrowHref",function(value){
	var time = new Date(value);
	var year = time.getFullYear();
	var month = time.getMonth()+1;
	var day = time.getDate();
	if(month < 10)
		{
			month = '0' + month;
		}
	if(day < 10)
	{
		day = '0' + day;
	}
	return "/member_borrow/borrow/repayment.html?status=0&startTime="+year+"-"+month+"-"+day+"&endTime="+year+"-"+month+"-"+day;
})

//提现手续费判断  cashFeeBear  承担方： 1为平台  2为个人
Handlebars.registerHelper("cashFee",function(fee,cashFeeBear){
	if(cashFeeBear == 1)
	{
		return "0";
	}
	else if(cashFeeBear == 2)
	{
		return fee;
	}
})

//--------------------担保公司账户中----------------------------

//登记
Handlebars.registerHelper("guaranteeRegFun",function(id){
		return new Handlebars.SafeString("<a href='javascript:;' data-id='"+id+"'  class='guaranteeReg'>登记</a>");
});

//格式:2014-08-15
Handlebars.registerHelper("transGuaranteeDateFormat",function(value){
	function formatDate(now) { 
		var year=now.getFullYear();
		var month=now.getMonth()+1; 
		var date=now.getDate(); 
		var hour=now.getHours(); 
		var minute=now.getMinutes(); 
		var second=now.getSeconds(); 
		if(second < 10)
		{
			second = '0' + second;
		}
		return year+"-"+month+"-"+date; 
	} 
	if(value==null ||value==''){
		return '';
	}
	var d = new Date(value);
	return formatDate(d);
});

//操作
Handlebars.registerHelper("guaranteeOperateFun",function(borrowId){
	return new Handlebars.SafeString("<a href='/member_guarantee/repayment/repaymentDetail.html?borrowId=" + borrowId + "' target='_blank'>还款明细</a>");
})
//代偿操作
Handlebars.registerHelper("guaranteeCompensatoryOperateFun",function(id,status){
	if(status==0){
		return new Handlebars.SafeString("<a href='javascript:;' data-id='"+id+"'  class='toCompensateBtn'>代偿</a>");
	}else{
		return "-";
	}
})
//还款状态
Handlebars.registerHelper("guaranteeRepaymentStatusFun",function(status){
	if(status==0){
		return "未还";
	}else if(status==1||status==2){
		return  "已还";
	}else if(status==3){
		return "还款处理中";
	}else if(status==4){
		return "代偿处理中";
	}
})
//剩余天数
Handlebars.registerHelper("guaranteeRepaymentLateDaysFun",function(lateDays){
	return -lateDays;
})

//用户红包
Handlebars.registerHelper("redpacketStatus",function(status){
	switch (status)
	{
		case 1:
			return "已使用";
			break;
		case 2:
			return "未使用";
			break;
		case -1:
			return "已过期";
			break;
	}
})

//用户红包类型
Handlebars.registerHelper("redpacketType",function(redPacketType){
	switch (redPacketType)
	{
		case 1:
			return "现金红包";
			break;
		case 2:
			return "虚拟红包";
			break;
	}
})

//邀请好友
Handlebars.registerHelper("userInviteGift",function(status){
	if(status)
	{
		return "已派送";
	}
	else
	{
		return "未派送";
	}
})

//--S--投资详情借款人信息转换
//婚姻状况
Handlebars.registerHelper("transMaritalStatus",function(value){
	switch(value)
	{
		case 0:
			return "未婚";
			break;
		case 1:
			return "已婚";
			break;
		case 2:
			return "离异";
			break;
		case 3:
			return "丧偶";
			break;
		default:
			return "";
			break;
	}
});
//收入范围
Handlebars.registerHelper("transMonthIncomeRange",function(value){
	switch(value)
	{
		case 1:
			return "3000元以下";
			break;
		case 2:
			return "3000-5000元";
			break;
		case 3:
			return "5000-8000元";
			break;
		case 4:
			return "8000-10000元";
			break;
		case 5:
			return "10000-13000元";
			break;
		case 6:
			return "13000-15000元";
			break;
		case 7:
			return "15000-20000元";
			break;
		case 8:
			return "20000元以上";
			break;
		default:
			return "";
			break;
	}
});

//年龄
Handlebars.registerHelper("transBirthday",function(value){
	return (new Date()).getFullYear()-(new Date(value)).getFullYear()+"岁";
});

//车产和房产
Handlebars.registerHelper("transValue",function(value){
	switch(value)
	{
		case 0:
			return "无";
			break;
		case 1:
			return "有";
			break;
		default:
			return "";
			break;
	}
});

//学历
Handlebars.registerHelper("transEducation",function(value){
	switch(value)
	{
		case 1:
			return "小学";
			break;
		case 2:
			return "初中";
			break;
		case 3:
			return "高中";
			break;
		case 4:
			return "中专";
			break;
		case 5:
			return "大专";
			break;
		case 6:
			return "本科";
			break;
		case 7:
			return "硕士";
			break;
		case 8:
			return "博士";
			break;
		case 9:
			return "其他";
			break;
		default:
			return "";
			break;
	}
});

//工作时间
Handlebars.registerHelper("transWorkExperience",function(value){
	switch(value)
	{
		case 1:
			return "0-3年";
			break;
		case 2:
			return "3-5年";
			break;
		case 3:
			return "5-8年";
			break;
		case 4:
			return "8年以上";
			break;
		default:
			return "";
			break;
	}
});

//--E--投资详情借款人信息转换

//果果币判断
Handlebars.registerHelper("transuserCoin",function(value){
	if(value=="")
	{
		value = 0;
	}
	return value;
});

//抵用券判断
Handlebars.registerHelper("transborrowVoucherUserMoney",function(value){
	if(value=="")
	{
		value = 0;
	}
	return value;
});


//加息券判断
Handlebars.registerHelper("transVoucherMoney",function(value){
	if(value=="")
	{
		value = 0;
	}
	return value;
});
//--E--投资详情借款人信息转换
Handlebars.registerHelper("titleTransFun",function(value){
	if(value==1){
		return "投资中冻结金额 + 预约中冻结金额 + 提现中冻结金额+VIP管理费冻结金额";
	}else{
		return "投资中冻结金额 + 提现中冻结金额+VIP管理费冻结金额";
	}
});

//转让价格
Handlebars.registerHelper("transferVal",function(bondMoney,bondApr){
	var transfer = bondMoney-bondMoney*bondApr/100;
	if(transfer == 0)
	{
		return transfer+".00";
	}
	else
	{
		n = 2;
		transfer = parseFloat((transfer + "").replace(/[^\d\.-]/g, "")).toFixed(n) + ""; 
		var l = transfer.split(".")[0].split("").reverse(), r = transfer.split(".")[1]; 
		t = ""; 
		for (i = 0; i < l.length; i++) { 
			t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : ""); 
		} 
		return  t.split("").reverse().join("") + "." + r;
	}

})

//债权转让进度
Handlebars.registerHelper("progress",function(soldCapital,bondMoney){
	return Math.floor(soldCapital/bondMoney*100);
})

//赎回金额
Handlebars.registerHelper("redemption",function(bondMoney,bondApr,payInterest,manageFee){
	var redemptionVal =bondMoney *(1 - bondApr/100) + payInterest - manageFee;
	if(redemptionVal == 0)
	{
		return redemptionVal+".00";
	}
	else
	{
		n = 2;
		redemptionVal = parseFloat((redemptionVal + "").replace(/[^\d\.-]/g, "")).toFixed(n) + ""; 
		var l = redemptionVal.split(".")[0].split("").reverse(), r = redemptionVal.split(".")[1]; 
		t = ""; 
		for (i = 0; i < l.length; i++) { 
			t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : ""); 
		} 
		return  t.split("").reverse().join("") + "." + r;
	}
})

//剩余债权
Handlebars.registerHelper("transRemainMoney",function(bondMoney,soldCapital){
	var money = bondMoney - soldCapital;
	if(money == 0)
	{
		return money+".00";
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
		return  t.split("").reverse().join("") + "." + r;
	}
})

//债权专区按钮状态
Handlebars.registerHelper("transBondListBtn",function(id,bondMoney,soldCapital){
	if(bondMoney == soldCapital)
	{
		return ' <a href="/bond/detail.html?id='+id+'" class="borderBtnSucess bondBtn">转让完成</a>'
	}
	else
	{
		return ' <a href="/bond/detail.html?id='+id+'" class="borderBtn bondBtn">立即受让</a>'
	}
	
});

//债权专区完成状态
Handlebars.registerHelper("transBondStatus",function(bondMoney,soldCapital){
	if(bondMoney == soldCapital)
	{
		return '<dd class="status statusComplete">已转让</dd>';
	}
	else
	{
		return '<dd class="status">转让中</dd>';
	}
});

//金额减法
Handlebars.registerHelper("transMoneySub",function(value1,value2){
	var money = value1 - value2;
	if(money == 0)
	{
		return money+".00";
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
		return  t.split("").reverse().join("") + "." + r;
	}
})

//协议预览
Handlebars.registerHelper("protocolPreview",function(status){
	if(status == 1 || status == 3 || status == 6 || status == 7 || status == 8){ //添加判断是否满标复审通过
		return '<a href="javascript:;" class="protocolPreview">[协议预览]</a>';
	}else{
		return "";
	}
})

//最新发布时间
Handlebars.registerHelper("countTimes",function(addTime,serverTime){
	var time= parseInt(serverTime)-parseInt(addTime);
	var d = Math.ceil(time/1000/24/60/60);
	var h = Math.ceil(time/1000/60/60);
	var m = Math.ceil(time/1000/60);
	var s = Math.ceil(time/1000);
	if(d > 1)
	{
		return d+"天前";
	}
	else if(h > 1)
	{
		return h+"小时前";
	}
	else if(m > 1)
	{
		return m+"分钟前";
	}
	else if(s > 1)
	{
		return s+"秒前";
	}
})

//文章列表图片显示
Handlebars.registerHelper("articleImage",function(value,url){
	if(value == '' || typeof(value) == 'undefined' || value== 'undefined'){
		return '/themes/theme_default/images/invest-list-img.jpg';
	}else{
		return url+value;
	}
});
