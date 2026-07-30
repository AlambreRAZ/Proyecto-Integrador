<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Eventos</title>
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
    <h3 class="page-title mb-4">GESTION DE EVENTOS</h3>

    <div class="d-flex flex-wrap gap-2 mb-4">
        <a href="#" class="nav-pill active">Todos</a>
        <a href="#" class="nav-pill">Diplomado</a>
        <a href="#" class="nav-pill">Conferencia</a>
        <a href="#" class="nav-pill">Taller</a>
        <a href="#" class="nav-pill">Curso</a>
        <a href="#" class="nav-pill">Certificacion</a>
    </div>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="search-box mb-0" style="max-width: 600px; flex-grow: 1; margin-right: 20px;">
            <i class="bi bi-search"></i>
            <input type="text" placeholder="Buscar Evento por nombre ...">
        </div>
        <a href="agregar_evento_co.jsp" class="btn-teal">
            <i class="bi bi-calendar-plus"></i> Agregar Evento
        </a>
    </div>

    <div class="data-card p-0 mb-4" style="overflow: hidden;">
        <table class="table-custom mb-0">
            <colgroup>
                <col style="width: 30%;">
                <col style="width: 14%;">
                <col style="width: 22%;">
                <col style="width: 20%;">
                <col style="width: 14%;">
            </colgroup>
            <thead>
                <tr>
                    <th>Titulo</th>
                    <th>Tipo</th>
                    <th>Institución</th>
                    <th>Fecha</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody id="tablaEventosBody">
                <tr>
                    <td colspan="5" class="text-center text-muted py-4">Cargando eventos...</td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="pagination-container">
        <a href="#" class="page-btn"><i class="bi bi-chevron-left"></i></a>
        <a href="#" class="page-btn active">1</a>
        <a href="#" class="page-btn">2</a>
        <a href="#" class="page-btn">3</a>
        <span class="page-btn dots">...</span>
        <a href="#" class="page-btn">10</a>
        <a href="#" class="page-btn"><i class="bi bi-chevron-right"></i></a>
    </div>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/coordinador.js"></script>
<script>
    const contextPath = '<%= request.getContextPath() %>';
    const tbody = document.getElementById('tablaEventosBody');

    function escapeHtml(texto) {
        if (texto === null || texto === undefined) return '';
        return String(texto)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;');
    }

    function formatearFecha(fechaIso) {
        if (!fechaIso) return '';
        const partes = fechaIso.split('-');
        if (partes.length !== 3) return fechaIso;
        return partes[2] + '/' + partes[1] + '/' + partes[0].slice(2);
    }

    function cargarEventos() {
        fetch(contextPath + '/ListarEventosServlet')
            .then(function (response) { return response.json(); })
            .then(function (eventos) {
                if (!eventos.length) {
                    tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted py-4">No hay eventos registrados.</td></tr>';
                    return;
                }

                tbody.innerHTML = '';
                eventos.forEach(function (ev) {
                    const fila = document.createElement('tr');
                    fila.setAttribute('data-id', ev.id);
                    fila.innerHTML =
                        '<td>' +
                            '<div class="fw-semibold">' + escapeHtml(ev.nombre) + '</div>' +
                            '<div class="small text-muted">' + escapeHtml(ev.descripcion) + '</div>' +
                        '</td>' +
                        '<td>' + escapeHtml(ev.tipo) + '</td>' +
                        '<td>' +
                            '<div>' + escapeHtml(ev.institucion) + '</div>' +
                            '<div class="small text-muted">' + escapeHtml(ev.lugar) + '</div>' +
                        '</td>' +
                        '<td>' + formatearFecha(ev.fechaInicio) + ' - ' + formatearFecha(ev.fechaFin) + '</td>' +
                        '<td>' +
                            '<a href="editar_evento_co.jsp?id=' + ev.id + '" class="action-btn"><i class="bi bi-pencil"></i></a>' +
                            '<a href="ver_mas_evento_co.jsp?id=' + ev.id + '" class="action-btn"><i class="bi bi-eye"></i></a>' +
                            '<a href="#" class="action-btn delete" data-id="' + ev.id + '"><i class="bi bi-trash"></i></a>' +
                        '</td>';
                    tbody.appendChild(fila);
                });
            })
            .catch(function (error) {
                console.error('Error al cargar eventos:', error);
                tbody.innerHTML = '<tr><td colspan="5" class="text-center text-danger py-4">No se pudieron cargar los eventos.</td></tr>';
            });
    }

    tbody.addEventListener('click', function (e) {
        const boton = e.target.closest('.action-btn.delete');
        if (!boton) return;
        e.preventDefault();

        const id = boton.getAttribute('data-id');

        Swal.fire({
            icon: 'warning',
            title: '¿Deseas eliminar este evento?',
            text: 'Esta acción no se puede deshacer.',
            showCancelButton: true,
            confirmButtonColor: '#00847b',
            cancelButtonColor: '#aaaaaa',
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar'
        }).then(function (result) {
            // Si cancela, simplemente se cierra la alerta y no pasa nada más.
            if (!result.isConfirmed) return;

            const datos = new FormData();
            datos.append('id', id);

            fetch(contextPath + '/EliminarEventoServlet', {
                method: 'POST',
                body: datos
            })
                .then(function (response) {
                    return response.json().then(function (data) {
                        return { ok: response.ok, data: data };
                    });
                })
                .then(function (resultado) {
                    if (resultado.ok && resultado.data.success) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Evento eliminado',
                            text: 'El evento se eliminó correctamente.',
                            confirmButtonColor: '#00847b'
                        });
                        cargarEventos();
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'No se pudo eliminar',
                            text: resultado.data.message || 'Ocurrió un error al eliminar el evento.',
                            confirmButtonColor: '#00847b'
                        });
                    }
                })
                .catch(function (error) {
                    console.error('Error al eliminar el evento:', error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Error de conexión',
                        text: 'No fue posible comunicarse con el servidor.',
                        confirmButtonColor: '#00847b'
                    });
                });
        });
    });

    cargarEventos();
</script>
</body>
</html>