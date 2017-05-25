define(function(require,exports,module){
	require('jquery');
	var tpl = require('../../tpl/member/friend_userInvite.tpl');//载入tpl模板
	//初始化显示表格、搜索
	require.async('./showTable',function(showTable){
		showTable.ajaxUrl('/member/friend/userInviteJSON.html',tpl);
	});

	require.async("/plugins/ZeroClipboard/ZeroClipboard",function(){
		var clip = new ZeroClipboard( document.getElementById("d_clip_button"), {
		  moviePath: "/plugins/ZeroClipboard/ZeroClipboard.swf"
		} );
		clip.setText($("#fe_text").val());
		clip.glue($("#d_clip_button"));
		clip.addEventListener( "complete", function(){   alert("复制成功！");  });
	})

	require.async("/plugins/ZeroClipboard/ZeroClipboard",function(){
		var clip1 = new ZeroClipboard( document.getElementById("d_clip_button1"), {
		  moviePath: "/plugins/ZeroClipboard/ZeroClipboard.swf"
		} );
		clip1.setText($("#fe_text1").val());
		clip1.glue($("#d_clip_button1"));
		clip1.addEventListener( "complete", function(){   alert("复制成功！");  });
	})

});