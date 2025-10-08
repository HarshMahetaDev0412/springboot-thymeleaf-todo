var allowNeg = false;


function checkDateRange(from_dt,to_dt)
{
	var from_dt = from_dt.value;
	var to_dt = to_dt.value;
	var flag1=true;
	
	if(from_dt !='' && to_dt !='')
	{
		if(trim(from_dt)!="" && trim(to_dt)!="")
	   	{
	   		var tmp = from_dt.split("/")
	   		var tmp1 = to_dt.split("/")
	   	 	var date1 = new Date(tmp[1]+"/"+tmp[0]+"/"+tmp[2]);
         	var date2 = new Date(tmp1[1]+"/"+tmp1[0]+"/"+tmp1[2]);
         	
         	var time_difference = date2.getTime() - date1.getTime();
         	var days_difference = time_difference / (1000 * 60 * 60 * 24);
         	
         	if(parseInt(days_difference)+1 <= 180)
         	{
			   	p = compareDate(from_dt,to_dt); 	
				
				 if(p==1)
				{
					alert('From Date should be less than or equal to To Date ');
					document.forms[0].to_dt.value='';
					flag1=false;
					return flag1;
				}
         	}
         	else
         	{
         		alert("Date range can not exceed more than 180 days!!")
         		document.forms[0].from_dt.value='';
         		flag1=false;
				return flag1;
         	}
		}
    }
}

function trim(argvalue)
{
	var tmpstr = ltrim(argvalue);
   	return rtrim(tmpstr);
}

function ltrim(argvalue)
{
	while(1)
	{
		if(argvalue.substring(0, 1) != " ")
		{
			break;
		}
		argvalue = argvalue.substring(1, argvalue.length);
	}
    return argvalue;
}

function rtrim(argvalue)
{
	while(1)
	{
		if(argvalue.substring(argvalue.length - 1, argvalue.length) != " ")
		{
        	break;
		}
        argvalue = argvalue.substring(0, argvalue.length - 1);
	}
    return argvalue;
}

function checkNumber1(obj,a,b)
{ 
	var c = parseInt(a)-parseInt(b);
	var flag=true;
	
	var fieldValue=obj.value;
    
    var len = 0;
    
    var str = fieldValue.substring(0,fieldValue.indexOf('.')).length;
	
	if(str == 0)
	{
		len = fieldValue.length;
	}
	else
	{
		len = str;
	}
    
    if(obj.value!="" && obj.value!=null && obj.value!=' ')
    {
		if((parseInt(len) > parseInt(c)))
		{
    		alert("Please, Enter In the Required  Format.."+'('+c+' ,'+b+' )');
			obj.value= "";
			obj.select();
			flag = false;
		}
		else
		{
			var decallowed = b;  // how many decimals are allowed?
        
        	if(isNaN(fieldValue) || fieldValue == "")
        	{
        		alert("Please, Enter In the Required  Format.."+'('+c+' ,'+b+' )');
 		    	obj.value="";
	 	    	obj.select();	 
				flag=false;
        	}
      		else
      		{
         		if(fieldValue.indexOf('.') == -1) 
		    	{
		    		fieldValue += ".";
         		}
         	
         		dectext = fieldValue.substring(fieldValue.indexOf('.')+1, fieldValue.length);
         	
         		if(parseInt(dectext.length) > parseInt(decallowed))
            	{
         			alert("Please, Enter In the Required  Format.."+'('+c+' ,'+b+') !!!');
             		obj.value="";
             		obj.select();		
			 		flag=false;
            	}
         		else
         		{
              		flag=true;
            	}
        	}
		}
   	}
    
    return flag;
}

function checkForNumber(obj)
{
	var size = obj.value.length;
	var charCode;
	var i = 0;
	
	for(i=0;i<size;i++)
	{
		charCode = obj.value.charCodeAt(i);	
		if(charCode > 31 && (charCode < 48 || charCode > 57))
		{
			alert("Enter only Numeric Value !!!");
			obj.value = "";
			return false;
		}
	}
	return true;	
}

