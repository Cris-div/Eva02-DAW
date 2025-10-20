const API_BASE = "/api";

document.getElementById("formLogin").addEventListener("submit", async (e) => {
    e.preventDefault();

    const nombreUsuario = document.getElementById("username").value.trim();
    const contraseña = document.getElementById("password").value.trim();

    if (!nombreUsuario || !contraseña) {
        alert("Por favor, complete todos los campos.");
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/usuarios/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nombreUsuario, contraseña }),
        });

        if (res.ok) {
            const usuario = await res.json(); // ✅ backend debe devolver JSON
            localStorage.setItem("usuario", JSON.stringify(usuario));

            alert(`✅ Bienvenido ${usuario.nombreUsuario} (${usuario.rol})`);
            window.location.replace("/"); // ✅ reemplaza en lugar de href
        } else {
            const txt = await res.text();
            alert("❌ " + txt);
        }
    } catch (e) {
        alert("⚠️ No se pudo conectar con el servidor.");
        console.error(e);
    }
});
