export class ScreenManager {
    constructor(rootId) {
        this.root = document.getElementById(rootId);
        this.screens = {};
    }

    async loadScreen(name, url) {
        const resp = await fetch(url);
        this.root.innerHTML = await resp.text();

        if (this.screens[name]) {
            this.screens[name].init();
        }
    }

    registerScreen(name, screen) {
        this.screens[name] = screen;
    }
}