function validateDate(obj)
{     
	//This function takes date in dd/MM/yyyy format and validate for
	//valid month,no of days depending on month and year.
	//like checks for 30/31/28-29 days for valid months i.e. months >0 and 
	//	months <=12
	
	dateutilFlag = true;
	dateValue = obj.value;
	dateValue = trim(dateValue);
	len = dateValue.length;
    
    if(len!=0)
    {
		var monthname = new Array("","January", "February", "March", "April","May","June","July",
				   "August", "September","October","November","December" );
		fullyear = "";
		msg = "Incorrect Date Format. \r\n\r\nPlease use dd/mm/yyyy format";
		
		firstIndex = dateValue.indexOf('/');
		lastIndex = dateValue.lastIndexOf('/');
		
		if(firstIndex==0  || firstIndex>2)
		{
			dateutilFlag = false;
		}
	
		if(lastIndex<3 || lastIndex>5)
		{
			dateutilFlag =false;
		}
    
    	if(dateutilFlag)
    	{
			mydd = dateValue.substring(0,firstIndex);
       
			if(mydd.length == 2)
			{
		 		x_x = mydd.substring(0,1);
		 		y_y = mydd.substring(1);
				
				if(!(x_x == '0'))
				{
					if(!isDigit(x_x)) 	dateutilFlag = false;
				}
				
				if(!(y_y == '0'))
				{
					if(!isDigit(y_y)) 	dateutilFlag = false;
				}
			}

			if(mydd.length == 1)
			{
				if(!isDigit(mydd)) 	dateutilFlag = false;
		        mydd = "0" + mydd;
			}
			mymm = dateValue.substring(firstIndex+1,lastIndex);
	     
	     	if(mymm.length == 2)
	     	{
		 		x_x_x = mymm.substring(0,1);
		 		y_y_y = mymm.substring(1);
				
				if(!(x_x_x == '0'))
				{
					if(!isDigit(x_x_x)) 	dateutilFlag = false;
				}

				if(!(y_y_y == '0'))
				{
					if(!isDigit(y_y_y)) 	dateutilFlag = false;
				}
			}

			if(mymm.length == 1) mymm = "0" + mymm;
			
			myyy = dateValue.substring(lastIndex+1);
			
			if(myyy.length > 4) dateutilFlag=false;
			
			if(myyy.length < 4) dateutilFlag =false;
			
			if(myyy.length == 2) myyy = "20" + myyy;
			
			fullyear = myyy;

			if(!isDigit(fullyear))
			{
				dateutilFlag = false;
			}
			dateValue = mydd + "/" + mymm + "/" + myyy;
		}
		
		if(dateutilFlag)
     	{
			day = getDay(dateValue);
			mon = getMon(dateValue);
			maxDay = 31;
	       
			if(mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 || mon == 10 || mon == 12)
				maxDay = 31;
			
			if(mon == 4 || mon == 6 || mon == 9 || mon == 11)
				maxDay = 30;

			if(mon == 2)
            {
 				year = getYear(dateValue)
				yearStr = year / 4 +"";
				
				if(yearStr.indexOf('.') == -1) maxDay = 29;
				else maxDay = 28;
		    }
	
			if(mon<=0 || mon > 12) dateutilFlag = false;
			
			if(day > maxDay)
			{
				dateutilFlag = false;
				msg = monthname[mon] + " of " +fullyear + " does not have " + day + " days.";
			}
	    }
		
	   	if(!dateutilFlag)
	   	{
	   		alert(msg);
	   		obj.value="";
	 		//obj.focus();
	   		return false;
	    }
	    else
	    {
	    	dateValue=dateValue.substring(0,6) + dateValue.substring(6);
	   		obj.value=dateValue;
	   		return true;
	   	}
    }
    else
    {
        return false;
    }    
}

