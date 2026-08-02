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

// Elementos de Participantes
const tbodyParticipantes = document.getElementById('tablaParticipantesBody');
const inputBuscarParticipante = document.getElementById('buscarParticipante');
const selectDocenteAAsignar = document.getElementById('selectDocenteAAsignar');
const btnConfirmarAsignacion = document.getElementById('btnConfirmarAsignacion');

let participantesOriginales = [];
let todosLosUsuarios = [];
let filtroParticipante = '';

function aFechaVisible(iso) {
    if (!iso) return '';
    const partes = iso.split('-');
    if (partes.length !== 3) return iso;
    return partes[2] + '/' + partes[1] + '/' + partes[0].slice(2);
}

function aFechaServidor(visible) {
    const partes = (visible || '').split('/');
    if (partes.length !== 3) return '';
    let [d, m, y] = partes;
    if (y.length === 2) y = '20' + y;
    return y + '-' + m.padStart(2, '0') + '-' + d.padStart(2, '0');
}

function escHtml(texto) {
    if (!texto) return '';
    return String(texto).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}

function normString(t) {
    return String(t || '').toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');
}

function getNombreCompleto(u) {
    return [u.nombre, u.apellidoPaterno, u.apellidoMaterno].filter(Boolean).join(' ');
}

// -----------------------------------------------------------------------------
// EVENTOS - CARGA Y GUARDADO
// -----------------------------------------------------------------------------
function cargarEvento() {
    if (!idEvento) {
        Swal.fire({ icon: 'error', title: 'Falta el id del evento', text: 'Entra a esta página desde "Gestión de Eventos" para poder editar.', confirmButtonColor: '#00847b' });
        return;
    }

    fetch(contextPath + '/EditarEventoServlet?id=' + encodeURIComponent(idEvento) + '&t=' + Date.now())
        .then(res => res.json())
        .then(data => {
            if (!data.success) {
                Swal.fire({ icon: 'error', title: 'Error', text: data.message || 'No se pudo cargar', confirmButtonColor: '#00847b' });
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
            document.querySelectorAll('input[name="modalidad"]').forEach(chk => {
                chk.checked = (chk.value === data.modalidad);
            });
            
            cargarParticipantes();
            cargarTodosLosUsuarios();
        })
        .catch(err => {
            console.error('Error al cargar:', err);
        });
}

form.addEventListener('submit', function (e) {
    e.preventDefault();
    const modalidadSeleccionada = document.querySelector('input[name="modalidad"]:checked');
    const datos = new URLSearchParams();
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
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: datos.toString() 
    })
        .then(res => res.json().then(data => ({ ok: res.ok, data: data })))
        .then(resultado => {
            if (resultado.ok && resultado.data.success) {
                Swal.fire({ icon: 'success', title: '¡Evento actualizado con éxito!', confirmButtonColor: '#00847b', confirmButtonText: 'Aceptar' })
                    .then(r => { if (r.isConfirmed) window.location.href = 'gestion_evento_co.jsp'; });
            } else {
                Swal.fire({ icon: 'error', title: 'Error', text: resultado.data.message || 'No se pudo actualizar.', confirmButtonColor: '#00847b' });
            }
        }).catch(err => console.error(err));
});

// -----------------------------------------------------------------------------
// PARTICIPANTES - CARGA Y GESTIÓN
// -----------------------------------------------------------------------------
function cargarParticipantes() {
    fetch(contextPath + '/ListarParticipantesEventoServlet?id=' + encodeURIComponent(idEvento) + '&t=' + Date.now())
        .then(res => {
            if (!res.ok) throw new Error('HTTP error ' + res.status);
            return res.json();
        })
        .then(data => {
            participantesOriginales = data || [];
            aplicarFiltrosParticipantes();
            actualizarSelectDocentes(); // Refresca qué opciones mostrar en el select
        })
        .catch(err => {
            console.error('Error al cargar participantes:', err);
            if (tbodyParticipantes) {
                tbodyParticipantes.innerHTML = '<tr><td colspan="4" class="text-center text-danger py-4">Error al cargar datos. Verifica tu conexión o reinicia Tomcat.</td></tr>';
            }
        });
}

function aplicarFiltrosParticipantes() {
    const texto = normString(filtroParticipante);
    const filtrados = participantesOriginales.filter(u => {
        return texto === '' || normString(getNombreCompleto(u)).includes(texto) || normString(u.correo).includes(texto);
    });
    renderParticipantes(filtrados);
}

