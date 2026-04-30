<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<nav class="navbar navbar-expand-lg navbar-dark navbar-saludboyaca">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">
            <i class="fas fa-hospital-alt me-2"></i>SaludBoyaca
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link ${param.menu == 'dashboard' ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/dashboard">
                        <i class="fas fa-tachometer-alt me-1"></i><fmt:message key="nav.dashboard" />
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${param.menu == 'pacientes' ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/pacientes">
                        <i class="fas fa-users me-1"></i><fmt:message key="nav.pacientes" />
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${param.menu == 'citas' ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/citas">
                        <i class="fas fa-calendar-check me-1"></i><fmt:message key="nav.citas" />
                    </a>
                </li>
            </ul>

            <ul class="navbar-nav">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown">
                        <i class="fas fa-globe me-1"></i>${sessionScope.lang}
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li><a class="dropdown-item" href="?lang=es">🇨🇴 Español</a></li>
                        <li><a class="dropdown-item" href="?lang=en">🇺🇸 English</a></li>
                        <li><a class="dropdown-item" href="?lang=it">🇮🇹 Italiano</a></li>
                    </ul>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                        <i class="fas fa-sign-out-alt me-1"></i><fmt:message key="nav.salir" />
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Espaciado debajo del navbar -->
<div style="height: 20px;"></div>