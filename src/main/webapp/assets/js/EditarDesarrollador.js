const contextPath = window.contextPath || '';
const params = new URLSearchParams(window.location.search);
const idDesarrollador = params.get('id');

const form = document.getElementById('formEditarDesarrollador');
const btnGuardar = document.getElementById('btnGuardar');

const campoId = document.getElementById('campoId');
const campoNombre = document.getElementById('campoNombre');
const campoApellidoPaterno = document.getElementById('campoApellidoPaterno');
const campoApellidoMaterno = document.getElementById('campoApellidoMaterno');
const campoDivision = document.getElementById('campoDivision');
const campoNumeroEmpleado = document.getElementById('campoNumeroEmpleado');
const campoTelefono = document.getElementById('campoTelefono');
const campoCorreo = document.getElementById('campoCorreo');

function cargarDesarrollador() {
    if (!idDesarrollador) {
        Swal.fire({
            icon: 'error',
            title: 'Falta el id del desarrollador',
            text: 'Entra a esta página desde "Gestión de Desarrolladores" para poder editar.',
            confirmButtonColor: '#00847b'
        }).then(function () {
            window.location.href = 'gestion_desarrolladores_de.jsp';
        });
        return;
    }

    fetch(contextPath + '/EditarDesarrollador?id=' + encodeURIComponent(idDesarrollador))
        .then(function (response) { return response.json(); })
        .then(function (data) {
            if (!data.success) {
                Swal.fire({
                    icon: 'error',
                    title: 'No se pudo cargar el desarrollador',
                    text: data.message || 'Ocurrió un error al obtener los datos.',
                    confirmButtonColor: '#00847b'
                });
                return;
            }

            campoId.value = data.id;
            campoNombre.value = data.nombre || '';
            campoApellidoPaterno.value = data.apellidoPaterno || '';
            campoApellidoMaterno.value = data.apellidoMaterno || '';
            campoNumeroEmpleado.value = data.numeroEmpleado || '';
            campoTelefono.value = data.telefono || '';
            campoCorreo.value = data.correo || '';

            if (data.idDivision && campoDivision.querySelector('option[value="' + data.idDivision + '"]')) {
                campoDivision.value = data.idDivision;
            }
        })
        .catch(function (error) {
            console.error('Error al cargar el desarrollador:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error de conexión',
                text: 'No fue posible comunicarse con el servidor.',
                confirmButtonColor: '#00847b'
            });
        });
}

form.addEventListener('submit', function (e) {
    e.preventDefault();

    const contrasenaVal = form.querySelector('[name="contrasena"]').value;
    const confirmarVal = form.querySelector('[name="confirmar_contrasena"]').value;

    if (contrasenaVal.length < 8) {
        Swal.fire({
            icon: 'warning',
            title: 'Contraseña demasiado corta',
            text: 'La contraseña debe tener al menos 8 caracteres.',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    if (contrasenaVal !== confirmarVal) {
        Swal.fire({
            icon: 'warning',
            title: 'Las contraseñas no coinciden',
            text: 'Verifica que ambas contraseñas sean iguales.',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    Swal.fire({
        icon: 'question',
        title: '¿Deseas actualizar este desarrollador?',
        text: 'Se guardarán los cambios realizados en el formulario.',
        showCancelButton: true,
        confirmButtonColor: '#00847b',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, actualizar',
        cancelButtonText: 'Cancelar'
    }).then(function (confirmacion) {
        if (!confirmacion.isConfirmed) {
            return;
        }

        btnGuardar.disabled = true;

        const datosForm = new FormData(form);

        fetch(contextPath + '/EditarDesarrollador', {
            method: 'POST',
            body: datosForm
        })
            .then(function (response) {
                return response.json().then(function (data) {
                    return { ok: response.ok, data: data };
                });
            })
            .then(function (resultado) {
                if (resultado.ok && resultado.data.success) {
                    Swal.fire({
                        icon: 'success',
                        title: '¡Desarrollador Actualizado con Éxito!',
                        text: 'Los cambios se guardaron correctamente.',
                        confirmButtonColor: '#00847b',
                        confirmButtonText: 'Aceptar'
                    }).then(function (result) {
                        if (result.isConfirmed) {
                            window.location.href = 'gestion_desarrolladores_de.jsp';
                        }
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'No se pudo actualizar el desarrollador',
                        text: resultado.data.message || 'Ocurrió un error al conectar con la base de datos.',
                        confirmButtonColor: '#00847b'
                    });
                    btnGuardar.disabled = false;
                }
            })
            .catch(function (error) {
                console.error('Error al actualizar el desarrollador:', error);
                Swal.fire({
                    icon: 'error',
                    title: 'Error de conexión',
                    text: 'No fue posible comunicarse con el servidor.',
                    confirmButtonColor: '#00847b'
                });
                btnGuardar.disabled = false;
            });
    });
});

cargarDesarrollador();