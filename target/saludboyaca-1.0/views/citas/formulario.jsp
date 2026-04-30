<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
    <head>
        <meta charset="UTF-8">
        <title>${cita != null ? 'Editar' : 'Nueva'} Cita - SaludBoyaca</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <style>
            .navbar-saludboyaca {
                background: #1A5276 !important;
            }
        </style>
    </head>
    <body>
        <jsp:include page="../templates/header.jsp">
            <jsp:param name="menu" value="citas" />
        </jsp:include>

        <div class="container mt-4">
            <div class="row justify-content-center">
                <div class="col-md-10">
                    <div class="card shadow">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0">
                                <i class="fas fa-calendar-plus me-2"></i>
                                ${cita != null ? 'Editar Cita' : 'Nueva Cita'}
                            </h5>
                        </div>
                        <div class="card-body">
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger">${error}</div>
                            </c:if>

                            <form action="${pageContext.request.contextPath}/citas" method="post">
                                <c:if test="${cita != null}">
                                    <input type="hidden" name="id" value="${cita.id}">
                                </c:if>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label"><fmt:message key="cita.paciente" /> *</label>
                                        <select name="idPaciente" class="form-select" required>
                                            <option value="">Seleccione...</option>
                                            <c:forEach items="${pacientes}" var="p">
                                                <option value="${p.id}" ${cita.idPaciente == p.id ? 'selected' : ''}>
                                                    ${p.nombreCompleto} - ${p.documento}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="col-md-6 mb-3">
                                        <label class="form-label"><fmt:message key="cita.especialidad" /> *</label>
                                        <select name="idEspecialidad" class="form-select" required>
                                            <option value="">Seleccione...</option>
                                            <c:forEach items="${especialidades}" var="e">
                                                <option value="${e.id}" ${cita.idEspecialidad == e.id ? 'selected' : ''}>
                                                    ${e.nombre}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label"><fmt:message key="cita.medico" /> *</label>
                                        <select name="idMedico" class="form-select" required>
                                            <option value="">Seleccione...</option>
                                            <c:forEach items="${medicos}" var="m">
                                                <c:if test="${m.rol == 'MEDICO'}">
                                                    <option value="${m.id}" ${cita.idMedico == m.id ? 'selected' : ''}>
                                                        ${m.nombreCompleto} - ${m.especialidad}
                                                    </option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="col-md-3 mb-3">
                                        <label class="form-label"><fmt:message key="cita.fecha" /> *</label>
                                        <input type="date" name="fechaCita" class="form-control" 
                                               value="${fechaFormateada != null ? fechaFormateada : cita.fechaCita}" required>
                                    </div>

                                    <div class="col-md-3 mb-3">
                                        <label class="form-label"><fmt:message key="cita.hora" /> *</label>
                                        <input type="time" name="horaCita" class="form-control" 
                                               value="${horaFormateada != null ? horaFormateada : cita.horaCita}" required>
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><fmt:message key="cita.motivo" /></label>
                                    <textarea name="motivo" class="form-control" rows="3">${cita.motivo}</textarea>
                                </div>

                                <div class="d-flex justify-content-between">
                                    <a href="${pageContext.request.contextPath}/citas" class="btn btn-secondary">
                                        <fmt:message key="paciente.cancelar" />
                                    </a>
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fas fa-save me-2"></i><fmt:message key="cita.guardar" />
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>