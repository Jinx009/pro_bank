function getBankInfo(obj,obj2,obj3){
	var cardval = obj.value;
	var bcKey = $("#bankCode").val();
	if(cardval!=""){			
		$.ajax({
			url:"/cf/wechat/recharge/checkBankCanUse.html?cardNo="+cardval,
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

function checkBankInfo(cardNo){
	var bcKey = $("#bankCode").val();
	if(cardNo!=""){			
		$.ajax({
			url:"/cf/wechat/recharge/checkBankCanUse.html?cardNo="+cardNo,
			type:"get",
			dataType:"json",
			success:function(data){
				if(data.result==false){
					$("#onlineError").html("<span style='color:red;' id='bankPPError'>"+data.message+"<span>");
				}else{
					var bankCode = data.supportBank.bankCode;
					var html = "<img src='/data/bank/llmini/"+bankCode+".png'> " + data.supportBank.bankName;
					if(bankCode===bcKey){						
						$("#onlineError").html(html);
						document.addBankForm.submit();
					}else{
						if(1==1){//判断是否与之前选择一致								
							$("#onlineError").html(html+"   <span style='color:red;' id='bankPPError'>与选择银行不匹配<span>");
						}else{
							$("#onlineError").html(html);
						}
					}
				}
			}
		})
	}
}

//验证在线充值表单
function checkForm(){
	var channelKey = $("#channelKey").val();
	var accName = $("#accName").val();
	var accId = $("#accId").val();
	var bankNo = $("#infoyhzh").val();
	var mobile = $("#mobilePhone").val();
	var code = $("#code").val();
	var flag = true;
	
	if(accName.trim().length==0){
		$("#onlineError").html("请输入您的真实姓名");
		flag = false;
	}else if(accId.trim().length==0){
		$("#onlineError").html("请输入您的身份证号");
		flag = false;
	}else if(!isIdCardNo(accId.toUpperCase().trim())){
		$("#onlineError").html("身份证号格式不正确");
		flag = false;
	}else if(bankNo.trim().length==0){
		$("#onlineError").html("请输入您的银行卡号");
		flag = false;
	}else if(channelKey==="unionpay_channel_key"){
		if(mobile.trim().length==0){
			$("#onlineError").html("请输入您的银行预留手机");
			flag = false;
		}else{
			$("#onlineError").html("");
		}
		if(code.trim().length==0){
			$("#onlineError").html("请输入手机验证码");
			flag = false;
		}else{
			$("#onlineError").html("");
		}
	}else{
		$("#onlineError").html("");
	}
	if(flag){
		checkBankInfo(bankNo);
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
							$('#timeval').val(time+'秒后重新获取').attr('disabled',true);
                            $('#timeval').css('color','#333');
                            $('#timeval').css('background','#ccc');
						}else{
							time=60;
							$('#timeval').val('获取验证码').removeAttr('disabled');
							$('#timeval').css('background','#2370b6');
							$('#timeval').css('color','#fff');
							clearInterval(timeFun);
					}
				},1000);
			}
			else{
				$('.tip').html(data.msg)
			}
		}
	});
}