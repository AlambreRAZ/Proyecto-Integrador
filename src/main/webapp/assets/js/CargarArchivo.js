const contextPath = window.contextPath || '';
const urlParams = new URLSearchParams(window.location.search);
const idEvento = urlParams.get('id');

document.addEventListener('DOMContentLoaded', function () {
    // 1. Configurar enlace de volver manteniendo el ID
    if (idEvento) {
        document.getElementById('btnVolver').href = contextPath + '/ver_mas_evento_de.jsp?id=' + idEvento;
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
                document.getElementById('eventoNombre').innerText = (ev.nombre || 'SIN NOMBRE').toUpperCase();
                document.getElementById('eventoTipo').innerText = ev.tipo || 'N/A';
                document.getElementById('eventoLugar').innerText = ev.lugar || 'N/A';
                document.getElementById('eventoInstitucion').innerText = ev.institucion || 'N/A';
                document.getElementById('eventoDescripcion').innerText = ev.descripcion || 'Sin descripción';
                document.getElementById('eventoFechaInicio').innerText = formatearFecha(ev.fechaInicio);
                document.getElementById('eventoFechaFin').innerText = formatearFecha(ev.fechaFin);
                if (ev.modalidad) {
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

    // Habilitar / Deshabilitar Fecha de Vigencia
    radioSi.addEventListener('change', () => inputFecha.disabled = false);
    radioNo.addEventListener('change', () => {
        inputFecha.disabled = true;
        inputFecha.value = '';
    });

    // Abrir ventana de archivo al dar clic en Explorar
    btnExplorar.addEventListener('click', () => inputArchivo.click());

    // Validar extensiones (.pdf, .jpg, .png) y mostrar nombre seleccionado
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
                textoArchivo.innerText = 'Selecciona el Archivo a subir (.pdf, .jpg, .png)';
                return;
            }

            textoArchivo.innerHTML = `<b class="text-success">Archivo seleccionado:</b> ${file.name}`;
        }
    });

    // Envío del formulario al Servlet
    form.addEventListener('submit', function (e) {
        e.preventDefault();

        if (!inputArchivo.files || inputArchivo.files.length === 0) {
            Swal.fire({
                icon: 'warning',
                title: 'Atención',
                text: 'Por favor selecciona un archivo antes de continuar.',
                confirmButtonColor: '#00847b'
            });
            return;
        }

        if (radioSi.checked && !inputFecha.value) {
            Swal.fire({
                icon: 'warning',
                title: 'Fecha requerida',
                text: 'Por favor especifica la fecha de vigencia.',
                confirmButtonColor: '#00847b'
            });
            return;
        }

        const formData = new FormData();
        formData.append('idEvento', idEvento);
        formData.append('archivo', inputArchivo.files[0]);
        formData.append('tieneVigencia', radioSi.checked);
        formData.append('fechaVigencia', inputFecha.value);

        fetch(contextPath + '/CargarArchivo', {
            method: 'POST',
            body: formData
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Éxito',
                        text: 'El archivo se subió correctamente.',
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
                }
            })
            .catch(err => {
                console.error('Error:', err);
                Swal.fire({
                    icon: 'error',
                    title: 'Error de conexión',
                    text: 'No se pudo conectar con el servidor.',
                    confirmButtonColor: '#00847b'
                });
            });
    });
}