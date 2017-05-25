var cityCodeJson;
$(function(){
	changeHeader("我要提现");
		 //我的提现信息
		var htmlStr="";
		 $.ajax({
			type:"post",
			url:"/nb/pc/cash/newCash.html?random="+Math.random(),
			 dataType:"json", 
			success:function(json){
				if(checkUser(json.result)){	
					if(json.bankModel!=0){
						$("#pic").html("<img src=\"/data/bank/llbank/"+json.bankCode+".png\" style=\"vertical-align: middle;\"/>"+json.bankModel.hideBankNo)
						$("#useMoney").html(json.account.useMoney+"元");
					}else{

						htmlStr += "<div class=\"row margin0\">";
						htmlStr += "<p class=\"col-md-12 padding_col0\" style=\"color:#efb322;font-size: 24px;\">您暂无已认证的银行卡信息</p></div>";
						htmlStr += "<div class=\"row margin0\" style=\"margin-bottom: 77px;\">";
						htmlStr += "<p class=\"col-md-12 padding_col0\" style=\"color: #898989;font-size: 18px;\">请先进行不小于10元的充值，认证并绑定银行卡</p></div>";
						htmlStr += "<div class=\"form-group row margin0\" style=\"margin-bottom: 0px;\">";
						htmlStr += "<a class=\"col-md-6 newChashBtn padding_col0\" id='auth' href='/nb/pc/recharge/newRecharge.html'>去认证</a></div>";

						$("#cashForm").html(htmlStr);
						$("#tips").show();
					}
					cityCodeJson = json.cityCodeList;
					$("#use_money").html(json.account.useMoney);
					$("#useMoney2").html(json.account.useMoney+"元");
					$("#userName2").val(json.realName);
					$("#cardId2").val(json.cardId);
					$("#bankNo2").val(json.bankModel.bankNo);
					$("#minCashMoney").val(json.minCashMoney);
					$("#cashMoney2").val(json.minCashMoney);
					$("#minCashMoneyTis").html(json.minCashMoney);
					$("#cashToken").val(json.nbCashToken);
					if(json.webCashKey=="llpay_channel_key" && (json.bankModel.branch===undefined || json.bankModel.branch==="")) {
						$("#isCityType").val("1");
						document.getElementById("cityCodeSpan").style.display="block";
					}
					
				}else{
					showDiv("util_login");
				}
			}
		});
		 
		 var cashMoney = false;
		 var tradPwd = false;
		 var brachs = true;
		 var citys = true;
		 $('input[name="money"]').blur(function(){
			 var useMoney = $("#useMoney").html();
			 if($(this).val() !=""){
				 if($(this).val() > parseFloat(useMoney)){
					 cashMoney =false;
					 $("#cash_tips").html("提现金额不能大于可用余额");
				 } else if(parseFloat($(this).val()) <= 0){
					 cashMoney =false
					 $("#cash_tips").html("提现金额必须大于0元");
				 }else{
					 cashMoney =true;
					 $("#cash_tips").html(" ");
				 }
				 
			 }else{
				 cashMoney = false;
				 $("#cash_tips").html("请输入提现金额");
			 }
			
		 });
		 
		 $('input[name="payPwd"]').blur(function(){
			 if($(this).val()==""){
				 tradPwd = false;
				 $("#trdPwd_tips").html("请输入交易密码");
			 }else{
				 if($(this).val().length <6 || $(this).val().length >=7){
				 tradPwd = false;
				 $("#trdPwd_tips").html("交易密码不合法");
				 }else{
					 tradPwd = true;
					 $("#trdPwd_tips").html(" ");
				 }
			 }
		 });
		 
		 $('input[name="branch"]').blur(function(){
			 if($(this).val()==""){
				 brachs = false;
				$("#branch_tips").html("请输入支行名称");
			}else{
				brachs = true;
				$("#branch_tips").html("");
			}
		 });
		 
		
		 
		 //提现到我的银行卡
		 $("#sub").click(function(){
			 checkAll_online();
//			 alert(cashMoney +" | "+ tradPwd  +" | "+  brachs  +" | "+  citys);
			if(cashMoney && tradPwd && brachs && citys){
				
				$.ajax({
					type:'POST',
					 url:"/nb/checkPayPwd.html?random="+Math.random(),
					  dataType:"json",
					  data:{
						  key:$("#payPwd").val()
					  },
					  success:function(json){
						  if(checkUser(json.result)){
							  if("success"==json.result){
								  $.ajax({
										 type:'POST',
										 url:"/nb/pc/cash/doAllCash.html?random="+Math.random(),
										  dataType:"json",
										  data:{
											  money:$("#money").val(),
											  payPwd:$("#payPwd").val(),
											  branch:$("#branch").val(),
											  city: $("#city").val(),
											  province: $("#province").val(),
											  nbCashToken: $("#cashToken").val()
										  },
										  success:function(json){
											  if(checkUser(json.result)){
												  if("success"==json.flag){
													  showAlertDiv(true,"提现申请成功", "", "/user/center.html?random="+Math.random());
													
												  }else{
													  showAlertDiv(false,json.title,json.txt,"/nb/pc/cash/cashPage.html?random="+Math.random());
												  }
											  }else{
												  showDiv("util_login");
											  }
										  }
									 });
							  }else{
								  if(json.error_time <=4){
									  showAlertDiv(false,"交易密码不正确","您还可以输入"+(5-json.error_time)+"次","");
								  }else{
									  showAlertDiv(false,"交易密码被锁定","24小时内无法进行账户交易","");
								  }
								 
							  }
						  }else{
							  showDiv("util_login");
						  }
					  }
				});
			}
		 });

		 
		 function checkAll_online(){
			 var useMoney = $("#useMoney").html();
			 if($("#money").val() ==""){
				 cashMoney = false;
				 $("#cash_tips").html("请输入提现金额");
			 }else{
				 $("#cash_tips").html("");
			 }
			 if(parseFloat($("#money").val()) > parseFloat(useMoney)){
				 cashMoney = false;
				 $("#useMoney_tips").html("提现金额不能大于可用余额");
			 }else{
				 $("#useMoney_tips").html("");
			 }
			 if($("#isCityType").val()==="1"){				 
				 var selcity = $("#city").val();
				 if($("#branch").val()==""){
					 brachs = false;
					 $("#branch_tips").html("请输入支行名称");
				 }else{
					 $("#branch_tips").html("");
				 }
				 if(selcity.length==0){
					 citys = false;
					 $("#citys_tips").html("请选择地区");
		  		 }else{
		  			$("#citys_tips").html("");
		  		 }
			 }
			 if($("#payPwd").val() ==""){
				 tradPwd = false;
				 $("#trdPwd_tips").html("请输入交易密码");
			 }else{
				 $("#trdPwd_tips").html("");
			 }
			 if($("#payPwd").val().length < 6 || $("#payPwd").val().length > 7 ){
				 tradPwd = false;
				 $("#trdPwd_tips").html("交易密码不合法");
			 }else{
				 $("#trdPwd_tips").html("");
			 }
		 }
		 
		/*********线下提现  begin**************/
		
		 var cashMoneyFlag=false;
		 var realNameFlag = false;
		 var cardIdFlag = false;
		 var bankNoFlag = false;
		 var brachsFlag = false;
		 
		 $('input[name="cashMoney2"]').blur(function(){
			 var useMoney = $("#useMoney2").html();
			 if($(this).val() ==""){
				 cashMoneyFlag =false;
				 $("#money_tips").html("请输入提现金额");
			 }else{
				 if(parseFloat($("#cashMoney2").val()) < parseFloat($("#minCashMoney").val())){
					 cashMoneyFlag =false;
					 $("#money_tips").html("提现金额不能小于"+$("#minCashMoney").val());
				 }else if($(this).val() > parseFloat(useMoney)){
					 cashMoneyFlag =false;
					 $("#money_tips").html("提现金额不能大于可用余额");
				 }else{
					 cashMoneyFlag =true;
					 $("#money_tips").html("");
				 }
			 }
			
		 });
		 
		 $('input[name="realName"]').blur(function(){
			 if($(this).val() ==""){
				 realNameFlag = false;
				 $("#userName_tips").html("请输入转账姓名");
			 }else{
				 realNameFlag = true;
				 $("#userName_tips").html("");
			 }
		 });
		 
		 $('input[name="cardId"]').blur(function(){
			 if($(this).val()==""){
				 cardIdFlag = false;
				 $("#cardId_tips").html("请输入身份证号");
			 }else{
				 if(!isIdCardNo($(this).val().trim())){
					 cardIdFlag = false;
					 $("#cardId_tips").html("身份证号格式不正确");
				 }else{
					 cardIdFlag = true;
					 $("#cardId_tips").html("");
				 }
			 }
		 });
		 
		 $('input[name="bankNo2"]').blur(function(){
			if($(this).val()==""){
				bankNoFlag = false;
				 $("#bankNo_tips").html("请输入银行卡号");
			}else{
				bankNoFlag = true;
				 $("#bankNo_tips").html("");
			}
		 });
		 
		 $('input[name="branch2"]').blur(function(){
			 if($(this).val()==""){
				 brachsFlag = false;
				$("#branch_tips2").html("请输入支行名称");
			}else{
				brachsFlag = true;
				$("#branch_tips2").html("");
			}
		 });
		 
		 //线下提现
		 $("#offSub").click(function(){
			 checkAll();
			 if(cashMoneyFlag && realNameFlag && cardIdFlag && bankNoFlag && brachsFlag){
				 $.ajax({
						type:"post",
						url:"/nb/pc/cash/doAllOfflineCash.html?random="+Math.random(),
						 dataType:"json", 
						 data:{
							 money:$("#cashMoney2").val(),
							 realName:$("#userName2").val(),
							 bankNo:$("#bankNo2").val(),
							 cardId:$("#cardId2").val(),
							 branch:$("#branch2").val(),
							 province:$("#province2").val(),
							 city:$("#city2").val(),
							 nbCashToken: $("#cashToken").val()
						 },
						success:function(json){
							if(checkUser(json.result)){	
								if("success"==json.flag){
									  showAlertDiv(true,"提现申请成功", "", "/user/center.html?random="+Math.random());
								  }else{
									  showAlertDiv(false,json.title,json.txt,"/nb/pc/cash/cashPage.html?random="+Math.random());
								  }
							}else{
								 showDiv("util_login");
							}
						}
				 });
			 }
		 });
		 
		 function checkAll(){
			 var useMoney2 = $("#useMoney2").html();
			 if($("#cashMoney2").val()==""){
				 cashMoneyFlag =false;
				 $("#money_tips").html("请输入提现金额");
			 }else{
				 cashMoneyFlag =true;
				 $("#money_tips").html("");
			 }
			 
			 if(parseFloat($("#cashMoney2").val()) > parseFloat(useMoney2)){
				 cashMoneyFlag = false;
				 $("#money_tips").html("提现金额不能大于可用余额");
			 }else if(parseFloat($("#cashMoney2").val()) < parseFloat($("#minCashMoney").val())){
				 cashMoneyFlag = false;
				 $("#money_tips").html("提现金额不能小于"+$("#minCashMoney").val());
			 }else{
				 cashMoneyFlag =true;
				 $("#money_tips").html("");
			 }
			 
			 if($("#userName2").val()==""){
				 realNameFlag = false;
				 $("#userName_tips").html("请输入转账姓名");
			 }else{
				 realNameFlag = true;
				 $("#userName_tips").html("");
			 }
			 if($("#cardId2").val()==""){
				 cardIdFlag = false;
				 $("#cardId_tips").html("请输入身份证号");
			 }else{
				 cardIdFlag = true;
				 $("#cardId_tips").html("");
			 }
			 
			 if($("#bankNo2").val()==""){
				 bankNoFlag = false;
				 $("#bankNo_tips").html("请输入银行卡号");
			 }else{
				 bankNoFlag = true;
				 $("#bankNo_tips").html("");
			 }
			 
			 var selcity = $("#city2").val();
			 if($("#branch2").val()==""){
				 brachs = false;
				 $("#branch_tips2").html("请输入支行名称");
			 }else{
				 $("#branch_tips2").html("");
			 }
			 if(selcity.length==0){
				 citys = false;
				 $("#citys_tips2").html("请选择地区");
	  		 }else{
	  			$("#citys_tips2").html("");
	  		 }
		 }
		 
		 /*********线下提现  end**************/
		 
});

function loadCity(obj){
	var pc = $("#"+obj).children().length;
	if(pc<=1){  				
		var province = document.getElementById(obj);
	    var cityArr =  eval(cityCodeJson);
	    var num = 1;
	    $.each(cityArr,function(i,item){
	    	var obj = cityArr[i];
	    	if(obj.city===""){
	    		province[num]=new Option(obj.province,obj.province);
	        num++;
	    	}
	    })
	}
}

function getCityInfo(tem,tem2){
	var city = document.getElementById(tem2);
	city.options.length=1;
    var cityArr =  eval(cityCodeJson);
    var num = 1;
    $.each(cityArr,function(i,item){
    	var obj = cityArr[i];
    	if(obj.province===tem && obj.city!=""){
    		city[num]=new Option(obj.city,obj.city);
	        num++;
    	}
    })
}
function getBankInfo(obj,obj2){
	var cardval = obj.value;
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
					var html = "<span style='color:#8a8a8a;'><img src='/data/bank/llmini/"+bankCode+".png'> " + data.supportBank.bankName+"<span>";
					$("#"+obj2).html(html);
				}
			}
		})
	}
}