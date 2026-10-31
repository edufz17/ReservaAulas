const API_URL = window.RESERVA_API_URL || "http://localhost:8080";

const $ = (selector) => document.querySelector(selector);
const state = { rooms: [], bookings: [] };

function showMessage(text, isError = false) {
  const message = $("#message");
  message.textContent = text;
  message.classList.toggle("error", isError);
  if (text) window.setTimeout(() => { message.textContent = ""; }, 4500);
}

async function request(path, options = {}) {
  const response = await fetch(`${API_URL}${path}`, {
    headers: { "Content-Type": "application/json", ...options.headers },
    ...options
  });
  if (!response.ok) throw new Error(`La API respondió con ${response.status}`);
  return response.status === 204 ? null : response.json();
}

function roomQuery() {
  const params = new URLSearchParams();
  const capacity = $("#capacity-filter").value;
  const computers = $("#computers-filter").value;
  if (capacity) params.set("capacidad", capacity);
  if (computers) params.set("ordenadores", computers);
  const query = params.toString();
  return query ? `/aulas?${query}` : "/aulas";
}

function renderRooms(rooms) {
  $("#room-count").textContent = rooms.length;
  $("#rooms-table").innerHTML = rooms.length ? rooms.map((room) => `
    <tr>
      <td class="room-name-cell">${escapeHtml(room.nombre)}</td>
      <td>${room.capacidad} plazas</td>
      <td><span class="tag ${room.tieneOrdenadores ? "" : "neutral"}">${room.tieneOrdenadores ? "Con ordenadores" : "Sin ordenadores"}</span></td>
    </tr>`).join("") : '<tr><td class="empty-state" colspan="3">No hay aulas con estos filtros.</td></tr>';
  $("#room-select").innerHTML = '<option value="">Selecciona un aula</option>' +
    rooms.map((room) => `<option value="${room.id}">${escapeHtml(room.nombre)} · ${room.capacidad} plazas</option>`).join("");
}

function renderBookings(bookings) {
  state.bookings = bookings;
  $("#booking-count").textContent = bookings.length;
  const recent = [...bookings].sort((a, b) => new Date(b.fechaReserva) - new Date(a.fechaReserva)).slice(0, 8);
  $("#bookings-table").innerHTML = recent.length ? recent.map((booking) => `
    <tr>
      <td>${formatDate(booking.fechaReserva)}</td>
      <td>${escapeHtml(fullName(booking.profesor))}</td>
      <td>${escapeHtml(booking.aula?.nombre || "—")}</td>
      <td>${escapeHtml(booking.franjaHoraria?.nombre || "—")}</td>
    </tr>`).join("") : '<tr><td class="empty-state" colspan="4">Todavía no hay reservas.</td></tr>';
}

async function loadData() {
  try {
    const [rooms, bookings, teachers, slots] = await Promise.all([
      request(roomQuery()), request("/reservas"), request("/profesores"), request("/franjasHorarias")
    ]);
    state.rooms = rooms;
    renderRooms(rooms);
    renderBookings(bookings);
    $("#teacher-select").innerHTML = '<option value="">Selecciona un profesor</option>' +
      teachers.map((teacher) => `<option value="${teacher.id}">${escapeHtml(fullName(teacher))}</option>`).join("");
    $("#slot-select").innerHTML = '<option value="">Selecciona una franja</option>' +
      slots.map((slot) => `<option value="${slot.id}">${escapeHtml(slot.nombre)}</option>`).join("");
    $("#connection-status").classList.add("online");
    $("#connection-status span:last-child").textContent = "API conectada";
  } catch (error) {
    $("#connection-status span:last-child").textContent = "API no disponible";
    showMessage(`No se han podido cargar los datos. Comprueba que Spring Boot está activo en ${API_URL}.`, true);
    $("#rooms-table").innerHTML = '<tr><td class="empty-state" colspan="3">No se pudo conectar con la API.</td></tr>';
    $("#bookings-table").innerHTML = '<tr><td class="empty-state" colspan="4">No se pudo conectar con la API.</td></tr>';
  }
}

async function createRoom(event) {
  event.preventDefault();
  const room = {
    nombre: $("#room-name").value.trim(),
    capacidad: Number($("#room-capacity").value),
    tieneOrdenadores: $("#room-computers").checked
  };
  try {
    await request("/aulas", { method: "POST", body: JSON.stringify(room) });
    $("#room-dialog").close();
    $("#room-form").reset();
    showMessage("Aula registrada correctamente.");
    await loadData();
  } catch (error) {
    showMessage("No se pudo registrar el aula.", true);
  }
}

async function createBooking(event) {
  event.preventDefault();
  const booking = {
    fechaReserva: $("#booking-date").value,
    profesor: { id: Number($("#teacher-select").value) },
    aula: { id: Number($("#room-select").value) },
    franjaHoraria: { id: Number($("#slot-select").value) }
  };
  try {
    await request("/reservas", { method: "POST", body: JSON.stringify(booking) });
    event.target.reset();
    showMessage("Reserva registrada correctamente.");
    await loadData();
  } catch (error) {
    showMessage("No se pudo registrar la reserva.", true);
  }
}

function fullName(person = {}) { return [person.nombre, person.apellidos].filter(Boolean).join(" ") || "—"; }
function formatDate(value) {
  if (!value) return "—";
  return new Intl.DateTimeFormat("es-ES", { dateStyle: "medium", timeStyle: "short" }).format(new Date(value));
}
function escapeHtml(value = "") {
  return String(value).replace(/[&<>"']/g, (char) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#039;" })[char]);
}

$("#room-filters").addEventListener("submit", (event) => { event.preventDefault(); loadData(); });
$("#refresh-button").addEventListener("click", loadData);
$("#booking-form").addEventListener("submit", createBooking);
$("#room-form").addEventListener("submit", createRoom);
$("#new-room-button").addEventListener("click", () => $("#room-dialog").showModal());
$("#cancel-room-button").addEventListener("click", () => $("#room-dialog").close());
loadData();
