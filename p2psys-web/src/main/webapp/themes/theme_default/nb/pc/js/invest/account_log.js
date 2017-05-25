$(function(){	
	changeHeader("账户中心");
	
	//资金明细初次加载默认（充值纪录展示）
	var type="all";
	accoutLog("all");
	setTitle(type);
	$("#all").css("color","#3c94d1");
	$("#recharge").click(function(){
		type="online_recharge";
		accoutLog("online_recharge");
		setTitle(type);
		$("#recharge").css("color","#3c94d1");
		$("#frost").css("color","");
		$("#all").css("color","");
	});
	$("#frost").click(function(){
		type="cash_frost";
		accoutLog("cash_frost");
		setTitle(type);		
		$("#frost").css("color","#3c94d1");
		$("#recharge").css("color","");
		$("#all").css("color","");
	});
	$("#all").click(function(){
		type="all";
		accoutLog("all");
		setTitle(type);		
		$("#frost").css("color","");
		$("#recharge").css("color","");
		$("#all").css("color","#3c94d1");
	});
	
	$("#sub").click(function(){
		 $(".logData").html("");
		accoutLog(type);
		setTitle(type);
	});
	
	/**
	 * 账户信息
	 */
	 $.ajax({
		  type:"post",
		  url:"/user/capitalStatis.html?random="+Math.random(),
		  dataType:"json", 
		  success:function(json){
			  if(checkUser(json.result)){
				  $(".total_money").html(moneyFormat_(json.total,"total_money"));
				  $(".account_income_money").html(moneyFormat_(json.netProfit,"account_income_money"));
			  }else{
				  showDiv("util_login");
			  }
		  }
	   });
	
	/**
	 * 资金明细（充值纪录、提现纪录）
	 * @param type
	 */
	function accoutLog(type){
		  $(".logData").html("");
		  $("#kkpager").html("加载中....");
		$.ajax({
			type:"post",
			 url:"/account/logList.html?random="+Math.random(),
			  dataType:"json",
			  data:{
				  status:type,
				  startTime:$("#startDate").val(),
				  endTime:$("#endDate").val()},
			  success:function(json){
				//总记录数
				  if(checkUser(json.result)){
					  page(json,type);
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
									        		url:"/account/logList.html",
									        		dataType:"json",//这个必不可少，如果缺少，导致传回来的不是json格式
									        		 data:{
									        			  page:n,
									   				  status:type,
									   				  startTime:$("#startDate").val(),
									   				  endTime:$("#endDate").val()},
									        		success:function(json){
									        			$(".logData").html("");
									        			page(json,type);
									        			setTitle(type);
									        		}
									        	});
										this.selectPage(n); //处理完后可以手动条用selectPage进行页码选中切换
									}
								});
							
						}else{
							$("#kkpager").html("<p style='text-align:center;width:100%;margin:0 auto;font-size:14px;color:rgb(140,140,140)'>暂无数据</p>");
						}
				  }else{
					  showDiv("util_login");
				  }
				
			  }
		});
	}
	function page(json,type){
		 var htmlStr ="";
		  if(json.data!=""){
			  $.each(json.data.list,function(i,item){
				  htmlStr +="<tr>"
				  htmlStr +="<td>"+noticeDateFormat_(item.addTime)+"</td>	";
				  if(type=="online_recharge"){
					  if(item.type=="online_recharge"){
						  htmlStr +="<td>线上充值</td>";
					  }else if(item.type="off_recharge"){
						  htmlStr +="<td>线下充值</td>";
					  }else if(item.type="back_recharge"){
						  htmlStr +="<td>后台充值</td>";
					  }
					  htmlStr +="<td><span class='col'>"+item.money+"</span>元</td>";
					  htmlStr +="<td><span class='col'>"+item.money+"</span>元</td>";
					  htmlStr +="<td>成功充值</td>";
				  }else if(type=="cash_frost"){
					  htmlStr +="<td><span class='col'>"+item.money+"</span>元</td>";
					  htmlStr +="<td><span class='col'>"+item.money+"</span>元</td>";
					  if(item.type=="cash_frost"){
						  htmlStr +="<td>提现冻结</td>";
					  }else if(item.type=="cash_success"){
						  htmlStr +="<td>提现成功</td>";
					  }else if(item.type=="cash_cancel"){
						  htmlStr +="<td>提现取消</td>";
					  }else if(item.type=="cash_fail"){
						  htmlStr +="<td>提现失败</td>";
					  }
				  }else if(type=="all"){
					  htmlStr +="<td>"+item.typeName+"</span></td>";
					  htmlStr +="<td><span class='col'>"+item.money+"</span>元</td>";
				  }
				  htmlStr +="<td>"+item.remark+"</td>";
				  htmlStr +="</tr>";
			  });
			  $(".logData").html(htmlStr);
		  }else{
			  $(".logData").html(htmlStr);
		  }
	}
	
	function setTitle(type){
		var htmlStr ="";
		if("online_recharge" == type){
			htmlStr +="<th>时间</th><th>支付方式</th><th>充值金额</th><th>到账金额</th><th>状态</th><th>充值状态</th>";
		}
		if("cash_frost" ==  type){
			htmlStr +="<th>时间</th><th>提现金额</th><th>到账金额</th><th>状态</th><th>提现状态</th>";
		}
		if("all" == type){
			htmlStr +="<th>时间</th><th>类型</th><th>资金金额</th><th>说明</th>"
		}
		$("#title").html(htmlStr);
	}
	
	function moneyFormat_(num,id){
		var pageMoney = parseFloat(num).toFixed(2).toString();
		var array = new Array();
		array=pageMoney.split(".")[0]; 
		console.log(array)
		pageUseMoney = "";
		var arrayFirst = new Array();
		var arraySecond = new Array();
		var j = 0;
		for(var i = array.length-1;i>=0;i--)
		{
			if(0===(array.length-i)%3&&i!==(array.length-1)&&0!==i)
			{
				arrayFirst[j] = array[i];
				j++;
				arrayFirst[j] = ",";
			}
			else
			{
				arrayFirst[j] = array[i];
			}
			j++;
		}

		
		for(var k = arrayFirst.length-1;k>=0;k--)
		{
		
			pageUseMoney += arrayFirst[k]; 
		}
		if(j>8)
		{
			$("."+id).css("font-size","1.9em");
			$(".account_income_money").css("font-size","1.9em");
		}
		$("."+id).html(pageUseMoney+"."+pageMoney.split(".")[1]);
	}
});

