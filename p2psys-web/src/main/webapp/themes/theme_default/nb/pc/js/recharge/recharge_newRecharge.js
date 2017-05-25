$(function(){
	changeHeader("我要充值");

		 //我的充值信息
		var htmlStr="";
		 $.ajax({
			type:"post",
			url:"/nb/pc/recharge/newRechargeOper.html?random="+Math.random(),
			 dataType:"json", 
			success:function(json){
				if(checkUser(json.result)){	
					var obbb = json.bankModel;
					if(json.bankModel!=0){
						document.getElementById("home0").style.display="block";
						document.getElementById("home1").style.display="none";
	    				document.getElementById("home2").style.display="none";
	    				htmlStr += "<form action=\""+json.webRechargeUrl+"\"  method=\"post\" class=\"rechargeForm col-md-6 padding_col0\" name=\"crForm\" id=\"crForm\">";
	    				htmlStr += "<div class=\"form-group row rechargeForm_col margin0\">";
						htmlStr += "<label  class=\"col-md-3 text-left padding_col0\">已绑银行卡:</label>";
						htmlStr += "<div class=\"col-md-8 text-left padding_col0\"><img src=\"/data/bank/llbank/"+json.bankCode+".png\" style=\"vertical-align: middle;\"/>";
						htmlStr += json.bankModel.hideBankNo+"</div></div>";
						htmlStr += "<div class=\"form-group row newCash_col margin0\">";
						htmlStr += "<label  class=\"col-md-3 text-left padding_col0\">可用余额:</label>";
						htmlStr += "<div class=\"col-md-8 padding_col0\"><p class=\"col-md-12 padding_col0\">"+json.useMoney+"元</p></div></div>";
						htmlStr += "<div class=\"form-group row newCash_col margin0\">";
						htmlStr += "<label  class=\"col-md-3 text-left padding_col0\">充值金额:</label>";
						htmlStr += "<div id=\"cashMoney\" class=\"col-md-8 padding_col0\">";
						htmlStr += "<input type=\"text\"  class=\"col-md-12 rechargeForm_inp\"  id=\"money\"  name=\"money\"  placeholder=\"充值金额\"  onkeyup=\"value=value.replace(/[^\\d]/g,'')\" maxlength=\"9\">";
						htmlStr += "<span class=\"yuan\">元</span></div></div>";
						htmlStr +="<div class=\"form-group row margin0\"><div class=\"col-md-3 padding_col0\"></div><div class=\"col-md-8 text-right forgetPayPwd padding_col0\" id=\"trdMoney_tips\"></div><div class=col-md-1></div></div>";
						htmlStr += "<div class=\"form-group row newCash_col margin0\">";
						htmlStr += "<label  class=\"col-md-3 text-left padding_col0\">交易密码:</label><div class=\"col-md-8 padding_col0\">";
						if(json.userPayPwd!=null && json.userPayPwd!=""){	
							htmlStr += "<input type=\"password\" class=\"col-md-12 rechargeForm_inp\" id=\"jyPassword\"  name=\"payPwd\" placeholder=\"交易密码\">";
						}else{
							htmlStr += "<a href=\"javascript:showDiv('util_setting_pay_pwd');\"><font color=\"#FF0000\">请先设置一个交易密码</font></a>";
						}
						htmlStr +="</div></div><div class=\"form-group row margin0\"><div class=\"col-md-3 padding_col0\"></div><div class=\"text-right forgetPayPwd padding_col0 col-md-8\" id=\"trdPwd_tips\"></div><div class=col-md-1></div></div>";
						if(json.webChannelKey==="unionpay_channel_key" && json.isYLBank!="1"){							
							htmlStr += "<div class=\"form-group row newCash_col margin0\">";
							htmlStr += "<label  class=\"col-md-3 text-left padding_col0\">验证码:</label>";
							htmlStr += "<div id=\"addBankCode\" class=\"col-md-9 padding_col0\">";
							htmlStr += "<input type=\"text\" class=\"col-md-5 rechargeForm_inp\"  name=\"code\" id=\"code\" autocomplete=\"off\" placeholder=\"验证码\" maxlength=\"6\" style=\"float:left;margin-right:10px;\">";
							htmlStr += "<input name=\"timeval\" value=\"获取验证码\" type=\"button\" id=\"timeval\" onclick=\"doajax()\" class=\"getCode\" style=\"width: 150px;float:left;\"></div></div>";
						}
						htmlStr +="<div class=\"form-group row margin0\"><div class=\"col-md-4\"></div><div class=\"text-right forgetPayPwd\" id=\"trdMoney_tips\"></div></div>";
						htmlStr += "<div class=\"form-group row margin0\" style=\"margin-bottom: 5px;\"><div  class=\"col-md-3\"></div>";
						htmlStr += "<button type=\"button\" class=\"col-md-8 newChashBtn padding_col0\" onclick=\"checkCRForm();\">确认充值</button></div>";
						htmlStr += "<div class=\"form-group row\"><div  class=\"col-md-3\"></div>";
						htmlStr += "<a href=javascript:showDiv('util_forget_pay_pwd') ><p class=\"text-right forgetPayPwd col-md-8 padding_col0\">忘记交易密码？</p></a></div><input type=\"hidden\" id=\"xsRToken\" name=\"nbRechargeToken\" value=\""+json.nbRechargeToken+"\"></form>";
						                               
						htmlStr += "<div class=\"col-md-5 padding_col0 rightTip\">";
						//htmlStr += "<div class=\"warmPrompt\" style=\"padding: 10px;\">";
						//htmlStr += "<p><img src=\"/data/bank/llmini/"+json.bankCode+".png\" style=\"vertical-align: middle;\"/>&nbsp;"+json.bankModel.bank+"：单笔限额：20万，每日限额：无&nbsp;&nbsp;</p></div></div>";
						
						htmlStr += "<div class=\"col-md-12 padding_col0 bankDetails\" >";
						htmlStr += "<li><img src=\"/data/bank/llmini/"+json.bankCode+".png\" style=\"vertical-align: middle;\"/>&nbsp;"+json.bankModel.bank+"</li>";
						htmlStr += "<li class=\"otherLi\">单笔限额：<span id=\"danbi\"></span></li>";
						htmlStr += "<li class=\"otherLi\">每日限额：<span id=\"danri\"></span></li></div></div>";
						
						$("#home0").html(htmlStr);
						$("#webChannelKey").val(json.webChannelKey);
	        			$("#bankCodeKey").val(json.bankCode);
	        			if(json.webChannelKey==="unionpay_channel_key"){
	        				$("#choosBankInfo").html("<img src=\"/data/bank/llmini/"+json.bankCode+".png\"  style=\"vertical-align: middle;\">"+json.bankModel.bank);
	        				getUnionpayBankInfo(json.bankCode);
	        			}else{	        				
	        				getOneBankInfo(json.bankCode);
	        			}
					}else{
						var obbss = json.ccList;
						document.getElementById("home0").style.display="none";
						var isSecond = $("#isSecond").val();
						//alert(isSecond);
						if(isSecond!=0){							
							document.getElementById("home0").style.display="none";
							document.getElementById("home1").style.display="none";
		    				document.getElementById("home2").style.display="block";
						}else{							
							document.getElementById("home1").style.display="block";
							var length = json.ccList.length;
					        if(length>0){
					        	for (var i = 0; i < length; i++) {
					        		var singleAmt = parseFloat(json.ccList[i].sb.singleAmt)/10000 + "万";
						        	var dayAmt = parseFloat(json.ccList[i].sb.dayAmt)/10000 + "万";
						        	var monthAmt = parseFloat(json.ccList[i].sb.monthAmt)/10000 + "万";
					        		if(i==0){		  
					        			var channelKey = json.ccList[i].webRechargeKey;
					        			var bankCode = json.ccList[i].sb.bankCode;
					        			htmlStr += "<div class=\"bank-col bank-col-active\"><img src=\""+json.ccList[i].sb.bankLogo+"\">";
					        			htmlStr += "<span style=\"display:none;\">"+json.ccList[i].sb.bankName+"#"+singleAmt+"#"+dayAmt+"#"+monthAmt+"</span><label  style=\"display:none;\">"+bankCode+"</label>";
					        			htmlStr += "<input type=\"hidden\" value=\""+json.ccList[i].webRechargeURL+"\"><div style=\"display:none;\">"+channelKey+"</div></div>";
					        			document.addBankForm.action =json.ccList[i].webRechargeURL;
					        			if(channelKey==="银联支付"){
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
					        			htmlStr += "<div class=\"bank-col\"><img src=\""+json.ccList[i].sb.bankLogo+"\">";
					        			htmlStr += "<span style=\"display:none;\">"+json.ccList[i].sb.bankName+"#"+singleAmt+"#"+dayAmt+"#"+monthAmt+"</span><label  style=\"display:none;\">"+json.ccList[i].sb.bankCode+"</label>";
					        			htmlStr += "<input type=\"hidden\" value=\""+json.ccList[i].webRechargeURL+"\"><div style=\"display:none;\">"+json.ccList[i].webRechargeKey+"</div></div>";
					        		}
								}
					        }	
					        $("#bankListInfo").html(htmlStr);
					        loadClick();
						}
					}		
					$("#use_money").html(json.useMoney);
					$("#userName2").val(json.realName);
					$("#cardId2").val(json.cardId);
					$("#bankNo2").val(json.bankModel.bankNo);
					$("#mobilePhone").val(json.mobile);
					$("#minRechargeMoney").val(json.minRechargeMoney);
					$("#rechargeMoney2").val(json.minRechargeMoney);
					$("#minRechargeMoneyTis").html(json.minRechargeMoney);
				}else{
					showDiv("util_login");
				}
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
		url:"/nb/wechat/recharge/querySupportBank.html?bank_code="+bank_code+"&random="+Math.random(),
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
						$("#choosBankInfo").html("<img src=\"/data/bank/llmini/"+bank_code+".png\"  style=\"vertical-align: middle;\">"+llbank_name);
			        	$("#danbi").html(numInput0/10000 + "万");
			        	$("#danri").html(numInput1/10000 + "万");
			        	$("#danyue").html(numInput2/10000 + "万");
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
				url:"/nb/pc/cash/checkBankCanUse.html?cardNo="+cardval,
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
