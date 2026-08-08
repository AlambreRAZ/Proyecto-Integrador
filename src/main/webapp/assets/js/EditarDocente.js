const params = new URLSearchParams(window.location.search);
const idDocente = params.get('id');

// Referencias del DOM
const campoIdUsuario = document.getElementById('campoIdUsuario');
const campoNombre = document.getElementById('campoNombre');
const campoApellidoP = document.getElementById('campoApellidoP');
const campoApellidoM = document.getElementById('campoApellidoM');
const campoDivision = document.getElementById('campoDivision');
const campoDivisionHidden = document.getElementById('campoDivisionHidden');
const campoNumEmpleado = document.getElementById('campoNumEmpleado');
const campoTelefono = document.getElementById('campoTelefono');
const campoCorreo = document.getElementById('campoCorreo');

// Campos de contraseña (TRES CAMPOS)
const passActual = document.getElementById('passActual');
const passNueva = document.getElementById('passNueva');
const passConfirm = document.getElementById('passConfirm');

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

function obtenerPaginaDestino() {
    const pathActual = window.location.pathname;
    if (pathActual.includes('_de.jsp')) {
        return 'gestion_docente_de.jsp';
    } else if (pathActual.includes('_do.jsp')) {
        return 'gestion_docente_do.jsp';
    }
    return 'gestion_docente_co.jsp';
}

function llenarFormularioDocente(data) {
    if (!data) return;

    if (campoIdUsuario) campoIdUsuario.value = data.idUsuario || data.id_usuario || data.id || '';
    if (campoNombre) campoNombre.value = data.nombre || '';
    if (campoApellidoP) campoApellidoP.value = data.apellidoPaterno || data.apellido_paterno || '';
    if (campoApellidoM) campoApellidoM.value = data.apellidoMaterno || data.apellido_materno || '';
    if (campoNumEmpleado) campoNumEmpleado.value = data.numeroEmpleado || data.numero_empleado || '';
    if (campoTelefono) campoTelefono.value = data.telefono || '';
    if (campoCorreo) campoCorreo.value = data.correoInstitucional || data.correo || '';

    const divisionId = data.idDivision || data.division;
    if (divisionId && campoDivision) {
        campoDivision.value = divisionId;
        if (campoDivisionHidden) campoDivisionHidden.value = divisionId;
    }
}

function cargarDatosDocente() {
    if (!idDocente) {
        mostrarAlerta('Falta ID del usuario', 'Accede a esta página desde la gestión de docentes.', 'error');
        return;
    }

    fetch('ObtenerDocente?id=' + idDocente, { credentials: 'same-origin' })
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
                mostrarAlerta('Docente no encontrado', data.message || 'No se encontraron datos para el ID: ' + idDocente, 'error');
                return;
            }
            llenarFormularioDocente(data);
        })
        .catch(err => {
            console.error('Error al cargar datos:', err);
            mostrarAlerta('Error de servidor', 'No se pudieron cargar los datos del servidor.', 'error');
        });
}

document.addEventListener("DOMContentLoaded", () => {
    cargarDatosDocente();

    // Validaciones en vivo de entradas de texto
    const inputsTexto = [campoNombre, campoApellidoP, campoApellidoM].filter(Boolean);
    inputsTexto.forEach(input => {
        input.addEventListener('input', (e) => {
            e.target.value = e.target.value.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑ\s]/g, '');
        });
    });

    const inputsNumericos = [campoNumEmpleado, campoTelefono].filter(Boolean);
    inputsNumericos.forEach(input => {
        input.addEventListener('input', (e) => {
            e.target.value = e.target.value.replace(/\D/g, '');
        });
    });

    const formEditar = document.getElementById('formEditarDocente') || document.querySelector('form');
    if (formEditar) {
        formEditar.addEventListener('submit', function (e) {
            e.preventDefault();
            guardarCambios(e);
        });
    }
});

