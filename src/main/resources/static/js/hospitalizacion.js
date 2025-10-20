document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("formHosp");
    const pacienteSelect = document.getElementById("paciente");
    const habitacionSelect = document.getElementById("habitacion");
    const tabla = document.querySelector("#tablaHosp tbody");
    const btnRefrescar = document.getElementById("btnRefrescar");

    // Cargar pacientes y habitaciones
    async function cargarListas() {
        try {
            // Pacientes
            const resPac = await fetch("http://localhost:8080/api/pacientes");
            const pacientes = await resPac.json();
            pacienteSelect.innerHTML = "<option value=''>--Seleccione Paciente--</option>";
            pacientes.forEach(p => {
                pacienteSelect.innerHTML += `<option value="${p.idPaciente}">${p.nombres} ${p.apellidos}</option>`;
            });

            // Habitaciones
            const resHab = await fetch("http://localhost:8080/api/habitaciones");
            const habitaciones = await resHab.json();
            habitacionSelect.innerHTML = "<option value=''>--Seleccione Habitación--</option>";
            habitaciones.forEach(h => {
                habitacionSelect.innerHTML += `<option value="${h.idHabitacion}">${h.numero} - ${h.tipo}</option>`;
            });
        } catch (err) {
            console.error("Error al cargar listas:", err);
        }
    }

    // Cargar hospitalizaciones
    async function cargarHospitalizaciones() {
        try {
            const res = await fetch("http://localhost:8080/api/hospitalizaciones");
            const data = await res.json();
            tabla.innerHTML = "";
            data.forEach(h => {
                tabla.innerHTML += `
                <tr>
                  <td>${h.idHospitalizacion}</td>
                  <td>${h.paciente?.nombres ?? ""} ${h.paciente?.apellidos ?? ""}</td>
                  <td>${h.habitacion?.numero ?? ""} (${h.habitacion?.tipo ?? ""})</td>
                  <td>${h.diagnosticoIngreso ?? ""}</td>
                  <td>${h.fechaIngreso ?? ""}</td>
                  <td>${h.fechaAlta ?? "-"}</td>
                  <td>${h.estado ?? ""}</td>
                  <td>
                    <button class="btn btn-danger btn-sm" onclick="eliminarHospitalizacion(${h.idHospitalizacion})">🗑️</button>
                  </td>
                </tr>
              `;
            });
        } catch (err) {
            console.error("Error al cargar hospitalizaciones:", err);
        }
    }

    // Registrar hospitalización
    form.addEventListener("submit", async e => {
        e.preventDefault();

        const hospitalizacion = {
            paciente: { idPaciente: parseInt(pacienteSelect.value) },
            habitacion: { idHabitacion: parseInt(habitacionSelect.value) },
            diagnosticoIngreso: document.getElementById("diagnosticoIngreso").value,
            estado: document.getElementById("estado").value
        };

        try {
            const res = await fetch("http://localhost:8080/api/hospitalizaciones", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(hospitalizacion)
            });

            if (!res.ok) {
                const errorMsg = await res.text();
                alert("Error: " + errorMsg);
                return;
            }

            form.reset();
            cargarHospitalizaciones();
        } catch (err) {
            console.error("Error al registrar hospitalización:", err);
        }
    });

    // Eliminar hospitalización
    window.eliminarHospitalizacion = async (id) => {
        if (confirm("¿Desea eliminar esta hospitalización?")) {
            try {
                await fetch(`http://localhost:8080/api/hospitalizaciones/${id}`, { method: "DELETE" });
                cargarHospitalizaciones();
            } catch (err) {
                console.error("Error al eliminar hospitalización:", err);
            }
        }
    };

    // Botón refrescar
    btnRefrescar.addEventListener("click", () => {
        cargarListas();
    });

    // Inicializar listas y tabla
    cargarListas();
    cargarHospitalizaciones();
});
