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

// Helper para obtener ID de usuario independientemente de si viene como id o idUsuario
function getIdUsuario(u) {
    return u.idUsuario || u.id;
}

// Helper para obtener el correo sin importar el mapeo
function getCorreoUsuario(u) {
    return u.correoInstitucional || u.correo || '';
}

// Transformar ISO (YYYY-MM-DD) a formato input o visible
function aFechaVisible(iso) {
    if (!iso) return '';
    return iso;
}

// Convierte la fecha del input para el Servidor
function aFechaServidor(visible) {
    if (!visible) return '';
    if (visible.includes('-')) return visible;

    const partes = visible.split('/');
    if (partes.length !== 3) return visible;
    let [d, m, y] = partes;
    if (y.length === 2) y = '20' + y;
    return `${y}-${m.padStart(2, '0')}-${d.padStart(2, '0')}`;
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
        Swal.fire({
            icon: 'error',
            title: 'Falta el id del evento',
            text: 'Entra a esta página desde "Gestión de Eventos" para poder editar.',
            confirmButtonColor: '#00847b'
        });
        return;
    }

    fetch(`${contextPath}/EditarEventoServlet?id=${encodeURIComponent(idEvento)}&t=${Date.now()}`)
        .then(res => res.json())
        .then(data => {
            if (!data.success) {
                Swal.fire({ icon: 'error', title: 'Error', text: data.message || 'No se pudo cargar', confirmButtonColor: '#00847b' });
                return;
            }
            if (campoNombre) campoNombre.value = data.nombre || '';
            if (campoLugar) campoLugar.value = data.lugar || '';
            if (campoInstitucion) campoInstitucion.value = data.institucion || '';
            if (campoDescripcion) campoDescripcion.value = data.descripcion || '';
            if (campoFechaInicio) campoFechaInicio.value = aFechaVisible(data.fechaInicio);
            if (campoFechaFin) campoFechaFin.value = aFechaVisible(data.fechaFin);

            if (campoTipo && campoTipo.querySelector(`option[value="${data.tipo}"]`)) {
                campoTipo.value = data.tipo;
            }
            document.querySelectorAll('input[name="modalidad"]').forEach(chk => {
                chk.checked = (chk.value === data.modalidad);
            });

            cargarParticipantes();
            cargarTodosLosUsuarios();
        })
        .catch(err => console.error('Error al cargar evento:', err));
}

if (form) {
    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const modalidadSeleccionada = document.querySelector('input[name="modalidad"]:checked');
        const datos = new URLSearchParams();
        datos.append('id', idEvento);
        datos.append('nombre', campoNombre ? campoNombre.value : '');
        datos.append('lugar', campoLugar ? campoLugar.value : '');
        datos.append('institucion', campoInstitucion ? campoInstitucion.value : '');
        datos.append('tipo', campoTipo ? campoTipo.value : '');
        datos.append('descripcion', campoDescripcion ? campoDescripcion.value : '');
        datos.append('fechaInicio', aFechaServidor(campoFechaInicio ? campoFechaInicio.value : ''));
        datos.append('fechaFin', aFechaServidor(campoFechaFin ? campoFechaFin.value : ''));
        datos.append('modalidad', modalidadSeleccionada ? modalidadSeleccionada.value : '');

        // --- PRELOADER CON PORCENTAJE SIMULADO ---
        let porcentaje = 0;
        let timerCarga;

        Swal.fire({
            title: 'Actualizando evento...',
            html: '<div style="font-size: 1.5rem; font-weight: bold; color: #00847b; margin-top: 10px;" id="lblPorcentajeEditEvento">0%</div>',
            allowOutsideClick: false,
            allowEscapeKey: false,
            showConfirmButton: false,
            didOpen: () => {
                Swal.showLoading();
                timerCarga = setInterval(() => {
                    if (porcentaje < 90) {
                        porcentaje += 10;
                        const el = document.getElementById('lblPorcentajeEditEvento');
                        if (el) el.textContent = porcentaje + '%';
                    }
                }, 80);
            }
        });

        fetch(`${contextPath}/EditarEventoServlet`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
            body: datos.toString()
        })
            .then(res => res.json().then(data => ({ ok: res.ok, data: data })))
            .then(resultado => {
                clearInterval(timerCarga);

                const el = document.getElementById('lblPorcentajeEditEvento');
                if (el) el.textContent = '100%';

                setTimeout(() => {
                    if (resultado.ok && resultado.data.success) {
                        Swal.fire({
                            icon: 'success',
                            title: '¡Evento actualizado con éxito!',
                            text: resultado.data.message || 'Los cambios se guardaron correctamente.',
                            confirmButtonColor: '#00847b',
                            confirmButtonText: 'Aceptar'
                        }).then(r => {
                            if (r.isConfirmed) {
                                const esDesarrollador = window.location.pathname.includes('_de.jsp');
                                const destino = esDesarrollador ? 'gestion_eventos_de.jsp' : 'gestion_evento_co.jsp';
                                window.location.href = destino;
                            }
                        });
                    } else {
                        Swal.fire({ icon: 'error', title: 'Error', text: resultado.data.message || 'No se pudo actualizar.', confirmButtonColor: '#00847b' });
                    }
                }, 300);
            })
            .catch(err => {
                clearInterval(timerCarga);
                console.error(err);
                Swal.fire({ icon: 'error', title: 'Error de conexión', text: 'No fue posible comunicarse con el servidor.', confirmButtonColor: '#00847b' });
            });
    });
}

