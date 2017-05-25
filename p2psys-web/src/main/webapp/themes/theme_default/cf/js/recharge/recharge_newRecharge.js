$(function(){
		 //我的充值信息
		var htmlStr="";
		 $.ajax({
			type:"post",
			url:"/cf/recharge/newRechargeOper.html?random="+Math.random(),
			 dataType:"json", 
			success:function(json){
				var obbb = json.bankModel;
				if(json.bankModel==0){
					var obbss = json.ccList;
					var isSecond = $("#isSecond").val();
					//alert(isSecond);
					if(isSecond!=0){							
						document.getElementById("home1").style.display="none";
	    				document.getElementById("home2").style.display="block";
					}else{							
						document.getElementById("home1").style.display="block";
						var length = json.ccList.length;
						if(length>0){
							var bnum = 0;
							for (var i = 0; i < length; i++) {
								var enable = json.ccList[i].sb.enable;
								var channelKey = json.ccList[i].webRechargeKey;
								var bankCode = json.ccList[i].sb.bankCode;
								if(enable===1){
									var singleAmt = "";
					        		if(parseFloat(json.ccList[i].sb.singleAmt)<10000){
					        			singleAmt = parseFloat(json.ccList[i].sb.singleAmt) + "元";
					        		}else{
					        			singleAmt = parseFloat(json.ccList[i].sb.singleAmt)/10000 + "万";
					        		}
						        	var dayAmt = "";
						        	if(parseFloat(json.ccList[i].sb.dayAmt)<10000){
						        		dayAmt = parseFloat(json.ccList[i].sb.dayAmt) + "元";
						        	}else{
						        		dayAmt = parseFloat(json.ccList[i].sb.dayAmt)/10000 + "万";
						        	}
						        	var monthAmt = "";
						        	if(parseFloat(json.ccList[i].sb.monthAmt)<10000){
						        		monthAmt = parseFloat(json.ccList[i].sb.monthAmt) + "元";
						        	}else{
						        		monthAmt = parseFloat(json.ccList[i].sb.monthAmt)/10000 + "万";
						        	}
									if(bnum==0){	
										bnum = 1;
										htmlStr += "<li class=\"bank-col bank-col-active\"><img src=\""+json.ccList[i].sb.bankLogo+"\">";
										htmlStr += "<span style=\"display:none;\">"+json.ccList[i].sb.bankName+"#"+singleAmt+"#"+dayAmt+"#"+monthAmt+"</span><label  style=\"display:none;\">"+bankCode+"</label>";
										htmlStr += "<input type=\"hidden\" value=\""+json.ccList[i].webRechargeURL+"\"><div style=\"display:none;\">"+channelKey+"</div></li>";
										document.addBankForm.action =json.ccList[i].webRechargeURL;
										if(channelKey==="银联支付"){
											$("#bankToto").html("<img src=\"/data/bank/llbank/"+bankCode+".png\"  style=\"vertical-align: middle;\"><span class=\"bank-name\">"+json.ccList[i].sb.bankName+"</span>");
											$("#choosBankInfo").html("<img src=\"/data/bank/llmini/"+bankCode+".png\"  style=\"vertical-align: middle;\">"+json.ccList[i].sb.bankName);
											$("#danbi").html(singleAmt);
											$("#danri").html(dayAmt);
											$("#danyue").html(monthAmt);
										}else{					        				
											getOneBankInfo(bankCode);
										}
										$("#webChannelKey").val(channelKey);
										$("#bankCodeKey").val(bankCode);
									}else{
										htmlStr += "<li class=\"bank-col\"><img src=\""+json.ccList[i].sb.bankLogo+"\">";
										htmlStr += "<span style=\"display:none;\">"+json.ccList[i].sb.bankName+"#"+singleAmt+"#"+dayAmt+"#"+monthAmt+"</span><label  style=\"display:none;\">"+bankCode+"</label>";
										htmlStr += "<input type=\"hidden\" value=\""+json.ccList[i].webRechargeURL+"\"><div style=\"display:none;\">"+channelKey+"</div></li>";
									}
								}
							}
						}	
						$("#bankListInfo").html(htmlStr);
						loadClick();
					}
				}		
//				$("#use_money").html(json.useMoney);
//				$("#userName2").val(json.realName);
//				$("#cardId2").val(json.cardId);
//				$("#bankNo2").val(json.bankModel.bankNo);
				$("#mobilePhone").val(json.mobile);
				$("#xsRToken").val(json.nbRechargeToken);
//				$("#minRechargeMoney").val(json.minRechargeMoney);
//				$("#rechargeMoney2").val(json.minRechargeMoney);
//				$("#minRechargeMoneyTis").html(json.minRechargeMoney);
				
			}
		});
	});

