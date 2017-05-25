define(function(require , exports , module){
	//S-添加银行卡
	$( "#addbank_form" ).dialog({ autoOpen: false , modal: true ,height: 550,width:580});
	$('.addbank').click(function(){
		$( "#addbank_form" ).dialog( "open" );
	});
		$("#addbank_form").validate({
			rules:{
				branch:{
					required:true
				},
				account:{
					required:true
				},
				comfirm_account:{
					required:true,
					equalTo:"#infoyhzh"
				}
			},
			messages:{
				branch:{
					required:"请输入开户行"
				},
				account:{
					required:"卡号不能为空"
				},
				comfirm_account:{
					required:"输入确认卡号"
				}
			},
			submitHandler:function(form,event,validator){
				$(form).ajaxSubmit(function(data){
					if(data.result==true)
					{
						reLoad()
					}else{
						$(form).find(".errorMsg").text(data.msg)
					}	
				})
			}
		});	
	//银行卡号4位一空格处理
	function splitBank(){
		var bank = $(".addbanknum");
		$("body").append("<div class='ui-cardbox' style='display:none;'></div>")
		bank.each(function(){
			var that = $(this)
			var cardBox = $(".ui-cardbox"); 
			
			that.focus(function(){
				creditBox(that)
				cardBox.show()
				var values = $(this).val()
				replaceVal(values)
				
			}).blur(function(){
				cardBox.hide();	
			})
			that.bind("keyup blur",function(){
				var values = $(this).val()
				replaceVal(values)
			})
			
			function creditBox(obj,status){
				var leftVal = obj.offset().left;
				var topVal = obj.offset().top;
				var heightVal = obj.height()+10;
				var widthVal = obj.width();
				topVal-=heightVal;
				
				cardBox.css({"position":"absolute","left":leftVal+"px","top":topVal+"px","width":widthVal+"px","height":heightVal+"px",
				"line-height":heightVal+"px","z-index":9999})
				
			}
			function replaceVal(val){
				values=val.replace(/(.{4})/g,"$1 ");
				cardBox.text(values)
			}
		})
	}
	splitBank();
	
	//E-添加银行卡
	
	//单选框添加样式
	$.fn.extend({
		addInputCls:function(options){
			var defaults = {
					box:".J_cashbank", //父级对象
					addCls:"hover",
					childLi:"li"	//单个对象
			}
			var options = $.extend(defaults,options);
			this.each(function(){
				var o = options;
				var that = $(o.box).find(o.childLi);
				that.css({"cursor":"pointer"})
				$("li:first" , o.box).find("input").attr("checked",true);
				that.each(function(){
					$(this).click(function(){
						that.removeClass(o.addCls)
						$(this).addClass(o.addCls);
						$("input",o.box).removeAttr("checked")
						$("input" , $(this)).prop("checked",true);
				})
				})
			})
		}
	});
	//选择提现银行
	$(".J_cashbank").addInputCls();
	
	//VIP,专属客服选择效果显示
	$(".J_vip_box").addInputCls({box:".J_vip_box"});
	
	
	$(".J_cashbank li").each(function(){
		var that = $(this);
		$(".J_cashbank li:first").find("input").attr("checked",true);
		that.click(function(){
			$(".J_cashbank input").removeAttr("checked")
			$("input" , $(this)).attr("checked",true);
		})
	});
	
			
		//隐藏完成充值的提示框
		$("#close_wd").click(function(){
			$(".ui-widget-overlay").remove();	
			$(".ui-dialog").remove();	
		})
		

		function payment(){
	 		var type = GetRadioValue("type");
			if (type==1){
				$("#returnpay").html("<font color='red'>请到打开的新页面充值</font>");
				
			}
			
		}
		function ctype(){
		var resualt=false;
		
			layer.alert(document.form1.payment2.length);
			for(var i=0;i<document.form1.payment2.length;i++)
			{
				
				if(document.form1.payment2[i].checked)
				{
				  resualt=true;
				}
			}
			return resualt;
		}
        $("#js_recharge").keyup(function(){
        	var obj = $(this)
            if (parseFloat(obj.val()) > 0 ) 
            {
            	 var realMoney=parseFloat(obj.val());
 				 realMoney = parseInt($("#use_money").text())+realMoney;
                 document.getElementById("real_money").innerText = realMoney ;
            }
        })
        
        
        //提现页面---------------------------------------------------------
        $("#js_sendPhone").click(function(){
        	doajax();
        })
        function doajax(){
            var Time;
            var phone = jQuery('#phone').val();	
            var now = new Date(); 
            var gettime=now.getTime();
            var phoneBtn = $(".timeval");
            var timeVal = parseInt("60");
            Time = setInterval(function(){
                timeVal--;
                if(timeVal>0)
                {
                    phoneBtn.val(timeVal+"秒后重新获取").attr("disabled","disabled");
                }
                else{
                    timeVal="60";
                    phoneBtn.val("获取验证码").removeAttr("disabled","disabled");
                    clearInterval(Time);
                    }
                },1000) 
            
            $(function(){
                $.ajax({
                    "url": "/member/identify/mobileaccess.html?mobile="+phone+"&phone_type="+1,
                    "type": "post",
                    "cache": "false",
                    "data": {'name':$("#phone").val()},
                    "dataType": "json",
                    "error": function(){
                    },
                    "success": function(data){
                        if(data != null){
                        }
                    }
                });
            });
        
        }
        function sms(){
            var phone = jQuery('#phone').val();
            layer.alert(phone);
            var valicode = jQuery('#valicode').val();
            if(phone==''){
                layer.alert('手机号码不能为空'); 
                return false;
            }else{
                
                    $(function(){
                        $.ajax({
                            "url": "${webroot}/member/identify/phoneSms.html?mobile="+phone+"&valicode="+valicode,
                            "type": "post",
                            "cache": "false",
                            "data": {'name':$("#phone").val()},
                            "dataType": "json",
                            "error": function(){
                            },
                            "success": function(data){
                                if(data != null){
                                    var error= $(".error").html(data.data);
                                    if(error!=''){
                                        $("#msg25").css("display","block");

                                    }
                                }
                            }
                        });
                    });
                }
        
            
        }
        //S-提现金额实际到账
        $(".J_cash_money").blur(function(){
        	commit($(this))
        })
       function commit(obj) {
            if (parseFloat(obj.val()) > 0 )
            {
                var realMoney=parseFloat(obj.val());
                var inputValue=parseFloat(obj.val());
                var wz=(inputValue+"").indexOf(".");
                 if(wz>0){
                        var curNum=inputValue.toString().split(".")[0].length+3;
                        inputValue=((inputValue+"").substring(0,curNum));
                        obj.val(inputValue);
                }
                if(inputValue<=30000 && inputValue>100){
                    realMoney=parseFloat(inputValue-3);
                }else if(30000<inputValue && inputValue<=50000){
                    realMoney=parseFloat(inputValue-5);
                }else if(inputValue>50000 || inputValue <100){
                    layer.alert("您好，提现资金不能低于100元高于50000元");
                    obj.value=0;
                    document.getElementById("real_money").innerText = 0 ;
                    return;
                }
                if(inputValue>50000){
                    layer.alert("您好，大额提现金额必须小于5万");
                    obj.value=0;
                    document.getElementById("real_money").innerText = 0 ;
                    return;
                }
               
                
                var cashAmount;
                cashAmount = parseFloat(inputValue);
                
                //getCashFeeValue(cashAmount);
                //document.getElementById("real_money").innerText = realMoney ;
            }
        }
        
        function getCashFeeValue(cashAmount){
            jQuery.ajax({
                url:"${webroot}/member/account/getAvailableCash.html",
                dataType:'json', 
                data:"money="+cashAmount,
                success:function(json){
                    $("#real_money").html(changeTwoDecimal(cashAmount-json.data.fee));
                }
            });
        }
        
        function changeTwoDecimal(x)
        {
            var f_x = parseFloat(x);
            if (isNaN(f_x))
            {
                layer.alert('function:changeTwoDecimal->parameter error');
                return false;
            }
            var f_x = Math.round(x*100)/100;
            return f_x;
        };
        
        //E-提现金额实际到账
                
       
})