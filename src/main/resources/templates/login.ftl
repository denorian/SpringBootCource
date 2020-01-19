<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
    ${message!}
    <@l.login "/login" true/>
</@c.page>