const contextPath = window.contextPath || '';
const form = document.getElementById('formAgregarDesarrollador');
const btnGuardar = document.getElementById('btnGuardar');

form.addEventListener('submit', function (e) {
    e.preventDefault();

    const contrasenaVal = form.querySelector('[name="contrasena"]').value;
    const confirmarVal = form.querySelector('[name="confirmar_contrasena"]').value;

    if (contrasenaVal !== confirmarVal) {
        Swal.fire({
            icon: 'warning',
            title: 'Las contraseñas no coinciden',
            text: 'Verifica que ambas contraseñas sean iguales.',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    btnGuardar.disabled = true;

    const datosForm = new FormData(form);

    fetch(contextPath + '/AgregarDesarrolladorServlet', {
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
                    title: '¡Desarrollador Registrado con Éxito!',
                    text: 'El desarrollador se ha guardado correctamente en la base de datos.',
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
                    title: 'No se pudo guardar el desarrollador',
                    text: resultado.data.message || 'Ocurrió un error al conectar con la base de datos.',
                    confirmButtonColor: '#00847b'
                });
                btnGuardar.disabled = false;
            }
        })
        .catch(function (error) {
            console.error('Error al registrar el desarrollador:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error de conexión',
                text: 'No fue posible comunicarse con el servidor.',
                confirmButtonColor: '#00847b'
            });
            btnGuardar.disabled = false;
        });
});