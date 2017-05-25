$(function(){
	var id = $('#id').val();
	var params = 'id='+id;
	
	$.ajax({
		url:'/cf/wechat/p/detail.action',
		type:'POST',
		data:params,
		dataType:'json',
		success:function(res){
			var data = {};
			data.project = res.data.project;
			$('.back-left').bind('click',function(){
				location.href = '/cf/wechat/pro/index.html?id='+data.project.type;
			})
			data.leaderText = res.data.leaderText;
			data.project.proportion = parseFloat((res.data.project.wannaAccount/1.05)/parseFloat(data.project.companyMoney+(res.data.project.wannaAccount/1.05))).toFixed(2)*100;
			data.project.proportion = parseFloat(data.project.proportion).toFixed(2);
			if(data.project.id=="40"){
				data.project.companyMoney = "1000000000";
			}else if(data.project.id=="41"){
				data.project.companyMoney = "42000000";
				data.project.proportion = "7.5";
			}else if(data.project.id=="43"){
				data.project.companyMoney = "3000000";
				data.project.proportion = "83.33";
			}else{
				data.project.companyMoney = parseFloat(res.data.project.companyMoney+res.data.project.wannaAccount/1.05).toFixed(2);
			}
			data.attention = res.data.attention;
			data.orders = res.data.order;
			data.orderStatus = 2;
			data.address = getLength(res.data.project.address);
			data.leaderStatus = 0;
			data.attentionText = '关注';
			data.attentionCode = 0;
			data.step = res.data.project.account/res.data.project.wannaAccount*100;
			data.timeStatus = res.data.project.timeStatus;
			if(data.timeStatus ==4 || data.timeStatus ==3){
				$("#leader").hide();
			}
			data.attentionCode = res.data.attention;
			if(1==res.data.attention){
				data.attentionText = '已关注';
				data.attentionStyle = 'rights-right-btn_ban';
			}else{
				data.attentionStyle = 'rights-right-btn';
			}
			//存在领投人
			if(null!=res.data.project.leader&&null!=res.data.order){
				data.leaderStatus = 2;
			}
			if(null!=res.data.project.leader&&null==res.data.order){
				data.leaderStatus = 1;
			}
			//不需要领投人框
			if(2!=res.data.project.type){
				data.leaderStatus = 3;
			}
			data.detailImg = '';
			data.detailActive = '';
			if(null!=res.data.project.materialsList){
				var size = res.data.project.materialsList.length;
				for(var i in res.data.project.materialsList){
					//项目详情图
					if('project_detail'==res.data.project.materialsList[i].materialCode){
						data.detailImg = adminUrl+res.data.project.materialsList[i].materialContent;
					}
					//项目动态图
					if('act_img'==res.data.project.materialsList[i].materialCode){
						data.detailActive += '<img src='+adminUrl+res.data.project.materialsList[i].materialContent+' class="detail-img" >';
					}
					//项目动态文字
					if('act_code'==res.data.project.materialsList[i].materialCode){
						data.detailActive += '<p class="act-p" style="line-height:24px;font-size:16px;" >'+res.data.project.materialsList[i].materialContent+'</p>';
					}else{
						if(i== size-1){
							data.detailActive += '<p class="act-p" style="line-height:24px;font-size:16px;" >&nbsp;&nbsp;该项目暂时未更新状态</p>';
						}
					}
				}
			}
			if(null==data.orders||''==data.orders){
				if(4==data.project.timeStatus){
					data.orderStatus = -1;
				}else{
					data.orderStatus = 0;
				}
			}
			$('#actDiv').html(data.detailActive);
			//创建模型
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
 * 判断字符串长度
 * @param str
 */
function getLength(str){
	var array = {};
	if(str.length>20){
		array.l = 1;
		array.d = str.substr(0,20);
	}else{
		array.l = 0;
		array.d = str;
	}
	return array;
}

/**
 * 关注项目
 * @param code
 */
var attentionStatus = false;
function addAttention(code){
	if(1!=code){
		var id = $('#id').val();
		var params = 'id='+id;
		if(!attentionStatus){
			attentionStatus = true;
			$.ajax({
				url:'/cf/pro/addAttention.action',
				type:'POST',
				data:params,
				dataType:'json',
				success:function(res){
					attentionStatus = false;
					if('success'==res.result){
						layer.alert('关注成功！',{title:false,closeBtn: 0},function(){
							location.reload();
						});
					}else{
						layer.alert(res.errorMsg,{title:false,closeBtn: 0});
					}
				}
			})
		}else{
			layer.alert('请勿重复提交哦！',{title:false,closeBtn: 0});
		}
	}
}

/**
 * 申请成为领投人
 */
function beLeader(){
	var id = $('#id').val();
	location.href = '/cf/wechat/user/beLeader.html?projectId='+id;
}