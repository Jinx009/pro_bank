$(function(){
	getData();
})
/**
 * 获取数据
 */
function getData(){
	var projectId = $('#id').val();
	$.ajax({
		url:'/manage/code/profitRules.action?projectId='+projectId,
		type:'GET',
		dataType:'json',
		success:function(res){
			if(200==res.code){
				var data = {};
				data.data = res;
				data.id = projectId;
				data.url1 = '/manage/code/editPro.html?id='+data.id;
				data.url2 = '/manage/code/materials.html?id='+data.id;
				data.url3 = '/manage/code/profit.html?id='+data.id;
				data.url4 = '/manage/code/show.html?id='+data.id;
				var model = new Vue({
	                el: 'body',
	                data:{
	                    item:data,
	                }
	            });
			}else{
				layer.alert(res.data,{title:false,closeBtn:0});
			}
		}
	})
}
/**
 * 编辑收益规则
 * @param id
 */
function edit(id){
	$('#spanname'+id).hide();
	$('#spanmoney'+id).hide();
	$('#spanmaxInvestor'+id).hide();
	$('#spancontent'+id).hide();
	$('#isAccept'+id).attr('disabled',false);
	$('#inputname'+id).show();
	$('#inputmoney'+id).show();
	$('#inputmaxInvestor'+id).show();
	$('#inputcontent'+id).show();
	$('#saveBtn'+id).show();
	$('#editBtn'+id).hide();
}
/**
 * 执行修改
 * @param id
 */
function save(id){
	var money = $('#inputmoney'+id).val();
	var name = $('#inputname'+id).val();
	var maxInvestor = $('#inputmaxInvestor'+id).val();
	var content = $('#inputcontent'+id).val();
	var isAccept = $('#isAccept'+id).val();
	
	if(null==name||''==name){
		layer.alert('权益名称未填写！',{title:false,closeBtn:0});
	}else if(null==money||''==money){
		layer.alert('权益金额未填写！',{title:false,closeBnt:0});
	}else if(null==maxInvestor||''==maxInvestor){
		layer.alert('权益接受人数未填写！',{title:false,closeBtn:0});
	}else if(null==content||''==content){
		layer.alert('权益内容未填写！',{title:false,closeBnt:0});
	}else if(!checkNumber(maxInvestor)){
		layer.alert('权益接受人数格式不正确！',{title:false,closeBtn:0});
	}else if(!checkNumber(money)){
		layer.alert('权益金额格式不正确',{titlr:false,closeBtn:0});
	}
	else{
		var params = 'money='+money+'&name='+name+'&maxInvestor='+maxInvestor+'&content='+content+'&isAccept='+isAccept+'&id='+id;
		$.ajax({
			url:'/manage/code/editProfit.action',
			type:'POST',
			data:params,
			dataType:'json',
			success:function(res){
				if(200==res.code){
					$('#spanname'+id).html(name);
					$('#spanmoney'+id).html(money);
					$('#spanmaxInvestor'+id).html(maxInvestor);
					$('#spancontent'+id).html(content);
					$('#spanname'+id).show();
					$('#spanmoney'+id).show();
					$('#spanmaxInvestor'+id).show();
					$('#spancontent'+id).show();
					$('#isAccept'+id).attr('disabled',true);
					$('#inputname'+id).hide();
					$('#inputmoney'+id).hide();
					$('#inputmaxInvestor'+id).hide();
					$('#inputcontent'+id).hide();
					$('#saveBtn'+id).hide();
					$('#editBtn'+id).show();
					layer.alert('修改成功！',{title:false,closeBtn:0});
				}else{
					layer.alert(res.data,{title:false,closeBtn:0});
				}
			}
		})
	}
}

function del(id){
	layer.confirm('确定要删除该条记录？', {
	    btn: ['确定','取消'],title:false,closeBtn: 0 
	}, function(){
		$.ajax({
			url:'/manage/code/delProfit.action',
			data:'id='+id,
			dataType:'json',
			type:'POST',
			success:function(res){
				if(200==res.code){
					layer.closeAll();
					layer.alert('删除成功！',{title:false,closeBtn:0},function(){
						$('#tr'+id).remove();
						layer.closeAll();
					})
				}else{
					layer.closeAll();
					layer.alert(res.data,{title:false,closeBtn:0});
				}
			}
		})
	});
}
/**
 * 新建权益
 */
function newProfit(){
	var name = $('#name').val();
	var content = $('#content').val();
	var isAccept = $('#isAccept').val();
	var money = $('#money').val();
	var maxInvestor = $('#maxInvestor').val();
	var projectId = $('#id').val();
	
	if(!checkNumber(maxInvestor)){
		layer.alert('最大接受人数格式不正确！',{title:false,closeBtn:0});
	}else if(!checkNumber(money)){
		layer.alert('支持金额格式不正确！',{title:false,closeBtn:0});
	}else if(null==name||''==name){
		layer.alert('权益名称未填写！',{tutle:false,closeBtn:0});
	}else if(null==content||''==content){
		layer.alert('权益内容为填写！',{title:false,closeBtn:0});
	}else{
		var params = 'name='+name+'&money='+money+'&maxInvestor='+maxInvestor+'&content='+content+'&isAccept='+isAccept+'&id='+projectId;
		$.ajax({
			url:'/manage/code/saveProfit.action',
			data:params,
			dataType:'json',
			type:'POST',
			success:function(res){
				if(200==res.code){
					layer.alert('新增成功！',{title:false,closeBtn:0},function(){
						getData();
						layer.closeAll();
					})
				}else{
					layer.alert(res.data,{title:false,closeBtn:0});
				}
			}
		})
	}
}
/**
 * 校验数字
 * @param value
 */
function checkNumber(value){
	var re = /^[0-9]+.?[0-9]*$/;
	if(!re.test(value)){
		return false;
	}
	return true;
}