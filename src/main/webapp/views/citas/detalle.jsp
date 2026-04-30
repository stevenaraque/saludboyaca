<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
    <head>
        <meta charset="UTF-8">
        <title>Detalle Cita - SaludBoyaca</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <style>
            .navbar-saludboyaca {
                background: #1A5276 !important;
            }
            .badge-programada {
                background: #F39C12;
                color: white;
            }
            .badge-confirmada {
                background: #27AE60;
                color: white;
            }
            .badge-atendida {
                background: #2980B9;
                color: white;
            }
            .badge-cancelada {
                background: #E74C3C;
                color: white;
            }
        </style>
    </head>
    <body>
        <jsp:include page="../templates/header.jsp">
            <jsp:param name="menu" value="citas" />
        </jsp:include>

        <div class="container mt-4">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card shadow">
                        <div class="card-header bg-info text-white d-flex justify-content-between">
                            <h5 class="mb-0"><i class="fas fa-file-medical me-2"></i>Detalle de Cita #${cita.id}</h5>
                            <span class="badge badge-${cita.estado.toLowerCase()} fs-6">
                                <fmt:message key="cita.estado.${cita.estado.toLowerCase()}" />
                            </span>
                        </div>
                        <div class="card-body">
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="text-muted"><fmt:message key="cita.paciente" /></label>
                                    <p class="fw-bold">${cita.nombrePaciente}</p>
                                </div>
                                <div class="col-md-6">
                                    <label class="text-muted"><fmt:message key="cita.medico" /></label>
                                    <p class="fw-bold">${cita.nombreMedico}</p>
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="text-muted"><fmt:message key="cita.especialidad" /></label>
                                    <p class="fw-bold">${cita.nombreEspecialidad}</p>
                                </div>
                                <div class="col-md-6">
                                    <label class="text-muted"><fmt:message key="cita.fecha" /> / <fmt:message key="cita.hora" /></label>
                                    <p class="fw-bold">${cita.fechaCita} ${cita.horaCita}</p>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="text-muted"><fmt:message key="cita.motivo" /></label>
                                <p>${cita.motivo != null ? cita.motivo : 'No especificado'}</p>
                            </div>

                            <hr>

                            <h6><fmt:message key="cita.cambiar.estado" /></h6>
                            <div class="btn-group mb-3">
                                <a href="${pageContext.request.contextPath}/citas?accion=cambiarEstado&id=${cita.id}&estado=PROGRAMADA" 
                                   class="btn btn-warning ${cita.estado == 'PROGRAMADA' ? 'active' : ''}">
                                    <fmt:message key="cita.estado.programada" />
                                </a>
                                <a href="${pageContext.request.contextPath}/citas?accion=cambiarEstado&id=${cita.id}&estado=CONFIRMADA" 
                                   class="btn btn-success ${cita.estado == 'CONFIRMADA' ? 'active' : ''}">
                                    <fmt:message key="cita.estado.confirmada" />
                                </a>
                                <a href="${pageContext.request.contextPath}/citas?accion=cambiarEstado&id=${cita.id}&estado=ATENDIDA" 
                                   class="btn btn-info ${cita.estado == 'ATENDIDA' ? 'active' : ''}">
                                    <fmt:message key="cita.estado.atendida" />
                                </a>
                                <a href="${pageContext.request.contextPath}/citas?accion=cambiarEstado&id=${cita.id}&estado=CANCELADA" 
                                   class="btn btn-danger ${cita.estado == 'CANCELADA' ? 'active' : ''}">
                                    <fmt:message key="cita.estado.cancelada" />
                                </a>
                            </div>
                        </div>
                        <div class="card-footer d-flex justify-content-between">
                            <a href="${pageContext.request.contextPath}/citas" class="btn btn-secondary">
                                <i class="fas fa-arrow-left me-2"></i>Volver
                            </a>
                            <div>
                                <a href="${pageContext.request.contextPath}/comprobante?id=${cita.id}" class="btn btn-primary">
                                    <i class="fas fa-file-pdf me-2"></i><fmt:message key="cita.descargar" />
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>