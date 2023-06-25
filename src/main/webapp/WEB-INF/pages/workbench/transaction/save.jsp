<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<%--引入自动补全插件--%>
	<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>

	<script type="text/javascript">
		$(function () {
			//加载日历插件
			$("#create-nextContactTime,#create-expectedClosingDate").datetimepicker({
				language:"zh-CN",
				format:"yyyy-mm-dd",
				minView:"month",
				autoclose:true
			})


			//市场活动源的搜索
			$("#searchActivity").keyup(function () {
				let name = this.value
				//属于局部刷新，使用ajax请求技术
				$.ajax({
					url:"workbench/transaction/searchActivityByName",
					data:{name},
					type:"post",
					dataType:"json",
					success:function (data) {
						if(data.code==="1"){
							let activityList = data.returnData;
							let activityHtml=''
							$.each(activityList,function (i, activity) {
								activityHtml+='<tr>'+
												'<td><input type="radio" name="activity" value="'+activity.id+'"/></td>'+
												'<td id="name_'+activity.id+'">'+activity.name+'</td>'+
												'<td>'+activity.startDate+'</td>'+
												'<td>'+activity.endDate+'</td>'+
												'<td>'+activity.owner+'</td>'+
											   '</tr>'
							})
							$("#activityBody").html(activityHtml)
						}else{
							//清空原来的数据
							$("#activityBody").html("")
						}
					}
				})
			})

			//用户选中其中一个活动，把活动名称显示在文本框，把id也记录下来,关闭模态窗口
			$("#activityBody").on("click","[name='activity']",function () {
				let activityId = this.value
				if (this.checked){
					$("#create-activitySrc").val($("#name_"+activityId).text())
					$("#create-activitySrc").attr("activityid",activityId)
				}
				$("#findMarketActivity").modal("hide")
			})

			//联系人搜索
			$("#searchContacts").keyup(function () {
				let fullname = this.value.trim()
				if (fullname!==''){//不等于空才发送请求进行查询，因为模糊查询时，空字符串会匹配所有条数
					$.ajax({
						url:"workbench/transaction/searchContactsByName",
						data:{fullname},
						type:"post",
						dataType:"json",
						success:function (data) {
							if(data.code==="1"){
								let contactsList = data.returnData
								let contactsHtml = '';
								$.each(contactsList,function (i, contacts) {
									contactsHtml+='<tr>'+
													'<td><input type="radio" name="contacts" value="'+contacts.id+'"/></td>'+
													'<td id="name_'+contacts.id+'">'+contacts.fullname+'</td>'+
													'<td>'+contacts.email+'</td>'+
													'<td>'+contacts.mphone+'</td>'+
												   '</tr>'
								})
								$("#contactsTable tbody").html(contactsHtml)
							}else{
								//清空原来的数据
								$("#contactsTable tbody").html("")

							}
						}
					})
				}else{
					$("#contactsTable tbody").html("")
				}
			})

			//用户选中其中一个联系人，把联系人名称显示在文本框，把id也记录下来,关闭模态窗口
			$("#contactsTable tbody").on("click","[name='contacts']",function () {
				let contactsId = this.value
				if (this.checked){
					$("#create-contactsName").val($("#name_"+contactsId).text())
					$("#create-contactsName").attr("contactsId",contactsId)
				}
				$("#findContacts").modal("hide")
			})

			//客户名称自动补全
			$("#create-accountName").typeahead({
				// source:['xx','xx','xx']
				source:function (value, process) {
					//value:每次键盘弹起时，获取到容器中的值（这里指的是input文本框）
					//process:是一个函数，作用是 把该函数中的参数赋值给source属性
					$.ajax({
						url:'workbench/transaction/queryCustomerNameByName',
						data:{name:value},
						type:"post",
						dataType:"json",
						success:function (data) {
							if (data.code==="1"){
								let customerNameList = data.returnData
								process(customerNameList)
							}
						}
					})
				}
			})

			//可能性是可配置的,用户选择了某个阶段，就把这个阶段对应的可能性动态地显示出来
			$("#create-transactionStage").change(function () {
				//当交易的阶段发生变化时，该函数执行
				//属于局部刷新，发送ajax请求
				let stage = this.value
				if (stage==''){
					$("#create-possibility").val("0%")
					return;
				}
				$.ajax({
					url:"workbench/transaction/queryPossibilityForStage",
					data:{
						stage:this.value
					},
					type:"post",
					dataType:"json",
					success:function (data) {
						if (data.code==="1"){
							console.log(data.returnData)
							$("#create-possibility").val(data.returnData+"%")
						}
					}
				})


			})

			//给对应的文本框绑定获取焦点事件
			$("#create-transactionOwner").focus(function () {
				$("#saveOwnerTip").html("")
			})
			$("#create-transactionName").focus(function () {
				$("#saveNameTip").html("")
			})
			$("#create-expectedClosingDate").focus(function () {
				$("#saveExpectedDaterTip").html("")
			})
			$("#create-accountName").focus(function () {
				$("#saveCustomerNameTip").html("")
			})
			$("#create-transactionStage").focus(function () {
				$("#saveStageTip").html("")
			})

			//点击保存按钮，把数据发送到后台，创建交易
			$("#saveTranBtn").click(function () {
				//表单验证
				let owner = $("#create-transactionOwner").val()
				if (owner===''){
					$("#saveOwnerTip").html("所有者不能为空")
					return;
				}
				let money = $("#create-amountOfMoney").val()
				let name = $("#create-transactionName").val()
				if (name===''){
					$("#saveNameTip").html("名称不能为空")
					return;
				}
				let expectedDate = $("#create-expectedClosingDate").val()
				if (expectedDate===''){
					$("#saveExpectedDateTip").html("预计成交日期不能为空")
					return;
				}
				let customerId = $("#create-accountName").val()//这里发送的是实际上是客户名称，不是id
				if (customerId===''){
					$("#saveCustomerNameTip").html("客户名称不能为空")
					return;
				}
				let stage = $("#create-transactionStage").val()
				if (stage===''){
					$("#saveStageTip").html("交易阶段不能为空")
					return;
				}
				let type = $("#create-transactionType").val()
				let source = $("#create-clueSource").val()
				let activityId = $("#create-activitySrc").attr("activityid")
				let contactsId = $("#create-contactsName").attr("contactsid")
				let description = $("#create-describe").val()
				let contactSummary = $("#create-contactSummary").val()
				let nextContactTime = $("#create-nextContactTime").val()
				let tranStr = "owner="+owner+"&"
				tranStr+="money="+money+"&"
				tranStr+="name="+name+"&"
				tranStr+="expectedDate="+expectedDate+"&"
				tranStr+="customerId="+customerId+"&"
				tranStr+="stage="+stage+"&"
				tranStr+="type="+type+"&"
				tranStr+="source="+source+"&"
				tranStr+="activityId="+activityId+"&"
				tranStr+="contactsId="+contactsId+"&"
				tranStr+="description="+description+"&"
				tranStr+="contactSummary="+contactSummary+"&"
				tranStr+="nextContactTime="+nextContactTime
				// alert(tranStr)
				window.location.href="workbench/transaction/saveTran?"+tranStr

			})









		})
	</script>
