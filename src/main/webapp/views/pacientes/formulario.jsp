<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <title>${paciente != null ? 'Editar' : 'Nuevo'} Paciente - SaludBoyaca</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        .navbar-saludboyaca { background: #1A5276 !important; }
    </style>
</head>
<body>
    <jsp:include page="../templates/header.jsp">
        <jsp:param name="menu" value="pacientes" />
    </jsp:include>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">
                            <i class="fas fa-user-plus me-2"></i>
                            ${paciente != null ? 'Editar Paciente' : 'Nuevo Paciente'}
                        </h5>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">${error}</div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/pacientes" method="post">
                            <c:if test="${paciente != null}">
                                <input type="hidden" name="id" value="${paciente.id}">
                            </c:if>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label"><fmt:message key="paciente.nombres" /> *</label>
                                    <input type="text" name="nombres" class="form-control" 
                                           value="${paciente.nombres}" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label"><fmt:message key="paciente.apellidos" /> *</label>
                                    <input type="text" name="apellidos" class="form-control" 
                                           value="${paciente.apellidos}" required>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label"><fmt:message key="paciente.documento" /> *</label>
                                    <input type="text" name="documento" class="form-control" 
                                           value="${paciente.documento}" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label"><fmt:message key="paciente.nacimiento" /> *</label>
                                    <input type="date" name="fechaNacimiento" class="form-control" 
                                           value="${paciente.fechaNacimiento}" required>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label"><fmt:message key="paciente.telefono" /></label>
                                    <input type="tel" name="telefono" class="form-control" 
                                           value="${paciente.telefono}">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label"><fmt:message key="paciente.email" /></label>
                                    <input type="email" name="email" class="form-control" 
                                           value="${paciente.email}">
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label"><fmt:message key="paciente.eps" /> *</label>
                                    <input type="text" name="eps" class="form-control" 
                                           value="${paciente.eps}" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label"><fmt:message key="paciente.vereda" /></label>
                                    <input type="text" name="veredaBarrio" class="form-control" 
                                           value="${paciente.veredaBarrio}">
                                </div>
                            </div>

                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/pacientes" class="btn btn-secondary">
                                    <fmt:message key="paciente.cancelar" />
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save me-2"></i><fmt:message key="paciente.guardar" />
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