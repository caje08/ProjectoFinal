var button=document.getElementById("chatRoombtns:refresh");
var chat = document.getElementById("CHATROOM");

var myVar=setInterval(function () {refresh()}, 500);

function refresh(){
	button.click();
	chat.scrollTop = chat.scrollHeight;
}