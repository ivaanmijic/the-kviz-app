export class AlertManager {
    static alertElement = null;
    static iconElement = null;
    static messageElement = null;
    static onHideCallback = null;

    static init() {
        this.alertElement = document.querySelector('#globalAlert');
        if (this.alertElement) {
            this.iconElement = this.alertElement.querySelector('#globalAlertIcon');
            this.messageElement = this.alertElement.querySelector('#globalAlertMessage');

            this.alertElement.addEventListener('sl-after-hide', () => {
                if (typeof this.onHideCallback === 'function') {
                    this.onHideCallback();
                }
                this.onHideCallback = null;
            });
        }
    }

    static show(message, variant = 'primary', icon = 'info-circle', duration = 4000, onHide = null) {
        if (!this.alertElement) {
            console.error('AlertManager could not find the #globalAlert element. Make sure it exists in your HTML.');
            return;
        }

        this.alertElement.hide();

        this.alertElement.variant = variant;
        this.alertElement.duration = duration;
        this.iconElement.name = icon;
        this.messageElement.textContent = message;

        this.onHideCallback = onHide;

        setTimeout(() => this.alertElement.show(), 50);
    }

    static showInfo(message, onHideCallback = null) {
        this.show(message, 'primary', 'info-circle', 4000, onHideCallback);
    }

    static showSuccess(message, onHideCallback = null) {
        this.show(message, 'success', 'check2-circle', 4000, onHideCallback);
    }

    static showError(message, onHideCallback = null) {
        this.show(message, 'danger', 'exclamation-octagon', 5000, onHideCallback);
    }
}
