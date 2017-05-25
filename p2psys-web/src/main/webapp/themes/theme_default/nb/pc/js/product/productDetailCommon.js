/**
 * 初始化
 */

var redPacketData;
$(function() {
    ddd();
    ddd2();

    $(window).scroll(function() {
        ddd();
        ddd2();
    });
    $(window).bind("resize", ddd);
    $(window).bind("resize", ddd2);

    changeHeader("产品中心");
    $("#safe .safetyControl_box").hover(function() {
        var index = $(this).index();
        if (index == 0) {
            $("#safeBox1").toggle();
            $("#control").toggle();
        }
        if (index == 1) {
            $("#safeBox2").toggle();
            $("#control").toggle();
        }
        if (index == 2) {
            $("#safeBox3").toggle();
            $("#control").toggle();
        }

    });

    $("#safe1 .safetyControl_box").hover(function() {
        var index = $(this).index();
        if (index == 0) {
            $("#safeBox4").toggle();
            $("#control").toggle();
        }
        if (index == 1) {
            $("#safeBox5").toggle();
            $("#control").toggle();
        }

    });

    //红包数据
    $.ajax({
        url: "/nb/pc/redPacketListJson.action?time=" + jsStrToTime(),
        type: "GET",
        dataType: "json",
        success: function(res) {
            redPacketData = res.notUsedList;
            getRedPacketHtml();
            //- 判断如果红包个数为零，不显示
            if(redPacketData.length===0){
                $("#userRedPacketBtn").hide();
            }

        }
    })

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

    //Vue过滤器，添加特殊字符
    Vue.filter('addPer', function(value, before, after) {
        return before + value + after;
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
                            cid: '',
                            unusedList: couponsList.unusedList
                        }
                    });

                    $("#couponsDataDiv").delegate('.coulist', 'click', function(event) {

                        if ($(this).attr("biaoji") == 0) {
                            var couRate = $(this).children(".inner-div").children(".couRate").val();
                            var couId = $(this).children(".inner-div").children('.coupons-id').val();
                            $(this).attr("biaoji", "1");
                            $(this).siblings().attr("biaoji", "0");
                            $(this).css("background", "#fff");
                            $(this).siblings().css("background", "none");
                            $("#coupons-id").val(couId);
                            $("#selectCoupons").text(couRate);
                            $("#userCouponsBtn").text("已选择加息券：" + couRate + "%");
                        } else {
                            $(this).attr("biaoji", "0");
                            $(this).css("background", "none");
                            $("#coupons-id").val("");
                            $("#selectCoupons").text("0");
                            $("#userCouponsBtn").text("使用加息券");
                        }

                    });

                } //-success
        }) //- $ajax

    //新添加加息券弹出层end

});

/**
 * 产品详情，产品资料，安全保障，投资记录四个导航条切换
 * @param str：导航条的内容
 */
function onChange(str) {
    $(".detail-menus .detail-menus-col").each(function() {
        var index = $(this).index();
        $(this).removeClass("detail-menus-actived");
        var textValue = $(this).html();
        if (textValue == str) {
            $(this).addClass("detail-menus-actived");

            if (index == 0) {
                $(".invest-detail").show();
            }
            if (index == 1) {
                $(".invest-detail").show();
                $("#detail-menus1").hide();
            }
            if (index == 2) {
                $(".invest-detail").hide();
                $("#detail-menus3").show();
                $("#detail-menus4").show();
            }
            if (index == 3) {
                $(".invest-detail").hide();
                $("#detail-menus4").show();
            }

        }


    });
}
/**
 * 文字输入更换钱
 */
function changeMoneyText() {
    var redMoney = $("#selectRedPacketMoney").html();

    if (parseFloat(redMoney) > 0) {
        changeRedPacketMoney();
    }
    getVate(data.borrowModel.apr, data);
}

/**
 * 更改红包金钱
 */
function changeRedPacketMoney() {
    $("#selectRedPacketMoney").html(redPacketNotRecommendMoney + redPacketRecommendMoney);

    var money = $("#money").val();

    if ("" == money || null == money) {
        money = 0;
    } else {
        money = parseFloat(money);
    }
    if ((money - redPacketNotRecommendMoney - redPacketRecommendMoney) < 0) {
        $("#realMoney").html("0");
    } else {
        var realMoney = money - redPacketNotRecommendMoney - redPacketRecommendMoney;
        realMoney = realMoney.toFixed(2);
        $("#realMoney").html(realMoney);
    }

    $("#userRedPacketBtn").html("选择红包总额:￥" + $("#selectRedPacketMoney").html() + ",实际投资金额:￥" + $("#realMoney").html());
}
/**
 * 更换红包
 */
