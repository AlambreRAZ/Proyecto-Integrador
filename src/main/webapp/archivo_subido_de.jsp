<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Archivo Subido – Desarrollador</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/coordinador.css">
    <style>
        body {
            background-color: #e5e5e5;
        }
        .pdf-header {
            background-color: #f3f4f6;
            padding: 15px 20px;
        }
        .pdf-title {
            color: var(--teal-main);
            font-weight: bold;
            font-size: 1.2rem;
            margin: 0;
        }
        .pdf-subtitle {
            color: #555;
            font-size: 0.85rem;
            margin: 0;
        }
        .pdf-toolbar {
            background-color: var(--teal-main);
            padding: 10px 20px;
            display: flex;
            align-items: center;
            gap: 20px;
        }
        .pdf-toolbar i {
            color: white;
            font-size: 1.5rem;
            cursor: pointer;
        }
        .pdf-toolbar .zoom-container {
            display: flex;
            align-items: center;
            gap: 15px;
        }
        .pdf-toolbar .zoom-icon {
            font-size: 1.2rem;
        }
        .pdf-viewer-container {
            display: flex;
            justify-content: center;
            padding: 30px;
            min-height: 80vh;
        }
        .pdf-page {
            background-color: #d9d9d9;
            width: 60%;
            max-width: 800px;
            min-height: 800px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>

<div class="pdf-header d-flex justify-content-between align-items-center">
    <div>
        <h1 class="pdf-title">Cetificado_cisco.pdf</h1>
        <p class="pdf-subtitle">Archivo</p>
    </div>
    <a href="ver_mas_evento_de.jsp" class="btn" style="border: 2px solid var(--teal-main); color: var(--teal-main); border-radius: 6px; text-decoration: none; padding: 6px 20px; font-weight: 600; background: white;">
        <i class="bi bi-chevron-left"></i> Volver
    </a>
</div>

<div class="pdf-toolbar">
    <div class="zoom-container">
        <i class="bi bi-dash zoom-icon"></i>
        <i class="bi bi-zoom-in"></i>
        <i class="bi bi-plus zoom-icon"></i>
    </div>
    <i class="bi bi-printer-fill ms-auto"></i>
</div>

<div class="pdf-viewer-container">
    <div class="pdf-page">
        <!-- Contenido del PDF (simulado) -->
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