//验证在线充值表单
function checkCRForm(){
	var money = $("#money").val();
	var pwd = $("#jyPassword").val();
	var flag = true;
	
	if(money.trim().length==0 || parseFloat(money)<10){
		$("#trdMoney_tips").html("充值金额必须不小于10元");
		flag = false;
	}else{
		$("#trdMoney_tips").html("");
	}
	if(pwd.trim().length==0){
		$("#trdPwd_tips").html("请输入交易密码");
		flag = false;
	}else{
		$("#trdPwd_tips").html("");
	}

	if(flag){
		$.ajax({
			type:'POST',
			url:"/nb/checkPayPwd.html?random="+Math.random(),
			dataType:"json",
			data:{
				key:$("#jyPassword").val()
			},
			success:function(json){
				if("success"==json.result){
					document.crForm.submit();
				}else{
					  if(json.error_time <=4){
						  showAlertDiv(false,"交易密码不正确","您还可以输入"+(5-json.error_time)+"次","");
					  }else{
						  showAlertDiv(false,"交易密码被锁定","24小时内无法进行账户交易","");
					  }
				}
			}
		});
	}
}

function loadClick() {
	  var qcloud={};
	$('[_t_nav]').hover(function(){
		var _nav = $(this).attr('_t_nav');
		clearTimeout( qcloud[ _nav + '_timer' ] );
		qcloud[ _nav + '_timer' ] = setTimeout(function(){
		$('[_t_nav]').each(function(){
		$(this)[ _nav == $(this).attr('_t_nav') ? 'addClass':'removeClass' ]('nav-up-selected ');
		});
		$('#'+_nav).stop(true,true).slideDown(200);
		}, 150);
	},function(){
		var _nav = $(this).attr('_t_nav');
		clearTimeout( qcloud[ _nav + '_timer' ] );
		qcloud[ _nav + '_timer' ] = setTimeout(function(){
		$('[_t_nav]').removeClass('nav-up-selected');
		$('#'+_nav).stop(true,true).slideUp(200);
		}, 150);
	});
	$(".bank-list .bank-col").bind("click",function(){
			$(this).addClass("bank-col-active").siblings().removeClass("bank-col-active");
//			$("#choosBankInfo").html(($(this).children("span")).html());
			document.addBankForm.action = ($(this).children("input")).attr("value");
			$("#bankCodeKey").val(($(this).children("label")).html());
			var channelKey = ($(this).children("div")).html();
			$("#webChannelKey").val(channelKey);
			var bank_code = ($(this).children("label")).html();
			if(channelKey==="银联支付"){
				var spanStr = ($(this).children("span")).html();
				var sbInfo = spanStr.split("#");
				$("#bankToto").html("<img src=\"/data/bank/llbank/"+bank_code+".png\"  style=\"vertical-align: middle;\"><span class=\"bank-name\">"+sbInfo[0]+"</span>");
				$("#choosBankInfo").html("<img src=\"/data/bank/llmini/"+bank_code+".png\"  style=\"vertical-align: middle;\">"+sbInfo[0]);
	        	$("#danbi").html(sbInfo[1] );
	        	$("#danri").html(sbInfo[2]);
	        	$("#danyue").html(sbInfo[3]);
			}else{					        				
				getOneBankInfo(bank_code);
			}
	});
}

