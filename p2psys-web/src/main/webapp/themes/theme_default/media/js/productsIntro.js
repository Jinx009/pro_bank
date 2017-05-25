define(function(require,exports,module){
	require('jquery');
	
    $(function(){
        var initTop = $('.cjwt-left').offset().top;
        $(window).scroll(function() {
            //网页滚动高度
            var scrollPos =  document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
            //如果滚动的高度超过了左侧菜单的top值，将菜单固定
            if(scrollPos >= initTop) {
                $('.cjwt-left').css({
                    position:'fixed',
                    top:"30px",
                    'z-index':100
                });
            }else{
                $('.cjwt-left').css({
                    position:'relative'
                });
            }
        });
    });
    
  //栏目列表
    $.ajax({
    	url:"/article/siteList.html?nid=question",
    	type:"get",
    	dataType:"json",
    	success:function(data){
    		require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
    			require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
    				var tpl = require('/themes/theme_default/media/tpl/question_site.tpl');//载入tpl模板
    				var template = Handlebars.compile(tpl);
    				var html = template(data);
    				$("#siteList").html(html);
                    $(function(){
                        var oli = $(".cjwt-left ul li")
                        oli.each(function(){
                           $(this).click(function(){
                             oli.removeClass("active")
                             $(this).addClass("active")
                           })
                        });
                    });
    			})
    		})
    	}
    })


    
     $.ajax({
    	url:"/article/siteList.html?nid=question",
    	type:"get",
    	dataType:"json",
    	success:function(data){
    		require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
    			require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
    				var tpl = require('/themes/theme_default/media/tpl/question_detail.tpl');//载入tpl模板
    				var template = Handlebars.compile(tpl);
    				var html = template(data);
    				$(".cjwt-content").html(html);

                    $(".cjwt-content li").each(function(){
                       var aa = $(this).find("input").val()
                       var bb = $(this).find("dl");
                       function ajaxUrl(aa){
                            $.ajax({
                                    url:"/index/articleList.html?nid="+aa+"&random="+Math.random(),
                                    type:"get",
                                    dataType:"json",
                                    success:function(data){
                                        var tpl1 = require('/themes/theme_default/media/tpl/question_list.tpl');//载入tpl模板
                                        var template1 = Handlebars.compile(tpl1);
                                        var html1 = template1(data);
                                        bb.html(html1);
                                        bb.find("span").parent().find("p").addClass("hide")
                                        bb.find("span").click(function(){
                                          if(($(this).is(".active"))){
                                            $(this).removeClass("active")
                                            $(this).parent().find("p").addClass("hide")
                                          }
                                          else{
                                            $(this).parent().find("p").removeClass("hide")
                                            $(this).addClass("active")
                                          }
                                        })
                                    }
                                })
                       }
                       ajaxUrl(aa);
    			})
    		})
         })
    	}
    })


require.async(["commonJS/keep","commonJS/rollTo"],function(){

                $(".cjwt-left ul li .cjwt-con71").rollTo({
                  oFinish: "#cjwt-con71", //要滚动到的元素
                  sSpeed: "300",  //滚动速度
                  bMonitor: true, //是否楼层监听
                  sClass: "active", //楼层监听时需要添加的样式
                  iBias:-110,
                  fnAdditional: "" //追加方法
                }); 

                 $(".cjwt-left ul li .cjwt-con72").rollTo({
                  oFinish: "#cjwt-con72", //要滚动到的元素
                  sSpeed: "300",  //滚动速度
                  bMonitor: true, //是否楼层监听
                  sClass: "active", //楼层监听时需要添加的样式
                  iBias:-110,
                  fnAdditional: "" //追加方法
                }); 

                 $(".cjwt-left ul li .cjwt-con73").rollTo({
                  oFinish: "#cjwt-con73", //要滚动到的元素
                  sSpeed: "300",  //滚动速度
                  bMonitor: true, //是否楼层监听
                  sClass: "active", //楼层监听时需要添加的样式
                  iBias:-110,
                  fnAdditional: "" //追加方法
                }); 

                 $(".cjwt-left ul li .cjwt-con74").rollTo({
                  oFinish: "#cjwt-con74", //要滚动到的元素
                  sSpeed: "300",  //滚动速度
                  bMonitor: true, //是否楼层监听
                  sClass: "active", //楼层监听时需要添加的样式
                  iBias:-110,
                  fnAdditional: "" //追加方法
                }); 

                 $(".cjwt-left ul li .cjwt-con75").rollTo({
                  oFinish: "#cjwt-con75", //要滚动到的元素
                  sSpeed: "300",  //滚动速度
                  bMonitor: true, //是否楼层监听
                  sClass: "active", //楼层监听时需要添加的样式
                  iBias:-110,
                  fnAdditional: "" //追加方法
                }); 

            });


})




