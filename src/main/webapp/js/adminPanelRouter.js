import { QuizController } from './controller/quizController.js';
import { AdminController } from './controller/adminController.js';
import { AlertManager } from "./manager/alertManager.js";

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
    const quizController = new QuizController(window.ctx);
    const adminController = new AdminController(window.ctx);
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
                    quizController.getQuizzes(window.admin.id)
                        .then(quizzesGrid => {
                            $('#quiz-section').append(quizzesGrid);
                            return quizController.getPublicQuizzes();
                        })
                        .then(quizzesGrid => {
                            $('#quiz-section').append(quizzesGrid);
                        })
                        .catch(() => AlertManager.showError('Could not display quizzes.'));
                }).fail(() => AlertManager.showError('Could not display dashboard.'));
                break;

            case "admin-list":
                adminController.getAdmins()
                    .then(adminsView => $content.append(adminsView))
                    .catch(() => AlertManager.showError("Could not display user list."));
                break;

            case "all-quizzes":
                quizController.getQuizzes()
                    .then(allQuizzesView => $content.append(allQuizzesView))
                    .catch(() => AlertManager.showError("Could not display all Quizzes."));
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