document.addEventListener("DOMContentLoaded", () => {
    // 1. RESTRICCIÓN EN TIEMPO REAL: Solo números
    const inputsNumericos = document.querySelectorAll('#campoTelefono, #campoNumEmpleado, [name="telefono"], [name="numero_empleado"]');
    inputsNumericos.forEach(input => {
        if (input) {
            input.addEventListener('input', (e) => {
                e.target.value = e.target.value.replace(/[^0-9]/g, '');
            });
        }
    });

    // 2. RESTRICCIÓN EN TIEMPO REAL: Solo letras (incluye acentos, espacios y Ñ/ñ)
    const inputsTexto = document.querySelectorAll('#campoNombre, #campoApellidoP, #campoApellidoM, [name="nombre"], [name="apellido_paterno"], [name="apellido_materno"]');
    inputsTexto.forEach(input => {
        if (input) {
            input.addEventListener('input', (e) => {
                e.target.value = e.target.value.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑ\s]/g, '');
            });
        }
    });

    const formAgregar = document.getElementById('formAgregarDocente') || document.querySelector('form');
    if (formAgregar) {
        formAgregar.addEventListener('submit', registrarDocente);
    }
});

// Función auxiliar para mostrar alertas de SweetAlert2 rápidamente
function mostrarAlerta(titulo, mensaje, icono = 'warning') {
    Swal.fire({
        icon: icono,
        title: titulo,
        text: mensaje,
        confirmButtonColor: '#00847b'
    });
}

function obtenerPaginaDestino() {
    const pathActual = window.location.pathname;
    if (pathActual.includes('_de.jsp')) return 'gestion_docente_de.jsp';
    if (pathActual.includes('_do.jsp')) return 'gestion_docente_do.jsp';
    return 'gestion_docente_co.jsp';
}

function registrarDocente(e) {
    if (e && e.preventDefault) e.preventDefault();

    // Función auxiliar para obtener valor limpio por ID o por atributo Name
    const getVal = (id, nameAttr) => {
        const el = document.getElementById(id) || document.querySelector(`[name="${nameAttr}"]`);
        return el ? el.value.trim() : '';
    };

    const nombre = getVal('campoNombre', 'nombre');
    const apeP = getVal('campoApellidoP', 'apellido_paterno');
    const apeM = getVal('campoApellidoM', 'apellido_materno');
    const division = getVal('campoDivision', 'division');
    const numEmp = getVal('campoNumEmpleado', 'numero_empleado');
    const tel = getVal('campoTelefono', 'telefono');
    const correo = getVal('campoCorreo', 'correo');
    const pass = getVal('pass1', 'contrasena');
    const confirmPass = getVal('pass2', 'confirmar_contrasena');

    // Obtener rol seleccionado
    const radioRol = document.querySelector('input[name="rol"]:checked');
    const rol = radioRol ? radioRol.value.toLowerCase() : 'docente';

    // --- VALIDACIONES DE FRONTEND ---
    if (!nombre || !apeP || !apeM || !numEmp || !tel || !correo || !pass || !confirmPass) {
        mostrarAlerta('Campos incompletos', 'Por favor, llena todos los campos obligatorios del formulario.');
        return;
    }

    if (!division) {
        mostrarAlerta('División requerida', 'Por favor selecciona una División Académica.');
        return;
    }

    if (!/^\d+$/.test(numEmp)) {
        mostrarAlerta('Número de Empleado inválido', 'El número de empleado solo debe contener dígitos numéricos.');
        return;
    }

    if (!/^\d{10}$/.test(tel)) {
        mostrarAlerta('Teléfono inválido', 'El teléfono debe ser de exactamente 10 dígitos numéricos.');
        return;
    }

    if (correo.length > 50) {
        mostrarAlerta('Correo muy largo', 'El correo institucional no debe exceder los 50 caracteres.');
        return;
    }

    if (!correo.toLowerCase().endsWith('@utez.edu.mx')) {
        mostrarAlerta('Correo no institucional', 'El correo debe terminar estrictamente en @utez.edu.mx');
        return;
    }

    if (pass.length < 12 || pass.length > 15) {
        mostrarAlerta('Contraseña inválida', 'La contraseña debe tener entre 12 y 15 caracteres.');
        return;
    }

    if (pass !== confirmPass) {
        mostrarAlerta('Las contraseñas no coinciden', 'Asegúrate de escribir exactamente la misma contraseña en ambos campos.');
        return;
    }

    // --- PREPARACIÓN DEL PRELOADER CON PORCENTAJE ---
    let porcentaje = 0;
    let timerCarga;

    Swal.fire({
        title: 'Registrando docente...',
        html: '<div style="font-size: 1.5rem; font-weight: bold; color: #00847b; margin-top: 10px;" id="lblPorcentajeDoc">0%</div>',
        allowOutsideClick: false,
        allowEscapeKey: false,
        showConfirmButton: false,
        didOpen: () => {
            Swal.showLoading();
            // Incremento progresivo hasta llegar al 90% mientras responde el Servlet
            timerCarga = setInterval(() => {
                if (porcentaje < 90) {
                    porcentaje += 10;
                    const el = document.getElementById('lblPorcentajeDoc');
                    if (el) el.textContent = porcentaje + '%';
                }
            }, 80);
        }
    });

    // --- CONSTRUCCIÓN Y ENVÍO DE DATOS ---
    const datos = new URLSearchParams();
    datos.append('nombre', nombre);
    datos.append('apellido_paterno', apeP);
    datos.append('apellido_materno', apeM);
    datos.append('numero_empleado', numEmp);
    datos.append('correo', correo);
    datos.append('telefono', tel);
    datos.append('division', division);
    datos.append('contrasena', pass);
    datos.append('confirmar_contrasena', confirmPass);
    datos.append('rol', rol);

    const contextPath = (window.contextPath || '').replace(/\/$/, '');
    const urlTarget = contextPath ? `${contextPath}/AgregarUsuarioServlet` : 'AgregarUsuarioServlet';

    fetch(urlTarget, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
        body: datos.toString(),
        credentials: 'same-origin'
    })
        .then(async res => {
            const data = await res.json().catch(() => null);
            if (!res.ok) {
                throw new Error((data && data.message) ? data.message : 'Error HTTP ' + res.status);
            }
            return data;
        })
        .then(resultado => {
            // Detenemos el reloj de simulación
            clearInterval(timerCarga);

            // Forzamos el 100%
            const el = document.getElementById('lblPorcentajeDoc');
            if (el) el.textContent = '100%';

            setTimeout(() => {
                if (!resultado || !resultado.success) {
                    mostrarAlerta('Error al registrar', (resultado && resultado.message) ? resultado.message : 'Ocurrió un problema en el servidor.', 'error');
                    return;
                }

                Swal.fire({
                    icon: 'success',
                    title: '¡Docente registrado!',
                    text: resultado.message || 'El docente fue registrado con éxito.',
                    confirmButtonColor: '#00847b'
                }).then(() => {
                    window.location.href = obtenerPaginaDestino();
                });
            }, 300);
        })
        .catch(err => {
            clearInterval(timerCarga);
            console.error('Error al registrar:', err);
            mostrarAlerta('Error de conexión', err.message || 'No se pudo comunicar con el servidor.', 'error');
        });
}