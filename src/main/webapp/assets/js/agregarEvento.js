const contextPath = window.contextPath || '';
const form = document.getElementById('formAgregarEvento');
const btnGuardar = document.getElementById('btnGuardar');

form.addEventListener('submit', function (e) {
    e.preventDefault();

    const fechaInicioVal = form.querySelector('[name="fechaInicio"]').value;
    const fechaFinVal = form.querySelector('[name="fechaFin"]').value;

    if (fechaInicioVal && fechaFinVal && fechaFinVal <= fechaInicioVal) {
        Swal.fire({
            icon: 'warning',
            title: 'Fechas inválidas',
            text: 'La fecha de fin debe ser posterior a la fecha de inicio.',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    btnGuardar.disabled = true;

    // Convertir FormData a URLSearchParams para evitar problemas de Multipart
    const formData = new FormData(form);
    const datos = new URLSearchParams();
    for (const pair of formData) {
        datos.append(pair[0], pair[1]);
    }

    fetch(contextPath + '/AgregarEventoCO', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: datos.toString()
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
                    title: '¡Evento Registrado con Éxito!',
                    text: 'El evento se ha guardado correctamente en la base de datos.',
                    confirmButtonColor: '#00847b',
                    confirmButtonText: 'Aceptar'
                }).then(function (result) {
                    if (result.isConfirmed) {
                        window.location.href = 'gestion_evento_co.jsp';
                    }
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'No se pudo guardar el evento',
                    text: resultado.data.message || 'Ocurrió un error al conectar con la base de datos.',
                    confirmButtonColor: '#00847b'
                });
                btnGuardar.disabled = false;
            }
        })
        .catch(function (error) {
            console.error('Error al registrar el evento:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error de conexión',
                text: 'No fue posible comunicarse con el servidor.',
                confirmButtonColor: '#00847b'
            });
            btnGuardar.disabled = false;
        });
});