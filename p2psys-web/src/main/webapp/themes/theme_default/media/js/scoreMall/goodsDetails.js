define(function(require,exports,module){
  require('jquery');
  //图片放大镜插件
  require.async(['/plugins/etalage/jquery.etalage.min','/plugins/etalage/etalage.css'],function(){
    require.async('scoreMall/etalage');//调用JS
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

//S-购买数量判断
//max购买数量最大值
var max = Math.floor(parseInt($("#userScore").text()) / parseInt($("#needScore").text()));
  if(parseInt($("#remainCount").text()) > 0 && max > 0)
  {
    $("#convertNumbers").val('1');
  }
  else
  {
    if(max <= 0)
    {
      $(".convertBtn").html('积分不足');
    }
    if(parseInt($("#remainCount").text())<=0)
    {
      $(".convertBtn").html('数量不足');
    }
    $("#convertNumbers").val('0').attr('disabled','disabled');
    $(".convertBtn").addClass("disabled");
  }
  $(".reduce").click(function(){
    var value = parseInt($("#convertNumbers").val());
    if(value > 1)
    {
      value = value -1;
      $("#convertNumbers").val(value);
    }
  });
  $(".add").click(function(){
    var value = parseInt($("#convertNumbers").val());
   
    if(value < max )
    {
      value = value + 1;
      $("#convertNumbers").val(value);
    }
    
  });
  $("#convertNumbers").keyup(function(){
    var max = parseInt($("#remainCount").text());
     var value = parseInt($("#convertNumbers").val());
     if(value > max)
     {
      $(this).val(max)
     }
  });

  $(".convertBtn").click(function(){
    if(!$(this).hasClass('disabled'))
    {
      $('input[name="score"]').val(parseInt($("#convertNumbers").val())*parseInt($("#needScore").text())); //传给程序商品的兑换积分
      $('input[name="convertNum"]').val(parseInt($("#convertNumbers").val()));//传给程序商品的兑换数量
        require.async('/plugins/layer-v1.8.4/layer.min.js',function(){
          var popUp = $.layer({
                    type: 1,   //0-4的选择,
                    title: false,
                    closeBtn: [0],
                    border:false,
                    shadeClose: true,
                    area: ['auto', 'auto'],
                    page: {
                      dom: '#receiveForm'
                    },
                    success:function(){
                        require.async("validate/validate",function(){
                          require.async("validate/addtional-method",function(){
                             $("#receiveForm").validate({
                                  rules:{
                                    receiveUserName:{
                                      required:true
                                    },
                                    receiveAddress:{
                                      required:true
                                    },
                                    receivePhone:{
                                      required:true
                                    }
                                  },
                                  messages:{
                                    receiveUserName:{
                                      required:'请输入收件人'
                                    },
                                    receiveAddress:{
                                      required:'请填写详细地址'
                                    },
                                    receivePhone:{
                                      required:'请填写电话号码'
                                    }
                                  },
                                  errorElement:'em',
                                  submitHandler:function(form){
                                    require.async('jquery.form',function(){
                                      $(form).ajaxSubmit({
                                        beforeSubmit:function(){},
                                        success:function(){},
                                        datatype:'json'
                                      });
                                    });
                                  }

                             });
                          });
                      });  
                    }
                });

          $(".receiveMsg .close").click(function(){
              layer.close(popUp);
          });
          });
      }
  })
});