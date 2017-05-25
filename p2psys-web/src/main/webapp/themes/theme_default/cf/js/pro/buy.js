$(function(){
	var id = $("#id").val();
	var payStatus = $("#payStatus").val();
	var profit = $("#profit").val();
	if(null==payStatus||""==payStatus){
		location.href = "/cf/user/set-pay.html?redirectUrl=/cf/user/buy.html?id="+id+"%26profit="+profit;
	}
	var type = $("#type").val();
	checkType(type);
	$("#minMoney").html("&yen;"+formatCurrency($("#minMoney").html()));
	getData(id);
	
	
	// add By False
	if(type == 2){
		$(".fame").html("由于认购股权众筹项目作为一种直接投资方式...>>")
		$(".contents").html("由于认购股权众筹项目作为一种直接投资方式，800众服平台（“平台”）及项目发起方或领投人等，均无法保证您的本金及收益，在投资过程中可能会面临多种风险因素。因此，根据有关法律法规规定的要求，在您选择参与股权众筹项目前，请仔细阅读以下《风险提示书》。")
	}else{
		$(".fame").html("法律声明：以上商品（或产品）标识、描述、图片...>>")
		$(".contents").html("法律声明：以上商品（或产品）标识、描述、图片及宣传内容等均由项目方提供，其真实性、准确性和合法性由信息提供者（项目方）负责，本站不提供任何保证。项目方应严格按照中国境内相关法律法规的规定提供商品（或产品）的内容及服务。投资者如遇任何商品（或产品）内容、质量及配送等问题可向项目方进行咨询。")
	}
	$(".fame").click(function(){
		$(".fame").hide();
		$(".contents").slideDown();
	})
	$(".contents").click(function(){
		$(".fame").slideDown();
		$(".contents").hide();
	})
})

/**
 * 选择框
 * @param type
 */
function checkType(type){
	if("2"==type){
		$("#checkDiv1").show();
		$("#profitNumDiv").show();
	}else{
		$("#checkDiv1").hide();
		$("#check1").prop("checked",false);
		$("#check2").prop("checked","checked");
		$("#check2").attr("onclick","return false;");
		$("#profitNumDiv").hide();
	} 
}

/**
 * checkBox切换事件
 * @param index
 */
function doCheck(index){
	if("2"==index){
		$("#check1").prop("checked",false);
		$("#check2").prop("checked","checked");
		$("#heheDiv").hide();
		checkResult = 2;
	}else{
		$("#check2").prop("checked",false);
		$("#check1").prop("checked","checked");
		$("#heheDiv").show();
		checkResult = 1;
	}
	var type = $("#type").val();
	if("2"==type){
		getProfitNum();
		getPayMoney();
	}
}

var profitList = "";
var profitId = "";
var money = 0;
var checkResult = 2;
var projectInfo;
var payMoney = 0;
/**
 * 获取收益信息
 * @param id
 */
function getData(id){
	var params = "id="+id;
	$.ajax({
		url:"/p/detail.html?id="+id,
		type:"POST",
		data:params,
		dataType:"json",
		success:function(res){
			projectInfo = res.errorMsg;
			profitList = res.errorMsg.profitRuleList;
			if(null!=res.errorMsg.materialsList){
				for(var j = 0;j<res.errorMsg.materialsList.length;j++){
					if("service_pdf"===res.errorMsg.materialsList[j].materialCode){//项目动态图片素材
						$('#hehehe').html('投资服务协议');
						$('#hehecheck').show();
						$('#hehehe').attr('onclick','openOpen("'+adminUrl+res.errorMsg.materialsList[j].materialContent+'")');
					}
				}
			}
			if(1==res.errorMsg.profitRuleList.length){
				profitId = res.errorMsg.profitRuleList[0].id;
				$("#buyNumDiv").hide();
				$("#money").attr("readonly",false);
				$("#selectMoneyDiv").hide();
				$("#profitInfo").val(res.errorMsg.profitRuleList[0].content);
				if("0"!=projectInfo.addAmount){
					$("#money").attr("readonly",true);
					$("#buyNumDiv").show();
					$("#buyNum").val("1");
					$("#money").val(projectInfo.minMoney);
				}else{
					$("#money").val(projectInfo.minMoney);
					$("#money").attr("oninput","changeMoney()");
				}
				getPayMoney();
				getProfitNum();
			}else{
				$("#minMoneyDiv").hide();
				$("#buyNumDiv").hide();
				var htmlStr = "";
				for(var i = 0;i<res.errorMsg.profitRuleList.length;i++){
					htmlStr += "<option value = "+i+"  >支持￥"+formatCurrency(res.errorMsg.profitRuleList[i].money)+"</option>";
				}
				$("#selectMoney").html(htmlStr);
				checkProfit();
			}
		}
	})
}

/**
 * 金钱转换
 */
function changeMoney(){
	getPayMoney();
	getProfitNum();
}

function openOpen(url){
	window.open(url);
}

/**
 * 判断权益
 */
function checkProfit(){
	var profit = $("#profit").val();
	var value = 0;
	for(var i = 0;i<profitList.length;i++){
		if(profit==profitList[i].id){
			value = i;
			profitId = profitList[i].id;
			$("#money").val(profitList[i].money);
			$("#profitInfo").val(profitList[i].content);
			money = profitList[i].money;
		}
	}
	var element = document.getElementById("selectMoney");   
 	for(i=0;i<element.length;i++){
      if(value==element.options[i].value){  
          element.options[i].selected=true; 
      }  
    }
	getPayMoney();
	getProfitNum();
}

/**
 * 增加份数
 */
