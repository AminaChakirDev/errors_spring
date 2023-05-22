package com.livecoding.blog.controller;

import com.livecoding.blog.entity.Article;
import com.livecoding.blog.entity.Category;
import com.livecoding.blog.payload.response.MessageResponse;
import com.livecoding.blog.repository.ArticleRepository;
import com.livecoding.blog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RequestMapping("/api/articles")
@RestController
public class ArticleController {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("")
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @GetMapping("/latest")
    public List<Article> getLatestArticles() {
        return articleRepository.findFirst3ByOrderByCreatedAtDesc();
    }

    @GetMapping("/{id}")
    public Article getArticleById(@PathVariable Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        Article article = optionalArticle.get();
        return article;
    }

    @PostMapping("")
    public Article createArticle(@RequestParam(required = true) Long category, @RequestBody Article article) {
        Optional<Category> optionalCategoryToUse = categoryRepository.findById(category);
        Category categoryToUse = optionalCategoryToUse.get();
        article.setCategory(categoryToUse);
        Article createdArticle = articleRepository.save(article);
        return createdArticle;
    }

    @PutMapping("/{articleId}")
    public Article updateArticle(@PathVariable Long articleId, @RequestParam(required = false) Long category, @RequestBody Article body) {

            Optional<Article> optionalArticleToUpdate = articleRepository.findById(articleId);
            Optional<Category> optionalCategoryToUse = categoryRepository.findById(category);
            Article articleToUpdate = optionalArticleToUpdate.get();
            Category categoryToUse = optionalCategoryToUse.get();
            articleToUpdate.setTitle(body.getTitle());
            articleToUpdate.setContent(body.getContent());
            articleToUpdate.setSlug(body.getSlug());
            articleToUpdate.setCategory(categoryToUse);

            Article updatedArticle = articleRepository.save(articleToUpdate);

            return updatedArticle;
    }

    @DeleteMapping("/{id}")
    public boolean deleteArticle(@PathVariable Long id) {
        articleRepository.deleteById(id);
        return true;
    }
}
