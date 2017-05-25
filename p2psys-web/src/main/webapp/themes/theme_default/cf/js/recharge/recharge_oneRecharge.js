$(function(){
	    //我的充值信息
		var htmlStr="";
		 $.ajax({
			type:"post",
			url:"/cf/recharge/newRechargeOper.html?random="+Math.random(),
			dataType:"json", 
			success:function(json){
				if(json.bankModel!=0){
					document.crForm.action = json.webRechargeUrl;
					$("#ybyhkDiv").html("<img src=\"/data/bank/llbank/"+json.bankCode+".png\"/>"+json.bankModel.hideBankNo);
					//$("#yhkxe").html("<span>"+json.bankModel.bank+"</span><span>单笔限额：<span id=\"danbi\"></span><span>每日限额：<span id=\"danri\"></span></span>");
					$('#yhkxe').html('<span>单笔限额：<font id="danbi" ></font></span><span>单日限额：<font id="danri" ></font></span><span>单月限额：<font id="danyue" ></font></span>');
					$("#kyye").html(json.useMoney+"元");
					if(json.userPayPwd!=null && json.userPayPwd!=""){	
						$("#jymm").html("<input type=\"password\" class=\"form-control\" id=\"jyPassword\"  name=\"payPwd\" placeholder=\"交易密码\">");
					}else{
						$("#jymm").html("<a href=\"/cf/user/set-pay.html?redirectUrl='recharge.html\"><font color=\"#FF0000\">请先设置一个交易密码</font></a>");
					}
					
//					if(json.webChannelKey==="unionpay_channel_key" && json.isYLBank!="1"){							
//						htmlStr += "<div class=\"form-group row newCash_col margin0\">";
//						htmlStr += "<label  class=\"col-md-3 text-left padding_col0\">验证码:</label>";
//						htmlStr += "<div id=\"addBankCode\" class=\"col-md-9 padding_col0\">";
//						htmlStr += "<input type=\"text\" class=\"col-md-5 rechargeForm_inp\"  name=\"code\" id=\"code\" autocomplete=\"off\" placeholder=\"验证码\" maxlength=\"6\" style=\"float:left;margin-right:10px;\">";
//						htmlStr += "<input name=\"timeval\" value=\"获取验证码\" type=\"button\" id=\"timeval\" onclick=\"doajax()\" class=\"getCode\" style=\"width: 150px;float:left;\"></div></div>";
//					}
					if(json.webChannelKey==="unionpay_channel_key"){
        				getUnionpayBankInfo(json.bankCode);
        			}else{	        				
        				getOneBankInfo(json.bankCode);
        			}
					$("#xsRToken").val(json.nbRechargeToken);
				}
			}
		});
	});

//验证在线充值表单
function checkCRForm(){
	var money = $("#money").val();
	var pwd = $("#jyPassword").val();
	var flag = true;
	
//	if(money.trim().length==0 || parseFloat(money)<10){
//		$("#trdMoney_tips").html("充值金额必须不小于10元");
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
			url:"/cf/recharge/checkPayPwd.html?random="+Math.random(),
			dataType:"json",
			data:{
				key:$("#jyPassword").val()
			},
			success:function(json){
				if("success"==json.result){
					document.crForm.submit();
				}else{
					  if(json.error_time <=4){
						  alert("交易密码不正确,您还可以输入"+(5-json.error_time)+"次!");
					  }else{
						  alert("交易密码被锁定,24小时内无法进行账户交易!");
					  }
				}
			}
		});
	}
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
			        	if(numInput0<10000){
		        			$("#danbi").html(numInput0 + "元；");
		        		}else{
		        			$("#danbi").html(numInput0/10000 + "万；");
		        		}
		        		if(numInput1<10000){
		        			$("#danri").html(numInput1 + "元；");
		        		}else{
		        			$("#danri").html(numInput1/10000 + "万；");
		        		}
		        		if(numInput2<10000){
		        			$("#danyue").html(numInput2 + "元；");
		        		}else{
		        			$("#danyue").html(numInput2/10000 + "万；");
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
//    	$("#danyue").html("无");
	}
	if(bank_code=="01030000"){
		$("#danbi").html("20万");
    	$("#danri").html("500万");
//    	$("#danyue").html("无");
	}
	if(bank_code=="01050000"){
		$("#danbi").html("20万");
    	$("#danri").html("无");
//    	$("#danyue").html("无");
	}
	if(bank_code=="01040000"){
		$("#danbi").html("1万");
    	$("#danri").html("1万");
//    	$("#danyue").html("无");
	}
	if(bank_code=="01000000"){
		$("#danbi").html("1万");
    	$("#danri").html("1万");
//    	$("#danyue").html("无");
	}
	if(bank_code=="03080000"){
		$("#danbi").html("5万");
    	$("#danri").html("5万");
//    	$("#danyue").html("无");
	}
	if(bank_code=="03030000"){
		$("#danbi").html("20万");
    	$("#danri").html("无");
//    	$("#danyue").html("无");
	}
	if(bank_code=="03060000"){
		$("#danbi").html("20万");
    	$("#danri").html("无");
//    	$("#danyue").html("无");
	}
	if(bank_code=="03070000"){
		$("#danbi").html("20万");
    	$("#danri").html("无");
//    	$("#danyue").html("无");
	}
}