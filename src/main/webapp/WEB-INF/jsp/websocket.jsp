<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>websocket</title>
</head>
<body>

<script type="text/javascript" src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>  
<script type="text/javascript" src="http://cdn.bootcss.com/sockjs-client/1.1.1/sockjs.js"></script>  
<script type="text/javascript">
	alert("Login successful");
    var websocket = null;
    if ('WebSocket' in window) {  
        //Websocket的连接  
        websocket = new WebSocket("ws://localhost:8080/userLogin_ssm/websocket");//WebSocket对应的地址 
    }
    
    websocket.onopen = onOpen;  
    websocket.onmessage = onMessage;  
    websocket.onerror = onError;  
    websocket.onclose = onClose;  

   	function onOpen() {  
    	$("#content").append("<font>WebSocket连接成功</font><br/>");   
    }  
    function onMessage(evt) {
        var message = evt.data;	// 接收后台发送的数据
        //$("#content").append(typeof(message));
        if(typeof(message)=="string"){
	        var m_items = message.split(";");
	        var file_name = "";
	        var content = "";
	        var senduser = "";
	        var onlineuser = "";
	        for(var item of m_items){
				if(item.search("Message:") != -1){content = item.split(":")[1];}
				else if(item.search("Filename:") != -1){file_name = item.split(":")[1];}
				else if(item.search("Online:") != -1){onlineuser = item.split(":")[1];}
				else if(item.search("@") != -1){senduser = item.split("@")[1];}
	        }
	        if(senduser != "" && content != ""){
	        	$("#content").append("<font style='float:left'>"+senduser+": "+content+"</font><br/>");
	        }
	        if(senduser != "" && file_name != ""){
	        	$("#content").append("<font style='float:left'>发送了"+senduser+": "+file_name+"</font><br/>");
	        }
	        if(onlineuser != ""){
	        	document.getElementById("online").innerHTML = onlineuser;
	        }
        }
        else{
        	 var reader = new FileReader();  
             reader.onload = function(evt){  
                 if(evt.target.readyState == FileReader.DONE){  
                    var url = evt.target.result;  
         			alert(url);  
                    var img = document.getElementById("imgDiv");  
         			img.innerHTML = "<img src = "+url+" />";  
                 }  
             }  
             reader.readAsDataURL(evt.data);
            }
    }  
    function onError() {
    	$("#content").append("<font>WebSocket连接发生错误</font><br/>");  
    }  
    function onClose() {
    	//$("#content").append("<font>WebSocket连接关闭</font><br/>");
    }    
    function doSend() {
        if (websocket.readyState == websocket.OPEN) {
            var message = "Message:" + $("#inputMsg").val() + ";";
            if($("#file").val()){
				message = message + addFile();
            }
            if($("#targetName").val()){
                message = message + "@" + $("#targetName").val();
            }else{
                message = message + "@Allusers";
            }
            //websocket.send(message);
            //var content = $("#inputMsg").val() + ": " + document.getElementById("username").innerHTML;
            var content = message + ": " + document.getElementById("username").innerHTML;
            $("#content").append("<font style='float:right'>"+content+"</font><br/>");
            alert("发送成功!");
        } else {
            alert("连接失败!"+websocket.readyState);  
        }  
    }  
    function closeWebSocket() {
        alert("你已经离开了聊天室");
    	window.location.href="http://localhost:8080/userLogin_ssm/user/login";
    	websocket.close();
    }
    function addFile(){
        var message = "";
		var inputElement = document.getElementById("file");
		var fileList = inputElement.files;
		var file = fileList[0];
		if(!file) return message;
		var reader = new FileReader();
		//以二进制形式读取文件
		reader.readAsArrayBuffer(file);
		//文件读取完毕后该函数响应
		reader.onload = function (){
			var blob = new Blob([this.result]);
			//发送二进制表示的文件
			websocket.send(blob);
			//$("#content").append(String.fromCharCode(blob));
			}
		message = message + "Filename:" + file.name + ";";
		inputElement.innerHTML = "";
		return message;
    }
    
</script>
<div>
	<strong>欢迎<font id="username">${sessionScope.user.username}</font>进入聊天室</strong><br/>
</div>
<div id="contain" style="width:80%; height:50%;">
<div id="left-area" style="width:10%; float: left;">
	<font>在线用户:</font><br/>
	<div id="online"></div>
</div>
<div id="right-area" style="width:50%; height:100%; float:left">
	<font>请输入聊天对象:<input type="text" id = "targetName" /></font><br/>
	聊天内容<br/>
		<div id="content" style="overflow:scroll; width:100%; height:100px; border:1px solid #000"></div><br/>
	请输入内容：<br/>
<textarea style="width: 100%;" id="inputMsg" name="inputMsg"></textarea><br/>
<input type="file" id="file"/>
<button style="float:right;" onclick="doSend()">发送</button>
<button style="float:right;" onclick="closeWebSocket()">关闭WebSocket连接</button><br/>
<div id="imgDiv"></div> 
</div>
</div>
</body>
</html>