<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><fmt:message key="consulta.titulo" /> - SaludBoyaca</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --color-primario: #1A5276;
            --color-acento: #2E86C1;
        }
        
        body {
            background: linear-gradient(135deg, #1A5276 0%, #2E86C1 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .consulta-card {
            background: white;
            border-radius: 16px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.3);
            max-width: 600px;
            width: 100%;
            overflow: hidden;
        }
        
        .consulta-header {
            background: var(--color-primario);
            color: white;
            padding: 30px;
            text-align: center;
        }
        
        .consulta-header i {
            font-size: 3rem;
            margin-bottom: 10px;
        }
        
        .consulta-body {
            padding: 30px;
        }
        
        .captcha-img {
            border: 2px solid #ddd;
            border-radius: 8px;
            cursor: pointer;
        }
        
        .btn-consultar {
            background: var(--color-primario);
            color: white;
            border: none;
            width: 100%;
            padding: 12px;
            border-radius: 8px;
            font-weight: 600;
        }
        
        .btn-consultar:hover {
            background: #154360;
            color: white;
        }
        
        .estado-PROGRAMADA { background: #F39C12; color: white; }
        .estado-CONFIRMADA { background: #27AE60; color: white; }
        .estado-ATENDIDA { background: #2980B9; color: white; }
        .estado-CANCELADA { background: #E74C3C; color: white; }
    </style>
</head>
<body>
    <div class="container">
        <div class="consulta-card">
            <div class="consulta-header">
                <i class="fas fa-search"></i>
                <h4><fmt:message key="consulta.titulo" /></h4>
                <small><fmt:message key="app.institucion" /></small>
            </div>
            
            <div class="consulta-body">
                <p class="text-muted text-center mb-4">
                    <fmt:message key="consulta.instruccion" />
                </p>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        <i class="fas fa-exclamation-circle me-2"></i>
                        <fmt:message key="${error}" />
                    </div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/consulta" method="post">
                    <div class="mb-3">
                        <label class="form-label">
                            <i class="fas fa-id-card me-2"></i>
                            <fmt:message key="consulta.documento" />
                        </label>
                        <input type="text" name="documento" class="form-control form-control-lg" 
                               value="${documento}" required placeholder="1052345681">
                    </div>
                    
                    <div class="mb-3">
                        <label class="form-label">
                            <i class="fas fa-shield-alt me-2"></i>
                            <fmt:message key="consulta.captcha" />
                        </label>
                        <div class="row align-items-center">
                            <div class="col-md-6">
                                <img src="${pageContext.request.contextPath}/captcha" 
                                     alt="CAPTCHA" class="captcha-img img-fluid" 
                                     onclick="this.src='${pageContext.request.contextPath}/captcha?'+Date.now()"
                                     title="Click para recargar">
                            </div>
                            <div class="col-md-6">
                                <input type="text" name="captcha" class="form-control form-control-lg" 
                                       maxlength="6" required placeholder="ABC123">
                                <small class="text-muted">Click en la imagen para cambiar</small>
                            </div>
                        </div>
                    </div>
                    
                    <button type="submit" class="btn btn-consultar btn-lg">
                        <i class="fas fa-search me-2"></i>
                        <fmt:message key="consulta.buscar" />
                    </button>
                </form>
                
                <!-- Resultados -->
                <c:if test="${resultado}">
                    <hr class="my-4">
                    
                    <h5 class="mb-3">
                        <i class="fas fa-list-alt me-2"></i>
                        Citas encontradas para: <strong>${documento}</strong>
                    </h5>
                    
                    <c:choose>
                        <c:when test="${empty citas}">
                            <div class="alert alert-warning">
                                <i class="fas fa-info-circle me-2"></i>
                                <fmt:message key="consulta.no.encontrado" />
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-sm table-hover">
                                    <thead class="table-dark">
                                        <tr>
                                            <th>Fecha</th>
                                            <th>Hora</th>
                                            <th>Médico</th>
                                            <th>Especialidad</th>
                                            <th>Estado</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${citas}" var="c">
                                            <tr>
                                                <td>${c.fechaCita}</td>
                                                <td>${c.horaCita}</td>
                                                <td>${c.nombreMedico}</td>
                                                <td>${c.nombreEspecialidad}</td>
                                                <td>
                                                    <span class="badge estado-${c.estado}">
                                                        <fmt:message key="cita.estado.${c.estado.toLowerCase()}" />
                                                    </span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                
                <div class="text-center mt-3">
                    <a href="${pageContext.request.contextPath}/login" class="text-decoration-none">
                        <i class="fas fa-user-lock me-1"></i>
                        <fmt:message key="login.titulo" />
                    </a>
                </div>
            </div>
        </div>
        
        <div class="text-center text-white mt-3">
            <small><fmt:message key="app.footer" /></small>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>