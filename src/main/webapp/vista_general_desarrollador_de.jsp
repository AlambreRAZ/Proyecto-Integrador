<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vista General – Desarrollador</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
</head>
<body>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="eventos" />
</jsp:include>

<main class="main-content">
    
    <div class="row mb-5 gx-4 mt-3">
        <div class="col-md-6 col-lg-5 mb-3">
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="bi bi-calendar-check"></i>
                </div>
                <div>
                    <div class="text-muted small">Eventos registrados</div>
                    <div class="fs-4 fw-bold lh-1 mt-1">16</div>
                    <div class="text-muted" style="font-size: 0.75rem;">Total eventos</div>
                </div>
            </div>
        </div>
        <div class="col-md-6 col-lg-5 mb-3">
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="bi bi-people"></i>
                </div>
                <div class="flex-grow-1">
                    <div class="text-muted small">Docentes registrados</div>
                    <div class="fs-4 fw-bold lh-1 mt-1">50</div>
                    <div class="text-muted" style="font-size: 0.75rem;">Total docentes</div>
                </div>
                <a href="gestion_docente_de.jsp" class="btn-teal" style="padding: 6px 12px; font-size: 0.8rem;">Ver detalles</a>
            </div>
        </div>
    </div>

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 class="fw-bold mb-0 text-uppercase" style="letter-spacing: 0.05em; color: #333;">PRÓXIMOS EVENTOS</h5>
        <a href="historial_eventos_de.jsp" class="btn-teal">Ver todos los eventos</a>
    </div>

    <div id="eventsList">
        <div class="event-card">
            <div>
                <div class="fw-bold fs-5 mb-1">Taller de innovacion</div>
                <div class="text-muted small">Junio 5 - agosto 20</div>
            </div>
            <a href="ver_mas_evento_de.jsp" class="btn-teal">Ver detalles</a>
        </div>
        <div class="event-card">
            <div>
                <div class="fw-bold fs-5 mb-1">Taller de innovacion</div>
                <div class="text-muted small">Junio 5 - agosto 20</div>
            </div>
            <a href="ver_mas_evento_de.jsp" class="btn-teal">Ver detalles</a>
        </div>
    </div>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/coordinador.js"></script>
</body>
</html>
