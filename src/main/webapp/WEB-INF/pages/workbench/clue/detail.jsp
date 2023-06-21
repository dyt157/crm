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
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	
	<script type="text/javascript">
	
		//默认情况下取消和保存按钮是隐藏的
		var cancelAndSaveBtnDefault = true;
		
		$(function(){
			$("#clueRemark").focus(function(){
				if(cancelAndSaveBtnDefault){
					//设置remarkDiv的高度为130px
					$("#remarkDiv").css("height","130px");
					//显示
					$("#cancelAndSaveBtn").show("2000");
					cancelAndSaveBtnDefault = false;
				}
			});
			
			$("#cancelBtn").click(function(){
				//显示
				$("#cancelAndSaveBtn").hide();
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","90px");
				cancelAndSaveBtnDefault = true;
			});

			//为了避免新添加的备注信息无法显示修改，删除按钮，以下事件的绑定均采用on()方法
			$("#clueRemarkDiv").on("mouseover",".remarkDiv",function () {
				$(this).children("div").children("div").show();
			})
			$("#clueRemarkDiv").on("mouseout",".remarkDiv",function () {
				$(this).children("div").children("div").hide();
			})
			$("#clueRemarkDiv").on("mouseout",".myHref",function () {
				$(this).children("span").css("color","#E6E6E6");
			})
			$("#clueRemarkDiv").on("mouseover",".myHref",function () {
				$(this).children("span").css("color","red");
			})
			// $(".remarkDiv").mouseover(function(){
			// 	$(this).children("div").children("div").show();
			// });
			
			// $(".remarkDiv").mouseout(function(){
			// 	$(this).children("div").children("div").hide();
			// });
			
			// $(".myHref").mouseover(function(){
			// 	$(this).children("span").css("color","red");
			// });
			//
			// $(".myHref").mouseout(function(){
			// 	$(this).children("span").css("color","#E6E6E6");
			// });

			//保存线索备注
			$("#saveClueRemarkBtn").click(function () {
				//备注不能为空
				let noteContent = $("#clueRemark").val().trim()
				if (noteContent==""){
					alert("备注信息不能为空")
					return;
				}
				let clueId = "${clue.id}"
				//发送ajax请求
				$.post("workbench/clue/saveClueRemark",{
					noteContent,clueId
				},data=>{
					if (data.code==="1"){
						let clueRemark = data.returnData;
						let name = "${clue.fullname}${clue.appellation}"
						let company = "${clue.company}"
						let html = '<div id="div_'+clueRemark.id+'" class="remarkDiv" style="height: 60px;">'+
										'<img title="'+clueRemark.createBy+'" src="image/user-thumbnail.png" style="width: 30px; height:30px;">'+
										'<div style="position: relative; top: -40px; left: 40px;" >'+
											'<h5 id="noteContent_'+clueRemark.id+'">'+clueRemark.noteContent+'</h5>'+
											'<font color="gray">线索</font> <font color="gray">-</font> <b>'+name+'-'+company+'</b> <small style="color: gray;" id="small_'+clueRemark.id+'"> '+clueRemark.createTime+' 由 '+clueRemark.createBy+' 创建</small>'+
											'<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">'+
												'<a clueremarkid="'+clueRemark.id+'" class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>'+
												'&nbsp;&nbsp;&nbsp;&nbsp;'+
												'<a clueremarkid="'+clueRemark.id+'" name="deleteA" class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>'+
											'</div>'+
										'</div>'+
								    '</div>'
						//把html字符串拼接到$("#remarkDiv")的前面
						$(html).insertBefore($("#remarkDiv"))
						// $("#remarkDiv").before(html)  //这样也行
						//清空文本框
						$("#clueRemark").val("")
						//隐藏保存取消按钮
						$("#cancelAndSaveBtn").hide()
						cancelAndSaveBtnDefault = true;
					}else{
						alert(data.message)
					}
				},"json")
			})
			let id;
			//点击修改按钮,把内容加载到模态窗口中，再弹出模态窗口
			//采用on方法
			$("#clueRemarkDiv").on("click",".myHref:even",function () {
				id =  $(this).attr("clueremarkid")
				//加载内容
				$("#clueNoteContent").val($(this).parent().parent().find("h5").text())
				//弹出窗口
				$("#editClueRemarkModal").modal("show")
			})
			/*$(".myHref:even").click(function () {
				id =  $(this).attr("clueremarkid")
				//加载内容
				$("#clueNoteContent").val($(this).parent().parent().find("h5").text())
				//弹出窗口
				$("#editClueRemarkModal").modal("show")
			})*/

			//点击更新，发送ajax请求更新备注
			$("#updateClueRemarkBtn").click(function () {
				//要发送什么？？
				//1、noteContent
				let noteContent = $("#clueNoteContent").val().trim()
				if (noteContent==""){
					$("#updateClueRemarkTip").html("备注不能为空")
					return;
				}
				//2、线索备注的id,上面已经获取
				//发送ajax请求
				$.ajax({
					url:"workbench/clue/modifyClueRemark",
					data:{
						noteContent,id
					},
					type:"post",
					dataType:"json",
					success:function (data) {
						if (data.code==="1"){
							let clueRemark = data.returnData
							//更新备注列表中的数据
							//需要更新什么？？
							//1、h5标签中的文本 noteContent
							$("#noteContent_"+id).html(clueRemark.noteContent)
							//创建人-->编辑人  创建时间-->编辑时间
							$("#small_"+id).html(" "+clueRemark.editTime+" 由 "+clueRemark.editBy+" 编辑")

							//关闭模态窗口
							$("#editClueRemarkModal").modal("hide")

						}else{
							alert(data.message)
						}
					}
				})
			})


			//删除备注
			$("#clueRemarkDiv").on("click","[name='deleteA']",function () {
				let id = $(this).attr("clueremarkid")
				if (confirm("你确定要删除吗")){
					$.ajax({
						url:"workbench/clue/deleteClueRemarkById",
						data:{
							id,
						},
						type:"post",
						dataType:"json",
						success:function (data) {
							if (data.code==="1"){
								//移除对应的备注
								$("#div_"+id).remove()
							}else{
								alert(data.message)
							}
						}
					})
				}
			})

			//用户点击“关联市场活动”
			//清空输入框，清空之前查询出来的列表，清空所有提示信息
			$("#openActivityModal").click(function () {
				$("#queryActivityInput").val("")
				$("#relationTip").html("")
				$("#activityTable tbody").html("")
				//打开窗口
				$("#bundModal").modal("show")
			})

			//关联市场活动模块
			//1、根据活动名字查询活动列表
			$("#queryActivityInput").keyup(function (event) {
				if (event.key=="Enter"){
					event.preventDefault();
				}
				//收集信息
				let name = this.value.trim()
				$.ajax({
					url:"workbench/clue/queryActivityByName",
					data:{name},
					type:"post",
					dataType:"json",
					success:function (data) {
						if (data.code==="1"){
							let activityList = data.returnData;
							let html='';
							$.each(activityList,function (i,activity) {
								html+='<tr>'+
											'<td><input type="checkbox" value="'+activity.id+'"/></td>'+
											'<td id="name_'+activity.id+'">'+activity.name+'</td>'+
											'<td>'+activity.startDate+'</td>'+
											'<td>'+activity.endDate+'</td>'+
											'<td>'+activity.owner+'</td>'+
											'<td name="relationTd" style="color: red" id="relationTd_'+activity.id+'"></td>'+
										'</tr>'
							})
							$("#activityTable tbody").html(html)
						}else{
							//查询不出来，就清空原来的内容
							$("#activityTable tbody").html("")
						}
					}
				})


			})
			//用户点击活动前面的复选框，发送ajax请求，验证是否已经存在关联关系
			$("#activityTable tbody").on("change",":checkbox",function () {
				//每点击一次，取得所有被选中的活动jq对象,逐个进行验证
				let activityChecked = $("#activityTable tbody :checked")
				for (let i = 0; i < activityChecked.length; i++) {
					//发送ajax请求
					$.ajax({
						url:"workbench/clue/queryClueActivityRelationByClueIdAndActivityId",
						data:"clueId=${clue.id}&activityId="+activityChecked[i].value,
						type:"post",
						dataType:"json",
						success:function (data) {
							if (data.code==="0"){
								$("#relationTd_"+activityChecked[i].value).html("已关联")
							}
						}
					})
				}

			})
			//用户点击复选框后，提示信息删除
			$("#activityTable tbody").on("click",":checkbox",function () {
				$("#relationTip").html("")
				if (!this.checked){
					$(this).parent().parent().find(":last").html("")
				}
			})
			$("#selectAllActivity").click(function () {
				$("#relationTip").html("")
			})


			//用户选择对应的市场活动，点击关联，发送请求
			$("#relationBtn").click(function () {
				//验证查询出来的活动记录后面都没有"已关联"的字样，才能发送请求
				let $relationTd = $("[name='relationTd']")
				let flag = true;
				for (let i = 0; i < $relationTd.length; i++) {
					if ($relationTd[i].innerText!=''){
						flag=false;
						break;
					}
				}
				if (!flag){
					$("#relationTip").html("已有活动被关联了")
					return;
				}
				//至少选中一条
				let activityChecked = $("#activityTable tbody :checked")
				if (!activityChecked.length){
					$("#relationTip").html("至少选中一条")
					return;
				}
				//发送ajax请求，数据：选中的市场活动id，线索id
				//市场活动id
				let activityIdStr=""
				for (let i = 0; i < activityChecked.length; i++) {
					activityIdStr+="activityIds="+activityChecked[i].value+"&"
				}
				activityIdStr = activityIdStr.substring(0,activityIdStr.length-1)
				$.post("workbench/clue/saveClueActivityRelation",
				"clueId=${clue.id}&"+activityIdStr,data=>{
					if (data.code==="1"){
						//刷新关联市场活动列表
						let activityList = data.returnData
						//拼接数据到原来的列表中
						let activityHtml='';
						$.each(activityList,function (i, activity) {
							activityHtml+='<tr id="tr_'+activity.id+'">'+
												'<td id="name_'+activity.id+'">'+activity.name+'</td>'+
												'<td>'+activity.startDate+'</td>'+
												'<td>'+activity.endDate+'</td>'+
												'<td>'+activity.owner+'</td>'+
												'<td><a activityid="'+activity.id+'" href="javascript:void(0);"  style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>'+
										  '</tr>'
						})
						//追加到tbody中子元素的后面
						$(activityHtml).appendTo($("#relationActivityBody"))
						//关闭窗口
						$("#bundModal").modal("hide")
					}else{
						alert(data.message)
					}
				},"json")

			})

			//解除关联模块
			$("#relationActivityBody").on("click","a",function () {
				let activityId = $(this).attr("activityid")
				let activityName = $("#name_"+activityId).text()
				if (confirm("你确定要解除与["+activityName+"]的关联关系吗")){
					$.post("workbench/clue/releaseContact",{
						clueId:"${clue.id}",
						activityId,
					},data=>{
						if (data.code==="1"){
							//移除该市场活动
							$("#tr_"+activityId).remove()
						}else {
							alert(data.message)
						}
					},"json")
				}
			})


		});
		
	</script>

