<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>食品购物网站</title>
    <link rel="stylesheet" href="../css/libs/bootstrap.css">
    <link rel="stylesheet" href="../css/base.css">
</head>
<body class="LoginBody">
<%--登录框--%>
<div class="LoginBg">
    <div class="LoginTop">
        <img src="../image/零食.png">
        <h2>食品购物网站管理员登录</h2>
    </div>
    <div class="cf"></div>
    <div class="LoginMain">
        <input id="name" type="text" placeholder="请输入账号">
        <input id="pwd" type="password" placeholder="请输入密码">
        <button id="login">立即登录</button>
    </div>
</div>
<script src="../js/libs/jquery-3.3.1.js"></script>
<script src="../js/libs/bootstrap.js"></script>
<script src="../js/login.js"></script>
</body>
</html>
