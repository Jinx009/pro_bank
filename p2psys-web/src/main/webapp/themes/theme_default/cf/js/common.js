$(function(){
	if(!placeholderSupport()){   // 判断浏览器是否支持 placeholder
	    $('[placeholder]').focus(function() {
	        var input = $(this);
	        if (input.val() == input.attr('placeholder')) {
	            input.val('');
	            input.removeClass('placeholder');
	        }
	    }).blur(function() {
	        var input = $(this);
	        if (input.val() == '' || input.val() == input.attr('placeholder')) {
	            input.addClass('placeholder');
	            input.val(input.attr('placeholder'));
	        }
	    }).blur();
	};
//	$(".datepicker").datepicker();//调用日期控件
})
function placeholderSupport() {
    return 'placeholder' in document.createElement('input');
}

/**
 * 验证身份证号
 * @param num
 * @returns {Boolean}
 */
function isIdCardNo(num) 
{
    var factorArr = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1);
    var parityBit = new Array("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2");
    var varArray = new Array();
    var intValue;
    var lngProduct = 0;
    var intCheckDigit;
    var intStrLen = num.length;
    var idNumber = num;
  
    if ((intStrLen != 15) && (intStrLen != 18)) 
    {
        return false;
    }
    for (i = 0; i < intStrLen; i++) 
    {
        varArray[i] = idNumber.charAt(i);
        if ((varArray[i] < '0' || varArray[i] > '9') && (i != 17)) 
        {
            return false;
        } 
        else if (i < 17) 
        {
            varArray[i] = varArray[i] * factorArr[i];
        }
    }
    if (intStrLen == 18) 
    {
       
        var date8 = idNumber.substring(6, 14);
        if (isDate8(date8) == false) 
        {
            return false;
        }
        for (i = 0; i < 17; i++) 
        {
            lngProduct = lngProduct + varArray[i];
        }
        intCheckDigit = parityBit[lngProduct % 11];
        if (varArray[17] != intCheckDigit) 
        {
            return false;
        }
    }
    else 
    {        
        var date6 = idNumber.substring(6, 12);
        if (isDate6(date6) == false) 
        {
            return false;
        }
    }
    return true;
}

function isDate6(sDate) 
{
    if (!/^[0-9]{6}$/.test(sDate)) 
    {
        return false;
    }
    var year, month, day;
    year = sDate.substring(0, 4);
    month = sDate.substring(4, 6);
    if (year < 1700 || year > 2500) return false
    if (month < 1 || month > 12) return false
    return true;
}

function isDate8(sDate) 
{
    if (!/^[0-9]{8}$/.test(sDate)) 
    {
        return false;
    }
    var year, month, day;
    year = sDate.substring(0, 4);
    month = sDate.substring(4, 6);
    day = sDate.substring(6, 8);
    var iaMonthDays = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
    if (year < 1700 || year > 2500) return false
    if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) iaMonthDays[1] = 29;
    if (month < 1 || month > 12) return false
    if (day < 1 || day > iaMonthDays[month - 1]) return false
    return true;
}