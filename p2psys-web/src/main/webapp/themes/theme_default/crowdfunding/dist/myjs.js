function getProjectInfo()
{
	$.ajax({
		url:"/crowdfunding/getProjectInfo.html?project_id="+$("#project_id").val(),
		type:"GET",
		dataType:"json",
		success:function(res)
		{
			var htmlStr = "";
			
			console.log()
			
			htmlStr += "<tr><th>项目ID</th><td>"+res.data.id+"</td><th>项目名称</th><td>"+res.data.projectName+"</td><th>股权转让比例</th><td>"+res.data.stakeShare+"</td></tr>";
			htmlStr += "<tr><th>众筹类型</th>";
			if("0"==res.data.type)
			{
				htmlStr += "<td>实物众筹</td><th>标状态</th>";
			}
			if("1"==res.data.type)
			{
				htmlStr += "<td>股权众筹</td><th>标状态</th>";
			}
			if("2"==res.data.type)
			{
				htmlStr += "<td>权益众筹</td><th>标状态</th>";
			}
			//众筹标状态。 0. 未提交审核； 1. 提交等待审核  2. 审核通过 3.审核未通过 4. 手动截标 5.项目正在进行6.项目结束-1.项目撤回
			if("0"==res.data.status) 
			{
				htmlStr += "<td>未提交审核</td><th>是否接受超募</th>";
			}
			if("1"==res.data.status) 
			{
				htmlStr += "<td>等待审核</td><th>是否接受超募</th>";
			}
			if("2"==res.data.status) 
			{
				htmlStr += "<td>审核通过</td><th>是否接受超募</th>";
			}
			if("3"==res.data.status) 
			{
				htmlStr += "<td>审核未通过</td><th>是否接受超募</th>";
			}
			if("4"==res.data.status) 
			{
				htmlStr += "<td>手动截标</td><th>是否接受超募</th>";
			}
			if("5"==res.data.status) 
			{
				htmlStr += "<td>项目正在进行</td><th>是否接受超募</th>";
			}
			if("6"==res.data.status) 
			{
				htmlStr += "<td>项目结束</td><th>是否接受超募</th>";
			}
			if("-1"==res.data.status) 
			{
				htmlStr += "<td>项目撤回</td><th>是否接受超募</th>";
			}
			if("0"==res.data.planedIsExceedAccept) 
			{
				htmlStr += "<td>不接受</td></tr>";
			}
			if("1"==res.data.planedIsExceedAccept) 
			{
				htmlStr += "<td>接受</td></tr>";
			}
			
			htmlStr += "<tr><th>最小接受金额</th><td>"+res.data.planedMinAmount+"</td><th>最大接受金额</th><td>"+res.data.planedMaxAmount+"</td><th>最小接受人数</th><td>"+res.data.planedMinInvestor+"</td></tr>";
			htmlStr += "<tr><th>最大接受人数</th><td>"+res.data.planedMaxInvestor+"</td><th>项目名称</th><td>"+res.data.projectName+"</td>";
			
			if("0"==res.data.planedIsExceedAcceptInvestor)
			{
				htmlStr += "<th>超过人数是否接受</th><td>不接受</td></tr>";
			}
			if("1"==res.data.planedIsExceedAcceptInvestor)
			{
				htmlStr += "<th>超过人数是否接受</th><td>接受</td></tr>";
			}
			
			htmlStr += "<tr><th>推荐级别</th><td>"+res.data.isRecommend+"</td><th>开始时间</th><td>"+getLocalTime(res.data.startTime,1)+"</td><th>结束时间</th><td>"+getLocalTime(res.data.endTime,1)+"</td></tr>";
			htmlStr += "<tr><th>权益到期日</th><td>"+getLocalTime(res.data.rightDuetime,1)+"</td><th>领投人总提成</th><td>"+res.data.leaderIncomeRate+"</td><th>管理费总提成</th><td></td></tr>";
			htmlStr += "<tr><th>定向标准投密码</th><td>"+res.data.acceptanceCode+"</td><th>添加时间</th><td>"+getLocalTime(res.data.addTime,1)+"</td><th>添加IP</th><td>"+res.data.appIp+"</td></tr>";
			if("0"==res.data.stopIsProfit)
			{
				htmlStr += "<tr><th>截标是否计算收益</th><td colspan=5 >否</td></tr>";
			}
			if("1"==res.data.stopIsProfit)
			{
				htmlStr += "<tr><th>截标是否计算收益</th><td colspan=5 >是</td></tr>";
			}
			
			
			
			
			$("#project_info").append(htmlStr);
		}
	})	
}


var getLocalTime = function(value,type) 
{
    if (value == null || value == '') 
    {
        return '';
    }
	var dt;
	if (value instanceof Date) 
	{
	    dt = value;
	}
	else 
	{
	    dt = new Date(value);
	    if (isNaN(dt)) 
	    {
	        value = value.replace(/\/Date\((-?\d+)\)\//, '$1'); //将那个长字符串的日期值转换成正常的JS日期格式
	        dt = new Date();
	        dt.setTime(value);
	    }
	}
	 switch (type)
	 {
	 case 1:
		 return dt.format("yyyy年MM月dd日");   //这里用到一个javascript的Date类型的拓展方法
		 break;
	 case 2:
		 return dt.format("yyyy年MM月dd日 hh:mm:ss");   //这里用到一个javascript的Date类型的拓展方法
		 break;
	 case 3:
		 return dt.format("yyyy-MM-dd");   //这里用到一个javascript的Date类型的拓展方法
		 break;
	 case 4:
		 return dt.format("yyyy-MM-dd hh:mm:ss");   //这里用到一个javascript的Date类型的拓展方法
		 break;
	 }
}
Date.prototype.format = function (format) 
{
    var o = 
    {
        "M+": this.getMonth() + 1, //month 
        "d+": this.getDate(),    //day 
        "h+": this.getHours(),   //hour 
        "m+": this.getMinutes(), //minute 
        "s+": this.getSeconds(), //second 
        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter 
        "S": this.getMilliseconds() //millisecond 
    }
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
    (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) if (new RegExp("(" + k + ")").test(format))
        format = format.replace(RegExp.$1,
      RegExp.$1.length == 1 ? o[k] :
        ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}
