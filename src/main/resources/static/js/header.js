/**
 * ONLY USE FOR HEADER 
 */

function home_click1(){
	//url="../home/index4.jsp?std=1";
	//location.replace(url);
}
var newWindow;
var newWindow2;
function OpenUnitconv()
{
	//alert("NOT AVAILABLE");
   	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("../home/calculator.jsp","Calculator","top=10,left=100,width=350,height=500,scrollbars=1,status=1");
	}
	else
	{
		 newWindow.close();
		 newWindow= window.open("../home/calculator.jsp","Calculator","top=10,left=100,width=350,height=500,scrollbars=1,status=1");
	}
}

function OpenUserManual()
{
  	if(!newWindow2 || newWindow2.closed)
	{
		newWindow2 = window.open("../manual/TRACKER-MANUAL.pdf","UserManual","top=10,left=10,width=1000,height=700,scrollbars=1,status=1");
	}
	else
	{
		 newWindow2.close();
		 newWindow2 = window.open("../manual/TRACKER-MANUAL.pdf","UserManual","top=10,left=10,width=350,height=700,scrollbars=1,status=1");
	}
}
function home_click(stemp,login,pwd){
	var url="";
	if(stemp=="0"){
		url="../home/index3.jsp?std=0&home_click=Y";
		location.replace(url);
	}	else if(stemp=="1"){
		url="../home/index3.jsp?std=1&login="+login+"&password="+pwd+"&home_click=Y";
		location.replace(url);
	}
}