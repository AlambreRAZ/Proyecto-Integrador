<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Periodo de Carga</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
</head>
<body>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="periodos_carga" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title mb-4">EDITAR PERIODO DE CARGA</h3>

    <div class="d-flex align-items-center mb-4" style="color: var(--teal-main);">
        <i class="bi bi-info-circle me-2 fs-5"></i>
        <h5 class="mb-0 fw-bold">DATOS DEL PERIODO</h5>
    </div>

    <form action="gestion_periodos_carga_de.jsp">
        <div class="row mb-5">
            <div class="col-md-4">
                <label class="form-label text-muted">Division Academica <span class="text-danger">*</span> :</label>
                <select class="form-select bg-white">
                    <option value="DATID" selected>DATID</option>
                    <option value="DAMI">DAMI</option>
                    <option value="DACEA">DACEA</option>
                    <option value="DATEFI">DATEFI</option>
                    <option value="GENERAL">GENERAL</option>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label text-muted">Fecha de inicio <span class="text-danger">*</span> :</label>
                <input type="date" class="form-control bg-white" value="2026-08-08">
            </div>
            <div class="col-md-4">
                <label class="form-label text-muted">fecha fin <span class="text-danger">*</span> :</label>
                <input type="date" class="form-control bg-white" value="2026-09-09">
            </div>
        </div>

        <div class="d-flex justify-content-end gap-3 mt-5">
            <a href="gestion_periodos_carga_de.jsp" class="btn btn-outline-teal px-5 py-2 fw-semibold d-flex align-items-center" style="border: 2px solid var(--teal-main); color: var(--teal-main); border-radius: 6px;">
                <i class="bi bi-chevron-left me-2"></i> Volver
            </a>
            <button type="submit" class="btn-teal px-5 py-2" style="border-radius: 6px;">Guardar</button>
        </div>
    </form>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/coordinador.js"></script>
</body>
</html>
