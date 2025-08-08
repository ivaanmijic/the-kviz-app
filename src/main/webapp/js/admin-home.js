import { QuizController } from './controller/quizController.js';
import { AdminController } from './adminController.js';

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
  const quizController = new QuizController(window.ctx);
  const adminController = new AdminController(window.ctx);

  const $content = $("#changeablePart");
  const $sidebar = $(".sidebar-list");
  const $items = $sidebar.find(".sidebar-item");

  function loadView(view) {
    $items.removeClass("active");
    $sidebar.find(`[data-view="${view}"]`).addClass("active");

    $content.empty();

    if (view === 'dashboard') {

      $content.html(`
      <div class="user-box">
    <div class="user-left">
        <sl-avatar label="username"></sl-avatar>
        <div class="user-labels">
            <span class="welcome">Welcome</span>
            <span class="title">${window.admin.username}</span>
        </div>
    </div>
    <sl-button class="yellow" size="large" onclick="document.getElementById('<%=editDialogId%>').show()">Profile</sl-button>
</div>
      `)

      quizController.getQuizzes(window.admin.id)
          .then(quizzes => {
            $content.append(`
            <section>
            <h2 class="section-title">My Quizzes</h2>
            `);
            $content.append(quizzes);
            $content.append('</section>');
          })
          .catch(err => {
            $content.append('<p class="warning">Failed to load quizzes content</p>');
          });
    } else if (view === 'admin-list') {
        adminController.getAdmins()
            .then(admins => {
                $content.append(`
                    <section>
                    <h2 class="section-title">Users</h2>
                `);
                $content.append(admins);
                $content.append('</section>');
            })
            .catch(err => {
                $content.append('<p class="warning">Failed to load users content</p>');
            });

    } else if (view === 'all-quizzes') {
      quizController.getQuizzes()
          .then(quizzes => {
            $content.append(`
                <section>
                <h2 class="section-title">All Quizzes</h2>
            `);
            $content.append(quizzes);
            $content.append('</section>');
          })
          .catch(err => {
            $content.append('<p class="warning">Failed to load quizzes content</p>');
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