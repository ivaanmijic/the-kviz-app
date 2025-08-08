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

    static fromJson(obj) {
        return new Admin({
            id: obj.id,
            username: obj.username,
            email: obj.email,
            role: obj.role,
            password: ""
        })
    }
}


class AdminApi {
    constructor(basePath = '') {
        this.base = basePath;
    }

    delete(id, self = false) {
        return new Promise((resolve, reject) => {
            $.delete(this.base + `/admins/${id}`, {self: String(self)})
                .done(data => resolve(data))
                .fail((jqXHR) => reject(jqXHR));
        });
    }

    update(admin, newPassword) {
        return fetch(`${this.base}/admins/${admin.id}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({admin: admin.toJSON(), newPassword})
        });
    }

    getList() {
        return $.get(`${this.base}/admins`, {});
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
        this.dialog.addEventListener('sl-show', () => {
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

export class AdminController {
    constructor(baseUrl = '') {
        this.api = new AdminApi(baseUrl);
    }

    getAdmins() {
        return this.api.getList()
            .then(data => {
                console.log(data)
                const admins = data.editors.map(Admin.fromJson)
                return this.renderAdmins(admins);
            })
            .catch(error => {
                let msg = "Failed to fetch admins.";
                try {
                    const json = JSON.parse(error.responseText || '{}');
                    if (json.error) {
                        msg = json.error;
                    }
                } catch (e) {
                }
                this.showError(msg);
                console.error('Admin API error', error);
                return null
            });
    }

    renderAdmins(admins) {
        const container = document.createElement('div');
        container.className = 'w-full';

        const card = document.createElement('div');
        card.className = 'sl-card';

        const table = document.createElement('table');
        table.className = 'w-full text-left';

        const thead = document.createElement('thead');
        thead.className = 'bg-gray-50';
        const headRow = document.createElement('tr');

        ['Username', 'Email', 'Role', 'Actions'].forEach(text => {
            const th = document.createElement('th');
            th.className = 'p-4 font-semibold';
            th.textContent = text;
            headRow.appendChild(th);
        });
        thead.appendChild(headRow);
        table.appendChild(thead);

        const tbody = document.createElement('tbody');
        tbody.className = 'divide-y';

        const roleLabel = (role = '') => {
            if (!role) return '';
            const r = String(role).trim().toLowerCase();
            return role[0].toUpperCase() + role.slice(1).toLowerCase();
        };

        admins.forEach(admin => {
            const {id = '', username = '', email = '', role = '' } = admin;
            const tr = document.createElement('tr');
            tr.className = 'hover:bg-gray-50';
            tr.dataset.email = email;
            tr.dataset.role = role;
            const isYou = id === String(window.admin.id);

            const nameTd = document.createElement('td');
            nameTd.className = 'p-4';
            nameTd.dataset.label = 'Username';
            nameTd.textContent = username + (isYou ? ' (You)' : '');

            const emailTd = document.createElement('td');
            emailTd.className = 'p-4';
            emailTd.dataset.label = 'Email';
            emailTd.textContent = email;

            const roleTd = document.createElement('td');
            roleTd.className = 'p-4';
            roleTd.dataset.label = 'Role';
            const badge = document.createElement('span');
            badge.className = 'text-xs font-medium mr-2 px-2.5 py-0.5 rounded-full';
            if (String(role).toUpperCase() === 'SUPERADMIN') {
                badge.classList.add('bg-red-100', 'text-red-800');
            } else {
                badge.classList.add('bg-blue-100', 'text-blue-800');
            }
            badge.textContent = roleLabel(role);
            roleTd.appendChild(badge);

            const actionsTd = document.createElement('td');
            actionsTd.className = 'p-4';
            actionsTd.dataset.label = 'Actions';

            const actionWrapper = document.createElement('div');
            actionWrapper.className = 'action-buttons flex space-x-2';

            if (!isYou) {
                const editBtn = document.createElement('sl-button');
                editBtn.setAttribute('outline', '');
                editBtn.variant = 'neutral';
                editBtn.className = 'sl-button sl-button-secondary text-sm !p-2';
                editBtn.textContent = 'Edit';
                editBtn.dataset.action = 'edit';
                editBtn.dataset.email = email;

                const deleteBtn = document.createElement('sl-button');
                deleteBtn.setAttribute('outline', '');
                deleteBtn.variant = 'danger'
                deleteBtn.className = 'sl-button sl-button-danger text-sm !p-2';
                deleteBtn.textContent = 'Delete';
                deleteBtn.dataset.action = 'delete';
                deleteBtn.dataset.email = email;

                actionWrapper.appendChild(editBtn);
                actionWrapper.appendChild(deleteBtn);
            }

            actionsTd.appendChild(actionWrapper);

            tr.appendChild(nameTd);
            tr.appendChild(emailTd);
            tr.appendChild(roleTd);
            tr.appendChild(actionsTd);

            tbody.appendChild(tr);
        });

        table.appendChild(tbody);
        card.appendChild(table);

        container.appendChild(card);

        return container;
    }

    showError(message) {
        let alert = document.getElementById('errorAlert');
        if (alert) {
            alert.innerHTML = `<sl-icon slot=\"icon\" name=\"exclamation-octagon\"></sl-icon> ${message}'`;
        }
        alert.show();
    }

    clearAlerts() {
        const alert = document.getElementById('errorAlert');
        if (alert) {
            alert.hide();
        }
    }

}

document.addEventListener('DOMContentLoaded', () => {
    const api    = new AdminApi(`${window.ctx}`);
    new AdminUpdateController({ adminId, api, role, contextPath });
    new AdminDeleteController( {api, selector: '.delete-admin-btn'});
});
