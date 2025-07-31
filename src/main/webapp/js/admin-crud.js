function deleteAdmin(id, self = false) {
    fetch(`/admin/delete`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({id: id, self: String(self)})
    })
        .then(reponse => {
            if (!reponse.ok) throw new Error("Failed to delete admin");
            console.error(reponse);
            return reponse.text();
        })
        .then(data => {
            console.log("User deleted successfully: ", data);

            if (self) {
                location.reload();
            } else {
                const drawerId = id + "Drawer";
                const dialogId = id + "Dialog";
                const cardId = id + "Card";

                document.getElementById(drawerId)?.hide()
                document.getElementById(dialogId)?.hide()
                document.getElementById(cardId)?.remove()
            }
        })
        .catch(error => {
            alert(error);
        })
}