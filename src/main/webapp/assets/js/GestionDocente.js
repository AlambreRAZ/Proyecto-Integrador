// GestionDocente.js - Para la vista del Coordinador (gestion_docente_co.jsp)
// Carga docentes/coordinadores de la MISMA división con switch activar/desactivar y paginación
const contextPathDocente = window.contextPath || '';
const tbodyDocente = document.getElementById('tablaDocentesBody');
const inputBuscarDocente = document.getElementById('buscarDocente');
const paginationDocente = document.getElementById('paginationContainerDocente');

const ITEMS_POR_PAGINA_D = 10;
let usuariosOriginales = [];
let filtroTextoDocente = '';
let paginaActualD = 1;

const DIVISIONES_MAP = { 1: 'DATID', 2: 'DACEA', 3: 'DATEFI', 4: 'DAMI' };

function escHtml(t) {
    if (t === null || t === undefined) return '';
    return String(t).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}

function normDocente(t) {
    return String(t || '').toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');
}

function getNombreCompleto(u) {
    return [u.nombre, u.apellidoPaterno, u.apellidoMaterno].filter(Boolean).join(' ');
}

function getDivisionNombre(id) {
    return DIVISIONES_MAP[id] || (id ? 'Div. ' + id : 'N/A');
}

function renderPaginacionD(total) {
    if (!paginationDocente) return;
    const totalPaginas = Math.ceil(total / ITEMS_POR_PAGINA_D);
    if (totalPaginas <= 1) {
        paginationDocente.innerHTML = '';
        return;
    }
    let html = '<a href="#" class="page-btn" id="btnPrevPageD"><i class="bi bi-chevron-left"></i></a>';
    for (let i = 1; i <= totalPaginas; i++) {
        html += '<a href="#" class="page-btn ' + (i === paginaActualD ? 'active' : '') + '" data-page="' + i + '">' + i + '</a>';
    }
    html += '<a href="#" class="page-btn" id="btnNextPageD"><i class="bi bi-chevron-right"></i></a>';
    paginationDocente.innerHTML = html;

    paginationDocente.querySelectorAll('[data-page]').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            paginaActualD = parseInt(this.getAttribute('data-page'));
            aplicarFiltrosDocente();
        });
    });
    document.getElementById('btnPrevPageD').addEventListener('click', function (e) {
        e.preventDefault();
        if (paginaActualD > 1) { paginaActualD--; aplicarFiltrosDocente(); }
    });
    document.getElementById('btnNextPageD').addEventListener('click', function (e) {
        e.preventDefault();
        const totalPags = Math.ceil(obtenerFiltrados().length / ITEMS_POR_PAGINA_D);
        if (paginaActualD < totalPags) { paginaActualD++; aplicarFiltrosDocente(); }
    });
}

function obtenerFiltrados() {
    const texto = normDocente(filtroTextoDocente);
    return usuariosOriginales.filter(function (u) {
        return texto === '' ||
            normDocente(getNombreCompleto(u)).includes(texto) ||
            normDocente(u.correo || '').includes(texto) ||
            normDocente(u.correoInstitucional || '').includes(texto) ||
            normDocente(u.numeroEmpleado || '').includes(texto);
    });
}

function renderDocentes(lista) {
    if (!tbodyDocente) return;
    const inicio = (paginaActualD - 1) * ITEMS_POR_PAGINA_D;
    const paginados = lista.slice(inicio, inicio + ITEMS_POR_PAGINA_D);

    if (!paginados.length) {
        tbodyDocente.innerHTML = '<tr><td colspan="6" class="text-center text-muted py-4">No se encontraron usuarios.</td></tr>';
        if (paginationDocente) renderPaginacionD(0);
        return;
    }

    tbodyDocente.innerHTML = '';
    paginados.forEach(function (u) {
        const id = u.id || u.idUsuario;
        const nombreCompleto = escHtml(getNombreCompleto(u));
        const correo = escHtml(u.correo || u.correoInstitucional || '');
        const divisionNombre = escHtml(getDivisionNombre(u.idDivision));
        const initial = u.nombre ? u.nombre.charAt(0).toUpperCase() : 'U';
        const activo = Number(u.activo) === 1;
        const iconoToggle = activo
            ? '<i class="bi bi-toggle-on text-success fs-4 toggle-estado" style="cursor:pointer;" data-id="' + id + '" data-activo="1" title="Activo - click para desactivar"></i>'
            : '<i class="bi bi-toggle-off text-danger fs-4 toggle-estado" style="cursor:pointer;" data-id="' + id + '" data-activo="0" title="Inactivo - click para activar"></i>';

        const tr = document.createElement('tr');
        tr.setAttribute('data-id', id);
        tr.innerHTML =
            '<td class="text-start">' +
            '  <div class="docente-name-container">' +
            '    <div class="avatar-circle" style="flex-shrink:0;">' + initial + '</div>' +
            '    <div class="docente-name" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">' + nombreCompleto + '</div>' +
            '  </div>' +
            '</td>' +
            '<td>' + correo + '</td>' +
            '<td>' + divisionNombre + '</td>' +
            '<td>' + escHtml(u.numeroEmpleado || '') + '</td>' +
            '<td>' + iconoToggle + '</td>' +
            '<td style="white-space:nowrap;">' +
            '  <a href="editar_docente_co.jsp?id=' + id + '" class="action-btn" title="Editar"><i class="bi bi-pencil"></i></a>' +
            '  <a href="#" class="action-btn" title="Ver"><i class="bi bi-eye"></i></a>' +
            '</td>';
        tbodyDocente.appendChild(tr);
    });

    renderPaginacionD(lista.length);
}

