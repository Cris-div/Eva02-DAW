const API_BASE = "/api"; // relativo al mismo servidor (usa el mismo origin)

async function apiGet(endpoint) {
    const res = await fetch(`${API_BASE}${endpoint}`);
    if (!res.ok) throw new Error(`GET ${endpoint} -> ${res.status}`);
    return await res.json();
}

async function apiPost(endpoint, data) {
    const res = await fetch(`${API_BASE}${endpoint}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    });
    if (!res.ok) {
        const txt = await res.text();
        throw new Error(`POST ${endpoint} -> ${res.status}: ${txt}`);
    }
    return await res.json();
}

async function apiPut(endpoint, data) {
    const res = await fetch(`${API_BASE}${endpoint}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    });
    if (!res.ok) throw new Error(`PUT ${endpoint} -> ${res.status}`);
    return await res.json();
}
