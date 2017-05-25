/**
 * 下一步
 */
function updateType(){
	var selBank = $('#bankCode').val();
	var money = $('#money1').val();
	
	if(money.length==0 || parseFloat(money)<0){
		$('#onlineError').html('充值金额必须不小于10元');
	}else if(selBank.length==0 || selBank=='0'){
		layer.alert('请选择支持的银行！',{title:false,closeBtn: 0})
	}else{	
		$('#form1').submit();
	}
}

/**
 * 清除错误信息
 */
function delClass(){
	$('#lineError').html('');
	$('#onlineError').html('');
}
/**
 * 校验银行卡信息
 * @param bankno
 * @param obj
 */
function checkBankInfo(bankno,obj){
	if(bankno.length==0){
		$('#lineError').css('display','none');
	}else{
		$.ajax({
	   		 url:'/cf/wechat/recharge/binBank.html?cardNo='+bankno+'&random='+Math.random(),
	   		 data:{cardNo:bankno},
	   		 type:'GET',
	   		 dataType:'json',
	   		 success:function(data){
	 			if($('#lineError').length){
	 				$('#lineError').css('display','block');
		   			 if(data.ret_code=='0000' && data.card_type=='2'){
		   				 $('#lineError').html('<span style="color:black;"><img src="/data/bank/llmini/'+data.bank_code+'.png"  style="width: 16px;height: 16px;">'+data.bank_name+'</span>');
		   					if(obj==1){		   						
		   						$('#form2').submit();
		   					}
		   			 }else if(data.card_type=='3'){
		   				 $('#lineError').html('不支持信用卡，请重新输入！');
		   			 }else{
		   				 $('#lineError').html('卡号有误，请检查！');
		   			 }
	   			}
	   		 }
		 })
	}
}

/**
 * 获取银行限额
 * @param bank_code
 */
function getOneBankInfo(bank_code){
	if(bank_code!=""){
  		$.ajax({
  			url:'/nb/wechat/recharge/querySupportBank.html?bank_code='+bank_code+'&random='+Math.random(),
  			type:'GET',
  			dataType:'json',
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
			        	$('#danbi').html(numInput0/10000 +'万');
			        	$('#danri').html(numInput1/10000 +'万');
			        	$('#danyue').html(numInput2/10000 +'万');
			        	$('#bankTbl2').css('display','block');
			        }
			    }
  			}
  		})
	}else{
		$('#bankTbl2').css('display','none');
	}
}

function AngelMoney(s, n) { 
	n = n > 0 && n <= 20 ? n : 2; 
	s = parseFloat((s +'').replace(/[^\d\.-]/g,'')).toFixed(n) + ''; 
	var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1]; 
	t = ''; 
	for (i = 0; i < l.length; i++) { 
	t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? ',' : ''); 
	} 
	return t.split('').reverse().join('') + '.' + r; 
} 
function rmoney(s) { 
	return parseFloat(s.replace(/[^\d\.-]/g, '')); 
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
				$('.tip').html()
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