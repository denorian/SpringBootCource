<#macro login path isRegisterForm>
	<form action="${path}" method="post">
		<div class="form-group row">
			<label class="col-sm-2 col-form-label">User Name :</label>
			<div class="col-sm-10">
				<input type="text" name="username" class="form-control"  placeholder="username"  />
			</div>
		</div>
		<div class="form-group row">
			<label class="col-sm-2 col-form-label"> Password: </label>
			<div class="col-sm-10">
				<input type="password" name="password"class="form-control" placeholder="password" />
			</div>
		</div>
		<#if !isRegisterForm>
		<div class="form-group row">
			<label class="col-sm-2 col-form-label"> Email: </label>
			<div class="col-sm-10">
				<input type="email" name="email"class="form-control" placeholder="some@one.com" />
			</div>
		</div>
        </#if>
		<input type="hidden" name="_csrf" value="${_csrf.token}">
		<#if isRegisterForm><a href="/registration">Registration</a></#if>
		<button class="btn btn-primary mb-2" type="submit"><#if isRegisterForm>Sign In<#else>Create</#if></button>
	</form>
</#macro>
<#macro logout>
	<div>
		<form action="/logout" method="post">
			<button class="btn btn-primary mb-2"  type="submit">Sign Out</button>
			<input type="hidden" name="_csrf" value="${_csrf.token}">
		</form>
	</div>
</#macro>