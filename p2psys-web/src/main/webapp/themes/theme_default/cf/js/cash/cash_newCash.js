var cityCodeJson;
$(function(){
		 //我的提现信息
		var htmlStr="";
		 $.ajax({
			type:"post",
			url:"/cf/cash/newCash.html?random="+Math.random(),
			 dataType:"json", 
			success:function(json){
				if(json.bankModel!=0){
					$("#pic").html("<img src=\"/data/bank/llbank/"+json.bankCode+".png\"/>"+json.bankModel.hideBankNo)
					$("#useMoney").html(json.account.useMoney+"元");
				}else{
					htmlStr += "<div class=\"form-group\"><div class=\"col-sm-9 col-no-padding\">";
					htmlStr += "<p class=\"help-block\">请进行身份认证</p></div></div>";
					htmlStr += "<div class=\"form-group\"><div class=\"col-sm-9 col-no-padding\">";
					htmlStr += "<a href=\"/cf/user/recharge.html\">";
					htmlStr += "<button class=\"sure-btn\" type=\"button\">去认证</button></a></div></div>";
					$("#cashForm").html(htmlStr);
				}
				cityCodeJson = json.cityCodeList;
				$("#cashToken").val(json.nbCashToken);
				$("#isCityType").val("1");
//				if(json.webCashKey=="llpay_channel_key" && (json.bankModel.branch===undefined || json.bankModel.branch==="")) {
//					document.getElementById("cityCodeSpan").style.display="block";
//				}
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
					 url:"/cf/recharge/checkPayPwd.html?random="+Math.random(),
					  dataType:"json",
					  data:{
						  key:$("#payPwd").val()
					  },
					  success:function(json){
						  if("success"==json.result){
							  $.ajax({
									 type:'POST',
									 url:"/cf/cash/doAllCash.html?random="+Math.random(),
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
										  if("success"==json.flag){
											  layer.alert('提现申请成功',{title:false,closeBtn: 0},function(){
												  location.href = '/cf/user/main.html';
											  });
										  }else{
											  layer.alert(json.title+" - "+json.txt,{title:false,closeBtn: 0});
										  }
									  }
								 });
						  }else{
							  if(json.error_time <=4){
								  layer.alert("交易密码不正确,您还可以输入"+(5-json.error_time)+"次",{title:false,closeBtn: 0});
							  }else{
								  layer.alert("交易密码被锁定,24小时内无法进行账户交易",{title:false,closeBtn: 0});
							  }
							 
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
			url:"/cf/cash/checkBankCanUse.html?cardNo="+cardval,
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