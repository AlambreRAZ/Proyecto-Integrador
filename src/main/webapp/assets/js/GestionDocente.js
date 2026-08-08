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

// Helper para alertas SweetAlert2 unificadas
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
// 👁ALTERNAR VISIBILIDAD DE CONTRASEÑA (SI APLICA EN FORMULARIO/MODAL)
// ------------------------------------------------------------------
function setupTogglePassword(btnId, inputId) {
    const btn = document.getElementById(btnId);
    const input = document.getElementById(inputId);
    if (!btn || !input) return;

    btn.addEventListener('click', function () {
        const icon = btn.querySelector('i');
        if (input.type === 'password') {
            input.type = 'text';
            if (icon) icon.classList.replace('bi-eye', 'bi-eye-slash');
        } else {
            input.type = 'password';
            if (icon) icon.classList.replace('bi-eye-slash', 'bi-eye');
        }
    });
}

// ------------------------------------------------------------------
//  VALIDACIONES EN EL FRONTEND (GUARDAR / EDITAR)
// ------------------------------------------------------------------
function validarFormularioDocente(datos) {
    const { nombre, apeP, apeM, division, numEmp, tel, correo, pass, confirmPass } = datos;

    if (!nombre || !apeP || !apeM || !numEmp || !tel || !correo || !pass || (confirmPass !== undefined && !confirmPass)) {
        mostrarAlerta('Campos incompletos', 'Por favor, llena todos los campos obligatorios del formulario.');
        return false;
    }

    if (!division) {
        mostrarAlerta('División requerida', 'Por favor selecciona una División Académica.');
        return false;
    }

    if (!/^\d+$/.test(numEmp)) {
        mostrarAlerta('Número de Empleado inválido', 'El número de empleado solo debe contener dígitos numéricos.');
        return false;
    }

    if (!/^\d{10}$/.test(tel)) {
        mostrarAlerta('Teléfono inválido', 'El teléfono debe ser de exactamente 10 dígitos numéricos.');
        return false;
    }

    if (correo.length > 50) {
        mostrarAlerta('Correo demasiado largo', 'El correo institucional no debe exceder los 50 caracteres.');
        return false;
    }

    if (!correo.toLowerCase().endsWith('@utez.edu.mx')) {
        mostrarAlerta('Correo no institucional', 'El correo debe terminar estrictamente en @utez.edu.mx');
        return false;
    }

    if (pass.length < 12 || pass.length > 15) {
        mostrarAlerta('Contraseña inválida', 'La contraseña debe tener entre 12 y 15 caracteres.');
        return false;
    }

    if (confirmPass !== undefined && pass !== confirmPass) {
        mostrarAlerta('Las contraseñas no coinciden', 'Asegúrate de escribir exactamente la misma contraseña en ambos campos.');
        return false;
    }

    return true;
}

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
        const divisionNombre = DIVISIONES[doc.idDivision] || doc.division || '';

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

            /* VER DETALLES (Ruta corregida con sufijo de rol) */
            '<a href="' + contextPath + '/ver_docente_' + sufijoRol + '.jsp?id=' + doc.id + '" class="action-btn" title="Ver"><i class="bi bi-eye"></i></a>' +

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

    fetch(contextPath + '/ListarDocente', { credentials: 'same-origin' })
        .then(function (response) {
            if (response.redirected || (response.url && response.url.includes('login.jsp'))) {
                window.location.href = 'login.jsp';
                return null;
            }
            return response.json();
        })
        .then(function (docentes) {
            if (!docentes) return;
            docentesOriginales = docentes || [];
            aplicarFiltro();
        })
        .catch(function (error) {
            console.error('Error al cargar docentes:', error);
            tbody.innerHTML = '<tr><td colspan="6" class="text-center text-danger py-4">No se pudieron cargar los docentes.</td></tr>';
        });
}

