var telreg = /^0?1[3|4|5|7|8][0-9]\d{8}$/; //手机号正则
$(function() {
        changeHeader("首页");

        var regUi = $("#regUi").val();

        if (null != regUi && "" != regUi) {
            $("#registerInviteCode").val(regUi);
            $("#reg_div_close").attr("onclick", "hideRegDiv()");

            showDiv("util_register");
        }
        // banner list
        $.ajax({
            url: "/bannerList.action?time=" + jsStrToTime(),
            type: "GET",
            dataType: "json",
            success: function(res) {
                if (null !== res.errorMsg && res.errorMsg.length) {
                    var htmlStr = "",
                        htmlStr2 = "";

                    for (var i = 0; i < res.errorMsg.length; i++) {
                        if (0 === i) {
                            htmlStr2 += "<li data-target='#carousel-example-generic' data-slide-to='0'  style='margin:5px;'  class='active'></li>";
                            htmlStr += "<div class='item active'  >";
                        } else {
                            htmlStr2 += "<li data-target='#carousel-example-generic' data-slide-to='" + i + "' style='margin:5px;' ></li>";
                            htmlStr += "<div class='item'  >";
                        }
                        htmlStr += "<a href='" + res.errorMsg[i].bannerLinkUrl + "'><img style='min-height:400px;min-width:1280px;'  src='" + window.sessionStorage.getItem("adminUrl") + res.errorMsg[i].bannerPicUrl + "'   /></a>";
                        htmlStr += "<div class='carousel-caption'></div>";
                        htmlStr += "</div>";
                    }
                    $("#bannerIndexDiv").html(htmlStr2);
                    $("#bannerDiv").html(htmlStr);

                    // 点击轮播图图片弹出抢加息券层begin
                    $("#bannerDiv").delegate('.item a', 'click', function(event) {
                        var cous = $(this).attr("href");
                        var category_id = cous.slice(7);
                        if (cous.slice(0, 7) == "coupons") {
                            event.preventDefault();
                            var self = $(this);
                            layer.confirm('<div style="font-size: 16px; font-weight: 300; color: #4A4747; margin:18px 40px 23px;"><div class="wrong-msg" style="display:none; font-size: 14px;color:red; text-align: center;"></div><input type="tel" style="padding:1px 0 1px 4px; height: 37px; margin-top:4px; margin-bottom: 20px; border-radius: 3px; border: 1px solid #31b0d5; width:260px;" placeholder="请输入您的手机号" id="donation-tel"><div style="display: -webkit-flex; display: -ms-flexbox; display: flex; width:260px; margin:0 auto;" autofocus><input id="donation-code" type="text" style="padding:1px 0 1px 4px; -webkit-flex: 1; -ms-flex: 1; flex: 1; height: 39px; border-top-left-radius: 3px; border-bottom-left-radius: 3px; border-top: 1px solid #31b0d5; border-left: 1px solid #31b0d5; border-bottom: 1px solid #31b0d5;" placeholder="验证码"><img src="/validimg.html" alt="点击刷新" id="change_code" style="height:39px;-webkit-flex: 1; -ms-flex: 1; flex: 1; border-top-right-radius: 3px; border-bottom-right-radius: 3px;border-top: 1px solid #31b0d5; border-right: 1px solid #31b0d5; border-bottom: 1px solid #31b0d5;" /></div></div>', {
                                btn: ['确认', '取消'] //按钮
                            }, function(index) {
                                var loot_tel = $("#donation-tel").val();
                                var loot_code = $("#donation-code").val();
                                if (!telreg.test(loot_tel)) {
                                    $(".wrong-msg").html("手机号不正确").show();
                                } else {
                                    $.ajax({
                                        url: '/nb/pc/member/lootCoupons.html',
                                        type: 'POST',
                                        data: {
                                            category_id: category_id,
                                            loot_tel: loot_tel,
                                            loot_code: loot_code
                                        },
                                        success: function(res) {
                                                // if (res.result == "failure") {
                                                //     $(".wrong-msg").html("验证码不正确").show();
                                                //     $("#change_code").attr("src", '/validimg.html?t=' + Math.random());
                                                // } else if (res.result == "success") {
                                                //     layer.close(index);
                                                //     layer.msg('赠送成功');
                                                // }
                                                // ###################################
                                                // 0-：成功抢到加息券
                                                // 1-：失败（包含未知错误）
                                                // 2-：验证码不正确
                                                // 3-：已经有此加息券，无法再抢
                                                // 4-：今天的加息券已经抢完(自己的一次机会用完or今天的券已发完)
                                                // 5-：未抽到明天再来
                                                switch (res.code) {
                                                    case 0:
                                                        layer.close(index);
                                                        layer.msg("成功抢到加息券");
                                                        break;
                                                    case 1:
                                                        $(".wrong-msg").html("未知错误，请重试").show();
                                                        $("#change_code").attr("src", '/validimg.html?t=' + Math.random());
                                                        break;
                                                    case 2:
                                                        $(".wrong-msg").html("验证码不正确").show();
                                                        $("#change_code").attr("src", '/validimg.html?t=' + Math.random());
                                                        break;
                                                    case 3:
                                                        layer.close(index);
                                                        layer.msg("已结抢过了，别太贪心哦");
                                                        break;
                                                    case 4:
                                                        layer.close(index);
                                                        layer.msg("今天的加息券已经抢完");
                                                        break;
                                                    case 5:
                                                        layer.close(index);
                                                        layer.msg("未抽到，明天再来吧^_^");
                                                        break;
                                                } //-switch
                                            } //-$ajax-url=/nb/pc/member/lootCoupons.html-success
                                    }); //-$ajax-url=/nb/pc/member/lootCoupons.html
                                } //-else
                            }, function(index) {
                                layer.close(index);
                            }); //-layer.confirm
                            // 验证码点击更新图片
                            $("#change_code").click(function() {
                                this.src = '/validimg.html?t=' + Math.random();
                            });
                        } //-if href=coupons

                    }); //-$.swiper-wraper .swiper-slide a click
                    // 点击轮播图图片弹出抢加息券层end


                }
            }
        })

        // 产品分类标签 icon
        $.ajax({
            url: "/product/showProductTypeFlagListForPc.action?time=" + jsStrToTime(),
            type: "GET",
            dataType: "json",
            success: function(res) {
                if (null != res.data && res.data.length) {
                    var htmlStr = "";

                    for (var i = 0; i < res.data.length; i++) {
                        htmlStr += "<div class='item' onclick=openUrl('/nb/pc/product/product_list.html?id=" + res.data[i].id + "') >";

                        if (res.data[i].iconUrl && ifTrue(res.data[i].iconUrl)) {
                            htmlStr += "<img src='" + res.data[i].iconUrl + "' id=info_img_0" + i + " onmouseover=showChange('" + i + "')  class='o-img' >";
                        } else {
                            htmlStr += "<img src='/themes/theme_default/nb/pc/images/product_index_img01.png' ";
                            htmlStr += " id=info_img_0" + i + " onmouseover=showChange('" + i + "')  ";
                            htmlStr += " class='o-img' ";
                        }

                        htmlStr += "<div class='txt' >";
                        htmlStr += "<div class='space-div-2' ></div>";
                        htmlStr += "<h4><a href='#' class='txt-a' id=info_a_0" + i + " >" + res.data[i].flagName + "</a></h4>";
                        htmlStr += "<div class='space-div-6' >";
                        htmlStr += "<div class='owl-info-div' id=owl_div0" + i + " >";
                        htmlStr += "<img src='/themes/theme_default/nb/pc/images/triangle.png' >";
                        htmlStr += "<div class='owl-info' >" + res.data[i].flagDescription + "</div>";
                        htmlStr += "</div></div></div></div>";
                    }
                    $("#scroll").html(htmlStr);
                    showChange("0");
                    $('#scroll').owlCarousel({
                        items: 4,
                        autoPlay: false,
                        navigation: true,
                        navigationText: ["", ""],
                        scrollPerPage: true
                    });
                }
            }
        })

        //热门产品
        $.ajax({
            url: "/nb/pc/product/showPopularProductListForPc.action?time=" + jsStrToTime(),
            type: "GET",
            dataType: "json",
            success: function(res) {
                var systemDate = res.systemDate;
                if (ifTrue(res.data)) {
                    var htmlStr = "";
                    var index = 0;

                    for (var i = 0; i < res.data.length; i++) {
                        if (index < 4) {
                            if (!res.data[i].experienceModel) {

                                if (0 == i % 2) {
                                    htmlStr += "<div class='row list_col bg-white' >";
                                } else {
                                    htmlStr += "<div class='row list_col ' >";
                                }
                                htmlStr += "<div class='col-md-2 col_1' >";
                                htmlStr += getProductName(res.data[i].productName);
                                htmlStr += "</div>";
                                htmlStr += "<div class='col-md-2 text col_2' >";
                                htmlStr += "<p class='rate'  >" + getRate(res.data[i].lowestRefundRate, res.data[i].highestRefundRate, res.data[i]) + "</p>";
                                htmlStr += "<p>预计年化收益率</p>";
                                htmlStr += "</div>";
                                htmlStr += "<div class='col-md-2 text col_3' >";
                                htmlStr += "<p class='startMoney'>" + res.data[i].lowestAccount + "<span>元</span></p>";
                                htmlStr += "<p>起投金额</p>";
                                htmlStr += "</div>";
                                htmlStr += "<div class='col-md-2 text col_3' >";
                                htmlStr += "<p class='startMoney' ><span>" + getProductType(res.data[i]) + "</span></p>";
                                htmlStr += "<p>还款方式</p>";
                                htmlStr += "</div>";
                                htmlStr += "<div class='col-md-2 text col_4' >";
                                htmlStr += "<p class='investState' >" + getInvestDate(res.data[i]) + "</p>";
                                htmlStr += "<p>投资期限</p>";
                                htmlStr += "</div>";
                                htmlStr += "<div class='col-md-2 text col_5' >";
                                htmlStr += getInvestBtn(res.data[i], systemDate);
                                htmlStr += "</div>";
                                htmlStr += "</div>";

                                index++;
                            }
                        }
                    }
                    $("#product_list_data").html(htmlStr);
                }
            }
        })

        // 六大风控保障体系
        $("#safe .safetyControl_box").hover(function() {
            var index = $(this).index();
            if (index == 0) {
                $("#safeBox1").toggle();
            }
            if (index == 1) {
                $("#safeBox2").toggle();
            }
            if (index == 2) {
                $("#safeBox3").toggle();
            }
        });

        // 六大风控保障体系
        $("#safe1 .safetyControl_box").hover(function() {
            var index = $(this).index();
            if (index == 0) {
                $("#safeBox4").toggle();
            }
            if (index == 1) {
                $("#safeBox5").toggle();
            }
            if (index == 2) {
                $("#safeBox6").toggle();
            }
        });
    })
    /**
     * 更改注册信息
     */
