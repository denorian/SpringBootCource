<#import "parts/common.ftl" as c>
<@c.page>
	<div class="form-row">
		<div class="form-group col-md-6">
			<form method="get" action="/main" class="form-inline">
				<input type="text" name="filter" class="form-control" value="${filter!}" placeholder="Search by tag">
				<button type="submit" class="btn btn-primary ml-2">Search</button>
			</form>
		</div>
	</div>
	<a class="btn btn-primary" data-toggle="collapse" href="#collapse" role="button" aria-expanded="false"
	   aria-controls="collapse">Add new message</a>

	<div class="collapse ${(textError??)?string('show','')}" id="collapse">
		<div class="form-group mt-3">
			<form method="post" enctype="multipart/form-data">
				<div class="form-group">
					<input type="text"
						   name="text"
						   class="form-control ${(textError??)?string('is-invalid','')}"
						   value="<#if message??>${message.text}"</#if>"
						   placeholder="Message"/>
                    <#if textError??>
						<div class="invalid-feedback">
                            ${textError}
						</div>
                    </#if>
				</div>
				<div class="form-group">
					<input type="text" name="tag" class="form-control ${(tagError??)?string('is-invalid','')}" placeholder="Tags">
                    <#if tagError??>
						<div class="invalid-feedback">
                            ${tagError}
						</div>
                    </#if>
				</div>
				<div class="custom-file">
					<input type="file" name="file" class="custom-file-input" id="customFile">
					<label class="custom-file-label" for="customFile">Choose file</label>
				</div>
				<input type="hidden" name="_csrf" value="${_csrf.token}"/>
				<button type="submit" class="btn btn-primary ml-2 mt-2">Add</button>
			</form>
		</div>
	</div>
	<div class="card-columns">
        <#list messages as message>
			<div class="card my-3" style="width: 18rem;">
                <#if message.filename??>
					<img src="/img/${message.filename}" class="card-img-top">
                </#if>
				<div class="card-body m-2">
					<span class="card-text">${message.text}</span>
					<i>${message.tag}</i>
				</div>
				<div class="card-footer text-muted">
                    ${message.authorName}
				</div>
			</div>
        <#else>
			No message
        </#list>
	</div>
</@c.page>