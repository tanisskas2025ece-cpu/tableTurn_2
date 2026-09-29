// ==========================================
// TABLETURN - FRONTEND JAVASCRIPT
// ==========================================

const BASE_URL = window.location.origin;

const API = {
    tables: "/api/tables",
    reservations: "/api/reservations",
    orders: "/api/orders",
    orderItems: "/api/order-items",
    bills: "/api/bills"
};

// ==========================================
// MODULE CONFIGURATION
// ==========================================

const MODULES = {
    tables: {
        formId: "tableForm",
        resultId: "tablesResult",
        listUrl: "/api/tables/all",
        idInput: "tableId",
        fields: {
            tableNumber: "tableNumber",
            capacity: "tableCapacity",
            status: "tableStatus"
        },
        numberFields: ["capacity"]
    },

    reservations: {
        formId: "reservationForm",
        resultId: "reservationsResult",
        listUrl: "/api/reservations/all",
        idInput: "reservationId",
        fields: {
            tableId: "reservationTableId",
            customerName: "customerName",
            partySize: "partySize",
            startTime: "startTime",
            endTime: "endTime"
        },
        numberFields: ["tableId", "partySize"],
        dateFields: ["startTime", "endTime"]
    },

    orders: {
        formId: "orderForm",
        resultId: "ordersResult",
        listUrl: "/api/orders",
        idInput: "orderId",
        fields: {
            reservationId: "orderReservationId",
            customerName: "orderCustomerName",
            status: "orderStatus",
            totalAmount: "orderTotalAmount"
        },
        numberFields: ["reservationId", "totalAmount"]
    },

    orderItems: {
        formId: "orderItemForm",
        resultId: "orderItemsResult",
        listUrl: "/api/order-items",
        idInput: "orderItemId",
        fields: {
            orderId: "itemOrderId",
            itemName: "itemName",
            quantity: "itemQuantity",
            unitPrice: "itemUnitPrice"
        },
        numberFields: ["orderId", "quantity", "unitPrice"]
    },

    bills: {
        formId: "billForm",
        resultId: "billsResult",
        listUrl: "/api/bills",
        idInput: "billId",
        fields: {
            orderId: "billOrderId",
            taxPercent: "taxPercent",
            discount: "discount",
            paymentStatus: "paymentStatus",
            paymentMethod: "paymentMethod"
        },
        numberFields: ["orderId", "taxPercent", "discount"]
    }
};


// ==========================================
// MESSAGE DISPLAY
// ==========================================

function showMessage(message, isError = false) {
    const messageBox = document.getElementById("message");

    if (!messageBox) {
        alert(message);
        return;
    }

    messageBox.textContent = message;
    messageBox.style.color = isError ? "#c62828" : "#176b38";
}


// ==========================================
// DATE/TIME FORMAT FIX
// ==========================================

function formatDateTime(value) {
    if (!value) {
        return value;
    }

    value = value.trim();

    // Browser datetime-local normally gives:
    // 2026-09-30T12:04
    // Backend LocalDateTime needs:
    // 2026-09-30T12:04:00

    const dateTimeWithoutSeconds =
        /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}$/;

    if (dateTimeWithoutSeconds.test(value)) {
        return value + ":00";
    }

    // If seconds already exist, keep the value.
    // Example: 2026-09-30T12:04:30
    const dateTimeWithSeconds =
        /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}$/;

    if (dateTimeWithSeconds.test(value)) {
        return value;
    }

    // Handle a value that uses a space instead of T.
    // Example: 2026-09-30 12:04
    if (/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}$/.test(value)) {
        return value.replace(" ", "T") + ":00";
    }

    if (/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(value)) {
        return value.replace(" ", "T");
    }

    // Return unchanged if the value is already another format.
    return value;
}


// ==========================================
// API REQUEST
// ==========================================

async function apiRequest(url, method = "GET", data = null) {
    const options = {
        method: method,
        headers: {
            "Accept": "application/json"
        }
    };

    if (data !== null) {
        options.headers["Content-Type"] = "application/json";
        options.body = JSON.stringify(data);
    }

    const response = await fetch(BASE_URL + url, options);
    const responseText = await response.text();

    let result = null;

    if (responseText) {
        try {
            result = JSON.parse(responseText);
        } catch {
            result = responseText;
        }
    }

    if (!response.ok) {
        let message;

        if (typeof result === "string") {
            message = result;
        } else if (result && result.message) {
            message = result.message;
        } else if (result && result.error) {
            message = result.error;
        } else {
            message = JSON.stringify(result);
        }

        throw new Error("HTTP " + response.status + ": " + message);
    }

    return result;
}