</head>
<body>

	<!-- 修改线索活动备注的模态窗口 -->
	<div class="modal fade" id="editClueRemarkModal" role="dialog">
	<%-- 备注的id --%>
		<input type="hidden" id="remarkId">
		<div class="modal-dialog" role="document" style="width: 40%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改备注</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<div class="form-group">
							<label for="clueNoteContent" class="col-sm-2 control-label">内容</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="clueNoteContent"></textarea>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<span id="updateClueRemarkTip" style="color: red"></span>
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateClueRemarkBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 关联市场活动的模态窗口 -->
	<div class="modal fade" id="bundModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">关联市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form" onkeydown="if(event.key=='Enter'){return false;}">
						  <div class="form-group has-feedback" >
						    <input id="queryActivityInput" type="text" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td><input type="checkbox" id="selectAllActivity"/></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody>
							<%--<tr>
								<td><input type="checkbox"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="checkbox"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<span id="relationTip" style="color: red"></span>
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="relationBtn">关联</button>
				</div>
			</div>
		</div>
	</div>


	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>${clue.fullname}${clue.appellation}<small>${clue.company}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 500px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" onclick="window.location.href='workbench/clue/toConvert?clueId=${clue.id}';"><span class="glyphicon glyphicon-retweet"></span> 转换</button>
			
		</div>
	</div>
	
	<br/>
	<br/>
	<br/>

	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.fullname}${clue.appellation}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.owner}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">公司</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.company}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">职位</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.job}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">邮箱</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.email}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">公司座机</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.phone}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">公司网站</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.website}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">手机</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.mphone}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">线索状态</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.state}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">线索来源</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.source}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${clue.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${clue.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${clue.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${clue.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 70px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					<c:if test="${empty clue.description}">
						无描述
					</c:if>
					<c:if test="${not empty clue.description}">
						${clue.description}
					</c:if>
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 80px;">
			<div style="width: 300px; color: gray;">联系纪要</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					<c:if test="${empty clue.contactSummary}">
						无联系纪要
					</c:if>
					<c:if test="${not empty clue.contactSummary}">
						${clue.contactSummary}
					</c:if>
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 90px;">
			<div style="width: 300px; color: gray;">下次联系时间</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;">
				<b>
					<c:if test="${empty clue.nextContactTime}">
						无联系时间
					</c:if>
					<c:if test="${not empty clue.nextContactTime}">
						${clue.nextContactTime}
					</c:if>

				</b>
			</div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px; "></div>
		</div>
        <div style="position: relative; left: 40px; height: 30px; top: 100px;">
            <div style="width: 300px; color: gray;">详细地址</div>
            <div style="width: 630px;position: relative; left: 200px; top: -20px;">

                <b>
					<c:if test="${empty clue.address}">
						无地址记录
					</c:if>
					<c:if test="${not empty clue.address}">
						${clue.address}
					</c:if>

                </b>
            </div>
            <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
        </div>
	</div>
	
	<!-- 备注 -->
	<div style="position: relative; top: 40px; left: 40px;" id="clueRemarkDiv">
		<div class="page-header">
			<h4>备注</h4>
		</div>

		<c:forEach items="${clueRemarkList}" var="clueRemark">
			<div id="div_${clueRemark.id}" class="remarkDiv" style="height: 60px;">
				<img title="${clueRemark.createBy}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
				<div style="position: relative; top: -40px; left: 40px;" >
					<h5 id="noteContent_${clueRemark.id}">${clueRemark.noteContent}</h5>
					<font color="gray">线索</font> <font color="gray">-</font> <b>${clue.fullname}${clue.appellation}-${clue.company}</b>
					<small style="color: gray;" id="small_${clueRemark.id}">
					<c:if test="${empty clueRemark.editBy and empty clueRemark.editTime}">
						${clueRemark.createTime} 由 ${clueRemark.createBy} 创建
					</c:if>
					<c:if test="${not empty clueRemark.editBy and not empty clueRemark.editTime}">
						${clueRemark.editTime} 由 ${clueRemark.editBy} 编辑
					</c:if>
					</small>
					<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
						<a clueremarkid="${clueRemark.id}" class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a  clueremarkid="${clueRemark.id}" name="deleteA" class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
					</div>
				</div>
			</div>
		</c:forEach>
		<!-- 备注1 -->
		<%--<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">线索</font> <font color="gray">-</font> <b>李四先生-动力节点</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>--%>
		
		<!-- 备注2 -->
		<%--<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">线索</font> <font color="gray">-</font> <b>李四先生-动力节点</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>--%>
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="clueRemark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button id="saveClueRemarkBtn" type="button" class="btn btn-primary">保存</button>
				</p>
			</form>
		</div>
	</div>
	
	<!-- 市场活动 -->
	<div>
		<div style="position: relative; top: 60px; left: 40px;">
			<div class="page-header">
				<h4>市场活动</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>名称</td>
							<td>开始日期</td>
							<td>结束日期</td>
							<td>所有者</td>
							<td></td>
						</tr>
					</thead>
					<tbody id="relationActivityBody">
						<c:forEach items="${activityList}" var="activity">
							<tr id="tr_${activity.id}">
								<td id="name_${activity.id}">${activity.name}</td>
								<td>${activity.startDate}</td>
								<td>${activity.endDate}</td>
								<td>${activity.owner}</td>
								<td><a activityid="${activity.id}" id="releaseContactA" href="javascript:void(0);"  style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>
							</tr>
						</c:forEach>
						<%--<tr>
							<td>发传单</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
							<td>zhangsan</td>
							<td><a href="javascript:void(0);"  style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>
						</tr>
						<tr>
							<td>发传单</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
							<td>zhangsan</td>
							<td><a href="javascript:void(0);"  style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>
						</tr>--%>
					</tbody>
				</table>
			</div>
			
			<div>
				<a id="openActivityModal" href="javascript:void(0);" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>关联市场活动</a>
			</div>
		</div>
	</div>
	
	
	<div style="height: 200px;"></div>
</body>
</html>