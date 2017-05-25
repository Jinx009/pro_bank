"use strict";
var telreg = /^0?1[3|4|5|7|8][0-9]\d{8}$/; //手机号正则
$(function() {
    changeLeftMenue("我的福袋", "我的加息券");
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
    Vue.filter('telMosaics', function(value) {
        if (value) {
            return value.substring(0, 3) + "****" + value.substring(7, 11);
        } else {
            return "";
        };
    });
    //获取加息券详细信息，并对返回的json进行预处理
    $.ajax({
            url: '/nb/pc/member/couponsList.html',
            type: 'GET',
            dataType: 'JSON',
            ansyc: false,
            success: function(res) {
                    var couponsList = res;
                    //初始化Vue组件，渲染数据到页面
                    var couponsVue = new Vue({
                        el: 'body',
                        data: {
                            donateList: couponsList.donateList,
                            overdueList: couponsList.overdueList,
                            unusedList: couponsList.unusedList,
                            usedList: couponsList.usedList
                        }
                    });
                    // 操作按钮中转赠点击监听
                    $(".coupons-detail").delegate('.btn-donation', 'click', function(event) {
                        var self = $(this);
                        layer.confirm('<div style="font-size: 16px; font-weight: 300; color: #4A4747; margin:18px 40px 23px;"><div class="wrong-msg" style="display:none; font-size: 14px;color:red; text-align: center;"></div><input type="tel" style="padding:1px 0 1px 4px; height: 37px; margin-top:4px; margin-bottom: 20px; border-radius: 3px; border: 1px solid #31b0d5; width:260px;" placeholder="请输入您要赠送的手机号" id="donation-tel"><div style="display: -webkit-flex; display: -ms-flexbox; display: flex; width:260px; margin:0 auto;" autofocus><input id="donation-code" type="text" style="padding:1px 0 1px 4px; -webkit-flex: 1; -ms-flex: 1; flex: 1; height: 39px; border-top-left-radius: 3px; border-bottom-left-radius: 3px; border-top: 1px solid #31b0d5; border-left: 1px solid #31b0d5; border-bottom: 1px solid #31b0d5;" placeholder="验证码"><img src="/validimg.html" alt="点击刷新" id="change_code" style="height:39px;-webkit-flex: 1; -ms-flex: 1; flex: 1; border-top-right-radius: 3px; border-bottom-right-radius: 3px;border-top: 1px solid #31b0d5; border-right: 1px solid #31b0d5; border-bottom: 1px solid #31b0d5;" /></div></div>', {
                            btn: ['确认', '取消'] //按钮
                        }, function(index) {
                            var donation_id = self.parent().parent().children(".coupons-id").val();
                            var donation_tel = $("#donation-tel").val();
                            var donation_code = $("#donation-code").val();
                            var coupons_userto_mobile = self.parent().parent().children(".coupons-userto-mobile").val();
                            if (!telreg.test(donation_tel) || donation_tel === coupons_userto_mobile) {
                                $(".wrong-msg").html("手机号不正确").show();
                            } else {
                                $.ajax({
                                    url: '/nb/pc/member/donateCoupons.html',
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
                                            layer.msg('赠送成功');
                                            setTimeout(function () {
                                                window.location.href = "/nb/pc/member/coupons.html";
                                            },3200);
                                        }
                                    }
                                }); //-$ajax
                            } //-else
                        }, function(index) {
                            layer.close(index);
                        }); //-layer.confirm


                        $("#change_code").click(function() {
                            this.src = '/validimg.html?t=' + Math.random();
                        });

                    }); //-.btn-donation.click

                    // 加息券名称点击弹出赠送人信息
                    $(".coupons-name").delegate('span', 'click', function(event) {
                        var tips = $(this).next();
                        if (tips.attr("tip_toggle") == 0) {
                            tips.show();
                            tips.attr("tip_toggle", "1");
                        } else {
                            tips.hide();
                            tips.attr("tip_toggle", "0");
                        }
                    });
                } //-success
        }) //- $ajax

}); //-$function
/**
 * 点击事件
 * @param id
 */
function changeTo(id) {
    for (var i = 1; i < 5; i++) {
        $("#li_" + i).removeClass("active");
        $("#table_content" + i).addClass("none");
    }

    $("#li_" + id).addClass("active");
    $("#table_content" + id).removeClass("none");
}
