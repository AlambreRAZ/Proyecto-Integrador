<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioListaDao" %>
<%@ page import="mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioDao" %>
<%@ page import="mx.edu.utez.DesarrolloAcademico.model.Evento" %>
<%@ page import="java.util.List" %>
<%@ page import="mx.edu.utez.DesarrolloAcademico.model.Usuario" %>
<%
    UsuarioDao usuarioDao = new UsuarioDao();
    UsuarioListaDao usuarioListaDao = new UsuarioListaDao(); // Instancia para contar docentes

    Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
    int idDivision = (usuarioSesion != null) ? usuarioSesion.getIdDivision() : 0;
    int totalEventos = usuarioListaDao.contarEventos();
    int totalDocentes = usuarioListaDao.contarDocentes(idDivision);
    List<Evento> listaEventos = usuarioDao.obtenerProximosEventos();
%>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vista General – Coordinador</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
</head>
<body>

<jsp:include page="sidebar_co.jsp">
    <jsp:param name="active" value="eventos_proximo" />
</jsp:include>

<main class="main-content">

    <div class="row mb-5 gx-4 mt-3">
        <div class="col-md-6 col-lg-5 mb-3">
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="bi bi-calendar-check"></i>
                </div>
                <div>
                    <div class="text-muted small">Eventos registrados</div>
                    <div class="fs-4 fw-bold lh-1 mt-1"><%= totalEventos %></div>
                    <div class="text-muted" style="font-size: 0.75rem;">Total eventos</div>
                </div>
            </div>
        </div>
        <div class="col-md-6 col-lg-5 mb-3">
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="bi bi-people"></i>
                </div>
                <div class="flex-grow-1">
                    <div class="text-muted small">Docentes registrados</div>
                    <div class="fs-4 fw-bold lh-1 mt-1"><%= totalDocentes %></div>
                    <div class="text-muted" style="font-size: 0.75rem;">Total docentes</div>
                </div>
                <!-- Apunta a la vista del coordinador (_co) usando contextPath -->
                <a href="${pageContext.request.contextPath}/gestion_docente_co.jsp" class="btn-teal">Ver detalles</a>
            </div>
        </div>
    </div>

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 class="fw-bold mb-0 text-uppercase" style="letter-spacing: 0.05em; color: #333;">PRÓXIMOS EVENTOS</h5>
        <a href="${pageContext.request.contextPath}/historial_evento_co.jsp" class="btn-teal">Ver historial de Eventos</a>
    </div>

    <div id="eventsList">
        <%
            if (listaEventos != null && !listaEventos.isEmpty()) {
                for (Evento ev : listaEventos) {
        %>
        <div class="event-card mb-3">
            <div>
                <div class="fw-bold fs-5 mb-1"><%= ev.getNombre() %></div>
                <div class="text-muted small">
                    <%= ev.getFecha_Inicio() %> - <%= ev.getFecha_Fin() %>
                </div>
            </div>
            <!-- Enlace corregido para el rol coordinador -->
            <a href="${pageContext.request.contextPath}/ver_mas_evento_co.jsp?id=<%= ev.getID() %>" class="btn-teal">
                Ver detalles
            </a>
        </div>
        <%
            }
        } else {
        %>
        <!-- Si no hay eventos registrados en la BD -->
        <div class="alert alert-light text-center border p-4 text-muted">
            <i class="bi bi-calendar-x fs-2 d-block mb-2"></i>
            No hay próximos eventos registrados.
        </div>
        <%
            }
        %>
    </div>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/coordinador.js"></script>
</body>
</html>