const contextPath = window.contextPath || '';
const tbody = document.getElementById('tablaDocentesBody');
const inputBuscar = document.getElementById('buscarDocente');

// Mismo mapeo id -> nombre de división.
const DIVISIONES = {
    1: 'Datid',
    2: 'Dacea',
    3: 'Datefi',
    4: 'Dami',
    5: 'General'
};

// "Lista maestra" con todos los docentes que trae el servidor.
let docentesOriginales = [];
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
        .replace(/[\u0300-\u036f]/g, ''); // quita acentos
}

function nombreCompleto(doc) {
    return [doc.nombre, doc.apellidoPaterno, doc.apellidoMaterno].filter(Boolean).join(' ');
}

function obtenerDocentesFiltrados() {
    const texto = normalizar(filtroTexto);
    if (texto === '') return docentesOriginales;

    return docentesOriginales.filter(function (doc) {
        return normalizar(nombreCompleto(doc)).includes(texto) ||
            normalizar(doc.correo).includes(texto);
    });
}

function renderDocentes(lista) {
    if (!tbody) return;

    if (!lista || !lista.length) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted py-4">No se encontraron docentes.</td></tr>';
        return;
    }

    let sufijoRol = 'de';
    const pathActual = window.location.pathname;

    if (pathActual.includes('_co.jsp')) {
        sufijoRol = 'co';
    } else if (pathActual.includes('_do.jsp')) {
        sufijoRol = 'do';
    } else if (pathActual.includes('_de.jsp')) {
        sufijoRol = 'de';
    }

    tbody.innerHTML = '';
    lista.forEach(function (doc) {
        const activo = Number(doc.activo) === 1;
        const iconoEstado = activo ? 'bi-toggle-on text-success' : 'bi-toggle-off text-danger';
        const divisionNombre = DIVISIONES[doc.idDivision] || '';

        const fila = document.createElement('tr');
        fila.setAttribute('data-id', doc.id);
        fila.innerHTML =
            '<td class="text-start">' +
            '<div class="docente-name-container">' +
            '<div class="avatar-circle" style="flex-shrink:0;"></div>' +
            '<div class="docente-name" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">' +
            escapeHtml(nombreCompleto(doc)) +
            '</div>' +
            '</div>' +
            '</td>' +
            '<td>' + escapeHtml(doc.correo) + '</td>' +
            '<td>' + escapeHtml(divisionNombre) + '</td>' +
            '<td>' + escapeHtml(doc.numeroEmpleado) + '</td>' +
            '<td>' +
            '<i class="bi ' + iconoEstado + ' fs-4 toggle-estado" style="cursor:pointer;" data-id="' + doc.id + '" data-activo="' + (activo ? 1 : 0) + '"></i>' +
            '</td>' +
            '<td class="acciones-cell" style="white-space: nowrap;">' +

            /* EDITAR */
            '<a href="' + contextPath + '/editar_docente_' + sufijoRol + '.jsp?id=' + doc.id + '" class="action-btn" title="Editar"><i class="bi bi-pencil"></i></a>' +

            /* VER DETALLES */
            '<a href="' + contextPath + '/verDocente?id=' + doc.id + '" class="action-btn" title="Ver"><i class="bi bi-eye"></i></a>' +

            /* ELIMINAR PERMANENTE */
            '<a href="#" class="action-btn delete" title="Eliminar" data-id="' + doc.id + '"><i class="bi bi-trash"></i></a>' +
            '</td>';
        tbody.appendChild(fila);
    });
}

function aplicarFiltro() {
    renderDocentes(obtenerDocentesFiltrados());
}

function cargarDocentes() {
    if (!tbody) return;
    tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted py-4">Cargando...</td></tr>';

    fetch(contextPath + '/ListarDocente')
        .then(function (response) { return response.json(); })
        .then(function (docentes) {
            docentesOriginales = docentes || [];
            aplicarFiltro();
        })
        .catch(function (error) {
            console.error('Error al cargar docentes:', error);
            tbody.innerHTML = '<tr><td colspan="6" class="text-center text-danger py-4">No se pudieron cargar los docentes.</td></tr>';
        });
}

function llenarFormularioDocente(data) {
    document.getElementById('campoIdUsuario').value = data.idUsuario || '';
    document.getElementById('campoNombre').value = data.nombre || '';
    document.getElementById('campoApellidoP').value = data.apellidoPaterno || '';
    document.getElementById('campoApellidoM').value = data.apellidoMaterno || '';
    document.getElementById('campoDivision').value = data.idDivision || '';
    document.getElementById('campoNumEmpleado').value = data.numeroEmpleado || '';
    document.getElementById('campoTelefono').value = data.telefono || '';
    document.getElementById('campoCorreo').value = data.correo || '';
    document.getElementById('campoContrasena').value = data.contrasena || '';
}

