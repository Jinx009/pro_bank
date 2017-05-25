define(function(require,exports,module){
  require('jquery');
      require('common');
      
      
      $.ajax({
  		url:"/product/showProductTypeFlagList.html",
  		type:"GET",
  		dataType:"json",
  		success:function(res)
  		{
  			list_data = res.data;
  			var flagId = $("#flagId").val();
  			for(var i=0;i<list_data.length;i++){
  				var id=list_data[i].id;
  				if(id==flagId){
  					$("#flag").html(list_data[i].flagName);
  					$("#flag").attr("href","/nb/pc/product/productList.html?id="+list_data[i].id);
  					
  				}
  			}
  			
  			
  		}
  	})
  	
  //异步请求标的数据
  $.ajax({
    type:"get",
    url:"/invest/borrowDetail.html"+window.location.search,
    dataType:"json",
    cache:false,
    success:function(json){
    	
      $("#loading_tip").slideUp();
      require.async("/plugins/handlebars-v1.3.0/handlebars-v1.3.0",function(){
          require.async("/plugins/handlebars-v1.3.0/transFormatJson",function(){
            $("#invest-main-box").html(Handlebars.compile(require("../../tpl/invest/detail.tpl"))(json));
            $("#investDetail").attr("href","/nb/pc/product/productList.html?id="+$("#flagId").val());
//            $("#lc-title").html(Handlebars.compile(require("../../tpl/invest/detailtitle.tpl"))(json));
            
           /* var pageUseMoney = parseFloat($(".pageUseMoney").html())- 0.01;
            pageUseMoney= pageUseMoney.toFixed(2);
            if(pageUseMoney<0){
            	$(".pageUseMoney").html("0");
            }
            else
            {
            	
            	var pageMoney = pageUseMoney.toString();
            	var array = new Array();
            	array=pageMoney.split(".")[0]; 
            	
            	pageUseMoney = "";
            	var arrayFirst = new Array();
            	var arraySecond = new Array();
            	var j = 0;
            	for(var i = array.length-1;i>=0;i--)
            	{
            		if(0===i%3&&i!==(array.length-1)&&0!==i)
            		{
            			arrayFirst[j] = array[i];
            			j++;
            			arrayFirst[j] = ",";
            		}
            		else
            		{
            			arrayFirst[j] = array[i];
            		}
            		j++;
            	}
            	console.log(arrayFirst)
            	for(var k = arrayFirst.length-1;k>=0;k--)
            	{
            		pageUseMoney += arrayFirst[k]; 
            	}
            	
            	$(".pageUseMoney").html(pageUseMoney+"."+pageMoney.split(".")[1]);
            	
            }*/
            
          //解决密码自动填充问题
          if($(".form-group input[type='password']").hasClass("isDirectional")){
              $(".isDirectionalText").css("display","none");
              $(".isDirectional").css("display","inline-block");
          }
          if($(".form-group input[type='password']").hasClass("payPwd")){
               $(".payPwdText").css("display","none");
               $(".payPwd").css("display","inline-block");
          }
           //解决IE下不支持placeholder
          require.async('common1',function(){
            if($.browser.msie) { 
              $(":input[placeholder]").each(function(){
                $(this).placeholder({posL: 15});
              });
            }
          });


           //全投金额

             $(".all-tb").click(function(){
               var okmoney = $(".ok-money").html().replace(/,/gm, "");
               $("#money").val(okmoney);
             })


          //协议弹窗
          $(".protocolPreview").click(function(){
            require.async("/plugins/layer-v1.8.4/layer.min",function(){
              $.layer({
                  type: 2,
                  title : "协议预览",
                  border: [1, 1, '#cecfd0'],
                  area: ['1000px' , '500px'],
                  iframe: {src: '/invest/protocolPreview.html?id='+$("#bid_id").val()}
              }); 
            })
          });
          
            //标进度条
            require.async("/plugins/jquery.knob/jquery.knob.min",function(){
              //不同的进度，更换成不同的颜色
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
                    $(this).parent().addClass("investSchedule100");
                  }
                  $(this).attr("data-fgColor","#c83e41");
                }
              });
              $('.knob').knob({
                'width':50,
                'height':50,
                'thickness':.2,
                'readOnly' : true
              });
            })
            //选择红包功能
            var 
                  $choiceRedPacket = $('.choiceRedPacket'),//选择红包按钮
                  //$checkAllRedPacket = $('.checkAllRedPacket input[type="checkbox"]'),//红包全选按钮
                  $choiceRedPacketInput = $('.choiceRedPacketBox ul li input[type="checkbox"]'),//单个红包选择框
                  $radioRedPacketInput = $('.choiceRedPacketBox ul li input[type="radio"]'),//单个红包选择框
                  $choiceRedPacketMoney = $('.choiceRedPacketMoney'),//选中红包总额
                  $investMoney = $('.investMoney'),//实际支付总额
                  $redPacketMoney = $('.redPacketMoney'),//单个红包金额
                  $money = $("#money"),//输入投资金额
                  $redPacketConfirm =$("#redPacketConfirm"); //点击确认
            	  $radioRedPacketHide =$("#radioRedPacketHide"); 

            //选择红包弹窗
            $choiceRedPacket.click(function(){
                require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
                    $.layer({
                        type: 1,
                        closeBtn: [0,true],
                        title: "请选择可使用红包",
                        area: ['600px', '500px'],
                        border: [1, 1, '#cecfd0'],
                        page: {
                            dom: '#choiceRedPacket',
                            url:"",
                            ok:function(data){

                            }
                        },
                        close: function(){uncheckAllRedPacket();}
                    }); 
                    $redPacketConfirm.click(function(){
                      layer.closeAll();
                      redPacketMoneyTotal()
                    });
                });
            });
            
            //投资金额输入框判断
            $money.on('keyup',function(){
                redPacketMoneyTotal();
            })
            
            var uncheckAllRedPacket = function(){
            	$choiceRedPacketInput.each(function(){
                     this.checked = false;
                });
            	$radioRedPacketHide.attr("checked",true);
            	redPacketMoneyTotal();
            }

            //红包金额计算
            var redPacketMoneyTotal = function(){
              var redMoney = 0,//选中红包金额数字
              money = $money.val(),//投资金额
              investMoney = 0; //实际支付金额
              //输入金额转成数字
              if(money == "") money = 0;
              $redPacketMoney.each(function(){
                if($(this).next('input[type="checkbox"]').is(":checked"))
                {
                 redMoney += parseFloat($(this).html());
                }
                if($(this).next('input[type="radio"]').is(":checked"))
                {
                 redMoney += parseFloat($(this).html());
                }
              });

               $choiceRedPacketMoney.text(redMoney);//计算选中红包总额
               //计算实际支付总额
               if(money > redMoney)
               {
                investMoney = money-redMoney;
                investMoney = investMoney.toFixed(2);
               }
               $investMoney.text(investMoney);
               $choiceRedPacket.html("使用红包：<em>"+redMoney+"</em>元&nbsp;&nbsp;&nbsp;实际支付：<em>"+investMoney+"</em>元");
            }

             //红包全选功能    
            /*$checkAllRedPacket.click(function(){
                var flag = this.checked;
                $choiceRedPacketInput.each(function(){
                  this.checked = flag;
                })
                redPacketMoneyTotal();
            });*/
            //红包单个选择
            $choiceRedPacketInput.live("click",function(){
              redPacketMoneyTotal();
            });
            //红包单个选择
            $radioRedPacketInput.live("click",function(){
              redPacketMoneyTotal();
            });


            //校验提示框关闭
            $(".form-group input").focus(function(){
              $(this).next(".form_group_tip").addClass("hide");
            });

            //标预约校验
              require.async('jquery.form',function(){
              $(".sub-invest-btn1").click(function(){
               var moneyval = $("#money").val();
               var payPwdval = $(".payPwd").val();
               var isDirectionalval = $(".isDirectional").val();
               if(moneyval==""||payPwdval==""||isDirectionalval==""){

                return false;
               }
               $("#invest_detail_form").ajaxSubmit({
                   type: "post",
                   url: "/invest/appointmentBid.html",
                   dataType: "json",
                   success: function(data){
                    require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
                      //构造确认框DOM
                      $.layer({
                        type: 1,
                        closeBtn: true,
                                title: "&nbsp;",
                          area: ['450px', '190px'],
                          border: [1, 1, '#cecfd0'],
                          page: {
                              html: "<div class='tipsWrap w450'>"+"<div class='tipsTxt'>"+"<i class='iconfont tipIco'>"+"&#xe63c;"+"</i><span>"+data.msg+"</span></div>"+"<div class='tipsBtnBar'>"+"<a href='javascript:' onclick='javascript:window.location.href=window.location.href;'  class='okBtn'>"+"返回"+"</a></div></div>"
                          }
                      });
                  });
                
              }
              })
            })
          })




            //标投资校验
            $(".sub-invest-btn").click(function(){
              var $form_mony_tip = $(".form_mony_tip");
              /*if(parseInt(json.userInvestIdentify.realNameStatus) != 1)
              {
                //判断是否开启托管
                if(json.isOpenAip)
                {
                  $form_mony_tip.removeClass("hide").html('为了保证您的安全，请先<a href="/member/security/realNameIdentify.html" target="_blank">实名认证</a>再进行投资！');
                }
                else
                {
                  $form_mony_tip.removeClass("hide").html('为了保证您的安全，请先<a href="/member/security/setting.html" target="_blank">实名认证</a>再进行投资！');
                }
                return false;
              }*/
				if($money.val() == "" || $money.val() == 0)
              {
                $form_mony_tip.removeClass("hide").html('<b class="iconfont">&#xe60f;</b>请输入投标金额！');
                return false;
              }
              //借款金额 json.borrow.account
              //可投金额 json.accountWait
              //账户余额 json.account.useMoney
              //实际支付金额 invest_money
              //投标金额 money
              //最小投标 json.borrow.lowestSingleLimit
              //最大投标 json.borrow.mostAccount
              var invest_money = $investMoney.text();
              var  money= $money.val();
              if(invest_money > json.account.useMoney)
              {
                $form_mony_tip.removeClass("hide").html('<b class="iconfont">&#xe60f;</b>账户余额不足，请<a href="/member/recharge/newRecharge.html" target="_blank">立即充值</a>!');
                return false;
              }
              //可投金额大于最小投标金额
              if(parseFloat(json.accountWait) > parseFloat(json.borrow.lowestAccount))
              {
                                  if(money < parseFloat(json.borrow.lowestAccount))
                                  {
                                      $form_mony_tip.removeClass("hide").html('<b class="iconfont">&#xe60f;</b>投标金额不能小于最小投标金额<a href="javascript:;" id="invest_all">'+json.borrow.lowestAccount+'</a>元！');
                                      $("#invest_all").click(function(){
                                        $money.val(json.borrow.lowestAccount);
                                        $form_mony_tip.addClass("hide");
                                        redPacketMoneyTotal();
                                      });
                                      return false;
                                  }
                                  else if(parseFloat(json.borrow.mostAccount) != 0 && money > parseFloat(json.borrow.mostAccount)  )
                                  {
                                      $form_mony_tip.removeClass("hide").html('<b class="iconfont">&#xe60f;</b>投标金额不能大于最大投标额度,当前<a href="javascript:;" id="invest_all">可投'+json.borrow.mostAccount+'</a>!');
                                      $("#invest_all").click(function(){
                                          $money.val(json.borrow.mostAccount);
                                          $form_mony_tip.addClass("hide");
                                          redPacketMoneyTotal();
                                        });
                                      return false;
                                  }
                                  else if(money > 200000 && parseFloat(json.borrow.mostAccount) == 0 && json.isOpenAip && json.apiCode == 2)
                                  {
                                          $form_mony_tip.removeClass("hide").html('<b class="iconfont">&#xe60f;</b>单笔投资金额不能大于<a href="javascript:;" id="invest_all">20万</a>!');
                                          $("#invest_all").click(function(){
                                            $money.val(200000);
                                            $form_mony_tip.addClass("hide");
                                            redPacketMoneyTotal();
                                          });
                                          return false;
                                  }
                                  else if(money > json.accountWait)
                                  {
                                          $form_mony_tip.removeClass("hide").html('<b class="iconfont">&#xe60f;</b>投标金额不能大于可投金额，可点击<a href="javascript:;" id="invest_all">全部投满</a>剩余金额!');
                                          $("#invest_all").click(function(){
                                              $money.val(json.accountWait);
                                              $form_mony_tip.addClass("hide");
                                              redPacketMoneyTotal();
                                          });
                                          return false;
                                  }
                                  else if($(".form-group input[type='password']").hasClass("isDirectional"))
                                  {
                                          if($(".isDirectional").val() == '')
                                          {
                                            $(".form_directional_tip").removeClass("hide").html('<b class="iconfont">&#xe60f;</b>请输入定向密码');
                                            $form_mony_tip.addClass("hide");
                                            return false;
                                          }
                                   }
                                  else if($(".form-group input[type='password']").hasClass("payPwd"))
                                  {
                                        if($(".payPwd").val() == "")
                                        {
                                          $(".form_trade_tip").removeClass("hide").html('<b class="iconfont">&#xe60f;</b>请输入交易密码');
                                          $form_mony_tip.addClass("hide");
                                          return false;
                                        }
                                  }
              }
              else
              {
                                  if(money != json.accountWait) //可投金额小于最小投标金额——投标金额不等于可投金额
                                  {
                                          $form_mony_tip.removeClass("hide").html('请填入全部剩余<a href="javascript:;" id="invest_all">可投金额</a>!');
                                            $("#invest_all").click(function(){
                                              $money.val(json.accountWait);
                                              $form_mony_tip.addClass("hide");
                                              redPacketMoneyTotal();
                                          });
                                            return false;
                                  }
                                   else if($(".form-group input[type='password']").hasClass("isDirectional"))
                                  {
                                          if($(".isDirectional").val() == '')
                                          {
                                            $(".form_directional_tip").removeClass("hide").html('<b class="iconfont">&#xe60f;</b>请输入定向密码');
                                            $form_mony_tip.addClass("hide");
                                            return false;
                                          }
                                   }
                                  else if($(".form-group input[type='password']").hasClass("payPwd"))
                                  {
                                        if($(".payPwd").val() == "")
                                        {
                                          $(".form_trade_tip").removeClass("hide").html('<b class="iconfont">&#xe60f;</b>请输入交易密码');
                                          $form_mony_tip.addClass("hide");
                                          return false;
                                        }
                                  }
              }
                                    $("#invest_detail_form1").submit();
                                    
                                    require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
                                        //构造确认框DOM
                                        $.layer({
                                            type: 1,
                                            closeBtn: [0,true],
                                            title: "&nbsp;",
                                            area: ['460px', '194px'],
                                            border: [1, 1, '#cecfd0'],
                                            page: {
                                                html: '<div class="tipsWrap"><dl><dt>投资完成前，请不要关闭窗口；</dt><dd>投资完成后，请根据您的投资结果点击下面按钮。</dd></dl><div class="tipsBtnBar"><a href="javascript:;" class="okBtn reFreshWin">投资成功</a><a href="javascript:;" class="cancleBtn closeBtn reFreshWin">投资失败</a></div></div>'
                                            },
                                          close:function(){
                                            window.location.reload();
                                          }
                                        }); 
                                        //刷新页面 
                                        $(".reFreshWin").click(function(){
                                          window.location.reload();
                                        })
                                        //关闭窗口操作
                                        $(".closeBtn").click(function(){
                                          layer.closeAll();
                                        });
                                    });
            });
            //滚动定位效果
            require.async(["commonJS/keep","commonJS/rollTo"],function(){
              //滚动固定
              $(".detail-menus").keep(function(){
                  $(".detail-menus").css({
                    "width":"100%",
                    "left":0,
                    "border-bottom":"1px solid #e5e5e5",
                    "background-color":"#fff",
                    "z-index":1
                  });
                  $(".detail-menus .detail_menus_center").css({
                    "left":"25px"
                  })
                  $("#invest_btn").removeClass("js_hide");
                },function(){
                  $(".detail-menus .detail_menus_center").attr("style", "");
                  $(".detail-menus a").removeClass("active");
                  $("#invest_btn").addClass("js_hide");
                });

                $("#invest_btn").rollTo({
                  oFinish: ".invest-main-right", //要滚动到的元素
                  sSpeed: "300",  //滚动速度
                  bMonitor: false, //是否楼层监听
                  sClass: "active", //楼层监听时需要添加的样式
                  iBias:-18,
                  fnAdditional: "" //追加方法
                });

                if(json.userBaseInfo)
                 {
                    $(".detail-menus a.a1").rollTo({
                      oFinish: ".borrower-info", //要滚动到的元素
                      sSpeed: "300",  //滚动速度
                      bMonitor: true, //是否楼层监听
                      sClass: "active", //楼层监听时需要添加的样式
                      iBias:-60,
                      fnAdditional: "" //追加方法
                    });
                }
                $(".detail-menus a.a2").rollTo({
                  oFinish: ".borrow-desc", //要滚动到的元素
                  sSpeed: "300",  //滚动速度
                  bMonitor: true, //是否楼层监听
                  sClass: "active", //楼层监听时需要添加的样式
                  iBias:-110,
                  fnAdditional: "" //追加方法
                });
                if(json.borrow.type != 101)//秒标不显示
                {
                $(".detail-menus a.a4").rollTo({
                  oFinish: ".borrow-detail", //要滚动到的元素
                  sSpeed: "300",  //滚动速度
                  bMonitor: true, //是否楼层监听
                  sClass: "active", //楼层监听时需要添加的样式
                  iBias:-110,
                  fnAdditional: "" //追加方法
                });
                }
                $(".detail-menus a.a5").rollTo({
                  oFinish: ".invest-record", //要滚动到的元素
                  sSpeed: "300",  //滚动速度
                  bMonitor: true, //是否楼层监听
                  sClass: "active", //楼层监听时需要添加的样式
                  iBias:-150,
                  fnAdditional: "" //追加方法
                });
               
            });
            //借款详情图片展示
            require.async("/plugins/jquery.SuperSlide.2.1.1/jquery.SuperSlide.2.1.1",function(){
                $(".album").slide( { mainCell:".img-box",titCell:".hd li",effect:"fade",titOnClassName:"active",autoPlay:"false",trigger:"mouseover",easing:"swing",delayTime:500,mouseOverStop:true,pnLoop:true});
            });
            //借款详情fancybox图片展示
            require.async(["/plugins/fancybox/jquery.fancybox.css","/plugins/fancybox/jquery.fancybox.pack"],function(){
              $(".fancybox").attr("rel","gallery").fancybox({
                openEffect    : 'fade',
                prevEffect    : 'fade',
                nextEffect    : 'fade'
              });
            });
          //投标记录分页显示
          if(json.data.page.pages > 0)
          {
            require.async(['/plugins/pager/pager.css','/plugins/pager/pager'],function(){
              kkpager.generPageHtml({
                  pno : json.data.page.currentPage,//当前页码
                  total : json.data.page.pages,//总页码
                  totalRecords : json.data.page.total,//总数据条数
                  isShowFirstPageBtn  : false, 
                  isShowLastPageBtn : false, 
                  isShowTotalPage   : false, 
                  isShowTotalRecords  : false, 
                  isGoPage      : false,
                  lang:{
                    prePageText       : '<',
                    nextPageText      : '>'
                  },
                  mode:'click',
                  click:function(n){
                        $.ajax({
                          type:"get",
                          url:"/invest/detailTenderForJson.html?page="+n+"&id="+$("#bid_id").val(),
                          dataType:"json",
                          success:function(json){
                              $('#invest-txjl-tbl tbody').html(Handlebars.compile(require("../../tpl/invest/invest_record.tpl"))(json));
                              var num = 0;
                              $(".order").each(function(){
                                num = num+1;
                                $(this).html((n-1)*10+num);
                              })
                          }
                        });
                    this.selectPage(n); //处理完后可以手动条用selectPage进行页码选中切换
                  }
              });
            });
          }
          })
      })
    }
  });
  
});

          
