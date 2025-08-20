<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 8/19/25
  Time: 3:35 PM
  To change this template use File | Settings | File Templates.
--%>
<div class="w-full flex justify-center px-4">
    <div class="w-full max-w-2xl">
        <h2 class="text-4xl font-bold mb-6 mt-6 text-center">Results</h2>

        <sl-card class="p-4">
            <div id="leaderboardCont" class="space-y-3 overflow-y-auto" style="max-height:20rem;">
            </div>
        </sl-card>

        <div class="mt-4 flex justify-end">
            <sl-button id="downloadXLS" variant="primary" size="small" aria-label="Download results">
                <sl-icon name="download" slot="prefix"></sl-icon>
                Download results
            </sl-button>
        </div>
    </div>
</div>

<style>
    sl-card {
        display: block;
        width: 100%;
        box-sizing: border-box;
    }

    #leaderboardCont > .row {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: .5rem .5rem;
        border-radius: .375rem;
    }

    #leaderboardCont > .row + .row {
        margin-top: .375rem;
    }
</style>