function hideRegDiv() {
    $("#regUi").val("");

    closeRegDiv();
}
/**
 * 关闭注册框
 */
function closeRegDiv() {
    $("#big_well").slideUp(100);
    $(".util_main").slideUp(100);
}
/**
 * 产品标签鼠标移动到上面事件
 * @param id
 */
function showChange(id) {
    for (var i = 0; i < 20; i++) {
        if ($("#info_img_0" + id).length > 0) {
            hideChange(i);
        }
    }

    $("#info_img_0" + id).removeClass("o-img");
    $("#info_a_0" + id).css("color", "rgb(11,116,171)");
    $("#owl_div0" + id).show();
}

/**
 * /**
 * 产品标签鼠标移动脱离事件
 * @param id
 */
function hideChange(id) {
    $("#info_img_0" + id).addClass("o-img");
    $("#info_a_0" + id).css("color", "black");
    $("#owl_div0" + id).hide();
}
/**
 * 判断json是否为空
 * @param res
 * @returns {Boolean}
 */
function ifTrue(res) {
    if (null !== res && "" !== res) {
        return true;
    } else {
        return false;
    }
}
/**
 * 获取产品名称
 * @param data
 */
function getProductName(data) {
    if (data.toString().length <= 10) {
        return "<div  class='product_name col-md-12'>" + data + "</div>";
    } else {
        return "<div class='product_name col-md-12' style='font-size:14px;' >" + data + "</div>";
    }
}
/**
 * 获取收益率
 * @param low
 * @param high
 */
