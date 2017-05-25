define(function(require, exports, module) {
	require('jquery');
	// 异步请求标的数据
	$.ajax({
		type : "get",
		url : "/ppfund/ppfundDetail.html" + window.location.search + "&random=" + Math.random(),
		dataType : "json",
		success : function(json) {
		// $("#loading_tip").slideUp();
		require.async("/plugins/handlebars-v1.3.0/handlebars-v1.3.0",
			function() {
				require.async("/plugins/handlebars-v1.3.0/transFormatJson",
					function() {
						// $(".invest-main-box").html(Handlebars.compile(require("../../wx/tpl/ppfundDetail.tpl"))(json));
						// 解决IE下不支持placeholder
						var tpl = require("../../wx/tpl/ppfundDetail.tpl");
						var template = Handlebars.compile(tpl);
						var html = template(json);
						$('#xm-con').html(html);
						$(".btn_close").hide();
						$("#btn").click(function() {
							$("#formDiv").show();
							$(".btn_close").show();
							$(".input-amount").keyup(function(){
								var flag = false;
								var use_money = parseFloat($("#use_money").val());//账户余额
								var most_Account = parseFloat($("#most_Account").val());//最大投资金额
								var lowest_Account = parseFloat($("#lowest_Account").val());//最小投资金额 
								var balance_val = parseFloat($("#balance-val").text().replace(/,/g,""));//可投金额
								var DATE_FORMAT = /^[0-9]{4}-[0-1]?[0-9]{1}-[0-3]?[0-9]{1}$/;//时间格式yyyy-MM-dd
								
								if (most_Account == 0) {
									most_Account = 100000000000;
								}
								if(use_money>=0 && balance_val<lowest_Account){
									if($("#input-amount").val() < 0.01){
										$(".msg_error").html("投资金额不能小于0.01元");
										return flag;
									}
									else if($("#input-amount").val() > use_money){
										$(".msg_error").html("账户余额不足,去<a href='/wx/account/newRecharge.html' class='ppfund_recharge'>充值</a>");
										return flag;
									}
									else if ($("#input-amount").val() > balance_val){
										$(".msg_error").html("投资金额不能大于可投金额");
										return flag;
									}
									else if ($("#input-amount").val() > most_Account){
										$(".msg_error").html("投资金额不能大于最大投资金额");
										return flag;
									}
									else{
										flag = true;
										$(".msg_error").html("");
									}
								}else if(use_money>=0 && balance_val>=lowest_Account){
									if($("#input-amount").val() < 0.01){
										$(".msg_error").html("投资金额不能小于0.01元");
										return flag;
									}
									else if($("#input-amount").val() > use_money){
										$(".msg_error").html("账户余额不足,去<a href='/wx/account/newRecharge.html' class='ppfund_recharge'>充值</a>");
										return flag;
									}
									else if ($("#input-amount").val() > balance_val){
										$(".msg_error").html("投资金额不能大于可投金额");
										return flag;
									}
									else if ($("#input-amount").val() < lowest_Account){
										$(".msg_error").html("投资金额不能小于最小投资金额");
										return flag;
									}
									else if ($("#input-amount").val() > most_Account){
										$(".msg_error").html("投资金额不能大于最大投资金额");
										return flag;
									}
									else{
										flag = true;
										$(".msg_error").html("");
									}
								}
								/**
								$("#outTime").keyup(function(){
									if ($("#outTime").val()=='') {
										$(".outTime_error").show();
										$(".outTime_error").html("预约赎回时间不能为空");
										return flag;
									}else if (!DATE_FORMAT.test($("#outTime").val())) {
										$(".outTime_error").show();
										$(".outTime_error").html("时间格式为:年-月-日");
										return flag;
									}else{
										flag = true;
										$(".outTime_error").hide();
									}
								})
								*/
								if(flag==true){
									$("#pay-pwd").keyup(function(){
										if($("#pay-pwd").val()!=''){
											$.ajax({
													type: "get",
													url: "/user/checkPayPwd.html?payPwd=" + $("#pay-pwd").val(),
													success:function(data){
														if(data.result){
															$(".pwd_error").html("");
															//$("#form1").submit();
														}else{
															$(".pwd_error").html("交易密码错误");
														}
													}
											})
										}else{
											if($("#pay-pwd").val()==''){
												$(".pwd_error").html("交易密码不能为空");
											}else{
												$(".pwd_error").hide();
											}
										}
									})
								}
								$("#pay-sure").click(function(){
								 	if(flag==true){
								 		if ($("#pay-pwd").val()!='') {
								 			$.ajax({
													type: "get",
													url: "/user/checkPayPwd.html?payPwd=" + $("#pay-pwd").val(),
													success:function(data){
														if(data.result){
															$(".pwd_error").html("");
															$("#form1").submit();
														}else{
															$(".pwd_error").html("交易密码错误");
														}
													}
											})
											
										}else{
											if($("#pay-pwd").val()==''){
												$(".pwd_error").html("交易密码不能为空");
											}else{
												$(".pwd_error").html("");
											}
										}
									}
									
								});
							});
						});
						
						$(".btn_close").click(function() {
							$(".btn_close").hide();
							$(".popup-div").hide();
						});
						
					});
			});
		}
	});
});
