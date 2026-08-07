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
    <title>Detalles del Desarrollador</title>
    <!-- CSS Dependencies -->
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
</head>
<body>

<!-- Sidebar / Navegación -->
<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="gestion_usuarios" />
    <jsp:param name="active_sub" value="desarrollador" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title mb-4">DETALLES DEL DESARROLLADOR</h3>

    <div class="data-card p-4 mb-4">
        <h5 class="mb-4 text-teal">
            <i class="bi bi-person-lines-fill me-2"></i>INFORMACIÓN DEL DESARROLLADOR
        </h5>

        <form autocomplete="off">
            <div class="row g-3">
                <!-- Nombre del Docente -->
                <div class="col-md-4">
                    <label class="form-label fw-bold">Nombre del Docente :</label>
                    <input type="text" class="form-control" value="<%= nombre %>" disabled>
                </div>

                <!-- Apellido Paterno -->
                <div class="col-md-4">
                    <label class="form-label fw-bold">Apellido Paterno :</label>
                    <input type="text" class="form-control" value="<%= apePat %>" disabled>
                </div>

                <!-- Apellido Materno -->
                <div class="col-md-4">
                    <label class="form-label fw-bold">Apellido Materno :</label>
                    <input type="text" class="form-control" value="<%= apeMat %>" disabled>
                </div>

                <!-- División Académica -->
                <div class="col-md-4">
                    <label class="form-label fw-bold">División Académica :</label>
                    <select class="form-select" disabled>
                        <option value="1" <%= (idDivision == 1) ? "selected" : "" %>>Datid</option>
                        <option value="2" <%= (idDivision == 2) ? "selected" : "" %>>Dacea</option>
                        <option value="3" <%= (idDivision == 3) ? "selected" : "" %>>Datefi</option>
                        <option value="4" <%= (idDivision == 4) ? "selected" : "" %>>Dami</option>
                        <option value="5" <%= (idDivision == 5) ? "selected" : "" %>>General</option>
                    </select>
                </div>

                <!-- Número de Empleado -->
                <div class="col-md-4">
                    <label class="form-label fw-bold">Número de Empleado :</label>
                    <input type="text" class="form-control" value="<%= numEmp %>" disabled>
                </div>

                <!-- Número de Teléfono -->
                <div class="col-md-4">
                    <label class="form-label fw-bold">Número de Teléfono :</label>
                    <input type="text" class="form-control" value="<%= tel %>" disabled>
                </div>

                <!-- Correo Institucional -->
                <div class="col-md-4">
                    <label class="form-label fw-bold">Correo Institucional :</label>
                    <input type="email" class="form-control" value="<%= correo %>" disabled>
                </div>


                <div class="col-md-4">
                    <label for="campoContrasenaVer" class="form-label fw-bold">Contraseña :</label>
                    <div class="input-group">
                        <input type="password" class="form-control" id="campoContrasenaVer"
                               value="<%= pass %>" readonly disabled style="background-color: #e9ecef;">
                        <button class="btn btn-outline-secondary" type="button" id="btnTogglePassVer">
                            <i class="bi bi-eye"></i>
                        </button>
                    </div>
                </div>
            </div>s
            <!-- Botones -->
            <div class="d-flex justify-content-end gap-2 mt-4">
                <a href="gestion_desarrolladores_de.jsp" class="btn btn-outline-secondary px-4">
                    <i class="bi bi-chevron-left"></i> Volver
                </a>
            </div>
        </form>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.getElementById('btnTogglePass').addEventListener('click', function () {
        const input = document.getElementById('campoPass');
        const icon = this.querySelector('i');
        if (input.type === 'password') {
            input.type = 'text';
            icon.classList.replace('bi-eye', 'bi-eye-slash');
        } else {
            input.type = 'password';
            icon.classList.replace('bi-eye-slash', 'bi-eye');
        }
    });
</script>
</body>
</html>