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
    <title><fmt:message key="error.titulo"/> - SaludBoyacá</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&family=Space+Grotesk:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">
</head>
<body class="error-wrapper">

    <div class="error-card animate-fade-in-scale">
        <div class="error-icon">
            <i class="fas fa-triangle-exclamation"></i>
        </div>
        
        <div class="error-code">!</div>
        
        <h2 class="error-title">
            <fmt:message key="error.titulo"/>
        </h2>
        
        <p class="error-message">
            <fmt:message key="error.mensaje"/>
        </p>
        
        <div class="d-flex gap-2 justify-content-center flex-wrap">
            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">
                <i class="fas fa-arrow-left"></i>
                <fmt:message key="error.volver"/>
            </a>
            <a href="${pageContext.request.contextPath}/consulta" class="btn btn-outline">
                <i class="fas fa-search"></i>
                <fmt:message key="nav.consulta"/>
            </a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>