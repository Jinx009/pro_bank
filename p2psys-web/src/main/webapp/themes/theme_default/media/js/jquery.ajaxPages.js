/*分页功能函数*/
define(function(require,exports,modlue){
	//exports.runData
	$.fn.extend({
		jsonPage:function(options){
			var defaults = {
				jsonUrl: "",
				colModel:"",
				jsPageBox:"#js_page",
				jsonData:"#json_data",
				callBack:null
			};
			var options = $.extend(defaults,options);
			this.each(function(){
				var o = options;
				var objBox = $(this);
				var handlebar = require("./handlebars-v1.3.0");
				var typeVal = "";
				var model = null;
				var tarName = objBox.get(0).tagName;	//获取当前元素的标签类型;
				var pageParent = $(o.jsPageBox);
				if(o.jsonUrl.indexOf("?")>0)
				{
					ajaxPages(o.jsonUrl+"&randomTime="+ (new Date()).getTime())//调用分页函数ajaxPages   增加了随机数
				}else{
					ajaxPages(o.jsonUrl+"?randomTime="+ (new Date()).getTime())//调用分页函数ajaxPages   增加了随机数
				}
				

				function ajaxPages(url){
					if(tarName!="TABLE"){
						var loadingBlock = '<div class="xubox_loading_1_new"></div>';//loading效果
					}
					var index_jsonContent = "";
					
					var indexJsId = $(o.jsonData).html();
					
					var index_template = Handlebars.compile(indexJsId);//编译模块
					
					var pageTotal = $(".total font",pageParent);
					var pageBox   = $(".pageBox",pageParent);
					var pageList  = $(".pageList",pageParent);
					var showPages = 5;
					var pageNumWidth  = 28; //页码宽度
					var finalData=new Array();
					//进行页面json数据的渲染
					function changeDate(url){
						$.ajax({
							url:url,
							type:"POST",
							success:function(ret){
								creditDiv(ret);//创建数据		
								if(ret.data.list=="")
								{
									pageParent.parent().find(".J_nomsg").remove();
									pageParent.css("left","-9999px").after("<div class='J_nomsg'>暂无记录</div>");
								}else{
									pageParent.parent().find(".J_nomsg").remove();
									pageParent.css("left","auto")
									ajaxPageJson(ret,url);//分页功能	
								}
							}
						});
					}
					//第一次加载,改变显示数据
					changeDate(url);
					
					//formatter改变json数据状态
					function _formatter(data){
						finalData=new Array();
						var models=o.colModel;
						for(var i=0;i<data.length;i++){
							var rows=data[i];
							var finalRows=$.extend(true, {}, rows);
							
							for(var j in models){
								var col=models[j];
								if(col.formatter){
									var value=rows[col.field];;
									finalRows[col.field]=col.formatter(value,rows);
								}
								
							}
							finalData[i]=finalRows;
						}
						
						return finalData;
					}
					   
					//创建数据
					function creditDiv(ret){
						ret.data.list=_formatter(ret.data.list);
						
						//获取当前的序列号 addOne方法
						Handlebars.registerHelper("addNum",function(index,options){
							var itemNum = "";
							index = parseInt(index)+1;
							if(ret.data.page.currentPage){
								itemNum = (ret.data.page.currentPage-1)*10;
								itemNum = itemNum+index
							}
							return itemNum;
						});
						
						//handlebarJS的编译	
						index_jsonContent = index_template(ret);
						
						
						if(tarName=="TABLE"){
							//如果是表格则按如此显示
							setTableInnerHTML(objBox[0],index_jsonContent); 
						}else{
							//普通标的数据显示
							objBox.html(index_jsonContent);
						}
						//增加表格奇偶样式
						$("tr:odd").addClass("c_fefe");
						indexList = $(".data_item ",objBox);//
						
						//回调函数
						if(typeof o.callBack=='function')
						{
							o.callBack.call(this)
						}
					}
					
					//IE版本下表格.html()方法失效不能渲染的兼容处理		
					function setTableInnerHTML(table, html) { 
						if(navigator && navigator.userAgent.match(/msie/i)){ 
							var temp = table.ownerDocument.createElement('div'); 
							temp.innerHTML = '<table><tbody>' + html + '</tbody></table>'; 
							if(table.tBodies.length == 0){ 
							var tbody=document.createElement("tbody"); 
							table.appendChild(tbody); 
						} 
							table.replaceChild(temp.firstChild.firstChild, table.tBodies[0]); 
						} else { 
							table.innerHTML=html; 
						} 
					} 
					
					//ajax分页底层处理方法
					function ajaxPageJson(result,Url){	//result:返回对象， Url：接口地址		
						var pageNum = $(".pagenum" , pageParent);
						var pages = "",indexPage = "",
							front = $(".front"),
							next = $(".next"),
							first = $(".first"),
							last = $(".last"),
							goBtn  = $(".gopage_btn"),
							preNum = "";//分页按钮
							next.css("cursor","pointer");
						last.css("cursor","pointer");
						creditPage(result,pageNum,indexPage);//创建分页
						var pageChild = $("span" , pageBox);//分页DOM内的所有span标签
						var PageNumChild = $("span" , pageNum);//分页的数字span标签
						var num = "";
						$(PageNumChild[0]).addClass("active");
						var formerNum = ""; 
						
						
						//单页点击事件
						PageNumChild.each(function(){
							var that = $(this);
							//that.css("cursor","pointer");
							that.unbind();
							that.bind("click",function(){
								formerNum = indexPage;
								num = $(this).attr("data-value");
								pageMove("numClk",num);
								pageJson(num);
								cursorType(num,(PageNumChild.first().attr("data-value")),(PageNumChild.last().attr("data-value")));
							});
						});		
						
						//解除事件绑定
						front.unbind();
						//向前一页	
						front.bind("click",function(){
							next.css("cursor","pointer");
							preNum = $("span.active").attr("data-value");
							if(preNum==(PageNumChild.first().attr("data-value")))
							{
								return false;
							}else{
								pageMove("front",preNum);		
								preNum--;
								pageJson(preNum);
								if(preNum==(PageNumChild.first().attr("data-value")))
								{
									front.removeAttr("style");
								}
							}
						});
						
						next.unbind();
						//往后一页
						next.bind("click",function(){
							front.css("cursor","pointer");
							preNum = $("span.active").attr("data-value");
							if(preNum==PageNumChild.last().attr("data-value"))
							{
								next.removeAttr("style");
								return false;
							}else{
								pageMove("next",preNum)
								preNum++;
								pageJson(preNum);
								if(preNum==(PageNumChild.last().attr("data-value")))
								{
									next.removeAttr("style");
								}
							}
						});
						
						first.unbind();
						//跳转至首页
						first.bind("click",function(){
							preNum = $("span.active").attr("data-value");
							if(preNum==1){
							  return false;	
							}
							pageMove("first",1);
							pageJson(1);
							cursorType(1,1,parseInt(pageTotal.text()));
						});
						
						last.unbind();
						//跳转至未页
						last.click(function(){
							preNum = $("span.active").attr("data-value");
							totalNum = parseInt(pageTotal.text());
							if(preNum==totalNum){
							  return false;	
							}
							pageMove("last",totalNum);
							pageJson(totalNum);
							cursorType(totalNum,1,totalNum);
						});		
						
						goBtn.unbind();
						//输入数字进行页数跳转
						goBtn.bind("click",function(){
							preNum = $("span.active").attr("data-value");
							var gopageBox = $(".gopage_val");
							var gopageNum = gopageBox.val();
							var maxLen = parseInt(pageTotal.text());
							var minLen = 1;
								gopageNum = parseInt(gopageNum);
							if(gopageNum>maxLen)
							{
								gopageBox.val(maxLen);
								gopageNum = maxLen;
							}else if(gopageNum<0)
							{
								gopageBox.val(minLen);
								gopageNum = minLen;
							}
							if(preNum==gopageNum){
								return false
							}
							pageMove("gopage",gopageNum);
							pageJson(gopageNum);
							cursorType(gopageNum,minLen,maxLen);
						});
						
						//页码移动
						function pageMove(type,pageNum){
							pageNum = parseInt(pageNum)
							var speed =  "fast";			
							var totalNum = parseInt(pageTotal.text());
							//console.log(pageNum,showPages)
							var pageListMargin = parseInt(pageList.css('margin-left'));
							if(type=="next"&&(totalNum-pageNum>showPages-1)){
								 pageList.animate({marginLeft:-pageNum*pageNumWidth},speed);	
							}else if(type=="front"&&pageNum>showPages){
								 pageList.animate({marginLeft:pageListMargin+pageNumWidth},speed);
							}else if(type=="numClk"&&(totalNum-pageNum>=showPages-1)){
								//如果单页点击并且小于设置页数（5页）时，margin为0；
								 if(pageNum<showPages){
									 pageList.animate({marginLeft:0},speed); 
								 }else{
									 pageList.animate({marginLeft:-(pageNum-1)*pageNumWidth},speed);
								 }
							}else{	
								//当为5页的时候，则进行动画翻页
								if(pageNum%showPages==0){
									pageList.animate({marginLeft:-(totalNum-showPages)*pageNumWidth},speed);	
								}else if(pageNum<=showPages){
									pageList.animate({marginLeft:0},speed);
								}
								else if((totalNum-pageNum)<showPages){
									 pageList.animate({marginLeft:-(totalNum-showPages)*pageNumWidth},speed);	
								}else{
									 pageList.animate({marginLeft:-(pageNum-1)*pageNumWidth},speed);
								}
							}
						}		
						
						//页码的鼠标样式处理,根据页数判断是否显示鼠标样式，如果是最大的时候,去掉鼠标的样式，否则显示鼠标的样式
						function cursorType(indexs,min,max){
							if(indexs==min)
							{
								front.removeAttr("style");
								first.removeAttr("style");
							}
							else{
								front.css("cursor","pointer");
								first.css("cursor","pointer");
							}//如果是最小的时候,去掉鼠标的样式，否则显示鼠标的样式
							if(indexs==max)
							{
								next.removeAttr("style");
								last.removeAttr("style");
							}
							else{
								next.css("cursor","pointer");
								last.css("cursor","pointer");
							}
						}
						
						//分页数据渲染
						function pageJson(pNum){
							PageNumChild = $("span" , pageNum);
							$(PageNumChild[pNum-1]).addClass("active").siblings().removeClass("active");
							if(Url.indexOf("?")>0)
							{
								pageUrl = Url+"&page="+pNum;//接口地址	
							}else{
								pageUrl = Url+"?page="+pNum;//接口地址	
							}
							//objBox.append(loadingBlock);
							$.ajax({
								url:pageUrl,
								type:"POST",
								success:function(ret)
								{
									
									setTimeout(function(){
										creditDiv(ret);
									},400);
									
								}
							});
						}
					}
					
					//对象，分页的DOM对象，当前页数
					function creditPage(data,pageNum,indexPage)
					{
						var str = "";
						var pageObj = "";
						var pageLen = "";
						if(data.data.page){
							pageObj = data.data.page
						}else{
							pageLen = 0	
						}
						pageLen = pageObj.pages;	
						//pageLen = 20;//调试设置为20页
						
						pageLen < showPages && (showPages = pageLen);	
						pageBox.width(showPages*pageNumWidth);//页码可见部分宽度
						pageList.width(pageLen*pageNumWidth);//页面列表整体宽度		
						indexPage = pageObj.currentPage;
						pageTotal.text(pageLen);
						for(var i=0; i<pageLen; i++)
						{
							str+="<span data-value='"+(i+1)+"'>"+(i+1)+"</span>";
						}
						pageNum.html(str);
						pageNum.addClass("clearfix")
					}//创建分页
				}
				
			});
		}
	});
	
	
});
