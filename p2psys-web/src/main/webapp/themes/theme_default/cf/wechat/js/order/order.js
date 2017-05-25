/**
 *  购买记录加载
 */
var va;
//vue开发调试，到线上之前下面一行删掉
Vue.config.debug = true;
var xl_flag=0;
$(function(){
	 showData(0);
	 $('.fl').click(function(){
		 showData(0);
	 });
	 $('.gq').click(function(){
		 showData(2);
	 });
	 $('.sw').click(function(){
		 showData(1);
	 });
	 $('.gy').click(function(){
		 showData(4);
	 });
	 
});
function showData(type){
	 $.ajax({
	        url: '/cf/wehcat/user/buyListData.html',
	        type: 'GET',
	        dataType: 'JSON',
	        data: {type: type},
	    }).done(function(res) {
	    	if(xl_flag===0){
	    		xl_flag=1;
	    		for(var i in res.data){
	    			res.data[i].projectPic = adminUrl+res.data[i].projectPic;
	    		}
	    		va=new Vue({
	    	        el: '#buyList',
	    	        data: {
	    	            orders: res.data
	    	        }
	    	    });
	    		
	    		return;
	    	}
	    	va.orders=res.data;
	    	 
	    });
}

/**
 * 跳转至收货地址页面
 * @param id
 */
function goUrl(id){
	location.href = '/cf/wechat/user/address.html?id='+id;
}

/**
 * 撤销预约产品
 * @param id
 */
function doCacle(id){
	if(confirm('确定撤销？')){
		var params = 'id='+id;
		$.ajax({
			url:'/cf/wechat/user/caclePay.action',
			type:'POST',
			data:params,
			dataType:'json',
			success:function(res){
				if(200==res.code){
					layer.alert('撤销成功！',{title:false,closeBtn: 0},function(){
						location.reload();
					});
				}else{
					layer.alert(res.data,{title:false,closeBtn: 0});
				}
			}
		})
	}
}

/**
 * 付全款
 * @param id
 * @param money
 * @param payMoney
 */
function payAll(id,money,payMoney){
	console.log(money+'---'+payMoney)
	money = parseFloat(parseFloat(money)-parseFloat(payMoney)).toFixed(2);
	var params = 'id='+id;
	if(confirm('确定支付剩余款项：'+money+'元？')){
		$.ajax({
			url:'/cf/wechat/user/payAll.action',
			type:'POST',
			dataType:'json',
			data:params,
			success:function(res){
				if(200==res.code){
					layer.alert('支付成功！',{title:false,closeBtn: 0});
				}else{
					layer.alert(res.data,{title:false,closeBtn: 0});
				}
			}
		})
	}
}

function showProtocol(id){
	$.ajax({
		url:'/get/protocol.action',
		type:'GET',
		data:'id='+id+'&protocolCode=second_protocol',
		dataType:'json',
		success:function(res){
			layer.alert('微信端合同仅供预览，若要下载请前往电脑端！',{title:false,closeBtn:0},function(){
				layer.closeAll();
				$('#buyList').hide();
				$('#protocol').html(res.data);
				$('.protocolDiv').show();
			})
		}
	})
}

function hideProtocol(){
	$('#buyList').show();
	$('.protocolDiv').hide();
}