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
    <title>
        <c:choose>
            <c:when test="${cita != null}">
                <fmt:message key="cita.editar.titulo"/>
            </c:when>
            <c:otherwise>
                <fmt:message key="cita.nuevo.titulo"/>
            </c:otherwise>
        </c:choose> - SaludBoyacá
    </title>
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
                        <i class="fas ${cita != null ? 'fa-calendar-edit' : 'fa-calendar-plus'} me-2"></i>
                        <c:choose>
                            <c:when test="${cita != null}">
                                <fmt:message key="cita.editar.titulo"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="cita.nuevo.titulo"/>
                            </c:otherwise>
                        </c:choose>
                    </h2>
                    <p class="mb-0 opacity-75">
                        <i class="fas fa-info-circle me-1"></i>
                        <c:choose>
                            <c:when test="${cita != null}">
                                <fmt:message key="cita.subtitulo.editar"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="cita.subtitulo.nuevo"/>
                            </c:otherwise>
                        </c:choose>
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

        <!-- FORMULARIO -->
        <div class="row justify-content-center animate-fade-in-up delay-1">
            <div class="col-12 col-lg-8">
                <div class="glass-card">
                    <div class="glass-card-header">
                        <h5 class="mb-0">
                            <i class="fas ${cita != null ? 'fa-calendar-edit' : 'fa-calendar-plus'}"></i>
                            <c:choose>
                                <c:when test="${cita != null}">
                                    <fmt:message key="cita.info.editar"/>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="cita.info.nuevo"/>
                                </c:otherwise>
                            </c:choose>
                        </h5>
                    </div>
                    
                    <div class="glass-card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">
                                <i class="fas fa-circle-exclamation"></i>
                                ${error}
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/citas" method="post">
                            <c:if test="${cita != null}">
                                <input type="hidden" name="id" value="${cita.id}">
                            </c:if>

                            <div class="form-row">
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">
                                            <i class="fas fa-user-injured"></i>
                                            <fmt:message key="cita.paciente"/>
                                            <span class="required">*</span>
                                        </label>
                                        <select name="pacienteId" class="form-select" required>
                                            <option value=""><fmt:message key="cita.select.paciente"/></option>
                                            <c:forEach items="${pacientes}" var="p">
                                                <%-- CORREGIDO: cita.idPaciente en lugar de cita.pacienteId --%>
                                                <option value="${p.id}" ${cita.idPaciente == p.id ? 'selected' : ''}>
                                                    ${p.nombreCompleto} - ${p.documento}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">
                                            <i class="fas fa-user-doctor"></i>
                                            <fmt:message key="cita.medico"/>
                                            <span class="required">*</span>
                                        </label>
                                        <select name="medicoId" class="form-select" required>
                                            <option value=""><fmt:message key="cita.select.medico"/></option>
                                            <c:forEach items="${medicos}" var="m">
                                                <%-- CORREGIDO: cita.idMedico en lugar de cita.medicoId --%>
                                                <option value="${m.id}" ${cita.idMedico == m.id ? 'selected' : ''}>
                                                    ${m.nombreCompleto} - ${m.especialidad}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">
                                            <i class="fas fa-calendar-day"></i>
                                            <fmt:message key="cita.fecha"/>
                                            <span class="required">*</span>
                                        </label>
                                        <input type="date" name="fechaCita" class="form-control" 
                                               value="${cita.fechaCita}" required>
                                    </div>
                                </div>
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">
                                            <i class="fas fa-clock"></i>
                                            <fmt:message key="cita.hora"/>
                                            <span class="required">*</span>
                                        </label>
                                        <input type="time" name="horaCita" class="form-control" 
                                               value="${cita.horaCita}" required>
                                    </div>
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">
                                            <i class="fas fa-stethoscope"></i>
                                            <fmt:message key="cita.especialidad"/>
                                            <span class="required">*</span>
                                        </label>
                                        <select name="especialidadId" class="form-select" required>
                                            <option value=""><fmt:message key="cita.select.especialidad"/></option>
                                            <c:forEach items="${especialidades}" var="e">
                                                <%-- CORREGIDO: cita.idEspecialidad en lugar de cita.especialidadId --%>
                                                <option value="${e.id}" ${cita.idEspecialidad == e.id ? 'selected' : ''}>
                                                    ${e.nombre}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">
                                            <i class="fas fa-tag"></i>
                                            <fmt:message key="cita.estado"/>
                                            <span class="required">*</span>
                                        </label>
                                        <select name="estado" class="form-select" required>
                                            <option value="PROGRAMADA" ${cita.estado == 'PROGRAMADA' ? 'selected' : ''}>
                                                <fmt:message key="cita.estado.programada"/>
                                            </option>
                                            <option value="CONFIRMADA" ${cita.estado == 'CONFIRMADA' ? 'selected' : ''}>
                                                <fmt:message key="cita.estado.confirmada"/>
                                            </option>
                                            <option value="ATENDIDA" ${cita.estado == 'ATENDIDA' ? 'selected' : ''}>
                                                <fmt:message key="cita.estado.atendida"/>
                                            </option>
                                            <option value="CANCELADA" ${cita.estado == 'CANCELADA' ? 'selected' : ''}>
                                                <fmt:message key="cita.estado.cancelada"/>
                                            </option>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-align-left"></i>
                                    <fmt:message key="cita.motivo"/>
                                </label>
                                <textarea name="motivo" class="form-control" rows="3"
                                          placeholder="<fmt:message key='cita.placeholder.motivo'/>">${cita.motivo}</textarea>
                            </div>

                            <div class="form-actions">
                                <a href="${pageContext.request.contextPath}/citas" class="btn btn-outline">
                                    <i class="fas fa-times"></i>
                                    <fmt:message key="cita.cancelar"/>
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save"></i>
                                    <fmt:message key="cita.guardar"/>
                                </button>
                            </div>
                        </form>
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
