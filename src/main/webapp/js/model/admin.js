export class Admin {
    createdAt = null
    updatedAt = null

    constructor({id, username, email, role, password, createdAt, updatedAt}) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    toJSON() {
        return {
            id: this.id,
            username: this.username,
            email: this.email,
            role: this.role,
            password: this.password,
            createdAt: this.createdAt,
            updatedAt: this.updatedAt,
        };
    }

    static fromJson(obj) {
        return new Admin({
            id: obj.id,
            username: obj.username,
            email: obj.email,
            role: obj.role,
            createdAt: obj.createdAt,
            updatedAt: obj.updatedAt,
            password: ""
        })
    }
}
