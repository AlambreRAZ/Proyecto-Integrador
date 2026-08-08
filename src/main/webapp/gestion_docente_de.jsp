<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Docentes</title>

    <!-- Bootstrap 5 CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">

    <!-- Bootstrap Icons (CDN para garantizar que carguen todos los íconos como bi-eye, bi-pencil, etc.) -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <!-- Estilos personalizados -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/coordinador.css">

    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="gestion_usuarios" />
    <jsp:param name="active_sub" value="docente" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title mb-4">GESTIÓN DE DOCENTES</h3>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="search-box mb-0" style="max-width: 600px; flex-grow: 1; margin-right: 20px;">
            <i class="bi bi-search"></i>
            <input type="text" id="buscarDocente" placeholder="Buscar por nombre, correo, número de empleado...">
        </div>
        <a href="${pageContext.request.contextPath}/agregar_docente_de.jsp" class="btn-teal">
            <i class="bi bi-person-plus"></i> Agregar Docente
        </a>
    </div>

    <style>
        .table-custom td.acciones-cell {
            overflow: visible;
            text-overflow: clip;
            max-width: none;
        }
        /* Garantiza que los botones de acción tengan puntero y dimensiones visibles */
        .action-btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 32px;
            height: 32px;
            text-decoration: none;
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
                <th>División</th>
                <th>Núm. Empleado</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody id="tablaDocentesBody">
            <tr>
                <td colspan="6" class="text-center text-muted py-4">Cargando docentes...</td>
            </tr>
            </tbody>
        </table>
    </div>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- Definición del contextPath global ANTES de importar scripts locales -->
<script>
    window.contextPath = '${pageContext.request.contextPath}';
</script>

<script src="${pageContext.request.contextPath}/assets/js/coordinador.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/GestionDocente.js?v=3"></script>
</body>
</html>