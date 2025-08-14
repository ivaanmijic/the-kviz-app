import { AdminApiClient } from "../client/adminApiClient.js";
import { Admin } from "../model/admin.js";
import { AlertManager } from "../manager/alertManager.js";


export class AdminController {
    constructor(baseUrl = '') {
        this.api = new AdminApiClient(baseUrl);
        this._initializeDeleteDialogListeners();
    }

    _initializeDeleteDialogListeners() {
        const deleteDialog = document.querySelector('#deleteAdminDialog');
        if (!deleteDialog) {
            return;
        }

        const confirmButton = document.querySelector('#confirmDeleteBtn');
        const cancelButton = document.querySelector('#cancelDeleteBtn');

        confirmButton.addEventListener('click', async () => {
            const adminId = deleteDialog.dataset.adminId;
            if (!adminId) return;

            confirmButton.loading = true;
            cancelButton.disabled = true;

            try {
                await this.api.delete(adminId);
                deleteDialog.dataset.deleteStatus = 'success';
            } catch (jqXHR) {
                deleteDialog.dataset.deleteStatus = 'fail';
                deleteDialog.dataset.errorMessage = jqXHR.responseJSON?.error || 'Failed to delete admin';
            } finally {
                deleteDialog.hide();
            }
        });

        cancelButton.addEventListener('click', () => deleteDialog.hide());

        deleteDialog.addEventListener('sl-after-hide', () => {
            const adminId = deleteDialog.dataset.adminId;
            const adminUsername = deleteDialog.dataset.adminUsername;

            if (deleteDialog.dataset.deleteStatus === 'success') {
                document.querySelector(`sl-button[data-admin-id='${adminId}']`)?.closest('tr')?.remove();
                AlertManager.showSuccess(`Successfully deleted admin ${adminUsername}`);
            } else if (deleteDialog.dataset.deleteStatus === 'fail') {
                AlertManager.showError(deleteDialog.dataset.errorMessage);
            }

            confirmButton.loading = false;
            cancelButton.disabled = false;
            delete deleteDialog.dataset.adminId;
            delete deleteDialog.dataset.adminUsername;
            delete deleteDialog.dataset.deleteStatus;
            delete deleteDialog.dataset.errorMessage;
        });
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
                AlertManager.showError(msg);
                console.error('Admin API error', error);
                return null
            });
    }

    async renderAdmins(admins) {
        const section = document.createElement('section');
        const sectionTitle = document.createElement('h2');
        sectionTitle.classList.add('section-title');
        sectionTitle.textContent = "Users"
        section.appendChild(sectionTitle);

        const container = document.createElement('div');
        container.className = 'w-full';

        try {
            const tableResponse = await fetch(`${window.ctx}/templates/admin-table.html`);
            if (!tableResponse.ok) throw new Error('Failed to load admin table template');
            container.innerHTML = await tableResponse.text();

            const tbody = container.querySelector('tbody');

            const rowResponse = await fetch(`${window.ctx}/templates/admin-row.html`);
            if (!rowResponse.ok) throw new Error('Failed to load admin row template');
            const rowTemplate = await rowResponse.text();

            admins.forEach((admin) => {
                const isYou = String(admin.id) === String(window.admin.id);
                const role = String(admin.role).toUpperCase();

                let rowHTML = rowTemplate
                    .replace('{{username}}', admin.username + (isYou ? '(You)' : ''))
                    .replace(/{{email}}/g, admin.email)
                    .replace('{{role}}', admin.role)
                    .replace('{{roleClass}}', role === 'SUPERADMIN' ? 'bg-red-100 text-red-800' : 'bg-blue-100 text-blue-800')
                    .replace('{{roleLabel}}', admin.role.charAt(0).toUpperCase() + admin.role.slice(1).toLowerCase());

                const tempTr = document.createElement('tbody');
                tempTr.innerHTML = rowHTML;
                const rowElement = tempTr.firstElementChild;

                rowElement.dataset.adminRowId = admin.id;

                if (!isYou) {
                    const actionWrapper = rowElement.querySelector('.action-buttons');
                    actionWrapper.innerHTML = `
                        <sl-button outline variant="neutral" class="sl-button sl-button-secondary text-sm !p-2" data-action="edit" data-admin-id="${admin.id}">Edit</sl-button>
                        <sl-button outline variant="danger" class="sl-button sl-button-danger text-sm !p-2 delete-admin-btn" data-username="${admin.username}" data-admin-id="${admin.id}" data-action="delete" data-email="${admin.email}">Delete</sl-button>
                    `;
                }
                tbody.appendChild(rowElement);
            });

            container.addEventListener('click', (e) => {
                const button = e.target.closest('sl-button[data-action]');
                if (!button) { return; }

                const adminId = button.dataset.adminId;
                const action = button.dataset.action;

                if (action === 'delete') {
                    const adminUsername = button.dataset.username;
                    this.showDeleteDialog(adminId, adminUsername);
                } else if (action === 'edit') {
                    console.log(adminId)
                    if (adminId) {
                        window.loadView('profile', { id: adminId, from: 'admin-list' });
                    }
                }
            });

        } catch (error) {
            console.log('Failed to render admins:', error);
            AlertManager.showError('Could not display admin list.');
        }

        section.appendChild(container);
        return section;
    }

    showDeleteDialog(adminId, adminUsername) {
        const deleteDialog = document.querySelector('#deleteAdminDialog');
        if (!deleteDialog) {
            console.error('Delete dialog element #deleteAdminDialog not found');
            AlertManager.showError('Error: Cound not open the delete confirmation dialog');
            return;
        }

        const usernameSpan = deleteDialog.querySelector('#deleteAdminUsername');
        if (!usernameSpan) {
            console.error('Delete dialog username span #deleteAdminUsername not found');
            AlertManager.showError('Error: Could not find admin username element in the dialog');
            return;
        }

        deleteDialog.dataset.adminId = adminId;
        deleteDialog.dataset.adminUsername = adminUsername;
        usernameSpan.textContent = adminUsername;

        deleteDialog.show();
    }

    setupProfileListeners(profileId) {
        console.log('setupProfileListeners for admin profile', profileId);
        const $form = $('#editProfileForm');
        if (!$form.length) return;

        const $updateBtn = $('#updateBtn');
        const $deleteBtn = $('#deleteProfileBtn');
        const $deleteDialog = $('#deleteDialog');
        const $confirmDeleteBtn = $('#confirmDeleteBtn');
        const $cancelDeleteBtn = $('#cancelDeleteBtn');

        $form.on('input', () => {
            $updateBtn.prop('disabled', false);
        });

        $form.on('submit', async (e) => {
            e.preventDefault();
            $updateBtn.prop('loading', true);

            const formData = new FormData(e.target);
            const flatData = Object.fromEntries(formData.entries());

            const payload = {
                admin: {
                    email: flatData.email,
                    username: flatData.username,
                    role: flatData.role,
                    password: flatData['old-password'],
                },
                newPassword: flatData['new-password'],
            };

            try {
                console.log(payload);
                await this.api.update(profileId, payload);
                AlertManager.showSuccess('Successfully updated profile');
                $updateBtn.prop('disabled', true);
            } catch (jqXHR) {
                const errorMsg = jqXHR.responseJSON?.error || 'Failed to update profile';
                AlertManager.showError(errorMsg);
            } finally {
                $updateBtn.prop('loading', false);
            }
        });

        $deleteBtn.on('click', () => {
            $deleteDialog[0].show();
        });

        $cancelDeleteBtn.on('click', () => {
            $deleteDialog[0].hide();
        });

        $confirmDeleteBtn.on('click', async (e) => {
            $confirmDeleteBtn.prop('loading', true);
            try {
                const self = String(profileId) === String(window.admin.id);
                await this.api.delete(profileId, self);

                if (self) {
                    window.location.reload();
                } else {
                    window.loadView('admin-list');
                }
            } catch (jqXHR) {
                const errorMsg = jqXHR.responseJSON?.error || 'Failed to delete profile';
                AlertManager.showError(errorMsg);
                $confirmDeleteBtn.prop('loading', false);
                $deleteDialog[0].hide();
            }
        });
    }
}