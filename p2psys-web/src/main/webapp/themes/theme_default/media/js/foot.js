define(function(require,exports,module){
	require('jquery');
	$(function(){
		$("#jk").click(function(){
    	 $(".help-tab span").eq(1).trigger('click');
     })
  });
})

