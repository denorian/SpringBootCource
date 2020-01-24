<a class="btn btn-primary"
   data-toggle="collapse"
   href="#collapse"
   role="button"
   aria-expanded="false"
   aria-controls="collapse">Add new message</a>
<div class="collapse ${(textError??)?string('show','')} ${(message??)?string('show','')}" id="collapse">
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
				<input type="text"
					   name="tag"
					   class="form-control ${(tagError??)?string('is-invalid','')}"
					   placeholder="Tags"
					   value="<#if message??>${message.tag}</#if>" />
				<#if tagError??>
					<div class="invalid-feedback">
						${tagError}
					</div>
				</#if>
			</div>
			<div class="custom-file">
				<input type="file" name="file" class="custom-file-input" id="customFile" />
				<label class="custom-file-label" for="customFile">Choose file</label>
			</div>
			<input type="hidden" name="id" value="<#if message??>${message.id}</#if>"/>
			<input type="hidden" name="_csrf" value="${_csrf.token}"/>
			<button type="submit" class="btn btn-primary ml-2 mt-2">Save</button>
		</form>
	</div>
</div>