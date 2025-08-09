export class AlertManager {

    static show(message, variant = 'primary', icon = 'info-circle', duration = 4000, onHide = null) {
        const alert = document.createElement('sl-alert');
        alert.variant = variant;
        alert.closeable = true;
        alert.duration = duration;

        alert.classList.add('alert');

        alert.innerHTML = `
            <sl-icon slot="icon" name="${icon}"></sl-icon>
            ${message}
        `;
        document.body.appendChild(alert);

        setTimeout(() => alert.show(), 10);

        alert.addEventListener('sl-after-hide', () => {
            if (typeof onHide === 'function') {
                onHide();
            }
            alert.remove();
        });
    }

    static showInfo(message, onHideCallback = null) {
        this.show(message, 'primary', 'info-circle', 4000, onHideCallback);
    }

    static showSuccess(message, onHideCallback = null) {
        this.show(message, 'check2-circle', 'success-circle', 4000, onHideCallback);
    }

    static showError(message, onHideCallback = null) {
        this.show(message, 'danger', 'exclamation-octagon', 5000, onHideCallback);
    }

}