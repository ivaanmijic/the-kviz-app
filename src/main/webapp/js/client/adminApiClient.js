export class AdminApiClient {
    constructor(basePath = '') {
        this.base = basePath;
    }

    delete(id, self = false) {
        return $.ajax({
            url: `${this.base}/admins/${id}`,
            type: 'DELETE',
            data: {self: String(self)},
        });
    }

    update(id, data) {
        return $.ajax({
            url: `${this.base}/admins/${id}`,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(data)
        });
    }

    getList() {
        return $.get(`${this.base}/admins`, {});
    }

}