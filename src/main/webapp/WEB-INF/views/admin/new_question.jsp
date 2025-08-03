<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 7/29/25
  Time: 7:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<sl-details id="questionAddDetail">
    <div class="question-cont">
        <label for="imageInput" class="uploadBox" id="uploadBox">
            <sl-icon name="plus-circle"></sl-icon>
        </label>
        <input type="file" name="image" id="imageInput" accept="image/*"/>
        <sl-input type="number" min=1 placeholder="Points"></sl-input>
        <sl-select placeholder="Time">
            <sl-option value="10">10s</sl-option>
            <sl-option value="15">15s</sl-option>
            <sl-option value="20">20s</sl-option>
            <sl-option value="30">30s</sl-option>
        </sl-select>
        <sl-select placeholder="Type">
            <sl-option value="one_correct">One correct answer</sl-option>
            <sl-option value="more_correct">Multiple correct answers</sl-option>
            <sl-option value="slider">Slider</sl-option>
        </sl-select>
        <sl-input class="question" type="text" placeholder="Question"></sl-input>
        <sl-textarea size="large" placeholder="Quiz Description"></sl-textarea>
    </div>
</sl-details>