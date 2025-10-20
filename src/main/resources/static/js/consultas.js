document.addEventListener("DOMContentLoaded", inicializarConsultas);

async function inicializarConsultas() {
    await cargarSelects();
    await listarConsultas();
    document.getElementById("formConsulta").addEventListener("submit", registrarConsulta);
    document.getElementById("btnRecargar").addEventListener("click", listarConsultas);
}

async function cargarSelects() {
    try {
        const pacientes = await apiGet("/pacientes");
        const medicos = await apiGet("/medicos");
        const citas = await apiGet("/citas");

        const selPac = document.getElementById("selectPaciente");
        const selMed = document.getElementById("selectMedico");
        const selCita = document.getElementById("selectCita");

        selPac.innerHTML = "<option value=''>--Seleccione Paciente--</option>";
        pacientes.forEach(p => {
            selPac.innerHTML += `<option value="${p.idPaciente}">${p.nombre || (p.nombres + ' ' + p.apellidos)}</option>`;
        });

        selMed.innerHTML = "<option value=''>--Seleccione Médico--</option>";
        medicos.forEach(m => {
            selMed.innerHTML += `<option value="${m.idMedico}">${m.nombre || (m.nombres + ' ' + m.apellidos)}</option>`;
        });

        selCita.innerHTML = "<option value=''>--Seleccione Cita--</option>";
        citas.forEach(c => {
            selCita.innerHTML += `<option value="${c.idCita}">#${c.idCita} (${c.fecha} ${c.hora})</option>`;
        });

    } catch (error) {
        console.error("Error cargando selects", error);
        alert("⚠️ Error cargando listas de pacientes, médicos o citas.");
    }
}

async function registrarConsulta(e) {
    e.preventDefault();

    const idPaciente = document.getElementById("selectPaciente").value;
    const idMedico = document.getElementById("selectMedico").value;
    const idCita = document.getElementById("selectCita").value;

    const consulta = {
        motivoConsulta: document.getElementById("motivo").value,
        observaciones: document.getElementById("observaciones").value,
        fecha: new Date().toISOString().split("T")[0],
        hora: new Date().toTimeString().split(" ")[0].slice(0,5)
    };

    if (!idPaciente || !idMedico || !idCita) {
        alert("Debe seleccionar Paciente, Médico y Cita.");
        return;
    }

    try {
        await apiPost(`/consultas/${idPaciente}/${idMedico}/${idCita}`, consulta);
        alert("✅ Consulta registrada correctamente");
        document.getElementById("formConsulta").reset();
        await listarConsultas();
    } catch (err) {
        console.error(err);
        alert("❌ Error registrando consulta. Revisa consola.");
    }
}

async function listarConsultas() {
    try {
        const consultas = await apiGet("/consultas");
        const tbody = document.querySelector("#tablaConsultas tbody");
        tbody.innerHTML = "";

        consultas.forEach(c => {
            const paciente = c.paciente ? (c.paciente.nombre || c.paciente.nombres) : "";
            const medico = c.medico ? (c.medico.nombre || c.medico.nombres) : "";
            const fecha = c.fecha || "";
            const motivo = c.motivoConsulta || "";

            tbody.innerHTML += `
        <tr>
          <td>${c.idConsulta}</td>
          <td>${paciente}</td>
          <td>${medico}</td>
          <td>${fecha}</td>
          <td>${motivo}</td>
          <td>
            <button class="btn btn-sm btn-info" onclick="verDetalles(${c.idConsulta})">Detalles</button>
          </td>
        </tr>`;
        });
    } catch (err) {
        console.error(err);
        alert("Error listando consultas");
    }
}

// Función placeholder para futuro (diagnósticos/recetas)
window.verDetalles = function(idConsulta) {
    alert(`Abrir detalles para consulta #${idConsulta}`);
};
