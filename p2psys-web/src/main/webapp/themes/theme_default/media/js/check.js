$.fn.extend({
    minCheck:function(options){
        var defaults = {
			checkedClass:"check_box_hover", //点击之后的样式变化
			Element:"check_box",	//checkbox的父级对象
			showTxt:true //是否生成标题文字结构
        };
        var options = $.extend(defaults,options);
         this.each(function(){
            var o = options;
            var obj = $(this);
			obj.wrap("<div class="+o.Element+"></div>").hide().before("<b></b>");
			if(o.showTxt)
			{
				obj.before("<label></label>").siblings("label").text(obj.data("value"));	
			}
			//创建DOM结构并且隐藏掉checkbox元素，再给同级的label的文字内容修改成当前的checkbox的data-value值
			var checkParent = obj.parent("."+o.Element);//获得当前checkbox的父级元素
			if($(obj).prop("checked"))
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
