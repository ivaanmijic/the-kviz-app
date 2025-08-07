export class QuizApiClient {
    constructor(baseUrl = '') {
        this.base = baseUrl;
    }

    getList(adminId = null) {
        const params = {}
        if (adminId && adminId !== '') params.admin_id = adminId;
        return $.get(`${this.base}/admin/quiz/list`, params)
    }
}