function getRate(low, high, data) {
    var rate = "";
    if (data.experienceModel) {
        var interestRateValue = data.experienceModel.interestRateValue;
        if (interestRateValue == 0 || interestRateValue == undefined || interestRateValue == null) {
            rate = ""
        } else {
            rate += "+" + interestRateValue + "<span'>%</span>"
        }
    }
    if (data.ppfundModel) {
        var interestRateValue = data.ppfundModel.interestRateValue;
        if (interestRateValue == 0 || interestRateValue == undefined || interestRateValue == null) {
            rate = ""
        } else {
            rate += "+" + interestRateValue + "<span>%</span>"
        }
    }
    if (data.borrowModel) {
        var interestRateValue = data.borrowModel.interestRateValue;
        if (interestRateValue == 0 || interestRateValue == undefined || interestRateValue == null) {
            rate = ""
        } else {
            rate += "+" + interestRateValue + "<span>%</span>"
        }
    }
    /*low = parseFloat(low).toFixed(2);
    high = parseFloat(high).toFixed(2);
    var index = parseFloat(-1).toFixed(2);*/
    if (low === high && "-1" == high) {
        return "0.0<span>%</span>" + rate;
    }

    if (low === high && "0" === high) {
        return "<span>浮动</span>" + rate;
    }
    if (low === high && "0" !== high) {
        return low + "<span>%</span>" + rate;
    }
    if (low !== high && "0" == high) {
        return "<span>" + low + "%+浮动</span>" + rate;

    }
    if (low !== high && "0" !== high) {
        return "<span>" + low + "%-" + high + "%</span>" + rate;
    }
}
/**
 * 还款方式
 * @param data
 */
