// Depende de api.js (apiGet, apiPost, apiPut)

document.addEventListener("DOMContentLoaded", inicializarCitas);

async function inicializarCitas() {
    await cargarSelects();
    await listarCitas();
    document.getElementById("formCita").addEventListener("submit", crearCitaHandler);
    document.getElementById("btnRefresh").addEventListener("click", listarCitas);
}

async function cargarSelects() {
    // Cargar pacientes
    try {
        const pacientes = await apiGet("/pacientes");
        const selPac = document.getElementById("selectPaciente");
        selPac.innerHTML = "<option value=''>--Seleccione paciente--</option>";
        pacientes.forEach(p => {
            // Asegúrate que en tu entidad Paciente existan propiedades idPaciente y nombres/apellidos
            const nombre = (p.nombres ? `${p.nombres} ${p.apellidos}` : (p.nombre || 'Sin nombre'));
            selPac.innerHTML += `<option value="${p.idPaciente}">${nombre} (DNI: ${p.dni || ''})</option>`;
        });
    } catch (e) {
        console.error(e);
        alert("Error cargando pacientes. Revisa la consola.");
    }

    // Cargar medicos
    try {
        const medicos = await apiGet("/medicos");
        const selMed = document.getElementById("selectMedico");
        selMed.innerHTML = "<option value=''>--Seleccione médico--</option>";
        medicos.forEach(m => {
            const nombre = `${m.nombres || m.nombre || ''} ${m.apellidos || ''}`.trim();
            selMed.innerHTML += `<option value="${m.idMedico}">${nombre} (${m.colegiatura || ''})</option>`;
        });
    } catch (e) {
        console.error(e);
        alert("Error cargando médicos. Revisa la consola.");
    }
}

async function crearCitaHandler(e) {
    e.preventDefault();
    const idPaciente = document.getElementById("selectPaciente").value;
    const idMedico = document.getElementById("selectMedico").value;
    const fecha = document.getElementById("fecha").value; // 'YYYY-MM-DD'
    const hora = document.getElementById("hora").value;   // 'HH:MM'
    const motivo = document.getElementById("motivo").value;

    if (!idPaciente || !idMedico || !fecha || !hora) {
        alert("Completa paciente, médico, fecha y hora.");
        return;
    }

    // El backend espera POST /api/citas/{idPaciente}/{idMedico} con body { fecha: "...", hora: "...", motivo: "..."}
    const body = { fecha: fecha, hora: hora, motivo: motivo };

    try {
        await apiPost(`/citas/${idPaciente}/${idMedico}`, body);
        alert("✅ Cita agendada");
        document.getElementById("formCita").reset();
        await listarCitas();
    } catch (err) {
        console.error(err);
        alert("❌ Error al agendar la cita. Revisa consola.");
    }
}

async function listarCitas() {
    try {
        const citas = await apiGet("/citas");
        const tbody = document.querySelector("#tablaCitas tbody");
        tbody.innerHTML = "";
        citas.forEach(c => {
            const paciente = c.paciente ? (c.paciente.nombres ? `${c.paciente.nombres} ${c.paciente.apellidos}` : (c.paciente.nombre || '')) : '';
            const medico = c.medico ? (c.medico.nombres ? `${c.medico.nombres} ${c.medico.apellidos}` : (c.medico.nombre || '')) : '';
            const fecha = c.fecha || '';
            const hora = c.hora || '';
            tbody.innerHTML += `
        <tr>
          <td>${c.idCita}</td>
          <td>${paciente}</td>
          <td>${medico}</td>
          <td>${fecha}</td>
          <td>${hora}</td>
          <td>${c.motivo || ''}</td>
          <td>${c.estado || ''}</td>
          <td>
            <button class="btn btn-sm btn-success" onclick="cambiarEstado(${c.idCita}, 'atendida')">Atendida</button>
            <button class="btn btn-sm btn-warning" onclick="cambiarEstado(${c.idCita}, 'programada')">Programada</button>
            <button class="btn btn-sm btn-danger" onclick="cambiarEstado(${c.idCita}, 'cancelada')">Cancelar</button>
          </td>
        </tr>
      `;
        });
    } catch (e) {
        console.error(e);
        alert("Error listando citas. Revisa la consola.");
    }
}

// funciones globales usadas por los botones (expuestas en window)
window.cambiarEstado = async function(id, estado) {
    try {
        // El backend tiene PUT /api/citas/{id}/estado?estado=...
        await fetch(`/api/citas/${id}/estado?estado=${encodeURIComponent(estado)}`, { method: "PUT" });
        await listarCitas();
    } catch (e) {
        console.error(e);
        alert("Error cambiando estado.");
    }
};
