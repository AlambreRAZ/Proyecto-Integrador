const contextPath = window.contextPath || '';
const tbody = document.getElementById('tablaEventosBody');
const inputBuscar = document.getElementById('buscarEvento');
const filtrosTipo = document.getElementById('filtrosTipo');
const contenedorPaginacion = document.getElementById('paginacionContainer');

let eventosOriginales = [];
let filtroTexto = '';
let filtroTipo = 'todos';

// Configuración de Paginación
let paginaActual = 1;
const eventosPorPagina = 5; // Cambia este valor según cuántos registros quieras ver por página

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

// Renderiza los registros correspondientes a la página activa
function renderEventos(eventos) {
    if (!eventos.length) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted py-4">No se encontraron eventos.</td></tr>';
        renderPaginacion(0);
        return;
    }

    // Lógica para cortar la lista según la página actual
    const totalPaginas = Math.ceil(eventos.length / eventosPorPagina);
    if (paginaActual > totalPaginas) paginaActual = 1;

    const inicio = (paginaActual - 1) * eventosPorPagina;
    const fin = inicio + eventosPorPagina;
    const eventosPágina = eventos.slice(inicio, fin);

    tbody.innerHTML = '';
    eventosPágina.forEach(function (ev) {
        const fila = document.createElement('tr');
        fila.setAttribute('data-id', ev.id);
        fila.innerHTML =
            '<td class="text-start">' +
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
            '<a href="ver_mas_evento_de.jsp?id=' + ev.id + '" class="action-btn"><i class="bi bi-eye"></i></a>' +
            '</td>';
        tbody.appendChild(fila);
    });

    renderPaginacion(totalPaginas);
}

// Renderiza los botones dinámicos de paginación
function renderPaginacion(totalPaginas) {
    if (!contenedorPaginacion) return;
    contenedorPaginacion.innerHTML = '';

    if (totalPaginas <= 1) return; // Si es 1 página o menos, oculta los botones

    // Botón Anterior
    const btnAnt = document.createElement('a');
    btnAnt.href = '#';
    btnAnt.className = 'page-btn' + (paginaActual === 1 ? ' disabled' : '');
    btnAnt.innerHTML = '<i class="bi bi-chevron-left"></i>';
    btnAnt.addEventListener('click', function (e) {
        e.preventDefault();
        if (paginaActual > 1) {
            paginaActual--;
            aplicarFiltros();
        }
    });
    contenedorPaginacion.appendChild(btnAnt);

    // Renderizado numerado
    for (let i = 1; i <= totalPaginas; i++) {
        if (
            i === 1 ||
            i === totalPaginas ||
            (i >= paginaActual - 1 && i <= paginaActual + 1)
        ) {
            const btnPage = document.createElement('a');
            btnPage.href = '#';
            btnPage.className = 'page-btn' + (i === paginaActual ? ' active' : '');
            btnPage.textContent = i;
            btnPage.addEventListener('click', function (e) {
                e.preventDefault();
                paginaActual = i;
                aplicarFiltros();
            });
            contenedorPaginacion.appendChild(btnPage);
        } else if (
            (i === 2 && paginaActual > 3) ||
            (i === totalPaginas - 1 && paginaActual < totalPaginas - 2)
        ) {
            const dots = document.createElement('span');
            dots.className = 'page-btn dots';
            dots.textContent = '...';
            contenedorPaginacion.appendChild(dots);
        }
    }

    // Botón Siguiente
    const btnSig = document.createElement('a');
    btnSig.href = '#';
    btnSig.className = 'page-btn' + (paginaActual === totalPaginas ? ' disabled' : '');
    btnSig.innerHTML = '<i class="bi bi-chevron-right"></i>';
    btnSig.addEventListener('click', function (e) {
        e.preventDefault();
        if (paginaActual < totalPaginas) {
            paginaActual++;
            aplicarFiltros();
        }
    });
    contenedorPaginacion.appendChild(btnSig);
}

function aplicarFiltros() {
    renderEventos(obtenerEventosFiltrados());
}

function cargarEventos() {
    let url = contextPath + '/ListarEventosServlet';
    if (window.idPersona) {
        url += '?idPersona=' + encodeURIComponent(window.idPersona);
    }

    fetch(url)
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
            tbody.innerHTML = '<tr><td colspan="5" class="text-center text-danger py-4">No se pudieron cargar los eventos. Revisa tu servidor.</td></tr>';
        });
}

if (inputBuscar) {
    inputBuscar.addEventListener('input', function () {
        filtroTexto = inputBuscar.value;
        paginaActual = 1; // Reinicia a la página 1 al buscar
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
        paginaActual = 1; // Reinicia a la página 1 al cambiar de filtro
        aplicarFiltros();
    });
}

cargarEventos();