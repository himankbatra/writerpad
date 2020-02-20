'use strict'

var demoApp = angular.module('writerpad', [ 'ui.bootstrap', 'writerpad.controllers',
		'writerpad.services']);
demoApp.constant("CONSTANTS", {
	getArticleByIdUrl : "/api/articles/",
	getAllArticles : "/api/articles",
	saveUser : "/api/users",
	saveArticle: "/api/articles"
});

