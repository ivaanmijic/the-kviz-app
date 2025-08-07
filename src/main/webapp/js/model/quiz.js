export class Quiz {
    constructor({ id, title, description, thumbnail, owner, category, visible }) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.category = category;
        this.visible = visible;
    }

    toJson() {
        return {
            id: this.id,
            title: this.title,
            description: this.description,
            thumbnail: this.thumbnail,
            category: this.category,
            visible: this.visible,
        };
    }

    static fromJson(json) {
        return new Quiz(json);
    }
}