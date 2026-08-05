// GestionUsuarios.js - Lista docentes/coordinadores con paginación dinámica y columna Rol
const contextPath = window.contextPath || '';
const tbody = document.getElementById('tablaUsuariosBody');
const inputBuscar = document.getElementById('buscarUsuario');
const paginationContainer = document.getElementById('paginationContainerDocente');

const ITEMS_POR_PAGINA = 10;
let usuariosOriginales = [];
let filtroTexto = '';
let paginaActual = 1;

const DIVISIONES = { 1: 'DATID', 2: 'DACEA', 3: 'DATEFI', 4: 'DAMI' };

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

function getDivisionNombre(id) {
    return DIVISIONES[id] || (id ? 'Div. ' + id : 'N/A');
}

function obtenerUsuariosFiltrados() {
    const texto = normalizar(filtroTexto);
    const filtrados = usuariosOriginales.filter(function (u) {
        const nombreCompleto = [u.nombre, u.apellidoPaterno, u.apellidoMaterno].filter(Boolean).join(' ');
        return texto === '' ||
            normalizar(nombreCompleto).includes(texto) ||
            normalizar(u.correo || u.correoInstitucional || '').includes(texto) ||
            normalizar(u.numeroEmpleado || '').includes(texto);
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
    let html = '<a href="#" class="page-btn" id="btnPrevPageU"><i class="bi bi-chevron-left"></i></a>';
    for (let i = 1; i <= totalPaginas; i++) {
        html += '<a href="#" class="page-btn ' + (i === paginaActual ? 'active' : '') + '" data-page="' + i + '">' + i + '</a>';
    }
    html += '<a href="#" class="page-btn" id="btnNextPageU"><i class="bi bi-chevron-right"></i></a>';
    paginationContainer.innerHTML = html;

    paginationContainer.querySelectorAll('[data-page]').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            paginaActual = parseInt(this.getAttribute('data-page'));
            aplicarFiltros();
        });
    });
    document.getElementById('btnPrevPageU').addEventListener('click', function (e) {
        e.preventDefault();
        if (paginaActual > 1) { paginaActual--; aplicarFiltros(); }
    });
    document.getElementById('btnNextPageU').addEventListener('click', function (e) {
        e.preventDefault();
        const totalPags = Math.ceil(obtenerUsuariosFiltrados().length / ITEMS_POR_PAGINA);
        if (paginaActual < totalPags) { paginaActual++; aplicarFiltros(); }
    });
}

function renderUsuarios(usuarios) {
    const total = usuarios.length;
    const inicio = (paginaActual - 1) * ITEMS_POR_PAGINA;
    const paginados = usuarios.slice(inicio, inicio + ITEMS_POR_PAGINA);

    if (!paginados.length) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center text-muted py-4">No se encontraron usuarios.</td></tr>';
        renderPaginacion(0);
        return;
    }

    tbody.innerHTML = '';
    paginados.forEach(function (u) {
        const fila = document.createElement('tr');
        fila.setAttribute('data-id', u.id || u.idUsuario);

        const nombreCompleto = [u.nombre, u.apellidoPaterno, u.apellidoMaterno].filter(Boolean).join(' ');
        const initial = u.nombre ? u.nombre.charAt(0).toUpperCase() : 'U';
        const division = getDivisionNombre(u.idDivision);
        const correo = u.correo || u.correoInstitucional || '';
        const rolLabel = u.rol ? (u.rol.charAt(0).toUpperCase() + u.rol.slice(1)) : 'Docente';
        const rolColor = u.rol === 'coordinador' ? '#0dcaf0' : '#198754';
        const estadoColor = u.activo === 1 ? '#28a745' : '#d32f2f';
        const estadoTexto = u.activo === 1 ? 'Activo' : 'Inactivo';

        fila.innerHTML =
            '<td class="text-start">' +
            '  <div class="docente-name-container">' +
            '    <div class="avatar-circle" style="flex-shrink:0;">' + initial + '</div>' +
            '    <div class="docente-name" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">' + escapeHtml(nombreCompleto) + '</div>' +
            '  </div>' +
            '</td>' +
            '<td>' + escapeHtml(correo) + '</td>' +
            '<td>' + escapeHtml(division) + '</td>' +
            '<td>' + escapeHtml(u.numeroEmpleado || '') + '</td>' +
            '<td><span class="badge" style="background:' + rolColor + ';color:white;">' + escapeHtml(rolLabel) + '</span></td>' +
            '<td style="color:' + estadoColor + ';font-weight:600;">' + estadoTexto + '</td>' +
            '<td style="white-space:nowrap;">' +
            '  <a href="editar_docente_de.jsp?id=' + (u.id || u.idUsuario) + '" class="action-btn" title="Editar"><i class="bi bi-pencil"></i></a>' +
            '  <a href="#" class="action-btn" title="Ver"><i class="bi bi-eye"></i></a>' +
            '  <a href="#" class="action-btn delete" data-id="' + (u.id || u.idUsuario) + '" title="Eliminar"><i class="bi bi-trash"></i></a>' +
            '</td>';
        tbody.appendChild(fila);
    });

    renderPaginacion(total);
}

function aplicarFiltros() {
    renderUsuarios(obtenerUsuariosFiltrados());
}

function cargarUsuarios() {
    // Carga docentes Y coordinadores (sin filtro de rol para que el desarrollador vea todos)
    fetch(contextPath + '/ListarUsuariosServlet?t=' + Date.now())
        .then(function (response) {
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.indexOf('application/json') !== -1) {
                return response.json();
            } else {
                throw new Error('El servidor no devolvió un JSON.');
            }
        })
        .then(function (usuarios) {
            usuariosOriginales = usuarios || [];
            paginaActual = 1;
            aplicarFiltros();
        })
        .catch(function (error) {
            console.error('Error al cargar usuarios:', error);
            tbody.innerHTML = '<tr><td colspan="7" class="text-center text-danger py-4">No se pudieron cargar los docentes.</td></tr>';
        });
}

if (inputBuscar) {
    inputBuscar.addEventListener('input', function () {
        filtroTexto = inputBuscar.value;
        paginaActual = 1;
        aplicarFiltros();
    });
}

if (tbody) {
    tbody.addEventListener('click', function (e) {
        const btn = e.target.closest('.action-btn.delete');
        if (!btn) return;
        e.preventDefault();
        const id = btn.getAttribute('data-id');

        if (typeof Swal !== 'undefined') {
            Swal.fire({
                icon: 'warning',
                title: '¿Deseas eliminar este usuario?',
                text: 'Esta acción no se puede deshacer.',
                showCancelButton: true,
                confirmButtonColor: '#00847b',
                cancelButtonColor: '#aaaaaa',
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar'
            }).then(function (result) {
                if (!result.isConfirmed) return;
                eliminarUsuario(id);
            });
        } else {
            if (confirm('¿Deseas eliminar este usuario?')) eliminarUsuario(id);
        }
    });

    cargarUsuarios();
}

function eliminarUsuario(id) {
    const datos = new FormData();
    datos.append('id', id);
    fetch(contextPath + '/EliminarUsuarioServlet', { method: 'POST', body: datos })
        .then(function (res) { return res.json(); })
        .then(function (data) {
            if (data.success) {
                if (typeof Swal !== 'undefined') {
                    Swal.fire({ icon: 'success', title: 'Usuario eliminado', confirmButtonColor: '#00847b' });
                }
                cargarUsuarios();
            } else {
                alert('No se pudo eliminar: ' + (data.message || 'Error desconocido'));
            }
        })
        .catch(function (err) { console.error('Error al eliminar:', err); });
}
