<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title><fmt:message key="otp.titulo"/> - SaludBoyaca</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&family=Space+Grotesk:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">
</head>
<body>

    <!-- SELECTOR DE IDIOMA -->
    <div class="lang-bar-fixed">
        <button type="button" class="lang-btn ${sessionScope.lang == 'es' ? 'active' : ''}" data-lang="es">🇨🇴 ES</button>
        <button type="button" class="lang-btn ${sessionScope.lang == 'en' ? 'active' : ''}" data-lang="en">🇺🇸 EN</button>
        <button type="button" class="lang-btn ${sessionScope.lang == 'it' ? 'active' : ''}" data-lang="it">🇮🇹 IT</button>
    </div>

    <div class="public-page">
        <div class="public-card animate-fade-in-scale" style="max-width: 440px;">
            <div class="public-card-header otp-header">
                <i class="fas fa-shield-halved"></i>
                <h4><fmt:message key="otp.titulo"/></h4>
            </div>
            
            <div class="public-card-body">
                <!-- Errores -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger animate-fade-in-up" style="margin-bottom: 16px;">
                        <i class="fas fa-circle-exclamation"></i>
                        ${error}
                    </div>
                </c:if>
                
                <!-- Instruccion -->
                <p class="otp-instruccion">
                    <i class="fas fa-envelope-circle-check"></i>
                    <fmt:message key="otp.instruccion">
                        <fmt:param value="${emailMasked}"/>
                    </fmt:message>
                </p>
                
                <!-- Formulario -->
                <form action="${pageContext.request.contextPath}/otp" method="post" id="formOTP">
                    <div class="form-group" style="margin-bottom: 12px;">
                        <div class="otp-label">
                            <i class="fas fa-key"></i>
                            <fmt:message key="otp.campo"/>
                        </div>
                        <input type="text" name="otpCodigo" class="form-control otp-input" 
                               maxlength="6" pattern="[0-9]{6}" inputmode="numeric"
                               placeholder="000000" 
                               required autofocus autocomplete="off">
                    </div>
                    
                    <!-- Timer -->
                    <div class="otp-timer-box" id="timerBox">
                        <div class="otp-timer-label"><fmt:message key="otp.tiempo.restante"/></div>
                        <div class="otp-timer-value" id="otpTimer">05:00</div>
                    </div>
                    
                    <button type="submit" class="btn-verificar" id="btnVerificar">
                        <i class="fas fa-check-circle"></i>
                        <fmt:message key="otp.verificar"/>
                    </button>
                </form>
                
                <!-- Acciones -->
                <div class="otp-actions">
                    <button type="button" class="btn-reenviar" id="btnReenviar" onclick="reenviarOTP()">
                        <i class="fas fa-redo"></i>
                        <fmt:message key="otp.reenviar"/>
                    </button>
                    
                    <a href="${pageContext.request.contextPath}/login" class="btn-volver-login">
                        <i class="fas fa-arrow-left"></i>
                        <fmt:message key="otp.volver.login"/>
                    </a>
                </div>
            </div>
        </div>
        
        <div class="public-footer">
            <i class="fas fa-building-columns"></i>
            <fmt:message key="app.footer"/>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="${pageContext.request.contextPath}/resources/js/saludboyaca-swal.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/saludboyaca-i18n.js"></script>
    
    <script>
        // ============================================
        // TIMER CON SESSIONSTORAGE - NO SE REINICIA
        // ============================================
        
        const OTP_TIMER_KEY = 'otp_remaining_time';
        const OTP_EXPIRY_KEY = 'otp_expiry_timestamp';
        const OTP_DURATION = 300; // 5 minutos en segundos
        
        var otpRemaining = OTP_DURATION;
        var otpTimerInterval = null;
        var otpIsExpired = false;
        
        // Guardar tiempo restante antes de cambiar de idioma
        function saveOTPTimer() {
            sessionStorage.setItem(OTP_TIMER_KEY, otpRemaining);
            sessionStorage.setItem(OTP_EXPIRY_KEY, Date.now() + (otpRemaining * 1000));
        }
        
        // Restaurar tiempo restante al cargar
        function restoreOTPTimer() {
            var savedExpiry = sessionStorage.getItem(OTP_EXPIRY_KEY);
            if (savedExpiry) {
                var now = Date.now();
                var expiry = parseInt(savedExpiry);
                var diff = Math.floor((expiry - now) / 1000);
                
                if (diff > 0 && diff <= OTP_DURATION) {
                    otpRemaining = diff;
                    console.log('[OTP] Timer restaurado:', diff, 'segundos restantes');
                    return true;
                } else {
                    sessionStorage.removeItem(OTP_TIMER_KEY);
                    sessionStorage.removeItem(OTP_EXPIRY_KEY);
                    return false;
                }
            }
            return false;
        }
        
        // Limpiar storage cuando expira o se verifica
        function clearOTPTimer() {
            sessionStorage.removeItem(OTP_TIMER_KEY);
            sessionStorage.removeItem(OTP_EXPIRY_KEY);
        }
        
        function formatTime(seconds) {
            var mins = Math.floor(seconds / 60);
            var secs = seconds % 60;
            return (mins < 10 ? '0' : '') + mins + ':' + (secs < 10 ? '0' : '') + secs;
        }
        
        function updateOTPTimer() {
            var timerEl = document.getElementById('otpTimer');
            var timerBox = document.getElementById('timerBox');
            var inputOtp = document.querySelector('input[name="otpCodigo"]');
            var btnVerificar = document.getElementById('btnVerificar');
            
            if (!timerEl) return;
            
            timerEl.textContent = formatTime(otpRemaining);
            saveOTPTimer();
            
            if (otpRemaining <= 60 && otpRemaining > 0) {
                timerBox.classList.add('warning');
            }
            
            if (otpRemaining > 0) {
                otpRemaining--;
                otpTimerInterval = setTimeout(updateOTPTimer, 1000);
            } else {
                otpIsExpired = true;
                clearTimeout(otpTimerInterval);
                clearOTPTimer();
                timerBox.classList.remove('warning');
                timerBox.classList.add('expired');
                timerEl.textContent = '00:00';
                
                if (inputOtp) inputOtp.disabled = true;
                if (btnVerificar) {
                    btnVerificar.disabled = true;
                    btnVerificar.style.opacity = '0.5';
                }
                
                if (typeof SwalWarning === 'function') {
                    SwalWarning('Codigo expirado', 'El codigo ha expirado. Por favor solicite uno nuevo.');
                }
            }
        }
        
        if (!restoreOTPTimer()) {
            otpRemaining = OTP_DURATION;
        }
        updateOTPTimer();
        
        // ============================================
        // REENVIAR OTP - CON PETICION AL SERVIDOR
        // ============================================
        
        function reenviarOTP() {
            var btnReenviar = document.getElementById('btnReenviar');
            
            // Deshabilitar boton para evitar doble-click
            if (btnReenviar) {
                btnReenviar.disabled = true;
                btnReenviar.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Enviando...';
            }
            
            // Peticion AJAX al servidor
            fetch('${pageContext.request.contextPath}/otp', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'action=reenviar'
            })
            .then(function(response) {
                return response.json();
            })
            .then(function(data) {
                if (data.success) {
                    // Exito: reiniciar timer visual
                    clearOTPTimer();
                    otpRemaining = OTP_DURATION;
                    otpIsExpired = false;
                    clearTimeout(otpTimerInterval);
                    
                    var timerBox = document.getElementById('timerBox');
                    var inputOtp = document.querySelector('input[name="otpCodigo"]');
                    var btnVerificar = document.getElementById('btnVerificar');
                    
                    if (timerBox) {
                        timerBox.classList.remove('expired');
                        timerBox.classList.remove('warning');
                    }
                    if (inputOtp) {
                        inputOtp.disabled = false;
                        inputOtp.value = '';
                        inputOtp.focus();
                    }
                    if (btnVerificar) {
                        btnVerificar.disabled = false;
                        btnVerificar.style.opacity = '1';
                    }
                    
                    updateOTPTimer();
                    
                    if (typeof SwalExito === 'function') {
                        SwalExito('Codigo reenviado', 'Se ha enviado un nuevo codigo a su correo.', {toast: true});
                    }
                } else {
                    // Error del servidor
                    if (typeof SwalWarning === 'function') {
                        SwalWarning('Error', data.message || 'No se pudo reenviar el codigo');
                    }
                }
            })
            .catch(function(error) {
                console.error('Error:', error);
                if (typeof SwalWarning === 'function') {
                    SwalWarning('Error', 'Error de conexion al reenviar codigo');
                }
            })
            .finally(function() {
                // Rehabilitar boton despues de 30 segundos (cooldown)
                setTimeout(function() {
                    if (btnReenviar) {
                        btnReenviar.disabled = false;
                        btnReenviar.innerHTML = '<i class="fas fa-redo"></i> <fmt:message key="otp.reenviar"/>';
                    }
                }, 30000);
            });
        }
        
        // ============================================
        // FORM SUBMIT
        // ============================================
        
        document.getElementById('formOTP').addEventListener('submit', function(e) {
            if (otpIsExpired) {
                e.preventDefault();
                if (typeof SwalWarning === 'function') {
                    SwalWarning('Codigo expirado', 'El codigo ha expirado. Solicite uno nuevo.');
                }
                return false;
            }
            
            clearOTPTimer();
            
            var btn = document.getElementById('btnVerificar');
            btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Verificando...';
            btn.disabled = true;
        });
        
        // ============================================
        // SOLO NUMEROS
        // ============================================
        
        document.querySelector('input[name="otpCodigo"]').addEventListener('input', function(e) {
            this.value = this.value.replace(/[^0-9]/g, '');
        });
        
        // Auto-focus
        document.addEventListener('DOMContentLoaded', function() {
            var otpInput = document.querySelector('input[name="otpCodigo"]');
            if (otpInput) otpInput.focus();
        });
    </script>
</body>
</html>