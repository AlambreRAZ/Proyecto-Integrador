const contextPath = window.contextPath || '';
const tbody = document.getElementById('tablaDesarrolladoresBody');
const inputBuscar = document.getElementById('buscarDesarrollador');

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

// Helper unificado para mostrar alertas con SweetAlert2
function mostrarAlerta(titulo, mensaje, icono = 'warning') {
    if (typeof Swal !== 'undefined') {
        Swal.fire({
            icon: icono,
            title: titulo,
            text: mensaje,
            confirmButtonColor: '#00847b'
        });
    } else {
        alert(titulo + ': ' + mensaje);
    }
}

// ------------------------------------------------------------------
//  FUNCIÓN DE VALIDACIÓN FRONTEND COMPLETA
// ------------------------------------------------------------------
function validarFormularioFrontend(datos) {
    const { nombre, apeP, apeM, division, numEmp, tel, correo, pass, confirmPass } = datos;

    // 1. CAMPOS INCOMPLETOS / OBLIGATORIOS
    if (!nombre || !apeP || !apeM || !numEmp || !tel || !correo || !pass || !confirmPass) {
        mostrarAlerta('Campos incompletos', 'Por favor, llena todos los campos obligatorios del formulario.');
        return false;
    }

    // 2. SELECCIÓN DE DIVISIÓN ACADÉMICA
    if (!division) {
        mostrarAlerta('División requerida', 'Por favor selecciona una División Académica.');
        return false;
    }

    // 3. NÚMERO DE EMPLEADO ÚNICAMENTE CON DÍGITOS
    if (!/^\d+$/.test(numEmp)) {
        mostrarAlerta('Número de Empleado inválido', 'El número de empleado solo debe contener dígitos numéricos.');
        return false;
    }

    // 4. TELÉFONO ESTRICTAMENTE DE 10 DÍGITOS
    if (!/^\d{10}$/.test(tel)) {
        mostrarAlerta('Teléfono inválido', 'El teléfono debe ser de exactamente 10 dígitos numéricos.');
        return false;
    }

    // 5. CORREO INSTITUCIONAL MÁXIMO 50 CARACTERES
    if (correo.length > 50) {
        mostrarAlerta('Correo demasiado largo', 'El correo institucional no debe exceder los 50 caracteres.');
        return false;
    }

    // 6. CORREO ESTRICTAMENTE TERMINADO EN @utez.edu.mx
    if (!correo.toLowerCase().endsWith('@utez.edu.mx')) {
        mostrarAlerta('Correo no institucional', 'El correo debe terminar estrictamente en @utez.edu.mx');
        return false;
    }

    // 7. CONTRASEÑA ENTRE 12 Y 15 CARACTERES
    if (pass.length < 12 || pass.length > 15) {
        mostrarAlerta('Contraseña inválida', 'La contraseña debe tener entre 12 y 15 caracteres.');
        return false;
    }

    // 8. COINCIDENCIA EXACTA DE CONTRASEÑAS
    if (pass !== confirmPass) {
        mostrarAlerta('Las contraseñas no coinciden', 'Asegúrate de escribir exactamente la misma contraseña en ambos campos.');
        return false;
    }

    return true;
}

// ------------------------------------------------------------------
//  SANITIZACIÓN Y MANEJO DE DATOS
// ------------------------------------------------------------------
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
    if (!tbody) return;

    if (!lista || !lista.length) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted py-4">No se encontraron desarrolladores.</td></tr>';
        return;
    }

    tbody.innerHTML = '';
    lista.forEach(function (dev) {
        const activo = Number(dev.activo) === 1;
        const iconoEstado = activo ? 'bi-toggle-on text-success' : 'bi-toggle-off text-danger';
        const divisionNombre = DIVISIONES[dev.idDivision] || dev.division || '';

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
            '<a href="ver_desarrollador_de.jsp?id=' + dev.id + '" class="action-btn" title="Ver"><i class="bi bi-eye"></i></a>' +
            '<a href="#" class="action-btn delete" title="Eliminar" data-id="' + dev.id + '"><i class="bi bi-trash"></i></a>' +
            '</td>';
        tbody.appendChild(fila);
    });
}

function aplicarFiltro() {
    renderDesarrolladores(obtenerDesarrolladoresFiltrados());
}

function cargarDesarrolladores() {
    if (!tbody) return;
    tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted py-4">Cargando...</td></tr>';

    fetch(contextPath + '/ListarDesarrollador', { credentials: 'same-origin' })
        .then(function (response) {
            if (response.redirected || (response.url && response.url.includes('login.jsp'))) {
                window.location.href = 'login.jsp';
                return null;
            }
            return response.json();
        })
        .then(function (desarrolladores) {
            if (!desarrolladores) return;
            desarrolladoresOriginales = desarrolladores || [];
            aplicarFiltro();
        })
        .catch(function (error) {
            console.error('Error al cargar desarrolladores:', error);
            tbody.innerHTML = '<tr><td colspan="6" class="text-center text-danger py-4">No se pudieron cargar los desarrolladores.</td></tr>';
        });
}

