<#import "parts/common.ftlh" as c>

<@c.page>

    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="post" action="filter" class="form-inline">
                <input type="text" name="filter" value="${filter?if_exists}" placeholder="Search">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button class="btn btn-primary ml-2" type="submit">Search</button>
            </form>
        </div>
    </div>
    <#include "parts/messageEdit.ftlh" />
    <#include "parts/messageList.ftlh" />
</@c.page>