"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
document.addEventListener("DOMContentLoaded", () => {
    const configs = {
        signin: {
            formSelector: '.sign-in-form',
            endpoint: '/signin',
            fields: [
                { name: 'emailOrUsername', selector: 'sl-input[type="text"]', type: 'text' },
                { name: 'password', selector: 'sl-input[type="password"]', type: 'password' }
            ]
        },
        signup: {
            formSelector: '.sign-up-form',
            endpoint: '/signup',
            fields: [
                { name: 'email', selector: 'sl-input[type="email"]', type: 'email' },
                { name: 'username', selector: 'sl-input[type="text"]', type: 'text' },
                { name: 'password', selector: 'sl-input[type="password"]', type: 'password' }
            ]
        }
    };
    Object.keys(configs).forEach(formType => {
        const { formSelector, endpoint, fields } = configs[formType]; // use `endpoint`
        const form = document.querySelector(formSelector);
        if (!form)
            return;
        const errorLabel = form.querySelector('.error-label');
        form.addEventListener('submit', (e) => __awaiter(void 0, void 0, void 0, function* () {
            e.preventDefault();
            if (errorLabel)
                errorLabel.style.visibility = 'hidden';
            const inputs = fields.map(f => ({
                name: f.name,
                el: form.querySelector(f.selector),
            }));
            let isValid = true;
            const payload = {};
            inputs.forEach(({ name, el }) => {
                const v = el.value.trim();
                if (!v) {
                    el.setCustomValidity(`${capitalize(name)} is required`);
                    el.reportValidity();
                    isValid = false;
                }
                else {
                    el.setCustomValidity('');
                }
                payload[name] = v;
            });
            if (!isValid)
                return;
            try {
                const username = yield sendRequest(endpoint, payload);
                if (username) {
                    goToSignIn(username);
                }
            }
            catch (err) {
                showError(err, errorLabel);
            }
        }));
    });
});
function sendRequest(endpoint, body) {
    return __awaiter(this, void 0, void 0, function* () {
        console.log('Sending request...');
        const resp = yield fetch(`${window.ctx}${endpoint}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body),
        });
        if (resp.redirected) {
            window.location.href = resp.url;
            return;
        }
        if (!resp.ok) {
            const text = yield resp.text();
            throw new Error(text || resp.statusText);
        }
        return resp.json();
    });
}
function showError(err, label) {
    const message = err instanceof Error ? err.message : "Unexpected Error";
    if (label) {
        label.style.visibility = 'visible';
        label.textContent = message;
    }
    else {
        alert(message);
    }
}
function capitalize(s) {
    return s.charAt(0).toUpperCase() + s.slice(1);
}
function goToSignIn(username) {
    const usernamePlaceholder = document.getElementById('usernamePlaceholder');
    usernamePlaceholder.value = username;
    const container = document.querySelector('.container');
    container === null || container === void 0 ? void 0 : container.classList.remove('sign-up-mode');
}
