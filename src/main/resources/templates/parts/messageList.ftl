<#import "pager.ftl" as p>
<#include "security.ftl" />
<@p.pager url page />
<div class="card-columns" id="message-list">
    <#list page.content as message>
		<div class="card my-3" style="width: 18rem;" data-id="${message.id}">
            <#if message.filename??>
				<img src="/img/${message.filename}" class="card-img-top">
            </#if>
			<div class="card-body m-2">
				<span class="card-text">${message.text}</span><br/>
				<i>#${message.tag}</i>
			</div>
			<div class="card-footer text-muted container">
				<div class="row>">
					<a class="col align-self-center"
					   href="/user-messages/${message.author.id}">${message.authorName}</a>
					<a class="col align-self-center" href="/messages/${message.id}/like">
                        <#if message.meLiked>
							<i class="fas fa-heart"></i>
                        <#else >
							<i class="far fa-heart"></i>
                        </#if>
						${message.likes}
					</a>
                    <#if message.author.id == currentUserId>
						<a href="/user-messages/${message.author.id}?message=${message.id}"
						   class="btn btn-primary float-right"
						   style="margin: -7px">Edit</a>
                    </#if>
				</div>
			</div>
		</div>
    <#else>
		No message
    </#list>
</div>
<@p.pager url page />