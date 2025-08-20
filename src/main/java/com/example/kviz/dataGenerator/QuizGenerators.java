package com.example.kviz.dataGenerator;

import com.example.kviz.model.Admin;
import com.example.kviz.model.Answer;
import com.example.kviz.model.Question;
import com.example.kviz.model.Quiz;
import com.example.kviz.model.supporting.QuestionTypeFactory;
import com.example.kviz.model.supporting.QuizCategoryFactory;
import com.example.kviz.service.AdminService;
import com.example.kviz.service.AnswerServices;
import com.example.kviz.service.QuestionServices;
import com.example.kviz.service.QuizService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizGenerators {
    private static final QuizService quizService = new QuizService();
    private static final QuestionServices questionServices = new QuestionServices();
    private static final AnswerServices answerServices = new AnswerServices();
    private static final AdminService adminService = new AdminService();

    private static final Admin admin = adminService.getAdminByUsername("admin").get();
    public static void main(String[] args) throws IOException {
        generateQuiz1();
        generateQuiz2();
    }
    private static void generateQuiz1() throws IOException {
        Quiz quiz = new Quiz();
        quiz.setOwner(admin);
        quiz.setId((long)-1);
        quiz.setTitle("JavaScript");
        quiz.setDescription("Ovaj kviz testira tvoje znanje iz JavaScript-a, jednog od najvažnijih jezika za razvoj web aplikacija. Pitanja pokrivaju osnovne pojmove, tipove podataka, rad sa nizovima, operatore, kao i specifične koncepte poput hoistinga i arrow funkcija.");
        quiz.setCategory(QuizCategoryFactory.getCategory("technology"));
        quiz.setVisible(true);
        quiz = quizService.save(quiz);
        ImageSave.save("quizImages", "imageQuiz1.jpg", "quizImages" + quiz.getId());
        quiz.setThumbnail("quizImages" + quiz.getId() + ".jpg");

        List<Question> questions = new ArrayList<>();
        List<QuestionString> lista = Quiz1GeneratorString.generateQuestions();
        for(int i = 0; i<10; i++){
            Question q =  new Question();
            q.setQuiz(quiz);
            q.setQuestion(lista.get(i).getText());
            q.setTime(10);
            q.setType(i<8? QuestionTypeFactory.getType("classic"): QuestionTypeFactory.getType("multiple_correct"));
            q.setPoints(i%2*10+30);
            q = questionServices.save(q);
            ImageSave.save("questions", "im1q1.jpg", "quiz" + quiz.getId() + "_question" + q.getId());
            q.setImage("quiz" + quiz.getId() + "_question" + q.getId() + ".jpg");
            List<Answer> answers = new ArrayList<>();
            for(int j = 0; j<4; j++){
                Answer a = new Answer();
                a.setQuestion(q);
                a.setAnswer(lista.get(i).getOptions().get(j));
                if(lista.get(i).getCorrectAnswers().contains(j)){
                    a.setCorrect(true);
                }else{
                    a.setCorrect(false);
                }
                a = answerServices.save(a);
                answers.add(a);
            }
            q.setAnswers(answers);
            q = questionServices.save(q);
            questions.add(q);
        }
        quiz.setQuestions(questions);
        quizService.save(quiz);

    }
    private static void generateQuiz2() throws IOException {
        Quiz quiz = new Quiz();
        quiz.setOwner(admin);
        quiz.setTitle("Java Servlets");
        quiz.setDescription("Ovaj kviz proverava tvoje znanje o osnovama Java Servlet tehnologije. Pitanja obuhvataju životni ciklus servleta, rad sa HttpServletRequest i HttpServletResponse, sesije, servlet mapiranja i HTTP status kodove. Kviz se sastoji od 10 pitanja – većina sa jednim tačnim odgovorom, dok 30% pitanja ima više tacnih odgovora. Pogodan je za pocetnike i one koji zele da provere svoje razumevanje Java Servleta na osnovnom i srednjem nivou.");
        quiz.setCategory(QuizCategoryFactory.getCategory("technology"));
        quiz.setVisible(true);
        quiz = quizService.save(quiz);
        ImageSave.save("quizImages", "imageQuiz2.jpg", "quizImages" + quiz.getId());
        quiz.setThumbnail("quizImages" + quiz.getId() + ".jpg");

        List<Question> questions = new ArrayList<>();
        List<QuestionString> lista = Quiz2GeneratorString.generateQuestions();
        for(int i = 0; i<10; i++){
            Question q =  new Question();
            q.setQuiz(quiz);
            q.setImage("quiz-1_question" + (i-11) + ".jpg");
            q.setQuestion(lista.get(i).getText());
            q.setTime(10);
            q.setType(i<8? QuestionTypeFactory.getType("classic"): QuestionTypeFactory.getType("multiple_correct"));
            q.setPoints(i%2*10+30);
            q = questionServices.save(q);
            ImageSave.save("questions", "im2q1.jpg", "quiz" + quiz.getId() + "_question" + q.getId());
            q.setImage("quiz" + quiz.getId() + "_question" + q.getId() + ".jpg");
            List<Answer> answers = new ArrayList<>();
            for(int j = 0; j<4; j++){
                Answer a = new Answer();
                a.setQuestion(q);
                a.setAnswer(lista.get(i).getOptions().get(j));
                if(lista.get(i).getCorrectAnswers().contains(j)){
                    a.setCorrect(true);
                }else{
                    a.setCorrect(false);
                }
                a = answerServices.save(a);
                answers.add(a);
            }
            q.setAnswers(answers);
            q = questionServices.save(q);
            questions.add(q);
        }
        quiz.setQuestions(questions);
        quizService.save(quiz);

    }
}