function getProductType(data) {
    if (data.ppfundModel) {
        return "随投随享";
    } else {
        return "到期还款";
    }
}
/**
 * 投资期限
 * @param data
 */
function getInvestDate(data) {
    if (data.ppfundModel) {
        return "活期";
    } else {
        if ("0" == data.borrowModel.borrowTimeType) {
            return data.borrowModel.timeLimit + "月";
        }
        if ("1" == data.borrowModel.borrowTimeType) {
            return data.borrowModel.timeLimit + "天";
        }
    }
}
/**
 * 投资连接
 * @param data
 */
function getInvestUrl(data) {
    if (data.ppfundModel) {
        return "ppfundDetail('" + data.id + "','" + data.flagId + "','" + data.relatedId + "')";
    } else {
        return "borrowDetail('" + data.id + "','" + data.flagId + "','" + data.relatedId + "')";
    }
}
/**
 * 现金管理产品
 * @param productId
 * @param flagId
 * @param ppfundId
 */
function ppfundDetail(productId, flagId, ppfundId) {
    var url = "/nb/pc/product/ppfund_detail.html?productId=" + productId + "&flagId=" + flagId + "&ppfundId=" + ppfundId;

    $.ajax({
        url: "/nb/sessionUser.action?time=" + jsStrToTime(),
        type: "GET",
        ansyc: false,
        dataType: "json",
        success: function(res) {
            if (null != res.result && "" != res.result) {
                location.href = url;
            } else {
                changeLoginBtn(url);
            }
        }
    })
}
/**
 * 更改登陆确定按钮链接
 * @param url
 */
function changeLoginBtn(url) {
    $("#commonLoginBtn").attr("onclick", "");
    $("#commonLoginBtn").attr("onclick", "doProductLogin('" + url + "')");
    showDiv("util_login");
}
/**
 * 非现金
 * @param productId
 * @param flagId
 * @param investId
 */
function borrowDetail(productId, flagId, investId) {
    var url = "/nb/pc/product/borrow_detail.html?productId=" + productId + "&flagId=" + flagId + "&investId=" + investId;

    $.ajax({
        url: "/nb/sessionUser.action?time=" + jsStrToTime(),
        type: "GET",
        ansyc: false,
        dataType: "json",
        success: function(res) {
            if (null != res.result && "" != res.result) {
                location.href = url;
            } else {
                changeLoginBtn(url);
            }
        }
    })
}
/**
 * 投资按钮
 * @param data
 * @returns {String}
 */
function getInvestBtn(data, systemDate) {
    var nowTime = parseInt(systemDate);
    var appointTime = 0,
        endTime = 0;

    if (data.borrowModel) {
        appointTime = parseInt(data.borrowModel.fixedTime);
        endTime = parseInt(data.borrowModel.expirationTime);

        if (nowTime < appointTime) {
            return "<div class='investBtn' onclick=" + getInvestUrl(data) + " >预约</div>";
        } else if (nowTime > endTime) {
            return "<div class='investBtn-1' onclick=" + getInvestUrl(data) + ">结束了</div>";
        } else {
            return getScale(data);
        }
    } else {
        return getScale(data);
    }
}
/**
 * 根据投资进度判断显示的内容
 * @param data
 */
function getScale(data) {
    if (data.experienceModel) {
        if (parseFloat(data.experienceModel.account) == 0) {
            return "<div class='investBtn'  onclick=" + getInvestUrl(data) + ">立刻投资</div>";
        } else {
            if (parseFloat(data.experienceModel.scales) >= 100) {
                return "<div class='investBtn-1' onclick=" + getInvestUrl(data) + ">抢光了</div>";
            } else {
                return "<div class='investBtn' onclick=" + getInvestUrl(data) + "  >立刻投资</div>";
            }
        }
    }
    if (data.ppfundModel) {
        if (parseFloat(data.ppfundModel.account) == 0) {
            return "<div class='investBtn' onclick=" + getInvestUrl(data) + " >立刻投资</div>";
        } else {
            if (parseFloat(data.ppfundModel.scales) >= 100) {
                return "<div class='investBtn-1' onclick=" + getInvestUrl(data) + "  >抢光了</div>";
            } else {
                return "<div class='investBtn' onclick=" + getInvestUrl(data) + " >立刻投资</div>";
            }
        }
    }
    if (data.borrowModel) {

        if (parseFloat(data.borrowModel.scales) >= 100) {
            return "<div class='investBtn-1' onclick=" + getInvestUrl(data) + " >抢光了</div>";
        } else {
            return "<div class='investBtn' onclick=" + getInvestUrl(data) + " >立刻投资</div>";
        }
    }

}
