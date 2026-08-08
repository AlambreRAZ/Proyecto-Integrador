<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agregar Periodo de Carga</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
</head>
<body>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="periodos_carga" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title mb-4">AGREGAR PERIODO DE CARGA</h3>

    <div class="d-flex align-items-center mb-4" style="color: var(--teal-main);">
        <i class="bi bi-info-circle me-2 fs-5"></i>
        <h5 class="mb-0 fw-bold">DATOS DEL PERIODO</h5>
    </div>


    <form id="formPeriodo">
        <div class="row mb-5">
            <div class="col-md-4">
                <label for="division" class="form-label text-muted">División Académica <span class="text-danger">*</span> :</label>
                <select id="division" name="division" class="form-select bg-white" required>
                    <option value="">Selecciona una opción</option>
                    <option value="1">DATID</option>
                    <option value="2">DACEA</option>
                    <option value="3">DATEFI</option>
                    <option value="4">DAMI</option>
                    <option value="5">General</option>
                </select>
            </div>
            <div class="col-md-4">
                <label for="fechaInicio" class="form-label text-muted">Fecha de inicio <span class="text-danger">*</span> :</label>
                <input type="date" id="fechaInicio" name="fechaInicio" class="form-control bg-white" required>
            </div>
            <div class="col-md-4">
                <label for="fechaFin" class="form-label text-muted">Fecha fin <span class="text-danger">*</span> :</label>
                <input type="date" id="fechaFin" name="fechaFin" class="form-control bg-white" required>
            </div>
        </div>

        <div class="d-flex justify-content-end gap-3 mt-5">
            <a href="gestion_periodos_carga_de.jsp" class="btn btn-outline-teal px-5 py-2 fw-semibold d-flex align-items-center" style="border: 2px solid var(--teal-main); color: var(--teal-main); border-radius: 6px;">
                <i class="bi bi-chevron-left me-2"></i> Volver
            </a>
            <button type="submit" id="btnGuardar" class="btn-teal px-5 py-2" style="border-radius: 6px;">
                Agregar
            </button>
        </div>
    </form>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="assets/js/coordinador.js"></script>

<!-- Script con Fetch API -->
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const form = document.getElementById("formPeriodo");
        const inputFechaInicio = document.getElementById("fechaInicio");
        const inputFechaFin = document.getElementById("fechaFin");
        const btnGuardar = document.getElementById("btnGuardar");

        // Limitar calendario en tiempo real
        inputFechaInicio.addEventListener("change", function () {
            inputFechaFin.min = this.value;
            if (inputFechaFin.value && inputFechaFin.value < this.value) {
                inputFechaFin.value = "";
            }
        });

        // Envío de datos mediante FETCH
        form.addEventListener("submit", async function (e) {
            e.preventDefault(); // Previene el envío tradicional/recarga de página

            // 1. Validar fechas
            if (new Date(inputFechaFin.value) < new Date(inputFechaInicio.value)) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error en fechas',
                    text: 'La fecha de fin no puede ser anterior a la fecha de inicio.'
                });
                return;
            }

            // 2. Preparar payload (Objeto JSON con los datos del formulario)
            const data = {
                division: document.getElementById("division").value,
                fechaInicio: inputFechaInicio.value,
                fechaFin: inputFechaFin.value
            };

            // Deshabilitar botón durante el envío
            btnGuardar.disabled = true;

            try {
                // 3. Petición FETCH enviada a tu Servlet / Backend
                const response = await fetch("AgregarPeriodoServlet", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json; charset=UTF-8"
                    },
                    body: JSON.stringify(data)
                });

                // Parsear respuesta del servidor (asumiendo respuesta JSON)
                const result = await response.json();

                if (response.ok && result.status === "success") {
                    Swal.fire({
                        icon: 'success',
                        title: '¡Registrado!',
                        text: result.message || 'El periodo de carga se guardó correctamente.',
                        confirmButtonColor: '#008080'
                    }).then(() => {
                        // Redireccionar al listado tras confirmar
                        window.location.href = "gestion_periodos_carga_de.jsp";
                    });
                } else {
                    throw new Error(result.message || "Error al procesar la solicitud.");
                }

            } catch (error) {
                Swal.fire({
                    icon: 'error',
                    title: 'Ocurrió un error',
                    text: error.message || 'No se pudo conectar con el servidor.'
                });
            } finally {
                btnGuardar.disabled = false;
            }
        });
    });
</script>

</body>
</html>