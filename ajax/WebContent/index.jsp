<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户注册</title>
<script type="text/javascript">
	//创建XMLHttpRequest组件
	function getXmlHttpReq(){
		if(window.XObjectActive){//IE
			try{
				return new XObjectActive("Msxml2.XMLHTTP");//高版本
			}catch(e){
				return new XObjectActive("Microsoft.XMLHTTP");//低版本
			}
		}else if(window.XMLHttpRequest){//firefox、chrome、opera、safar等等
			return new XMLHttpRequest();
		}else{
			alert("请换一种支持ajax浏览器！");
		}
	}
	function checkUname(obj){
		//取文本框中的值
		var val = obj.value;
		//获取用户名文本框后的span对象
		var spanobj = obj.nextSibling;
		if(!val){//用户名为空
			spanobj.innerHTML = "用户名不能为空！";
			return;
		}
		//ajax的请求
		//1,创建XMLHttpRequest组件
		var xmlHttp = getXmlHttpReq();
		//2,设置回调函数
		xmlHttp.onreadystatechange = function(){
			if(xmlHttp.readyState==4 && xmlHttp.status == 200){
				var res = xmlHttp.responseText;//获取响应结果
				spanobj.innerHTML = res;
			}
		}
		//3,初始化
		var url = "CheckUname?uname="+val;
		xmlHttp.open("get",url,true);
		//4,发送请求
		xmlHttp.send(null);
		
		//post请求
		/*xmlHttp.open("post","checkuname",true);
		xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		xmlHttp.send("uname="+val);*/
	}
</script>
</head>
<body>
	<fieldset>
		<legend>用户注册</legend>
		<form action="" method="post">
			<table>
				<tr>
					<td>用户名:</td><td><input name="uname" id="uname" onblur="checkUname(this)"><span></span></td>
				</tr>
				<tr>
					<td>密码:</td><td><input type="password" id="password" name="password" autocomplete="new-password"></td>
				</tr>
				<tr>
					<td>确认密码:</td><td><input type="password" id="password2" name="password2"></td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="submit" value="注册">
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
</body>
</html>