// MisEventos.js - Carga los eventos del usuario autenticado desde la BD
const contextPathMisEventos = window.contextPath || '';
const tbodyMisEventos = document.getElementById('tablaMisEventosBody');
const inputBuscarMisEventos = document.getElementById('buscarMisEventos');

let misEventosOriginales = [];
let filtroTextoMisEventos = '';

function escHtmlEv(t) {
    if (t === null || t === undefined) return '';
    return String(t).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}

function normEv(t) {
    return String(t || '').toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');
}

function formatFechaMisEv(fechaIso) {
    if (!fechaIso) return '';
    const partes = fechaIso.split('-');
    if (partes.length !== 3) return fechaIso;
    return partes[2] + '/' + partes[1] + '/' + partes[0].slice(2);
}

function renderMisEventos(lista) {
    if (!tbodyMisEventos) return;
    if (!lista.length) {
        tbodyMisEventos.innerHTML = '<tr><td colspan="5" class="text-center text-muted py-4">No tienes eventos asignados.</td></tr>';
        return;
    }

    tbodyMisEventos.innerHTML = '';
    lista.forEach(function (ev) {
        const urlVer = window.urlVerEvento ? window.urlVerEvento + '?id=' + ev.id : '#';
        const urlCargar = window.urlCargarArchivo ? window.urlCargarArchivo + '?id=' + ev.id : '#';

        const tr = document.createElement('tr');
        tr.innerHTML =
            '<td>' +
            '  <div class="fw-semibold">' + escHtmlEv(ev.nombre) + '</div>' +
            '  <div class="small text-muted">' + escHtmlEv(ev.descripcion) + '</div>' +
            '</td>' +
            '<td>' + escHtmlEv(ev.tipo) + '</td>' +
            '<td>' +
            '  <div>' + escHtmlEv(ev.institucion) + '</div>' +
            '  <div class="small text-muted">' + escHtmlEv(ev.lugar) + '</div>' +
            '</td>' +
            '<td>' + formatFechaMisEv(ev.fechaInicio) + ' - ' + formatFechaMisEv(ev.fechaFin) + '</td>' +
            '<td>' +
            '  <a href="' + urlVer + '" class="action-btn"><i class="bi bi-eye"></i></a>' +
            '  <a href="' + urlCargar + '" class="action-btn"><i class="bi bi-cloud-upload"></i></a>' +
            '</td>';
        tbodyMisEventos.appendChild(tr);
    });
}

function aplicarFiltrosMisEventos() {
    const texto = normEv(filtroTextoMisEventos);
    const filtrados = misEventosOriginales.filter(function (ev) {
        return texto === '' || normEv(ev.nombre).includes(texto) || normEv(ev.tipo).includes(texto);
    });
    renderMisEventos(filtrados);
}

function cargarMisEventos() {
    fetch(contextPathMisEventos + '/ListarMisEventosServlet')
        .then(function (res) {
            if (!res.ok) throw new Error('Error de servidor: ' + res.status);
            return res.json();
        })
        .then(function (data) {
            misEventosOriginales = data || [];
            aplicarFiltrosMisEventos();
        })
        .catch(function (err) {
            console.error('Error al cargar mis eventos:', err);
            if (tbodyMisEventos) {
                tbodyMisEventos.innerHTML = '<tr><td colspan="5" class="text-center text-danger py-4">No se pudieron cargar los eventos.</td></tr>';
            }
        });
}

if (inputBuscarMisEventos) {
    inputBuscarMisEventos.addEventListener('input', function () {
        filtroTextoMisEventos = inputBuscarMisEventos.value;
        aplicarFiltrosMisEventos();
    });
}

if (tbodyMisEventos) {
    cargarMisEventos();
}
