const params = new URLSearchParams(window.location.search);
const idDocente = params.get('id');

// Elementos del DOM
const campoIdUsuario = document.getElementById('campoIdUsuario') || document.getElementById('id_usuario') || document.getElementById('idUsuario');
const campoNombre = document.getElementById('campoNombre') || document.getElementById('nombre');
const campoApellidoP = document.getElementById('campoApellidoP') || document.getElementById('apellidoPaterno') || document.getElementById('apellido_paterno');
const campoApellidoM = document.getElementById('campoApellidoM') || document.getElementById('apellidoMaterno') || document.getElementById('apellido_materno');
const campoDivision = document.getElementById('campoDivision') || document.getElementById('idDivision') || document.getElementById('division');
const campoNumEmpleado = document.getElementById('campoNumEmpleado') || document.getElementById('numeroEmpleado') || document.getElementById('numero_empleado');
const campoTelefono = document.getElementById('campoTelefono') || document.getElementById('telefono');
const campoCorreo = document.getElementById('campoCorreo') || document.getElementById('correoInstitucional') || document.getElementById('correo');
const campoContrasena = document.getElementById('campoContrasena') || document.getElementById('contrasena');
const campoConfirmarContrasena = document.getElementById('campoConfirmarContrasena') || document.getElementById('confirmarContrasena');

// Detectar a qué pantalla de gestión regresar
function obtenerPaginaDestino() {
    const pathActual = window.location.pathname;
    if (pathActual.includes('_de.jsp')) {
        return 'gestion_docente_de.jsp';
    } else if (pathActual.includes('_do.jsp')) {
        return 'gestion_docente_do.jsp';
    }
    return 'gestion_docente_co.jsp';
}

// Llenar formulario con los datos recibidos del Servidor
function llenarFormularioDocente(data) {
    if (!data) return;

    if (campoIdUsuario) campoIdUsuario.value = data.idUsuario || data.id_usuario || '';
    if (campoNombre) campoNombre.value = data.nombre || '';
    if (campoApellidoP) campoApellidoP.value = data.apellidoPaterno || data.apellido_paterno || '';
    if (campoApellidoM) campoApellidoM.value = data.apellidoMaterno || data.apellido_materno || '';
    if (campoDivision) campoDivision.value = data.idDivision || data.division || '';
    if (campoNumEmpleado) campoNumEmpleado.value = data.numeroEmpleado || data.numero_empleado || '';
    if (campoTelefono) campoTelefono.value = data.telefono || '';
    if (campoCorreo) campoCorreo.value = data.correoInstitucional || data.correo || '';

    // Asignar contraseña a ambos campos si viene desde el backend
    if (data.contrasena) {
        if (campoContrasena) campoContrasena.value = data.contrasena;
        if (campoConfirmarContrasena) campoConfirmarContrasena.value = data.contrasena;
    }
}

// Cargar datos iniciales
function cargarDatosDocente() {
    if (!idDocente) {
        if (typeof Swal !== 'undefined') {
            Swal.fire({
                icon: 'error',
                title: 'Falta ID del usuario',
                text: 'Accede a esta página desde la gestión de docentes.',
                confirmButtonColor: '#00847b'
            });
        }
        return;
    }

    fetch('ObtenerDocente?id=' + idDocente, {
        credentials: 'same-origin'
    })
        .then(res => {
            if (res.redirected || (res.url && res.url.includes('login.jsp'))) {
                window.location.href = 'login.jsp';
                return null;
            }
            return res.json();
        })
        .then(data => {
            if (!data) return;

            if (data.success === false) {
                Swal.fire({
                    icon: 'error',
                    title: 'Docente no encontrado',
                    text: data.message || 'No se encontraron datos para el ID: ' + idDocente,
                    confirmButtonColor: '#00847b'
                });
                return;
            }
            llenarFormularioDocente(data);
        })
        .catch(err => {
            console.error('Error al cargar datos:', err);
            if (typeof Swal !== 'undefined') {
                Swal.fire({
                    icon: 'error',
                    title: 'Error de servidor',
                    text: 'No se pudieron cargar los datos del servidor.',
                    confirmButtonColor: '#00847b'
                });
            }
        });
}

