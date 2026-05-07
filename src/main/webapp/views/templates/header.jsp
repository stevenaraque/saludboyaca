<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>

<nav class="navbar navbar-expand-lg navbar-saludboyaca sticky-top">
    <div class="container-fluid">
        <!-- Brand -->
        <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">
            <i class="fas fa-heart-pulse"></i>
            <span>Salud<span class="text-accent">Boyacá</span></span>
        </a>

        <!-- Toggler móvil -->
        <button class="navbar-toggler btn-ghost" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <i class="fas fa-bars"></i>
        </button>

        <!-- Contenido colapsable -->
        <div class="collapse navbar-collapse" id="navbarNav">
            <!-- Menú principal -->
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link ${param.menu == 'dashboard' ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/dashboard">
                        <i class="fas fa-chart-line"></i>
                        <fmt:message key="nav.dashboard"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${param.menu == 'pacientes' ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/pacientes">
                        <i class="fas fa-users"></i>
                        <fmt:message key="nav.pacientes"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${param.menu == 'citas' ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/citas">
                        <i class="fas fa-calendar-check"></i>
                        <fmt:message key="nav.citas"/>
                    </a>
                </li>
                <c:if test="${sessionScope.usuarioRol == 'MEDICO' || sessionScope.usuarioRol == 'RECEPCIONISTA'}">
                    <li class="nav-item">
                        <a class="nav-link ${param.menu == 'horarios' ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/horarios">
                            <i class="fas fa-clock"></i>
                            <fmt:message key="nav.horarios"/>
                        </a>
                    </li>
                </c:if>
            </ul>

            <!-- Menú derecho: idioma + usuario + salir -->
            <ul class="navbar-nav align-items-lg-center gap-lg-2">
                <!-- Selector de idioma -->
                <li class="nav-item dropdown lang-dropdown">
                    <button class="dropdown-toggle lang-btn-toggle" type="button" 
                            data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-globe"></i>
                        <span class="lang-code">${sessionScope.lang}</span>
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li>
                            <button class="dropdown-item ${sessionScope.lang == 'es' ? 'active' : ''}" 
                                    type="button" data-lang="es">
                                🇨🇴 <fmt:message key="idioma.es"/>
                            </button>
                        </li>
                        <li>
                            <button class="dropdown-item ${sessionScope.lang == 'en' ? 'active' : ''}" 
                                    type="button" data-lang="en">
                                🇺🇸 <fmt:message key="idioma.en"/>
                            </button>
                        </li>
                        <li>
                            <button class="dropdown-item ${sessionScope.lang == 'it' ? 'active' : ''}" 
                                    type="button" data-lang="it">
                                🇮🇹 <fmt:message key="idioma.it"/>
                            </button>
                        </li>
                    </ul>
                </li>
                
                <!-- Usuario -->
                <li class="nav-item">
                    <div class="navbar-user">
                        <i class="fas fa-user-circle"></i>
                        <span class="d-none d-lg-inline">${sessionScope.usuarioNombre}</span>
                        <span class="badge-rol">${sessionScope.usuarioRol}</span>
                    </div>
                </li>
                
                <!-- Salir -->
                <li class="nav-item">
                    <a class="btn-logout" href="${pageContext.request.contextPath}/logout">
                        <i class="fas fa-power-off"></i>
                        <span class="d-none d-lg-inline"><fmt:message key="nav.salir"/></span>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="navbar-spacer"></div>