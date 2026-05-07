from reportlab.lib.pagesizes import A4
from reportlab.lib import colors
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import cm
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle,
    HRFlowable, KeepTogether
)
from reportlab.lib.enums import TA_LEFT, TA_CENTER, TA_JUSTIFY
from reportlab.platypus import PageBreak
import os

# ── Colores institucionales ──────────────────────────────────────────
AZUL       = colors.HexColor('#1A5276')
AZUL_CLARO = colors.HexColor('#2E86C1')
VERDE      = colors.HexColor('#39A900')
GRIS_FONDO = colors.HexColor('#EAF0F7')
GRIS_TEXTO = colors.HexColor('#2C3E50')
GRIS_MED   = colors.HexColor('#7F8C8D')
MORADO     = colors.HexColor('#6C3483')
BLANCO     = colors.white

WIDTH, HEIGHT = A4

def header_footer(canvas, doc):
    canvas.saveState()
    # Franja superior
    canvas.setFillColor(AZUL)
    canvas.rect(0, HEIGHT - 1.2*cm, WIDTH, 1.2*cm, fill=1, stroke=0)
    canvas.setFillColor(BLANCO)
    canvas.setFont('Helvetica-Bold', 9)
    canvas.drawString(1.5*cm, HEIGHT - 0.85*cm,
                      'SaludBoyacá — Gestión de Citas Médicas')
    canvas.setFont('Helvetica', 8)
    canvas.drawRightString(WIDTH - 1.5*cm, HEIGHT - 0.85*cm,
                           'SENA · CIMM · Regional Boyacá · 2026')
    # Franja inferior
    canvas.setFillColor(AZUL)
    canvas.rect(0, 0, WIDTH, 0.9*cm, fill=1, stroke=0)
    canvas.setFillColor(BLANCO)
    canvas.setFont('Helvetica', 7.5)
    canvas.drawCentredString(WIDTH/2, 0.3*cm,
        f'Taller: i18n + Docker + OTP   |   Pág. {doc.page}')
    canvas.restoreState()

def build_styles():
    base = getSampleStyleSheet()
    s = {}

    s['titulo_doc'] = ParagraphStyle('titulo_doc',
        fontSize=20, fontName='Helvetica-Bold',
        textColor=BLANCO, alignment=TA_CENTER,
        spaceAfter=4)

    s['subtitulo_doc'] = ParagraphStyle('subtitulo_doc',
        fontSize=10, fontName='Helvetica',
        textColor=colors.HexColor('#D6EAF8'), alignment=TA_CENTER,
        spaceAfter=2)

    s['seccion'] = ParagraphStyle('seccion',
        fontSize=12, fontName='Helvetica-Bold',
        textColor=BLANCO, alignment=TA_LEFT,
        spaceBefore=6, spaceAfter=4)

    s['pregunta'] = ParagraphStyle('pregunta',
        fontSize=10, fontName='Helvetica-Bold',
        textColor=AZUL, alignment=TA_LEFT,
        spaceBefore=10, spaceAfter=4)

    s['respuesta'] = ParagraphStyle('respuesta',
        fontSize=9.5, fontName='Helvetica',
        textColor=GRIS_TEXTO, alignment=TA_JUSTIFY,
        leading=14, spaceAfter=4)

    s['normal'] = ParagraphStyle('normal',
        fontSize=9.5, fontName='Helvetica',
        textColor=GRIS_TEXTO, alignment=TA_JUSTIFY,
        leading=14, spaceAfter=3)

    s['enlace'] = ParagraphStyle('enlace',
        fontSize=9.5, fontName='Helvetica',
        textColor=AZUL_CLARO, alignment=TA_LEFT)

    s['footer_text'] = ParagraphStyle('footer_text',
        fontSize=8, fontName='Helvetica-Oblique',
        textColor=GRIS_MED, alignment=TA_CENTER)

    return s

