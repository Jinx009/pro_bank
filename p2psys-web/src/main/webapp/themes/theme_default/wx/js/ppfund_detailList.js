define(function(require,exports,module){
    require('jquery');
    wt();
    function wt(){
    $.ajax({
      	type:"post",
      	url:"/ppfund/myInList.html?&_="+(new Date()).getTime(),
      	dataType:"json",
      	success:function(data){
    	 	if(data.data.list!=null){
    		 	var msg="";
    		 	for(var j=0;j<data.data.list.length;j++){
    			 	var att = data.data.list[j];
    			 	var name = att.ppfundName;/*产品名称*/
    			 	var account = att.account;/*投资本金*/
				 	var d = att.addTime;/*投标开始时间*/
				 	var endtime; 
				 	var apr = att.ppfundApr;/*收益率*/
				 	var isFixedTerm = att.isFixedTerm;/*0:活期  1:固定期限*/
				 	var isOut = att.isOut;/* 0:未转出  1:已转出*/
				 	var ppfundId = att.ppfundId;
				 	var id = att.id;
				 	var url = "/wx/account/ppfundDetail.html?id="+ppfundId; 
			     	function formatDate(d){
					  	var date =new Date(d);//转化成标准时间
					  	var  year=date.getFullYear(); 
					  	var  month=date.getMonth()+1;  
					  	var  date1=date.getDate();      
					  	var  hour=date.getHours(); 
					  	var  minute=date.getMinutes(); 
					  	var  second=date.getSeconds(); 
					  	var str1 = year+"-"+month+"-"+date1; 
					  	return str1;
					}
			    	var pbtime=formatDate(d) ;
			    	if(isFixedTerm==0){
			    		if(isOut==0){
			    			msg += "<div class='show_detail' style='position:relative;'><div class='col w60'><div class='list-row overflow'><a href='"+url+"'>"+name+"</a></div><div class='list-row'>"+account+"元<input class='zc' type='button' value='转出' id='zchu' /><input type='hidden' value='/ppfund/doPpfundOut.html?id="+id+"' class='zcid'></div></div><div class='col fr w40'><div class='list-row list-ltr'>"+apr+"%</div><div class='list-row list-ltr'>"+pbtime+"</div></div></div>";
			    		}else{
			    			msg += "<div class='show_detail' style='position:relative;'><div class='col w60'><div class='list-row overflow'><a href='"+url+"'>"+name+"</a></div><div class='list-row'>"+account+"元<a class='yzc'>已转出</a></div></div><div class='col fr w40'><div class='list-row list-ltr'>"+apr+"%</div><div class='list-row list-ltr'>"+pbtime+"</div></div></div>";
			    		}
			    	}else{
			    		msg += "<div class='show_detail' style='position:relative;'><div class='col w60'><div class='list-row overflow'><a href='"+url+"'>"+name+"</a></div><div class='list-row'>"+account+"元</div></div><div class='col fr w40'><div class='list-row list-ltr'>"+apr+"%</div><div class='list-row list-ltr'>"+pbtime+"</div></div></div>";
			 		}
			 	}
	    	 	$("#invest_list").html(msg);
    	 	}
      	}
   	}); 
	}
	
	$("#zc").click(function(){
		$("#yx").removeClass("current");
		$("#wt").removeClass("current");
		$(this).addClass("current");
		$("#invest_list").html("暂无数据");
	})
	$("#yx").click(function(){
		$("#wt").removeClass("current");
		$("#zc").removeClass("current");
		$(this).addClass("current");
		$("#invest_list").html("暂无数据");
	})
	$("#wt").click(function(){
		wt();
		$("#zc").removeClass("current");
		$("#yx").removeClass("current");
		$(this).addClass("current");
	});

	$(document).on("click","#zchu",function(){
		var zcid = $(this).parent().find(".zcid").val();
		require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
			//构造确认框DOM
			$.layer({
				type: 1,
				closeBtn: true,
                title: "&nbsp;",
			    area: ['450px', '190px'],
			    border: [1, 1, '#cecfd0'],
			    page: {
			        html: "<div class='tipsWrap w450' id='thps'>"+"<div class='tipsTxt'>"+"<span>"+"是否确定转出？"+"</span></div>"+"<div class='tipsBtnBar'>"+"<a href='###' class='okBtn' id='okBtn' >"+"确定"+"</a>"+"<a href='###' class='okBtn' id='okClose'>"+"取消"+"</a></div></div>"
			    }
			});
			
			$("#okBtn").click(function(){
				$.ajax({
					url:zcid,
					type:"get",
					dataType:"json",
					success:function(json){
						require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
							//构造确认框DOM
							$.layer({
								type: 1,
								closeBtn: true,
				                title: "&nbsp;",
							    area: ['450px', '190px'],
							    border: [1, 1, '#cecfd0'],
							    page: {
							        html: "<div class='tipsWrap w450' id='thps'>"+"<div class='tipsTxt'>"+"<span>"+"转出成功！"+"</span></div>"+"<div class='tipsBtnBar'>"+"<a href='/wx/account/ppfund.html' class='okBtn' >"+"确定"+"</a></div></div>"
							    }
							});
					});
						}
					})
				layer.closeAll();
			})
			
			$("#okClose").click(function(){
				layer.closeAll();
			})
		});
	})
});
