<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>简单页面</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.js"></script>

<script type="text/javascript">
	// 计算(提交表单)
	function formSub() {
		
		// 隐藏表格
		$("#his").hide();
		
		// 将表单数据封装到FormData对象中
		var formData = new FormData($("#form")[0]);
		$.ajax({
			url : 'http://localhost:8080/act2/activiti02/computation',
			type : 'POST',
			data : formData,
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(returndata) {
				var divshow = $("#showInfo2");
				divshow.text("");// 清空数据
				divshow.html('<span><font color="red">计算结果: ' + returndata
						+ ' </font></span>');

				// 显示div
				$("#divPreview").show();
			},
			error : function(returndata) {
				alert("参数不完整或参数类型错误!");
			}
		});
	}

	// 查询历史记录
	function showHis() {
		$.ajax({
			url : 'http://localhost:8080/act2/activiti02/history',
			type : 'get',
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(returndata) {
				var obj = eval(returndata);
				//{10005: "{"abc":3,"b":1,"c":1,"a":1}", 12505: "{"abc":3,"b":1,"c":1,"a":1}", 15005: "{"abc":3,"b":1,"c":1,"a":1}"}
				//<tr><td>流程实例id</td><td>变量a</td><td>变量b</td><td>变量c</td><td>运算结果</td></tr>
					var tab = $("#his");
					// 清空数据
					tab.html("");
					// 动态添加数据
					tab.html("<tr><th>流程实例id</th><th>参数1</th><th>参数2</th><th>参数3</th><th>运算结果</th></tr>"); 
				$.each(obj, function(key, value) {
					// id
					var id = key;
					var a , b, c, result;
					$.each(value, function(key1, value1) {
						if("a" == key1){
							a = value1;
						}else if ("b" == key1) {
							b = value1;
						}else if ("c" == key1) {
							c = value1;
						}else if ("abc" == key1) {
							result = value1;
						}
					});
					// 追加Html内容，不能用Text 或 Val
					tab.append("<tr><td align='center'>"+ id +"</td><td align='center'>"+a+"</td><td align='center'>"+b+"</td><td align='center'>"+c+"</td><td align='center'>"+result+"</td></tr>");
					
				});
				
				// 显示表格
				tab.show();

			},
			error : function(returndata) {
				alert("发生错误!");
			}
		});
	}

	// 获取上传文件的文件名
	function getProcessFileName() {
		var file = $("#processFile").val();
		//var fileName = getFileName(file);
		var pos = file.lastIndexOf("\\");
		var fileName = file.substring(pos + 1);
		// 给input标签加个value属性
		$("#processFileName").attr("value", fileName);
	}
</script>
</head>

<body>
	<div
		style="position: absolute; left: 200px; top: 20px; border: 1px solid;">
		<table border="1" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<form id="form">
						<!-- action="http://localhost:8080/act2/activiti02" enctype="multipart/form-data" method="post" -->
						<table>
							<caption>
								<h3>简单计算流程:请输入整数!</h3>
							</caption>
							<tr>
								<td>参数1:</td>
								<td><input type="text" name="param1"></td>
							</tr>
							<tr>
								<td>参数2:</td>
								<td><input type="text" name="param2"></td>
							</tr>
							<tr>
								<td>参数3:</td>
								<td><input type="text" name="param3"></td>
							</tr>
							<tr>
								<td>流程:</td>
								<td><input type="file" name="processFile" id="processFile"
									onchange="getProcessFileName()"> <input type="hidden"
									name="processFileName" id="processFileName"></td>
							</tr>

							<tr>
								<td><input type="button" onclick="formSub()" value="计算" /></td>
								<td><input type="button" onclick="showHis()" value="查看历史记录" /></td>
							</tr>

						</table>
					</form>
				</td>
			</tr>

			<tr>
				<td colspan="1" 　align="center">
					<div id="divPreview" style="display: none">
						<text id="showInfo2"></text>
					</div>
				</td>
			</tr>

		</table>
		<table id="his" style="display: none"  border="1" cellpadding="0" cellspacing="0"></table>
	</div>

</body>
</html>