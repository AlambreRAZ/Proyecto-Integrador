const contextPath = window.contextPath || '';
const params = new URLSearchParams(window.location.search);
const idEvento = params.get('id');

const form = document.getElementById('formEditarEvento');
const campoNombre = document.getElementById('campoNombre');
const campoLugar = document.getElementById('campoLugar');
const campoInstitucion = document.getElementById('campoInstitucion');
const campoTipo = document.getElementById('campoTipo');
const campoDescripcion = document.getElementById('campoDescripcion');
const campoFechaInicio = document.getElementById('campoFechaInicio');
const campoFechaFin = document.getElementById('campoFechaFin');

// Convierte "yyyy-MM-dd" (formato que maneja el servidor) a "dd/mm/yy" (formato que usa esta vista)
function aFechaVisible(iso) {
    if (!iso) return '';
    const partes = iso.split('-');
    if (partes.length !== 3) return iso;
    return partes[2] + '/' + partes[1] + '/' + partes[0].slice(2);
}

// Convierte "dd/mm/yy" o "dd/mm/yyyy" (lo que escribe el usuario) a "yyyy-MM-dd" (lo que espera el servidor)
function aFechaServidor(visible) {
    const partes = (visible || '').split('/');
    if (partes.length !== 3) return '';
    let [d, m, y] = partes;
    if (y.length === 2) y = '20' + y;
    return y + '-' + m.padStart(2, '0') + '-' + d.padStart(2, '0');
}

function cargarEvento() {
    if (!idEvento) {
        Swal.fire({
            icon: 'error',
            title: 'Falta el id del evento',
            text: 'Entra a esta página desde "Gestión de Eventos" para poder editar.',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    fetch(contextPath + '/EditarEventoServlet?id=' + encodeURIComponent(idEvento))
        .then(function (response) { return response.json(); })
        .then(function (data) {
            if (!data.success) {
                Swal.fire({
                    icon: 'error',
                    title: 'No se pudo cargar el evento',
                    text: data.message || 'Ocurrió un error al obtener los datos.',
                    confirmButtonColor: '#00847b'
                });
                return;
            }
            campoNombre.value = data.nombre || '';
            campoLugar.value = data.lugar || '';
            campoInstitucion.value = data.institucion || '';
            campoDescripcion.value = data.descripcion || '';
            campoFechaInicio.value = aFechaVisible(data.fechaInicio);
            campoFechaFin.value = aFechaVisible(data.fechaFin);

            if (campoTipo.querySelector('option[value="' + data.tipo + '"]')) {
                campoTipo.value = data.tipo;
            }

            document.querySelectorAll('input[name="modalidad"]').forEach(function (chk) {
                chk.checked = (chk.value === data.modalidad);
            });
        })
        .catch(function (error) {
            console.error('Error al cargar el evento:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error de conexión',
                text: 'No fue posible comunicarse con el servidor.',
                confirmButtonColor: '#00847b'
            });
        });
}

form.addEventListener('submit', function (e) {
    e.preventDefault();

    const modalidadSeleccionada = document.querySelector('input[name="modalidad"]:checked');

    const datos = new FormData();
    datos.append('id', idEvento);
    datos.append('nombre', campoNombre.value);
    datos.append('lugar', campoLugar.value);
    datos.append('institucion', campoInstitucion.value);
    datos.append('tipo', campoTipo.value);
    datos.append('descripcion', campoDescripcion.value);
    datos.append('fechaInicio', aFechaServidor(campoFechaInicio.value));
    datos.append('fechaFin', aFechaServidor(campoFechaFin.value));
    datos.append('modalidad', modalidadSeleccionada ? modalidadSeleccionada.value : '');

    fetch(contextPath + '/EditarEventoServlet', {
        method: 'POST',
        body: datos
    })
        .then(function (response) {
            return response.json().then(function (data) {
                return { ok: response.ok, data: data };
            });
        })
        .then(function (resultado) {
            if (resultado.ok && resultado.data.success) {
                Swal.fire({
                    icon: 'success',
                    title: '¡Evento actualizado con éxito!',
                    text: 'Los cambios se guardaron correctamente.',
                    confirmButtonColor: '#00847b',
                    confirmButtonText: 'Aceptar'
                }).then(function (result) {
                    if (result.isConfirmed) {
                        window.location.href = 'gestion_evento_co.jsp';
                    }
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'No se pudo actualizar el evento',
                    text: resultado.data.message || 'Ocurrió un error al conectar con la base de datos.',
                    confirmButtonColor: '#00847b'
                });
            }
        })
        .catch(function (error) {
            console.error('Error al actualizar el evento:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error de conexión',
                text: 'No fue posible comunicarse con el servidor.',
                confirmButtonColor: '#00847b'
            });
        });
});

cargarEvento();