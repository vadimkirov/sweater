<#import "parts/common.ftlh" as c>
<#import "parts/loginAndOut.ftlh" as l>
<@c.page>

    <@l.logout />
    <span><a href="/user">User list</a></span>
    <div>
        <form method="post">
            <input type="text" name="text" placeholder="Введите сообщение"/>
            <input type="text" name="tag" placeholder="Тэг"/>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit">Добавить</button>
        </form>
    </div>
    <div>Список сообщений</div>
        <form method="post" action="filter">
            <input type="text" name="filter" value="${RequestParameters.filter!}">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit">Найти</button>
        </form>
        <#list messages as message>
            <div>
                <b>${message.id}</b>
                <span>${message.text}</span>
                <i>${message.tag}</i>
                <strong>${message.authorName}</strong>
            </div>
            <#else>
            No messages
        </#list>
</@c.page>