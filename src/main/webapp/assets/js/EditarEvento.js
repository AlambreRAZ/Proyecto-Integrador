document.addEventListener('DOMContentLoaded', function () {
    const campoDivision = document.getElementById('campoDivision');
    const formEditar = document.getElementById('formEditarEvento');

    // Cargar datos iniciales del evento si existe un ID en la URL o hidden
    const urlParams = new URLSearchParams(window.location.search);
    const idEventoUrl = urlParams.get('id') || urlParams.get('id_evento');
    if (idEventoUrl) {
        cargarDatosEvento(idEventoUrl);
    }

    if (formEditar) {
        formEditar.addEventListener('submit', function (e) {
            e.preventDefault(); // Detener el envío nativo del navegador
            datos.append('division', campoDivision ? campoDivision.value : '');
            // Captura de valores
            const id = document.getElementById('idEvento') ? document.getElementById('idEvento').value.trim() : '';
            const nombre = document.getElementById('campoNombre') ? document.getElementById('campoNombre').value.trim() : '';
            const lugar = document.getElementById('campoLugar') ? document.getElementById('campoLugar').value.trim() : '';
            const institucion = document.getElementById('campoInstitucion') ? document.getElementById('campoInstitucion').value.trim() : '';
            const tipo = document.getElementById('campoTipo') ? document.getElementById('campoTipo').value : '';
            const division = document.getElementById('campoDivision') ? document.getElementById('campoDivision').value : '';
            const descripcion = document.getElementById('campoDescripcion') ? document.getElementById('campoDescripcion').value.trim() : '';
            const fechaInicio = document.getElementById('campoFechaInicio') ? document.getElementById('campoFechaInicio').value : '';
            const fechaFin = document.getElementById('campoFechaFin') ? document.getElementById('campoFechaFin').value : '';
            const modalidadSelected = document.querySelector('input[name="modalidad"]:checked');

            // VALIDACIÓN COMPLETA
            if (!id || id === '0' || !nombre || !lugar || !institucion || !tipo || !division || !descripcion || !fechaInicio || !fechaFin || !modalidadSelected) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error de validación',
                    text: 'Faltan campos obligatorios o el ID del evento no es válido.',
                    confirmButtonColor: '#4d887b'
                });
                return;
            }

            // Confirmación antes de enviar
            Swal.fire({
                title: '¿Deseas guardar los cambios?',
                text: "Se actualizará la información del evento en el sistema.",
                icon: 'question',
                showCancelButton: true,
                confirmButtonColor: '#00796b',
                cancelButtonColor: '#6c757d',
                confirmButtonText: 'Sí, guardar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {

                    // Convertir el formulario a URL-encoded para el Servlet (incluye los inputs ocultos de docentes)
                    const formData = new FormData(formEditar);
                    const params = new URLSearchParams(formData);

                    // Envío por FETCH AJAX en segundo plano
                    fetch('EditarEventoServlet', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                        },
                        body: params
                    })
                        .then(response => response.json())
                        .then(data => {
                            if (data.success) {
                                Swal.fire({
                                    icon: 'success',
                                    title: '¡Actualizado!',
                                    text: data.message || 'El evento fue actualizado correctamente.',
                                    confirmButtonColor: '#00796b'
                                }).then(() => {
                                    window.location.href = 'gestion_eventos_de.jsp?t=' + new Date().getTime();
                                });
                            } else {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Error al actualizar',
                                    text: data.message || 'No se pudo actualizar el evento.',
                                    confirmButtonColor: '#d33'
                                });
                            }
                        })
                        .catch(error => {
                            console.error('Error al actualizar:', error);
                            Swal.fire({
                                icon: 'error',
                                title: 'Error de servidor',
                                text: 'Ocurrió un fallo en la comunicación con el servidor.',
                                confirmButtonColor: '#d33'
                            });
                        });
                }
            });
        });
    }
});

// Cargar y seleccionar automáticamente la División y los datos del servidor
function cargarDatosEvento(id) {
    fetch('EditarEventoServlet?id=' + id)
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                // Asignar división seleccionada correctamente al select
                const selectDivision = document.getElementById('campoDivision');
                if (campoDivision && data.idDivision) {
                    campoDivision.value = String(data.idDivision);
                }
            }
        })
        .catch(err => console.error("Error al cargar evento:", err));
}

// Funciones para la gestión de la tabla de docentes
function filtrarTablaDocentes() {
    const input = document.getElementById('inputBuscarDocente');
    if (!input) return;
    const filter = input.value.toLowerCase();
    const rows = document.querySelectorAll('#tablaDocentesBody tr');

    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(filter) ? '' : 'none';
    });
}

function eliminarDocenteFila(idFila) {
    Swal.fire({
        title: '¿Remover docente?',
        text: "El docente será desvinculado de este evento.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            const fila = document.getElementById(idFila);
            if (fila) {
                fila.remove();
                Swal.fire('Removido', 'El docente ha sido removido de la lista.', 'success');
            }
        }
    });
}

function agregarNuevoDocente() {
    const nombreInput = document.getElementById('modalNombreDocente');
    const correoInput = document.getElementById('modalCorreoDocente');

    if (!nombreInput || !correoInput) return;

    const nombre = nombreInput.value.trim();
    const correo = correoInput.value.trim();

    if (!nombre || !correo) {
        Swal.fire('Error', 'Debes ingresar el nombre y correo del docente.', 'error');
        return;
    }

    const tbody = document.getElementById('tablaDocentesBody') || document.querySelector('table tbody');
    if (!tbody) return;

    const idUnico = 'docente-' + Date.now();

    // Se incluye un <input type="hidden"> para que FormData capture el docente asignado al hacer Submit
    const nuevaFila = `
        <tr id="${idUnico}">
            <td>
                <div class="d-flex align-items-center">
                    <div class="avatar-circle me-2"></div>
                    <div class="docente-name">${nombre}</div>
                    <input type="hidden" name="docentesCorreos" value="${correo}">
                    <input type="hidden" name="docentesNombres" value="${nombre}">
                </div>
            </td>
            <td>${correo}</td>
            <td class="status-active">Activo</td>
            <td>
                <a href="javascript:void(0)" class="action-btn delete" onclick="eliminarDocenteFila('${idUnico}')"><i class="bi bi-trash"></i></a>
            </td>
        </tr>
    `;

    tbody.insertAdjacentHTML('beforeend', nuevaFila);

    // Limpiar modal y cerrarlo
    nombreInput.value = '';
    correoInput.value = '';

    const modalElement = document.getElementById('modalAgregarDocente');
    if (modalElement) {
        const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
        modalInstance.hide();
    }

    Swal.fire('¡Agregado!', 'El docente ha sido asignado al evento.', 'success');
}