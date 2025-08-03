function createQuizWindow(){
fetch(window.ctx + "create-quiz-window", {
    method: "GET",
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
})
    .then(response => response.text())
    .then(html => {
        document.getElementById('changeablePart').innerHTML = html;
        scriptForCreateQuiz()
    })
    .catch(error => console.log(error));
}

var qCount = 0;

function scriptForCreateQuiz() {
    allowQuizSubmit();

//Quiz Image
    quizImageListeners();

//Questions
    const addQuestionBtn = document.getElementById('add-question-button');

    addQuestionBtn.addEventListener('click', addQuestion);

//opening and closing details
    openingAndClosingDetails();



    const submitButton = document.getElementById('submitQuiz');
    submitButton.addEventListener('click', submitQuizButton)
}

function allowQuizSubmit(){
    document.getElementById('createQuiz').addEventListener('keydown', (e) => {
        if (e.key === 'Enter' && e.target.tagName !== 'TEXTAREA') {
            e.preventDefault();
        }
    })
    const quizSubmit = document.getElementById('submitQuiz');
    const createQuizForm = document.getElementById('createQuiz');
    quizSubmit.addEventListener('submit', (e) => {
        createQuizForm.submit();
    });
}

function quizImageListeners(){
    const quizBox = document.getElementById('uploadBox');
    const quizInput = document.getElementById('imageInput');
    const quizPreview = document.getElementById('quizPreview');
    const quizRemove = document.getElementById('removeBtn');
    const slIcon = quizBox.querySelector('sl-icon');
    quizBox.addEventListener('click', () => quizInput.click());
    quizInput.addEventListener('change', () => {
        const file = quizInput.files[0];
        window.quizImageFile = file;
        if (file) {
            const reader = new FileReader();
            imagePreviewLook(file, reader, quizPreview, quizRemove, slIcon, quizBox);
        }
    });
    quizRemove.addEventListener('click', e => {
        e.stopPropagation();
        inputImageLook(quizInput, quizPreview, quizRemove, slIcon, quizBox);
    });
}

function imagePreviewLook(file, reader, quizPreview, quizRemove, slIcon, quizBox){
    reader.onload = e => {
        quizPreview.src = e.target.result;
        quizPreview.style.display = 'block';
        quizPreview.dataset.imageSet = 'true';
    };
    reader.readAsDataURL(file);
    quizRemove.style.display = 'block';
    slIcon.style.display = 'none';
    quizBox.style.border = 'none';
}

function inputImageLook(quizInput, quizPreview, quizRemove, slIcon, quizBox){
    quizInput.value = '';
    quizPreview.src = '';
    quizPreview.style.display = 'none';
    quizRemove.style.display = 'none';
    quizRemove.removeAttribute('data-imageSet');
    slIcon.style.display = 'block';
    quizBox.style.border= '1px dashed var(--sl-color-neutral-500)';
    window.quizImageFile = null;
}

function addQuestion() {
    let answerType = 1;
    const questionsContainer = document.getElementById('questions-container');
    const qcnt = ++qCount;
    const wrapper = document.createElement("sl-details");
    wrapper.className = "question-wrapper";
    wrapper.draggable = true;
    const initialTitle = "Question " + qcnt;
    wrapper.innerHTML =
        '<span slot="summary"><span class="summary-title">' + initialTitle + '</span><sl-icon-button class="remove-question-btn" style="margin-left:10px;" name="trash"></sl-icon-button></span>' +
        '<div class="question-cont">' +
        questionHtml() +
        answersBoxHtml(answerType) +
        '</div>';

    questionTitleInSummery(wrapper)

    questionImageListeners(wrapper);
    //Remove question
    removeQuestionButton(wrapper);

    wrapper.querySelectorAll('.answer-text').forEach((el) => {
        el.addEventListener('keydown', e => {
            if(e.code === 'Space' || e.key === ' ' || e.which === 32){
                e.stopPropagation();
            }
        })
    })
    //Drag-n-drop
    questionsContainer.appendChild(wrapper);
    wrapper.setAttribute("data-id", qcnt);
    makeDraggable(wrapper);
    wrapper.getData = qcnt;

    questionsContainer.appendChild(wrapper);

    function makeDraggable(wrapper) {
        wrapper.setAttribute("draggable", "true");

        let dragged;

        wrapper.addEventListener('dragstart', e => {
            dragged = wrapper;
            e.dataTransfer.effectAllowed = 'move';
            wrapper.classList.add('dragging');
        });

        wrapper.addEventListener('dragend', () => {
            wrapper.classList.remove('dragging');
        });

        const container = document.getElementById('questions-container');

        container.addEventListener('dragover', e => {
            e.preventDefault();

            const dragging = container.querySelector('.dragging');
            const afterElement = getClosestElement(container, e.clientY);

            if (!afterElement) {
                container.appendChild(dragging);
            } else if (afterElement !== dragging) {
                container.insertBefore(dragging, afterElement);
            }
        });

        function getClosestElement(container, y) {
            const elements = [...container.querySelectorAll('sl-details:not(.dragging)')];

            return elements.reduce((closest, child) => {
                const box = child.getBoundingClientRect();
                const offset = y - (box.top + box.height / 2);
                if (offset < 0 && offset > closest.offset) {
                    return { offset: offset, element: child };
                } else {
                    return closest;
                }
            }, { offset: Number.NEGATIVE_INFINITY, element: null }).element;
        }
    }


}



