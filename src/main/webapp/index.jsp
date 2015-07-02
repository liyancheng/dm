<%@ page pageEncoding="utf-8" %>
<html>
<head>
    <title>数据迁移</title>
</head>
<body>
<h1 style="text-align:center">欢迎登陆数据迁移页面</h1>

<div style="text-align:center"><a href="javascript:void(0)" onclick="access()">进入用户学习课程学习次数数据迁移页面</a></div>
</body>


<script type="text/javascript">
    function access() {
        window.self.location.href = "${pageContext.request.contextPath}/courseStudy/listCorp.do";
    }
</script>
</html>
