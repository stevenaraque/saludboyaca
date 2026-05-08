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
        <title><fmt:message key="consulta.titulo"/> - SaludBoyacá</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&family=Space+Grotesk:wght@400;500;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <!-- reCAPTCHA solo se carga si NO hay resultados -->
        <c:if test="${not resultado}">
            <script src="https://www.google.com/recaptcha/api.js" async defer></script>
        </c:if>
        <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">

        <style>
            /* ── Botón PDF ── */
            .btn-pdf {
                display: inline-flex;
                align-items: center;
                gap: 6px;
                padding: 5px 12px;
                font-size: 0.8rem;
                font-weight: 600;
                color: #fff;
                background: linear-gradient(135deg, #00324D, #005580);
                border: none;
                border-radius: 8px;
                text-decoration: none;
                transition: opacity 0.2s, transform 0.15s;
                white-space: nowrap;
            }
            .btn-pdf:hover {
                opacity: 0.88;
                transform: translateY(-1px);
                color: #fff;
            }
            .btn-pdf i { font-size: 0.75rem; }

            /* ── Botón nueva consulta ── */
            .btn-nueva-consulta {
                display: inline-flex;
                align-items: center;
                gap: 8px;
                padding: 10px 22px;
                font-size: 0.9rem;
                font-weight: 600;
                color: #fff;
                background: var(--color-primario, #39A900);
                border: none;
                border-radius: 10px;
                cursor: pointer;
                transition: opacity 0.2s, transform 0.15s;
                margin-top: 18px;
            }
            .btn-nueva-consulta:hover {
                opacity: 0.88;
                transform: translateY(-1px);
            }

            /* ── Columna acciones en tabla ── */
            .table-saludboyaca th.col-acciones,
            .table-saludboyaca td.col-acciones {
                text-align: center;
                width: 130px;
            }
        </style>
    </head>
    <body>

        <!-- SELECTOR DE IDIOMA -->
        <div class="lang-bar-fixed">
            <button type="button" class="lang-btn ${sessionScope.lang == 'es' ? 'active' : ''}" data-lang="es">🇨🇴 ES</button>
            <button type="button" class="lang-btn ${sessionScope.lang == 'en' ? 'active' : ''}" data-lang="en">🇺🇸 EN</button>
            <button type="button" class="lang-btn ${sessionScope.lang == 'it' ? 'active' : ''}" data-lang="it">🇮🇹 IT</button>
        </div>

        <div class="public-page">
            <div class="public-card animate-fade-in-scale">
                <div class="public-card-header">
                    <i class="fas fa-search"></i>
                    <h4><fmt:message key="consulta.titulo"/></h4>
                    <small><fmt:message key="app.institucion"/></small>
                </div>

                <div class="public-card-body">

                    <!-- ============================================================
                         FORMULARIO: solo se muestra cuando NO hay resultados
                    ============================================================ -->
                    <c:if test="${not resultado}">

                        <p class="consulta-intro">
                            <fmt:message key="consulta.instruccion"/>
                        </p>

                        <!-- Errores -->
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger animate-fade-in-up" style="margin-bottom: 16px;">
                                <i class="fas fa-circle-exclamation"></i>
                                <fmt:message key="${error}"/>
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/consulta" method="post" id="formConsulta">
                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-id-card"></i>
                                    <fmt:message key="consulta.documento"/>
                                </label>
                                <input type="text" name="documento" class="form-control"
                                       value="${documento}" required
                                       placeholder="<fmt:message key='consulta.placeholder.documento'/>">
                            </div>

                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-shield-halved"></i>
                                    <fmt:message key="consulta.captcha"/>
                                </label>
                                <div class="captcha-container">
                                    <div class="captcha-img-wrapper">
                                        <img src="${pageContext.request.contextPath}/captcha"
                                             alt="CAPTCHA" class="captcha-img"
                                             onclick="this.src = '${pageContext.request.contextPath}/captcha?' + Date.now()"
                                             title="<fmt:message key='consulta.title.recargar'/>">
                                    </div>
                                    <div class="captcha-input-wrapper" style="flex: 1;">
                                        <input type="text" name="captcha" class="form-control captcha-input"
                                               maxlength="6" required
                                               placeholder="<fmt:message key='consulta.placeholder.captcha'/>">
                                        <div class="captcha-hint">
                                            <i class="fas fa-rotate" style="font-size: 0.7rem;"></i>
                                            <fmt:message key="consulta.click.recargar"/>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Google reCAPTCHA v2 -->
                            <div class="form-group">
                                <div class="recaptcha-wrapper">
                                    <div id="recaptcha-container" class="g-recaptcha"
                                         data-sitekey="6LdlRNMsAAAAAITPJ_SXztpmC-3qeDEZis8M4c5c"
                                         data-callback="enableSubmit"
                                         data-expired-callback="disableSubmit"></div>
                                </div>
                                <div class="recaptcha-error-msg" id="recaptcha-error" style="text-align: center; margin-top: 4px;">
                                    <i class="fas fa-circle-exclamation"></i>
                                    <fmt:message key="recaptcha.error"/>
                                </div>
                            </div>

                            <button type="submit" class="btn btn-primary btn-consultar" id="btnConsultar" disabled>
                                <i class="fas fa-magnifying-glass"></i>
                                <fmt:message key="consulta.buscar"/>
                            </button>
                        </form>

                    </c:if>
                    <!-- fin formulario -->


                    <!-- ============================================================
                         RESULTADOS: solo se muestran cuando resultado == true
                    ============================================================ -->
                    <c:if test="${resultado}">
                        <div class="resultados-section">
                            <div class="resultados-header">
                                <h5>
                                    <i class="fas fa-list-check" style="color: var(--color-primario); margin-right: 6px;"></i>
                                    <fmt:message key="consulta.resultados"/>
                                </h5>
                                <span class="doc-badge">${documento}</span>
                            </div>

                            <c:choose>
                                <c:when test="${empty citas}">
                                    <div class="empty-state">
                                        <i class="fas fa-clipboard-question"></i>
                                        <p><fmt:message key="consulta.no.encontrado"/></p>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive-custom">
                                        <div class="table-results-wrapper">
                                            <table class="table-saludboyaca">
                                                <thead>
                                                    <tr>
                                                        <th><fmt:message key="tabla.fecha"/></th>
                                                        <th><fmt:message key="tabla.hora"/></th>
                                                        <th><fmt:message key="tabla.estado"/></th>
                                                        <%-- NUEVA columna para el PDF --%>
                                                        <th class="col-acciones">
                                                            <i class="fas fa-file-pdf" style="margin-right:4px;"></i>
                                                            <fmt:message key="tabla.comprobante"/>
                                                        </th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach items="${citas}" var="c">
                                                        <tr>
                                                            <td>
                                                                <i class="fas fa-calendar-day" style="color: var(--color-primario); margin-right: 6px;"></i>
                                                                ${c.fechaCita}
                                                            </td>
                                                            <td>
                                                                <i class="fas fa-clock" style="color: var(--color-primario); margin-right: 6px;"></i>
                                                                ${c.horaCita}
                                                            </td>
                                                            <td>
                                                                <span class="badge badge-sm badge-estado-${c.estado.toLowerCase()}">
                                                                    <fmt:message key="cita.estado.${c.estado.toLowerCase()}"/>
                                                                </span>
                                                            </td>
                                                            <%-- BOTÓN PDF por cada cita --%>
                                                            <td class="col-acciones">
                                                                <fmt:message key="consulta.descargar.pdf" var="tooltipPdf"/>
                                                                <a href="${pageContext.request.contextPath}/comprobante?id=${c.id}&doc=${documento}"
                                                                   class="btn-pdf"
                                                                   title="${tooltipPdf}"
                                                                   target="_blank">
                                                                    <i class="fas fa-file-arrow-down"></i>
                                                                    PDF
                                                                </a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>

                                    <div style="margin-top: 12px; text-align: center;">
                                        <small style="color: var(--text-muted); font-size: 0.8rem;">
                                            <i class="fas fa-info-circle" style="margin-right: 4px;"></i>
                                            <fmt:message key="consulta.resultados.info"/>
                                        </small>
                                    </div>
                                </c:otherwise>
                            </c:choose>

                            <%-- BOTÓN NUEVA CONSULTA: regresa al formulario limpio --%>
                            <div style="text-align: center;">
                                <a href="${pageContext.request.contextPath}/consulta"
                                   class="btn-nueva-consulta">
                                    <i class="fas fa-rotate-left"></i>
                                    <fmt:message key="consulta.nueva"/>
                                </a>
                            </div>

                        </div>
                    </c:if>
                    <!-- fin resultados -->


                    <!-- Link a login -->
                    <div class="login-link">
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-ghost">
                            <i class="fas fa-user-shield"></i>
                            <fmt:message key="login.titulo"/>
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

        <%-- El JS de reCAPTCHA solo corre si no hay resultados --%>
        <c:if test="${not resultado}">
        <script>
            var recaptchaValidated = false;
            var recaptchaWidgetId = null;

            function enableSubmit() {
                recaptchaValidated = true;
                document.getElementById('btnConsultar').disabled = false;
                document.getElementById('recaptcha-error').style.display = 'none';
            }

            function disableSubmit() {
                recaptchaValidated = false;
                document.getElementById('btnConsultar').disabled = true;
            }

            function renderRecaptcha() {
                var container = document.getElementById('recaptcha-container');
                if (container && typeof grecaptcha !== 'undefined' && recaptchaWidgetId === null) {
                    recaptchaWidgetId = grecaptcha.render('recaptcha-container', {
                        'sitekey': '6LdlRNMsAAAAAITPJ_SXztpmC-3qeDEZis8M4c5c',
                        'callback': enableSubmit,
                        'expired-callback': disableSubmit,
                        'error-callback': disableSubmit
                    });
                }
            }

            window.onRecaptchaLoad = function () {
                renderRecaptcha();
            };

            if (typeof grecaptcha !== 'undefined') {
                renderRecaptcha();
            }

            document.getElementById('formConsulta').addEventListener('submit', function (e) {
                if (!recaptchaValidated) {
                    e.preventDefault();
                    document.getElementById('recaptcha-error').style.display = 'block';
                    return false;
                }
                var btn = document.getElementById('btnConsultar');
                btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Buscando...';
                btn.disabled = true;
                return true;
            });

            document.addEventListener('DOMContentLoaded', function () {
                var docInput = document.querySelector('input[name="documento"]');
                if (docInput && !docInput.value) {
                    docInput.focus();
                }
            });
        </script>
        </c:if>

    </body>
</html>
