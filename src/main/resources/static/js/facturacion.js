document.addEventListener("DOMContentLoaded", () => {
    const formFactura = document.getElementById("formFactura");
    const selectPaciente = document.getElementById("selectPaciente");
    const tabla = document.querySelector("#tablaFacturas tbody");

    // Cargar pacientes en el select
    async function cargarPacientes() {
        const res = await fetch("http://localhost:8080/api/pacientes");
        const pacientes = await res.json();
        selectPaciente.innerHTML = `<option value="">--Seleccione Paciente--</option>`;
        pacientes.forEach(p => {
            selectPaciente.innerHTML += `<option value="${p.idPaciente}">${p.nombres} ${p.apellidos}</option>`;
        });
    }

    // Cargar facturas en la tabla
    async function cargarFacturas() {
        const res = await fetch("http://localhost:8080/api/facturas");
        const facturas = await res.json();
        tabla.innerHTML = "";
        facturas.forEach(f => {
            tabla.innerHTML += `
        <tr>
          <td>${f.idFactura ?? "-"}</td>
          <td>${f.paciente ? f.paciente.nombres + " " + f.paciente.apellidos : "-"}</td>
          <td>${f.fechaEmision}</td>
          <td>${f.total ?? "0.00"}</td>
          <td>${f.estado}</td>
          <td>
            <button class="btn btn-danger btn-sm" onclick="eliminarFactura(${f.idFactura})">🗑️</button>
          </td>
        </tr>
      `;
        });
    }

    // Crear nueva factura
    formFactura.addEventListener("submit", async (e) => {
        e.preventDefault();
        const factura = {
            paciente: {idPaciente: parseInt(selectPaciente.value)},
            fechaEmision: document.getElementById("fecha").value
        };
        await fetch("http://localhost:8080/api/facturas", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(factura)
        });
        formFactura.reset();
        cargarFacturas();
    });

    // Eliminar factura
    window.eliminarFactura = async (id) => {
        if (confirm("¿Desea eliminar esta factura?")) {
            await fetch(`http://localhost:8080/api/facturas/${id}`, {method: "DELETE"});
            cargarFacturas();
        }
    };

    // Inicializar
    cargarPacientes();
    cargarFacturas();
});
