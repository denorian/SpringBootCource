<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
    Registration page
    <br>
    ${message!}
    <@l.login "/login" />
    <a href="/registration">Registration</a>
</@c.page>