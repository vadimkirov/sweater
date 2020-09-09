<#import "parts/common.ftlh" as c>
<#import "parts/loginAndOut.ftlh" as l>

<@c.page>
    ${message?if_exists}
    <@l.login "/login" false/>
</@c.page>