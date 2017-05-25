var keyArray = new Array();
var myreg = /^[0-9\ ]+$/;
var error_msg = "";
var nowNum = 0;
var data;
$(function() {
    var payPwd = $("#payPwd").val();
    var pwdType = $("#pwdType").val();
    var borrow = $("#borrow").val();
    var money = $("#money").val();

    //新添加加息券弹出层begin

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
            url: '/nb/wechat/account/couponsList.html',
            type: 'GET',
            dataType: 'JSON',
            ansyc: false,
            success: function(res) {
                    var couponsList = res;
                    //初始化Vue组件，渲染数据到页面
                    var couponsVue = new Vue({
                        el: 'body',
                        data: {
                            cid: '',
                            unusedList: couponsList.unusedList
                        }
                    });

                    $(".coupons-detail").delegate(".my-ceheckbox", "click", function(event) {
                        var couRate = $(this).parent().parent().children(".couRate").text();
                        $(".couRateChek").text(couRate);
                    })

                    $(".couNo").click(function() {
                        $(".couRateChek").text("0");
                    });

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

    //新添加加息券弹出层end



    if ("nothas" == payPwd) {
        location.href = "/nb/wechat/account/payPwdSetting.html?redirectURL=/nb/wechat/account/pay.html?productBasicId=" + $("#productBasicId").val();
    }
    if ("is" === pwdType) {
        $("#pwdP").show();
        $("#pwdDiv").show();
    }
    if ("borrow" === borrow) {
        getRedPacket();
    }
    if ("borrow" !== borrow) {
        $("#myRedPacket").html("");
        $("#myRedPacket").attr("onclick", "");
        $("#myCoupons").html("");
        $("#myCoupons").attr("onclick", "");
        // $("#myRedPacket").hide();
        // $("#myCoupons").hide();

    }
    if (null !== money && "" !== money) {
        if (parseFloat(money) !== 0) {
            $("#subMoney").html(parseFloat(money));
        }
    }

    getUserMoney();
})

function getUserMoney() {
    $.ajax({
        url: "/product/showProductDetail.html?prodId=" + $("#productBasicId").val(),
        type: "GET",
        dataType: "json",
        success: function(res) {
            data = res.data;
            if (data.ppfundModel) {
                $("#pRedMoney").hide();
                $("#pCouponsMoney").hide();
                $(".pRealyMoney").hide();
                if (data.ppfundModel.account == 0) {
                    $("#userMoney").hide();

                } else {
                    var userMoney = parseFloat(parseFloat(data.ppfundModel.account).toFixed(2) - parseFloat(data.ppfundModel.accountYes).toFixed(2)).toFixed(2);
                    $("#userMoney span").html("&yen;" + userMoney);
                }
                getRate(data.ppfundModel.apr, data);
            }
            if (data.experienceModel) {
                $("#pRedMoney").hide();
                $("#pCouponsMoney").hide();
                $(".pRealyMoney").hide();
                if (data.experienceModel.account == 0) {
                    $("#userMoney").hide();
                } else {
                    var userMoney = parseFloat(parseFloat(data.experienceModel.account).toFixed(2) - parseFloat(data.experienceModel.accountYes).toFixed(2)).toFixed(2);
                    $("#userMoney span").html("&yen;" + userMoney);
                }
                getRate(data.experienceModel.apr, data);
            }
            if (data.borrowModel) {
                var userMoney = parseFloat(parseFloat(data.borrowModel.account).toFixed(2) - parseFloat(data.borrowModel.accountYes).toFixed(2)).toFixed(2);
                $("#userMoney span").html("&yen;" + userMoney);
                getRate(data.borrowModel.apr, data);
            }

        }
    })
}

function getRate(apr, data) {
    $("#expected_return").show();
    var money = $("#money").val();
    var rate = 0;
    if (data.borrowModel) {
        var interestRateValue = data.borrowModel.interestRateValue;
        if (interestRateValue == 0 || interestRateValue == undefined || interestRateValue == null) {
            interestRateValue = 0;
        } else {
            interestRateValue = data.borrowModel.interestRateValue;
        }
        if ("0" !== data.borrowModel.timeLimit) {
            if ("0" == data.borrowModel.borrowTimeType) {
                //月
                rate = parseFloat(money * (apr + interestRateValue) / 100 / 365 * data.borrowModel.timeLimit * 30).toFixed(2);
                rate = isNaN(rate)?0:rate;
                $("#expected_return span").html("&yen;" + rate);
            }

            if ("1" == data.borrowModel.borrowTimeType) {
                //天
                rate = parseFloat(money * (apr + interestRateValue) / 100 / 365 * data.borrowModel.timeLimit).toFixed(2);
                $("#expected_return span").html("&yen;" + rate);
            }

        } else {

            $("#expected_return").hide();
        }
    }

    if (data.experienceModel) {
        if ("0" == data.experienceModel.timeLimit) {
            $("#expected_return").hide();
        } else {
            var interestRateValue = data.experienceModel.interestRateValue;
            if (interestRateValue == 0 || interestRateValue == undefined || interestRateValue == null) {
                interestRateValue = 0;
            } else {
                interestRateValue = data.experienceModel.interestRateValue;
            }
            rate = parseFloat(money * (apr + interestRateValue) / 100 / 365 * data.experienceModel.timeLimit).toFixed(2);
            $("#expected_return span").html("&yen;" + rate);
        }
    }
    if (data.ppfundModel) {
        if ("0" == data.ppfundModel.timeLimit) {
            $("#expected_return").hide();
        } else {
            var interestRateValue = data.ppfundModel.interestRateValue;
            if (interestRateValue == 0 || interestRateValue == undefined || interestRateValue == null) {
                interestRateValue = 0;
            } else {
                interestRateValue = data.ppfundModel.interestRateValue;
            }
            rate = parseFloat(money * (apr + interestRateValue) / 100 / 365 * data.ppfundModel.timeLimit).toFixed(2);
            $("#expected_return span").html("&yen;" + rate);
        }
    }


}

function checkKeys() {
    var key = $("#key").val();
    $("#loading").show();
    var params = "key=" + key;
    error_msg = "交易密码格式不正确!";

    if (validateTel(key)) {
        $.ajax({
            url: "/nb/wechat/checkPayPwd.html",
            type: "POST",
            data: params,
            dataType: "json",
            success: function(res) {
                if ("success" == res.result) {
                    invest();
                } else {
                    $("#loading").hide();
                    $("#errorMsg").html(res.errorMsg);
                    $("#success_div").click(function() {
                        hideError();
                    })
                    showError();
                }
            }
        })
    } else {
        $("#loading").hide();
        $("#errorMsg").html(error_msg);
        $("#success_div").click(function() {
            hideError();
        })
        showError();
    }
}

/**
 * 投资
 */
function invest() {
    var money = $("#money").val();
    var pwd = $("#pwd").val();
    var goldMoney = $("#goldMoney").val(); //体验金额
    var productBasicId = $("#productBasicId").val();

    if ("" === goldMoney || null === goldMoney) {
        if ("" === money || null === money) {
            $("#loading").hide();
            $("#errorMsg").html("投资金额不能为空!");
            $("#success_div").click(function() {
                hideError();
            })
            showError();
        }
        /*
        else if(parseFloat(money)-notRecommendMoney-recommendMoney<=0)
        {
            $("#loading").hide();
            $("#errorMsg").html("实际投资金额应大于红包金额!");
            $("#success_div").click(function()
            {
                hideError();
            })
            showError();
        }
        */
        else {
            var params = "money=" + money + "&productBasicId=" + productBasicId + "&pwd=" + pwd + "&goldMoney=" + goldMoney + "&ids=" + ids + "&cids=" + $("#coupons-id").val() + "&subMoney=" + $("#subMoney").text() + "&redPacketMoney=" + $("#redPacketMoney").html();

            $.ajax({
                url: "/nb/wechat/account/addInvest.html",
                type: "POST",
                data: params,
                dataType: "json",
                success: function(res) {
                    if ("success" == res.result) {
                        if ("301" === productBasicId || "302" === productBasicId) {
                            $("#errorMsg").html("恭喜您，本次投标成功！<p>&nbsp;</p><p style='font-size:12px' > 快想想喜欢什么颜色的手机，客服人员会尽快与您联系!</p>");
                        } else {
                            $("#errorMsg").html("恭喜您,投资成功!");
                        }

                        $("#pic").attr("src", "/themes/theme_default/nb/wechat/images/open.png");
                        $("#loading").hide();
                        $("#success_div").click(function() {
                            location.href = "/nb/wechat/account/main.html";
                        })
                        showError();
                    } else {
                        if ("noMoney" === res.errorMsg) {
                            $("#loading").hide();
                            $("#errorMsg").html("账户余额不足，去充值!");
                            $("#divText").html("去充值");
                            $("#success_div").attr("onclick", "goRecharge()");
                            showError();
                        } else {
                            $("#loading").hide();
                            $("#errorMsg").html(res.errorMsg + "点击此处关闭!");
                            $("#success_div").click(function() {
                                hideError();
                            })
                            showError();
                        }
                    }
                }
            })
        }
    } else {
        var params = "money=" + money + "&productBasicId=" + productBasicId + "&pwd=" + pwd + "&goldMoney=" + goldMoney + "&ids=" + ids + "&cids=" + $("#coupons-id").val() + "&subMoney=" + $("#subMoney").text() + "&redPacketMoney=" + $("#redPacketMoney").html();
        $.ajax({
            url: "/nb/wechat/account/addInvest.html",
            type: "POST",
            data: params,
            dataType: "json",
            success: function(res) {
                if ("success" == res.result) {
                    $("#pic").attr("src", "/themes/theme_default/nb/wechat/images/open.png");
                    $("#loading").hide();

                    if ("301" === productBasicId || "302" === productBasicId) {
                        $("#errorMsg").html("恭喜您，本次投标成功！<p>&nbsp;</p><p style='font-size:12px' > 快想想喜欢什么颜色的手机，客服人员会尽快与您联系!</p>");
                    } else {
                        $("#errorMsg").html("恭喜您,投资成功!");
                    }
                    $("#success_div").click(function() {
                        location.href = "/nb/wechat/account/main.html";
                    })
                    showError();
                } else {
                    if ("noMoney" === res.errorMsg) {
                        $("#loading").hide();
                        $("#errorMsg").html("账户余额不足，去充值!");
                        $("#divText").html("去充值");
                        $("#success_div").attr("onclick", "goRecharge()");
                        showError();
                    } else {
                        $("#loading").hide();
                        $("#errorMsg").html(res.errorMsg + "点击此处关闭!");
                        $("#success_div").click(function() {
                            hideError();
                        })
                        showError();
                    }
                }
            }
        })
    }
}
/**
 * 开启弹窗
 */
function showError() {
    $("#success_div").show();
    $("#mask_div").show();
}
/**
 * 关闭弹窗
 */
function hideError() {
    $("#success_div").hide();
    $("#mask_div").hide();
}
/**
 * 交易密码
 * @param mobile
 * @returns {Boolean}
 */
function validateTel(mobile) {

    var leftAccountMoney = $("#leftAccountMoney").val();
    var money = $("#money").val();

    if (0 < parseFloat(leftAccountMoney)) {
        if (parseFloat(leftAccountMoney) < parseFloat(money)) {
            error_msg = "投资金额超过剩余可投金额" + leftAccountMoney;

            return false;
        }
    }

    if (mobile.length == 0) {
        error_msg = "请输入交易密码!";

        return false;
    }
    if (mobile.length != 6) {
        error_msg = "请输入6位数字交易密码!";

        return false;
    }

    if (!myreg.test(mobile)) {
        error_msg = "请输入6位数字交易密码!";

        return false;
    }

    return true;
}

function goRecharge() {
    location.href = "/nb/wechat/recharge/newRecharge.html";
}
var notRecommend, recommend;
/**
 * 红包信息
 */
function getRedPacket() {
    $.ajax({
        url: "/nb/wechat/account/redPacketList.html",
        type: "GET",
        dataType: "json",
        ansyc: false,
        success: function(res) {
            notRecommend = res.notRecommendRedPacket;
            recommend = res.recommendRedPacket;

            if (notRecommend.length) {
                for (var i = 0; i < notRecommend.length; i++) {
                    $("#notRecommend").append("<tr><td>" + notRecommend[i].serviceName + "</td><td>" + jsDateTime(notRecommend[i].expiredTime) + "</td><td>" + notRecommend[i].amount + "</td><td><input type=checkbox onchange=changeNotRecommend('" + i + "')  id=check" + notRecommend[i].id + " value=" + notRecommend[i].id + "  class='my-ceheckbox' name=notcheck  ></td></tr>");
                }
            } else {
                $("#notRecommendDiv").hide();
                $("#notRecommendStrong").hide();
            }
            if (recommend.length) {

                $("#recommend").append("<tr><td>" + recommend[0].serviceName + "</td><td>" + jsDateTime(recommend[0].expiredTime) + "</td><td>" + recommend[0].amount + "</td><td><input type=checkbox id=check" + recommend[0].id + " onchange=changeRecommend('0')  value=" + recommend[0].id + " class='my-ceheckbox' name=notcheck  ></td></tr>");

            } else {
                $("#recommendDiv").hide();
                $("#recommendStrong").hide();
            }
            if (!recommend.length && !notRecommend.length) {
                $("#myRedPacket").html("");
                $("#myRedPacket").attr("onclick", "");
            }
        }
    })
}
var recommendMoney = 0,
    notRecommendMoney = 0;

function changeRecommend(index) {
    if ($("#check" + recommend[parseInt(index)].id).is(":checked")) {
        recommendMoney = recommend[parseInt(index)].amount;
    }
    if (!$("#check" + recommend[parseInt(index)].id).is(":checked")) {
        recommendMoney = 0;
    }
    $("#sumRedPacketMoney").html(notRecommendMoney + recommendMoney);
}

function changeNotRecommend(index) {

    if ($("#check" + notRecommend[parseInt(index)].id).is(":checked")) {
        notRecommendMoney = notRecommendMoney + notRecommend[parseInt(index)].amount;
    }
    if (!$("#check" + notRecommend[parseInt(index)].id).is(":checked")) {
        notRecommendMoney = notRecommendMoney - notRecommend[parseInt(index)].amount;
    }
    $("#sumRedPacketMoney").html(notRecommendMoney + recommendMoney);
}
var ids = "";

function closeRedPacket() {
    $("[name = notcheck]:checkbox").each(function() {
        if ($(this).is(":checked")) {
            ids += $(this).val() + "str";
        }
    });


    var money = notRecommendMoney + recommendMoney;

    $("#redPacketMoney").html(money);

    var money = $("#money").val();

    var subMoney = parseFloat(money) - notRecommendMoney - recommendMoney;

    if (subMoney > 0) {
        $("#subMoney").html(subMoney);
    }

    $("#coupons").hide();
    $("#redPacket").hide();
    $("#main").show();

}

/**
 * 切换
 */
function changeInputMoney() {
    var _leftMoney = parseFloat($("#leftMoney").text());
    var _value2 = $("#money").attr('value2');
    if ($("#money").val().search(/^\d*(?:\.\d{0,2})?$/) == -1 || $("#money").val()>_leftMoney) {
        _value2 ? $("#money").val(_value2) : $("#money").val('');
    } else {
        $("#money").attr("value2",$("#money").val());
    }
    var money = $("#money").val();
    if (money > 0) {
        if (parseFloat(money) - notRecommendMoney - recommendMoney >= 0) {
            $("#subMoney").html(parseFloat(money) - notRecommendMoney - recommendMoney);
        }
    } else {
        $("#subMoney").html("0");
    }


    if (data.borrowModel) {
        getRate(data.borrowModel.apr, data);
    }
    if (data.experienceModel) {
        getRate(data.experienceModel.apr, data);
    }
    if (data.ppfundModel) {
        getRate(data.ppfundModel.apr, data);
    }
}
/**
 * 显示红包
 */
function showRedPacketDiv() {
    $("#redPacket").show();
    $("#coupons").hide();
    $("#main").hide();
}

/**
 * 显示加息券
 */
function showCouponsDiv() {
    $("#coupons").show();
    $("#redPacket").hide();
    $("#main").hide();
}
/**
 * 时间戳转换
 * 
 * @param unixtime
 * @returns
 */
function jsDateTime(unixtime) {
    var date = new Date(unixtime);

    return date.format("yyyy-MM-dd");
}
Date.prototype.format = function(format) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    }

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }

    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}
