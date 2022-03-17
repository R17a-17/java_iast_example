<%--
  Created by IntelliJ IDEA.
  User: R17a
  Date: 2022/3/2
  Time: 1:25 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.r17a.selvlet.service.SelectUser" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<pre>
<%
    SelectUser selectUser = new SelectUser();
    String id = request.getParameter("id");
    String name = null;
    try {
        name = selectUser.selectPoc(id);
    } catch (Exception e) {
        e.printStackTrace();
    }
    System.out.printf("id:%s,name:%s\n", id, name);
%>
</pre>
