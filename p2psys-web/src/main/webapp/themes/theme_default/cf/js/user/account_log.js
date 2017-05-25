$(function(){
	$.ajax({
		url:"/cf/user/accountLog.html",
		type:"POST",
		dataType:"json",
		success:function(res){
			var htmlStr = "";
			if("success"==res.result){
				if(null!=res.errorMsg){
					for(var i = 0;i<res.errorMsg.length;i++){
						htmlStr += "<tr>";
						htmlStr += "<td>";
						htmlStr += getType(res.errorMsg[i].type);
						htmlStr += "</td>";
						htmlStr += "<td>";
						htmlStr += res.errorMsg[i].money;
						htmlStr += "</td>";
						htmlStr += "<td>";
						htmlStr += jsDateTimeDate(res.errorMsg[i].addTime);
						htmlStr += "</td>";
						htmlStr += "<td>";
						htmlStr += res.errorMsg[i].remark;
						htmlStr += "</td>";
						htmlStr += "</tr>";
					}
				}
			}
			$("#dataDiv").html(htmlStr);
		}
	})
})

/**
 * 资金动向
 * @param type
 * @returns {String}
 */
function getType(type){
	if("invest"==type){
		return "投资相关";
	}else if("cash_success"==type){
		return "提现成功"
	}else if("cash_frost"==type){
		return "提现冻结";
	}else if("off_recharge"==type){
		return "线下充值";
	}else if("online_recharge"==type){
		return "线上充值";
	}else{
		return "";
	}
}

/**
 * 时间戳转换标准时间
 * @param unixtime
 * @returns
 */
function jsDateTimeDate(unixtime)  
{  
	 var date = new Date(unixtime);
	 
	 return  date.format("yyyy-MM-dd"); 
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