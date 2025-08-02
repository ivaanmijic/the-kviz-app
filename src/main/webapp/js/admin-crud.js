function deleteAdmin(id, self = false) {
    $.post("/admin/delete", { id: id, self: String(self)} )
        .done(function (data) {
            console.log("User deleted successfully:", data);
            if (self) {
                location.reload();
            } else {
                const card = $('#' + id + 'Card');
                const drawer = $('#' + id + 'Drawer');
                const dialog = $('#' + id + 'Dialog');

                dialog.hide();

                card.fadeOut(200, function () {
                    card.remove();
                    drawer.remove();
                    dialog.remove();
                });
            }
        })
        .fail(function (jqXHR) {
            const alert = document.getElementById('errorAlert')
            alert.innerHTML = `
            <sl-icon slot="icon" name="exclamation-octagon"></sl-icon>
            <strong>${jqXHR.responseText}</strong><br/>
            Please try again.
            `;
            alert.show();
        });
}

document.addEventListener('DOMContentLoaded', () => {
    const editDialogId = `${adminId}EditDialog`;
    const dialog = document.getElementById(editDialogId);

    if (!dialog) return;

    const updateBtn = dialog.querySelector('#updateBtn');
    const emailInput = dialog.querySelector('sl-input[name="email"]');
    const usernameInput = dialog.querySelector('sl-input[name="username"]');
    const oldPasswordInput = dialog.querySelector('sl-input[name="old-password"]');
    const newPasswordInput = dialog.querySelector('sl-input[name="new-password"]');
    const repPasswordInput = dialog.querySelector('sl-input[name="rep-password"]');
    const pwMatchIndicator = dialog.querySelector('#pwMatchIndicator');

    const initialValues = {
        email: "",
        username: "",
    };

    dialog.addEventListener('sl-show', () => {
        initialValues.email = emailInput.value;
        initialValues.email = usernameInput.value;
        toggleUpdateButton()
    });

    const checkPasswordMatch = () => {
        const newPw = newPasswordInput.value;
        const repPw = repPasswordInput.value;

        if (newPw || repPw) {
            if (newPw === repPw && newPw.length > 0) {
                pwMatchIndicator.textContent = '✅';
                return true;
            } else {
                pwMatchIndicator.textContent = '❌';
                return false;
            }
        }
        pwMatchIndicator.textContent = '';
        return true;
    };

    const toggleUpdateButton = () => {
        const oldPasswordFilled = oldPasswordInput.value.length > 0;
        const emailChanged      = emailInput.value !== initialValues.email;
        const usernameChanged   = usernameInput.value !== initialValues.username;
        const newPasswordEntered = newPasswordInput.value.length > 0;
        const passwordsMatch    = checkPasswordMatch();
        const hasValidChanges   = emailChanged || usernameChanged || (newPasswordEntered && passwordsMatch);

        updateBtn.disabled = !(oldPasswordFilled && hasValidChanges);
    };

    [emailInput, usernameInput, oldPasswordInput, newPasswordInput, repPasswordInput]
        .forEach(input => input.addEventListener('sl-input', toggleUpdateButton));

    updateBtn.addEventListener('click', async () => {

        const admin = {
            username: usernameInput.value,
            email: emailInput.value,
            role: role,
            password: oldPasswordInput.value,
        }

        const data = {
            admin: admin,
            newPassword: newPasswordInput.value,
        };

        try {
            const response = await fetch(`${contextPath}/admin/update?id=${adminId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                const alert = document.getElementById('succesAlert');
                console.log(alert);
                alert.innerHTML = `
                    <sl-icon slot="icon" name="info-circle"></sl-icon>
                    <strong>Profile updated successfully!</strong>
                    The page will now reload.
                    `;
                alert.show();
                alert.addEventListener('sl-after-hide', () => {
                    location.reload();
                })
            } else {
                const errorResult = await response.json();
                throw new Error(errorResult.error || 'Update failed. Please check your credentials.');
            }
        } catch (error) {
            const alert = document.getElementById('errorAlert')
            alert.innerHTML = `
            <sl-icon slot="icon" name="exclamation-octagon"></sl-icon>
            <strong>${error.message}</strong><br/>
            Please try again.
            `;
            alert.show();
        }
    });
});