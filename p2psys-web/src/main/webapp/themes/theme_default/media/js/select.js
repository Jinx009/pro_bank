$.fn.extend({
    minSelect:function(options){
        var defaults = {
			title:"tit",//模拟select的标题
			selectbox:"selectbox",//生成的单个div结构
			ulSelect:"select_ul",//模拟的select
			titleClass:"tit_hover",//点击select后变化的样式变化
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
			obj.wrap("<div class="+o.selectbox+"></div>").before("<div class="+o.title+"><span>"+o.titleTxt+"</span><b class=ico></b></div>").before("<ul class="+o.ulSelect+"></ul>");//创建伪selectDOM结构
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