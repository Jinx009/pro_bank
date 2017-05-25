 "use strict";
var uns = _.noConflict(); //使用underscore库
$(function() {
    var promot = $("#promot").val();
    var listSize = $("#listSize").val();
    if ("" != promot && null != promot) {
        var sessionStorage = window.localStorage;
        sessionStorage.setItem("localPromot", promot);
    }
    //底部导航颜色切换
    $("#foot_nav_list_a").css("color", "#326eaf").find("svg").css({
        "fill": "#326eaf",
        "stroke-width": "0"
    });

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

    // Vue过滤器，金额格式化（保留小数点后两位）
    Vue.filter('moneyFormat', function(value) {
        if (value == 0 || value == 'undefined') {
            return 0;
        } else {
            var n = 2;
            value = parseFloat((value + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
            var l = value.split(".")[0].split("").reverse(),
                r = value.split(".")[1];
            var t = "";
            for (var i = 0; i < l.length; i++) {
                t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
            }
            return t.split("").reverse().join("") + "." + r;
        }
    })

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
                    var nowTime = res.systemDate;
                    var fixTime = goods.borrowModel.fixedTime;
                    var expTime = goods.borrowModel.expirationTime;
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

                // matlist数据初始化
                var matlists = {};
                //  产品亮点
                matlists.highlights = uns.find(matlist, function(item) {
                    if (item.materialType) {
                        return item.materialType == 'WC202';
                    }
                });
                // console.log(highlights);
                //  产品期限
                matlists.qixian = uns.find(matlist, function(item) {
                    if (item.materialType) {
                        return item.materialType == 'WC206';
                    }
                });
                //  产品详情
                matlists.product_details = uns.find(matlist, function(item) {
                	if(goods.ppfundModel){
                		if (item.materialType) {
                            return item.materialType == 'WC203';
                        }
                	}else if(goods.borrowModel){
                		if (item.materialType) {
                            return item.materialType == 'WC205';
                        }
                	}
                });
                //  项目保障
                matlists.baozhang = uns.find(matlist, function(item) {
                    if (item.materialType) {
                        return item.materialType == 'WC209';
                    }
                });

                matlists.highlights = matlists.highlights ? matlists.highlights.material : "";
                matlists.qixian = matlists.qixian ? matlists.qixian.material : "";
                matlists.product_details = matlists.product_details ? matlists.product_details.material : "";
                matlists.baozhang = matlists.baozhang ? matlists.baozhang.material : "";

                //初始化Vue组件，渲染数据到页面
                var product_detail = new Vue({
                    el: 'body',
                    data: {
                        product: goods,
                        v_css: {
                            floatRate: 'background: #ff7e00; color: #fff; display: inline-block; width:75px;height:24px;line-height:24px; border-radius:3px;'
                        },
                        matlists: {
                            highlights: matlists.highlights,
                            qixian: matlists.qixian,
                            product_details: matlists.product_details,
                            baozhang: matlists.baozhang
                        },
                        nowTime:nowTime
                    }
                });

                // 倒计时，每隔一秒钟刷新一下倒计时显示
                product_detail.$nextTick(function() {
                    var self = this;
                    //倒计时函数
                    var reTimes = parseInt((self.product.reTime - self.nowTime) / 1000);
                    var timer = setInterval(function() {
                        if (reTimes > 1) {
                            reTimes--;
                            var day = Math.floor((reTimes / 3600) / 24);
                            var hour = Math.floor((reTimes / 3600) % 24);
                            var minute = Math.floor((reTimes / 60) % 60);
                            var second = Math.floor(reTimes % 60);
                            hour = hour < 10 ? '0' + hour : hour;
                            minute = minute < 10 ? '0' + minute : minute;
                            second = second < 10 ? '0' + second : second;
                            self.product.reTimeStr = self.product.reTimeSpan + day + '天' + hour + '时' + minute + '分' + second + '秒';
                        }
                    }, 1000);
                }); //nextTick

            } //success
    }); //ajax

    // 滑动块高度
    // var slideHeight = [];
    // setTimeout(function() {
    //     $('.swiper-wrapper .swiper-slide').each(function(index, ele) {
    //         slideHeight.push($(ele).height());
    //     });
    // }, 1000);

    // tab切换收益详情 产品详情 保障方式
    var tabsSwiper = new Swiper('.swiper-container', {
        speed: 500,
        // onInit: function(swiper) {
        //     setTimeout(function () {
        //         $('.swiper-wrapper .swiper-slide').each(function(index, ele) {
        //         slideHeight.push($(ele).height());
        //         console.log($(ele).height());
        //     });
        //         // $(".swiper-wrapper").height(slideHeight[0]);
        //     },100);
        // },
        onSlideChangeStart: function() {
            $(".tabs .active").removeClass('active');
            $(".tabs a").eq(tabsSwiper.activeIndex).addClass('active');
            // $(".swiper-wrapper").height(slideHeight[parseInt(tabsSwiper.progress * 2)]);
        }
    });
   // $(".swiper-wrapper").height(slideHeight[0]);

    $(".tabs a").on('touchstart mousedown', function(e) {
        e.preventDefault()
        $(".tabs .active").removeClass('active');
        $(this).addClass('active');
        tabsSwiper.slideTo($(this).index());
        // $(".swiper-wrapper").height(slideHeight[parseInt(tabsSwiper.progress * 2)]);
        // console.log(slideHeight);
    });

    $(".tabs a").click(function(e) {
        e.preventDefault();
    });

    // setTimeout(function() {
    //     $(".biaoji").mousedown();
    // }, 1100);


}); //$(function)
