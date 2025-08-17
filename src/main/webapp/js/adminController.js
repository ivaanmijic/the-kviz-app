class Admin {
    constructor({id, username, email, role, password}) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    toJSON() {
        return {
            id: this.id,
            username: this.username,
            email: this.email,
            role: this.role,
            password: this.password
        };
    }
}


class AdminApi {
    constructor(basePath = '') {
        this.base = basePath;
    }

    delete(id, self = false) {
        return new Promise((resolve, reject) => {
            $.post(this.base + '/admin/delete', {id, self: String(self)})
                .done(data => resolve(data))
                .fail((jqXHR) => reject(jqXHR));
        });
    }

    update(admin, newPassword) {
        return fetch(`${this.base}/admin/update?id=${admin.id}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({admin: admin.toJSON(), newPassword})
        });
    }
}

class AdminDeleteController {
    constructor({api, selector = '.delete-admin-btn'}) {
        this.api = api;
        this.selector = selector;

        this._bindButtons();
    }

    _bindButtons() {
        document.querySelectorAll(this.selector).forEach(btn => {
            btn.addEventListener('click', () => this._onDeleteClick(btn));
        });
    }

    async _onDeleteClick(button) {
        const id = button.dataset.adminId;
        const self = button.dataset.self === 'true';

        try {
            const data = await this.api.delete(id, self);
            this._handleSuccess(id, self, data);
        } catch (jqXHR) {
            this._handleError(jqXHR);
        }
    }

    _handleSuccess(id, self, data) {
        console.log("Deleted admin:", data);
        if (self) {
            return location.reload();
        }

        ['Card', 'Drawer', 'Dialog'].forEach(suffix => {
            const el = document.getElementById(`${id}${suffix}`);
            if (el) el.remove();
        });
    }

    _handleError(jqXHR) {
        const alert = document.getElementById('errorAlert');
        alert.innerHTML = `
      <sl-icon slot="icon" name="exclamation-octagon"></sl-icon>
      <strong>${jqXHR.responseText || 'Delete failed'}</strong><br/>
      Please try again.
    `;
        alert.show();
    }
}


class AdminUpdateController {
    constructor({adminId, api, role, contextPath}) {
        this.adminId = adminId;
        this.api = api;
        this.role = role;
        this.contextPath = contextPath;

        this.dialog = document.getElementById(`${adminId}EditDialog`);
        if (!this.dialog) return;

        this.inputs = {
            email: this.dialog.querySelector('sl-input[name="email"]'),
            username: this.dialog.querySelector('sl-input[name="username"]'),
            oldPw: this.dialog.querySelector('sl-input[name="old-password"]'),
            newPw: this.dialog.querySelector('sl-input[name="new-password"]'),
            repeatPw: this.dialog.querySelector('sl-input[name="rep-password"]'),
            matchIcon: this.dialog.querySelector('#pwMatchIndicator'),
            updateBtn: this.dialog.querySelector('#updateBtn')
        };

        this._bindEvents();
    }

    _bindEvents() {
        this.dialog.addEventListener('sl-show', () => {('submitQuiz').innerText = 'Edit Quiz';
            this._storeOriginalValues();
            this._toggleUpdate();
        });

        Object.values(this.inputs).forEach(el => {
            if (el.addEventListener)
                el.addEventListener('sl-input', () => this._toggleUpdate());
        });

        this.inputs.updateBtn.addEventListener('click', () => this._onUpdateClick());
    }

    _storeOriginalValues() {
        this.original = {
            email: this.inputs.email.value,
            username: this.inputs.username.value
        };
    }

    _passwordsMatch() {
        const {newPw, repeatPw, matchIcon} = this.inputs;
        if (newPw.value || repeatPw.value) {
            const match = newPw.value === repeatPw.value;
            matchIcon.textContent = match ? '✅' : '❌';
            return match;
        }
        matchIcon.textContent = '';
        return true;
    }

    _toggleUpdate() {
        const {email, username, oldPw, newPw} = this.inputs;
        const changedEmail = email.value !== this.original.email;
        const changedUsername = username.value !== this.original.username;
        const pwEntered = newPw.value.length > 0;
        const validPwChange = pwEntered && this._passwordsMatch();

        const canUpdate = oldPw.value.length > 0
            && (changedEmail || changedUsername || validPwChange);

        this.inputs.updateBtn.disabled = !canUpdate;
    }

    async _onUpdateClick() {
        const {email, username, oldPw, newPw} = this.inputs;
        const admin = new Admin({
            id: this.adminId,
            username: username.value,
            email: email.value,
            role: this.role,
            password: oldPw.value
        });

        try {
            const res = await this.api.update(admin, newPw.value);
            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.error || 'Update failed.');
            }
            this._showSuccess();
        } catch (e) {
            this._showError(e.message);
        }
    }

    _showSuccess() {
        const alert = document.getElementById('succesAlert');
        alert.innerHTML = `
      <sl-icon slot="icon" name="info-circle"></sl-icon>
      <strong>Profile updated!</strong><br/>Reloading...
    `;
        alert.show();
        alert.addEventListener('sl-after-hide', () => location.reload());
    }

    _showError(msg) {
        const alert = document.getElementById('errorAlert');
        alert.innerHTML = `
      <sl-icon slot="icon" name="exclamation-octagon"></sl-icon>
      <strong>${msg}</strong><br/>Please try again.
    `;
        alert.show();
    }
}


document.addEventListener('DOMContentLoaded', () => {
    const api    = new AdminApi(contextPath);
    new AdminUpdateController({ adminId, api, role, contextPath });
    new AdminDeleteController( {api, selector: '.delete-admin-btn'});
});
