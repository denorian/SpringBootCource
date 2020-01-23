<#include "security.ftl">
<#import "login.ftl" as l>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
	<a class="navbar-brand" href="/">Sweater</a>
	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarSupportedContent">
		<ul class="navbar-nav mr-auto">
			<li class="nav-item">
				<a class="nav-link" href="/">Home</a>
			</li>
			<#if user??>
				<li class="nav-item">
				<a class="nav-link" href="/main">Messages</a>
			</li>
			<#if isAdmin>
				<li class="nav-item">
					<a class="nav-link" href="/user">User list</a>
				</li>
			</#if>
			<li class="nav-item">
				<a class="nav-link" href="/user-messages/${currentUserId}">My messages</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="/user/profile">Profile</a>
			</li>
			</#if>
		</ul>
	</div>
    <#if isAutorized>
	<div class="navbar-text mr-3">${name}</div>
    <@l.logout />
	<#else>
		<a href="/login" class="btn btn-primary mb-2">Sing in</a>
	</#if>
</nav>