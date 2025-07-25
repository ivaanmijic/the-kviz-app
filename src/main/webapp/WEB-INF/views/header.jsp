<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 7/25/25
  Time: 8:46 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/shoelace-imoprt.jsp"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
<header>
    <div class="header-container">
        <div class="search-bar">
            <sl-input placeholder="Search" size="medium" pill>
                <sl-icon-button name="search" slot="suffix" ></sl-icon-button>
            </sl-input>
        </div>
        <div class="profile-buttons">
            <sl-icon-button name="box-arrow-left" ></sl-icon-button>
            <sl-avatar label="UserProfile"></sl-avatar>
        </div>
    </div>
</header>