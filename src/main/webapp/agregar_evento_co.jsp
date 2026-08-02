<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agregar Evento</title>
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
    <h3 class="page-title">AGREGAR EVENTO</h3>

    <div class="d-flex align-items-center mb-4 mt-4" style="color: var(--teal-main);">
        <i class="bi bi-info-circle me-2 fs-5"></i>
        <h5 class="mb-0 fw-bold">DATOS DEL EVENTO</h5>
    </div>

    <form id="formAgregarEvento" method="POST">
        <div class="row mb-3">
            <div class="col-md-4">
                <label class="form-label">Nombre del evento <span class="text-danger">*</span></label>
                <input type="text" class="form-control" name="nombre" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Lugar <span class="text-danger">*</span></label>
                <input type="text" class="form-control" name="lugar" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Institución / Empresa</label>
                <input type="text" class="form-control" name="institucion">
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-4">
                <label class="form-label">Tipo de evento <span class="text-danger">*</span></label>
                <select class="form-select" name="tipo" required>
                    <option value="" disabled selected>Selecciona una opción</option>
                    <option value="Diplomado">Diplomado</option>
                    <option value="Conferencia">Conferencia</option>
                    <option value="Taller">Taller</option>
                    <option value="Curso">Curso</option>
                    <option value="Certificacion">Certificación</option>
                </select>
            </div>
            <div class="col-md-8">
                <label class="form-label">Descripción del evento</label>
                <input type="text" class="form-control" name="descripcion">
            </div>
        </div>

        <div class="row mb-5 align-items-end">
            <div class="col-md-3">
                <label class="form-label">Fecha de inicio <span class="text-danger">*</span></label>
                <input type="date" class="form-control" name="fechaInicio" required>
            </div>
            <div class="col-md-3">
                <label class="form-label">Fecha de fin <span class="text-danger">*</span></label>
                <input type="date" class="form-control" name="fechaFin" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Modalidad <span class="text-danger">*</span></label>
                <select class="form-select" name="modalidad" required>
                    <option value="" disabled selected>Selecciona una opción</option>
                    <option value="presencial">Presencial</option>
                    <option value="virtual">Virtual</option>
                    <option value="mixto">Mixto</option>
                </select>
            </div>
        </div>

        <div class="d-flex justify-content-end gap-3">
            <a href="gestion_evento_co.jsp" class="btn btn-outline-teal px-4 py-2 fw-semibold d-flex align-items-center" style="border: 2px solid var(--teal-main); color: var(--teal-main); border-radius: 6px;">
                <i class="bi bi-chevron-left me-2"></i> Volver
            </a>
            <button type="submit" class="btn-teal px-4 py-2" style="border-radius: 6px;" id="btnGuardar">
                <i class="bi bi-save me-2"></i> Guardar Evento
            </button>
        </div>
    </form>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>window.contextPath = '<%= request.getContextPath() %>';</script>
<script src="assets/js/coordinador.js"></script>
<script src="assets/js/agregarEvento.js">  </script>
</body>
</html>