class QuestionBlock {
    constructor(index, question = null) {
        this.question = question;
        this.index = index;
        this.element = this.createElement();
        this.bindEvents();
        if (question) this.fillData(question);
        return this.element;
    }

    createElement() {
        this.answerType = 1; //in case of the multiple correct answers this will be changed with action listener
        const wrapper = document.createElement("sl-details");
        wrapper.className = "question-wrapper";
        const initialTitle = this.question !== null ? this.question.question : "Question " + this.index;
        wrapper.innerHTML =
            '<span slot="summary"><span class="summary-title">' + initialTitle + '</span><sl-icon-button class="remove-question-btn" style="margin-left:10px;" name="trash"></sl-icon-button></span>' +
            '<div class="question-cont">' +
            this._questionHtml() +
            this._answersBoxHtml(this.answerType) +
            '</div>';

        return wrapper;
    }

    bindEvents() {
        this._addQuestionTitleInSummery();
        this._questionImageListeners();
        this._removeQuestionButton();
        this._disableSpaceButton();
        this._makeDraggable();
    }

    fillData(question) {
        const img = this.element.querySelector('.question-img-preview')
        img.src = "/uploads/questions/" + question.img;
        img.style.display = 'block';
        this.element.imageFile = null;
        this.element.questionId = question.id;
        this.element.querySelector('.remove-btn').style.display = 'block';
        this.element.querySelector('sl-icon').style.display = 'none';
        this.element.querySelector('.upload-box').style.border = 'none';
        this.element.querySelector('.question-points').value = question.points.toString();
        this.element.querySelector('.question-time').value = question.time.toString();
        console.log(question.category.toLowerCase());
        this.element.querySelector('.question-category').value = question.category.toLowerCase();
        this.element.querySelector('.question').value = question.question;
        const answers = question.answers;
        const answersSpan = this.element.querySelectorAll('.answer-text');
        let correctIdx;
        answers.forEach((ans, idx) => {
            if (ans === question.correctAnswer) {
                correctIdx = idx;
            }
            if (answersSpan[idx]) {
                answersSpan[idx].value = ans;
            }
        })

        if (correctIdx != null) {
            this.element.querySelector('.radio-group').value = correctIdx.toString();
        }
    }

//Create element additional functions
    _questionHtml() {
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
            '<sl-option value="classic">One correct answer</sl-option>' +
            '<sl-option value="multiple_correct">Multiple correct answers</sl-option>' +
            '<sl-option value="slider">Slider</sl-option>' +
            '</sl-select>' +
            '<sl-input class="question" type="text" placeholder="Question"></sl-input>';
    }

    _answersBoxHtml(answerType) {
        switch (answerType) {
            case 1: {
                return this._oneCorrectAnswer();
            }
            case 2: {
                return this._multipleCorrectAnswer();
            }
            case 3: {
                return this._sliderAnswer();
            }
        }
    }

    _oneCorrectAnswer() {
        return '<sl-radio-group class="radio-group" name="qcnt" value="1">' +
            '<div class="answers">' +
            '<sl-radio style="width: 100%" value="0"><sl-input class="answer-text" style="width: 100%" type="text" placeholder="Answer1"></sl-input></sl-radio>' +
            '<sl-radio style="width: 100%" value="1"><sl-input class="answer-text" style="width: 100%" type="text" placeholder="Answer2"></sl-input></sl-radio>' +
            '<sl-radio style="width: 100%" value="2"><sl-input class="answer-text" style="width: 100%" type="text" placeholder="Answer3"></sl-input></sl-radio>' +
            '<sl-radio style="width: 100%" value="3"><sl-input class="answer-text" style="width: 100%" type="text" placeholder="Answer4"></sl-input></sl-radio>' +
            '</div>' +
            '</sl-radio-group>';
    }

    _multipleCorrectAnswer() {
    }

    _sliderAnswer() {
    }

//Bind events additional functions
    _addQuestionTitleInSummery() {
        const qTitle = this.element.querySelector('.question');
        qTitle.addEventListener('input', e => {
            const dSummary = this.element.querySelector('.summary-title');
            dSummary.innerText = qTitle.value.trim() === '' ? `Question ${this.index}` : qTitle.value;
        })
    }

