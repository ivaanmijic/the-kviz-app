import { QuizController } from './controller/quizController.js';

document.addEventListener("DOMContentLoaded", function () {
  const drawer = document.querySelector(".nav-drawer");
  const openButton = document.querySelector(".hamburger-menu");

  if (openButton && drawer) {
    openButton.addEventListener("click", () => {
      drawer.show();
    });
  }
});

document.addEventListener("DOMContentLoaded", () => {
  const navItems = document.querySelectorAll("aside li");

  navItems.forEach((item) => {
    item.addEventListener("click", () => {
      navItems.forEach((el) => el.classList.remove("selected"));
      item.classList.add("selected");
    });
  });
});

$(document).ready (() => {
  const quizController = new QuizController(window.ctx, window.admin.id);

  const $content = $("#changeablePart");
  const $sidebar = $(".sidebar-list");
  const $items = $sidebar.find(".sidebar-item");

  function loadView(view) {
    $items.removeClass("active");
    $sidebar.find(`[data-view="${view}"]`).addClass("active");

    $content.empty();

    if (view === 'quizzes') {
      quizController.getQuizzes()
          .then(quizzes => {
            $content.html(quizzes);
          })
          .catch(err => {
            $content.html('<p class="warning">Failed to load quizzes content</p>');
          });
    } else {
      $.ajax({
        url: `${window.ctx}/admin/view/${view}`,
        dataType: "html",
      })
          .done((data) => {
            $content.html(data);
          })
          .fail(() => {
            $content.html('<p class="warning">Failed to load content</p>');
          });
    }
  }

  $items.on("click", function () {
    const view = $(this).data("view");
    loadView(view);
  });

  const $init = $items.filter(".selected").first();
  loadView($init.length ? $init.data("view") : $items.first().data("view"));
});