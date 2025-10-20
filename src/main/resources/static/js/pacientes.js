document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("formPaciente");
    const tabla = document.querySelector("#tablaPacientes tbody");

    // 🔹 Cargar pacientes al iniciar
    cargarPacientes();

    async function cargarPacientes() {
        const res = await fetch("http://localhost:8080/api/pacientes");
        const data = await res.json();

        tabla.innerHTML = "";
        data.forEach(p => {
            tabla.innerHTML += `
        <tr>
          <td>${p.idPaciente ?? "-"}</td>
          <td>${p.nombres ?? "-"}</td>
          <td>${p.apellidos ?? "-"}</td>
          <td>${p.dni ?? "-"}</td>
          <td>${p.fechaNacimiento ?? "-"}</td>
          <td>${p.direccion ?? "-"}</td>
          <td>${p.telefono ?? "-"}</td>
          <td>${p.correo ?? "-"}</td>
          <td>${p.sexo ?? "-"}</td>
          <td>${p.estado ?? "-"}</td>
        </tr>
      `;
        });
    }

    // 🔹 Registrar paciente nuevo
    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const paciente = {
            nombres: document.getElementById("nombres").value,
            apellidos: document.getElementById("apellidos").value,
            dni: document.getElementById("dni").value,
            fechaNacimiento: document.getElementById("fechaNacimiento").value,
            direccion: document.getElementById("direccion").value,
            telefono: document.getElementById("telefono").value,
            correo: document.getElementById("correo").value,
            sexo: document.getElementById("sexo").value,
            estado: document.getElementById("estado").value
        };

        await fetch("http://localhost:8080/api/pacientes", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(paciente)
        });

        form.reset();
        cargarPacientes();
    });
});
