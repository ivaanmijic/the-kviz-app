export class AdminUpdateService {
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