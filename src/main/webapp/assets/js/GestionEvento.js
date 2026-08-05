// GestionEvento.js - Eventos con paginación dinámica
const contextPath = window.contextPath || '';
const tbody = document.getElementById('tablaEventosBody');
const inputBuscar = document.getElementById('buscarEvento');
const filtrosTipo = document.getElementById('filtrosTipo');
const paginationContainer = document.getElementById('paginationContainer');

const ITEMS_POR_PAGINA = 10;
let eventosOriginales = [];
let filtroTexto = '';
let filtroTipo = 'todos';
let paginaActual = 1;

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

function renderPaginacion(total) {
    if (!paginationContainer) return;
    const totalPaginas = Math.ceil(total / ITEMS_POR_PAGINA);
    if (totalPaginas <= 1) {
        paginationContainer.innerHTML = '';
        return;
    }

    let html = '<a href="#" class="page-btn" id="btnPrevPage"><i class="bi bi-chevron-left"></i></a>';
    for (let i = 1; i <= totalPaginas; i++) {
        html += '<a href="#" class="page-btn ' + (i === paginaActual ? 'active' : '') + '" data-page="' + i + '">' + i + '</a>';
    }
    html += '<a href="#" class="page-btn" id="btnNextPage"><i class="bi bi-chevron-right"></i></a>';
    paginationContainer.innerHTML = html;

    paginationContainer.querySelectorAll('[data-page]').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            paginaActual = parseInt(this.getAttribute('data-page'));
            aplicarFiltros();
        });
    });

    document.getElementById('btnPrevPage').addEventListener('click', function (e) {
        e.preventDefault();
        if (paginaActual > 1) { paginaActual--; aplicarFiltros(); }
    });
    document.getElementById('btnNextPage').addEventListener('click', function (e) {
        e.preventDefault();
        if (paginaActual < totalPaginas) { paginaActual++; aplicarFiltros(); }
    });
}

function renderEventos(eventos) {
    const totalFiltrados = eventos.length;
    const inicio = (paginaActual - 1) * ITEMS_POR_PAGINA;
    const fin = inicio + ITEMS_POR_PAGINA;
    const paginados = eventos.slice(inicio, fin);

    if (!paginados.length) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted py-4">No se encontraron eventos.</td></tr>';
        renderPaginacion(0);
        return;
    }

    tbody.innerHTML = '';
    const isDesarrollador = window.location.pathname.includes('_de.jsp');
    paginados.forEach(function (ev) {
        const fila = document.createElement('tr');
        fila.setAttribute('data-id', ev.id);
        const colDivision = isDesarrollador ? '<td><span class="badge bg-secondary">' + escapeHtml(ev.nombreDivision) + '</span></td>' : '';
        const editUrl = isDesarrollador ? 'agregar_evento_de.jsp?id=' + ev.id : 'editar_evento_co.jsp?id=' + ev.id;
        const verUrl = isDesarrollador ? 'ver_mas_evento_de.jsp?id=' + ev.id : 'ver_mas_evento_co.jsp?id=' + ev.id;

        fila.innerHTML =
            '<td>' +
            '<div class="fw-semibold">' + escapeHtml(ev.nombre) + '</div>' +
            '</td>' +
            '<td>' + escapeHtml(ev.tipo) + '</td>' +
            '<td>' +
            '<div>' + escapeHtml(ev.institucion) + '</div>' +
            '</td>' +
            '<td>' + formatearFecha(ev.fechaInicio) + ' - ' + formatearFecha(ev.fechaFin) + '</td>' +
            colDivision +
            '<td>' +
            '<a href="' + editUrl + '" class="action-btn"><i class="bi bi-pencil"></i></a>' +
            '<a href="' + verUrl + '" class="action-btn"><i class="bi bi-eye"></i></a>' +
            '<a href="#" class="action-btn delete" data-id="' + ev.id + '"><i class="bi bi-trash"></i></a>' +
            '</td>';
        tbody.appendChild(fila);
    });

    renderPaginacion(totalFiltrados);
}

function aplicarFiltros() {
    renderEventos(obtenerEventosFiltrados());
}

function cargarEventos() {
    fetch(contextPath + '/ListarEventosServlet')
        .then(function (response) {
            const contentType = response.headers.get("content-type");
            if (contentType && contentType.indexOf("application/json") !== -1) {
                return response.json();
            } else {
                throw new Error("El servidor no devolvió un JSON.");
            }
        })
        .then(function (eventos) {
            eventosOriginales = eventos || [];
            paginaActual = 1;
            aplicarFiltros();
        })
        .catch(function (error) {
            console.error('Error al cargar eventos:', error);
            tbody.innerHTML = '<tr><td colspan="6" class="text-center text-danger py-4">No se pudieron cargar los eventos. Revisa tu servidor.</td></tr>';
        });
}

if (inputBuscar) {
    inputBuscar.addEventListener('input', function () {
        filtroTexto = inputBuscar.value;
        paginaActual = 1;
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
        paginaActual = 1;
        aplicarFiltros();
    });
}

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

    cargarEventos();
}