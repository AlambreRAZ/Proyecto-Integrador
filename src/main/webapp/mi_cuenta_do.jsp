<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mx.edu.utez.DesarrolloAcademico.model.Usuario" %>
<%
    Usuario u = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
    if (u == null) { response.sendRedirect("login.jsp"); return; }
    String divisionStr = (u.getIdDivision() != null) ? String.valueOf(u.getIdDivision()) : "N/A";
%>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Cuenta – Docente</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
    <style>
        .form-control[readonly] {
            background-color: #e9ecef;
            opacity: 1;
        }
    </style>
</head>
<body>

<jsp:include page="sidebar_do.jsp">
    <jsp:param name="active" value="mi_cuenta" />
</jsp:include>

<main class="main-content">
    <div class="d-flex align-items-center mb-4" style="color: var(--teal-main);">
        <i class="bi bi-info-circle me-2 fs-5"></i>
        <h3 class="page-title mb-0">DOCENTE</h3>
    </div>

    <form id="formMiCuenta">
        <div class="row mb-4">
            <div class="col-md-4">
                <label class="form-label text-muted">Nombre(s):</label>
                <input type="text" class="form-control" value="<%= u.getNombre() %>" readonly>
            </div>
            <div class="col-md-4">
                <label class="form-label text-muted">Apellido Paterno :</label>
                <input type="text" class="form-control" value="<%= u.getApellidoPaterno() %>" readonly>
            </div>
            <div class="col-md-4">
                <label class="form-label text-muted">Apellido Materno :</label>
                <input type="text" class="form-control" value="<%= u.getApellidoMaterno() %>" readonly>
            </div>
        </div>

        <div class="row mb-4">
            <div class="col-md-4">
                <label class="form-label text-muted">Division Academica :</label>
                <input type="text" class="form-control" value="División <%= divisionStr %>" readonly>
            </div>
            <div class="col-md-4">
                <label class="form-label text-muted">Número de Empleado:</label>
                <input type="text" class="form-control" value="<%= u.getNumeroEmpleado() %>" readonly>
            </div>
            <div class="col-md-4">
                <label class="form-label text-muted">Teléfono:</label>
                <input type="text" class="form-control" value="<%= u.getTelefono() %>" readonly>
            </div>
        </div>

        <div class="row mb-5">
            <div class="col-md-4">
                <label class="form-label text-muted">Correo Institucional :</label>
                <input type="text" class="form-control" value="<%= u.getCorreoInstitucional() %>" readonly>
            </div>
        </div>

        <div class="bg-teal text-white p-2 mb-4 fs-5" style="background-color: var(--teal-main);">
            Cambio de contraseña
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
                <label class="form-label text-muted">Confirmar Contraseña:</label>
                <div class="input-group">
                    <input type="password" id="passConfirm" class="form-control bg-white">
                    <span class="input-group-text bg-white border-start-0" onclick="togglePassword('passConfirm')" style="cursor: pointer;">
                        <i class="bi bi-eye-fill text-muted"></i>
                    </span>
                </div>
            </div>
        </div>

        <div class="d-flex justify-content-end gap-3">
            <a href="vista_general_docente_do.jsp" class="btn btn-outline-teal px-4 py-2 fw-semibold d-flex align-items-center" style="border: 2px solid var(--teal-main); color: var(--teal-main); border-radius: 6px;">
                <i class="bi bi-chevron-left me-2"></i> Volver
            </a>
            <button type="submit" class="btn-teal px-4 py-2" style="border-radius: 6px;">
                <i class="bi bi-save me-2"></i> Actualizar Contraseña
            </button>
        </div>
    </form>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/coordinador.js"></script>
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
<script>window.contextPath = '<%= request.getContextPath() %>';</script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="assets/js/MiCuenta.js"></script>
</body>
</html>
