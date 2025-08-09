export class Admin {
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
