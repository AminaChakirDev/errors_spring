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
    public ResponseEntity<?> getArticleById(@PathVariable Long id) {
        try {
            Optional<Article> optionalArticle = articleRepository.findById(id);
            if (optionalArticle.isPresent()) {
                Article article = optionalArticle.get();
                return ResponseEntity.ok(article);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cet article n'existe pas");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue. L'opération n'a pu aboutir");
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createArticle(@RequestParam(required = true) Long category, @RequestBody Article article) {
        try {
            Optional<Category> optionalCategoryToUse = categoryRepository.findById(category);
            if(optionalCategoryToUse.isPresent()) {
                Category categoryToUse = optionalCategoryToUse.get();
                article.setCategory(categoryToUse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cette catégorie n'existe pas");
            }
            Article createdArticle = articleRepository.save(article);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdArticle);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue. L'opération n'a pu aboutir");
        }
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<?> updateArticle(@PathVariable Long articleId, @RequestParam(required = false) Long category, @RequestBody Article body) {
        try {
            Optional<Article> optionalArticleToUpdate = articleRepository.findById(articleId);
            Optional<Category> optionalCategoryToUse = categoryRepository.findById(category);

            if (optionalArticleToUpdate.isPresent() && optionalCategoryToUse.isPresent()) {
                Article articleToUpdate = optionalArticleToUpdate.get();
                Category categoryToUse = optionalCategoryToUse.get();
                articleToUpdate.setTitle(body.getTitle());
                articleToUpdate.setContent(body.getContent());
                articleToUpdate.setSlug(body.getSlug());
                articleToUpdate.setCategory(categoryToUse);

                Article updatedArticle = articleRepository.save(articleToUpdate);

                return ResponseEntity.ok(updatedArticle);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cette catégorie et/ou cet article n'existe pas");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue. L'opération n'a pu aboutir");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long id) {
        try {
            if(articleRepository.findById(id).isPresent()) {
                articleRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cet article n'existe pas");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue. L'opération n'a pu aboutir");
        }
    }
}
