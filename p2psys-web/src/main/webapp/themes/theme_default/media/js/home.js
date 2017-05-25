define(function(require,exports,modlue){
	require('jquery');
	require.async('common1',function(){
		$(".investindex-product").tabChange({
			isClick:true,
            isHover:false,
			childLi:".product-right ul li",//tab选项卡
			childContent:".product-left-con",//tab内容
			hoverClassName:"active",//选中当前选项卡的样式
			callBack:false	
		});
	});

$(function(){
	var aa = String($(".num").html())
	var bb = aa.split("")
	var bblen = bb.length;
	var str="<span class='data-num-tip'></span>"
	for (var i = 0; i < bblen; i++) { 
		str+="<span>"+bb[i]+"</span>"
	}
	$(".data-num").html(str)
})


function DY_scroll(wraper, prev, next, img, speed, or) {
        var wraper = $(wraper);
        var prev = $(prev);
        var next = $(next);
        var img = $(img).find('ul');
        var w = img.find('li').outerWidth(true);
        var s = speed;
        next.click(function () {
            img.animate({ 'margin-left': -w}, function () {
                img.find('li').eq(0).appendTo(img);
                img.css({ 'margin-left': 0 });
            });
        });
        prev.click(function () {
            img.find('li:last').prependTo(img);
            img.css({ 'margin-left': -w});
            img.animate({ 'margin-left': 0 });
        });
        if (or == true) {
            ad = setInterval(function () { next.click(); }, s * 1000);
            wraper.hover(function () { clearInterval(ad); }, function () { ad = setInterval(function () { next.click(); }, s * 1000); });
    
        }
    }
    DY_scroll('.img-scroll', '.prev', '.next', '.img-list', 3, false); // true为自动播放，不加此参数或false就默认不自动
	  


	require.async('jquery.nstSlider.min',function(){
		 $('.nstSlider').nstSlider({
                "left_grip_selector": ".leftGrip",
                "value_changed_callback": function(cause, leftValue, rightValue) {
                    $(this).parent().find('.leftLabel').text(leftValue);
                    var leftLabel = $(".leftLabel").val();

                    var money=1.12
                    for (var i = 1; i < 66; i++) {
                    	money=money*1.12
                    };
                    money = (leftValue*money-leftValue).toFixed(2)
                	$(".tipmoney em").html(money);
                }
               
            });
	});



	  require.async(["commonJS/keep","commonJS/rollTo"],function(){
	  				$(".header-top").keep(function(){
	  					$("#header").removeClass("hide")
	  					$(".header-top").css("position","relative")
	  				},function(){
	  					$("#header").addClass("hide")
	  				})
             })
	
	//首页轮播图片加载
	$.ajax({
		url:"/index/articleList.html?nid=scrollPic&random="+Math.random(),
		type:"post",
		success:function(result){
			var data = result.data;
			var len = data.length;
			var str = "";
			for(i=0;i<len;i++){
				str+="<li style='background:url("+data[i].picPath+") no-repeat center 0'><a href='"+data[i].introduction+"' ></a></li>";
			}
			$(".banner_con").html(str);
			require.async('./jquery.flexslider-min',function(){
				function funB(){
					$('.flexslider').flexslider({
						directionNav: true,
						pauseOnAction: false
					});
				}  
				setTimeout(funB, 10000);
			})
		}
	});
	
	//页面加载时，判断是否登陆，并显示相应的登陆框口或已登陆框
	$(function(){
		if(session_status==1){
			$(".login_before").hide();
    		$(".logined").show();				
		}else{
			$(".logined").hide();
			$(".login_before").show();
    		
		}
	})
	
	var count = $("#count").val();
	if( count == 1){
		$(".showForgetBox").before('<dd><input class="code" name="validCode" autocomplete="off" placeholder="验证码" type="text" maxlength="4" /><img src="/validimg.html" align="absmiddle" class="valicode_img" alt="点击刷新" /></dd>');
	    $(".valicode_img").click(function(){
	    	$(this).attr("src",'/validimg.html?t=' + Math.random());
	    });
	    $(".login_bg").css("height","380px");
	}
	//首页登陆验证
	require.async('/plugins/jquery-validation-1.13.0/jquery.validate.min',function(){
		require.async('/plugins/jquery-validation-1.13.0/additional-methods',function(){
			$("#loginForm").validate({
				rules:{
					userName: {
			    		required: true,
			    		minlength: 4
			    	},
			    	pwd: {
			    		required: true,
			    		regexPassword:true
			    	},
			    	validCode: {
			    		required: true,
			    		minlength: 4
			    	}
			    },
			    messages:{
			    	userName:{
			    		required: "请输入用户名",
			    		minlength: "用户名格式错误"
			    	},
			    	pwd: {
			    		required: "请输入密码",
			    		regexPassword:"密码格式错误"
			    	},
			    	validCode: {
			    		required: "请输入验证码",
			    		minlength: "验证码格式错误"
			    	}
			   },
			   showErrors: function(errorMap, errorList) {
			    	this.defaultShowErrors();
			    	$(".showErrorTips div").css("display","none");
			    	if(errorMap.userName){
			    		$(".errorTips b").html(errorMap.userName);
			    		$(".showErrorTips div").css("display","block");
			    	}else if(errorMap.pwd){
			    		$(".errorTips b").html(errorMap.pwd);
			    		$(".showErrorTips div").css("display","block");
			    	}else if(errorMap.validCode){
			    		$(".errorTips b").html(errorMap.validCode);
			    		$(".showErrorTips div").css("display","block");
			    	}else{
			    		$(".errorTips b").html('');
			    	}
			    },
			    errorPlacement:function(error,element){
			    },
			    submitHandler:function(form){
			    	require.async('jquery.form',function(){
				    	$("form").ajaxSubmit(function(data){
				        	if(data.result==true){
				        		if(data.userNature == 2){
				        			window.location.href = "/member/main.html";
				        		}else{
				        			//顶部
					        		$(".loginStatus").html('欢迎您，&nbsp;&nbsp;<a href="/member/main.html">'+data.userName+'</a>　|　<a href="/user/logout.html" title="退出登录">退出登录</a>　|　<a href="/help/guide.html" class="guide">新手指引</a>')
					        		//登陆框
					        		$(".login_before").hide();
					        		$(".logined").show();	      
					        		$(".logined_name").text(data.userName);
					        		$(".userImg").attr("src",data.imgurl);
					        		$(".login_bg").css("height","380px");
				        		}
				        	}else{
				        		$(".showErrorTips div").css("display","block");
				        		$(".errorTips b").html(data.msg);
				        		if(data.count){
				        			$(".showForgetBox").before('<dd><input class="code" name="validCode" autocomplete="off" placeholder="验证码" type="text" maxlength="4" /><img src="/validimg.html" align="absmiddle" class="valicode_img" alt="点击刷新" /></dd>');
				        		    $(".valicode_img").click(function(){
				        		    	$(this).attr("src",'/validimg.html?t=' + Math.random());
				        		    });
				        		    $("input[typ='validCode']").val('');
				        		    $(".login_bg").css("height","380px");
				        		}else{
				        			$(".errorTips b").html(data.msg);
				        			$("#email").val(data.email)
					        		$("#userid").val(data.userid);
					        		$("input[typ='validCode']").val('');
					        		$(".valicode_img").each(function(){
										$(this).attr("src",'/validimg.html?t=' + Math.random());
					        		})
				        		}
	
				        	}
				    	});   
			    	})
			     }  
			});
		})
	})
	
	$("#reset_email").live("click",function(){
    	$.ajax({
    		url:"/user/sentActivationEmail.html?userId="+$("#userid").val(),
    		type:"post",
    		data:{email:$("#email").val()},
    		success:function(data){
    			if(data)
    			{
    				loginEmail($("#email").val());
    			}
    			else
    			{
    				$(".errorTips b").html("邮件发送失败！");
    			}
    			
    		}
    	});
	});
	
	//["qq.com","gmail.com","126.com","163.com","hotmail.com","yahoo.com","yahoo.com.cn","live.com","sohu.com","sina.com"]	
	//点击登录邮箱地址
    function loginEmail(emailValue){
    	var email_suffix = (emailValue.split("@"))[1];
    	var loginEmailValue = "";
    	switch (email_suffix)
    	{
    		case "qq.com":
    			loginEmailValue = "mail.qq.com";
    			break;
    		case "gmail.com":
    			loginEmailValue = "mail.google.com";
    			break;
    		case "126.com":
    			loginEmailValue = "mail.126.com";
    			break;
    		case "163.com":
    			loginEmailValue = "mail.163.com";
    			break;
    		case "hotmail.com":
    			loginEmailValue = "login.live.com";
    			break;
    		case "yahoo.com":
    			loginEmailValue = "login.yahoo.com";
    			break;
    		case "live.com":
    			loginEmailValue = "login.live.com";
    			break;
    		case "sohu.com":
    			loginEmailValue = "mail.sohu.com";
    			break;
    		case "sina.com":
    			loginEmailValue = "mail.sina.com";
    			break;	
    	}
    	if(loginEmailValue)
    	{
    		$(".errorTips b").html('邮件已发送，点击<a href="http://'+loginEmailValue+'" target="_blank">立即激活</a>');
    	}
    	else
    	{
    		$(".errorTips b").html("邮件已发送，请注意查收！");
    	}
    }

    //判断时间是否小于10月10日10点10分
    if((new Date()).valueOf() < 1412907000000)
    {
    	$(".countDown").css("display","block").html('<img src="/themes/theme_default/images/Countdown.png" class="CountdownImg" />');
    }
    else
    {
    	$(".countDown").css("display","block");
    }
	
	//投标列表
	$.ajax({
		url:"/indexJson.html?isNovice=0&random="+Math.random(),
		type:"get",
		dataType:"json",
		error:function(){},
		success:function(data){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					var tpl = require('/themes/theme_default/media/tpl/home_invest.tpl');//载入tpl模板
					var template = Handlebars.compile(tpl);
					var html = template(data);
					$(".index-list ul").html(html);
					//不同的进度，更换成不同的颜色
					require.async('/plugins/jquery.knob/jquery.knob.min',function(){
						$('.knob').each(function(){
							var val = parseInt($(this).val());
							if(val <= 25)
							{
								$(this).attr("data-fgColor","#5bc0de");
							}
							else if(val > 25 && val <= 50)
							{
								$(this).attr("data-fgColor","#12adff");
							}
							else if(val > 50 && val < 100)
							{
								$(this).attr("data-fgColor","#28b726");
							}
							else
							{
								var isIE = function(ver){
								    var b = document.createElement('b')
								    b.innerHTML = '<!--[if IE ' + ver + ']><i></i><![endif]-->'
								    return b.getElementsByTagName('i').length === 1
								}
								if(isIE(8)){
								    $(this).next().addClass("investComplete100");
					 			}
								$(this).attr("data-fgColor","#c83e41");
							}
						});
						$('.knob').knob({
							'width':50,
   							'height':50,
   							'thickness':.2
						});
					});
					require.async('commonJS/jquery.accordion',function(){
						$(".index_invest_list").accordion({
							wrapBox    : ".index_invest_list",//最外围wrap元素
							titleBox   : ".index_invest_item",//显示的标题
							contentBox : ".index_invest_more",//隐藏的内容
							hoverClass : "active",//展示之后的样式
							isClick    : false  //默认点击展开
						});
					});
				})
			})
		}
	})
	
	//首页统计数据
	$.ajax({
		url:"/index/indexStatistics.html?random="+Math.random(),
		type:"get",
		dataType:"json",
		success:function(data){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					var tpl = require('/themes/theme_default/media/tpl/home_statistics.tpl');//载入tpl模板
					var template = Handlebars.compile(tpl);
					var html = template(data);
					$("#home_statistical").html(html);
				})
			})
		}
	})
	
	$.ajax({
		url:"/index/indexStatistics.html?random="+Math.random(),
		type:"get",
		dataType:"json",
		success:function(data){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					var tpl = require('/themes/theme_default/media/tpl/home_data_num.tpl');//载入tpl模板
					var template = Handlebars.compile(tpl);
					var html = template(data);
					$(".data-num-con").html(html);
				})
			})
		}
	})
	
	$(function(){
		require.async('./jquery.flexslider-min',function(){
			$('.index_intro_box').flexslider({
				directionNav: true,
				pauseOnAction: false
			});
		})
	});

	//解决IE下不支持placeholder
	require.async('common1',function(){
		if($.browser.msie) { 
			$(":input[placeholder]").each(function(){
				$(this).placeholder();
			});
		}
	})

	//首页理财商学院栏目
	$.ajax({
		url:"/index/indexFinanceSite.html",
		type:"get",
		dataType:"json",
		success:function(data){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					var tpl = require('/themes/theme_default/media/tpl/lcschool-index.tpl');//载入tpl模板
					var template = Handlebars.compile(tpl);
					var html = template(data);
					$(".talk-people").html(html);
				})
			})
		}
	})
	
	
	//合作伙伴
	$.ajax({
		url:"/index/articleList.html?nid=partner&random="+Math.random(),
		type:"get",
		dataType:"json",
		success:function(data){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					var tpl = require('/themes/theme_default/media/tpl/home_partner.tpl');//载入tpl模板
					var template = Handlebars.compile(tpl);
					var html = template(data);
					$("#partnerList").html(html);
				})
			})
		}
	})
});

