package com.livecoding.blog.controller;

import com.livecoding.blog.entity.Article;
import com.livecoding.blog.entity.Category;
import com.livecoding.blog.repository.ArticleRepository;
import com.livecoding.blog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
public class ArticleController {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("articles")
    public List<Article> getAllArticles(@RequestParam(required = false) Long categoryId) {
        return articleRepository.findAll();
    }

    @GetMapping("articles/latest")
    public List<Article> getLatestArticles() {
        return articleRepository.findFirst3ByOrderByCreatedAtDesc();
    }

    @GetMapping("articles/{id}")
    public Article getArticle(@PathVariable Long id) {
        return articleRepository.findById(id).get();
    }

    @PostMapping("articles")
    public Article createArticle(@RequestParam(required = false) Long category, @RequestBody Article article) {
        Category categoryToUse = categoryRepository.findById(category).get();
        article.setCategory(categoryToUse);
        return articleRepository.save(article);
    }

    @PutMapping("articles/{articleId}")
    public Article updateArticle(@PathVariable Long articleId, @RequestParam(required = false) Long category, @RequestBody Article body) {
        Article articleToUpdate = articleRepository.findById(articleId).get();
        Category categoryToUse = categoryRepository.findById(category).get();
        articleToUpdate.setTitle(body.getTitle());
        articleToUpdate.setContent(body.getContent());
        articleToUpdate.setSlug(body.getSlug());
        articleToUpdate.setCategory(categoryToUse);

        return articleRepository.save(articleToUpdate);
    }

    @DeleteMapping("articles/{id}")
    public boolean deleteArticle(@PathVariable Long id) {
        articleRepository.deleteById(id);
        return true;
    }
}
