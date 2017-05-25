define(function(require,exports,module){
	require('jquery');
	$("#private_appointment").click(function(){
		//通过ajax动态判断是否选择了预约时间
	require.async('jquery.form',function(){
	     var id = $(".idval").val();
	     if($("#startTime").val() =="" && $("#endTime").val() == ""){
	     	return false;
	     }
		 $("#appointment_time").ajaxSubmit({
                   type: "post",
                   url: "/lcschool/addAc.html?consultant_id="+id,
                   dataType: "json",
                  success:function(data){
					require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
					if(data.result==true){
						//构造确认框DOM
						window.location.href = "/wx/account/consultantsSuccess.html";
					}else{
						$.layer({
							type: 1,
							closeBtn: false,
							title: false,
							area: ['460px', '190px'],
							shade: [0.5, '#000'],
							border: [10, 0.2, '#545454'],
							page: {
								html: '<div class="tipsWrap w450"><div class="tipsTxt"><i class="iconfont tipIco">&#xe63c;</i><span>'+data.msg+'</span></div><div class="tipsBtnBar"><a href="javascript:location.reload();" class="okBtn">继续预约</a></div></div>'
							}
						});
					}
				});
			}
                   
		});
	});
});
});