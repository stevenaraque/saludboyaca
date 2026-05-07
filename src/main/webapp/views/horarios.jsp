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
    <title><fmt:message key="nav.horarios"/> - SaludBoyacá</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&family=Space+Grotesk:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">
</head>
<body class="bg-body">

    <jsp:include page="templates/header.jsp">
        <jsp:param name="menu" value="horarios"/>
    </jsp:include>

    <div class="container py-4">

        <!-- HEADER -->
        <div class="page-header mb-4 animate-fade-in-up">
            <div class="row align-items-center g-3">
                <div class="col-12 col-lg-8">
                    <h2 class="font-title mb-1">
                        <i class="fas fa-clock me-2"></i>
                        <fmt:message key="nav.horarios"/>
                    </h2>
                    <p class="mb-0 opacity-75">
                        <i class="fas fa-info-circle me-1"></i>
                        <c:choose>
                            <c:when test="${sessionScope.usuarioRol == 'MEDICO'}">
                                <fmt:message key="horario.mi.atencion"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="horario.todos.medicos"/>
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>
        </div>

        <!-- TABLA DE HORARIOS -->
        <div class="glass-card animate-fade-in-up delay-1">
            <div class="glass-card-header">
                <h5 class="mb-0">
                    <i class="fas fa-calendar-week"></i>
                    <fmt:message key="horario.listado"/>
                </h5>
                <span class="badge badge-sm badge-estado-confirmada">
                    <i class="fas fa-list-ol me-1"></i>
                    ${horarios.size()} <fmt:message key="horario.registros"/>
                </span>
            </div>
            
            <div class="glass-card-body p-0">
                <div class="table-responsive-custom">
                    <table class="table-saludboyaca mb-0">
                        <thead>
                            <tr>
                                <th class="d-none d-md-table-cell">ID</th>
                                <th><fmt:message key="horario.medico"/></th>
                                <th><fmt:message key="horario.dia"/></th>
                                <th><fmt:message key="horario.horario"/></th>
                                <th class="d-none d-lg-table-cell"><fmt:message key="horario.max.citas"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty horarios}">
                                    <tr>
                                        <td colspan="5" class="text-center py-5">
                                            <div class="empty-state">
                                                <i class="fas fa-calendar-xmark"></i>
                                                <p><fmt:message key="horario.vacio"/></p>
                                            </div>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${horarios}" var="h" varStatus="status">
                                        <tr class="animate-fade-in-up delay-${(status.index % 5) + 1}">
                                            <td class="d-none d-md-table-cell">
                                                <span class="text-muted">#${h.id}</span>
                                            </td>
                                            <td>
                                                <div class="d-flex align-items-center gap-2">
                                                    <div class="user-avatar">
                                                        <i class="fas fa-user-doctor"></i>
                                                    </div>
                                                    <span class="d-none d-sm-inline">${h.nombreMedico}</span>
                                                    <span class="d-sm-none">${h.nombreMedico.split(' ')[0]}</span>
                                                </div>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${h.diaSemana == 1}">
                                                        <span class="badge badge-sm badge-estado-programada">
                                                            <fmt:message key="dia.lunes"/>
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${h.diaSemana == 2}">
                                                        <span class="badge badge-sm badge-estado-pendiente">
                                                            <fmt:message key="dia.martes"/>
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${h.diaSemana == 3}">
                                                        <span class="badge badge-sm badge-estado-confirmada">
                                                            <fmt:message key="dia.miercoles"/>
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${h.diaSemana == 4}">
                                                        <span class="badge badge-sm badge-estado-atendida">
                                                            <fmt:message key="dia.jueves"/>
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${h.diaSemana == 5}">
                                                        <span class="badge badge-sm badge-estado-cancelada">
                                                            <fmt:message key="dia.viernes"/>
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-sm badge-estado-default">
                                                            <fmt:message key="dia.otro"/>
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <div class="d-flex align-items-center gap-2">
                                                    <i class="fas fa-clock text-muted d-none d-sm-inline"></i>
                                                    <span class="fw-bold">${h.horaInicio}</span>
                                                    <i class="fas fa-arrow-right text-muted" style="font-size: 0.7rem;"></i>
                                                    <span class="fw-bold">${h.horaFin}</span>
                                                </div>
                                            </td>
                                            <td class="d-none d-lg-table-cell">
                                                <span class="badge badge-sm badge-estado-confirmada">
                                                    <i class="fas fa-users me-1"></i>
                                                    ${h.maxCitas}
                                                </span>
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="${pageContext.request.contextPath}/resources/js/saludboyaca-i18n.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/saludboyaca-swal.js"></script>
</body>
</html>