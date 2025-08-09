export class AdminDeleteService {
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