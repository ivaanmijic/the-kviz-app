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

    const $content = $("#changeablePart");
    const $sidebar = $(".sidebar-list");
    const $items = $sidebar.find(".sidebar-item");

    function loadView(view) {
        $items.removeClass("active");
        $sidebar.find(`[data-view="${view}"]`).addClass("active");

        $content.empty();

        switch (view) {
            case "dashboard":
                $.get(`${window.ctx}/templates/dashboard.html`, dashboardHtml => {
                    dashboardHtml = dashboardHtml.replace('{{Username}}', window.admin.username);
                    $content.append(dashboardHtml);

                    quizController.getQuizzes(window.admin.id)
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


            default:
                $.ajax({
                    url: `${window.ctx}/admin/view/${view}`,
                    dataType: "html",
                })
                    .done(data => $content.html(data))
                    .fail(() => AlertManager.showError("Failed to load content"));
        }
    }

    $items.on("click", function () {
        loadView($(this).data("view"));
    });

    const $init = $items.filter(".selected").first();
    loadView($init.length ? $init.data("view") : $items.first().data("view"));
});