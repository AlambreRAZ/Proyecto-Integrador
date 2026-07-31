<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Desarrollador</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
</head>
<body>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="gestion_usuarios" />
    <jsp:param name="active_sub" value="desarrollador" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title">EDITAR DESARROLLADOR</h3>

    <div class="d-flex align-items-center mb-4 mt-4" style="color: var(--teal-main);">
        <i class="bi bi-info-circle me-2 fs-5"></i>
        <h5 class="mb-0 fw-bold">DATOS DEL DESARROLLADOR</h5>
    </div>

    <form id="formEditarDesarrollador" action="#" method="POST">
        <input type="hidden" id="campoId" name="id" value="">

        <div class="row mb-4">
            <div class="col-md-4">
                <label class="form-label">Nombre del Docente <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" id="campoNombre" name="nombre" placeholder="Nombre(s)" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Apellido Paterno <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" id="campoApellidoPaterno" name="apellido_paterno" placeholder="Apellido" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Apellido Materno <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" id="campoApellidoMaterno" name="apellido_materno" required>
            </div>
        </div>

        <div class="row mb-4">
            <div class="col-md-4">
                <label class="form-label">Division Academica <span class="text-danger">*</span> :</label>
                <select class="form-select" id="campoDivision" name="division" required>
                    <option value="" disabled selected></option>
                    <option value="2">DACEA</option>
                    <option value="4">DAMI</option>
                    <option value="1">DATID</option>
                    <option value="3">DATEFI</option>
                    <option value="5">GENERAL</option>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label">Numero de Empleado <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" id="campoNumeroEmpleado" name="numero_empleado" required placeholder="Num. Empleado">
            </div>
            <div class="col-md-4">
                <label class="form-label">Numero de Telefono <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" id="campoTelefono" name="telefono" required placeholder="Teléfono">
            </div>
        </div>

        <div class="row mb-5">
            <div class="col-md-4">
                <label class="form-label">Correo Institucional<span class="text-danger">*</span> :</label>
                <input type="email" class="form-control" id="campoCorreo" name="correo" placeholder="Correo institucional" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Contraseña <span class="text-danger">*</span> :</label>
                <div class="password-container">
                    <input type="password" class="form-control" name="contrasena" id="pass1" placeholder="Mínimo 8 caracteres" minlength="8" required>
                    <i class="bi bi-eye-fill" onclick="togglePassword('pass1')"></i>
                </div>
            </div>
            <div class="col-md-4">
                <label class="form-label">Confirmar Contraseña <span class="text-danger">*</span> :</label>
                <div class="password-container">
                    <input type="password" class="form-control" name="confirmar_contrasena" id="pass2" placeholder="Repite la contraseña" minlength="8" required>
                    <i class="bi bi-eye-fill" onclick="togglePassword('pass2')"></i>
                </div>
            </div>
        </div>

        <div class="d-flex justify-content-end gap-3 mt-4">
            <a href="gestion_desarrolladores_de.jsp" class="btn btn-outline-teal px-4 py-2 fw-semibold d-flex align-items-center" style="border: 2px solid var(--teal-main); color: var(--teal-main); border-radius: 6px;">
                <i class="bi bi-chevron-left me-2"></i> Volver
            </a>
            <button type="submit" id="btnGuardar" class="btn-teal px-5 py-2" style="border-radius: 6px;">Guardar</button>
        </div>
    </form>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>window.contextPath = '<%= request.getContextPath() %>';</script>
<script src="assets/js/coordinador.js"></script>
<script src="assets/js/EditarDesarrollador.js"></script>
<script>
    function togglePassword(inputId) {
        const input = document.getElementById(inputId);
        const icon = input.nextElementSibling;
        if (input.type === "password") {
            input.type = "text";
            icon.classList.remove("bi-eye-fill");
            icon.classList.add("bi-eye-slash-fill");
        } else {
            input.type = "password";
            icon.classList.remove("bi-eye-slash-fill");
            icon.classList.add("bi-eye-fill");
        }
    }
</script>
</body>
</html>
