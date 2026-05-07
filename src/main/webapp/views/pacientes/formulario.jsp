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
            <c:when test="${paciente != null}">
                <fmt:message key="paciente.editar.titulo"/>
            </c:when>
            <c:otherwise>
                <fmt:message key="paciente.nuevo.titulo"/>
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
        <jsp:param name="menu" value="pacientes"/>
    </jsp:include>

    <div class="container py-4">

        <!-- HEADER -->
        <div class="page-header mb-4 animate-fade-in-up">
            <div class="row align-items-center g-3">
                <div class="col-12 col-lg-8">
                    <h2 class="font-title mb-1">
                        <i class="fas ${paciente != null ? 'fa-user-edit' : 'fa-user-plus'} me-2"></i>
                        <c:choose>
                            <c:when test="${paciente != null}">
                                <fmt:message key="paciente.editar.titulo"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="paciente.nuevo.titulo"/>
                            </c:otherwise>
                        </c:choose>
                    </h2>
                    <p class="mb-0 opacity-75">
                        <i class="fas fa-info-circle me-1"></i>
                        <c:choose>
                            <c:when test="${paciente != null}">
                                <fmt:message key="paciente.subtitulo.editar"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="paciente.subtitulo.nuevo"/>
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
                <div class="col-12 col-lg-4 text-lg-end">
                    <a href="${pageContext.request.contextPath}/pacientes" class="btn btn-outline">
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
                            <i class="fas ${paciente != null ? 'fa-user-edit' : 'fa-user-plus'}"></i>
                            <c:choose>
                                <c:when test="${paciente != null}">
                                    <fmt:message key="paciente.info.editar"/>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="paciente.info.paciente"/>
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

                        <form action="${pageContext.request.contextPath}/pacientes" method="post">
                            <c:if test="${paciente != null}">
                                <input type="hidden" name="id" value="${paciente.id}">
                            </c:if>

                            <div class="form-row">
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">
                                            <i class="fas fa-user"></i>
                                            <fmt:message key="paciente.nombres"/>
                                            <span class="required">*</span>
                                        </label>
                                        <input type="text" name="nombres" class="form-control" 
                                               value="${paciente.nombres}" required 
                                               placeholder="<fmt:message key='paciente.placeholder.nombres'/>">
                                    </div>
                                </div>
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">
                                            <i class="fas fa-user"></i>
                                            <fmt:message key="paciente.apellidos"/>
                                            <span class="required">*</span>
                                        </label>
                                        <input type="text" name="apellidos" class="form-control" 
                                               value="${paciente.apellidos}" required 
                                               placeholder="<fmt:message key='paciente.placeholder.apellidos'/>">
                                    </div>
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">
                                            <i class="fas fa-id-card"></i>
                                            <fmt:message key="paciente.documento"/>
                                            <span class="required">*</span>
                                        </label>
                                        <input type="text" name="documento" class="form-control" 
                                               value="${paciente.documento}" required 
                                               placeholder="<fmt:message key='paciente.placeholder.documento'/>">
                                    </div>
                                </div>
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">
                                            <i class="fas fa-calendar"></i>
                                            <fmt:message key="paciente.nacimiento"/>
                                            <span class="required">*</span>
                                        </label>
                                        <input type="date" name="fechaNacimiento" class="form-control" 
                                               value="${paciente.fechaNacimiento}" required>
                                    </div>
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">
                                            <i class="fas fa-phone"></i>
                                            <fmt:message key="paciente.telefono"/>
                                        </label>
                                        <input type="tel" name="telefono" class="form-control" 
                                               value="${paciente.telefono}" 
                                               placeholder="<fmt:message key='paciente.placeholder.telefono'/>">
                                    </div>
                                </div>
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">
                                            <i class="fas fa-envelope"></i>
                                            <fmt:message key="paciente.email"/>
                                        </label>
                                        <input type="email" name="email" class="form-control" 
                                               value="${paciente.email}" 
                                               placeholder="<fmt:message key='paciente.placeholder.email'/>">
                                    </div>
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">
                                            <i class="fas fa-hospital"></i>
                                            <fmt:message key="paciente.eps"/>
                                            <span class="required">*</span>
                                        </label>
                                        <input type="text" name="eps" class="form-control" 
                                               value="${paciente.eps}" required 
                                               placeholder="<fmt:message key='paciente.placeholder.eps'/>">
                                    </div>
                                </div>
                                <div class="form-col">
                                    <div class="form-group">
                                        <label class="form-label">
                                            <i class="fas fa-map-marker-alt"></i>
                                            <fmt:message key="paciente.vereda"/>
                                        </label>
                                        <input type="text" name="veredaBarrio" class="form-control" 
                                               value="${paciente.veredaBarrio}" 
                                               placeholder="<fmt:message key='paciente.placeholder.vereda'/>">
                                    </div>
                                </div>
                            </div>

                            <div class="form-actions">
                                <a href="${pageContext.request.contextPath}/pacientes" class="btn btn-outline">
                                    <i class="fas fa-times"></i>
                                    <fmt:message key="paciente.cancelar"/>
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save"></i>
                                    <fmt:message key="paciente.guardar"/>
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