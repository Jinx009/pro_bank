  $(function() {
      var bind_status = false;
      var telreg = /^0?1[3|4|5|7|8][0-9]\d{8}$/; //手机号正则
      var tel = $("#tel").val();
      var pwd = $("#pwd").val();
      var yzm = $("#yzm").val();
      var couponCode = $("#couponCode").val();
      var is_login = true;
      var adminUrl = "";

      var size = $("#list").val();

      if (parseInt(size) <= 1) {

          var couponCode = $("#couponCode").val();
          var sessionStorage = window.localStorage;

          if (null != couponCode && "" != couponCode) {
              sessionStorage.setItem("localPromot", couponCode);
          } else {
              if (null != sessionStorage.getItem("localPromot") && "" != sessionStorage.getItem("localPromot")) {
                  $("#couponCode").val(sessionStorage.getItem("localPromot"));
              }
              $("#couponCode").attr("readonly", false);
          }
      } else {
          location.href = $("#redirectURL").val();
      }
      //表单验证
      function checkForm() {
          var tel = $("#tel").val();
          var pwd = $("#pwd").val();
          if (!telreg.test(tel)) {
              layer.open({
                  content: "手机号输入错误",
                  style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:20px; letter-spacing: 4px; line-height: 200%;',
                  time: 1.5
              });
              return false;
          } else if ("" === pwd || null === pwd || pwd.length < 8) {
              layer.open({
                  content: "密码至少8位",
                  style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:20px; letter-spacing: 4px; line-height: 200%;',
                  time: 1.5
              });
              return false;
          }
          return true;
      }
      // 启动倒计时
      function startTime() {
          var sendmsg = $("#send-msg"),
              curCount = 60; //倒计时时间
          // 倒计时初始化函数
          function SetRemainTime() {
              if (curCount == 0) {
                  window.clearInterval(InterValObj); //停止计时器
                  sendmsg.removeAttr("disabled"); //启用按钮
                  sendmsg.removeClass('disabled');
                  sendmsg.text("重新发送");
              } else {
                  curCount--;
                  sendmsg.text(curCount + "秒后重新获取");
              }
          }
          //设置button效果，开始计时
          sendmsg.attr("disabled", "true");
          sendmsg.addClass('disabled');
          sendmsg.text(curCount + "秒后重新获取");
          InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
      }
      //发送验证码函数
      function sendMsg() {
          var tel = $("#tel").val();
          if (checkForm()) {
              $.ajax({
                  url: "/nb/wechat/getBindCode.html?tel=" + tel,
                  type: "GET",
                  success: function(res) {
                      if ("success" == res.result) {
                          startTime();
                      } else {
                          layer.open({
                              content: "发送失败",
                              style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:20px; letter-spacing: 4px; line-height: 200%;',
                              time: 1.5
                          });
                          return false;
                      }
                  }
              });
          }
      }
      // 检查手机号
      function checkTel() {
          var tel = $("#tel").val();
          if (telreg.test(tel)) {
              $.ajax({
                  url: "/nb/wechat/userExists.html?tel=" + tel,
                  type: "POST",
                  dataType: "json",
                  success: function(res) {
                      if ("success" != res.result) {
                          $(".tips").hide();
                          $(".code").hide();
                          $("#confirm").text("登录");
                          $("#login").text("注册");
                          bind_status = true;
                      }
                  }
              })
          }
      }
      //绑定或者登录
      function bindAccount() {
          var tel = $("#tel").val();
          var pwd = $("#pwd").val();
          var yzm = $("#yzm").val();
          var redirectURL = $("#redirectURL").val();
          var couponCode = $("#couponCode").val();
          var params = "tel=" + tel + "&pwd=" + pwd + "&yzm=" + yzm + "&bind_status=" + bind_status + "&couponCode=" + couponCode;
          var params2 = "tel@" + tel + "!couponCode@" + couponCode;

          if (checkForm()) {
              $.ajax({
                  url: "/nb/wechat/writeBindData.action",
                  type: "POST",
                  data: "bindData=" + params2,
                  success: function(res) {

                  }
              });

              $.ajax({
                  url: "/nb/wechat/bindUser.html",
                  type: "POST",
                  data: params,
                  dataType: "json",
                  success: function(res) {
                      if ("success" == res.result) {
                          layer.open({
                              content: "绑定成功",
                              style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:20px; letter-spacing: 4px; line-height: 200%;',
                              time: 1.5
                          });
                          location.href = redirectURL;
                      }else {
                    	  if("noopenid"==res.errorMsg){
                    		  layer.open({
                                  content: "参数异常",
                                  time: 1.5,
                                  style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:20px; letter-spacing: 4px; line-height: 200%;'
                              });
                    		  location.reload();
                    	  }else{
                    		  layer.open({
                                  content: res.errorMsg,
                                  time: 1.5,
                                  style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:20px; letter-spacing: 4px; line-height: 200%;'
                              });
                    	  }
                      }
                  }
              });
          }
      }
      //切换登录和注册
      function toggleLogin() {
          if (bind_status) {
              $(".tips").show();
              $(".code").show();
              $("#confirm").text("确认绑定");
              $("#login").text("已有账号登录");
              bind_status = false;
          } else {
              $(".tips").hide();
              $(".code").hide();
              $("#confirm").text("登录");
              $("#login").text("注册");
              bind_status = true;
          }
      }
      $("#send-msg").bind("click", sendMsg); //发送验证码
      $("#confirm").bind("click", bindAccount); //登录绑定
      $("#tel").bind("input propertychange", checkTel); //手机号输入框验证
      $("#login").bind("click", toggleLogin); //切换登录和注册

      // 服务协议
      $("#service_protocol").bind("click", function() {
          var pageii = layer.open({
              type: 1,
              content: '<div class="close-layer">&times;</div>'+service_paotocol_text,
              style: 'width:100%; background-color:#F2F2F2; border:none;'
          });
          $(".close-layer").bind("click", function() {
              layer.closeAll();
          });
      });
      // 风险条款
      $("#risk_provision").bind("click", function() {
          var pageii2 = layer.open({
              type: 1,
              content: '<div class="close-layer">&times;</div>'+risk_provision_text,
              style: 'width:100%; background-color:#F2F2F2; border:none; '
          });
          $(".close-layer").bind("click", function() {
              layer.closeAll();
          });
      });
       // 轮播图
      $.ajax({
            url: "/getHostUrl.action",
            type: "GET",
            dataType: "json",
            ansyc: false,
            success: function(res) {
                adminUrl = res.con_adminurl;

                $.ajax({
                    url: "/nb/wechat/banner.action",
                    type: "GET",
                    dataType: "json",
                    ansyc: false,
                    success: function(res) {
                        var htmlStr = "";
                        if (0 != res.data.length) {
                            for (var i = 0; i < res.data.length; i++) {
                                htmlStr += "<div class='swiper-slide'>";
                                htmlStr += "<a href=" + res.data[i].bannerLinkUrl + " >";
                                htmlStr += " <img src=" + adminUrl + res.data[i].bannerPicUrl + " class='indexBg swiper-lazy'/>";
                                htmlStr += "</a>";
                                htmlStr += "</div>";
                            }
                        } else {
                            htmlStr += "<div class='swiper-slide'>";
                            htmlStr += "<a href='javascript:;' >";
                            htmlStr += " <img src='/themes/theme_default/nb/wechat/img/indexIcon.jpg' class='indexBg swiper-lazy'/>";
                            htmlStr += "</a>";
                            htmlStr += "</div>";
                        }

                        $(".swiper-wrapper").html(htmlStr);
                        // 首页轮播图
                        var mySwiper = new Swiper('.swiper-container', {
                            loop: true, //循环播放
                            autoplay: 5000,
                            pagination: '.swiper-pagination', //导航分页
                            paginationClickable: true //导航点击切换
                        });
                    }
                })
            }
        })
        // 轮播图
  })
