<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 7/25/25
  Time: 8:45 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/shoelace-imoprt.jsp"%>
<aside>
    <header>
        <h1>TheKvizApp</h1>
    </header>
    <ul>
        <li class="selected">
            <sl-icon name="bookmarks-fill"></sl-icon>
            <span>My Quizzes</span>
        </li>
        <li>
            <sl-icon name="plus-circle"></sl-icon>
            <span>New Quiz</span>
        </li>
        <li>
            <sl-icon name="globe-europe-africa"></sl-icon>
            <span>All Quizzes</span>
        </li>
        <li>
            <sl-icon name="controller"></sl-icon>
            <span>Play Quiz</span>
        </li>
        <li>
            <sl-icon name="person"></sl-icon>
            <span>Users</span>
        </li>
    </ul>
</aside>