"use strict";
var telreg = /^0?1[3|4|5|7|8][0-9]\d{8}$/; //手机号正则
$(function() {
        "use strict";
        var uns = _.noConflict(); //使用underscore库
        var adminUrl = "";
        // 产品id
        var product_id = $("#product_id").val();
        //Vue过滤器，添加特殊字符
        Vue.filter('addPer', function(value, before, after) {
            return before + value + after;
        });

        // Vue过滤器，保留两位小数
        Vue.filter('fixDot', function(value) {
            return parseFloat(value).toFixed(2);
        });
        localStorage.type = "btn_one";
        // 导航颜色修改
        $("#foot_nav_home_a").css("color", "#326eaf").find("svg").css({
            "fill": "#326eaf",
            "stroke-width": "0"
        });


        /**
         * layer弹出信息函数简单封装
         * @param  {string} msg 弹出的信息
         * @return {layer}     layer_msg
         */
        function laymsg(msg) {
            layer.open({
                content: msg,
                style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:10px; letter-spacing: 2px; line-height: 100%;',
                time: 1.5
            });
        } //-laymsg

        // $("#rate").html(getRate($("#rateValueLow").val(), $("#highestRefundRate").val(), $("#interestRateValue").val()));
        // 推荐理由开始
        var featureDetail = [];
        var featureDet = [];
        featureDetail[0] = $("#feature_detail").attr("featureDetail1");
        featureDetail[1] = $("#feature_detail").attr("featureDetail2");
        featureDetail[2] = $("#feature_detail").attr("featureDetail3");
        for (var i = featureDetail.length - 1; i >= 0; i--) {
            if (featureDetail[i]) {
                featureDet.push(featureDetail[i]);
            }
        };
        var feaStr = "";
        for (var i = featureDet.length - 1; i >= 0; i--) {
            feaStr += '<div class="feature-detail"><span class="feature-icon">' + featureDet[i].slice(0, 1);
            feaStr += '</span><span class="feature-desc">' + featureDet[i].slice(1);
            feaStr += '</span></div>';
        };
        $('.feature').html(feaStr);
        // 推荐理由结束

        //获取产品详细信息，并对返回的json进行预处理
        $.ajax({
            url: "/product/showProductDetail.html?prodId=" + product_id,
            type: 'GET',
            dataType: 'JSON',
            ansyc: false,
            success: function(res) {
                    // 数据获取和处理
                    var goods = res.data;
                    var matlist = res.matList;
                    goods.accountCan = false; //可投余额
                    goods.reTime = false; //倒计时
                    goods.reTimeStr = false; //倒计时文字
                    goods.proStatus = ""; //产品状态

                    //体验金
                    if (goods.experienceModel) {
                        //是否抢光了
                        goods.isNone = (parseFloat(goods.experienceModel.scales) < 100) ? true : false;

                        // 产品状态
                        if (parseFloat(goods.experienceModel.scales) < 100) {
                            if (goods.hotProduct == 1) {
                                goods.proStatus = 3;
                            } else {
                                goods.proStatus = 4;
                            }
                        } else {
                            goods.proStatus = 1;
                        }

                        //浮动加息
                        goods.interestRateValue = (goods.experienceModel.interestRateValue) ? (goods.experienceModel.interestRateValue) : false;
                    }

                    //现金类
                    if (goods.ppfundModel) {
                        goods.isNone = (parseFloat(goods.ppfundModel.scales) < 100) ? true : false; // 产品状态
                        if (parseFloat(goods.ppfundModel.scales) < 100) {
                            if (goods.hotProduct == 1) {
                                goods.proStatus = 3;
                            } else {
                                goods.proStatus = 4;
                            }
                        } else {
                            goods.proStatus = 1;
                        }
                        goods.interestRateValue = (goods.ppfundModel.interestRateValue) ? (goods.ppfundModel.interestRateValue) : false;
                    }

                    //短期
                    if (goods.borrowModel) {
                        goods.isNone = (parseFloat(goods.borrowModel.scales) < 100) ? true : false;
                        if (parseFloat(goods.borrowModel.scales) < 100) {
                            if (goods.hotProduct == 1) {
                                goods.proStatus = 3
                            } else {
                                goods.proStatus = 4
                            }
                        } else {
                            goods.proStatus = 1;
                        }
                        goods.interestRateValue = (goods.borrowModel.interestRateValue) ? (goods.borrowModel.interestRateValue) : false;
                        goods.accountCan = parseFloat(parseFloat(goods.borrowModel.account).toFixed(2) - parseFloat(goods.borrowModel.accountYes).toFixed(2)).toFixed(2);
                        var nowTime = parseInt((new Date()).valueOf() / 1000);
                        var fixTime = parseInt(goods.borrowModel.fixedTime / 1000);
                        var expTime = parseInt(goods.borrowModel.expirationTime / 1000);
                        // console.log(nowTime ,fixTime,expTime);
                        if (nowTime < expTime) {
                            if (nowTime < fixTime) {
                                goods.reTimeSpan = '即将上线：';
                                goods.reTime = fixTime; //热销中
                            } else {
                                goods.reTimeSpan = '剩余时间：';
                                goods.reTime = expTime; //即将开始
                            }
                        } else {
                            goods.proStatus = 2;
                        }
                        // goods.reTimeSpan = "剩余时间："; //此行仅做测试用
                        // goods.reTime = fixTime / 1000; //此行仅做测试用
                    }



                    //初始化Vue组件，渲染数据到页面
                    var product_detail = new Vue({
                        el: 'body',
                        data: {
                            product: goods,
                            v_css: {
                                floatRate: 'background: #ff7e00; color: #fff; display: inline-block; width:75px;height:24px;line-height:24px; border-radius:3px;'
                            }
                        }
                    });



                } //success
        }); //ajax

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
                                    htmlStr += " <img data-src=" + adminUrl + res.data[i].bannerPicUrl + " class='swiper-lazy'/>";
                                    htmlStr += " <div class='swiper-lazy-preloader'></div>"
                                    htmlStr += "</a>";
                                    htmlStr += "</div>";
                                }
                            } else {
                                htmlStr += "<div class='swiper-slide'>";
                                htmlStr += "<a href='javascript:;' >";
                                htmlStr += " <img data-src='/themes/theme_default/nb/wechat/img/indexIcon.jpg' class='swiper-lazy'/>";
                                htmlStr += "</a>";
                                htmlStr += "</div>";
                            }

                            $(".swiper-wrapper").html(htmlStr);
                            // 首页轮播图
                            var mySwiper = new Swiper('.swiper-container', {
                                loop: true, //循环播放
                                autoplay: 5000,
                                lazyLoading: true, //延迟加载图片
                                lazyLoadingInPrevNext: true, //延迟加载当前图片之前和之后一张图片
                                lazyLoadingOnTransitionStart: true, //过渡一开始就加载
                                pagination: '.swiper-pagination', //导航分页
                                paginationClickable: true //导航点击切换
                            });



                            $(".swiper-wrapper").delegate('.swiper-slide a', 'click', function(event) {
                                var cous = $(this).attr("href");
                                var category_id = cous.slice(7);
                                if (cous.slice(0, 7) == "coupons") {
                                    event.preventDefault();
                                    layer.open({
                                        content: '<div style="font-size: 16px; font-weight: 300; color: #4A4747; margin-top: 28px; margin-bottom: 3px;position: relative;text-align: center;"><div class="wrong-msg" style="font-size: 13px; color: red; position: absolute; top: -22px; left: 50%; width: 200px; text-align: center; margin-left: -100px;"></div><input type="tel" style="padding:1px 0 1px 4px; height: 27px; margin-top:4px; margin-bottom: 20px; border-radius: 3px; border: none; width:200px;" placeholder="请输入被赠人手机号" id="loot_tel" autofocus=""><div style="width: 206px; margin: 0 auto;"><div style="width: 60%;float: left;"><input id="loot_code" type="text" style="width:100%;padding:1px 0 1px 4px; height: 27px; border-top-left-radius: 3px; border-bottom-left-radius: 3px; border-top: 1px solid #31b0d5; border-left: 1px solid #31b0d5; border-bottom: 1px solid #31b0d5;border: none;" placeholder="验证码"></div><img src="/validimg.html" alt="点击刷新" id="change_code" style="  height:29px;   width: 40%;border-top-right-radius: 3px; border-bottom-right-radius: 3px;border-top: 1px solid #31b0d5; border-right: 1px solid #31b0d5; border-bottom: 1px solid #31b0d5;border: none;"></div></div>',
                                        btn: ['确认'],
                                        style: 'min-width: 280px;min-height:125px;text-align: center;border-radius: 6px;padding-bottom: 20px;background: #ededed;',
                                        shadeClose: true,
                                        yes: function(index) {
                                                var loot_tel = $("#loot_tel").val();
                                                var loot_code = $("#loot_code").val();
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
                                                                // 0-：成功抢到加息券
                                                                // 1-：失败（包含未知错误）
                                                                // 2-：验证码不正确
                                                                // 3-：已经有此加息券，无法再抢
                                                                // 4-：今天的加息券已经抢完(自己的一次机会用完or今天的券已发完)
                                                                // 5-：未抽到明天再来
                                                                switch (res.code) {
                                                                    case 0:
                                                                        layer.close(index);
                                                                        laymsg("成功抢到加息券");
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
                                                                        laymsg("已结抢过了，别太贪心哦");
                                                                        break;
                                                                    case 4:
                                                                        layer.close(index);
                                                                        laymsg("今天的加息券已经抢完");
                                                                        break;
                                                                    case 5:
                                                                        layer.close(index);
                                                                        laymsg("未抽到，明天再来吧^_^");
                                                                        break;
                                                                } //-switch
                                                            } //-success
                                                    }); //ajax-url=/nb/pc/member/lootCoupons.html
                                                } //-else

                                            } //-yes
                                    }); //-layer.open
                                    // 验证码点击更新图片
                                    $("#change_code").click(function() {
                                        this.src = '/validimg.html?t=' + Math.random();
                                    });
                                } //-if href=coupons

                            }); //-$(".swiper-wrapper").delegate

                        }  //-ajax-url=/nb/wechat/banner.action-success
                    })  //-ajax-url=/nb/wechat/banner.action
                }   //-ajax-url=/getHostUrl.action-success
            })  //-ajax-url=/getHostUrl.action
            // 轮播图



    })
    /**
     * 获取收益
     */
function getRate(low, high, interestRateValue) {
    var rate;
    if (interestRateValue == 0) {
        rate = "";
    } else {
        rate = "<span style='color:#ff5858;font-size:1.2em;'>+" + interestRateValue + "%</span>";
    }
    if (low === high && "0" == high) {
        return "浮动" + rate;
    }
    if (low === high && "0" !== high) {
        return low + "%" + rate;
    }
    if (low !== high && "0" == high) {
        return low + "%+浮动" + rate;
    }
    if (low !== high && "0" !== high) {
        return low + "%-" + high + "%" + rate;
    }
}
