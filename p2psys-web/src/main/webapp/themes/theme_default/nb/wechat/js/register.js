  $(function() {
      var bind_status = false;
      var telreg = /^0?1[3|4|5|7|8][0-9]\d{8}$/; //手机号正则
      var pwdreg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$/;
      var couponCode = $("#couponCode").val();
      var adminUrl = "";

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
          } else if (pwd.toString().match(pwdreg) == null) {
              layer.open({
                  content: "密码不合法",
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
                  url: "/nb/wechat/getRegisterCode.html?tel=" + tel,
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
                          layer.open({
                              content: "号码已存在，请登录",
                              style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:20px; letter-spacing: 4px; line-height: 200%;',
                              time: 1.5
                          });
                      }
                  }
              })
          }
      }
      /**
       * 执行注册
       */
      function executeRegister() {
          var tel = $("#tel").val();
          var pwd = $("#pwd").val();
          var repwd = $("#pwd").val();
          var valid_code = $("#valid_code").val();
          var tel_code = $("#yzm").val();
          var invite_code = getTrimStr($("#couponCode").val());
          var redirectURL = $("#redirectURL").val();

          var params = "tel=" + tel + "&pwd=" + pwd + "&repwd=" + repwd + "&valid_code=" + valid_code + "&tel_code=" + tel_code + "&invite_code=" + invite_code;
          if (checkForm()) {
              if (valid_code == "") {
                  layer.open({
                      content: "请输入图形验证码",
                      style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:20px; letter-spacing: 4px; line-height: 200%;',
                      time: 1.5
                  });
              } else if (tel_code == "") {
                  layer.open({
                      content: "请输入手机校验码",
                      style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:20px; letter-spacing: 4px; line-height: 200%;',
                      time: 1.5
                  });
              } else {
                  $.ajax({
                          url: "/nb/wechat/executeRegister.html",
                          type: "POST",
                          data: params,
                          dataType: "json",
                          success: function(res) {
                                  if ("success" == res.result) {
                                      layer.open({
                                          content: "注册成功",
                                          style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:20px; letter-spacing: 4px; line-height: 200%;',
                                          time: 1.5
                                      });
                                      var url = $("#redirectURL").val();

                                      if ("" !== url) {
                                          location.href = url;
                                      } else {
                                          location.href = "/nb/wechat/account/main.html";
                                      }
                                      
                                  } else {
                                      layer.open({
                                          content: res.errorMsg,
                                          time: 1.5,
                                          style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:20px; letter-spacing: 4px; line-height: 200%;'
                                      });
                                      // $("#change_code").attr("src",'/validimg.html?t=' + Math.random());
                                  }
                              } //success
                      }) //ajax
              } //else
          } //if
      } //执行注册

      function getTrimStr(str) {
          var a = /^[0-9a-zA-Z]*$/g;

          if ("" !== str && null !== str) {
              var array = str.split("");
              var new_str = "";

              for (var i = 0; i < array.length; i++) {
                  if (a.test(array[i])) {
                      new_str += array[i];
                  }
              }
              return new_str;
          } else {
              return "";
          }
      } //getTrimStr

      /**
       * 跳转登录
       */
      function goToLogin() {
          location.href = "/nb/wechat/land.html";
      }

      $("#send-msg").bind("click", sendMsg); //发送验证码
      $("#confirm").bind("click", executeRegister); //注册
      $("#tel").bind("input propertychange", checkTel); //手机号输入框验证
      $("#login").bind("click", goToLogin); //跳转登录

      // 服务协议
      $("#service_protocol").bind("click", function() {
          var pageii = layer.open({
              type: 1,
              content: '<div class="close-layer">&times;</div>' + service_paotocol_text,
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
              content: '<div class="close-layer">&times;</div>' + risk_provision_text,
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
