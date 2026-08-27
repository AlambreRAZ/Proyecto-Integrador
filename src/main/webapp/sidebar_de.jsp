<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mx.edu.utez.DesarrolloAcademico.model.Usuario" %>
<%
    Usuario usuarioSidebar = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
    String nombreSidebar = (usuarioSidebar != null && usuarioSidebar.getNombre() != null)
            ? usuarioSidebar.getNombre() + " " + (usuarioSidebar.getApellidoPaterno() != null ? usuarioSidebar.getApellidoPaterno() : "")
            : "Usuario";
%>

<style>
    .sidebar-hover {
        display: flex !important;
        flex-direction: column !important;
        height: 100vh !important;
        max-height: 100vh !important;
    }

    .sidebar-menu-scroll {
        flex: 1 1 auto !important;
        min-height: 0 !important;      /* ← LA LÍNEA QUE FALTABA */
        overflow-y: auto !important;
        overflow-x: hidden !important;
        padding-bottom: 10px;
    }

    /* Compactamos un poco los items para que quepan con el submenú abierto */
    .sidebar-hover .sidebar-item {
        margin-top: 6px !important;
        margin-bottom: 6px !important;
    }
    .sidebar-hover .sidebar-submenu {
        margin-bottom: 4px !important;
    }

    .sidebar-menu-scroll::-webkit-scrollbar { width: 4px; }
    .sidebar-menu-scroll::-webkit-scrollbar-thumb {
        background: rgba(255, 255, 255, 0.2);
        border-radius: 4px;
    }
</style>

<nav class="sidebar-hover">
    <div class="sidebar-avatar-container">
        <div class="sidebar-avatar"></div>
        <div class="sidebar-info text-white">
            <div class="fw-semibold mb-1"><%= nombreSidebar %></div>
            <div class="small" style="color: rgba(255,255,255,0.85);">Desarrollador Académico</div>
        </div>
    </div>

    <!-- Contenedor con scroll inteligente -->
    <div class="sidebar-menu-scroll">
        <a href="${pageContext.request.contextPath}/vista_general_desarrollador_de.jsp"
           class="sidebar-item ${param.active == 'eventos' ? 'active' : ''}">
            <div class="sidebar-icon">
                <i class="bi bi-calendar-check"></i>
            </div>
            <span class="sidebar-text">Eventos</span>
        </a>

        <a href="${pageContext.request.contextPath}/gestion_eventos_de.jsp"
           class="sidebar-item ${param.active == 'gestion_eventos' ? 'active' : ''}">
            <div class="sidebar-icon">
                <i class="bi bi-calendar2-check"></i>
            </div>
            <span class="sidebar-text">Gestión de Eventos</span>
        </a>

        <div class="sidebar-item-container">
            <a href="#" class="sidebar-item ${param.active == 'gestion_usuarios' ? 'active' : ''}" onclick="return false;">
                <div class="sidebar-icon">
                    <i class="bi bi-person-video3"></i>
                </div>
                <span class="sidebar-text">Gestión de Usuarios</span>
            </a>

            <!-- Submenú con rutas absolutas -->
            <div class="sidebar-submenu">
                <a href="${pageContext.request.contextPath}/gestion_docente_de.jsp"
                   class="submenu-item ${param.active_sub == 'docente' ? 'active' : ''}">
                    Docente/Coordinador
                </a>
                <a href="${pageContext.request.contextPath}/gestion_desarrolladores_de.jsp"
                   class="submenu-item ${param.active_sub == 'desarrollador' ? 'active' : ''}">
                    Desarrollo académico
                </a>
            </div>
        </div>

        <!-- Mis Eventos -->
        <a href="${pageContext.request.contextPath}/mi_evento_de.jsp"
           class="sidebar-item ${param.active == 'mi_evento' ? 'active' : ''}">
            <div class="sidebar-icon">
                <i class="bi bi-calendar-event"></i>
            </div>
            <span class="sidebar-text">Mis Eventos</span>
        </a>

        <a href="${pageContext.request.contextPath}/gestion_periodos_carga_de.jsp"
           class="sidebar-item ${param.active == 'periodos_carga' ? 'active' : ''}">
            <div class="sidebar-icon">
                <i class="bi bi-calendar-plus"></i>
            </div>
            <span class="sidebar-text">Periodos de Carga</span>
        </a>

        <a href="${pageContext.request.contextPath}/mi_cuenta_de.jsp"
           class="sidebar-item ${param.active == 'mi_cuenta' ? 'active' : ''}">
            <div class="sidebar-icon">
                <i class="bi bi-person"></i>
            </div>
            <span class="sidebar-text">Mi cuenta</span>
        </a>

        <a href="${pageContext.request.contextPath}/logout"
           class="sidebar-item ${param.active == 'cerrar_sesion' ? 'active' : ''}">
            <div class="sidebar-icon">
                <i class="bi bi-box-arrow-left"></i>
            </div>
            <span class="sidebar-text">Cerrar sesión</span>
        </a>
    </div>
</nav>