class QuestionString {
    private String text;
    private List<String> options;
    private List<Integer> correctAnswers; // indexes of correct answers

    public QuestionString(String text, List<String> options, List<Integer> correctAnswers) {
        this.text = text;
        this.options = options;
        this.correctAnswers = correctAnswers;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    @Override
    public String toString() {
        return "Q: " + text + "\nOptions: " + options + "\nCorrect: " + correctAnswers;
    }
}
class Quiz1GeneratorString {
    public static List<QuestionString> generateQuestions() {
        List<QuestionString> questions = new ArrayList<>();

        // 1
        questions.add(new QuestionString(
                "Koja je ispravna sintaksa za deklaraciju promenljive u ES6?",
                Arrays.asList("var name = \"Marko\";", "let name = \"Marko\";", "constant name = \"Marko\";", "int name = \"Marko\";"),
                List.of(1) // correct is index 1 ("let")
        ));

        // 2
        questions.add(new QuestionString(
                "Koji tip vrednosti vraća metoda Array.prototype.includes()?",
                Arrays.asList("Number", "Array", "Boolean", "String"),
                List.of(2) // Boolean
        ));

        // 3
        questions.add(new QuestionString(
                "Šta vraća izraz typeof null u JavaScript-u?",
                Arrays.asList("null", "object", "undefined", "boolean"),
                List.of(1) // object
        ));

        // 4
        questions.add(new QuestionString(
                "Koji od sledećih operatora se koristi za strogu jednakost (bez konverzije tipa)?",
                Arrays.asList("==", "=", "===", "!=="),
                List.of(2) // ===
        ));

        // 5
        questions.add(new QuestionString(
                "Koja metoda se koristi za dodavanje elementa na kraj niza?",
                Arrays.asList("push()", "add()", "append()", "insert()"),
                List.of(0) // push()
        ));

        // 6
        questions.add(new QuestionString(
                "Šta će ispisati sledeći kod: console.log(2 + \"2\");",
                Arrays.asList("22", "4", "NaN", "Grešku u kodu"),
                List.of(0) // "22"
        ));

        // 7
        questions.add(new QuestionString(
                "Šta znači 'hoisting' u JavaScript-u?",
                Arrays.asList(
                        "Podizanje HTML elemenata na vrh stranice",
                        "Automatsko pomeranje deklaracija funkcija i var promenljivih na vrh opsega",
                        "Automatsko kreiranje globalnih promenljivih",
                        "Dinamičko kreiranje DOM elemenata"
                ),
                List.of(1)
        ));

        // 8 (multiple correct)
        questions.add(new QuestionString(
                "Koji od sledećih tipova podataka postoje u JavaScript-u?",
                Arrays.asList("string", "number", "boolean", "character"),
                Arrays.asList(0, 1, 2) // string, number, boolean, undefined
        ));

        // 9 (multiple correct)
        questions.add(new QuestionString(
                "Koje metode mogu da menjaju originalni niz (mutiraju ga)?",
                Arrays.asList("map()", "filter()", "push()", "splice()"),
                Arrays.asList(2, 3) // push(), splice()
        ));

        // 10 (multiple correct)
        questions.add(new QuestionString(
                "Koje su karakteristike arrow funkcija u JavaScript-u?",
                Arrays.asList(
                        "Nemaju svoj this kontekst",
                        "Uvek vraćaju undefined",
                        "Ne mogu se koristiti kao konstruktori",
                        "Kraća sintaksa u odnosu na function deklaracije"
                ),
                Arrays.asList(0, 2, 3) // A, C, D
        ));

        return questions;
    }
}

class Quiz2GeneratorString {
    public static List<QuestionString> generateQuestions() {
        List<QuestionString> questions = new ArrayList<>();

        // 1
        questions.add(new QuestionString(
                "Koji paket sadrži osnovne Servlet klase i interfejse?",
                Arrays.asList("javax.web", "javax.servlet", "org.servlet", "java.http"),
                List.of(1)
        ));

        // 2
        questions.add(new QuestionString(
                "Koji metod u HttpServlet klasi obrađuje GET zahtev?",
                Arrays.asList("doPost()", "doGet()", "service()", "init()"),
                List.of(1)
        ));

        // 3
        questions.add(new QuestionString(
                "Šta metoda getWriter() iz HttpServletResponse vraća?",
                Arrays.asList("PrintWriter objekat", "OutputStream", "BufferedReader", "ResponseWriter"),
                List.of(0)
        ));

        // 4
        questions.add(new QuestionString(
                "Koja metoda u Servletu se poziva samo jednom pri inicijalizaciji?",
                Arrays.asList("service()", "init()", "doInit()", "start()"),
                List.of(1)
        ));

        // 5
        questions.add(new QuestionString(
                "Šta radi metoda sendRedirect() iz HttpServletResponse?",
                Arrays.asList(
                        "Prosleđuje zahtev drugom Servletu unutar iste aplikacije",
                        "Šalje klijentu odgovor sa novom lokacijom (URL)",
                        "Resetuje trenutnu HTTP sesiju",
                        "Zaustavlja izvršenje Servlet-a"
                ),
                List.of(1)
        ));

        // 6
        questions.add(new QuestionString(
                "Gde se definiše Servlet mapping u klasičnim Java EE aplikacijama?",
                Arrays.asList("web.xml", "server.xml", "context.xml", "pom.xml"),
                List.of(0)
        ));

        // 7
        questions.add(new QuestionString(
                "Koja metoda u HttpServlet obrađuje i GET i POST zahteve ako nisu override-ovane?",
                Arrays.asList("doGet()", "doPost()", "service()", "handle()"),
                List.of(2)
        ));

        // 8 (multiple correct)
        questions.add(new QuestionString(
                "Koje su tačne osobine HttpSession objekta?",
                Arrays.asList(
                        "Koristi se za čuvanje podataka o korisniku između zahteva",
                        "Može se obrisati metodom invalidate()",
                        "Traje zauvek dok server radi",
                        "Identifikuje se pomoću JSESSIONID kolačića"
                ),
                Arrays.asList(0, 1, 3)
        ));

        // 9 (multiple correct)
        questions.add(new QuestionString(
                "Koji su tačni HTTP status kodovi?",
                Arrays.asList("200 OK", "302 Found", "404 Not Found", "700 Server Error"),
                Arrays.asList(0, 1, 2)
        ));

        // 10 (multiple correct)
        questions.add(new QuestionString(
                "Koje metode ServletContext interfejsa postoje?",
                Arrays.asList(
                        "getInitParameter()",
                        "getRequestDispatcher()",
                        "log()",
                        "getSession()"
                ),
                Arrays.asList(0, 1, 2)
        ));

        return questions;
    }
}

class ImageSave {
    public static void save(String folder, String filename, String finalName) throws IOException {

        String uploads = Paths.get(System.getProperty("user.dir"), "uploads", folder).toString();
        String sourceStr = Paths.get(System.getProperty("user.dir"), "src/main/webapp/css/resources", filename).toString();
        Files.createDirectories(Paths.get(uploads));
        String filePath = uploads + "/" + finalName + ".jpg";

        File sourceFile = new File(sourceStr);
        File destFile = new File(filePath);

        try {
            // Read the image into memory
            BufferedImage image = ImageIO.read(sourceFile);

            // Save it to new location (format = "jpg" here, can be "png", etc.)
            ImageIO.write(image, "jpg", destFile);

            System.out.println("Image saved to: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}