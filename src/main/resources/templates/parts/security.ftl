<#assign
	known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
	<#assign
		user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
		name = user.getUsername()
		isAdmin = user.isAdmin()
		isAutorized = true
		currentUserId = user.getId()
	>
<#else>
    <#assign
    name = "please, log in."
    isAdmin = false
    isAutorized = false
    currentUserId = -1
    >
</#if>