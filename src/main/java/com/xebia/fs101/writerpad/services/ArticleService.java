package com.xebia.fs101.writerpad.services;


import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.ArticleStatus;
import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.exceptions.ArticleNotFoundException;
import com.xebia.fs101.writerpad.exceptions.ForbiddenOperationException;
import com.xebia.fs101.writerpad.repositories.ArticleRepository;
import com.xebia.fs101.writerpad.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.xebia.fs101.writerpad.utils.StringUtils.toUuid;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    public Article save(Article article, User user) {
        Optional<User> foundUser = this.userRepository.findById(user.getUserid());
        article.setUser(foundUser.get());
        return this.articleRepository.save(article);

    }

    public Article findById(String slugUuid) {
        UUID id = toUuid(slugUuid);
        return this.articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article id : " + id
                        + " does not exist !!! "));

    }

    public Article update(String slugId, Article copyFrom, User user) {
        Article article = this.findById(slugId);
        Optional<User> foundUser = this.userRepository.findById(user.getUserid());
        if (!Objects.equals(article.getUser(), foundUser.get())) {
            throw new ForbiddenOperationException("You are not allowed to "
                    + "perform this operation");
        }
        return this.articleRepository.save(article.update(copyFrom));

    }


    public List<Article> findAll(String status, Pageable pageable) {
        if (Objects.nonNull(status)) {
            return this.articleRepository.findAllByStatus(ArticleStatus.valueOf(status)
                    , pageable);
        }
        return this.articleRepository.findAll(pageable).getContent();
    }

    public void delete(String slugId, User user) {
        UUID id = toUuid(slugId);
        Article article = this.findById(slugId);
        Optional<User> foundUser = this.userRepository.findById(user.getUserid());
        if (!Objects.equals(article.getUser(), foundUser.get())) {
            throw new ForbiddenOperationException("You are not allowed to "
                    + "perform this operation");
        }
        this.articleRepository.deleteById(id);

    }


    public boolean publish(String slugId) {
        Article article = this.findById(slugId);
        if (ArticleStatus.PUBLISH != article.getStatus()) {
            this.articleRepository.save(article.publish());
            return true;
        }
        return false;

    }

    @Transactional(readOnly = true)
    public Map<String, Long> getTagsWithCount() {
        return this.articleRepository.findAllTags().collect(Collectors.groupingBy(tag -> tag
                , Collectors.counting()));

    }

    public void favourite(String slugId) {
        Article article = findById(slugId);
        article.favourite();
        this.articleRepository.save(article);
    }

    public void unFavourite(String slugId) {
        Article article = findById(slugId);
        if (article.getFavouritesCount() >= 0) {
            article.unFavourite();
            this.articleRepository.save(article);
        }
    }
}
