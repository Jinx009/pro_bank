"use strict";
var uns = _.noConflict();  //使用underscore库
$(function() {
    //底部导航颜色切换
    $("#foot_nav_list_a").css("color", "#326eaf").find("svg").css({
        "fill": "#326eaf",
        "stroke-width": "0"
    });
    //Vue过滤器，添加特殊字符
    Vue.filter('addPer', function(value,before,after) {
        return before+value+after;
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


    $.ajax({
        url: '/product/showPopularProductList.html',
        type: 'GET',
        dataType: 'JSON',
        ansyc: false,
        success: function(res) {
                // 数据获取和处理
                var goods = res.data;
                for (var ii = 0, ll = goods.length; ii < ll; ii++) {
                    goods[ii].accountCan = false; //可投余额
                    goods[ii].reTime = false; //倒计时
                    goods[ii].reTimeStr = false; //倒计时文字
                    goods[ii].proStatus=""; //产品状态
                    // goods[ii].nowTime=false;
                    //体验金
                    if (goods[ii].experienceModel) {
                        //是否抢光了
                        goods[ii].isNone = (parseFloat(goods[ii].experienceModel.scales) < 100) ? true:false;
                        // 产品状态
                        goods[ii].proStatus = (parseFloat(goods[ii].experienceModel.scales) < 100) ? 3:1;
                        //浮动加息
                        goods[ii].interestRateValue = (goods[ii].experienceModel.interestRateValue) ? (goods[ii].experienceModel.interestRateValue) : false;
                    }
                    //现金类
                    if (goods[ii].ppfundModel) {
                        goods[ii].isNone = (parseFloat(goods[ii].ppfundModel.scales) < 100) ? true:false;
                        goods[ii].proStatus = (parseFloat(goods[ii].ppfundModel.scales) < 100) ? 3:1;
                        goods[ii].interestRateValue = (goods[ii].ppfundModel.interestRateValue) ? (goods[ii].ppfundModel.interestRateValue) : false;
                    }
                    //短期
                    if (goods[ii].borrowModel) {
                        goods[ii].isNone = (parseFloat(goods[ii].borrowModel.scales) < 100) ? true:false;
                        goods[ii].proStatus = (parseFloat(goods[ii].borrowModel.scales) < 100) ? 3:1;
                        goods[ii].interestRateValue = (goods[ii].borrowModel.interestRateValue) ? (goods[ii].borrowModel.interestRateValue) : false;
                        goods[ii].accountCan = parseFloat(parseFloat(goods[ii].borrowModel.account).toFixed(2) - parseFloat(goods[ii].borrowModel.accountYes).toFixed(2)).toFixed(2);
                        var nowTime = res.systemDate;
                        var fixTime = goods[ii].borrowModel.fixedTime;
                        var expTime = goods[ii].borrowModel.expirationTime;
                        if (nowTime < expTime) {
                            if (nowTime < fixTime) {
                            // goods[ii].nowTime=nowTime;
                            goods[ii].reTimeSpan = '即将上线：';
                            goods[ii].reTime = fixTime; //热销中
                        }else{
                            // goods[ii].nowTime=nowTime;
                            goods[ii].reTime = expTime; //即将开始
                            goods[ii].reTimeSpan = '剩余时间：';
                            }
                        }else{
                            goods[ii].proStatus=2;
                        }
                        // goods[ii].reTimeSpan = "剩余时间："; //此行仅做测试用
                        // goods[ii].reTime = fixTime / 1000; //此行仅做测试用
                    }
                } //for
                //初始化Vue组件，渲染数据到页面
                var product_list = new Vue({
                    el: '#product_list',
                    data: {
                        products: goods,
                        v_css: {
                            floatRate: 'background: #ff7e00; color: #fff; display: inline-block; width:75px;height:24px;line-height:24px; border-radius:3px;'
                        },
                        nowTime:nowTime
                    }
                });

                product_list.$nextTick(function() {
                    var self = this;
                    uns(self.products).map(function(item) {
                        //倒计时函数
                        var reTimes = parseInt((item.reTime - self.nowTime) / 1000);
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
                                item.reTimeStr = item.reTimeSpan + day + '天' + hour + '时' + minute + '分' + second + '秒';
                            }
                        }, 1000);
                        return self.products;
                    });
                }); //nextTick


            } //success
    }); //ajax
}); //$(function)
