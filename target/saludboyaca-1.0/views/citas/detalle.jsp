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
    <title><fmt:message key="cita.detalle.titulo"/> #${cita.id} - SaludBoyacá</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&family=Space+Grotesk:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">
</head>
<body class="bg-body">
    
    <jsp:include page="../templates/header.jsp">
        <jsp:param name="menu" value="citas"/>
    </jsp:include>

    <div class="container py-4">

        <!-- HEADER -->
        <div class="page-header mb-4 animate-fade-in-up">
            <div class="row align-items-center g-3">
                <div class="col-12 col-lg-8">
                    <h2 class="font-title mb-1">
                        <i class="fas fa-file-medical me-2"></i>
                        <fmt:message key="cita.detalle.titulo"/> #${cita.id}
                    </h2>
                    <p class="mb-0 opacity-75">
                        <i class="fas fa-info-circle me-1"></i>
                        <fmt:message key="cita.detalle.subtitulo"/>
                    </p>
                </div>
                <div class="col-12 col-lg-4 text-lg-end">
                    <a href="${pageContext.request.contextPath}/citas" class="btn btn-outline">
                        <i class="fas fa-arrow-left"></i>
                        <span class="d-none d-md-inline ms-1"><fmt:message key="btn.volver"/></span>
                    </a>
                </div>
            </div>
        </div>

        <!-- DETALLE -->
        <div class="row justify-content-center animate-fade-in-up delay-1">
            <div class="col-12 col-lg-8">
                <div class="glass-card">
                    <div class="glass-card-header flex-wrap gap-2">
                        <h5 class="mb-0">
                            <i class="fas fa-file-medical"></i>
                            <fmt:message key="cita.info.titulo"/>
                        </h5>
                        <span class="badge badge-lg badge-estado-${cita.estado.toLowerCase()}">
                            <fmt:message key="cita.estado.${cita.estado.toLowerCase()}"/>
                        </span>
                    </div>

                    <div class="glass-card-body">
                        <!-- Grid de info -->
                        <div class="row g-3 mb-4">
                            <div class="col-12 col-sm-6 animate-fade-in-up delay-2">
                                <div class="info-card h-100">
                                    <div class="info-icon">
                                        <i class="fas fa-user-injured"></i>
                                    </div>
                                    <div>
                                        <label class="text-muted text-uppercase small fw-bold">
                                            <fmt:message key="cita.paciente"/>
                                        </label>
                                        <p class="font-title fw-bold text-title mb-0">${cita.nombrePaciente}</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-12 col-sm-6 animate-fade-in-up delay-2">
                                <div class="info-card h-100">
                                    <div class="info-icon">
                                        <i class="fas fa-user-doctor"></i>
                                    </div>
                                    <div>
                                        <label class="text-muted text-uppercase small fw-bold">
                                            <fmt:message key="cita.medico"/>
                                        </label>
                                        <p class="font-title fw-bold text-title mb-0">${cita.nombreMedico}</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-12 col-sm-6 animate-fade-in-up delay-3">
                                <div class="info-card h-100">
                                    <div class="info-icon">
                                        <i class="fas fa-stethoscope"></i>
                                    </div>
                                    <div>
                                        <label class="text-muted text-uppercase small fw-bold">
                                            <fmt:message key="cita.especialidad"/>
                                        </label>
                                        <p class="font-title fw-bold text-title mb-0">${cita.nombreEspecialidad}</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-12 col-sm-6 animate-fade-in-up delay-3">
                                <div class="info-card h-100">
                                    <div class="info-icon">
                                        <i class="fas fa-calendar-day"></i>
                                    </div>
                                    <div>
                                        <label class="text-muted text-uppercase small fw-bold">
                                            <fmt:message key="cita.fecha"/>
                                        </label>
                                        <p class="font-title fw-bold text-title mb-0">${cita.fechaCita}</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-12 col-sm-6 animate-fade-in-up delay-4">
                                <div class="info-card h-100">
                                    <div class="info-icon">
                                        <i class="fas fa-clock"></i>
                                    </div>
                                    <div>
                                        <label class="text-muted text-uppercase small fw-bold">
                                            <fmt:message key="cita.hora"/>
                                        </label>
                                        <p class="font-title fw-bold text-title mb-0">${cita.horaCita}</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-12 col-sm-6 animate-fade-in-up delay-4">
                                <div class="info-card h-100">
                                    <div class="info-icon">
                                        <i class="fas fa-hashtag"></i>
                                    </div>
                                    <div>
                                        <label class="text-muted text-uppercase small fw-bold">
                                            <fmt:message key="cita.id"/>
                                        </label>
                                        <p class="font-title fw-bold text-title mb-0">#${cita.id}</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Motivo -->
                        <div class="glass-card mb-4 animate-fade-in-up delay-5">
                            <div class="glass-card-body">
                                <h6 class="font-title text-title mb-3">
                                    <i class="fas fa-align-left me-2"></i>
                                    <fmt:message key="cita.motivo"/>
                                </h6>
                                <c:choose>
                                    <c:when test="${not empty cita.motivo}">
                                        <p class="mb-0">${cita.motivo}</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="text-muted fst-italic mb-0">
                                            <i class="fas fa-minus-circle me-1"></i>
                                            <fmt:message key="cita.motivo.vacio"/>
                                        </p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <!-- Cambiar estado -->
                        <div class="glass-card mb-4 animate-fade-in-up delay-5">
                            <div class="glass-card-body">
                                <h6 class="font-title text-title mb-3">
                                    <i class="fas fa-exchange-alt me-2"></i>
                                    <fmt:message key="cita.cambiar.estado"/>
                                </h6>
                                <div class="d-flex gap-2 flex-wrap">
                                    <a href="${pageContext.request.contextPath}/citas?accion=cambiarEstado&id=${cita.id}&estado=PROGRAMADA" 
                                       class="btn btn-sm ${cita.estado == 'PROGRAMADA' ? 'btn-warning' : 'btn-outline'}"
                                       onclick="return confirmarCambioEstado('PROGRAMADA', '${cita.nombrePaciente}')">
                                        <i class="fas fa-clock"></i>
                                        <span class="d-none d-sm-inline ms-1"><fmt:message key="cita.estado.programada"/></span>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/citas?accion=cambiarEstado&id=${cita.id}&estado=CONFIRMADA" 
                                       class="btn btn-sm ${cita.estado == 'CONFIRMADA' ? 'btn-success' : 'btn-outline'}"
                                       onclick="return confirmarCambioEstado('CONFIRMADA', '${cita.nombrePaciente}')">
                                        <i class="fas fa-check"></i>
                                        <span class="d-none d-sm-inline ms-1"><fmt:message key="cita.estado.confirmada"/></span>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/citas?accion=cambiarEstado&id=${cita.id}&estado=ATENDIDA" 
                                       class="btn btn-sm ${cita.estado == 'ATENDIDA' ? 'btn-primary' : 'btn-outline'}"
                                       onclick="return confirmarCambioEstado('ATENDIDA', '${cita.nombrePaciente}')">
                                        <i class="fas fa-user-check"></i>
                                        <span class="d-none d-sm-inline ms-1"><fmt:message key="cita.estado.atendida"/></span>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/citas?accion=cambiarEstado&id=${cita.id}&estado=CANCELADA" 
                                       class="btn btn-sm ${cita.estado == 'CANCELADA' ? 'btn-danger' : 'btn-outline'}"
                                       onclick="return confirmarCambioEstado('CANCELADA', '${cita.nombrePaciente}')">
                                        <i class="fas fa-times"></i>
                                        <span class="d-none d-sm-inline ms-1"><fmt:message key="cita.estado.cancelada"/></span>
                                    </a>
                                </div>
                            </div>
                        </div>

                        <!-- Acciones -->
                        <div class="d-flex justify-content-between align-items-center flex-wrap gap-3 pt-4 border-top animate-fade-in-up delay-5">
                            <a href="${pageContext.request.contextPath}/citas" class="btn btn-outline">
                                <i class="fas fa-arrow-left"></i>
                                <span class="d-none d-md-inline ms-1"><fmt:message key="cita.volver.lista"/></span>
                            </a>
                            <a href="${pageContext.request.contextPath}/comprobante?id=${cita.id}" class="btn btn-warning" target="_blank">
                                <i class="fas fa-file-pdf"></i>
                                <span class="d-none d-md-inline ms-1"><fmt:message key="cita.descargar"/></span>
                            </a>
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
    
    <script>
        function confirmarCambioEstado(estado, paciente) {
            SwalConfirmarEstado(estado, paciente, function() {
                window.location.href = '${pageContext.request.contextPath}/citas?accion=cambiarEstado&id=${cita.id}&estado=' + estado;
            });
            return false;
        }
    </script>
</body>
</html>