// ==========================================
// READ FORM VALUES
// ==========================================

function getFormData(moduleName) {
    const config = MODULES[moduleName];
    const data = {};

    for (const [property, inputId] of Object.entries(config.fields)) {
        const input = document.getElementById(inputId);

        if (!input) {
            continue;
        }

        let value = input.value.trim();

        // Ignore empty fields.
        if (value === "") {
            continue;
        }

        // Convert numeric values.
        if (
            config.numberFields &&
            config.numberFields.includes(property)
        ) {
            value = Number(value);

            if (!Number.isFinite(value)) {
                throw new Error(property + " must be a valid number.");
            }
        }

        // FIX: format LocalDateTime fields correctly.
        if (
            config.dateFields &&
            config.dateFields.includes(property)
        ) {
            value = formatDateTime(value);
        }

        data[property] = value;
    }

    return data;
}


// ==========================================
// DISPLAY RESPONSE
// ==========================================

function displayResult(moduleName, data) {
    const config = MODULES[moduleName];
    const resultBox = document.getElementById(config.resultId);

    if (!resultBox) {
        return;
    }

    if (data === null || data === undefined) {
        resultBox.textContent = "No data returned.";
        return;
    }

    if (typeof data === "string") {
        resultBox.textContent = data;
    } else {
        resultBox.textContent = JSON.stringify(data, null, 2);
    }
}


// ==========================================
// CREATE
// ==========================================

async function createRecord(moduleName, event) {
    if (event) {
        event.preventDefault();
    }

    try {
        const data = getFormData(moduleName);

        console.log("Sending POST data:", data);

        const result = await apiRequest(
            API[moduleName],
            "POST",
            data
        );

        displayResult(moduleName, result);

        showMessage(
            moduleName + " record saved successfully."
        );

        await loadModule(moduleName);

    } catch (error) {
        console.error(error);
        showMessage(error.message, true);
    }
}


// ==========================================
// UPDATE
// ==========================================

async function updateRecord(moduleName) {
    const config = MODULES[moduleName];
    const id = document.getElementById(config.idInput).value.trim();

    if (!id) {
        showMessage("Enter the existing ID before updating.", true);
        return;
    }

    try {
        const data = getFormData(moduleName);

        console.log("Sending PUT data:", data);

        const result = await apiRequest(
            API[moduleName] + "/" + encodeURIComponent(id),
            "PUT",
            data
        );

        displayResult(moduleName, result);

        showMessage(
            moduleName + " record updated successfully."
        );

        await loadModule(moduleName);

    } catch (error) {
        console.error(error);
        showMessage(error.message, true);
    }
}


// ==========================================
// DELETE
// ==========================================

async function deleteRecord(moduleName) {
    const config = MODULES[moduleName];
    const id = document.getElementById(config.idInput).value.trim();

    if (!id) {
        showMessage("Enter the existing ID before deleting.", true);
        return;
    }

    if (!confirm("Delete record ID " + id + "?")) {
        return;
    }

    try {
        const result = await apiRequest(
            API[moduleName] + "/" + encodeURIComponent(id),
            "DELETE"
        );

        displayResult(moduleName, result);

        showMessage(
            moduleName + " delete request completed."
        );

        await loadModule(moduleName);

    } catch (error) {
        console.error(error);
        showMessage(error.message, true);
    }
}


// ==========================================
// LOAD ONE MODULE
// ==========================================

async function loadModule(moduleName) {
    const config = MODULES[moduleName];

    try {
        const result = await apiRequest(config.listUrl);

        displayResult(moduleName, result);

        showMessage(moduleName + " loaded successfully.");

    } catch (error) {
        console.error(error);
        displayResult(moduleName, error.message);
        showMessage(error.message, true);
    }
}


// ==========================================
// LOAD ALL FIVE MODULES
// ==========================================

async function loadAll() {
    for (const moduleName of Object.keys(MODULES)) {
        try {
            const result = await apiRequest(
                MODULES[moduleName].listUrl
            );

            displayResult(moduleName, result);

        } catch (error) {
            console.error(error);
            displayResult(moduleName, error.message);
        }
    }

    showMessage("All modules refreshed.");
}


// ==========================================
// CONNECT FORMS
// ==========================================

document.addEventListener("DOMContentLoaded", function () {
    Object.keys(MODULES).forEach(function (moduleName) {
        const config = MODULES[moduleName];
        const form = document.getElementById(config.formId);

        if (form) {
            form.addEventListener("submit", function (event) {
                createRecord(moduleName, event);
            });
        }
    });

    showMessage("TableTurn frontend is ready.");
});