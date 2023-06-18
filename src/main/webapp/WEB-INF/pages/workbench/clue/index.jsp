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

	<script type="text/javascript">
		let globalPageSize=2;
		let globalPageNum=1;
		$(function(){
			//加载日历插件
			$("#create-nextContactTime,#edit-nextContactTime").datetimepicker({
				language:"zh-CN",
				format:"yyyy-mm-dd",
				minView:"month",
				autoclose:true,
				pickerPosition:"top-right" //日历显示的位置设置在上方
			})


			//创建线索模块
			//点击创建按钮，弹出模态窗口
			$("#createClueModalBtn").click(function () {
				//在弹出窗口前，需要清空之前所填写的所有表单信息，以及提示信息
				$("#createClueModal :input").val("")
				$("#createClueTip").html("")

				//弹出模态窗口
				$("#createClueModal").modal("show")
			})

			//获取焦点时，清空提示信息
			$("#create-clueOwner,#create-company,#create-surname,#create-email").focus(function () {
				$("#createClueTip").html("")
			})

			//点击保存
			$("#saveClueBtn").click(function () {
				//收集信息，作表单验证
				let owner = $("#create-clueOwner").val()
				//alert(owner) null
				if (owner===null){
					$("#createClueTip").html("所有者不能为空")
					return;
				}
				let company = $("#create-company").val()
				if (company===""){
					$("#createClueTip").html("公司名字不能为空")
					return;
				}

				let fullname = $("#create-surname").val()
				if (fullname===""){
					$("#createClueTip").html("姓名不能为空")
					return;
				}

				let email = $("#create-email").val()
				let emailRegExp = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
				if (!emailRegExp.test(email)){
					$("#createClueTip").html("邮箱不符合规范")
					return;
				}

				let job = $("#create-job").val()
				let appellation = $("#create-call").val()
				let phone = $("#create-phone").val()
				let website = $("#create-website").val()
				let mphone = $("#create-mphone").val()
				let state = $("#create-status").val()
				let source = $("#create-source").val()
				let description = $("#create-describe").val()
				let contactSummary = $("#create-contactSummary").val()
				let nextContactTime = $("#create-nextContactTime").val()
				let address = $("#create-address").val()

				//通过表单验证后，发送ajax请求给后台
				$.ajax({
					url:"workbench/clue/saveClue",
					data:{
						owner,
						company,
						fullname,
						email,
						job,
						appellation,
						phone,
						website,
						mphone,
						state,
						source,
						description,
						contactSummary,
						nextContactTime,
						address,
					},
					type:"post",
					dataType:"json",
					success:function (data) {
						if (data.code==="1"){
							//保存成功，关闭模态窗口
							$("#createClueModal").modal("hide")
							//刷新列表
							showClueListAndPage(1,globalPageSize)
						}else{
							alert(data.message)
						}
					}
				})

			})

			//查询出所有的线索信息并分页展示
			showClueListAndPage(1,2);

			//给导航栏的a标签全部绑定单击事件，点击查询对应页数的线索信息
			$("#cluePageDiv").on("click",".pagination a",function () {
				//获取到页码，每页显示条数
				let pageNum = $(this).attr("pageNum");
				let pageSize = $("#pageSizeSpan").text()
				showClueListAndPage(pageNum,pageSize)
				return false;
			})

			//给 选择条数的a标签绑定事件
			$("#cluePageDiv").on("click",".dropdown-menu a",function () {
				//点击了哪个a标签，就查询出对应条数的线索列表
				showClueListAndPage(1,this.innerText)
				return false;
			})

			//修改模块
			//用户选中一条线索，点击修改，查询出这条线索需要的所有信息，再弹出窗口
			$("#editClueModalBtn").click(function (){
				//清空提示信息
				$("#updateClueTip").html("")

				//局部刷新，ajax请求
				//只能选中一条线索
				let clueChecked = $("#clueBody :checked")
				if (clueChecked.length==0){
					alert("请选中一条线索进行修改")
					return;
				}
				if (clueChecked.length>1){
					alert("最多选中一条线索进行修改")
					return;
				}
				//获取线索id
				let id = clueChecked.val()
				$.ajax({
					url:"workbench/clue/queryClueById",
					data:{
						id,
					},
					type:"post",
					dataType:"json",
					success:function (data) {
						if (data.code==="1"){
							//把数据填充到修改线索的模态窗口
							let clue = data.returnData;
							$("#edit-clueOwner").val(clue.owner)
							$("#edit-company").val(clue.company)
							$("#edit-call").val(clue.appellation)
							$("#edit-surname").val(clue.fullname)
							$("#edit-job").val(clue.job)
							$("#edit-email").val(clue.email)
							$("#edit-phone").val(clue.phone)
							$("#edit-website").val(clue.website)
							$("#edit-mphone").val(clue.mphone)
							$("#edit-status").val(clue.state)
							$("#edit-source").val(clue.source)
							$("#edit-describe").val(clue.description)
							$("#edit-contactSummary").val(clue.contactSummary)
							$("#edit-nextContactTime").val(clue.nextContactTime)
							$("#edit-address").val(clue.address)

							//弹出模态窗口
							$("#editClueModal").modal("show")

						}else{
							alert(data.message)
						}
					}
				})
			})
			//获取焦点时，清空提示信息
			$("#edit-clueOwner,#edit-company,#edit-surname,#edit-email").focus(function () {
				$("#updateClueTip").html("")
			})
			//点击更新按钮，表单验证，发送ajax请求
			$("#updateClueBtn").click(function () {
				let id = $("#clueBody :checked").val()
				//收集信息，作表单验证
				let owner = $("#edit-clueOwner").val()
				//alert(owner) null
				if (owner===null){
					$("#updateClueTip").html("所有者不能为空")
					return;
				}
				let company = $("#edit-company").val()
				if (company===""){
					$("#updateClueTip").html("公司名字不能为空")
					return;
				}

				let fullname = $("#edit-surname").val()
				if (fullname===""){
					$("#updateClueTip").html("姓名不能为空")
					return;
				}

				let email = $("#edit-email").val()
				let emailRegExp = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
				if (!emailRegExp.test(email)){
					$("#updateClueTip").html("邮箱不符合规范")
					return;
				}
				let job = $("#edit-job").val()
				let appellation = $("#edit-call").val()
				let phone = $("#edit-phone").val()
				let website = $("#edit-website").val()
				let mphone = $("#edit-mphone").val()
				let state = $("#edit-status").val()
				let source = $("#edit-source").val()
				let description = $("#edit-describe").val()
				let contactSummary = $("#edit-contactSummary").val()
				let nextContactTime = $("#edit-nextContactTime").val()
				let address = $("#edit-address").val()

				//发送ajax请求
				$.post("workbench/clue/modifyClue",{
					id,owner,company,fullname,email,
					job,
					appellation,
					phone,
					website,
					mphone,
					state,
					source,
					description,
					contactSummary,
					nextContactTime,
					address
				},data=>{
					if (data.code==="1"){
						//刷新列表,当前页，当前条数都不变
						showClueListAndPage(globalPageNum,globalPageSize)
						//关闭模态窗口
						$("#editClueModal").modal("hide");
					}else{
						alert(data.message)
					}
				},"json")
			})

			//全选模块
			$("#selectAllClue").click(function () {
				//获取属性值是true/false的属性，需要使用prop 不能使用attr
				$("#clueBody :checkbox").prop("checked",this.checked)
			})
			//
			$("#clueBody").on("click",":checkbox",function () {
				let totalCount = $("#clueBody :checkbox").length
				let checkedCount = $("#clueBody :checked").length
				$("#selectAllClue").prop("checked",totalCount==checkedCount)

			})

			//删除模块
			//至少选中一条
			$("#deleteClueBtn").click(function () {
				let $clueChecked = $("#clueBody :checked")
				if ($clueChecked.length==0){
					alert("至少选中一条线索进行删除!!!")
					return;
				}
				if (confirm("你确定要删除这些线索吗")){
					//收集用户选中的全部线索id
					let ids = []
					for (let i = 0; i < $clueChecked.length; i++) {
						ids.push($clueChecked[i].value)
					}
					// alert(ids)
					//发送ajax请求
					$.ajax({
						url:"workbench/clue/deleteClueByIds",
						data:{ids},
						type:"post",
						dataType:"json",
						traditional:true,
						success:function (data) {
							if(data.code==="1"){
								//刷新线索列表
								showClueListAndPage(1,globalPageSize)
							}else{
								alert(data.message)
							}
						}
					})
				}



			})

			//查询模块
			//点击查询按钮，收集信息，发送ajax请求
			$("#queryClueBtn").click(function () {
				//发送ajax请求
				showClueListAndPage(1,globalPageSize)
				return false;

			})





		})

		function showClueListAndPage(pageNum,pageSize) {
			let fullname = $("#query-name").val().trim()
			let appellation = $("#query-appellation").val().trim()
			let company = $("#query-company").val().trim()
			let mphone = $("#query-mphone").val().trim()
			let phone = $("#query-phone").val().trim()
			let state = $("#query-state").val().trim()
			let source = $("#query-source").val().trim()
			let owner = $("#query-owner").val().trim()
			globalPageSize = pageSize
			globalPageNum = pageNum
			$.ajax({
				url:"workbench/clue/queryClueByCondition",
				data:{
					pageNum:pageNum,
					pageSize:pageSize,
					fullname,
					appellation,
					company,
					mphone,
					phone,
					state,
					source,
					owner,
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if (data.code==="1"){
						let page = data.returnData;
						let clueList = page.list
						//开始拼字符串
						let clueListHtml=''

						$.each(clueList,function (i, clue) {
							clueListHtml+='<tr>'+
									'<td><input type="checkbox" value="'+clue.id+'"/></td>'+
									'<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'detail.html\';">'+clue.fullname+''+clue.appellation+'</a></td>'+
									'<td>'+clue.company+'</td>'+
									'<td>'+clue.phone+'</td>'+
									'<td>'+clue.mphone+'</td>'+
									'<td>'+clue.source+'</td>'+
									'<td>'+clue.owner+'</td>'+
									'<td>'+clue.state+'</td>'+
									'</tr>'
						})
						//导航栏
						let cluePageHtml='<div><button type="button" class="btn btn-default" style="cursor: default;">共<b>'+page.total+'</b>条记录</button> </div>'+
								'<div class="btn-group" style="position: relative;top: -34px; left: 110px;">'+
								'<button type="button" class="btn btn-default" style="cursor: default;">显示</button>'+
								'<div class="btn-group">'+
								'<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">'+
								'<span id="pageSizeSpan">'+pageSize+'</span>'+
								'<span class="caret"></span>'+
								'</button>'+
								'<ul class="dropdown-menu" role="menu">'
						if (pageSize==2){
							cluePageHtml+='<li><a href="#" pageNum="'+pageNum+'">1</a></li>'+
									'<li><a href="#" pageNum="'+pageNum+'">3</a></li>'
						}else if (pageSize==3){
							cluePageHtml+='<li><a href="#" pageNum="'+pageNum+'">1</a></li>'+
									'<li><a href="#" pageNum="'+pageNum+'">2</a></li>'
						}else if (pageSize==1){
							cluePageHtml+='<li><a href="#" pageNum="'+pageNum+'">2</a></li>'+
									'<li><a href="#" pageNum="'+pageNum+'">3</a></li>'
						}

						cluePageHtml+=
								'</ul>'+
								'</div>'+
								'<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>'+
								'</div>'+
								'<div style="position: relative;top: -88px; left: 285px;">'+
								'<nav>'+
								'<ul class="pagination">'
						if (pageNum!=1){
							cluePageHtml+='<li><a href="#" pageNum="1">首页</a></li>'+
									'<li><a href="#" pageNum="'+(page.pageNum-1)+'">上一页</a></li>'
						}

						for (let i = page.navigateFirstPage; i <=page.navigateLastPage; i++) {
							if (pageNum==i){
								cluePageHtml+='<li class="active"><a href="#" pageNum="'+i+'">'+i+'</a></li>'
							}else{
								cluePageHtml+='<li><a href="#" pageNum="'+i+'">'+i+'</a></li>'
							}

						}
						if (pageNum!=page.pages){
							cluePageHtml+='<li><a href="#" pageNum="'+(page.pageNum+1)+'">下一页</a></li>'+
									'<li><a href="#" pageNum="'+page.pages+'">末页</a></li>'

						}
						cluePageHtml+='</ul></nav></div></div>'
						$("#clueBody").html(clueListHtml)
						$("#cluePageDiv").html(cluePageHtml)

					}else{
						$("#clueBody").html("")
						$("#cluePageDiv").html("")
					}
				}
			})
		}
	</script>
