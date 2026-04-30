<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="paciente.titulo" /> - SaludBoyaca</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- DataTables CSS -->
    <link href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css" rel="stylesheet">
    
    <style>
        .navbar-saludboyaca { background: #1A5276 !important; }
        .btn-nuevo { background: #39A900; color: white; }
        .btn-nuevo:hover { background: #2d8000; color: white; }
        .badge-programada { background: #F39C12; }
        .badge-confirmada { background: #27AE60; }
    </style>
</head>
<body>
    <!-- Incluir header -->
    <jsp:include page="../templates/header.jsp">
        <jsp:param name="menu" value="pacientes" />
        <jsp:param name="titulo" value="${sessionScope.lang == 'es' ? 'Gestion de Pacientes' : (sessionScope.lang == 'en' ? 'Patient Management' : 'Gestione Pazienti')}" />
        <jsp:param name="icono" value="fa-users" />
    </jsp:include>

    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3><fmt:message key="paciente.titulo" /></h3>
            <a href="${pageContext.request.contextPath}/pacientes?accion=nuevo" class="btn btn-nuevo">
                <i class="fas fa-plus me-2"></i><fmt:message key="paciente.nuevo" />
            </a>
        </div>

        <c:if test="${not empty mensaje}">
            <div class="alert alert-success alert-dismissible fade show">
                ${mensaje}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="card shadow">
            <div class="card-body">
                <table id="tablaPacientes" class="table table-striped table-hover" style="width:100%">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th><fmt:message key="paciente.documento" /></th>
                            <th><fmt:message key="paciente.nombres" /></th>
                            <th><fmt:message key="paciente.apellidos" /></th>
                            <th><fmt:message key="paciente.eps" /></th>
                            <th><fmt:message key="paciente.telefono" /></th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${pacientes}" var="p">
                            <tr>
                                <td>${p.id}</td>
                                <td>${p.documento}</td>
                                <td>${p.nombres}</td>
                                <td>${p.apellidos}</td>
                                <td>${p.eps}</td>
                                <td>${p.telefono}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/pacientes?accion=editar&id=${p.id}" 
                                       class="btn btn-sm btn-primary" title="Editar">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <button onclick="confirmarEliminar('${p.id}', '${p.nombreCompleto}')" 
                                            class="btn btn-sm btn-danger" title="Eliminar">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Modal de confirmación -->
    <div class="modal fade" id="modalEliminar" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title"><i class="fas fa-exclamation-triangle me-2"></i>Confirmar</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p><fmt:message key="paciente.confirmar" /></p>
                    <p class="fw-bold" id="nombrePacienteEliminar"></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <fmt:message key="paciente.cancelar" />
                    </button>
                    <a id="btnConfirmarEliminar" href="#" class="btn btn-danger">
                        <fmt:message key="paciente.eliminar" />
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
    <!-- jQuery (requerido por DataTables) -->
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <!-- DataTables -->
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

    <script>
        $(document).ready(function() {
            $('#tablaPacientes').DataTable({
                language: {
                    url: '${sessionScope.lang == 'es' ? '//cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' : 
                          (sessionScope.lang == 'it' ? '//cdn.datatables.net/plug-ins/1.13.6/i18n/it-IT.json' : 
                          '//cdn.datatables.net/plug-ins/1.13.6/i18n/en-GB.json')}'
                },
                pageLength: 10,
                responsive: true
            });
        });

        function confirmarEliminar(id, nombre) {
            document.getElementById('nombrePacienteEliminar').textContent = nombre;
            document.getElementById('btnConfirmarEliminar').href = 
                '${pageContext.request.contextPath}/pacientes?accion=eliminar&id=' + id;
            
            var modal = new bootstrap.Modal(document.getElementById('modalEliminar'));
            modal.show();
        }
    </script>
</body>
</html>