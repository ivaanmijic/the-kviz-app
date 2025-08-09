export class AdminApiClient {
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