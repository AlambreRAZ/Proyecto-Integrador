document.addEventListener('DOMContentLoaded', function () {

    function alertaSwal(title, text, icon) {
        Swal.fire({
            icon: icon,
            title: title,
            text: text,
            confirmButtonColor: '#4cbab8'
        });
    }

    const formPaso1 = document.getElementById('formPaso1');
    if (formPaso1) {
        formPaso1.addEventListener('submit', function (e) {
            const inputDato = document.getElementById('txtDato');
            const valor = inputDato.value.trim();

            // 1. Vacío
            if (valor === '') {
                e.preventDefault();
                alertaSwal('Atención', 'Aún faltan campos', 'warning');
                return;
            }


            if (valor.length > 50) {
                e.preventDefault();
                alertaSwal('Atención', 'Correo muy largo', 'warning');
                return;
            }

            if (valor.includes('@') && !valor.toLowerCase().endsWith('@utez.edu.mx')) {
                e.preventDefault();
                alertaSwal('Atención', 'El correo debe ser institucional (@utez.edu.mx)', 'warning');
                return;
            }
        });
    }

    const formPaso2 = document.getElementById('formPaso2');
    const inputCodigo = document.getElementById('txtCodigo');

    if (inputCodigo) {

        inputCodigo.addEventListener('input', function () {
            inputCodigo.classList.remove('border-error');
            const valor = this.value;
            const regexAlfanumerico = /^[a-zA-Z0-9]*$/;

            if (!regexAlfanumerico.test(valor)) {
                this.value = valor.replace(/[^a-zA-Z0-9]/g, '');
                alertaSwal('Formato no válido', 'Formato no válido. El código solo debe contener valores alfanuméricos', 'warning');
            }
        });
    }

    if (formPaso2) {
        formPaso2.addEventListener('submit', function (e) {
            inputCodigo.classList.remove('border-error');
            const codigo = inputCodigo.value.trim();

            // 1. Validar Expiración de 5 minutos
            const tiempoInicio = inputCodigo.dataset.tiempoEnvio;
            if (tiempoInicio) {
                const tiempoActual = new Date().getTime();
                if ((tiempoActual - parseInt(tiempoInicio)) > 300000) { // 300,000 ms = 5 min
                    e.preventDefault();
                    inputCodigo.value = '';
                    alertaSwal('Código expirado', 'El código ha expirado. Por favor, solicita uno nuevo', 'error');
                    return;
                }
            }

            // 2. Casillas vacías o incompletas (< 6 caracteres)
            if (codigo === '' || codigo.length < 6) {
                e.preventDefault();
                inputCodigo.classList.add('border-error');
                alertaSwal('Atención', 'Introduce el código de verificación', 'warning');
                return;
            }
        });
    }

    // ==================== REGLAS PASO 3 (CONTRASEÑA) ====================
    const formPaso3 = document.getElementById('formPaso3');
    if (formPaso3) {
        formPaso3.addEventListener('submit', function (e) {
            const pass1 = document.getElementById('pass1');
            const pass2 = document.getElementById('pass2');

            pass1.classList.remove('border-error');
            pass2.classList.remove('border-error');

            const p1 = pass1.value.trim();
            const p2 = pass2.value.trim();

            // 1. Vacíos
            if (p1 === '' || p2 === '') {
                e.preventDefault();
                if (p1 === '') pass1.classList.add('border-error');
                if (p2 === '') pass2.classList.add('border-error');
                alertaSwal('Atención', 'No se pueden dejar vacíos los campos obligatorios', 'warning');
                return;
            }

            // 2. Longitud entre 12 y 15 caracteres
            if (p1.length < 12 || p1.length > 15) {
                e.preventDefault();
                pass1.classList.add('border-error');
                alertaSwal('Atención', 'La contraseña debe tener entre 12 y 15 caracteres', 'warning');
                return;
            }

            // 3. Coincidencia exacta
            if (p1 !== p2) {
                e.preventDefault();
                pass1.classList.add('border-error');
                pass2.classList.add('border-error');
                alertaSwal('Atención', 'Las contraseñas no coinciden', 'warning');
                return;
            }
        });
    }

    const serverError = document.getElementById('serverErrorMsg')?.value;
    const serverStep = document.getElementById('serverStep')?.value;

    if (serverError) {
        const errLower = serverError.toLowerCase();

        if (serverStep === 'verificar' && errLower.includes('incorrecto')) {
            if (inputCodigo) inputCodigo.value = '';
            alertaSwal('Error', 'El código ingresado es incorrecto. Por favor, inténtalo de nuevo', 'error');
        } else if (serverStep === 'solicitar' && (errLower.includes('no registrado') || errLower.includes('no existe'))) {
            alertaSwal('No encontrado', 'El correo ingresado no está registrado', 'error');
        }
    }
});