define(function(require,exports,module){
	require('jquery');
	
	//证件列表
	$.ajax({
		url:"/member/usercertification/fileType.html",
		type:"get",
		dataType:"json",
		success:function(data){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
				require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
					var tpl = require('/themes/theme_default/media/tpl/member/usercertification_fileType.tpl');//载入tpl模板
					var template = Handlebars.compile(tpl);
					var html = template(data);
					$(".user_filesList_tab").html(html);
					//默认选中第一个标签
					$(".user_filesList_tab li").eq(0).addClass("hover");
					getPicList($(".user_filesList_tab  .hover").attr("data-id"));
								
					//标签切换
					$(".user_filesList_tab li").click(function(){
						$(this).addClass("hover").siblings().removeClass("hover");
						var fileType = $(this).attr("data-id");
						getPicList(fileType);
					});
					
				})
			})
		}
	})
	//获取图片列表
	function getPicList(id){
		$.ajax({
			url:"/member/usercertification/filesList.html?tid="+id+'&randomTime=' + (new Date()).getTime(),
			type:"get",
			dataType:"json",
			success:function(data){
				require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
					require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
						var picTpl = require('/themes/theme_default/media/tpl/member/usercertification_fliePicList.tpl');//载入tpl模板
						var template = Handlebars.compile(picTpl);
						var html = template(data);
						$(".user_filesList_content ul").html(html);
					})
				})
			}
		})
	}
	
	
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
	              fileSizeLimit:'500',
	              widht:98,
	              height:40,
	              // uploadLimit:1000,
	              swf:'/themes/theme_default/media/js/uploadify/uploadify.swf',
	              uploader:"/borrow/uploadBorrowPic.html"+"?random="+(new Date()).getTime(),
	              onSelectError: function(file,errorCode,errorMsg){
	                    if(errorCode==-110){
	                    	layer.msg('上传的单张图片不能超过500K，请重新上传', 1, -1);
	                    	return false;
	                    }
	                    // else if(errorCode==-100){
	                    // 	layer.msg('上传的图片最多不能超过10张', 2, -1);
	                    // 	return false;
	                    // }
	                    else if(errorCode==-130){
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
						      html: '<div class="tipsWrap w450"><div class="tipsTxt"><i class="iconfont infoIco">&#xe614;</i><span>资料上传后，在审核完成前不可修改，确定上传？</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn confirmOk">确定</a><a href="javascript:;" class="cancleBtn confirmCancle">取消</a></div></div>'
						  }
					});
				}
				//确认提交操作
				$(".confirmOk").click(function(){
						require.async('jquery.form',function(){
							 $("#picForm").ajaxSubmit({
								url:'/member/usercertification/certificationApply.html',
								type:"post",
			   					dataType:'json',
			   					data:{typeId : $(".user_filesList_tab  .hover").attr("data-id")},
			   					success:function(data){
			   						if(data.result){
										layer.closeAll();
										getPicList($(".user_filesList_tab  .hover").attr("data-id"));	
									}else{
										var closeLayer = $.layer({
											type: 1,
										    closeBtn: [0,true],
							                title: "&nbsp;",
										    area: ['384px', '186px'],
										    border: [1, 1, '#cecfd0'],
										    page: {
										        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont errIco">&#xe63e;</i><span>'+data.msg+'</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn failBtn">确定</a></div></div>'
										    },
										    close: function(index){
										    	layer.close(closeLayer);
										    }
										});
										$(".failBtn").click(function(){
											layer.close(closeLayer);
										});
									}
			   					}
			   				});				
						 })

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
		       url : "/member/usercertification/deletePic.html",
		       data : {pathPic : $(this).prev().attr("src")},
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
			        html: '<div class="tipsWrap w450"><div class="tipsTxt"><i class="iconfont tipIco">&#xe63c;</i><span>确定删除证明材料？</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn">确认</a><a href="javascript:;" class="cancleBtn">取消</a></div></div>'
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