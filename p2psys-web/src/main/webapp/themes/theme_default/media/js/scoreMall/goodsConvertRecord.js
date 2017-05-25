define(function(require,exports,module){
  require('jquery');
  ajaxUrl('?')
  function ajaxUrl(param)
  {
  $.ajax({
  	type:'get',
  	url:'/scoreMall/recordList.html'+param,
  	dataType:'json',
  	success:function(json){
  		require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js',function(){
        require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
          var tpl = require('../../tpl/scoreMall/goodsConvertRecord.tpl');
          var template = Handlebars.compile(tpl);
          var html = template(json);
          $("#convert_record_content").html(html);
        })
  		});

  		if(json.data.page.pages > 0)
  		{
  			require.async(['/plugins/pager/pager.css','/plugins/pager/pager'],function(){
							kkpager.generPageHtml({
								pno : json.data.page.currentPage,//当前页码
								total : json.data.page.pages,//总页码
								totalRecords : json.data.page.total,//总数据条数
								isShowFirstPageBtn	: false, 
								isShowLastPageBtn	: false, 
								isShowTotalPage 	: false, 
								isShowTotalRecords 	: false, 
								isGoPage 			: false,
								lang:{
									prePageText				: '<',
									nextPageText			: '>'
								},
								mode:'click',
								click:function(n){
						        	$.ajax({
						        		type:'get',
						        		url:'/scoreMall/recordList.html'+param,
						        		dataType:'json',
						        		success:function(json){
						        			require.async(['/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js','/plugins/handlebars-v1.3.0/transFormatJson'],function(){
    												var tpl = require('../../tpl/scoreMall/goodsConvertRecord.tpl');
    												var template = Handlebars.compile(tpl);
    												var html    = template(json);
    												$('#convert_record_content').html(html);
    											});
						        		}
						        	});
									this.selectPage(n); //处理完后可以手动条用selectPage进行页码选中切换
								}
							});
						});
  		}
  		else
  		{
        $("#kkpager").html('暂无数据');
  		}
  	}
  });   
  }

  $(".searchBtn").click(function(){
    var startTimeVal = $('input[name="startTime"]').val();
    var endTimeVal = $('input[name="endTime"]').val();
    var nameVal = $('input[name="name"]').val();
    if(nameVal != '')
    {
      var search = '?name='+nameVal;
    }
    else
    {
      var search = '?name=-1';
    }
    if(startTimeVal != '')
    {
      search += '&startTime='+startTimeVal;
    }
    if(endTimeVal != '')
    {
      search += '&endTime='+endTimeVal;
    }
    ajaxUrl(search);
  });

  $.ajax({
    type:'get',
    url:'/scoreMall/convertRecordList.html',
    dataType:'json',
    success:function(json){
      require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0.js',function(){
        require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
      var tpl = require('../../tpl/scoreMall/goodsDetails.tpl');
      var template = Handlebars.compile(tpl);
      var html = template(json);
      $("#convert_dynamic_ul").html(html);
      require.async('common',function(){
        $("#convert_dynamic_ul").slideFn();
      })
       });
    });
    }
  });
});