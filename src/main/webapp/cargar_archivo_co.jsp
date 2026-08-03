<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cargar Archivo</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
    <style>
        .form-control[readonly] {
            background-color: #f8f9fa;
            opacity: 1;
        }
        .constancia-card {
            border: 2px solid var(--teal-main);
            border-radius: 16px;
            padding: 30px;
            background: linear-gradient(135deg, #f0fdfb 0%, #e6f7f5 100%);
            display: none;
        }
        .constancia-file-icon {
            font-size: 3rem;
            color: #e74c3c;
        }
        .constancia-meta {
            font-size: 0.9rem;
            color: #6c757d;
        }
        .vencido-banner {
            background: #fee2e2;
            border: 1px solid #fca5a5;
            border-radius: 8px;
            padding: 12px 16px;
            color: #991b1b;
            font-size: 0.9rem;
        }
    </style>
</head>
<body>

<jsp:include page="sidebar.jsp">
    <jsp:param name="active" value="gestion_evento" />
</jsp:include>

<main class="main-content">
    <div class="mb-4">
        <h3 class="page-title mb-3" id="tituloEvento">EVENTO</h3>
    </div>

    <!-- Event Info Card -->
    <div class="info-card-outline mb-4" style="border-color: var(--teal-main); padding: 25px;">
        <div class="row mb-3">
            <div class="col-md-4">
                <div class="text-muted mb-1" style="font-size: 0.9rem;">Tipo de evento:</div>
                <div class="fs-6 text-dark" id="campoTipo">-</div>
            </div>
            <div class="col-md-4">
                <div class="text-muted mb-1" style="font-size: 0.9rem;">Lugar:</div>
                <div class="fs-6 text-dark" id="campoLugar">-</div>
            </div>
            <div class="col-md-4">
                <div class="text-muted mb-1" style="font-size: 0.9rem;">Institución / Empresa:</div>
                <div class="fs-6 text-dark" id="campoInstitucion">-</div>
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-12">
                <div class="text-muted mb-1" style="font-size: 0.9rem;">Descripción del evento:</div>
                <div class="fs-6 text-dark" id="campoDescripcion">-</div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-4">
                <div class="text-muted mb-1" style="font-size: 0.9rem;">Fecha de inicio:</div>
                <div class="fs-6 text-dark" id="campoFechaInicio">-</div>
            </div>
            <div class="col-md-4">
                <div class="text-muted mb-1" style="font-size: 0.9rem;">Fecha de fin:</div>
                <div class="fs-6 text-dark" id="campoFechaFin">-</div>
            </div>
            <div class="col-md-4">
                <div class="text-muted mb-1" style="font-size: 0.9rem;">Modalidad</div>
                <div class="fs-6 text-dark" id="campoModalidad">-</div>
            </div>
        </div>
    </div>

    <!-- Panel cuando YA EXISTE una constancia (oculto por defecto) -->
    <div class="constancia-card mb-5" id="constanciaCard">
        <h4 class="fw-bold mb-4" style="color: var(--teal-main);">Archivo cargado</h4>
        <div class="d-flex align-items-center gap-4 flex-wrap">
            <div class="constancia-file-icon">
                <i class="bi bi-file-earmark-pdf-fill"></i>
            </div>
            <div class="flex-grow-1">
                <div class="fw-bold fs-5 mb-1" id="constanciaNombre"></div>
                <div class="constancia-meta">Subido el: <span id="constanciaFechaSubida"></span></div>
                <div class="constancia-meta" id="constanciaVigenciaWrap" style="display:none;">Vigencia hasta: <span id="constanciaVigencia"></span></div>
            </div>
            <div class="d-flex gap-2 flex-wrap">
                <a id="btnVerArchivo" href="#" target="_blank" class="btn-teal d-inline-flex align-items-center gap-2 px-4 py-2" style="border-radius: 8px; text-decoration: none;">
                    <i class="bi bi-eye"></i> Ver archivo
                </a>
                <button id="btnCancelarEntrega" class="btn btn-outline-danger d-inline-flex align-items-center gap-2 px-4 py-2" style="border-radius: 8px;">
                    <i class="bi bi-x-circle"></i> Cancelar entrega
                </button>
            </div>
        </div>
        <div class="vencido-banner mt-4" id="vencidoBanner" style="display:none;">
            <i class="bi bi-lock-fill me-2"></i>
            <strong>Plazo vencido.</strong> Ya no es posible modificar ni cancelar esta entrega.
        </div>
    </div>

    <!-- Formulario de carga (OCULTO por defecto, JS decide cuál mostrar) -->
    <form id="formCargaArchivo" style="display:none;">
        <input type="hidden" name="idEvento" id="hiddenIdEvento" value="">
        
        <div class="data-card mb-5" style="padding: 25px;">
            <h4 class="fw-bold mb-4" style="color: var(--teal-main);">Cargar archivo</h4>
            
            <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap">
                <div class="d-flex align-items-center gap-3">
                    <span class="fw-medium">¿Tiene vigencia?:</span>
                    <div class="form-check mb-0">
                        <input class="form-check-input" type="radio" name="vigencia" id="vigenciaNo" value="no" checked>
                        <label class="form-check-label" for="vigenciaNo">No</label>
                    </div>
                    <div class="form-check mb-0">
                        <input class="form-check-input" type="radio" name="vigencia" id="vigenciaSi" value="si">
                        <label class="form-check-label" for="vigenciaSi">Si</label>
                    </div>
                </div>
                
                <div class="d-flex align-items-center gap-3">
                    <span class="fw-medium">Fecha de Vigencia:</span>
                    <input type="date" name="fechaVencimiento" id="fechaVencimiento" class="form-control" style="width: auto; border-radius: 8px;" disabled>
                </div>
            </div>

            <!-- Upload Zone -->
            <div class="upload-zone text-center p-5 mt-4" id="uploadZone" style="border: 2px dashed #444; border-radius: 12px; background-color: transparent; position: relative;">
                <input type="file" name="archivo" id="archivoPdf" accept="application/pdf" style="opacity: 0; position: absolute; top: 0; left: 0; width: 100%; height: 100%; cursor: pointer;">
                <div class="mb-3">
                    <div style="display: inline-flex; justify-content: center; align-items: center; width: 60px; height: 40px; background-color: var(--teal-main); border-radius: 30px 30px 10px 10px; color: white;">
                        <i class="bi bi-arrow-up-short fs-2"></i>
                    </div>
                </div>
                <button type="button" class="btn-teal px-4 py-2 mb-3" style="border-radius: 20px;">Explorar</button>
                <div class="text-muted small" id="nombreArchivoTexto">Selecciona el Archivo a subir (.pdf)</div>
            </div>

            <!-- Vista del archivo seleccionado (oculta hasta que eliges un archivo) -->
            <div id="archivoSeleccionadoInfo" style="display:none; border-radius: 10px; background: #f0fdfb;" class="d-flex align-items-center gap-3 mt-4 p-3">
                <i class="bi bi-file-earmark-pdf-fill text-danger fs-2"></i>
                <div>
                    <div class="fw-bold" id="archivoSeleccionadoNombre"></div>
                    <div class="text-muted small">Listo para subir</div>
                </div>
                <button type="button" class="btn btn-sm btn-outline-secondary ms-auto" id="btnQuitarArchivo">✕ Quitar</button>
            </div>
        </div>

        <div class="d-flex justify-content-center justify-content-md-end gap-3 mb-5">
            <a href="gestion_evento_co.jsp" class="btn btn-outline-teal px-5 py-2 fw-semibold d-flex align-items-center" style="border: 2px solid var(--teal-main); color: var(--teal-main); border-radius: 6px;">
                <i class="bi bi-chevron-left me-2"></i> Volver
            </a>
            <button type="submit" class="btn-teal px-5 py-2" style="border-radius: 6px;">Cargar Archivo</button>
        </div>
    </form>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="assets/js/coordinador.js"></script>
<script>
    const contextPath = '<%= request.getContextPath() %>';
    const params = new URLSearchParams(window.location.search);
    const idEvento = params.get('id');
    let constanciaIdActual = null;
    let eventoFechaFin = null;
    
    if (idEvento) {
        document.getElementById('hiddenIdEvento').value = idEvento;
    } else {
        Swal.fire('Error', 'No se especificó un evento válido', 'error').then(() => {
            window.location.href = 'mi_evento_co.jsp';
        });
    }

    function aFechaVisible(iso) {
        if (!iso) return '';
        const partes = iso.split('-');
        if (partes.length !== 3) return iso;
        return partes[2] + '/' + partes[1] + '/' + partes[0].slice(2);
    }

    function capitalizar(texto) {
        if (!texto) return '';
        return texto.charAt(0).toUpperCase() + texto.slice(1);
    }

    function mostrarFormulario() {
        document.getElementById('formCargaArchivo').style.display = '';
        document.getElementById('constanciaCard').style.display = 'none';
    }

    function mostrarConstancia(c, estaVencido) {
        document.getElementById('formCargaArchivo').style.display = 'none';
        const card = document.getElementById('constanciaCard');
        card.style.display = '';

        constanciaIdActual = c.idConstancia;
        document.getElementById('constanciaNombre').textContent = c.nombreArchivo;
        document.getElementById('constanciaFechaSubida').textContent = c.fechaSubida;

        if (c.tieneVigencia === 1 && c.fechaVencimiento) {
            document.getElementById('constanciaVigenciaWrap').style.display = '';
            document.getElementById('constanciaVigencia').textContent = c.fechaVencimiento;
        }

        // Botón ver archivo
        document.getElementById('btnVerArchivo').href = contextPath + '/' + c.rutaArchivo;

        if (estaVencido) {
            document.getElementById('vencidoBanner').style.display = '';
            document.getElementById('btnCancelarEntrega').disabled = true;
            document.getElementById('btnCancelarEntrega').title = 'El plazo del evento ha vencido';
        }
    }

    // Paso 1: Cargar datos del evento
    async function inicializarPagina() {
        try {
            // Cargar info del evento
            const resEvento = await fetch(contextPath + '/EditarEventoServlet?id=' + encodeURIComponent(idEvento) + '&t=' + Date.now());
            const dataEvento = await resEvento.json();
            if (dataEvento.success) {
                document.getElementById('tituloEvento').textContent = (dataEvento.nombre || '').toUpperCase();
                document.getElementById('campoTipo').textContent = capitalizar(dataEvento.tipo);
                document.getElementById('campoLugar').textContent = dataEvento.lugar || '';
                document.getElementById('campoInstitucion').textContent = dataEvento.institucion || '';
                document.getElementById('campoDescripcion').textContent = dataEvento.descripcion || '';
                document.getElementById('campoFechaInicio').textContent = aFechaVisible(dataEvento.fechaInicio);
                document.getElementById('campoFechaFin').textContent = aFechaVisible(dataEvento.fechaFin);
                document.getElementById('campoModalidad').textContent = capitalizar(dataEvento.modalidad);

                if (dataEvento.fechaFin) {
                    const p = dataEvento.fechaFin.split('-');
                    eventoFechaFin = new Date(p[0], p[1] - 1, p[2]);
                    eventoFechaFin.setHours(23, 59, 59);
                }
            }
        } catch (err) {
            console.error('Error al cargar evento:', err);
        }

        // Paso 2: Verificar si ya existe constancia
        try {
            const resConst = await fetch(contextPath + '/ObtenerConstanciaServlet?idEvento=' + encodeURIComponent(idEvento) + '&t=' + Date.now());
            const result = await resConst.json();
            const estaVencido = eventoFechaFin ? new Date() > eventoFechaFin : false;

            if (result && result.success && result.constancia) {
                mostrarConstancia(result.constancia, estaVencido);
            } else {
                mostrarFormulario();
                if (estaVencido) {
                    document.getElementById('formCargaArchivo').querySelectorAll('input, button[type="submit"]').forEach(el => el.disabled = true);
                    document.getElementById('uploadZone').style.opacity = '0.4';
                    document.getElementById('uploadZone').style.pointerEvents = 'none';
                    const warn = document.createElement('div');
                    warn.className = 'vencido-banner mt-3';
                    warn.innerHTML = '<i class="bi bi-lock-fill me-2"></i><strong>Plazo vencido.</strong> Ya no es posible subir constancias para este evento.';
                    document.getElementById('formCargaArchivo').querySelector('.data-card').appendChild(warn);
                }
            }
        } catch (err) {
            console.error('Error al verificar constancia:', err);
            // Si falla la consulta de constancia, al menos mostramos el formulario
            mostrarFormulario();
        }
    }

    inicializarPagina();

    // Botón cancelar entrega
    document.getElementById('btnCancelarEntrega').addEventListener('click', () => {
        if (!constanciaIdActual) return;
        Swal.fire({
            title: '¿Cancelar entrega?',
            text: 'Se eliminará el archivo que subiste. Esta acción no se puede deshacer.',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#e74c3c',
            cancelButtonColor: '#6c757d',
            confirmButtonText: 'Sí, cancelar entrega',
            cancelButtonText: 'No, mantener'
        }).then(result => {
            if (!result.isConfirmed) return;
            const fd = new FormData();
            fd.append('idConstancia', constanciaIdActual);
            fd.append('idEvento', idEvento);
            fetch(contextPath + '/CancelarConstanciaServlet', { method: 'POST', body: fd })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        Swal.fire('¡Cancelado!', data.message, 'success').then(() => location.reload());
                    } else {
                        Swal.fire('Error', data.message || 'No se pudo cancelar', 'error');
                    }
                })
                .catch(() => Swal.fire('Error', 'Problema de conexión', 'error'));
        });
    });

    // Lógica de vigencia
    const vigenciaSi = document.getElementById('vigenciaSi');
    const vigenciaNo = document.getElementById('vigenciaNo');
    const fechaVencimiento = document.getElementById('fechaVencimiento');

    vigenciaSi.addEventListener('change', () => {
        fechaVencimiento.disabled = false;
        fechaVencimiento.required = true;
    });

    vigenciaNo.addEventListener('change', () => {
        fechaVencimiento.disabled = true;
        fechaVencimiento.required = false;
        fechaVencimiento.value = '';
    });

    // Al seleccionar archivo: ocultar la zona y mostrar el nombre
    const archivoPdf = document.getElementById('archivoPdf');
    const uploadZone = document.getElementById('uploadZone');
    const archivoSeleccionadoInfo = document.getElementById('archivoSeleccionadoInfo');
    const archivoSeleccionadoNombre = document.getElementById('archivoSeleccionadoNombre');

    archivoPdf.addEventListener('change', (e) => {
        if (e.target.files.length > 0) {
            uploadZone.style.display = 'none';
            archivoSeleccionadoNombre.textContent = e.target.files[0].name;
            archivoSeleccionadoInfo.style.display = 'flex';
        }
    });

    document.getElementById('btnQuitarArchivo').addEventListener('click', () => {
        archivoPdf.value = '';
        uploadZone.style.display = '';
        archivoSeleccionadoInfo.style.display = 'none';
    });

    // Form submit AJAX
    document.getElementById('formCargaArchivo').addEventListener('submit', function(e) {
        e.preventDefault();
        
        if (archivoPdf.files.length === 0) {
            Swal.fire('Advertencia', 'Debes seleccionar un archivo PDF', 'warning');
            return;
        }

        const formData = new FormData(this);

        if (vigenciaSi.checked && !fechaVencimiento.value) {
            Swal.fire('Advertencia', 'Debes elegir una fecha de vigencia', 'warning');
            return;
        }

        Swal.fire({
            title: 'Subiendo archivo...',
            allowOutsideClick: false,
            didOpen: () => Swal.showLoading()
        });

        fetch(contextPath + '/SubirConstanciaServlet', {
            method: 'POST',
            body: formData
        })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                Swal.fire('¡Éxito!', data.message, 'success').then(() => location.reload());
            } else {
                Swal.fire('Error', data.message || 'Ocurrió un error', 'error');
            }
        })
        .catch(err => {
            console.error(err);
            Swal.fire('Error', 'Problema de conexión al subir', 'error');
        });
    });
</script>
</body>
</html>
