<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
<div class="mb-1">Registration</div>

${message!}
<@l.login "/registration" false/>
</@c.page>