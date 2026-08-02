<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Eventos</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
</head>
<body>

<jsp:include page="sidebar.jsp">
    <jsp:param name="active" value="mi_evento" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title mb-4">MIS EVENTOS</h3>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="search-box mb-0" style="max-width: 600px; flex-grow: 1;">
            <i class="bi bi-search"></i>
            <input type="text" id="buscarMisEventos" placeholder="Buscar Evento por nombre ...">
        </div>
    </div>

    <div class="data-card p-0 mb-4" style="overflow: hidden;">
        <table class="table-custom mb-0">
            <colgroup>
                <col style="width: 35%;">
                <col style="width: 15%;">
                <col style="width: 22%;">
                <col style="width: 18%;">
                <col style="width: 10%;">
            </colgroup>
            <thead>
                <tr>
                    <th>Título</th>
                    <th>Tipo</th>
                    <th>Institución</th>
                    <th>Fecha</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody id="tablaMisEventosBody">
                <tr>
                    <td colspan="5" class="text-center text-muted py-4">Cargando eventos...</td>
                </tr>
            </tbody>
        </table>
    </div>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    window.contextPath = '<%= request.getContextPath() %>';
    window.urlVerEvento = 'ver_mas_evento_co.jsp';
    window.urlCargarArchivo = 'cargar_archivo_co.jsp';
</script>
<script src="assets/js/coordinador.js"></script>
<script src="assets/js/MisEventos.js"></script>
</body>
</html>