function cambiarEstado(id, nuevoEstado) {
    const datos = new URLSearchParams();
    datos.append('id', id);
    datos.append('estado', nuevoEstado);

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

// ------------------------------------------------------------------
// ⌨️ RESTRICCIONES EN TIEMPO REAL (INPUT) Y MANEJO DE EVENTOS DOM
// ------------------------------------------------------------------
document.addEventListener("DOMContentLoaded", function () {

    // Inicializar ojito de contraseña si existe en esta vista
    setupTogglePassword('btnTogglePass', 'campoContrasena');
    setupTogglePassword('btnToggleConfirmPass', 'campoConfirmarContrasena');

    // 1. RESTRICCIÓN EN TIEMPO REAL: Solo letras y espacios en Nombre y Apellidos
    const inputsTexto = document.querySelectorAll('#campoNombre, #campoApellidoP, #campoApellidoM, #nombre, #apellidoPaterno, #apellidoMaterno');
    inputsTexto.forEach(function (input) {
        input.addEventListener('input', function () {
            this.value = this.value.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑ\s]/g, '');
        });
    });

    // 2. RESTRICCIÓN EN TIEMPO REAL: Solo dígitos en Num. Empleado y Teléfono
    const inputsNumericos = document.querySelectorAll('#campoNumEmpleado, #campoTelefono, #numeroEmpleado, #telefono');
    inputsNumericos.forEach(function (input) {
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

            // 1. Interruptor "Estado" (Activar / Desactivar)
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

                    let porcentaje = 0;
                    let timerCarga;

                    Swal.fire({
                        title: 'Actualizando estado...',
                        html: '<div style="font-size: 1.5rem; font-weight: bold; color: #00847b; margin-top: 10px;" id="lblPorcentajeDoc">0%</div>',
                        allowOutsideClick: false,
                        allowEscapeKey: false,
                        showConfirmButton: false,
                        didOpen: () => {
                            Swal.showLoading();
                            timerCarga = setInterval(() => {
                                if (porcentaje < 90) {
                                    porcentaje += 10;
                                    const el = document.getElementById('lblPorcentajeDoc');
                                    if (el) el.textContent = porcentaje + '%';
                                }
                            }, 80);
                        }
                    });

                    cambiarEstado(id, nuevoEstado)
                        .then(function (resultado) {
                            clearInterval(timerCarga);
                            const el = document.getElementById('lblPorcentajeDoc');
                            if (el) el.textContent = '100%';

                            setTimeout(function () {
                                if (resultado.ok && resultado.data.success) {
                                    Swal.fire({
                                        icon: 'success',
                                        title: '¡Éxito!',
                                        text: resultado.data.message || 'Estado actualizado correctamente.',
                                        confirmButtonColor: '#00847b',
                                        timer: 1500,
                                        showConfirmButton: false
                                    });
                                    cargarDocentes();
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

            // 2. Botón Bote de Basura 🗑 (Eliminación permanente)
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

                let porcentaje = 0;
                let timerCarga;

                Swal.fire({
                    title: 'Eliminando docente...',
                    html: '<div style="font-size: 1.5rem; font-weight: bold; color: #dc3545; margin-top: 10px;" id="lblPorcentajeDoc">0%</div>',
                    allowOutsideClick: false,
                    allowEscapeKey: false,
                    showConfirmButton: false,
                    didOpen: () => {
                        Swal.showLoading();
                        timerCarga = setInterval(() => {
                            if (porcentaje < 90) {
                                porcentaje += 10;
                                const el = document.getElementById('lblPorcentajeDoc');
                                if (el) el.textContent = porcentaje + '%';
                            }
                        }, 80);
                    }
                });

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
                        clearInterval(timerCarga);
                        const el = document.getElementById('lblPorcentajeDoc');
                        if (el) el.textContent = '100%';

                        setTimeout(function () {
                            if (data && data.success) {
                                Swal.fire({
                                    icon: 'success',
                                    title: '¡Eliminado!',
                                    text: data.message || 'El docente fue eliminado de la base de datos.',
                                    confirmButtonColor: '#00847b'
                                });
                                cargarDocentes();
                            } else {
                                mostrarAlerta('No se pudo eliminar', data.message || 'Ocurrió un error al intentar eliminar el registro.', 'error');
                            }
                        }, 300);
                    })
                    .catch(function (error) {
                        clearInterval(timerCarga);
                        console.error('Error al eliminar el docente:', error);
                        mostrarAlerta('Error de conexión', 'No fue posible comunicarse con el servidor.', 'error');
                    });
            });
        });
    }

    // Cargar la lista al cargar la vista
    cargarDocentes();
});