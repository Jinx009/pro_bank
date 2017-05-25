define(function(require,exports,module){
    require('jquery');
    $.ajax({
      type:"post",
      url:"/lcschool/personalConsultants.html",
      dataType:"json",
      success:function(data){
    	 if(data.data.list != null){
    		 var msg="";
    		 for(var j=0;j<data.data.list.length;j++){
    			 var att=data.data.list[j];
    			 var imgUrl = "http://home.800bank.com.cn:8089"+att.avatar;/*专家头像*/
    			 var name = att.name;/*专家姓名*/
				 var job = att.position;/*专家职位*/
				 var introduction = att.introduction;/*专家简介*/
				 var id = att.id;/*专家ID*/
				 var appointment_url = "/wx/account/applyAc.html?consultant_id="+id; /*预约专家地址*/
				 if(j==0){
						msg += "<div class='private_detail'><div class='appointment_left'>";
						msg += "<img class='private_img' src='"+imgUrl+"' width='90' height='90' alt='"+name+"' /></br>";
						msg += "<div class='private_appointment'><a href='"+appointment_url+"'>预约</a></div></div>";
						msg += "<div class='appointment_right'><span class='private_name'>"+name+"</span><span class='private_job'>"+job+"</span></br>";
						msg += "<p>"+introduction+"</p></div><i class='private_ Recommend'></i></div>";
						
				 }else{
    					msg += "<div class='private_detail'><div class='appointment_left'>";
						msg += "<img class='private_img' src='"+imgUrl+"' width='90' height='90' alt='"+name+"' /></br>";
						msg += "<div class='private_appointment'><a href='"+appointment_url+"'>预约</a></div></div>";
						msg += "<div class='appointment_right'><span class='private_name'>"+name+"</span><span class='private_job'>"+job+"</span></br>";
						msg += "<p>"+introduction+"</p></div></div>";
				 }
             }
	    	 $("#private_appointment").html(msg);
    	 }
      }
   }); 
});
