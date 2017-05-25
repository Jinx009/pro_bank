$(function(){
	loadForms();
	var id = $('#id').val();
	$.ajax({
		url:'/manage/materials.action?id='+id,
		type:'GET',
		dataType:'json',
		success:function(res){
			var data = {};
			data.project_img = '';
			data.project_detail = '';
			data.company_logo = '';
			data.detail_img = '';
			data.wechat_code = '';
			data.business_book = '';
			data.project_img1 = '';
			data.project_detail1 = '';
			data.company_logo1 = '';
			data.detail_img1 = '';
			data.wechat_code1 = '';
			data.business_book1 = '';
			data.projectName = res.projectName;
			data.id = res.id;
			data.url1 = '/manage/code/editPro.html?id='+res.id;
			data.url2 = '/manage/code/materials.html?id='+res.id;
			data.url3 = '/manage/code/profit.html?id='+res.id;
			data.url4 = '/manage/code/show.html?id='+res.id;
			if(null!=res.data&&''!=res.data){
				for(var i=0;i<res.data.length;i++){
					if('project_img'==res.data[i].materialCode){
						data.project_img = adminUrl+res.data[i].materialContent;
						data.project_img1 = res.data[i].materialContent;
					}
					if('project_detail'==res.data[i].materialCode){
						data.project_detail = adminUrl+res.data[i].materialContent;
						data.project_detail1 = res.data[i].materialContent;
					}
					if('company_logo'==res.data[i].materialCode){
						data.company_logo = adminUrl+res.data[i].materialContent;
						data.company_logo1 = res.data[i].materialContent;
					}
					if('detail_img'==res.data[i].materialCode){
						data.detail_img = adminUrl+res.data[i].materialContent;
						data.detail_img1 = res.data[i].materialContent;
					}
					if('wechat_code'==res.data[i].materialCode){
						data.wechat_code = adminUrl+res.data[i].materialContent;
						data.wechat_code1 = res.data[i].materialContent;
					}
					if('business_book'==res.data[i].materialCode){
						data.business_book = adminUrl+res.data[i].materialContent;
						data.business_book1 = res.data[i].materialContent;
					}
				}
			}
			var model = new Vue({
                el: 'body',
                data:{
                    item: data,
                }
            });
		}
	})
})
/**
 * 触发选择图片
 */
function getSub(cla){
	$('.'+cla).click();
}

/**
 * 触发表单提交
 */
function subForm(id){
	index = id.split('form')[1];
	$('#'+id).submit();
}
var index = 1;
/**
 * ajax form表单加载
 */
function loadForms(){
	for(var i = 1;i<=6;i++){
		$('#form'+i).ajaxForm({
			success : function(data) {
				if(null!=data.data&&''!=data.data){
					if(6==parseInt(index)){
						$('#a6').attr('href',adminUrl+data.data);
						$('#a6').html('点击此处查看PDF');
						$('#path6').val(data.data);
					}else{
						$('#img'+index).attr('src',adminUrl+data.data);
						$('#path'+index).val(data.data);
					}
				}
			},
			complete : function(t) {
			}
		});
	}
}

/**
 * 数据保存
 */
function sendData(){
	var projectId = $('#id').val();
	var projectImg = $('#path1').val();
	var projectDetail = $('#path2').val();
	var companyLogo = $('#path3').val();
	var detailImg = $('#path4').val();
	var wechatCode = $('#path5').val();
	var businessBook = $('#path6').val();
	if(''==projectImg||null==projectImg){
		layer.alert('产品头图未上传！',{title:false,closeBtn: 0});
	}else if(''==projectDetail||null==projectDetail){
		layer.alert('产品详情图未上传！',{title:false,closeBtn: 0});
	}else if(''==companyLogo||null==companyLogo){
		layer.alert('公司logo或个人头像未上传！',{title:false,closeBtn: 0});
	}else if(''==detailImg||null==detailImg){
		layer.alert('产品详情页头图未上传！',{title:false,closeBtn: 0});
	}else if(''==wechatCode||null==wechatCode){
		layer.alert('微信群聊二维码未上传！',{title:false,closeBtn: 0});
	}else if(''==businessBook||null==businessBook){
		layer.alert('商业计划书未上传！',{title:false,closeBtn: 0});
	}else{
		var index = layer.load();
		var json = {};
		json.data = new Array();
		json.projectId = projectId;
		var obj = {};
		obj.materials_code = 'project_img';
		obj.materials_content = projectImg;
		obj.materials_name = '头图';
		obj.materials_type = 1;
		json.data.push(obj);
		obj = {};
		obj.materials_code = 'project_detail';
		obj.materials_content = projectDetail;
		obj.materials_name = '产品详情';
		obj.materials_type = 1;
		json.data.push(obj);
		obj = {};
		obj.materials_code = 'company_logo';
		obj.materials_content = companyLogo;
		obj.materials_name = '公司logo';
		obj.materials_type = 1;
		json.data.push(obj);
		obj = {};
		obj.materials_code = 'detail_img';
		obj.materials_content = detailImg;
		obj.materials_name = '产品详情头图';
		obj.materials_type = 1;
		json.data.push(obj);
		obj = {};
		obj.materials_code = 'wechat_code';
		obj.materials_content = wechatCode;
		obj.materials_name = '微信群聊二维码';
		obj.materials_type = 1;
		json.data.push(obj);
		obj = {};
		obj.materials_code = 'business_book';
		obj.materials_content = businessBook;
		obj.materials_name = '商业计划书';
		obj.materials_type = 3;
		json.data.push(obj);
		var params = JSON.stringify(json);
		console.log(params)
		$.ajax({
			url:'/manage/code/saveOrUpdateMaterials.action',
			type:'POST',
			data:'data='+params,
			dataType:'json',
			success:function(res){
				if(200==res.code){
					layer.close(index); 
					layer.alert('素材提交成功！',{title:false,closeBtn: 0},function(){
						location.href = '/manage/code/profit.html?id='+projectId;
					});
				}else{
					layer.alert(res.data,{title:false,closeBtn:0});
				}
			}
		})
	}
}