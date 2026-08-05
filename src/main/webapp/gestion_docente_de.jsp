<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Docentes</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="gestion_usuarios" />
    <jsp:param name="active_sub" value="docente" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title mb-4">GESTION DE DOCENTES/COORDINADORES</h3>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="search-box mb-0" style="max-width: 600px; flex-grow: 1; margin-right: 20px;">
            <i class="bi bi-search"></i>
            <input type="text" id="buscarUsuario" placeholder="Buscar Docente por nombre, correo ...">
        </div>
        <a href="agregar_docente_de.jsp" class="btn-teal">
            <i class="bi bi-person-plus"></i> Agregar Docente/Coordinador
        </a>
    </div>

    <div class="data-card p-0 mb-4" style="overflow: hidden;">
        <table class="table-custom mb-0 text-center">
            <colgroup>
                <col style="width: 22%">
                <col style="width: 22%">
                <col style="width: 12%">
                <col style="width: 12%">
                <col style="width: 10%">
                <col style="width: 10%">
                <col style="width: 12%">
            </colgroup>
            <thead>
                <tr>
                    <th class="text-start">Nombre</th>
                    <th>Correo</th>
                    <th>Division</th>
                    <th>Num. Empleado</th>
                    <th>Rol</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody id="tablaUsuariosBody">
                <tr>
                    <td colspan="6" class="text-center text-muted py-4">Cargando docentes...</td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="pagination-container" id="paginationContainerDocente"></div>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>window.contextPath = '<%= request.getContextPath() %>';</script>
<script src="assets/js/coordinador.js"></script>
<script src="assets/js/GestionUsuarios.js"></script>
</body>
</html>
