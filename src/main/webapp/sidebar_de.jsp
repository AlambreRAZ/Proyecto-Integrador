<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mx.edu.utez.DesarrolloAcademico.model.Usuario" %>
<%
    Usuario usuarioSidebar = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
    String nombreSidebar = (usuarioSidebar != null) ? usuarioSidebar.getNombre() + " " + usuarioSidebar.getApellidoPaterno() : "Usuario";
%>
<nav class="sidebar-hover">
    <div class="sidebar-avatar-container">
        <div class="sidebar-avatar"></div>
        <div class="sidebar-info text-white">
            <div class="fw-semibold mb-1"><%= nombreSidebar %></div>
            <div class="small" style="color: rgba(255,255,255,0.85);">Desarrollador Academico</div>
        </div>
    </div>

    <a href="vista_general_desarrollador_de.jsp" class="sidebar-item ${param.active == 'eventos' ? 'active' : ''}">
        <div class="sidebar-icon">
            <i class="bi bi-calendar-check"></i>
        </div>
        <span class="sidebar-text">Eventos</span>
    </a>

    <a href="gestion_eventos_de.jsp" class="sidebar-item ${param.active == 'gestion_eventos' ? 'active' : ''}">
        <div class="sidebar-icon">
            <i class="bi bi-calendar2-check"></i>
        </div>
        <span class="sidebar-text">Gestion de Eventos</span>
    </a>

    <div class="sidebar-item-container">
        <a href="#" class="sidebar-item ${param.active == 'gestion_usuarios' ? 'active' : ''}" onclick="return false;">
            <div class="sidebar-icon">
                <i class="bi bi-person-video3"></i>
            </div>
            <span class="sidebar-text">Gestion de Usuarios</span>
        </a>
        <div class="sidebar-submenu">
            <a href="gestion_docente_de.jsp" class="submenu-item ${param.active_sub == 'docente' ? 'active' : ''}">Docente/Cordinador</a>
            <a href="gestion_desarrolladores_de.jsp" class="submenu-item ${param.active_sub == 'desarrollador' ? 'active' : ''}">Desarrollo academico</a>
        </div>
    </div>

    <a href="gestion_periodos_carga_de.jsp" class="sidebar-item ${param.active == 'periodos_carga' ? 'active' : ''}">
        <div class="sidebar-icon">
            <i class="bi bi-calendar-plus"></i>
        </div>
        <span class="sidebar-text">Periodos de Carga</span>
    </a>

    <a href="mi_cuenta_de.jsp" class="sidebar-item ${param.active == 'mi_cuenta' ? 'active' : ''}">
        <div class="sidebar-icon">
            <i class="bi bi-person"></i>
        </div>
        <span class="sidebar-text">Mi cuenta</span>
    </a>

    <a href="logout" class="sidebar-item ${param.active == 'cerrar_sesion' ? 'active' : ''}">
        <div class="sidebar-icon">
            <i class="bi bi-box-arrow-left"></i>
        </div>
        <span class="sidebar-text">Cerrar sesion</span>
    </a>
</nav>
