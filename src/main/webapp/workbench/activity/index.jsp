<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<%
	String basePath = request.getScheme() + "://" +
	request.getServerName() + ":" +
	request.getServerPort() +
	request.getContextPath() + "/";
	%>
	<base href="<%=basePath%>">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<%--
     下面开始调用日历组件
     注意：
        导入的顺序必须注意，由从大到小
--%>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


	<script type="text/javascript">

	$(function(){

		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});
		$("#addBtn").click(function () {



			/*
			   操作模态窗口的方式：
			       需要操作模态窗口的jquery对象，调用model方法，给方法传递参数   show：打开模态窗口    hide：关闭模态窗口
			*/

			//此处走后台，目的是为了取得用户信息列表，为所有者下拉框铺值

			$.ajax({
				url:"workbench/activity/getUserList.do",

				type:"get",
				dataType:"json",
				success:function (data) {
					//此处data表示的是由list集合转换的json数据
					var html = "<option></option>";
					//每遍历出来一个n，就是一个user对象
					$.each(data,function (i,n) {
						html += "<option value='"+n.id+"'>"+n.name+"</option>";
					})
					$("#create-Owner").html(html);
					var id = "${sessionScope.user.id}";
					$("#create-Owner").val(id);

					//所有者下拉框处理完毕，展现模态窗口
					$("#createActivityModal").modal("show");
				}
			})



		})

		$("#saveBtn").click(function () {
			$.ajax({
				url: "workbench/activity/save.do",
				data:{
					"owner":$.trim($("#create-Owner").val()),
					"name":$.trim($("#create-Name").val()),
					"startDate":$.trim($("#create-startDate").val()),
					"endDate":$.trim($("#create-endDate").val()),
					"cost":$.trim($("#create-cost").val()),
					"description":$.trim($("#create-description").val()),
				},

				type:"post",
				dataType: "json",
				success:function (data) {
					/*
					    data
					    {"success":false/true}
					*/
					if (data){
						//添加成功后刷新市场活动信息列表（局部刷新）
						//pageList(1,2)

						/*
						    pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
						    操作后停留在当前页
						    $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						    操作后维持设置好的每页展现记录数

						    这两个参数不需要我们进行任何修改
						    直接使用即可

						*/
						//$("#activityPage").bs_pagination('getOption', 'currentPage')
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


						//清空添加操作模态窗口的数据

						/*
						    注意：
						       我们拿到了form表单的jquery对象
						       对于表单的jQuery对象，提供了submit（）方法让我们提交表单
						       但是jquery对象，没有为我们提供reset（）方法让我们重置表单（坑，idea为我们提示了有reset（）方法）
						       虽然jquery对象没有为我们提供reset方法，但是原生js为我们提供reset方法

						       jQuery转换为dom对象
						          jquery对象[下表]
						       dom对象转换为jquery对象
						          $(dom)

						*/

						//清空表单内容，虽然idea有提示，但是不可用,需要转换为dom对象
						$("#activityAddForm")[0].reset();


						//关闭添加操作的模态窗口
						$("#createActivityModal").modal("hide")

					}else{
						alert("市场活动添加失败")
					}
				}
			})


		})

		pageList(1,2);
		$("#searchBtn").click(function () {

			$("#hidden-name").val($.trim($("#search-name").val()))
			$("#hidden-owner").val($.trim($("#search-owner").val()))
			$("#hidden-startTime").val($.trim($("#search-startDate").val()))
			$("#hidden-endTime").val($.trim($("#search-endDate").val()))


			pageList(1,2)
		})

		$("#qx").click(function () {
			$("input[name=xz]").prop("checked",this.checked)
		})

		//对于动态拼接的元素，不能以普通绑定事件的形式来进行操用，要采用on的方式
		/*$("input[name=xz]").click(function () {
			alert("qwer")
		})*/

		/*
		   动态生成的元素，要以on的形式来触发
		    语法：
		      $(需要绑定元素的有效外层元素).on(绑定的事件，需要绑定元素的jquery对象，回调函数)
		*/
		$("#activityBody").on("click",$("input[name=xz]"),function () {
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length)
		});

		$("#deleteBtn").click(function () {


			 var $xz =$("input[name=xz]:checked")
			if ($xz.length==0){
				alert("请选择要删除的市场活动")

				//到这里,要么选择一个,要么选中多个
			}else{
				var param = "";
				//获取复选框中的id,作为参数传递
				for(var i=0;i<$xz.length;i++){
					param += "ids="+$($xz[i]).val();
					//如果不是最后一个元素,加上 &
					if (i<$xz.length-1){
						param += "&";
					}
				}
				if (confirm("确定要执行删除操作吗？")){
					$.ajax({
						url:"workbench/activity/delete.do",
						data:param,
						dataType:"json",
						type:"post",
						success:function (data) {
							/*
                               data:
                                  {success:true/false}
                            */
							if (data){
								//说明保存成功
								//pageList(1,2)
								//删除操作后回到第一页
								pageList(1
										,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							}else{
								alert("删除失败")
							}
						}
					})
				}

			}


		})

		$("#editBtn").click(function () {
			var $xz = $("input[name=xz]:checked")

			if ($xz.length==0){
				alert("请选择要修改的市场活动");

			}else if($xz.length > 1){
				alert("只能选中一条记录");

			}else{
				var id = $xz.val();
				$.ajax({
					url:"workbench/activity/getUserListAndActivity.do",
					data:{
						"id":id
					},
					dataType:"json",
					type:"get",
					success:function (data) {
						/*
						    data:
						      {uList:{[用户1],[2],[3]},activity:{市场活动}}
						*/
						var html = "<option></option>"
						$.each(data.uList,function (i,n) {
							html += "<option value='"+n.id+"'>"+n.name +"</option>"
						})

						$("#edit-owner").html(html);


						$("#edit-id").val(data.a.id);

						$("#edit-name").val(data.a.name);

						$("#edit-owner").val(data.a.owner);

						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endData);
						$("#edit-cost").val(data.a.cost);
						$("#edit-description").val(data.a.description);

						$("#editActivityModal").modal("show")
					}
				})
			}
		})

		$("#updateBtn").click(function () {

				$.ajax({
					url: "workbench/activity/update.do",
					data:{
						"id":$.trim($("#edit-id").val()),
						"owner":$.trim($("#edit-owner").val()),
						"name":$.trim($("#edit-name").val()),
						"startDate":$.trim($("#edit-startDate").val()),
						"endDate":$.trim($("#edit-endDate").val()),
						"cost":$.trim($("#edit-cost").val()),
						"description":$.trim($("#edit-description").val()),
					},

					type:"post",
					dataType: "json",
					success:function (data) {
						/*
                            data
                            {"success":false/true}
                        */
						if (data){
							//添加成功后刷新市场活动信息列表（局部刷新）
							//修改操作后，维持当前展现的记录数
							pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
									,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

							//清空添加操作模态窗口的数据

							//清空表单内容，虽然idea有提示，但是不可用,需要转换为dom对象
							//$("#activityAddForm")[0].reset();

							//关闭添加操作的模态窗口
							$("#editActivityModal").modal("hide")

						}else{
							alert("市场活动修改失败")
						}
					}
				})
			})


	});
	/*
	     对于关系型数据库，做前端的分页相关操作的基础组件
	     就是pageNo和pageSize
	     pageNo：页码
	     pageSize：每页展现的记录数

	     pageList方法：就是发出ajax请求到后台，从后台取得最新的市场活动信息列表数据
	                  通过响应回来的数据，局部刷新市场活动信息列表

	     在哪些情况下，需要调用pageList方法（什么情况下需要刷新一下市场活动列表）
	     （1）点击左侧菜单中的“市场活动”超链接
	     （2）添加，修改，删除后
	     （3）点击查询按钮的时候
	     （4）点击分页组件的时候
	*/
	function pageList(pageNo,pageSize) {

		$("#search-name").val($.trim($("#hidden-name").val()))
		$("#search-owner").val($.trim($("#hidden-owner").val()))
		$("#search-startTime").val($.trim($("#hidden-startDate").val()))
		$("#search-endTime").val($.trim($("#hidden-endDate").val()))

		$.ajax({
			url:"workbench/activity/pageList.do",
			data: {
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startData":$.trim($("#search-startTime").val()),
				"endData":$.trim($("#search-endTime").val()),

			},
			type:"get",
			dataType:"json",
			success:function (data) {
				/*
				   data
				      我们需要的：市场活动信息列表
				      [{市场活动1},{2},{3}] list<Activity> alist
				      一会分页插件需要的：查询出来的总记录数
				      {"total":100} int total


				      {"total":100,"dataList":[{市场活动1},{2},{3}]
				*/
				var html = "";
				$.each(data.dataList,function (i,n) {
					html += '<tr class="active">';
						html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
						html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
						html += '<td>'+n.owner+'</td>';
						html += '<td>'+n.startDate+'</td>';
						html += '<td>'+n.endDate+'</td>';
					html += '</tr>';
				})
				$("#activityBody").html(html);


				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
				//数据处理完毕后，结合分页插件，对前端展现分页信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 5, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//回调函数，点击分页组件的时候触发
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
						$("#qx").prop("checked",false)
					}
				});
			}
		})

	}

</script>
</head>
<body>

<input type="hidden" id="hidden-name" />
<input type="hidden" id="hidden-owner" />
<input type="hidden" id="hidden-startDate" />
<input type="hidden" id="hidden-endDate" />

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
				
					<form class="form-horizontal" id="activityAddForm" role="form">
					
						<div class="form-group">
							<label for="create-Owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-Owner">

								</select>
							</div>
                            <label for="create-Name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-Name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">

 <%--
                     data-dismiss="modal:
                             表示关闭模态窗口
--%>
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
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
					    <input type="hidden" id="edit-id" />
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate" readonly >
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate" readonly>
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
								<%--
								     关于文本域textarea：
								         (1)一定要以标签对的形式成对出现,正常情况下要紧紧挨着
								         (2)textarea虽然是以标签对的形式出现,但是它属于表单元素的范畴
								            虽然它没有value属性,但我们说有的赋值和取值操作,都应该使用val()方法(而不是html()方法)
								--%>
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
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
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endTime">
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" id="addBtn" class="btn btn-primary" ><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" id="editBtn" class="btn btn-default" ><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" id="deleteBtn" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div id="activityPage">


			</div>
			
		</div>
		
	</div>
</body>
</html>