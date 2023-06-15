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
			$("#remark").focus(function(){
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

			//事件的绑定得使用on函数，动态绑定
			$("#activityRemarkDiv").on("mouseover",".remarkDiv",function(){
				$(this).children("div").children("div").show();
			})
			$("#activityRemarkDiv").on("mouseout",".remarkDiv",function(){
				$(this).children("div").children("div").hide();
			})
			$("#activityRemarkDiv").on("mouseover",".myHref",function(){
				$(this).children("span").css("color","red");
			})
			$("#activityRemarkDiv").on("mouseout",".myHref",function(){
				$(this).children("span").css("color","#E6E6E6");
			})

			/*$(".remarkDiv").mouseover(function(){
				$(this).children("div").children("div").show();
			});*/
			
			/*$(".remarkDiv").mouseout(function(){
				$(this).children("div").children("div").hide();
			});
			
			$(".myHref").mouseover(function(){
				$(this).children("span").css("color","red");
			});
			
			$(".myHref").mouseout(function(){
				$(this).children("span").css("color","#E6E6E6");
			});*/

			//页面加载完毕后，发送ajax请求获取活动的备注信息
			queryActivityRemarkList();

			//点击保存按钮，把信息发送给后端
			$("#saveActivityRemarkBtn").click(function () {
				//收集信息
				let noteContent = $("#remark").val().trim()
				//备注信息不能为空
				if (noteContent===''){
					alert("备注信息不能为空")
					return;
				}

				//因为在点击保存按钮后，需要局部刷新备注信息，所以是异步请求
				$.ajax({
					url:"workbench/activity/saveActivityRemark",
					data: {
						noteContent,
						activityId:"${activity.id}"
					},
					type:"post",
					dataType:"json",
					success:function (data) {
						if (data.code==="1"){
							//保存成功，清空文本框
							$("#remark").val("")
							//刷新备注列表
							queryActivityRemarkList();
						}else{
							alert(data.message)
						}
					}
				})
			})


			//备注信息的修改按钮，弹出模态窗口
			//不能写成 "a :even"  这样写取到的元素是所有a标签下的子元素，也就是span元素
			let id;
			$("#activityRemarkDiv").on("click","a:even",function () {
				// alert($(this).parent().parent().find("h5").text()) //这样也行
				id = $(this).attr("activityremarkid")
				let noteContentOriginal = $("#noteContent_"+id).text()
				$("#noteContent").val(noteContentOriginal)
				//弹出模态窗口
				$("#editRemarkModal").modal("show")

			})
			//点击更新按钮，做表单验证，发送ajax请求
			$("#updateRemarkBtn").click(function () {
				let noteContent = $("#noteContent").val().trim()
				if (noteContent===""){
					$("#updateRemarkTip").html("备注信息不能为空")
					return;
				}

				//发送ajax请求
				$.post("workbench/activity/modifyActivityRemark",{
					id,noteContent
				},data=>{
					if (data.code==="1"){
						//刷新页面
						//第一种方法：可以重新查询全部备注信息
						queryActivityRemarkList()
						//第二种方法:replace方法进行替换，不需要全部刷新
						//但是我们这里已经定义了查询所有备注的函数，不好改了
						//关闭模态窗口
						$("#editRemarkModal").modal("hide")
					}else{
						alert(data.message)
					}
				},"json")
			})
			//给文本框绑定回车事件，实现回车也能完成更新
			$("#noteContent").keydown(function (evnet) {
				if (evnet.key==="Enter"){
					//触发点击事件即可
					$("#updateRemarkBtn").click()
				}
			})




			//删除备注
			//第一种方法，选择器写法麻烦，但是不用拼字符串拼到傻逼。。。
			/*$("#activityRemarkDiv").on("click",".myHref:odd",function () {
				console.log(this)
				let id = $(this).attr("activityremarkid")
				//发送Ajax请求
				//......
				return false;
			})*/
			//第二种写法，定义函数,在标签中绑定事件 onclick="定义的函数"
			//并且在入口函数外面定义


		});
		//根据用户id查询用户名字
		function queryUserById(id) {
			let createName;
			$.ajax({
				url:"workbench/activity/queryUserById",
				data:{id},
				type:"post",
				dataType:"json",
				//得使用同步，不然无法成功赋值
				async:false,
				success:function (data) {
					if (data.code==="1"){
						createName = data.returnData;
					}else{
						alert(data.message)
					}
				}
			})
			return createName;
		}


		//删除备注信息的函数
		function deleteActivityRemark(id) {
			if (confirm("你确定要删除这条备注吗")){
				//发送Ajax请求
				$.ajax({
					url:"workbench/activity/deleteActivityRemarkById",
					data:{id},
					type:"post",
					dataType:'json',
					success:function (data) {
						if (data.code==="1"){
							// 刷新页面
							queryActivityRemarkList()
						}else{
							alert(data.message)
						}
					}
				})
			}

		}

		//查询所有备注信息的函数
		function queryActivityRemarkList() {
			$.ajax({
				url:'workbench/activity/queryActivityRemark',
				data:{
					activityId:"${activity.id}"
				},
				type:'post',
				dataType:"json",
				success:function (data) {
					if (data.code==="1"){
						//展示活动信息
						let activityRemarks = data.returnData;
						console.log(activityRemarks)
						let html = '';
						$.each(activityRemarks,function (i,activityRemark) {
							//因为返回的备注对象中的创建人是id值，所以还要发送请求根据id查询出名字
							let createName = queryUserById(activityRemark.createBy)
							let name = "${activity.name}"

							//只有备注的创建人的id和当前登陆的用户id一样，才显示修改，删除选项
							let userId = "${user.id}"
							if (activityRemark.createBy===userId){
								html+='<div class="remarkDiv" style="height: 60px;">'+
										'<img title="'+activityRemark.createBy+'" src="image/user-thumbnail.png" style="width: 30px; height:30px;">'+
										'<div style="position: relative; top: -40px; left: 40px;" >'+
										'<h5 id="noteContent_'+activityRemark.id+'">'+activityRemark.noteContent+'</h5>'+
										'<font color="gray">市场活动</font> <font color="gray">-</font> <b>'+name+'</b> <small style="color: gray;"> '+(activityRemark.editTime!=null?activityRemark.editTime:activityRemark.createTime)+' 由'+createName+''+(activityRemark.editBy==null?"创建":"编辑")+'</small>'+
										'<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">'+
										'<a name="updateA" class="myHref" href="javascript:void(0);" activityremarkid="'+activityRemark.id+'"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>'+
										'&nbsp;&nbsp;&nbsp;&nbsp;'+
										'<a onclick="deleteActivityRemark(\''+activityRemark.id+'\')" class="myHref" href="javascript:void(0);" activityremarkid="'+activityRemark.id+'"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>'+
										'</div>'+
										'</div>'+
										'</div>'
							}else{
								html+='<div class="remarkDiv" style="height: 60px;">'+
										'<img title="'+activityRemark.createBy+'" src="image/user-thumbnail.png" style="width: 30px; height:30px;">'+
										'<div style="position: relative; top: -40px; left: 40px;" >'+
										'<h5>'+activityRemark.noteContent+'</h5>'+
										'<font color="gray">市场活动</font> <font color="gray">-</font> <b>'+name+'</b> <small style="color: gray;"> '+activityRemark.createTime+' 由'+createName+'创建</small>'+
										'</div>'+
										'</div>'
							}

						})

					$("#activityRemarkDiv").html(html)
					}
				}
			})
		}
	</script>
</head>
<body>
	
	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
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
                            <label for="noteContent" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="noteContent"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
					<span id="updateRemarkTip" style="color: red"></span>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
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
			<h3>市场活动-${activity.name} <small>${activity.startDate} ~ ${activity.endDate}</small></h3>
		</div>
		
	</div>
	
	<br/>
	<br/>
	<br/>

	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.name}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>

		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.startDate}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.endDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">成本</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.cost}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${activity.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${activity.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${activity.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!--备注-->
	<div style="position: relative; top: 30px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		<div id="activityRemarkDiv"></div>
		<!-- 备注1 -->
		<%--<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
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
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>--%>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button id="saveActivityRemarkBtn" type="button" class="btn btn-primary">保存</button>
				</p>
			</form>
		</div>
	</div>
	<div style="height: 200px;"></div>
</body>
</html>