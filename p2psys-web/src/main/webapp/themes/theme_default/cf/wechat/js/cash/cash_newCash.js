var cityCodeJson;
var isBank;

//在线提现变量
var cashMoney = false;
var brachs = true;
var citys = true;

//在线提现变量
var cashMoneyFlag=false;
var realNameFlag = false;
var cardIdFlag = false;
var bankNoFlag = false;
var brachsFlag = false;
var citysFlag = false;

$(function(){
		 //我的提现信息
		var htmlStr="";
		 $.ajax({
			type:"post",
			url:"/cf/wechat/cash/cash.html?random="+Math.random(),
			 dataType:"json", 
			success:function(json){
				if(json.bankModel!=0){
					isBank=1;
					$(".product-detail-btn").text("线上提现");
		        	$(".product-detail-btn").attr("onclick","sub()");
				}else{
					isBank=0;
					htmlStr += "<div class=\"recharge-tip\"><div class=\"recharge-tip-left\"></div>";
					htmlStr += "<div class=\"recharge-tip-right\"><h3>请进行身份认证</h3></div></div>";
					$("#cashForm").html(htmlStr);
					$(".product-detail-btn").text("去认证");
		        	$(".product-detail-btn").attr("onclick","auth()");
				}
				cityCodeJson = json.cityCodeList;
				$("#useMoney").val(json.account.useMoney);
				$("#userName2").val(json.realName);
				$("#cardId2").val(json.cardId);
				$("#bankNo2").val(json.bankModel.bankNo);
				$("#minCashMoney").val(json.minCashMoney);
				$("#cashMoney2").val(json.minCashMoney);
				$("#minCashMoneyTis").html(json.minCashMoney);
				$("#cashToken").val(json.nbCashToken);
				if(json.wapCashKey=="llpay_channel_key" && (json.bankModel.branch===undefined || json.bankModel.branch==="")) {
					$("#isCityType").val("1");
					document.getElementById("cityCodeSpan").style.display="block";
				}
			}
		});
		 
		 
		 $('input[name="money"]').blur(function(){
			 var useMoney = $("#useMoney").val();
			 if($(this).val() !=""){
				 if($(this).val() > parseFloat(useMoney)){
					 cashMoney =false;
					 $("#onlineError").html("提现金额不能大于可用余额");
				 } else if(parseFloat($(this).val()) <= 0){
					 cashMoney =false;
					 $("#onlineError").html("提现金额必须大于0元");
				 }else{
					 cashMoney =true;
					 $("#onlineError").html(" ");
				 }
				 
			 }else{
				 cashMoney = false;
				 $("#onlineError").html("请输入提现金额");
			 }
			
		 });

		 $('input[name="branch"]').blur(function(){
			 if($(this).val()==""){
				 brachs = false;
				$("#onlineError").html("请输入支行名称");
			}else{
				brachs = true;
				$("#onlineError").html("");
			}
		 });
		 
		

		 
		 
		 
		/*********线下提现  begin**************/
		
		 $('input[name="cashMoney2"]').blur(function(){
			 var useMoney = $("#useMoney").val();
			 if($(this).val() ==""){
				 cashMoneyFlag =false;
				 $("#onlineError2").html("请输入提现金额");
			 }else{
				 if(parseFloat($("#cashMoney2").val()) < parseFloat($("#minCashMoney").val())){
					 cashMoneyFlag =false;
					 $("#onlineError2").html("提现金额不能小于"+$("#minCashMoney").val());
				 }else if($(this).val() > parseFloat(useMoney)){
					 cashMoneyFlag =false;
					 $("#onlineError2").html("提现金额不能大于可用余额");
				 }else{
					 cashMoneyFlag =true;
					 $("#onlineError2").html("");
				 }
			 }
			
		 });
		 
		 $('input[name="realName"]').blur(function(){
			 if($(this).val() ==""){
				 realNameFlag = false;
				 $("#onlineError2").html("请输入转账姓名");
			 }else{
				 realNameFlag = true;
				 $("#onlineError2").html("");
			 }
		 });
		 
		 $('input[name="cardId"]').blur(function(){
			 if($(this).val()==""){
				 cardIdFlag = false;
				 $("#onlineError2").html("请输入身份证号");
			 }else{
				 if(!isIdCardNo($(this).val().trim())){
					 cardIdFlag = false;
					 $("#onlineError2").html("身份证号格式不正确");
				 }else{
					 cardIdFlag = true;
					 $("#onlineError2").html("");
				 }
			 }
		 });
		 
		 $('input[name="bankNo2"]').blur(function(){
			if($(this).val()==""){
				bankNoFlag = false;
				 $("#onlineError2").html("请输入银行卡号");
			}else{
				bankNoFlag = true;
				 $("#onlineError2").html("");
			}
		 });
		 
		 $('input[name="branch2"]').blur(function(){
			 if($(this).val()==""){
				 brachsFlag = false;
				$("#onlineError2").html("请输入支行名称");
			}else{
				brachsFlag = true;
				$("#onlineError2").html("");
			}
		 });
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
			url:"/cf/wechat/cash/checkBankCanUse.html?cardNo="+cardval,
			type:"get",
			dataType:"json",
			success:function(data){
				if(data.result==false){
					$("#"+obj2).html("<span style='color:red;' id='bankPPError'>"+data.message+"<span>");
				}else{
					var bankCode = data.supportBank.bankCode;
					var html = "<span style='color:#8a8a8a;'><img src='/data/bank/llmini/"+bankCode+".png' style='width:16px;height:16px;'> " + data.supportBank.bankName+"<span>";
					$("#"+obj2).html(html);
				}
			}
		})
	}
}

