<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

<jsp:include page="sidebar.jsp">
    <jsp:param name="active" value="gestion_evento" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title">EDITAR EVENTO</h3>

    <form id="formEditarEvento" action="#" method="POST">
        <div class="row mb-3">
            <div class="col-md-4">
                <label class="form-label text-muted">Nombre del evento:</label>
                <input type="text" class="form-control" id="campoNombre" name="nombre" value="Introducion a redes" required>
            </div>
            <div class="col-md-4">
                <label class="form-label text-muted">Lugar:</label>
                <input type="text" class="form-control" id="campoLugar" name="lugar" value="CDMX" required>
            </div>
            <div class="col-md-4">
                <label class="form-label text-muted">Institución / Empresa:</label>
                <input type="text" class="form-control" id="campoInstitucion" name="institucion" value="Academia de formacion profesional del estado" required>
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-4">
                <label class="form-label text-muted">Tipo de evento:</label>
                <select class="form-select" id="campoTipo" name="tipo" required>
                    <option value="" disabled>Selecciona un tipo</option>
                    <option value="Taller">Taller</option>
                    <option value="Diplomado" selected>Diplomado</option>
                </select>
            </div>
            <div class="col-md-8">
                <label class="form-label text-muted">Descripción del evento:</label>
                <input type="text" class="form-control" id="campoDescripcion" name="descripcion" value="Gran evento de introducion a redes para los futuros rederos" required>
            </div>
        </div>

        <div class="row mb-5 align-items-end">
            <div class="col-md-3">
                <label class="form-label text-muted">Fecha de inicio:</label>
                <input type="text" class="form-control" id="campoFechaInicio" name="fecha_inicio" value="05/05/26" required>
            </div>
            <div class="col-md-3">
                <label class="form-label text-muted">Fecha de fin:</label>
                <input type="text" class="form-control" id="campoFechaFin" name="fecha_fin" value="22/05/26" required>
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
            <table class="table-custom mb-0">
                <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Correo</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <div class="docente-name-container">
                            <div class="avatar-circle"></div>
                            <div class="docente-name">
                                Luis Gerardo<br>Barron Flores
                            </div>
                        </div>
                    </td>
                    <td>ejemplo@gmail.com</td>
                    <td class="status-active">Activo</td>
                    <td>
                        <a href="#" class="action-btn"><i class="bi bi-eye"></i></a>
                        <a href="#" class="action-btn delete"><i class="bi bi-trash"></i></a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="docente-name-container">
                            <div class="avatar-circle"></div>
                            <div class="docente-name">
                                Luis Gerardo<br>Barron Flores
                            </div>
                        </div>
                    </td>
                    <td>ejemplo@gmail.com</td>
                    <td class="status-active">Activo</td>
                    <td>
                        <a href="#" class="action-btn"><i class="bi bi-eye"></i></a>
                        <a href="#" class="action-btn delete"><i class="bi bi-trash"></i></a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="docente-name-container">
                            <div class="avatar-circle"></div>
                            <div class="docente-name">
                                Luis Gerardo<br>Barron Flores
                            </div>
                        </div>
                    </td>
                    <td>ejemplo@gmail.com</td>
                    <td class="status-active">Activo</td>
                    <td>
                        <a href="#" class="action-btn"><i class="bi bi-eye"></i></a>
                        <a href="#" class="action-btn delete"><i class="bi bi-trash"></i></a>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="d-flex justify-content-end gap-3 mb-5">
            <a href="gestion_evento_co.jsp" class="btn btn-outline-teal px-4 py-2 fw-semibold d-flex align-items-center" style="border: 2px solid var(--teal-main); color: var(--teal-main); border-radius: 6px;">
                <i class="bi bi-chevron-left me-2"></i> Volver
            </a>
            <button type="submit" class="btn-teal px-4 py-2" style="border-radius: 6px;">
                <i class="bi bi-save me-2"></i> Confirmar
            </button>
        </div>
    </form>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>window.contextPath = '<%= request.getContextPath() %>';</script>
<script src="assets/js/coordinador.js"></script>
<script src="assets/js/EditarEvento.js"> </script>
</body>
</html>