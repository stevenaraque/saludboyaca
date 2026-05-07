// saludboyaca-i18n.js
// Cambio de idioma sin recarga de pagina - VERSION ROBUSTA

(function () {
    'use strict';

    function detectarContextPath() {
        var path = window.location.pathname;
        var parts = path.split('/').filter(function (p) {
            return p.length > 0;
        });

        if (parts.length > 0) {
            var primerSegmento = parts[0];
            if (primerSegmento.indexOf('.') === -1) {
                return '/' + primerSegmento;
            }
        }
        return '';
    }

    var CONTEXT_PATH = detectarContextPath();
    var IDIOMA_URL = CONTEXT_PATH + '/idioma';

    console.log('[i18n] Context path detectado:', CONTEXT_PATH || '(ROOT)');
    console.log('[i18n] URL del servlet:', IDIOMA_URL);

    // ============================================
    // FUNCION PRINCIPAL
    // ============================================

    window.cambiarIdioma = function (lang) {
        if (!lang || !/^(es|en|it)$/.test(lang)) {
            console.error('[i18n] Idioma no valido:', lang);
            return;
        }

        // ============================================
        // FIX: Si estamos en consulta con reCAPTCHA, recargar pagina completa
        // El reCAPTCHA de Google no soporta re-renderizado despues de innerHTML
        // ============================================
        if (window.location.pathname.includes('/consulta')) {
            console.log('[i18n] Pagina de consulta detectada. Recarga completa para reCAPTCHA.');
            window.location.href = window.location.pathname + '?lang=' + lang;
            return;
        }

        var botones = document.querySelectorAll('.lang-btn, [data-lang]');
        botones.forEach(function (btn) {
            btn.disabled = true;
        });

        fetch(IDIOMA_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: 'lang=' + encodeURIComponent(lang)
        })
                .then(function (resp) {
                    if (!resp.ok) {
                        if (resp.status === 404) {
                            throw new Error('Servlet no encontrado. Verifica que IdiomaServlet este mapeado en ' + IDIOMA_URL);
                        }
                        throw new Error('Error HTTP ' + resp.status + ': ' + resp.statusText);
                    }

                    var contentType = resp.headers.get('content-type');
                    if (!contentType || contentType.indexOf('application/json') === -1) {
                        throw new Error('Respuesta no es JSON. Content-Type: ' + contentType);
                    }

                    return resp.json();
                })
                .then(function (data) {
                    if (!data || !data.ok) {
                        var errorMsg = (data && data.error) ? data.error : 'Respuesta invalida del servidor';
                        console.error('[i18n] Error del servidor:', errorMsg);
                        throw new Error(errorMsg);
                    }

                    console.log('[i18n] Idioma cambiado en sesion a:', data.lang);

                    return fetch(window.location.href, {
                        method: 'GET',
                        headers: {
                            'X-Requested-With': 'XMLHttpRequest'
                        }
                    });
                })
                .then(function (resp) {
                    if (!resp)
                        return;
                    if (!resp.ok) {
                        throw new Error('Error al cargar pagina traducida: ' + resp.status);
                    }
                    return resp.text();
                })
                .then(function (html) {
                    if (!html)
                        return;

                    var parser = new DOMParser();
                    var nuevoDoc = parser.parseFromString(html, 'text/html');

                    if (!nuevoDoc || !nuevoDoc.body) {
                        throw new Error('No se pudo parsear el HTML recibido');
                    }

                    var bodyActual = document.body;
                    var bodyNuevo = nuevoDoc.body;
                    var scrollY = window.scrollY;

                    bodyActual.innerHTML = bodyNuevo.innerHTML;
                    document.documentElement.lang = lang;
                    window.scrollTo(0, scrollY);
                    actualizarBotonesIdioma(lang);
                    ejecutarScripts(bodyActual);

                    // ============================================
                    // FIX: RE-INICIALIZAR DROPDOWNS DE BOOTSTRAP
                    // ============================================
                    reinitBootstrapComponents();

                    console.log('[i18n] Pagina actualizada. Idioma:', lang);
                })
                .catch(function (err) {
                    console.error('[i18n] Error:', err.message);

                    if (err.message.indexOf('404') !== -1 || err.message.indexOf('no encontrado') !== -1) {
                        console.warn('[i18n] Fallback a recarga tradicional...');
                        window.location.href = window.location.pathname + '?lang=' + lang;
                    }
                })
                .finally(function () {
                    botones.forEach(function (btn) {
                        btn.disabled = false;
                    });
                });
    };

    // ============================================
    // RE-INICIALIZAR DROPDOWNS DE BOOTSTRAP
    // ============================================

    function reinitBootstrapComponents() {
        document.querySelectorAll('.dropdown-toggle').forEach(function (toggle) {
            var instancia = bootstrap.Dropdown.getInstance(toggle);
            if (instancia) {
                instancia.dispose();
            }

            var dropdown = new bootstrap.Dropdown(toggle, {
                autoClose: true
            });

            toggle.addEventListener('click', function (e) {
                e.preventDefault();
                e.stopPropagation();
                dropdown.toggle();
            });
        });

        console.log('[i18n] Dropdowns re-inicializados manualmente');
    }

    // ============================================
    // FUNCIONES AUXILIARES
    // ============================================

    function actualizarBotonesIdioma(langActivo) {
        document.querySelectorAll('[data-lang]').forEach(function (btn) {
            var btnLang = btn.getAttribute('data-lang');
            if (btnLang === langActivo) {
                btn.classList.add('active');
            } else {
                btn.classList.remove('active');
            }
        });
    }

    function ejecutarScripts(container) {
        var scripts = container.querySelectorAll('script');
        scripts.forEach(function (antiguo) {
            var nuevo = document.createElement('script');

            if (antiguo.src) {
                nuevo.src = antiguo.src;
            } else {
                nuevo.textContent = antiguo.textContent;
            }

            if (antiguo.type) {
                nuevo.type = antiguo.type;
            }

            if (antiguo.parentNode) {
                antiguo.parentNode.replaceChild(nuevo, antiguo);
            }
        });
    }

    // ============================================
    // EVENT LISTENERS
    // ============================================

    document.addEventListener('click', function (e) {
        var btn = e.target.closest('[data-lang]');
        if (!btn)
            return;

        e.preventDefault();
        var lang = btn.getAttribute('data-lang');
        if (lang) {
            cambiarIdioma(lang);
        }
    });

    console.log('[i18n] Modulo i18n inicializado. URL:', IDIOMA_URL);

})();