function oneCorrectAnswer(){
    return '<sl-radio-group class="radio-group" name="qcnt" value="1">' +
        '<div class="answers">' +
        '<sl-radio style="width: 100%" value="0"><sl-input class="answer-text" style="width: 100%" type="text" placeholder="Answer1"></sl-input></sl-radio>' +
        '<sl-radio style="width: 100%" value="1"><sl-input class="answer-text" style="width: 100%" type="text" placeholder="Answer2"></sl-input></sl-radio>' +
        '<sl-radio style="width: 100%" value="2"><sl-input class="answer-text" style="width: 100%" type="text" placeholder="Answer3"></sl-input></sl-radio>' +
        '<sl-radio style="width: 100%" value="3"><sl-input class="answer-text" style="width: 100%" type="text" placeholder="Answer4"></sl-input></sl-radio>' +
        '</div>' +
        '</sl-radio-group>';
}
function multipleCorrectAnswer(){}
function sliderAnswer(){}

function answersBoxHtml(i){
    switch(i){
        case 1: {
            return oneCorrectAnswer();
        }
        case 2: {
            return multipleCorrectAnswer();
        }
        case 3: {
            return sliderAnswer();
        }
    }
}

function questionHtml(){
    return '<label class="upload-box" id="uploadBox">' +
    '<img class="question-img-preview" style="display:none;">' +
    '<sl-icon-button name="x" type="button" class="remove-btn" style="display:none;"></sl-icon-button>' +
    '<sl-icon name="plus-circle"></sl-icon>' +
    '</label>' +
    '<input type="file" name="image" id="imageInput" accept="image/*"/>' +
    '<sl-input type="number" class="question-points" min=1 placeholder="Points"></sl-input>' +
    '<sl-select class="question-time" placeholder="Time">' +
    '<sl-option value="10">10s</sl-option>' +
    '<sl-option value="15">15s</sl-option>' +
    '<sl-option value="20">20s</sl-option>' +
    '<sl-option value="30">30s</sl-option>' +
    '</sl-select>' +
    '<sl-select class="question-category" placeholder="Type">' +
    '<sl-option value="one_correct">One correct answer</sl-option>' +
    '<sl-option value="more_correct">Multiple correct answers</sl-option>' +
    '<sl-option value="slider">Slider</sl-option>' +
    '</sl-select>' +
    '<sl-input class="question" type="text" placeholder="Question"></sl-input>';
}

function questionTitleInSummery(wrapper) {
    const qTitle = wrapper.querySelector('.question');
    qTitle.addEventListener('input', e => {
        const dSummary = wrapper.querySelector('.summary-title');
        dSummary.innerText = qTitle.value.trim() === '' ? `Question ${qcnt}` : qTitle.value;
    })
}

function questionImageListeners(wrapper){
    const qBox = wrapper.querySelector('.upload-box');
    const qInput = wrapper.querySelector('input[type=file]');
    const qPreview = wrapper.querySelector('.question-img-preview');
    const qRemove = wrapper.querySelector('.remove-btn');
    const slIcon = wrapper.querySelector('sl-icon');

    qBox.addEventListener('click', () => qInput.click());
    qInput.addEventListener('change', () => {
        const f = qInput.files[0];
        wrapper.imageFile = f;
        if (f) {
            const r = new FileReader();
            r.onload = e => {
                qPreview.src = e.target.result;
                qPreview.style.display = 'block';
                wrapper.dataset.imageSet = 'true';
            };
            r.readAsDataURL(f);
            slIcon.style.display = 'none';
            wrapper.style.display = 'block';
            qBox.style.border = 'none';
        } else {
            qPreview.removeAttribute('data-imageSet');
        }
    });
    qRemove.addEventListener('click', e => {
        e.stopPropagation();
        qInput.value = '';
        qPreview.src = '';
        qPreview.style.display = 'none';
        qRemove.style.display = 'none';
        slIcon.style.display = 'block';
        wrapper.removeAttribute('data-imageSet');
        wrapper.imageFile = null;
        qBox.style.border = '1px dashed var(--sl-color-neutral-500)';
    });
}

function removeQuestionButton(wrapper){
    const questionsContainer = document.getElementById('questions-container');
    const removeQuestionBtn = wrapper.querySelector('.remove-question-btn');
    removeQuestionBtn.addEventListener('click', e => {
        questionsContainer.removeChild(wrapper);
    })

}
function openingAndClosingDetails(){
    const container = document.getElementById('questions-container');

    container.addEventListener('sl-show', event => {
        if (event.target.localName === 'sl-details') {
            [...container.querySelectorAll('sl-details')].map(details => (details.open = event.target === details));
        }
    });
}

