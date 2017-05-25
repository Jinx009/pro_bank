// JavaScript Document
//对网站js进行整合
//包含一些常用的js
//NewDate()兼容各个浏览器的格式化时间代码
//scrollLoading() 延迟加载图片
//nav()头部导航样式,可以选择tab形式或是下拉形式
//slideFn() 实现无缝的上滚，无缝左滚
//flashSlider() 实现轮播图片左滚
//toggleNav()导航内容收缩展开
//tabChange()tab切换，可以点击，鼠标滑过
//flowChange() 改变流转标的分数
//floatBox() 浮动导航，自由配置初始高度
//fadeSlide()轮播图片淡入淡出
//登录框的文字显示
//QQ显示函数
//地区级联处理
//账户中心头像上传
//借款图标详情介绍
//minSelect(),minCheck(),minRadio 表单数据显示
//fullScreen footer置底
//articleMenu()咨询页面左侧导航高度自适应
//showRemainTime()倒计时功能显示
//listKeep()首页文章列表数据保持不变
//navKeep()导航保持不变
//jsonArticle()首页文章数据加载
//mailAutoComplete邮箱自动填充
//testValidCode() 验证码验证长度
//snallImgScroll() 底部友情链接滚动图片
//showIE() IE浏览器提示
//select美化
define(function(require, exports, module){
//兼容各个浏览器的格式化时间代码----------------------------------------------------
function NewDate(str) { 
	str = str.split('-'); 
	var date = new Date(); 
	date.setUTCFullYear(str[0], str[1] - 1, str[2]); 
	date.setUTCHours(0, 0, 0, 0); 
	return date; 
} 
//我要投资页面类别搜索------------------------------------------------------------------------	
	function reg(){
        var iput=document.getElementById("infonlv");
        iput.value=iput.value.replace(/[^0-9\.]/g,'');
        iput.value=iput.value.replace(/\.{2,}/g,'.');
        if(/\.\d+\.+/.test(iput.value)){
            iput.value=iput.value.replace(/\.+$/g,'');
        }
    }
   $("#infonlv").keyup(function(){
	   	reg();
	});
//我要借款页面利率输入控制小数点------------------------------------------------------------------------	



//延时加载图片-----------------------------------------------------------------------------
(function($) {
$.fn.scrollLoading = function(options) {
	var defaults = {
		attr: "data-url",
		container: $(window),
		callback: $.noop
	};
	var params = $.extend({}, defaults, options || {});
	params.cache = [];
	$(this).each(function() {
		var node = this.nodeName.toLowerCase(), url = $(this).attr(params["attr"]);
	//重组
		var data = {
				obj: $(this),
				tag: node,
				url: url
			};
		params.cache.push(data);
	});
	var callback = function(call) {
		if ($.isFunction(params.callback)) {
			params.callback.call(call.get(0));
		}
	};
	//动态显示数据
	var loading = function() {
		var contHeight = params.container.height();
		if ($(window).get(0) === window) {
			contop = $(window).scrollTop();
		} else {
			contop = params.container.offset().top;
		}
		$.each(params.cache, function(i, data) {
			var o = data.obj, tag = data.tag, url = data.url, post, posb;
			if (o) {
				post = o.offset().top - contop, post + o.height();
				if ((post >= 0 && post < contHeight) || (posb > 0 && posb <= contHeight)) {
					if (url) {
						//在浏览器窗口内
						if (tag === "img") {
							//图片，改变src
							callback(o.attr("src", url));
						} else {
							o.load(url, {}, function() {
								callback(o);
							});
						}
					} else {
						// 无地址，直接触发回调
						callback(o);
					}
					data.obj = null;
				}
			}
		});
	};
	//事件触发
	//加载完毕即执行
	loading();
	//滚动执行
	params.container.bind("scroll", loading);
	};
})(jQuery);


//常用js函数------------------------------------------------------------------------	

	//首页导航------------------------------------------------------------------------------
	(function($){
        $.fn.extend({
            nav:function(options){
                var defaults = {
                    isTab: true,
					childLi:".navul li",
					childContent:".navList",
					hoverClassName:"hover",
					hasClassName:"hover2"
                };
                var options = $.extend(defaults,options);
                 this.each(function(){
                    var o = options;
                    var obj = $(this);
                    var items = $(o.childLi,obj);
                    var oDiv = $(o.childContent,obj);
                    var Timer;
                     if(o.isTab){
                         oDiv.eq(0).show(); // tab切换可以使用这句;
                     }
                     var showDiv = function(){
                         items.each(function(i){
                            if($(items[i]).hasClass(o.hasClassName))
                            {
								$(items).removeClass(o.hoverClassName);
                                $(items[i]).addClass(o.hoverClassName);
                                oDiv.hide();
                                $(oDiv[i]).show();
                            }
                         });
                     };
                     if(o.isTab==true){
                        showDiv();
                     }
                    items.hover(function(){
                        var index = items.index(this);
                		$(items).removeClass(o.hoverClassName);
                        $(items[index]).addClass(o.hoverClassName);
                        $(oDiv).hide();
                        $(oDiv[index]).show();
                        clearTimeout(Timer)
                    });
                    $(oDiv).mouseenter(function(){
                    	clearTimeout(Timer)
                    })
                    
                     $(this).mouseleave(function(){
                        if(o.isTab==true){
							$(items).removeClass(o.hoverClassName);
                            showDiv();
                        }else{
							$(items).removeClass(o.hoverClassName);
							Timer = setTimeout(function(){
								oDiv.hide();
							},300)
                        }
                    });

                })
            }
        })
    })(jQuery);
	
	
	//账户中心导航------------------------------------------------------------------------------
	(function($){
        $.fn.extend({
            memberNav:function(options){
                var defaults = {
					childLi:".navul li",
					childContent:".navList",
					hoverClassName:"hover"
                };
                var options = $.extend(defaults,options);
                 this.each(function(){
                	
                    var o = options;
                    var obj = $(this);
                    var items = $(o.childLi,obj);
                    var oDiv = $(o.childContent,obj);
                    var showDiv = function(){
                         items.each(function(i){
                            if($(items[i]).hasClass(o.hasClassName))
                            {
								$(items).removeClass(o.hoverClassName);
                                $(items[i]).addClass(o.hoverClassName);
                                oDiv.hide();
                                $(oDiv[i]).show();
                            }
                         });
                     };
                    
                    items.hover(function(){
                        var index = items.index(this);
                		$(items).removeClass(o.hoverClassName);
                        $(items[index]).addClass(o.hoverClassName);
                        $(oDiv).hide();
                        $(oDiv[index]).show();
                    });
                    items.mouseleave(function(){
                    	$(items).removeClass(o.hoverClassName);
						 oDiv.hide();
                    });

                })
            }
        })
    })(jQuery);
	
	
	//实现无缝上下滚动 无缝左右滚动--------------------------------------------------------------
    (function($){
        $.fn.extend({
            slideFn:function(options){
                var defaults = {
                    isTop:true,//是否
                    slideTimer:"3000"
                };
                var options = $.extend(defaults,options);
                this.each(function(){
                    var o = options;
                    var obj = $(this);
                    var oUl = obj.find("ul:first");
                    var oLi = $("li",oUl);

                    var Timer;
                    obj.hover(function(){
                        clearInterval(Timer);
                    },function(){
                        Timer = setInterval(function(){
                            if(o.isTop==true){
                                slideTop(oUl);
                            }else{
                                slideLeft(oUl);
                            }
                        }, o.slideTimer )
                    }).trigger("mouseleave");

                    var slideTop = function(box){
                        var oLiHeight = box.find("li:first").height();
                        box.animate({"marginTop":-oLiHeight+"px"},800,function(){
                            box.css("marginTop",0).find("li:first").appendTo(box);
                        })
                    };//上滚
                    var slideLeft = function(box2){
                        box2.css("width",((oLi.width())*(oLi.length))+"px");
                        var oLiWidth = box2.find("li:first").width();
                        box2.animate({"marginLeft":-oLiWidth+"px"},800,function(){
                            box2.css("marginLeft",0).find("li:first").appendTo(box2);
                        })
                    };//左滚
                })
            }

        })
    })(jQuery);
	
  //轮播图片-------------------------------------------------------
    (function(jQuery) {
        jQuery.fn.flashSlider = function(options) {
            var options = jQuery.extend({}, jQuery.fn.flashSlider.defaults, options);
            this.each(function() {
                var obj = jQuery(this);
                var curr = 1; //当前索引
                var jQueryimg = obj.find("img");
                var s = jQueryimg.length;
                var w = jQueryimg.eq(0).width();
                var h = jQueryimg.eq(0).height();

                var jQueryflashelement = jQuery("ul", obj);
                options.height == 0 ? obj.height(h) : obj.height(options.height);
                options.width == 0 ? obj.width(w) : obj.width(options.width);
                obj.css("overflow", "hidden");
                obj.css("position", "relative");
                jQueryflashelement.css('width', s * w);
                if (!options.vertical) {
                    jQuery("li", obj).css('float', 'left')
                }
                else {
                    jQueryimg.css('display', 'block')
                };
                if (options.controlsShow) {
                    var navbtnhtml = '<div id="flashnvanum"><div class="numbox">';
                    for (var i = 0; i < s; i++) {
                        navbtnhtml += '<span>' + (i + 1) + '</span>';
                    }
                    navbtnhtml += '</div></div>';
                    obj.append(navbtnhtml);
                    obj.find("#flashnvanum span").hover(function() {
                        var num = jQuery(this).html();
                        flash(num, true);
                    }, function() {
                        timeout = setTimeout(function() {
                            flash((curr / 1 + 1), false);
                        }, options.pause / 4);
                    });
                };
                function setcurrnum(index) {
                    obj.find("#flashnvanum span").eq(index).addClass('on').siblings().removeClass('on');
                }
                function flash(index, clicked) {
                    jQueryflashelement.stop();
                    var next = index == s ? 1 : index + 1;
                    curr = index - 1;
                    setcurrnum((index - 1));
                    if (!options.vertical) {
                        p = ((index - 1) * w * -1);
                        jQueryflashelement.animate(
                                { marginLeft: p },
                                options.speed, options.easing
                        );
                    } else {
                        p = ((index - 1) * h * -1);
                        jQueryflashelement.animate(
                                { marginTop: p },
                                options.speed, options.easing
                        );
                    };
                    if (clicked) {
                        clearTimeout(timeout);
                    };
                    if (options.auto && !clicked) {
                        timeout = setTimeout(function() {
                            flash(next, false);
                        }, options.speed + options.pause);
                    };
                }
                var timeout;
                //初始化
                setcurrnum(0);

                if (options.auto) {
                    timeout = setTimeout(function() {
                        flash(2, false);
                    }, options.pause);
                }
            });
        };
        //默认值
        jQuery.fn.flashSlider.defaults = {
            controlsShow: true, //是否显示数字导航
            vertical: false, //纵向还是横向滑动
            speed: 800, //滑动速度
            auto: true, //是否自定滑动
            pause: 4000, //两次滑动暂停时间
            easing: "swing", //easing效果，默认swing，更多效果需要easing插件支持
            height: 222, //容器高度，不设置自动获取图片高度
            width: 741//容器宽度，不设置自动获取图片宽度
        }
    })(jQuery);
	
    //我的账户收缩导航显示----------------------------------------------------------------
    (function($){
        $.fn.extend({
            toggleNav:function(options){
                var defaults = {
                    allShow:false,//是否全部显示
					childTitle:".toggle-title",//标题
					childContent:".toggle-content",//内容
					itemToggleBox:".togglediv",
					hoverClassName:"hover"//hover过后的样式
                };
                var options = $.extend(defaults,options);
                this.each(function(){
                    var o =options;
                    var obj = $(this);
                    var objDom = $(o.itemToggleBox, obj);
                    if(o.allShow==true){
                        $(o.childTitle,obj).addClass(o.hoverClassName)
                    }
                    var clickFn = function(box){
                        var oLi = $(o.childTitle,box);
                        var oDiv = $(o.childContent,box);
                        if(oLi.hasClass(o.hoverClassName))
                        {
                            oDiv.show();
                        }
                        oLi.toggle(function(){
                            if(oLi.hasClass(o.hoverClassName))
                            {
                                if(!oDiv.is(":animated")){
                                    $(this).removeClass(o.hoverClassName);
                                    oDiv.slideUp();
                                }
                            }else{
                                if(!oDiv.is(":animated")){
                                    $(this).addClass(o.hoverClassName);
                                    oDiv.slideDown();
                                }
                            }//有hover样式 则先隐藏，再显示
                        },function(){
                            if(!oLi.hasClass(o.hoverClassName))
                            {
                                if(!oDiv.is(":animated")){
                                    $(this).addClass(o.hoverClassName);
                                    oDiv.slideDown();
                                }
                            }else{
                                if(!oDiv.is(":animated")){
                                    $(this).removeClass(o.hoverClassName);
                                    oDiv.slideUp();
                                }
                            }//没有hover样式 则先显示，再隐藏

                        })
                    };
                    objDom.each(function(i){
                        clickFn($(this));
                    });

                })
            }
        })
    })(jQuery);
	
  //我的账户收缩导航显示----------------------------------------------------------------
    (function($){
        $.fn.extend({
            toggleTab:function(options){
                var defaults = {
                    allShow:true,//是否全部显示
					childTitle:".toggle-title",//标题
					childContent:".toggle-content",//内容
					itemToggleBox:".togglediv",
					hoverClassName:"hover",//hover过后的样式
					pageNum:0
                };
                var options = $.extend(defaults,options);
                return  this.each(function(){
                    var o =options;
                    var obj = $(this);
                    
                    var oLi = $(o.childTitle,obj);
                    var oDiv = $(o.childContent,obj);
                    oLi.each(function(){
                    	var that = $(this)
                    	$(oLi[0]).addClass(o.hoverClassName)
                    	
                    	if(o.pageNum!=0){
                    		oLi.removeClass(o.hoverClassName)
	                    	oDiv.hide();
                    		$(oLi[o.pageNum]).addClass(o.hoverClassName)
	                    	$(oDiv[o.pageNum]).show(300);
                    	}
	                    that.click(function(){
	                    	var index = oLi.index($(this));
	                    	if($(this).hasClass(o.hoverClassName))	
	                    	{
	                    		$(this).text("点击修改")
	                    		oLi.removeClass(o.hoverClassName)
	                    		$(oDiv[index]).hide(300);
	                    	}else{
	                    		oLi.text("点击修改").removeClass(o.hoverClassName)
	                    		$(this).text("点击收起").addClass(o.hoverClassName);
	                    		$(oDiv).hide();
	                    		$(oDiv[index]).show(300)
	                    	}
	                    })
                    })
                })
            }
        })
    })(jQuery);
	
    
  //tab切换-----------------------------------------------------------------------
    (function($){
        $.fn.extend({
            tabChange:function(options){
                var defaults = {
                    isClick:false,
                    isHover:true,
					childLi:".tab-ul ",//tab选项卡
					childContent:".tab-contentbox",//tab内容
					hoverClassName:"hover",//选中当前选项卡的样式
					callBack:false	
                };
                var options = $.extend(defaults,options);
                this.each(function(){
                    var o = options;
                    var obj = $(this);
                    var oLi = $(o.childLi,obj);
                    var oDiv = $(o.childContent,obj);
                    var Timer;
                    var oLiClick = function(){
                        oLi.eq(0).addClass(o.hoverClassName);
                        oDiv.eq(0).show();
                        addType(oLi.eq(0).find("span").attr("data-value"));
                        oLi.click(function(){
                        	addType($(this).find("span").attr("data-value"));
                            var index = oLi.index(this);
                            $(oLi[index]).addClass(o.hoverClassName).siblings().removeClass(o.hoverClassName);
                            oDiv.hide();
                            $(oDiv[index]).show();
                        })
                    };
                    var oLiHover = function(){
                        oLi.eq(0).addClass(o.hoverClassName);
                        oDiv.eq(0).show();
                        oLi.mouseenter(function(){
                        		addType($(this).find("span").attr("data-value"))
                                var index = oLi.index(this);
                                $(oLi[index]).addClass(o.hoverClassName).siblings().removeClass(o.hoverClassName);
                                oDiv.hide();
                                $(oDiv[index]).show();
                        });
                    };

                    if(o.isClick==true)
                    {
                        oLiClick();
                    }
                    else if(o.isHover==true)
                    {
                        oLiHover();
                    }
                    
                    function addType(vals){
                    	
                    }

                })
            }
        });
    })(jQuery);
	
  //流转标的份数加减---------------------------------------------------------------------------
	(function($){
		$.fn.extend({
			flowChange:function(options){
				var defaults = {
					changeBox:"#flow_num",//对form表单的循环
					addBtn:".add",//增加按钮
					lessBtn:".less",//减少按钮
					numTxt:".nums",//文本显示框
					maxNumBox:".max"//最大数的div
				};
				var options = $.extend(defaults,options);
				this.each(function(){
					var o = options;
					var obj = $(this);
					var changeNum = $(o.changeBox,obj);
					var getNum = function(obj0){
						var inputBox = $(o.numTxt,obj0);
						inputBox.blur(function(){
							inputNum  = parseInt(inputBox.attr("value"));
							var maxNum = parseInt($(o.maxNumBox,obj0).text());
							if(!isNaN(inputNum))
							{
								if(inputNum>maxNum)
								{
									inputBox.attr("value",maxNum);
								}
								else{
									inputBox.attr("value",inputNum);
								}
							}else{
								inputBox.attr("value",0);
							}
						})
					}
					var add = function(obj1){//增加
						var addObj = $(o.addBtn,obj1);
						addObj.click(function(){
							var num = $(o.numTxt,obj1).attr("value");
							var maxNum = parseInt($(o.maxNumBox).text());
							num++;
							if(num>maxNum)
								return false;//获取当前的对象的 最大分数  判断是否大于，大于剩下的分数 就不在增加
							$(o.numTxt,obj1).attr("value",num);
						})
					}
					var less = function(obj2){ //减少
						var lessObj = $(o.lessBtn,obj2);
						lessObj.click(function(){
							var num = $(o.numTxt,obj2).attr("value");
							num--;
							if(num<0)
							{
								return false;
							}//获取当前的对象的最小值  判断是否小于0，小于0 就不在减少
							$(o.numTxt,obj2).attr("value",num);
						})
					}
					changeNum.each(function(){
						add($(this)); //对当前form下面的 增加分数 进行操作
						less($(this));//对当前form下面的 减少分数 进行操作
						getNum($(this));
					})
	
				})
			}
		})
	})(jQuery);
	
	//浮动导航-----------------------------------------------------------------------
	(function($){
		$.fn.extend({
			floatBox:function(options){
				var defaults = {
					topVal:180,
					scrollTopVal:300
				};
				var options = $.extend(defaults,options);
				this.each(function(){
					var o = options;
					var obj = $(this);
					$(window).scroll(function (){
						var offsetTop = $(window).scrollTop() + parseInt(o.topVal) +"px";//滚动的高度加上当前的绝对定位的top值
						obj.animate({top : offsetTop},{duration:500 , queue:false});
					});
				});
			}
		})				
	})(jQuery);
	
	//轮播图片淡入淡出---------------------------------------------------------------------------------
	(function($){
		$.fn.extend({
			fadeSlide:function(options){
			var defaults = {
				slideTime:"3000",//变化时间
				childLi:".fade-ul li",//数字选项
				childContent:".fade-content div",//显示内容
				hoverClass:"hover"//hover过后的样式
				};
			var options = $.extend(defaults,options);
			this.each(function(){
				var o = options;
				var obj = $(this);
				var oLi = $(o.childLi,obj);
				var oDiv = $(o.childContent,obj);
				var Timer;
				var index = 1;
				var leng = (oLi.length);
				$(oDiv[0]).css({"display":"block"})
				oLi.mouseover(function(){
					index = oLi.index(this);
					showImg(index);
					}).eq(0).mouseover();
				obj.hover(function(){
						clearInterval(Timer);
					},function(){
						Timer = setInterval(function(){
								showImg(index);
								index++;
								if(index==leng)
								{
									index=0;	
								}
							},o.slideTime)	
					}).trigger("mouseleave");
					function showImg(index){
						$(oDiv[index]).fadeIn("slow").show().siblings().fadeOut().hide();
						$(oLi).removeClass(o.hoverClass).eq(index).addClass(o.hoverClass);
					}	
				});
			}
		});
	})(jQuery);
	
	
	



//QQ显示----------------------------------------------------------------------------------------------------------------------------
var online= new Array();
var urlroot = "http://gdp.istudy.com.cn/";
var tOut = -1;
var drag = false;
var g_safeNode = null;
lastScrollY = 0;
var kfguin;
var ws;
var companyname;
var welcomeword;
var type;
var wpadomain;
var eid;
var Browser = {
    ie:/msie/.test(window.navigator.userAgent.toLowerCase()),
    moz:/gecko/.test(window.navigator.userAgent.toLowerCase()),
    opera:/opera/.test(window.navigator.userAgent.toLowerCase()),
    safari:/safari/.test(window.navigator.userAgent.toLowerCase())
};
if(kfguin)
{

    //_Ten_rightDivHtml = '<div id="_Ten_rightDiv" style="position:absolute; top:160px; right:1px; display:none;">';

    //_Ten_rightDivHtml += kf_getPopup_Ten_rightDivHtml(kfguin,ws,wpadomain);

    //_Ten_rightDivHtml += '</div>';

    //document.write(_Ten_rightDivHtml);
    if(type==1 && kf_getCookie('hasshown')==0)
    {
        companyname = companyname.substr(0,15);
        welcomeword = kf_processWelcomeword(welcomeword);

        kfguin = kf_getSafeHTML(kfguin);
        companyname = kf_getSafeHTML(companyname);

        welcomeword = welcomeword.replace(/<br>/g,'\r\n');
        welcomeword = kf_getSafeHTML(welcomeword);
        welcomeword = welcomeword.replace(/\r/g, "").replace(/\n/g, "<BR>");

        window.setTimeout("kf_sleepShow()",200);
    }
    window.setTimeout("kf_moveWithScroll()",1);
}

function kf_getSafeHTML(s)
{
    var html = "";
    var safeNode = g_safeNode;
    if(!safeNode){
        safeNode = document.createElement("TEXTAREA");
    }
    if(safeNode){
        if(Browser.moz){
            safeNode.textContent = s;
        }
        else{
            safeNode.innerText = s;
        }
        html = safeNode.innerHTML;
        if(Browser.moz){
            safeNode.textContent = "";
        }
        else{
            safeNode.innerText = "";
        }
        g_safeNode = safeNode;
    }
    return html;
}

function kf_moveWithScroll()
{
    if(typeof window.pageYOffset != 'undefined') {
        nowY = window.pageYOffset;
    }
    else if(typeof document.compatMode != 'undefined' && document.compatMode != 'BackCompat') {
        nowY = document.documentElement.scrollTop;
    }
    else if(typeof document.body != 'undefined') {
        nowY = document.body.scrollTop;
    }

    percent = .1*(nowY - lastScrollY);
    if(percent > 0)
    {
        percent=Math.ceil(percent);
    }
    else
    {
        percent=Math.floor(percent);
    }

    //document.getElementById("_Ten_rightDiv").style.top = parseInt(document.getElementById("_Ten_rightDiv").style.top) + percent+"px";
    if(document.getElementById("kfpopupDiv"))
    {
        document.getElementById("kfpopupDiv").style.top = parseInt(document.getElementById("kfpopupDiv").style.top) + percent+"px";
    }
    lastScrollY = lastScrollY + percent;
    tOut = window.setTimeout("kf_moveWithScroll()",1);
}

function kf_hide()
{
    if(tOut!=-1)
    {
        clearTimeout(tOut);
        tOut=-1;
    }
    //document.getElementById("_Ten_rightDiv").style.visibility = "hidden";
    //document.getElementById("_Ten_rightDiv").style.display = "none";
    kf_setCookie('hasshown', 1, '', '/', wpadomain);
}

function kf_hidekfpopup()
{
    if(tOut!=-1)
    {
        clearTimeout(tOut);
        tOut=-1;
    }
    document.getElementById("kfpopupDiv").style.visibility = "hidden";
    document.getElementById("kfpopupDiv").style.display = "none";
    tOut=window.setTimeout("kf_moveWithScroll()",1);
    kf_setCookie('hasshown', 1, '', '/', wpadomain);
}

function kf_getPopupDivHtml(kfguin,reference,companyname,welcomeword, wpadomain)
{
    var temp = '';
    temp += '<span class="zixun0704_x"><a href="javascript:void(0);" onclick="kf_hidekfpopup();return false;"><!--鍏抽棴--></a></span>';
    temp += '<img src="'+urlroot+'web/pic_zixun0704_nv.jpg" class="zixun0704_img" />';
    temp += '<p class="zixun0704_font">'+welcomeword+'</p>';
    temp += '<div class="zixun0704_button"><a href="javascript:void(0);" onclick="kf_openChatWindow(1,\'b\',\''+kfguin+'\')"><img src="'+urlroot+'web/pic_zixun0704QQ.jpg" /></a>&nbsp;<a href="javascript:void(0);" onclick="kf_hidekfpopup();return false;"><img src="'+urlroot+'web/pic_zixun0704_later.jpg" /></a></div>';

    return temp;
}

function kf_openChatWindow(flag, wpadomain, kfguin)
{
    window.open('http://b.qq.com/webc.htm?new=0&sid='+kfguin+'&eid='+eid+'&o=&q=7', '_blank', 'height=544, width=644,toolbar=no,scrollbars=no,menubar=no,status=no');
    if(flag==1)
    {
        kf_hidekfpopup();
    }
    return false;
}
//added by simon 2008-11-04 end

function kf_validateWelcomeword(word)
{
    var count = 0;

    for(var i=0;i<word.length;i++)
    {
        if(word.charAt(i)=='\n')
        {
            count++;
        }
        if(count>2)
        {
            return 2;
        }
    }
    if(word.length > 57+2*count)
    {
        return 1;
    }

    count = 0;
    var temp = word.indexOf('\n');
    while(temp!=-1)
    {
        word = word.substr(temp+1);
        if(temp-1<=19)
        {
            count += 19;
        }
        else if(temp-1<=38)
        {
            count += 38;
        }
        else if(temp-1<=57)
        {
            count += 57;
        }

        temp = word.indexOf('\n');
    }
    count+=word.length;
    if(count>57)
    {
        return 3;
    }

    return 0;
}

function kf_processWelcomeword(word)
{
    word = word.substr(0,57+10);
    var result = '';
    var count = 0;
    var temp = word.indexOf('<br>');

    while(count<57 && temp!=-1)
    {

        if(temp<=19)
        {
            count += 19;
            if(count<=57)
            {
                result += word.substr(0,temp+5);
            }
            else
            {
                result += word.substr(0,57-count>word.length?word.length:57-count);
            }
        }
        else if(temp<=38)
        {
            count += 38;
            if(count<=57)
            {
                result += word.substr(0,temp+5);
            }
            else
            {
                result += word.substr(0,57-count>word.length?word.length:57-count);
            }
        }
        else if(temp<=57)
        {
            count += 57;
            if(count<=57)
            {
                result += word.substr(0,temp+5);
            }
            else
            {
                result += word.substr(0,57-count>word.length?word.length:57-count);
            }
        }

        word = word.substr(temp+5);
        temp = word.indexOf('<br>');
    }

    if(count<57)
    {
        result += word.substr(0,57-count>word.length?word.length:57-count);
    }

    return result;
}

function kf_setCookie(name, value, exp, path, domain)
{
    var nv = name + "=" + escape(value) + ";";

    var d = null;
    if(typeof(exp) == "object")
    {
        d = exp;
    }
    else if(typeof(exp) == "number")
    {
        d = new Date();
        d = new Date(d.getFullYear(), d.getMonth(), d.getDate(), d.getHours(), d.getMinutes() + exp, d.getSeconds(), d.getMilliseconds());
    }

    if(d)
    {
        nv += "expires=" + d.toGMTString() + ";";
    }

    if(!path)
    {
        nv += "path=/;";
    }
    else if(typeof(path) == "string" && path != "")
    {
        nv += "path=" + path + ";";
    }

    if(!domain && typeof(VS_COOKIEDM) != "undefined")
    {
        domain = VS_COOKIEDM;
    }

    if(typeof(domain) == "string" && domain != "")
    {
        nv += "domain=" + domain + ";";
    }

    document.cookie = nv;
}

function kf_getCookie(name)
{
    var value = "";
    var cookies = document.cookie.split("; ");
    var nv;
    var i;

    for(i = 0; i < cookies.length; i++)
    {
        nv = cookies[i].split("=");

        if(nv && nv.length >= 2 && name == kf_rTrim(kf_lTrim(nv[0])))
        {
            value = unescape(nv[1]);
        }
    }

    return value;
}

function kf_sleepShow()
{
    kf_setCookie('hasshown', 0, '', '/', wpadomain);
    var position_1 = (document.documentElement.clientWidth-381)/2+document.body.scrollLeft;
    var position_2 = (document.documentElement.clientHeight-159)/2+document.body.scrollTop;
    popupDivHtml = '<div class="zixun0704" id="kfpopupDiv" onmousedown="MyMove.Move(\'kfpopupDiv\',event,1);"  style="z-index:10000; position: absolute; top: '+position_2+'px; left: '+position_1+'px;color:#000;font-size: 12px;cursor:move;height: 159px;width: 381px;">';
    popupDivHtml += kf_getPopupDivHtml(kfguin,ws,companyname,welcomeword, wpadomain);
    popupDivHtml += '</div>';
    if(document.body.insertAdjacentHTML)
    {
        document.body.insertAdjacentHTML("beforeEnd",popupDivHtml);
    }
    else
    {
        $("#footer").before(popupDivHtml);
    }
}

function kf_dealErrors()
{
    kf_hide();
    return true;
}

function kf_lTrim(str)
{
    while (str.charAt(0) == " ")
    {
        str = str.slice(1);
    }
    return str;
}

function kf_rTrim(str)
{
    var iLength = str.length;
    while (str.charAt(iLength - 1) == " ")
    {
        str = str.slice(0, iLength - 1);
        iLength--;
    }
    return str;
}

window.onerror = kf_dealErrors;
var MyMove = new Tong_MoveDiv();

function Tong_MoveDiv()
{
    this.Move=function(Id,Evt,T)
    {
        if(Id == "")
        {
            return;
        }
        var o = document.getElementById(Id);
        if(!o)
        {
            return;
        }
        evt = Evt ? Evt : window.event;
        o.style.position = "absolute";
        o.style.zIndex = 9999;
        var obj = evt.srcElement ? evt.srcElement : evt.target;
        var w = o.offsetWidth;
        var h = o.offsetHeight;
        var l = o.offsetLeft;
        var t = o.offsetTop;
        var div = document.createElement("DIV");
        document.body.appendChild(div);
        div.style.cssText = "filter:alpha(Opacity=10,style=0);opacity:0.2;width:"+w+"px;height:"+h+"px;top:"+t+"px;left:"+l+"px;position:absolute;background:#000";
        div.setAttribute("id", Id +"temp");
        this.Move_OnlyMove(Id,evt,T);
    }

    this.Move_OnlyMove = function(Id,Evt,T)
    {
        var o = document.getElementById(Id+"temp");
        if(!o)
        {
            return;
        }
        evt = Evt?Evt:window.event;
        var relLeft = evt.clientX - o.offsetLeft;
        var relTop = evt.clientY - o.offsetTop;
        if(!window.captureEvents)
        {
            o.setCapture();
        }
        else
        {
            window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);
        }

        document.onmousemove = function(e)
        {
            if(!o)
            {
                return;
            }
            e = e ? e : window.event;

            var bh = Math.max(document.body.scrollHeight,document.body.clientHeight,document.body.offsetHeight,
                document.documentElement.scrollHeight,document.documentElement.clientHeight,document.documentElement.offsetHeight);
            var bw = Math.max(document.body.scrollWidth,document.body.clientWidth,document.body.offsetWidth,
                document.documentElement.scrollWidth,document.documentElement.clientWidth,document.documentElement.offsetWidth);
            var sbw = 0;
            if(document.body.scrollWidth < bw)
                sbw = document.body.scrollWidth;
            if(document.body.clientWidth < bw && sbw < document.body.clientWidth)
                sbw = document.body.clientWidth;
            if(document.body.offsetWidth < bw && sbw < document.body.offsetWidth)
                sbw = document.body.offsetWidth;
            if(document.documentElement.scrollWidth < bw && sbw < document.documentElement.scrollWidth)
                sbw = document.documentElement.scrollWidth;
            if(document.documentElement.clientWidth < bw && sbw < document.documentElement.clientWidth)
                sbw = document.documentElement.clientWidth;
            if(document.documentElement.offsetWidth < bw && sbw < document.documentElement.offsetWidth)
                sbw = document.documentElement.offsetWidth;

            if(e.clientX - relLeft <= 0)
            {
                o.style.left = 0 +"px";
            }
            else if(e.clientX - relLeft >= bw - o.offsetWidth - 2)
            {
                o.style.left = (sbw - o.offsetWidth - 2) +"px";
            }
            else
            {
                o.style.left = e.clientX - relLeft +"px";
            }
            if(e.clientY - relTop <= 1)
            {
                o.style.top = 1 +"px";
            }
            else if(e.clientY - relTop >= bh - o.offsetHeight - 30)
            {
                o.style.top = (bh - o.offsetHeight) +"px";
            }
            else
            {
                o.style.top = e.clientY - relTop +"px";
            }
        }

        document.onmouseup = function()
        {
            if(!o) return;

            if(!window.captureEvents)
            {
                o.releaseCapture();
            }
            else
            {
                window.releaseEvents(Event.MOUSEMOVE|Event.MOUSEUP);
            }

            var o1 = document.getElementById(Id);
            if(!o1)
            {
                return;
            }
            var l0 = o.offsetLeft;
            var t0 = o.offsetTop;
            var l = o1.offsetLeft;
            var t = o1.offsetTop;

            //layer.alert(l0 + " " +  t0 +" "+ l +" "+t);

            MyMove.Move_e(Id, l0 , t0, l, t,T);
            document.body.removeChild(o);
            o = null;
        }
    }


    this.Move_e = function(Id, l0 , t0, l, t,T)
    {
        if(typeof(window["ct"+ Id]) != "undefined")
        {
            clearTimeout(window["ct"+ Id]);
        }

        var o = document.getElementById(Id);
        if(!o) return;
        var sl = st = 8;
        var s_l = Math.abs(l0 - l);
        var s_t = Math.abs(t0 - t);
        if(s_l - s_t > 0)
        {
            if(s_t)
            {
                sl = Math.round(s_l / s_t) > 8 ? 8 : Math.round(s_l / s_t) * 6;
            }

            else
            {
                sl = 0;
            }
        }
        else
        {
            if(s_l)
            {
                st = Math.round(s_t / s_l) > 8 ? 8 : Math.round(s_t / s_l) * 6;
            }
            else
            {
                st = 0;
            }
        }

        if(l0 - l < 0)
        {
            sl *= -1;
        }
        if(t0 - t < 0)
        {
            st *= -1;
        }
        if(Math.abs(l + sl - l0) < 52 && sl)
        {
            sl = sl > 0 ? 2 : -2;
        }
        if(Math.abs(t + st - t0) < 52 && st)
        {
            st = st > 0 ? 2 : -2;
        }
        if(Math.abs(l + sl - l0) < 16 && sl)
        {
            sl = sl > 0 ? 1 : -1;
        }
        if(Math.abs(t + st - t0) < 16 && st)
        {
            st = st > 0 ? 1 : -1;
        }
        if(s_l == 0 && s_t == 0)
        {
            return;
        }
        if(T)
        {
            o.style.left = l0 +"px";
            o.style.top = t0 +"px";
            return;
        }
        else
        {
            if(Math.abs(l + sl - l0) < 2)
            {
                o.style.left = l0 +"px";
            }
            else
            {
                o.style.left = l + sl +"px";
            }
            if(Math.abs(t + st - t0) < 2)
            {
                o.style.top = t0 +"px";
            }
            else
            {
                o.style.top = t + st +"px";
            }

            window["ct"+ Id] = window.setTimeout("MyMove.Move_e('"+ Id +"', "+ l0 +" , "+ t0 +", "+ (l + sl) +", "+ (t + st) +","+T+")", 1);
        }
    }
}

function wpa_count()
{
    var body = document.getElementsByTagName('body').item(0);
    var img = document.createElement('img');
    var now = new Date();
    img.src = "http://"+wpadomain+".qq.com/cgi/wpac?kfguin=" + kfguin + "&ext=0" + "&time=" + now.getTime() + "ip=172.23.30.15&";
    img.style.display = "none";
    body.appendChild(img);
}



function onload_hide() {
    $('#divFloatToolsView').animate({width:'hide', opacity:'hide'}, 'normal', function () {
        $('#divFloatToolsView').hide();
        kf_setCookie('RightFloatShown', 1, '', '/', '');
    });
    $('#aFloatTools_Show').attr('style', 'display:block');
    $('#aFloatTools_Hide').attr('style', 'display:none');
}

function onload_show() {
    $('#divFloatToolsView').animate({width:'show', opacity:'show'}, 'normal', function () {
        $('#divFloatToolsView').show();
        kf_setCookie('RightFloatShown', 0, '', '/', '');
    });
    $('#aFloatTools_Show').attr('style', 'display:none');
    $('#aFloatTools_Hide').attr('style', 'display:block');

}
//QQ显示end----------------------------------------------------------------------------------------


//ajax 地区级联--------------------------------------------------------------------------------------
jQuery(document).ready(function (){
    jQuery("#province").change(function(){
        var province = jQuery(this).val();
        var count = 0;
        jQuery.ajax({
            url:"/member/showarea.html",
            dataType:'json',
            data:"pid="+province,
            success:function(json){
                jQuery("#city option").each(function(){
                    jQuery(this).remove();
                });
                jQuery("<option value='0'>请选择</option>").appendTo("#city");
                jQuery(json).each(function(){
                    jQuery("<option value='"+json[count].id+"'>"+json[count].name+"</option>").appendTo("#city");
                    count++;
                });
            }
        });
    });
    jQuery("#city").change(function(){
        var city = jQuery(this).val();
        var count = 0;
        jQuery.ajax({
            url:"/member/showarea.html",
            dataType:'json',
            data:"pid="+city,
            success:function(json){
                jQuery("#area option").each(function(){
                    jQuery(this).remove();
                });
                jQuery("<option value='0'>请选择</option>").appendTo("#area");
                jQuery(json).each(function(){
                    jQuery("<option value='"+json[count].id+"'>"+json[count].name+"</option>").appendTo("#area");
                    count++;
                });
                if(count>0)
                {
                    jQuery("#area").show();
                }else
                {
                    jQuery("#area").hide();
                }
            }
        });
    });
});
//ajax 地区级联--------------------------------------------------------------------------------------------------




//标的详情显示----------------------------------------------------------------------------------------------------------------------------
/* ===========================================================
 * bootstrap-tooltip.js v2.0.2
 * http://twitter.github.com/bootstrap/javascript.html#tooltips
 * ========================================================== */
!function( $ ) {
    "use strict"
    /* TOOLTIP PUBLIC CLASS DEFINITION
     * =============================== */
    var Tooltip = function ( element, options ) {
        this.init('tooltip', element, options)
    }
    Tooltip.prototype = {

        constructor: Tooltip

        , init: function ( type, element, options ) {
            var eventIn
                , eventOut

            this.type = type
            this.$element = $(element)
            this.options = this.getOptions(options)
            this.enabled = true

            if (this.options.trigger != 'manual') {
                eventIn  = this.options.trigger == 'hover' ? 'mouseenter' : 'focus'
                eventOut = this.options.trigger == 'hover' ? 'mouseleave' : 'blur'
                this.$element.on(eventIn, this.options.selector, $.proxy(this.enter, this))
                this.$element.on(eventOut, this.options.selector, $.proxy(this.leave, this))
            }

            this.options.selector ?
                (this._options = $.extend({}, this.options, { trigger: 'manual', selector: '' })) :
                this.fixTitle()
        }

        , getOptions: function ( options ) {
            options = $.extend({}, $.fn[this.type].defaults, options, this.$element.data())

            if (options.delay && typeof options.delay == 'number') {
                options.delay = {
                    show: options.delay
                    , hide: options.delay
                }
            }

            return options
        }

        , enter: function ( e ) {
            var self = $(e.currentTarget)[this.type](this._options).data(this.type)

            if (!self.options.delay || !self.options.delay.show) {
                self.show()
            } else {
                self.hoverState = 'in'
                setTimeout(function() {
                    if (self.hoverState == 'in') {
                        self.show()
                    }
                }, self.options.delay.show)
            }
        }

        , leave: function ( e ) {
            var self = $(e.currentTarget)[this.type](this._options).data(this.type)

            if (!self.options.delay || !self.options.delay.hide) {
                self.hide()
            } else {
                self.hoverState = 'out'
                setTimeout(function() {
                    if (self.hoverState == 'out') {
                        self.hide()
                    }
                }, self.options.delay.hide)
            }
        }

        , show: function () {
            var $tip
                , inside
                , pos
                , actualWidth
                , actualHeight
                , placement
                , tp

            if (this.hasContent() && this.enabled) {
                $tip = this.tip()
                this.setContent()

                if (this.options.animation) {
                    $tip.addClass('fade')
                }

                placement = typeof this.options.placement == 'function' ?
                    this.options.placement.call(this, $tip[0], this.$element[0]) :
                    this.options.placement

                inside = /in/.test(placement)

                $tip
                    .remove()
                    .css({ top: 0, left: 0, display: 'block' })
                    .appendTo(inside ? this.$element : document.body)

                pos = this.getPosition(inside)

                actualWidth = $tip[0].offsetWidth
                actualHeight = $tip[0].offsetHeight

                switch (inside ? placement.split(' ')[1] : placement) {
                    case 'bottom':
                        tp = {top: pos.top + pos.height, left: pos.left + pos.width / 2 - actualWidth / 2}
                        break
                    case 'top':
                        tp = {top: pos.top - actualHeight, left: pos.left + pos.width / 2 - actualWidth / 2}
                        break
                    case 'left':
                        tp = {top: pos.top + pos.height / 2 - actualHeight / 2, left: pos.left - actualWidth}
                        break
                    case 'right':
                        tp = {top: pos.top + pos.height / 2 - actualHeight / 2, left: pos.left + pos.width}
                        break
                }

                $tip
                    .css(tp)
                    .addClass(placement)
                    .addClass('in')
            }
        }

        , setContent: function () {
            var $tip = this.tip()
            $tip.find('.tooltip-inner').html(this.getTitle())
            $tip.removeClass('fade in top bottom left right')
        }

        , hide: function () {
            var that = this
                , $tip = this.tip()

            $tip.removeClass('in')

            function removeWithAnimation() {
                var timeout = setTimeout(function () {
                    $tip.off($.support.transition.end).remove()
                }, 500)

                $tip.one($.support.transition.end, function () {
                    clearTimeout(timeout)
                    $tip.remove()
                })
            }

            $.support.transition && this.$tip.hasClass('fade') ?
                removeWithAnimation() :
                $tip.remove()
        }

        , fixTitle: function () {
            var $e = this.$element
            if ($e.attr('title') || typeof($e.attr('data-original-title')) != 'string') {
                $e.attr('data-original-title', $e.attr('title') || '').removeAttr('title')
            }
        }

        , hasContent: function () {
            return this.getTitle()
        }

        , getPosition: function (inside) {
            return $.extend({}, (inside ? {top: 0, left: 0} : this.$element.offset()), {
                width: this.$element[0].offsetWidth
                , height: this.$element[0].offsetHeight
            })
        }

        , getTitle: function () {
            var title
                , $e = this.$element
                , o = this.options

            title = $e.attr('data-original-title')
                || (typeof o.title == 'function' ? o.title.call($e[0]) :  o.title)

            title = (title || '').toString().replace(/(^\s*|\s*$)/, "")

            return title
        }

        , tip: function () {
            return this.$tip = this.$tip || $(this.options.template)
        }

        , validate: function () {
            if (!this.$element[0].parentNode) {
                this.hide()
                this.$element = null
                this.options = null
            }
        }

        , enable: function () {
            this.enabled = true
        }

        , disable: function () {
            this.enabled = false
        }

        , toggleEnabled: function () {
            this.enabled = !this.enabled
        }

        , toggle: function () {
            this[this.tip().hasClass('in') ? 'hide' : 'show']()
        }

    }


    /* TOOLTIP PLUGIN DEFINITION
     * ========================= */

    $.fn.tooltip = function ( option ) {
        return this.each(function () {
            var $this = $(this)
                , data = $this.data('tooltip')
                , options = typeof option == 'object' && option
            if (!data) $this.data('tooltip', (data = new Tooltip(this, options)))
            if (typeof option == 'string') data[option]()
        })
    }

    $.fn.tooltip.Constructor = Tooltip

    $.fn.tooltip.defaults = {
        animation: true
        , delay: 0
        , selector: false
        , placement: 'top'
        , trigger: 'hover'
        , title: ''
        , template: '<div class="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>'
    }

}( window.jQuery );


//投标详情显示----------------------------------------------------------------------------------------------------------
!function( $ ) {

    "use strict"

    var Popover = function ( element, options ) {
        this.init('popover', element, options)
    }

    /* NOTE: POPOVER EXTENDS BOOTSTRAP-TOOLTIP.js
     ========================================== */

    Popover.prototype = $.extend({}, $.fn.tooltip.Constructor.prototype, {

        constructor: Popover

        , setContent: function () {
            var $tip = this.tip()
                , title = this.getTitle()
                , content = this.getContent()

            $tip.find('.popover-title')[ $.type(title) == 'object' ? 'append' : 'html' ](title)
            $tip.find('.popover-content > *')[ $.type(content) == 'object' ? 'append' : 'html' ](content)

            $tip.removeClass('fade top bottom left right in')
        }

        , hasContent: function () {
            return this.getTitle() || this.getContent()
        }

        , getContent: function () {
            var content
                , $e = this.$element
                , o = this.options

            content = $e.attr('data-content')
                || (typeof o.content == 'function' ? o.content.call($e[0]) :  o.content)

            content = content.toString().replace(/(^\s*|\s*$)/, "")

            return content
        }

        , tip: function() {
            if (!this.$tip) {
                this.$tip = $(this.options.template)
            }
            return this.$tip
        }

    })


    /* POPOVER PLUGIN DEFINITION
     * ======================= */

    $.fn.popover = function ( option ) {
        return this.each(function () {
            var $this = $(this)
                , data = $this.data('popover')
                , options = typeof option == 'object' && option
            if (!data) $this.data('popover', (data = new Popover(this, options)))
            if (typeof option == 'string') data[option]()
        })
    }

    $.fn.popover.Constructor = Popover

    $.fn.popover.defaults = $.extend({} , $.fn.tooltip.defaults, {
        placement: 'right'
        , content: ''
        , template: '<div class="popover"><div class="arrow"></div><div class="popover-inner"><div class="popover-content"><p></p></div></div></div>'
    })
    $("[id^='info']").popover();
}( window.jQuery );

jQuery(function(){
    jQuery("[rel='tooltip']").tooltip();
});//显示首页的标的详情介绍
//发标页面的备注信息显示----------------------------------------------------------------------------------------------------------


//倒计时显示功能---------------------------------------------------------------------------------------------------------
// function showRemainTime(){
//     var endtimes=$(".endtime");
//     endtimes.each(function(){
//         RemainTime($(this));
//     });
// }
// showRemainTime();
// setInterval("showRemainTime()",1000);
// function RemainTime(t){
//     var iDay,iHour,iMinute,iSecond,account;
//     var sDay="",sTime="";
//     var at="data-time"
//     var iTime=t.attr(at);
//     if (iTime >= 0){
//         iDay = parseInt(iTime/24/3600);
//         iHour = parseInt((iTime/3600)%24);
//         iMinute = parseInt((iTime/60)%60);
//         iSecond = parseInt(iTime%60);
//         if (iDay > 0){
//             sDay = iDay + "天";
//         }
//         sTime =sDay + iHour + "小时" + iMinute + "分钟" + iSecond + "秒";
//         if(iTime==0){
//             sTime="<span style='color:green'>时间到了！</span>";
//         }
//         t.attr(at,iTime-1);
//     }else{
//         sTime="<span style='color:red'>此标已过期！</span>";
//     }
//     t.html(sTime);
// }


//form表单样式处理-----------------------------------------------------------------------------------------------
(function($){
	$.fn.extend({
		minSelect:function(options){
			var defaults = {
				title:"tit",//模拟select的标题
				selectbox:"selectbox",//生成的单个div结构
				ulSelect:"select_ul",//模拟的select
				liOption:"select_li",//模拟的option
				titleClass:"title_hover",//点击select后变化的样式变化
				titleTxt:"请选择"//初始化的文字显示
			};
			var options = $.extend(defaults,options);
			 this.each(function(){
				var o = options;
				var obj = $(this);
				var optionVal;
				var optionTxt;
				var str = "";
				var liOption;
				var selected = obj.find("option:selected").text();
				var titleCon = o.titleTxt;
				if(selected)
				{
					titleCon = selected
				}	
				obj.hide().wrap("<div class="+o.selectbox+"></div>").before("<div class="+o.title+"><span>"+titleCon+"</span><b class=ico></b></div>").before("<ul class="+o.ulSelect+"></ul>");//创建伪selectDOM结构
				$("option",obj).each(function(i){
					optionVal = $(this).val();
					optionTxt = $(this).text();
					str+="<li data-value='"+optionVal+"'>"+optionTxt+"</li>"
				})
				var ulSelect = obj.siblings("."+o.ulSelect);
				var ulTitle =  obj.siblings("."+o.title);
				ulSelect.html(str);
				ulTitle.click(function(){
					
					if(ulSelect.is(":visible"))
					{
						ulTitle.removeClass(o.titleClass);
						ulSelect.hide();
					}
					else
					{
						$("."+o.ulSelect).hide();		//所有的下拉都隐藏，目的是为了达到当前的select显示
						$("."+o.title).removeClass(o.titleClass);	//所有的标题的样式变为初始状态，只有当前的标题是下拉的状态显示
						$(document).click(function(){  //加载Element对象的click事件
							ulTitle.removeClass(o.titleClass);
							ulSelect.hide();		//点击浏览器的任何地方都可以关闭下来菜单
						})
						ulTitle.addClass(o.titleClass);	//当前的标题加上点击过后的样式
						ulSelect.show();	
						return false; 	//当下拉显示后立即停止冒泡，阻止下拉的隐藏
					}
				})
				liOption = ulSelect.find("li");
				liOption.each(function(){	
					var That = $(this);
					var len = liOption.length;
					if(len>8)
					{
						ulSelect.css({"overflow-y":"auto","height":8*$(liOption[0]).height()+"px"})		
					}
					That.click(function(){	
						var optionVal = $(this).attr("data-value");  //获取li的data-value属性,赋值给select
						var optionTxt = $(this).text();		//获取li的文本内容，改变标题的文字，达到模拟select的文字改变
						ulTitle.removeClass(o.titleClass).find("span").text(optionTxt);
						$(this).parent(ulSelect).hide();
						obj.val(optionVal)	 
					})
				})
			})
		}
	})
})(jQuery);
	
(function($){
        $.fn.extend({
            minRadio:function(options){
                var defaults = {
					checkedClass:"radio_box_hover", //点击后的变化样式
					Element:"radio_box",	//radio的动态添加父级
					showTxt:true //是否生成标题文字结构
                };
					
                var options = $.extend(defaults,options);
                 this.each(function(){
                    var o = options;
                    var obj = $(this);
					obj.wrap("<div class="+o.Element+"></div>").hide();
					if(o.showTxt)
					{
						obj.before("<label></label>").before("<b></b>").siblings("label").text(obj.attr("data-value"))	
					}
					//创建DOM结构并且隐藏掉radio元素，再给同级的label的文字内容修改成当前的radio的data-value值
					var obj_name = obj.attr("name");
					var radioChild = $("input[name="+obj_name+"]")
					var radioParent = obj.parent("."+o.Element)	 //获得当前radio的父级元素
					//radioParent.addClass("clearfix") //清除浮动
					if($(this).is(":checked"))
					{    
						radioParent.addClass(o.checkedClass);
					}
					radioParent.click(function()
					{
						var radioParents = $("input[name="+obj_name+"]").parent() //所有radio的父级元素
						radioParents.removeClass(o.checkedClass)		
						$(this).addClass(o.checkedClass);
						radioChild.prop("checked", false);	//所有radio的状态checked改成false
						$(obj).prop("checked", true);	//当前的radio状态checked改成true
					})
                })
            }
        })
    })(jQuery);
	

(function($){
	$.fn.extend({
		minCheck:function(options){
			var defaults = {
				checkedClass:"check_box_hover", //点击之后的样式变化
				Element:"check_box",	//checkbox的父级对象
				ElementClass:".check_box"	//checkbox的父级对象的class名称	
			};
			var options = $.extend(defaults,options);
			 this.each(function(){
				var o = options;
				var obj = $(this);
				obj.wrap("<div class="+o.Element+"></div>").before("<label></label>");
				obj.hide().siblings("label").text(obj.attr("data-value"));
				var checkParent = obj.parent(o.ElementClass)
				if($(this).is(":checked"))
				{    
					checkParent.addClass(o.checkedClass);
				}
				checkParent.click(function(){
					if($(this).hasClass(o.checkedClass))
					{
						$(this).removeClass(o.checkedClass);
						$(obj).prop("checked",false)  
					}
					else{
						$(this).addClass(o.checkedClass);
						$(obj).prop("checked",true)	
					}	
				})

			})
		}
	})
})(jQuery);


	$(".jbxx").find("tr:odd").css("background-color", "#f1f1f1");
	function MaxHeight () {
	  var maxheight = $(".maxheight ul");
	  var height_1 = $(".maxheight ul:nth-child(1)").height();            
	  var height_2 = $(".maxheight ul:nth-child(2)").height();           
	  var height_3 = $(".maxheight ul:nth-child(3)").height();
	 
		if ( (height_1 >= height_2 ) && (height_1 >= height_3) ) {
			maxheight.height(height_1);
		}
		else if(height_2 >= height_1 && height_2 >= height_3){
			maxheight.height(height_2);
		}
		else if(height_3 >= height_1 && height_3 >= height_2){
			 maxheight.height(height_3);
		}
	
	}
	MaxHeight(); // 待收待还下的三个矩形框限制为等宽登高，js自动控制高度，当手动添加<LI>时，js会自动判断哪个框最高，给全部的框都加上那个高，不用时注释"MaxHeight();"






//------------------------投资内页---------------------------------------------------
   

function inputAll(lowest,most,use_money,wait){
	if(most==0&&use_money>wait){
		jQuery("#money").val(wait);
	}else if(most==0&&use_money<=wait){
		jQuery("#money").val(use_money);
	}else if(wait>most&&use_money>=most){
		jQuery("#money").val(most);
	}else if(wait>most&&use_money<most){
		jQuery("#money").val(use_money);
	}else if(wait<=most&&use_money>=wait){
		jQuery("#money").val(wait);
	}else{
		jQuery("#money").val(use_money);
	}
}

//咨询左侧导航自适应--------------------------------------------------------------------
function articleMenu(){
	var menuLeft = $(".service_left");
	var menuRight = $(".service_right");
	var leftHeight = menuLeft.height();
	var rightHeight = menuRight.height();
	if(leftHeight<rightHeight){
		menuLeft.css({"height":rightHeight+"px"})
	}else{
		menuLeft.css({"min-height":"380px"})
	}
}
articleMenu();


//footer置底--------------------------------------------------------------------------
$.fn.extend({
	fullScreen:function(options){
		var defaults = {
			footObj:"#footer",
			diffentVal:""
		};
		var options = $.extend(defaults,options);
		this.each(function(){
			var o = options;
			var footer = $(o.footObj);
			var fTopHeight,footVal,allHeight,diffVal,footDiff,brotherObj,stpVal;
			$(window).bind("resize load",function(){
				//footer.css({"position":"static","bottom":""});
				fTopHeight =  footer.height()
			 	footVal = footer.offset().top;
				allHeight = $(window).height();
				footDiff = allHeight-fTopHeight;
				brotherObj = $(footer.prevAll("div")[0]);
				stpVal = brotherObj.offset().top+brotherObj.height()
				diffVal = footDiff-stpVal-80;
				if((footVal+fTopHeight)-allHeight<0){
					brotherObj.animate({"paddingBottom":diffVal+"px"},600);
				}
				else{
					brotherObj.css({"paddingBottom":""});
				}
			})
		})
	}
});
$("body").fullScreen()

//首页文章列表数据保持不变--------------------------------------------------------------

//验证码验证长度testValidCode()
$.fn.extend({
	testValidCode:function(options){
		var defaults = {
			maxlen:4
		};
		var options = $.extend(defaults,options);
		this.each(function(){
			var that = $(this)
			var o = options;
			that.blur(function(){
				var that = $(this)
				var valiCode = $(this).val();
				var leng = valiCode.length;
				if(leng<o.maxlen&&leng>0)
				{
					that.addClass("error").removeClass("vali_right valid")
					return false
				}
				else{
					that.removeClass("valid error")
				}
			})
		})
	}
})
$("input[name='valicode']").testValidCode();
$("input[name='validCode']").testValidCode();
$("input[name='valid_code']").testValidCode();

//文章数据加载------------------------------------------------------------------------
$.fn.extend({
	jsonArticle:function(options){
		var defaults = {
			handBox:"",	
			jsonUrl: "",
			callBack:null
		};
		var options = $.extend(defaults,options);
		this.each(function(){
			var that = $(this)
			var o = options;
			var json_url = o.jsonUrl;//数据地址
			$.ajax({
				url:json_url,
				type:"POST",
				success:function(data){ 
					var jsonHtml = o.handBox;//$("#article_json").html();
					var index_template = Handlebars.compile(jsonHtml);//编译模块
					
					var index_jsonContent = index_template(data);
					that.html(index_jsonContent);//文章数据显示
					
					if(typeof o.callBack=='function')
					{
						o.callBack.call(this)
					}
					
					var listContent = $(".notice_content",that);
					var itemList = $("li",that);
					listContent.each(function(){
						var txt = $(this).text();
						if(txt.length>25)
						{
							txt = ($.trim(txt)).substr(0,24)+"...";
						}
						$(this).text(txt)
					})
					$(itemList[0]).find("a").append('<img src="'+theme+'/images/news_icon.png" />')
					$(itemList[0]).find(".notice_content").show();
					itemList.each(function(){
						var _that = $(this);
						_that.mouseenter(function(){
							listContent.hide();
							$(this).find(".notice_content").show();
						});
					});
					
				}
			});
		});
	}
});

//邮箱自动填充----------------------------------------------------------------------------------------------
(function($){
	$.fn.mailAutoComplete = function(options){
		var defaults = {
			boxClass: "mailListBox", //外部box样式
			listClass: "mailListDefault", //默认的列表样式
			focusClass: "mailListFocus", //列表选样式中
			markCalss: "mailListHlignt", //高亮样式
			zIndex: 1,
			autoClass: true, //是否使用插件自带class样式
			mailArr: ["qq.com","gmail.com","126.com","163.com","hotmail.com","yahoo.com","yahoo.com.cn","live.com","sohu.com","sina.com"], //邮件数组
			textHint: false, //文字提示的自动显示与隐藏
			hintText: "",
			focusColor: "#333",
			blurColor: "#999"
		};
		var settings = $.extend({}, defaults, options || {});
		//页面装载CSS样式
		if(settings.autoClass && $("#mailListAppendCss").size() === 0){
			$('<style id="mailListAppendCss" type="text/css">.mailListBox{border:1px solid #369; background:#fff; font:12px/20px Arial;}.mailListDefault{padding:0 5px;cursor:pointer;white-space:nowrap;}.mailListFocus{padding:0 5px;cursor:pointer;white-space:nowrap;background:#369;color:white;}.mailListHlignt{color:red;}.mailListFocus .mailListHlignt{color:#fff;}</style>').appendTo($("head"));	
		}
		var cb = settings.boxClass, cl = settings.listClass, cf = settings.focusClass, cm = settings.markCalss; //插件的class变量
		var z = settings.zIndex, newArr = mailArr = settings.mailArr, hint = settings.textHint, text = settings.hintText, fc = settings.focusColor, bc = settings.blurColor;
		//创建邮件内部列表内容
		$.createHtml = function(str, arr, cur){
			var mailHtml = "";
			if($.isArray(arr)){
				$.each(arr, function(i, n){
					if(i === cur){
						mailHtml += '<div class="mailHover '+cf+'" id="mailList_'+i+'"><span class="'+cm+'">'+str+'</span>@'+arr[i]+'</div>';	
					}else{
						mailHtml += '<div class="mailHover '+cl+'" id="mailList_'+i+'"><span class="'+cm+'">'+str+'</span>@'+arr[i]+'</div>';	
					}
				});
			}
			return mailHtml;
		};
		//一些全局变量
		
		var index = -1, s;
		$(this).each(function(){
			var that = $(this), i = $(".justForJs").size();	
			if(i > 0){ //只绑定一个文本框
				return;	
			}
			var w = that.outerWidth(), h = that.outerHeight(); //获取当前对象（即文本框）的宽高
			//样式的初始化
			that.wrap('<span style="display:inline-block;position:relative;"></span>')
				.before('<div id="mailListBox_'+i+'" class="justForJs '+cb+'" style="min-width:'+w+'px;_width:'+w+'px;position:absolute;left:-6000px;top:'+h+'px;z-index:'+z+';"></div>');
			var x = $("#mailListBox_" + i), liveValue; //列表框对象
			that.focus(function(){
				//父标签的层级
				$(this).css("color", fc).parent().css("z-index", z);	
				//提示文字的显示与隐藏
				if(hint && text){
					var focus_v = $.trim($(this).val());
					if(focus_v === text){
						$(this).val("");
					}
				}
				//键盘事件
				$(this).keyup(function(e){
					s = v = $.trim($(this).val());	
					if(/@/.test(v)){
						s = v.replace(/@.*/, "");
					}
					if(v.length > 0){
						//如果按键是上下键
						if(e.keyCode === 38){
							//向上
							if(index <= 0){
								index = newArr.length;	
							}
							index--;
						}else if(e.keyCode === 40){
							//向下
							if(index >= newArr.length - 1){
								index = -1;
							}
							index++;
						}else if(e.keyCode === 13){
							//回车
							if(index > -1 && index < newArr.length){
								//如果当前有激活列表
								$(this).val($("#mailList_"+index).text());	
							}
						}else{
							if(/@/.test(v)){
								index = -1;
								//获得@后面的值
								//s = v.replace(/@.*/, "");
								//创建新匹配数组
								var site = v.replace(/.*@/, "");
								newArr = $.map(mailArr, function(n){
									var reg = new RegExp(site);	
									if(reg.test(n)){
										return n;	
									}
								});
							}else{
								newArr = mailArr;
							}
						}
						x.html($.createHtml(s, newArr, index)).css("left", 0);
						if(e.keyCode === 13){
							//回车
							if(index > -1 && index < newArr.length){
								//如果当前有激活列表
								x.css("left", "-6000px");	
							}
						}
					}else{
						x.css("left", "-6000px");	
					}
				}).blur(function(){
					if(hint && text){
						var blur_v = $.trim($(this).val());
						if(blur_v === ""){
							$(this).val(text);
						}
					}
					$(this).css("color", bc).unbind("keyup").parent().css("z-index",0);
					x.css("left", "-6000px");	
					
				});	
				//鼠标经过列表项事件
				//鼠标经过
				$(".mailHover").live("mouseover", function(){
					index = Number($(this).attr("id").split("_")[1]);	
					liveValue = $("#mailList_"+index).text();
					x.children("." + cf).removeClass(cf).addClass(cl);
					$(this).addClass(cf).removeClass(cl);
				});
			});

			x.bind("mousedown", function(){
				that.val(liveValue);		
			});
		});
	};
	
})(jQuery);
(function($){
	$.fn.extend({
		showIE:function(options){
			var defaults = {
				txt:"请升级"
			};
			var options = $.extend(defaults,options);
			this.each(function(){
				var o = options;
				if ($.browser.msie && ($.browser.version<"8.0") && !$.support.style) { 
					$(this).append("<div class='IE6show'>"+o.txt+"</div>")
				}
			});
		}
	})
})(jQuery);

//底部友情链接
$.fn.extend({
	snallImgScroll:function(options){
		var defaults ={
			wraper:"",//外层div
			prev:"",
			next:"",
			img:"",//ul对象
			speed:"",
			or:true
		}
		var options = $.extend(defaults,options)
		this.each(function(){
			var o = options
			var ad;
			var wraper = $(o.wraper);
			var prev = $(o.prev);
			var next = $(o.next);
			var img = $(o.img);
			var w = img.find('li').outerWidth(true);
			var s = o.speed;
			img.css("width",w*(img.find("li").length)+"px");
			next.click(function () {
				if(!img.is(":animated")){
					img.animate({ 'margin-left': -w}, function () {
						img.find('li').eq(0).appendTo(img);
						img.css({ 'margin-left': 0 });
					});
				}
			});
			prev.click(function () {
				if(!img.is(":animated")){
					img.find('li:last').prependTo(img);
					img.css({ 'margin-left': -w});
					img.animate({ 'margin-left': 0 });
				}
			});
			if (o.or == true) {
				ad = setInterval(function () { next.click(); }, s * 1000);
				wraper.hover(function(){ 
					clearInterval(ad);
					
				}, function() { 
					ad = setInterval(function () { next.click(); }, s * 1000); 
				});
		
			}	
		})
	}	
})

//select美化
	$.fn.extend({
		minSelect : function(options) {
			var defaults = {
				title : "tit",// 模拟select的标题
				selectbox : "selectbox",// 生成的单个div结构
				ulSelect : "select_ul",// 模拟的select
				liOption : "select_li",// 模拟的option
				titleClass : "title_hover",// 点击select后变化的样式变化
				titleTxt : "请选择"// 初始化的文字显示
			};
			var options = $.extend(defaults, options);
			this.each(function() {
				var o = options;
				var obj = $(this);
				var optionVal;
				var optionTxt;
				var str = "";
				var liOption;
				obj.wrap("<div class=" + o.selectbox + "></div>").before(
						"<div class=" + o.title + "><span>" + o.titleTxt
								+ "</span><b class=ico></b></div>").before(
						"<ul class=" + o.ulSelect + "></ul>");// 创建伪selectDOM结构
				$("option", obj).each(
						function(i) {
							optionVal = $(this).val();
							optionTxt = $(this).text();
							str += "<li data-value='" + optionVal + "'>"
									+ optionTxt + "</li>"
						})
				var ulSelect = obj.siblings("." + o.ulSelect);
				var ulTitle = obj.siblings("." + o.title);
				ulSelect.html(str);
				ulTitle.click(function() {
					if (ulSelect.is(":visible")) {
						ulTitle.removeClass(o.titleClass);
						ulSelect.hide();
					} else {
						$("." + o.ulSelect).hide(); // 所有的下拉都隐藏，目的是为了达到当前的select显示
						$("." + o.title).removeClass(o.titleClass); // 所有的标题的样式变为初始状态，只有当前的标题是下拉的状态显示
						$(document).click(function() { // 加载Element对象的click事件
							ulTitle.removeClass(o.titleClass);
							ulSelect.hide(); // 点击浏览器的任何地方都可以关闭下来菜单
						})
						ulTitle.addClass(o.titleClass); // 当前的标题加上点击过后的样式
						ulSelect.show();
						return false; // 当下拉显示后立即停止冒泡，阻止下拉的隐藏
					}
				})
				liOption = ulSelect.find("li");
				liOption.each(function() {
					var That = $(this);
					That.click(function() {
						var optionVal = $(this).attr("data-value"); // 获取li的data-value属性,赋值给select
						var optionTxt = $(this).text(); // 获取li的文本内容，改变标题的文字，达到模拟select的文字改变
						ulTitle.removeClass(o.titleClass).find("span").text(
								optionTxt);
						$(this).parent(ulSelect).hide();
						obj.val(optionVal)
					})
				})
			})
		}
	});
/*=18 字数统计*/
$.fn.extend({
    count:function(value){
        var o={
            oRelated:"",                //显示当前字数
            iMax:0,                     //最大字数
            bBreak:true,                //超出是否截断
            fnAdditional:""             //追加函数
        }
        o=$.extend(o,value);
        var oThis=$(this)[0],
            oRelated=$(o.oRelated)[0],
            iMax=o.iMax;
        function fnCount(){
            iCount=oThis.value.length;
            oRelated && (oRelated.innerHTML=iCount);
            if(o.bBreak&&iCount>=iMax){
                oThis.value =oThis.value.substring(0,iMax);
                oRelated && (oRelated.innerHTML=iMax);
            }
            o.fnAdditional && o.fnAdditional();
        }
        oThis.onkeyup=oThis.onchange=fnCount;
    }
});

$("body").snallImgScroll({wraper:'#partner-content', prev:'.og_prev', next:'.og_next', img:'.piclist',speed:3,or:true}); // true为自动播放，不加此参数或false就默认不自动
//解决IE不支持HTML5表单属性placeholder的问题
(function ($) {
    $.fn.placeholder = function (options) {
        var defaults = {
            pColor: "#ccc",
            pActive: "#999",
            pFont: "14px",
            activeBorder: "#080",
            posL: 10,
            zIndex: "99"
        },
        opts = $.extend(defaults, options);
        //
        return this.each(function () {
            if ("placeholder" in document.createElement("input")) return;
            $(this).parent().css("position", "relative");
            var isIE = $.browser.msie,
            version = $.browser.version;
  
            //不支持placeholder的浏览器
            var $this = $(this),
                msg = $this.attr("placeholder"),
                iH = $this.outerHeight(),
                iW = $this.outerWidth(),
                iX = $this.position().left,
                iY = $this.position().top,
                oInput = $("<label>", {
                "class": "placeholderCss",
                "text": msg,
                "css": {
                    "position": "absolute",
                    "left": iX + "px",
                    "top": iY + "px",
                    "width": iW - opts.posL + "px",
                    "padding-left": opts.posL + "px",
                    "height": iH + "px",
                    "line-height": iH + "px",
                    "color": opts.pColor,
                    "font-size": opts.pFont,
                    "z-index": opts.zIndex,
                    "cursor": "text"
                }
                }).insertBefore($this);
            //初始状态就有内容
            var value = $this.val();
            if (value.length > 0) {
            oInput.hide();
            };
  
            //
            $this.on("focus", function () {
                var value = $(this).val();
                if (value.length > 0) {
                    oInput.hide();
                }
                oInput.css("color", opts.pActive);
                //
  
                if(isIE && version < 9){
                    var myEvent = "propertychange";
                }else{
                    var myEvent = "input";
                }
  
                $(this).on(myEvent, function () {
                    var value = $(this).val();
                    if (value.length == 0) {
                        oInput.show();
                    } else {
                        oInput.hide();
                    }
                });
  
            }).on("blur", function () {
                var value = $(this).val();
                if (value.length == 0) {
                    oInput.css("color", opts.pColor).show();
                }
            });
            //
            oInput.on("click", function () {
                $this.trigger("focus");
                $(this).css("color", opts.pActive)
            });
            //
            $this.filter(":focus").trigger("focus");
        });
    }
})(jQuery)
});
