$(function(){
		 
		 //我的体验金信息
		var htmlStr="";
		 $.ajax({
			type:"post",
			url:"/nb/pc/goldInfo.html?random="+Math.random(),
			 dataType:"json", 
			success:function(json){
				if(checkUser(json.result)){	
					if(json.egFlag){
							htmlStr +="<dl class=\"col-md-2 text experience_col padding_col0\">";
							htmlStr +="<dt class=\"\"><img src='/themes/theme_default/nb/pc/img/experience_bg.png' style='width: 846px;'></dt>";
							if(json.isBindC>0){
								htmlStr +="<a href=\"/nb/pc/product/product_list.html?id=2\"><dd class=\"useBtn2\">去体验</dd></a></dl>";
							}else{
								htmlStr +="<a href=\"/nb/pc/recharge/newRecharge.html\"><dd class=\"useBtn2\">去认证</dd></a></dl>";
							}
					}else{
						htmlStr += "<dl class=\"col-md-2 text experience_col padding_col0\"><dt class=\"\"><img src='/themes/theme_default/nb/pc/img/experience_usedBg.png' style='width: 846px;'></dt></dl>";
					}
					$("#experienceInfo_div").html(htmlStr);
				}else{
					showDiv("util_login");
				}
			}
		});
	});


