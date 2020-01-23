<#import "parts/common.ftl" as c>
<@c.page>
	<h3>${userChannel}</h3>
	<#if isCurrentUser>
        <#include "parts/messageEdit.ftl">
	</#if>
    <#include "parts/messageList.ftl">
</@c.page>