function getOneBankInfo(bank_code){
	$.ajax({
		url:"/cf/recharge/querySupportBank.html?bank_code="+bank_code+"&random="+Math.random(),
		type:"get",
		dataType:"json",
		success:function(data){
			    if(data.ret_code==='0000'){
			      	var length = data.support_banklist.length;
			        if(length>0){
			        	var numInput0 = 0;
			        	var numInput1 = 0;
			        	var numInput2 = 0;
			        	var llbank_name = "";
			        	if(bank_code.length>0 && bank_code!=""){	
				        	numInput0 = parseFloat(data.support_banklist[0].single_amt);
				        	numInput1 = parseFloat(data.support_banklist[0].day_amt);
				        	numInput2 = parseFloat(data.support_banklist[0].month_amt);
				        	llbank_name = data.support_banklist[0].bank_name;
			        	}
			        	$("#bankToto").html("<img src=\"/data/bank/llbank/"+bank_code+".png\"  style=\"vertical-align: middle;\"><span class=\"bank-name\">"+llbank_name+"</span>");
						$("#choosBankInfo").html("<img src=\"/data/bank/llmini/"+bank_code+".png\"  style=\"vertical-align: middle;\">"+llbank_name);

			        	if(numInput0<10000){
		        			$("#danbi").html(numInput0 + "元");
		        		}else{
		        			$("#danbi").html(numInput0/10000 + "万");
		        		}
		        		if(numInput1<10000){
		        			$("#danri").html(numInput1 + "元");
		        		}else{
		        			$("#danri").html(numInput1/10000 + "万");
		        		}
		        		if(numInput2<10000){
		        			$("#danyue").html(numInput2 + "元");
		        		}else{
		        			$("#danyue").html(numInput2/10000 + "万");
		        		}
			        }
			    }
		}
	})
}

function getUnionpayBankInfo(bank_code){
		if(bank_code=="01020000"){
        	$("#danbi").html("5万");
        	$("#danri").html("5万");
//        	$("#danyue").html("无");
		}
		if(bank_code=="01030000"){
			$("#danbi").html("20万");
        	$("#danri").html("500万");
//        	$("#danyue").html("无");
		}
		if(bank_code=="01050000"){
			$("#danbi").html("20万");
        	$("#danri").html("无");
//        	$("#danyue").html("无");
		}
		if(bank_code=="01040000"){
			$("#danbi").html("1万");
        	$("#danri").html("1万");
//        	$("#danyue").html("无");
		}
		if(bank_code=="01000000"){
			$("#danbi").html("1万");
        	$("#danri").html("1万");
//        	$("#danyue").html("无");
		}
		if(bank_code=="03080000"){
			$("#danbi").html("5万");
	    	$("#danri").html("5万");
//	    	$("#danyue").html("无");
		}
		if(bank_code=="03030000"){
			$("#danbi").html("20万");
        	$("#danri").html("无");
//        	$("#danyue").html("无");
		}
		if(bank_code=="03060000"){
			$("#danbi").html("20万");
        	$("#danri").html("无");
//        	$("#danyue").html("无");
		}
		if(bank_code=="03070000"){
			$("#danbi").html("20万");
        	$("#danri").html("无");
//        	$("#danyue").html("无");
		}
}

function getBankInfo(obj,obj2,obj3){
	var cardval = obj.value;
	var bcKey = $("#bankCodeKey").val();
	if(cardval!=""){			
		$.ajax({
			url:"/cf/recharge/checkBankCanUse.html?cardNo="+cardval,
			type:"get",
			dataType:"json",
			success:function(data){
				if(data.result==false){
					$("#"+obj2).html("<span style='color:red;' id='bankPPError'>"+data.message+"<span>");
				}else{
					var bankCode = data.supportBank.bankCode;
					var html = "<img src='/data/bank/llmini/"+bankCode+".png'> " + data.supportBank.bankName;
					if(bankCode===bcKey){						
						$("#"+obj2).html(html);
					}else{
						if(obj3==1){								
							$("#"+obj2).html(html+"   <span style='color:red;' id='bankPPError'>与选择银行不匹配<span>");
						}else{
							$("#"+obj2).html(html);
						}
					}
				}
			}
		})
	}
}

