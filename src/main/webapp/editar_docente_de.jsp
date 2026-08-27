<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Docente</title>

    <!-- Bootstrap 5 CSS & Icons -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/coordinador.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="gestion_usuarios" />
    <jsp:param name="active_sub" value="docente" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title mb-4">EDITAR DOCENTE</h3>

    <div class="data-card p-4">
        <form id="formEditarDocente">
            <input type="hidden" id="idDocente" name="id" value="${param.id}">

            <div class="row g-3">
                <div class="col-md-4">
                    <label class="form-label fw-semibold">Nombre(s)</label>
                    <input type="text" class="form-control" id="campoNombre" name="nombre" required>
                </div>
                <div class="col-md-4">
                    <label class="form-label fw-semibold">Apellido Paterno</label>
                    <input type="text" class="form-control" id="campoApellidoP" name="apellidoPaterno" required>
                </div>
                <div class="col-md-4">
                    <label class="form-label fw-semibold">Apellido Materno</label>
                    <input type="text" class="form-control" id="campoApellidoM" name="apellidoMaterno">
                </div>

                <div class="col-md-6">
                    <label class="form-label fw-semibold">Correo Institucional</label>
                    <input type="email" class="form-control" id="campoCorreo" name="correo" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label fw-semibold">Número de Empleado</label>
                    <input type="text" class="form-control" id="campoNumEmpleado" name="numeroEmpleado" required>
                </div>

                <div class="col-md-6">
                    <label class="form-label fw-semibold">División Académica</label>
                    <select class="form-select" id="campoDivision" name="idDivision" required>
                        <option value="">Selecciona una división...</option>
                        <option value="1">Datid</option>
                        <option value="2">Dacea</option>
                        <option value="3">Datefi</option>
                        <option value="4">Dami</option>
                        <option value="5">General</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label fw-semibold">Teléfono</label>
                    <input type="text" class="form-control" id="campoTelefono" name="telefono" maxlength="10">
                </div>
            </div>

            <div class="d-flex justify-content-end gap-2 mt-4">
                <a href="${pageContext.request.contextPath}/gestion_docente_de.jsp" class="btn btn-outline-secondary px-4">
                    <i class="bi bi-arrow-left"></i> Cancelar
                </a>
                <button type="submit" class="btn-teal px-4 border-0">
                    <i class="bi bi-check-circle"></i> Guardar Cambios
                </button>
            </div>
        </form>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    window.contextPath = '${pageContext.request.contextPath}';

    document.addEventListener("DOMContentLoaded", function () {
        const idDocente = document.getElementById('idDocente').value;
        if (!idDocente) return;

        // 1. CARGAR DATOS DEL DOCENTE DESDE /ListarDocente
        fetch(window.contextPath + '/ListarDocente')
            .then(res => res.json())
            .then(listaDocentes => {
                const docente = listaDocentes.find(d => String(d.id) === String(idDocente));
                if (docente) {
                    document.getElementById('campoNombre').value = docente.nombre || '';
                    document.getElementById('campoApellidoP').value = docente.apellidoPaterno || '';
                    document.getElementById('campoApellidoM').value = docente.apellidoMaterno || '';
                    document.getElementById('campoCorreo').value = docente.correo || '';
                    document.getElementById('campoNumEmpleado').value = docente.numeroEmpleado || '';
                    document.getElementById('campoDivision').value = docente.idDivision || '';
                    document.getElementById('campoTelefono').value = docente.telefono || '';
                } else {
                    Swal.fire('Error', 'No se encontró la información del docente.', 'error');
                }
            })
            .catch(err => {
                console.error("Error al cargar datos:", err);
                Swal.fire('Error', 'No se pudieron obtener los datos del servidor.', 'error');
            });

        document.getElementById('formEditarDocente').addEventListener('submit', function (e) {
            e.preventDefault();

            const formData = new URLSearchParams(new FormData(this));

            // Si tu Servlet de edición se llama diferente (ej. /EditarDocente o /ModificarDocente), cámbialo aquí:
            const urlServlet = window.contextPath + '/EditarDocente';

            fetch(urlServlet, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
                body: formData.toString(),
                credentials: 'same-origin'
            })
                .then(async res => {
                    // Manejo de sesión expirada
                    if (res.redirected || res.status === 401 || res.status === 403) {
                        window.location.href = window.contextPath + '/index.jsp'; // o la ruta real de tu login
                        return null;
                    }
                    if (!res.ok) {
                        throw new Error(`Error HTTP ${res.status}: Servlet no encontrado en ${urlServlet}`);
                    }
                    return res.json();
                })
                .then(data => {
                    if (!data) return;
                    if (data.success || data.status === 'success') {
                        Swal.fire({
                            icon: 'success',
                            title: '¡Actualizado!',
                            text: 'Los datos del docente se actualizaron correctamente.',
                            confirmButtonColor: '#00847b'
                        }).then(() => {
                            window.location.href = window.contextPath + '/gestion_docente_de.jsp';
                        });
                    } else {
                        Swal.fire('Error', data.message || 'No se pudieron guardar los cambios.', 'error');
                    }
                })
                .catch(err => {
                    console.error("Error al guardar:", err);
                    Swal.fire('Error', err.message, 'error');
                });
        });
    });
</script>
</body>
</html>