var useMoney,investMoney;
$(function(){
	$.ajax({
		url:"/cf/user/account.html",
		type:"POST",
		dataType:"json",
		success:function(res){
			useMoney = res.useMoney;
			investMoney = res.investMoney;
			$("#useMoney").html(useMoney+"元");
			$("#investMoney").html(investMoney+"元");
			if(null!=res.bankCode&&""!=res.bankCode){
				$(".bind-card").html("<img src='/data/bank/llmini/"+res.bankCode+".png' /><p style='position:relative;top:90px;'>"+res.bankNo+"</p>");
			}
			getPic();
		}
	})
})

function getPic(){
	var useMoney1 = parseFloat(useMoney/(useMoney+investMoney)*100).toFixed(2);
	var investMoney1 = parseFloat(investMoney/(useMoney+investMoney)*100).toFixed(2);
	
	if(0==useMoney&&investMoney!=0){
		investMoney1 = 100.00;
		useMoney1 = 0;
	}
	if(0!=useMoney&&investMoney==0){
		useMoney1 = 100.00;
		investMoney1 = 0;
	}
	if(useMoney==0&&investMoney==0){
		investMoney1 = 50.00;
		useMoney1 = 50.00;
	}
	
	Morris.Donut({
	    element: 'hero-donut',
	    data: [
	        {label: '可用余额', value:useMoney1 },
	        {label: '投资资产', value:investMoney1 },
	    ],
	    colors: ["#ffeab0", "#f74459"],
	    formatter: function (y) { return y + "%" }
	});
}
