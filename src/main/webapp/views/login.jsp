<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${empty sessionScope.locale}">
    <c:set var="locale" value="es" scope="session" />
</c:if>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><fmt:message key="login.titulo" /> - SaludBoyaca</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --color-primario: #1A5276;
            --color-secundario: #39A900;
            --color-acento: #2E86C1;
        }
        
        body {
            background: linear-gradient(135deg, #1A5276 0%, #2E86C1 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .login-card {
            background: #D6EAF8;
            border-radius: 16px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.3);
            max-width: 420px;
            width: 100%;
            padding: 0;
            overflow: hidden;
        }
        
        .login-header {
            background: var(--color-primario);
            color: white;
            padding: 30px;
            text-align: center;
        }
        
        .login-header i {
            font-size: 3rem;
            margin-bottom: 10px;
        }
        
        .login-body {
            padding: 30px;
        }
        
        .btn-saludboyaca {
            background: var(--color-primario);
            color: white;
            border: none;
            width: 100%;
            padding: 12px;
            border-radius: 8px;
            font-weight: 600;
        }
        
        .btn-saludboyaca:hover {
            background: #154360;
            color: white;
        }
        
        .language-selector {
            text-align: center;
            margin-top: 20px;
        }
        
        .language-selector a {
            margin: 0 10px;
            text-decoration: none;
            font-size: 1.2rem;
        }
        
        .language-selector a.active {
            font-weight: bold;
            border-bottom: 2px solid var(--color-primario);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="login-card">
            <div class="login-header">
                <i class="fas fa-hospital-alt"></i>
                <h4><fmt:message key="app.nombre" /></h4>
                <small><fmt:message key="app.institucion" /></small>
            </div>
            
            <div class="login-body">
                <h5 class="text-center mb-4"><fmt:message key="login.titulo" /></h5>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show">
                        <i class="fas fa-exclamation-circle me-2"></i>${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/login" method="post">
                    <div class="mb-3">
                        <label class="form-label">
                            <i class="fas fa-user me-2"></i><fmt:message key="login.usuario" />
                        </label>
                        <input type="text" name="username" class="form-control form-control-lg" 
                               required autofocus placeholder="cpedraza">
                    </div>
                    
                    <div class="mb-4">
                        <label class="form-label">
                            <i class="fas fa-lock me-2"></i><fmt:message key="login.contrasena" />
                        </label>
                        <input type="password" name="password" class="form-control form-control-lg" 
                               required placeholder="admin123">
                    </div>
                    
                    <button type="submit" class="btn btn-saludboyaca btn-lg">
                        <i class="fas fa-sign-in-alt me-2"></i><fmt:message key="login.ingresar" />
                    </button>
                </form>
                
                <hr class="my-4">
                
                <div class="language-selector">
                    <a href="?lang=es" class="${sessionScope.lang == 'es' ? 'active' : ''}">🇨🇴 ES</a>
                    <a href="?lang=en" class="${sessionScope.lang == 'en' ? 'active' : ''}">🇺🇸 EN</a>
                    <a href="?lang=it" class="${sessionScope.lang == 'it' ? 'active' : ''}">🇮🇹 IT</a>
                </div>
                
                <div class="text-center mt-3">
                    <a href="${pageContext.request.contextPath}/consulta" class="text-decoration-none">
                        <i class="fas fa-search me-1"></i><fmt:message key="nav.consulta" />
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