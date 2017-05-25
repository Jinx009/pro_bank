$(function(){
	$.ajax({
		url:'/cf/wechat/pro/attentionData.action',
		type:'GET',
		dataType:'json',
		success:function(res){
			var data = res.data;
			for(var i in data){
			    if(null!=data[i].materials){
			    	data[i].img = '/themes/theme_default/cf/wechat/img/attention_img.png';
			    	for(var j in data[i].materials){
			    		if('project_img'== data[i].materials[j].materialCode){
			    			data[i].img = adminUrl+data[i].materials[j].materialContent;
			    		}
			    	}
			    }
			}
			//创建模型
            var model = new Vue({
                el: '#content',
                data:{
                    items: data
                }
            });
		}
	})
})