function cambiarEstado(id, nuevoEstado) {
    const datos = new URLSearchParams();
    datos.append('id', id);
    datos.append('estado', nuevoEstado);

    // Si creaste 'CambiarEstadoUsuarioServlet', cambia la URL a '/CambiarEstadoUsuarioServlet'
    const urlServlet = contextPath + '/CambiarEstado';

    return fetch(urlServlet, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
        body: datos.toString(),
        credentials: 'same-origin'
    }).then(function (response) {
        return response.json().then(function (data) {
            return { ok: response.ok, data: data };
        });
    });
}

if (inputBuscar) {
    inputBuscar.addEventListener('input', function () {
        filtroTexto = inputBuscar.value;
        aplicarFiltro();
    });
}

if (tbody) {
    tbody.addEventListener('click', function (e) {
        // -----------------------------------------------------------
        // 1. Interruptor de la columna "Estado" (Activar / Desactivar)
        // -----------------------------------------------------------
        const toggle = e.target.closest('.toggle-estado');
        if (toggle) {
            const id = toggle.getAttribute('data-id');
            const activoActual = toggle.getAttribute('data-activo') === '1';
            const nuevoEstado = activoActual ? 0 : 1;
            const accion = activoActual ? 'desactivar' : 'activar';

            Swal.fire({
                icon: 'question',
                title: '¿Deseas ' + accion + ' a este docente?',
                text: activoActual
                    ? 'El docente no podrá iniciar sesión mientras esté inactivo.'
                    : 'El docente podrá volver a iniciar sesión.',
                showCancelButton: true,
                confirmButtonColor: '#00847b',
                cancelButtonColor: '#aaaaaa',
                confirmButtonText: 'Sí, ' + accion,
                cancelButtonText: 'Cancelar'
            }).then(function (result) {
                if (!result.isConfirmed) return;

                cambiarEstado(id, nuevoEstado)
                    .then(function (resultado) {
                        if (resultado.ok && resultado.data.success) {
                            Swal.fire({
                                icon: 'success',
                                title: '¡Éxito!',
                                text: resultado.data.message || 'Estado actualizado correctamente.',
                                confirmButtonColor: '#00847b',
                                timer: 1500,
                                showConfirmButton: false
                            });
                            cargarDocentes(); // Recarga la tabla para reflejar el cambio del switch
                        } else {
                            Swal.fire({
                                icon: 'error',
                                title: 'No se pudo actualizar el estado',
                                text: resultado.data.message || 'Ocurrió un error al conectar con la base de datos.',
                                confirmButtonColor: '#00847b'
                            });
                        }
                    })
                    .catch(function (error) {
                        console.error('Error al cambiar el estado:', error);
                        Swal.fire({
                            icon: 'error',
                            title: 'Error de conexión',
                            text: 'No fue posible comunicarse con el servidor.',
                            confirmButtonColor: '#00847b'
                        });
                    });
            });
            return;
        }

        // -----------------------------------------------------------
        // 2. Botón Bote de Basura 🗑 (Sentencia DELETE permanente)
        // -----------------------------------------------------------
        const boton = e.target.closest('.action-btn.delete');
        if (!boton) return;
        e.preventDefault();

        const idUsuario = boton.getAttribute('data-id');

        Swal.fire({
            icon: 'warning',
            title: '¿Estás seguro de eliminar?',
            text: 'Esta acción borrará permanentemente al docente de la base de datos.',
            showCancelButton: true,
            confirmButtonColor: '#dc3545',
            cancelButtonColor: '#aaaaaa',
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar'
        }).then(function (result) {
            if (!result.isConfirmed) return;

            const datos = new URLSearchParams();
            datos.append('idUsuario', idUsuario);

            fetch(contextPath + '/EliminarDocente', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
                body: datos.toString(),
                credentials: 'same-origin'
            })
                .then(function (res) { return res.json(); })
                .then(function (data) {
                    if (data && data.success) {
                        Swal.fire({
                            icon: 'success',
                            title: '¡Eliminado!',
                            text: data.message || 'El docente fue eliminado de la base de datos.',
                            confirmButtonColor: '#00847b'
                        });
                        cargarDocentes();
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'No se pudo eliminar',
                            text: data.message || 'Ocurrió un error al intentar eliminar el registro.',
                            confirmButtonColor: '#00847b'
                        });
                    }
                })
                .catch(function (error) {
                    console.error('Error al eliminar el docente:', error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Error de conexión',
                        text: 'No fue posible comunicarse con el servidor.',
                        confirmButtonColor: '#00847b'
                    });
                });
        });
    });
}

// Iniciar carga al entrar
cargarDocentes();