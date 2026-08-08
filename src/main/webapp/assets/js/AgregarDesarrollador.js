const contextPath = window.contextPath || '';

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById('formAgregarDesarrollador') || document.querySelector('form');
    const btnGuardar = document.getElementById('btnGuardar');

    // -------------------------------------------------------------------------
    // 1. RESTRICCIÓN EN TIEMPO REAL: Solo números en Teléfono y Num. Empleado
    // -------------------------------------------------------------------------
    const inputsNumericos = document.querySelectorAll('#campoTelefono, #campoNumEmpleado, [name="telefono"], [name="numero_empleado"]');
    inputsNumericos.forEach(input => {
        if (input) {
            input.addEventListener('input', (e) => {
                e.target.value = e.target.value.replace(/[^0-9]/g, '');
            });
        }
    });

    // -------------------------------------------------------------------------
    // 2. RESTRICCIÓN EN TIEMPO REAL: Solo letras en Nombres y Apellidos
    // -------------------------------------------------------------------------
    const inputsTexto = document.querySelectorAll('#campoNombre, #campoApellidoP, #campoApellidoM, [name="nombre"], [name="apellido_paterno"], [name="apellido_materno"]');
    inputsTexto.forEach(input => {
        if (input) {
            input.addEventListener('input', (e) => {
                // Elimina cualquier carácter que no sea letra (incluye acentos y ñ) o espacio
                e.target.value = e.target.value.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑ\s]/g, '');
            });
        }
    });

    // -------------------------------------------------------------------------
    // 3. ENVÍO DEL FORMULARIO CON VALIDACIONES COMPLETAS DE FRONTEND
    // -------------------------------------------------------------------------
    if (form) {
        form.addEventListener('submit', function (e) {
            e.preventDefault();

            // Helper para obtener datos limpios
            const getVal = (selector) => {
                const el = form.querySelector(selector);
                return el ? el.value.trim() : '';
            };

            const nombre = getVal('[name="nombre"]') || getVal('#campoNombre');
            const apeP = getVal('[name="apellido_paterno"]') || getVal('#campoApellidoP');
            const apeM = getVal('[name="apellido_materno"]') || getVal('#campoApellidoM');
            const numEmp = getVal('[name="numero_empleado"]') || getVal('#campoNumEmpleado');
            const tel = getVal('[name="telefono"]') || getVal('#campoTelefono');
            const correo = getVal('[name="correo"]') || getVal('#campoCorreo');
            const contrasenaVal = getVal('[name="contrasena"]');
            const confirmarVal = getVal('[name="confirmar_contrasena"]');

            // Expresión regular que admite letras de A-Z, a-z, acentos, espacios y Ñ / ñ
            const soloLetrasRegex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;

            // --- A) VALIDACIÓN DE CAMPOS VACÍOS ---
            if (!nombre || !apeP || !apeM || !numEmp || !tel || !correo || !contrasenaVal || !confirmarVal) {
                mostrarAlerta('Campos incompletos', 'Por favor, llena todos los campos obligatorios del formulario.');
                return;
            }

            // --- B) VALIDACIÓN DE NOMBRES Y APELLIDOS ---
            if (!soloLetrasRegex.test(nombre)) {
                mostrarAlerta('Nombre inválido', 'El nombre no puede contener números ni caracteres especiales.');
                return;
            }
            if (!soloLetrasRegex.test(apeP)) {
                mostrarAlerta('Apellido Paterno inválido', 'El apellido paterno no puede contener números.');
                return;
            }
            if (!soloLetrasRegex.test(apeM)) {
                mostrarAlerta('Apellido Materno inválido', 'El apellido materno no puede contener números.');
                return;
            }

            // --- C) VALIDACIÓN DE NÚMERO DE EMPLEADO ---
            if (!/^\d+$/.test(numEmp)) {
                mostrarAlerta('Número de Empleado inválido', 'El número de empleado solo debe contener dígitos.');
                return;
            }

            // --- D) VALIDACIÓN DE TELÉFONO (Exactamente 10 dígitos) ---
            if (!/^\d{10}$/.test(tel)) {
                mostrarAlerta('Teléfono inválido', 'El teléfono debe ser exactamente de 10 dígitos numéricos.');
                return;
            }

            // --- E) VALIDACIÓN DE CORREO INSTITUCIONAL ---
            if (correo.length > 50) {
                mostrarAlerta('Correo muy largo', 'El correo institucional no debe exceder los 50 caracteres.');
                return;
            }
            if (!correo.toLowerCase().endsWith('@utez.edu.mx')) {
                mostrarAlerta('Correo no institucional', 'El correo debe terminar estrictamente en @utez.edu.mx');
                return;
            }

            // --- F) VALIDACIÓN DE CONTRASEÑA (Entre 12 y 15 caracteres) ---
            if (contrasenaVal.length < 12 || contrasenaVal.length > 15) {
                mostrarAlerta('Contraseña inválida', 'La contraseña debe tener entre 12 y 15 caracteres.');
                return;
            }
            if (contrasenaVal !== confirmarVal) {
                mostrarAlerta('Las contraseñas no coinciden', 'Asegúrate de escribir exactamente la misma contraseña en ambos campos.');
                return;
            }

            // --- G) PREPARACIÓN Y PRELOADER CON PORCENTAJE ---
            if (btnGuardar) btnGuardar.disabled = true;

            const datosForm = new FormData(form);

            let porcentaje = 0;
            let timerCarga;

            // Alerta preloader con el porcentaje
            Swal.fire({
                title: 'Registrando desarrollador...',
                html: '<div style="font-size: 1.5rem; font-weight: bold; color: #00847b; margin-top: 10px;" id="lblPorcentajeDev">0%</div>',
                allowOutsideClick: false,
                allowEscapeKey: false,
                showConfirmButton: false,
                didOpen: () => {
                    Swal.showLoading();

                    // Incremento progresivo hasta llegar al 90%
                    timerCarga = setInterval(() => {
                        if (porcentaje < 90) {
                            porcentaje += 10;
                            const el = document.getElementById('lblPorcentajeDev');
                            if (el) el.textContent = porcentaje + '%';
                        }
                    }, 80);
                }
            });

            // --- H) ENVÍO AJAX AL SERVIDOR ---
            fetch(contextPath + '/AgregarDesarrolladorServlet', {
                method: 'POST',
                body: datosForm
            })
                .then(response => response.json().then(data => ({ ok: response.ok, data })))
                .then(resultado => {
                    // Detenemos el reloj del contador
                    clearInterval(timerCarga);

                    // Forzamos el 100% de manera visual
                    const el = document.getElementById('lblPorcentajeDev');
                    if (el) el.textContent = '100%';

                    // Damos una pausa rápida para que el usuario perciba el 100%
                    setTimeout(() => {
                        if (resultado.ok && resultado.data.success) {
                            Swal.fire({
                                icon: 'success',
                                title: '¡Desarrollador Registrado con Éxito!',
                                text: resultado.data.message || 'El desarrollador se ha guardado correctamente.',
                                confirmButtonColor: '#00847b',
                                confirmButtonText: 'Aceptar'
                            }).then(result => {
                                if (result.isConfirmed) {
                                    window.location.href = 'gestion_desarrolladores_de.jsp';
                                }
                            });
                        } else {
                            mostrarAlerta('No se pudo guardar', resultado.data.message || 'Ocurrió un error en el servidor.');
                            if (btnGuardar) btnGuardar.disabled = false;
                        }
                    }, 300);
                })
                .catch(error => {
                    clearInterval(timerCarga);
                    console.error('Error al registrar:', error);
                    mostrarAlerta('Error de conexión', 'No fue posible comunicarse con el servidor.');
                    if (btnGuardar) btnGuardar.disabled = false;
                });
        });
    }
});

// Función auxiliar para SweetAlerts
function mostrarAlerta(titulo, texto) {
    Swal.fire({
        icon: 'warning',
        title: titulo,
        text: texto,
        confirmButtonColor: '#00847b'
    });
}
