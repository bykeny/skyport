const apiBaseInput = document.querySelector("#apiBaseUrl");
const retailApiBaseInput = document.querySelector("#retailApiBaseUrl");
const createForm = document.querySelector("#createForm");
const listForm = document.querySelector("#listForm");
const productForm = document.querySelector("#productForm");
const orderForm = document.querySelector("#orderForm");
const loadProductsButton = document.querySelector("#loadProductsButton");
const createState = document.querySelector("#createState");
const listState = document.querySelector("#listState");
const productState = document.querySelector("#productState");
const orderState = document.querySelector("#orderState");
const createResult = document.querySelector("#createResult");
const productResult = document.querySelector("#productResult");
const orderResult = document.querySelector("#orderResult");
const checkinRows = document.querySelector("#checkinRows");
const productRows = document.querySelector("#productRows");
const pageTitle = document.querySelector("#pageTitle");
const navLinks = document.querySelectorAll(".nav-link");
const views = document.querySelectorAll(".view");
const serviceTiles = document.querySelectorAll("[data-view-target]");

const storedBaseUrl = localStorage.getItem("airportApiBaseUrl");
if (storedBaseUrl) {
  apiBaseInput.value = storedBaseUrl;
}

const storedRetailBaseUrl = localStorage.getItem("airportRetailApiBaseUrl");
if (storedRetailBaseUrl) {
  retailApiBaseInput.value = storedRetailBaseUrl;
}

apiBaseInput.addEventListener("change", () => {
  localStorage.setItem("airportApiBaseUrl", apiBaseInput.value.trim());
});

retailApiBaseInput.addEventListener("change", () => {
  localStorage.setItem("airportRetailApiBaseUrl", retailApiBaseInput.value.trim());
});

navLinks.forEach((button) => {
  button.addEventListener("click", () => {
    showView(button.dataset.view, true);
  });
});

serviceTiles.forEach((tile) => {
  tile.addEventListener("click", () => {
    showView(tile.dataset.viewTarget, true);
  });
});

window.addEventListener("hashchange", () => {
  showView(getViewFromHash(), false);
});

showView(getViewFromHash(), false);

createForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  setState(createState, "Saving", "busy");
  createForm.querySelector("button").disabled = true;

  const formData = new FormData(createForm);
  const payload = {
    passengerId: Number(formData.get("passengerId")),
    flightId: Number(formData.get("flightId")),
    seatNumber: String(formData.get("seatNumber")).trim(),
    baggageCount: Number(formData.get("baggageCount") || 0),
  };

  try {
    const data = await request("/api/v1/checkins", {
      method: "POST",
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify(payload),
    });
    createResult.textContent = JSON.stringify(data, null, 2);
    listForm.elements.flightId.value = data.flightId;
    renderRows(await request(`/api/v1/checkins/flight/${data.flightId}`));
    setState(createState, "Created", "ok");
  } catch (error) {
    createResult.textContent = error.message;
    setState(createState, "Error", "error");
  } finally {
    createForm.querySelector("button").disabled = false;
  }
});

listForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  setState(listState, "Loading", "busy");
  listForm.querySelector("button").disabled = true;

  try {
    const flightId = Number(new FormData(listForm).get("flightId"));
    const data = await request(`/api/v1/checkins/flight/${flightId}`);
    renderRows(data);
    setState(listState, "Loaded", "ok");
  } catch (error) {
    checkinRows.innerHTML = `<tr><td colspan="6" class="empty">${escapeHtml(error.message)}</td></tr>`;
    setState(listState, "Error", "error");
  } finally {
    listForm.querySelector("button").disabled = false;
  }
});

productForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  setState(productState, "Saving", "busy");
  productForm.querySelector("button").disabled = true;

  const formData = new FormData(productForm);
  const payload = {
    sku: String(formData.get("sku")).trim(),
    name: String(formData.get("name")).trim(),
    category: String(formData.get("category")).trim(),
    price: Number(formData.get("price")),
    stockQuantity: Number(formData.get("stockQuantity")),
  };

  try {
    const data = await retailRequest("/api/v1/products", {
      method: "POST",
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify(payload),
    });
    productResult.textContent = JSON.stringify(data, null, 2);
    orderForm.elements.productId.value = data.id;
    renderProducts(await retailRequest("/api/v1/products"));
    setState(productState, "Created", "ok");
  } catch (error) {
    productResult.textContent = error.message;
    setState(productState, "Error", "error");
  } finally {
    productForm.querySelector("button").disabled = false;
  }
});

orderForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  setState(orderState, "Saving", "busy");
  orderForm.querySelector("button").disabled = true;

  const formData = new FormData(orderForm);
  const payload = {
    passengerId: Number(formData.get("passengerId")),
    flightId: Number(formData.get("flightId")),
    productId: Number(formData.get("productId")),
    quantity: Number(formData.get("quantity")),
  };

  try {
    const data = await retailRequest("/api/v1/orders", {
      method: "POST",
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify(payload),
    });
    orderResult.textContent = JSON.stringify(data, null, 2);
    renderProducts(await retailRequest("/api/v1/products"));
    setState(orderState, "Created", "ok");
  } catch (error) {
    orderResult.textContent = error.message;
    setState(orderState, "Error", "error");
  } finally {
    orderForm.querySelector("button").disabled = false;
  }
});

loadProductsButton.addEventListener("click", async () => {
  setState(orderState, "Loading", "busy");
  loadProductsButton.disabled = true;

  try {
    renderProducts(await retailRequest("/api/v1/products"));
    setState(orderState, "Loaded", "ok");
  } catch (error) {
    productRows.innerHTML = `<tr><td colspan="5" class="empty">${escapeHtml(error.message)}</td></tr>`;
    setState(orderState, "Error", "error");
  } finally {
    loadProductsButton.disabled = false;
  }
});

async function request(path, options = {}) {
  const baseUrl = apiBaseInput.value.trim().replace(/\/$/, "");
  return rawRequest(baseUrl, path, options);
}

async function retailRequest(path, options = {}) {
  const baseUrl = retailApiBaseInput.value.trim().replace(/\/$/, "");
  return rawRequest(baseUrl, path, options);
}

async function rawRequest(baseUrl, path, options = {}) {
  const response = await fetch(`${baseUrl}${path}`, options);
  const contentType = response.headers.get("content-type") || "";
  const body = contentType.includes("application/json") ? await response.json() : await response.text();

  if (!response.ok) {
    const message = typeof body === "object" && body.error ? body.error : `Request failed with ${response.status}`;
    throw new Error(message);
  }

  return body;
}

function renderProducts(products) {
  if (!Array.isArray(products) || products.length === 0) {
    productRows.innerHTML = '<tr><td colspan="5" class="empty">No products found</td></tr>';
    return;
  }

  productRows.innerHTML = products.map((product) => `
    <tr>
      <td>${product.id}</td>
      <td>${escapeHtml(product.sku || "")}</td>
      <td>${escapeHtml(product.name || "")}</td>
      <td>${product.price}</td>
      <td>${product.stockQuantity ?? 0}</td>
    </tr>
  `).join("");
}

function renderRows(checkins) {
  if (!Array.isArray(checkins) || checkins.length === 0) {
    checkinRows.innerHTML = '<tr><td colspan="6" class="empty">No check-ins found</td></tr>';
    return;
  }

  checkinRows.innerHTML = checkins.map((checkin) => `
    <tr>
      <td>${checkin.id}</td>
      <td>${checkin.passengerId}</td>
      <td>${escapeHtml(checkin.seatNumber || "")}</td>
      <td>${escapeHtml(checkin.boardingPassCode || "")}</td>
      <td>${checkin.baggageCount ?? 0}</td>
      <td>${escapeHtml(checkin.status || "")}</td>
    </tr>
  `).join("");
}

function setState(element, label, mode) {
  element.textContent = label;
  element.className = `state is-${mode}`;
}

function escapeHtml(value) {
  return String(value)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function getViewFromHash() {
  const view = window.location.hash.replace("#", "");
  return view || "dashboard";
}

function showView(viewName, updateHash) {
  const activeView = document.querySelector(`#${viewName}View`) ? viewName : "dashboard";

  views.forEach((view) => {
    view.classList.toggle("is-active", view.id === `${activeView}View`);
  });

  navLinks.forEach((button) => {
    button.classList.toggle("is-active", button.dataset.view === activeView);
  });

  const viewElement = document.querySelector(`#${activeView}View`);
  pageTitle.textContent = viewElement.dataset.title;

  if (updateHash) {
    window.location.hash = activeView;
  }
}
