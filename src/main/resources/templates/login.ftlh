<#import "parts/common.ftlh" as c>
<#import "parts/loginAndOut.ftlh" as l>

<@c.page>
Login page
    <@l.login "/login" />
<a href="/registration">Add new User</a>
</@c.page>