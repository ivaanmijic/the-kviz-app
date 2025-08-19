import { AdminController } from './controller/adminController.js';
import { AlertManager } from "./manager/alertManager.js";
import { QuizController } from "./controller/quizController.js";

document.addEventListener("DOMContentLoaded", () => {
    const drawer = document.querySelector(".nav-drawer");
    const openButton = document.querySelector(".hamburger-menu");

    if (openButton && drawer) {
        openButton.addEventListener("click", () => drawer.show());
    }

    document.querySelectorAll("aside li").forEach(item => {
        item.addEventListener("click", () => {
            document.querySelectorAll("aside li").forEach(el => el.classList.remove("selected"));
            item.classList.add("selected");
        });
    });
});

$(document).ready(() => {
    const adminController = new AdminController(window.ctx);
    const quizController = new QuizController();
    AlertManager.init();

    const $content = $("#changeablePart");
    const $sidebar = $(".sidebar-list");
    const $items = $sidebar.find(".sidebar-item");

    const $drawer = $(".nav-drawer");
    const $menuButton = $('.hamburger-menu');

    if ($drawer.length && $menuButton.length) {
        $menuButton.on('click', () => $drawer[0].show());
    }

    window.loadView = function (view, param = {}) {

        const activeView = param.from || view;
        $items.removeClass("active");
        $sidebar.find(`[data-view="${activeView}"]`).addClass("active");

        $content.empty();

        switch (view) {
            case "dashboard":
                $.get(`${window.ctx}/templates/dashboard.html`, dashboardHtml => {
                    dashboardHtml = dashboardHtml.replace('{{Username}}', window.admin.username);
                    $content.append(dashboardHtml);

                    $.get(`${window.ctx}/admin/quiz/list`, { admin_id: window.admin.id }, html => {
                        $content.append(html);
                    }).then(() => {
                        return $.get(`${window.ctx}/admin/quiz/list/public`, html => {
                            $content.append(html);
                            quizController.setupListeners();
                        });
                    }).fail(
                        () => AlertManager.showError("Could not display quizzes.")
                    );
                }).fail(() => AlertManager.showError('Could not display dashboard.'));
                break;

            case "admin-list":
                adminController.getAdmins()
                    .then(adminsView => $content.append(adminsView))
                    .catch(() => AlertManager.showError("Could not display user list."));
                break;

            case "all-quizzes":
                $.get(`${window.ctx}/admin/quiz/list`, html => {
                    $content.append(html);
                    quizController.setupListeners();
                }).fail(() => AlertManager.showError("Could not display quizzes."));
                break;

            case "profile":
                const profileId = param.id || window.admin.id;

                $.get(`${window.ctx}/admins/${profileId}/profile`, profileHtml => {
                    $content.append(profileHtml);
                    adminController.setupProfileListeners(profileId);

                }).fail(() => AlertManager.showError("Could not display profile."));
                break;
        }
    }

    $items.on("click", function () {
        window.loadView($(this).data("view"));
    });

    const $init = $items.filter(".selected").first();
    window.loadView($init.length ? $init.data("view") : $items.first().data("view"));
});