    _questionImageListeners() {
        const qBox = this.element.querySelector('.upload-box');
        const qInput = this.element.querySelector('input[type=file]');
        const qPreview = this.element.querySelector('.question-img-preview');
        const qRemove = this.element.querySelector('.remove-btn');
        const slIcon = this.element.querySelector('sl-icon');

        qBox.addEventListener('click', () => qInput.click());
        qInput.addEventListener('change', () => {
            const f = qInput.files[0];
            this.element.imageFile = f;
            if (f) {
                const r = new FileReader();
                r.onload = e => {
                    qPreview.src = e.target.result;
                    qPreview.style.display = 'block';
                };
                r.readAsDataURL(f);
                slIcon.style.display = 'none';
                this.element.style.display = 'block';
                qBox.style.border = 'none';
                qRemove.style.display = 'block';
            }
        });
        qRemove.addEventListener('click', e => {
            e.stopPropagation();
            qInput.value = '';
            qPreview.src = '';
            qPreview.style.display = 'none';
            qRemove.style.display = 'none';
            slIcon.style.display = 'block';
            this.element.imageFile = undefined;
            qBox.style.border = '1px dashed var(--sl-color-neutral-500)';
        });
    }

    _removeQuestionButton() {
        const questionsContainer = document.getElementById('questions-container');
        const removeQuestionBtn = this.element.querySelector('.remove-question-btn');
        removeQuestionBtn.addEventListener('click', e => {
            questionsContainer.removeChild(this.element);
        })
    }

    _disableSpaceButton() {
        this.element.querySelectorAll('.answer-text').forEach((el) => {
            el.addEventListener('keydown', e => {
                if (e.code === 'Space' || e.key === ' ' || e.which === 32) {
                    e.stopPropagation();
                }
            })
        })
    }

    _makeDraggable() {
        this.element.setAttribute("draggable", "true");

        let dragged;

        this.element.addEventListener('dragstart', e => {
            dragged = this.element;
            e.dataTransfer.effectAllowed = 'move';
            this.element.classList.add('dragging');
        });

        this.element.addEventListener('dragend', () => {
            this.element.classList.remove('dragging');
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
                    return {offset: offset, element: child};
                } else {
                    return closest;
                }
            }, {offset: Number.NEGATIVE_INFINITY, element: null}).element;
        }
    }

}

//Fill the fields additional functions

class QuizForm {
    constructor(id = null) {
        this.questionsContainer = document.getElementById('questions-container');
        console.log(this.questionsContainer)
        this.quizId = id;
        this.idx = 1;
        this.bindEvents();
    }

    bindEvents() {
        this._submitQuizEnterBehaviour();
        this._imageListener();
        this._toggleDetail();
        document.getElementById('add-question-button').addEventListener('click', () => {
            this.addQuestion()
        });
        document.getElementById("submitQuiz").addEventListener('click', (e) => {
            this._createQuiz(e)
        })
    }

