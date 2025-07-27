interface ShoelaceInputElement extends HTMLElement {
    value: string;
    setCustomValidity(message: string): void;
    reportValidity(): void;
}

type FormType = "signin" | "signup";

interface FormConfiguration {
    formSelector: string;
    endpoint: string;
    fields: { name: string; selector: string; type: string }[];
}

document.addEventListener("DOMContentLoaded", () => {
    const configs: Record<FormType, FormConfiguration> = {
        signin: {
            formSelector: '.sign-in-form',
            endpoint: '/signin',
            fields: [
                { name: 'emailOrUsername', selector: 'sl-input[type="text"]',    type: 'text'     },
                { name: 'password',          selector: 'sl-input[type="password"]', type: 'password' }
            ]
        },
        signup: {
            formSelector: '.sign-up-form',
            endpoint: '/signup',
            fields: [
                { name: 'email',    selector: 'sl-input[type="email"]',    type: 'email'    },
                { name: 'username', selector: 'sl-input[type="text"]',     type: 'text'     },
                { name: 'password', selector: 'sl-input[type="password"]', type: 'password' }
            ]
        }
    };

    (Object.keys(configs) as FormType[]).forEach(formType => {
        const { formSelector, endpoint, fields } = configs[formType];  // use `endpoint`
        const form = document.querySelector<HTMLFormElement>(formSelector);
        if (!form) return;

        const errorLabel = form.querySelector<HTMLElement>('.error-label');

        form.addEventListener('submit', async e => {
            e.preventDefault();
            if (errorLabel) errorLabel.style.visibility = 'hidden';

            const inputs = fields.map(f => ({
                name: f.name,
                el: form.querySelector<ShoelaceInputElement>(f.selector)!,
            }));

            let isValid = true;
            const payload: Record<string, string> = {};
            inputs.forEach(({ name, el }) => {
                const v = el.value.trim();
                if (!v) {
                    el.setCustomValidity(`${capitalize(name)} is required`);
                    el.reportValidity();
                    isValid = false;
                } else {
                    el.setCustomValidity('');
                }
                payload[name] = v;
            });
            if (!isValid) return;

            try {
                const username = await sendRequest(endpoint, payload);
                if (username) {
                    goToSignIn(username);
                }
            } catch (err) {
                showError(err, errorLabel);
            }
        });
    });
});

async function sendRequest(endpoint: string, body: Record<string, any>) {
    console.log('Sending request...');
    const resp = await fetch(`${window.ctx}${endpoint}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
    });

    if (resp.redirected) {
        window.location.href = resp.url;
        return;
    }

    if (!resp.ok) {
        const text = await resp.text();
        throw new Error(text || resp.statusText);
    }
    return resp.json();
}

function showError(err: unknown, label: HTMLElement | null) {
    const message = err instanceof Error ? err.message : "Unexpected Error";
    if (label) {
        label.style.visibility = 'visible';
        label.textContent = message;
    } else {
        alert(message);
    }
}

function capitalize(s: string) {
    return s.charAt(0).toUpperCase() + s.slice(1);
}

function goToSignIn(username: string) {
    const usernamePlaceholder = document.getElementById('usernamePlaceholder') as ShoelaceInputElement;
    usernamePlaceholder.value = username;

    const container = document.querySelector('.container');
    container?.classList.remove('sign-up-mode')
}
