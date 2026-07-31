<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Eventos</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<jsp:include page="sidebar.jsp">
    <jsp:param name="active" value="gestion_evento" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title mb-4">GESTION DE EVENTOS</h3>

    <div class="d-flex flex-wrap gap-2 mb-4" id="filtrosTipo">
        <a href="#" class="nav-pill active" data-tipo="todos">Todos</a>
        <a href="#" class="nav-pill" data-tipo="diplomado">Diplomado</a>
        <a href="#" class="nav-pill" data-tipo="conferencia">Conferencia</a>
        <a href="#" class="nav-pill" data-tipo="taller">Taller</a>
        <a href="#" class="nav-pill" data-tipo="curso">Curso</a>
        <a href="#" class="nav-pill" data-tipo="certificacion">Certificacion</a>
    </div>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="search-box mb-0" style="max-width: 600px; flex-grow: 1; margin-right: 20px;">
            <i class="bi bi-search"></i>
            <input type="text" id="buscarEvento" placeholder="Buscar Evento por nombre ...">
        </div>
        <a href="agregar_evento_co.jsp" class="btn-teal">
            <i class="bi bi-calendar-plus"></i> Agregar Evento
        </a>
    </div>

    <div class="data-card p-0 mb-4" style="overflow: hidden;">
        <table class="table-custom mb-0">
            <colgroup>
                <col style="width: 30%;">
                <col style="width: 14%;">
                <col style="width: 22%;">
                <col style="width: 20%;">
                <col style="width: 14%;">
            </colgroup>
            <thead>
            <tr>
                <th>Titulo</th>
                <th>Tipo</th>
                <th>Institución</th>
                <th>Fecha</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody id="tablaEventosBody">
            <tr>
                <td colspan="5" class="text-center text-muted py-4">Cargando eventos...</td>
            </tr>
            </tbody>
        </table>
    </div>

    <div class="pagination-container">
        <a href="#" class="page-btn"><i class="bi bi-chevron-left"></i></a>
        <a href="#" class="page-btn active">1</a>
        <a href="#" class="page-btn">2</a>
        <a href="#" class="page-btn">3</a>
        <span class="page-btn dots">...</span>
        <a href="#" class="page-btn">10</a>
        <a href="#" class="page-btn"><i class="bi bi-chevron-right"></i></a>
    </div>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>window.contextPath = '<%= request.getContextPath() %>';</script>
<script src="assets/js/coordinador.js"></script>
<script src="assets/js/GestionEvento.js"></script>
</body>
</html>