function isDigit(digitValue)
{
	utilFlag = false;
	prevLength = 0;
	currLength = 0;
	prevLength = digitValue.length;
	currLength = prevLength;
	len = 0;
	
	while(len < prevLength)
	{
		if(parseInt(digitValue.substring(0,1)) == 0)
		{
			digitValue = digitValue.substring(1);
		}
		len++;
	}
	
	if(prevLength > 0)
    {
 	    if(parseInt(digitValue.substring(0, 1)) > 0 && parseInt(digitValue.substring(0, 1)) < 10)
 	    {
			valueStr = parseInt(digitValue)+"";
 	    }
	    else
	    {
			valueStr = "";
	    }

  	    currLength = valueStr.length;

	    if(prevLength == currLength)
	    {
			utilFlag = true;
	    }
  	    else 
  	    {
			utilFlag = false;
  	    }
    }
	return utilFlag;
}

function validateEmail(obj)
{
	var email = obj.value;
	var flag = true;	
	if(email!=null && email!='' && email!=' ')
	{
		var str = trim(obj.value);
		var at = "@";
		var dot = ".";
		var lat = str.indexOf(at);
		var lstr = str.length;
		var ldot = str.indexOf(dot,lat)+1;
		
		var consec=str.indexOf(dot);
		var consecond=str.indexOf(dot,consec+1);
		var idot=str.indexOf("@");
		var consecat=str.indexOf(".",idot);
		var consecondat=str.indexOf(dot,consecat+1);
		var secdot=	str.indexOf(".");
		var consecdot=str.indexOf(".",secdot+1);
		var consecondot=str.indexOf(dot,consecdot+1);
		//alert("consecat = "+consecat+" consecondat = "+consecondat);
		
		var count = 0;
		var cnt = 0;
		var str1 = str.split("@");
		var i = 0;
		var j = 0;
		
		for(i=0; i<str1.length; i++)
		{
			for(j=0; j<str1[i].length; j++)
			{
				if(i==0)
				{
					if(str1[i].charAt(j)==".")
					{
						++count;
					}
				}
				else if(i==1)
				{
					if(str1[i].charAt(j)==".")
					{
						++cnt;
					}
				}
			}
		}
			
		if(count>2 || cnt>2)
		{
			flag = false;
		}
		if((str.indexOf(dot)==consec && consecond==(consec+1)) || (str.indexOf(".",idot)==consecat && consecondat==(consecat+1)) || (str.indexOf(".",secdot)==consecdot && consecondot==(consecdot+1)) )
		{
			flag = false;
		}	
		
		if(str.indexOf(at)==-1)
		{
			flag = false;
		}	
		if(str.indexOf(at)==-1 || str.indexOf(at)==0 || str.indexOf(at)==lstr)
		{
			flag = false;
		}	
		if(str.indexOf(dot)==-1 || str.indexOf(dot)==0 || str.indexOf(dot)==lstr)
		{
			flag = false;
		}	
		if(str.indexOf(at,(lat+1))!=-1)
		{
			flag = false;
		}			
		if(str.substring(lat-1,lat)==dot || str.substring(lat+1,lat+2)==dot)
		{
			flag = false;		   
		}	
		if(str.indexOf(dot,(lat+2))==-1)
		{
			flag = false;
		}			
		if(str.indexOf(" ")!=-1)
		{
			flag = false;
		}		
		if(lstr>1 && lstr==ldot)
		{
			flag = false;
		}
		
		var spec_char_flg = true;
		
		for(i=0; i<str.length; i++)
		{
			if((str.charCodeAt(i)>=33 && str.charCodeAt(i)<=44) || str.charCodeAt(i)==47)
			{
				flag = false;
				spec_char_flg = false;
			}
			
			if((str.charCodeAt(i)>=91 && str.charCodeAt(i)<=94) || str.charCodeAt(i)==96)
			{
				flag = false;
				spec_char_flg = false;
			}
			
			if(str.charCodeAt(i)>=58 && str.charCodeAt(i)<=63)
			{
				flag = false;
				spec_char_flg = false;
			}
			
			if(str.charCodeAt(i)>=123 && str.charCodeAt(i)<=126)
			{
				flag = false;
				spec_char_flg = false;
			}
		}
		
		if(flag==false)
		{
			if(spec_char_flg==false)
			{
				alert("Special Characters Are NOT Allowed For E-mail ID !!!");
			}
			else
			{
				alert("Invalid E-mail ID !!!");
			}
			obj.value = "";
			obj.select();
		}
	}	
	return flag;
}

