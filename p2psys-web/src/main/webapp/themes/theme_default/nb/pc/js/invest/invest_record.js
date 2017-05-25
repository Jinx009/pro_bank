var flag = false;
$(function(){
	changeHeader("账户中心");
	investLog();
	$("#sub").click(function(){
		investLog();
	});
	function investLog(){
		$.ajax({
			type:"post",
			 url:"/invest/record.html",
			  dataType:"json",
			  data:{
				  startTime:$("#startDate").val(),
				  endTime:$("#endDate").val()
				  },
			  success:function(json){	
				  if(checkUser(json.result)){
					investPage(json);
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
								        		url:"/invest/record.html",
								        		dataType:"json",//这个必不可少，如果缺少，导致传回来的不是json格式
								        		 data:{
								        			  page:n,
								   				  startTime:$("#startTime").val(),
								   				  endTime:$("#endTime").val()},
								        		success:function(json){
								        			$(".investData").html("");
								        			investPage(json);
								        		}
								        	});
									this.selectPage(n); //处理完后可以手动条用selectPage进行页码选中切换
								}
							});
						
					}else{
						$("#kkpager").html("<p style='text-align:center;width:100%;margin:0 auto;'>暂无数据</p>");
					}
				  }else{
//					  showDiv("util_login");
				  }
			  }
		});
	}
	
	function investPage(json){
		  var htmlStr ="";
		  $.each(json.data.list,function(i,item){
			  htmlStr +="<tr title="+item.productName+">";
			  htmlStr +="<td>"+noticeDateFormat_(item.addTime)+"</td>";
			  htmlStr +="<td>"+productFormat_(item.productName)+"</td>";
			  if(item.ppfundInType=="体验标"){
				 htmlStr +="<td>¥"+format_(item.interestAmount)+"元</td>";
			  }else{
				  htmlStr +="<td>¥"+format_(item.money)+"</td>";
			  }
			  
			  if(item.typeId == 122){
				  //收益率
				  htmlStr +="<td>"+item.expectedLow+"%~"+item.expectedUp+"%</td>"
				  //到期收益
				  if(item.expectProfit==-1){
					  htmlStr +="<td class='expectProfit'>--</td>";
				  }else{
					  htmlStr +="<td class='expectProfit'>"+format_(item.expectProfit)+"元</td>";
				  }
				  
			  }else{
				 //收益率 
				  if(item.rateStatus == 1){
					  if(item.addRate > 0){
						  htmlStr +="<td>"+item.apr+"%+"+item.addRate+"%</td>"
					  }else{
						  htmlStr +="<td>"+item.apr+"%</td>"
					  }
					 
				  }else{
					  htmlStr +="<td>"+item.apr+"%</td>";
				  }
				 
				 //到期收益
				 if(item.type ==1){//现金
					 if(item.isFixedTerm ==1){//定期
						 htmlStr +="<td class='expectProfit'>"+format_(item.expectProfit)+"元</td>";
					 }else{//活期
						 htmlStr +="<td class='expectProfit' class='fd'>--</td>";
					 }
				 }else{//非现金
					 htmlStr +="<td class='expectProfit'>"+format_(item.expectProfit)+"元</td>";
				 }
				 
			  }
			  //到期日			  
			  if(item.type ==2){
				  htmlStr +="<td>"+item.expirationDate+"</td>";
			  }else{
				  if(item.isFixedTerm ==1){//现金类不可赎回
					  htmlStr +="<td>"+item.expirationDate+"</td>";
				  }else{
					  htmlStr +="<td class='fd'>--</td>";
				  }
			  }
			  
			  
			  //赎回
			  if(item.type==1){//现金类
				  if(item.isFixedTerm ==1){
					  htmlStr += "<td>不可赎回</td>";
				  }else{
					  if(item.isOut==0){
						  htmlStr +="<td><a class='redeems' style='cursor: pointer;' onclick='doRedeem(\"/ppfund/doPpfundOut.html?id="+item.id+"\","+item.money+","+item.id+");'>赎回</a></td>"; 
					  }else{
						  htmlStr +="<td>已赎回</td>";
					  }
				  }
			   }else if(item.type ==2){//非现金类
				htmlStr  +="<td class='fd'>--</td>";	  
			  }
			  //投资协议
			  if(item.type ==2){
				  if(item.typeId == 199){
					  htmlStr += "<td><a href='/invest/tenderProtocol.html?tenderId="+item.id+"'>下载</a></td>";
				  }else{
					if(item.status == 1 && item.scales == 100){ 
						htmlStr += "<td><a href='/invest/tenderProtocol.html?tenderId="+item.id+"'>下载</a></td>";
					}else{
						htmlStr += "<td class='fd'>--</td>";
					}
				  }
			  }else{
				  htmlStr +="<td><a href='/invest/inProtocol.html?inId="+item.id+"'>下载</a></td>";
			  }
			  //说明
			  htmlStr +="<td>"+item.remark+"</td>";
			  htmlStr +="</tr>";
			  
		  });
		  $(".investData").html(htmlStr);
	}
	
	
	
//	$(".redeems").click(function(){
//		alert($(".redeemId").val());
//		showDoubleAlert("确定赎回？","","openUrl("+$(".redeemId").val()+")","closeDiv('')");
//	});
});


function doRedeem(url,redeemMoney,ppfundId){
//	showDoubleAlert("确定赎回？","","closeDiv('')","sub_('"+url+"')");
//	showRedeemAlert("closeDiv('')","sub_('"+url+"')");
	$("#msg").html(" ");
	showRedeem_("closeDiv('')","sub_('"+url+"')",redeemMoney,ppfundId);
//	
}

function sub_(zcid){
	$("#msg").html(" ");
	var url = zcid+"&redeemMoney="+$("#redeemMoney").val();
	var valid ="/ppfund/ppfundOut.html?ppfundId="+$("#ppfundId").val()+"&redeemMoney="+$("#redeemMoney").val();
	if(!flag){
		flag = true;
		$.ajax({
			url:valid,
			type:"POST",
			dataType:"json",
			success:function(json)
			{
				if(json.result ==false)
				{
					$("#msg").html(json.msg);
				}else{
					var cash =parseFloat(json.msg).toFixed(2);
					$.ajax({
						url:url,
						type:"POST",
						dataType:"json",
						success:function(json)
						{
							
							if(json.result ==false)
							{
								 showAlertDiv(false,"赎回失败","请联系客服","");
							}
							else if(json.result==true)
							{
								 showAlertDiv(true,"赎回成功!","您当日可赎回额度［"+cash+"］元","/invest/log.html?random="+Math.random());
							}
							flage = false;
						}
					});
				}
				flag = false;
			}
		});
	}
	
	
	
}
function productFormat_(productName){
		if(productName.length>5){
			return (productName.substring(5,productName)+"...");
		}else{
			return productName;
		}
}