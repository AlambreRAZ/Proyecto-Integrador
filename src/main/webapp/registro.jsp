<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Crear Cuenta</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/login.css">
</head>
<body class="d-flex align-items-center justify-content-center min-vh-100 body-registro">

<div class="container my-4">
    <div class="row justify-content-center">
        <div class="col-12 col-sm-10 col-md-6 col-lg-4">
            <div class="p-2">

                <div class="text-center mb-4">
                    <div class="mb-2">
                        <i class="bi bi-person-fill-add" style="font-size: 5.5rem; color: #51B9B7;"></i>
                    </div>
                    <h2 class="fw-bold fs-2 texto-primario">Crear Cuenta</h2>
                </div>

                <%-- Alertas de error --%>
                <% String error = (String) request.getAttribute("error");
                   if (error != null && !error.isEmpty()) { %>
                    <div class="alert alert-danger py-2 rounded-3" role="alert">
                        <div class="small"><i class="bi bi-exclamation-triangle-fill me-2"></i><%= error %></div>
                    </div>
                <% } %>

                <form action="registro" method="post">

                    <div class="mb-3">
                        <label for="nombre" class="form-label label-custom">Nombre(s):</label>
                        <div class="input-group">
                            <span class="input-group-text icon-prepend"><i class="bi bi-person"></i></span>
                            <input type="text" class="form-control input-custom" id="nombre" name="nombre" placeholder="Nombre(s)" value="${nombre}" required>
                        </div>
                    </div>

                    <div class="row g-2 mb-3">
                        <div class="col-6">
                            <label for="apellidoPaterno" class="form-label label-custom">Apellido Paterno:</label>
                            <div class="input-group">
                                <span class="input-group-text icon-prepend"><i class="bi bi-person-vcard"></i></span>
                                <input type="text" class="form-control input-custom" id="apellidoPaterno" name="apellidoPaterno" placeholder="Paterno" value="${apellidoPaterno}" required>
                            </div>
                        </div>
                        <div class="col-6">
                            <label for="apellidoMaterno" class="form-label label-custom">Apellido Materno:</label>
                            <div class="input-group">
                                <span class="input-group-text icon-prepend"><i class="bi bi-person-vcard"></i></span>
                                <input type="text" class="form-control input-custom" id="apellidoMaterno" name="apellidoMaterno" placeholder="Materno" value="${apellidoMaterno}" required>
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="NumeroEmpleado" class="form-label label-custom">Número de Empleado:</label>
                        <div class="input-group">
                            <span class="input-group-text icon-prepend">
                                <svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor">
                                    <path d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V4zm2-1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V4a1 1 0 0 0-1-1H2z"/>
                                    <path d="M3 6.5A1.5 1.5 0 0 1 4.5 5h1A1.5 1.5 0 0 1 7 6.5v1A1.5 1.5 0 0 1 5.5 9h-1A1.5 1.5 0 0 1 3 7.5v-1zM9 6.25a.75.75 0 0 1 .75-.75h3.5a.75.75 0 0 1 0 1.5h-3.5A.75.75 0 0 1 9 6.25zM9 9.25a.75.75 0 0 1 .75-.75h3.5a.75.75 0 0 1 0 1.5h-3.5A.75.75 0 0 1 9 9.25zM3.75 11a.75.75 0 0 0 0 1.5h3.5a.75.75 0 0 0 0-1.5h-3.5z"/>
                                </svg>
                            </span>
                            <input type="text" class="form-control input-custom" id="NumeroEmpleado" name="numeroEmpleado" placeholder="Número de Empleado" value="${numeroEmpleado}" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="correoInstitucional" class="form-label label-custom">Correo institucional:</label>
                        <div class="input-group">
                            <span class="input-group-text icon-prepend"><i class="bi bi-envelope"></i></span>
                            <input type="email" class="form-control input-custom" id="correoInstitucional" name="correoInstitucional" placeholder="Correo institucional" value="${correoInstitucional}" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="telefono" class="form-label label-custom">Teléfono:</label>
                        <div class="input-group">
                            <span class="input-group-text icon-prepend"><i class="bi bi-telephone"></i></span>
                            <input type="tel" class="form-control input-custom" id="telefono" name="telefono" placeholder="Teléfono" value="${telefono}" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="division" class="form-label label-custom">División:</label>
                        <select class="form-select select-custom text-muted" id="division" name="division" required>
                            <option value="" disabled ${empty division ? 'selected' : ''} hidden>División</option>
                            <option value="1" ${division == '1' ? 'selected' : ''}>DATID</option>
                            <option value="2" ${division == '2' ? 'selected' : ''}>DACEA</option>
                            <option value="3" ${division == '3' ? 'selected' : ''}>DATEFI</option>
                            <option value="4" ${division == '4' ? 'selected' : ''}>DAMI</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="txtPassword" class="form-label label-custom">Contraseña:</label>
                        <div class="input-group">
                            <span class="input-group-text icon-prepend"><i class="bi bi-lock"></i></span>
                            <input type="password" class="form-control input-custom" id="txtPassword" name="contra" placeholder="Contraseña" required>
                            <i class="bi bi-eye toggle-password-icon" id="togglePassword"></i>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label for="txtConfirmPassword" class="form-label label-custom">Confirmar contraseña:</label>
                        <div class="input-group">
                            <span class="input-group-text icon-prepend"><i class="bi bi-lock"></i></span>
                            <input type="password" class="form-control input-custom" id="txtConfirmPassword" name="confirmContra" placeholder="Confirmar contraseña" required>
                            <i class="bi bi-eye toggle-password-icon" id="toggleConfirmPassword"></i>
                        </div>
                    </div>

                    <div class="d-flex justify-content-center gap-4 mb-4">
                        <div class="form-check">
                            <input class="form-check-input checkbox-border" type="radio" value="docente" id="roleDocente" name="rol" ${rol == 'coordinador' ? '' : 'checked'}>
                            <label class="form-check-label fw-bold text-dark small" for="roleDocente">Docente</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input checkbox-border" type="radio" value="coordinador" id="roleCoordinador" name="rol" ${rol == 'coordinador' ? 'checked' : ''}>
                            <label class="form-check-label fw-bold text-dark small" for="roleCoordinador">Coordinador</label>
                        </div>
                    </div>

                    <button class="btn w-100 mb-3 fw-bold btn-registrarme" type="submit">
                        Registrarme
                    </button>

                    <div class="text-center small">
                        <span class="text-dark">¿Ya te acordaste de tu cuenta? </span>
                        <a href="login.jsp" class="text-decoration-none enlace-login">Inicia sesión aquí</a>
                    </div>
                </form>

            </div>
        </div>
    </div>
</div>

<script src="assets/js/bootstrap.js"></script>
<script src="assets/js/login.js"></script>
</body>
</html>