function renderParticipantes(lista) {
    if (!tbodyParticipantes) return;
    if (!lista.length) {
        tbodyParticipantes.innerHTML = '<tr><td colspan="4" class="text-center text-muted py-4">No hay docentes asignados a este evento.</td></tr>';
        return;
    }
    tbodyParticipantes.innerHTML = '';
    lista.forEach(u => {
        const tr = document.createElement('tr');
        const estadoColor = u.activo === 1 ? '#28a745' : '#d32f2f';
        const estadoTexto = u.activo === 1 ? 'Activo' : 'Inactivo';
        
        tr.innerHTML = 
            '<td>' +
            '  <div class="docente-name-container">' +
            '    <div class="avatar-circle"></div>' +
            '    <div class="docente-name">' + escHtml(getNombreCompleto(u)) + '</div>' +
            '  </div>' +
            '</td>' +
            '<td>' + escHtml(u.correo) + '</td>' +
            '<td style="color:' + estadoColor + ';font-weight:600;">' + estadoTexto + '</td>' +
            '<td>' +
            '  <a href="#" class="action-btn delete btn-remover-participante" data-id="' + u.id + '" title="Remover del evento"><i class="bi bi-trash"></i></a>' +
            '</td>';
        tbodyParticipantes.appendChild(tr);
    });
}

if (inputBuscarParticipante) {
    inputBuscarParticipante.addEventListener('input', function () {
        filtroParticipante = this.value;
        aplicarFiltrosParticipantes();
    });
}

// REMOVER PARTICIPANTE
if (tbodyParticipantes) {
    tbodyParticipantes.addEventListener('click', function (e) {
        const btn = e.target.closest('.btn-remover-participante');
        if (!btn) return;
        e.preventDefault();
        
        const idUsuario = btn.getAttribute('data-id');
        Swal.fire({
            icon: 'warning', title: '¿Remover a este docente?', text: 'Se quitará su asignación al evento.',
            showCancelButton: true, confirmButtonColor: '#d33', cancelButtonColor: '#aaaaaa', confirmButtonText: 'Sí, remover'
        }).then(res => {
            if (res.isConfirmed) {
                const formData = new URLSearchParams();
                formData.append("idEvento", idEvento);
                formData.append("idUsuario", idUsuario);
                
                fetch(contextPath + '/RemoverDocenteEventoServlet', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: formData.toString()
                }).then(r => r.json()).then(data => {
                    if (data.success) {
                        Swal.fire({ icon: 'success', title: 'Removido', timer: 1500, showConfirmButton: false });
                        cargarParticipantes();
                    } else {
                        Swal.fire({ icon: 'error', title: 'Error', text: data.message });
                    }
                });
            }
        });
    });
}

// -----------------------------------------------------------------------------
// ASIGNAR NUEVO PARTICIPANTE (MODAL)
// -----------------------------------------------------------------------------
function cargarTodosLosUsuarios() {
    fetch(contextPath + '/ListarUsuariosServlet?t=' + Date.now())
        .then(res => {
            if (!res.ok) throw new Error('HTTP error ' + res.status);
            return res.json();
        })
        .then(data => {
            todosLosUsuarios = data || [];
            actualizarSelectDocentes();
        })
        .catch(err => {
            console.error('Error al cargar todos los usuarios:', err);
            if (selectDocenteAAsignar) {
                selectDocenteAAsignar.innerHTML = '<option value="" disabled selected>Error al cargar la lista.</option>';
            }
        });
}

function actualizarSelectDocentes() {
    if (!selectDocenteAAsignar) return;
    
    // Convertir a Number para comparación estricta correcta
    const asignadosIds = participantesOriginales.map(p => Number(p.id));
    const noAsignados = todosLosUsuarios.filter(u => !asignadosIds.includes(Number(u.id)));
    
    selectDocenteAAsignar.innerHTML = '';
    
    if (noAsignados.length === 0) {
        selectDocenteAAsignar.innerHTML = '<option value="" disabled selected>No hay docentes disponibles para asignar.</option>';
        return;
    }
    
    selectDocenteAAsignar.innerHTML = '<option value="" disabled selected>Selecciona un docente/coordinador...</option>';
    noAsignados.forEach(u => {
        const opt = document.createElement('option');
        opt.value = u.id;
        opt.textContent = getNombreCompleto(u) + ' (' + u.numeroEmpleado + ')';
        selectDocenteAAsignar.appendChild(opt);
    });
}

if (btnConfirmarAsignacion) {
    btnConfirmarAsignacion.addEventListener('click', function () {
        const idUsuario = selectDocenteAAsignar.value;
        if (!idUsuario) {
            Swal.fire({ icon: 'warning', title: 'Atención', text: 'Selecciona un docente primero.', confirmButtonColor: '#00847b' });
            return;
        }
        
        const formData = new URLSearchParams();
        formData.append("idEvento", idEvento);
        formData.append("idUsuario", idUsuario);
        
        fetch(contextPath + '/AsignarDocenteEventoServlet', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: formData.toString()
        }).then(r => r.json()).then(data => {
            if (data.success) {
                Swal.fire({ icon: 'success', title: 'Asignado', timer: 1500, showConfirmButton: false });
                // Cerrar modal
                const modal = bootstrap.Modal.getInstance(document.getElementById('modalAsignarDocente'));
                if(modal) modal.hide();
                // Recargar participantes
                cargarParticipantes();
            } else {
                Swal.fire({ icon: 'error', title: 'Error', text: data.message });
            }
        });
    });
}

// Iniciar
cargarEvento();