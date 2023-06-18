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
	<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
	
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

	<script type="text/javascript">
		$(function () {
			//给保存按钮绑定单击事件
			$("#saveDicTypeBtn").click(function () {
				//避免页面刚刷新时点击保存也能提交的情况
				$("#code").blur();
				//收集信息
				let code = $("#code").val()
				let name = $("#name").val()
				let description = $("#describe").val()
				let codeTip = $("#codeTip").text()
				if (codeTip===""){
					//发送请求，需要跳转回index页面，可以采用同步请求
					let data = "code="+code+"&name="+name+"&description"+description;
					window.location.href="settings/dictionary/type/saveDicType?"+data
				}
			})

			//编码的文本框添加失去焦点事件
			$("#code").blur(function () {
				let code = this.value;
				//做表单验证
				//编码不能为空、不能重复
				if (code===""){
					$("#codeTip").html("编码不能为空")
					return;
				}

				//编码不为空，还要验证是否重复,所以要把code发送到后台数据库进行验证
				//发送的请求不能对页面进行全局刷新，所以是ajax请求
				$.ajax({
					url:"settings/dictionary/type/queryDicTypeByCode",
					data:{code},
					type:"post",
					dataType:"json",
					success:function (data) {
						if (data.code==="0"){
							//数据库中已经有相同的code了
							$("#codeTip").html("编码已经存在了,请重新输入")
						}
					}
				})
			})
			$("#code").focus(function () {
				$("#codeTip").html("")
			})
		})
	</script>
</head>
<body>

	<div style="position:  relative; left: 30px;">
		<h3>新增字典类型</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button id="saveDicTypeBtn" type="button" class="btn btn-primary">保存</button>
			<button type="button" class="btn btn-default" onclick="window.history.back();">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form">
					
		<div class="form-group">
			<label for="create-code" class="col-sm-2 control-label">编码<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="code" style="width: 200%;">
				<span id="codeTip" style="color: red"></span>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-name" class="col-sm-2 control-label">名称</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="name" style="width: 200%;">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 300px;">
				<textarea class="form-control" rows="3" id="describe" style="width: 200%;"></textarea>
			</div>
		</div>
	</form>
	
	<div style="height: 200px;"></div>
</body>
</html>