function add(){
	var num = parseInt($("#buyNum").val());
	num++;
	$("#buyNum").val(num);
	var money = num*parseFloat(projectInfo.addAmount);
	$("#money").val(money);
	getPayMoney();
	getProfitNum();
}

/**
 * 减少份数
 */
function sub(){
	var num = parseInt($("#buyNum").val());
	if(1!=num){
		num--;
		$("#buyNum").val(num);
		var money = num*parseFloat(projectInfo.addAmount);
		$("#money").val(money);
		getPayMoney();
		getProfitNum();
	}
}

/**
 * 股权占比
 */
function getProfitNum(){
	var type = $("#type").val();
	if("2"==type){
		var money = $("#money").val();
		if(null==money||""==money){
			money = 0;
		}else{
			money = parseFloat(money);
		}
		var realCompanyMoney = parseFloat(projectInfo.companyMoney);
		var realPlandMoney = parseFloat(projectInfo.wannaAccount);
		var realAccount = parseFloat(projectInfo.account);
		var meetMoney = parseFloat(projectInfo.breach);
		var maxSum = 0;
		if((money+realAccount)>realPlandMoney){
			maxSum = realCompanyMoney+money+realAccount/1.05;
		}else{
			maxSum = realCompanyMoney+realPlandMoney/1.05;
		}
		var profit = money/maxSum*100;
		$("#profitNum").html("预计股权占比:"+parseFloat(profit).toFixed(2)+"%。");
		$("#hehe").html("预约需支付"+(meetMoney*100)+"%的保证金。"+
						"<span class='glyphicon glyphicon-question-sign' aria-hidden='true'data-toggle='tooltip' data-placement='right' title='保证金：投资人在投资股权众筹项目时，按一定比例缴付投资总额的一部分作为保证金预定该项目的投资认购权。在缴纳保证金后的三天内，若投资人自愿放弃认购权，则申请退还保证金，同时放弃该项目的认购权。'> </span>"); 
		$('[data-toggle="tooltip"]').tooltip();
	}
}

/**
 * 购买
 */
function doBuy(){
	var profit = $("#profit").val();
	var payPwd = $("#payPwd").val();
	var investMoney = $("#money").val();
	var id= $("#id").val();
	var investType = checkResult;
	var type = $("#type").val();
	var params = "payPwd="+payPwd+"&investMoney="+investMoney+"&id="+id+"&type="+investType+"&profitRule="+profit;
	if("2"==type){
		$.ajax({
			url:"/cf/user/order.action",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res){
				if("success"==res.result){
					layer.alert('恭喜您，投资成功！',{title:false,closeBtn: 0},function(){
						openUrl('/cf/user/buy-list.html');
					});
				}else{
					if("账户可用余额不足!"==res.errorMsg){
						layer.confirm('账户可用余额不足！', {
						    btn: ['去充值','确定'],title:false,closeBtn: 0 
						}, function(){
							openUrl('/cf/user/recharge.html');
						});
					}else{
						layer.alert(res.errorMsg,{title:false,closeBtn: 0});
					}
				}
			}
		})
	}else{
		var params = "payPwd="+payPwd+"&investMoney="+investMoney+"&id="+id+"&profitRule="+profit;
		$.ajax({
			url:"/cf/user/buyData.action",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res){
				if("success"==res.result){
					if("1"==type){
						layer.alert('恭喜您，投资成功！',{title:false,closeBtn: 0},function(){
							openUrl('/cf/user/order-address.html?id='+res.errorMsg.id+'');
						});
					}else{
						layer.alert('恭喜您，投资成功！',{title:false,closeBtn: 0},function(){
							openUrl('/cf/user/buy-list.html');
						});
					}
				}else{
					if("账户可用余额不足!"==res.errorMsg){
						layer.confirm('账户可用余额不足！', {
						    btn: ['去充值','确定'],title:false,closeBtn: 0
						}, function(){
							openUrl('/cf/user/recharge.html');
						});
					}else{
						layer.alert(res.errorMsg,{title:false,closeBtn: 0});
					}
				}
			}
		})
	}
}

/**
 * 返回
 */
function goBack(){
	var id = $("#id").val();
	location.href = "/pro/detail.html?id="+id;
}

var error_msg = "";
/**
 * 校验输入金额
 * @param investMoney
 * @returns {Boolean}
 */
function checkMoney(investMoney){
    var exp = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
	var minMoney = projectInfo.minMoney;
	var addRule = projectInfo.addAmount;
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
 * 关闭弹窗
 */
function hideAlert(){
	$("#errorDiv").hide();
}

/**
 * 显示弹窗
 */
function showAlert(){
	$("#errorDiv").show();
}

/**
 * 打开一个链接
 * @param url
 */
function openUrl(url){
	location.href = url;
}

/**
 * 选择投资金额
 */
function getProfitId(){
	var index = parseInt($("#selectMoney").val());
	profitId = profitList[index].id;
	$("#profit").val(profitId);
	money = profitList[index].money;
	$("#money").val(profitList[index].money);
	$("#profitInfo").val(profitList[index].content);
	var type = $("#type").val();
	getProfitNum();
	getPayMoney();
}

/**
 * 实付金额
 */
function getPayMoney(){
	if(2==checkResult){
		$("#realMoney").html("￥"+formatCurrency($("#money").val()));
	}else{
		$("#realMoney").html("￥"+formatCurrency(parseFloat(parseFloat($("#money").val())*parseFloat(projectInfo.breach)).toFixed(2)));
	}
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

function openRisk(){
	$("#regist-box1").show();
	$(".regist-mask").show();
}
function closeProtocol1(){
	$("#regist-box1").hide();
	$(".regist-mask").hide();
}