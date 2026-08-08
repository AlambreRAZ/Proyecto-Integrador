const contextPath = window.contextPath || '';
const tbody = document.getElementById('tablaEventosBody');
const inputBuscar = document.getElementById('buscarEvento');
const contenedorFiltros = document.getElementById('contenedorFiltrosTipo');

let eventosOriginales = [];
let filtroTexto = '';
let filtroTipo = 'todos';

// Sanitización contra inyección HTML
function escapeHtml(texto) {
    if (texto === null || texto === undefined) return '';
    return String(texto)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;');
}

// Normalizador para búsqueda insensible a acentos/mayúsculas
function normalizar(texto) {
    return String(texto || '')
        .toLowerCase()
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '');
}

// Formateador de rangos de fecha
function formatearFechas(fechaInicio, fechaFin) {
    if (!fechaInicio) return 'Sin fecha';
    if (!fechaFin) return escapeHtml(fechaInicio);
    return `${escapeHtml(fechaInicio)} - ${escapeHtml(fechaFin)}`;
}

// Filtrado combinado: Texto de búsqueda + Filtro de Categoría/Pill
function obtenerEventosFiltrados() {
    const texto = normalizar(filtroTexto);

    return eventosOriginales.filter(function (evt) {
        const coincideTexto = normalizar(evt.titulo).includes(texto) ||
            normalizar(evt.subtitulo).includes(texto) ||
            normalizar(evt.institucion).includes(texto);

        const tipoEventoNormalizado = normalizar(evt.tipo || evt.tipoEvento);
        const tipoFiltroNormalizado = normalizar(filtroTipo);

        const coincideTipo = (filtroTipo === 'todos') || (tipoEventoNormalizado === tipoFiltroNormalizado);

        return coincideTexto && coincideTipo;
    });
}

// Renderizado dinámico de la tabla
function renderEventos(lista) {
    if (!tbody) return;

    if (!lista || !lista.length) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted py-4">No se encontraron eventos registrados.</td></tr>';
        return;
    }

    tbody.innerHTML = '';
    lista.forEach(function (evt) {
        const idEvento = evt.id || evt.idEvento;
        const titulo = escapeHtml(evt.titulo || evt.nombre);
        const subtitulo = escapeHtml(evt.subtitulo || evt.descripcionCorta || '');
        const tipo = escapeHtml(evt.tipo || evt.tipoEvento || 'Sin especificación');
        const institucion = escapeHtml(evt.institucion || 'N/A');
        const fechas = formatearFechas(evt.fechaInicio, evt.fechaFin);

        const fila = document.createElement('tr');
        fila.setAttribute('data-id', idEvento);
        fila.innerHTML = `
            <td class="text-start">
                <div class="fw-semibold">${titulo}</div>
                ${subtitulo ? `<div class="small text-muted">${subtitulo}</div>` : ''}
            </td>
            <td>${tipo}</td>
            <td>${institucion}</td>
            <td>${fechas}</td>
            <td>
                <a href="${contextPath}/ver_mas_evento_de.jsp?id=${idEvento}" class="action-btn" title="Ver detalle">
                    <i class="bi bi-eye"></i>
                </a>
            </td>
        `;
        tbody.appendChild(fila);
    });
}

function aplicarFiltro() {
    renderEventos(obtenerEventosFiltrados());
}

function cargarMisEventos() {
    if (!tbody) return;
    tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted py-4">Cargando eventos...</td></tr>';

    // Ajustar URL al Servlet de Backend que lista los eventos del usuario autenticado (ej. /ListarMisEventos)
    fetch(contextPath + '/ListarMisEventosServlet', { credentials: 'same-origin' })
        .then(function (response) {
            if (response.redirected || (response.url && response.url.includes('login.jsp'))) {
                window.location.href = 'login.jsp';
                return null;
            }
            return response.json();
        })
        .then(function (eventos) {
            if (!eventos) return;
            eventosOriginales = eventos || [];
            aplicarFiltro();
        })
        .catch(function (error) {
            console.error('Error al cargar la lista de eventos:', error);
            tbody.innerHTML = '<tr><td colspan="5" class="text-center text-danger py-4">No se pudieron cargar los eventos.</td></tr>';
        });
}

document.addEventListener("DOMContentLoaded", function () {

    if (inputBuscar) {
        inputBuscar.addEventListener('input', function () {
            filtroTexto = inputBuscar.value.trim();
            aplicarFiltro();
        });
    }

    if (contenedorFiltros) {
        contenedorFiltros.addEventListener('click', function (e) {
            const pill = e.target.closest('.nav-pill');
            if (pill) {
                e.preventDefault();

                contenedorFiltros.querySelectorAll('.nav-pill').forEach(p => p.classList.remove('active'));
                pill.classList.add('active');

                filtroTipo = pill.getAttribute('data-tipo') || 'todos';
                aplicarFiltro();
            }
        });
    }

    cargarMisEventos();
});