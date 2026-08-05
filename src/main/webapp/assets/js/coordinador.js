document.addEventListener('DOMContentLoaded', function () {
    const sidebar = document.querySelector('.sidebar-hover');
    if (!sidebar) return;

    // Si el localStorage dice que el menu debe estar abierto, aplícalo
    if (localStorage.getItem('menuAbierto') === 'true') {
        sidebar.classList.add('locked');
    }

    // Si hay un submenu-item activo en esta página, siempre mostrar el sidebar abierto
    // para que el usuario pueda ver en qué subopción está
    const subActivo = sidebar.querySelector('.submenu-item.active');
    if (subActivo) {
        sidebar.classList.add('locked');
        localStorage.setItem('menuAbierto', 'true');
    }

    // Bloquear apertura al hacer clic en cualquier sidebar-item (incluyendo los de submenú)
    const links = document.querySelectorAll('.sidebar-item, .submenu-item');
    links.forEach(function (link) {
        link.addEventListener('click', function () {
            localStorage.setItem('menuAbierto', 'true');
            sidebar.classList.add('locked');
        });
    });

    // Colapsar al salir del sidebar con el mouse
    sidebar.addEventListener('mouseleave', function () {
        // Solo colapsar si no hay subitem activo o si el menú está forzosamente abierto por click
        // Para que el usuario no pierda el contexto al pasar el mouse por encima
        if (!subActivo) {
            localStorage.setItem('menuAbierto', 'false');
            sidebar.classList.remove('locked');
        }
    });
});
