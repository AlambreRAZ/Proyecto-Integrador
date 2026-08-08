const contextPath = window.contextPath || '';
const formMiCuenta = document.getElementById('formMiCuenta');

document.addEventListener("DOMContentLoaded", () => {
    const inputTelefono = document.getElementById('campoTelefono');

    // -------------------------------------------------------------------------
    // 1. RESTRICCIÓN EN TIEMPO REAL: Solo números y máximo 10 dígitos en Teléfono
    // -------------------------------------------------------------------------
    if (inputTelefono) {
        inputTelefono.addEventListener('input', (e) => {
            // Elimina inmediatamente cualquier caracter que no sea número (0-9)
            e.target.value = e.target.value.replace(/[^0-9]/g, '');
        });
    }

    // -------------------------------------------------------------------------
    // 2. ENVÍO DEL FORMULARIO Y VALIDACIONES
    // -------------------------------------------------------------------------
    if (formMiCuenta) {
        formMiCuenta.addEventListener('submit', function (e) {
            e.preventDefault();

            const inputActual = document.getElementById('passActual');
            const inputNueva = document.getElementById('passNueva');
            const inputConfirm = document.getElementById('passConfirm');

            const telefonoVal = inputTelefono ? inputTelefono.value.trim() : '';
            const passActualVal = inputActual ? inputActual.value.trim() : '';
            const novaPasswordVal = inputNueva ? inputNueva.value.trim() : '';
            const passConfirmVal = inputConfirm ? inputConfirm.value.trim() : '';

            // --- A) VALIDACIÓN DE TELÉFONO (Exactamente 10 dígitos) ---
            if (!/^\d{10}$/.test(telefonoVal)) {
                Swal.fire({
                    icon: 'warning',
                    title: 'Teléfono inválido',
                    text: 'El número de teléfono debe contener exactamente 10 dígitos numéricos.',
                    confirmButtonColor: '#00847b',
                    confirmButtonText: 'Aceptar'
                });
                return;
            }

            // --- B) VALIDACIÓN DE CONTRASEÑA (Solo si intenta cambiarla) ---
            const estaCambiandoPass = novaPasswordVal !== '' || passConfirmVal !== '';

            if (estaCambiandoPass) {
                if (!novaPasswordVal || !passConfirmVal) {
                    Swal.fire({
                        icon: 'warning',
                        title: 'Campos vacíos',
                        text: 'Por favor, llena los campos de la nueva contraseña y su confirmación.',
                        confirmButtonColor: '#00847b',
                        confirmButtonText: 'Aceptar'
                    });
                    return;
                }

                if (novaPasswordVal !== passConfirmVal) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Contraseñas no coinciden',
                        text: 'La nueva contraseña y su confirmación deben ser iguales.',
                        confirmButtonColor: '#00847b',
                        confirmButtonText: 'Aceptar'
                    });
                    return;
                }

                if (novaPasswordVal.length < 12 || novaPasswordVal.length > 15) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Contraseña Inválida',
                        text: 'La nueva contraseña debe tener entre 12 y 15 caracteres.',
                        confirmButtonColor: '#00847b',
                        confirmButtonText: 'Aceptar'
                    });
                    return;
                }
            }

            // --- C) PREPARACIÓN DE DATOS PARA ENVÍO ---
            const btnGuardar = formMiCuenta.querySelector('button[type="submit"]');
            if (btnGuardar) btnGuardar.disabled = true;

            const formData = new URLSearchParams();
            formData.append('telefono', telefonoVal);
            formData.append('passActual', passActualVal);
            formData.append('passNueva', novaPasswordVal);

            // --- D) PRELOADER CON PORCENTAJE PARA GUARDAR CAMBIOS ---
            let porcentaje = 0;
            let timerCarga;

            Swal.fire({
                title: 'Actualizando tu información...',
                html: '<div style="font-size: 1.5rem; font-weight: bold; color: #00847b; margin-top: 10px;" id="lblPorcentajeCuenta">0%</div>',
                allowOutsideClick: false,
                allowEscapeKey: false,
                showConfirmButton: false,
                didOpen: () => {
                    Swal.showLoading();
                    timerCarga = setInterval(() => {
                        if (porcentaje < 90) {
                            porcentaje += 10;
                            const el = document.getElementById('lblPorcentajeCuenta');
                            if (el) el.textContent = porcentaje + '%';
                        }
                    }, 80);
                }
            });

            // --- E) ENVÍO AL SERVLET ---
            fetch(contextPath + '/ActualizarMiCuentaServlet', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
                body: formData.toString()
            })
                .then(res => res.json().then(data => ({ ok: res.ok, data: data })))
                .then(resultado => {
                    clearInterval(timerCarga);
                    const el = document.getElementById('lblPorcentajeCuenta');
                    if (el) el.textContent = '100%';

                    setTimeout(() => {
                        if (resultado.ok && resultado.data.success) {
                            // Si cambió la contraseña, actualizamos visualmente los inputs
                            if (estaCambiandoPass) {
                                if (inputActual) inputActual.value = novaPasswordVal;
                                if (inputNueva) inputNueva.value = '';
                                if (inputConfirm) inputConfirm.value = '';
                            }

                            Swal.fire({
                                icon: 'success',
                                title: '¡Actualizado con Éxito!',
                                text: resultado.data.message || 'Tus datos han sido actualizados correctamente.',
                                confirmButtonColor: '#00847b',
                                confirmButtonText: 'Aceptar'
                            });
                        } else {
                            Swal.fire({
                                icon: 'error',
                                title: 'No se pudo actualizar',
                                text: resultado.data.message || 'Error al actualizar tus datos.',
                                confirmButtonColor: '#00847b',
                                confirmButtonText: 'Aceptar'
                            });
                        }
                    }, 300);
                })
                .catch(err => {
                    clearInterval(timerCarga);
                    console.error('Error:', err);
                    Swal.fire({
                        icon: 'error',
                        title: 'Error de conexión',
                        text: 'No fue posible comunicarse con el servidor.',
                        confirmButtonColor: '#00847b',
                        confirmButtonText: 'Aceptar'
                    });
                })
                .finally(() => {
                    if (btnGuardar) btnGuardar.disabled = false;
                });
        });
    }
});