<#import "parts/common.ftlh" as c>
<#import "parts/loginAndOut.ftlh" as l>

<@c.page>
<div class="mb-1">Add new User</div>
${message?if_exists}
<@l.login "/registration" true/>
</@c.page>