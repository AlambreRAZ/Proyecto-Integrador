const contextPath = window.contextPath || '';
const urlParams = new URLSearchParams(window.location.search);
const idEvento = urlParams.get('id');

document.addEventListener('DOMContentLoaded', function () {
    // 1. Configurar enlace de volver manteniendo el ID
    if (idEvento) {
        const btnVolver = document.getElementById('btnVolver');
        if (btnVolver) {
            btnVolver.href = contextPath + '/ver_mas_evento_de.jsp?id=' + idEvento;
        }
        cargarDatosEvento(idEvento);
    } else {
        console.warn("No se proporcionó ID de evento en la URL.");
    }

    configurarVigenciaYArchivo();
});

function cargarDatosEvento(id) {
    fetch(contextPath + '/ListarEventosServlet')
        .then(res => res.json())
        .then(eventos => {
            const ev = eventos.find(e => e.id == id);
            if (ev) {
                if (document.getElementById('eventoNombre')) document.getElementById('eventoNombre').innerText = (ev.nombre || 'SIN NOMBRE').toUpperCase();
                if (document.getElementById('eventoTipo')) document.getElementById('eventoTipo').innerText = ev.tipo || 'N/A';
                if (document.getElementById('eventoLugar')) document.getElementById('eventoLugar').innerText = ev.lugar || 'N/A';
                if (document.getElementById('eventoInstitucion')) document.getElementById('eventoInstitucion').innerText = ev.institucion || 'N/A';
                if (document.getElementById('eventoDescripcion')) document.getElementById('eventoDescripcion').innerText = ev.descripcion || 'Sin descripción';
                if (document.getElementById('eventoFechaInicio')) document.getElementById('eventoFechaInicio').innerText = formatearFecha(ev.fechaInicio);
                if (document.getElementById('eventoFechaFin')) document.getElementById('eventoFechaFin').innerText = formatearFecha(ev.fechaFin);
                if (ev.modalidad && document.getElementById('eventoModalidad')) {
                    document.getElementById('eventoModalidad').innerText = ev.modalidad;
                }
            }
        })
        .catch(err => console.error("Error al obtener evento:", err));
}

function formatearFecha(fechaIso) {
    if (!fechaIso) return '';
    const partes = fechaIso.split('-');
    if (partes.length !== 3) return fechaIso;
    return partes[2] + '/' + partes[1] + '/' + partes[0].slice(2);
}

