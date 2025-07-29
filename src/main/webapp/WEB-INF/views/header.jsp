<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 7/25/25
  Time: 8:46 AM
  To change this template use File | Settings | File Templates.
--%>
<header>
    <div class="header-container">
        <div class="search-bar">
            <sl-input class="sl-input-color"  placeholder="Search" size="medium" pill filled>
                <sl-icon-button name="search" slot="suffix" ></sl-icon-button>
            </sl-input>
        </div>
        <div class="profile-buttons">
            <sl-avatar label="UserProfile"></sl-avatar>
            <sl-dropdown class="dropdwon">
                <sl-button slot="trigger" caret>Options</sl-button>
                <sl-menu class="custom-menu">
                    <sl-menu-item>
                        <sl-button variant="text" class="text-button">Edit</sl-button>
                    </sl-menu-item>
                    <sl-menu-item class="destructive">
                        <sl-button variant="text" class="destructive-text-button" href="${pageContext.request.contextPath}/signout">Sign Out</sl-button>
                    </sl-menu-item>
                </sl-menu>
            </sl-dropdown>
        </div>
    </div>
</header>