</head>
<body>

	<!-- 查找市场活动 -->	
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input id="searchActivity" type="text" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody id="activityBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 查找联系人 -->	
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找联系人</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input id="searchContacts" type="text" class="form-control" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="contactsTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody>
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>创建交易</h3>

	  	<div style="position: relative; top: -40px; left: 70%;">
			<button id="saveTranBtn" type="button" class="btn btn-primary">保存</button>
			<button type="button" class="btn btn-default" onclick="window.history.back()">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
		<div class="form-group">
			<label for="create-transactionOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionOwner">
					<option></option>
					<c:forEach items="${userList}" var="user">
						<option value="${user.id}">${user.name}</option>
					</c:forEach>
				  <%--<option>zhangsan</option>
				  <option>lisi</option>
				  <option>wangwu</option>--%>
				</select>
				<span id="saveOwnerTip" style="color: red"></span>
			</div>

			<label for="create-amountOfMoney" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-amountOfMoney">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-transactionName">
				<span id="saveNameTip" style="color: red"></span>
			</div>
			<label for="create-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input readonly type="text" class="form-control" id="create-expectedClosingDate">
				<span id="saveExpectedDaterTip" style="color: red"></span>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-accountName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-accountName" placeholder="支持自动补全，输入客户不存在则新建">
				<span id="saveCustomerNameTip" style="color: red"></span>
			</div>
			<label for="create-transactionStage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="create-transactionStage">
			  	<option></option>
				  <c:forEach items="${stageList}" var="stage">
					  <option>${stage.value}</option>
				  </c:forEach>
			  	<%--<option>资质审查</option>
			  	<option>需求分析</option>
			  	<option>价值建议</option>
			  	<option>确定决策者</option>
			  	<option>提案/报价</option>
			  	<option>谈判/复审</option>
			  	<option>成交</option>
			  	<option>丢失的线索</option>
			  	<option>因竞争丢失关闭</option>--%>
			  </select>
				<span id="saveStageTip" style="color: red"></span>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionType" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionType">
				  <option></option>
					<c:forEach items="${transactionTypeList}" var="transactionType">
						<option>${transactionType.value}</option>
					</c:forEach>
				  <%--<option>已有业务</option>
				  <option>新业务</option>--%>
				</select>
			</div>
			<label for="create-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-possibility">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-clueSource" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-clueSource">
				  <option></option>
					<c:forEach items="${sourceList}" var="source">
						<option>${source.value}</option>
					</c:forEach>
				  <%--<option>广告</option>
				  <option>推销电话</option>
				  <option>员工介绍</option>
				  <option>外部介绍</option>
				  <option>在线商场</option>
				  <option>合作伙伴</option>
				  <option>公开媒介</option>
				  <option>销售邮件</option>
				  <option>合作伙伴研讨会</option>
				  <option>内部研讨会</option>
				  <option>交易会</option>
				  <option>web下载</option>
				  <option>web调研</option>
				  <option>聊天</option>--%>
				</select>
			</div>
			<label for="create-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#findMarketActivity"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input readonly activityid="" type="text" class="form-control" id="create-activitySrc">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#findContacts"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input readonly contactsid="" type="text" class="form-control" id="create-contactsName">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-describe"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input readonly type="text" class="form-control" id="create-nextContactTime">
			</div>
		</div>
		
	</form>
</body>
</html>