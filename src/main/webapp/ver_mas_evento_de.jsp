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

<jsp:include page="sidebar.jsp">
    <jsp:param name="active" value="gestion_evento" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title mb-4" id="tituloEvento">EVENTO</h3>

    <div class="row mb-3">
        <div class="col-md-4">
            <label class="form-label text-muted">Nombre del evento:</label>
            <input type="text" class="form-control" id="campoNombre" value="" readonly>
        </div>
        <div class="col-md-4">
            <label class="form-label text-muted">Lugar:</label>
            <input type="text" class="form-control" id="campoLugar" value="" readonly>
        </div>
        <div class="col-md-4">
            <label class="form-label text-muted">Institución / Empresa:</label>
            <input type="text" class="form-control" id="campoInstitucion" value="" readonly>
        </div>
    </div>

    <div class="row mb-3">
        <div class="col-md-4">
            <label class="form-label text-muted">Tipo de evento:</label>
            <input type="text" class="form-control" id="campoTipo" value="" readonly>
        </div>
        <div class="col-md-8">
            <label class="form-label text-muted">Descripción del evento:</label>
            <input type="text" class="form-control" id="campoDescripcion" value="" readonly>
        </div>
    </div>

    <div class="row mb-4">
        <div class="col-md-2">
            <label class="form-label text-muted">Fecha de inicio:</label>
            <input type="text" class="form-control" id="campoFechaInicio" value="" readonly>
        </div>
        <div class="col-md-2">
            <label class="form-label text-muted">Fecha de fin:</label>
            <input type="text" class="form-control" id="campoFechaFin" value="" readonly>
        </div>
        <div class="col-md-2 mt-3 mt-md-0 d-flex flex-column justify-content-center">
            <div class="modalidad-label mb-1">Modalidad</div>
            <div class="fs-6 text-dark" id="campoModalidad">-</div>
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
                <td class="status-active">Activo</td>
                <td>
                    <a href="archivo_subido.jsp" class="action-btn"><i class="bi bi-eye"></i></a>
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
                <td class="status-active">Activo</td>
                <td>
                    <a href="archivo_subido.jsp" class="action-btn"><i class="bi bi-eye"></i></a>
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
                <td class="status-active">Activo</td>
                <td>
                    <a href="archivo_subido.jsp" class="action-btn"><i class="bi bi-eye"></i></a>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div class="d-flex justify-content-end">
        <a href="gestion_evento_co.jsp" class="btn-teal">Volver</a>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="assets/js/coordinador.js"></script>
<script>
    const contextPath = '<%= request.getContextPath() %>';
    const params = new URLSearchParams(window.location.search);
    const idEvento = params.get('id');

    const tituloEvento = document.getElementById('tituloEvento');
    const campoNombre = document.getElementById('campoNombre');
    const campoLugar = document.getElementById('campoLugar');
    const campoInstitucion = document.getElementById('campoInstitucion');
    const campoTipo = document.getElementById('campoTipo');
    const campoDescripcion = document.getElementById('campoDescripcion');
    const campoFechaInicio = document.getElementById('campoFechaInicio');
    const campoFechaFin = document.getElementById('campoFechaFin');
    const campoModalidad = document.getElementById('campoModalidad');

    // Convierte "yyyy-MM-dd" (formato que maneja el servidor) a "dd/mm/yy" (formato que usa esta vista)
    function aFechaVisible(iso) {
        if (!iso) return '';
        const partes = iso.split('-');
        if (partes.length !== 3) return iso;
        return partes[2] + '/' + partes[1] + '/' + partes[0].slice(2);
    }

    function capitalizar(texto) {
        if (!texto) return '';
        return texto.charAt(0).toUpperCase() + texto.slice(1);
    }

    function cargarEvento() {
        if (!idEvento) {
            Swal.fire({
                icon: 'error',
                title: 'Falta el id del evento',
                text: 'Entra a esta página desde "Gestión de Eventos" para poder ver el detalle.',
                confirmButtonColor: '#00847b'
            });
            return;
        }

        fetch(contextPath + '/EditarEventoServlet?id=' + encodeURIComponent(idEvento))
            .then(function (response) { return response.json(); })
            .then(function (data) {
                if (!data.success) {
                    Swal.fire({
                        icon: 'error',
                        title: 'No se pudo cargar el evento',
                        text: data.message || 'Ocurrió un error al obtener los datos.',
                        confirmButtonColor: '#00847b'
                    });
                    return;
                }

                tituloEvento.textContent = (data.nombre || '').toUpperCase();
                campoNombre.value = data.nombre || '';
                campoLugar.value = data.lugar || '';
                campoInstitucion.value = data.institucion || '';
                campoTipo.value = capitalizar(data.tipo);
                campoDescripcion.value = data.descripcion || '';
                campoFechaInicio.value = aFechaVisible(data.fechaInicio);
                campoFechaFin.value = aFechaVisible(data.fechaFin);
                campoModalidad.textContent = capitalizar(data.modalidad);
            })
            .catch(function (error) {
                console.error('Error al cargar el evento:', error);
                Swal.fire({
                    icon: 'error',
                    title: 'Error de conexión',
                    text: 'No fue posible comunicarse con el servidor.',
                    confirmButtonColor: '#00847b'
                });
            });
    }

    cargarEvento();
</script>
</body>
</html>
