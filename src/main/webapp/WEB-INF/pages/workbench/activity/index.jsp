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

	<%--先引进bootstrap框架的css包	--%>
	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<%--日历插件的css包	--%>
	<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css"
		  type="text/css" rel="stylesheet" />
	<%--分页插件的css部分	--%>
	<link href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css"
	      type="text/css" rel="stylesheet"/>

	<%--顺序不能随便改变，bootstrap框架基于jquery框架写的，
		bootstrap-datetimepicker插件又是基于bootstrap框架的，
		所以顺序是 jquery-->bootstrap-->bootstrap-datetimepicker
		最后再引入bootstrap-datetimepicker这个日历插件的语言包
		--%>
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<%--分页插件 js包和语言包	--%>
	<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

	<script type="text/javascript">


		var globalPageSize = 3
		var globalPageNum = 1
		$(function(){
			//给日期相关的框添加日历插件()
			$("#startDate,#endDate,#queryStartDate,#queryEndDate,#edit-startTime,#edit-endTime").datetimepicker({
				language:'zh-CN',//指定语言格式
				format:'yyyy-mm-dd',//指定用户选中某个日期之后的日期格式展示
				minView:'month',//展示最小视图，month就表示最细能展示到月的全部天数
				autoclose:true,//用户选中某个日期后，自动关闭
			})


			//页面加载完毕，查询活动列表
			queryActivityForConditionByPage(1,3);

			//点击查询按钮，把条件发送到处理器进行 条件查询
			//查询到的结果显示到活动列表，属于局部刷新
			$("#queryActivityBtn").click(function (){

				//发送条件

				//如何保持每页显示条数不变？两种方法
				//1、使用全局变量
				queryActivityForConditionByPage(1,globalPageSize)
				//2、使用专门的函数：
				// jq对象.bs_pagination("getOption",配置对象的属性名)即可获取到配置对象对应的属性的值
				// "getOption"是固定写法，表示获取到分页插件的配置对象
				// queryActivityForConditionByPage(1,$("#pageDiv").bs_pagination("getOption","rowsPerPage"))

				//最后记得防止原来表单的提交
				return false;
			})

			//点击创建按钮，手动弹出创建市场活动的模态窗口
			$("#createActivityBtn").click(function () {
				//在弹出模态窗口前，清空之前的表单信息
				$("#activityOwner").val("")
				$("#activityName").val("")
				$("#startDate").val("")
				$("#endDate").val("")
				$("#cost").val("")
				$("#description").val("")

				//弹出模态窗口
				$("#createActivityModal").modal("show");

				//定义在这里。。。

			})

			//点击保存事件，点击后进行表单验证，发送ajax请求....
			//这个点击事件的绑定不要在 "模态窗口点击事件的回调函数" 中声明
			//不然每次点击点击"创建"按钮，都会进行一次点击事件的绑定
			//导致出现，点击一次，执行多次的回调函数
			//所以得出结论：不要在一个事件(除了"页面加载完毕事件")的回调函数中的函数体中又进行事件的绑定
			$("#saveActivityBtn").click(function () {
				//对需要提交的信息进行验证
				//所有者和名称不能为空
				let owner = $("#create-marketActivityOwner").val().trim();
				if (owner===''){
					$("#message").html("所有者不能为空")
					return;
				}
				let name = $("#activityName").val().trim();
				if (name===''){
					$("#message").html("活动名称不能为空")
					return;
				}
				//如果开始日期和结束日期都不为空,则结束日期不能比开始日期小
				let startDate = $("#startDate").val();
				let endDate = $("#endDate").val();
				if (startDate!==''&&endDate!==''){
					if (startDate>endDate){
						$("#message").html("结束日期不能比开始日期小")
						return;
					}
				}

				//成本只能为非负整数
				let cost = $("#cost").val().trim()
				const costRegExp = /^([1-9]\d*)|([1-9]\d*\.0)$/;
				if (!costRegExp.test(cost)){
					$("#message").html("成本只能为非负整数")
					return;
				}

				let description = $("#description").val().trim()

				//表单验证通过后，把信息发送给后端作验证，关闭模态窗口
				//活动列表部分进行刷新，属于局部刷新，故发送Ajax请求
				$.post("workbench/activity/saveActivity",{
					owner,name,startDate,endDate,cost, description
				},data=>{
					if (data.code==="0"){
						//保存失败
						alert("系统繁忙，请稍后重试......")
					}else{
						//保存成功，关闭模态窗口，刷新活动列表
						$("#createActivityModal").modal("hide");
						queryActivityForConditionByPage(1,globalPageSize,true)
					}
				},"json")
			})


			//全选 全不选
			$("#selectAllActivity").change(function () {
				//全选框的状态和这一页所有数据的复选框状态一致
				// #activityBody对应元素下的所有复选框元素
				$("#activityBody :checkbox").prop("checked",this.checked)
			})

			//当这一页数据全部选上时，全选框也选上
			$("#activityBody").on("click",":checkbox",function () {
				//获取复选框选中的数量
				let checkedCount = $("#activityBody :checked").length
				//总数量
				let totalCount = $("#activityBody :checkbox").length
				$("#selectAllActivity").prop("checked",checkedCount===totalCount)

			})


			//点击删除按钮，判断用户是否选中至少一条记录，弹出“确定删除”
			//根据用户点击情况进行删除操作
			$("#deleteActivityBtn").click(function () {
				//至少选中一条
				let activityChecked = $("#activityBody :checked");
				if (activityChecked.length===0){
					alert("请至少选中一条需要删除的记录")
					return;
				}
				//提醒用户是否删除，如果点击确定，就发送请求
				if (confirm("你确定要删除吗")){
					//收集需要删除的活动id
					let ids=[]
					for (let i = 0; i < activityChecked.length; i++) {
						if (activityChecked[i].checked){
							ids.push(activityChecked[i].value)
						}
					}

					//发送ajax请求
					$.ajax({
						url:"workbench/activity/deleteActivityByIds",
						type:"post",
						data:{
							ids:ids
						},
						success:function (data){
							if (data.code==="1"){
								//刷新页面
								queryActivityForConditionByPage(1,globalPageSize)
								//把全选框的状态更新
								$("#selectAllActivity").prop("checked",false)
							}else{
								alert(data.message)
							}
						},
						dataType:"json",
						// 原来：ids[]=xxx&ids[]=xxx&ids[]=xxx
						// 加了下面这个属性后：ids=xxx&ids=xxx&ids=xxx
						// 这样后端直接用 String[] ids接收即可
						traditional:true

					})

					/*$.post("workbench/activity/deleteActivityByIds",{
						ids,
					},data=>{
						if (data.code==="1"){
							//刷新页面
							queryActivityForConditionByPage(1,globalPageSize)
							//把全选框的状态更新
							$("#selectAllActivity").prop("checked",false)
						}else{
							alert(data.message)
						}
					}, "json")*/


				}
			})

			//修改模块
			//点击修改按钮，判断是否选中并且只选中一条记录
			let id;
			$("#updateActivityModalBtn").click(function () {

				$("#edit-marketActivityOwner,#edit-marketActivityName,#edit-startTime,#edit-endTime,#edit-cost,#edit-describe").focus(function () {
					$("#edit-message").html("")
				})

				let activityChecked = $("#activityBody :checked")
				let checkedCount = activityChecked.length
				if (checkedCount===0){
					alert("请选中一条活动记录")
					return;
				}
				if (checkedCount>1){
					alert("最多只能选中一条记录")
					return;
				}
				id=activityChecked.val()
				//符合条件
				//1、先根据用户id查询出用户选中的活动信息，展示在模态窗口中
				//发送Ajax请求
				$.post("workbench/activity/queryActivityById",{
					id
				},data=>{
					// console.log(data.returnData)
					if (data.code==="1"){
						//把数据展示在模态窗口中
						let activity = data.returnData;
						$("#edit-marketActivityOwner").val(activity.owner)
						$("#edit-marketActivityName").val(activity.name)
						$("#edit-startTime").val(activity.startDate)
						$("#edit-endTime").val(activity.endDate)
						$("#edit-cost").val(activity.cost)
						$("#edit-describe").val(activity.description)
					}else{
						alert("系统繁忙，请稍后重试.....")
					}
					//2、然后再打开模态窗口
					$("#edit-message").html("")
					$("#editActivityModal").modal("show")
				},"json")





			})

			//用户更新信息，点击更新按钮
			//对用户填写的信息进行表单验证
			//没有问题后，再提交请求
			$("#updateActivityBtn").click(function () {
				//进行表单验证
				let owner = $("#edit-marketActivityOwner").val().trim();
				if (owner===''){
					$("#edit-message").html("所有者不能为空")
					return;
				}
				let name = $("#edit-marketActivityName").val().trim();
				if (name===''){
					$("#edit-message").html("活动名称不能为空")
					return;
				}
				//如果开始日期和结束日期都不为空,则结束日期不能比开始日期小
				let startDate = $("#edit-startTime").val();
				let endDate = $("#edit-endTime").val();
				if (startDate!==''&&endDate!==''){
					if (startDate>endDate){
						$("#edit-message").html("结束日期不能比开始日期小")
						return;
					}
				}

				//成本只能为非负整数
				let cost = $("#edit-cost").val().trim()
				const costRegExp = /^([1-9]\d*)|([1-9]\d*\.0)$/;
				if (!costRegExp.test(cost)){
					$("#edit-message").html("成本只能为非负整数")
					return;
				}

				let description = $("#edit-describe").val().trim()

				//表单验证通过后，发送Ajax请求更新数据库信息
				$.post("workbench/activity/modifyActivity",{
					id,owner,name,startDate,endDate,cost,description
				},data=>{
					console.log(data)
					if(data.code==="1"){
						//更新成功，关闭模态窗口，刷新活动列表
						$("#editActivityModal").modal("hide")
						queryActivityForConditionByPage(globalPageNum,globalPageSize)
					}else{
						alert("系统繁忙，请重新尝试.......")
					}
				})
			})

			//批量导出
			$("#exportActivityAllBtn").click(function () {
				if (confirm("你确定要导出全部活动列表信息吗")){
					//下载文件的请求只能是同步请求，因为ajax请求无法处理响应信息是一个文件的情况
					window.location.href = "workbench/activity/activityListDownLoad"
				}
			})

			//选择导出
			$("#exportActivityXzBtn").click(function () {
				//先判断用户是否选中至少一条记录
				let activityChecked = $("#activityBody :checked")
				if (activityChecked.length===0){
					alert("至少选中一条活动记录！！！")
					return;
				}
				//收集用户选中的活动信息的id
				let idStr = ''
				for (let i = 0; i < activityChecked.length; i++) {
					idStr+="id="+activityChecked[i].value+"&";
				}
				// alert(idStr)
				//把最后的 ’&‘ 符号去掉
				idStr = idStr.substring(0,idStr.length-1);
				if (confirm("你确定要导出选中的活动列表信息吗")){
					//下载文件的请求只能是同步请求，因为ajax请求无法处理响应信息是一个文件的情况
					window.location.href = "workbench/activity/activityListDownLoadByIds?"+idStr
				}



			})

			//上传文件
			$("#importActivityBtn").click(function () {
				//点击导入按钮，获取文件对象，进行后缀名以及大小的验证
				//在js的组件标签（<input type="file"/> ）对应的DOM对象中，有一个files属性
				//files是一个数组，存储了 给这个标签上传的多个文件所对应的文件对象
				//所以理论上你可以一次性上传多个文件
				//但现在市面上的浏览器基本上只支持一次上传一个文件，所以我们取第一个文件对象即可
				let activityFile = $("#activityFile")[0].files[0]
				//获取属性名做验证
				//activityFile.name :获取这个文件的文件名
				//从该文件名的最后一个'.'字符的下标+1 开始截取，截取到最后
				let activityFileSuffix = activityFile.name.substr(activityFile.name.lastIndexOf('.')+1)
				if (activityFileSuffix!=="xls"&&activityFileSuffix!=="xlsx"){
					alert("只能上传Excel文件！！！")
					return;
				}

				//文件大小验证
				if (activityFile.size>1024*1024*5){
					alert("文件大小不能超过5M")
					return;
				}
				//表单验证通过，发送Ajax请求
				//关键点：data属性填什么？？
				//以前学习的方式只能提交文本数据
				//以后只要是发送excel,word,图片，视频.....等等二进制文件
				//只能使用FormData对象
				let formData = new FormData()
				//formData.append(name,value)
				//name需要和后端的处理器方法形参一致!!!
				formData.append("activityFile",activityFile)
				$.ajax({
					url:"workbench/activity/activityListUpLoad",
					data:formData,
					//ajax请求默认的也是把你上传的数据
					//全部转换成字符串的形式，再按照urlencoded编码格式进行编码
					//最终得到数据：参数名1=xxx&参数名2=xxx&参数名3=xxx...

					//所以为了阻止上述的默认行为，上传文件还需要补充两个属性
					processData:false,//阻止浏览器把对象中的数据转换为字符串
					//contentType代表前端发送数据时的数据格式（编码格式）,也就是请求头中的Content-type属性
					//值是true时，表示采用application/x-www-form-urlencoded编码
					//改为false后，不采用application/x-www-form-urlencoded编码
					//将采用multipart/form-data编码格式
					contentType:false,
					type: "post",
					dataType: "json",
					success:function (data) {
						if (data.code==="1"){
							let count = data.returnData;
							alert("成功导入"+count+"条活动记录！！!");
							//刷新活动列表
							queryActivityForConditionByPage(1,
									$("#pageDiv").bs_pagination("getOption","rowsPerPage"))
							//关闭模态窗口
							$("#importActivityModal").modal("hide")
						}else{
							alert(data.message)
						}
					}
				})


			})

			//拼字符串太折磨了，还是这样做把......
			$("#activityBody").on("click","a",function () {
				window.location.href = "workbench/activity/toDetail?id="+$(this).attr("activityid")
			})

		});


		//在入口函数外面定义查询活动列表的函数
		function queryActivityForConditionByPage(pageNum,pageSize,flag) {
			$("#selectAllActivity").prop("checked",false)

			let name = ''
			let owner = ''
			let startDate = ''
			let endDate = ''

			//flag为true代表需要展示全部活动，为false代表需要收集信息
			if (!flag){
				//收集信息
				name = $("#queryName").val().trim()
				owner = $("#queryOwner").val().trim()
				startDate = $("#queryStartDate").val().trim()
				endDate = $("#queryEndDate").val().trim()
			}
			$.get("workbench/activity/queryActivityForConditionByPage",{
				pageNum: pageNum,
				pageSize: pageSize,
				name,owner,startDate,endDate
			},data=>{
				//开始拼字符串咯.....
				let page = data.returnData;
				globalPageSize = pageSize
				globalPageNum = pageNum
				let activityList = page.list;
				let html = ""
				for (let i = 0; i < activityList.length; i++) {
					html+="<tr class=\"active\">"+
							"<td><input type=\"checkbox\"  value='"+activityList[i].id+"'/></td>"+
							"<td><a style=\"text-decoration: none; cursor: pointer;\" activityid='"+activityList[i].id+"'>"+activityList[i].name+"</a></td>"+
							"<td>"+activityList[i].owner+"</td>"+
							"<td>"+activityList[i].startDate+"</td>"+
							"<td>"+activityList[i].endDate+"</td>"+
							"</tr>"
				}
				$("#activityBody").html(html);
				//加载分页相关信息
				// jq对象.bs_pagination(配置对象)
				$("#pageDiv").bs_pagination({
					currentPage:pageNum, //当前页码
					rowsPerPage:pageSize, //每页显示条数
					totalRows: page.total, //总条数
					totalPages: page.pages, //总页数
					visiblePageLinks: 5,  //导航页数,在前端进行控制即可

					showGoToPage: true, //是否展示“跳转到”部分，默认true，
					showRowsPerPage: true,//是否展示“每页展示条数”,默认true
					showRowsInfo: true,  //是否展示记录信息，默认true

					onChangePage(event, pageObj) {
						//当页码或者每页条数发生改变时，执行这个函数

						queryActivityForConditionByPage(pageObj.currentPage,pageObj.rowsPerPage,flag)


					}
				})
			},"json")
		}
	
	</script>