//去认证
function auth(){
	 window.location.href="/cf/wechat/user/recharge.html"
}

//提现到我的银行卡
function sub(){
	 checkAll_online();
	if(cashMoney && brachs && citys){
		$.ajax({
			 type:'POST',
			 url:"/cf/wechat/cash/doAllCash.html?random="+Math.random(),
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
						  location.href = '/cf/wechat/user/index.html';
					  });
				  }else{
					  layer.alert(json.title+','+json.txt,{title:false,closeBtn: 0});
				  }
			  }
		 });
	}
}

function checkAll_online(){
	 var useMoney = $("#useMoney").val();
	 if($("#money").val() ==""){
		 cashMoney = false;
		 $("#onlineError").html("请输入提现金额");
	 }else if(parseFloat($("#money").val()) > parseFloat(useMoney)){
		 cashMoney = false;
		 $("#onlineError").html("提现金额不能大于可用余额");
	 }else if($("#isCityType").val()==="1"){	
		 cashMoney = true;
		 var selcity = $("#city").val();
		 if($("#branch").val()==""){
			 brachs = false;
			 $("#onlineError").html("请输入支行名称");
		 }else if(selcity.length==0){
			 cashMoney = true;
			 brachs = true;
			 citys = false;
			 $("#onlineError").html("请选择地区");
 		 }else{
 			cashMoney = true;
			brachs = true;
			citys = true;
 			$("#onlineError").html("");
 		 }
	 }else{
		 cashMoney = true;
		 brachs = true;
		 citys = true;
	 }
}

//线下提现
function offSub(){
	 checkAll();
	 if(cashMoneyFlag && realNameFlag && cardIdFlag && bankNoFlag && brachsFlag){
		 $.ajax({
				type:"post",
				url:"/cf/wechat/cash/doAllOfflineCash.html?random="+Math.random(),
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
					if("success"==json.flag){
						layer.alert('提现申请成功',{title:false,closeBtn: 0},function(){
							location.href = '/cf/wechat/user/index.html';
						});
					  }else{
						  layer.alert(json.title+','+json.txt,{title:false,closeBtn: 0});
					  }
				}
		 });
	 }
}
function checkAll(){
	 var useMoney2 = $("#useMoney").val();
	 var selcity = $("#city2").val();
	 if($("#cashMoney2").val()==""){
		 cashMoneyFlag =false;
		 $("#onlineError2").html("请输入提现金额");
	 }else if(parseFloat($("#cashMoney2").val()) > parseFloat(useMoney2)){
		 cashMoneyFlag = false;
		 $("#onlineError2").html("提现金额不能大于可用余额");
	 }else if(parseFloat($("#cashMoney2").val()) < parseFloat($("#minCashMoney").val())){
		 cashMoneyFlag = false;
		 $("#onlineError2").html("提现金额不能小于"+$("#minCashMoney").val());
	 }else if($("#userName2").val()==""){
		 cashMoneyFlag =true;
		 realNameFlag = false;
		 $("#onlineError2").html("请输入转账姓名");
	 }else if($("#cardId2").val()==""){
		 cashMoneyFlag =true;
		 realNameFlag = true;
		 cardIdFlag = false;
		 $("#onlineError2").html("请输入身份证号");
	 }else if($("#bankNo2").val()==""){
		 cashMoneyFlag =true;
		 realNameFlag = true;
		 cardIdFlag = true;
		 bankNoFlag = false;
		 $("#onlineError2").html("请输入银行卡号");
	 }else if($("#branch2").val()==""){
		 cashMoneyFlag =true;
		 realNameFlag = true;
		 cardIdFlag = true;
		 bankNoFlag = true;
		 brachsFlag = false;
		 $("#onlineError2").html("请输入支行名称");
	 }else if(selcity.length==0){
		 cashMoneyFlag =true;
		 realNameFlag = true;
		 cardIdFlag = true;
		 bankNoFlag = true;
		 brachsFlag = true;
		 citysFlag = false;
		 $("#onlineError2").html("请选择地区");
	 }else{
		 cashMoneyFlag =true;
		 realNameFlag = true;
		 cardIdFlag = true;
		 bankNoFlag = true;
		 brachsFlag = true;
		 citysFlag = true;
		$("#onlineError2").html("");
	 }
}