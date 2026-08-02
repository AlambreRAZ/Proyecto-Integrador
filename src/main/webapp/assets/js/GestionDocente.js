// GestionDocente.js - Carga dinámica de usuarios (docentes/coordinadores) desde la BD
const contextPathDocente = window.contextPath || '';
const tbodyDocente = document.getElementById('tablaDocentesBody');
const inputBuscarDocente = document.getElementById('buscarDocente');

let usuariosOriginales = [];
let filtroTextoDocente = '';

const DIVISIONES = { 1: 'DATID', 2: 'DACEA', 3: 'DATEFI', 4: 'DAMI' };

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
    return DIVISIONES[id] || (id ? 'Div. ' + id : 'N/A');
}

function renderDocentes(lista) {
    if (!tbodyDocente) return;
    if (!lista.length) {
        tbodyDocente.innerHTML = '<tr><td colspan="6" class="text-center text-muted py-4">No se encontraron usuarios.</td></tr>';
        return;
    }

    tbodyDocente.innerHTML = '';
    lista.forEach(function (u) {
        const nombreCompleto = escHtml(getNombreCompleto(u));
        const estadoColor = u.activo === 1 ? '#28a745' : '#d32f2f';
        const estadoTexto = u.activo === 1 ? 'Activo' : 'Inactivo';
        const divisionNombre = escHtml(getDivisionNombre(u.idDivision));

        const tr = document.createElement('tr');
        tr.setAttribute('data-id', u.id);
        tr.innerHTML =
            '<td class="text-start">' +
            '  <div class="docente-name-container">' +
            '    <div class="avatar-circle" style="flex-shrink:0;"></div>' +
            '    <div class="docente-name" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">' + nombreCompleto + '</div>' +
            '  </div>' +
            '</td>' +
            '<td>' + escHtml(u.correo) + '</td>' +
            '<td>' + divisionNombre + '</td>' +
            '<td>' + escHtml(u.numeroEmpleado) + '</td>' +
            '<td style="color:' + estadoColor + ';font-weight:600;">' + estadoTexto + '</td>' +
            '<td style="white-space:nowrap;">' +
            '  <a href="editar_docente_co.jsp?id=' + u.id + '" class="action-btn" title="Editar"><i class="bi bi-pencil"></i></a>' +
            '  <a href="#" class="action-btn delete delete-docente" data-id="' + u.id + '" title="Eliminar"><i class="bi bi-trash"></i></a>' +
            '</td>';
        tbodyDocente.appendChild(tr);
    });
}

function aplicarFiltrosDocente() {
    const texto = normDocente(filtroTextoDocente);
    const filtrados = usuariosOriginales.filter(function (u) {
        return texto === '' ||
            normDocente(getNombreCompleto(u)).includes(texto) ||
            normDocente(u.correo).includes(texto) ||
            normDocente(u.numeroEmpleado).includes(texto);
    });
    renderDocentes(filtrados);
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
        aplicarFiltrosDocente();
    });
}

if (tbodyDocente) {
    tbodyDocente.addEventListener('click', function (e) {
        const btn = e.target.closest('.action-btn.delete-docente');
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
                eliminarDocente(id);
            });
        } else {
            if (confirm('¿Deseas eliminar este usuario?')) eliminarDocente(id);
        }
    });

    cargarDocentes();
}

function eliminarDocente(id) {
    const datos = new FormData();
    datos.append('id', id);
    fetch(contextPathDocente + '/EliminarUsuarioServlet', { method: 'POST', body: datos })
        .then(function (res) { return res.json(); })
        .then(function (data) {
            if (data.success) {
                if (typeof Swal !== 'undefined') {
                    Swal.fire({ icon: 'success', title: 'Usuario eliminado', confirmButtonColor: '#00847b' });
                }
                cargarDocentes();
            } else {
                alert('No se pudo eliminar: ' + (data.message || 'Error desconocido'));
            }
        })
        .catch(function (err) {
            console.error('Error al eliminar:', err);
        });
}