function aplicarFiltrosDocente() {
    renderDocentes(obtenerFiltrados());
}

function cargarDocentes() {
    const url = contextPathDocente + '/ListarUsuariosServlet?t=' + Date.now();
    fetch(url)
        .then(function (res) {
            if (!res.ok) throw new Error('Error de servidor: ' + res.status);
            return res.json();
        })
        .then(function (data) {
            usuariosOriginales = data || [];
            paginaActualD = 1;
            aplicarFiltrosDocente();
        })
        .catch(function (err) {
            console.error('Error al cargar usuarios:', err);
            if (tbodyDocente) {
                tbodyDocente.innerHTML = '<tr><td colspan="6" class="text-center text-danger py-4">No se pudieron cargar los usuarios.</td></tr>';
            }
        });
}

if (inputBuscarDocente) {
    inputBuscarDocente.addEventListener('input', function () {
        filtroTextoDocente = inputBuscarDocente.value;
        paginaActualD = 1;
        aplicarFiltrosDocente();
    });
}

if (tbodyDocente) {
    tbodyDocente.addEventListener('click', function (e) {
        // Switch toggle de estado
        const toggle = e.target.closest('.toggle-estado');
        if (toggle) {
            const id = toggle.getAttribute('data-id');
            const activoActual = toggle.getAttribute('data-activo') === '1';
            const nuevoEstado = activoActual ? 0 : 1;
            const accion = activoActual ? 'desactivar' : 'activar';

            if (typeof Swal !== 'undefined') {
                Swal.fire({
                    icon: 'question',
                    title: '¿Deseas ' + accion + ' este usuario?',
                    text: activoActual
                        ? 'El usuario no podrá iniciar sesión mientras esté inactivo.'
                        : 'El usuario podrá volver a iniciar sesión.',
                    showCancelButton: true,
                    confirmButtonColor: '#00847b',
                    cancelButtonColor: '#aaaaaa',
                    confirmButtonText: 'Sí, ' + accion,
                    cancelButtonText: 'Cancelar'
                }).then(function (result) {
                    if (!result.isConfirmed) return;
                    cambiarEstadoDocente(id, nuevoEstado);
                });
            } else {
                if (confirm('¿Deseas ' + accion + ' este usuario?')) cambiarEstadoDocente(id, nuevoEstado);
            }
            return;
        }
    });

    cargarDocentes();
}

function cambiarEstadoDocente(id, nuevoEstado) {
    const datos = new URLSearchParams();
    datos.append('id', id);
    datos.append('estado', nuevoEstado);
    fetch(contextPathDocente + '/CambiarEstadoUsuarioServlet', { 
        method: 'POST', 
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: datos.toString() 
    })
        .then(function (res) { return res.json(); })
        .then(function (data) {
            if (data.success) {
                if (typeof Swal !== 'undefined') {
                    Swal.fire({ icon: 'success', title: nuevoEstado === 1 ? 'Usuario activado' : 'Usuario desactivado', confirmButtonColor: '#00847b' });
                }
                cargarDocentes();
            } else {
                alert('No se pudo cambiar el estado: ' + (data.message || 'Error desconocido'));
            }
        })
        .catch(function (err) { console.error('Error:', err); });
}
