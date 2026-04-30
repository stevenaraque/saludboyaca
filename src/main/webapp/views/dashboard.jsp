<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="nav.dashboard" /> - SaludBoyaca</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        .navbar-saludboyaca { background: #1A5276 !important; }
        
        .card-stat {
            border-left: 4px solid;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            transition: transform 0.2s;
        }
        
        .card-stat:hover { transform: translateY(-5px); }
        
        .card-stat.primary { border-left-color: #1A5276; }
        .card-stat.success { border-left-color: #39A900; }
        .card-stat.info { border-left-color: #2E86C1; }
        .card-stat.warning { border-left-color: #F39C12; }
    </style>
</head>
<body>
    <!-- USAR EL HEADER TEMPLATE (navbar reutilizable) -->
    <jsp:include page="templates/header.jsp">
        <jsp:param name="menu" value="dashboard" />
    </jsp:include>

    <div class="container mt-4">
        <h3>
            <fmt:message key="dashboard.bienvenida">
                <fmt:param value="${sessionScope.usuarioNombre}" />
            </fmt:message>
        </h3>
        <p class="text-muted">Rol: ${rol}</p>
        
        <div class="row mt-4">
            <div class="col-md-3">
                <div class="card card-stat primary mb-4">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h6 class="text-muted"><fmt:message key="dashboard.citas.hoy" /></h6>
                                <h3>0</h3>
                            </div>
                            <i class="fas fa-calendar-day fa-2x text-primary"></i>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-3">
                <div class="card card-stat warning mb-4">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h6 class="text-muted"><fmt:message key="dashboard.citas.pendientes" /></h6>
                                <h3>0</h3>
                            </div>
                            <i class="fas fa-clock fa-2x text-warning"></i>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-3">
                <div class="card card-stat success mb-4">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h6 class="text-muted"><fmt:message key="dashboard.citas.mes" /></h6>
                                <h3>0</h3>
                            </div>
                            <i class="fas fa-calendar-alt fa-2x text-success"></i>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-3">
                <div class="card card-stat info mb-4">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h6 class="text-muted"><fmt:message key="dashboard.pacientes.total" /></h6>
                                <h3>0</h3>
                            </div>
                            <i class="fas fa-users fa-2x text-info"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="alert alert-info">
            <i class="fas fa-info-circle me-2"></i>
            Dashboard en construccion. Los modulos CRUD se agregaran en las proximas sesiones.
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>