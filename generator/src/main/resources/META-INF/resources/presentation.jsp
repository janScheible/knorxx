<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="knorxx.framework.generator.web.client.JsonHelper" %>
<%@ page import="knorxx.framework.generator.web.client.ErrorHandler" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${applicationName}</title>        
        <c:set var="req" value="${pageContext.request}" /><c:set var="baseURL" value="${req.scheme}://${req.serverName}:${req.serverPort}${req.contextPath}" />
        <base href="${baseURL}/"/>
        
        <script type="text/javascript" src="webjars/jquery/1.9.1/jquery.js"></script>

        <c:forEach items="${libraryCssUrls}" var="libraryCssUrl"><link rel="stylesheet" type="text/css" href="${libraryCssUrl}"><%="\n        "%></c:forEach>
        <c:forEach items="${libraryJavaScriptUrls}" var="libraryJavaScriptUrl"><script type="text/javascript" src="${libraryJavaScriptUrl}"></script><%="\n        "%></c:forEach>        
        
        <script type="text/javascript" src="jquery/jquery.atmosphere-min.js"></script>        
        <script type="text/javascript" src="resources/js/stjs.js"></script>
        
        <c:forEach items="${cssUrls}" var="cssUrl"><link rel="stylesheet" type="text/css" href="${cssUrl}"><%="\n        "%></c:forEach>
        <c:forEach items="${javaScriptUrls}" var="javaScriptUrl"><script type="text/javascript" src="${javaScriptUrl}"></script><%="\n        "%></c:forEach>
        
        <script type="text/javascript">
            var <%= JsonHelper.INSTANCE_NAME %> = new ${jsonHelperClassName}();
            var <%= ErrorHandler.INSTANCE_NAME %> = new ${errorHandlerClassName}();

            window.onerror = function(message, url, lineNumber, columnNumber, error) {
                error = error || window.event;
                var stacktrace = error && error.stack ? error.stack : (url + ':' + lineNumber);
            
                <%= ErrorHandler.INSTANCE_NAME %>.displayRuntimeError(message, url, lineNumber, stacktrace);
            };

            $(document).ajaxError(function(event, jqxhr, settings, exception) {
                 <%= ErrorHandler.INSTANCE_NAME %>.displayCommunicationError(event, jqxhr, settings, exception);
            });
        </script>        
    </head>
    <body>
        <table style="border: 3px solid #42B4E6;">
            <tr>
                <td id="container">
                    <div id="menu"></div>
                    <h1 id="title">Login</h1>
                    <div id="content">
                        <c:if test="${not empty preRenderedHtml}">${preRenderedHtml}</c:if>
                    </div>
                </td>
            </tr>
        </table>
        
        <c:if test="${not empty mainClassName}"><script type="text/javascript">
            var page = new ${mainClassName}();
            page.model = JSON.parse('<%= StringEscapeUtils.escapeJson((String)request.getAttribute("webPageModelJson")) %>', <%= JsonHelper.PARSE_REVIVER_FUNCTION %>);
            page.render();
        </script></c:if>
    </body>
</html>