function getDay(dateStr)
{
    dateStr = dateStr + "";
	dayStr = dateStr.substring(0,2);

	if(dayStr.indexOf('0') == 0)
	{
		dayStr = dayStr.substring(1);
	}

	if(isDigit(dayStr))
	{
		return parseInt(dayStr);
	}
	else
	{
		return 0;
	}
}


function getMon(dateStr)
{
	monStr = dateStr.substring(3,5);

	if(monStr.indexOf('0') == 0)
	{
		monStr = monStr.substring(1);
	}

	if(isDigit(monStr))
	{
		return parseInt(monStr);
	}
	else
	{
		return 0;
	}
}


function getYear(dateStr)
{
	yearStr = dateStr.substring(6);
	prevLength = yearStr.length;
	len = 0;
	
	while(len < prevLength)
	{
		if(parseInt(yearStr.substring(0,1)) == 0)
		{
			yearStr = yearStr.substring(1);
		}
		len++;
	}

	if(isDigit(yearStr))
	{
		return parseInt(yearStr);
	}
	else
	{
		return 0;
	}
}

function compareDate(date1,date2)
{
	/*
	returns 0 if date1 = date2
	returns 1 if date1 > date2
	returns 2 if date1 < date2
	*/
	day1 = getDay(date1)
	mon1 = getMon(date1)
	year1 = getYear(date1);

	day2 = getDay(date2)
	mon2 = getMon(date2)
	year2 = getYear(date2);
	
	resultVal = 0;

	if(year1 == year2 && mon1== mon2 && day1 == day2)
	{
		resultVal = 0;
	}
	if(year1 > year2 || ( year1==year2 && mon1 > mon2) || (year1 == year2 && mon1==mon2 && day1 > day2))
	{
		resultVal = 1;
	}
	if(year1 < year2 || ( year1==year2 && mon1 < mon2) || (year1 == year2 && mon1==mon2 && day1 < day2))
	{
		resultVal = 2;
	}
	return parseInt(resultVal);
}

function checkFromToDate(obj,obj1,flag)
{
	if((obj.value!="" && trim(obj.value) != "" && obj.value != null) && (obj1.value!="" && trim(obj1.value) != "" && obj1.value != null))
	{
		if(flag=="F")
		{
			var count = compareDate(obj.value,obj1.value);
			if(parseInt(count) == 1)
			{
				alert("From Date should be less or equal To Date!")
				obj.value="";
				return false;
			}
		}
		else if(flag=="T")
		{
			var count = compareDate(obj.value,obj1.value);
			if(parseInt(count) == 1)
			{
				alert("To Date should be grater or equal From Date!")
				obj1.value="";
				return false;
			}
		}
	}
}

function checkStartEndDate(obj,obj1,flag)
{
	if((obj.value!="" && trim(obj.value) != "" && obj.value != null) && (obj1.value!="" && trim(obj1.value) != "" && obj1.value != null))
	{
		if(flag=="F")
		{
			var count = compareDate(obj.value,obj1.value);
			if(parseInt(count) == 1)
			{
				alert("Start Date should be less or equal End Date!")
				obj.value="";
				return false;
			}
		}
		else if(flag=="T")
		{
			var count = compareDate(obj.value,obj1.value);
			if(parseInt(count) == 1)
			{
				alert("End Date should be grater or equal Start Date!")
				obj1.value="";
				return false;
			}
		}
	}
}