// Eventos del DOM
document.addEventListener("DOMContentLoaded", () => {
    cargarDatosDocente();

    // Toggle Contraseña 1
    const btnTogglePass = document.getElementById('btnTogglePass');
    if (btnTogglePass) {
        btnTogglePass.addEventListener('click', (e) => {
            e.preventDefault();
            if (campoContrasena) {
                const iconoPass = document.getElementById('iconoPass');
                if (campoContrasena.type === 'password') {
                    campoContrasena.type = 'text';
                    if (iconoPass) iconoPass.className = 'bi bi-eye';
                } else {
                    campoContrasena.type = 'password';
                    if (iconoPass) iconoPass.className = 'bi bi-eye-slash';
                }
            }
        });
    }

    // Toggle Contraseña 2 (Confirmación)
    const btnToggleConfirmPass = document.getElementById('btnToggleConfirmPass');
    if (btnToggleConfirmPass) {
        btnToggleConfirmPass.addEventListener('click', (e) => {
            e.preventDefault();
            if (campoConfirmarContrasena) {
                const iconoConfirmPass = document.getElementById('iconoConfirmPass');
                if (campoConfirmarContrasena.type === 'password') {
                    campoConfirmarContrasena.type = 'text';
                    if (iconoConfirmPass) iconoConfirmPass.className = 'bi bi-eye';
                } else {
                    campoConfirmarContrasena.type = 'password';
                    if (iconoConfirmPass) iconoConfirmPass.className = 'bi bi-eye-slash';
                }
            }
        });
    }

    // Guardar Cambios
    const btnGuardar = document.getElementById('btnGuardar');
    if (btnGuardar) {
        btnGuardar.addEventListener('click', guardarCambios);
    } else {
        const formEditar = document.getElementById('formEditarDocente') || document.querySelector('form');
        if (formEditar) {
            formEditar.addEventListener('submit', function (e) {
                e.preventDefault();
                guardarCambios(e);
            });
        }
    }
});

// Función Principal con Validaciones
function guardarCambios(e) {
    if (e && e.preventDefault) e.preventDefault();

    const form = document.getElementById('formEditarDocente') || document.querySelector('form');
    if (form && !form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const correoVal = campoCorreo ? campoCorreo.value.trim() : '';
    const passVal = campoContrasena ? campoContrasena.value.trim() : '';
    const confirmPassVal = campoConfirmarContrasena ? campoConfirmarContrasena.value.trim() : '';

    // 🔴 1. VALIDACIÓN: Máximo 50 caracteres para el correo
    if (correoVal.length > 50) {
        Swal.fire({
            icon: 'warning',
            title: 'Correo demasiado largo',
            text: 'El correo institucional no debe exceder los 50 caracteres.',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    // 🔴 2. VALIDACIÓN: Longitud de la contraseña entre 12 y 15 caracteres
    if (passVal.length < 12 || passVal.length > 15) {
        Swal.fire({
            icon: 'warning',
            title: 'Contraseña inválida',
            text: 'La contraseña debe tener entre 12 y 15 caracteres.',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    // 🔴 3. VALIDACIÓN: Que coincidan ambas contraseñas
    if (passVal !== confirmPassVal) {
        Swal.fire({
            icon: 'warning',
            title: 'Las contraseñas no coinciden',
            text: 'Por favor, asegúrate de escribir exactamente la misma contraseña en ambos campos.',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    // Creación de parámetros a enviar
    const datos = new URLSearchParams();

    const idVal = campoIdUsuario ? campoIdUsuario.value : idDocente;
    datos.append('idUsuario', idVal);
    datos.append('id_usuario', idVal);
    datos.append('id', idVal);

    datos.append('nombre', campoNombre ? campoNombre.value : '');

    const apeP = campoApellidoP ? campoApellidoP.value : '';
    datos.append('apellidoPaterno', apeP);
    datos.append('apellido_paterno', apeP);

    const apeM = campoApellidoM ? campoApellidoM.value : '';
    datos.append('apellidoMaterno', apeM);
    datos.append('apellido_materno', apeM);

    const divVal = campoDivision ? campoDivision.value : '';
    datos.append('idDivision', divVal);
    datos.append('division', divVal);

    const numEmp = campoNumEmpleado ? campoNumEmpleado.value : '';
    datos.append('numeroEmpleado', numEmp);
    datos.append('numero_empleado', numEmp);

    datos.append('telefono', campoTelefono ? campoTelefono.value : '');

    datos.append('correoInstitucional', correoVal);
    datos.append('correo', correoVal);

    datos.append('contrasena', passVal);
    datos.append('confirmarContrasena', confirmPassVal);
    datos.append('confirmar_contrasena', confirmPassVal);

    // Enviar Petición
    fetch('EditarDocente', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
        body: datos.toString(),
        credentials: 'same-origin'
    })
        .then(async res => {
            const data = await res.json().catch(() => null);
            if (!res.ok) {
                throw new Error((data && data.message) ? data.message : 'HTTP ' + res.status);
            }
            return data;
        })
        .then(resultado => {
            if (!resultado || !(resultado.success || resultado.ok)) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error al actualizar',
                    text: (resultado && resultado.message) ? resultado.message : 'Ocurrió un problema al guardar los cambios.',
                    confirmButtonColor: '#00847b'
                });
                return;
            }

            Swal.fire({
                icon: 'success',
                title: '¡Docente actualizado con éxito!',
                text: resultado.message || 'Los cambios se guardaron correctamente.',
                confirmButtonColor: '#00847b'
            }).then(() => {
                window.location.href = obtenerPaginaDestino();
            });
        })
        .catch(err => {
            console.error('Error al guardar:', err);
            Swal.fire({
                icon: 'error',
                title: 'Error al guardar',
                text: err.message || 'Hubo un problema al intentar guardar los datos.',
                confirmButtonColor: '#00847b'
            });
        });
}