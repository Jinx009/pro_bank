/**
 * 表单校验
 * @param step
 */
function checkStep(step){
	if(1==step){
		var projectName = $('#projectName').val();
		var type = $('#type').val();
		var info = $('#info').val();
		var wannaAccount = $('#wannaAccount').val();
		var breach = $('#breach').val();
		
		if(''==projectName||null==projectName){
			$('#projectName_error').html('*项目名称不能为空');
			return false;
		}else if(''==info||null==info){
			$('#info_error').html('*项目简介不能为空');
			return false;
		}else if(!checkNumber(wannaAccount)){
			$('#wannaAccount_error').html('*众筹金额格式不正确');
			return false;
		}else if(!checkNumber(breach)){
			$('#breach_error').html('*保障金比例格式不正确')
			return false;
		}
		return true;
	}else if(2==step){
		var minMoney = $('#minMoney').val();
		var maxMoney = $('#maxMoney').val();
		var creater = $('#creater').val();
		var company = $('#company').val();
		var address = $('#address').val();
		var companyMoney = $('#companyMoney').val();
		
		if(!checkNumber(minMoney)){
			$('#minMoney_error').html('*最小支持金额格式不正确');
			return false;
		}else if(!checkNumber(maxMoney)){
			$('#maxMoney_error').html('*最大支持金额格式不正确');
			return false;
		}else if(''==creater||null==creater){
			$('#creater_error').html('*发起人未填写');
			return false;
		}else if(''==company||null==company){
			$('#company_error').html('*发起人公司未填写');
			return false;
		}else if(''==address||null==address){
			$('#address_error').html('*发起人简介未填写');
			return false;
		}else if(!checkNumber(companyMoney)){
			$('#companyMoney_error').html('*公司估值格式不正确');
			return false;
		}else if(parseFloat(maxMoney)<parseFloat(minMoney)){
			$('#maxMoney_error').html('*最大支持金额必须大于最小支持金额');
			return false;
		}
		return true;
	}else if(3==step){
		var addAmount = $('#addAmount').val();
		var startTime = $('#startTime').val();
		var endTime = $('#endTime').val();
		
		if(!checkNumber(addAmount)){
			$('#addAmount_error').html('*金额增加规则格式不正确');
			return false;
		}else if(''==startTime||null==startTime){
			$('#startTime_error').html('*众筹开始时间未选择');
			return false;
		}else if(''==endTime||null==endTime){
			$('#endTime_error').html('*众筹结束时间未选择');
			return false;
		}
		return true;
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

/**
 * 发送数据
 */
function sendData(){
	var data = $('#sample-form').serialize();
	var index = layer.load();
	$.ajax({
		url:'/manage/code/addPro.action',
		type:'POST',
		data:data,
		dataType:'json',
		success:function(res){
			layer.close(index); 
			layer.alert('提交成功！',{title:false,closeBtn: 0},function(){
				location.href = '/manage/code/material.html?id='+res.data;
			});
		}
	})
}