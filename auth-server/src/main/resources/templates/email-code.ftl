<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <style>
        @page  {
            margin: 0;
        }
    </style>
</head>
<body>
<div class="header">
    <div style="padding: 10px 10px 0px;">
        <p>感谢你的注册，欢迎有你</p>
        <p style="text-indent: 2em;margin-bottom: 10px">你正在注册成为编程开发技术学习平台的用户，你的验证码:</p>
        <p class="code-text">
            ${code}
        </p>
        <p style="text-indent: 2em;margin-bottom: 10px">该验证码有效期为${expiration}分钟,请你尽快使用哦！</p>
        <div class="footer"></div>
    </div>
</div>

</body>

</html>

<style lang="css">
    body {
        margin: 0px;
        padding: 0px;
        font: 100% SimSun, Microsoft YaHei, Times New Roman, Verdana, Arial, Helvetica, sans-serif;
        color: #000;
    }

    .header {
        height: auto;
        width: 820px;
        min-width: 820px;
        margin: 0 auto;
        margin-top: 20px;
        border: 1px solid #eee;
    }

    .code-text {
        text-align: center;
        font-family: Times New Roman;
        font-size: 22px;
        color: #C60024;
        padding: 20px 0px;
        margin-bottom: 10px;
        font-weight: bold;
        background: #ebebeb;
    }

    .footer {
        margin: 0 auto;
        z-index: 111;
        width: 800px;
        margin-top: 30px;
        border-top: 1px solid #DA251D;
    }
</style>