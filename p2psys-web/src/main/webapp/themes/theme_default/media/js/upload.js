var cropX,cropY,cropW,cropH;
function crop(){
		$(".user_help").html("请上传你网站的头像").css("color","#c09853")
		var boundx=100;boundy=100;
		$("#preview").css({"height":"auto","width":"auto"});
		cropObj=$("#target").Jcrop({
			setSelect:[0,0,80,80],
			onChange: updatePreview,
			onSelect: updatePreview,
			aspectRatio: 1,
			minSize:[30,30]
		  },function(){
			var bounds = this.getBounds();
			boundx = bounds[0];
			boundy = bounds[1];
			jcrop_api = this;
		  });
		  
		  function updatePreview(c)
		  {
			if (parseInt(c.w) > 0)
			{
			  var rx = 100 / c.w;
			  var ry = 100 / c.h;
			  cropX=c.x;
			  cropY=c.y;
			  cropW=c.w;
			  cropH=c.h;
			 
			  $('#preview').css({
				width: Math.round(rx * boundx) + 'px',
				height: Math.round(ry * boundy) + 'px',
				marginLeft: '-' + Math.round(rx * c.x) + 'px',
				marginTop: '-' + Math.round(ry * c.y) + 'px'
			  });
			}
			
		  };
		  
		  function boxborder(){
				return [0,0,100,100];
		  }
}

$("#btn_Save").click(function(){
	$.post("/member/info/setFace.html",
			{
				cropX:cropX,
				cropY:cropY,
				cropW:cropW,
				cropH:cropH
			},
			function(data,textStatus){
				if(data){
					$("#cropview").hide();
					var src=$("#userImg").attr("src")+"&t="+Math.random();
					$("#userImg").attr("src",src);
					$("#user_menu_img").attr("src",src+"&time="+Math.random());
					$(".user_help").html("头像保存成功").css("color","#000")
				}
			});
	
});
