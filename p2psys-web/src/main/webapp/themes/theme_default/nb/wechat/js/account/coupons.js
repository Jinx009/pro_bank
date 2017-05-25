"use strict";
var telreg = /^0?1[3|4|5|7|8][0-9]\d{8}$/; //手机号正则
$(function() {
    //底部导航颜色切换
    $("#foot_nav_my_a").css("color", "#326eaf").find("svg").css({
        "fill": "#326eaf",
        "stroke-width": "0"
    });

    //Vue过滤器，添加特殊字符
    Vue.filter('addPer', function(value, before, after) {
        return before + value + after;
    });

    // Vue过滤器，保留两位小数
    Vue.filter('fixDot', function(value) {
        return parseFloat(value).toFixed(2);
    });

    // Vue过滤器，日期格式化（2015-08-27）
    Vue.filter('dateFormat', function(value) {
        if (value) {
            var d = new Date(value);
            var year = d.getFullYear();
            var month = d.getMonth() + 1;
            var date = d.getDate();
            if (month < 10) {
                month = '0' + month;
            }
            if (date < 10) {
                date = '0' + date;
            }
            return year + "-" + month + "-" + date;
        } else {
            return "";
        }
    });

     // Vue过滤器，手机号中间四位打吗
     Vue.filter('telMosaics',function (value) {
         if (value) {
            return value.substring(0,3)+"****"+value.substring(7,11);
         }else{
            return "";
         };
     })


    //获取加息券详细信息，并对返回的json进行预处理
    $.ajax({
        url: '/nb/wechat/account/couponsList.html',
        type: 'GET',
        dataType: 'JSON',
        ansyc: false,
        success: function(res) {
            var couponsList = res;
            //初始化Vue组件，渲染数据到页面
            var couponsVue = new Vue({
                el: '#tabs_container',
                data: {
                    donateList: couponsList.donateList,
                    overdueList: couponsList.overdueList,
                    unusedList: couponsList.unusedList,
                    usedList: couponsList.usedList
                }
            });
            // 操作按钮中转赠点击监听
            $(".coupons-table").delegate('.btn-donation', 'click', function(event) {
                var self = $(this);
                layer.open({
                    content: '<div style="font-size: 16px; font-weight: 300; color: #4A4747; margin-top: 28px; margin-bottom: 3px;position: relative;text-align: center;"><div class="wrong-msg" style="font-size: 13px; color: red; position: absolute; top: -22px; left: 50%; width: 100px; text-align: center; margin-left: -50px;"></div><input type="tel" style="padding:1px 0 1px 4px; height: 27px; margin-top:4px; margin-bottom: 20px; border-radius: 3px; border: none; width:200px;" placeholder="请输入被赠人手机号" id="donation-tel" autofocus=""><div style="width: 206px; margin: 0 auto;"><div style="width: 60%;float: left;"><input id="donation-code" type="text" style="width:100%;padding:1px 0 1px 4px; height: 27px; border-top-left-radius: 3px; border-bottom-left-radius: 3px; border-top: 1px solid #31b0d5; border-left: 1px solid #31b0d5; border-bottom: 1px solid #31b0d5;border: none;" placeholder="验证码"></div><img src="/validimg.html" alt="点击刷新" id="change_code" style="  height:29px;   width: 40%;border-top-right-radius: 3px; border-bottom-right-radius: 3px;border-top: 1px solid #31b0d5; border-right: 1px solid #31b0d5; border-bottom: 1px solid #31b0d5;border: none;"></div></div>',
                    btn: ['确认', '取消'],
                    style: 'min-width: 280px;min-height:125px;text-align: center;border-radius: 6px;padding-bottom: 20px;background: #ededed;',
                    shadeClose: true,
                    yes: function(index) {
                        var donation_id = self.parent().parent().children(".coupons-id").val();
                        var donation_tel = $("#donation-tel").val();
                        var donation_code = $("#donation-code").val();
                        var coupons_userto_mobile = self.parent().parent().children(".coupons-userto-mobile").val();
                        if (!telreg.test(donation_tel)  || donation_tel===coupons_userto_mobile) {
                            $(".wrong-msg").html("手机号不正确").show();
                        } else {
                            $.ajax({
                                url: '/nb/wechat/account/donateCoupons.html',
                                type: 'POST',
                                data: {
                                    donation_id: donation_id,
                                    donation_tel: donation_tel,
                                    donation_code: donation_code
                                },
                                success: function(res) {
                                    if (res.result == "failure") {
                                        $(".wrong-msg").html("验证码不正确").show();
                                        $("#change_code").attr("src",'/validimg.html?t=' + Math.random());
                                    } else if (res.result == "success") {
                                        layer.close(index);
                                        layer.open({
                                            content: "赠送成功",
                                            style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:10px; letter-spacing: 2px; line-height: 100%;',
                                            time: 1.5
                                        });
                                        window.location.href="/nb/wechat/account/coupons.html";
                                    }
                                }
                            });
                        } //-else

                    },
                    no: function(index) {
                        layer.close(index);
                    }
                });
                $("#change_code").click(function() {
                    this.src = '/validimg.html?t=' + Math.random();
                });

            }); //-.btn-donation.click

            //加息券名称点击弹出赠送人信息
            $(".coupons-detail").delegate('.coupons-name', 'click', function(event) {
                var tips = $(this).next();
                if (tips.attr("tip_toggle") == 0) {
                    tips.show();
                    tips.attr("tip_toggle", "1");
                } else {
                    tips.hide();
                    tips.attr("tip_toggle", "0");
                }
            });
        }
    })

    // tab切换加息券状态
    var tabsSwiper = new Swiper('.swiper-container', {
        speed: 500,
        onSlideChangeStart: function() {
            $(".tabs .active").removeClass('active');
            $(".tabs a").eq(tabsSwiper.activeIndex).addClass('active');
        }
    });

    $(".tabs a").on('touchstart mousedown', function(e) {
        e.preventDefault()
        $(".tabs .active").removeClass('active');
        $(this).addClass('active');
        tabsSwiper.slideTo($(this).index());
    });

    $(".tabs a").click(function(e) {
        e.preventDefault();
    });

}); //$(function)
