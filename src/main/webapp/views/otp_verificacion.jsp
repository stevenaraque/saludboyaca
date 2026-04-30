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
    <title><fmt:message key="otp.titulo" /> - SaludBoyaca</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #1A5276 0%, #2E86C1 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .otp-card {
            background: white;
            border-radius: 16px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.3);
            max-width: 420px;
            width: 100%;
            overflow: hidden;
            border-top: 4px solid #6C3483;
        }
        
        .otp-header {
            background: #6C3483;
            color: white;
            padding: 30px;
            text-align: center;
        }
        
        .otp-header i {
            font-size: 3rem;
            margin-bottom: 10px;
        }
        
        .otp-body {
            padding: 30px;
        }
        
        .otp-input {
            font-size: 2rem;
            letter-spacing: 12px;
            text-align: center;
            font-weight: bold;
        }
        
        .btn-verificar {
            background: #6C3483;
            color: white;
            border: none;
            width: 100%;
            padding: 12px;
            border-radius: 8px;
            font-weight: 600;
        }
        
        .btn-verificar:hover {
            background: #5B2C6F;
            color: white;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="otp-card">
            <div class="otp-header">
                <i class="fas fa-shield-alt"></i>
                <h4><fmt:message key="otp.titulo" /></h4>
            </div>
            
            <div class="otp-body">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        <i class="fas fa-exclamation-circle me-2"></i>${error}
                    </div>
                </c:if>
                
                <p class="text-center text-muted mb-4">
                    <fmt:message key="otp.instruccion">
                        <fmt:param value="${emailMasked}" />
                    </fmt:message>
                </p>
                
                <form action="${pageContext.request.contextPath}/otp" method="post">
                    <div class="mb-4">
                        <label class="form-label fw-bold">
                            <fmt:message key="otp.campo" />
                        </label>
                        <input type="text" name="otpCodigo" class="form-control form-control-lg otp-input" 
                               maxlength="6" pattern="[0-9]{6}" placeholder="000000" 
                               required autofocus>
                    </div>
                    
                    <button type="submit" class="btn btn-verificar btn-lg">
                        <i class="fas fa-check-circle me-2"></i><fmt:message key="otp.verificar" />
                    </button>
                </form>
                
                <div class="text-center mt-3">
                    <a href="${pageContext.request.contextPath}/login" class="text-decoration-none">
                        <i class="fas fa-arrow-left me-1"></i><fmt:message key="otp.reenviar" />
                    </a>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>