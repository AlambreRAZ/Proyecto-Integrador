<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de Eventos</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
    <!-- SweetAlert2 para mensajes de error si aplica -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="eventos" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title mb-4">HISTORIAL DE EVENTOS</h3>

    <!-- Filtros por tipo de evento -->
    <div class="d-flex flex-wrap gap-2 mb-4" id="contenedorFiltrosTipo">
        <a href="#" class="nav-pill active" data-tipo="todos">Todos</a>
        <a href="#" class="nav-pill" data-tipo="Diplomado">Diplomado</a>
        <a href="#" class="nav-pill" data-tipo="Conferencia">Conferencia</a>
        <a href="#" class="nav-pill" data-tipo="Taller">Taller</a>
        <a href="#" class="nav-pill" data-tipo="Curso">Curso</a>
        <a href="#" class="nav-pill" data-tipo="Certificación">Certificación</a>
    </div>

    <!-- Buscador en tiempo real -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="search-box mb-0" style="max-width: 600px; flex-grow: 1;">
            <i class="bi bi-search"></i>
            <input type="text" id="buscarEvento" placeholder="Buscar Evento por nombre...">
        </div>
    </div>

    <!-- Tabla dinámicamente renderizada -->
    <div class="data-card p-0 mb-4" style="overflow: hidden;">
        <table class="table-custom mb-0 text-center">
            <colgroup>
                <col style="width: 30%;">
                <col style="width: 15%;">
                <col style="width: 25%;">
                <col style="width: 20%;">
                <col style="width: 10%;">
            </colgroup>
            <thead>
            <tr>
                <th class="text-start">Título</th>
                <th>Tipo</th>
                <th>Institución</th>
                <th>Fecha</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody id="tablaEventosBody">
            <!-- Se llena mediante JavaScript desde el Servlet /ListarMisEventos -->
            </tbody>
        </table>
    </div>

    <!-- Paginador (Preparado para lógica si se requiere) -->
    <div class="pagination-container" id="paginacionContainer">
        <a href="#" class="page-btn"><i class="bi bi-chevron-left"></i></a>
        <a href="#" class="page-btn active">1</a>
        <a href="#" class="page-btn"><i class="bi bi-chevron-right"></i></a>
    </div>

</main>

<script>
    // ContextPath global para construir URLs dinámicamente
    window.contextPath = '<%= request.getContextPath() %>';
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/historial_eventos_de.js"></script>
</body>
</html>