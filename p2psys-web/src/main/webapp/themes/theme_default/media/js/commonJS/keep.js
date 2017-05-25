/*=8 滚动悬停*/
	$.fn.extend({
		keep: function(fn1,fn2) {
			var oThis = $(this),
				iTop = oThis.offset().top,
				iLeft = oThis.offset().left;
			$(window).bind("scroll load", function() {
				if ($(window).scrollTop() >= iTop) {
						oThis.css({
							"position": "fixed",
							"top": 0,
							"left": iLeft
						});
					
					fn1 && fn1();//未滚动到运行的函数
				} else {
					oThis.attr("style", "");
					fn2 && fn2();//滚动到运行的函数
				}
			});
		}
	});