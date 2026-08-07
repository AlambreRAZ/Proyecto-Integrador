document.addEventListener('DOMContentLoaded', function() {
    const sidebar = document.querySelector('.sidebar-hover');
    if (!sidebar) return;


    if (localStorage.getItem('menuAbierto') === 'true') {
        sidebar.classList.add('locked');
    }


    const links = document.querySelectorAll('.sidebar-item');
    links.forEach(link => {
        link.addEventListener('click', () => {
            localStorage.setItem('menuAbierto', 'true');
            sidebar.classList.add('locked');
        });
    });


    sidebar.addEventListener('mouseleave', () => {
        localStorage.setItem('menuAbierto', 'false');
        sidebar.classList.remove('locked');
    });
});