function AngelMoney(s, n) { 
	n = n > 0 && n <= 20 ? n : 2; 
	s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + ""; 
	var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1]; 
	t = ""; 
	for (i = 0; i < l.length; i++) { 
	t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : ""); 
	} 
	return t.split("").reverse().join("") + "." + r; 
} 
function rmoney(s) { 
	return parseFloat(s.replace(/[^\d\.-]/g, "")); 
} 

//下一步事件
function nextPage(){
	var rcMoney = $("#oneRCMoney").val();
	var webChannelKey = $("#webChannelKey").val();
	if(rcMoney.length>0 && parseFloat(rcMoney)>=10){
	//if(rcMoney.length>0 && parseFloat(rcMoney)<10){
		document.getElementById("home1").style.display="none";
		document.getElementById("home2").style.display="block";
		if(webChannelKey==="银联支付"){							
			document.getElementById("addYLBank").style.display="block";
		}else{
			document.getElementById("addYLBank").style.display="none";
		}
		$("#rechMoney2").html(rcMoney+"元");
		$("#money_order").val(rcMoney);
		$("#moneyError").html("");
	}else{
		$("#moneyError").html("充值金额不能小于10元");
		//$("#moneyError").html("充值金额不能大于10元");
		//alert("请输入充值金额！");
	}
}

//验证在线充值表单
function checkForm(){
	var webChannelKey = $("#webChannelKey").val();
	var accName = $("#accName").val();
	var accId = $("#accId").val();
	var bankNo = $("#infoyhzh").val();
	var mobile = $("#mobilePhone").val();
	var code = $("#code").val();
	var xycbox = document.getElementById("xycbox");
	var flag = true;
	
	if(accName.trim().length==0){
		//showAlertDiv("请输入您的真实姓名！",null);
		$("#accNameError").html("请输入您的真实姓名");
		flag = false;
	}else{
		$("#accNameError").html("");
	}
	if(accId.trim().length==0){
		$("#accIdError").html("请输入您的身份证号");
		flag = false;
	}else if(!isIdCardNo(accId.toUpperCase().trim())){
		$("#accIdError").html("身份证号格式不正确");
		flag = false;
	}else{
		$("#accIdError").html("");
	}
	if(bankNo.trim().length==0){
		$("#cardNoError").html("请输入您的银行卡号");
		flag = false;
	/*}else if($("#bankPPError")!=null && $("#bankPPError").html()!=undefined){
		$("#cardNoError").html($("#bankPPError").html());
		flag = false;*/
	}else{
		$("#cardNoError").html("");
	}
	if(mobile.trim().length==0){
		if(webChannelKey==="银联支付"){
			$("#mobileError").html("请输入您的银行预留手机");
			flag = false;
		}else{
			$("#mobileError").html("");
		}
	}else{
		$("#mobileError").html("");
	}
	if(code.trim().length==0){
		if(webChannelKey==="银联支付"){
			$("#codeError").html("请输入手机验证码");
			flag = false;
		}else{
			$("#codeError").html("");
		}
	}else{
		$("#codeError").html("");
	}
/*	if(!xycbox.checked){
		showAlertDiv("您还未接受委托协议！",null);
		flag = false;
	}*/
	if(flag){
		document.addBankForm.submit();
	}
}

/**
 * 获取手机验证码
 */
function doajax(){
	$.ajax({
		url:'/cf/recharge/getAddBankCode.html?mobilePhone='+$('#mobilePhone').val(),
		type:'post',
		success:function(data){
			if(data.result){
				$(".tip").html()
				var time=60;
				var timeFun=setInterval(function(){
					time--;
						if(time>0){
							$('#timeval').val(time+"秒后重新获取").attr("disabled",true);
                            $('#timeval').css("color","#333");
                            $('#timeval').css("background","#ccc");
						}else{
							time=60;
							$('#timeval').val("获取验证码").removeAttr("disabled");
							$('#timeval').css("background","#2370b6");
							$('#timeval').css("color","#fff");
							clearInterval(timeFun);
					}
				},1000);
			}
			else{
				$(".tip").html(data.msg)
			}
		}
	});
}