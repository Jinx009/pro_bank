var realNameStatus = 0;
var moenyRule = 0;
var addRule = 0;
var minMoney = 0;

$(function(){
	$("#dialog-box").hide();
	var type = $("#type").val();
	var id = $("#id").val();
	var params = "id="+id;
	realNameStatus = $("#realNameStatus").val();
	
	$.ajax({
		url:"/p/detail.html?id="+id,
		type:"POST",
		data:params,
		dataType:"json",
		success:function(res){
			var projectImg = "/themes/theme_default/cf/img/product_img_big.png";
			var companyLogo = "";
			var profit = "";
			var detailLogo = "/themes/theme_default/cf/img/logo.png";
			var htmlStr  = "";
			
			moneyRule = 0;
			addRule = res.errorMsg.addAmount;
			minMoney = res.errorMsg.minMoney;
			
			if(null!=res.errorMsg.materialsList){
				for(var j = 0;j<res.errorMsg.materialsList.length;j++){
					if("project_detail"==res.errorMsg.materialsList[j].materialCode){
						projectImg = adminUrl+res.errorMsg.materialsList[j].materialContent;
					}
					if("company_logo"==res.errorMsg.materialsList[j].materialCode){
						companyLogo = adminUrl+res.errorMsg.materialsList[j].materialContent;
					}
					if("project_profit"==res.errorMsg.materialsList[j].materialCode){
						profit = res.errorMsg.materialsList[j].materialContent;
					}
					if("detail_img"==res.errorMsg.materialsList[j].materialCode){
						detailLogo = adminUrl+res.errorMsg.materialsList[j].materialContent;
					}
				}
			}
			$(".project-name").html(res.errorMsg.projectName);
			$("#projectName").html(res.errorMsg.projectName);
			$("#step").html(jsDateTimeOnly(res.errorMsg.startTime)+"至"+jsDateTimeOnly(res.errorMsg.endTime)+"<span>"+getRealStep(res.errorMsg)+"</span>");
			$("#detailLogo").attr("src",detailLogo);
			$("#info").html(res.errorMsg.info)
			$("#maxMoney").html(getMoney(res.errorMsg.wannaAccount));
			$("#minMoney").html(getMoney(res.errorMsg.minMoney));
			$("#account").html(getAccount(res.errorMsg.account));
			$("#stepStyle").css("width",getStepStyle(res.errorMsg.account,res.errorMsg.wannaAccount)+"%");
			$("#companyLogo").attr("src",companyLogo);
			htmlStr += "<p>发起人："+res.errorMsg.creater+"</p>";
			htmlStr += "<p>公司名称："+res.errorMsg.company+"</p>";
			htmlStr += "<p>公司地址："+res.errorMsg.address+"</p>";
			$("#company").html(htmlStr);
			getOrderList(id);
			$("#profit").html(profit);
			$("#detailImg").attr("src",projectImg);
			if(null!=res.errorMsg.leader){
				htmlStr = "";
				htmlStr += "<p>领投人："+res.errorMsg.leader.name+"</p>";
				htmlStr += "<p>个人简介；"+res.errorMsg.leader.info+"</p>";
				htmlStr += "<p>领投理由："+res.errorMsg.leader.reason+"</p>";
				$("#leaderDiv").html(htmlStr);
			}
		}
	})
	
})

/**
 * 设置交易密码页面
 */
function setPayPwd(){
	var id= $("#id").val();
	location.href = "/cf/user/set-pay.html?redirectUrl=/pro/detail.html?id="+id;
}

/**
 * 购买
 */
function doBuy(){
	var payPwd = $("#payPwd").val();
	var investMoney = $("#investMoney").val();
	var id= $("#id").val();
	
	var params = "payPwd="+payPwd+"&investMoney="+investMoney+"&id="+id;
	
	if(!checkMoney(investMoney)){
		$("#buyError").html(error_msg);
	}else{
		$.ajax({
			url:"/cf/user/buy.action",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res){
				if("success"==res.result){
					 hideAlert();
					$("#dialog-box-success").show();
				}else{
					$("#buyError").html(res.errorMsg);
				}
			}
		})
	}
}
var error_msg = "";
/**
 * 校验输入金额
 * @param investMoney
 * @returns {Boolean}
 */
function checkMoney(investMoney){
    var exp = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
	    
	var moneyStatus = false;
	if(""!=investMoney&&null!=investMoney){
		if(exp.test(investMoney)){
			if("0"!=moneyRule){
				var array = moneyRule.split(",");
				for(var i = 0;i<array.length;i++){
					if(""!=array[i]&&null!=array[i]){
						if(parseFloat(array[i]).toFixed(2)==parseFloat(investMoney).toFixed(2)){
							moneyStatus = true;
						}
					}
				}
				if(!moneyStatus){
					error_msg = "跟投金额不符合金额规则!";
					return false;
				}
			}else{
				if("0"!=addRule){
					if(parseFloat(investMoney)<parseFloat(minMoney)){
						error_msg = "跟投金额小于最低跟头金额!";
						return false;
					}else if(0!=(parseFloat(investMoney)-parseFloat(minMoney))%parseFloat(addRule)){
						error_msg = "跟投金额不满足增加规则!";
						return false;
					}
				}
			}
		}else{
			error_msg = "输入金额不合法!";
			return false;
		}
	}else{
		error_msg = "跟投金额不能为空!";
		return false;
	}
	return true;
}