<body>

	<!-- 创建线索的模态窗口 -->
	<div class="modal fade" id="createClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">创建线索</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-clueOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-clueOwner">
									<c:forEach items="${userList}" var="user">
										<c:if test="${user.id.equals(sessionScope.user.id)}">
											<option value="${user.id}" selected>${user.name}</option>
										</c:if>
										<c:if test="${!user.id.equals(sessionScope.user.id)}">
											<option value="${user.id}">${user.name}</option>
										</c:if>
									</c:forEach>
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
							<label for="create-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-company">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-call">
								  <option></option>
									<c:forEach items="${appellationList}" var="appellation">
										<option >${appellation.value}</option>
									</c:forEach>
								  <%--<option>先生</option>
								  <option>夫人</option>
								  <option>女士</option>
								  <option>博士</option>
								  <option>教授</option>--%>
								</select>
							</div>
							<label for="create-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-surname">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-job">
							</div>
							<label for="create-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-email">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
							<label for="create-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-website">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-mphone">
							</div>
							<label for="create-status" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-status">
								  <option></option>
									<c:forEach items="${clueStateList}" var="clueState">
										<option >${clueState.value}</option>
									</c:forEach>
								  <%--<option>试图联系</option>
								  <option>将来联系</option>
								  <option>已联系</option>
								  <option>虚假线索</option>
								  <option>丢失线索</option>
								  <option>未联系</option>
								  <option>需要条件</option>--%>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-source">
								  <option></option>
									<c:forEach items="${sourceList}" var="source">
										<option >${source.value}</option>
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
						</div>
						

						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">线索描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input readonly type="text" class="form-control" id="create-nextContactTime">
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>
						
						<div style="position: relative;top: 20px;">
							<div class="form-group">
                                <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address"></textarea>
                                </div>
							</div>
						</div>
					</form>
					
				</div>
				<div class="modal-footer">
					<span id="createClueTip" style="color: red"></span>
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="saveClueBtn" type="button" class="btn btn-primary" >保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改线索的模态窗口 -->
	<div class="modal fade" id="editClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">修改线索</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-clueOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-clueOwner">
									<c:forEach items="${userList}" var="user">
										<c:if test="${user.id.equals(sessionScope.user.id)}">
											<option value="${user.id}" selected>${user.name}</option>
										</c:if>
										<c:if test="${!user.id.equals(sessionScope.user.id)}">
											<option value="${user.id}">${user.name}</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
							<label for="edit-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-company" value="动力节点">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-call">
								  <option></option>
									<c:forEach items="${appellationList}" var="appellation">
										<option >${appellation.value}</option>
									</c:forEach>
								</select>
							</div>
							<label for="edit-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-surname" value="李四">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-job" value="CTO">
							</div>
							<label for="edit-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-email" value="lisi@bjpowernode.com">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone" value="010-84846003">
							</div>
							<label for="edit-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-website" value="http://www.bjpowernode.com">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-mphone" value="12345678901">
							</div>
							<label for="edit-status" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-status">
								  <option></option>
									<c:forEach items="${clueStateList}" var="clueState">
										<option >${clueState.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-source">
								  <option></option>
									<c:forEach items="${sourceList}" var="source">
										<option >${source.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">这是一条线索的描述信息</textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="edit-contactSummary">这个线索即将被转换</textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input readonly type="text" class="form-control" id="edit-nextContactTime" value="2017-05-01">
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address">北京大兴区大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<span id="updateClueTip" style="color: red"></span>
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateClueBtn" >更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>线索列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
					  <select id="query-appellation" style="width: 80px;height: 33px">
						  <option></option>
						  <c:forEach items="${appellationList}" var="appellation">
							  <option>${appellation.value}</option>
						  </c:forEach>
					  </select>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司</div>
				      <input class="form-control" type="text" id="query-company">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司座机</div>
				      <input class="form-control" type="text" id="query-phone">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索来源</div>
					  <select class="form-control" id="query-source">
					  	  <option></option>
						  <c:forEach items="${sourceList}" var="source">
							  <option >${source.value}</option>
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
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>
				  
				  
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">手机</div>
				      <input class="form-control" type="text" id="query-mphone">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索状态</div>
					  <select class="form-control" id="query-state">
					  	<option></option>
						  <c:forEach items="${clueStateList}" var="clueState">
							  <option >${clueState.value}</option>
						  </c:forEach>
					  	<%--<option>试图联系</option>
					  	<option>将来联系</option>
					  	<option>已联系</option>
					  	<option>虚假线索</option>
					  	<option>丢失线索</option>
					  	<option>未联系</option>
					  	<option>需要条件</option>--%>
					  </select>
				    </div>
				  </div>

				  <button id="queryClueBtn" type="submit" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 40px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button id="createClueModalBtn" type="button" class="btn btn-primary" ><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button id="editClueModalBtn" type="button" class="btn btn-default" ><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button id="deleteClueBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 50px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="selectAllClue"/></td>
							<td>名称</td>
							<td>公司</td>
							<td>公司座机</td>
							<td>手机</td>
							<td>线索来源</td>
							<td>所有者</td>
							<td>线索状态</td>
						</tr>
					</thead>
					<tbody id="clueBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">李四先生</a></td>
							<td>动力节点</td>
							<td>010-84846003</td>
							<td>12345678901</td>
							<td>广告</td>
							<td>zhangsan</td>
							<td>已联系</td>
						</tr>--%>
                        <%--<tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">李四先生</a></td>
                            <td>动力节点</td>
                            <td>010-84846003</td>
                            <td>12345678901</td>
                            <td>广告</td>
                            <td>zhangsan</td>
                            <td>已联系</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 60px;" id="cluePageDiv">
				<%--<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>
			
			</div>
		</div>
	</div>
</body>
</html>