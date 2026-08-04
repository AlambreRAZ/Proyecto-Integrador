const contextPath = window.contextPath || '';
const tbody = document.getElementById('tablaUsuariosBody');
const inputBuscar = document.getElementById('buscarUsuario');

let usuariosOriginales = [];
let filtroTexto = '';

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

function obtenerUsuariosFiltrados() {
    const texto = normalizar(filtroTexto);

    const filtrados = usuariosOriginales.filter(function (u) {
        const nombreCompleto = (u.nombre + ' ' + u.apellidoPaterno + ' ' + u.apellidoMaterno);
        const coincideTexto = texto === '' || normalizar(nombreCompleto).includes(texto) || normalizar(u.correo).includes(texto);
        return coincideTexto;
    });

    filtrados.sort(function (a, b) {
        return normalizar(a.nombre).localeCompare(normalizar(b.nombre));
    });

    return filtrados;
}

function renderUsuarios(usuarios) {
    if (!usuarios.length) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted py-4">No se encontraron usuarios.</td></tr>';
        return;
    }

    tbody.innerHTML = '';
    usuarios.forEach(function (u) {
        const fila = document.createElement('tr');
        fila.setAttribute('data-id', u.id);
        
        let initial = u.nombre ? u.nombre.charAt(0).toUpperCase() : 'U';
        let division = u.idDivision === 1 ? 'DATID' : (u.idDivision === 2 ? 'DACEA' : 'OTRA');
        let estadoIcon = u.activo === 1 
            ? '<i class="bi bi-toggle-on text-success fs-4"></i>' 
            : '<i class="bi bi-toggle-off text-danger fs-4"></i>';
            
        fila.innerHTML =
            '<td class="text-start">' +
            '    <div class="docente-name-container">' +
            '        <div class="avatar-circle" style="flex-shrink:0;">' + initial + '</div>' +
            '        <div class="docente-name" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">' +
            '            ' + escapeHtml(u.nombre + ' ' + u.apellidoPaterno + ' ' + u.apellidoMaterno) +
            '        </div>' +
            '    </div>' +
            '</td>' +
            '<td>' + escapeHtml(u.correo) + '</td>' +
            '<td>' + division + '</td>' +
            '<td>' + escapeHtml(u.numeroEmpleado) + '</td>' +
            '<td>' + estadoIcon + '</td>' +
            '<td style="white-space: nowrap;">' +
            '    <a href="editar_docente_de.jsp?id=' + u.id + '" class="action-btn" title="Editar"><i class="bi bi-pencil"></i></a>' +
            '    <a href="#" class="action-btn" title="Ver"><i class="bi bi-eye"></i></a>' +
            '    <a href="#" class="action-btn delete" title="Eliminar"><i class="bi bi-trash"></i></a>' +
            '</td>';
        tbody.appendChild(fila);
    });
}

function aplicarFiltros() {
    renderUsuarios(obtenerUsuariosFiltrados());
}

function cargarUsuarios() {
    fetch(contextPath + '/ListarUsuariosServlet?rol=docente') // o se puede cambiar si necesitas docentes y coordinadores
        .then(function (response) {
            const contentType = response.headers.get("content-type");
            if (contentType && contentType.indexOf("application/json") !== -1) {
                return response.json();
            } else {
                throw new Error("El servidor no devolvió un JSON.");
            }
        })
        .then(function (usuarios) {
            usuariosOriginales = usuarios || [];
            aplicarFiltros();
        })
        .catch(function (error) {
            console.error('Error al cargar usuarios:', error);
            tbody.innerHTML = '<tr><td colspan="6" class="text-center text-danger py-4">No se pudieron cargar los docentes.</td></tr>';
        });
}

if (inputBuscar) {
    inputBuscar.addEventListener('input', function () {
        filtroTexto = inputBuscar.value;
        aplicarFiltros();
    });
}

if (tbody) {
    cargarUsuarios();
}
