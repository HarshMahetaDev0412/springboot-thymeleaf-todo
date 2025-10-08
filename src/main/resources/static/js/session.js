window.onmousemove = HandleTimeOut;
window.onclick = HandleTimeOut;
window.onwheel = HandleTimeOut;
window.onkeypress = HandleTimeOut;
window.onload = HandleTimeOut;

var timeOutTime = 1000*60*1000;	//30 min
var timeOut;
var timer;
function HandleTimeOut()
{
	var count = (timeOutTime/1000)-1;
	var min = (((timeOutTime/1000)/60)-1)>=0?((timeOutTime/1000)/60)-1:0;
	var sec = (timeOutTime/1000)<60?(timeOutTime/1000)-1:59;
	CountDown(count,min,sec);
	clearTimeout(timeOut);
	timeOut=setInterval(TimeOut,timeOutTime);
}
function TimeOut()
{
	alert("Your Session Expires!!!");
	window.location.assign("../sess/Expire.jsp?expire_msg=Timeout");
}
function CountDown(c,min,sec)
{
	clearTimeout(timer);
	timer = setInterval(
	function() {
		if(min<10)
		{
			document.getElementById("min").innerHTML = "0"+min;
		}
		else
		{
			document.getElementById("min").innerHTML = min;
		}
		if(sec<10)
		{
			document.getElementById("sec").innerHTML = "0"+sec;
		}
		else
		{
			document.getElementById("sec").innerHTML = sec;
		}
		c=c-1;
		sec=sec-1;
		if(sec<0)
		{
			min=min-1;
			sec=59;
		}
		if(c < 0)
		{
			clearTimeout(timer);
		}
	},1000);
}