function guardarCambios(e) {
    if (e && e.preventDefault) e.preventDefault();

    const nombreVal = campoNombre ? campoNombre.value.trim() : '';
    const apePVal = campoApellidoP ? campoApellidoP.value.trim() : '';
    const apeMVal = campoApellidoM ? campoApellidoM.value.trim() : '';
    const divisionVal = campoDivisionHidden ? campoDivisionHidden.value.trim() : (campoDivision ? campoDivision.value.trim() : '');
    const numEmpVal = campoNumEmpleado ? campoNumEmpleado.value.trim() : '';
    const telVal = campoTelefono ? campoTelefono.value.trim() : '';
    const correoVal = campoCorreo ? campoCorreo.value.trim() : '';

    // Extracción de Contraseñas
    const passActualVal = passActual ? passActual.value.trim() : '';
    const passNuevaVal = passNueva ? passNueva.value.trim() : '';
    const passConfirmVal = passConfirm ? passConfirm.value.trim() : '';

    // 1. CAMPOS BÁSICOS OBLIGATORIOS
    if (!nombreVal || !apePVal || !apeMVal || !numEmpVal || !telVal || !correoVal) {
        mostrarAlerta('Campos incompletos', 'Por favor, llena todos los campos obligatorios (*).');
        return;
    }

    if (!divisionVal) {
        mostrarAlerta('División requerida', 'No se detectó la división académica.');
        return;
    }

    if (!/^\d+$/.test(numEmpVal)) {
        mostrarAlerta('Número de Empleado inválido', 'El número de empleado solo debe contener dígitos numéricos.');
        return;
    }

    if (!/^\d{10}$/.test(telVal)) {
        mostrarAlerta('Teléfono inválido', 'El teléfono debe ser de exactamente 10 dígitos numéricos.');
        return;
    }

    if (correoVal.length > 50) {
        mostrarAlerta('Correo demasiado largo', 'El correo no debe exceder los 50 caracteres.');
        return;
    }

    if (!correoVal.toLowerCase().endsWith('@utez.edu.mx')) {
        mostrarAlerta('Correo no institucional', 'El correo debe terminar strictly en @utez.edu.mx');
        return;
    }

    // 2. VALIDACIÓN DE CONTRASEÑA (SOLO SI SE ESCRIBE UNA NUEVA CONTRASEÑA)
    if (passNuevaVal !== '') {
        if (passActualVal === '') {
            mostrarAlerta('Contraseña actual requerida', 'Debes ingresar tu contraseña actual para autorizar el cambio de contraseña.');
            return;
        }

        if (passNuevaVal.length < 12 || passNuevaVal.length > 15) {
            mostrarAlerta('Contraseña inválida', 'La nueva contraseña debe tener entre 12 y 15 caracteres.');
            return;
        }

        if (passNuevaVal !== passConfirmVal) {
            mostrarAlerta('Las contraseñas no coinciden', 'Asegúrate de escribir exactamente la misma contraseña en "Nueva Contraseña" y "Confirmar Nueva Contraseña".');
            return;
        }
    }

    // Mapeo completo de parámetros para el Servlet Java
    const datos = new URLSearchParams();

    const idVal = campoIdUsuario ? campoIdUsuario.value : idDocente;
    datos.append('idUsuario', idVal);
    datos.append('id_usuario', idVal);
    datos.append('id', idVal);

    datos.append('nombre', nombreVal);
    datos.append('apellidoPaterno', apePVal);
    datos.append('apellido_paterno', apePVal);
    datos.append('apellidoMaterno', apeMVal);
    datos.append('apellido_materno', apeMVal);

    datos.append('idDivision', divisionVal);
    datos.append('division', divisionVal);

    datos.append('numeroEmpleado', numEmpVal);
    datos.append('numero_empleado', numEmpVal);

    datos.append('telefono', telVal);

    datos.append('correoInstitucional', correoVal);
    datos.append('correo', correoVal);

    // Mandamos la Contraseña Actual
    datos.append('passActual', passActualVal);
    datos.append('contrasenaActual', passActualVal);
    datos.append('contrasena_actual', passActualVal);

    // Mandamos la Contraseña Nueva
    datos.append('contrasena', passNuevaVal);
    datos.append('passNueva', passNuevaVal);
    datos.append('contrasenaNueva', passNuevaVal);
    datos.append('contrasena_nueva', passNuevaVal);

    // Mandamos la Confirmación
    datos.append('confirmarContrasena', passConfirmVal);
    datos.append('confirmar_contrasena', passConfirmVal);
    datos.append('passConfirm', passConfirmVal);

    // PRELOADER Y PETICIÓN AL SERVLET
    Swal.fire({
        icon: 'question',
        title: '¿Deseas guardar los cambios?',
        text: 'Se actualizarán los datos del docente.',
        showCancelButton: true,
        confirmButtonColor: '#00847b',
        cancelButtonColor: '#aaaaaa',
        confirmButtonText: 'Sí, guardar',
        cancelButtonText: 'Cancelar'
    }).then(function (res) {
        if (!res.isConfirmed) return;

        let porcentaje = 0;
        let timerCarga;

        Swal.fire({
            title: 'Actualizando docente...',
            html: '<div style="font-size: 1.8rem; font-weight: bold; color: #00847b; margin-top: 10px;" id="lblPorcentajeEdit">0%</div>',
            allowOutsideClick: false,
            allowEscapeKey: false,
            showConfirmButton: false,
            didOpen: () => {
                Swal.showLoading();
                timerCarga = setInterval(() => {
                    if (porcentaje < 90) {
                        porcentaje += 10;
                        const el = document.getElementById('lblPorcentajeEdit');
                        if (el) el.textContent = porcentaje + '%';
                    }
                }, 80);
            }
        });

        fetch('EditarDocente', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
            body: datos.toString(),
            credentials: 'same-origin'
        })
            .then(async res => {
                const data = await res.json().catch(() => null);
                if (!res.ok) {
                    throw new Error((data && data.message) ? data.message : 'Error al procesar en servidor');
                }
                return data;
            })
            .then(resultado => {
                clearInterval(timerCarga);
                const el = document.getElementById('lblPorcentajeEdit');
                if (el) el.textContent = '100%';

                setTimeout(() => {
                    if (!resultado || !(resultado.success || resultado.ok)) {
                        mostrarAlerta('Error al actualizar', (resultado && resultado.message) ? resultado.message : 'Ocurrió un problema al guardar los cambios.', 'error');
                        return;
                    }

                    if (typeof Swal !== 'undefined') {
                        Swal.fire({
                            icon: 'success',
                            title: '¡Docente actualizado con éxito!',
                            text: resultado.message || 'Los cambios se guardaron correctamente.',
                            confirmButtonColor: '#00847b'
                        }).then(() => {
                            window.location.href = obtenerPaginaDestino();
                        });
                    } else {
                        alert('¡Docente actualizado con éxito!');
                        window.location.href = obtenerPaginaDestino();
                    }
                }, 300);
            })
            .catch(err => {
                clearInterval(timerCarga);
                console.error('Error al guardar:', err);
                mostrarAlerta('Error al guardar', err.message || 'Hubo un problema al intentar guardar los datos.', 'error');
            });
    });
}