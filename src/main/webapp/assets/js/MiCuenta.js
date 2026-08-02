const contextPath = window.contextPath || '';
const formMiCuenta = document.getElementById('formMiCuenta');

if (formMiCuenta) {
    formMiCuenta.addEventListener('submit', function (e) {
        e.preventDefault();
        
        const passActual = document.getElementById('passActual').value;
        const passNueva = document.getElementById('passNueva').value;
        const passConfirm = document.getElementById('passConfirm').value;
        
        if (!passActual || !passNueva || !passConfirm) {
            Swal.fire({
                icon: 'warning',
                title: 'Campos vacíos',
                text: 'Por favor, llena los campos de contraseña.',
                confirmButtonColor: '#00847b'
            });
            return;
        }
        
        if (passNueva !== passConfirm) {
            Swal.fire({
                icon: 'error',
                title: 'Contraseñas no coinciden',
                text: 'La nueva contraseña y su confirmación deben ser iguales.',
                confirmButtonColor: '#00847b'
            });
            return;
        }

        const btnGuardar = formMiCuenta.querySelector('button[type="submit"]');
        if (btnGuardar) btnGuardar.disabled = true;

        const formData = new URLSearchParams();
        formData.append('passActual', passActual);
        formData.append('passNueva', passNueva);

        fetch(contextPath + '/CambiarPasswordServlet', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: formData.toString()
        })
        .then(res => res.json().then(data => ({ ok: res.ok, data: data })))
        .then(resultado => {
            if (resultado.ok && resultado.data.success) {
                Swal.fire({
                    icon: 'success',
                    title: '¡Actualizado con Éxito!',
                    text: resultado.data.message || 'Tu contraseña ha sido actualizada.',
                    confirmButtonColor: '#00847b',
                    confirmButtonText: 'Aceptar'
                }).then(() => {
                    // Limpiar campos
                    document.getElementById('passActual').value = '';
                    document.getElementById('passNueva').value = '';
                    document.getElementById('passConfirm').value = '';
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'No se pudo actualizar',
                    text: resultado.data.message || 'Error al cambiar contraseña.',
                    confirmButtonColor: '#00847b'
                });
            }
        })
        .catch(err => {
            console.error('Error:', err);
            Swal.fire({
                icon: 'error',
                title: 'Error de conexión',
                text: 'No fue posible comunicarse con el servidor.',
                confirmButtonColor: '#00847b'
            });
        })
        .finally(() => {
            if (btnGuardar) btnGuardar.disabled = false;
        });
    });
}
