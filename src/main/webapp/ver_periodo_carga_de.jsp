<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Ver Periodo de Carga</title>
  <link rel="stylesheet" href="assets/css/bootstrap.css">
  <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
  <link rel="stylesheet" href="assets/css/coordinador.css">
  <!-- SweetAlert2 CDN por si no existe el ID -->
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<jsp:include page="sidebar_de.jsp">
  <jsp:param name="active" value="periodos_carga" />
</jsp:include>

<main class="main-content">
  <h3 class="page-title mb-4">DETALLE DEL PERIODO DE CARGA</h3>

  <div class="d-flex align-items-center mb-4" style="color: var(--teal-main);">
    <i class="bi bi-info-circle me-2 fs-5"></i>
    <h5 class="mb-0 fw-bold">DATOS DEL PERIODO</h5>
  </div>

  <form>
    <div class="row mb-5">
      <div class="col-md-4">
        <label class="form-label text-muted">División Académica :</label>
        <select class="form-select bg-light" id="selectDivision" disabled>
          <option value="" disabled selected>Cargando...</option>
          <option value="DATID">DATID</option>
          <option value="DAMI">DAMI</option>
          <option value="DACEA">DACEA</option>
          <option value="DATEFI">DATEFI</option>
          <option value="GENERAL">GENERAL</option>
        </select>
      </div>
      <div class="col-md-4">
        <label class="form-label text-muted">Fecha de inicio :</label>
        <input type="date" class="form-control bg-light" id="fechaInicio" readonly disabled>
      </div>
      <div class="col-md-4">
        <label class="form-label text-muted">Fecha fin :</label>
        <input type="date" class="form-control bg-light" id="fechaFin" readonly disabled>
      </div>
    </div>

    <div class="d-flex justify-content-end gap-3 mt-5">
      <a href="gestion_periodos_carga_de.jsp" class="btn btn-outline-teal px-5 py-2 fw-semibold d-flex align-items-center" style="border: 2px solid var(--teal-main); color: var(--teal-main); border-radius: 6px;">
        <i class="bi bi-chevron-left me-2"></i> Volver
      </a>
    </div>
  </form>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/coordinador.js"></script>

<script>
  document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);
    const idPeriodo = urlParams.get('id');

    if (!idPeriodo) {
      Swal.fire('Error', 'No se especificó un ID de periodo.', 'error')
              .then(() => window.location.href = 'gestion_periodos_carga_de.jsp');
      return;
    }

    cargarDatosPeriodo(idPeriodo);
  });

  function cargarDatosPeriodo(id) {
    fetch("ListarPeriodosServlet")
            .then(res => res.json())
            .then(data => {
              // Se busca el periodo por ID dentro del JSON
              const periodo = data.find(p => (p.idPeriodo == id || p.id == id));

              if (periodo) {
                document.getElementById("selectDivision").value = periodo.division;
                document.getElementById("fechaInicio").value = periodo.fechaInicio;
                document.getElementById("fechaFin").value = periodo.fechaFin;
              } else {
                Swal.fire('Error', 'No se encontró la información del periodo.', 'error')
                        .then(() => window.location.href = 'gestion_periodos_carga_de.jsp');
              }
            })
            .catch(err => {
              console.error("Error al cargar datos:", err);
              Swal.fire('Error', 'No se pudieron consultar los datos.', 'error');
            });
  }
</script>
</body>
</html>