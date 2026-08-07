<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Docente</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="gestion_usuarios" />
    <jsp:param name="active_sub" value="docente" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title">EDITAR DOCENTE</h3>

    <div class="d-flex align-items-center mb-4 mt-4" style="color: var(--teal-main);">
        <i class="bi bi-info-circle me-2 fs-5"></i>
        <h5 class="mb-0 fw-bold">DATOS DEL DOCENTE</h5>
    </div>

    <form id="formEditarDocente" autocomplete="off">
        <input type="hidden" id="campoIdUsuario" name="id_usuario">

        <div class="row mb-4">
            <div class="col-md-4">
                <label for="campoNombre" class="form-label">Nombre del Docente <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" id="campoNombre" name="nombre" required>
            </div>
            <div class="col-md-4">
                <label for="campoApellidoP" class="form-label">Apellido Paterno <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" id="campoApellidoP" name="apellido_paterno" required>
            </div>
            <div class="col-md-4">
                <label for="campoApellidoM" class="form-label">Apellido Materno :</label>
                <input type="text" class="form-control" id="campoApellidoM" name="apellido_materno">
            </div>
        </div>

        <div class="row mb-4">
            <div class="col-md-4">
                <label for="campoDivision" class="form-label">División Académica <span class="text-danger">*</span> :</label>
                <select class="form-select" id="campoDivision" name="division" required>
                    <option value="" disabled selected>Seleccione división</option>
                    <option value="1">Datid</option>
                    <option value="2">Dacea</option>
                    <option value="3">Datefi</option>
                    <option value="4">Dami</option>
                    <option value="5">General</option>
                </select>
            </div>
            <div class="col-md-4">
                <label for="campoNumEmpleado" class="form-label">Número de Empleado <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" id="campoNumEmpleado" name="numero_empleado" required>
            </div>
            <div class="col-md-4">
                <label for="campoTelefono" class="form-label">Número de Teléfono :</label>
                <input type="tel" class="form-control" id="campoTelefono" name="telefono">
            </div>
        </div>

        <div class="row mb-4">
            <div class="col-md-4">
                <label for="campoCorreo" class="form-label">Correo Institucional <span class="text-danger">*</span> :</label>
                <input type="email" class="form-control" id="campoCorreo" name="correo" required>
            </div>

            <!-- Campo Contraseña Actual o Nueva -->
            <div class="col-md-6 mb-3">
                <label for="campoContrasena" class="form-label">Contraseña</label>
                <div class="input-group">
                    <input type="password" class="form-control" id="campoContrasena" name="contrasena" placeholder="Nueva contraseña">
                    <button class="btn btn-outline-secondary" type="button" id="btnTogglePass">
                        <i class="bi bi-eye-slash" id="iconoPass"></i>
                    </button>
                </div>
            </div>

            <!--  CAMPO: Confirmar Contraseña -->
            <div class="col-md-6 mb-3">
                <label for="campoConfirmarContrasena" class="form-label">Confirmar Contraseña</label>
                <div class="input-group">
                    <input type="password" class="form-control" id="campoConfirmarContrasena" name="confirmarContrasena" placeholder="Repite la contraseña">
                    <button class="btn btn-outline-secondary" type="button" id="btnToggleConfirmPass">
                        <i class="bi bi-eye-slash" id="iconoConfirmPass"></i>
                    </button>
                </div>
            </div>

            <div class="d-flex justify-content-end gap-2">
                <a href="gestion_docente_co.jsp" class="btn btn-secondary">Cancelar</a>
                <button type="submit" class="btn btn-primary" style="background-color: var(--teal-main); border: none;">Guardar Cambios</button>
            </div>
    </form>
</main>

<script src="${pageContext.request.contextPath}/assets/js/EditarDocente.js?v=1.0.1"></script>
</body>
</html>