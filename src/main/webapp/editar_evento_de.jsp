<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Evento</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="gestion_evento" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title">EDITAR EVENTO</h3>

    <form id="formEditarEvento" action="EditarEventoServlet" method="POST">
        <input type="hidden" id="idEvento" name="id" value="${evento.id}">

        <div class="row mb-3">
            <div class="col-md-4">
                <label class="form-label text-muted">Nombre del evento:</label>
                <input type="text" class="form-control" id="campoNombre" name="nombre" value="${evento.nombre}" required>
            </div>
            <div class="col-md-4">
                <label class="form-label text-muted">Lugar:</label>
                <input type="text" class="form-control" id="campoLugar" name="lugar" value="${evento.lugar}" required>
            </div>
            <div class="col-md-4">
                <label class="form-label text-muted">Institución / Empresa:</label>
                <input type="text" class="form-control" id="campoInstitucion" name="institucion" value="${evento.institucion}" required>
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-3">
                <label class="form-label text-muted">Tipo de evento:</label>
                <select class="form-select" id="campoTipo" name="tipo" required>
                    <option value="" disabled ${empty evento.tipo ? 'selected' : ''}>Selecciona un tipo</option>
                    <option value="Taller" ${evento.tipo == 'Taller' ? 'selected' : ''}>Taller</option>
                    <option value="Diplomado" ${evento.tipo == 'Diplomado' ? 'selected' : ''}>Diplomado</option>
                    <option value="Conferencia" ${evento.tipo == 'Conferencia' ? 'selected' : ''}>Conferencia</option>
                    <option value="Curso" ${evento.tipo == 'Curso' ? 'selected' : ''}>Curso</option>
                </select>
            </div>
            <div class="col-md-3">
                <label class="form-label text-muted">División:</label>
                <select class="form-select" id="campoDivision" name="division" required>
                    <option value="" disabled selected>Selecciona división</option>
                    <option value="1" ${evento.idDivision == 1 || evento.division == 'DATID' ? 'selected' : ''}>DATID</option>
                    <option value="2" ${evento.idDivision == 2 || evento.division == 'DACEA' ? 'selected' : ''}>DACEA</option>
                    <option value="3" ${evento.idDivision == 3 || evento.division == 'DATEFI' ? 'selected' : ''}>DATEFI</option>
                    <option value="4" ${evento.idDivision == 4 || evento.division == 'DAMI' ? 'selected' : ''}>DAMI</option>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label text-muted">Descripción del evento:</label>
                <input type="text" class="form-control" id="campoDescripcion" name="descripcion" value="${evento.descripcion}" required>
            </div>
        </div>

        <div class="row mb-5 align-items-end">
            <div class="col-md-3">
                <label class="form-label text-muted fw-semibold mb-2">Fecha de inicio</label>
                <input type="date" class="form-control" id="campoFechaInicio" name="fechaInicio" value="${evento.fechaInicio}" required>
            </div>
            <div class="col-md-3">
                <label class="form-label text-muted fw-semibold mb-2">Fecha de termino</label>
                <input type="date" class="form-control" id="campoFechaFin" name="fechaFin" value="${evento.fechaFin}" required>
            </div>
            <div class="col-md-6 custom-checkbox ps-md-4">
                <div class="modalidad-label">MODALIDAD</div>
                <div class="d-flex gap-4">
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="modalidad" value="Presencial" id="modPresencial" ${evento.modalidad == 'Presencial' ? 'checked' : ''}>
                        <label class="form-check-label fs-6" for="modPresencial">Presencial</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="modalidad" value="Virtual" id="modVirtual" ${evento.modalidad == 'Virtual' ? 'checked' : ''}>
                        <label class="form-check-label fs-6" for="modVirtual">Virtual</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="modalidad" value="Mixta" id="modMixta" ${empty evento.modalidad || evento.modalidad == 'Mixta' ? 'checked' : ''}>
                        <label class="form-check-label fs-6" for="modMixta">Mixta</label>
                    </div>
                </div>
            </div>
        </div>

        <h5 class="fw-bold mb-3" style="color: var(--teal-main);">Docentes Asignados</h5>

        <div class="d-flex justify-content-between align-items-center mb-4">
            <div class="search-box mb-0" style="max-width: 500px;">
                <i class="bi bi-search"></i>
                <input type="text" id="inputBuscarDocente" onkeyup="filtrarTablaDocentes()" placeholder="Buscar Docente por nombre, correo ...">
            </div>
            <button type="button" class="btn-teal-outline" data-bs-toggle="modal" data-bs-target="#modalAgregarDocente">
                <i class="bi bi-plus-lg me-1"></i> Agregar docente
            </button>
        </div>

        <div class="data-card p-0 mb-4" style="overflow: hidden;">
            <table class="table-custom mb-0">
                <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Correo</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody id="tablaDocentesBody">
                <!-- Se puebla dinámicamente -->
                </tbody>
            </table>
        </div>

        <div class="d-flex justify-content-end gap-3 mb-5">
            <a href="gestion_eventos_de.jsp" class="btn btn-outline-teal px-4 py-2 fw-semibold d-flex align-items-center" style="border: 2px solid var(--teal-main); color: var(--teal-main); border-radius: 6px;">
                <i class="bi bi-chevron-left me-2"></i> Volver
            </a>
            <button type="submit" id="btnConfirmar" class="btn-teal px-4 py-2" style="border-radius: 6px;">
                <i class="bi bi-save me-2"></i> Confirmar
            </button>
        </div>
    </form>
