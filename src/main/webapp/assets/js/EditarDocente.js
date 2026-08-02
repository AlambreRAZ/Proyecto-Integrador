const contextPath = window.contextPath || '';
const params = new URLSearchParams(window.location.search);
const idDocente = params.get('id');

const campoIdUsuario = document.getElementById('campoIdUsuario');
const campoNombre = document.getElementById('campoNombre');
const campoApellidoP = document.getElementById('campoApellidoP');
const campoApellidoM = document.getElementById('campoApellidoM');
const campoDivision = document.getElementById('campoDivision');
const campoNumEmpleado = document.getElementById('campoNumEmpleado');
const campoTelefono = document.getElementById('campoTelefono');
const campoCorreo = document.getElementById('campoCorreo');

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

    fetch(contextPath + '/ListarUsuariosServlet')
        .then(res => {
            if (!res.ok) throw new Error('HTTP error ' + res.status);
            return res.json();
        })
        .then(usuarios => {
            const docente = usuarios.find(u => u.id == idDocente);
            if (!docente) {
                if (typeof Swal !== 'undefined') {
                    Swal.fire({
                        icon: 'error',
                        title: 'Usuario no encontrado',
                        text: 'El usuario no existe o no se pudo cargar.',
                        confirmButtonColor: '#00847b'
                    });
                }
                return;
            }

            campoIdUsuario.value = docente.id;
            campoNombre.value = docente.nombre || '';
            campoApellidoP.value = docente.apellidoPaterno || '';
            campoApellidoM.value = docente.apellidoMaterno || '';
            campoNumEmpleado.value = docente.numeroEmpleado || '';
            campoTelefono.value = docente.telefono || '';
            campoCorreo.value = docente.correo || '';

            if (docente.idDivision) {
                campoDivision.value = docente.idDivision;
            }
        })
        .catch(err => {
            console.error('Error al cargar datos:', err);
            if (typeof Swal !== 'undefined') {
                Swal.fire({
                    icon: 'error',
                    title: 'Error de conexión',
                    text: 'No se pudieron cargar los datos del servidor.',
                    confirmButtonColor: '#00847b'
                });
            }
        });
}

// Iniciar carga de datos al abrir la página
cargarDatosDocente();

document.querySelector('form').addEventListener('submit', function (e) {
    e.preventDefault();

    const datos = new URLSearchParams();
    datos.append('id_usuario', campoIdUsuario.value);
    datos.append('nombre', campoNombre.value);
    datos.append('apellido_paterno', campoApellidoP.value);
    datos.append('apellido_materno', campoApellidoM.value);
    datos.append('division', campoDivision.value);
    datos.append('numero_empleado', campoNumEmpleado.value);
    datos.append('telefono', campoTelefono.value);
    datos.append('correo', campoCorreo.value);

    const passActual = document.getElementById('passActual');
    const passNueva = document.getElementById('passNueva');
    const passConfirm = document.getElementById('passConfirm');

    // Validar contraseñas si se llenaron
    const cambiarPass = passActual && passActual.value.trim() !== '';
    if (cambiarPass) {
        if (!passNueva || !passNueva.value || !passConfirm || !passConfirm.value) {
            Swal.fire({ icon: 'warning', title: 'Completa los campos de contraseña', text: 'Si deseas cambiar la contraseña, llena los tres campos.', confirmButtonColor: '#00847b' });
            return;
        }
        if (passNueva.value !== passConfirm.value) {
            Swal.fire({ icon: 'error', title: 'Contraseñas no coinciden', text: 'La nueva contraseña y su confirmación deben ser iguales.', confirmButtonColor: '#00847b' });
            return;
        }
    }

    fetch(contextPath + '/EditarUsuarioServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: datos.toString()
    })
    .then(res => res.json().then(data => ({ ok: res.ok, data: data })))
    .then(resultado => {
        if (!resultado.ok || !resultado.data.success) {
            Swal.fire({ icon: 'error', title: 'Error', text: resultado.data.message || 'Error al actualizar', confirmButtonColor: '#00847b' });
            return;
        }

        // Si no hay cambio de contraseña, terminar aquí
        if (!cambiarPass) {
            Swal.fire({
                icon: 'success',
                title: '¡Docente actualizado con éxito!',
                text: resultado.data.message,
                confirmButtonColor: '#00847b'
            }).then(() => { window.location.href = 'gestion_docente_co.jsp'; });
            return;
        }

        // Cambiar contraseña usando el ID del usuario editado
        const passData = new URLSearchParams();
        passData.append('idUsuarioTarget', campoIdUsuario.value);
        passData.append('passActual', passActual.value);
        passData.append('passNueva', passNueva.value);

        fetch(contextPath + '/CambiarPasswordServlet', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: passData.toString()
        })
        .then(r => r.json())
        .then(passResult => {
            if (passResult.success) {
                Swal.fire({
                    icon: 'success',
                    title: '¡Docente actualizado con éxito!',
                    text: 'Datos y contraseña actualizados correctamente.',
                    confirmButtonColor: '#00847b'
                }).then(() => { window.location.href = 'gestion_docente_co.jsp'; });
            } else {
                Swal.fire({
                    icon: 'warning',
                    title: 'Datos actualizados, pero…',
                    text: 'Los datos se guardaron pero la contraseña actual era incorrecta: ' + (passResult.message || ''),
                    confirmButtonColor: '#00847b'
                }).then(() => { window.location.href = 'gestion_docente_co.jsp'; });
            }
        });
    })
    .catch(err => {
        console.error('Error al guardar:', err);
        Swal.fire({ icon: 'error', title: 'Error de conexión', text: 'Hubo un problema de conexión al guardar.', confirmButtonColor: '#00847b' });
    });
});
