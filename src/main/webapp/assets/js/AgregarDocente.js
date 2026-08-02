const contextPath = window.contextPath || '';
const form = document.getElementById('formAgregarDocente');

if (form) {
    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const btnGuardar = form.querySelector('button[type="submit"]');
        if (btnGuardar) btnGuardar.disabled = true;

        const formData = new URLSearchParams();
        const inputs = form.querySelectorAll('input, select');
        
        // Form validations
        let valid = true;
        let pass1 = '', pass2 = '';
        inputs.forEach(input => {
            if (input.type === 'radio' && !input.checked) return;
            if (input.name) {
                formData.append(input.name, input.value);
            }
            if (input.name === 'contrasena') pass1 = input.value;
            if (input.name === 'confirmar_contrasena') pass2 = input.value;
        });

        if (pass1 !== pass2) {
            Swal.fire({
                icon: 'error',
                title: 'Contraseñas no coinciden',
                text: 'Por favor verifica que la contraseña y la confirmación sean iguales.',
                confirmButtonColor: '#00847b'
            });
            if (btnGuardar) btnGuardar.disabled = false;
            return;
        }

        fetch(contextPath + '/AgregarUsuarioServlet', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: formData.toString()
        })
        .then(res => res.json().then(data => ({ ok: res.ok, data: data })))
        .then(resultado => {
            if (resultado.ok && resultado.data.success) {
                Swal.fire({
                    icon: 'success',
                    title: '¡Registrado con Éxito!',
                    text: resultado.data.message || 'El usuario se ha guardado correctamente en la base de datos.',
                    confirmButtonColor: '#00847b',
                    confirmButtonText: 'Aceptar'
                }).then(function (result) {
                    if (result.isConfirmed) {
                        window.location.href = 'gestion_docente_co.jsp';
                    }
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'No se pudo guardar',
                    text: resultado.data.message || 'Ocurrió un error al guardar.',
                    confirmButtonColor: '#00847b'
                });
                if (btnGuardar) btnGuardar.disabled = false;
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
            if (btnGuardar) btnGuardar.disabled = false;
        });
    });
}