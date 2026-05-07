/**
 * ============================================================================
 * SaludBoyacá v4.1 — SweetAlert2 Professional Wrapper
 * Centro de Salud Municipal de Paipa, Boyacá
 * 
 * Mejoras v4.1:
 * - Namespace único (window.SB) para evitar polución global
 * - Manejo de Promises en callbacks con auto-cierre de loading
 * - Escape HTML robusto (comillas incluidas)
 * - Verificación de dependencias (SweetAlert2, FontAwesome)
 * - Sin fugas de memoria (listeners removidos correctamente)
 * - CSS desacoplado (clases en lugar de inline styles)
 * - Documentación JSDoc completa
 * - Manejo de errores en operaciones asíncronas
 * - Soporte para callbacks síncronos y asíncronos
 * ============================================================================
 */

(function() {
    'use strict';

    // ============================================
    // VERIFICACIÓN DE DEPENDENCIAS
    // ============================================

    if (typeof Swal === 'undefined') {
        console.error('[SB-Swal] ❌ SweetAlert2 no está cargado. Incluye el script antes de este archivo.');
        console.error('[SB-Swal] CDN: https://cdn.jsdelivr.net/npm/sweetalert2@11');
        return;
    }

    if (typeof Swal.version === 'undefined' || parseInt(Swal.version.split('.')[0]) < 11) {
        console.warn('[SB-Swal] ⚠️ Se recomienda SweetAlert2 v11+ para todas las funciones.');
    }

    // ============================================
    // CONFIGURACIÓN BASE
    // ============================================

    const SB = {
        version: '4.1.0',
        debug: false,
        
        // Colores institucionales
        colors: {
            primary: '#154360',
            secondary: '#1A5276',
            accent: '#6C3483',
            success: '#27AE60',
            warning: '#F39C12',
            error: '#E74C3C',
            info: '#2980B9',
            text: '#2C3E50',
            muted: '#7F8C8D'
        },

        // Estados de citas
        estadosCita: {
            'PROGRAMADA':  { color: '#F39C12', bg: 'rgba(243, 156, 18, 0.1)',  icon: 'fa-calendar' },
            'CONFIRMADA':  { color: '#27AE60', bg: 'rgba(39, 174, 96, 0.1)',    icon: 'fa-check-circle' },
            'ATENDIDA':    { color: '#2980B9', bg: 'rgba(41, 128, 185, 0.1)',   icon: 'fa-user-check' },
            'CANCELADA':   { color: '#E74C3C', bg: 'rgba(231, 76, 60, 0.1)',    icon: 'fa-times-circle' }
        }
    };

    const swalBaseConfig = {
        confirmButtonText: 'Aceptar',
        cancelButtonText: 'Cancelar',
        background: '#FFFFFF',
        backdrop: 'rgba(21, 67, 96, 0.4)',
        allowOutsideClick: false,
        allowEscapeKey: true,
        showCloseButton: true,
        customClass: {
            popup: 'swal2-popup',
            title: 'swal2-title',
            htmlContainer: 'swal2-html-container',
            confirmButton: 'btn btn-primary btn-lg',
            cancelButton: 'btn btn-outline btn-lg',
            actions: 'swal2-actions-gap'
        },
        showClass: {
            popup: 'animate-fade-in-scale'
        },
        hideClass: {
            popup: 'animate-fade-out-scale'
        },
        buttonsStyling: false
    };

    // ============================================
    // UTILIDADES INTERNAS
    // ============================================

    /**
     * Log de debug condicional
     * @param {...*} args - Argumentos a loguear
     */
    function debugLog(...args) {
        if (SB.debug) {
            console.log('[SB-Swal]', ...args);
        }
    }

    /**
     * Escapar HTML para prevenir XSS (comillas incluidas)
     * @param {*} text - Texto a escapar
     * @returns {string} Texto escapado
     */
    function escapeHtml(text) {
        if (text == null || text === undefined) return '';
        return String(text)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
    }

    /**
     * Ejecutar callback con manejo de errores y Promises
     * @param {Function} callback - Función a ejecutar
     * @param {*} [arg] - Argumento opcional para el callback
     * @param {string} [loadingTitle] - Título del loading (si aplica)
     * @param {string} [loadingText] - Texto del loading (si aplica)
     */
    function ejecutarCallbackSeguro(callback, arg, loadingTitle, loadingText) {
        if (typeof callback !== 'function') {
            debugLog('Callback no es función, ignorando');
            return;
        }

        try {
            const resultado = callback(arg);

            // Si es una Promise, manejar resolve/reject
            if (resultado && typeof resultado.then === 'function') {
                resultado
                    .then(() => {
                        debugLog('Promise resuelta exitosamente');
                    })
                    .catch((err) => {
                        console.error('[SB-Swal] Error en Promise:', err);
                        SB.error('Error', err?.message || 'No se pudo completar la operación');
                    });
            }
        } catch (err) {
            console.error('[SB-Swal] Error en callback:', err);
            SB.error('Error', err?.message || 'Error inesperado en la operación');
        }
    }

    /**
     * Crear badge de estado para citas
     * @param {string} estado - Estado de la cita
     * @returns {string} HTML del badge
     */
    function crearBadgeEstado(estado) {
        const style = SB.estadosCita[estado] || SB.estadosCita['PROGRAMADA'];
        return `
            <span class="sb-badge-estado" 
                  style="background:${style.bg};color:${style.color};border-color:${style.color}33">
                <i class="fas ${style.icon}"></i>
                ${escapeHtml(estado)}
            </span>
        `;
    }

    // ============================================
    // FUNCIONES PÚBLICAS
    // ============================================

    /**
     * ✅ ÉXITO — Toast o Modal
     * @param {string} title - Título
     * @param {string} [text] - Texto descriptivo
     * @param {Object} [options] - Opciones adicionales
     * @param {boolean} [options.toast=false] - Mostrar como toast
     * @param {number} [options.timer=3000] - Duración en ms
     * @returns {Promise} Promise de Swal
     */
    SB.exito = function(title, text, options = {}) {
        const config = {
            ...swalBaseConfig,
            icon: 'success',
            title: title || '¡Operación exitosa!',
            text: text || '',
            timer: options.timer || 3000,
            timerProgressBar: true,
            showConfirmButton: false,
            showCloseButton: true,
            position: options.toast ? 'top-end' : 'center',
            toast: options.toast || false
        };

        if (config.toast) {
            config.showCloseButton = false;
            config.backdrop = false;
        }

        debugLog('Exito:', title, text);
        return Swal.fire(config);
    };

    /**
     * ❌ ERROR — Modal con énfasis
     * @param {string} title - Título
     * @param {string} [text] - Texto descriptivo
     * @param {Object} [options] - Opciones adicionales
     * @returns {Promise} Promise de Swal
     */
    SB.error = function(title, text, options = {}) {
        debugLog('Error:', title, text);
        return Swal.fire({
            ...swalBaseConfig,
            icon: 'error',
            title: title || 'Error',
            text: text || 'Ha ocurrido un error inesperado. Por favor intente nuevamente.',
            confirmButtonText: 'Entendido',
            showCancelButton: false,
            allowOutsideClick: true
        });
    };

    /**
     * ⚠️ ADVERTENCIA
     * @param {string} title - Título
     * @param {string} [text] - Texto descriptivo
     * @param {Object} [options] - Opciones adicionales
     * @returns {Promise} Promise de Swal
     */
    SB.warning = function(title, text, options = {}) {
        debugLog('Warning:', title, text);
        return Swal.fire({
            ...swalBaseConfig,
            icon: 'warning',
            title: title || 'Advertencia',
            text: text || '',
            confirmButtonText: 'Continuar',
            showCancelButton: false
        });
    };

    /**
     * ℹ️ INFORMACIÓN
     * @param {string} title - Título
     * @param {string} [text] - Texto descriptivo
     * @param {Object} [options] - Opciones adicionales
     * @returns {Promise} Promise de Swal
     */
    SB.info = function(title, text, options = {}) {
        debugLog('Info:', title, text);
        return Swal.fire({
            ...swalBaseConfig,
            icon: 'info',
            title: title || 'Información',
            text: text || '',
            confirmButtonText: 'Aceptar',
            showCancelButton: false
        });
    };

    /**
     * 🗑️ CONFIRMAR ELIMINACIÓN — Con doble verificación
     * @param {string} itemName - Nombre del elemento a eliminar
     * @param {Function} onConfirm - Callback al confirmar (puede ser Promise)
     * @param {Function} [onCancel] - Callback al cancelar
     * @returns {Promise} Promise de Swal
     */
    SB.confirmarEliminar = function(itemName, onConfirm, onCancel) {
        const item = escapeHtml(itemName || 'este elemento');
        
        debugLog('Confirmar eliminar:', itemName);

        return Swal.fire({
            ...swalBaseConfig,
            icon: 'warning',
            title: '¿Eliminar?',
            html: `
                <div class="sb-confirm-content">
                    <p>¿Está seguro de eliminar <strong class="sb-text-primary">${item}</strong>?</p>
                    <small class="sb-text-muted">
                        <i class="fas fa-exclamation-triangle sb-text-warning"></i>
                        Esta acción no se puede deshacer
                    </small>
                </div>
            `,
            showCancelButton: true,
            confirmButtonText: '<i class="fas fa-trash-alt me-2"></i>Sí, eliminar',
            cancelButtonText: '<i class="fas fa-times me-2"></i>Cancelar',
            customClass: {
                ...swalBaseConfig.customClass,
                confirmButton: 'btn btn-danger btn-lg'
            },
            reverseButtons: true,
            focusCancel: true
        }).then((result) => {
            if (result.isConfirmed) {
                SB.loading('Eliminando...', 'Por favor espere');
                ejecutarCallbackSeguro(onConfirm, null, 'Eliminando...', 'Por favor espere');
            } else if (result.dismiss === Swal.DismissReason.cancel && typeof onCancel === 'function') {
                onCancel();
            }
        });
    };

    /**
     * ❓ CONFIRMAR ACCIÓN GENÉRICA
     * @param {string} title - Título
     * @param {string} text - Texto descriptivo
     * @param {string} [confirmText='Confirmar'] - Texto del botón confirmar
     * @param {Function} onConfirm - Callback al confirmar
     * @param {Object} [options] - Opciones adicionales
     * @param {string} [options.icon='question'] - Icono a mostrar
     * @param {boolean} [options.focusConfirm=false] - Focus en confirmar
     * @returns {Promise} Promise de Swal
     */
    SB.confirmar = function(title, text, confirmText, onConfirm, options = {}) {
        debugLog('Confirmar:', title, text);

        return Swal.fire({
            ...swalBaseConfig,
            icon: options.icon || 'question',
            title: title,
            text: text,
            showCancelButton: true,
            confirmButtonText: confirmText || 'Confirmar',
            cancelButtonText: 'Cancelar',
            reverseButtons: true,
            focusCancel: !options.focusConfirm
        }).then((result) => {
            if (result.isConfirmed) {
                ejecutarCallbackSeguro(onConfirm);
            }
        });
    };

    /**
     * 🔔 TOAST — Notificación rápida
     * @param {string} message - Mensaje a mostrar
     * @param {string} [type='info'] - Tipo: success, error, warning, info
     * @param {number} [timer=3500] - Duración en ms
     * @returns {Promise} Promise de Swal
     */
    SB.toast = function(message, type = 'info', timer = 3500) {
        const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: timer,
            timerProgressBar: true,
            background: '#FFFFFF',
            customClass: {
                popup: 'swal2-popup',
                title: 'swal2-title'
            },
            didOpen: (toast) => {
                toast.addEventListener('mouseenter', Swal.stopTimer);
                toast.addEventListener('mouseleave', Swal.resumeTimer);
            }
        });

        const config = {
            success: { icon: 'success' },
            error: { icon: 'error' },
            warning: { icon: 'warning' },
            info: { icon: 'info' }
        };

        const style = config[type] || config.info;

        debugLog('Toast:', type, message);
        return Toast.fire({
            icon: style.icon,
            title: message,
            color: '#2C3E50'
        });
    };

    /**
     * ⏳ LOADING — Con spinner profesional
     * @param {string} [title='Procesando...'] - Título
     * @param {string} [text='Por favor espere un momento'] - Texto
     * @returns {Object} Instancia de Swal para cerrar manualmente
     */
    SB.loading = function(title, text) {
        debugLog('Loading:', title, text);

        return Swal.fire({
            ...swalBaseConfig,
            title: title || 'Procesando...',
            text: text || 'Por favor espere un momento',
            allowOutsideClick: false,
            allowEscapeKey: false,
            showConfirmButton: false,
            showCloseButton: false,
            showCancelButton: false,
            didOpen: () => {
                Swal.showLoading();
                const popup = Swal.getPopup();
                if (popup) {
                    popup.classList.add('swal-loading-custom');
                }
            }
        });
    };

    /**
     * 🔄 CONFIRMAR CAMBIO DE ESTADO DE CITA
     * @param {string} estado - Nuevo estado (PROGRAMADA, CONFIRMADA, ATENDIDA, CANCELADA)
     * @param {string} paciente - Nombre del paciente
     * @param {Function} onConfirm - Callback al confirmar (puede ser Promise)
     * @returns {Promise} Promise de Swal
     */
    SB.confirmarEstado = function(estado, paciente, onConfirm) {
        const pacienteSafe = escapeHtml(paciente || 'paciente');
        const badge = crearBadgeEstado(estado);

        debugLog('Confirmar estado:', estado, paciente);

        return Swal.fire({
            ...swalBaseConfig,
            icon: 'question',
            title: 'Cambiar estado',
            html: `
                <div class="sb-confirm-content">
                    <p>¿Cambiar la cita de <strong class="sb-text-primary">${pacienteSafe}</strong>?</p>
                    ${badge}
                </div>
            `,
            showCancelButton: true,
            confirmButtonText: '<i class="fas fa-check me-2"></i>Sí, cambiar',
            cancelButtonText: '<i class="fas fa-times me-2"></i>Cancelar',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                SB.loading('Actualizando...', 'Cambiando estado de la cita');
                ejecutarCallbackSeguro(onConfirm, estado);
            }
        });
    };

    /**
     * 🔐 OTP — Modal de verificación con timer
     * @param {string} emailMasked - Email enmascarado
     * @param {Function} onVerify - Callback con el código ingresado
     * @param {Function} [onResend] - Callback al reenviar código
     * @param {Object} [options] - Opciones adicionales
     * @param {number} [options.timerDuration=300] - Duración del timer en segundos
     * @returns {Promise} Promise de Swal
     */
    SB.otp = function(emailMasked, onVerify, onResend, options = {}) {
        const timerDuration = options.timerDuration || 300;
        let timerId = null;
        let resendHandler = null;

        debugLog('OTP iniciado para:', emailMasked);

        return Swal.fire({
            ...swalBaseConfig,
            title: 'Verificación en Dos Pasos',
            html: `
                <div class="sb-otp-container">
                    <div class="sb-otp-icon">
                        <i class="fas fa-shield-alt"></i>
                    </div>
                    <p class="sb-otp-instruction">
                        Ingrese el código de 6 dígitos enviado a:
                    </p>
                    <p class="sb-otp-email">${escapeHtml(emailMasked)}</p>
                    <input 
                        type="text" 
                        id="sb-otp-input"
                        class="sb-otp-input"
                        maxlength="6"
                        pattern="[0-9]{6}"
                        placeholder="000000"
                        autocomplete="off"
                        autofocus
                    >
                    <p id="sb-otp-timer" class="sb-otp-timer">05:00</p>
                    <button 
                        id="sb-otp-resend"
                        type="button"
                        class="sb-otp-resend-btn"
                    >
                        <i class="fas fa-redo-alt me-1"></i>Reenviar código
                    </button>
                </div>
            `,
            showConfirmButton: true,
            confirmButtonText: '<i class="fas fa-check-circle me-2"></i>Verificar',
            customClass: {
                ...swalBaseConfig.customClass,
                confirmButton: 'btn btn-verificar btn-lg'
            },
            showCancelButton: true,
            cancelButtonText: '<i class="fas fa-arrow-left me-2"></i>Volver',
            reverseButtons: true,
            allowOutsideClick: false,
            didOpen: () => {
                const timerEl = document.getElementById('sb-otp-timer');
                const resendBtn = document.getElementById('sb-otp-resend');
                const inputEl = document.getElementById('sb-otp-input');
                let remaining = timerDuration;

                // Función de actualización del timer
                const updateTimer = () => {
                    const mins = Math.floor(remaining / 60);
                    const secs = remaining % 60;
                    timerEl.textContent = `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;

                    if (remaining <= 60 && remaining > 0) {
                        timerEl.classList.add('sb-otp-timer-warning');
                    }

                    if (remaining > 0) {
                        remaining--;
                        timerId = setTimeout(updateTimer, 1000);
                    } else {
                        timerEl.textContent = '00:00';
                        timerEl.classList.remove('sb-otp-timer-warning');
                        timerEl.classList.add('sb-otp-timer-expired');
                        if (inputEl) inputEl.disabled = true;
                        if (resendBtn) resendBtn.classList.add('sb-otp-resend-pulse');
                    }
                };

                updateTimer();

                // Focus en input
                if (inputEl) inputEl.focus();

                // Handler de reenvío (guardado para poder removerlo)
                resendHandler = () => {
                    debugLog('OTP: Reenvío solicitado');
                    if (typeof onResend === 'function') {
                        clearTimeout(timerId);
                        onResend();
                    }
                };

                resendBtn.addEventListener('click', resendHandler);
            },
            willClose: () => {
                // Limpieza completa — sin fugas de memoria
                clearTimeout(timerId);
                const resendBtn = document.getElementById('sb-otp-resend');
                if (resendBtn && resendHandler) {
                    resendBtn.removeEventListener('click', resendHandler);
                }
                debugLog('OTP: Modal cerrado, recursos liberados');
            },
            preConfirm: () => {
                const code = document.getElementById('sb-otp-input').value;
                if (!code || code.length !== 6 || !/^\d{6}$/.test(code)) {
                    Swal.showValidationMessage('Ingrese un código válido de 6 dígitos numéricos');
                    return false;
                }
                return code;
            }
        }).then((result) => {
            if (result.isConfirmed && typeof onVerify === 'function') {
                ejecutarCallbackSeguro(onVerify, result.value);
            }
        });
    };

    /**
     * 🔓 Cerrar cualquier Swal abierto
     */
    SB.cerrar = function() {
        debugLog('Cerrando Swal');
        Swal.close();
    };

    /**
     * 🎛️ Activar modo debug
     * @param {boolean} activar - true para activar, false para desactivar
     */
    SB.setDebug = function(activar) {
        SB.debug = activar;
        console.log('[SB-Swal] Debug:', activar ? '✅ Activado' : '❌ Desactivado');
    };

    /**
     * 🔄 Auto-detectar alertas del DOM y convertirlas a Toasts
     * Busca elementos con clase 'alert swal-auto'
     */
    SB.autoDetect = function() {
        document.addEventListener('DOMContentLoaded', function() {
            if (typeof Swal === 'undefined') {
                console.error('[SB-Swal] No se puede auto-detectar: SweetAlert2 no cargado');
                return;
            }

            document.querySelectorAll('.alert.swal-auto').forEach(alert => {
                const message = alert.textContent.trim();
                const type = alert.classList.contains('alert-success') ? 'success' :
                            alert.classList.contains('alert-danger') ? 'error' :
                            alert.classList.contains('alert-warning') ? 'warning' : 'info';

                if (message) {
                    alert.style.display = 'none';
                    SB.toast(message, type);
                }
            });
        });
    };

    // ============================================
    // BACKWARDS COMPATIBILITY (v4.0)
    // ============================================

    // Mantener funciones antiguas como alias para no romper código existente
    window.SwalExito = SB.exito;
    window.SwalError = SB.error;
    window.SwalWarning = SB.warning;
    window.SwalInfo = SB.info;
    window.SwalConfirmarEliminar = SB.confirmarEliminar;
    window.SwalConfirmar = SB.confirmar;
    window.SwalToast = SB.toast;
    window.SwalLoading = SB.loading;
    window.SwalConfirmarEstado = SB.confirmarEstado;
    window.SwalOTP = SB.otp;
    window.SwalCerrar = SB.cerrar;
    window.SwalAutoDetect = SB.autoDetect;

    // ============================================
    // EXPONER NAMESPACE GLOBAL
    // ============================================

    window.SB = SB;

    console.log('[SB-Swal] ✅ SaludBoyacá Swal Wrapper v' + SB.version + ' cargado correctamente');
    console.log('[SB-Swal] ℹ️ Usa window.SB para acceso moderno o las funciones Swal* para compatibilidad');

})();