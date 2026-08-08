<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agregar Docente</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">

    <!-- Librería SweetAlert2 requerida por el JS -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="gestion_usuarios" />
    <jsp:param name="active_sub" value="docente" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title">AGREGAR DOCENTE</h3>

    <div class="d-flex align-items-center mb-4 mt-4" style="color: var(--teal-main);">
        <i class="bi bi-info-circle me-2 fs-5"></i>
        <h5 class="mb-0 fw-bold">DATOS DEL DOCENTE</h5>
    </div>

    <form id="formAgregarDocente" action="AgregarUsuarioServlet" method="POST">
        <div class="row mb-4">
            <div class="col-md-4">
                <label class="form-label">Nombre del Docente <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" id="campoNombre" name="nombre" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Apellido Paterno <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" id="campoApellidoP" name="apellido_paterno" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Apellido Materno <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" id="campoApellidoM" name="apellido_materno" required>
            </div>
        </div>

        <div class="row mb-4">
            <div class="col-md-4">
                <label class="form-label">División Académica <span class="text-danger">*</span> :</label>
                <select class="form-select" id="campoDivision" name="division" required>
                    <option value="" disabled selected>Selecciona una opción</option>
                    <option value="DACEA">DACEA</option>
                    <option value="DAMI">DAMI</option>
                    <option value="DATID">DATID</option>
                    <option value="DATEFI">DATEFI</option>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label">Número de Empleado <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" id="campoNumEmpleado" name="numero_empleado" required placeholder="Num. Empleado">
            </div>
            <div class="col-md-4">
                <label class="form-label">Número de Teléfono <span class="text-danger">*</span> :</label>
                <input type="text" class="form-control" id="campoTelefono" name="telefono" required placeholder="Teléfono" maxlength="10">
            </div>
        </div>

        <div class="row mb-4">
            <div class="col-md-4">
                <label class="form-label">Correo Institucional <span class="text-danger">*</span> :</label>
                <input type="email" class="form-control" id="campoCorreo" name="correo" required placeholder="ejemplo@utez.edu.mx">
            </div>
            <div class="col-md-4">
                <label class="form-label">Contraseña <span class="text-danger">*</span> :</label>
                <div class="password-container">
                    <input type="password" class="form-control" name="contrasena" id="pass1" required>
                    <i class="bi bi-eye-fill" onclick="togglePassword('pass1')"></i>
                </div>
            </div>
            <div class="col-md-4">
                <label class="form-label">Confirmar Contraseña <span class="text-danger">*</span> :</label>
                <div class="password-container">
                    <input type="password" class="form-control" name="confirmar_contrasena" id="pass2" required>
                    <i class="bi bi-eye-fill" onclick="togglePassword('pass2')"></i>
                </div>
            </div>
        </div>

        <div class="mb-5">
            <label class="form-label mb-3">Rol <span class="text-danger">*</span> :</label>
            <div class="d-flex gap-3">
                <div class="rol-option-card" id="cardDocente" onclick="selectRol('docente')" style="border: 2px solid var(--teal-main); border-radius: 10px; padding: 15px 30px; cursor: pointer; background: white; text-align: center; min-width: 130px;">
                    <i class="bi bi-person-fill fs-3 d-block mb-2" style="color: var(--teal-main);"></i>
                    <div class="fw-semibold" style="color: var(--teal-main);">Docente</div>
                    <input type="radio" name="rol" value="docente" id="radioDocente" checked style="display:none;">
                </div>
                <div class="rol-option-card" id="cardCoordinador" onclick="selectRol('coordinador')" style="border: 2px solid #ccc; border-radius: 10px; padding: 15px 30px; cursor: pointer; background: white; text-align: center; min-width: 130px;">
                    <i class="bi bi-person-workspace fs-3 d-block mb-2" style="color: #aaa;"></i>
                    <div class="fw-semibold" style="color: #aaa;">Coordinador</div>
                    <input type="radio" name="rol" value="coordinador" id="radioCoordinador" style="display:none;">
                </div>
            </div>
        </div>

        <div class="d-flex justify-content-end gap-3">
            <a href="gestion_docente_de.jsp" class="btn btn-outline-teal px-4 py-2 fw-semibold d-flex align-items-center" style="border: 2px solid var(--teal-main); color: var(--teal-main); border-radius: 6px;">
                <i class="bi bi-chevron-left me-2"></i> Volver
            </a>
            <button type="submit" class="btn-teal px-5 py-2" style="border-radius: 6px;">Agregar</button>
        </div>
    </form>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // ContextPath global para peticiones AJAX en Tomcat
    window.contextPath = "${pageContext.request.contextPath}";

    // Alternar visibilidad de contraseña
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

    // Selección de Rol tipo radio button (Exclusivo)
    function selectRol(rolElegido) {
        const radioDocente = document.getElementById('radioDocente');
        const radioCoordinador = document.getElementById('radioCoordinador');
        const cardDocente = document.getElementById('cardDocente');
        const cardCoordinador = document.getElementById('cardCoordinador');

        const isDocente = rolElegido === 'docente';

        radioDocente.checked = isDocente;
        radioCoordinador.checked = !isDocente;

        // Tarjeta Docente
        cardDocente.style.border = isDocente ? '2px solid var(--teal-main)' : '2px solid #ccc';
        cardDocente.querySelector('i').style.color = isDocente ? 'var(--teal-main)' : '#aaa';
        cardDocente.querySelector('div').style.color = isDocente ? 'var(--teal-main)' : '#aaa';

        // Tarjeta Coordinador
        cardCoordinador.style.border = !isDocente ? '2px solid var(--teal-main)' : '2px solid #ccc';
        cardCoordinador.querySelector('i').style.color = !isDocente ? 'var(--teal-main)' : '#aaa';
        cardCoordinador.querySelector('div').style.color = !isDocente ? 'var(--teal-main)' : '#aaa';
    }
</script>

<script src="assets/js/AgregarDocente.js"></script>
<script src="assets/js/coordinador.js"></script>
</body>
</html>