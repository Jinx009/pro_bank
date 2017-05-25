$(function(){
	var id = $('#orderId').val();
	$.ajax({
		url:'/cf/wechat/user/orderDetail.action?id='+id,
		type:'GET',
		dataType:'json',
		success:function(res){
			if(200==res.code){
				var data = res.data;
				if(null==data.address){
					data.address = '';
				}
				if(null==data.mobilePhone){
					data.mobilePhone = '';
				}
				if(null==data.postNum){
					data.postNum = '';
				}
				if(null==data.realName){
					data.realName = '';
				}
				var model =new Vue({
	    	        el: 'body',
	    	        data: {
	    	            data:data
	    	        }
	    	    });
	    		
			}
		}
	})
})

/**
 * 保存收货地址
 */
function saveAddress(){
	var id = $('#orderId').val();
	var address = $('#address').val();
	var mobilePhone = $('#mobilePhone').val();
	var postNum = $('#postNum').val();
	var realName = $('#realName').val();
	var params = 'id='+id+'&realName='+realName+'&postNum='+postNum+'&mobilePhone='+mobilePhone+'&address='+address;
	$.ajax({
		url:'/cf/wechat/user/saveAddress.action',
		type:'POST',
		data:params,
		dataType:'json',
		success:function(res){
			if(200==res.code){
				layer.alert('保存成功！',{title:false,closeBtn: 0},function(){
					location.reload();
				});
			}
		}
	})
}