function negNumber(fObj)
{
	if(fObj.value=="") return;
	if(isNaN(fObj.value))
	{
		alert("No number entered");
		fObj.value="";
		return;
	}
	var actualVal = parseFloat(fObj.value);

	if (actualVal < 0)
	{
		if (!allowNeg)
		{
			alert("Sorry, negative numbers not permitted!");
			fObj.value="";
			return false;
		}
	}
}

function checkEffectiveDate(obj,last_eff_dt)
{
	var eff_dt = obj.value;
	
	if((eff_dt!="" && trim(eff_dt) != "" && eff_dt != null) && (last_eff_dt!="" && trim(last_eff_dt) != "" && last_eff_dt != null))
	{
		var value = compareDate(eff_dt,last_eff_dt);
		if(parseInt(value) == 2)
	  	{
	    	alert("Effective Date ("+eff_dt+") Must Be Grater Or Equal To Last Effective Date ("+last_eff_dt+")!");
	    	obj.value = "";
	    	
	    	return false;
	  	}
	}
}

function checkEffDtWithContStDt(obj,last_eff_dt)
{
	var eff_dt = obj.value;
	
	if((eff_dt!="" && trim(eff_dt) != "" && eff_dt != null) && (last_eff_dt!="" && trim(last_eff_dt) != "" && last_eff_dt != null))
	{
		var value = compareDate(eff_dt,last_eff_dt);
		if(parseInt(value) == 2)
	  	{
	    	alert("Contract Start Date ("+eff_dt+") Must Be Grater Or Equal To Counterparty Effective Date ("+last_eff_dt+")!");
	    	obj.value = "";
	    	
	    	return false;
	  	}
	}
}

function checkSigningStartDt(obj,signing_dt)
{
	var eff_dt = obj.value;
	
	if((eff_dt!="" && trim(eff_dt) != "" && eff_dt != null) && (signing_dt!="" && trim(signing_dt) != "" && signing_dt != null))
	{
		var value = compareDate(eff_dt,signing_dt);
		if(parseInt(value) == 2)
	  	{
	    	alert("Contract Start Date ("+eff_dt+") Must Be Grater Or Equal To Contract Date ("+signing_dt+")!");
	    	obj.value = "";
	    	
	    	return false;
	  	}
	}
}

function validateTime(obj)
{	
	var fieldValue = obj.value;
	
	if(fieldValue!=null && fieldValue!='' && fieldValue!=' ')
	{
		var t1 = fieldValue.substr(0,2);
		var t2 = fieldValue.substr(2,1);
		var t3 = fieldValue.substr(3,2);
		
		if(fieldValue.length!=5)
		{
			alert(" Not A Valid Time Format !!!\nPlease Enter Time In HH:MM Format !!!");
	        obj.value="";
	        obj.select();
			return false;
		}
		
		//alert("t1 = "+t1+",  t2 = "+t2+",  &  t3 = "+t3);
		
		for(var j=0; j<t1.length; j++)
		{
			var alphaa = ""+t1.charAt(j);
			var hh = alphaa.charCodeAt(0);
			if(hh>47 && hh<58)
			{
			
			}
			else
			{
				alert(" Not A Valid Time Format !!!\nPlease Enter Time In HH:MM Format !!!");
	            obj.value="";
	            obj.select();
				return false;
			 }
	 	}
	 	
	 	for(var j=0; j<t3.length; j++)
		{
			var alphaa =""+t3.charAt(j);
			var hh = alphaa.charCodeAt(0);
			if(hh>47 && hh<58)
			{
			
			}
			else
			{
				alert(" Not A Valid Time Format !!!\nPlease Enter Time In HH:MM Format !!!");
	            obj.value="";
	            obj.select();
				return false;
			 }
	 	}
	 		
		if(parseInt(t1)<0 || parseInt(t1)>24)
		{
			alert(" Not A Valid Time Format !!!\nPlease Enter Time In HH:MM Format !!!");
			obj.value="";
			obj.select();
			return false;						
		}
		
		if(parseInt(t1)==24)
		{
			if(parseInt(t3)>0 || parseInt(t3)<0)
			{
				alert(" Not A Valid Time Format !!!\nPlease Enter Time In HH:MM Format !!!");
				obj.value="";
				obj.select();
				return false;
			}
		}
		
		if(parseInt(t3)<0 || parseInt(t3)>59)
		{
			alert(" Not A Valid Time Format !!!\nPlease Enter Time In HH:MM Format !!!");
			obj.value="";
			obj.select();
			return false;
		}
		
		if(t2!=':')
		{
			alert(" Not A Valid Time Format !!!\nPlease Enter Time In HH:MM Format !!!");
			obj.value="";
			obj.select();
			return false;
		}
	}	
	return true;
}

