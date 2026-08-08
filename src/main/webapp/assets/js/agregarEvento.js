const contextPath = window.contextPath || '';
const form = document.getElementById('formAgregarEvento');
const btnGuardar = document.getElementById('btnGuardar');

if (form) {
    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const fechaInicioVal = form.querySelector('[name="fechaInicio"]')?.value;
        const fechaFinVal = form.querySelector('[name="fechaFin"]')?.value;

        // --- VALIDACIÓN DE FECHAS ---
        if (fechaInicioVal && fechaFinVal && fechaFinVal <= fechaInicioVal) {
            Swal.fire({
                icon: 'warning',
                title: 'Fechas inválidas',
                text: 'La fecha de fin debe ser posterior a la fecha de inicio.',
                confirmButtonColor: '#00847b'
            });
            return;
        }

        if (btnGuardar) btnGuardar.disabled = true;

        // --- PREPARACIÓN DEL PRELOADER CON PORCENTAJE ---
        let porcentaje = 0;
        let timerCarga;

        Swal.fire({
            title: 'Guardando evento...',
            html: '<div style="font-size: 1.5rem; font-weight: bold; color: #00847b; margin-top: 10px;" id="lblPorcentajeEvento">0%</div>',
            allowOutsideClick: false,
            allowEscapeKey: false,
            showConfirmButton: false,
            didOpen: () => {
                Swal.showLoading();
                // Incremento progresivo hasta 90%
                timerCarga = setInterval(() => {
                    if (porcentaje < 90) {
                        porcentaje += 10;
                        const el = document.getElementById('lblPorcentajeEvento');
                        if (el) el.textContent = porcentaje + '%';
                    }
                }, 80);
            }
        });

        // Convertir FormData a URLSearchParams para evitar problemas de Multipart
        const formData = new FormData(form);
        const datos = new URLSearchParams();
        for (const pair of formData) {
            datos.append(pair[0], pair[1]);
        }

        // --- PETICIÓN FETCH ---
        fetch(contextPath + '/AgregarEventoCO', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
            body: datos.toString()
        })
            .then(function (response) {
                return response.json().then(function (data) {
                    return { ok: response.ok, data: data };
                });
            })
            .then(function (resultado) {
                // Detenemos el temporizador
                clearInterval(timerCarga);

                // Forzamos el 100% de carga visual
                const el = document.getElementById('lblPorcentajeEvento');
                if (el) el.textContent = '100%';

                setTimeout(function () {
                    if (resultado.ok && resultado.data.success) {
                        Swal.fire({
                            icon: 'success',
                            title: '¡Evento Registrado con Éxito!',
                            text: resultado.data.message || 'El evento se ha guardado correctamente en la base de datos.',
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
                        if (btnGuardar) btnGuardar.disabled = false;
                    }
                }, 300);
            })
            .catch(function (error) {
                clearInterval(timerCarga);
                console.error('Error al registrar el evento:', error);
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