var flag = false;
$(function(){
    var id ="investItem";
    var status;
	ajaxUrl('/nb/wechat/account/assetsListByTime.html');
	
	//通用显示函数
	function ajaxUrl(param){
		$.ajax({
			type:'get',
			cache:false,
			url:param+"?page=1&status="+status,
			dataType:'json',
			success:function(json){
					if(json.data.page.pages <= 0){
						$("#f_d").css("display","none");
					}
				    if(json.data.page.pages >=1){
						$("#f_d").css("display","block");
					}
				    if(id =="investItem"){
				    		pageGD(json,id)
				    }else{
				    		pageFD(json,id);
				    }
					
					$(".invest-list:nth-child(4n)").css("margin-right","0");
					//分页插件
					if(json.data.page.pages > 0)	
					{
						$("#kkpager").html("");
							kkpager.generPageHtml({
								pno : json.data.page.currentPage,//当前页码
								total : json.data.page.pages,//总页码
								totalRecords : json.data.page.total,//总数据条数
								isShowFirstPageBtn	: false, 
								isShowLastPageBtn	: false, 
								isShowTotalPage 	: false, 
								isShowTotalRecords 	: false, 
								isGoPage 			: false,
								lang:{
									prePageText				: '<',
									nextPageText			: '>'
								},
								mode:'click',//click模式匹配getHref 和 click
								click:function(n,total,totalRecords){
								        	$.ajax({
								        		type:"get",
								        		cache:false,
								        		url:param+"?page="+n+"&status="+status,
								        		dataType:"json",//这个必不可少，如果缺少，导致传回来的不是json格式
								        		success:function(json){
								        			  if(id =="investItem"){
												    		pageGD(json,id)
												    }else{
												    		pageFD(json,id);
												    }
												$(".invest-list:nth-child(4n)").css("margin-right","0");
								        		}
								        	});
									this.selectPage(n); //处理完后可以手动条用selectPage进行页码选中切换
								}
							});
					}else{
						$("#kkpager").html("<p style='text-align:center;width:100%;margin:0 auto;'>暂无数据</p>");
					}
				}
		})	
	}	
	
	$(".fix_header ul li").click(function(){
 		if("浮动收益类"==$(this).html()){
 			 id = "investItems";
 			 status=1;
 			ajaxUrl("/nb/wechat/account/entrustListByTime.html");
 			$("#fix_income").css("display","none");
		    $("#float_income").css("display","block");
 		}else{
 		     id ="investItem";
 			 ajaxUrl('/nb/wechat/account/assetsListByTime.html');
 			$("#fix_income").css("display","block");
		    $("#float_income").css("display","none");
 		}
 	});
	
	function pageGD(json,id){
		 var htmlStr ="";
		  if(json.data!=""){
			  $.each(json.data.list,function(i,item){
					  if(item.type == 1 && item.isOut == 0 && item.isFixedTerm == 0 ){
						  htmlStr +="<div class='fix_details_w'>";
						  htmlStr +="<dl>";
						  htmlStr +="<dt>"+productFormat_(item.productName)+"</dt>";
						  if(item.ppfundInType =='体验标'){
							  htmlStr +="<dd>"+format_(item.interestAmount)+"</dd>";
						  }else{
							  htmlStr +="<dd>"+format_(item.money)+"</dd>";
						  }
						 htmlStr +="</dl>";
						 
						 htmlStr +="<dl>";
						 htmlStr +="<dt>"+item.apr+"%</dt>";
						 htmlStr +="<dd>"+noticeDateFormat_(item.addTime)+"</dd>";
						 htmlStr +="</dl>";
						 
						 htmlStr +="<dl>";
						 htmlStr +="<p style='height: 60px;line-height: 60px; color:#3F72AA;'><a class='redeem'  id='"+item.id+"' vtext='"+item.money+"' >赎回</a></p>";
						 htmlStr +="</dl>";
						  
						 htmlStr +="</div>";
					  }
					  if(!checkEndTime(item.expirationDate)){
						  htmlStr +="<div class='fix_details_w'>";
						  htmlStr +="<dl>";
						  htmlStr +="<dt>"+productFormat_(item.productName)+"</dt>";
						  if(item.ppfundInType =='体验标'){
							  htmlStr +="<dd>"+format_(item.interestAmount)+"</dd>";
						  }else{
							  htmlStr +="<dd>"+format_(item.money)+"</dd>";
						  }
						 htmlStr +="</dl>";
						 
						 htmlStr +="<dl>";
						 if(item.typeId ==122){
							 htmlStr +="<dt>"+item.expectedLow+"%~"+item.expectedUp+"%</dt>";
						 }else{
							 if(item.rateStatus==1){
								 if(item.addRate > 0){
									 htmlStr +="<dt>"+item.apr+"%+"+item.addRate+"%</dt>";
								 }else{
									 htmlStr +="<dt>"+item.apr+"%</dt>";
								 }
								 
							 }else{
								 htmlStr +="<dt>"+item.apr+"%</dt>";
							 }
							 
						 }
						
						 htmlStr +="<dd>"+noticeDateFormat_(item.addTime)+"</dd>";
						 htmlStr +="</dl>";
						 
						
						 htmlStr +="<dl>";
						 htmlStr +="<dt>"+format_(item.expectProfit)+"元</dt>";
						 htmlStr +="<dd>"+item.expirationDate+"</dd>";
						 htmlStr +="</dl>";
						  
						 htmlStr +="</div>";
					  }
			  });
			  $("."+id).html(htmlStr);
		  }
	}
	
	function pageFD(json,id){
		var htmlStr ="";
		  if(json.data!=""){
			  $.each(json.data.list,function(i,item){
				  if( !checkEndTime(item.expirationDate)){
						  htmlStr +="<div class='fix_details_w'>";
						  htmlStr +="<dl>";
						  htmlStr +="<dt>"+productFormat_(item.borrowName)+"</dt>";
						  htmlStr +="<dd>"+format_(item.account)+"</dd>";
						  htmlStr +="</dl>";
						 
						 htmlStr +="<dl>";
						 htmlStr +="<dt>"+item.expectedLow+"%~"+item.expectedUp+"%</dt>";
						 htmlStr +="<dd>"+noticeDateFormat_(item.addTime)+"</dd>";
						 htmlStr +="</dl>";
						 
						
						 htmlStr +="<dl>";
						 if(checkTime(item.expirationDate)){
							 htmlStr +="<dt>"+format_(item.expectProfit)+"元</dt>";
						 }else{
							 htmlStr +="<dt>--</dt>"
						 }
						 htmlStr +="<dd>"+item.expirationDate+"</dd>";
						 htmlStr +="</dl>";
						  
						 htmlStr +="</div>";
					  }
			  });
			  $("."+id).html(htmlStr);
		  }
	}
	
	
	//赎回
	$(".investItem").delegate('.redeem', 'click', function(event) {
		event.preventDefault();
        var self = $(this);
        var _money =self.attr("vtext");
        layer.open({
            content: '<div style="font-size: 16px; font-weight: 300; color: #4A4747; margin-top: 28px; margin-bottom: 3px;position: relative;text-align: center;"><div style="font-size: 16px; color: #343434; position: absolute; top: -32px; left: 50%; width: 140px; text-align: center; margin-left: -70px;">请输入赎回金额</div><input type="text" value='+_money+'  style="padding:1px 0 1px 4px; height: 27px; margin-top:4px; margin-bottom: 20px; border-radius: 3px; border: none; width:200px;" placeholder="请输入赎回金额" id="redeemMoney" autofocus=""><div style="width: 206px; margin: 0 auto;color:red;margin-top: -10px;margin-bottom: 20px;font-size: 12px;" class="wrong-msg"></div></div>',
            btn: ['确认', '取消'],
            style: 'min-width: 280px;min-height:125px;text-align: center;border-radius: 6px;padding-bottom: 20px;background: #ededed;',
            shadeClose: true,
            yes: function(index) {
                var redeemMoney = $("#redeemMoney").val();
                var _id = self.attr("id");
                	if(!flag){
                		flag = true;
                		$.ajax({
                    		url:"/ppfund/ppfundOut.html",
                    		type:"POST",
                    		dataType:"json",
                    		data:{
                    			ppfundId:self.attr("id"),
                    			redeemMoney: $("#redeemMoney").val()
                    		},
                    		success:function(json)
                    		{
                    			if(json.result ==false)
                    			{
                    				 $(".wrong-msg").html(json.msg).show();
                    			}else{
                    				var cash =parseFloat(json.msg).toFixed(2);
                    			 $.ajax({
                                     url:"/ppfund/doPpfundOut.html",
                                     type: 'POST',
                                     data: {
                                         id:self.attr("id"),
                                         redeemMoney: $("#redeemMoney").val()
                                     },
                                     success: function(res) {
                                         if (res.result == false) {
                                        	 layer.close(index);
                                             layer.open({
                                                 content: "赎回失败! "+res.msg+"",
                                                 style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:10px; letter-spacing: 2px; line-height: 100%;',
                                                 time: 3
                                             });
                                         } else if (res.result == true) {
                                             layer.close(index);
                                             layer.open({
                                                 content: "赎回成功! 您当日可赎回额度［"+cash+"］元",
                                                 style: 'background-color: rgba(0,0,0,.6); text-align:center; color:#fff; border:none; font-size:10px; letter-spacing: 2px; line-height: 100%;',
                                                 time: 3
                                             });
                                             setTimeout(function(){
                                            	 location.href="/nb/wechat/account/collect.html";
                                             },3500);
                                            
                                         }
                                         flag = false;
                                     }
                                 });
                    			}
                    			flag = false;
                    		}
                     	});
                	}
                	

            },
            no: function(index) {
                layer.close(index);
            }
        });
        $("#redeemMoney").keyup(function(){
    		if($("#redeemMoney").val().search(/^\d*(?:\.\d{0,2})?$/)==-1)
    			{
    				$("#redeemMoney").val("");
    			}
    	});
    }); //-.btn-donation.click
	
	
    function checkEndTime(endTime){ 
        var startTime=new Date().format("yyyy-MM-dd"); 
        var start=new Date(startTime.replace("-", "/").replace("-", "/"));  
        var end=new Date(endTime.replace("-", "/").replace("-", "/"));  
        if(end<=start){  
            return true;  
        }  
        return false;  
    	 }  
    
    function checkTime(endTime){ 
        var startTime=new Date().format("yyyy-MM-dd"); 
        var start=new Date(startTime.replace("-", "/").replace("-", "/"));  
        var end=new Date(endTime.replace("-", "/").replace("-", "/"));  
        if(end==start){  
            return true;  
        }  
        return false;  
    	 }  

	
	function productFormat_(productName){
		if(productName.length>5){
			return (productName.substring(5,productName)+"...");
		}else{
			return productName;
		}
	}
	
	/**
	 * 金额格式化（保留小数点后两位）
	 * @param money
	 * @returns
	 */
	function format_(money){
		if(money == 0)
		{
			return money;
		}
		else if(money == 'undefined')
		{
			return 0;
		}
		else
		{
			n = 2; 
			money = parseFloat((money + "").replace(/[^\d\.-]/g, "")).toFixed(n) + ""; 
			var l = money.split(".")[0].split("").reverse(), r = money.split(".")[1]; 
			t = ""; 
			for (i = 0; i < l.length; i++) { 
			t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : ""); 
			} 
			return  t.split("").reverse().join("")+ "." + r;
		}
	}
	/**
	 * 日期时间各式话（2015.08.27 12:00）
	 * @param value
	 * @returns {String}
	 */
	function noticeDateFormat_(value)
	{
		function formatDate(now) 
		{ 
			var year=now.getFullYear();
			var month=now.getMonth()+1; 
			var date=now.getDate(); 
			var hour=now.getHours(); 
			var minute=now.getMinutes(); 
			var second=now.getSeconds(); 
			if(month < 10)
			{
				month = '0' + month;
			}
			if(date < 10)
			{
				date = '0' + date;
			}
			if(hour < 10)
			{
				hour = '0' + hour;
			}
			if(minute < 10)
			{
				minute = '0' + minute;
			}
			return year+"."+month+"."+date+" "+hour+":"+minute; 
		} 
		if(value==null ||value=='')
		{
			return '';
		}
		var d = new Date(value);
		return formatDate(d);
	}
	
	Date.prototype.format = function(format)
	{
		var o = 
		{
		"M+" : this.getMonth()+1, 
		"d+" : this.getDate(), 
		"h+" : this.getHours(),
		"m+" : this.getMinutes(), 
		"s+" : this.getSeconds(), 
		"q+" : Math.floor((this.getMonth()+3)/3), 
		"S" : this.getMilliseconds() 
		}

		if(/(y+)/.test(format)) 
		{
			format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
		}

		for(var k in o) 
		{
			if(new RegExp("("+ k +")").test(format))
			{
				format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
			}
		}
		return format;
	} 
});

function redeem(zcid){
	$("#wrong_btn").show();
	$("#success_div").show();
	$("#mask_div").show();
	$("#right_btn").attr("onclick","doRedeem('"+zcid+"')");
}

function doRedeem(zcid)
{
	$("#wrong_btn").hide();
	
	$.ajax({
		url:zcid,
		type:"get",
		dataType:"json",
		success:function(json)
		{
			if(json.result ==false)
			{
				$("#pic").attr("src","/themes/theme_default/nb/wechat/images/close.png");
				$("#errorMsg").html(json.msg);
				$("#right_btn").attr("onclick","hideError()");
				showError();
			}
			else if(json.result==true)
			{
				$("#pic").attr("src","/themes/theme_default/nb/wechat/images/open.png");
				$("#errorMsg").html("赎回成功");
				$("#right_btn").attr("onclick","goUrl('/nb/wechat/account/collect.html')");
				
				showError();
			}
		}
	});
}
function hideError()
{
	$("#success_div").hide();
	$("#mask_div").hide();
}
function showError()
{
	$("#success_div").show();
	$("#mask_div").show();
}
function goUrl(url)
{
	location.href = url;
}





