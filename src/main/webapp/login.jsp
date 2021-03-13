<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%
	String basePath = request.getScheme() + "://" +
			request.getServerName() + ":" +
			request.getServerPort() +
			request.getContextPath() + "/";
%>
<base href="<%=basePath%>">

<html>
<head>
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

	<script type="text/javascript">
		$(function () {

			if (window.top != window){
				window.top.location = window.location
			}

			//当页面加载完成后，将用户框中的值清空
			$("#loginAct").val("");
			$("#loginAct").focus();

			//为登录按钮绑定事件，执行验证操作
			$("#submitBtn").click(function () {
				login();
			})

			//为窗口绑定敲键盘事件，进行登录操作
			//event：这个参数可以获得我们敲的是哪个键
			$(window).keydown(function (event) {
				//alert(event.keyCode)
				//如果获得的键盘的码值是13，则表示敲得是回车键
				if (event.keyCode==13){
					login();
				}
			})
		})

		//自定义的function方法，要写在$(function(){})的外面
		//处理登录验证操作的函数
		function login() {
			//验证账号密码不能为空
			//$.trim方法，可以将字符串左右两边的空格去除
			var loginAct = $.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());

			if (loginAct=="" || loginPwd==""){
				$("#msg").html("账号密码不能为空")

				//如果账号密码为空，我们需要及时强制终止该方法
				return false;
			}

			//后台验证相关操作
			$.ajax({
				url:"settings/user/login.do",
				data:{
					"loginAct":loginAct,
					"loginPwd":loginPwd
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if (data.success){
						//登录成功
						window.location.href="workbench/index.jsp";
					}else {

						$("#msg").html(data.msg);
					}
				}
			  }
			)

		}
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red"></span>
						
					</div>

<%--					注意：
                            button按钮在表达中默认的行为是提交表单
                            我们要修改type为button
                            然后手动写js去验证表单
--%>
					<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>