</head>
<body>
	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityForm" class="form-horizontal" role="form" action="workbench/activity/saveActivity">
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
									<c:forEach items="${userList}" var="user">
										<c:if test="${user.name.equals(sessionScope.user.name)}">
											<option selected value="${user.id}">${user.name}</option>
										</c:if>
										<c:if test="${!user.name.equals(sessionScope.user.name)}">
											<option value="${user.id}">${user.name}</option>
										</c:if>
									</c:forEach>
									<%--<option>zhangsan</option>
									<option selected>lisi</option>
									<option>wangwu</option>--%>
								</select>
							</div>
                            <label for="activityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="activityName" name="name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input readonly type="text" class="form-control" id="startDate" name="startDate">
							</div>
							<label for="endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input readonly type="text" class="form-control" id="endDate" name="endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="cost" name="cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="description" name="description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<span id="message" style="color: red"></span>
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="saveActivityBtn" type="button" class="btn btn-primary">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>

								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input readonly type="text" class="form-control" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input readonly type="text" class="form-control" id="edit-endTime" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<span id="edit-message" style="color: red"></span>
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="updateActivityBtn" type="button" class="btn btn-primary" >更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
					<span id="importFileMessage" style="color: darkgreen;font-size: 25px"></span>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
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
				      <input class="form-control" type="text" id="queryName">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="queryOwner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="queryStartDate" readonly />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="queryEndDate" readonly>
				    </div>
				  </div>
				  
				  <button type="submit" class="btn btn-default" id="queryActivityBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button id="createActivityBtn" onclick="$('#message').html('')" type="button" class="btn btn-primary" ><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button id="updateActivityModalBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button id="deleteActivityBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="selectAllActivity"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<c:forEach items="${page.list}" var="activity">
							<tr class="active">
								<td><input type="checkbox" /></td>
								<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">${activity.name}</a></td>
								<td>${activity.owner}</td>
								<td>${activity.startDate}</td>
								<td>${activity.endDate}</td>
							</tr>
						</c:forEach>--%>
                        <%--<tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
				<div id="pageDiv"></div>
			</div>

		</div>

	</div>


</body>
</html>