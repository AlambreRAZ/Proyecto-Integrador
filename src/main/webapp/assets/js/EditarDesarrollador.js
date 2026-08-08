document.addEventListener('DOMContentLoaded', function () {
    const contextPath = window.contextPath || '';
    const params = new URLSearchParams(window.location.search);
    const idDesarrollador = params.get('id');

    const form = document.getElementById('formEditarDesarrollador');
    const btnGuardar = document.getElementById('btnGuardar');

    // Función auxiliar para mostrar alertas rápidamente
    function mostrarAlerta(titulo, mensaje, icono = 'warning') {
        Swal.fire({
            icon: icono,
            title: titulo,
            text: mensaje,
            confirmButtonColor: '#00847b'
        });
    }

    // ------------------------------------------------------------------
    //  RESTRICCIONES EN TIEMPO REAL (MIENTRAS ESCRIBEN)
    // ------------------------------------------------------------------

    // Solo letras y espacios (Nombres y Apellidos)
    const inputsSoloTexto = form ? form.querySelectorAll('#campoNombre, #campoApellidoPaterno, #campoApellidoMaterno') : [];
    inputsSoloTexto.forEach(function (input) {
        input.addEventListener('input', function () {
            // Elimina cualquier carácter que no sea letra (incluye acentos y ñ) o espacio
            this.value = this.value.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑ\s]/g, '');
        });
    });

    // Solo números (Teléfono y Número de Empleado)
    const inputsSoloNumeros = form ? form.querySelectorAll('#campoTelefono, #campoNumeroEmpleado') : [];
    inputsSoloNumeros.forEach(function (input) {
        input.addEventListener('input', function () {
            // Elimina todo lo que no sea un número (0-9)
            this.value = this.value.replace(/\D/g, '');
        });
    });

    // ------------------------------------------------------------------
    //  CARGAR DATOS EN EL FORMULARIO (GET)
    // ------------------------------------------------------------------
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
            .then(function (response) {
                return response.json();
            })
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

                // Llenado dinámico de cada campo por su ID
                const campoId = document.getElementById('campoId');
                const campoNombre = document.getElementById('campoNombre');
                const campoApellidoPaterno = document.getElementById('campoApellidoPaterno');
                const campoApellidoMaterno = document.getElementById('campoApellidoMaterno');
                const campoDivision = document.getElementById('campoDivision');
                const campoNumeroEmpleado = document.getElementById('campoNumeroEmpleado');
                const campoTelefono = document.getElementById('campoTelefono');
                const campoCorreo = document.getElementById('campoCorreo');

                // OBTENEMOS LOS CAMPOS DE CONTRASEÑA
                const campoContrasena = document.getElementById('campoContrasena');
                const campoConfirmarContrasena = document.getElementById('campoConfirmarContrasena');

                if (campoId) campoId.value = data.id || '';
                if (campoNombre) campoNombre.value = data.nombre || '';
                if (campoApellidoPaterno) campoApellidoPaterno.value = data.apellidoPaterno || '';
                if (campoApellidoMaterno) campoApellidoMaterno.value = data.apellidoMaterno || '';
                if (campoNumeroEmpleado) campoNumeroEmpleado.value = data.numeroEmpleado || '';
                if (campoTelefono) campoTelefono.value = data.telefono || '';
                if (campoCorreo) campoCorreo.value = data.correo || '';

                // PINTAMOS LA CONTRASEÑA ACTUAL EN AMBOS CAMPOS
                if (campoContrasena) campoContrasena.value = data.contrasena || '';
                if (campoConfirmarContrasena) campoConfirmarContrasena.value = data.contrasena || '';

                if (campoDivision && data.idDivision !== undefined && data.idDivision !== null) {
                    campoDivision.value = String(data.idDivision);
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

    // ------------------------------------------------------------------
    // 👁️ ALTERNAR VISIBILIDAD DE CONTRASEÑA
    // ------------------------------------------------------------------
    function setupTogglePassword(btnId, inputName) {
        const btn = document.getElementById(btnId);
        const input = form ? form.querySelector('[name="' + inputName + '"]') : null;
        if (!btn || !input) return;

        btn.addEventListener('click', function () {
            const icon = btn.querySelector('i');
            if (input.type === 'password') {
                input.type = 'text';
                icon.classList.replace('bi-eye', 'bi-eye-slash');
            } else {
                input.type = 'password';
                icon.classList.replace('bi-eye-slash', 'bi-eye');
            }
        });
    }

    setupTogglePassword('btnTogglePass', 'contrasena');
    setupTogglePassword('btnToggleConfirmPass', 'confirmar_contrasena');

    // ------------------------------------------------------------------
    //  ENVÍO Y VALIDACIONES FINALES DEL FORMULARIO (POST)
    // ------------------------------------------------------------------
    if (form) {
        form.addEventListener('submit', function (e) {
            e.preventDefault();

            // Función auxiliar para obtener valor por ID o por atributo Name
            const getVal = (id, nameAttr) => {
                const el = document.getElementById(id) || form.querySelector(`[name="${nameAttr}"]`);
                return el ? el.value.trim() : '';
            };

            const nombre = getVal('campoNombre', 'nombre');
            const apeP = getVal('campoApellidoPaterno', 'apellido_paterno');
            const apeM = getVal('campoApellidoMaterno', 'apellido_materno');
            const division = getVal('campoDivision', 'division');
            const numEmp = getVal('campoNumeroEmpleado', 'numero_empleado');
            const tel = getVal('campoTelefono', 'telefono');
            const correoVal = getVal('campoCorreo', 'correo');
            const contrasenaVal = getVal('campoContrasena', 'contrasena');
            const confirmarVal = getVal('campoConfirmarContrasena', 'confirmar_contrasena');

            // 1. VALIDACIÓN DE CAMPOS INCOMPLETOS
            if (!nombre || !apeP || !apeM || !numEmp || !tel || !correoVal || !contrasenaVal || !confirmarVal) {
                mostrarAlerta('Campos incompletos', 'Por favor, llena todos los campos obligatorios del formulario.');
                return;
            }

            // 2. VALIDACIÓN DE DIVISIÓN
            if (!division) {
                mostrarAlerta('División requerida', 'Por favor selecciona una División Académica.');
                return;
            }

            // 3. VALIDACIÓN DE NÚMERO DE EMPLEADO
            if (!/^\d+$/.test(numEmp)) {
                mostrarAlerta('Número de Empleado inválido', 'El número de empleado solo debe contener dígitos numéricos.');
                return;
            }

            // 4. VALIDACIÓN DE TELÉFONO (EXACTAMENTE 10 DÍGITOS)
            if (!/^\d{10}$/.test(tel)) {
                mostrarAlerta('Teléfono inválido', 'El teléfono debe ser de exactamente 10 dígitos numéricos.');
                return;
            }

            // 5. VALIDACIÓN DE LONGITUD DE CORREO
            if (correoVal.length > 50) {
                mostrarAlerta('Correo demasiado largo', 'El correo institucional no debe exceder los 50 caracteres.');
                return;
            }

            // 6. VALIDACIÓN DE CORREO INSTITUCIONAL (@utez.edu.mx)
            if (!correoVal.toLowerCase().endsWith('@utez.edu.mx')) {
                mostrarAlerta('Correo no institucional', 'El correo debe terminar strictly en @utez.edu.mx');
                return;
            }

            // 7. VALIDACIÓN DE CONTRASEÑA (ENTRE 12 Y 15 CARACTERES Y COINCIDENCIA)
            if (contrasenaVal.length < 12 || contrasenaVal.length > 15) {
                mostrarAlerta('Contraseña inválida', 'La contraseña debe tener entre 12 y 15 caracteres.');
                return;
            }

            if (contrasenaVal !== confirmarVal) {
                mostrarAlerta('Las contraseñas no coinciden', 'Asegúrate de escribir exactamente la misma contraseña en ambos campos.');
                return;
            }

            // Confirmación antes de enviar
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
                if (!confirmacion.isConfirmed) return;

                if (btnGuardar) btnGuardar.disabled = true;

                // --- PRELOADER CON PORCENTAJE SIMULADO ---
                let porcentaje = 0;
                let timerCarga;

                Swal.fire({
                    title: 'Actualizando desarrollador...',
                    html: '<div style="font-size: 1.5rem; font-weight: bold; color: #00847b; margin-top: 10px;" id="lblPorcentajeEditDev">0%</div>',
                    allowOutsideClick: false,
                    allowEscapeKey: false,
                    showConfirmButton: false,
                    didOpen: () => {
                        Swal.showLoading();
                        // Incrementa progresivamente hasta llegar al 90%
                        timerCarga = setInterval(() => {
                            if (porcentaje < 90) {
                                porcentaje += 10;
                                const el = document.getElementById('lblPorcentajeEditDev');
                                if (el) el.textContent = porcentaje + '%';
                            }
                        }, 80);
                    }
                });

                // Preparamos los parámetros como URLSearchParams
                const datosForm = new FormData(form);
                const paramsForm = new URLSearchParams(datosForm);

                fetch(contextPath + '/EditarDesarrollador', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                    },
                    body: paramsForm.toString()
                })
                    .then(function (response) {
                        return response.json().then(function (data) {
                            return { ok: response.ok, data: data };
                        });
                    })
                    .then(function (resultado) {
                        // Limpiamos el temporizador
                        clearInterval(timerCarga);

                        // Forzamos la visualización del 100%
                        const el = document.getElementById('lblPorcentajeEditDev');
                        if (el) el.textContent = '100%';

                        setTimeout(function () {
                            if (resultado.ok && resultado.data.success) {
                                Swal.fire({
                                    icon: 'success',
                                    title: '¡Desarrollador Actualizado con Éxito!',
                                    text: resultado.data.message || 'Los cambios se guardaron correctamente.',
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
                                    title: 'No se pudo actualizar',
                                    text: resultado.data.message || 'Ocurrió un error al conectar con la base de datos.',
                                    confirmButtonColor: '#00847b'
                                });
                                if (btnGuardar) btnGuardar.disabled = false;
                            }
                        }, 300);
                    })
                    .catch(function (error) {
                        clearInterval(timerCarga);
                        console.error('Error al actualizar:', error);
                        Swal.fire({
                            icon: 'error',
                            title: 'Error de conexión',
                            text: 'No fue posible comunicarse con el servidor.',
                            confirmButtonColor: '#00847b'
                        });
                        if (btnGuardar) btnGuardar.disabled = false;
                    });
            });
        });
    }

    // Cargar los datos al abrir la ventana
    cargarDesarrollador();
});