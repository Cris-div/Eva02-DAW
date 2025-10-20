document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("formUsuario");
    const tabla = document.querySelector("#tablaUsuarios tbody");

    cargarUsuarios();

    async function cargarUsuarios() {
        const res = await fetch("http://localhost:8080/api/usuarios");
        const data = await res.json();

        tabla.innerHTML = "";
        data.forEach(u => {
            tabla.innerHTML += `
        <tr>
          <td>${u.idUsuario ?? "-"}</td>
          <td>${u.nombreUsuario ?? "-"}</td>
          <td>${u.rol ?? "-"}</td>
          <td>${u.activo ? "Activo" : "Inactivo"}</td>
          <td>
            <button class="btn btn-danger btn-sm" onclick="eliminarUsuario(${u.idUsuario})">🗑️</button>
          </td>
        </tr>
      `;
        });
    }

    // Registrar usuario nuevo
    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const usuario = {
            nombreUsuario: document.getElementById("nombreUsuario").value,
            contraseña: document.getElementById("contraseña").value,
            rol: document.getElementById("rol").value,
            activo: document.getElementById("activo").value === "true"
        };

        await fetch("http://localhost:8080/api/usuarios", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(usuario)
        });

        form.reset();
        cargarUsuarios();
    });

    // Eliminar usuario
    window.eliminarUsuario = async (id) => {
        if (confirm("¿Seguro que deseas eliminar este usuario?")) {
            await fetch(`http://localhost:8080/api/usuarios/${id}`, { method: "DELETE" });
            cargarUsuarios();
        }
    };
});
