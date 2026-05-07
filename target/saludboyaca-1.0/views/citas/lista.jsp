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
        <title><fmt:message key="cita.titulo"/> - SaludBoyacá</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&family=Space+Grotesk:wght@400;500;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css" rel="stylesheet">
        <link href="https://cdn.datatables.net/responsive/2.5.0/css/responsive.bootstrap5.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/saludboyaca.css" rel="stylesheet">
    </head>
    <body class="bg-body">

        <jsp:include page="../templates/header.jsp">
            <jsp:param name="menu" value="citas"/>
        </jsp:include>

        <div class="container py-4">

            <!-- HEADER -->
            <div class="page-header mb-4 animate-fade-in-up">
                <div class="row align-items-center g-3">
                    <div class="col-12 col-lg-8">
                        <h2 class="font-title mb-1">
                            <i class="fas fa-calendar-check me-2"></i>
                            <fmt:message key="cita.titulo"/>
                        </h2>
                        <p class="mb-0 opacity-75">
                            <i class="fas fa-list-ul me-1"></i>
                            <fmt:message key="cita.subtitulo.lista"/>
                        </p>
                    </div>
                    <div class="col-12 col-lg-4 text-lg-end">
                        <a href="${pageContext.request.contextPath}/citas?accion=nuevo" class="btn btn-success">
                            <i class="fas fa-plus"></i>
                            <span class="d-none d-md-inline ms-1"><fmt:message key="cita.nueva"/></span>
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
                        <i class="fas fa-list-check"></i>
                        <fmt:message key="cita.listado"/>
                    </h5>
                    <span class="badge badge-sm badge-estado-confirmada">
                        <i class="fas fa-calendar-check me-1"></i>
                        ${citas.size()} <fmt:message key="cita.registros"/>
                    </span>
                </div>

                <div class="glass-card-body p-0">
                    <div class="table-responsive-custom">
                        <table id="tablaCitas" class="table-saludboyaca mb-0">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th><fmt:message key="cita.fecha"/></th>
                                    <th><fmt:message key="cita.hora"/></th>
                                    <th><fmt:message key="cita.paciente"/></th>
                                    <th><fmt:message key="cita.medico"/></th>
                                    <th><fmt:message key="cita.especialidad"/></th>
                                    <th><fmt:message key="cita.estado"/></th>
                                    <th class="text-end"><fmt:message key="tabla.acciones"/></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${citas}" var="c" varStatus="status">
                                    <tr>
                                        <td><span class="text-muted">#${c.id}</span></td>
                                        <td>
                                            <div class="d-flex align-items-center gap-2">
                                                <i class="fas fa-calendar-day text-muted"></i>
                                                <span class="fw-bold">${c.fechaCita}</span>
                                            </div>
                                        </td>
                                        <td>
                                            <span class="text-muted">
                                                <i class="fas fa-clock me-1"></i>
                                                ${c.horaCita}
                                            </span>
                                        </td>
                                        <td>
                                            <div class="d-flex align-items-center gap-2">
                                                <div class="user-avatar">${c.nombrePaciente.charAt(0)}</div>
                                                <span>${c.nombrePaciente}</span>
                                            </div>
                                        </td>
                                        <td>
                                            <span class="text-muted">
                                                <i class="fas fa-user-doctor me-1"></i>
                                                ${c.nombreMedico}
                                            </span>
                                        </td>
                                        <td>
                                            <span class="badge badge-sm badge-estado-confirmada">${c.nombreEspecialidad}</span>
                                        </td>
                                        <td>
                                            <span class="badge badge-sm badge-estado-${c.estado.toLowerCase()}">
                                                <fmt:message key="cita.estado.${c.estado.toLowerCase()}"/>
                                            </span>
                                        </td>
                                        <td class="text-end">
                                            <div class="d-flex gap-1 justify-content-end">
                                                <a href="${pageContext.request.contextPath}/citas?accion=detalle&id=${c.id}"
                                                   class="btn-action btn-action-view"
                                                   title="<fmt:message key='btn.ver'/>">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/citas?accion=editar&id=${c.id}"
                                                   class="btn-action btn-action-edit"
                                                   title="<fmt:message key='btn.editar'/>">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/comprobante?id=${c.id}"
                                                   class="btn-action btn-action-pdf"
                                                   title="<fmt:message key='btn.pdf'/>"
                                                   target="_blank">
                                                    <i class="fas fa-file-pdf"></i>
                                                </a>
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
        <script src="https://cdn.datatables.net/responsive/2.5.0/js/dataTables.responsive.min.js"></script>
        <script src="https://cdn.datatables.net/responsive/2.5.0/js/responsive.bootstrap5.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script src="${pageContext.request.contextPath}/resources/js/saludboyaca-i18n.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/saludboyaca-swal.js"></script>

        <script>
            $(document).ready(function () {
                $('#tablaCitas').DataTable({
                    language: {
                        url: '${pageContext.request.contextPath}/resources/js/datatables-lang/${sessionScope.lang == "es" ? "es-ES.json" : (sessionScope.lang == "it" ? "it-IT.json" : "en-GB.json")}',
                        emptyTable: '<div class="text-center py-4"><i class="fas fa-calendar-xmark fa-2x mb-2 d-block text-muted"></i><fmt:message key="cita.vacio"/></div>'
                    },
                    pageLength: 10,
                    responsive: true,
                    order: [[1, 'desc'], [2, 'desc']],
                    autoWidth: false,
                    columnDefs: [
                        { orderable: false, targets: -1 },
                        { responsivePriority: 1, targets: [1, 3] },
                        { responsivePriority: 2, targets: 6 },
                        { responsivePriority: 3, targets: [2, 4] },
                        { responsivePriority: 4, targets: [0, 5] }
                    ],
                    dom: '<"row g-3"<"col-sm-6"l><"col-sm-6"f>>rt<"row g-3"<"col-sm-6"i><"col-sm-6"p>>'
                });
            });
        </script>
    </body>
</html>
