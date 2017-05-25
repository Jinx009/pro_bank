$(function(){
	$.ajax({
		type:"post",
		 url:"/nb/wechat/account/lockRecord.html?random="+Math.random(),
		  dataType:"json",
		  data:{page:1},
		  success:function(json){
			//总记录数
				  page(json);
					//分页插件
					if(json.data.page.pages > 0)	
					{
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
								        		type:"post",
								        		cache:false,
								        		url:"/nb/wechat/account/lockRecord.html",
								        		dataType:"json",//这个必不可少，如果缺少，导致传回来的不是json格式
								        		 data:{
								        			  page:n},
								        		success:function(json){
								        			$(".investItem").html("");
								        			page(json);
								        		}
								        	});
									this.selectPage(n); //处理完后可以手动条用selectPage进行页码选中切换
								}
							});
						
					}else{
						$("#kkpager").html("<p style='text-align:center;width:100%;margin:0 auto;font-size:14px;color:rgb(140,140,140)'>暂无数据</p>");
					}
		  }
	});
	
	function page(json){
		 var htmlStr ="";
		  if(json.data!=""){
			  $.each(json.data.list,function(i,item){
				  htmlStr +="<div class='fix_details_w'>";
				  htmlStr +="<dl>";
				  if(item.type ==1){
					  htmlStr +="<dt>提现锁定</dt>";
				  }
				  if(item.type==2){
					  htmlStr +="<dt>投资锁定</dt>";
				  }
				 htmlStr +="<dd>"+productFormat_(item.productName)+"</dd>";
				 htmlStr +="</dl>"
				 htmlStr +="<dl>"
				 htmlStr +="<dt>"+format_(item.money)+"元</dt>"
				 htmlStr +="<dd>"+noticeDateFormat_(item.addTime)+"</dd>"
				 htmlStr +="</dl>"
				 htmlStr +="<dl>"
				 htmlStr +="<dt></dt>"
				  if(item.type ==1){
					  htmlStr +="<dd><a href='/nb/wechat/account/rules.html'>查看到账规则</a></dd>";
				  }
				  if(item.type==2){
					  htmlStr +="<dd><a href='/nb/wechat/product/productDetail.html?product_id="+item.id+"'>查看产品详情</a></dd>";
				  }
				  
				 htmlStr +="</dl></div>";
			  });
			  $(".investItem").html(htmlStr);
		  }
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
});