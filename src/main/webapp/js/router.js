$(function () {
    const $content = $("#content");
    const $sidebar = $(".sidebar-list");
    const $items = $sidebar.find(".sidebar-item");

    function loadView(view) {
        $items.removeClass("active");
        $sidebar.find(`[data-view="${view}"]`).addClass("active");

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

    $items.on("click", function () {
        const view = $(this).data("view");
        loadView(view);
    });

    const $init = $items.filter(".selected").first();
    loadView($init.length ? $init.data("view") : $items.first().data("view"));
});
