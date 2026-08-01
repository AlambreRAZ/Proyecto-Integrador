// 1. CORREGIDO: "const" en lugar de "onst"
const contextPath = window.contextPath || '';
const tbody = document.getElementById('tablaEventosBody');
const inputBuscar = document.getElementById('buscarEvento');
const filtrosTipo = document.getElementById('filtrosTipo');

let eventosOriginales = [];
let filtroTexto = '';
let filtroTipo = 'todos';

function escapeHtml(texto) {
    if (texto === null || texto === undefined) return '';
    return String(texto)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;');
}

function normalizar(texto) {
    return String(texto || '')
        .toLowerCase()
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '');
}

function formatearFecha(fechaIso) {
    if (!fechaIso) return '';
    const partes = fechaIso.split('-');
    if (partes.length !== 3) return fechaIso;
    return partes[2] + '/' + partes[1] + '/' + partes[0].slice(2);
}

function obtenerEventosFiltrados() {
    const texto = normalizar(filtroTexto);

    const filtrados = eventosOriginales.filter(function (ev) {
        const coincideTipo = filtroTipo === 'todos' || normalizar(ev.tipo) === filtroTipo;
        const coincideTexto = texto === '' || normalizar(ev.nombre).includes(texto);
        return coincideTipo && coincideTexto;
    });

    filtrados.sort(function (a, b) {
        return normalizar(a.nombre).localeCompare(normalizar(b.nombre));
    });

    return filtrados;
}

function renderEventos(eventos) {
    if (!eventos.length) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted py-4">No se encontraron eventos.</td></tr>';
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
}

function aplicarFiltros() {
    renderEventos(obtenerEventosFiltrados());
}

function cargarEventos() {
    fetch(contextPath + '/ListarEventosServlet')
        .then(function (response) {
            // 3. MEJORA: Validar si la respuesta es JSON
            const contentType = response.headers.get("content-type");
            if (contentType && contentType.indexOf("application/json") !== -1) {
                return response.json();
            } else {
                throw new Error("El servidor no devolvió un JSON.");
            }
        })
        .then(function (eventos) {
            eventosOriginales = eventos || [];
            aplicarFiltros();
        })
        .catch(function (error) {
            console.error('Error al cargar eventos:', error);
            tbody.innerHTML = '<tr><td colspan="5" class="text-center text-danger py-4">No se pudieron cargar los eventos. Revisa tu servidor.</td></tr>';
        });
}

if (inputBuscar) {
    inputBuscar.addEventListener('input', function () {
        filtroTexto = inputBuscar.value;
        aplicarFiltros();
    });
}

if (filtrosTipo) {
    filtrosTipo.addEventListener('click', function (e) {
        const pill = e.target.closest('.nav-pill');
        if (!pill) return;
        e.preventDefault();

        filtrosTipo.querySelectorAll('.nav-pill').forEach(function (p) {
            p.classList.remove('active');
        });
        pill.classList.add('active');

        filtroTipo = pill.getAttribute('data-tipo') || 'todos';
        aplicarFiltros();
    });
}

// 2. CORREGIDO: Validamos que tbody exista antes de meterle eventos y cargar la data
if (tbody) {
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
            if (!result.isConfirmed) return;

            const datos = new FormData();
            datos.append('id', id);

            fetch(contextPath + '/EliminarEventoServlet', {
                method: 'POST',
                body: datos
            })
                .then(function (response) {
                    // 4. MEJORA: Validamos si la respuesta es JSON al eliminar
                    const contentType = response.headers.get("content-type");
                    if (contentType && contentType.indexOf("application/json") !== -1) {
                        return response.json().then(function (data) {
                            return { ok: response.ok, data: data };
                        });
                    } else {
                        throw new Error("El servidor devolvió un error HTML al intentar eliminar.");
                    }
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

    // Solo cargamos los eventos si existe la tabla
    cargarEventos();
}