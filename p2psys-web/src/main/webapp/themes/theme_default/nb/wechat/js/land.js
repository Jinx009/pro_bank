    $(function() {
        var telreg = /^0?1[3|4|5|7|8][0-9]\d{8}$/; //手机号正则
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
            } else if ("" === pwd || null === pwd || pwd.length < 8) {
                layer.open({
                    content: "密码不正确",
                    style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:20px; letter-spacing: 4px; line-height: 200%;',
                    time: 1.5
                });
                return false;
            }
            return true;
        }

        /**
         * 跳转注册
         */
        function goToRegister() {
            location.href = "/nb/wechat/register.html?redirectURL=" + $("#redirectURL").val();
        }
        /**
         * 登录
         */
        function executeLogin() {
            var tel = $("#tel").val();
            var pwd = $("#pwd").val();
            var params = "tel=" + tel + "&pwd=" + pwd;

            if (checkForm()) {
                $.ajax({
                    url: "/nb/wechat/executeLogin.html",
                    data: params,
                    type: "POST",
                    dataType: "json",
                    success: function(res) {
                        var redirectURL = $("#redirectURL").val();
                        if ("success" === res.result) {
                            if ("" !== redirectURL) {
                                location.href = redirectURL;
                            } else {
                                location.href = "/nb/wechat/account/main.html";
                            }
                        } else {
                            layer.open({
                                content: res.errorMsg,
                                time: 1.5,
                                style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:20px; letter-spacing: 4px; line-height: 200%;'
                            });
                        }
                    }
                })
            } else {
                layer.open({
                    content: res.errorMsg,
                    time: 1.5,
                    style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:20px; letter-spacing: 4px; line-height: 200%;'
                });
            }
        }


        $("#confirm").bind("click", executeLogin); //登录
        $("#login").bind("click", goToRegister); //注册

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
