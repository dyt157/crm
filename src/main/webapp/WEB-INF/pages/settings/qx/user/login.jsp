<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+
			request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">
	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2019&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>

				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd" >
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<label>
							<input type="checkbox" id="isRemAP" checked> 十天内免登录
						</label>
						&nbsp;&nbsp;
						<span id="msg" style="color: red"></span>
					</div>
					<button id="loginBtn" type="submit" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>

		</div>
	</div>

	<script type="text/javascript">
		$(function () {
			$("#loginBtn").click(function (){
				//点击登录按钮，获取用户信息，并发送Ajax请求给后台进行验证
				let loginAct = $("#loginAct").val().trim()
				let loginPwd = $("#loginPwd").val().trim()
				let isRemAP = $("#isRemAP").prop("checked")
				//表单验证
				if (loginAct===''){
					$("#msg").html("用户名不能为空")
					return;
				}
				if (loginPwd===''){
					$("#msg").html("密码不能为空")
					return;
				}
				$("#msg").html("<font color='#006400'>正在登录，请稍候.....</font>")

				//发送Ajax请求验证用户名密码
				$.post("settings/qx/user/login",{
					loginAct:loginAct,
					loginPwd:loginPwd,
					isRemAP:isRemAP
				},function (data) {
					// console.log(data)
					if (data.code==="1"){
						//登录成功
						$("#msg").html("")
						window.location.href = "workbench/toIndex";
					}else{
						//登录失败
						$("#msg").html(data.message)
					}


				},"json")
			})

			//给账户框和密码框绑定了回车键按下事件
			$("#loginAct,#loginPwd").keydown(function (event) {
				if (event.key==="Enter"){//大小写严格按照键盘的按键写
					// alert(111)
					//触发登录按钮点击事件
					$("#loginBtn").click()
				}
			})

		})
	</script>
</body>
</html>