<#import "parts/common.ftl" as c>
<@c.page>
    <h5>${username}</h5>
    ${message!}
<form method="post">

    <div class="form-group row">
        <label class="col-sm-2 col-form-label"> Password: </label>
        <div class="col-sm-10">
            <input type="password" name="password"class="form-control" placeholder="password" />
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">User Name :</label>
        <div class="col-sm-10">
            <input type="email" name="email" class="form-control"  placeholder="some@one.com" value="${email!''}" />
        </div>
    </div>
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button class="btn btn-primary mb-2" type="submit">Save</button>
</form>
</@c.page>