    _submitQuizEnterBehaviour() {
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

    _imageListener() {
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
                this._imagePreviewLook(file, reader, quizPreview, quizRemove, slIcon, quizBox);
            }
        });
        quizRemove.addEventListener('click', e => {
            e.stopPropagation();
            this._inputImageLook(quizInput, quizPreview, quizRemove, slIcon, quizBox);
        });
    }

    _imagePreviewLook(file, reader, quizPreview, quizRemove, slIcon, quizBox) {
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

    _inputImageLook(quizInput, quizPreview, quizRemove, slIcon, quizBox) {
        quizInput.value = '';
        quizPreview.src = '';
        quizPreview.style.display = 'none';
        quizRemove.style.display = 'none';
        quizRemove.removeAttribute('data-imageSet');
        slIcon.style.display = 'block';
        quizBox.style.border = '1px dashed var(--sl-color-neutral-500)';
        window.quizImageFile = undefined;
    }

    addQuestion(question = null) {
        this.questionsContainer.appendChild(new QuestionBlock(this.idx, question));
        this.idx++;
    }

    _toggleDetail() {
        this.questionsContainer.addEventListener('sl-show', event => {
            if (event.target.localName === 'sl-details') {
                [...this.questionsContainer.querySelectorAll('sl-details')].map(details => (details.open = event.target === details));
            }
        });
    }

    _createQuiz(e) {
        if (this._validateQuiz() === false) {
            e.preventDefault();
        } else {
            e.preventDefault();
            const formData = new FormData();
            if (!!this.quizId) {
                formData.append('quizId', this.quizId);
                console.log(window.quizImageFile);
            }
            formData.append("quizTitle", document.getElementById('quizTitle').value.trim());
            console.log(document.getElementById("quizCategory").value.trim());
            formData.append("quizCategory", document.getElementById("quizCategory").value.trim());
            formData.append("quizDescription", document.getElementById("quizDescription").value.trim());
            formData.append("quizVisibility", document.getElementById("quizVisibility").value.trim());
            if (window.quizImageFile !== null) formData.append("quizImage", window.quizImageFile);

            const questionWrappers = document.querySelectorAll('.question-wrapper');
            questionWrappers.forEach((question, index) => {
                const title = question.querySelector('.question').value;
                const points = question.querySelector('.question-points').value;
                const time = question.querySelector('.question-time').value;
                const category = question.querySelector('.question-category').value;
                const correctAnswerIndex = question.querySelector('sl-radio-group').value;

                if (!!question.questionId) {
                    formData.append(`questions[${index}][id]`, question.questionId);
                }
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
                if (imgFile !== null) formData.append(`questions[${index}][image]`, imgFile);
            });
            console.log(window.ctx + "submit-quiz-creation");
            console.log(formData.get("quizCategory"));
            fetch(window.ctx + "submit-quiz-creation", {
                method: 'POST',
                body: formData,
            })
                .then(response => {
                    if (!response.ok) throw new Error("Request failed.");
                    return response.json();
                })
                .then(data => {
                    if (data.message === 'ok')
                        fetch(window.ctx + "create-quiz-window")
                            .then(response => response.text())
                            .then(html => {
                                document.getElementById('changeablePart').innerHTML = html;
                                let q = new QuizForm()
                                const alert = document.createElement('sl-alert');
                                alert.setAttribute('variant', "success");
                                alert.setAttribute('duration', "3000");
                                alert.setAttribute('closable', 'true');
                                alert.innerHTML =
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

    _validateQuiz() {
        const quizTitle = document.querySelector('#quizTitle').value.trim();
        const quizCategory = document.querySelector('#quizCategory').value.trim();
        const quizDescription = document.querySelector('#quizDescription').value.trim();
        const quizVisibility = document.querySelector('#quizVisibility').value.trim();
        const quizImagePresent = window.quizImageFile !== undefined;

        if (!quizTitle || !quizCategory || !quizDescription || !quizImagePresent || !quizVisibility) {
            const alert = document.createElement('sl-alert');
            alert.setAttribute('variant', "danger");
            alert.setAttribute('duration', "3000");
            alert.setAttribute('closable', 'true');
            alert.innerHTML =
                '<sl-icon slot="icon" name="exclamation-octagon"></sl-icon>' +
                '<strong>Your quiz submission has been canceled</strong><br />' +
                'Please fill all the quiz fields;';
            document.getElementsByTagName('body')[0].appendChild(alert);
            alert.toast();
            return false;
        }

        const questionWrappers = document.querySelectorAll('.question-wrapper');
        if (questionWrappers.length <= 0) {
            const alert = document.createElement('sl-alert');
            alert.setAttribute('variant', "danger");
            alert.setAttribute('duration', "3000");
            alert.setAttribute('closable', 'true');
            alert.innerHTML =
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
            const image = question.imageFile !== undefined;
            const answers = [...question.querySelectorAll('.answer-text')].map(el => el.value.trim());
            const selected = question.querySelector('sl-radio-group').value;

            if (!title || !time || !points || !category || !image || answers.some(a => !a) || selected === '') {
                const alert = document.createElement('sl-alert');
                alert.setAttribute('variant', "danger");
                alert.setAttribute('duration', "3000");
                alert.setAttribute('closable', 'true');
                alert.innerHTML =
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
}