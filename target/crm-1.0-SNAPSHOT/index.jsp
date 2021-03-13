<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%
	String basePath = request.getScheme() + "://" +
			request.getServerName() + ":" +
			request.getServerPort() +
			request.getContextPath() + "/";
%>
<base href="<%=basePath%>">

<html>
<head>
<meta charset="UTF-8">
</head>
<body>
	<script type="text/javascript">
		document.location.href = "login.jsp";
	</script>
</body>
</html>