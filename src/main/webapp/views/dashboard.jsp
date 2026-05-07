<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title><fmt:message key="nav.dashboard"/> - SaludBoyacá</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&family=Space+Grotesk:wght@400;500;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">
    </head>
    <body class="bg-body">

        <jsp:include page="templates/header.jsp">
            <jsp:param name="menu" value="dashboard"/>
        </jsp:include>

        <div class="container py-4">

            <!-- HEADER DEL DASHBOARD -->
            <div class="page-header mb-4 animate-fade-in-up">
                <div class="row align-items-center g-3">
                    <div class="col-12 col-lg-8">
                        <h2 class="font-title mb-1">
                            <i class="fas fa-hand-holding-medical me-2"></i>
                            <fmt:message key="dashboard.bienvenida">
                                <fmt:param value="${sessionScope.usuarioNombre}"/>
                            </fmt:message>
                        </h2>
                        <p class="mb-0 opacity-75">
                            <i class="fas fa-shield-halved me-1"></i>
                            <fmt:message key="dashboard.rol"/>:
                            <span class="badge badge-estado-confirmada badge-sm ms-1">
                                ${sessionScope.usuarioRol}
                            </span>
                            <span class="mx-2 d-none d-md-inline">|</span>
                            <span class="d-block d-md-inline mt-1 mt-md-0">
                                <i class="fas fa-hospital me-1"></i>
                                <fmt:message key="app.institucion"/>
                            </span>
                        </p>
                    </div>
                    <div class="col-12 col-lg-4 text-lg-end">
                        <div class="date-badge d-inline-flex align-items-center gap-2">
                            <i class="fas fa-calendar-day"></i>
                            <span class="d-none d-md-inline">
                                <fmt:formatDate value="<%= new java.util.Date()%>" pattern="EEEE, dd 'de' MMMM 'de' yyyy" dateStyle="full"/>
                            </span>
                            <span class="d-md-none">
                                <fmt:formatDate value="<%= new java.util.Date()%>" pattern="dd/MM/yyyy"/>
                            </span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- STATS PRINCIPALES -->
            <div class="row g-3 mb-4">
                <div class="col-12 col-sm-6 col-xl-3 animate-fade-in-up delay-1">
                    <div class="card-stat h-100">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <div class="stat-number">${citasHoy}</div>
                                <div class="stat-label"><fmt:message key="dashboard.citas.hoy"/></div>
                            </div>
                            <div class="stat-icon primary">
                                <i class="fas fa-calendar-day"></i>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-12 col-sm-6 col-xl-3 animate-fade-in-up delay-2">
                    <div class="card-stat h-100">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <div class="stat-number">${citasPendientes}</div>
                                <div class="stat-label"><fmt:message key="dashboard.citas.pendientes"/></div>
                            </div>
                            <div class="stat-icon warning">
                                <i class="fas fa-clock"></i>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-12 col-sm-6 col-xl-3 animate-fade-in-up delay-3">
                    <div class="card-stat h-100">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <div class="stat-number">${citasMes}</div>
                                <div class="stat-label"><fmt:message key="dashboard.citas.mes"/></div>
                            </div>
                            <div class="stat-icon success">
                                <i class="fas fa-calendar-check"></i>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-12 col-sm-6 col-xl-3 animate-fade-in-up delay-4">
                    <div class="card-stat h-100">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <div class="stat-number">${totalPacientes}</div>
                                <div class="stat-label"><fmt:message key="dashboard.pacientes.total"/></div>
                            </div>
                            <div class="stat-icon info">
                                <i class="fas fa-users"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- SECCIÓN PRINCIPAL: TABLA + ACCESO RÁPIDO -->
            <div class="row g-4 mb-4">
                <!-- Tabla de citas recientes -->
                <div class="col-12 col-lg-8 animate-fade-in-up delay-3">
                    <div class="glass-card h-100">
                        <div class="glass-card-header flex-wrap gap-2">
                            <h5 class="mb-0">
                                <i class="fas fa-list-check"></i>
                                <fmt:message key="dashboard.citas.recientes"/>
                            </h5>
                            <div class="d-flex gap-2">
                                <c:if test="${sessionScope.usuarioRol == 'MEDICO' || sessionScope.usuarioRol == 'RECEPCIONISTA'}">
                                    <a href="${pageContext.request.contextPath}/citas?accion=exportarPDF" class="btn btn-sm btn-primary">
                                        <i class="fas fa-file-pdf"></i>
                                        <span class="d-none d-md-inline ms-1"><fmt:message key="btn.exportar.pdf"/></span>
                                    </a>
                                </c:if>
                                <a href="${pageContext.request.contextPath}/citas" class="btn btn-sm btn-outline">
                                    <span class="d-none d-md-inline"><fmt:message key="btn.ver.todas"/></span>
                                    <i class="fas fa-arrow-right ms-0 ms-md-1"></i>
                                </a>
                            </div>
                        </div>
                        <div class="glass-card-body p-0">
                            <div class="table-responsive-custom">
                                <table class="table-saludboyaca mb-0">
                                    <thead>
                                        <tr>
                                            <th><fmt:message key="tabla.paciente"/></th>
                                            <th class="d-none d-md-table-cell"><fmt:message key="tabla.especialidad"/></th>
                                            <th><fmt:message key="tabla.fecha"/></th>
                                            <th class="d-none d-sm-table-cell"><fmt:message key="tabla.hora"/></th>
                                            <th><fmt:message key="tabla.estado"/></th>
                                            <th class="text-end"><fmt:message key="tabla.acciones"/></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${empty citasRecientes}">
                                                <tr>
                                                    <td colspan="6" class="text-center py-4">
                                                        <div class="empty-state">
                                                            <i class="fas fa-clipboard-list"></i>
                                                            <p><fmt:message key="dashboard.sin.citas"/></p>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach items="${citasRecientes}" var="c" varStatus="loop">
                                                    <tr class="animate-fade-in-up delay-${loop.index + 1}">
                                                        <td>
                                                            <div class="d-flex align-items-center gap-2">
                                                                <div class="user-avatar">${c.pacienteNombre.charAt(0)}</div>
                                                                <span class="d-none d-sm-inline">${c.pacienteNombre}</span>
                                                                <span class="d-sm-none">${c.pacienteNombre.split(' ')[0]}</span>
                                                            </div>
                                                        </td>
                                                        <td class="d-none d-md-table-cell">
                                                            <span class="text-muted">
                                                                <i class="fas fa-stethoscope me-1" style="color: var(--color-acento);"></i>
                                                                ${c.especialidadNombre}
                                                            </span>
                                                        </td>
                                                        <td>
                                                            <i class="fas fa-calendar-day text-muted me-1 d-none d-sm-inline"></i>
                                                            ${c.fechaCita}
                                                        </td>
                                                        <td class="d-none d-sm-table-cell">
                                                            <i class="fas fa-clock text-muted me-1 d-none d-md-inline"></i>
                                                            ${c.horaCita}
                                                        </td>
                                                        <td>
                                                            <span class="badge badge-sm badge-estado-${c.estado.toLowerCase()}">
                                                                <fmt:message key="cita.estado.${c.estado.toLowerCase()}"/>
                                                            </span>
                                                        </td>
                                                        <td class="text-end">
                                                            <a href="${pageContext.request.contextPath}/citas?accion=ver&id=${c.id}" class="btn-action btn-action-view" title="<fmt:message key='btn.ver'/>">
                                                                <i class="fas fa-eye"></i>
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Acceso rápido -->
                <div class="col-12 col-lg-4 animate-fade-in-up delay-4">
                    <div class="quick-access-card h-100">
                        <h6 class="d-flex align-items-center gap-2 mb-3">
                            <i class="fas fa-bolt"></i>
                            <fmt:message key="dashboard.acceso.rapido"/>
                        </h6>

                        <c:choose>
                            <c:when test="${sessionScope.usuarioRol == 'RECEPCIONISTA'}">
                                <a href="${pageContext.request.contextPath}/citas?accion=nuevo" class="quick-access-link">
                                    <div class="d-flex align-items-center gap-3">
                                        <div class="quick-icon">
                                            <i class="fas fa-plus-circle"></i>
                                        </div>
                                        <div class="flex-grow-1">
                                            <div class="fw-bold"><fmt:message key="btn.nueva.cita"/></div>
                                            <small class="quick-desc"><fmt:message key="btn.nueva.cita.desc"/></small>
                                        </div>
                                    </div>
                                    <i class="fas fa-chevron-right quick-arrow"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/pacientes?accion=nuevo" class="quick-access-link">
                                    <div class="d-flex align-items-center gap-3">
                                        <div class="quick-icon">
                                            <i class="fas fa-user-plus"></i>
                                        </div>
                                        <div class="flex-grow-1">
                                            <div class="fw-bold"><fmt:message key="btn.nuevo.paciente"/></div>
                                            <small class="quick-desc"><fmt:message key="btn.nuevo.paciente.desc"/></small>
                                        </div>
                                    </div>
                                    <i class="fas fa-chevron-right quick-arrow"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/horarios" class="quick-access-link">
                                    <div class="d-flex align-items-center gap-3">
                                        <div class="quick-icon">
                                            <i class="fas fa-clock"></i>
                                        </div>
                                        <div class="flex-grow-1">
                                            <div class="fw-bold"><fmt:message key="nav.horarios"/></div>
                                            <small class="quick-desc"><fmt:message key="btn.ver.horarios.desc"/></small>
                                        </div>
                                    </div>
                                    <i class="fas fa-chevron-right quick-arrow"></i>
                                </a>
                            </c:when>

                            <c:when test="${sessionScope.usuarioRol == 'MEDICO'}">
                                <a href="${pageContext.request.contextPath}/citas?accion=listar" class="quick-access-link">
                                    <div class="d-flex align-items-center gap-3">
                                        <div class="quick-icon">
                                            <i class="fas fa-list"></i>
                                        </div>
                                        <div class="flex-grow-1">
                                            <div class="fw-bold"><fmt:message key="btn.mis.citas"/></div>
                                            <small class="quick-desc"><fmt:message key="btn.mis.citas.desc"/></small>
                                        </div>
                                    </div>
                                    <i class="fas fa-chevron-right quick-arrow"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/horarios" class="quick-access-link">
                                    <div class="d-flex align-items-center gap-3">
                                        <div class="quick-icon">
                                            <i class="fas fa-clock"></i>
                                        </div>
                                        <div class="flex-grow-1">
                                            <div class="fw-bold"><fmt:message key="btn.mi.horario"/></div>
                                            <small class="quick-desc"><fmt:message key="btn.mi.horario.desc"/></small>
                                        </div>
                                    </div>
                                    <i class="fas fa-chevron-right quick-arrow"></i>
                                </a>
                            </c:when>

                            <c:when test="${sessionScope.usuarioRol == 'ENFERMERO'}">
                                <a href="${pageContext.request.contextPath}/citas?accion=listar" class="quick-access-link">
                                    <div class="d-flex align-items-center gap-3">
                                        <div class="quick-icon">
                                            <i class="fas fa-search"></i>
                                        </div>
                                        <div class="flex-grow-1">
                                            <div class="fw-bold"><fmt:message key="btn.consultar.citas"/></div>
                                            <small class="quick-desc"><fmt:message key="btn.consultar.citas.desc"/></small>
                                        </div>
                                    </div>
                                    <i class="fas fa-chevron-right quick-arrow"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/pacientes" class="quick-access-link">
                                    <div class="d-flex align-items-center gap-3">
                                        <div class="quick-icon">
                                            <i class="fas fa-users"></i>
                                        </div>
                                        <div class="flex-grow-1">
                                            <div class="fw-bold"><fmt:message key="nav.pacientes"/></div>
                                            <small class="quick-desc"><fmt:message key="nav.pacientes.desc"/></small>
                                        </div>
                                    </div>
                                    <i class="fas fa-chevron-right quick-arrow"></i>
                                </a>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
            </div>

            <!-- SECCIÓN INFERIOR: INFO + STATS -->
            <div class="row g-4">
                <div class="col-12 col-lg-8 animate-fade-in-up delay-5">
                    <div class="glass-card">
                        <div class="glass-card-header">
                            <h5 class="mb-0">
                                <i class="fas fa-hospital"></i>
                                <fmt:message key="app.institucion"/>
                            </h5>
                        </div>
                        <div class="glass-card-body">
                            <div class="row g-4">
                                <div class="col-12 col-md-6">
                                    <div class="d-flex align-items-start gap-3">
                                        <div class="info-icon">
                                            <i class="fas fa-location-dot"></i>
                                        </div>
                                        <div>
                                            <h6 class="font-title text-title mb-1">Paipa, Boyacá</h6>
                                            <p class="text-muted mb-0" style="font-size: 0.85rem;">
                                                Centro de Salud Municipal<br>
                                                Carrera 8 # 12-45
                                            </p>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12 col-md-6">
                                    <div class="d-flex align-items-start gap-3">
                                        <div class="info-icon">
                                            <i class="fas fa-phone"></i>
                                        </div>
                                        <div>
                                            <h6 class="font-title text-title mb-1"><fmt:message key="contacto.telefono"/></h6>
                                            <p class="text-muted mb-0" style="font-size: 0.85rem;">
                                                +57 (8) 731 2450<br>
                                                <fmt:message key="contacto.horario"/>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-12 col-lg-4 animate-fade-in-up delay-5">
                    <div class="row g-3">
                        <div class="col-6">
                            <div class="card-stat h-100" style="padding: 16px;">
                                <div class="stat-icon danger" style="width: 36px; height: 36px; font-size: 1rem; margin-bottom: 8px;">
                                    <i class="fas fa-user-doctor"></i>
                                </div>
                                <div class="stat-number" style="font-size: 1.4rem;">${totalMedicos}</div>
                                <div class="stat-label" style="font-size: 0.8rem;"><fmt:message key="dashboard.medicos.activos"/></div>
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="card-stat h-100" style="padding: 16px;">
                                <div class="stat-icon primary" style="width: 36px; height: 36px; font-size: 1rem; margin-bottom: 8px;">
                                    <i class="fas fa-stethoscope"></i>
                                </div>
                                <div class="stat-number" style="font-size: 1.4rem;">${totalEspecialidades}</div>
                                <div class="stat-label" style="font-size: 0.8rem;"><fmt:message key="dashboard.especialidades"/></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script src="${pageContext.request.contextPath}/resources/js/saludboyaca-i18n.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/saludboyaca-swal.js"></script>
    </body>
</html>