/**
 * 购买成功
 */
function buySuccess(){
	location.href = "/cf/user/buy-list.html";
}

/**
 * 显示购买输入
 */
function showBuy(){
	var payStatus = $("#payStatus").val();
	if(null!=payStatus&&""!=payStatus){
		if("0"!=moneyRule){
			$("#moneyRule").html("（跟投金额必须是"+moneyRule+"中其一。）");
		}else{
			if("0"!=addRule){
				$("#moneyRule").html("（跟投金额增加额度必须是"+formatCurrency(addRule)+"的倍数。）");
			}else{
				$("#moneyRule").html("");
			}
		}
		$("#dialog-box-input").show();
	}else{
		$("#dialog-box-pwd").show();
	}
}

/**
 * 隐藏弹窗
 */
function hideAlert(){
	$("#dialog-box-input").hide();
	$("#dialog-box-pwd").hide();
	$("#dialog-box-success").hide();
	$("#dialog-box-leader").hide();
	$("#dialog-box-leader-success").hide();
}

/**
 * 申请领投人
 */
function showLeader(){
	hideAlert();
	$("#dialog-box-leader").show();
}

/**
 * 提交领头人信息
 */
function doLeader(){
	var name = $("#name").val();
	var info = $("#leader_info").val();
	var reason = $("#reason").val();
	var id = $("#id").val();
	
	var params = "name="+name+"&info="+info+"&reason="+reason+"&id="+id;
	
	$.ajax({
		url:"/cf/user/beLeader.html",
		data:params,
		type:"POST",
		dataType:"json",
		success:function(res){
			if("success"==res.result){
				hideAlert();
				$("#dialog-box-leader-success").show();
			}else{
				$("#leader_error").html(res.errorMsg);
				hideAlert();
				$("#dialog-box-leader-success").show();
			}
		}
	})
}


/**
 * 打开页面
 * @param url
 */
function openUrl(url){
	location.href = url;
}
/**
 * 标签切换
 * @param index
 */
function showDiv(index){
	for(var i = 1;i<3;i++){
		$("#detailli"+i).removeClass("active");
		$("#detailIndex"+i).css("display","none");
	}
	$("#detailli"+index).addClass("active");
	$("#detailIndex"+index).css("display","block");
}
/**
 * 获取购买记录
 * @param id
 */
function getOrderList(id){
	var params = "id="+id;
	$.ajax({
		url:"/pro/buyList.action",
		type:"POST",
		data:params,
		dataType:"json",
		success:function(res){
			if(res.errorMsg){
				var htmlStr = "<table class='table detail-table' >";
				htmlStr += "<tr><th>用户名</th><th>跟投额度</th><th>购买日期</th></tr>";
				for(var i = 0;i<res.errorMsg.length;i++){
					htmlStr += "<tr>";
					htmlStr += "<td>"+res.errorMsg[i].userName+"</td>";
					htmlStr += "<td>"+res.errorMsg[i].buyMoney+"元</td>";
					htmlStr += "<td>"+jsDateTimeDate(res.errorMsg[i].addTime)+"</td></tr>";
				}
				htmlStr += "</table><div class='space-4' ></div>";
				$("#detailIndex2").html(htmlStr);
			}
		}
	})
}
/**
 * 投资进度条
 */
function getStepStyle(account,money){
	var step = parseFloat(account)/parseFloat(money)*100;
	if(step>=100){
		return 100;
	}
	return step;
}
/**
 * 投资金额格式化
 */
function getAccount(account){
	account = parseFloat(account);
	if(account>10000){
		return parseFloat(account/10000).toFixed(0)+"万";;
	}else{
		return account+"元";
	}
}
/**
 * 金钱格式化
 */
function getMoney(money){
	money = parseFloat(money);
	if(money>=10000){
		return parseFloat(money/10000).toFixed(0)+"万";
	}else{
		return money+"元";
	}
}
/**
 * 项目进度
 */
function getRealStep(object){
	var step = "紧张众筹中";
	if("1"==object.timeStatus){
		 step = "奋力预热中";
	}else if("2"==object.timeStatus){
		 step = "紧张众筹中";
	}else if("3"==object.timeStatus){
		 step = "遗憾过期中";
	}else{
		 step = "顺利已完成";
	}
	return step;
}

/**
 * 时间戳转换标准时间
 * @param unixtime
 * @returns
 */
function jsDateTimeOnly(unixtime)  
{  
	 var date = new Date(unixtime);
	 
	 return  date.format("yyyy-MM-dd hh:mm:ss"); 
} 
/**
 * 金钱格式化
 * @param num
 * @returns {String}
 */
function formatCurrency(num) {  
    num = num.toString().replace(/\$|\,/g,'');  
    if(isNaN(num))  
        num = "0";  
    sign = (num == (num = Math.abs(num)));  
    num = Math.floor(num*100+0.50000000001);  
    cents = num%100;  
    num = Math.floor(num/100).toString();  
    if(cents<10)  
    cents = "0" + cents;  
    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)  
    num = num.substring(0,num.length-(4*i+3))+','+  
    num.substring(num.length-(4*i+3));  
    return (((sign)?'':'-') + num + '.' + cents);  
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