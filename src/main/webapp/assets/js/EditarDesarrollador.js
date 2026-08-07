document.addEventListener('DOMContentLoaded', function () {
    const contextPath = window.contextPath || '';
    const params = new URLSearchParams(window.location.search);
    const idDesarrollador = params.get('id');

    const form = document.getElementById('formEditarDesarrollador');
    const btnGuardar = document.getElementById('btnGuardar');

    // ------------------------------------------------------------------
    // 🔒 RESTRICCIONES EN TIEMPO REAL (MIENTRAS ESCRIBEN)
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
    //  CARGAR DATOS EN EL FORMULARIO (GET)
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

        console.log("Cargando desarrollador ID:", idDesarrollador);

        fetch(contextPath + '/EditarDesarrollador?id=' + encodeURIComponent(idDesarrollador))
            .then(function (response) {
                return response.json();
            })
            .then(function (data) {
                console.log("Datos obtenidos de la BD:", data);

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

                //  OBTENEMOS LOS CAMPOS DE CONTRASEÑA
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

            const correoVal = form.querySelector('[name="correo"]').value.trim();
            const contrasenaVal = form.querySelector('[name="contrasena"]').value.trim();
            const confirmarVal = form.querySelector('[name="confirmar_contrasena"]').value.trim();

            // 1. VALIDACIÓN DE CORREO INSTITUCIONAL (@utez.edu.mx)
            if (!correoVal.toLowerCase().endsWith('@utez.edu.mx')) {
                Swal.fire({
                    icon: 'warning',
                    title: 'Correo inválido',
                    text: 'El correo debe pertenecer al dominio institucional (@utez.edu.mx).',
                    confirmButtonColor: '#00847b'
                });
                return;
            }

            // 2. VALIDACIÓN DE CONTRASEÑA (ENTRE 12 Y 15 CARACTERES)
            if (contrasenaVal.length > 0) {
                if (contrasenaVal.length < 12 || contrasenaVal.length > 15) {
                    Swal.fire({
                        icon: 'warning',
                        title: 'Longitud de Contraseña',
                        text: 'La contraseña debe tener entre 12 y 15 caracteres.',
                        confirmButtonColor: '#00847b'
                    });
                    return;
                }

                if (contrasenaVal !== confirmarVal) {
                    Swal.fire({
                        icon: 'warning',
                        title: 'Las contraseñas no coinciden',
                        text: 'Verifica que ambas contraseñas sean idénticas.',
                        confirmButtonColor: '#00847b'
                    });
                    return;
                }
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

                // Preparamos los parámetros como URLSearchParams para evitar problemas con MultipartConfig
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
                    })
                    .catch(function (error) {
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