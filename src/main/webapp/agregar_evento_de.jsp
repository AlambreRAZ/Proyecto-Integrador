<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agregar Evento</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="gestion_eventos" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title">AGREGAR EVENTO</h3>

    <form id="formAgregarEvento" method="POST">
        <div class="row mb-3">
            <div class="col-md-4">
                <label class="form-label">Nombre del evento <span class="text-danger">*</span></label>
                <input type="text" class="form-control bg-white" name="nombre" placeholder="Nombre" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Lugar <span class="text-danger">*</span></label>
                <input type="text" class="form-control bg-white" name="lugar" placeholder="Lugar" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Institución / Empresa <span class="text-danger">*</span></label>
                <input type="text" class="form-control bg-white" name="institucion" placeholder="Institución/empresa" required>
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-4">
                <label class="form-label">Tipo de evento <span class="text-danger">*</span></label>
                <select class="form-select bg-white" name="tipo" required>
                    <option value="" disabled selected>Selecciona un tipo</option>
                    <option value="1">Taller</option>
                    <option value="2">Conferencia</option>
                </select>
            </div>
            <div class="col-md-8">
                <label class="form-label">Descripción del evento <span class="text-danger">*</span></label>
                <input type="text" class="form-control bg-white" name="descripcion" placeholder="Describe brevemente el evento..." required>
            </div>
        </div>

        <div class="row mb-5 align-items-end">
            <div class="col-md-3">
                <label class="form-label">Fecha de inicio <span class="text-danger">*</span></label>
                <input type="date" class="form-control bg-white" name="fecha_inicio" required>
            </div>
            <div class="col-md-3">
                <label class="form-label">Fecha de fin <span class="text-danger">*</span></label>
                <input type="date" class="form-control bg-white" name="fecha_fin" required>
            </div>
            <div class="col-md-6 custom-checkbox ps-md-4">
                <div class="modalidad-label">MODALIDAD</div>
                <div class="d-flex gap-4">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="modalidad" value="presencial" id="modPresencial" checked>
                        <label class="form-check-label fs-6" for="modPresencial">Presencial</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="modalidad" value="virtual" id="modVirtual">
                        <label class="form-check-label fs-6" for="modVirtual">Virtual</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="modalidad" value="mixta" id="modMixta">
                        <label class="form-check-label fs-6" for="modMixta">Mixta</label>
                    </div>
                </div>
            </div>
        </div>

        <h5 class="fw-bold mb-3" style="color: var(--teal-main);">Docentes Asignados</h5>
        
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div class="search-box mb-0" style="max-width: 500px;">
                <i class="bi bi-search"></i>
                <input type="text" placeholder="Buscar Docente por nombre, correo ...">
            </div>
            <button type="button" class="btn-teal-outline">Agregar docente</button>
        </div>

        <div class="data-card p-0 mb-4" style="overflow: hidden;">
            <table class="table-custom mb-0 text-center">
                <colgroup>
                    <col style="width: 35%;">
                    <col style="width: 35%;">
                    <col style="width: 15%;">
                    <col style="width: 15%;">
                </colgroup>
                <thead>
                    <tr>
                        <th class="text-start">Nombre</th>
                        <th>Correo</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="text-start">
                            <div class="docente-name-container">
                                <div class="avatar-circle" style="flex-shrink:0;"></div>
                                <div class="docente-name">
                                    Luis Gerardo<br>Barron Flores
                                </div>
                            </div>
                        </td>
                        <td>ejemplo@gmail.com</td>
                        <td><i class="bi bi-toggle-on text-success fs-4"></i></td>
                        <td style="white-space: nowrap;">
                            <a href="#" class="action-btn"><i class="bi bi-eye"></i></a>
                            <a href="#" class="action-btn delete"><i class="bi bi-trash"></i></a>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-start">
                            <div class="docente-name-container">
                                <div class="avatar-circle" style="flex-shrink:0;"></div>
                                <div class="docente-name">
                                    Luis Gerardo<br>Barron Flores
                                </div>
                            </div>
                        </td>
                        <td>ejemplo@gmail.com</td>
                        <td><i class="bi bi-toggle-on text-success fs-4"></i></td>
                        <td style="white-space: nowrap;">
                            <a href="#" class="action-btn"><i class="bi bi-eye"></i></a>
                            <a href="#" class="action-btn delete"><i class="bi bi-trash"></i></a>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-start">
                            <div class="docente-name-container">
                                <div class="avatar-circle" style="flex-shrink:0;"></div>
                                <div class="docente-name">
                                    Luis Gerardo<br>Barron Flores
                                </div>
                            </div>
                        </td>
                        <td>ejemplo@gmail.com</td>
                        <td><i class="bi bi-toggle-on text-success fs-4"></i></td>
                        <td style="white-space: nowrap;">
                            <a href="#" class="action-btn"><i class="bi bi-eye"></i></a>
                            <a href="#" class="action-btn delete"><i class="bi bi-trash"></i></a>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="d-flex justify-content-end gap-3 mb-5 mt-4">
            <a href="gestion_eventos_de.jsp" class="btn btn-outline-teal px-4 py-2 fw-semibold d-flex align-items-center" style="border: 2px solid var(--teal-main); color: var(--teal-main); border-radius: 6px;">
                <i class="bi bi-chevron-left me-2"></i> Volver
            </a>
            <button type="submit" class="btn-teal px-4 py-2" style="border-radius: 6px;">
                <i class="bi bi-save me-2"></i> Guardar Evento
            </button>
        </div>
    </form>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/coordinador.js"></script>
<script>
    document.getElementById('formAgregarEvento').addEventListener('submit', function(e) {
        e.preventDefault();
        Swal.fire({
            icon: 'success',
            title: '¡Evento Registrado con Éxito!',
            text: 'el evento se ha guardado correctamente',
            confirmButtonColor: '#00847b',
            confirmButtonText: 'Aceptar'
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = 'gestion_eventos_de.jsp';
            }
        });
    });
</script>
</body>
</html>