function round(num, x)
{
	num*= Math.pow(10,x);
	num=Math.round(num);
	num/=Math.pow(10,x);
	if( ("" + num).indexOf(".")   > 0  )
	{
		var adjusted_num =  adjustDecimalPoint(num,x);
		return (adjusted_num);
	}
	else
	{
		return num;
	}
}

function adjustDecimalPoint(numb, zz)
{	
	var  tq = ("" + numb).indexOf(".");
	var ss = ("" + numb).substring(tq+1);

	if((ss.length) == zz)
	{
			return(numb);
	}
	else{
	numb = "" + numb + "0";
	return(adjustDecimalPoint(numb,zz));
	}
}

function appPercentage(percentage)
{
	let percentage_num;

	if(percentage.value != '')
	{
		if (typeof percentage.value === 'string')
	    {
	    	percentage_num = parseFloat(percentage.value);
	    }
	    else 
	    {
	        percentage_num = Number(percentage.value);
	    }

	    if (isNaN(percentage_num))
	    {
	        alert("Enter a valid number for percentage!!");
	        percentage.value="";
	        return false;
	    }

	    if (percentage_num < 0 || percentage_num > 100)
	    {
	        alert("Percentage should be in the range between 0-100!!");
	        percentage.value="";
	        return false;
	    }
	    
	    const percentageStr = percentage_num.toString();
	    const decimalIndex = percentageStr.indexOf('.');
	    
	    if (decimalIndex !== -1)
	    {
	        const decimalPlaces = percentageStr.length - decimalIndex - 1;
	        if (decimalPlaces > 2)
	        {
	            alert("Please enter a value in the format with up to 2 decimal places!!");
	            percentage.value="";
	            return false;
	        }
	    }
	}
	else
	{
		return false;
	}
}


$(document).ready(function () 
{
	window.setTimeout(function() 
	{
		$(".fadealert").fadeTo(1000, 0).slideUp(1000, function()
		{
    		$(this).remove(); 
		});
	}, 5000);
});

$(document).ready(function() 
{
	//for(var i=1;i<=2;i++)
	//{
		$('.fmsdtpick').datepicker({
		    changeMonth : true,
			changeYear : true,
			todayHighlight: true,
			autoclose : true,
			language : "en",
			orientation : "bottom auto",
			format: 'dd/mm/yyyy'
		});
	//}
});

function FmsDatePicker()
{
	$('.fmsdtpick').datepicker({
	    changeMonth : true,
		changeYear : true,
		todayHighlight: true,
		autoclose : true,
		language : "en",
		orientation : "bottom auto",
		format: 'dd/mm/yyyy'
	});
}

$(document).ready(function() 
{
	$("tbody > tr").click(function(){
		$(this).toggleClass("table-active");
	});
});

