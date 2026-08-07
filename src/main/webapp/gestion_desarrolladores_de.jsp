<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Desarrolladores</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
</head>
<body>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="gestion_usuarios" />
    <jsp:param name="active_sub" value="desarrollador" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title mb-4">GESTION DE DESARROLLADORES</h3>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="search-box mb-0" style="max-width: 600px; flex-grow: 1; margin-right: 20px;">
            <i class="bi bi-search"></i>
            <input type="text" id="buscarDesarrollador" placeholder="Buscar Desarrollador por nombre, correo ...">
        </div>
        <a href="agregar_desarrollador_de.jsp" class="btn-teal">
            <i class="bi bi-person-plus"></i> Agregar Desarrollador
        </a>
    </div>

    <style>
        /* La celda de Acciones tiene varios botones; no debe recortarse con "..." como el resto de la tabla. */
        .table-custom td.acciones-cell {
            overflow: visible;
            text-overflow: clip;
            max-width: none;
        }
    </style>

    <div class="data-card p-0 mb-4" style="overflow: hidden;">
        <table class="table-custom mb-0 text-center">
            <colgroup>
                <col style="width: 24%;">
                <col style="width: 22%;">
                <col style="width: 13%;">
                <col style="width: 13%;">
                <col style="width: 10%;">
                <col style="width: 18%;">
            </colgroup>
            <thead>
            <tr>
                <th class="text-start">Nombre</th>
                <th>Correo</th>
                <th>Division</th>
                <th>Num. Empleado</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody id="tablaDesarrolladoresBody">
            <tr>
                <td colspan="6" class="text-center text-muted py-4">Cargando...</td>
            </tr>
            </tbody>
        </table>
    </div>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>window.contextPath = '<%= request.getContextPath() %>';</script>
<script src="assets/js/coordinador.js"></script>
<script src="assets/js/GestionDesarrolladores.js"></script>
</body>
</html>