function cambiarEstado(id, nuevoEstado) {
    const datos = new URLSearchParams();
    datos.append('id', id);
    datos.append('estado', nuevoEstado);

    return fetch(contextPath + '/CambiarEstadoUsuarioServlet', {
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

function eliminarDesarrolladorPermanente(id) {
    const datos = new URLSearchParams();
    datos.append('idUsuario', id);

    return fetch(contextPath + '/EliminarDocente', {
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

// ------------------------------------------------------------------
// ⌨️ RESTRICCIONES EN TIEMPO REAL (INPUT) Y EVENTOS DOM
// ------------------------------------------------------------------
document.addEventListener("DOMContentLoaded", function () {
    // A) RESTRICCIÓN: Solo letras y espacios en Nombre y Apellidos si existen en el DOM
    const inputsSoloTexto = document.querySelectorAll('#campoNombre, #campoApellidoP, #campoApellidoM, #nombre, #apellidoPaterno, #apellidoMaterno');
    inputsSoloTexto.forEach(function (input) {
        input.addEventListener('input', function () {
            this.value = this.value.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑ\s]/g, '');
        });
    });

    // B) RESTRICCIÓN: Solo dígitos en Num. Empleado y Teléfono si existen en el DOM
    const inputsSoloNumeros = document.querySelectorAll('#campoNumEmpleado, #campoTelefono, #numeroEmpleado, #telefono');
    inputsSoloNumeros.forEach(function (input) {
        input.addEventListener('input', function () {
            this.value = this.value.replace(/\D/g, '');
        });
    });

    // Buscador en vivo
    if (inputBuscar) {
        inputBuscar.addEventListener('input', function () {
            filtroTexto = inputBuscar.value.trim();
            aplicarFiltro();
        });
    }

    // Delegación de eventos para la tabla
    if (tbody) {
        tbody.addEventListener('click', function (e) {
            // Switch Toggle Estado
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

                    // --- PRELOADER CON PORCENTAJE PARA CAMBIAR ESTADO ---
                    let porcentaje = 0;
                    let timerCarga;

                    Swal.fire({
                        title: 'Actualizando estado...',
                        html: '<div style="font-size: 1.5rem; font-weight: bold; color: #00847b; margin-top: 10px;" id="lblPorcentajeDev">0%</div>',
                        allowOutsideClick: false,
                        allowEscapeKey: false,
                        showConfirmButton: false,
                        didOpen: () => {
                            Swal.showLoading();
                            timerCarga = setInterval(() => {
                                if (porcentaje < 90) {
                                    porcentaje += 10;
                                    const el = document.getElementById('lblPorcentajeDev');
                                    if (el) el.textContent = porcentaje + '%';
                                }
                            }, 80);
                        }
                    });

                    cambiarEstado(id, nuevoEstado)
                        .then(function (resultado) {
                            clearInterval(timerCarga);
                            const el = document.getElementById('lblPorcentajeDev');
                            if (el) el.textContent = '100%';

                            setTimeout(function () {
                                if (resultado.ok && resultado.data.success) {
                                    Swal.close();
                                    cargarDesarrolladores();
                                } else {
                                    mostrarAlerta('No se pudo actualizar el estado', resultado.data.message || 'Ocurrió un error al conectar con la base de datos.', 'error');
                                }
                            }, 300);
                        })
                        .catch(function (error) {
                            clearInterval(timerCarga);
                            console.error('Error al cambiar el estado:', error);
                            mostrarAlerta('Error de conexión', 'No fue posible comunicarse con el servidor.', 'error');
                        });
                });
                return;
            }

            // Bote de basura / Eliminar
            const botonEliminar = e.target.closest('.action-btn.delete');
            if (botonEliminar) {
                e.preventDefault();
                const id = botonEliminar.getAttribute('data-id');

                Swal.fire({
                    icon: 'warning',
                    title: '¿Deseas eliminar este desarrollador?',
                    text: 'Esta acción borrará al desarrollador de la base de datos.',
                    showCancelButton: true,
                    confirmButtonColor: '#dc3545',
                    cancelButtonColor: '#aaaaaa',
                    confirmButtonText: 'Sí, eliminar',
                    cancelButtonText: 'Cancelar'
                }).then(function (result) {
                    if (!result.isConfirmed) return;

                    // --- PRELOADER CON PORCENTAJE PARA ELIMINAR ---
                    let porcentaje = 0;
                    let timerCarga;

                    Swal.fire({
                        title: 'Eliminando desarrollador...',
                        html: '<div style="font-size: 1.5rem; font-weight: bold; color: #dc3545; margin-top: 10px;" id="lblPorcentajeDev">0%</div>',
                        allowOutsideClick: false,
                        allowEscapeKey: false,
                        showConfirmButton: false,
                        didOpen: () => {
                            Swal.showLoading();
                            timerCarga = setInterval(() => {
                                if (porcentaje < 90) {
                                    porcentaje += 10;
                                    const el = document.getElementById('lblPorcentajeDev');
                                    if (el) el.textContent = porcentaje + '%';
                                }
                            }, 80);
                        }
                    });

                    eliminarDesarrolladorPermanente(id)
                        .then(function (resultado) {
                            clearInterval(timerCarga);
                            const el = document.getElementById('lblPorcentajeDev');
                            if (el) el.textContent = '100%';

                            setTimeout(function () {
                                if (resultado.ok && resultado.data.success) {
                                    Swal.fire({
                                        icon: 'success',
                                        title: '¡Eliminado!',
                                        text: resultado.data.message || 'El desarrollador fue eliminado correctamente.',
                                        confirmButtonColor: '#00847b'
                                    });
                                    cargarDesarrolladores();
                                } else {
                                    mostrarAlerta('No se pudo eliminar', resultado.data.message || 'Ocurrió un error al eliminar el desarrollador.', 'error');
                                }
                            }, 300);
                        })
                        .catch(function (error) {
                            clearInterval(timerCarga);
                            console.error('Error al eliminar el desarrollador:', error);
                            mostrarAlerta('Error de conexión', 'No fue posible comunicarse con el servidor.', 'error');
                        });
                });
                return;
            }
        });
    }

    // Inicializar carga de tabla
    cargarDesarrolladores();
});