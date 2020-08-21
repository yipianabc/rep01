<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户注册</title>
<script type="text/javascript" src="js/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#uname").blur(function() {//绑定用户名文本框失去焦点事件
			var val = $(this).val();
			var $spanobj = $(this).next();
			if (!val) {			//用户名为空
				$spanobj.html("用户不能为空！");
				return; 
			}
			//jquery封装的ajax
			$.ajax({
				type:"get",//发送请求的方式
				url:"CheckUname",//发请求的url地址
				data:"uname="+val,//传送的参数
				success:function(res){//设置成功的回调函数
					$spanobj.html(res);
				},
				error:function(e){//失败
					alert('ajax请求失败!');
				}
			});
			//jquery封装的post方法
			/*$.post("CheckUname", "uname=" + val, function(res) {//第一个参数是url,第二个参数是传递参数,第三个是回调函数
				$spanobj.html(res);
			});*/
		});

		//ajax提交form表单
		$("#submitbut").click(function() {
			$("form").ajaxSubmit(function(res) {
				alert(res);
			});
			return false;//阻止form表单默认提交
		});
	});
</script>
</head>
<body>
	<fieldset>
		<legend>用户注册</legend>
		<form action="registeruser" method="post">
			<table>
				<tr>
					<td>用户名:</td>
					<td><input name="uname" id="uname"><span></span></td>
				</tr>
				<tr>
					<td>密码:</td>
					<td><input type="password" id="password" name="password"></td>
				</tr>
				<tr>
					<td>确认密码:</td>
					<td><input type="password" id="password2" name="password2"></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="注册" id="submitbut">
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
</body>
</html>