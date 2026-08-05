<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String step = (String) request.getAttribute("step");
  if (step == null) step = "solicitar";

  String error = (String) request.getAttribute("mensajeError");
  String info = (String) request.getAttribute("mensajeInfo");
  String emailParaReenvio = (String) request.getAttribute("emailParaReenvio");
%>
<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Recuperación de Contraseña</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bi/bootstrap-icons.css">
  <style>
    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f5fafa; }

    .card-recuperar {
      background: #fff;
      border-radius: 20px;
      box-shadow: 0 8px 32px rgba(76, 186, 184, 0.15);
      padding: 2.5rem 2rem;
      max-width: 420px;
      width: 100%;
    }

    .logo-recuperar {
      width: 90px;
      height: auto;
      margin-bottom: 1rem;
    }

    .titulo-recuperar {
      font-size: 1.7rem;
      font-weight: 700;
      color: #4cbab8;
      margin-bottom: 0.4rem;
    }

    .subtitulo-recuperar {
      font-size: 0.95rem;
      color: #555;
      margin-bottom: 1.5rem;
    }

    /* Cajas OTP separadas */
    .otp-container {
      display: flex;
      gap: 10px;
      justify-content: center;
      margin-bottom: 1.2rem;
    }

    .otp-input {
      width: 48px;
      height: 56px;
      text-align: center;
      font-size: 1.4rem;
      font-weight: 700;
      border: 2px solid #b2e0df;
      border-radius: 10px;
      color: #4cbab8;
      background: #f0fdfb;
      outline: none;
      transition: border 0.2s;
      text-transform: uppercase;
    }

    .otp-input:focus {
      border-color: #4cbab8;
      background: #e6f9f8;
    }

    .btn-teal-rec {
      background: #4cbab8;
      color: #fff;
      border: none;
      border-radius: 10px;
      padding: 0.65rem;
      width: 100%;
      font-size: 1rem;
      font-weight: 600;
      transition: background 0.2s;
      cursor: pointer;
    }

    .btn-teal-rec:hover { background: #3aa8a6; }

    .btn-reenviar {
      background: none;
      border: none;
      color: #4cbab8;
      font-weight: 600;
      font-size: 0.9rem;
      cursor: pointer;
      text-decoration: underline;
      padding: 0;
      margin-top: 0.5rem;
    }

    .btn-reenviar:hover { color: #3aa8a6; }

    .link-login {
      color: #5B7FFF;
      text-decoration: none;
      font-weight: 600;
    }

    .link-login:hover { text-decoration: underline; }

    .pass-hint {
      font-size: 0.8rem;
      color: #888;
      margin-top: 0.2rem;
    }

    .pass-strength {
      height: 4px;
      border-radius: 2px;
      margin-top: 6px;
      transition: all 0.3s;
      background: #e0e0e0;
    }
  </style>
</head>
<body class="d-flex align-items-center justify-content-center min-vh-100 py-4">

<div class="container d-flex justify-content-center">
  <div class="card-recuperar">

    <!-- Mensajes -->
    <% if (error != null) { %>
      <div class="alert alert-danger d-flex align-items-center py-2 rounded-3 mb-3" role="alert">
        <i class="bi bi-exclamation-triangle-fill me-2"></i>
        <div class="small"><%= error %></div>
      </div>
    <% } %>

    <% if (info != null) { %>
      <div class="alert alert-info d-flex align-items-center py-2 rounded-3 mb-3" role="alert">
        <i class="bi bi-info-circle-fill me-2"></i>
        <div class="small"><%= info %></div>
      </div>
    <% } %>

    <!-- ===== PASO 1: SOLICITAR ===== -->
    <% if ("solicitar".equals(step)) { %>
    <div class="text-center mb-4">
      <img src="${pageContext.request.contextPath}/assets/img/login_logo.png" alt="Recuperar" class="logo-recuperar">
      <div class="titulo-recuperar">Recuperar contraseña</div>
      <div class="subtitulo-recuperar">Ingresa tu correo o número de empleado y te enviaremos un código</div>
    </div>

    <form action="recuperar" method="post">
      <input type="hidden" name="action" value="solicitar">
      <div class="mb-3">
        <label for="txtDato" class="form-label fw-bold text-dark mb-1 small">Correo o Número de Empleado:</label>
        <div class="input-group">
          <span class="input-group-text bg-transparent border-end-0 text-secondary py-2"><i class="bi bi-person"></i></span>
          <input type="text" class="form-control border-start-0 py-2" id="txtDato" name="dato"
                 placeholder="correo@utez.edu.mx o num. empleado" required>
        </div>
      </div>
      <button class="btn-teal-rec" type="submit">
        <i class="bi bi-envelope-arrow-up me-1"></i> Enviar código
      </button>
    </form>
    <% } %>

    <!-- ===== PASO 2: VERIFICAR CÓDIGO ===== -->
    <% if ("verificar".equals(step)) { %>
    <div class="text-center mb-4">
      <img src="${pageContext.request.contextPath}/assets/img/login_logo.png" alt="Código" class="logo-recuperar">
      <div class="titulo-recuperar">Ingresar código</div>
      <div class="subtitulo-recuperar">Introduce los 6 caracteres enviados a tu correo</div>
    </div>

    <form action="recuperar" method="post" id="formVerificar">
      <input type="hidden" name="action" value="verificar">
      <input type="hidden" name="codigoCompleto" id="codigoCompleto">

      <div class="otp-container" id="otpContainer">
        <input class="otp-input" maxlength="1" data-idx="0" id="otp0" autocomplete="off">
        <input class="otp-input" maxlength="1" data-idx="1" id="otp1" autocomplete="off">
        <input class="otp-input" maxlength="1" data-idx="2" id="otp2" autocomplete="off">
        <input class="otp-input" maxlength="1" data-idx="3" id="otp3" autocomplete="off">
        <input class="otp-input" maxlength="1" data-idx="4" id="otp4" autocomplete="off">
        <input class="otp-input" maxlength="1" data-idx="5" id="otp5" autocomplete="off">
      </div>

      <button class="btn-teal-rec" type="submit">
        <i class="bi bi-shield-check me-1"></i> Verificar código
      </button>
    </form>

    <div class="text-center mt-3">
      <form action="recuperar" method="post" style="display:inline;">
        <input type="hidden" name="action" value="solicitar">
        <% if (emailParaReenvio != null) { %>
        <input type="hidden" name="dato" value="<%= emailParaReenvio %>">
        <% } %>
        <button type="submit" class="btn-reenviar">
          <i class="bi bi-arrow-clockwise me-1"></i> Reenviar código
        </button>
      </form>
    </div>

    <script>
      (function () {
        var inputs = document.querySelectorAll('.otp-input');
        inputs.forEach(function (inp, i) {
          inp.addEventListener('input', function () {
            var val = inp.value.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
            inp.value = val ? val[val.length - 1] : '';
            if (inp.value && i < inputs.length - 1) inputs[i + 1].focus();
          });
          inp.addEventListener('keydown', function (e) {
            if (e.key === 'Backspace' && !inp.value && i > 0) inputs[i - 1].focus();
          });
          inp.addEventListener('paste', function (e) {
            e.preventDefault();
            var pasted = (e.clipboardData || window.clipboardData).getData('text').toUpperCase().replace(/[^A-Z0-9]/g, '');
            for (var j = 0; j < 6 && j < pasted.length; j++) {
              inputs[j].value = pasted[j];
            }
            var nextEmpty = Math.min(pasted.length, inputs.length - 1);
            inputs[nextEmpty].focus();
          });
        });

        document.getElementById('formVerificar').addEventListener('submit', function () {
          var codigo = '';
          inputs.forEach(function (inp) { codigo += inp.value; });
          document.getElementById('codigoCompleto').value = codigo;
        });
      })();
    </script>
    <% } %>

    <!-- ===== PASO 3: CAMBIAR CONTRASEÑA ===== -->
    <% if ("cambiar".equals(step)) { %>
    <div class="text-center mb-4">
      <img src="${pageContext.request.contextPath}/assets/img/login_logo.png" alt="Nueva contraseña" class="logo-recuperar">
      <div class="titulo-recuperar">Nueva contraseña</div>
      <div class="subtitulo-recuperar">Ingresa tu nueva contraseña (12–15 caracteres)</div>
    </div>

    <form action="recuperar" method="post" id="formCambiar" onsubmit="return validarPass()">
      <input type="hidden" name="action" value="cambiar">
      <div class="mb-3">
        <label class="form-label fw-bold text-dark mb-1 small">Nueva contraseña:</label>
        <div class="input-group">
          <span class="input-group-text bg-transparent border-end-0 text-secondary py-2"><i class="bi bi-lock"></i></span>
          <input type="password" class="form-control border-start-0 py-2" name="pass1" id="pass1"
                 placeholder="Nueva contraseña" minlength="12" maxlength="15" required>
        </div>
        <div class="pass-strength" id="passStrength"></div>
        <div class="pass-hint">Entre 12 y 15 caracteres</div>
      </div>
      <div class="mb-3">
        <label class="form-label fw-bold text-dark mb-1 small">Confirmar contraseña:</label>
        <div class="input-group">
          <span class="input-group-text bg-transparent border-end-0 text-secondary py-2"><i class="bi bi-lock"></i></span>
          <input type="password" class="form-control border-start-0 py-2" name="pass2" id="pass2"
                 placeholder="Repite la contraseña" minlength="12" maxlength="15" required>
        </div>
        <div id="passMatchMsg" class="small mt-1"></div>
      </div>
      <button class="btn-teal-rec" type="submit">
        <i class="bi bi-floppy me-1"></i> Actualizar contraseña
      </button>
    </form>

    <script>
      document.getElementById('pass1').addEventListener('input', function () {
        var len = this.value.length;
        var bar = document.getElementById('passStrength');
        if (len === 0) { bar.style.background = '#e0e0e0'; bar.style.width = '0'; return; }
        var pct = Math.min(100, Math.round((len / 15) * 100)) + '%';
        var color = len < 8 ? '#e74c3c' : (len < 12 ? '#f39c12' : '#27ae60');
        bar.style.background = color;
        bar.style.width = pct;
      });

      document.getElementById('pass2').addEventListener('input', function () {
        var p1 = document.getElementById('pass1').value;
        var msg = document.getElementById('passMatchMsg');
        if (this.value.length === 0) { msg.textContent = ''; return; }
        if (p1 === this.value) {
          msg.textContent = '✓ Las contraseñas coinciden';
          msg.style.color = '#27ae60';
        } else {
          msg.textContent = '✗ Las contraseñas no coinciden';
          msg.style.color = '#e74c3c';
        }
      });

      function validarPass() {
        var p1 = document.getElementById('pass1').value;
        var p2 = document.getElementById('pass2').value;
        if (p1.length < 12 || p1.length > 15) {
          alert('La contraseña debe tener entre 12 y 15 caracteres.');
          return false;
        }
        if (p1 !== p2) {
          alert('Las contraseñas no coinciden.');
          return false;
        }
        return true;
      }
    </script>
    <% } %>

    <div class="text-center small mt-4">
      <span class="text-dark">¿Ya recordaste tu contraseña? </span>
      <a href="login.jsp" class="link-login">Inicia sesión aquí</a>
    </div>

  </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
</body>
</html>