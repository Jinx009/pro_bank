$(function(){
	var id = $('#orderId').val();
	var params = 'id='+id;
	$.ajax({
		url:'/cf/user/orderDetail.action',
		type:'POST',
		data:params,
		dataType:'json',
		success:function(res){
			if('success'==res.result){
				if(null!=res.errorMsg.realName&&''!=res.errorMsg.realName  ){
					$('#realName').val(res.errorMsg.realName);
					$('#realName').attr('readonly',true);
					$('#address').attr('readonly',true);
					$('#postNum').attr('readonly',true);
					$('#mobilePhone').attr('readonly',true);
					$('#backBtn').css('display','block');
					$('#successBtn').css('display','none');
				}
				if(null!=res.errorMsg.address&&""!=res.errorMsg.address){
					$('#address').val(res.errorMsg.address);
				}
				if(null!=res.errorMsg.postNum&&""!=res.errorMsg.postNum){
					$('#postNum').val(res.errorMsg.postNum);
				}
				if(null!=res.errorMsg.mobilePhone&&""!=res.errorMsg.mobilePhone){
					$('#mobilePhone').val(res.errorMsg.mobilePhone);
				}
			}
		}
	})
})

/**
 * 保存地址
 */
function saveAddress(){
	var id = $('#orderId').val();
	var realName = $('#realName').val();
	var address = $('#address').val();
	var postNum = $('#postNum').val();
	var mobilePhone = $('#mobilePhone').val();
	var params = 'realName='+realName+'&address='+address+'&postNum='+postNum+'&mobilePhone='+mobilePhone+'&id='+id;
	
	$.ajax({
		url:'/cf/order/addressInfo.html',
		type:'POST',
		data:params,
		dataType:'json',
		success:function(res){
			if("success"==res.result){
				layer.alert('地址添加成功！',{title:false,closeBtn: 0},function(){
					goReload();
				});
			}
		}
	})
	
}

/**
 * 页面刷新
 */
function goReload(){
	location.reload();
}


/**
 * 返回
 */
function goBack(){
	location.href = '/cf/user/buy-list.html';
}