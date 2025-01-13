<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <style>
        @page {
            margin: 0;
        }
    </style>
</head>
<body>
<div class="header">
    <div style="padding: 10px 10px 0;">
        <div class="head"></div>
        <p class="top-tip">你正在注册成为编程开发技术学习平台的用户，你的验证码为:</p>
        <p class="code-text">
            ${code}
        </p>
        <p class="bottom-tip">该验证码有效期为${expiration}分钟,请你尽快使用哦！</p>
        <div class="bottom-tip">
            如有需求，可联系邮箱：
            <a class="email" href="mailto:lineben@163.com">lineben@163.com</a>

        </div>
    </div>
</div>

</body>

</html>

<style lang="css">
    body {
        margin: 0;
        padding: 0;
        font: 100% SimSun, Microsoft YaHei, Times New Roman, Verdana, Arial, Helvetica, sans-serif;
        color: #000;
    }

    .header {
        height: auto;
        width: 820px;
        min-width: 820px;
        margin: 20px auto 0;
        border: 1px solid #ebebeb;
        border-radius: 5px;
    }

    .head {
        z-index: 111;
        width: 800px;
        margin: 0 auto 10px;
        border-top: 5px solid #1d98da;
    }

    .top-tip {
        margin-bottom: 10px;
        font-size: 20px;
    }

    .code-text {
        text-align: center;
        font-family: Times New Roman;
        font-size: 22px;
        color: #2a4288;
        padding: 60px;

        margin-bottom: 10px;
        font-weight: bold;
        background: #ebebeb;
    }

    .bottom-tip {
        margin-bottom: 10px;
        font-size: 16px;
    }

    .email {
        cursor: pointer;
        transition: 0.4s;
    }

    .email:hover {
        color: #2a61a5;
    }
</style>