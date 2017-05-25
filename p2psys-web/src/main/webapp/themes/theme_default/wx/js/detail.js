define(function(require, exports, module) {
	require('jquery');
	// 异步请求标的数据
	$.ajax({
			type : "get",
			url : "/invest/borrowDetail.html" + window.location.search,
			dataType : "json",
			cache : false,
			success : function(json) {
				// $("#loading_tip").slideUp();
				require.async("/plugins/handlebars-v1.3.0/handlebars-v1.3.0",
					function() {
						require.async("/plugins/handlebars-v1.3.0/transFormatJson",
								function() {
									// $("#invest-main-box").html(Handlebars.compile(require("../../wx/tpl/detail.tpl"))(json));
									var tpl = require('../../wx/tpl/detail.tpl');
									var template = Handlebars.compile(tpl);
									var html = template(json);
									$("#xm-con").html(html);
									$(".btn_close").hide();
									$("#btn").click(function() {
										$("#formDiv").show();
										$(".btn_close").show();
										$(".input-amount").keyup(function(){
											var flag = false;
											var payPwd_sure = false;
											var tradersPwd_sure = false;
											var use_money = parseFloat($("#use_money").val());//账户余额
											var most_Account = parseFloat($("#most_Account").val());//最大投资金额
											var lowest_Account = parseFloat($("#lowest_Account").val());//最小投资金额 
											var balance_val = parseFloat($("#balance-val").text().replace(/,/g,""));//可投金额

											if(use_money>=0 && balance_val<lowest_Account){
												if($("#input-amount").val() < 0.01){
													$(".msg_error").html("投资金额不能小于0.01元");
													return flag;
												}
												else if($("#input-amount").val()> use_money){
													$(".msg_error").html("账户余额不足,去<a href='/wx/account/newRecharge.html' class='detail_recharge'>充值</a>");
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
												else if($("#input-amount").val()> use_money){
													$(".msg_error").html("账户余额不足,去<a href='/wx/account/newRecharge.html' class='detail_recharge'>充值</a>");
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
											if(flag==true){
												$("#pay-pwd").keyup(function(){
													if($("#pay-pwd").val()!=''){
														$.ajax({
																type: "get",
																url: "/user/checkPayPwd.html?payPwd=" + $("#pay-pwd").val(),
																success:function(data){
																	if(data.result){
																		$(".payPwd_error").html("");
																		//$("#form1").submit();
																	}else{
																		$(".payPwd_error").html("交易密码错误");
																	}
																}
														})
													}else{
														if($("#pay-pwd").val()==''){
															$(".payPwd_error").html("交易密码不能为空");
														}else{
															$(".payPwd_error").hide();
														}
													}
												})
											}
											/* 当$("#istraders_pwd").val()==1 时，说明有定向标*/
											if($("#istraders_pwd").val()==1){
												$("#pay-sure").click(function(){
												 	if(flag==true){
												 		if ($("#pay-pwd").val()!='' && $("#traders_pwd").val()!='') {
												 			$.ajax({
																	type: "get",
																	url: "/user/checkPayPwd.html?payPwd=" + $("#pay-pwd").val(),
																	success:function(data){
																		if(data.result){
																			$(".payPwd_error").html("");
																			$("#form1").submit();
																		}else{
																			$(".payPwd_error").html("交易密码错误");
																		}
																	}
															})
															
														}else{
															if($("#pay-pwd").val()==''){
																$(".payPwd_error").html("交易密码不能为空");
															}else{
																$(".payPwd_error").html("");
															}
															if($("#traders_pwd").val()==''){
																$(".pwd_error").html("定向密码不能为空");
															}else{
																$(".pwd_error").html("");
															}
														}
													}
													
												});
											}else{
												$("#pay-sure").click(function(){
												 	if(flag==true){
												 		if ($("#pay-pwd").val()!='') {
												 			$.ajax({
																	type: "get",
																	url: "/user/checkPayPwd.html?payPwd=" + $("#pay-pwd").val(),
																	success:function(data){
																		if(data.result){
																			$(".payPwd_error").html("");
																			$("#form1").submit();
																		}else{
																			$(".payPwd_error").html("交易密码错误");
																		}
																	}
															})
															
														}else{
															if($("#pay-pwd").val()==''){
																$(".payPwd_error").html("交易密码不能为空");
															}else{
																$(".payPwd_error").html("");
															}
														}
													}
													
												});
											}
										});
									});

									$(".btn_close").click(function() {
										$(".btn_close").hide();
										$(".popup-div").hide();
									});
								
								})
					})
			}
		});
	
	
});
