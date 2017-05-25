$(function(){
	var status = $('#status').val();
	var data = {};
	if(null!=status&&''!=status){
		data.status = parseInt(status);
	}else{
		data.status = 0;
	}
	//创建模型
    var model = new Vue({
        el: 'body',
        data:{
            data:data
        }
    });
})