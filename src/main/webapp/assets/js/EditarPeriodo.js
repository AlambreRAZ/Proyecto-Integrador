document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const idPeriodo = urlParams.get('id');

    if (!idPeriodo) {
        Swal.fire('Error', 'No se especificó un ID de periodo válido.', 'error')
            .then(() => window.location.href = 'gestion_periodos_carga_de.jsp');
        return;
    }

    document.getElementById("idPeriodo").value = idPeriodo;

    cargarDatosPeriodo(idPeriodo);

    const form = document.getElementById("formEditarPeriodo");
    if (form) {
        form.addEventListener("submit", function (e) {
            e.preventDefault(); // Evita la recarga de página
            guardarEdicionPeriodo();
        });
    }
});

function cargarDatosPeriodo(id) {
    fetch("ListarPeriodosServlet")
        .then(res => res.json())
        .then(data => {
            const periodo = data.find(p => (p.idPeriodo == id || p.id == id));
            if (periodo) {
                document.getElementById("selectDivision").value = periodo.division;
                document.getElementById("fechaInicio").value = periodo.fechaInicio;
                document.getElementById("fechaFin").value = periodo.fechaFin;
            } else {
                Swal.fire('Error', 'No se encontraron los datos del periodo.', 'error')
                    .then(() => window.location.href = 'gestion_periodos_carga_de.jsp');
            }
        })
        .catch(err => {
            console.error("Error al cargar periodo:", err);
            Swal.fire('Error', 'No se pudieron obtener los datos.', 'error');
        });
}

// Función principal que envía la actualización y valida la respuesta
function guardarEdicionPeriodo() {
    const form = document.getElementById("formEditarPeriodo");
    const formData = new URLSearchParams(new FormData(form));

    // --- PRELOADER CON PORCENTAJE SIMULADO ---
    let porcentaje = 0;
    let timerCarga;

    Swal.fire({
        title: 'Actualizando periodo...',
        html: '<div style="font-size: 1.5rem; font-weight: bold; color: #00847b; margin-top: 10px;" id="lblPorcentajeEditPeriodo">0%</div>',
        allowOutsideClick: false,
        allowEscapeKey: false,
        showConfirmButton: false,
        didOpen: () => {
            Swal.showLoading();
            timerCarga = setInterval(() => {
                if (porcentaje < 90) {
                    porcentaje += 10;
                    const el = document.getElementById('lblPorcentajeEditPeriodo');
                    if (el) el.textContent = porcentaje + '%';
                }
            }, 80);
        }
    });

    fetch("EditarPeriodoServlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"
        },
        body: formData.toString()
    })
        .then(async response => {
            const data = await response.json();
            clearInterval(timerCarga);

            // VALIDACIÓN 1: Si hay conflicto / duplicado de división (Código HTTP 409)
            if (response.status === 409) {
                Swal.fire({
                    icon: 'warning',
                    title: 'División duplicada',
                    text: data.message || 'La división ya tiene un periodo de carga asignado.'
                });
                return; // DETIENE EL FLUJO.
            }

            // VALIDACIÓN 2: Si ocurrió cualquier otro error HTTP
            if (!response.ok) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error al actualizar',
                    text: data.message || 'Ocurrió un error inesperado al procesar la solicitud.'
                });
                return; // DETIENE EL FLUJO.
            }

            // Forzar visualización de 100% en caso de éxito
            const el = document.getElementById('lblPorcentajeEditPeriodo');
            if (el) el.textContent = '100%';

            setTimeout(() => {
                Swal.fire({
                    icon: 'success',
                    title: '¡Actualizado!',
                    text: data.message || 'El periodo de carga se actualizó correctamente.',
                    timer: 2000,
                    showConfirmButton: false
                }).then(() => {
                    window.location.href = "gestion_periodos_carga_de.jsp";
                });
            }, 300);
        })
        .catch(err => {
            clearInterval(timerCarga);
            console.error("Error en la solicitud:", err);
            Swal.fire({
                icon: 'error',
                title: 'Error de red',
                text: 'No se pudo establecer comunicación con el servidor.'
            });
        });
}