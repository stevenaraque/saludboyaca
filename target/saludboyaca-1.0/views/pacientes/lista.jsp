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
        <title><fmt:message key="paciente.titulo"/> - SaludBoyacá</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&family=Space+Grotesk:wght@400;500;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css" rel="stylesheet">
        <%-- AGREGADO: CSS responsive DataTables --%>
        <link href="https://cdn.datatables.net/responsive/2.5.0/css/responsive.bootstrap5.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">
    </head>
    <body class="bg-body">

        <jsp:include page="../templates/header.jsp">
            <jsp:param name="menu" value="pacientes"/>
        </jsp:include>

        <div class="container py-4">

            <!-- HEADER -->
            <div class="page-header mb-4 animate-fade-in-up">
                <div class="row align-items-center g-3">
                    <div class="col-12 col-lg-8">
                        <h2 class="font-title mb-1">
                            <i class="fas fa-users me-2"></i>
                            <fmt:message key="paciente.titulo"/>
                        </h2>
                        <p class="mb-0 opacity-75">
                            <i class="fas fa-list-ul me-1"></i>
                            <fmt:message key="paciente.subtitulo.lista"/>
                        </p>
                    </div>
                    <div class="col-12 col-lg-4 text-lg-end">
                        <a href="${pageContext.request.contextPath}/pacientes?accion=nuevo" class="btn btn-success">
                            <i class="fas fa-plus"></i>
                            <span class="d-none d-md-inline ms-1"><fmt:message key="paciente.nuevo"/></span>
                        </a>
                    </div>
                </div>
            </div>

            <!-- ALERTAS -->
            <c:if test="${not empty mensaje}">
                <div class="alert alert-success animate-fade-in-up">
                    <i class="fas fa-check-circle"></i>
                    ${mensaje}
                    <button type="button" class="btn-close ms-auto" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- TABLA -->
            <div class="glass-card animate-fade-in-up delay-1">
                <div class="glass-card-header">
                    <h5 class="mb-0">
                        <i class="fas fa-address-book"></i>
                        <fmt:message key="paciente.listado"/>
                    </h5>
                    <span class="badge badge-sm badge-estado-confirmada">
                        <i class="fas fa-users me-1"></i>
                        ${pacientes.size()} <fmt:message key="paciente.registros"/>
                    </span>
                </div>

                <div class="glass-card-body p-0">
                    <div class="table-responsive-custom">
                        <%-- CORREGIDO: sin clases d-none en th/td, sin fila colspan en tbody --%>
                        <table id="tablaPacientes" class="table-saludboyaca mb-0">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th><fmt:message key="paciente.documento"/></th>
                                    <th><fmt:message key="paciente.nombres"/></th>
                                    <th><fmt:message key="paciente.apellidos"/></th>
                                    <th><fmt:message key="paciente.eps"/></th>
                                    <th><fmt:message key="paciente.telefono"/></th>
                                    <th class="text-end"><fmt:message key="tabla.acciones"/></th>
                                </tr>
                            </thead>
                            <tbody>
                                <%-- Solo filas de datos; estado vacío lo maneja DataTables con emptyTable --%>
                                <c:forEach items="${pacientes}" var="p" varStatus="status">
                                    <tr>
                                        <td><span class="text-muted">#${p.id}</span></td>
                                        <td><span class="fw-bold">${p.documento}</span></td>
                                        <td>
                                            <div class="d-flex align-items-center gap-2">
                                                <div class="user-avatar">${p.nombres.charAt(0)}</div>
                                                <span>${p.nombres}</span>
                                            </div>
                                        </td>
                                        <td>${p.apellidos}</td>
                                        <td>
                                            <span class="badge badge-sm badge-estado-confirmada">${p.eps}</span>
                                        </td>
                                        <td>
                                            <span class="text-muted">
                                                <i class="fas fa-phone me-1"></i>
                                                ${p.telefono != null ? p.telefono : '—'}
                                            </span>
                                        </td>
                                        <td class="text-end">
                                            <div class="d-flex gap-1 justify-content-end">
                                                <a href="${pageContext.request.contextPath}/pacientes?accion=editar&id=${p.id}"
                                                   class="btn-action btn-action-edit"
                                                   title="<fmt:message key='btn.editar'/>">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <button onclick="confirmarEliminar('${p.id}', '${p.nombreCompleto}')"
                                                        class="btn-action btn-action-delete"
                                                        title="<fmt:message key='btn.eliminar'/>">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
        <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>
        <%-- AGREGADO: JS responsive DataTables --%>
        <script src="https://cdn.datatables.net/responsive/2.5.0/js/dataTables.responsive.min.js"></script>
        <script src="https://cdn.datatables.net/responsive/2.5.0/js/responsive.bootstrap5.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script src="${pageContext.request.contextPath}/resources/js/saludboyaca-i18n.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/saludboyaca-swal.js"></script>

        <script>
            $(document).ready(function () {
                $('#tablaPacientes').DataTable({
                    language: {
                        url: '${pageContext.request.contextPath}/resources/js/datatables-lang/${sessionScope.lang == "es" ? "es-ES.json" : (sessionScope.lang == "it" ? "it-IT.json" : "en-GB.json")}',
                        emptyTable: '<div class="text-center py-4"><i class="fas fa-users-slash fa-2x mb-2 d-block text-muted"></i><fmt:message key="paciente.vacio"/></div>'
                    },
                    pageLength: 10,
                    responsive: true,
                    ordering: true,
                    searching: true,
                    info: true,
                    lengthChange: true,
                    autoWidth: false,
                    columnDefs: [
                        { orderable: false, targets: -1 },
                        { responsivePriority: 1, targets: [1, 2] },  // documento y nombres: siempre visibles
                        { responsivePriority: 2, targets: 3 },        // apellidos
                        { responsivePriority: 3, targets: 4 },        // eps
                        { responsivePriority: 4, targets: [0, 5] }    // # y teléfono: se ocultan primero
                    ],
                    dom: '<"row g-3"<"col-sm-6"l><"col-sm-6"f>>rt<"row g-3"<"col-sm-6"i><"col-sm-6"p>>'
                });
            });

            function confirmarEliminar(id, nombre) {
                SwalConfirmarEliminar(nombre, function () {
                    window.location.href = '${pageContext.request.contextPath}/pacientes?accion=eliminar&id=' + id;
                });
            }
        </script>
    </body>
</html>
