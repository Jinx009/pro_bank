define(function(require,exports,module){
	require('jquery');
//加载城市选择插件
  require.async('jquery-citySelect/jquery.cityselect',function(){
    var prov = $(".province").val();
    var city =$(".cityOpt").val();
    var dist =$(".dist").val();
    $("#city").citySelect({
      url:"../../../themes/theme_default/media/js/jquery-citySelect/city.json",
      prov:"上海", //省份 
        city:"黄浦区", //城市 
        dist:"", //区县 
        required:true,
        nodata:"none" //当子集无数据时，隐藏select  
      });
  })
 
  $(".addFile").live('click',function(){
    require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
      $.layer({
          type: 1,
          closeBtn: [0, true],
          title: '上传资料',
          area: ['550px', '450px'],
          border: [1, 1, '#cecfd0'],
          page : {
            html:'<div class="uploaderBox"><div class="uploadList"><form id="picForm"><ul class="filelist"></ul></form></div><div class="statusBar"><span class="float_right okBtn">确定</span><span class="float_right"><em id="files" name="file"></em></span></div></div>'
          }
      });
      
      //上传图片
        require.async(["uploadify/uploadify.css","uploadify/jquery.uploadify"],function(){
          $('#files').uploadify({
                fileObjName : 'file',
                buttonClass:'uploadBtn',
                buttonText:'选择图片',
                fileTypeDesc:'Image Files',
                fileTypeExts:'*.gif; *.jpg; *.png',
                fileSizeLimit:'1025',
                widht:98,
                height:40,
                uploadLimit:100,
                swf:'/themes/theme_default/media/js/uploadify/uploadify.swf',
                uploader:"/borrowBespeak/fileUpload.html"+"?random="+(new Date()).getTime(),
                onSelectError: function(file,errorCode,errorMsg){
                      if(errorCode==-110){
                        layer.msg('上传的图片不能超过1M，请重新上传', 1, -1);
                        return false;
                      }else if(errorCode==-130){
                        layer.msg('选择的文件类型跟设置的不匹配', 2, -1);
                        return false;
                      }
                },
                onUploadSuccess:function(file,data, response) {
                  data = typeof data =="object"?data:$.parseJSON(data);
                  if(data.picPath!=null){
                    $('.filelist').append("<li><a href='"+data.picPath+"' class='fileshow'><img src='"+data.picPath+"' width='110px' height='110px' /></a><i class='iconfont delImg' title='点击删除图片'>&#xe60f;</i><input  type='hidden' name='picPath' value='"+data.picPath+"'/></li>");
                  }
                }
                
          });
        })
        //确定操作
      $(".okBtn").click(function(){
        if($(".filelist li").length<1){
          layer.closeAll();
        }else{
          var okLayer = $.layer({
            type: 1,
              closeBtn: [0,true],
                    title: "&nbsp;",
              area: ['450px', '186px'],
              border: [1, 1, '#cecfd0'],
            page: {
                  html: '<div class="tipsWrap w450"><div class="tipsTxt"><i class="iconfont infoIco">&#xe614;</i><span>确定上传？</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn confirmOk">确定</a><a href="javascript:;" class="cancleBtn confirmCancle">取消</a></div></div>'
              }
          });
        }
        //确认提交操作
        $(".confirmOk").click(function(){
            var picForm = $("#picForm .filelist").html();
            $(".user_filesListshow ul.tp-img").append(picForm);
            layer.closeAll();
        });
        //取消提交操作
        $(".confirmCancle").click(function(){
          layer.close(okLayer);
        });
      });
        
      //删除图片
      $(".delImg").live("click",function(){
        var _that = $(this);
         $.ajax({
           url : "/borrowBespeak/deletePic.html",
           data : {pathPic : $(this).prev().attr("href")},
           success:function(data){
              if(data.result){
                _that.parents("li").remove();
              }
           }
         })
      })
    })
  })
  
  
  //删除图片
  $(".user_filesList_content").delegate(".fileclose","click",function(){
    var idVal=$(this).attr("data-id");
    var current = $(this);
    require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
      $.layer({
        type: 1,
          closeBtn: [0,true],
                title: "&nbsp;",
          area: ['450px', '190px'],
          border: [1, 1, '#cecfd0'],
          page: {
              html: '<div class="tipsWrap w450"><div class="tipsTxt"><i class="iconfont tipIco">&#xe63c;</i><span>确定解除绑定这张银行卡？</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn">确认</a><a href="javascript:;" class="cancleBtn">取消</a></div></div>'
          }
      }); 
      //确认操作
      $(".okBtn").click(function(){
        $.ajax({
          url:"/member/usercertification/fileDel.html?id="+idVal,
          dataType:"json",
          success: function(data){
            if(data.result){
              current.parent("li").remove();
              layer.closeAll();
            }else{
              $.layer({
                type: 1,
                  closeBtn: [0,true],
                        title: "&nbsp;",
                  area: ['384px', '186px'],
                  border: [1, 1, '#cecfd0'],
                  page: {
                      html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont errIco">&#xe63e;</i><span>系统错误</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn failBtn">确定</a></div></div>'
                  },
                  close: function(index){
                    layer.closeAll();
                  }
              });
              $(".failBtn").click(function(){
                layer.closeAll();
              });
            }           
          }     
        });       
      });
      //删除操作
      $(".closeBtn").click(function(){
        layer.closeAll();
      });
    }); 
  });
  
  //预览图片
  require.async(['/plugins/fancybox/jquery.fancybox.css','/plugins/fancybox/jquery.fancybox.pack'],function(){
    $(".fileshow").fancybox();
  });

      });



        