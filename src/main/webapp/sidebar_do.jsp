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
            <div class="small" style="color: rgba(255,255,255,0.85);">Docente Académico</div>
        </div>
    </div>

    <a href="vista_general_docente_do.jsp" class="sidebar-item ${param.active == 'vista_general' ? 'active' : ''}">
        <div class="sidebar-icon">
            <i class="bi bi-calendar-event"></i>
        </div>
        <span class="sidebar-text">Vista general</span>
    </a>

    <a href="mis_eventos_do.jsp" class="sidebar-item ${param.active == 'mis_eventos' ? 'active' : ''}">
        <div class="sidebar-icon">
            <i class="bi bi-calendar-heart"></i>
        </div>
        <span class="sidebar-text">Mis eventos</span>
    </a>

    <a href="mi_cuenta_do.jsp" class="sidebar-item ${param.active == 'mi_cuenta' ? 'active' : ''}">
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
