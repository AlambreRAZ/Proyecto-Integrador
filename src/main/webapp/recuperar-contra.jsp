<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String step = (String) request.getAttribute("step");
  if (step == null) step = "solicitar";

  String error = (String) request.getAttribute("mensajeError");
  String info = (String) request.getAttribute("mensajeInfo");
%>
<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Recuperación de Contraseña</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bi/bootstrap-icons.css">
</head>
<body class="d-flex align-items-center justify-content-center min-vh-100 py-4 bg-white">

<div class="container">
  <div class="row justify-content-center">
    <div class="col-12 col-sm-10 col-md-6 col-lg-4">
      <div class="p-4">
        
        <!-- Mensajes -->
        <% if (error != null) { %>
          <div class="alert alert-danger d-flex align-items-center py-2 rounded-3" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>
            <div class="small"><%= error %></div>
          </div>
        <% } %>

        <% if (info != null) { %>
          <div class="alert alert-info d-flex align-items-center py-2 rounded-3" role="alert">
            <i class="bi bi-info-circle-fill me-2"></i>
            <div class="small"><%= info %></div>
          </div>
        <% } %>

        <!-- PASO 1: SOLICITAR -->
        <% if ("solicitar".equals(step)) { %>
        <div class="text-center mb-4">
          <img src="${pageContext.request.contextPath}/assets/img/logo_recu.png" alt="Recuperar" class="img-fluid" style="max-width: 100px; height: auto;">
          <h2 class="fw-bold mb-3" style="font-size: 2rem; color: #4cbab8;">Recuperar contraseña</h2>
          <p class="text-dark">Ingresa el correo electrónico o usuario con el que te registraste</p>
        </div>

        <form action="recuperar" method="post">
          <input type="hidden" name="action" value="solicitar">
          <div class="mb-3">
            <label for="txtDato" class="form-label fw-bold text-dark mb-1 small">Correo o Username:</label>
            <div class="input-group">
              <span class="input-group-text bg-transparent border-end-0 text-secondary py-2"><i class="bi bi-person"></i></span>
              <input type="text" class="form-control border-start-0 py-2" id="txtDato" name="dato" placeholder="Ingresa tu correo o usuario" required>
            </div>
          </div>
          <button class="btn w-100 py-2 rounded-3 text-white fw-medium" type="submit" style="background-color: #4cbab8;">
            Enviar código
          </button>
        </form>
        <% } %>

        <!-- PASO 2: VERIFICAR CÓDIGO -->
        <% if ("verificar".equals(step)) { %>
        <div class="text-center mb-4">
          <h2 class="fw-bold mb-3" style="font-size: 2rem; color: #4cbab8;">Ingresar código</h2>
          <p class="text-dark">Introduce el código de 6 caracteres enviado a tu correo.</p>
        </div>

        <form action="recuperar" method="post">
          <input type="hidden" name="action" value="verificar">
          <div class="mb-3">
            <label for="txtCodigo" class="form-label fw-bold text-dark mb-1 small">Código de seguridad:</label>
            <div class="input-group">
              <span class="input-group-text bg-transparent border-end-0 text-secondary py-2"><i class="bi bi-key"></i></span>
              <input type="text" class="form-control border-start-0 py-2" id="txtCodigo" name="codigo" maxlength="6" placeholder="Ej. A1B2C3" required style="text-transform: uppercase;">
            </div>
          </div>
          <button class="btn w-100 py-2 rounded-3 text-white fw-medium" type="submit" style="background-color: #4cbab8;">
            Verificar código
          </button>
        </form>
        <% } %>

        <!-- PASO 3: CAMBIAR CONTRASEÑA -->
        <% if ("cambiar".equals(step)) { %>
        <div class="text-center mb-4">
          <h2 class="fw-bold mb-3" style="font-size: 2rem; color: #4cbab8;">Nueva contraseña</h2>
          <p class="text-dark">Ingresa tu nueva contraseña y confírmala.</p>
        </div>

        <form action="recuperar" method="post">
          <input type="hidden" name="action" value="cambiar">
          <div class="mb-3">
            <label class="form-label fw-bold text-dark mb-1 small">Nueva contraseña:</label>
            <div class="input-group">
              <span class="input-group-text bg-transparent border-end-0 text-secondary py-2"><i class="bi bi-lock"></i></span>
              <input type="password" class="form-control border-start-0 py-2" name="pass1" placeholder="Nueva contraseña" required>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-label fw-bold text-dark mb-1 small">Confirmar contraseña:</label>
            <div class="input-group">
              <span class="input-group-text bg-transparent border-end-0 text-secondary py-2"><i class="bi bi-lock"></i></span>
              <input type="password" class="form-control border-start-0 py-2" name="pass2" placeholder="Repite la contraseña" required>
            </div>
          </div>
          <button class="btn w-100 py-2 rounded-3 text-white fw-medium" type="submit" style="background-color: #4cbab8;">
            Actualizar contraseña
          </button>
        </form>
        <% } %>

        <div class="text-center small mt-3">
          <span class="text-dark">¿Ya te acordaste de tu cuenta? </span>
          <a href="login.jsp" class="text-decoration-none fw-semibold" style="color: #4ea9e6;">
            Inicia sesión aquí
          </a>
        </div>

      </div>
    </div>
  </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
</body>
</html>