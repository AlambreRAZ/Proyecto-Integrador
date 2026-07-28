<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Docentes</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
</head>
<body>

<jsp:include page="sidebar_de.jsp">
    <jsp:param name="active" value="gestion_usuarios" />
    <jsp:param name="active_sub" value="docente" />
</jsp:include>

<main class="main-content">
    <h3 class="page-title mb-4">GESTION DE DOCENTES</h3>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="search-box mb-0" style="max-width: 600px; flex-grow: 1; margin-right: 20px;">
            <i class="bi bi-search"></i>
            <input type="text" placeholder="Buscar Docente por nombre, correo ...">
        </div>
        <a href="agregar_docente_de.jsp" class="btn-teal">
            <i class="bi bi-person-plus"></i> Agregar Docente
        </a>
    </div>

    <div class="data-card p-0 mb-4" style="overflow: hidden;">
        <table class="table-custom mb-0 text-center">
            <colgroup>
                <col style="width: 25%;">
                <col style="width: 25%;">
                <col style="width: 15%;">
                <col style="width: 15%;">
                <col style="width: 10%;">
                <col style="width: 10%;">
            </colgroup>
            <thead>
                <tr>
                    <th class="text-start">Nombre</th>
                    <th>Correo</th>
                    <th>Division</th>
                    <th>Num. Empleado</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td class="text-start">
                        <div class="docente-name-container">
                            <div class="avatar-circle" style="flex-shrink:0;"></div>
                            <div class="docente-name" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">
                                Luis Gerardo Barron Flores
                            </div>
                        </div>
                    </td>
                    <td>ejemplo@gmail.com</td>
                    <td>DATID</td>
                    <td>67</td>
                    <td><i class="bi bi-toggle-on text-success fs-4"></i></td>
                    <td style="white-space: nowrap;">
                        <a href="editar_docente_de.jsp" class="action-btn" title="Editar"><i class="bi bi-pencil"></i></a>
                        <a href="#" class="action-btn" title="Ver"><i class="bi bi-eye"></i></a>
                        <a href="#" class="action-btn delete" title="Eliminar"><i class="bi bi-trash"></i></a>
                    </td>
                </tr>
                <tr>
                    <td class="text-start">
                        <div class="docente-name-container">
                            <div class="avatar-circle" style="flex-shrink:0;"></div>
                            <div class="docente-name" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">
                                Luis Gerardo Barron Flores
                            </div>
                        </div>
                    </td>
                    <td>ejemplo@gmail.com</td>
                    <td>DATID</td>
                    <td>67</td>
                    <td><i class="bi bi-toggle-on text-success fs-4"></i></td>
                    <td style="white-space: nowrap;">
                        <a href="editar_docente_de.jsp" class="action-btn" title="Editar"><i class="bi bi-pencil"></i></a>
                        <a href="#" class="action-btn" title="Ver"><i class="bi bi-eye"></i></a>
                        <a href="#" class="action-btn delete" title="Eliminar"><i class="bi bi-trash"></i></a>
                    </td>
                </tr>
                <tr>
                    <td class="text-start">
                        <div class="docente-name-container">
                            <div class="avatar-circle" style="flex-shrink:0;"></div>
                            <div class="docente-name" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">
                                Luis Gerardo Barron Flores
                            </div>
                        </div>
                    </td>
                    <td>ejemplo@gmail.com</td>
                    <td>DATID</td>
                    <td>67</td>
                    <td><i class="bi bi-toggle-off text-danger fs-4"></i></td>
                    <td style="white-space: nowrap;">
                        <a href="editar_docente_de.jsp" class="action-btn" title="Editar"><i class="bi bi-pencil"></i></a>
                        <a href="#" class="action-btn" title="Ver"><i class="bi bi-eye"></i></a>
                        <a href="#" class="action-btn delete" title="Eliminar"><i class="bi bi-trash"></i></a>
                    </td>
                </tr>
                <tr>
                    <td class="text-start">
                        <div class="docente-name-container">
                            <div class="avatar-circle" style="flex-shrink:0;"></div>
                            <div class="docente-name" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">
                                Luis Gerardo Barron Flores
                            </div>
                        </div>
                    </td>
                    <td>ejemplo@gmail.com</td>
                    <td>DATID</td>
                    <td>67</td>
                    <td><i class="bi bi-toggle-on text-success fs-4"></i></td>
                    <td style="white-space: nowrap;">
                        <a href="editar_docente_de.jsp" class="action-btn" title="Editar"><i class="bi bi-pencil"></i></a>
                        <a href="#" class="action-btn" title="Ver"><i class="bi bi-eye"></i></a>
                        <a href="#" class="action-btn delete" title="Eliminar"><i class="bi bi-trash"></i></a>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="pagination-container">
        <a href="#" class="page-btn"><i class="bi bi-chevron-left"></i></a>
        <a href="#" class="page-btn active">1</a>
        <a href="#" class="page-btn">2</a>
        <a href="#" class="page-btn">3</a>
        <span class="page-btn dots">...</span>
        <a href="#" class="page-btn">10</a>
        <a href="#" class="page-btn"><i class="bi bi-chevron-right"></i></a>
    </div>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/coordinador.js"></script>
</body>
</html>
