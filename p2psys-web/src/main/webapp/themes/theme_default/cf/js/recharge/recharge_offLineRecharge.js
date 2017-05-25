$(function(){
		 //我的充值信息
		var htmlStr="";
		 $.ajax({
			type:"post",
			url:"/cf/recharge/newRechargeOper.html?random="+Math.random(),
			 dataType:"json", 
			success:function(json){	
				$("#use_money").html(json.useMoney+"元");
				$("#userName2").val(json.realName);
				$("#cardId2").val(json.cardId);
				$("#bankNo2").val(json.bankModel.bankNo);
				$("#minRechargeMoney").val(json.minRechargeMoney);
				$("#rechargeMoney2").val(json.minRechargeMoney);
				$("#minRechargeMoneyTis").html(json.minRechargeMoney);
			}
		});
	});

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

//验证线下充值表单
function checkOLForm(){
	var money = $("#rechargeMoney2").val();
	var accName = $("#userName2").val();
	var accId = $("#cardId2").val();
	var bankNo = $("#bankNo2").val();
	var minRechargeMoney = $("#minRechargeMoney").val();
	var flag = true;
	
	if(money.trim().length==0){
		$("#trdRecharge_tips").html("请输入充值金额");
		flag = false;
    }else if(parseFloat(money)<parseFloat(minRechargeMoney)){
    	$("#trdRecharge_tips").html("线下充值金额不能小于"+minRechargeMoney+"元");
		flag = false;
    }else{
    	$("#trdRecharge_tips").html("");
    } 
	if(accName.trim().length==0){
		$("#trdName_tips").html("请输入您的真实姓名");
		flag = false;
	}else{
		$("#trdName_tips").html("");
	}
	if(accId.trim().length==0){
		$("#trdCardId_tips").html("请输入您的身份证号");
		flag = false;
	}else if(!isIdCardNo(accId.trim())){
		$("#trdCardId_tips").html("身份证号格式不正确");
		flag = false;
	}else{
		$("#trdCardId_tips").html("");
	}
	if(bankNo.trim().length==0){
		$("#trdBankNo_tips").html("请输入您的银行卡号");
		flag = false;
	}else{
		$("#trdBankNo_tips").html("");
	}
	if(flag){
		document.form2.submit();
	}
}