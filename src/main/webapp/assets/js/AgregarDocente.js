document.addEventListener("DOMContentLoaded", () => {
    // Restricción en tiempo real: Solo números en Teléfono y Num. Empleado
    const inputsNumericos = document.querySelectorAll('#campoTelefono, #campoNumEmpleado, [name="telefono"], [name="numero_empleado"]');
    inputsNumericos.forEach(input => {
        if (input) {
            input.addEventListener('input', (e) => {
                e.target.value = e.target.value.replace(/[^0-9]/g, '');
            });
        }
    });

    // VINCULAR EL FORMULARIO AL EVENTO SUBMIT
    const formAgregar = document.getElementById('formAgregarDocente') || document.querySelector('form');
    if (formAgregar) {
        formAgregar.addEventListener('submit', registrarDocente);
    }
});

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

    // 1. OBTENER ROL SELECCIONADO CORRECTAMENTE DESDE EL RADIO BUTTON
    const radioRol = document.querySelector('input[name="rol"]:checked');
    const rol = radioRol ? radioRol.value.toLowerCase() : 'docente';

    // 2. VALIDACIONES DE FRONTEND
    if (!nombre || !apeP || !apeM || !numEmp || !tel || !correo || !pass || !confirmPass) {
        Swal.fire({
            icon: 'warning',
            title: 'Campos incompletos',
            text: 'Por favor, llena todos los campos obligatorios del formulario.',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    if (!division) {
        Swal.fire({
            icon: 'warning',
            title: 'División requerida',
            text: 'Por favor selecciona una División Académica.',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    if (!/^\d+$/.test(numEmp)) {
        Swal.fire({
            icon: 'warning',
            title: 'Número de Empleado inválido',
            text: 'El número de empleado solo debe contener dígitos numéricos.',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    if (!/^\d{10}$/.test(tel)) {
        Swal.fire({
            icon: 'warning',
            title: 'Teléfono inválido',
            text: 'El teléfono debe ser de exactamente 10 dígitos numéricos.',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    if (correo.length > 50) {
        Swal.fire({
            icon: 'warning',
            title: 'Correo demasiado largo',
            text: 'El correo institucional no debe exceder los 50 caracteres.',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    if (!correo.toLowerCase().endsWith('@utez.edu.mx')) {
        Swal.fire({
            icon: 'warning',
            title: 'Correo no institucional',
            text: 'El correo debe terminar estrictamente en @utez.edu.mx',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    if (pass.length < 12 || pass.length > 15) {
        Swal.fire({
            icon: 'warning',
            title: 'Contraseña inválida',
            text: 'La contraseña debe tener entre 12 y 15 caracteres.',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    if (pass !== confirmPass) {
        Swal.fire({
            icon: 'warning',
            title: 'Las contraseñas no coinciden',
            text: 'Asegúrate de escribir exactamente la misma contraseña en ambos campos.',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    // ENVÍO CON NOMBRES EXACTOS DE PARÁMETROS
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

    // 🛠️ CONSTRUCCIÓN SEGURA DE LA URL
    // Elimina la barra diagonal al final si existe en contextPath
    const contextPath = (window.contextPath || '').replace(/\/$/, '');

    // Si contextPath existe, agrega la barra; si está vacío, usa la ruta relativa directa
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
            if (!resultado || !resultado.success) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error al registrar',
                    text: (resultado && resultado.message) ? resultado.message : 'Ocurrió un problema en el servidor.',
                    confirmButtonColor: '#00847b'
                });
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
        })
        .catch(err => {
            console.error('Error al registrar:', err);
            Swal.fire({
                icon: 'error',
                title: 'Error de conexión',
                text: err.message || 'No se pudo comunicar con el servidor.',
                confirmButtonColor: '#00847b'
            });
        });
}