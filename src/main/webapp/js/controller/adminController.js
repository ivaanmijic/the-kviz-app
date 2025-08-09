import { AdminApiClient } from "../client/adminApiClient.js";
import { Admin } from "../model/admin.js";
import { AlertManager } from "../manager/alertManager.js";


export class AdminController {
    constructor(baseUrl = '') {
        this.api = new AdminApiClient(baseUrl);
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

                if (!isYou) {
                    const actionWrapper = rowElement.querySelector('.action-buttons');
                    actionWrapper.innerHTML = `
                        <sl-button outline variant="neutral" class="sl-button sl-button-secondary text-sm !p-2" data-action="edit" data-email="${admin.email}">Edit</sl-button>
                        <sl-button outline variant="danger" class="sl-button sl-button-danger text-sm !p-2 delete-admin-btn" data-admin-id="${admin.id}" data-action="delete" data-email="${admin.email}">Delete</sl-button>
                    `;
                }
                tbody.appendChild(rowElement);
            });

        } catch (error) {
            console.log('Failed to render admins:', error);
            AlertManager.showError('Could not display admin list.');
        }

        section.appendChild(container);
        return section;
    }
}