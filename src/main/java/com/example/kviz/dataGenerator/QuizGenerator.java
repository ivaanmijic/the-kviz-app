package com.example.kviz.dataGenerator;

import com.example.kviz.model.Admin;
import com.example.kviz.model.Quiz;
import com.example.kviz.service.AdminService;
import com.example.kviz.service.QuizService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class QuizGenerator {
    private static final List<String> descs= Arrays.asList(
        "Explore the fascinating landscapes, countries, and capitals of our planet! This quiz tests your knowledge of continents, major cities, and famous landmarks. From towering mountains to vast deserts, see how well you know the world map. Perfect for travelers and geography enthusiasts, this quiz will challenge your memory and broaden your horizons. Are you ready to prove you’re a true globetrotter?",
        "Step back in time and relive the moments that shaped human civilization. This quiz covers ancient empires, world wars, and revolutionary events that changed the course of history. Test your knowledge of influential leaders, iconic battles, and cultural milestones. Whether you’re a history buff or just curious, this quiz will take you on an exciting journey through the ages.",
        "Dive into the world of science and discover amazing facts about physics, chemistry, and biology! From the laws of motion to the mysteries of DNA, this quiz will challenge your curiosity and critical thinking. Whether you’re fascinated by space, atoms, or life on Earth, these questions are designed to spark your interest and test your knowledge of the natural world.",
        "Are you a pop culture guru? This quiz is packed with questions about movies, music, TV shows, and celebrities. From classic films to modern hits, you’ll find a mix of nostalgia and current trends. Perfect for entertainment lovers, this challenge will test how much you really know about the world of pop culture. Grab your friends and see who reigns supreme!",
        "Think you know sports? This quiz will put your knowledge to the ultimate test! From football and basketball to cricket and tennis, explore fun facts about teams, athletes, and legendary moments in sports history. Whether you’re a casual fan or a hardcore enthusiast, these questions will keep you on your toes. Ready to score big?",
        "Step into the digital age and see how much you know about technology and innovation! This quiz covers groundbreaking inventions, famous tech companies, and modern trends shaping our future. From early computers to AI and space tech, test your knowledge of the most transformative technologies in history. Are you a true tech-savvy mind?",
        "Calling all foodies! This quiz is all about cuisines from around the world, famous dishes, and cooking techniques. From exotic spices to traditional recipes, you’ll explore the rich diversity of flavors and cultures. Perfect for those who love to eat and cook, this quiz will leave your taste buds tingling and your brain craving more!",
        "Enter the world of gods, heroes, and mythical creatures! This quiz explores fascinating stories from Greek, Norse, Egyptian, and other mythologies. Test your knowledge of legendary figures, epic battles, and ancient tales that still captivate our imagination today. Whether you’re a fan of myths or fantasy, this quiz will awaken the storyteller in you."
    );
    private static final List<String> title = Arrays.asList(
        "World Geography Challenge",
            "History Through the Ages",
            "Ultimate Science Quiz",
            "Pop Culture Trivia",
            "Sports Fanatics Quiz",
            "Technology & Innovation Quiz",
            "Food & Culinary Delights Quiz",
            "Mythology & Legends Quiz"
    );
    private static final List<String> imgs = Arrays.asList(
            "geography.jpeg",
            "history.jpg",
            "science.jpeg",
            "pop.png",
            "sport.jpeg",
            "technology.jpg",
            "food.jpg",
            "mythology.jpg"
    );

    public static void main(String[] args) {
        AdminService adminService = new AdminService();
        Optional<Admin> owner = adminService.getAdminByUsername("superadmin");
        if (owner.isPresent()) {
            QuizService qs = new QuizService();
            for (int i = 0; i < 8; i++) {
                Quiz quiz = new Quiz();
                quiz.setTitle(title.get(i));
                quiz.setThumbnail(imgs.get(i));
                quiz.setDescription(descs.get(i));
                quiz.setOwner(owner.get());
                qs.save(quiz);
            }
        }
    }

}
