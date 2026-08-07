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
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        .form-control[readonly] {
            background-color: #f8f9fa;
            opacity: 1;
        }
    </style>
</head>
<body>

<script>
    window.contextPath = "${pageContext.request.contextPath}";
</script>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="gestion_eventos" />
</jsp:include>

<main class="main-content">
    <form id="formCargarArchivo" enctype="multipart/form-data">
        <div class="mb-4">
            <!-- Título dinámico del Evento -->
            <h3 class="page-title mb-3" id="eventoNombre" style="color: var(--teal-main);">CARGANDO...</h3>
        </div>

        <!-- Event Info Card Dinámica -->
        <div class="info-card-outline mb-4" style="border-color: var(--teal-main); padding: 25px;">
            <div class="row mb-3">
                <div class="col-md-4">
                    <div class="text-muted mb-1" style="font-size: 0.9rem;">Tipo de evento:</div>
                    <div class="fs-6 text-dark" id="eventoTipo">-</div>
                </div>
                <div class="col-md-4">
                    <div class="text-muted mb-1" style="font-size: 0.9rem;">Lugar:</div>
                    <div class="fs-6 text-dark" id="eventoLugar">-</div>
                </div>
                <div class="col-md-4">
                    <div class="text-muted mb-1" style="font-size: 0.9rem;">Institución / Empresa:</div>
                    <div class="fs-6 text-dark" id="eventoInstitucion">-</div>
                </div>
            </div>

            <div class="row mb-3">
                <div class="col-12">
                    <div class="text-muted mb-1" style="font-size: 0.9rem;">Descripción del evento:</div>
                    <div class="fs-6 text-dark" id="eventoDescripcion">-</div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-4">
                    <div class="text-muted mb-1" style="font-size: 0.9rem;">Fecha de inicio:</div>
                    <div class="fs-6 text-dark" id="eventoFechaInicio">-</div>
                </div>
                <div class="col-md-4">
                    <div class="text-muted mb-1" style="font-size: 0.9rem;">Fecha de fin:</div>
                    <div class="fs-6 text-dark" id="eventoFechaFin">-</div>
                </div>
                <div class="col-md-4">
                    <div class="text-muted mb-1" style="font-size: 0.9rem;">Modalidad</div>
                    <div class="fs-6 text-dark" id="eventoModalidad">Presencial</div>
                </div>
            </div>
        </div>

        <!-- Upload Card -->
        <div class="data-card mb-5" style="padding: 25px;">
            <h4 class="fw-bold mb-4" style="color: var(--teal-main);">Cargar archivo</h4>

            <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
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
                    <input type="date" id="inputFechaVigencia" name="fechaVigencia" class="form-control" style="width: auto; border-radius: 8px;" disabled>
                </div>
            </div>

            <!-- Drag & Drop Zone -->
            <div class="upload-zone text-center p-5 mt-4" style="border: 2px dashed #444; border-radius: 12px; background-color: transparent;">
                <div class="mb-3">
                    <div style="display: inline-flex; justify-content: center; align-items: center; width: 60px; height: 40px; background-color: var(--teal-main); border-radius: 30px 30px 10px 10px; color: white;">
                        <i class="bi bi-arrow-up-short fs-2"></i>
                    </div>
                </div>

                <!-- Input oculto para cargar archivos -->
                <input type="file" id="inputArchivo" name="archivo" accept=".pdf,.jpg,.jpeg,.png" style="display: none;">

                <button type="button" id="btnExplorar" class="btn-teal px-4 py-2 mb-3" style="border-radius: 20px;">Explorar</button>
                <div id="textoArchivo" class="text-muted small">Selecciona el Archivo a subir (.pdf, .jpg, .png)</div>
            </div>
        </div>

        <div class="d-flex justify-content-center justify-content-md-end gap-3 mb-5">
            <a id="btnVolver" href="gestion_eventos_de.jsp" class="btn btn-outline-teal px-5 py-2 fw-semibold d-flex align-items-center" style="border: 2px solid var(--teal-main); color: var(--teal-main); border-radius: 6px;">
                <i class="bi bi-chevron-left me-2"></i> Volver
            </a>
            <button type="submit" class="btn-teal px-5 py-2" style="border-radius: 6px;">Cargar Archivo</button>
        </div>
    </form>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/coordinador.js"></script>
<script src="assets/js/CargarArchivo.js"></script>
</body>
</html>