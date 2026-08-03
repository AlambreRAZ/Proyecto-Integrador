<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Iniciar sesión</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
</head>
<body class="d-flex align-items-center justify-content-center vh-100 bg-white" style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;">

<div class="container">
    <div class="row justify-content-center">
        <div class="col-12 col-sm-10 col-md-6 col-lg-4">

            <div class="p-4">

                <div class="text-center mb-4">
                    <img src="assets/img/login_logo.png" alt="Bienvenido" class="mb-2" style="width: 120px; height: 120px; object-fit: contain;">
                    <h2 class="fw-bold fs-1" style="color: #49B2B0;">Bienvenido</h2>
                </div>

                <% String error = (String) request.getAttribute("error");
                   if (error != null && !error.isEmpty()) { %>
                    <div class="alert alert-danger d-flex align-items-center py-2 rounded-3" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                        <div class="small"><%= error %></div>
                    </div>
                <% } %>

                <% String mensaje = (String) request.getAttribute("mensaje");
                   if (mensaje != null && !mensaje.isEmpty()) { %>
                    <div class="alert alert-info d-flex align-items-center py-2 rounded-3" role="alert">
                        <i class="bi bi-info-circle-fill me-2"></i>
                        <div class="small"><%= mensaje %></div>
                    </div>
                <% } %>

                <form action="login" method="post">

                    <div class="mb-3">
                        <label for="txtUsuario" class="form-label fw-bold mb-1 small" style="color: #2B1818;">Correo institucional:</label>
                        <div class="position-relative">
                            <i class="bi bi-person-fill position-absolute text-secondary fs-5" style="left: 15px; top: 50%; transform: translateY(-50%);"></i>
                            <input type="text" class="form-control rounded-3" id="txtUsuario" name="email" placeholder="Correo institucional" required
                                   style="padding: 0.6rem 1rem 0.6rem 2.8rem; border: 1.5px solid #6C5555; background-color: rgba(255, 255, 255, 0.6);">
                        </div>
                    </div>

                    <div class="mb-4">
                        <label for="txtPassword" class="form-label fw-bold mb-1 small" style="color: #2B1818;">Contraseña:</label>
                        <div class="position-relative">
                            <i class="bi bi-lock-fill position-absolute text-secondary fs-5" style="left: 15px; top: 50%; transform: translateY(-50%);"></i>
                            <input type="password" class="form-control rounded-3" id="txtPassword" name="contra" placeholder="Contraseña" required
                                   style="padding: 0.6rem 2.8rem 0.6rem 2.8rem; border: 1.5px solid #6C5555; background-color: rgba(255, 255, 255, 0.6);">
                            <i class="bi bi-eye position-absolute text-secondary fs-5" id="togglePassword" style="right: 15px; top: 50%; transform: translateY(-50%); cursor: pointer;"></i>
                        </div>
                    </div>

                    <button class="btn w-100 mb-3 text-white rounded-3 fw-medium" type="submit"
                            style="background-color: #49B2B0; padding: 0.6rem; font-size: 1.1rem;"
                            onmouseover="this.style.backgroundColor='#3AA19F'"
                            onmouseout="this.style.backgroundColor='#49B2B0'">
                        Iniciar
                    </button>

                    <% String exito = (String) request.getAttribute("mensajeExito");
                        if (exito != null) { %>
                    <div class="alert alert-success d-flex align-items-center py-2 rounded-3" role="alert">
                        <i class="bi bi-check-circle-fill me-2"></i>
                        <div class="small"><%= exito %></div>
                    </div>
                    <% } %>
                    <div class="text-center small">
                        <div class="mb-1">
                            <span class="text-dark">¿No tienes cuenta?</span>
                            <a href="registro.jsp" class="fw-semibold text-decoration-none" style="color: #5B7FFF;" onmouseover="this.className='fw-semibold text-decoration-underline'" onmouseout="this.className='fw-semibold text-decoration-none'">Regístrate</a>
                        </div>
                        <div>
                            <a href="recuperar-contra.jsp" class="text-decoration-none" style="color: #5B7FFF;" onmouseover="this.className='text-decoration-underline'" onmouseout="this.className='text-decoration-none'">Olvidé mi contraseña</a>
                        </div>
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