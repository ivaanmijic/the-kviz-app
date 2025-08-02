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
            alert("Failed to delete admin: " + jqXHR.responseText);
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
        email: emailInput.value,
        username: usernameInput.value,
    };

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
        const emailChanged = emailInput.value !== initialValues.email;
        const usernameChanged = usernameInput.value !== initialValues.username;
        const newPasswordEntered = newPasswordInput.value.length > 0;
        const passwordsMatch = checkPasswordMatch();

        const hasValidChanges = emailChanged || usernameChanged || (newPasswordEntered && passwordsMatch);

        updateBtn.disabled = !(oldPasswordFilled && hasValidChanges);
    };

    [emailInput, usernameInput, oldPasswordInput, newPasswordInput, repPasswordInput].forEach(input => {
        input.addEventListener('sl-input', toggleUpdateButton);
    });

    updateBtn.addEventListener('click', async () => {

        const data = {
            username: usernameInput.value,
            email: emailInput.value,
            role: role,
            currentPassword: oldPasswordInput.value,
            newPassword: newPasswordInput.value,
        };

        try {
            const response = await fetch(`${contextPath}/admin/update?id=${adminId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                alert('Profile updated successfully! The page will now reload.');
                location.reload();
            } else {
                const errorResult = await response.json();
                throw new Error(errorResult.message || 'Update failed. Please check your credentials.');
            }
        } catch (error) {
            alert(`Error: ${error.message}`);
        }
    });
});
