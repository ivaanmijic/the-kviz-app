<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 8/19/25
  Time: 10:05 AM
  To change this template use File | Settings | File Templates.
--%>
    <div class="w-full">
        <div class="flex justify-between items-center mb-4">
            <span class="text-gray-500 font-semibold" id="numberOfPlayers"></span>
            <div class="flex items-center space-x-2">
                <div class="w-24 bg-gray-200 rounded-full h-2.5">
                    <sl-progress-bar id="progress" value="100"></sl-progress-bar>
                </div>
            </div>
        </div>
        <h2 class="text-center text-3xl font-bold text-gray-800 my-8" id="questionText"></h2>
    </div>
    <div class="w-full grid grid-cols-1 md:grid-cols-2 gap-4">
        <button class="sl-button !h-24 !text-xl bg-red-500 text-white hover:bg-red-600 answers"><sl-icon name="triangle-fill" slot="prefix"></sl-icon><span id="answ0"></span></button>
        <button class="sl-button !h-24 !text-xl bg-blue-500 text-white hover:bg-blue-600 answers"><sl-icon name="circle-fill" slot="prefix"></sl-icon><span id="answ1"></span></button>
        <button class="sl-button !h-24 !text-xl bg-yellow-500 text-white hover:bg-yellow-600 answers"><sl-icon name="square-fill" slot="prefix"></sl-icon><span id="answ2"></span></button>
        <button class="sl-button !h-24 !text-xl bg-green-500 text-white hover:bg-green-600 answers"><sl-icon name="diamond-fill" slot="prefix"></sl-icon><span id="answ3"></span></button>
    </div>
