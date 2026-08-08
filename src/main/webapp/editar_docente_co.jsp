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
</head>
<body>

<jsp:include page="sidebar_co.jsp">
    <jsp:param name="active" value="gestion_docente" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title">EDITAR DOCENTE/COORDINADOR</h3>

    <div class="d-flex align-items-center mb-4 mt-4" style="color: var(--teal-main);">
        <i class="bi bi-info-circle me-2 fs-5"></i>
        <h5 class="mb-0 fw-bold">EDITAR DOCENTE/COORDINADOR</h5>
    </div>

    <form id="formEditarDocente" action="#" method="POST">
        <input type="hidden" name="id_usuario" id="campoIdUsuario">

        <div class="row mb-4">
            <div class="col-md-4">
                <label class="form-label">Nombre del Docente <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" name="nombre" id="campoNombre" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Apellido Paterno <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" name="apellido_paterno" id="campoApellidoP" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Apellido Materno <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" name="apellido_materno" id="campoApellidoM" required>
            </div>
        </div>

        <div class="row mb-4">
            <div class="col-md-4">
                <label class="form-label">División Académica <span class="text-danger">*</span> :</label>

                <!-- Input hidden para enviar la división bloqueada -->
                <input type="hidden" name="division" id="campoDivisionHidden" value="${not empty sessionScope.usuario.idDivision ? sessionScope.usuario.idDivision : sessionScope.idDivision}">

                <!-- Select BLOQUEADO (disabled) con la división del coordinador -->
                <select class="form-select bg-light" id="campoDivision" disabled style="cursor: not-allowed;">
                    <option value="1" ${ (sessionScope.usuario.idDivision == 1 || sessionScope.idDivision == 1) ? 'selected' : '' }>DATID</option>
                    <option value="2" ${ (sessionScope.usuario.idDivision == 2 || sessionScope.idDivision == 2) ? 'selected' : '' }>DACEA</option>
                    <option value="3" ${ (sessionScope.usuario.idDivision == 3 || sessionScope.idDivision == 3) ? 'selected' : '' }>DATEFI</option>
                    <option value="4" ${ (sessionScope.usuario.idDivision == 4 || sessionScope.idDivision == 4) ? 'selected' : '' }>DAMI</option>
                    <option value="5" ${ (sessionScope.usuario.idDivision == 5 || sessionScope.idDivision == 5) ? 'selected' : '' }>GENERAL</option>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label">Número de Empleado <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" name="numero_empleado" id="campoNumEmpleado" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Número de Teléfono <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" name="telefono" id="campoTelefono" required>
            </div>
        </div>

        <div class="row mb-5">
            <div class="col-md-4">
                <label class="form-label">Correo Institucional <span class="text-danger">*</span> :</label>
                <input type="email" class="form-control" name="correo" id="campoCorreo" required>
            </div>
        </div>

        <div class="bg-teal text-white p-2 mb-4 fs-5" style="background-color: var(--teal-main);">
            Cambio de contraseña (Opcional)
        </div>

        <div class="row mb-5">
            <div class="col-md-4">
                <label class="form-label text-muted">Contraseña Actual:</label>
                <div class="input-group">
                    <input type="password" id="passActual" class="form-control bg-white">
                    <span class="input-group-text bg-white border-start-0" onclick="togglePassword('passActual')" style="cursor: pointer;">
                        <i class="bi bi-eye-fill text-muted"></i>
                    </span>
                </div>
            </div>
            <div class="col-md-4">
                <label class="form-label text-muted">Nueva Contraseña:</label>
                <div class="input-group">
                    <input type="password" id="passNueva" class="form-control bg-white">
                    <span class="input-group-text bg-white border-start-0" onclick="togglePassword('passNueva')" style="cursor: pointer;">
                        <i class="bi bi-eye-fill text-muted"></i>
                    </span>
                </div>
            </div>
            <div class="col-md-4">
                <label class="form-label text-muted">Confirmar Nueva Contraseña:</label>
                <div class="input-group">
                    <input type="password" id="passConfirm" class="form-control bg-white">
                    <span class="input-group-text bg-white border-start-0" onclick="togglePassword('passConfirm')" style="cursor: pointer;">
                        <i class="bi bi-eye-fill text-muted"></i>
                    </span>
                </div>
            </div>
        </div>

        <div class="d-flex justify-content-end gap-3">
            <a href="gestion_docente_co.jsp" class="btn btn-outline-teal px-4 py-2 fw-semibold d-flex align-items-center" style="border: 2px solid var(--teal-main); color: var(--teal-main); border-radius: 6px;">
                <i class="bi bi-chevron-left me-2"></i> Volver
            </a>
            <button type="submit" id="btnGuardar" class="btn-teal px-5 py-2" style="border-radius: 6px;">Editar</button>
        </div>
    </form>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/coordinador.js"></script>
<script>
    function togglePassword(inputId) {
        const input = document.getElementById(inputId);
        const icon = input.nextElementSibling ? input.nextElementSibling.querySelector('i') : null;
        if (input.type === "password") {
            input.type = "text";
            if (icon) {
                icon.classList.remove("bi-eye-fill");
                icon.classList.add("bi-eye-slash-fill");
            }
        } else {
            input.type = "password";
            if (icon) {
                icon.classList.remove("bi-eye-slash-fill");
                icon.classList.add("bi-eye-fill");
            }
        }
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>window.contextPath = '<%= request.getContextPath() %>';</script>
<script src="assets/js/EditarDocente.js?v=2"></script>
</body>
</html>