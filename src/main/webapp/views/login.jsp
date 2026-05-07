<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:if test="${empty sessionScope.locale}">
    <c:set var="locale" value="es" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><fmt:message key="login.titulo"/> - SaludBoyacá</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&family=Space+Grotesk:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">
</head>
<body>
    <div class="login-split">
        <div class="login-welcome-panel">
            <div class="particle particle-1"></div>
            <div class="particle particle-2"></div>
            <div class="particle particle-3"></div>
            <div class="particle particle-4"></div>
            
            <div class="welcome-content animate-fade-in-left">
                <div class="welcome-logo">
                    <i class="fas fa-heart-pulse"></i>
                </div>
                
                <h1 class="welcome-title">
                    Salud<span>Boyacá</span>
                </h1>
                
                <p class="welcome-subtitle">
                    <fmt:message key="app.nombre"/>
                </p>
                
                <div class="welcome-features">
                    <div class="welcome-feature-item delay-1">
                        <i class="fas fa-calendar-check"></i>
                        <div>
                            <strong><fmt:message key="login.feature.agendamiento"/></strong>
                            <span><fmt:message key="login.feature.agendamiento.desc"/></span>
                        </div>
                    </div>
                    
                    <div class="welcome-feature-item delay-2">
                        <i class="fas fa-shield-halved"></i>
                        <div>
                            <strong><fmt:message key="login.feature.otp"/></strong>
                            <span><fmt:message key="login.feature.otp.desc"/></span>
                        </div>
                    </div>
                    
                    <div class="welcome-feature-item delay-3">
                        <i class="fas fa-globe"></i>
                        <div>
                            <strong><fmt:message key="login.feature.idioma"/></strong>
                            <span><fmt:message key="login.feature.idioma.desc"/></span>
                        </div>
                    </div>
                    
                    <div class="welcome-feature-item delay-4">
                        <i class="fas fa-file-pdf"></i>
                        <div>
                            <strong><fmt:message key="login.feature.pdf"/></strong>
                            <span><fmt:message key="login.feature.pdf.desc"/></span>
                        </div>
                    </div>
                </div>
                
                <div class="welcome-footer">
                    <i class="fas fa-building-columns me-2"></i>
                    <fmt:message key="app.footer"/>
                </div>
            </div>
        </div>
        
        <div class="login-form-panel">
            <div class="login-form-container">
                <div class="login-form-header animate-fade-in-right">
                    <h2><fmt:message key="login.titulo"/></h2>
                    <p><fmt:message key="login.subtitulo"/></p>
                </div>
                
                <div class="login-card animate-fade-in-up delay-1">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">
                            <i class="fas fa-circle-exclamation"></i>
                            ${error}
                        </div>
                    </c:if>
                    
                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <div class="form-group">
                            <label class="form-label">
                                <i class="fas fa-user"></i>
                                <fmt:message key="login.usuario"/>
                            </label>
                            <input type="text" name="username" class="form-control" 
                                   required autofocus placeholder="cpedraza">
                        </div>
                        
                        <div class="form-group">
                            <label class="form-label">
                                <i class="fas fa-lock"></i>
                                <fmt:message key="login.contrasena"/>
                            </label>
                            <input type="password" name="password" class="form-control" 
                                   required placeholder="••••••••">
                        </div>
                        
                        <button type="submit" class="btn btn-primary btn-lg w-100">
                            <i class="fas fa-arrow-right-to-bracket"></i>
                            <fmt:message key="login.ingresar"/>
                        </button>
                    </form>
                    
                    <div class="language-selector">
                        <button type="button" class="lang-btn ${sessionScope.lang == 'es' ? 'active' : ''}" data-lang="es">
                            🇨🇴 ES
                        </button>
                        <button type="button" class="lang-btn ${sessionScope.lang == 'en' ? 'active' : ''}" data-lang="en">
                            🇺🇸 EN
                        </button>
                        <button type="button" class="lang-btn ${sessionScope.lang == 'it' ? 'active' : ''}" data-lang="it">
                            🇮🇹 IT
                        </button>
                    </div>
                    
                    <div class="text-center mt-3">
                        <a href="${pageContext.request.contextPath}/consulta" class="btn btn-outline w-100">
                            <i class="fas fa-magnifying-glass"></i>
                            <fmt:message key="nav.consulta"/>
                        </a>
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