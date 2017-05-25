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
											var use_money = parseInt($("#use_money").val());//账户余额
											var most_Account = $("#most_Account").val();//最大投资金额
											var lowest_Account = $("#lowest_Account").val();//最小投资金额 
											var balance_val = parseInt($("#balance-val").text().replace(/,/g,""));//可投金额
											
											if($("#input-amount").val() < 0.01){
												$(".msg_error").show();
												$(".msg_error").html("投资金额不能小于0.01元");
												return flag;
											}
											else if($("#input-amount").val()> use_money){
												$(".msg_error").show();
												$(".msg_error").html("投资金额不能大于账户余额");
												return flag;
											}
											else if ($("#input-amount").val() > balance_val){
												$(".msg_error").show();
												$(".msg_error").html("投资金额不能大于可投金额");
												return flag;
											}
											else if ($("#input-amount").val() < lowest_Account){
												$(".msg_error").show();
												$(".msg_error").html("投资金额不能小于最小投资金额");
												return flag;
											}
											else if ($("#input-amount").val() > most_Account){
												$(".msg_error").show();
												$(".msg_error").html("投资金额不能大于最大投资金额");
												return flag;
											}
											else{
												flag = true;
												$(".msg_error").hide();
											}
											 $("#pay-sure").click(function(){
											 	if(flag==true){
													if($("#pay-pwd").val()==''){
														$(".pwd_error").show();
														$(".pwd_error").html("交易密码不能为空");
													}else{
														$(".pwd_error").hide();
														$("#form1").submit();
													}
												}
												});
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
