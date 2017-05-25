define(function(require,exports,modlue){
	function questionFun(){
		var option1,option2,option3,option4,option5,option6,option7;
		option1 = '<option value="您母亲的姓名是？" data-value="_string">您母亲的姓名是？</option>'
		option2 = '<option value="您父亲的姓名是？" data-value="_string">您父亲的姓名是？</option>'
		option3 = '<option value="您配偶的姓名是？" data-value="_string">您配偶的姓名是？</option>'
		option4 = '<option value="您的出生地是？" data-value="_string">您的出生地是？</option>'
		option5 = '<option value="您父亲的生日是？" data-value="_number">您父亲的生日是？</option>'
		option6 = '<option value="您母亲的生日是？" data-value="_number">您母亲的生日是？</option>'
		option7 = '<option value="您配偶的生日是？" data-value="_number">您配偶的生日是？</option>';
		
		var options = option1+option2+option3+option4+option5+option6+option7
		
		var objBox = $("select");
		var valued = [],itemVal = "";
		var itemOption,optionVal;
		objBox.each(function(){
			$(this).append(options);
		});
		objBox.bind("change" ,function(){
			var that = $(this)
			valued.push(that.val());//获取到所有select的value
			if(valued.length>3){
				valued.shift();
			}
			itemVal = valued.join("？")
			itemOption = $("option",objBox);
			itemOption.each(function(i){
				optionVal = $(itemOption[i]).attr("value");
				if(itemVal.match(optionVal)){
					$(itemOption[i]).hide();
				}else{
					$(itemOption[i]).show();
				}
			})
		})
	}
	questionFun();
})