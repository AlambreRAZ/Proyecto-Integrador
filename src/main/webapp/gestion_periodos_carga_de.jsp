<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Periodos de Carga</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
    <!-- SweetAlert2 CDN -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="periodos_carga" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title mb-4">GESTION DE PERIODOS DE CARGA</h3>

    <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
        <!-- Buscador en tiempo real -->
        <div class="search-box mb-0" style="max-width: 600px; flex-grow: 1;">
            <i class="bi bi-search"></i>
            <input type="text" id="inputBuscar" placeholder="Buscar periodo por división...">
        </div>

        <!-- Botón Agregar -->
        <a href="agregar_periodos_cargar_de.jsp" class="btn-teal">
            <i class="bi bi-calendar-plus me-2"></i> Agregar periodo
        </a>
    </div>

    <div class="data-card p-0 mb-4" style="overflow: hidden;">
        <table class="table-custom mb-0 text-center">
            <colgroup>
                <col style="width: 25%;">
                <col style="width: 25%;">
                <col style="width: 25%;">
                <col style="width: 10%;">
                <col style="width: 15%;">
            </colgroup>
            <thead>
            <tr>
                <th class="text-start">Division</th>
                <th>Fecha inicio</th>
                <th>fecha fin</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody id="tablaPeriodosBody">
            <!-- Se poblará dinámicamente mediante JavaScript -->
            </tbody>
        </table>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/coordinador.js"></script>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        cargarPeriodos();

        // Filtro rápido de búsqueda en frontend
        document.getElementById("inputBuscar").addEventListener("keyup", function() {
            const valor = this.value.toLowerCase();
            const filas = document.querySelectorAll("#tablaPeriodosBody tr");

            filas.forEach(fila => {
                const texto = fila.innerText.toLowerCase();
                fila.style.display = texto.includes(valor) ? "" : "none";
            });
        });
    });

    function cargarPeriodos() {
        const tbody = document.getElementById("tablaPeriodosBody");
        tbody.innerHTML = `<tr><td colspan="5" class="py-4 text-muted">Cargando periodos...</td></tr>`;

        fetch("ListarPeriodosServlet")
            .then(response => {
                if (!response.ok) throw new Error("Error al consultar el servidor");
                return response.json();
            })
            .then(data => {
                tbody.innerHTML = "";

                if (!data || data.length === 0) {
                    tbody.innerHTML = `
                    <tr>
                        <td colspan="5" class="py-4 text-muted">
                            No hay periodos de carga registrados.
                        </td>
                    </tr>`;
                    return;
                }

                data.forEach(periodo => {
                    const tr = document.createElement("tr");

                    const id = periodo.idPeriodo || periodo.id;
                    const esActivo = periodo.activo;

                    tr.innerHTML = `
                    <td class="text-start">
                        <div class="d-flex align-items-center">
                            <div class="avatar-circle" style="flex-shrink:0;"></div>
                            <span class="fw-semibold ms-2">\${periodo.division}</span>
                        </div>
                    </td>
                    <td>\${periodo.fechaInicio}</td>
                    <td>\${periodo.fechaFin}</td>
                    <td>
                        <button type="button" class="btn btn-link p-0 text-decoration-none"
                                title="\${esActivo ? 'Desactivar' : 'Activar'}"
                                onclick="confirmarCambioEstado(\${id}, \${!esActivo})">
                            <i class="bi \${esActivo ? 'bi-toggle-on text-success' : 'bi-toggle-off text-danger'} fs-4"></i>
                        </button>
                    </td>
                    <td>
                        <a href="editar_periodo_carga_de.jsp?id=\${id}" class="action-btn" title="Editar"><i class="bi bi-pencil"></i></a>
                        <a href="ver_periodo_carga_de.jsp?id=\${id}" class="action-btn" title="Ver detalle"><i class="bi bi-eye"></i></a>
                        <button type="button" class="action-btn delete" title="Eliminar" onclick="confirmarEliminar(\${id})">
                            <i class="bi bi-trash"></i>
                        </button>
                    </td>
                `;
                    tbody.appendChild(tr);
                });
            })
            .catch(error => {
                console.error("Error:", error);
                tbody.innerHTML = `
                <tr>
                    <td colspan="5" class="py-4 text-danger">
                        Ocurrió un error al cargar la lista de periodos.
                    </td>
                </tr>`;
            });
    }

    // Proceso de cambio de estado (Activar / Desactivar) manejado 100% en el JSP
    function confirmarCambioEstado(id, nuevoEstado) {
        const accionTexto = nuevoEstado ? "activar" : "desactivar";

        Swal.fire({
            title: `¿Deseas \${accionTexto} este periodo?`,
            text: `El periodo pasará a estar \${nuevoEstado ? 'Activo' : 'Inactivo'}.`,
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: nuevoEstado ? '#198754' : '#ffc107',
            cancelButtonColor: '#6c757d',
            confirmButtonText: `Sí, \${accionTexto}`,
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                fetch(`CambiarEstadoServlet?id=\${id}&estado=\${nuevoEstado}`)
                    .then(() => {
                        Swal.fire({
                            title: '¡Actualizado!',
                            text: `El periodo ha sido \${nuevoEstado ? 'activado' : 'desactivado'} con éxito.`,
                            icon: 'success',
                            timer: 1800,
                            showConfirmButton: false
                        });

                        cargarPeriodos();
                    })
                    .catch(error => {
                        console.error("Error al cambiar estado:", error);
                        Swal.fire('Error', 'No se pudo cambiar el estado del periodo.', 'error');
                    });
            }
        });
    }

    // Proceso completo de eliminación manejado 100% en el JSP
    function confirmarEliminar(id) {
        Swal.fire({
            title: '¿Estás seguro?',
            text: "Esta acción eliminará el periodo de carga.",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#dc3545',
            cancelButtonColor: '#6c757d',
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {

                fetch(`EliminarPeriodoServlet?id=\${id}`)
                    .then(() => {
                        // Al terminar de borrar, mostramos la alerta de éxito inmediatamente
                        Swal.fire({
                            title: '¡Eliminado!',
                            text: 'El periodo de carga ha sido eliminado correctamente.',
                            icon: 'success',
                            timer: 2000,
                            showConfirmButton: false
                        });

                        cargarPeriodos();
                    })
                    .catch(error => {
                        console.error("Error al eliminar:", error);
                        Swal.fire('Error', 'No se pudo eliminar el periodo.', 'error');
                    });
            }
        });
    }
</script>
</body>
</html>