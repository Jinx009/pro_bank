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
							if(typeof o.callBack=='function')
							{
								o.callBack.call(this)
							}
						}
					});
				});
			}
	});
