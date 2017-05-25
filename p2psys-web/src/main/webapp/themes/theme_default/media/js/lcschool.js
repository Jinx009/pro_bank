define(function(require,exports,module){
	require('jquery');
	$(function(){
		var oli = $(".lcschool-banner ul li")
		oli.each(function(){
		   $(this).click(function(){
		   	 oli.removeClass("active")
		   	 $(this).addClass("active")
		   })
		});
  	});


    //理财商学院
    $.ajax({
		url:"/lcschool/showFinanceSiteList.html?random="+Math.random(),
		type:"get",
		dataType:"json",
		success:function(data){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					var tpl = require('/themes/theme_default/media/tpl/lcschool.tpl');//载入tpl模板
					var template = Handlebars.compile(tpl);
					var html = template(data);
					$(".lcschool-banner").after(html);
           $(function(){
             var oul = $(".lcschool .talk-people");
             var oullength = oul.length;
              for(var i=0;i<oullength;i++){
               var ab = "talk-people"+i;
               oul.eq(i).attr("id",ab)
              };
           })
				})
			})
		}
	}) 

     $.ajax({
    url:"/lcschool/showFinanceSiteList.html?random="+Math.random(),
    type:"get",
    dataType:"json",
    success:function(data){
      require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
        require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
          var tpl = require('/themes/theme_default/media/tpl/schooltab.tpl');//载入tpl模板
          var template = Handlebars.compile(tpl);
          var html = template(data);
          $("#schooltab").html(html);
           $(function(){
             var oli = $("#schooltab li");
             var olilength = oli.length;
              for(var i=0;i<olilength;i++){
               var ab = "#talk-people"+i;
               oli.eq(i).find("a").attr("href",ab)
              };
              oli.eq(0).addClass("active")
           })

        })
      })
    }
  })

})