function validateQuiz() {
    const quizTitle = document.querySelector('#quizTitle').value.trim();
    const quizCategory = document.querySelector('#quizCategory').value.trim();
    const quizDescription = document.querySelector('#quizDescription').value.trim();
    const quizImagePresent = !!window.quizImageFile;

    if (!quizTitle || !quizCategory || !quizDescription || !quizImagePresent) {
        const alert = document.createElement('sl-alert');
        alert.setAttribute('variant', "danger");
        alert.setAttribute('duration', "3000");
        alert.setAttribute('closable', 'true');
        alert.innerHTML=
            '<sl-icon slot="icon" name="exclamation-octagon"></sl-icon>' +
            '<strong>Your quiz submission has been canceled</strong><br />' +
            'Please fill all the quiz fields;';
        document.getElementsByTagName('body')[0].appendChild(alert);
        alert.toast();
        return false;
    }

    const questionWrappers = document.querySelectorAll('.question-wrapper');
    if(questionWrappers.length <= 0){
        const alert = document.createElement('sl-alert');
        alert.setAttribute('variant', "danger");
        alert.setAttribute('duration', "3000");
        alert.setAttribute('closable', 'true');
        alert.innerHTML=
            '<sl-icon slot="icon" name="exclamation-octagon"></sl-icon>' +
            '<strong>Your quiz submission has been canceled</strong><br />' +
            'Please add at least one question;';
        document.getElementsByTagName('body')[0].appendChild(alert);
        alert.toast();
        return false;
    }
    alert.toast = function () {

    };
    for (const question of questionWrappers) {
        const title = question.querySelector('.question').value.trim();
        const points = question.querySelector('.question-points').value.trim();
        const time = question.querySelector('.question-time').value.trim();
        const category = question.querySelector('.question-category').value.trim();
        const image = question.dataset.imageSet === 'true';
        const answers = [...question.querySelectorAll('.answer-text')].map(el => el.value.trim());
        const selected = question.querySelector('sl-radio-group').value;

        if (!title || !time || !points || !category || !image || answers.some(a => !a) || selected === '') {
            const alert = document.createElement('sl-alert');
            alert.setAttribute('variant', "danger");
            alert.setAttribute('duration', "3000");
            alert.setAttribute('closable', 'true');
            alert.innerHTML=
                '<sl-icon slot="icon" name="exclamation-octagon"></sl-icon>' +
                '<strong>Your quiz submission has been canceled</strong><br />' +
                'Please fill all the question fienls;';
            document.getElementsByTagName('body')[0].appendChild(alert);
            alert.toast();
            return false;
        }
    }
    return true;
}

function submitQuizButton(e){
    console.log("Allahu ti si kadar")
    if (validateQuiz() === false) {
        e.preventDefault();
    } else {
        e.preventDefault();
        const formData = new FormData();
        formData.append("quizTitle", document.getElementById('quizTitle').value.trim());
        console.log(document.getElementById("quizCategory").value.trim());
        formData.append("quizCategory", document.getElementById("quizCategory").value.trim());
        formData.append("quizDescription", document.getElementById("quizDescription").value.trim());
        formData.append("quizImage", window.quizImageFile);

        const questionWrappers = document.querySelectorAll('.question-wrapper');
        questionWrappers.forEach((question, index) => {
            const title = question.querySelector('.question').value;
            const points = question.querySelector('.question-points').value;
            const time = question.querySelector('.question-time').value;
            const category = question.querySelector('.question-category').value;
            const correctAnswerIndex = question.querySelector('sl-radio-group').value;

            formData.append(`questions[${index}][title]`, title);
            formData.append(`questions[${index}][category]`, category);
            formData.append(`questions[${index}][points]`, points);
            formData.append(`questions[${index}][time]`, time);
            formData.append(`questions[${index}][correctAnswer]`, correctAnswerIndex);

            const answers = question.querySelectorAll('.answer-text');
            answers.forEach((ansInput, i) => {
                formData.append(`questions[${index}][answers][${i}]`, ansInput.value)
            })

            const imgFile = question.imageFile;
            formData.append(`questions[${index}][image]`, imgFile);
        });
        console.log(window.ctx + "submit-quiz-creation");
        console.log(formData.get("quizCategory"));
        fetch(window.ctx + "submit-quiz-creation", {
            method: 'POST',
            body: formData,
        })
            .then(response => {
                if(!response.ok) throw new Error("Request failed.");
                return response.json();
            })
            .then(data => {
                if(data.message === 'ok')
                    fetch(window.ctx + "create-quiz-window")
                        .then(response => response.text())
                        .then(html => {
                            document.getElementById('changeablePart').innerHTML = html;
                            scriptForCreateQuiz();
                            const alert = document.createElement('sl-alert');
                            alert.setAttribute('variant', "success");
                            alert.setAttribute('duration', "3000");
                            alert.setAttribute('closable', 'true');
                            alert.innerHTML=
                                '<sl-icon slot="icon" name="exclamation-octagon"></sl-icon>' +
                                '<strong>Your quiz has been created successfuly</strong><br />' +
                                'You can continue with createing more quizzes';
                            document.getElementsByTagName('body')[0].appendChild(alert);
                            alert.toast();
                        })
            })
            .catch(error => console.error("Error saving quiz: ", error));
    }
}