<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mx.edu.utez.DesarrolloAcademico.model.Usuario" %>

<%
    Usuario dev = (Usuario) request.getAttribute("dev");

    if (dev == null) {
        response.sendRedirect("gestion_desarrolladores_de.jsp");
        return;
    }

    int idUser = dev.getIdUsuario();
    String nombre = dev.getNombre() != null ? dev.getNombre() : "";
    String apePat = dev.getApellidoPaterno() != null ? dev.getApellidoPaterno() : "";
    String apeMat = dev.getApellidoMaterno() != null ? dev.getApellidoMaterno() : "";
    String numEmp = dev.getNumeroEmpleado() != null ? dev.getNumeroEmpleado() : "";
    String tel = dev.getTelefono() != null ? dev.getTelefono() : "";
    String correo = dev.getCorreoInstitucional() != null ? dev.getCorreoInstitucional() : "";
    String pass = dev.getContrasena() != null ? dev.getContrasena() : "";

    int idDivision = 0;
    if (dev.getIdDivision() != null) {
        idDivision = dev.getIdDivision();
    }
%>

<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Desarrollador</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <!-- SweetAlert2 CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="assets/css/coordinador.css">
</head>
<body>

<!-- Sidebar / Navegación -->
<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="gestion_usuarios" />
    <jsp:param name="active_sub" value="desarrollador" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title mb-4">EDITAR DESARROLLADOR</h3>

    <div class="data-card p-4 mb-4">
        <h5 class="mb-4 text-teal">
            <i class="bi bi-info-circle me-2"></i>DATOS DEL DESARROLLADOR
        </h5>

        <form id="formEditarDesarrollador" autocomplete="off">

            <!-- ID oculto -->
            <input type="hidden" name="id" value="<%= idUser %>">

            <div class="row g-3">
                <!-- Nombre del Docente -->
                <div class="col-md-4">
                    <label for="campoNombre" class="form-label">Nombre del Docente * :</label>
                    <input type="text" class="form-control solo-texto" id="campoNombre" name="nombre"
                           value="<%= nombre %>" placeholder="Nombre(s)" required>
                </div>

                <!-- Apellido Paterno (NAME CORREGIDO) -->
                <div class="col-md-4">
                    <label for="campoApellidoPaterno" class="form-label">Apellido Paterno * :</label>
                    <input type="text" class="form-control solo-texto" id="campoApellidoPaterno" name="apellido_paterno"
                           value="<%= apePat %>" placeholder="Apellido" required>
                </div>

                <!-- Apellido Materno (NAME CORREGIDO) -->
                <div class="col-md-4">
                    <label for="campoApellidoMaterno" class="form-label">Apellido Materno * :</label>
                    <input type="text" class="form-control solo-texto" id="campoApellidoMaterno" name="apellido_materno"
                           value="<%= apeMat %>" placeholder="Apellido" required>
                </div>

                <!-- División Académica (NAME CORREGIDO) -->
                <div class="col-md-4">
                    <label for="campoDivision" class="form-label">División Académica * :</label>
                    <select class="form-select" id="campoDivision" name="division" required>
                        <option value="" disabled>Seleccione división</option>
                        <option value="1" <%= (idDivision == 1) ? "selected" : "" %>>Datid</option>
                        <option value="2" <%= (idDivision == 2) ? "selected" : "" %>>Dacea</option>
                        <option value="3" <%= (idDivision == 3) ? "selected" : "" %>>Datefi</option>
                        <option value="4" <%= (idDivision == 4) ? "selected" : "" %>>Dami</option>
                        <option value="5" <%= (idDivision == 5) ? "selected" : "" %>>General</option>
                    </select>
                </div>

                <!-- Número de Empleado (NAME CORREGIDO Y SOLO NÚMEROS) -->
                <div class="col-md-4">
                    <label for="campoNumeroEmpleado" class="form-label">Número de Empleado * :</label>
                    <input type="text" class="form-control solo-numeros" id="campoNumeroEmpleado" name="numero_empleado"
                           value="<%= numEmp %>" placeholder="Num. Empleado" required>
                </div>

                <!-- Número de Teléfono (SOLO NÚMEROS) -->
                <div class="col-md-4">
                    <label for="campoTelefono" class="form-label">Número de Teléfono * :</label>
                    <input type="tel" class="form-control solo-numeros" id="campoTelefono" name="telefono"
                           value="<%= tel %>" placeholder="Teléfono" required>
                </div>

                <!-- Correo Institucional -->
                <div class="col-md-4">
                    <label for="campoCorreo" class="form-label">Correo Institucional * :</label>
                    <input type="email" class="form-control" id="campoCorreo" name="correo"
                           value="<%= correo %>" placeholder="correo@utez.edu.mx" required>
                </div>

                <!-- Contraseña -->
                <!-- Contraseña -->
                <div class="col-md-4">
                    <label for="campoContrasena" class="form-label">Contraseña * :</label>
                    <div class="input-group">
                        <input type="password" class="form-control" id="campoContrasena" name="contrasena"
                               value="<%= pass %>" placeholder="Contraseña" required autocomplete="new-password">
                        <button class="btn btn-outline-secondary" type="button" id="btnTogglePass">
                            <i class="bi bi-eye"></i>
                        </button>
                    </div>
                </div>

                <!-- Confirmar Contraseña -->
                <div class="col-md-4">
                    <label for="campoConfirmarContrasena" class="form-label">Confirmar Contraseña * :</label>
                    <div class="input-group">
                        <input type="password" class="form-control" id="campoConfirmarContrasena" name="confirmar_contrasena"
                               value="<%= pass %>" placeholder="Repite la contraseña" required autocomplete="new-password">
                        <button class="btn btn-outline-secondary" type="button" id="btnToggleConfirmPass">
                            <i class="bi bi-eye"></i>
                        </button>
                    </div>
                </div>

            <!-- Botones -->
            <div class="d-flex justify-content-end gap-2 mt-4">
                <a href="gestion_desarrolladores_de.jsp" class="btn btn-outline-secondary px-4">
                    <i class="bi bi-chevron-left"></i> Volver
                </a>
                <button type="submit" id="btnGuardar" class="btn btn-teal px-4">Guardar</button>
            </div>
            </div>
        </form>
    </div>
