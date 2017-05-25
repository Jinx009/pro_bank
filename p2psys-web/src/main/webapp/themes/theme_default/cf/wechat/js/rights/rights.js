var flag=0,ruleId=0,show=0,show_money=0,show_rule_id=0;
$(function(){
	layer.config({
	    extend: 'extend/layer.ext.js'
	});
	 $.ajax({
	        url: '/cf/wechat/user/rightData.html',
	        type: 'GET',
	        dataType: 'JSON',
	        data: {projectId: $("#projectId").val()},
	    }).done(function(res) {
		    	if(res.code == 200){
		    		var model =new Vue({
			            el: '#rigthsData',
			            data: {
			                rights: res.data
			            }
			        });
		    		
		    		flag = res.data.attetionFlage;
		    		if(flag >0){
		    			$(".point").text("已关注");
		    			$(".point").removeClass("rights-right-btn");
		    			$(".point").addClass("rights-right-btn_ban");
		    		}
		    		 for(var i in res.data.list){
		    			 ruleId = res.data.list[0].id;
		    		 }
		    		 show = res.data.showSupport;
		    	/*	 if(show ==5){
		    			 $(".support").removeClass("rights-left-btn");
		     		 $(".support").addClass("rights-left-btn_ban");
		     		 $(".support").html("您已经支持过")
		    		 }*/
		    		 if(show == 1){
		    			 $(".support").removeClass("rights-left-btn");
				     $(".support").addClass("rights-left-btn_ban");
		    			 $(".support").html("预热中")
		    		 }
		    		 if(show == 3){
		    			 $(".support").removeClass("rights-left-btn");
				     $(".support").addClass("rights-left-btn_ban");
		    			 $(".support").html("已过期")
		    		 }
		    		 if(show == 4){
		    			 $(".support").removeClass("rights-left-btn");
				     $(".support").addClass("rights-left-btn_ban");
		    			 $(".support").html("已完成")
		    		 }
		    		 $(".rights-detail-content .right-col").each(function(){
		    			if($(this).index()==0){
		    				$(this).addClass("right-col-active");
		    			}
		    		})
		    	}else{
		    		//取出code码弹出相应的tips 后续编码 ……Y(^_^)Y
		    	}
	    });
	 
	 
	 $('body').on('click', '.rights-detail-content .right-col', function(event) {
	       $(this).addClass('right-col-active').siblings().removeClass('right-col-active');
			var money = $(this).find('h1 font').text();
			ruleId = $(this).find('input').val();
			if(null!=money&&''!=money&&NaN!=money){
				show_money = money;
				show_rule_id = ruleId;
				$('.cash').text('¥'+money);
			}
	 });
	 
	 $('body').on('click', '.rights-detail-content .right-col .right-col-content', function(event) {
		 $(this).find("p").toggleClass("ellipsis").toggleClass("justify");
	 });
	 
	 //支持
	 $('.support').click(function(){
		 
		 if(show >0&&2!==show&&5!==show){
			 $(".point").removeClass("rights-left-btn support");
 			$(".point").addClass("rights-left-btn_ban");
		 }else if(show == 2||show==5){
			 var projectId = $('#projectId').val();
			 if(43==projectId&&'75000'==show_money){
				 layer.prompt({
					    title: '请输入优先定向码！',
					    formType: 1 
					}, function(pass){
						doGoBuy(pass);
					});
			 }else{
				 location.href='/wechat/pro/buy.html?projectId='+$('#projectId').val()+'&ruleId='+ ruleId;
			 }
		 }
	 })
	 //关注
	 $('.point').click(function(){
			if(flag <=0){
				attentionStatus = true;
				 $.ajax({
				        url: '/cf/pro/addAttention.html',
				        type: 'GET',
				        dataType: 'JSON',
				        data: {id: $('#projectId').val()},
				    }).done(function(res) {
				    	if('success'==res.result){
				    		layer.alert('关注成功！',{title:false,closeBtn: 0},function(){
				    			location.reload();
							});
						}else{
							layer.alert('关注失败！',{title:false,closeBtn: 0});
						}
				    });
			}
		
	 })
	 
	 $(".rightsBack").click(function(event){
		 event.preventDefault();
		 location.href="/wechat/pro/detail.html?id="+$("#projectId").val();
	 })
	 
})
function doGoBuy(pass){
	if('800ZF'==pass){
		location.href='/wechat/pro/buy.html?projectId='+$('#projectId').val()+'&ruleId='+ ruleId;
	}else{
		layer.closeAll();
		layer.alert('定向密码不正确！',{title:false,closeBtn:0});
	}
}