function changeRedPacket(type, id) {
    if ("1" === type) {
        for (var i = 0; i < redPacketData.length; i++) {
            if ($("#recommend" + redPacketData[i].id)) {
                $("#recommend" + redPacketData[i].id).css("background", "none");
            }

            if (id == redPacketData[i].id) {
                if (redPacketArray[i].status) {
                    redPacketRecommendMoney = 0;
                    redPacketRecommend = "";
                    redPacketArray[i].status = false;
                } else {
                    $("#recommend" + id).css("background", "white");
                    redPacketRecommendMoney = redPacketData[i].amount;
                    redPacketRecommend = id;
                    redPacketArray[i].status = true;
                }
            }
        }

    } else {
        for (var i = 0; i < redPacketData.length; i++) {
            if ($("#notRecommend" + redPacketData[i].id)) {
                $("#notRecommend" + redPacketData[i].id).css("background", "none");
            }

            if (id == redPacketData[i].id) {
                if (redPacketArray[i].status) {
                    redPacketNotRecommendMoney = 0;
                    redPacketNotRecommend = "";
                    redPacketArray[i].status = false;
                } else {
                    $("#notRecommend" + id).css("background", "white");
                    redPacketNotRecommendMoney = redPacketData[i].amount;
                    redPacketNotRecommend = id;
                    redPacketArray[i].status = true;
                }
            }
        }
    }

    changeRedPacketMoney();
}
/**
 * 获取红包信息
 */
var redPacketNotRecommend = "",
    redPacketRecommend = "",
    redPacketNotRecommendMoney = 0,
    redPacketRecommendMoney = 0,
    redPacketArray = new Array();

function getRedPacketHtml() {
    if (redPacketData.length > 0) {
        var htmlStr = "";

        for (var i = 0; i < redPacketData.length; i++) {
            var object = {};
            object.id = redPacketData[i].id;
            object.status = false;

            redPacketArray[i] = object;

            htmlStr += "<div class='col-md-3 col-xs-3 col-sm-3 col-lg-3 padding" + getListIndex(i) + " center'><div class='space-div-05' ></div>";
            if ("recommend" === redPacketData[i].serviceType) {
                htmlStr += "<div class='inner-div' id=recommend" + redPacketData[i].id + " onclick=changeRedPacket('1','" + redPacketData[i].id + "') >";
            } else {
                htmlStr += "<div class='inner-div' id=notRecommend" + redPacketData[i].id + " onclick=changeRedPacket('2','" + redPacketData[i].id + "') >";
            }
            htmlStr += "<div class='space-div-2' ></div>";
            htmlStr += "<img src='/themes/theme_default/nb/pc/images/redPacket.png' class='inner-img' >";
            htmlStr += "<p class='red-money' ><span>￥</span>" + redPacketData[i].amount + "</p>";
            if ("recommend" === redPacketData[i].serviceType) {
                htmlStr += "<p class='white-name' >" + redPacketData[i].serviceName + "</p>";
            } else {
                htmlStr += "<p class='red-name' >" + redPacketData[i].serviceName + "</p>";
            }
            htmlStr += "<p class='red-time' >" + jsDateTimeOnly(redPacketData[i].expiredTime) + "过期</p>";
            htmlStr += "<div class='space-div-2' ></div>";
            htmlStr += "</div><div class='space-div-05' ></div>";
            htmlStr += "</div>";
        }

        $("#redPacketDataDiv").html(htmlStr);
    } else {
        $("#redPacketBtn").html("");
    }
}
/**
 * 返回红包列号
 * @param index
 * @returns {Number}
 */
function getListIndex(index) {
    var num = (index + 5) % 4;

    if (0 == num) {
        return 4;
    }
    return (index + 5) % 4;
}
/**
 * 初始化红包div位置
 */
function ddd() {
    var width = $(window).width();
    var height = $(window).height();

    var leftWidth = (width - 960) / 2;
    var topHeight = (height - 760) / 2;

    if (topHeight < 100) {
        topHeight = 100;
    }

    $("#show_red_packet_div").css("left", leftWidth);
    $("#show_red_packet_div").css("top", (topHeight - 50));
}
/**
 * 关闭红包框
 */
function closeRedPacket() {
    $("#big_well").slideUp(500);
    $("#show_red_packet_div").slideUp(500);
}
/**
 * 显示红包
 */
function showRedPacketDiv() {
    var money = $("#money").val();

    if ("" == money || null == money) {
        money = 0;
    } else {
        money = parseFloat(money);
    }
    if (0 == redPacketNotRecommendMoney && 0 == redPacketRecommendMoney) {
        $("#realMoney").html(money);
    }

    $("#big_well").slideDown(500);
    $("#show_red_packet_div").slideDown(500);
}
/**
 * 初始化优惠券div位置
 */
function ddd2() {
    var width = $(window).width();
    var height = $(window).height();

    var leftWidth = (width - 960) / 2;
    var topHeight = (height - 760) / 2;

    if (topHeight < 100) {
        topHeight = 100;
    }

    $("#show_coupons_div").css("left", leftWidth);
    $("#show_coupons_div").css("top", (topHeight - 50));
}
/**
 * 关闭加息券框
 */
function closeCoupons() {
    $("#big_well").slideUp(500);
    $("#show_coupons_div").slideUp(500);
}
/**
 * 显示加息券
 */
function showCouponsDiv() {
    var money = $("#money").val();

    if ("" == money || null == money) {
        money = 0;
    } else {
        money = parseFloat(money);
    }
    if (0 == redPacketNotRecommendMoney && 0 == redPacketRecommendMoney) {
        $("#realMoney").html(money);
    }

    $("#big_well").slideDown(500);
    $("#show_coupons_div").slideDown(500);
}

/**
 * 删除数组某元素返回新数组
 * @param index
 * @param array
 * @returns {___anonymous1927_1931}
 */
function removeElement(index, array) {
    if (index >= 0 && index < array.length) {
        for (var i = index; i < array.length; i++) {
            array[i] = array[i + 1];
        }
        array.length = array.length - 1;
    }
    return array;
}