</main>

<!-- JS Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const form = document.getElementById('formEditarDesarrollador');

        // 1. RESTRICCIÓN: Solo letras en Nombres y Apellidos (Bloqueo en vivo)
        const inputsTexto = form.querySelectorAll('.solo-texto');
        inputsTexto.forEach(function (input) {
            input.addEventListener('input', function () {
                this.value = this.value.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑ\s]/g, '');
            });
        });

        // 2. RESTRICCIÓN: Solo números en Teléfono y Num. Empleado (Bloqueo en vivo)
        const inputsNumeros = form.querySelectorAll('.solo-numeros');
        inputsNumeros.forEach(function (input) {
            input.addEventListener('input', function () {
                this.value = this.value.replace(/\D/g, '');
            });
        });

        // Mostrar/Ocultar contraseña
        function setupTogglePassword(btnId, inputName) {
            const btn = document.getElementById(btnId);
            const input = form.querySelector('[name="' + inputName + '"]');
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

        // Procesar guardado vía AJAX
        form.addEventListener('submit', function (e) {
            e.preventDefault();

            const correoVal = form.querySelector('[name="correo"]').value.trim();
            const pass = form.querySelector('[name="contrasena"]').value.trim();
            const confirmPass = form.querySelector('[name="confirmar_contrasena"]').value.trim();

            // 3. VALIDACIÓN: Correo Institucional
            if (!correoVal.toLowerCase().endsWith('@utez.edu.mx')) {
                Swal.fire({
                    icon: 'warning',
                    title: 'Correo no válido',
                    text: 'El correo debe terminar estrictamente en @utez.edu.mx',
                    confirmButtonColor: '#00847b'
                });
                return;
            }

            // 4. VALIDACIÓN: Contraseña de 12 a 15 caracteres
            if (pass.length < 12 || pass.length > 15) {
                Swal.fire({
                    icon: 'warning',
                    title: 'Longitud de Contraseña',
                    text: 'La contraseña debe tener entre 12 y 15 caracteres.',
                    confirmButtonColor: '#00847b'
                });
                return;
            }

            if (pass !== confirmPass) {
                Swal.fire({
                    icon: 'warning',
                    title: 'No coinciden',
                    text: 'Las contraseñas ingresadas no son iguales.',
                    confirmButtonColor: '#00847b'
                });
                return;
            }

            Swal.fire({
                title: '¿Guardar cambios?',
                text: "Se actualizará la información del desarrollador.",
                icon: 'question',
                showCancelButton: true,
                confirmButtonColor: '#00847b',
                cancelButtonColor: '#6c757d',
                confirmButtonText: 'Sí, guardar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    const formData = new FormData(form);
                    const params = new URLSearchParams(formData);

                    fetch('<%= request.getContextPath() %>/EditarDesarrollador', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                        },
                        body: params.toString()
                    })
                        .then(res => res.json())
                        .then(data => {
                            if (data.success) {
                                Swal.fire({
                                    icon: 'success',
                                    title: '¡Actualizado!',
                                    text: 'El desarrollador fue modificado correctamente.',
                                    confirmButtonColor: '#00847b'
                                }).then(() => {
                                    window.location.href = 'gestion_desarrolladores_de.jsp';
                                });
                            } else {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Error',
                                    text: data.message || 'Ocurrió un error al guardar los cambios.',
                                    confirmButtonColor: '#00847b'
                                });
                            }
                        })
                        .catch(err => {
                            console.error(err);
                            Swal.fire({
                                icon: 'error',
                                title: 'Error de conexión',
                                text: 'No se pudo conectar con el servidor.',
                                confirmButtonColor: '#00847b'
                            });
                        });
                }
            });
        });
    });
</script>
</body>
</html>