<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ver Más Evento</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
    <style>
        .form-control[readonly] {
            background-color: #f8f9fa;
            opacity: 1;
        }
    </style>
</head>
<body>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="gestion_eventos" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title mb-4">TALLER DE INNOVACION</h3>

    <div class="row mb-3">
        <div class="col-md-4">
            <label class="form-label text-muted">Nombre del evento:</label>
            <input type="text" class="form-control" value="Taller de innovacion" readonly>
        </div>
        <div class="col-md-4">
            <label class="form-label text-muted">Lugar:</label>
            <input type="text" class="form-control" value="CDMX" readonly>
        </div>
        <div class="col-md-4">
            <label class="form-label text-muted">Institución / Empresa:</label>
            <input type="text" class="form-control" value="Academia de formacion profesional del estado" readonly>
        </div>
    </div>

    <div class="row mb-3">
        <div class="col-md-4">
            <label class="form-label text-muted">Tipo de evento:</label>
            <input type="text" class="form-control" value="Taller" readonly>
        </div>
        <div class="col-md-8">
            <label class="form-label text-muted">Descripción del evento:</label>
            <input type="text" class="form-control" value="Gran evento de introducion a redes para los futuros rederos" readonly>
        </div>
    </div>

    <div class="row mb-4">
        <div class="col-md-2">
            <label class="form-label text-muted">Fecha de inicio:</label>
            <input type="text" class="form-control" value="05/05/26" readonly>
        </div>
        <div class="col-md-2">
            <label class="form-label text-muted">Fecha de fin:</label>
            <input type="text" class="form-control" value="22/05/26" readonly>
        </div>
        <div class="col-md-2 mt-3 mt-md-0 d-flex flex-column justify-content-center">
            <div class="modalidad-label mb-1">Modalidad</div>
            <div class="fs-6 text-dark">Presencial</div>
        </div>
        <div class="col-md-3 mt-3 mt-md-0">
            <div class="info-card-outline h-100 d-flex flex-column justify-content-center align-items-center text-center">
                <div class="d-flex align-items-center mb-1">
                    <div class="info-card-icon"><i class="bi bi-clock"></i></div>
                    <span class="fw-bold text-teal" style="color: var(--teal-main);">Fecha limite de entrega</span>
                </div>
                <div class="text-dark fw-semibold">31 Jul - 23:56</div>
                <div class="badge-light-green">Faltan 18 dias</div>
            </div>
        </div>
        <div class="col-md-3 mt-3 mt-md-0">
            <div class="info-card-outline h-100 d-flex flex-column justify-content-center align-items-center text-center">
                <div class="d-flex align-items-center mb-1">
                    <div class="info-card-icon"><i class="bi bi-people"></i></div>
                    <span class="fw-bold text-teal" style="color: var(--teal-main);">Documentos entregados</span>
                </div>
                <div class="fs-5 fw-bold text-dark mt-1">5 de 10</div>
                <div class="d-flex align-items-center w-100 justify-content-center gap-2">
                    <div class="progress-bar-custom">
                        <div class="progress-bar-fill" style="width: 50%;"></div>
                    </div>
                    <span class="fw-bold text-teal" style="font-size: 0.85rem; color: var(--teal-main);">50%</span>
                </div>
            </div>
        </div>
    </div>

    <h5 class="fw-bold mb-3" style="color: var(--teal-main);">Docentes Asignados</h5>

    <div class="data-card p-0 mb-4" style="overflow: hidden;">
        <table class="table-custom mb-0 text-center">
            <thead>
                <tr>
                    <th class="text-start">Nombre</th>
                    <th>Correo</th>
                    <th>Estado</th>
                    <th>Entregado</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td class="text-start">
                        <div class="docente-name-container">
                            <div class="avatar-circle"></div>
                            <div class="docente-name">
                                Luis Gerardo<br>Barron Flores
                            </div>
                        </div>
                    </td>
                    <td>ejemplo@gmail.com</td>
                    <td><i class="bi bi-toggle-on text-success fs-4"></i></td>
                    <td>
                        <a href="archivo_subido_de.jsp" class="action-btn"><i class="bi bi-eye"></i></a>
                    </td>
                </tr>
                <tr>
                    <td class="text-start">
                        <div class="docente-name-container">
                            <div class="avatar-circle"></div>
                            <div class="docente-name">
                                Luis Gerardo<br>Barron Flores
                            </div>
                        </div>
                    </td>
                    <td>ejemplo@gmail.com</td>
                    <td><i class="bi bi-toggle-on text-success fs-4"></i></td>
                    <td>
                        <a href="archivo_subido_de.jsp" class="action-btn"><i class="bi bi-eye"></i></a>
                    </td>
                </tr>
                <tr>
                    <td class="text-start">
                        <div class="docente-name-container">
                            <div class="avatar-circle"></div>
                            <div class="docente-name">
                                Luis Gerardo<br>Barron Flores
                            </div>
                        </div>
                    </td>
                    <td>ejemplo@gmail.com</td>
                    <td><i class="bi bi-toggle-off text-danger fs-4"></i></td>
                    <td>
                        <a href="archivo_subido_de.jsp" class="action-btn"><i class="bi bi-eye"></i></a>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="d-flex justify-content-end mb-4">
        <a href="gestion_eventos_de.jsp" class="btn btn-outline-teal px-4 py-2 fw-semibold d-flex align-items-center" style="border: 2px solid var(--teal-main); color: var(--teal-main); border-radius: 6px;">
            <i class="bi bi-chevron-left me-2"></i> Volver
        </a>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/coordinador.js"></script>
</body>
</html>