def caja_seccion(titulo, s):
    data = [[Paragraph(titulo, s['seccion'])]]
    t = Table(data, colWidths=[16.5*cm])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,-1), AZUL),
        ('ROUNDEDCORNERS', [6]),
        ('TOPPADDING', (0,0), (-1,-1), 8),
        ('BOTTOMPADDING', (0,0), (-1,-1), 8),
        ('LEFTPADDING', (0,0), (-1,-1), 12),
    ]))
    return t

def caja_pregunta_respuesta(num, pregunta_txt, respuesta_txt, s):
    elementos = []
    p = Paragraph(f'Pregunta {num}: {pregunta_txt}', s['pregunta'])
    r = Paragraph(respuesta_txt, s['respuesta'])
    elementos.append(p)
    elementos.append(r)
    elementos.append(HRFlowable(width='100%', thickness=0.5,
                                color=colors.HexColor('#D5D8DC'), spaceAfter=4))
    return elementos

def main():
    path = 'C:/Users/USER/Desktop/saludboyaca/Reflexiones_SaludBoyaca.pdf'
    os.makedirs(os.path.dirname(path), exist_ok=True)
    doc = SimpleDocTemplate(
        path, pagesize=A4,
        topMargin=2*cm, bottomMargin=1.8*cm,
        leftMargin=2*cm, rightMargin=2*cm
    )
    s = build_styles()
    story = []

    # ── PORTADA ─────────────────────────────────────────────────────
    story.append(Spacer(1, 1.5*cm))

    portada_data = [[
        Paragraph('SaludBoyacá', s['titulo_doc']),
    ],[
        Paragraph('Gestión de Citas Médicas', s['subtitulo_doc']),
    ],[
        Paragraph('Preguntas de Reflexión — Taller i18n + Docker + OTP', s['subtitulo_doc']),
    ]]
    portada = Table(portada_data, colWidths=[16.5*cm])
    portada.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,-1), AZUL),
        ('TOPPADDING', (0,0), (-1,-1), 10),
        ('BOTTOMPADDING', (0,0), (-1,-1), 10),
        ('LEFTPADDING', (0,0), (-1,-1), 20),
        ('RIGHTPADDING', (0,0), (-1,-1), 20),
    ]))
    story.append(portada)
    story.append(Spacer(1, 0.5*cm))

    # Datos generales
    meta = [
        ['Aprendiz:', 'Steven Araque'],
        ['Programa:', 'Tecnólogo en Análisis y Desarrollo de Software — ADSO'],
        ['Centro:', 'CIMM — Centro Industrial de Mantenimiento y Manufactura'],
        ['Regional:', 'Boyacá'],
        ['Ficha:', '3171062'],
        ['Competencia:', '225501003 — Desarrollar e integrar componentes y servicios'],
    ]
    meta_table = Table(meta, colWidths=[4*cm, 12.5*cm])
    meta_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (0,-1), GRIS_FONDO),
        ('FONTNAME', (0,0), (0,-1), 'Helvetica-Bold'),
        ('FONTNAME', (1,0), (1,-1), 'Helvetica'),
        ('FONTSIZE', (0,0), (-1,-1), 9),
        ('TEXTCOLOR', (0,0), (-1,-1), GRIS_TEXTO),
        ('TOPPADDING', (0,0), (-1,-1), 5),
        ('BOTTOMPADDING', (0,0), (-1,-1), 5),
        ('LEFTPADDING', (0,0), (-1,-1), 8),
        ('GRID', (0,0), (-1,-1), 0.3, colors.HexColor('#D5D8DC')),
    ]))
    story.append(meta_table)
    story.append(Spacer(1, 0.8*cm))

    # ══════════════════════════════════════════════════════════════════
    # SECCIÓN 11.1 — Código de Referencia y Arquitectura
    # ══════════════════════════════════════════════════════════════════
    story.append(caja_seccion('11.1  Código de Referencia y Arquitectura', s))
    story.append(Spacer(1, 0.25*cm))

    preguntas_11_1 = [
        (
            1,
            'LoginServlet — similitudes y diferencias entre el sistema de vacunación y SaludBoyacá',
            'Al comparar el <b>LoginServlet</b> del sistema de vacunación con el de SaludBoyacá, '
            'aproximadamente el <b>60 % del código es idéntico o muy similar</b>: la estructura '
            'doGet/doPost, la lectura de parámetros con <i>request.getParameter()</i>, el manejo de '
            'sesión con <i>HttpSession</i>, el uso de <i>ResourceBundle</i> para textos i18n y la '
            'redirección con <i>sendRedirect()</i> o <i>forward()</i>. '
            'Lo que cambió completamente fue el bloque de autenticación: en vacunación se valida '
            'el CAPTCHA gráfico y luego se redirige directamente al dashboard; en SaludBoyacá se '
            'eliminó todo el bloque del CAPTCHA y en su lugar se genera un código OTP con '
            '<i>OTPService.generarOTP()</i>, se guarda en sesión y en la tabla <i>otp_tokens</i>, y se '
            'redirige a <i>/otp</i>. '
            'Esto demuestra el valor de estudiar código bien estructurado: no se parte de cero, se '
            'entiende el patrón y solo se modifica lo que cambia por dominio o requerimiento, '
            'reduciendo errores y tiempo de desarrollo.'
        ),
        (
            2,
            'El patrón switch/acción en PacienteServlet — por qué es repetible y qué ocurriría sin él',
            'El patrón <b>switch/acción</b> (leer el parámetro <i>accion</i> y ejecutar el método '
            'correspondiente: listar, nuevo, guardar, editar, eliminar) es repetible porque separa '
            'la decisión de control de la lógica de negocio. Cada Servlet actúa como un '
            '"mini-controlador" con una única URL y múltiples operaciones. '
            'En SaludBoyacá, <b>CitaServlet</b> y <b>PacienteServlet</b> replican exactamente este '
            'patrón: el Servlet no contiene SQL, delega en el DAO y reenvía a la vista JSP correcta. '
            'Si cada Servlet tuviera una estructura completamente diferente, el mantenimiento '
            'sería caótico: un nuevo integrante tendría que aprender el estilo de cada clase por '
            'separado, los errores serían más difíciles de rastrear y agregar una nueva acción '
            'implicaría entender lógica inconsistente. La uniformidad hace el código predecible, '
            'testeable y escalable.'
        ),
        (
            3,
            'Comparación del esquema BD de vacunación (5 tablas) vs SaludBoyacá (7 tablas)',
            'El esquema de vacunación tiene 5 tablas: <i>usuarios, pacientes, vacunas, '
            'registros_vacunacion, log_cambios</i>. De ese modelo se aprendió el uso de FK bien '
            'definidas, el campo ENUM para roles, la tabla de log de auditoría y la separación '
            'entre la entidad principal (registro) y sus catálogos (vacunas). '
            'SaludBoyacá requiere 7 tablas porque el dominio es más complejo: se agregaron '
            '<b>especialidades</b> (catálogo independiente), <b>horarios</b> (disponibilidad por '
            'médico y día) y <b>otp_tokens</b> (seguridad de autenticación). La tabla '
            '<i>log_accesos</i> reemplaza a <i>log_cambios</i> con un enfoque centrado en '
            'autenticación. El campo <i>lang_preferido</i> en usuarios no existe en vacunación y '
            'responde al requerimiento i18n multiusuario de SaludBoyacá. Cada decisión de diseño '
            'tuvo justificación funcional, no fue arbitraria.'
        ),
    ]

    for num, preg, resp in preguntas_11_1:
        for e in caja_pregunta_respuesta(num, preg, resp, s):
            story.append(e)

    # ══════════════════════════════════════════════════════════════════
    # SECCIÓN 11.2 — Internacionalización y OTP
    # ══════════════════════════════════════════════════════════════════
    story.append(Spacer(1, 0.3*cm))
    story.append(caja_seccion('11.2  Internacionalización (i18n) y OTP', s))
    story.append(Spacer(1, 0.25*cm))

    preguntas_11_2 = [
        (
            4,
            'LocaleFilter — por qué es reutilizable y cómo agregar un nuevo idioma',
            '<b>LocaleFilter</b> es reutilizable porque no contiene ninguna lógica de negocio del '
            'dominio: solo detecta el parámetro <i>?lang=XX</i> en cada petición, valida que sea '
            'un idioma permitido, lo guarda como atributo de sesión (<i>lang</i> y <i>locale</i>) y '
            'llama a <i>chain.doFilter()</i>. Al estar anotado con <i>@WebFilter("/*")</i> intercepta '
            'toda la aplicación sin que ningún Servlet ni JSP tenga que llamarlo explícitamente. '
            'Esa independencia total del dominio lo hace copiable casi sin cambios a cualquier '
            'proyecto Java EE. '
            'Para agregar portugués (<b>pt</b>) bastaría con: (1) agregar <i>"pt"</i> al array de '
            'idiomas válidos en el filtro, (2) crear el archivo '
            '<i>messages_pt.properties</i> con todas las claves traducidas, y (3) agregar el botón '
            'de la bandera brasileña en el selector de idioma de <i>header.jsp</i>. No se toca '
            'ningún Servlet ni DAO.'
        ),
        (
            5,
            'OTP reemplaza al CAPTCHA en el login — por qué no se usan juntos y dónde persiste el CAPTCHA',
            'El CAPTCHA protege contra bots automatizados que prueban credenciales en masa. '
            'El OTP cumple esa misma función (un bot no tiene acceso al correo del usuario real) '
            'pero además <b>verifica la identidad</b>: demuestra que quien inicia sesión controla '
            'la bandeja de correo registrada en la base de datos. Usarlos juntos sería redundante '
            'y degradaría la experiencia: el usuario tendría que superar dos barreras para el mismo '
            'objetivo. El OTP es superior porque protege más y, al mismo tiempo, es el segundo '
            'factor de autenticación. '
            'El CAPTCHA se conserva en el módulo de <b>consulta pública</b> '
            '(<i>/consulta-cita</i>) precisamente porque allí <b>no existe cuenta de usuario</b>: '
            'el ciudadano solo ingresa su número de documento y no tiene un correo registrado en '
            'el sistema al cual enviar un OTP. Sin cuenta no hay correo; sin correo no hay OTP '
            'posible. El CAPTCHA visual es, en ese contexto, el único mecanismo anti-bot viable.'
        ),
        (
            6,
            'SecureRandom vs Random — por qué guardar el OTP en BD es más seguro que solo en sesión',
            '<b>SecureRandom</b> usa fuentes de entropía del sistema operativo (ruido de hardware, '
            'eventos del kernel) para generar números impredecibles criptográficamente. La clase '
            '<b>Random</b> usa un algoritmo lineal congruencial que, conociendo la semilla o '
            'suficientes salidas anteriores, puede predecirse. Para un código de seguridad de 6 '
            'dígitos, la predictibilidad de <i>Random</i> es un riesgo real. '
            'Guardar el OTP solo en sesión HTTP tiene varias debilidades: si el servidor se '
            'reinicia, la sesión se pierde; si el usuario abre otra pestaña o dispositivo, las '
            'sesiones divergen; y no hay forma de auditar intentos fallidos. '
            'Guardarlo en la tabla <b>otp_tokens</b> resuelve todo: persiste ante reinicios, '
            'permite marcar el token como <i>usado</i> para evitar replay attacks (reutilización '
            'del mismo código), registra la fecha de expiración (<i>expira_en</i>) con precisión '
            'de DATETIME y permite auditoría completa. La BD es la fuente de verdad; la sesión '
            'es solo un caché temporal.'
        ),
    ]

    for num, preg, resp in preguntas_11_2:
        for e in caja_pregunta_respuesta(num, preg, resp, s):
            story.append(e)

    # ══════════════════════════════════════════════════════════════════
    # SECCIÓN 11.3 — Docker, Despliegue y Producción
    # ══════════════════════════════════════════════════════════════════
    story.append(Spacer(1, 0.3*cm))
    story.append(caja_seccion('11.3  Docker, Despliegue y Decisiones de Producción', s))
    story.append(Spacer(1, 0.25*cm))

    preguntas_11_3 = [
        (
            7,
            'Multi-Stage Build — por qué importa el tamaño de la imagen en hosting con recursos limitados',
            'Sin Multi-Stage Build, usando una sola etapa <i>maven:3.8.6-openjdk-14</i>, la imagen '
            'resultante pesaría entre <b>600 y 800 MB</b> porque incluye el JDK completo, '
            'el binario de Maven, las dependencias descargadas en caché y todos los archivos '
            'temporales de compilación. Con Multi-Stage Build, la etapa de build compila el '
            'WAR y la etapa de ejecución solo toma el WAR compilado y lo copia a una imagen '
            'limpia de <i>tomcat:9.0-jdk14-openjdk</i>, resultando en <b>~250-300 MB</b>. '
            'Ese ahorro es crítico en <b>Back4App</b> (256 MB de RAM total): una imagen '
            'pesada haría que Tomcat no pudiera arrancar por falta de memoria, de ahí que '
            'también se requiera configurar <i>JAVA_TOOL_OPTIONS=-Xms64m -Xmx200m</i>. '
            'En <b>Koyeb</b> con 0.1 vCPU, el tiempo de pull de la imagen desde Docker Hub '
            'impacta el tiempo de arranque; una imagen más pequeña se descarga más rápido y '
            'el servicio queda disponible antes.'
        ),
        (
            8,
            'Proceso de despliegue en 2 hosting — pasos, errores encontrados y cómo depurarlos',
            '<b>Render:</b> Se creó el servicio desde <i>render.com</i> seleccionando '
            '"Deploy an existing image from a registry", se ingresó la imagen '
            '<i>stevenalejandro/saludboyaca:v1</i> de Docker Hub, se configuraron las variables de '
            'entorno <i>DB_URL, DB_USER, DB_PASS, EMAIL_PASS</i> y se seleccionó el plan Free. '
            'El despliegue tardó aproximadamente 4 minutos. '
            'El primer error fue <b>Cannot connect to database</b>: la variable DB_URL tenía el '
            'host de Railway mal copiado (faltaba el puerto al final). Se corrigió revisando la '
            'pestaña Variables de Railway y copiando la cadena JDBC completa. '
            'El segundo error fue que la aplicación cargaba en blanco: Tomcat había desplegado '
            'el WAR pero el contexto raíz no resolvía porque el WAR no se llamaba ROOT.war. '
            'Se corrigió en el Dockerfile cambiando el nombre de copia a <i>ROOT.war</i>. '
            'Los logs se consultan en la pestaña "Logs" del dashboard de Render en tiempo real. '
            '<b>Koyeb:</b> Se creó el servicio con la misma imagen y se configuró el puerto 8080. '
            'El error más frecuente fue <b>Application failed to start</b> porque Koyeb asigna '
            'un puerto dinámico via la variable PORT; Tomcat arranca siempre en 8080 y eso '
            'generaba un health check fallido. Se resolvió configurando explícitamente el '
            'Container Port en 8080 dentro del panel de Koyeb. '
            'Otro error fue <b>AuthException: 535 Authentication failed</b> al enviar el OTP, '
            'causado porque EMAIL_PASS fue pegado con comillas dobles extra en el campo de '
            'variable de entorno. Se corrigió eliminando las comillas del valor en el panel. '
            'Los logs se ven en tiempo real desde la sección "Service Logs" del dashboard de Koyeb.'
        ),
        (
            9,
            'Recomendación de hosting para producción real con 50 usuarios simultáneos',
            'Evaluando los 5 hosting del taller para un entorno de producción real con '
            '<b>50 usuarios simultáneos</b> en el Centro de Salud de Paipa, se descartan '
            'primero las opciones insuficientes: <b>Render</b> queda eliminado por el sleep de '
            '15 minutos sin actividad, inaceptable en un sistema de salud donde cualquier '
            'funcionario puede necesitar acceso urgente. <b>Back4App</b> queda eliminado por '
            'el límite de 256 MB de RAM, insuficiente para Tomcat con carga real. '
            '<b>Google Cloud Run</b> escala a cero cuando no hay tráfico, lo que genera '
            'cold starts de varios segundos en la primera petición tras un periodo de inactividad; '
            'esto es aceptable para aplicaciones de alto volumen, pero en un centro de salud '
            'municipal con tráfico irregular puede generar demoras inesperadas. '
            'La primera recomendación es <b>Oracle Cloud Always Free</b>: ofrece hasta '
            '4 OCPU y 24 GB de RAM en VMs ARM Ampere A1, recursos suficientes para Tomcat '
            'con múltiples hilos concurrentes, una instancia MySQL local y monitoreo. '
            'Siempre activo, sin cold starts y sin límite de peticiones. Su única desventaja '
            'es la complejidad inicial de configuración (SSH, Docker manual, apertura de puertos), '
            'pero para un entorno permanente esa inversión se justifica. '
            'La <b>segunda opción</b> es <b>Koyeb</b>: siempre activo sin cold starts, '
            '512 MB de RAM, SSL automático y despliegue desde Docker Hub en menos de 2 minutos, '
            'sin la complejidad operativa de una VM. Para 50 usuarios con carga moderada '
            '(citas, consultas, generación de PDF) Koyeb es suficiente y más fácil de mantener.'
        ),
    ]

    for num, preg, resp in preguntas_11_3:
        for e in caja_pregunta_respuesta(num, preg, resp, s):
            story.append(e)

    # ══════════════════════════════════════════════════════════════════
    # REPOSITORIO GITHUB
    # ══════════════════════════════════════════════════════════════════
    story.append(Spacer(1, 0.4*cm))

    repo_data = [[
        Paragraph('<b>Repositorio del Proyecto en GitHub</b>', ParagraphStyle(
            'repo_t', fontSize=10, fontName='Helvetica-Bold',
            textColor=BLANCO, alignment=TA_LEFT)),
    ],[
        Paragraph(
            'El código fuente completo de SaludBoyacá está disponible públicamente en:',
            ParagraphStyle('repo_n', fontSize=9, fontName='Helvetica',
                           textColor=colors.HexColor('#D6EAF8'), alignment=TA_LEFT)),
    ],[
        Paragraph(
            'https://github.com/stevenaraque/saludboyaca.git',
            ParagraphStyle('repo_l', fontSize=10, fontName='Helvetica-Bold',
                           textColor=colors.HexColor('#AED6F1'), alignment=TA_LEFT)),
    ],[
        Paragraph(
            'Incluye: código fuente Java · JSP · script SQL · Dockerfile · README.md con '
            'instrucciones de despliegue, credenciales de prueba y capturas de pantalla de las '
            '6 pantallas principales del sistema.',
            ParagraphStyle('repo_s', fontSize=8.5, fontName='Helvetica-Oblique',
                           textColor=colors.HexColor('#D5D8DC'), alignment=TA_LEFT)),
    ]]

    repo_table = Table(repo_data, colWidths=[16.5*cm])
    repo_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,-1), AZUL),
        ('TOPPADDING', (0,0), (-1,-1), 7),
        ('BOTTOMPADDING', (0,0), (-1,-1), 7),
        ('LEFTPADDING', (0,0), (-1,-1), 14),
        ('RIGHTPADDING', (0,0), (-1,-1), 14),
        ('LINEABOVE', (0,0), (-1,0), 3, VERDE),
    ]))
    story.append(repo_table)
    story.append(Spacer(1, 0.3*cm))

    # Firma final
    story.append(HRFlowable(width='100%', thickness=0.5, color=GRIS_FONDO))
    story.append(Spacer(1, 0.15*cm))
    story.append(Paragraph(
        'SENA · Centro Industrial de Mantenimiento y Manufactura (CIMM) · Regional Boyacá · '
        'Tecnólogo ADSO · 2026',
        s['footer_text']))

    doc.build(story, onFirstPage=header_footer, onLaterPages=header_footer)
    print('PDF generado:', path)

main()