var cityCodeJson;
$(function(){
		 //我的提现信息
		var htmlStr="";
		 $.ajax({
			type:"post",
			url:"/cf/cash/newCash.html?random="+Math.random(),
			 dataType:"json", 
			success:function(json){
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
				$("#isCityType").val("1");
			}
		});
		 
		 
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
						url:"/cf/cash/doAllOfflineCash.html?random="+Math.random(),
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
								       layer.alert("提现申请成功！",{title:false,closeBtn: 0},function(){
											 location.href = '/cf/user/main.html';
										});
								  }else{
									  layer.alert(json.title+" - "+json.txt,{title:false,closeBtn: 0});
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