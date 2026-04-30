<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
    <head>
        <meta charset="UTF-8">
        <title><fmt:message key="cita.titulo" /> - SaludBoyaca</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css" rel="stylesheet">

        <style>
            .navbar-saludboyaca {
                background: #1A5276 !important;
            }
            .btn-nuevo {
                background: #39A900;
                color: white;
            }
            .btn-nuevo:hover {
                background: #2d8000;
                color: white;
            }

            .badge-programada {
                background: #F39C12;
                color: white;
            }
            .badge-confirmada {
                background: #27AE60;
                color: white;
            }
            .badge-atendida {
                background: #2980B9;
                color: white;
            }
            .badge-cancelada {
                background: #E74C3C;
                color: white;
            }
        </style>
    </head>
    <body>
        <jsp:include page="../templates/header.jsp">
            <jsp:param name="menu" value="citas" />
        </jsp:include>

        <div class="container mt-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h3><fmt:message key="cita.titulo" /></h3>
                <a href="${pageContext.request.contextPath}/citas?accion=nuevo" class="btn btn-nuevo">
                    <i class="fas fa-plus me-2"></i><fmt:message key="cita.nueva" />
                </a>
            </div>

            <div class="card shadow">
                <div class="card-body">
                    <table id="tablaCitas" class="table table-striped table-hover" style="width:100%">
                        <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th><fmt:message key="cita.fecha" /></th>
                                <th><fmt:message key="cita.hora" /></th>
                                <th><fmt:message key="cita.paciente" /></th>
                                <th><fmt:message key="cita.medico" /></th>
                                <th><fmt:message key="cita.especialidad" /></th>
                                <th><fmt:message key="cita.estado" /></th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${citas}" var="c">
                                <tr>
                                    <td>${c.id}</td>
                                    <td>${c.fechaCita}</td>
                                    <td>${c.horaCita}</td>
                                    <td>${c.nombrePaciente}</td>
                                    <td>${c.nombreMedico}</td>
                                    <td>${c.nombreEspecialidad}</td>
                                    <td>
                                        <span class="badge badge-${c.estado.toLowerCase()}">
                                            <fmt:message key="cita.estado.${c.estado.toLowerCase()}" />
                                        </span>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/citas?accion=detalle&id=${c.id}" 
                                           class="btn btn-sm btn-info" title="Ver detalle">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/citas?accion=editar&id=${c.id}" 
                                           class="btn btn-sm btn-primary" title="Editar">
                                            <i class="fas fa-edit"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/comprobante?id=${c.id}" 
                                           class="btn btn-sm btn-success" title="Descargar comprobante" target="_blank">
                                            <i class="fas fa-file-pdf"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
        <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

        <script>
            $(document).ready(function () {
                $('#tablaCitas').DataTable({
                    language: {
                        url: '${sessionScope.lang == 'es' ? '//cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' : 
                    (sessionScope.lang == 'it' ? '//cdn.datatables.net/plug-ins/1.13.6/i18n/it-IT.json' : 
                    '//cdn.datatables.net/plug-ins/1.13.6/i18n/en-GB.json')}'
                    },
                    pageLength: 10,
                    order: [[1, 'desc'], [2, 'desc']]
                });
            });
        </script>
    </body>
</html>