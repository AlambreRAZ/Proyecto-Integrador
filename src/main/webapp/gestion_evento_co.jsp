<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Eventos</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">    <link rel="stylesheet" href="assets/css/coordinador.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<jsp:include page="sidebar_co.jsp">
    <jsp:param name="active" value="gestion_evento" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title">GESTIÓN DE EVENTOS</h3>

    <!-- Filtros por tipo de evento -->
    <div class="d-flex gap-2 mb-3 flex-wrap" id="filtrosTipo">
        <button class="btn btn-teal-filter nav-pill active" data-tipo="todos">Todos</button>
        <button class="btn btn-teal-outline-filter nav-pill" data-tipo="diplomado">Diplomado</button>
        <button class="btn btn-teal-outline-filter nav-pill" data-tipo="conferencia">Conferencia</button>
        <button class="btn btn-teal-outline-filter nav-pill" data-tipo="taller">Taller</button>
        <button class="btn btn-teal-outline-filter nav-pill" data-tipo="curso">Curso</button>
        <button class="btn btn-teal-outline-filter nav-pill" data-tipo="certificacion">Certificación</button>
    </div>

    <!-- Buscador y Botón Agregar -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="search-box mb-0" style="max-width: 500px; flex-grow: 1;">
            <i class="bi bi-search"></i>
            <input type="text" id="buscarEvento" placeholder="Buscar Evento por nombre ...">
        </div>
        <a href="agregar_evento_co.jsp" class="btn-teal px-4 py-2">
            <i class="bi bi-calendar-plus me-1"></i> Agregar Evento
        </a>
    </div>

    <!-- TARJETA DE LA TABLA (6 Columnas exactas) -->
    <colgroup>
        <col style="width: 25%;">
        <col style="width: 14%;">
        <col style="width: 20%;">
        <col style="width: 15%;">
        <col style="width: 12%;">
        <col style="width: 14%;">
    </colgroup>
    <thead>
    <tr>
        <th>Titulo</th>
        <th>Tipo</th>
        <th>Institución</th>
        <th>Fecha</th>
        <th>División</th>
        <th>Acciones</th>
    </tr>
    </thead>
    <tbody id="tablaEventosBody">
    <tr>
        <td colspan="6" class="text-center text-muted py-4">Cargando eventos...</td>
    </tr>
    </tbody>
        </table>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>window.contextPath = '<%= request.getContextPath() %>';</script>
<script src="assets/js/GestionEvento.js?v=3"></script>
</body>
</html>