// Configurar inputs de archivo, fecha de vigencia y formulario
function configurarVigenciaYArchivo() {
    const radioSi = document.getElementById('vigenciaSi');
    const radioNo = document.getElementById('vigenciaNo');
    const inputFecha = document.getElementById('inputFechaVigencia');
    const btnExplorar = document.getElementById('btnExplorar');
    const inputArchivo = document.getElementById('inputArchivo');
    const textoArchivo = document.getElementById('textoArchivo');
    const form = document.getElementById('formCargarArchivo');

    if (!form) return;

    // Habilitar / Deshabilitar Fecha de Vigencia
    if (radioSi && radioNo && inputFecha) {
        radioSi.addEventListener('change', () => inputFecha.disabled = false);
        radioNo.addEventListener('change', () => {
            inputFecha.disabled = true;
            inputFecha.value = '';
        });
    }

    // Abrir ventana de archivo al dar clic en Explorar
    if (btnExplorar && inputArchivo) {
        btnExplorar.addEventListener('click', () => inputArchivo.click());
    }

    // Validar extensiones (.pdf, .jpg, .png) y mostrar nombre seleccionado
    if (inputArchivo) {
        inputArchivo.addEventListener('change', function () {
            if (this.files && this.files[0]) {
                const file = this.files[0];
                const ext = file.name.split('.').pop().toLowerCase();
                const permitidas = ['pdf', 'jpg', 'jpeg', 'png'];

                if (!permitidas.includes(ext)) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Formato no válido',
                        text: 'Solo se permiten archivos en formato PDF, JPG y PNG.',
                        confirmButtonColor: '#00847b'
                    });
                    this.value = '';
                    if (textoArchivo) textoArchivo.innerText = 'Selecciona el Archivo a subir (.pdf, .jpg, .png)';
                    return;
                }

                if (textoArchivo) {
                    textoArchivo.innerHTML = `<b class="text-success">Archivo seleccionado:</b> ${file.name}`;
                }
            }
        });
    }

    // Envío del formulario al Servlet con Preloader
    form.addEventListener('submit', function (e) {
        e.preventDefault();

        if (!inputArchivo || !inputArchivo.files || inputArchivo.files.length === 0) {
            Swal.fire({
                icon: 'warning',
                title: 'Atención',
                text: 'Por favor selecciona un archivo antes de continuar.',
                confirmButtonColor: '#00847b'
            });
            return;
        }

        if (radioSi && radioSi.checked && (!inputFecha || !inputFecha.value)) {
            Swal.fire({
                icon: 'warning',
                title: 'Fecha requerida',
                text: 'Por favor especifica la fecha de vigencia.',
                confirmButtonColor: '#00847b'
            });
            return;
        }

        const btnSubmit = form.querySelector('button[type="submit"]');
        if (btnSubmit) btnSubmit.disabled = true;

        const formData = new FormData();
        formData.append('idEvento', idEvento);
        formData.append('archivo', inputArchivo.files[0]);
        formData.append('tieneVigencia', radioSi ? radioSi.checked : false);
        formData.append('fechaVigencia', inputFecha ? inputFecha.value : '');

        // --- PRELOADER CON PORCENTAJE ---
        Swal.fire({
            title: 'Subiendo archivo...',
            html: '<div style="font-size: 1.5rem; font-weight: bold; color: #00847b; margin-top: 10px;" id="lblPorcentajeArchivo">0%</div>',
            allowOutsideClick: false,
            allowEscapeKey: false,
            showConfirmButton: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });

        // Petición XMLHttpRequest para medir progreso real de subida de archivos
        const xhr = new XMLHttpRequest();
        xhr.open('POST', contextPath + '/CargarArchivo', true);

        // Medir porcentaje real de subida
        xhr.upload.onprogress = function (e) {
            if (e.lengthComputable) {
                const porcentaje = Math.round((e.loaded / e.total) * 100);
                const el = document.getElementById('lblPorcentajeArchivo');
                // Dejamos en 99% como máximo hasta que el servlet responda el 200 OK
                if (el) el.textContent = Math.min(porcentaje, 99) + '%';
            }
        };

        xhr.onload = function () {
            const el = document.getElementById('lblPorcentajeArchivo');
            if (el) el.textContent = '100%';

            setTimeout(() => {
                if (xhr.status >= 200 && xhr.status < 300) {
                    try {
                        const data = JSON.parse(xhr.responseText);
                        if (data.success) {
                            Swal.fire({
                                icon: 'success',
                                title: '¡Éxito!',
                                text: data.message || 'El archivo se subió correctamente.',
                                confirmButtonColor: '#00847b'
                            }).then(() => {
                                window.location.href = contextPath + '/ver_mas_evento_de.jsp?id=' + idEvento;
                            });
                        } else {
                            Swal.fire({
                                icon: 'error',
                                title: 'Error',
                                text: data.message || 'No se pudo subir el archivo.',
                                confirmButtonColor: '#00847b'
                            });
                            if (btnSubmit) btnSubmit.disabled = false;
                        }
                    } catch (err) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error de formato',
                            text: 'Respuesta inválida del servidor.',
                            confirmButtonColor: '#00847b'
                        });
                        if (btnSubmit) btnSubmit.disabled = false;
                    }
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error en servidor',
                        text: 'Ocurrió un error con código ' + xhr.status,
                        confirmButtonColor: '#00847b'
                    });
                    if (btnSubmit) btnSubmit.disabled = false;
                }
            }, 300);
        };

        xhr.onerror = function () {
            console.error('Error de red al subir archivo.');
            Swal.fire({
                icon: 'error',
                title: 'Error de conexión',
                text: 'No fue posible conectar con el servidor.',
                confirmButtonColor: '#00847b'
            });
            if (btnSubmit) btnSubmit.disabled = false;
        };

        xhr.send(formData);
    });
}