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
			obj.wrap("<div class="+o.Element+"></div>").hide().before("<b></b>");
			if(o.showTxt){
				obj.before("<label></label>").siblings("label").html(obj.attr("data-value"))	;
			}else{
				var imgPath=obj.attr("data-img");
				obj.before("<label></label>").siblings("label").html("<img src='"+imgPath+"'/>")	;
			}
			//创建DOM结构并且隐藏掉radio元素，再给同级的label的文字内容修改成当前的radio的data-value值
			var obj_name = obj.attr("name");
			var radioChild = $("input[name="+obj_name+"]")
			var radioParent = obj.parent("."+o.Element)	 //获得当前radio的父级元素
			//radioParent.addClass("clearfix") //清除浮动
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
