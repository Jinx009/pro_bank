(function($){
	$.fn.accordion = function(opts){
		var defaults = {
			wrapBox    : ".wrap",//最外围wrap元素
			titleBox   : "dt",//显示的标题
			contentBox : "dd",//隐藏的内容
			hoverClass : "hover",//展示之后的样式
			isClick    : true  //默认点击展开
		}
		var option = $.extend(defaults,opts);
		//判断是否相邻
		if($(option.titleBox).next()[0] == $(option.contentBox)[0])
		{
			if($(option.titleBox).hasClass(option.hoverClass))
			{
				$(option.titleBox+''+'.'+option.hoverClass).next().slideDown();
			}
		}
		else
		{
			if($(option.titleBox).hasClass(option.hoverClass))
			{
				$(option.titleBox+''+'.'+option.hoverClass).parent().next().slideDown();
			}
		}

		if(option.isClick)
		{
			if($(option.titleBox).next()[0] == $(option.contentBox)[0])
			{
				$(option.titleBox,option.wrapBox).click(function(){
					if(!$(this).next().is(":visible"))
					{
						$(option.wrapBox).find(option.titleBox).removeClass(option.hoverClass);
						$(option.wrapBox).find(option.contentBox).slideUp();
						$(this).addClass(option.hoverClass).next().slideDown();
					}
					else
					{
						$(this).removeClass(option.hoverClass).next().slideUp();
					}

				});
			}
			else
			{
				$(option.titleBox,option.wrapBox).click(function(){
					if(!$(this).parent().next().is(":visible"))
					{
						$(option.wrapBox).find(option.titleBox).removeClass(option.hoverClass);
						$(option.wrapBox).find(option.contentBox).slideUp();
						$(this).addClass(option.hoverClass).parent().next().slideDown();
					}
					else
					{
						$(this).removeClass(option.hoverClass).parent().next().slideUp();
					}

				});
			}
			
		}
		else
		{
			$(option.titleBox,option.wrapBox).mouseenter(function(){
				if($(option.titleBox).next()[0] == $(option.contentBox)[0])
				{
					if(!$(this).next().is(":visible"))
					{
						if(!$(this).next().is(":animated"))
						{
							$(option.wrapBox).find(option.titleBox).removeClass(option.hoverClass);
							$(option.wrapBox).find(option.contentBox).slideUp();
							$(this).addClass(option.hoverClass).next().slideDown();
						}
					}
				}
				else
				{
					if(!$(this).parent().next().is(":visible"))
					{
						if(!$(this).parent().next().is(":animated"))
						{
							$(option.wrapBox).find(option.titleBox).removeClass(option.hoverClass);
							$(option.wrapBox).find(option.contentBox).slideUp();
							$(this).addClass(option.hoverClass).parent().next().slideDown();
						}
					}
				}
				
				
			});	
		}
			
	}
})(jQuery)