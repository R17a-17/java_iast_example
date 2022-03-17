<%--
  Created by IntelliJ IDEA.
  User: R17a
  Date: 2022/3/2
  Time: 1:25 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.r17a.selvlet.service.Log4j2Test" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<pre>
<%
    Log4j2Test log4j2Test = new Log4j2Test();
    String str = request.getParameter("str");
    log4j2Test.test(str);
%>
</pre>