// -----------------------------------------------------------------------------
// PARTICIPANTES - CARGA Y GESTIÓN
// -----------------------------------------------------------------------------
function cargarParticipantes() {
    fetch(`${contextPath}/ListarParticipantesEventoServlet?id=${encodeURIComponent(idEvento)}&t=${Date.now()}`)
        .then(res => {
            if (!res.ok) throw new Error('HTTP error ' + res.status);
            return res.json();
        })
        .then(data => {
            participantesOriginales = data || [];
            aplicarFiltrosParticipantes();
            actualizarSelectDocentes();
        })
        .catch(err => {
            console.error('Error al cargar participantes:', err);
            if (tbodyParticipantes) {
                tbodyParticipantes.innerHTML = '<tr><td colspan="4" class="text-center text-danger py-4">Error al cargar datos. Verifica tu conexión o Tomcat.</td></tr>';
            }
        });
}

function aplicarFiltrosParticipantes() {
    const texto = normString(filtroParticipante);
    const filtrados = participantesOriginales.filter(u => {
        const correo = getCorreoUsuario(u);
        return texto === '' || normString(getNombreCompleto(u)).includes(texto) || normString(correo).includes(texto);
    });
    renderParticipantes(filtrados);
}

function renderParticipantes(lista) {
    if (!tbodyParticipantes) return;
    if (!lista.length) {
        tbodyParticipantes.innerHTML = '<tr><td colspan="4" class="text-center text-muted py-4">No hay docentes asignados a este evento.</td></tr>';
        return;
    }

    const esDesarrollador = window.location.pathname.includes('_de.jsp');
    const sufijoRol = esDesarrollador ? '_de.jsp' : '_co.jsp';

    tbodyParticipantes.innerHTML = '';
    lista.forEach(u => {
        const tr = document.createElement('tr');
        const userId = getIdUsuario(u);
        const userCorreo = getCorreoUsuario(u);
        const estadoColor = u.activo === 1 ? '#28a745' : '#d32f2f';
        const estadoTexto = u.activo === 1 ? 'Activo' : 'Inactivo';

        tr.innerHTML = `
            <td>
              <div class="docente-name-container">
                <div class="avatar-circle"></div>
                <div class="docente-name">${escHtml(getNombreCompleto(u))}</div>
              </div>
            </td>
            <td>${escHtml(userCorreo)}</td>
            <td style="color:${estadoColor}; font-weight:600;">${estadoTexto}</td>
            <td>
              <a href="${contextPath}/ver_mas_evento${sufijoRol}?id=${idEvento}&usuarioId=${userId}" class="action-btn" title="Ver Detalles"><i class="bi bi-eye"></i></a>
              <a href="${contextPath}/cargar_archivo${sufijoRol}?id=${idEvento}&usuarioId=${userId}" class="action-btn" title="Cargar Archivo"><i class="bi bi-cloud-arrow-up"></i></a>
              <a href="#" class="action-btn delete btn-remover-participante" data-id="${userId}" title="Remover del evento"><i class="bi bi-trash"></i></a>
            </td>`;
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
            icon: 'warning',
            title: '¿Remover a este docente?',
            text: 'Se quitará su asignación al evento.',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#aaaaaa',
            confirmButtonText: 'Sí, remover'
        }).then(res => {
            if (res.isConfirmed) {
                const formData = new URLSearchParams();
                formData.append("idEvento", idEvento);
                formData.append("idUsuario", idUsuario);

                fetch(`${contextPath}/RemoverDocenteEventoServlet`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
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
    fetch(`${contextPath}/ListarUsuariosServlet?t=${Date.now()}`)
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

    const asignadosIds = participantesOriginales.map(p => Number(getIdUsuario(p)));
    const noAsignados = todosLosUsuarios.filter(u => !asignadosIds.includes(Number(getIdUsuario(u))));

    selectDocenteAAsignar.innerHTML = '';

    if (noAsignados.length === 0) {
        selectDocenteAAsignar.innerHTML = '<option value="" disabled selected>No hay docentes disponibles para asignar.</option>';
        return;
    }

    selectDocenteAAsignar.innerHTML = '<option value="" disabled selected>Selecciona un docente/coordinador...</option>';
    noAsignados.forEach(u => {
        const opt = document.createElement('option');
        const uId = getIdUsuario(u);
        opt.value = uId;
        opt.textContent = `${getNombreCompleto(u)} (${u.numeroEmpleado || 'S/N'})`;
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

        fetch(`${contextPath}/AsignarDocenteEventoServlet`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
            body: formData.toString()
        }).then(r => r.json()).then(data => {
            if (data.success) {
                Swal.fire({ icon: 'success', title: 'Asignado', timer: 1500, showConfirmButton: false });
                const modalEl = document.getElementById('modalAsignarDocente');
                if (modalEl && typeof bootstrap !== 'undefined') {
                    const modal = bootstrap.Modal.getInstance(modalEl);
                    if (modal) modal.hide();
                }
                cargarParticipantes();
            } else {
                Swal.fire({ icon: 'error', title: 'Error', text: data.message });
            }
        });
    });
}

// Iniciar
cargarEvento();