</main>

<!-- Modal con Buscador Dinámico Filtrable -->
<div class="modal fade" id="modalAgregarDocente" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title fw-bold">Agregar Docente al Evento</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3 position-relative">
                    <label for="buscarDocenteInput" class="form-label text-muted">Buscar Docente:</label>
                    <div class="input-group">
                        <span class="input-group-text bg-white"><i class="bi bi-search"></i></span>
                        <input type="text" id="buscarDocenteInput" class="form-control" placeholder="Escribe nombre o correo..." oninput="filtrarListaDocentes()" onclick="filtrarListaDocentes()" onfocus="filtrarListaDocentes()">
                    </div>
                    <div id="listaDocentesContainer" class="list-group position-absolute w-100 shadow mt-1" style="max-height: 200px; overflow-y: auto; z-index: 1050; display: none;"></div>
                </div>
                <input type="hidden" id="docenteSeleccionadoId">
                <div id="infoDocenteSeleccionado" class="alert alert-success d-none py-2 px-3 m-0">
                    <small class="fw-bold">Seleccionado:</small> <span id="nombreDocenteSeleccionado"></span>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn-teal" style="border-radius: 4px;" onclick="asignarDocenteASeleccionados()">Agregar</button>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    window.contextPath = '<%= request.getContextPath() %>';
    let docentesListaGlobal = [];

    // CORREGIDO: /ListarDocente devuelve JSON, no HTML.
    // Antes se parseaba como pagina HTML buscando <tbody><tr>, por eso la lista salia vacia.
    function cargarDocentesRegistrados() {
        return fetch(window.contextPath + '/ListarDocente?t=' + Date.now(), { credentials: 'same-origin' })
            .then(res => {
                if (!res.ok) throw new Error('HTTP ' + res.status);
                return res.json();
            })
            .then(data => {
                docentesListaGlobal = (data || []).map(d => ({
                    id: d.id,
                    nombre: [d.nombre, d.apellidoPaterno, d.apellidoMaterno].filter(Boolean).join(' '),
                    correo: d.correo || '',
                    numEmpleado: d.numeroEmpleado || ''
                }));
                console.log("Docentes cargados:", docentesListaGlobal.length);
            })
            .catch(err => {
                console.error("Error al obtener lista de docentes:", err);
                docentesListaGlobal = [];
            });
    }

    // NUEVO: carga en la tabla los docentes que YA estan asignados al evento,
    // para que al recargar la pagina no se vean vacios.
    function cargarParticipantesDelEvento(idEvento) {
        if (!idEvento) return;
        fetch(window.contextPath + '/ListarParticipantesEventoServlet?id=' + encodeURIComponent(idEvento) + '&t=' + Date.now(),
            { credentials: 'same-origin' })
            .then(res => res.ok ? res.json() : [])
            .then(lista => {
                (lista || []).forEach(u => {
                    const nombre = [u.nombre, u.apellidoPaterno, u.apellidoMaterno].filter(Boolean).join(' ');
                    agregarFilaTablaDocente(u.id, nombre, u.correo || '');
                });
            })
            .catch(err => console.error("Error al cargar participantes:", err));
    }

    document.addEventListener("DOMContentLoaded", function() {
        cargarDocentesRegistrados();

        // 1. Cargar datos del evento desde la URL
        const urlParams = new URLSearchParams(window.location.search);
        const idURL = urlParams.get('id');
        const inputId = document.getElementById('idEvento');

        // Cargar los docentes ya asignados a este evento
        cargarParticipantesDelEvento(idURL || inputId.value);

        if (idURL && (!inputId.value || inputId.value === '')) {
            inputId.value = idURL;
            fetch(window.contextPath + '/EditarEventoServlet?id=' + idURL)
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        if (data.id) inputId.value = data.id;
                        document.getElementById('campoNombre').value = data.nombre || '';
                        document.getElementById('campoLugar').value = data.lugar || '';
                        document.getElementById('campoInstitucion').value = data.institucion || '';
                        // Selecciona el tipo sin importar mayusculas/minusculas
                        const selTipo = document.getElementById('campoTipo');
                        const tipoBuscado = String(data.tipo || '').toLowerCase();
                        for (const opt of selTipo.options) {
                            if (opt.value.toLowerCase() === tipoBuscado) { selTipo.value = opt.value; break; }
                        }
                        document.getElementById('campoDescripcion').value = data.descripcion || '';
                        document.getElementById('campoFechaInicio').value = data.fechaInicio || '';
                        document.getElementById('campoFechaFin').value = data.fechaFin || '';
                        if (data.idDivision) document.getElementById('campoDivision').value = data.idDivision;

                        if (data.modalidad) {
                            const modBuscada = String(data.modalidad).toLowerCase();
                            document.querySelectorAll('input[name="modalidad"]').forEach(r => {
                                if (r.value.toLowerCase() === modBuscada) r.checked = true;
                            });
                        }
                    }
                })
                .catch(err => console.error("Error al obtener evento:", err));
        }

        // 2. Evento para abrir el modal desplegando todos los docentes por defecto
        const modalAgregar = document.getElementById('modalAgregarDocente');
        if (modalAgregar) {
            modalAgregar.addEventListener('shown.bs.modal', function () {
                document.getElementById('buscarDocenteInput').value = '';
                document.getElementById('docenteSeleccionadoId').value = '';
                document.getElementById('infoDocenteSeleccionado').classList.add('d-none');
                filtrarListaDocentes();
            });
        }

        // 3. Interceptor para actualizar evento
        document.getElementById('formEditarEvento').addEventListener('submit', function (e) {
            e.preventDefault();

            const idEvento = document.getElementById('idEvento').value;
            const nombre = document.getElementById('campoNombre').value.trim();
            const idDivision = document.getElementById('campoDivision').value;

            if (!idEvento || idEvento === "0" || !nombre || !idDivision) {
                Swal.fire('Error de validación', 'Verifica los campos obligatorios y selecciona una división.', 'error');
                return;
            }

            const formData = new URLSearchParams(new FormData(this));

            document.querySelectorAll('#tablaDocentesBody tr').forEach(tr => {
                const idDoc = tr.getAttribute('data-id-docente');
                if (idDoc) formData.append('docentes', idDoc);
            });

            fetch(window.contextPath + '/EditarEventoServlet', {
                method: 'POST',
                body: formData,
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' }
            })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        Swal.fire('¡Éxito!', 'Evento actualizado correctamente', 'success').then(() => {
                            window.location.href = 'gestion_eventos_de.jsp';
                        });
                    } else {
                        Swal.fire('Error al actualizar', data.message || 'Error al guardar cambios.', 'error');
                    }
                })
                .catch(err => {
                    console.error("Error Fetch:", err);
                    Swal.fire('Error', 'Problema al comunicarse con el servidor', 'error');
                });
        });
    });

    // Muestra todos los docentes o los filtra según la búsqueda
    function filtrarListaDocentes() {
        const query = document.getElementById('buscarDocenteInput').value.toLowerCase().trim();
        const container = document.getElementById('listaDocentesContainer');
        container.innerHTML = '';

        const filtrados = query === ''
            ? docentesListaGlobal
            : docentesListaGlobal.filter(d => {
                const nombre = (d.nombre || '').toLowerCase();
                const correo = (d.correo || '').toLowerCase();
                const numEmp = (d.numEmpleado || '').toString().toLowerCase();
                return nombre.includes(query) || correo.includes(query) || numEmp.includes(query);
            });

        if (filtrados.length === 0) {
            container.innerHTML = '<div class="list-group-item text-muted small py-2">No se encontraron docentes</div>';
        } else {
            filtrados.forEach(docente => {
                const item = document.createElement('button');
                item.type = 'button';
                item.className = 'list-group-item list-group-item-action d-flex justify-content-between align-items-center text-start py-2';
                item.innerHTML =
                    '<div>' +
                    '<div class="fw-semibold text-dark">' + docente.nombre + '</div>' +
                    '<small class="text-muted">' + docente.correo +
                    (docente.numEmpleado ? ' | Emp: ' + docente.numEmpleado : '') + '</small>' +
                    '</div>';
                item.onclick = function() { seleccionarDocenteDeLista(docente); };
                container.appendChild(item);
            });
        }
        container.style.display = 'block';
    }

    function seleccionarDocenteDeLista(docente) {
        document.getElementById('docenteSeleccionadoId').value = docente.id;
        document.getElementById('buscarDocenteInput').value = docente.nombre;
        document.getElementById('listaDocentesContainer').style.display = 'none';

        const infoDiv = document.getElementById('infoDocenteSeleccionado');
        document.getElementById('nombreDocenteSeleccionado').textContent =
            docente.nombre + ' (' + (docente.correo || '') + ')';
        infoDiv.classList.remove('d-none');
    }

    function asignarDocenteASeleccionados() {
        const idDocente = document.getElementById('docenteSeleccionadoId').value;

        if (!idDocente) {
            Swal.fire('Atención', 'Selecciona un docente válido de la lista desplegable.', 'warning');
            return;
        }

        const docenteObj = docentesListaGlobal.find(d => d.id == idDocente);
        const nombreDocente = docenteObj ? docenteObj.nombre : '';
        const correoDocente = docenteObj ? docenteObj.correo : '';

        agregarFilaTablaDocente(idDocente, nombreDocente, correoDocente);

        document.getElementById('docenteSeleccionadoId').value = '';
        document.getElementById('buscarDocenteInput').value = '';
        document.getElementById('infoDocenteSeleccionado').classList.add('d-none');

        const modalElement = document.getElementById('modalAgregarDocente');
        if (modalElement) {
            const modal = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
            modal.hide();
        }
    }

    // IMPORTANTE: nada de template literals (backticks) dentro de un JSP.
    // El motor EL del JSP evalua esas expresiones en el servidor y las borra.
    function agregarFilaTablaDocente(id, nombre, correo) {
        const tbody = document.getElementById('tablaDocentesBody');
        const filaId = 'tr-docente-' + id;
        if (document.getElementById(filaId)) return; // ya estaba en la tabla

        const tr = document.createElement('tr');
        tr.id = filaId;
        tr.setAttribute('data-id-docente', id);
        tr.innerHTML =
            '<td>' +
            '<div class="docente-name-container">' +
            '<div class="avatar-circle"></div>' +
            '<div class="docente-name">' + nombre + '</div>' +
            '</div>' +
            '</td>' +
            '<td>' + correo + '</td>' +
            '<td class="status-active">Activo</td>' +
            '<td>' +
            '<a href="javascript:void(0)" class="action-btn delete" title="Quitar del evento">' +
            '<i class="bi bi-trash"></i>' +
            '</a>' +
            '</td>';

        // El listener se enlaza aqui, no con onclick inline
        const btnBorrar = tr.querySelector('.action-btn.delete');
        if (btnBorrar) {
            btnBorrar.addEventListener('click', function () { tr.remove(); });
        }

        tbody.appendChild(tr);
    }
</script>
</body>
</html>
