const contextPath = window.contextPath || '';
const tbody = document.getElementById('tablaDesarrolladoresBody');
const inputBuscar = document.getElementById('buscarDesarrollador');

// Mismo mapeo id -> nombre de división que usan agregar_desarrollador_de.jsp y editar_desarrollador_de.jsp.
const DIVISIONES = {
    1: 'Datid',
    2: 'Dacea',
    3: 'Datefi',
    4: 'Dami',
    5: 'General'
};

// "Lista maestra" con todos los desarrolladores que trae el servidor.
let desarrolladoresOriginales = [];
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
        .replace(/[\u0300-\u036f]/g, ''); // quita acentos para que "búsqueda" == "busqueda"
}

function nombreCompleto(dev) {
    return [dev.nombre, dev.apellidoPaterno, dev.apellidoMaterno].filter(Boolean).join(' ');
}

function obtenerDesarrolladoresFiltrados() {
    const texto = normalizar(filtroTexto);
    if (texto === '') return desarrolladoresOriginales;

    return desarrolladoresOriginales.filter(function (dev) {
        return normalizar(nombreCompleto(dev)).includes(texto) ||
            normalizar(dev.correo).includes(texto);
    });
}

function renderDesarrolladores(lista) {
    if (!lista.length) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted py-4">No se encontraron desarrolladores.</td></tr>';
        return;
    }

    tbody.innerHTML = '';
    lista.forEach(function (dev) {
        const activo = Number(dev.activo) === 1;
        const iconoEstado = activo ? 'bi-toggle-on text-success' : 'bi-toggle-off text-danger';
        const divisionNombre = DIVISIONES[dev.idDivision] || '';

        const fila = document.createElement('tr');
        fila.setAttribute('data-id', dev.id);
        fila.innerHTML =
            '<td class="text-start">' +
            '<div class="docente-name-container">' +
            '<div class="avatar-circle" style="flex-shrink:0;"></div>' +
            '<div class="docente-name" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">' +
            escapeHtml(nombreCompleto(dev)) +
            '</div>' +
            '</div>' +
            '</td>' +
            '<td>' + escapeHtml(dev.correo) + '</td>' +
            '<td>' + escapeHtml(divisionNombre) + '</td>' +
            '<td>' + escapeHtml(dev.numeroEmpleado) + '</td>' +
            '<td>' +
            '<i class="bi ' + iconoEstado + ' fs-4 toggle-estado" style="cursor:pointer;" data-id="' + dev.id + '" data-activo="' + (activo ? 1 : 0) + '"></i>' +
            '</td>' +
            '<td class="acciones-cell" style="white-space: nowrap;">' +
            '<a href="editar_desarrollador_de.jsp?id=' + dev.id + '" class="action-btn" title="Editar"><i class="bi bi-pencil"></i></a>' +
            '<a href="editar_desarrollador_de.jsp?id=' + dev.id + '" class="action-btn" title="Ver"><i class="bi bi-eye"></i></a>' +
            '<a href="#" class="action-btn delete" title="Eliminar" data-id="' + dev.id + '"><i class="bi bi-trash"></i></a>' +
            '</td>';
        tbody.appendChild(fila);
    });
}

function aplicarFiltro() {
    renderDesarrolladores(obtenerDesarrolladoresFiltrados());
}

function cargarDesarrolladores() {
    tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted py-4">Cargando...</td></tr>';

    fetch(contextPath + '/ListarDesarrollador')
        .then(function (response) { return response.json(); })
        .then(function (desarrolladores) {
            desarrolladoresOriginales = desarrolladores || [];
            aplicarFiltro();
        })
        .catch(function (error) {
            console.error('Error al cargar desarrolladores:', error);
            tbody.innerHTML = '<tr><td colspan="6" class="text-center text-danger py-4">No se pudieron cargar los desarrolladores.</td></tr>';
        });
}

function cambiarEstado(id, nuevoEstado) {
    const datos = new FormData();
    datos.append('id', id);
    datos.append('estado', nuevoEstado);

    return fetch(contextPath + '/EliminarDesarrollador', {
        method: 'POST',
        body: datos
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

tbody.addEventListener('click', function (e) {
    // Interruptor de la columna "Estado": activa o desactiva directamente.
    const toggle = e.target.closest('.toggle-estado');
    if (toggle) {
        const id = toggle.getAttribute('data-id');
        const activoActual = toggle.getAttribute('data-activo') === '1';
        const nuevoEstado = activoActual ? 0 : 1;
        const accion = activoActual ? 'desactivar' : 'activar';

        Swal.fire({
            icon: 'question',
            title: '¿Deseas ' + accion + ' a este desarrollador?',
            text: activoActual
                ? 'El desarrollador no podrá iniciar sesión mientras esté inactivo.'
                : 'El desarrollador podrá volver a iniciar sesión.',
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
                        cargarDesarrolladores();
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

    // Botón de bote de basura: desactiva al desarrollador (no borra el registro).
    const boton = e.target.closest('.action-btn.delete');
    if (!boton) return;
    e.preventDefault();

    const id = boton.getAttribute('data-id');

    Swal.fire({
        icon: 'warning',
        title: '¿Deseas eliminar este desarrollador?',
        text: 'El desarrollador se marcará como inactivo y no podrá iniciar sesión.',
        showCancelButton: true,
        confirmButtonColor: '#00847b',
        cancelButtonColor: '#aaaaaa',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then(function (result) {
        if (!result.isConfirmed) return;

        cambiarEstado(id, 0)
            .then(function (resultado) {
                if (resultado.ok && resultado.data.success) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Desarrollador eliminado',
                        text: 'El desarrollador se eliminó correctamente.',
                        confirmButtonColor: '#00847b'
                    });
                    cargarDesarrolladores();
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'No se pudo eliminar',
                        text: resultado.data.message || 'Ocurrió un error al eliminar el desarrollador.',
                        confirmButtonColor: '#00847b'
                    });
                }
            })
            .catch(function (error) {
                console.error('Error al eliminar el desarrollador:', error);
                Swal.fire({
                    icon: 'error',
                    title: 'Error de conexión',
                    text: 'No fue posible comunicarse con el servidor.',
                    confirmButtonColor: '#00847b'
                });
            });
    });
});

cargarDesarrolladores();