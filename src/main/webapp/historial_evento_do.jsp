<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de Eventos – Docente</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
</head>
<body>

<jsp:include page="sidebar_do.jsp">
    <jsp:param name="active" value="" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title mb-4">HISTORIAL EVENTOS</h3>

    <div class="d-flex flex-wrap gap-2 mb-4">
        <a href="#" class="nav-pill active">Todos</a>
        <a href="#" class="nav-pill">Diplomado</a>
        <a href="#" class="nav-pill">Conferencia</a>
        <a href="#" class="nav-pill">Taller</a>
        <a href="#" class="nav-pill">Curso</a>
        <a href="#" class="nav-pill">Certificacion</a>
    </div>

    <div class="search-box mb-4">
        <i class="bi bi-search"></i>
        <input type="text" placeholder="Buscar Evento por nombre ...">
    </div>

    <div class="data-card p-0 mb-4" style="overflow: hidden;">
        <table class="table-custom mb-0">
            <colgroup>
                <col style="width: 26%;">
                <col style="width: 13%;">
                <col style="width: 20%;">
                <col style="width: 12%;">
                <col style="width: 20%;">
                <col style="width: 9%;">
            </colgroup>
            <thead>
                <tr>
                    <th>Titulo</th>
                    <th>Tipo</th>
                    <th>Institución</th>
                    <th>Modalidad</th>
                    <th>Fecha</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>
                        <div class="fw-semibold">Fundamentos de redes</div>
                        <div class="small text-muted">cisco paket tracer</div>
                    </td>
                    <td>Certificacion</td>
                    <td>
                        <div>Centro de</div>
                        <div class="small text-muted">Capacitacion</div>
                    </td>
                    <td>Virtual</td>
                    <td>02/02/26 - 30/03/26</td>
                    <td>
                        <a href="ver_mas_evento_do.jsp" class="action-btn"><i class="bi bi-eye"></i></a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="fw-semibold">Fundamentos de redes</div>
                        <div class="small text-muted">cisco paket tracer</div>
                    </td>
                    <td>Certificacion</td>
                    <td>
                        <div>Centro de</div>
                        <div class="small text-muted">Capacitacion</div>
                    </td>
                    <td>Virtual</td>
                    <td>02/02/26 - 30/03/26</td>
                    <td>
                        <a href="ver_mas_evento_do.jsp" class="action-btn"><i class="bi bi-eye"></i></a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="fw-semibold">Fundamentos de redes</div>
                        <div class="small text-muted">cisco paket tracer</div>
                    </td>
                    <td>Certificacion</td>
                    <td>
                        <div>Centro de</div>
                        <div class="small text-muted">Capacitacion</div>
                    </td>
                    <td>Virtual</td>
                    <td>02/02/26 - 30/03/26</td>
                    <td>
                        <a href="ver_mas_evento_do.jsp" class="action-btn"><i class="bi bi-eye"></i></a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="fw-semibold">Fundamentos de redes</div>
                        <div class="small text-muted">cisco paket tracer</div>
                    </td>
                    <td>Certificacion</td>
                    <td>
                        <div>Centro de</div>
                        <div class="small text-muted">Capacitacion</div>
                    </td>
                    <td>Virtual</td>
                    <td>02/02/26 - 30/03/26</td>
                    <td>
                        <a href="ver_mas_evento_do.jsp" class="action-btn"><i class="bi bi-eye"></i></a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="fw-semibold">Fundamentos de redes</div>
                        <div class="small text-muted">cisco paket tracer</div>
                    </td>
                    <td>Certificacion</td>
                    <td>
                        <div>Centro de</div>
                        <div class="small text-muted">Capacitacion</div>
                    </td>
                    <td>Virtual</td>
                    <td>02/02/26 - 30/03/26</td>
                    <td>
                        <a href="ver_mas_evento_do.jsp" class="action-btn"><i class="bi bi-eye"></i></a>
                    </td>
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
<script src="assets/js/coordinador.js"></script>
</body>
</html>
