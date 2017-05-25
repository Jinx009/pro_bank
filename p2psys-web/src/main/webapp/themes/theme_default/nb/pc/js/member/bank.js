$(function(){
		 
		 //我的银行卡信息
		var htmlStr="";
		 $.ajax({
			type:"post",
			url:"/nb/pc/cash/myBanks.html?random="+Math.random(),
			 dataType:"json", 
			success:function(json){
				if(checkUser(json.result)){	
					if(json.bankModel!=0){
							htmlStr +="<div class=\"leftMargin\"><dl class=\"col-md-3 padding_col0 hasedAddBank\">";
							htmlStr +="<dt class=\"bankIcon\"><img src=\"/data/bank/llbank/"+json.bankCode+".png\"/></dt>";
							htmlStr +="<dd class=\"addBankImg\">"+json.bankModel.hideBankNo+"</dd></dl></div>";
							htmlStr += "<input id=\"hidWebChannelKey\" value=\""+json.webChannelKey+"\" type=\"hidden\">";
					}else{

						htmlStr += "<div class=\"row margin0\">";
						htmlStr += "<p class=\"col-md-12 padding_col0 recharge_paddingleft\" style=\"color:#efb322;font-size: 24px;\">您暂无已认证的银行卡信息</p></div>";
						htmlStr += "<div class=\"row margin0\" style=\"margin-bottom: 77px;\">";
						htmlStr += "<p class=\"col-md-12 padding_col0 recharge_paddingleft\" style=\"color: #898989;font-size: 18px;\">请先进行不小于10元的充值，认证并绑定银行卡</p></div>";
						htmlStr += "<div class=\"form-group row margin0 \" style=\"margin-bottom: 0px;margin-left:30px;\">";
						htmlStr += "<a href=\"/nb/pc/recharge/newRecharge.html\" id=\"auth\" class=\"col-md-3 newChashBtn \">去认证</a></div>";

					}					
					$("#bankInfo_div").html(htmlStr);
				}else{
					showDiv("util_login");
				}
			}
		});
	});


