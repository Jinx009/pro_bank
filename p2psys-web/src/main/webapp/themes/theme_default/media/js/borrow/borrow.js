define(function(require,exports,module){
	require('jquery');
      //信用标额度校验
      if($("#type").val()=="month"){
        $("#account").change(function(){
          $.post("checkAccount.html",{"account":$(this).val(),"borrowId":$("#borrowId").val()}, function(data){
             $("#check").html(data);
          });
        });
      }

    //分步校验
    function borrowStep(){
      var borrow_con1,borrow_con2,borrow_con3,nexBtn1,nextBtn2,upBtn1,upBtn2,//按钮对象，操作对象
      account,apr,title,valicode,most_account,lowest_account,errorMes,formStatusBase,formStatusMore,  //表单验证
      infoBox,applyInfo;//确认数据对象
      borrow_con1 = $(".J_borrow1");
      borrow_con2 = $(".J_borrow2");
      borrow_con3 = $(".J_borrow3");
      heightVal1 = borrow_con1.height();
      heightVal2 = borrow_con2.height();
      widthVal = borrow_con1.width();
      var $bid_process = $(".bid_process");
      var navLi = $bid_process.find("li");
      //右侧导航显示效果
      function $bid_processShow(i)
      {
          $(navLi[i]).addClass("hover").siblings().removeClass("hover");
      }
     
      //显示确认数据
      function confirmInfo(){
        var applyVal,applyTxt,str,iType,bTitle,dataItem;
        str = "";
        bTitle = $("#infotitle").val();
        infoBox = $(".J_final_msg");
        applyInfo = borrow_con1.find(".J_item_list");//单列数据
        applyTit = applyInfo.find(".item_tit");//每条借款信息的标题
        applyBox = applyInfo.find(".J_input");//每条借款信息的val对象
        applyBox.each(function(i){
          iType = $(applyBox[i]).attr("type");//判断表单元素的类型，(undefined和select-one为select)
          dataItem = $(applyBox[i]).attr("data-item")//数据单位（元，%）
          if(iType=="text")
          { 
            applyVal = $(applyBox[i]).val();
          }
          if(iType=="checkbox")
          {
            if($(applyBox[i]).prop("checked"))
            {
              applyVal="选中";
            }else{
              
              applyVal="未选";
            }
          }
          if(iType=="hidden")
          {
            applyVal="隐藏内容";
          } 
          if(!iType||iType=="select-one")
          {
            applyVal = $(applyBox[i]).find('option:selected').text();
            
          } 
          applyTxt = $(applyTit[i]).text();
          
          if(!dataItem){
            dataItem = "";
          }
          if(applyVal=="")
          {   
            str+="<li><b>"+applyTxt+"</b><span>未填写</span></li>"
          }else{
            str+="<li><b>"+applyTxt+"</b><span>"+applyVal+dataItem+"</span></li>"
          }
        });
        infoBox.html(str)//输出借款信息的内容
        //添加标题
          $(".J_b_tit").append(bTitle)
        //复制图片
        if($(".upimg").find("img").length > 0)
        {
            var upImgBox = $(".upimg").find("img").clone();
            $(".J_upimgbox").html(upImgBox);
        }
        else
        {
             $(".J_upimgbox").html("暂无图片");
        }
        
        
        //确认页面显示借款详情
        var tinyMceBox = $("#J_tinymceTxt");
        var tinyMceVal = $("#tinymceEditor").val();
        console.log(tinyMceVal);
        
        if(tinyMceVal==""){
          tinyMceBox.text("没有填写借款详情");
        }else{
          tinyMceBox.html(tinyMceVal);
        }
      }
      
      //分步发布
      nextBtn1 = $(".J_borrowbtn");
      nextBtn2 = $(".J_borrowbtn2");
      upBtn1 = $(".J_borrowbtn_up");
      upBtn2 = $(".J_borrowbtn_up2");
      nextBtn1.click(function(){
        formStatusBase = checkBaseInfo();
        if(formStatusBase == true){
          borrow_con1.hide();
          borrow_con2.show();
          $bid_processShow(1)
        }
      })
      nextBtn2.click(function(){
        formStatusMore = checkMoreInfo();
        if(formStatusMore == true){
          borrow_con2.hide();
          confirmInfo();//加载数据
          borrow_con3.show();
          $bid_processShow(2)
        }
      })
      upBtn1.click(function(){
        borrow_con1.show();
        borrow_con2.hide();
        $bid_processShow(0)
      })
      upBtn2.click(function(){
        borrow_con2.show();
        borrow_con3.hide();
        $bid_processShow(1)
      })
      //确认发标
      $("#form_submit").on("click",function(){
        require.async('jquery.form',function(){
        $("#form1").ajaxSubmit(function(data){
          if($.browser.msie){
            data =(new Function("","return "+data))();
          } 
          var _result = data.result;
          if(_result==true){
            window.location.href="/member_borrow/borrow/mine.html";
          }
          else
          {
            require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
            layer.alert(data.msg);
          });
            $(".valicode_img").each(function(){
                $(this).attr("src",'/validimg.html?t=' + Math.random());
            })
          }
        })
        });
      })
      //投标奖励
      var $award = $("select[name='award']"),
            $part_account = $("#part_account");
      $award.change(function(){
        if($(this).val() == 0)
        {
          $part_account.prop("disabled",true);
          $part_account.val('');
        }
        else
        {
          $part_account.prop("disabled",false);
        }
      });
      //定向标
      $("#isDXB").click(function(){
          if($('input[name="isDXB"]').is(":checked"))
          {
              $(this).removeClass("dayhover");
              $('#pwd').prop("disabled",true);
              $('input[name="isDXB"]').attr("checked",false);
              $('#pwd').val("");
              
          }
          else
          {
              $(this).addClass("dayhover");
              $('#pwd').prop("disabled",false);
              $('input[name="isDXB"]').attr("checked",true);
          }
      });
      
      //基础信息校验  
      function checkBaseInfo(){
        account = $("input[name='account']");
        apr = $("input[name='apr']");
        partAccount = $("input[name='partAccount']"); 
        funds = $("input[name='funds']");
        most_account = $("#most_account");
        lowest_account = $("#lowest_account");
        time_limit = $("#time_limit");
        account_val = account.val();
        apr_val = apr.val();
        partAccount_val = partAccount.val();
        funds_val = funds.val();
        most_account_val = parseInt(most_account.val());
        lowest_account_val = parseInt(lowest_account.val());
        time_limit_val = parseInt(time_limit.val());
        errorMes = "";
        
        if($.trim($("#check").html())!="")
        {
          errorMes += $.trim($("#check").html())  
        }
        if(account_val==""&&account_val<500)
        {
          errorMes += "借款金额不能为空<br/>" 
        }
        if(account_val<500&&account_val!="")
        {
          errorMes += "借款金额需要大于500元<br/>" 
        }
        if(account_val>5000000&&account_val!="")
        {
          errorMes += "借款金额需要小于500万元<br/>"  
        }
        if(apr_val=="")
        {
          errorMes += "借款利率不能为空<br/>" 
        }
        if(time_limit_val <= 6)            //修改"<" 为"<=". huochaobo 20140708
        {
          if((apr_val!=""&&apr_val>22.4)||(apr_val!=""&&apr_val<1)){
            errorMes += "借款利率为1%-22.4%<br/>"  
          }
        }
        else
        {
          if((apr_val!=""&&apr_val>24)||(apr_val!=""&&apr_val<1)){
            errorMes += "借款利率为1%-24%<br/>"  
          }
        }
      console.log(partAccount_val);
        if($award.val() == 1 && (partAccount_val < 0.1 || partAccount_val > 6))
        {
          errorMes += "请输入0.1%到6%的奖励值<br/>" ;
        }
        if(most_account_val!=0){
          if(most_account_val<lowest_account_val)
          {
            errorMes += "最多投标金额不能小于最低投标金额<br/>" 
          }
        }
        //流转标判断
        if(document.getElementById("typeStr").value == "flow"){
          var flowMoney = $("input[name='flowMoney']").val();
          if(flowMoney == ""){
            errorMes += "每份投标金额必填<br/>"
          }else
          if(flowMoney <= 0){
            errorMes += "每份投标金额需要大于零<br/>"
          }else
          if(account_val % flowMoney != 0){
            errorMes += "借款金额必须是每份金额的整数倍<br/>"
          }
        }
        if($('input[name="isDXB"]').is(":checked")){
          var dxbPwd=$('#pwd').val();
          if(dxbPwd==''){
            errorMes += "定向密码不能为空<br/>" 
          }
        }
        if(errorMes.length>0)
        {
           require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
            layer.alert(errorMes);
          });
          return false  
        }else{
          return true
        }
      }
      //详细信息校验
      function checkMoreInfo(){
        errorMes = "";
        var 
        title = $("input[name='name']"),
        $details = $("#tinymceEditor"),
        valicode = $("input[name='valicode']"),
        valicode_val = valicode.val(),
        title_val = title.val()
        if(title_val=="")
        {
          errorMes += "借款标题不能为空<br/>" 
        }
        if($details.val() == "")
        {
          errorMes += "借款详情不能为空<br/>"
        }
        if($(".upimg").find("img").length == 0)
        {
          errorMes += "请至少上传一张图片<br/>"
        }
        if(errorMes.length>0)
        {
          require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
           layer.alert(errorMes);
          });
          return false  
        }else{
          return true
        }
      }
    }
    borrowStep();
      $('#borrowTimeType').val(0);
      $('#time_limit_day_box').hide();
      $('#time_limit_box').show();
  
    //显示天标  按天borrowTimeType ==  1 ；按月borrowTimeType == 0；
        $('#changetoDay').click(function(){
          var borrowTimeType=$('#borrowTimeType').val();
          if(borrowTimeType==0)
          {
            $(this).addClass("dayhover")
            $('#borrowTimeType').val('1');
            $('#time_limit_day_box').show();
            $('#time_limit_day').addClass("J_input")
            $('#time_limit_box').hide()
            $('#time_limit').removeClass("J_input");
          }
          else
          {
            $(this).removeClass("dayhover")
            $('#borrowTimeType').val('0');
            $('#time_limit_day_box').hide()
            $('#time_limit_day').removeClass("J_input");
            $('#time_limit_box').show()
            $('#time_limit').addClass("J_input");
          }
          checkBorrowStyle(102,$('#borrowTimeType').val());
        });

        function checkBorrowStyle(btype,borrowTimeType)
        {
            var styleSel = $("#style");
            var style0 = '<option value="1">按月分期还款</option>';
            var style2 = '<option value="2">一次性还款</option>';
            var style3 = '<option value="3">每月还息到期还本</option>';
            var styleOpt;
            if(btype==101)//秒标
            {
              styleSel.html(style2);
            }
            else if(borrowTimeType==0)//月标
            {
              styleOpt += (style0+style2+style3);
              styleSel.html(styleOpt);
              if(btype==103 || btype==112)//固定收益类产品、担保标
              {
                styleSel.html(styleOpt);
              }
            }
            else if(borrowTimeType==1)//天标
            {
              styleOpt += (style2);
              styleSel.html(styleOpt);
            }
        }

      //上传图片
      require.async(["uploadify/uploadify.css","uploadify/jquery.uploadify.min"],function(){
        //上传多张图片
        $('#files').uploadify({
              fileObjName : 'file',
              buttonText:'添加图片',

              fileTypeDesc:'Image Files',
              fileTypeExts:'*.gif; *.jpg; *.png',
              fileSizeLimit:'2146900',
              widht:67,
              height:67,
              swf:'/themes/theme_default/media/js/uploadify/uploadify.swf',
              uploader:"/borrow/uploadBorrowPic.html"+"?random="+(new Date()).getTime(),
              onUploadSuccess:function(file,data, response) {
                if(data!=null){
                  $('.upimg').append("<dd><img src='/data/upfiles/images/borrow/"+data+"' width='50px' height='50px' /></dd>");
                  $('#fileValue').val($('#fileValue').val()+","+data);
                }
              }
        });
      })
    //删除图片 
    function delImg(num, imgVal){
      $("#del_img" + num).remove();
    //  $.ajax({
    //    url : "/borrow/deleteBorrowPic.html",
    //    data : {pathPic : imgVal},
    //    success:function(data){
          
    //    }
    //  })
    }
});

          
