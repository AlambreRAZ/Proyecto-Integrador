<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cargar Archivo – Desarrollador</title>
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
    <div class="mb-4">
        <h3 class="page-title mb-3">INTRODUCCION A REDES</h3>
    </div>

    <!-- Event Info Card -->
    <div class="info-card-outline mb-4" style="border-color: var(--teal-main); padding: 25px;">
        <div class="row mb-3">
            <div class="col-md-4">
                <div class="text-muted mb-1" style="font-size: 0.9rem;">Tipo de evento:</div>
                <div class="fs-6 text-dark">Diplomado</div>
            </div>
            <div class="col-md-4">
                <div class="text-muted mb-1" style="font-size: 0.9rem;">Lugar:</div>
                <div class="fs-6 text-dark">CDMX</div>
            </div>
            <div class="col-md-4">
                <div class="text-muted mb-1" style="font-size: 0.9rem;">Institución / Empresa:</div>
                <div class="fs-6 text-dark">Academia de formacion profesional</div>
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-12">
                <div class="text-muted mb-1" style="font-size: 0.9rem;">Descripción del evento:</div>
                <div class="fs-6 text-dark">Gran evento de introducion a redes para los futuros rederos</div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-4">
                <div class="text-muted mb-1" style="font-size: 0.9rem;">Fecha de inicio:</div>
                <div class="fs-6 text-dark">05/05/26</div>
            </div>
            <div class="col-md-4">
                <div class="text-muted mb-1" style="font-size: 0.9rem;">Fecha de fin:</div>
                <div class="fs-6 text-dark">22/05/26</div>
            </div>
            <div class="col-md-4">
                <div class="text-muted mb-1" style="font-size: 0.9rem;">Modalidad</div>
                <div class="fs-6 text-dark">Presencial</div>
            </div>
        </div>
    </div>

    <!-- Upload Card -->
    <div class="data-card mb-5" style="padding: 25px;">
        <h4 class="fw-bold mb-4" style="color: var(--teal-main);">Cargar archivo</h4>
        
        <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap">
            <div class="d-flex align-items-center gap-3">
                <span class="fw-medium">¿Tiene vigencia?:</span>
                <div class="form-check mb-0">
                    <input class="form-check-input" type="radio" name="vigencia" id="vigenciaNo" value="no" checked style="accent-color: black;">
                    <label class="form-check-label" for="vigenciaNo">No</label>
                </div>
                <div class="form-check mb-0">
                    <input class="form-check-input" type="radio" name="vigencia" id="vigenciaSi" value="si">
                    <label class="form-check-label" for="vigenciaSi">Si</label>
                </div>
            </div>
            
            <div class="d-flex align-items-center gap-3">
                <span class="fw-medium">Fecha de Vigencia:</span>
                <input type="date" class="form-control" style="width: auto; border-radius: 8px;">
            </div>
        </div>

        <!-- Drag & Drop Zone -->
        <div class="upload-zone text-center p-5 mt-4" style="border: 2px dashed #444; border-radius: 12px; background-color: transparent;">
            <div class="mb-3">
                <div style="display: inline-flex; justify-content: center; align-items: center; width: 60px; height: 40px; background-color: var(--teal-main); border-radius: 30px 30px 10px 10px; color: white;">
                    <i class="bi bi-arrow-up-short fs-2"></i>
                </div>
            </div>
            <button class="btn-teal px-4 py-2 mb-3" style="border-radius: 20px;">Explorar</button>
            <div class="text-muted small">Selecciona el Archivo a subir</div>
        </div>
    </div>

    <div class="d-flex justify-content-center justify-content-md-end gap-3 mb-5">
        <a href="ver_mas_evento_de.jsp" class="btn btn-outline-teal px-5 py-2 fw-semibold d-flex align-items-center" style="border: 2px solid var(--teal-main); color: var(--teal-main); border-radius: 6px;">
            <i class="bi bi-chevron-left me-2"></i> Volver
        </a>
        <button type="submit" class="btn-teal px-5 py-2" style="border-radius: 6px;">Cargar Archivo</button>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/coordinador.js"></script>
</body>
</html>
