<!DOCTYPE html>
<html>
<head>
    <title>Spring Security Example </title>
</head>
<body>
Add new user
{{#message}}
    {{message}}
{{/message}}
<form action="/registration" method="post">
    <div><label> User Name : <input type="text" name="username"/> </label></div>
    <div><label> Password: <input type="password" name="password"/> </label></div>
    <input type="hidden" name="_csrf" value="{{_csrf.token}}">
    <div><input type="submit" value="Sign In"/></div>
</form>
</body>
</html>


<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
    Registration page
    <br>
    ${message}
    <@l.login "/login" />
    <a href="/registration">Registration</a>
</@c.page>