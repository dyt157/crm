<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
	<script type="text/javascript">
		//浏览器展示该页面的时候，地址栏是：http://localhost:8080/crm/
		//所以下面的路径对应的绝对路径写法是：http://localhost:8080/crm/settings/qx/user/login
		//所以和IndexController中的toIndex方法匹配上了
		document.location.href = "settings/qx/user/toLogin";
	</script>
</body>
</html>